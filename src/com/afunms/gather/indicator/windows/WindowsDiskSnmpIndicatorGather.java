/*
 * @(#)WindowsDiskSnmpIndicatorGather.java     v1.01, 2013 12 27
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.windows;

import java.util.Calendar;
import java.util.Vector;

import com.afunms.gather.indicator.base.SnmpIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Diskcollectdata;

/**
 * ClassName:   WindowsDiskSnmpIndicatorGather.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 27 15:48:21
 */
public class WindowsDiskSnmpIndicatorGather extends SnmpIndicatorGather {

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
    @SuppressWarnings("unchecked")
    @Override
    public SimpleIndicatorValue getSimpleIndicatorValue() {
        String[][] valueArray = getTableValuesByOids(OIDS);
        Vector<Diskcollectdata> diskVector = new Vector<Diskcollectdata>();
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
                } else if (descString.indexOf("Memory") >= 0) {
                    // ƒ⁄¥Êπ˝¬À
                    continue;
                } else if (descString.trim().length() > 2) {
                    descString = descString.substring(0, 3);
                }
                if (descString.indexOf("\\") >= 0) {
                    descString = descString.replace("\\", "/");
                }

                Double size = Double.valueOf(sizeString) * Double.valueOf(unitString) / (1024 * 1024);
                Double used = Double.valueOf(usedString) * Double.valueOf(unitString) / (1024 * 1024);
                Double utilization = 0D;
                if (size > 0) {
                    utilization = (used / size) * 100;
                }
                String unit = "M";
                if (size >= 1024) {
                    size = size / 1024;
                    unit = "G";
                }
                if ("G".equals(unit)) {
                    used = used / 1024;
                }

                Double utilizationInc = 0D;
                if (getLastSimpleIndicatorValue() != null) {
                    Vector<Diskcollectdata> lastVector = (Vector<Diskcollectdata>) getLastSimpleIndicatorValue().getValue();
                    for (Diskcollectdata diskcollectdata : lastVector) {
                        if ("Utilization".equalsIgnoreCase(diskcollectdata.getEntity())
                                        && descString.equalsIgnoreCase(diskcollectdata.getSubentity())) {
                            utilizationInc = utilization - Double.valueOf(diskcollectdata.getThevalue());
                        }
                    }
                }

                sizeString = String.valueOf(format(size));
                usedString = String.valueOf(format(used));
                String utilizationString = String.valueOf(format(utilization));
                String utilizationIncString = String.valueOf(format(utilizationInc));
                diskVector.add(createDiskcollectdata(date, "Utilization", descString, utilizationString, "%"));
                diskVector.add(createDiskcollectdata(date, "AllSize", descString, sizeString, unit));
                diskVector.add(createDiskcollectdata(date, "UsedSize", descString, usedString, unit));
                diskVector.add(createDiskcollectdata(date, "UtilizationInc", descString, utilizationIncString, "%"));
            }
        }
        return createSimpleIndicatorValue(diskVector);
    }

    public Diskcollectdata createDiskcollectdata(Calendar date, String entity, String subentity, String thevalue, String unit) {
        Diskcollectdata diskdata = new Diskcollectdata();
        diskdata.setIpaddress(getIpAddress());
        diskdata.setCollecttime(date);
        diskdata.setCategory("Disk");
        diskdata.setEntity(entity);
        diskdata.setSubentity(subentity);
        diskdata.setRestype("dynamic");
        diskdata.setUnit(unit);
        diskdata.setThevalue(thevalue);
        return diskdata;
    }
}

