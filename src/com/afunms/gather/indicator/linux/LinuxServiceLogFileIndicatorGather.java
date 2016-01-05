/*
 * @(#)LinuxServiceLogFileIndicatorGather.java     v1.01, 2014 1 14
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.linux;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.afunms.gather.indicator.base.LogFileIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.snmp.linux.LinuxLogFileKeywordConstant;

/**
 * ClassName:   LinuxServiceLogFileIndicatorGather.java
 * <p>
 *
 * @author      ÄôÁÖ
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 14 17:42:30
 */
public class LinuxServiceLogFileIndicatorGather extends LogFileIndicatorGather {

    /**
     * BEGIN_KEYWORD:
     * <p>¿ªÊ¼×Ö·û´®
     *
     * @since   v1.01
     */
    private static final String BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_SERVICE_BEGIN_KEYWORD;

    /**
     * END_KEYWORD:
     * <p>½áÊø×Ö·û´®
     *
     * @since   v1.01
     */
    private static final String END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_SERVICE_END_KEYWORD;

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
        String serviceContent = getLogFileContent(beginStr, endStr);

        String[] serviceLineArr = serviceContent.split("\n");

        List<Hashtable<String, String>> servicelist = new ArrayList<Hashtable<String, String>>();
        for (int i = 0; i < serviceLineArr.length; i++) {
            String[] tmpData = serviceLineArr[i].trim().split("\\s++");
            if (tmpData.length == 8) {
                Hashtable<String, String> service = new Hashtable<String, String>();
                String name = tmpData[0];
                String servicestatus = tmpData[5];
                String status = "Î´ÆôÓÃ";
                if (servicestatus != null
                        && (servicestatus.indexOf("on") >= 0 || servicestatus
                                .indexOf("ÆôÓÃ") >= 0)) {
                    status = "ÆôÓÃ";
                }
                service.put("name", name);
                service.put("status", status);
                servicelist.add(service);
            }
        }
        return createSimpleIndicatorValue(servicelist);
    }

}

