package com.afunms.subconfigcat.model;

import com.afunms.common.base.BaseVo;

public class SubconfigCatConfig extends BaseVo {
	private int id;
	private String name;    //�û���
	private String desc;  //����
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
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

}
