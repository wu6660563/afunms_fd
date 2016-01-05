package com.afunms.alarm.model;

import com.afunms.common.base.BaseVo;

public class AlarmWayDetail extends BaseVo{
	
	private int id;
	
	/**
	 * �澯��ʽid
	 */
	private String alarmWayId;
	
	/**
	 * �澯��ʽ������ �� ҳ�� , ���� , �ʼ� , ���� , �绰 , ...
	 */
	private String alarmCategory;
	
	/**
	 * ���� �� ���� , ����
	 */
	private String dateType;
	
	/**
	 * �������͵Ĵ���
	 */
	private String sendTimes;
	
	/**
	 * ���ͼ��ʱ��
	 */
	private String sendIntervalTimes;
	
	/**
	 * ��ʼ����
	 */
	private String startDate;
	
	/**
	 * ��������
	 */
	private String endDate;
	
	/**
	 * ��ʼʱ��
	 */
	private String startTime;
	
	/**
	 * ����ʱ��
	 */
	private String endTime;
	
	/**
	 * ������ id
	 */
	private String userIds;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the alarmWayId
	 */
	public String getAlarmWayId() {
		return alarmWayId;
	}

	/**
	 * @param alarmWayId the alarmWayId to set
	 */
	public void setAlarmWayId(String alarmWayId) {
		this.alarmWayId = alarmWayId;
	}

	/**
	 * @return the alarmCategory
	 */
	public String getAlarmCategory() {
		return alarmCategory;
	}

	/**
	 * @param alarmCategory the alarmCategory to set
	 */
	public void setAlarmCategory(String alarmCategory) {
		this.alarmCategory = alarmCategory;
	}

	/**
	 * @return the dateType
	 */
	public String getDateType() {
		return dateType;
	}

	/**
	 * @param dateType the dateType to set
	 */
	public void setDateType(String dateType) {
		this.dateType = dateType;
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
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the userIds
	 */
	public String getUserIds() {
		return userIds;
	}

	/**
	 * @param userIds the userIds to set
	 */
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	/**
	 * getSendIntervalTimes:
	 * <p>
	 *
	 * @return  String
	 *          -
	 * @since   v1.01
	 */
	public String getSendIntervalTimes() {
		return sendIntervalTimes;
	}

	/**
	 * setSendIntervalTimes:
	 * <p>
	 *
	 * @param   sendIntervalTimes
	 *          -
	 * @since   v1.01
	 */
	public void setSendIntervalTimes(String sendIntervalTimes) {
		this.sendIntervalTimes = sendIntervalTimes;
	}
	
	

	
	
}
