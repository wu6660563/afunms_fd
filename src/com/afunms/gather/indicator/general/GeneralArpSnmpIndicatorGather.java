/*
 * @(#)GeneralArpSnmpIndicatorGather.java     v1.01, 2013 12 30
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.general;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import com.afunms.gather.indicator.base.SnmpIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.IpMac;

/**
 * ClassName:   GeneralArpSnmpIndicatorGather.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 30 09:30:52
 */
public class GeneralArpSnmpIndicatorGather extends SnmpIndicatorGather {

    private final static String[] OIDS = new String[] {
        "1.3.6.1.2.1.4.22.1.1",     // 1.ifIndex
        "1.3.6.1.2.1.4.22.1.2",     // 2.mac
        "1.3.6.1.2.1.4.22.1.3",     // 3.ip
        "1.3.6.1.2.1.4.22.1.4" };   // 4.type

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
        Vector<IpMac> ipMacVector = new Vector<IpMac>();
        if (valueArray != null) {
            Calendar date = getCalendar();
            for (int i = 0; i < valueArray.length; i++) {
                String ifIndex = valueArray[i][0];
                String mac = valueArray[i][1];
                String ipaddress = valueArray[i][2];
                if (mac != null && !mac.contains(":")) {
                    // MAC地址如：00:d0:83:04:d5:97
                    // SysLogger.info("ArpSnmp.java MAC地址为乱码：" +
                    // sValue);
                    mac = "--";
                }
                if(ipaddress == null || "".equals(ipaddress)) {
                	continue;
                }
                ipMacVector.add(createIpMac(date, ifIndex, ipaddress, mac));
            }
        }
        return createSimpleIndicatorValue(ipMacVector);
    }

    public IpMac createIpMac(Calendar date, String ifIndex, String ipaddress, String mac) {
        IpMac ipmac = new IpMac();
        ipmac.setIfindex(ifIndex);
        ipmac.setIpaddress(ipaddress);
        ipmac.setMac(mac);
        ipmac.setIfband("0");
        ipmac.setIfsms("0");
        ipmac.setCollecttime(new GregorianCalendar());
        ipmac.setRelateipaddr(getIpAddress());
        return ipmac;
    }
}

