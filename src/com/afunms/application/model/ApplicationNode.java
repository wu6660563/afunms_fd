package com.afunms.application.model;

import com.afunms.common.base.BaseVo;

/**
 * 多种采集节点信息
 * 
 * @author yag
 *
 */
public class ApplicationNode extends BaseVo {

	private 	int 	id;
	private 	String	name;
	private		String	ipAddress;
	private		String	uniqueKey;			// 采集节点唯一性标识
	private		String	type;				// 所有节点均为application
	private		String	subtype;			// 子类型如sdh等
	private		String	bid;				// 所属业务
	private		boolean managed;			//是否管理
	private		String	descr;
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getName()+"[id="+ this.getId());
		sb.append(",name=" + this.getName() + ",ipaddress=" + this.getIpAddress());
		sb.append(",uniquekey=" + this.getUniqueKey() + ",type=" + this.getType());
		sb.append(",subtype=" + this.getSubtype() + ",bid=" + this.getBid());
		sb.append(",managed=" + this.isManaged());
		sb.append(",descr=" + this.getDescr());
		return sb.toString();
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
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getUniqueKey() {
		return uniqueKey;
	}
	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSubtype() {
		return subtype;
	}
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public boolean isManaged() {
		return managed;
	}
	public void setManaged(boolean managed) {
		this.managed = managed;
	}
	
	
}
