package com.afunms.biosreport.model;

import com.afunms.common.base.BaseVo;

/**
 * <p>
 * 		告警处理以及报告实体类
 * </p>
 * <p>
 * 	Description:
 * 	
 * </p>
 * 	@author yag
 *
 */
public class AlarmDealAndReportModel extends BaseVo{

	private int id;
	private	int eventid;
	private String enventlocation;
	private String context;
	private String recordtime;
	private int		alarmCount;
	private int 	level1;
	private int 	nodeid;
	private String	bussinessid;
	private String 	subtype;
	private	String	subentity;
	private	int		managesign;
	private	String	managetime;
	private	String	reportman;
	private String 	reportContent;
	private String	reportTime;
	private int 	reportCount;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getEventid() {
		return eventid;
	}
	public void setEventid(int eventid) {
		this.eventid = eventid;
	}
	public String getEnventlocation() {
		return enventlocation;
	}
	public void setEnventlocation(String enventlocation) {
		this.enventlocation = enventlocation;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getRecordtime() {
		return recordtime;
	}
	public void setRecordtime(String recordtime) {
		this.recordtime = recordtime;
	}
	public int getAlarmCount() {
		return alarmCount;
	}
	public void setAlarmCount(int alarmCount) {
		this.alarmCount = alarmCount;
	}
	public int getLevel1() {
		return level1;
	}
	public void setLevel1(int level1) {
		this.level1 = level1;
	}
	public int getNodeid() {
		return nodeid;
	}
	public void setNodeid(int nodeid) {
		this.nodeid = nodeid;
	}
	public String getBussinessid() {
		return bussinessid;
	}
	public void setBussinessid(String bussinessid) {
		this.bussinessid = bussinessid;
	}
	public String getSubtype() {
		return subtype;
	}
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}
	public String getSubentity() {
		return subentity;
	}
	public void setSubentity(String subentity) {
		this.subentity = subentity;
	}
	public int getManagesign() {
		return managesign;
	}
	public void setManagesign(int managesign) {
		this.managesign = managesign;
	}
	public String getManagetime() {
		return managetime;
	}
	public void setManagetime(String managetime) {
		this.managetime = managetime;
	}
	public String getReportman() {
		return reportman;
	}
	public void setReportman(String reportman) {
		this.reportman = reportman == null ? "" : reportman;
	}
	public String getReportContent() {
		return reportContent;
	}
	public void setReportContent(String reportContent) {
		this.reportContent = reportContent == null? "" : reportContent;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime == null ? "" : reportTime;
	}
	public int getReportCount() {
		return reportCount;
	}
	public void setReportCount(int reportCount) {
		this.reportCount = reportCount;
	}
	
	
}
