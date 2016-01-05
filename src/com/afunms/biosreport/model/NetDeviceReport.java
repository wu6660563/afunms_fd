package com.afunms.biosreport.model;

import java.text.DecimalFormat;

import com.afunms.discovery.IpAddress;

/**
 * �����豸�ۺϱ�������
 * 
 * @author Administrator
 *
 */
public class NetDeviceReport extends BiosReportBaseModel{

	
	public NetDeviceReport(){	
	}

	/**
	 * �����豸���췽��
	 * 
	 * @param ip IP��ַ
	 * @param alias ����
	 * @param dept ����
	 */
	public NetDeviceReport(String ip,String alias,String connectivty){	
	
		super(ip,alias,connectivty);
	}
	
	public String toString(){
		return getClass().getName()
			+ ":[ip=" + super.getIpAddress()
			+ " ,alias=" + super.getAliasName()
			+ " ,nodeid=" + super.getNodeid()
			+ " ,collecttime" + super.getCollectTime()
			+ " ,dept=" + super.getDeptName()
			+ " , ping=" + super.getAvergeConnectivity()
			+ " , cpuAvg=" + cpuAvgVal
			+ " , cpuMax=" + cpuMaxVal
			+ " , phyMemAvg=" + physicsMemoryAvgVal
			+ " , phyMemMax=" + physicsMemoryMaxVal
			+ " , outFlowAvg=" + outFlowAvgVal
			+ " , outFlowMax=" + outFlowMaxVal
			+ " , outFlowBandWidth=" + outFlowBandWidthRate
			+ " , inFlowAvg=" + inFlowAvgVal
			+ " , inFlowMax=" + inFlowMaxVal
			+ " , inFlowBandWidth=" + inFlowBandWidthRate
			+ " , alarmCommon=" + super.getAlarmCommon()
			+ " , alarmSerious=" + super.getAlarmSerious()
			+ " , alarmUrgency=" + super.getAlarmUrgency();	
	}
	
	private String cpuAvgVal;	// cpu������ƽ��ֵ
	private String cpuMaxVal;   // cpu���������ֵ
	
	private String physicsMemoryAvgVal;	// �����ڴ�ƽ��������
	private String physicsMemoryMaxVal;	// �����ڴ����������
	
	private String outFlowAvgVal;	// ��������ƽ��ֵ
	private String outFlowMaxVal;	// �����������ֵ
	private String outFlowBandWidthRate;	// ���ڴ���ռ����
	
	private String inFlowAvgVal;	// �������ƽ��ֵ
	private String inFlowMaxVal;	// ����������ֵ
	private String inFlowBandWidthRate;	// ��ڴ���ռ����
		
	
	public String getCpuAvgVal() {
		return cpuAvgVal;
	}
	public void setCpuAvgVal(String cpuAvgVal) {
		this.cpuAvgVal = get2(cpuAvgVal);
	}
	public String getCpuMaxVal() {
		return cpuMaxVal;
	}
	public void setCpuMaxVal(String cpuMaxVal) {
		this.cpuMaxVal = get2(cpuMaxVal);
	}
	public String getPhysicsMemoryAvgVal() {
		return physicsMemoryAvgVal;
	}
	public void setPhysicsMemoryAvgVal(String physicsMemoryAvgVal) {
		this.physicsMemoryAvgVal = get2(physicsMemoryAvgVal);
	}
	public String getPhysicsMemoryMaxVal() {
		return physicsMemoryMaxVal;
	}
	public void setPhysicsMemoryMaxVal(String physicsMemoryMaxVal) {
		this.physicsMemoryMaxVal = get2(physicsMemoryMaxVal);
	}
	public String getOutFlowAvgVal() {
		return outFlowAvgVal;
	}
	public void setOutFlowAvgVal(String outFlowAvgVal) {
		this.outFlowAvgVal = get2(outFlowAvgVal);
	}
	public String getOutFlowMaxVal() {
		return outFlowMaxVal;
	}
	public void setOutFlowMaxVal(String outFlowMaxVal) {
		this.outFlowMaxVal = get2(outFlowMaxVal);
	}
	public String getOutFlowBandWidthRate() {
		return outFlowBandWidthRate;
	}
	public void setOutFlowBandWidthRate(String outFlowBandWidthRate) {
		this.outFlowBandWidthRate = get2(outFlowBandWidthRate);
	}
	public String getInFlowAvgVal() {
		return inFlowAvgVal;
	}
	public void setInFlowAvgVal(String inFlowAvgVal) {
		this.inFlowAvgVal = get2(inFlowAvgVal);
	}
	public String getInFlowMaxVal() {
		return inFlowMaxVal;
	}
	public void setInFlowMaxVal(String inFlowMaxVal) {
		this.inFlowMaxVal = get2(inFlowMaxVal);
	}
	public String getInFlowBandWidthRate() {
		return inFlowBandWidthRate;
	}
	public void setInFlowBandWidthRate(String inFlowBandWidthRate) {
		this.inFlowBandWidthRate = get2(inFlowBandWidthRate);
	}
}
