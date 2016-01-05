/**
 * <p>Description:mapping table temp</p>
 * <p>Company: dhcc.com</p>
 * @author Соѕь
 * @project afunms
 * @date 2010-12-13
 */

package com.afunms.temp.model;

import com.afunms.common.base.BaseVo;

public class FdbNodeTemp extends BaseVo {
	
	private String nodeid;

	private String ip;

	private String type;

	private String subtype;

	private String ifindex;

	private String ipaddress;
	
	private String mac;
	
	private String ifband;
	
	private String ifsms;
	
	private String collecttime;
	
	private String bak;
	
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

	public String getIfindex() {
		return ifindex;
	}

	public void setIfindex(String ifindex) {
		this.ifindex = ifindex;
	}

	public String getIfband() {
		return ifband;
	}

	public void setIfband(String ifband) {
		this.ifband = ifband;
	}

	public String getIfsms() {
		return ifsms;
	}

	public void setIfsms(String ifsms) {
		this.ifsms = ifsms;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getBak() {
		return bak;
	}

	public void setBak(String bak) {
		this.bak = bak;
	}

}
