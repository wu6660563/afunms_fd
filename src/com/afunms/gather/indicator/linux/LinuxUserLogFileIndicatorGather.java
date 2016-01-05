/*
 * @(#)LinuxUserLogFileIndicatorGather.java     v1.01, 2014 1 14
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.linux;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.gather.indicator.base.IndicatorValueFactory;
import com.afunms.gather.indicator.base.LogFileIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Usercollectdata;
import com.afunms.polling.snmp.linux.LinuxLogFileKeywordConstant;

/**
 * ClassName:   LinuxUserLogFileIndicatorGather.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 14 20:39:34
 */
public class LinuxUserLogFileIndicatorGather extends LogFileIndicatorGather {


    private final static String LINUX_USERGROUP_BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_USERGROUP_BEGIN_KEYWORD;

    private final static String LINUX_USERGROUP_END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_USERGROUP_END_KEYWORD;

    private final static String LINUX_USER_BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_USER_BEGIN_KEYWORD;

    private final static String LINUX_USER_END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_USER_END_KEYWORD;

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
        String beginStr = LINUX_USERGROUP_BEGIN_KEYWORD;
        String endStr = LINUX_USERGROUP_END_KEYWORD;
        String usergroupContent = getLogFileContent(beginStr, endStr);
        String[] usergroupLineArr = usergroupContent.split("\n");
        Hashtable<String, String> usergroupHashtable = new Hashtable<String, String>();
        for (int i = 0; i < usergroupLineArr.length; i++) {
            String[] usergroup_tmpData = usergroupLineArr[i].split(":");
            if (usergroup_tmpData.length >= 3) {
                usergroupHashtable.put((String) usergroup_tmpData[2],
                        usergroup_tmpData[0]);
            }
        }

        Calendar date = getCalendar();
        beginStr = LINUX_USER_BEGIN_KEYWORD;
        endStr = LINUX_USER_END_KEYWORD;

        Vector<Usercollectdata> vector = new Vector<Usercollectdata>();

        String userContent = getLogFileContent(beginStr, endStr);
        String[] userLineArr = userContent.split("\n");
        for (int i = 0; i < userLineArr.length; i++) {
            String[] tmpData = userLineArr[i].trim().split(":");
            if (tmpData.length > 4) {
                String userName = tmpData[0];
                String userid = tmpData[2];
                String usergroupid = tmpData[3];
                if (userid.length() < 6) {
                    try {
                        if (Integer.parseInt(userid) < 500) {
                            // 小于500的为系统级用户,过滤
                            continue;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                String groupname = "";
                if (usergroupHashtable.containsKey(usergroupid)) {
                    groupname = (String) usergroupHashtable.get(usergroupid);
                }
                vector.addElement(createUsercollectdata(date, "Sysuser", groupname, userName, "", ""));
            }

        }
        return createSimpleIndicatorValue(vector);
    }

    public Usercollectdata createUsercollectdata(Calendar date, String entity, String subentity, String thevalue, String unit, String chname) {
        return IndicatorValueFactory.createUsercollectdata(getIpAddress(), date, entity, subentity, thevalue, unit, chname);
    }
}

