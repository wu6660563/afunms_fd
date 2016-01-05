package com.afunms.event.model;

import com.afunms.common.base.BaseVo;
/**
 * 华为北向接口 接受的Trap实体信息
 * 
 * 
 * @author yag
 *
 */
public class IManagerTrap extends BaseVo{

	private 	int 	id;
	private 	String 	name;			// 名称
	private		String 	eventName;		// 事件名
	private 	String 	deviceType; 	// 设备类型
	private		String	deviceIP;		// 设备IP
	private 	String 	elementInstance;	// 元素实例
	private 	String  networkManagementType;		// 网络管理类型
	private		String	alarmCreateTime;			// 告警产生时间
	private		String	reasonOfCausingAlarm;		// 引起告警的原因
	private		String	alarmLevel;					// 告警等级
	private		String	alarmInfo;			// 告警信息，包括ID,详细信息
	private		String	alarmAdditionalInfo;	// 告警额外信息
	private		String	alarmFlag;			// 告警标识。标识告警是一个事件，故障信息还是故障恢复信息
	private		String	alarmFunctionType;	// 告警源类型：是下列信息之一：“电源”,“环境”，“信号”，“继电器”，
											// “硬件”，“软件”，“运转”，“通讯”，“处理器”，“未知类型”。
	private		String	alarmNumber;		// 告警序列号
	private		String	adviceOfReparingAlarm;	// 处理告警建议
	private		String	resourceID;				// 资源号

	private 	String 	collecttime;			// 采集时间
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		
		this.name = name;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceIP() {
		return deviceIP;
	}
	public void setDeviceIP(String deviceIP) {
		this.deviceIP = deviceIP;
	}
	public String getElementInstance() {
		return elementInstance;
	}
	public void setElementInstance(String elementInstance) {
		this.elementInstance = elementInstance;
	}
	public String getNetworkManagementType() {
		return networkManagementType;
	}
	public void setNetworkManagementType(String networkManagementType) {
		this.networkManagementType = networkManagementType;
	}
	public String getAlarmCreateTime() {
		return alarmCreateTime;
	}
	public void setAlarmCreateTime(String alarmCreateTime) {
		this.alarmCreateTime = alarmCreateTime;
	}
	public String getReasonOfCausingAlarm() {
		return reasonOfCausingAlarm;
	}
	public void setReasonOfCausingAlarm(String reasonOfCausingAlarm) {
		this.reasonOfCausingAlarm = reasonOfCausingAlarm;
	}
	public String getAlarmLevel() {
		return alarmLevel;
	}
	public void setAlarmLevel(String alarmLevel) {
		this.alarmLevel = alarmLevel;
	}
	public String getAlarmInfo() {
		return alarmInfo;
	}
	public void setAlarmInfo(String alarmInfo) {
		this.alarmInfo = alarmInfo;
	}
	public String getAlarmAdditionalInfo() {
		return alarmAdditionalInfo;
	}
	public void setAlarmAdditionalInfo(String alarmAdditionalInfo) {
		this.alarmAdditionalInfo = alarmAdditionalInfo;
	}
	public String getAlarmFlag() {
		return alarmFlag;
	}
	public void setAlarmFlag(String alarmFlag) {
		this.alarmFlag = alarmFlag;
	}
	public String getAlarmFunctionType() {
		return alarmFunctionType;
	}
	public void setAlarmFunctionType(String alarmFunctionType) {
		this.alarmFunctionType = alarmFunctionType;
	}
	public String getAlarmNumber() {
		return alarmNumber;
	}
	public void setAlarmNumber(String alarmNumber) {
		this.alarmNumber = alarmNumber;
	}
	public String getAdviceOfReparingAlarm() {
		return adviceOfReparingAlarm;
	}
	public void setAdviceOfReparingAlarm(String adviceOfReparingAlarm) {
		this.adviceOfReparingAlarm = adviceOfReparingAlarm;
	}
	public String getResourceID() {
		return resourceID;
	}
	public void setResourceID(String resourceID) {
		this.resourceID = resourceID;
	}
	
	public String getCollecttime() {
		return collecttime;
	}
	public void setCollecttime(String collecttime) {
		this.collecttime = collecttime;
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getName() + ":[name=");
		sb.append(this.getName() + ", eventName=" + this.getEventName() + ",deviceType=");
		sb.append(this.getDeviceType()+",deviceIP=" + this.getDeviceIP() + ", elementInstance=" + this.getElementInstance());
		sb.append(",newworkManagementType=" + this.getNetworkManagementType() + ",alarmCreateTime=" + this.getAlarmCreateTime());
		sb.append(",reasonOfCausingAlarm=" + this.getReasonOfCausingAlarm() + ",alarmLevel=" + this.getAlarmLevel());
		sb.append(",alarmInfo=" + this.getAlarmInfo() + ",alarmAdditionalInfo=" + this.getAlarmAdditionalInfo());
		sb.append(",alarmFlag=" + this.getAlarmFlag() + ",alarmFunctionType=" + this.getAlarmFunctionType());
		sb.append(",alarmNumber=" + this.getAlarmNumber() + ",adviceOfReparingAlarm=" + this.getAdviceOfReparingAlarm());
		sb.append(",resourceID=" + this.getResourceID()+",collecttime="+ this.getCollecttime() + "]");
		return sb.toString();
	}
	
	
}
