/*
 * @(#)WindowsPhysicalMemorySnmpIndicatorGather.java     v1.01, 2013 12 27
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
 * ClassName:   WindowsPhysicalMemorySnmpIndicatorGather.java
 * <p>{@link WindowsPhysicalMemorySnmpIndicatorGather}  Window 物理内存 指标使用 SNMP 方式的指标采集类
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 27 16:57:18
 */
public class WindowsPhysicalMemorySnmpIndicatorGather extends
                SnmpIndicatorGather {

    /**
     * USED_OIDS:
     * <p>已使用
     *
     * @since   v1.01
     */
    private final static String[] USED_OIDS = new String[] { "1.3.6.1.2.1.25.5.1.1.2" };

    /**
     * SIZE_OIDS:
     * <p>总大小
     *
     * @since   v1.01
     */
    private final static String[] SIZE_OIDS = new String[] { "1.3.6.1.2.1.25.2.2" };

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

        String[][] usedValueArray = getTableValuesByOids(USED_OIDS);
        String[][] sizeValueArray = getTableValuesByOids(SIZE_OIDS);

        int allMemorySize = 0;
        if (sizeValueArray != null) {
            for (int i = 0; i < sizeValueArray.length; i++) {
                if (sizeValueArray[i][0] == null) {
                    continue;
                }
                allMemorySize = Integer.parseInt(sizeValueArray[i][0]);
            }
        }
        int allUsedSize = 0;
        if (usedValueArray != null) {
            for (int i = 0; i < usedValueArray.length; i++) {
                if (usedValueArray[i][0] == null) {
                    continue;
                }
                int processUsedSize = Integer
                        .parseInt(usedValueArray[i][0]);
                allUsedSize += processUsedSize;
            }
        }
        
        Double size = Double.valueOf(allMemorySize) / 1024;
        Double used = Double.valueOf(allUsedSize) / 1024;
        Double utilization = 0D;
        if (allMemorySize != 0) {
            utilization = used * 100D / size;
        }
        String sizeUnit = "M";
        if (size >= 1024) {
            size = size / 1024;
            sizeUnit = "G";
        }
        String usedUnit = "M";
        if (used >= 1024) {
            used = used / 1024;
            usedUnit = "G";
        }
        String sizeString = String.valueOf(format(size));
        String usedString = String.valueOf(format(used));
        String utilizationString = String.valueOf(format(utilization));

        Calendar date = getCalendar();
        Vector<Memorycollectdata> memoryVector = new Vector<Memorycollectdata>();
        memoryVector.add(createMemorycollectdata(date, "Utilization", "PhysicalMemory", utilizationString, "%"));
        memoryVector.add(createMemorycollectdata(date, "Capability", "PhysicalMemory", sizeString, sizeUnit));
        memoryVector.add(createMemorycollectdata(date, "UsedSize", "PhysicalMemory", usedString, usedUnit));
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

