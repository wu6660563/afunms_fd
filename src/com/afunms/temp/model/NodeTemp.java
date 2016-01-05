/**
 * <p>Description:mapping table temp</p>
 * <p>Company: dhcc.com</p>
 * @author Соѕь
 * @project afunms
 * @date 2010-12-13
 */

package com.afunms.temp.model;

import com.afunms.common.base.BaseVo;

public class NodeTemp extends BaseVo {
	
	private String nodeid;

	private String ip;

	private String type;

	private String subtype;

	private String entity;

	private String subentity;
	
	private String thevalue;
	
	private String chname;
	
	private String restype;
	
	private String unit;
	
	private String sindex;
	
	private String collecttime;
	
	private String bak;

	public String getBak() {
		return bak;
	}

	public void setBak(String bak) {
		this.bak = bak;
	}

	public String getChname() {
		return chname;
	}

	public void setChname(String chname) {
		this.chname = chname;
	}

	public String getCollecttime() {
		return collecttime;
	}

	public void setCollecttime(String string) {
		this.collecttime = string;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
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

	public String getRestype() {
		return restype;
	}

	public void setRestype(String restype) {
		this.restype = restype;
	}

	public String getSubentity() {
		return subentity;
	}

	public void setSubentity(String subentity) {
		this.subentity = subentity;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public String getThevalue() {
		return thevalue;
	}

	public void setThevalue(String thevalue) {
		this.thevalue = thevalue;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getSindex() {
		return sindex;
	}

	public void setSindex(String sindex) {
		this.sindex = sindex;
	}
}
