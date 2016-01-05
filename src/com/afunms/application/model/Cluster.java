package com.afunms.application.model;

import java.sql.Timestamp;

import com.afunms.common.base.BaseVo;

public class Cluster extends BaseVo{
	private int id;
	private String name;//集群名称
	private String serverType;//服务器类型
	private Timestamp createtime;//创建时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getServerType() {
		return serverType;
	}
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
	public Timestamp getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}
	
}
