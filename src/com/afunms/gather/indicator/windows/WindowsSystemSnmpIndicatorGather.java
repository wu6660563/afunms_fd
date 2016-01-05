/*
 * @(#)WindowsSystemSnmpIndicatorGather.java     v1.01, 2013 12 29
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.windows;

import java.util.Vector;

import com.afunms.gather.indicator.general.GeneralSystemSnmpIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Systemcollectdata;

/**
 * ClassName:   WindowsSystemSnmpIndicatorGather.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 29 15:29:14
 */
public class WindowsSystemSnmpIndicatorGather extends
                GeneralSystemSnmpIndicatorGather {

    private final static String[] OIDS = new String[] {
        "1.3.6.1.2.1.2.2.1.6"
    };

    /**
     * getSimpleIndicatorValue:
     *
     * @return
     *
     * @since   v1.01
     * @see com.afunms.gather.indicator.general.GeneralSystemSnmpIndicatorGather#getSimpleIndicatorValue()
     */
    @SuppressWarnings("unchecked")
    @Override
    public SimpleIndicatorValue getSimpleIndicatorValue() {
        SimpleIndicatorValue simpleIndicatorValue = super.getSimpleIndicatorValue();
        Vector<Systemcollectdata> systemVector = (Vector<Systemcollectdata>) simpleIndicatorValue.getValue();

        // 获取 MAC 地址
        String[][] valueArray = getTableValuesByOids(OIDS);
        String value = "";
        if (valueArray != null) {
            for (int i = 0; i < valueArray.length; i++) {
                if (valueArray[i][0] != null) {
                    value = valueArray[i][0];
                }
            }
        }
        systemVector.addElement(createSystemcollectdata(getCalendar(), "MacAddr", "MacAddr", value, "", ""));
        return simpleIndicatorValue;
    }

}

