package com.afunms.alarm.send;


import java.util.Calendar;

import com.afunms.alarm.model.AlarmWayDetail;
import com.afunms.event.dao.AlarmInfoDao;
import com.afunms.event.model.AlarmInfo;
import com.afunms.event.model.CheckEvent;
import com.afunms.event.model.EventList;

public class SendSoundAlarm implements SendAlarm{

	public void sendAlarm(CheckEvent checkEvent, EventList eventList,AlarmWayDetail alarmWayDetail) {
	    // 默认情况下,都是向向声音告警表里写数据
        // 向声音告警表里写数据
        AlarmInfo alarminfo = new AlarmInfo();
        alarminfo.setContent(eventList.getContent());
        alarminfo.setIpaddress(eventList.getEventlocation());
        alarminfo.setLevel1(new Integer(2));
        alarminfo.setRecordtime(Calendar.getInstance());
        alarminfo.setType("");
        AlarmInfoDao alarmdao = new AlarmInfoDao();
        try {
            alarmdao.save(alarminfo);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            alarmdao.close();
        }
	}

}
