/*
 * @(#)VenusMemorySnmpIndicatorGather.java     v1.01, 2014 1 14
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.venus;

import java.util.Calendar;
import java.util.Vector;

import com.afunms.gather.indicator.base.SnmpIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Memorycollectdata;

/**
 * ClassName:   VenusMemorySnmpIndicatorGather.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 14 11:38:08
 */
public class VenusMemorySnmpIndicatorGather extends SnmpIndicatorGather {

    /**
     * OIDS:
     * <p>÷∏±Í OID
     *
     * @since   v1.01
     */
    private final static String[] OIDS = new String[] {
        "1.3.6.1.4.1.15227.1.3.1.1.2"
    };

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
        String[][] valueArray = getTableValuesByOids(OIDS);
        Vector<Memorycollectdata> memoryVector = new Vector<Memorycollectdata>();
        if (valueArray != null) {
            Calendar date = getCalendar();
            Double allValue = 0D;
            for (int i = 0; i < valueArray.length; i++) {
                String value = valueArray[i][0];
                try {
                    if (value != null) {
                        value = value.replaceAll("%", "");
                        allValue += Double.valueOf(value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            String value = "0";
            if (valueArray.length > 0) {
                value = String.valueOf(format(allValue / (valueArray.length)));
            }
            memoryVector.add(createMemorycollectdata(date, "Utilization", "Utilization", value, "%"));
        }
        return createSimpleIndicatorValue(memoryVector);
    }

    public Memorycollectdata createMemorycollectdata(Calendar date, String entity, String subentity, String thevalue, String unit) {
        Memorycollectdata memorydata = new Memorycollectdata();
        memorydata.setIpaddress(getIpAddress());
        memorydata.setCollecttime(date);
        memorydata.setCategory("Memory");
        memorydata.setEntity(entity);
        memorydata.setSubentity(subentity);
        memorydata.setRestype("dynamic");
        memorydata.setThevalue(thevalue);
        memorydata.setUnit(unit);
        return memorydata;
    }

}

