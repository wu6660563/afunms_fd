/*
 * @(#)LinuxCollectTimeLogFileIndicatorGather.java     v1.01, 2014 1 14
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.linux;

import java.util.Date;

import com.afunms.gather.indicator.base.LogFileIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;

/**
 * ClassName:   LinuxCollectTimeLogFileIndicatorGather.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 14 23:37:19
 */
public class LinuxCollectTimeLogFileIndicatorGather extends
                LogFileIndicatorGather {

//    /**
//     * BEGIN_KEYWORD:
//     * <p>开始字符串
//     *
//     * @since   v1.01
//     */
//    private static final String BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_COLLECTTIME_BEGIN_KEYWORD;
//
//    /**
//     * END_KEYWORD:
//     * <p>结束字符串
//     *
//     * @since   v1.01
//     */
//    private static final String END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_COLLECTTIME_END_KEYWORD;

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
//        脚本里面没有CollectTime
//        String beginStr = BEGIN_KEYWORD;
//        String endStr = END_KEYWORD;
//        String collecttimeContent = getLogFileContent(beginStr, endStr);
        return createSimpleIndicatorValue(format(new Date()));
    }

}

