/*
 * @(#)LinuxProcessLogFileIndicatorGather.java     v1.01, 2014 1 14
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.linux;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.gather.indicator.base.LogFileIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Processcollectdata;
import com.afunms.polling.snmp.linux.LinuxLogFileKeywordConstant;

/**
 * ClassName:   LinuxProcessLogFileIndicatorGather.java
 * <p>
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 14 16:57:58
 */
public class LinuxProcessLogFileIndicatorGather extends LogFileIndicatorGather {

    /**
     * BEGIN_KEYWORD:
     * <p>��ʼ�ַ���
     *
     * @since   v1.01
     */
    private static final String BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_PROCESS_BEGIN_KEYWORD;

    /**
     * END_KEYWORD:
     * <p>�����ַ���
     *
     * @since   v1.01
     */
    private static final String END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_PROCESS_END_KEYWORD;

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
        String beginStr = BEGIN_KEYWORD;
        String endStr = END_KEYWORD;
        String processContent = getLogFileContent(beginStr, endStr);
        Calendar date = getCalendar();

        String[] processLineArr = processContent.split("\n");
        Vector<Processcollectdata> processVector = new Vector<Processcollectdata>();
        Hashtable<String, Processcollectdata> processNumberHashtable = new Hashtable<String, Processcollectdata>();
        try {
            int commond_start = processLineArr[0].trim().indexOf("COMMAND");
            for (int i = 1; i < processLineArr.length; i++) {
                String tmpCommondStr = processLineArr[i].substring(commond_start);
                if(' ' != processLineArr[i].charAt(commond_start-1)){
                    //�պ�Commond����
                    tmpCommondStr = processLineArr[i].substring(commond_start + tmpCommondStr.indexOf(' ')).trim();
                }
                if (tmpCommondStr == null || tmpCommondStr.trim().length() == 0) {
                    continue;
                }
                
                String[] tmpData = processLineArr[i].trim().substring(0, commond_start + tmpCommondStr.indexOf(' ')).split("\\s++");
                if ((tmpData != null)) {
                    if ("USER".equalsIgnoreCase(tmpData[0])) {
                        // ������
                        continue;
                    }
                    String USER = tmpData[0]; // USER
                    String PID = tmpData[1]; // PID
                    String CPU = tmpData[2]; // %CPU
                    String MEM = tmpData[3]; // %MEM
                    String VSZ = tmpData[4]; // VSZ
                    //String RSS = tmpData[5]; // RSS
                    //String TTY = tmpData[6]; // TTY
                    String STAT = tmpData[7]; // STAT
                    String START = tmpData[8]; // START
                    String TIME = tmpData[9]; // TIME
                    String COMMAND = tmpCommondStr; // COMMAND
//              System.out.println(USER+"==="+PID+"===="+CPU+"===="+MEM+"===="+VSZ+"===="+TTY+"===="+STAT+"===="+START+"===="+TIME+"===="+COMMAND);

                    // D �����ж� uninterruptible sleep (usually IO)
                    // R ���� runnable (on run queue)
                    // S �ж� sleeping
                    // T ֹͣ traced or stopped
                    // Z ���� a defunct (��zombie��) process
                    if ("Z".equalsIgnoreCase(STAT)) {
                        STAT = "�����ж�";
                    } else if ("R".equalsIgnoreCase(STAT)) {
                        STAT = "��������";
                    } else if ("S".equalsIgnoreCase(STAT)) {
                        STAT = "�����ж�";
                    } else if ("T".equalsIgnoreCase(STAT)) {
                        STAT = "����ֹͣ";
                    } else if ("Z".equalsIgnoreCase(STAT)) {
                        STAT = "��������";
                    } else {
                        STAT = "��������";
                    }

                    String processIndex = PID;
                    processVector.add(createProcesscollectdata(date, "process_id", processIndex, processIndex, "", ""));

                    processVector.add(createProcesscollectdata(date, "USER", processIndex, USER, "", ""));

                    processVector.add(createProcesscollectdata(date, "MemoryUtilization", processIndex, MEM, "%", ""));

                    processVector.add(createProcesscollectdata(date, "Memory", processIndex, VSZ, "K", ""));

                    processVector.add(createProcesscollectdata(date, "Type", processIndex, "Ӧ�ó���", "", ""));

                    processVector.add(createProcesscollectdata(date, "Status", processIndex, STAT, "", ""));

                    processVector.add(createProcesscollectdata(date, "Name", processIndex, COMMAND, "", ""));

                    processVector.add(createProcesscollectdata(date, "CpuTime", processIndex, TIME, "��", ""));

                    processVector.add(createProcesscollectdata(date, "StartTime", processIndex, START, "", ""));

                    processVector.add(createProcesscollectdata(date, "CpuUtilization", processIndex, CPU, "%", ""));

                    Processcollectdata processdata = processNumberHashtable.get(COMMAND);
                    if (processdata != null) {
                        Integer number = Integer.parseInt(processdata
                                .getThevalue());
                        number += 1;
                        processdata.setThevalue(String.valueOf(number));
                        processNumberHashtable.put(COMMAND, processdata);
                    } else {
                        processNumberHashtable.put(COMMAND, createProcesscollectdata(date, "Num", processIndex, "1", "��", ""));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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

