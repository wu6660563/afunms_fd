package com.afunms.alarm.send;

import com.afunms.alarm.model.AlarmWayDetail;
import com.afunms.event.model.CheckEvent;
import com.afunms.event.model.EventList;

public interface SendAlarm {
    
	public void sendAlarm(CheckEvent checkEvent, EventList eventList,AlarmWayDetail alarmWayDetail);
}
