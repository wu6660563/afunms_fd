/*
 * @(#)PingAlarmIndicatorValueAction.java     v1.01, 2014 1 3
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.alarm;

import java.util.Vector;

import com.afunms.node.indicator.action.AlarmIndicatorValueAction;
import com.afunms.polling.om.Pingcollectdata;

/**
 * ClassName:   PingAlarmIndicatorValueAction.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 3 11:22:21
 */
public class PingAlarmIndicatorValueAction extends AlarmIndicatorValueAction {

    private final static String ALARM_INDICATOR_NAME_PING = "ping";

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
        Vector<Pingcollectdata> vector = (Vector<Pingcollectdata>) getIndicatorValue().getValue();
        if (vector != null && vector.size() > 0) {
            for (Pingcollectdata pingcollectdata : vector) {
                if ("Utilization".equalsIgnoreCase(pingcollectdata.getEntity())) {
                    executeToAlarm(ALARM_INDICATOR_NAME_PING, pingcollectdata.getThevalue());
                }
            }
        }
    }

}

