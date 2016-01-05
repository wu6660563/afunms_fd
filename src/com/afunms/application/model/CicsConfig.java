/**
 * <p>Description:mapping app_db_node</p>
 * <p>Company: dhcc.com</p>
 * @author miiwill
 * @project afunms
 * @date 2007-1-7
 */

package com.afunms.application.model;

import com.afunms.common.base.BaseVo;

public class CicsConfig extends BaseVo {

	private int id;
	
	private String ipaddress;
	
	private String port_listener;
	
	private String network_protocol;
	
	private int conn_timeout;
	
	private String alias;
	
	private String region_name;
	
	private String sendmobiles;
	
	private String netid;
	
	private String sendemail;
	
	private int flag;
	
	private String gateway;
	
	private int status;
	
	private int supperid;// ��Ӧ��id snow add at 2010-5-20

	public int getSupperid() {
		return supperid;
	}

	public void setSupperid(int supperid) {
		this.supperid = supperid;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getSendmobiles() {
		return sendmobiles;
	}
	
	public void setSendmobiles(String sendmobiles) {
		this.sendmobiles = sendmobiles;
	}
	
	public String getNetid() {
		return netid;
	}
	
	public void setNetid(String netid) {
		this.netid = netid;
	}
	
	public String getSendemail() {
		return sendemail;
	}
	
	public void setSendemail(String sendemail) {
		this.sendemail = sendemail;
	}

	public int getConn_timeout() {
		return conn_timeout;
	}

	public void setConn_timeout(int conn_timeout) {
		this.conn_timeout = conn_timeout;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getNetwork_protocol() {
		return network_protocol;
	}

	public void setNetwork_protocol(String network_protocol) {
		this.network_protocol = network_protocol;
	}

	public String getPort_listener() {
		return port_listener;
	}

	public void setPort_listener(String port_listener) {
		this.port_listener = port_listener;
	}

	public String getRegion_name() {
		return region_name;
	}

	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
}