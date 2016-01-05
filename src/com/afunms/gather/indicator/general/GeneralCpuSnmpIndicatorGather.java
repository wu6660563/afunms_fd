/*
 * @(#)CpuSnmpIndicatorGather.java     v1.01, 2013 12 27
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.general;

import java.util.List;
import java.util.Vector;

import com.afunms.gather.indicator.base.SnmpIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.CPUcollectdata;

/**
 * ClassName:   CpuSnmpIndicatorGather.java
 * <p>{@link GeneralCpuSnmpIndicatorGather} 通用的 CPU 指标使用 SNMP 方式的指标采集类，
 * 具体子类可通过 {@link #getSimpleIndicatorValue(String[])} 方法传入不同的 OID 来获取 {@link SimpleIndicatorValue}
 * 也可以重写该方法
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 27 01:22:16
 */
public abstract class GeneralCpuSnmpIndicatorGather extends SnmpIndicatorGather {

    /**
     * getSimpleIndicatorValue:
     * <p>通过 oids 来获取 {@link SimpleIndicatorValue}
     *
     * @param   oids
     *          - {@link String[]} oids
     * @return
     *
     * @since   v1.01
     */
    public SimpleIndicatorValue getSimpleIndicatorValue(List<String[]> oidsList) {
        String[][] valueArray = getTableValuesByOids(oidsList);
        return createSimpleIndicatorValue(createCpuVector(valueArray));
    }

    /**
     * getSimpleIndicatorValue:
     * <p>通过 oids 来获取 {@link SimpleIndicatorValue}
     *
     * @param   oids
     *          - {@link String[]} oids
     * @return
     *
     * @since   v1.01
     */
    public SimpleIndicatorValue getSimpleIndicatorValue(String[] oids) {
        String[][] valueArray = getTableValuesByOids(oids);
        return createSimpleIndicatorValue(createCpuVector(valueArray));
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
    public Vector<CPUcollectdata> createCpuVector(String[][] valueArray) {
        Double value = 0D;
        if (valueArray != null && valueArray.length > 0) {
            int length = valueArray.length;
            for (int i = 0; i < valueArray.length; i++) {
                String _value = valueArray[i][0];
                try {
                    if (_value != null) {
                        _value = _value.replaceAll("%", "");
                        value += Double.valueOf(_value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            value = value / length;
        }

        Vector<CPUcollectdata> cpuVector = new Vector<CPUcollectdata>();
        CPUcollectdata cpudata = new CPUcollectdata();
        cpudata.setIpaddress(getIndicatorInfo().getNodeDTO().getIpaddress());
        cpudata.setCollecttime(getCalendar());
        cpudata.setCategory("CPU");
        cpudata.setEntity("Utilization");
        cpudata.setSubentity("Utilization");
        cpudata.setRestype("dynamic");
        cpudata.setUnit("%");
        cpudata.setThevalue(String.valueOf(format(value)));
        cpuVector.addElement(cpudata);
        return cpuVector;
    }

}

