/*
 * @(#)LinuxMacLogFileIndicatorGather.java     v1.01, 2014 1 14
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.linux;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.gather.indicator.base.IndicatorValueFactory;
import com.afunms.gather.indicator.base.LogFileIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Systemcollectdata;
import com.afunms.polling.snmp.linux.LinuxLogFileKeywordConstant;

/**
 * ClassName:   LinuxMacLogFileIndicatorGather.java
 * <p>
 *
 * @author      ÄôÁÖ
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 14 16:37:28
 */
public class LinuxMacLogFileIndicatorGather extends LogFileIndicatorGather {

    /**
     * BEGIN_KEYWORD:
     * <p>¿ªÊ¼×Ö·û´®
     *
     * @since   v1.01
     */
    private static final String BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_MAC_BEGIN_KEYWORD;

    /**
     * END_KEYWORD:
     * <p>½áÊø×Ö·û´®
     *
     * @since   v1.01
     */
    private static final String END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_MAC_END_KEYWORD;

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
        String beginStr = BEGIN_KEYWORD;
        String endStr = END_KEYWORD;
        String macContent = getLogFileContent(beginStr, endStr);

        String[] macLineArr = macContent.split("\n");
        String MAC = "";
        Hashtable<String, String> machash = new Hashtable<String, String>();
        boolean isFirst = true;
        for (int i = 0; i < macLineArr.length; i++) {
            String[] mac_tmpData = macLineArr[i].trim().split("\\s++");
            if (mac_tmpData.length == 4) {
                if (mac_tmpData[0].equalsIgnoreCase("link/ether")
                        && mac_tmpData[2].equalsIgnoreCase("brd")) {
                    String macPer = mac_tmpData[1].toLowerCase();
                    if (macPer.equalsIgnoreCase("00:00:00:00:00:00")) {
                        continue;
                    }
                    if (machash.containsKey(macPer)) {
                        continue;
                    }
                    machash.put(macPer, macPer);
                    if (!isFirst) {
                        MAC += ",";
                    }
                    MAC += macPer;
                    isFirst = false;
                }
            }
        }

        Vector<Systemcollectdata> vector = new Vector<Systemcollectdata>();
        vector.add(createSystemcollectdata(getCalendar(), "MacAddr", "MacAddr", MAC, "", ""));
        return createSimpleIndicatorValue(vector);
    
    }

    public Systemcollectdata createSystemcollectdata(Calendar date, String entity, String subentity, String thevalue, String unit, String chname) {
        return IndicatorValueFactory.createSystemcollectdata(getIpAddress(), date, entity, subentity, thevalue, unit, chname);
    }
}

