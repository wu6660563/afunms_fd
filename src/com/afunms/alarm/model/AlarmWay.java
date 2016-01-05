package com.afunms.alarm.model;

import com.afunms.common.base.BaseVo;

public class AlarmWay extends BaseVo{
	
	private int id;
	
	/**
	 * ����
	 */
	private String name;
	
	/**
	 * ����
	 */
	private String description;
	
	/**
	 * �Ƿ�ΪĬ�Ϸ�ʽ
	 */
	private String isDefault;
	
	/**
	 * ҳ���Ƿ�澯
	 */
	private String isPageAlarm;
	
	/**
	 * �����Ƿ�澯
	 */
	private String isSoundAlarm;
	
	/**
	 * �绰�Ƿ�澯
	 */
	private String isPhoneAlarm;
	
	/**
	 * �����Ƿ�澯
	 */
	private String isSMSAlarm;
	
	/**
	 * �ʼ��Ƿ�澯
	 */
	private String isMailAlarm;
	/**
	 * �����Ƿ�澯
	 */
	private String isDesktopAlarm;
	
   /**
     * ����̨�Ƿ�澯
     */
    private String isFvsdAlarm;

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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the isDefault
	 */
	public String getIsDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault the isDefault to set
	 */
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * @return the isPageAlarm
	 */
	public String getIsPageAlarm() {
		return isPageAlarm;
	}

	/**
	 * @param isPageAlarm the isPageAlarm to set
	 */
	public void setIsPageAlarm(String isPageAlarm) {
		this.isPageAlarm = isPageAlarm;
	}

	/**
	 * @return the isSoundAlarm
	 */
	public String getIsSoundAlarm() {
		return isSoundAlarm;
	}

	/**
	 * @param isSoundAlarm the isSoundAlarm to set
	 */
	public void setIsSoundAlarm(String isSoundAlarm) {
		this.isSoundAlarm = isSoundAlarm;
	}

	/**
	 * @return the isPhoneAlarm
	 */
	public String getIsPhoneAlarm() {
		return isPhoneAlarm;
	}

	/**
	 * @param isPhoneAlarm the isPhoneAlarm to set
	 */
	public void setIsPhoneAlarm(String isPhoneAlarm) {
		this.isPhoneAlarm = isPhoneAlarm;
	}

	/**
	 * @return the isSMSAlarm
	 */
	public String getIsSMSAlarm() {
		return isSMSAlarm;
	}

	/**
	 * @param isSMSAlarm the isSMSAlarm to set
	 */
	public void setIsSMSAlarm(String isSMSAlarm) {
		this.isSMSAlarm = isSMSAlarm;
	}

	/**
	 * @return the isMailAlarm
	 */
	public String getIsMailAlarm() {
		return isMailAlarm;
	}

	/**
	 * @param isMailAlarm the isMailAlarm to set
	 */
	public void setIsMailAlarm(String isMailAlarm) {
		this.isMailAlarm = isMailAlarm;
	}

	/**
	 * @return the isDesktopAlarm
	 */
	public String getIsDesktopAlarm() {
		return isDesktopAlarm;
	}

	/**
	 * @param isDesktopAlarm the isDesktopAlarm to set
	 */
	public void setIsDesktopAlarm(String isDesktopAlarm) {
		this.isDesktopAlarm = isDesktopAlarm;
	}

    /**
     * @return the isFvsdAlarm
     */
    public String getIsFvsdAlarm() {
        return isFvsdAlarm;
    }

    /**
     * @param isFvsdAlarm the isFvsdAlarm to set
     */
    public void setIsFvsdAlarm(String isFvsdAlarm) {
        this.isFvsdAlarm = isFvsdAlarm;
    }
	

}
