package com.afunms.alarm.model;

import com.afunms.common.base.BaseVo;

public class SendAlarmTime extends BaseVo{
	
	private String name;
	
	/**
	 * �澯��ʽ�����id
	 */
	private String alarmWayDetailId;
	
	/**
	 * �澯���ʹ���
	 */
	private String sendTimes;
	
	/**
	 * ����͸澯��ʱ��
	 */
	private String lastSendTime;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the alarmWayDetailId
	 */
	public String getAlarmWayDetailId() {
		return alarmWayDetailId;
	}

	/**
	 * @param alarmWayDetailId the alarmWayDetailId to set
	 */
	public void setAlarmWayDetailId(String alarmWayDetailId) {
		this.alarmWayDetailId = alarmWayDetailId;
	}

	/**
	 * @return the sendTimes
	 */
	public String getSendTimes() {
		return sendTimes;
	}

	/**
	 * @param sendTimes the sendTimes to set
	 */
	public void setSendTimes(String sendTimes) {
		this.sendTimes = sendTimes;
	}

	/**
	 * @return the lastSendTime
	 */
	public String getLastSendTime() {
		return lastSendTime;
	}

	/**
	 * @param lastSendTime the lastSendTime to set
	 */
	public void setLastSendTime(String lastSendTime) {
		this.lastSendTime = lastSendTime;
	}
	
	
}
