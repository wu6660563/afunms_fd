package com.afunms.config.model;

import com.afunms.common.base.BaseVo;

public class DistrictConfig extends BaseVo {
	private int id;
	private String name;    //�û���
	private String desc;  //����
	private String descolor;  //��ɫ
	
	public String getDescolor() {
		return descolor;
	}
	public void setDescolor(String descolor) {
		this.descolor = descolor;
	}
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
