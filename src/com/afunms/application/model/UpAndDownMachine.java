package com.afunms.application.model;

import java.sql.Timestamp;
import java.util.Date;

import com.afunms.common.base.BaseVo;

//create table nms_remote_up_down_machine(id int,name varchar(50),ipaddress varchar(30),serverType varchar(10),lasttime datetime,username varchar(30),passwd varchar(30))
public class UpAndDownMachine extends BaseVo
{
	private int id;
	private int clusterId;
	private String name;
	private String ipaddress;
	private String serverType;
	private Timestamp lasttime;
	private String username;
	private String passwd;
	private int monitorStatus;
	private int isMonitor;
	private int isJoin;//�Ƿ���뼯Ⱥ(0:�� 1����)
	private int sequence;//��Ⱥ˳��
	private String clusterName;//��Ⱥ����
	private String clusterType;//��Ⱥ����
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getClusterId() {
		return clusterId;
	}
	public void setClusterId(int clusterId) {
		this.clusterId = clusterId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	public String getServerType() {
		return serverType;
	}
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public Timestamp getLasttime() {
		return lasttime;
	}
	public void setLasttime(Timestamp lasttime) {
		this.lasttime = lasttime;
	}
	
	public int getMonitorStatus() {
		return monitorStatus;
	}
	public void setMonitorStatus(int monitorStatus) {
		this.monitorStatus = monitorStatus;
	}
	public int getIsMonitor() {
		return isMonitor;
	}
	public void setIsMonitor(int isMonitor) {
		this.isMonitor = isMonitor;
	}
	public int getIsJoin() {
		return isJoin;
	}
	public void setIsJoin(int isJoin) {
		this.isJoin = isJoin;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public String getClusterName() {
		return clusterName;
	}
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	public String getClusterType() {
		return clusterType;
	}
	public void setClusterType(String clusterType) {
		this.clusterType = clusterType;
	}
	
	
}
