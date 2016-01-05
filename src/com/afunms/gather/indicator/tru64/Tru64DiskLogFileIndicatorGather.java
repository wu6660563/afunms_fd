/*
 * @(#)Tru64DiskLogFileIndicatorGather.java     v1.01, 2014 1 15
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.tru64;

import java.util.Calendar;
import java.util.Vector;

import com.afunms.gather.indicator.base.IndicatorValueFactory;
import com.afunms.gather.indicator.base.LogFileIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Diskcollectdata;
import com.afunms.polling.snmp.tru64.Tru64LogFileKeywordConstant;

/**
 * ClassName:   Tru64DiskLogFileIndicatorGather.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 15 16:36:49
 */
public class Tru64DiskLogFileIndicatorGather extends LogFileIndicatorGather {

    /**
     * BEGIN_KEYWORD:
     * <p>开始字符串
     *
     * @since   v1.01
     */
    private static final String BEGIN_KEYWORD = Tru64LogFileKeywordConstant.TRU64_DISK_BEGIN_KEYWORD;

    /**
     * END_KEYWORD:
     * <p>结束字符串
     *
     * @since   v1.01
     */
    private static final String END_KEYWORD = Tru64LogFileKeywordConstant.TRU64_DISK_END_KEYWORD;

    /**
     * getSimpleIndicatorValue:
     *
     * @return
     *
     * @since   v1.01
     * @see com.afunms.gather.indicator.base.BaseIndicatorGather#getSimpleIndicatorValue()
     */
    @SuppressWarnings("unchecked")
    @Override
    public SimpleIndicatorValue getSimpleIndicatorValue() {
        String beginStr = BEGIN_KEYWORD;
        String endStr = END_KEYWORD;
        Vector<Diskcollectdata> diskVector = new Vector<Diskcollectdata>();
        try {
            String diskContent = getLogFileContent(beginStr, endStr);
            String[] diskLineArr = diskContent.trim().split("\n");
            Calendar date = getCalendar();
            
            for (int i = 1; i < diskLineArr.length; i++) {
                String[] tmpData = diskLineArr[i].split("\\s++");
                String allBlockSizeStr = tmpData[1];
                // 块总大小 浮点行类型
                String usedSizeStr = tmpData[2];
                // 利用率 字符串类型
                String utilizationStr = tmpData[4].substring(0, tmpData[4].indexOf("%"));
                String diskLabel = tmpData[5];

                
                // 利用率 浮点型类型
                Double utilizationDouble = Double.parseDouble(utilizationStr);

                // 块总大小 浮点行类型
                Double allBlockSizeDouble = Double.parseDouble(allBlockSizeStr);
                // 磁盘总大小
                Double allsizeDouble = allBlockSizeDouble / 1024D;
                // 磁盘总大小单位
                String sizeUnit = "M";
                if (allsizeDouble >= 1024D) {
                    allsizeDouble = allsizeDouble / 1024D;
                    sizeUnit = "G";
                }

                // 磁盘总大小
                Double usedSizeDouble = Double.parseDouble(usedSizeStr);
                // 磁盘总大小单位
                String usedUnit = "M";
                usedSizeDouble = usedSizeDouble / 1024;
                if (usedSizeDouble >= 1024.0f) {
                    usedSizeDouble = usedSizeDouble / 1024;
                    usedUnit = "G";
                }
                
                Double utilizationInc = 0D;
                if (getLastSimpleIndicatorValue() != null) {
                    Vector<Diskcollectdata> lastVector = (Vector<Diskcollectdata>) getLastSimpleIndicatorValue().getValue();
                    for (Diskcollectdata diskcollectdata : lastVector) {
                        if ("Utilization".equalsIgnoreCase(diskcollectdata.getEntity())
                                        && diskLabel.equalsIgnoreCase(diskcollectdata.getSubentity())) {
                            utilizationInc = utilizationDouble - Double.valueOf(diskcollectdata.getThevalue());
                        }
                    }
                }
                
                String allSize = String.valueOf(format(allsizeDouble));
                String usedSize = String.valueOf(format(usedSizeDouble));
                String utilizationIncString = String.valueOf(format(utilizationInc));
                // 利用率
                diskVector.add(createDiskcollectdata(date, "Utilization", diskLabel, utilizationStr, "%"));
                // 总空间大小
                diskVector.add(createDiskcollectdata(date, "AllSize", diskLabel, allSize, sizeUnit));
                // 使用大小
                diskVector.add(createDiskcollectdata(date, "UsedSize", diskLabel, usedSize, usedUnit));
                // 增长率
                diskVector.add(createDiskcollectdata(date, "UtilizationInc", diskLabel, utilizationIncString, "%"));

            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return createSimpleIndicatorValue(diskVector);
    }

    public Diskcollectdata createDiskcollectdata(Calendar date, String entity, String subentity, String thevalue, String unit) {
        return IndicatorValueFactory.createDiskcollectdata(getIpAddress(), date, entity, subentity, thevalue, unit);
    }
}

