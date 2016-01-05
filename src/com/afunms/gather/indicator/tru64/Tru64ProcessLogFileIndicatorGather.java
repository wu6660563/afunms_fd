/*
 * @(#)Tru64ProcessLogFileIndicatorGather.java     v1.01, 2014 1 15
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.tru64;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.SysLogger;
import com.afunms.gather.indicator.base.IndicatorValueFactory;
import com.afunms.gather.indicator.base.LogFileIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Processcollectdata;
import com.afunms.polling.snmp.tru64.Tru64LogFileKeywordConstant;

/**
 * ClassName:   Tru64ProcessLogFileIndicatorGather.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 15 22:59:55
 */
public class Tru64ProcessLogFileIndicatorGather extends LogFileIndicatorGather {

    /**
     * logger:
     * <p>日志
     *
     * @since   v1.01
     */
    private final static SysLogger logger = SysLogger.getLogger(Tru64ProcessLogFileIndicatorGather.class);


    /**
     * BEGIN_KEYWORD:
     * <p>开始字符串
     *
     * @since   v1.01
     */
    private static final String BEGIN_KEYWORD = Tru64LogFileKeywordConstant.TRU64_PROC_BEGIN_KEYWORD;

    /**
     * END_KEYWORD:
     * <p>结束字符串
     *
     * @since   v1.01
     */
    private static final String END_KEYWORD = Tru64LogFileKeywordConstant.TRU64_PROC_END_KEYWORD;
    /**
     * getSimpleIndicatorValue:
     *
     * @return
     *
     * @since   v1.01
     * @see com.afunms.gather.indicator.base.BaseIndicatorGather#getSimpleIndicatorValue()
     */
    @SuppressWarnings("static-access")
    @Override
    public SimpleIndicatorValue getSimpleIndicatorValue() {
        String beginStr = BEGIN_KEYWORD;
        String endStr = END_KEYWORD;

        Vector<Processcollectdata> processVector = new Vector<Processcollectdata>();
        Hashtable<String, Processcollectdata> processNumberHashtable = new Hashtable<String, Processcollectdata>();
        try {
            Calendar date = getCalendar();
            Double cpuUtilization = 0D;
            String procContent = getLogFileContent(beginStr, endStr);
            String[] processLineArr = procContent.split("\n");
            int startedStart = processLineArr[0].indexOf("STARTED");
            //int timeEnd = processLineArr[0].indexOf("TIME");
            int cmdStart = processLineArr[0].indexOf("COMMAND");
            for (int i = 1; i < processLineArr.length; i++) {
                int cmdStart_ = processLineArr[i].indexOf(' ', cmdStart - 1);

                String tempStr1 = processLineArr[i].substring(0, cmdStart_)
                        .trim(); // 去掉COMMAND
                String cmd = processLineArr[i].substring(cmdStart_).trim();
                String tempStr2 = tempStr1.substring(0,
                        tempStr1.lastIndexOf(" ")).trim(); // 去掉TIME
                String time = tempStr1.substring(tempStr1.lastIndexOf(" ")).trim();
                String tempStr3 = tempStr2.substring(0, startedStart).trim(); // 去掉STARTED
                String started = tempStr2.substring(startedStart).trim();

                // 去掉STARTED之后，可以直接分割
                String[] tempstrs = tempStr3.split("\\s++");
                //String tty = tempstrs[6];
                String rss = tempstrs[5];
                //String vsz = tempstrs[4];
                String mem = tempstrs[3];
                String cpu = tempstrs[2];
                String pid = tempstrs[1];
                String uid = tempstrs[0];

                String type = "应用程序";
                String STAT = "正在运行";

                if (cpu != null) {
                    cpuUtilization += Double.parseDouble(cpu);
                }
                
                String processIndex = pid;
                String memoryUtilization = mem;
                String memory = String.valueOf(formatMemory(rss));
                String cpuTime = formatCpuTime(time);
                processVector.add(createProcesscollectdata(date, "Name", processIndex, cmd, "", processIndex));
                processVector.add(createProcesscollectdata(date, "Path", processIndex, cmd, "", processIndex));
                processVector.add(createProcesscollectdata(date, "Type", processIndex, type, "", processIndex));
                processVector.add(createProcesscollectdata(date, "Status", processIndex, STAT, "", processIndex));
                processVector.add(createProcesscollectdata(date, "Memory", processIndex, memory, "M", processIndex));
                processVector.add(createProcesscollectdata(date, "MemoryUtilization", processIndex, memoryUtilization, "%", processIndex));
                processVector.add(createProcesscollectdata(date, "StartTime", processIndex, started, "", processIndex));
                processVector.add(createProcesscollectdata(date, "USER", processIndex, uid, "", ""));
                processVector.add(createProcesscollectdata(date, "CpuTime", processIndex, cpuTime, "秒", ""));

                // 添加关键进程
                Processcollectdata processdata = processNumberHashtable.get(cmd);
                if (processdata != null) {
                    Integer number = Integer.parseInt(processdata
                            .getThevalue());
                    number += 1;
                    processdata.setThevalue(String.valueOf(number));
                    processNumberHashtable.put(cmd, processdata);
                } else {
                    processNumberHashtable.put(cmd, createProcesscollectdata(date, "Num", processIndex, "1", "个", ""));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Tru64 设备 " + getIpAddress() + " 采集 key 为：" + getIndicatorInfo().getGatherIndicators().getName() + " 出错", e);
        }
        Hashtable<String, Object> processHashtable = new Hashtable<String, Object>();
        processHashtable.put("process", processVector);
        processHashtable.put("processNum", processNumberHashtable);
        return createSimpleIndicatorValue(processHashtable);
    }

    public Processcollectdata createProcesscollectdata(Calendar date, String entity, String subentity, String thevalue, String unit, String chname) {
        return IndicatorValueFactory.createProcesscollectdata(getIpAddress(), date, entity, subentity, thevalue, unit, chname);
    }

    /**
     * formatMemory:
     * <p>格式化内存大小
     *
     * @param   memSize
     *          - 内存大小
     * @return  {@link Double}
     *          - 返回格式化后的数据
     *
     * @since   v1.01
     */
    private Double formatMemory(String memSize) {
        Double tempSize = 0D;
        if (memSize.endsWith("G")) {
            tempSize = Double
                    .valueOf(memSize.substring(0, memSize.indexOf("G"))) * 1024;
        } else if (memSize.endsWith("M")) {
            tempSize = Double
                    .valueOf(memSize.substring(0, memSize.indexOf("M")));
        } else if (memSize.endsWith("K")) {
            tempSize = Double
                    .valueOf(memSize.substring(0, memSize.indexOf("K")));
            if ("0".equals(tempSize)) {
                tempSize = 0D;
            } else {
                tempSize = tempSize / 1024;
            }
        }
        return format(tempSize);
    }

    /**
     * formatMemory:
     * <p>格式化内存大小
     *
     * @param   memSize
     *          - 内存大小
     * @return  {@link Double}
     *          - 返回格式化后的数据
     *
     * @since   v1.01
     */
    @SuppressWarnings("static-access")
    private String formatCpuTime(String time) {
        // 处理时间
        String cpuTime = "0";
        try {
            String[] timeStrs = time.split("-"); // 1-13:19:30
            Double minute = 0D;
            Double second = 0D;
            Double hour = 0D;
            if (timeStrs.length == 2) { // 1-13:19:30
                String[] temps = timeStrs[1].split(":");
                hour = Double.valueOf(timeStrs[0]) * 24 + Double.valueOf(temps[0]);
                minute = Double.valueOf(temps[1]);
                second = Double.valueOf(temps[2]);
                cpuTime = hour.intValue() + ":" + minute.intValue() + ":" + second.intValue();
            } else { // 01:31:28 0:00.29
                cpuTime = time;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Tru64 设备 " + getIpAddress() + " 解析时间：" + time + " 出错", e);
        }
        return cpuTime;
    }
}

