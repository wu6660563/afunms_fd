package com.afunms.portscan.model;

import com.afunms.common.base.BaseVo;

public class PortConfig extends BaseVo{
	
	private int id;
	
	/**
	 * �˿�������ip
	 */
	private String ipaddress;
	
	/**
	 * �˿ں�
	 */
	private String port;
	
	/**
	 * �˿���
	 */
	private String portName;
	
	/**
	 * �˿�����
	 */
	private String description;
	
	/**
	 * �˿�����
	 */
	private String type;
	
	/**
	 * ��ʱʱ��
	 */
	private String timeout;
	
	/**
	 * �˿�״̬
	 * "0" δ����
	 * "1" �ѿ���
	 */
	private String status;
	
	/**
	 * ɨ��״̬
	 * "0" δɨ��
	 * "1" ��ɨ��
	 */
	private String isScanned;
	
	/**
	 * ��һ��ɨ��ʱ��
	 */
	private String scantime;

	/**
	 * 
	 */
	public PortConfig() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param ipaddress
	 * @param port
	 * @param portName
	 * @param description
	 * @param type
	 * @param timeout
	 * @param status
	 * @param isScanned
	 * @param scantime
	 */
	public PortConfig(int id, String ipaddress, String port, String portName,
			String description, String type, String timeout, String status,
			String isScanned, String scantime) {
		this.id = id;
		this.ipaddress = ipaddress;
		this.port = port;
		this.portName = portName;
		this.description = description;
		this.type = type;
		this.timeout = timeout;
		this.status = status;
		this.isScanned = isScanned;
		this.scantime = scantime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsScanned() {
		return isScanned;
	}

	public void setIsScanned(String isScanned) {
		this.isScanned = isScanned;
	}

	public String getScantime() {
		return scantime;
	}

	public void setScantime(String scantime) {
		this.scantime = scantime;
	}

	
	
}
