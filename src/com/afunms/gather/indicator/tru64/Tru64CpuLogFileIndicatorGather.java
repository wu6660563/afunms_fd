/*
 * @(#)Tru64CpuLogFileIndicatorGather.java     v1.01, 2014 1 15
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.tru64;

import java.util.Calendar;
import java.util.Vector;

import com.afunms.gather.indicator.base.LogFileIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.snmp.tru64.Tru64LogFileKeywordConstant;

/**
 * ClassName:   Tru64CpuLogFileIndicatorGather.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 15 16:22:36
 */
public class Tru64CpuLogFileIndicatorGather extends LogFileIndicatorGather {

    /**
     * BEGIN_KEYWORD:
     * <p>开始字符串
     *
     * @since   v1.01
     */
    private static final String BEGIN_KEYWORD = Tru64LogFileKeywordConstant.TRU64_CPU_BEGIN_KEYWORD;

    /**
     * END_KEYWORD:
     * <p>结束字符串
     *
     * @since   v1.01
     */
    private static final String END_KEYWORD = Tru64LogFileKeywordConstant.TRU64_CPU_END_KEYWORD;

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
        Vector<CPUcollectdata> vector = new Vector<CPUcollectdata>();
        try {
            String cpuContent = getLogFileContent(beginStr, endStr);
            Calendar date = getCalendar();
            
            String[] cpu_LineArr = cpuContent.split("\n");
            boolean flag = false;
            for (int i = 2; i < cpu_LineArr.length; i++) {
                String[] tmpData = cpu_LineArr[i].trim().split("\\s++");
                if(i == 2 && tmpData[17] != null && "id".equals(tmpData[17])) {
                    flag = true;
                }
                if(i == 3 && tmpData[17] != null && flag == true) {
                    vector.add(createCPUcollectdata(date, String.valueOf(format(100 - Double.parseDouble(tmpData[17])))));
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return createSimpleIndicatorValue(vector);
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

