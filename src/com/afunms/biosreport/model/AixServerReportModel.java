package com.afunms.biosreport.model;

/**
 * aix ������ģ��
 * 
 * @author Administrator
 *
 */
public class AixServerReportModel extends BiosReportBaseModel{

	public AixServerReportModel(){
	}
	
	public AixServerReportModel(String ip, String name, String connectivty){
		super(ip,name,connectivty);
	}
	
	// �ļ�ϵͳ������
	private String fileSysUsingAvgVal ;
	
	// CPU��ƽ�������� �� ���������
	private String cpuAvgVal;
	private String cpuMaxVal;
	
	// �����ڴ�������
	private String phyMemAvgVal;
	private String phyMemMaxVal;
	
	// ҳ��ռ�������
	private String pageSpaceAvgVal;
	private String pageSpaceMaxVal;
	
	public String toString(){
		return getClass().getName()
			+ "[IP=" + super.getIpAddress()
			+ ", aliasName=" + super.getAliasName()
			+ ", nodeid=" + super.getNodeid()
			+ ", collecttime=" + super.getCollectTime()
			+ ", dept=" + super.getDeptName()
			+ ", ping=" + super.getAvergeConnectivity()
			+ ", fileSysUsing=" + getFileSysUsingAvgVal()
			+ ", cpuAvgVal=" + getCpuAvgVal()
			+ ", cpuMaxVal=" + getCpuMaxVal()
			+ ", phyMemAvgVal=" + getPhyMemAvgVal()
			+ ", phyMemMaxVal=" + getPhyMemMaxVal()
			+ ", pageSpaceAvgVal=" + getPageSpaceAvgVal()
			+ ", pageSpaceMaxVal=" + getPageSpaceMaxVal()
			+ ", alarmCommon=" + getAlarmCommon()
			+ ", alarmSerious=" + getAlarmSerious()
			+ ", alarmUrgency=" + getAlarmUrgency();
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

	public String getFileSysUsingAvgVal() {
		return fileSysUsingAvgVal;
	}


	public void setFileSysUsingAvgVal(String fileSysUsingAvgVal) {
		this.fileSysUsingAvgVal = get2(fileSysUsingAvgVal);
	}


	public String getPageSpaceAvgVal() {
		return pageSpaceAvgVal;
	}


	public void setPageSpaceAvgVal(String pageSpaceAvgVal) {
		this.pageSpaceAvgVal = get2(pageSpaceAvgVal);
	}


	public String getPageSpaceMaxVal() {
		return pageSpaceMaxVal;
	}


	public void setPageSpaceMaxVal(String pageSpaceMaxVal) {
		this.pageSpaceMaxVal = get2(pageSpaceMaxVal);
	}
		
}
