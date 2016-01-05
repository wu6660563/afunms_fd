package com.afunms.alarm.send;

import com.afunms.alarm.model.AlarmWayDetail;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.model.CheckEvent;
import com.afunms.event.model.EventList;

public class SendPageAlarm implements SendAlarm {
	
	public void sendAlarm(CheckEvent checkEvent, EventList eventList,AlarmWayDetail alarmWayDetail){
		EventListDao eventListDao = new EventListDao();
		try {
			eventListDao.save(eventList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			eventListDao.close();
		}
	}
}
