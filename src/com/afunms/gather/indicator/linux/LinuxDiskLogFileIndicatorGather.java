/*
 * @(#)LinuxDiskLogFileIndicatorGather.java     v1.01, 2014 1 14
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.linux;

import java.util.Calendar;
import java.util.Vector;

import com.afunms.gather.indicator.base.IndicatorValueFactory;
import com.afunms.gather.indicator.base.LogFileIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Diskcollectdata;
import com.afunms.polling.snmp.linux.LinuxLogFileKeywordConstant;

/**
 * ClassName:   LinuxDiskLogFileIndicatorGather.java
 * <p>
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 14 15:45:01
 */
public class LinuxDiskLogFileIndicatorGather extends LogFileIndicatorGather {

    /**
     * BEGIN_KEYWORD:
     * <p>��ʼ�ַ���
     *
     * @since   v1.01
     */
    private static final String BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_DISK_BEGIN_KEYWORD;

    /**
     * END_KEYWORD:
     * <p>�����ַ���
     *
     * @since   v1.01
     */
    private static final String END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_DISK_END_KEYWORD;

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
        String diskContent = getLogFileContent(beginStr, endStr);
        Calendar date = getCalendar();

        Vector<Diskcollectdata> diskVector = new Vector<Diskcollectdata>();
        String[] diskLineArr = diskContent.split("\n");
        for (int i = 1; i < diskLineArr.length; i++) {
            String[] tmpData = diskLineArr[i].split("\\s++");
            if ((tmpData != null) && (tmpData.length == 6)) {
                String allBlockSizeStr = tmpData[1];
                // ���ܴ�С ����������
                String usedSizeStr = tmpData[2];
                // ������ �ַ�������
                String utilizationStr = tmpData[4].substring(0, tmpData[4].indexOf("%"));
                String diskLabel = tmpData[5];

                
                // ������ ����������
                Double utilizationDouble = Double.parseDouble(utilizationStr);

                // ���ܴ�С ����������
                Double allBlockSizeDouble = Double.parseDouble(allBlockSizeStr);
                // �����ܴ�С
                Double allsizeDouble = allBlockSizeDouble / 1024D;
                // �����ܴ�С��λ
                String sizeUnit = "M";
                if (allsizeDouble >= 1024D) {
                    allsizeDouble = allsizeDouble / 1024D;
                    sizeUnit = "G";
                }

                // �����ܴ�С
                Double usedSizeDouble = Double.parseDouble(usedSizeStr);
                // �����ܴ�С��λ
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
                // ������
                diskVector.add(createDiskcollectdata(date, "Utilization", diskLabel, utilizationStr, "%"));
                // �ܿռ��С
                diskVector.add(createDiskcollectdata(date, "AllSize", diskLabel, allSize, sizeUnit));
                // ʹ�ô�С
                diskVector.add(createDiskcollectdata(date, "UsedSize", diskLabel, usedSize, usedUnit));
                // ������
                diskVector.add(createDiskcollectdata(date, "UtilizationInc", diskLabel, utilizationIncString, "%"));

            }
        }
        return createSimpleIndicatorValue(diskVector);
    }

    public Diskcollectdata createDiskcollectdata(Calendar date, String entity, String subentity, String thevalue, String unit) {
        return IndicatorValueFactory.createDiskcollectdata(getIpAddress(), date, entity, subentity, thevalue, unit);
    }
}

