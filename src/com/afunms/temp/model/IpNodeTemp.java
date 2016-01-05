/**
 * <p>Description:mapping table temp</p>
 * <p>Company: dhcc.com</p>
 * @author Соѕь
 * @project afunms
 * @date 2010-12-13
 */

package com.afunms.temp.model;

import com.afunms.common.base.BaseVo;

public class IpNodeTemp extends BaseVo {
	
	private int id;

	private String aliasip;

	private String indexs;

	private String ipaddress;
	
	private String descr;
	
	private String speeds;
	
	private String types;
	
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

	public String getAliasip() {
		return aliasip;
	}

	public void setAliasip(String aliasip) {
		this.aliasip = aliasip;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getIndexs() {
		return indexs;
	}

	public void setIndexs(String indexs) {
		this.indexs = indexs;
	}

	public String getSpeeds() {
		return speeds;
	}

	public void setSpeeds(String speeds) {
		this.speeds = speeds;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

}
