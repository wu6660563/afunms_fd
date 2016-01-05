/*
 * @(#)AlarmAction.java     v1.01, 2013 12 20
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.synchronize.service;

import com.afunms.alarm.model.AlarmWayDetail;
import com.afunms.alarm.send.SendAlarmDispatcher;
import com.afunms.event.model.CheckEvent;
import com.afunms.event.model.EventList;
import com.afunms.rmi.service.RMIParameter;

/**
 * ClassName:   AlarmAction.java
 * <p>告警处理
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 20 10:53:39
 */
public class AlarmAction implements DataAction {

    /**
     * action:
     *
     * @param parameter
     *
     * @since   v1.01
     * @see com.afunms.synchronize.service.DataAction#action(com.afunms.rmi.service.RMIParameter)
     */
    public void action(RMIParameter parameter) {
        try {
            CheckEvent checkEvent = (CheckEvent) parameter.getParameter("checkEvent");
            EventList eventList = (EventList) parameter.getParameter("eventList");
            AlarmWayDetail alarmWayDetail = (AlarmWayDetail) parameter.getParameter("alarmWayDetail");
            
            SendAlarmDispatcher.sendAlarm(checkEvent, eventList, alarmWayDetail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

