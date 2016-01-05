/*
 * @(#)GeneralSystemSnmpIndicatorGather.java     v1.01, 2013 12 29
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.general;

import java.util.Calendar;
import java.util.Vector;

import com.afunms.gather.indicator.base.SnmpIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Systemcollectdata;
import com.afunms.polling.snmp.SnmpMibConstants;

/**
 * ClassName:   GeneralSystemSnmpIndicatorGather.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 29 15:12:43
 */
public class GeneralSystemSnmpIndicatorGather extends SnmpIndicatorGather {

    private final static String[] DESC = SnmpMibConstants.NetWorkMibSystemDesc;
    private final static String[] CHNAME = SnmpMibConstants.NetWorkMibSystemChname;

    private final static String[] OIDS = new String[] {
        "1.3.6.1.2.1.1.1",
        "1.3.6.1.2.1.1.3",
        "1.3.6.1.2.1.1.4",
        "1.3.6.1.2.1.1.5",
        "1.3.6.1.2.1.1.6",
        "1.3.6.1.2.1.1.7"
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
        Vector<Systemcollectdata> systemVector = new Vector<Systemcollectdata>();
        if (valueArray != null) {
            Calendar date = getCalendar();
            for (int i = 0; i < valueArray.length; i++) {
                for (int j = 0; j < DESC.length; j++) {
                    String entity = DESC[i];
                    String subentity = DESC[j];
                    String chname = CHNAME[j];

                    String value = valueArray[i][j];
                    if (value != null) {
                        value = value.replaceAll("'", " ");
                    }
                    systemVector.add(createSystemcollectdata(date, entity, subentity, value, "", chname));
                }
            }
        }
        return createSimpleIndicatorValue(systemVector);
    }

    public Systemcollectdata createSystemcollectdata(Calendar date, String entity, String subentity, String thevalue, String unit, String chname) {
        Systemcollectdata systemcollectdata = new Systemcollectdata();
        systemcollectdata.setIpaddress(getIpAddress());
        systemcollectdata.setCollecttime(date);
        systemcollectdata.setCategory("System");
        systemcollectdata.setEntity(entity);
        systemcollectdata.setSubentity(subentity);
        systemcollectdata.setRestype("static");
        systemcollectdata.setUnit(unit);
        systemcollectdata.setThevalue(thevalue);
        return systemcollectdata;
    }
}

