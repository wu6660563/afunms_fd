/*
 * @(#)IndicatorValueFactory.java     v1.01, 2014 1 14
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.base;

import java.util.Calendar;

import com.afunms.polling.om.Diskcollectdata;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.Processcollectdata;
import com.afunms.polling.om.Systemcollectdata;
import com.afunms.polling.om.Usercollectdata;

/**
 * ClassName:   IndicatorValueFactory.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 14 15:49:47
 */
public class IndicatorValueFactory {
    
    public static Systemcollectdata createSystemcollectdata(String ipAddress, Calendar date, String entity, String subentity, String thevalue, String unit, String chname) {
        Systemcollectdata systemcollectdata = new Systemcollectdata();
        systemcollectdata.setIpaddress(ipAddress);
        systemcollectdata.setCollecttime(date);
        systemcollectdata.setCategory("System");
        systemcollectdata.setEntity(entity);
        systemcollectdata.setSubentity(subentity);
        systemcollectdata.setRestype("static");
        systemcollectdata.setUnit(unit);
        systemcollectdata.setThevalue(thevalue);
        return systemcollectdata;
    }

    public static Diskcollectdata createDiskcollectdata(String ipAddress, Calendar date, String entity, String subentity, String thevalue, String unit) {
        Diskcollectdata diskdata = new Diskcollectdata();
        diskdata.setCollecttime(date);
        diskdata.setCategory("Disk");
        diskdata.setEntity(entity);
        diskdata.setSubentity(subentity);
        diskdata.setRestype("dynamic");
        diskdata.setUnit(unit);
        diskdata.setThevalue(thevalue);
        return diskdata;
    }

    public static Interfacecollectdata createInterfacecollectdata(String ipAddress, Calendar date, String entity, String subentity, String thevalue, String restype, String unit, String chname) {
        Interfacecollectdata interfacedata = new Interfacecollectdata();
        interfacedata.setIpaddress(ipAddress);
        interfacedata.setCollecttime(date);
        interfacedata.setCategory("Interface");
        interfacedata.setEntity(entity);
        interfacedata.setSubentity(subentity);
        interfacedata.setThevalue(thevalue);
        interfacedata.setRestype(restype);
        interfacedata.setUnit(unit);
        interfacedata.setChname(chname);
        return interfacedata;
    }

    public static Processcollectdata createProcesscollectdata(String ipAddress, Calendar date, String entity, String subentity, String thevalue, String unit, String chname) {
        Processcollectdata processcollectdata = new Processcollectdata();
        processcollectdata.setIpaddress(ipAddress);
        processcollectdata.setCollecttime(date);
        processcollectdata.setCategory("Process");
        processcollectdata.setEntity(entity);
        processcollectdata.setSubentity(subentity);
        processcollectdata.setRestype("dynamic");
        processcollectdata.setThevalue(thevalue);
        processcollectdata.setUnit(unit);
        processcollectdata.setChname(chname);
        return processcollectdata;
    }

    public static Usercollectdata createUsercollectdata(String ipAddress, Calendar date, String entity, String subentity, String thevalue, String unit, String chname) {
        Usercollectdata data = new Usercollectdata();
        data.setIpaddress(ipAddress);
        data.setCollecttime(date);
        data.setCategory("User");
        data.setEntity(entity);
        data.setSubentity(subentity);
        data.setRestype("static");
        data.setUnit(unit);
        data.setThevalue(thevalue);
        return data;
    }
}

