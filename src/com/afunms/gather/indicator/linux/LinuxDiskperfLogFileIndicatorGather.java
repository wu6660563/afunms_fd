/*
 * @(#)LinuxDiskperfLogFileIndicatorGather.java     v1.01, 2014 1 14
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
 * ClassName:   LinuxDiskperfLogFileIndicatorGather.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 14 16:06:40
 */
public class LinuxDiskperfLogFileIndicatorGather extends LogFileIndicatorGather {

    /**
     * BEGIN_KEYWORD:
     * <p>开始字符串
     *
     * @since   v1.01
     */
    private static final String BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_DISKPERF_BEGIN_KEYWORD;

    /**
     * END_KEYWORD:
     * <p>结束字符串
     *
     * @since   v1.01
     */
    private static final String END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_DISKPERF_END_KEYWORD;
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
        String diskperfContent = getLogFileContent(beginStr, endStr);

        String[] diskperfLineArr = diskperfContent.split("\n");
        String[] diskperf_tmpData = null;
        List<Hashtable<String,String>> alldiskperf = new ArrayList<Hashtable<String,String>>();
        
        for(int i=0; i<diskperfLineArr.length;i++){
            diskperf_tmpData = diskperfLineArr[i].trim().split("\\s++");
            if(diskperf_tmpData != null && diskperf_tmpData.length==10){
                if("Average:".equalsIgnoreCase(diskperf_tmpData[0].trim())
                        || "平均时间:".equalsIgnoreCase(diskperf_tmpData[0].trim())){
                    if(diskperf_tmpData[1].trim().equalsIgnoreCase("DEV")){
                        //处理第一行标题
                        continue;
                    }else{
                        Hashtable<String,String> diskperfhash = new Hashtable<String,String>();
                        diskperfhash.put("tps", diskperf_tmpData[2].trim());
                        diskperfhash.put("rd_sec/s", diskperf_tmpData[3].trim());
                        diskperfhash.put("wr_sec/s", diskperf_tmpData[4].trim());
                        diskperfhash.put("avgrq-sz", diskperf_tmpData[5].trim());
                        diskperfhash.put("avgqu-sz", diskperf_tmpData[6].trim());
                        diskperfhash.put("await", diskperf_tmpData[7].trim());
                        diskperfhash.put("svctm", diskperf_tmpData[8].trim());
                        diskperfhash.put("%util", diskperf_tmpData[9].trim());
                        diskperfhash.put("%busy",Math.round(Float.parseFloat(diskperf_tmpData[8].trim())*100/(Float.parseFloat(diskperf_tmpData[7].trim())+Float.parseFloat(diskperf_tmpData[8].trim())))+"");
                        diskperfhash.put("disklebel", diskperf_tmpData[1].trim());
                        alldiskperf.add(diskperfhash);
                    }
                }
            }
        }
        
        return createSimpleIndicatorValue(alldiskperf);
    }

}

