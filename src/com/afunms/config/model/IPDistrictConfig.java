package com.afunms.config.model;

import com.afunms.common.base.BaseVo;

public class IPDistrictConfig extends BaseVo {
	private int id;
	private int districtid;
	private String startip;    //�û���
	private String endip;  //����
	public int getDistrictid() {
		return districtid;
	}
	public void setDistrictid(int districtid) {
		this.districtid = districtid;
	}
	public String getEndip() {
		return endip;
	}
	public void setEndip(String endip) {
		this.endip = endip;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStartip() {
		return startip;
	}
	public void setStartip(String startip) {
		this.startip = startip;
	}
	
	

}
