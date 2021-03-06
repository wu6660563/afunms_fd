/*
 * @(#)LinuxUNameLogFileIndicatorGather.java     v1.01, 2014 1 14
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
 * ClassName:   LinuxUNameLogFileIndicatorGather.java
 * <p>
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 14 20:28:23
 */
public class LinuxUNameLogFileIndicatorGather extends LogFileIndicatorGather {

    /**
     * BEGIN_KEYWORD:
     * <p>��ʼ�ַ���
     *
     * @since   v1.01
     */
    private static final String BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_UNAME_BEGIN_KEYWORD;

    /**
     * END_KEYWORD:
     * <p>�����ַ���
     *
     * @since   v1.01
     */
    private static final String END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_UNAME_END_KEYWORD;

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
        String unameContent = getLogFileContent(beginStr, endStr);
        Calendar date = getCalendar();

        Vector<Systemcollectdata> vector = new Vector<Systemcollectdata>();
        String[] unameLineArr = unameContent.split("\n");
        for (int i = 0; i < unameLineArr.length; i++) {
            String[] uname_tmpData = unameLineArr[i].split("\\s++");
            if (uname_tmpData.length == 2) {
                String operatSystem = uname_tmpData[0];
                String sysName = uname_tmpData[1];

                vector.addElement(createSystemcollectdata(date, "operatSystem", "operatSystem", operatSystem, "", ""));

                vector.addElement(createSystemcollectdata(date, "SysName", "SysName", sysName, "", ""));

            }
        }
        return createSimpleIndicatorValue(vector);
    }

    public Systemcollectdata createSystemcollectdata(Calendar date, String entity, String subentity, String thevalue, String unit, String chname) {
        return IndicatorValueFactory.createSystemcollectdata(getIpAddress(), date, entity, subentity, thevalue, unit, chname);
    }
}

