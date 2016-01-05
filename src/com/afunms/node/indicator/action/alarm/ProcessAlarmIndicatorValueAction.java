/*
 * @(#)ProcessAlarmIndicatorValueAction.java     v1.01, 2014 1 3
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.alarm;

import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.CheckEventUtil;
import com.afunms.node.indicator.action.AlarmIndicatorValueAction;
import com.afunms.polling.om.Processcollectdata;

/**
 * ClassName:   ProcessAlarmIndicatorValueAction.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 3 11:32:59
 */
public class ProcessAlarmIndicatorValueAction extends AlarmIndicatorValueAction {

    private final static String ALARM_INDICATOR_NAME = "process";

    /**
     * executeToAlarm:
     *
     *
     * @since   v1.01
     * @see com.afunms.node.indicator.action.AlarmIndicatorValueAction#executeToAlarm()
     */
    @SuppressWarnings("unchecked")
    @Override
    public void executeToAlarm() {
        Hashtable<String, Object> processHashtable = (Hashtable<String, Object>) getIndicatorValue().getValue();
        Vector<Processcollectdata> vector = (Vector<Processcollectdata>) processHashtable.get("process");

        CheckEventUtil checkutil = new CheckEventUtil();
        checkutil.createProcessGroupEventList(getNode(), vector, getAlarmIndicatorsNode(ALARM_INDICATOR_NAME));
    }

}

