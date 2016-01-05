/*
 * @(#)Tru64UserLogFileIndicatorGather.java     v1.01, 2014 1 15
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.tru64;

import java.util.Calendar;
import java.util.Vector;

import com.afunms.gather.indicator.base.IndicatorValueFactory;
import com.afunms.gather.indicator.base.LogFileIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Usercollectdata;
import com.afunms.polling.snmp.tru64.Tru64LogFileKeywordConstant;

/**
 * ClassName:   Tru64UserLogFileIndicatorGather.java
 * <p>
 *
 * @author      ÄôÁÖ
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 15 23:58:05
 */
public class Tru64UserLogFileIndicatorGather extends LogFileIndicatorGather {

    /**
     * BEGIN_KEYWORD:
     * <p>¿ªÊ¼×Ö·û´®
     *
     * @since   v1.01
     */
    private static final String BEGIN_KEYWORD = Tru64LogFileKeywordConstant.TRU64_USER_BEGIN_KEYWORD;

    /**
     * END_KEYWORD:
     * <p>½áÊø×Ö·û´®
     *
     * @since   v1.01
     */
    private static final String END_KEYWORD = Tru64LogFileKeywordConstant.TRU64_USER_END_KEYWORD;

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
        Vector<Usercollectdata> vector = new Vector<Usercollectdata>();
        try {
            Calendar date = getCalendar();
            String userContent = getLogFileContent(beginStr, endStr);
            String[] userLineArr = userContent.split("\n");
            for (int i = 0; i < userLineArr.length; i++) {
                String[] result = userLineArr[i].trim().split(":");
                if (result.length > 0) {
                    vector.addElement(createUsercollectdata(date, "Sysuser", result[0], result[0], "", ""));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return createSimpleIndicatorValue(vector);
    }

    public Usercollectdata createUsercollectdata(Calendar date, String entity, String subentity, String thevalue, String unit, String chname) {
        return IndicatorValueFactory.createUsercollectdata(getIpAddress(), date, entity, subentity, thevalue, unit, chname);
    }
}

