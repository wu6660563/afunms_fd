package com.afunms.alarm.send;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.afunms.alarm.dao.SendAlarmTimeDao;
import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.model.AlarmWay;
import com.afunms.alarm.model.AlarmWayDetail;
import com.afunms.alarm.model.SendAlarmTime;
import com.afunms.common.util.SysLogger;
import com.afunms.event.model.CheckEvent;


/**
 * 告警次数过滤
 * @author nielin
 *
 */
public class AlarmTimesFilter implements AlarmFilter{
	
	public AlarmFilter alarmFilter = null;
	
	public boolean isSendAlarm(CheckEvent checkEvent, AlarmIndicatorsNode alarmIndicatorsNode , AlarmWay alarmWay , AlarmWayDetail alarmWayDetail){
		boolean result = false;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date currentDate = new Date();
		// 名称
		String name = alarmIndicatorsNode.getId() + "";
		if(checkEvent!=null && checkEvent.getSindex() != null && checkEvent.getSindex().trim().length() > 0){
			name = name + ":" + checkEvent.getSindex();
		}
		SendAlarmTime sendAlarmTime = null;
		try {
			SendAlarmTimeDao sendAlarmTimeDao = null;
			try {
				// 根据 checkEvent 的 name 以及 alarmWayDetail 的 id 查出告警发送时间
				sendAlarmTimeDao = new SendAlarmTimeDao();
				sendAlarmTime = (SendAlarmTime)sendAlarmTimeDao.findByNameAndId(name, alarmWayDetail.getId()+"");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				sendAlarmTimeDao.close();
			}
			if(sendAlarmTime != null){
				String lasSendTime = sendAlarmTime.getLastSendTime();
				Date lastSendTimeDate = simpleDateFormat.parse(lasSendTime);
				if((currentDate.getTime() - lastSendTimeDate.getTime()) / (1000*60*60*24) >= 1){
					// 如果 当前告警时间 超过 最后一次告警时间 一天 则 将发送次数为 0
					sendAlarmTime.setSendTimes("0");
					result = true;
				} else {
					// 否则 比较发送告警次数
					int lastSendTimes = 0;
					int sendTimes = 0;
					try {
						if(sendAlarmTime.getSendTimes()!=null && sendAlarmTime.getSendTimes().trim().length() > 0){
							lastSendTimes = Integer.valueOf(sendAlarmTime.getSendTimes().trim());
						}
						if(alarmWayDetail.getSendTimes()!=null && alarmWayDetail.getSendTimes().trim().length() > 0){
							sendTimes = Integer.valueOf(alarmWayDetail.getSendTimes().trim());
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(lastSendTimes < sendTimes || sendTimes == 0){
						// 如果 最后的告警次数 小于 连续发送的告警次数
						// 如果 当前时间-上一次发送时间 > 时间间隔
						if(currentDate.getTime() - lastSendTimeDate.getTime() > Long.parseLong(alarmWayDetail.getSendIntervalTimes())*60*1000){
							result = true;
						}
					}
				}
			} else {
				sendAlarmTime = new SendAlarmTime();
				sendAlarmTime.setName(name);
				sendAlarmTime.setAlarmWayDetailId(alarmWayDetail.getId()+"");
				sendAlarmTime.setSendTimes("0");
				result = true;
			}
			if(result){
				// 如果发送 则 更新 数据库
				int lastSendTimes = Integer.valueOf(sendAlarmTime.getSendTimes());
				sendAlarmTime.setLastSendTime(simpleDateFormat.format(currentDate));
				sendAlarmTime.setSendTimes(String.valueOf(lastSendTimes+1));
				sendAlarmTimeDao = new SendAlarmTimeDao();
				try {
					sendAlarmTimeDao.deleteByNameAndId(name, alarmWayDetail.getId()+"");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					sendAlarmTimeDao.close();
				}
				sendAlarmTimeDao = new SendAlarmTimeDao();
				try {
					sendAlarmTimeDao.save(sendAlarmTime);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					sendAlarmTimeDao.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}  
		if(!result){
			return result;
		} else if(alarmFilter!= null){
			return alarmFilter.isSendAlarm(checkEvent, alarmIndicatorsNode, alarmWay, alarmWayDetail);
		} 
		
		return result;
		
	}
	
	public void setNextFilter(AlarmFilter alarmFilter){
		this.alarmFilter = alarmFilter;
	}
	
}
