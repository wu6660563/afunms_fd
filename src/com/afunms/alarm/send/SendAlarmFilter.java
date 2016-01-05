package com.afunms.alarm.send;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.model.AlarmWay;
import com.afunms.alarm.model.AlarmWayDetail;
import com.afunms.event.model.CheckEvent;

/**
 * �澯���͹����� �����жϸ澯�Ƿ���
 * ���жϸ澯���͵ķ�ʽ������ �� Ȼ���ж����� �� ���ж�ʱ��
 * @author nielin
 * @date 2010-10-22
 *
 */
public class SendAlarmFilter {
	
	public boolean isSendAlarm(CheckEvent checkEvent ,AlarmIndicatorsNode alarmIndicatorsNode , AlarmWay alarmWay , AlarmWayDetail alarmWayDetail){
		AlarmTimesFilter alarmTimesFilter = new AlarmTimesFilter();
		AlarmCatgoryFilter alarmCatgoryFilter = new AlarmCatgoryFilter();
		AlarmDateFilter alarmDateFilter = new AlarmDateFilter();
		AlarmTimeFilter alarmTimeFilter = new AlarmTimeFilter();
		alarmTimesFilter.setNextFilter(alarmCatgoryFilter);
		alarmCatgoryFilter.setNextFilter(alarmDateFilter);
		alarmDateFilter.setNextFilter(alarmTimeFilter);
		return alarmTimesFilter.isSendAlarm(checkEvent ,alarmIndicatorsNode , alarmWay, alarmWayDetail);
	}
}
