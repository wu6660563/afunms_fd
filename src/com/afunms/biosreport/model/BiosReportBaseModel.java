package com.afunms.biosreport.model;

import java.text.DecimalFormat;

/**
 * 皕杰报表模板基类
 * 
 * @author Administrator
 *
 */
public class BiosReportBaseModel {

	public BiosReportBaseModel(){
	}
	
	public BiosReportBaseModel(String ip,String alias, String connectivety){
		ipAddress = ip;
		aliasName = alias;
		avergeConnectivity = get2(connectivety);
	}
	
	private String alarmCommon; // 普通告警数
	private String alarmSerious; // 严重告警数
	private String alarmUrgency; // 紧急告警数
	
	private String ipAddress; // IP
	private String aliasName; // 别名
	private String deptName; // 部门
	
	private String avergeConnectivity;	// 平均连通率
	
	private String collectTime;		// 时间
	private String nodeid;			// 设备id号
	
	public String getAlarmCommon() {
		return alarmCommon;
	}
	public void setAlarmCommon(String alarmCommon) {
		this.alarmCommon = get2(alarmCommon);
	}
	public String getAlarmSerious() {
		return alarmSerious;
	}
	public void setAlarmSerious(String alarmSerious) {
		this.alarmSerious = get2(alarmSerious);
	}
	public String getAlarmUrgency() {
		return alarmUrgency;
	}
	public void setAlarmUrgency(String alarmUrgency) {
		this.alarmUrgency = get2(alarmUrgency);
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getAliasName() {
		return aliasName;
	}
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getAvergeConnectivity() {
		return avergeConnectivity;
	}
	public void setAvergeConnectivity(String avergeConnectivity) {
		this.avergeConnectivity = get2(avergeConnectivity);
	}

	public String getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}

	public String get2(String str){
		if(str == null || "".equals(str) || str.length()<1){
			return "0";
		}
		int pos = str.indexOf('.');
		if( -1 != pos){
			if((str.length() -pos -1) >= 2){
				str = str.substring(0,pos+3);
			}else{// .0
				str = str.substring(0,str.length());
			}
		}
		return str;
	}

	public String getNodeid() {
		return nodeid;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}
}
