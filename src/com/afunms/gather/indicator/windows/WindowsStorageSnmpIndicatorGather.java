/*
 * @(#)WindowsStorageSnmpIndicatorGather.java     v1.01, 2013 12 29
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.windows;

import java.util.Hashtable;
import java.util.Vector;

import com.afunms.gather.indicator.base.SnmpIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Storagecollectdata;

/**
 * ClassName:   WindowsStorageSnmpIndicatorGather.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 29 14:44:16
 */
public class WindowsStorageSnmpIndicatorGather extends SnmpIndicatorGather {

    private final static String[] OIDS = new String[] {
        "1.3.6.1.2.1.25.2.3.1.1",   // hrStorageIndex
        "1.3.6.1.2.1.25.2.3.1.2",   // hrStorageType
        "1.3.6.1.2.1.25.2.3.1.3",   // hrStorageDescr
        "1.3.6.1.2.1.25.2.3.1.4",   // hrStorageAllocationUnits
        "1.3.6.1.2.1.25.2.3.1.5"    // hrStorageSize
    };

    private static Hashtable<String, String> storage_Type = null;
    static {
        storage_Type = new Hashtable<String, String>();
        storage_Type.put("1.3.6.1.2.1.25.2.1.1", "其他");
        storage_Type.put("1.3.6.1.2.1.25.2.1.2", "物理内存");
        storage_Type.put("1.3.6.1.2.1.25.2.1.3", "虚拟内存");
        storage_Type.put("1.3.6.1.2.1.25.2.1.4", "硬盘");
        storage_Type.put("1.3.6.1.2.1.25.2.1.5", "移动硬盘");
        storage_Type.put("1.3.6.1.2.1.25.2.1.6", "软盘");
        storage_Type.put("1.3.6.1.2.1.25.2.1.7", "光盘");
        storage_Type.put("1.3.6.1.2.1.25.2.1.8", "内存盘");
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
        Vector<Storagecollectdata> storageVector = new Vector<Storagecollectdata>();
        if (valueArray != null) {
            for (int i = 0; i < valueArray.length; i++) {
                String indexString = valueArray[i][0];
                String typeString = valueArray[i][1];
                String descrString = valueArray[i][2];
                String unitString = valueArray[i][3];
                String sizeString = valueArray[i][4];

                Double size = Double.valueOf(sizeString) * Double.valueOf(unitString) / (1024 * 1024);
                String unit = "M";
                if (size >= 1024) {
                    size = size / 1024;
                    unit = "G";
                }

                sizeString = String.valueOf(format(size)) + unit;
                storageVector.add(createStoragecollectdata(indexString, descrString, sizeString, storage_Type.get(typeString)));
            }
        }
        return createSimpleIndicatorValue(storageVector);
    }

    public Storagecollectdata createStoragecollectdata(String index, String name, String size, String type) {
        Storagecollectdata storagecollectdata = new Storagecollectdata();
        storagecollectdata.setIpaddress(getIpAddress());
        storagecollectdata.setStorageindex(index);
        storagecollectdata.setName(name);
        storagecollectdata.setCap(size);
        storagecollectdata.setType(type);
        return storagecollectdata;
    }
}

