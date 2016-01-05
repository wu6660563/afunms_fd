/**
 * <p>Description:mapping table temp</p>
 * <p>Company: dhcc.com</p>
 * @author Соѕь
 * @project afunms
 * @date 2010-12-13
 */

package com.afunms.temp.model;

import com.afunms.common.base.BaseVo;

public class StorageHostConnectNodeTemp extends BaseVo {
	
	private String nodeid;

	private String ip;
	
	private String name;
	
 	private String hostconnect_id;
 	
	private String wwpn;
	
	private String hostType;
	
	private String profile;
	
	private String portgrp;
	
	private String volgrpID;
	
	private String essIOport;
	
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the hostconnect_id
	 */
	public String getHostconnect_id() {
		return hostconnect_id;
	}

	/**
	 * @param hostconnect_id the hostconnect_id to set
	 */
	public void setHostconnect_id(String hostconnect_id) {
		this.hostconnect_id = hostconnect_id;
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
	 * @return the hostType
	 */
	public String getHostType() {
		return hostType;
	}

	/**
	 * @param hostType the hostType to set
	 */
	public void setHostType(String hostType) {
		this.hostType = hostType;
	}

	/**
	 * @return the profile
	 */
	public String getProfile() {
		return profile;
	}

	/**
	 * @param profile the profile to set
	 */
	public void setProfile(String profile) {
		this.profile = profile;
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
	 * @return the volgrpID
	 */
	public String getVolgrpID() {
		return volgrpID;
	}

	/**
	 * @param volgrpID the volgrpID to set
	 */
	public void setVolgrpID(String volgrpID) {
		this.volgrpID = volgrpID;
	}

	/**
	 * @return the essIOport
	 */
	public String getEssIOport() {
		return essIOport;
	}

	/**
	 * @param essIOport the essIOport to set
	 */
	public void setEssIOport(String essIOport) {
		this.essIOport = essIOport;
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
