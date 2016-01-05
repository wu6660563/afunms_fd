/*
 * @(#)CiscoCpuSnmpIndicatorGather.java     v1.01, 2013 12 27
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.cisco;

import java.util.ArrayList;
import java.util.List;

import com.afunms.gather.indicator.general.GeneralCpuSnmpIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;

/**
 * ClassName:   CiscoCpuSnmpIndicatorGather.java
 * <p>{@link CiscoCpuSnmpIndicatorGather} Cisco CPU 指标使用 SNMP 方式的指标采集类
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 27 01:07:41
 */
public class CiscoCpuSnmpIndicatorGather extends GeneralCpuSnmpIndicatorGather {

    /**
     * OIDS:
     * <p>指标 OID
     *
     * @since   v1.01
     */
    private final static String[] OIDS = new String[] { "1.3.6.1.4.1.9.2.1.57" };

    /**
     * OIDS:
     * <p>指标 OID_1
     *
     * @since   v1.01
     */
    private final static String[] OIDS_1 = new String[] { "1.3.6.1.4.1.9.9.109.1.1.1.1.4" };
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
        List<String[]> list = new ArrayList<String[]>();
        list.add(OIDS_1);
        list.add(OIDS);
        return getSimpleIndicatorValue(list);
    }
}

