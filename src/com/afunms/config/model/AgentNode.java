package com.afunms.config.model;

import com.afunms.common.base.BaseVo;

public class AgentNode extends BaseVo {
	
	private int nodeid; 			//topo_host_node��ID
	private int agentid;			//nms_agent_config��ID
	private String ip_address;		//topo_host_node��IP��ַ
	private String alias;			//topo_host_node��ϵͳ����
	
	public int getNodeid() {
		return nodeid;
	}
	public void setNodeid(int nodeid) {
		this.nodeid = nodeid;
	}
	public int getAgentid() {
		return agentid;
	}
	public void setAgentid(int d) {
		this.agentid = d;
	}
	public String getIp_address() {
		return ip_address;
	}
	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
}
