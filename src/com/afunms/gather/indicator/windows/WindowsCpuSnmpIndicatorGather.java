/*
 * @(#)WindowsCpuSnmpIndicatorGather.java     v1.01, 2013 12 27
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.windows;

import com.afunms.gather.indicator.general.GeneralCpuSnmpIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;

/**
 * ClassName:   WindowsCpuSnmpIndicatorGather.java
 * <p>{@link WindowsCpuSnmpIndicatorGather} Window CPU 指标使用 SNMP 方式的指标采集类
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 27 00:40:42
 */
public class WindowsCpuSnmpIndicatorGather extends GeneralCpuSnmpIndicatorGather {

    /**
     * OIDS:
     * <p>指标 OID
     *
     * @since   v1.01
     */
    private final static String[] OIDS = new String[] { "1.3.6.1.2.1.25.3.3.1.2" };

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
        return getSimpleIndicatorValue(OIDS);
    }
}

