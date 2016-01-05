package com.afunms.alarm.send;


import com.afunms.alarm.model.AlarmWayDetail;
import com.afunms.common.util.SysLogger;
import com.afunms.event.model.CheckEvent;
import com.afunms.event.model.EventList;
import com.afunms.system.dao.UserDao;
import com.afunms.system.model.User;

public class SendSMSAlarm implements SendAlarm{
	
	public void sendAlarm(CheckEvent checkEvent, EventList eventList,AlarmWayDetail alarmWayDetail){
//		SysLogger.info("==============发送短信告警==================");
		//向客户端写告警信息
		String userids = alarmWayDetail.getUserIds();
		if (userids == null || userids.trim().length() == 0) {
		    return;
		}
		String[] ids = alarmWayDetail.getUserIds().split(",");
		if (ids != null && ids.length > 0) {
		    SysLogger.info("==============发送短信告警人数共==================" + ids.length);
		    for (int j = 0; j < ids.length; j++) {

				String oid = ids[j];
				User op = null;
				UserDao userdao = new UserDao();
				try {
					op = (User) userdao.findByID(oid);
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					userdao.close();
				}
				if (op == null) {
					continue;
				}
				boolean result = SendSMSAlarmUtil.sendSMSAlarm(op.getName(), op.getMobile(), eventList.getContent());
				if (result) {
				    SendSMSAlarmUtil.saveSendSmsConfig(op.getName(), op.getMobile(), eventList.getContent());
				}
			}
		}			
	}
	
}
