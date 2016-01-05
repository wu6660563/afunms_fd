/*
 * @(#)Tru64UpTimeLogFileIndicatorGather.java     v1.01, 2014 1 15
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.tru64;

import java.util.Calendar;
import java.util.Vector;

import com.afunms.gather.indicator.base.IndicatorValueFactory;
import com.afunms.gather.indicator.base.LogFileIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Systemcollectdata;
import com.afunms.polling.snmp.tru64.Tru64LogFileKeywordConstant;

/**
 * ClassName:   Tru64UpTimeLogFileIndicatorGather.java
 * <p>
 *
 * @author      ÄôÁÖ
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 15 23:54:41
 */
public class Tru64UpTimeLogFileIndicatorGather extends LogFileIndicatorGather {

    /**
     * BEGIN_KEYWORD:
     * <p>¿ªÊ¼×Ö·û´®
     *
     * @since   v1.01
     */
    private static final String BEGIN_KEYWORD = Tru64LogFileKeywordConstant.TRU64_UPTIME_BEGIN_KEYWORD;

    /**
     * END_KEYWORD:
     * <p>½áÊø×Ö·û´®
     *
     * @since   v1.01
     */
    private static final String END_KEYWORD = Tru64LogFileKeywordConstant.TRU64_UPTIME_END_KEYWORD;

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
        String uptimeContent = getLogFileContent(beginStr, endStr);

        Calendar date = getCalendar();

        if (uptimeContent == null) {
            uptimeContent = "";
        }

        Vector<Systemcollectdata> vector = new Vector<Systemcollectdata>();
        vector.addElement(createSystemcollectdata(date, "SysUptime", "SysUptime", uptimeContent, "", ""));

        return createSimpleIndicatorValue(vector);
    }

    public Systemcollectdata createSystemcollectdata(Calendar date, String entity, String subentity, String thevalue, String unit, String chname) {
        return IndicatorValueFactory.createSystemcollectdata(getIpAddress(), date, entity, subentity, thevalue, unit, chname);
    }

}

