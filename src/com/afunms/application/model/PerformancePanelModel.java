package com.afunms.application.model;

import com.afunms.common.base.BaseVo;

/**
 * @author HONGLI E-mail: ty564457881@163.com
 * @version ����ʱ�䣺Aug 16, 2011 3:26:49 PM
 * ��˵�� :�������ģ����
 */
public class PerformancePanelModel extends BaseVo{
	private String id;
	/**
	 * �����������
	 */
	private String name;
	
	/**
	 * ������豸���� 
	 */
	private String deviceType;
	
	/**
	 * �豸ID
	 */
	private String deviceId;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}

