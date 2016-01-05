package com.afunms.biosreport.model;

/**
 * aix 服务器模板
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
	
	// 文件系统利用率
	private String fileSysUsingAvgVal ;
	
	// CPU的平均利用率 和 最大利用率
	private String cpuAvgVal;
	private String cpuMaxVal;
	
	// 物理内存利用率
	private String phyMemAvgVal;
	private String phyMemMaxVal;
	
	// 页面空间利用率
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
