/**
 * <p>Description:mapping table temp</p>
 * <p>Company: dhcc.com</p>
 * @author Соѕь
 * @project afunms
 * @date 2010-12-13
 */

package com.afunms.temp.model;

import com.afunms.common.base.BaseVo;

public class ArpNodeTemp extends BaseVo {
	
	private int id;

	private String relateipaddr;

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

	public String getBak() {
		return bak;
	}

	public void setBak(String bak) {
		this.bak = bak;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRelateipaddr() {
		return relateipaddr;
	}

	public void setRelateipaddr(String relateipaddr) {
		this.relateipaddr = relateipaddr;
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

}
