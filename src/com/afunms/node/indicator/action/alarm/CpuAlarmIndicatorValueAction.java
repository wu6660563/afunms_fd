/*
 * @(#)CpuAlarmIndicatorValueAction.java     v1.01, 2014 1 3
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.alarm;

import java.util.Vector;

import com.afunms.node.indicator.action.AlarmIndicatorValueAction;
import com.afunms.polling.om.CPUcollectdata;

/**
 * ClassName:   CpuAlarmIndicatorValueAction.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 3 09:17:36
 */
public class CpuAlarmIndicatorValueAction extends AlarmIndicatorValueAction {

    private final static String ALARM_INDICATOR_NAME = "cpu";

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
        Vector<CPUcollectdata> vector = (Vector<CPUcollectdata>) getIndicatorValue().getValue();
        if (vector != null && vector.size() > 0) {
            executeToAlarm(ALARM_INDICATOR_NAME, vector.get(0).getThevalue());
        }
    }

}

