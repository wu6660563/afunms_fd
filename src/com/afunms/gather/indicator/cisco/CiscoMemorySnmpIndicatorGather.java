/*
 * @(#)CiscoMemorySnmpIndicatorGather.java     v1.01, 2014 1 6
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.cisco;

import java.util.Calendar;
import java.util.Vector;

import com.afunms.gather.indicator.base.SnmpIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Memorycollectdata;

/**
 * ClassName:   CiscoMemorySnmpIndicatorGather.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 6 21:56:35
 */
public class CiscoMemorySnmpIndicatorGather extends SnmpIndicatorGather {

    /**
     * OIDS:
     * <p>÷∏±Í OID
     *
     * @since   v1.01
     */
    private final static String[] OIDS = new String[] {
        "1.3.6.1.4.1.9.9.48.1.1.1.5", // ciscoMemoryPoolUsed
        "1.3.6.1.4.1.9.9.48.1.1.1.6"  // ciscoMemoryPoolFree
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
            Double allUsedValue = 0D;
            Double allfreeValue = 0D;
            for (int i = 0; i < valueArray.length; i++) {
                String usedValue = valueArray[i][0];
                String freeValue = valueArray[i][1];
                if (usedValue == null || freeValue == null) {
                    continue;
                }
                try {
                    allUsedValue += Double.valueOf(usedValue);
                    allfreeValue += Double.valueOf(freeValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            String value = "0";
            if (allUsedValue + allfreeValue > 0D) {
                value = String.valueOf(format(allUsedValue * 100 / (allUsedValue + allfreeValue)));
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

