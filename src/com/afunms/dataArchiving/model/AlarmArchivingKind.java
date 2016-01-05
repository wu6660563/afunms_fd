package com.afunms.dataArchiving.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.afunms.common.base.BaseVo;

/**
 * 告警数据归档类型model
 * 对应数据库表:system_alarm_archiving_kinds
 * 
 * @author Administrator
 *
 */
public class AlarmArchivingKind extends BaseVo{

	private int id;
	private String name; 	// 名称
	private String type;	// 类型标示
	private Calendar	latestArchiveTime;	// 最新的归档时间
	private int fatherId;
	private int childrenId;
	private String archiveInterval;
	private String retentionTime;
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	
	
	public int getFatherId() {
		return fatherId;
	}
	public String getRetentionTime() {
		return retentionTime;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void setFatherId(int fatherId) {
		this.fatherId = fatherId;
	}
	public void setRetentionTime(String retentionTime) {
		this.retentionTime = retentionTime;
	}
	
	public int getChildrenId() {
		return childrenId;
	}
	public void setChildrenId(int childrenId) {
		this.childrenId = childrenId;
	}
	public String getArchiveInterval() {
		return archiveInterval;
	}
	public void setArchiveInterval(String archiveInterval) {
		this.archiveInterval = archiveInterval;
	}
	
	public Calendar getLatestArchiveTime() {
		return latestArchiveTime;
	}
	public void setLatestArchiveTime(Calendar latestArchiveTime) {
		this.latestArchiveTime = latestArchiveTime;
	}

	private static SimpleDateFormat sdf = new SimpleDateFormat(
		"yyyy-MM-dd HH:mm:ss");

	 
	public String toString(){
		StringBuffer kind = new StringBuffer();
		kind.append("AlarmArchivingKind INFO: " + "[ID]=" +this.getId()+"; [TYPE]="+this.getType()
			+ "; [Name]="+this.getName()
			+ "; [latestTime] = "+sdf.format(this.getLatestArchiveTime().getTime().getTime())
			+ "; [FatherId]="+this.getFatherId()+"; [ChildrenID]="+this.getChildrenId()
			+ "; [IntervalTime]="+this.getArchiveInterval()+"; [RetentionTime]="+this.getRetentionTime());
		return kind.toString();
	}
	
}
