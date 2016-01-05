/*
 * @(#)MemoryAlarmIndicatorValueAction.java     v1.01, 2014 1 3
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.alarm;

import java.util.Vector;

import com.afunms.node.indicator.action.AlarmIndicatorValueAction;
import com.afunms.polling.om.Memorycollectdata;

/**
 * ClassName:   MemoryAlarmIndicatorValueAction.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 3 09:40:33
 */
public class MemoryAlarmIndicatorValueAction extends AlarmIndicatorValueAction {

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
        Vector<Memorycollectdata> vector = (Vector<Memorycollectdata>) getIndicatorValue().getValue();
        if (vector != null && vector.size() > 0) {
            for (Memorycollectdata memorycollectdata : vector) {
                if ("Utilization".equalsIgnoreCase(memorycollectdata.getEntity())) {
                    executeToAlarm(getAlarmIndicatorsNode(getIndicatorName()), vector.get(0).getThevalue());
                }
            }
        }
    
    }

}

