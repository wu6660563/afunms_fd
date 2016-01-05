/*
 * @(#)WindowsProcessSnmpIndicatorGather.java     v1.01, 2013 12 28
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.windows;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.gather.indicator.base.SnmpIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Processcollectdata;

/**
 * ClassName:   WindowsProcessSnmpIndicatorGather.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 28 18:13:23
 */
public class WindowsProcessSnmpIndicatorGather extends SnmpIndicatorGather {

    private final static String[] OIDS = new String[] {
        "1.3.6.1.2.1.25.4.2.1.1",
        "1.3.6.1.2.1.25.4.2.1.2",
        "1.3.6.1.2.1.25.4.2.1.5",
        "1.3.6.1.2.1.25.4.2.1.6",
        "1.3.6.1.2.1.25.4.2.1.7",
        "1.3.6.1.2.1.25.5.1.1.2",
        "1.3.6.1.2.1.25.5.1.1.1"};
    
    private final static String[] ALL_MEMEORY_OIDS = new String[] { "1.3.6.1.2.1.25.2.2" };

    public static Hashtable<String, String> HOST_hrSWRun_hrSWRunType = null;
    static {
        HOST_hrSWRun_hrSWRunType = new Hashtable<String, String>();
        HOST_hrSWRun_hrSWRunType.put("1", "未知");
        HOST_hrSWRun_hrSWRunType.put("2", "操作系统");
        HOST_hrSWRun_hrSWRunType.put("3", "设备驱动");
        HOST_hrSWRun_hrSWRunType.put("4", "应用程序");

    }

    public static Hashtable<String, String> HOST_hrSWRun_hrSWRunStatus = null;
    static {
        HOST_hrSWRun_hrSWRunStatus = new Hashtable<String, String>();
        HOST_hrSWRun_hrSWRunStatus.put("1", "正在运行");
        HOST_hrSWRun_hrSWRunStatus.put("2", "等待");
        HOST_hrSWRun_hrSWRunStatus.put("3", "运行等待结果");
        HOST_hrSWRun_hrSWRunStatus.put("4", "有问题");
    }

    /**
     * getSimpleIndicatorValue:
     *
     * @return
     *
     * @since   v1.01
     * @see com.afunms.gather.indicator.base.BaseIndicatorGather#getSimpleIndicatorValue()
     */
    @Override
    public SimpleIndicatorValue getSimpleIndicatorValue() {
        // 获取内存总大小
        String[][] valueArray1 = getTableValuesByOids(ALL_MEMEORY_OIDS);
        Double allMemorySize = 0D;
        if (valueArray1 != null) {
            for (int i = 0; i < valueArray1.length; i++) {
                String svb0 = valueArray1[i][0];
                if (svb0 == null) {
                    continue;
                }
                allMemorySize = Double.parseDouble(svb0);
            }
        }

        String[][] valueArray = getTableValuesByOids(OIDS);

        // 先计算出所有总时间大小
        Double alltime = 0D;
        if (valueArray != null) {
            for (int i = 0; i < valueArray.length; i++) {
                String processCpu = valueArray[i][6].trim();
                alltime = alltime + Double.parseDouble(processCpu);
            }
        }

        Vector<Processcollectdata> processVector = new Vector<Processcollectdata>();
        Hashtable<String, Processcollectdata> processNumberHashtable = new Hashtable<String, Processcollectdata>();
        if (valueArray != null) {
            Calendar date = getCalendar();
            for (int i = 0; i < valueArray.length; i++) {
                String processIndex = valueArray[i][0];
                String processName = valueArray[i][1];
                String processPath = valueArray[i][2];
                String processType = valueArray[i][3];
                String processStatus = valueArray[i][4];
                String processMemory = valueArray[i][5];
                String processCpu = valueArray[i][6];
                
                Double memoryUtilization = 0D;
                if (allMemorySize > 0) {
                    memoryUtilization = Double.parseDouble(processMemory) * 100.0f / allMemorySize;
                }

                String processCpuStr = String.valueOf(format(Double.parseDouble(processCpu) / alltime * 100));
                String memoryUtilizationStr = String.valueOf(format(memoryUtilization));
                
                Processcollectdata processdata = processNumberHashtable.get(processName);
                if (processdata != null) {
                    Integer number = Integer.parseInt(processdata
                            .getThevalue());
                    number += 1;
                    processdata.setThevalue(String.valueOf(number));
                    processNumberHashtable.put(processName, processdata);
                } else {
                    processNumberHashtable.put(processName, createProcesscollectdata(date, "Num", processIndex, "1", "个", processName));
                }
                
                processType = HOST_hrSWRun_hrSWRunType.get(processType) != null ? HOST_hrSWRun_hrSWRunType.get(processType) : "未知";
                processStatus = HOST_hrSWRun_hrSWRunStatus.get(processStatus) != null ? HOST_hrSWRun_hrSWRunStatus.get(processStatus) : "未知";
                processVector.add(createProcesscollectdata(date, "Name", processIndex, processName, "", processName));
                processVector.add(createProcesscollectdata(date, "Path", processIndex, processPath, "", processName));
                processVector.add(createProcesscollectdata(date, "Type", processIndex, processType, "", processName));
                processVector.add(createProcesscollectdata(date, "Status", processIndex, processStatus, "", processName));
                processVector.add(createProcesscollectdata(date, "Memory", processIndex, processMemory, "K", processName));
                processVector.add(createProcesscollectdata(date, "MemoryUtilization", processIndex, memoryUtilizationStr, "%", processName));
                processVector.add(createProcesscollectdata(date, "CpuTime", processIndex, processCpuStr, "秒", processName));
            }
        }
        Hashtable<String, Object> processHashtable = new Hashtable<String, Object>();
        processHashtable.put("process", processVector);
        processHashtable.put("processNum", processNumberHashtable);
        return createSimpleIndicatorValue(processHashtable);
    }

    public Processcollectdata createProcesscollectdata(Calendar date, String entity, String subentity, String thevalue, String unit, String chname) {
        Processcollectdata processcollectdata = new Processcollectdata();
        processcollectdata.setIpaddress(getIpAddress());
        processcollectdata.setCollecttime(date);
        processcollectdata.setCategory("Process");
        processcollectdata.setEntity(entity);
        processcollectdata.setSubentity(subentity);
        processcollectdata.setRestype("dynamic");
        processcollectdata.setThevalue(thevalue);
        processcollectdata.setUnit(unit);
        processcollectdata.setChname(chname);
        return processcollectdata;
    }
}

