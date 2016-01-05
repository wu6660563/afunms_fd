/**
 * <p>Description:mapping table NMS_POSITION</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-07
 */

package com.afunms.event.model;

import java.util.Calendar;

import com.afunms.common.base.BaseVo;

public class Syslog extends BaseVo
{
	private Long id;
	
	private int	processid;//����id
	private String	processname;//������
	private String	processidstr;//����id�ַ�
	
	
	private int facility;//�¼���Դ
	private int priority;//���ȼ�
	private String facilityName;//�¼���Դ����
	private String priorityName;//���ȼ�����
	private String hostname;//������
	private Calendar recordtime;//ʱ���
	private String message;//����Ϣ����
	private String ipaddress;//IP��ַ
	private String businessid;//ҵ����Դ
	
	private int eventid;//�¼���Դ
	private String username;
	
	
	/** nullable persistent field */
	private String begin_date;

	/** nullable persistent field */
	private String end_date;
	
	public String getUsername(){
		return username;
	}
	public void setUsername(String username){
		this.username=username;
	}
	
	public void setEventid(int eventid){
		this.eventid=eventid;
	}
	public int getEventid(){
		return eventid;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long l) {
		id = l;
	}
	
	public void setProcessid(int processid){
		this.processid=processid;
	}
	public void setProcessname(String processname){
		this.processname=processname;
	}
	public void setProcessidstr(String processidstr){
		this.processidstr=processidstr;
	}
	public int getProcessid(){
		return processid;
	}
	public String getProcessname(){
		return processname;
	}
	public String getProcessidstr(){
		return processidstr;
	}
	
	public void setFacility(int facility){
		this.facility=facility;
	}
	public void setPriority(int priority){
		this.priority=priority;
	}
	public void setHostname(String hostname){
		this.hostname=hostname;
	}
	public void setRecordtime(Calendar recordtime){
		this.recordtime=recordtime;
	}
	public void setMessage(String message){
		this.message=message;
	}
	public void setFacilityName(String facilityName){
		this.facilityName=facilityName;
	}
	public void setPriorityName(String priorityName){
		this.priorityName=priorityName;
	}
	
	public int getFacility(){
		return facility;
	}
	public int getPriority(){
		return priority;
	}
	public String getHostname(){
		return hostname;
	}
	public Calendar getRecordtime(){
		return recordtime;
	}
	public String getMessage(){
		return message;
	}
	public String getFacilityName(){
		return facilityName;
	}
	public String getPriorityName(){
		return priorityName;
	}
	public String getIpaddress(){
		return ipaddress;
	}
	public void setIpaddress(String ipaddress){
		this.ipaddress=ipaddress;
	}
	public String getBusinessid(){
		return businessid;
	}
	public void setBusinessid(String businessid){
		this.businessid=businessid;
	}

}
