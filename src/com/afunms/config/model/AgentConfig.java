package com.afunms.config.model;

import com.afunms.common.base.BaseVo;

public class AgentConfig extends BaseVo {
	
	private int agentid;         //agent ID
	private String agentname;	 //agent ����
	private String ipaddress;    //agent IP��ַ
	private String agentport;	 //agent �˿ں�
	private String agentdesc;	 //agent ����
	
	public int getAgentid() {
		return agentid;
	}
	public void setAgentid(int agentid) {
		this.agentid = agentid;
	}
	public String getAgentname() {
		return agentname;
	}
	public void setAgentname(String agentname) {
		this.agentname = agentname;
	}
	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	public String getAgentport() {
		return agentport;
	}
	public void setAgentport(String agerntport) {
		this.agentport = agerntport;
	}
	public String getAgentdesc() {
		return agentdesc;
	}
	public void setAgentdesc(String agentdesc) {
		this.agentdesc = agentdesc;
	}
	
}
