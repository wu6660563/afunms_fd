package com.afunms.event.model;

import com.afunms.common.base.BaseVo;
/**
 * ��Ϊ����ӿ� ���ܵ�Trapʵ����Ϣ
 * 
 * 
 * @author yag
 *
 */
public class IManagerTrap extends BaseVo{

	private 	int 	id;
	private 	String 	name;			// ����
	private		String 	eventName;		// �¼���
	private 	String 	deviceType; 	// �豸����
	private		String	deviceIP;		// �豸IP
	private 	String 	elementInstance;	// Ԫ��ʵ��
	private 	String  networkManagementType;		// �����������
	private		String	alarmCreateTime;			// �澯����ʱ��
	private		String	reasonOfCausingAlarm;		// ����澯��ԭ��
	private		String	alarmLevel;					// �澯�ȼ�
	private		String	alarmInfo;			// �澯��Ϣ������ID,��ϸ��Ϣ
	private		String	alarmAdditionalInfo;	// �澯������Ϣ
	private		String	alarmFlag;			// �澯��ʶ����ʶ�澯��һ���¼���������Ϣ���ǹ��ϻָ���Ϣ
	private		String	alarmFunctionType;	// �澯Դ���ͣ���������Ϣ֮һ������Դ��,�������������źš������̵�������
											// ��Ӳ�������������������ת������ͨѶ������������������δ֪���͡���
	private		String	alarmNumber;		// �澯���к�
	private		String	adviceOfReparingAlarm;	// ����澯����
	private		String	resourceID;				// ��Դ��

	private 	String 	collecttime;			// �ɼ�ʱ��
	
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
