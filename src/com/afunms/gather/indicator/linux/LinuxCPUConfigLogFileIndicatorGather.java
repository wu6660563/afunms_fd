/*
 * @(#)LinuxCPUConfigLogFileIndicatorGather.java     v1.01, 2014 1 14
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.linux;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.afunms.config.model.Nodecpuconfig;
import com.afunms.gather.indicator.base.LogFileIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.snmp.linux.LinuxLogFileKeywordConstant;

/**
 * ClassName:   LinuxCPUConfigLogFileIndicatorGather.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 14 15:31:49
 */
public class LinuxCPUConfigLogFileIndicatorGather extends
                LogFileIndicatorGather {

    /**
     * BEGIN_KEYWORD:
     * <p>开始字符串
     *
     * @since   v1.01
     */
    private static final String BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_CPUCONFIG_BEGIN_KEYWORD;


    /**
     * END_KEYWORD:
     * <p>结束字符串
     *
     * @since   v1.01
     */
    private static final String END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_CPUCONFIG_END_KEYWORD;

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
        String cpuconfigContent = getLogFileContent(beginStr, endStr);
        int nodeid = Integer.valueOf(getNodeId());
        String[] cpuconfigLineArr = cpuconfigContent.split("\n");

        List<Nodecpuconfig> cpuconfiglist = new ArrayList<Nodecpuconfig>();
        String procesors = "";
        Nodecpuconfig nodecpuconfig = new Nodecpuconfig();
        
        int procesorsnum = 0;
        try {
            for (int i = 0; i < cpuconfigLineArr.length; i++) {
                String[] tmpData = cpuconfigLineArr[i].trim().split(":");
                if (tmpData != null && tmpData.length > 0) {
                    String key = tmpData[0].trim();
                    String value = tmpData[1].trim();
                    if ("processor".equalsIgnoreCase(key)) {
                        nodecpuconfig = new Nodecpuconfig();
                        nodecpuconfig.setNodeid(nodeid);
                        nodecpuconfig.setProcessorId(value);
                        procesors = value;
                    } else if ("model name".equalsIgnoreCase(key)) {
                        nodecpuconfig.setName(value);
                    } else if ("cpu MHz".equalsIgnoreCase(key)) {
                        nodecpuconfig.setProcessorSpeed(value);
                    } else if ("cache size".equalsIgnoreCase(key)) {
                        nodecpuconfig.setL2CacheSize(value);
                        cpuconfiglist.add(nodecpuconfig);
                    }
                }
            }

            // 计算节点的CPU配置个数
            if (procesors != null && procesors.trim().length() > 0) {
                try {
                    procesorsnum = Integer.parseInt(procesors) + 1;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        

        Hashtable<String, Object> CPUConfigHashtable = new Hashtable<String, Object>();
        CPUConfigHashtable.put("cpuconfiglist", cpuconfiglist);
        CPUConfigHashtable.put("procesorsnum", String.valueOf(procesorsnum));

        return createSimpleIndicatorValue(CPUConfigHashtable);
    }

}

