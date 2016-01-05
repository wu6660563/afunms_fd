/**
 * <p>Description:mapping app_db_node</p>
 * <p>Company: dhcc.com</p>
 * @author miiwill
 * @project afunms
 * @date 2007-1-7
 */

package com.afunms.application.model;

import com.afunms.common.base.BaseVo;

public class DominoServer extends BaseVo{
	private String name = "";//����������
	private String title = "";//����������
	private String os = "";//����������ϵͳ
	private String architecture = "";//λ��32/64
	private String starttime = "";//��������ʱ��
	private String cputype = "";//CPU����
	private String cpucount = "";//CPU����
	private String portnumber= "";//�˿ں�
	private String cpupctutil = "0";//CPU������
	
	private String imapstatus = "0";//IMAP״̬
	private String ldapstatus = "0";//LDAP״̬
	private String pop3status = "0";//POP3״̬
	private String smtpstatus = "0";//SMTP״̬
	private String availabilityIndex = "0";//��������
	private String sessionsDropped = "0";//���ߵĻỰ
	private String tasks = "0";//��������
	private String transPerMinute = "0";//һ���ӵĽ�������
	private String usersPeak = "0";//���������������û�����ֵ
	private String requestsPer1Hour = "0";//ÿСʱ��Domino��������
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getArchitecture() {
		return architecture;
	}
	public void setArchitecture(String architecture) {
		this.architecture = architecture;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getCputype() {
		return cputype;
	}
	public void setCputype(String cputype) {
		this.cputype = cputype;
	}
	public String getCpucount() {
		return cpucount;
	}
	public void setCpucount(String cpucount) {
		this.cpucount = cpucount;
	}
	public String getPortnumber() {
		return portnumber;
	}
	public void setPortnumber(String portnumber) {
		this.portnumber = portnumber;
	}
	public String getCpupctutil() {
		return cpupctutil;
	}
	public void setCpupctutil(String cpupctutil) {
		this.cpupctutil = cpupctutil;
	}
	public String getImapstatus() {
		return imapstatus;
	}
	public void setImapstatus(String imapstatus) {
		this.imapstatus = imapstatus;
	}
	public String getLdapstatus() {
		return ldapstatus;
	}
	public void setLdapstatus(String ldapstatus) {
		this.ldapstatus = ldapstatus;
	}
	public String getPop3status() {
		return pop3status;
	}
	public void setPop3status(String pop3status) {
		this.pop3status = pop3status;
	}
	public String getSmtpstatus() {
		return smtpstatus;
	}
	public void setSmtpstatus(String smtpstatus) {
		this.smtpstatus = smtpstatus;
	}
	public String getAvailabilityIndex() {
		return availabilityIndex;
	}
	public void setAvailabilityIndex(String availabilityIndex) {
		this.availabilityIndex = availabilityIndex;
	}
	public String getSessionsDropped() {
		return sessionsDropped;
	}
	public void setSessionsDropped(String sessionsDropped) {
		this.sessionsDropped = sessionsDropped;
	}
	public String getTasks() {
		return tasks;
	}
	public void setTasks(String tasks) {
		this.tasks = tasks;
	}
	public String getTransPerMinute() {
		return transPerMinute;
	}
	public void setTransPerMinute(String transPerMinute) {
		this.transPerMinute = transPerMinute;
	}
	public String getUsersPeak() {
		return usersPeak;
	}
	public void setUsersPeak(String usersPeak) {
		this.usersPeak = usersPeak;
	}
	public String getRequestsPer1Hour() {
		return requestsPer1Hour;
	}
	public void setRequestsPer1Hour(String requestsPer1Hour) {
		this.requestsPer1Hour = requestsPer1Hour;
	}
	


}