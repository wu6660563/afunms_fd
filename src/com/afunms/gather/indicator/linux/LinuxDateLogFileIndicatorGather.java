/*
 * @(#)LinuxDateLogFileIndicatorGather.java     v1.01, 2014 1 14
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.linux;

import java.util.Calendar;
import java.util.Vector;

import com.afunms.gather.indicator.base.IndicatorValueFactory;
import com.afunms.gather.indicator.base.LogFileIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Systemcollectdata;
import com.afunms.polling.snmp.linux.LinuxLogFileKeywordConstant;

/**
 * ClassName:   LinuxDateLogFileIndicatorGather.java
 * <p>
 *
 * @author      ÄôÁÖ
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 14 15:39:16
 */
public class LinuxDateLogFileIndicatorGather extends LogFileIndicatorGather {

    /**
     * BEGIN_KEYWORD:
     * <p>¿ªÊ¼×Ö·û´®
     *
     * @since   v1.01
     */
    private static final String BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_DATE_BEGIN_KEYWORD;

    /**
     * END_KEYWORD:
     * <p>½áÊø×Ö·û´®
     *
     * @since   v1.01
     */
    private static final String END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_DATE_END_KEYWORD;

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
        String dateContent = getLogFileContent(beginStr, endStr);

        String dateString = "";
        try {
            if (dateContent != null && dateContent.length() > 0) {
                dateString = dateContent.trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Vector<Systemcollectdata> vector = new Vector<Systemcollectdata>();
        vector.add(createSystemcollectdata(getCalendar(), "Systime", "Systime", dateString, "", ""));

        return createSimpleIndicatorValue(vector);
    }

    public Systemcollectdata createSystemcollectdata(Calendar date, String entity, String subentity, String thevalue, String unit, String chname) {
        return IndicatorValueFactory.createSystemcollectdata(getIpAddress(), date, entity, subentity, thevalue, unit, chname);
    }
}

