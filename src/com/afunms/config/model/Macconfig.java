package com.afunms.config.model;

import com.afunms.common.base.BaseVo;


/**
 * 
 *  mac ��ַ����bean 
 * @author konglq
 *
 */

public class Macconfig extends BaseVo{
	
	
	private int id; //id 
	private String mac;//mac ��ַ
	private int discrictid; //����id
	private int deptid;  //����id
	private int employeeid; //Ա��id
	private String macdesc; //mac ַ����
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public int getDiscrictid() {
		return discrictid;
	}
	public void setDiscrictid(int discrictid) {
		this.discrictid = discrictid;
	}
	public int getDeptid() {
		return deptid;
	}
	public void setDeptid(int deptid) {
		this.deptid = deptid;
	}
	public int getEmployeeid() {
		return employeeid;
	}
	public void setEmployeeid(int employeeid) {
		this.employeeid = employeeid;
	}
	public String getMacdesc() {
		return macdesc;
	}
	public void setMacdesc(String macdesc) {
		this.macdesc = macdesc;
	}
	
	
	
	
	
	
	

}
