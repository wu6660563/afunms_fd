/**
 * <p>Description:mapping table temp</p>
 * <p>Company: dhcc.com</p>
 * @author Соѕь
 * @project afunms
 * @date 2010-12-13
 */

package com.afunms.temp.model;

import com.afunms.common.base.BaseVo;

public class StorageIOPortNodeTemp extends BaseVo {
	
	private String nodeid;

	private String ip;
	
	private String ioport_id;
	
 	private String wwpn;
 	
	private String state;
	
	private String type;
	
	private String topo;
	
	private String portgrp;
	
	private String collecttime;

	/**
	 * @return the nodeid
	 */
	public String getNodeid() {
		return nodeid;
	}

	/**
	 * @param nodeid the nodeid to set
	 */
	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the ioport_id
	 */
	public String getIoport_id() {
		return ioport_id;
	}

	/**
	 * @param ioport_id the ioport_id to set
	 */
	public void setIoport_id(String ioport_id) {
		this.ioport_id = ioport_id;
	}

	/**
	 * @return the wwpn
	 */
	public String getWwpn() {
		return wwpn;
	}

	/**
	 * @param wwpn the wwpn to set
	 */
	public void setWwpn(String wwpn) {
		this.wwpn = wwpn;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the topo
	 */
	public String getTopo() {
		return topo;
	}

	/**
	 * @param topo the topo to set
	 */
	public void setTopo(String topo) {
		this.topo = topo;
	}

	/**
	 * @return the portgrp
	 */
	public String getPortgrp() {
		return portgrp;
	}

	/**
	 * @param portgrp the portgrp to set
	 */
	public void setPortgrp(String portgrp) {
		this.portgrp = portgrp;
	}

	/**
	 * @return the collecttime
	 */
	public String getCollecttime() {
		return collecttime;
	}

	/**
	 * @param collecttime the collecttime to set
	 */
	public void setCollecttime(String collecttime) {
		this.collecttime = collecttime;
	}
	
	


	
}
