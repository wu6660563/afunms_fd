package com.afunms.biosreport.model;

/**
 * windows������ģ��
 * 
 * @author Administrator
 *
 */
public class WindowsServerReportModel extends BiosReportBaseModel{

	public WindowsServerReportModel(){
	}
	
	public WindowsServerReportModel(String ip,String name,String connectivty){
		super(ip, name,connectivty);
	}
	
	// ���̿ռ�������
	private String diskSpaceUsingAvgVal ;
	
	// CPU��ƽ�������� �� ���������
	private String cpuAvgVal;
	private String cpuMaxVal;
	
	// �����ڴ�������
	private String phyMemAvgVal;
	private String phyMemMaxVal;
	
	// �����ڴ�������
	private String virMemAvgVal;
	private String virMemMaxVal;
	
	public String toString(){
		return getClass().getName()
			+ "[IP=" + super.getIpAddress()
			+ ", aliasName=" + super.getAliasName()
			+ ", nodeid=" + super.getNodeid()
			+ ", collecttime=" + super.getCollectTime()
			+ ", dept=" + super.getDeptName()
			+ ", ping=" + super.getAvergeConnectivity()
			+ ", diskSpaceUsing=" + getDiskSpaceUsingAvgVal()
			+ ", cpuAvgVal=" + getCpuAvgVal()
			+ ", cpuMaxVal=" + getCpuMaxVal()
			+ ", phyMemAvgVal=" + getPhyMemAvgVal()
			+ ", phyMemMaxVal=" + getPhyMemMaxVal()
			+ ", virMemAvgVal=" + getVirMemAvgVal()
			+ ", virMemMaxVal=" + getVirMemMaxVal()
			+ ", alarmCommon=" + getAlarmCommon()
			+ ", alarmSerious=" + getAlarmSerious()
			+ ", alarmUrgency=" + getAlarmUrgency();
	}
	
	public String getDiskSpaceUsingAvgVal() {
		return diskSpaceUsingAvgVal;
	}
	public void setDiskSpaceUsingAvgVal(String diskSpaceUsingAvgVal) {
		this.diskSpaceUsingAvgVal = get2(diskSpaceUsingAvgVal);
	}
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
	public String getPhyMemAvgVal() {
		return phyMemAvgVal;
	}
	public void setPhyMemAvgVal(String phyMemAvgVal) {
		this.phyMemAvgVal = get2(phyMemAvgVal);
	}
	public String getPhyMemMaxVal() {
		return phyMemMaxVal;
	}
	public void setPhyMemMaxVal(String phyMemMaxVal) {
		this.phyMemMaxVal = get2(phyMemMaxVal);
	}
	public String getVirMemAvgVal() {
		return virMemAvgVal;
	}
	public void setVirMemAvgVal(String virMemAvgVal) {
		this.virMemAvgVal = get2(virMemAvgVal);
	}
	public String getVirMemMaxVal() {
		return virMemMaxVal;
	}
	public void setVirMemMaxVal(String virMemMaxVal) {
		this.virMemMaxVal = get2(virMemMaxVal);
	}
}
