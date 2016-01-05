/**
 * <p>Description:mapping table temp</p>
 * <p>Company: dhcc.com</p>
 * @author Соѕь
 * @project afunms
 * @date 2010-12-13
 */

package com.afunms.temp.model;

import com.afunms.common.base.BaseVo;

public class DeviceNodeTemp extends BaseVo {
	
	private String nodeid;

	private String ip;

	private String type;

	private String subtype;

	private String name;
	
	private String deviceindex;
	
	private String dtype;
	
	private String status;
	
	private String collecttime;
	
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

	public String getDeviceindex() {
		return deviceindex;
	}

	public void setDeviceindex(String deviceindex) {
		this.deviceindex = deviceindex;
	}

	public String getDtype() {
		return dtype;
	}

	public void setDtype(String dtype) {
		this.dtype = dtype;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
