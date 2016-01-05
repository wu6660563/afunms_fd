package com.afunms.config.model;

import java.sql.Timestamp;

import com.afunms.common.base.BaseVo;

public class Hua3VPNFileConfig extends BaseVo
{
	private int id;
	private String ipaddress;
	private String fileName;
	private String descri;
	private int fileSize;
	private Timestamp backupTime;
	private String bkpType;
	public String getBkpType() {
		return bkpType;
	}
	public void setBkpType(String bkpType) {
		this.bkpType = bkpType;
	}
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	public Timestamp getBackupTime() {
		return backupTime;
	}
	public void setBackupTime(Timestamp backupTime) {
		this.backupTime = backupTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDescri() {
		return descri;
	}
	public void setDescri(String descri) {
		this.descri = descri;
	}
	
}
