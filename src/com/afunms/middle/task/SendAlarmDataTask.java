/*
 * @(#)AlarmDataTask.java     v1.01, Dec 3, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.task;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;

import net.sf.json.JSONObject;

import com.afunms.event.dao.CheckEventDao;
import com.afunms.event.model.CheckEvent;
import com.afunms.middle.model.AlarmData;
import com.afunms.middle.model.HeaderData;
import com.afunms.middle.model.SendData;
import com.afunms.middle.util.MQSend;

/**
 * ClassName: AlarmDataTask.java
 * <p>
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Dec 3, 2013 7:30:06 PM
 * @mail wupinlong@dhcc.com.cn
 */
public class SendAlarmDataTask extends TimerTask {

	private static java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	@Override
	public void run() {
		CheckEventDao dao = new CheckEventDao();
		List<CheckEvent> eventList = null;
		try {
			eventList = dao.loadAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}

		SendData<AlarmData> sendData = new SendData<AlarmData>();
		Set<AlarmData> set = new HashSet<AlarmData>();
		try {
			if (eventList != null && eventList.size() > 0) {
				for (CheckEvent eventVo : eventList) {
					AlarmData alarmData = new AlarmData();
					alarmData.setId(1L);
					Date date = sdf.parse(eventVo.getCollecttime());
					alarmData.setTime(date.getTime());
					alarmData.setType("performance");
					alarmData.setLevel(eventVo.getAlarmlevel());
					alarmData.setObject_gid("");
					alarmData.setObject_name("");
					alarmData.setSub_type(eventVo.getName());
					alarmData.setDevice_gid("");
					alarmData.setStatus(0);
					alarmData.setHandle_time(0L);
					alarmData.setTitle(eventVo.getContent());
					alarmData.setContent(eventVo.getContent());

					set.add(alarmData);
				}

				sendData.setBody(set);
				HeaderData header = new HeaderData();
				header.setSystype(4); // 地调系统
				header.setSysname("ForceView");
				header.setSysdomain(4);
				header.setMsgtype(3);
				sendData.setHeader(header);

				JSONObject jsonObject = JSONObject.fromObject(sendData);
				String objToString = jsonObject.toString();

				MQSend mqSend = new MQSend("topic_fs_donghua_all_nr");
				try {
					mqSend.sendMessage(objToString.getBytes());
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					mqSend.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
