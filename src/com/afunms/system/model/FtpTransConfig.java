package com.afunms.system.model;

import com.afunms.common.base.BaseVo;

public class FtpTransConfig extends BaseVo 
{
	private int id;
	private String ip;
	private String username;
	private String password;
	private int flag; //1: ��FTP���������͸澯��Ϣ     0��������
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	
}
