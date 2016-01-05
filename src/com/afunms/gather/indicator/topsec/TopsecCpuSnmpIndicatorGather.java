/*
 * @(#)TopsecCpuSnmpIndicatorGather.java     v1.01, 2014 1 14
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.topsec;

import com.afunms.gather.indicator.general.GeneralCpuSnmpIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;

/**
 * ClassName:   TopsecCpuSnmpIndicatorGather.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 14 11:27:21
 */
public class TopsecCpuSnmpIndicatorGather extends GeneralCpuSnmpIndicatorGather {

    /**
     * OIDS:
     * <p>÷∏±Í OID
     *
     * @since   v1.01
     */
    private final static String[] OIDS = new String[] { "1.3.6.1.4.1.14331.5.5.1.4.5" };

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

