/*
 * @(#)WindowsVirtualMemorySnmpIndicatorGather.java     v1.01, 2013 12 27
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.windows;

import java.util.Calendar;
import java.util.Vector;

import com.afunms.gather.indicator.base.SnmpIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Memorycollectdata;

/**
 * ClassName:   WindowsVirtualMemorySnmpIndicatorGather.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 27 17:40:10
 */
public class WindowsVirtualMemorySnmpIndicatorGather extends
                SnmpIndicatorGather {

    private final static String[] OIDS = new String[] {
        "1.3.6.1.2.1.25.2.3.1.1",
        "1.3.6.1.2.1.25.2.3.1.2",
        "1.3.6.1.2.1.25.2.3.1.3",
        "1.3.6.1.2.1.25.2.3.1.4",
        "1.3.6.1.2.1.25.2.3.1.5",
        "1.3.6.1.2.1.25.2.3.1.6",
        "1.3.6.1.2.1.25.2.3.1.7" };
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
            for (int i = 0; i < valueArray.length; i++) {
                //String indexString = valueArray[i][0];
                //String typeString = valueArray[i][1];
                String descString = valueArray[i][2];
                String unitString = valueArray[i][3];
                String sizeString = valueArray[i][4];
                String usedString = valueArray[i][5];
                //String failuresString = valueArray[i][6];
                
                if (descString == null) {
                    descString = "";
                } else if (!descString.trim().equals("Virtual Memory")) {
                    // ƒ⁄¥Êπ˝¬À
                    continue;
                }

                Double size = Double.valueOf(sizeString) * Double.valueOf(unitString) / (1024 * 1024);
                Double used = Double.valueOf(usedString) * Double.valueOf(unitString) / (1024 * 1024);
                Double utilization = 0D;
                if (used > 0) {
                    utilization = used * 100D / size;
                }
                String unit = "M";
                if (size >= 1024) {
                    size = size / 1024;
                    unit = "G";
                }
                if ("G".equals(unit)) {
                    used = used / 1024;
                }

                sizeString = String.valueOf(format(size));
                usedString = String.valueOf(format(used));
                String utilizationString = String.valueOf(format(utilization));
                memoryVector.add(createMemorycollectdata(date, "Utilization", "VirtualMemory", utilizationString, "%"));
                memoryVector.add(createMemorycollectdata(date, "Capability", "VirtualMemory", sizeString, unit));
                memoryVector.add(createMemorycollectdata(date, "UsedSize", "VirtualMemory", usedString, unit));
            }
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

