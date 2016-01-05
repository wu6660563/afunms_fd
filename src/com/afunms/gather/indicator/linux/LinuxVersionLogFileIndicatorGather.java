/*
 * @(#)LinuxVersionLogFileIndicatorGather.java     v1.01, 2014 1 14
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.linux;

import com.afunms.gather.indicator.base.LogFileIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.snmp.linux.LinuxLogFileKeywordConstant;

/**
 * ClassName:   LinuxVersionLogFileIndicatorGather.java
 * <p>
 *
 * @author      ÄôÁÖ
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 14 20:52:43
 */
public class LinuxVersionLogFileIndicatorGather extends LogFileIndicatorGather {

    /**
     * BEGIN_KEYWORD:
     * <p>¿ªÊ¼×Ö·û´®
     *
     * @since   v1.01
     */
    private static final String BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_VERSION_BEGIN_KEYWORD;

    /**
     * END_KEYWORD:
     * <p>½áÊø×Ö·û´®
     *
     * @since   v1.01
     */
    private static final String END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_VERSION_END_KEYWORD;

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
        String versionContent = getLogFileContent(beginStr, endStr);
        return createSimpleIndicatorValue(versionContent);
    }

}

