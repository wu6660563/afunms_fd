/*
 * @(#)LinuxCpuLogFileIndicatorGather.java     v1.01, 2014 1 14
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.linux;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.gather.indicator.base.LogFileIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.snmp.linux.LinuxLogFileKeywordConstant;

/**
 * ClassName:   LinuxCpuLogFileIndicatorGather.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 14 15:15:29
 */
public class LinuxCpuLogFileIndicatorGather extends LogFileIndicatorGather {

    /**
     * BEGIN_KEYWORD:
     * <p>开始字符串
     *
     * @since   v1.01
     */
    private static final String BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_CPU_BEGIN_KEYWORD;

    /**
     * END_KEYWORD:
     * <p>结束字符串
     *
     * @since   v1.01
     */
    private static final String END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_CPU_END_KEYWORD;

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
        String cpuContent = getLogFileContent(beginStr, endStr);
        Calendar date = getCalendar();

        String[] cpuLineArr = cpuContent.split("\n");

        List<Hashtable<String, String>> cpuperflist = new ArrayList<Hashtable<String, String>>();
        Hashtable<String, String> cpuperfhash = new Hashtable<String, String>();

        Vector<CPUcollectdata> cpuVector = new Vector<CPUcollectdata>();
        for (int i = 0; i < cpuLineArr.length; i++) {
            String[] tmpData = cpuLineArr[i].trim().split("\\s++");
            if (tmpData != null && tmpData.length >= 5) {
                if ("Average:".equalsIgnoreCase(tmpData[0].trim())
                        || "平均时间:".equalsIgnoreCase(tmpData[0].trim())) {
                    String user = tmpData[2].trim();
                    String sys = tmpData[4].trim();
                    String wio = tmpData[5].trim();
                    String idle = tmpData[7].trim();

                    String used = String.valueOf(format(100.0 - Double.parseDouble(idle)));

                    cpuperfhash.put("%usr", user);
                    cpuperfhash.put("%sys", sys);
                    cpuperfhash.put("%wio", wio);
                    cpuperfhash.put("%idle", wio);
                    cpuperflist.add(cpuperfhash);

                    cpuVector.addElement(createCPUcollectdata(date, used));

                }
            }
        }
        Hashtable<String, Object> cpuHashtable = new Hashtable<String, Object>();
        cpuHashtable.put("cpuperflist", cpuperflist);
        cpuHashtable.put("cpuVector", cpuVector);
        return createSimpleIndicatorValue(cpuHashtable);
    }

    /**
     * createCpuVector:
     * <p>创建 {@link Vector<CPUcollectdata>}
     *
     * @param   value
     *          - 值
     * @return
     *
     * @since   v1.01
     */
    public CPUcollectdata createCPUcollectdata(Calendar date, String thevalue) {
        CPUcollectdata data = new CPUcollectdata();
        data.setIpaddress(getIpAddress());
        data.setCollecttime(date);
        data.setCategory("CPU");
        data.setEntity("Utilization");
        data.setSubentity("Utilization");
        data.setRestype("dynamic");
        data.setUnit("%");
        data.setThevalue(thevalue);
        return data;
    }
}

