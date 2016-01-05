/**
 * <p>Description:mapping table temp</p>
 * <p>Company: dhcc.com</p>
 * @author 杨军
 * @project afunms
 * @date 2010-12-13
 */

package com.afunms.temp.model;

import com.afunms.common.base.BaseVo;

public class ServiceNodeTemp extends BaseVo {
	
	private String nodeid;

	private String ip;

	private String type;

	private String subtype;

	private String name;
	
	private String instate;
	
	private String opstate;
	
	private String uninst;
	
	private String paused;
	
	/**
	 * 启动模式
	 */
	private String startMode;
	
	/**
	 * 路径
	 */
	private String pathName;
	
	/**
	 * 描述
	 */
	private String description;
	
	/**
	 * 服务类型
	 */
	private String serviceType;
	
	private String pid;
	
	private String groupstr;
	
	private String collecttime;
	
	public String getStartMode() {
		return startMode;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getGroupstr() {
		return groupstr;
	}

	public void setGroupstr(String groupstr) {
		this.groupstr = groupstr;
	}

	public void setStartMode(String startMode) {
		this.startMode = startMode;
	}

	public String getPathName() {
		return pathName;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getCollecttime() {
		return collecttime;
	}

	public void setCollecttime(String string) {
		this.collecttime = string;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getNodeid() {
		return nodeid;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInstate() {
		return instate;
	}

	public void setInstate(String instate) {
		this.instate = instate;
	}

	public String getOpstate() {
		return opstate;
	}

	public void setOpstate(String opstate) {
		this.opstate = opstate;
	}

	public String getPaused() {
		return paused;
	}

	public void setPaused(String paused) {
		this.paused = paused;
	}

	public String getUninst() {
		return uninst;
	}

	public void setUninst(String uninst) {
		this.uninst = uninst;
	}

}
