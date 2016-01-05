/*
 * @(#)VenusCpuSnmpIndicatorGather.java     v1.01, 2014 1 14
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.venus;

import com.afunms.gather.indicator.general.GeneralCpuSnmpIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;

/**
 * ClassName:   VenusCpuSnmpIndicatorGather.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 14 11:36:37
 */
public class VenusCpuSnmpIndicatorGather extends GeneralCpuSnmpIndicatorGather {

    /**
     * OIDS:
     * <p>÷∏±Í OID
     *
     * @since   v1.01
     */
    private final static String[] OIDS = new String[] { "1.3.6.1.4.1.15227.1.3.1.1.1" };

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

