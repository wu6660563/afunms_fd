/*
 * @(#)ServiceAlarmIndicatorValueAction.java     v1.01, 2014 1 3
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.alarm;

import java.util.Vector;

import com.afunms.common.util.CheckEventUtil;
import com.afunms.node.indicator.action.AlarmIndicatorValueAction;
import com.afunms.polling.om.Servicecollectdata;

/**
 * ClassName:   ServiceAlarmIndicatorValueAction.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 3 11:39:08
 */
public class ServiceAlarmIndicatorValueAction extends AlarmIndicatorValueAction {

    private final static String ALARM_INDICATOR_NAME = "service";

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
        Vector<Servicecollectdata> vector = (Vector<Servicecollectdata>) getIndicatorValue().getValue();

        CheckEventUtil checkutil = new CheckEventUtil();
        checkutil.createHostServiceGroupEventList(getNode(), vector, getAlarmIndicatorsNode(ALARM_INDICATOR_NAME));
    
    }

}

