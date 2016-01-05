package com.afunms.alarm.model;

import com.afunms.common.base.BaseVo;

/**
 * <title>
 * 		告警联动 设备依赖关系实体
 * </title>
 * <p>
 * 		Description:
 * 		
 * </p>
 * 
 * @author yag
 *
 */
public class AlarmDeviceDependence extends BaseVo{

	private 	int 	id;
	private 	int 	fid;
	private		String	type;
	private		String	subtype;
	private 	int 	nodeid;
	private		String	linkPort;
	private		String	ipAddress;
	private		String	deviceName;
	
	/**
	 * nodeLevel:wupinlong add for nodeLevel
	 * <p>
	 *
	 * @since   v1.01
	 */
	private		int		nodeLevel;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFid() {
		return fid;
	}
	public void setFid(int fid) {
		this.fid = fid;
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
	public int getNodeid() {
		return nodeid;
	}
	public void setNodeid(int nodeid) {
		this.nodeid = nodeid;
	}
	public String getLinkPort() {
		return linkPort;
	}
	public void setLinkPort(String linkPort) {
		this.linkPort = linkPort;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getClass().getName() +":[id="
				+ this.getId()+",fid=" + this.getFid()
				+ ",type=" + this.getType() + ",subtype=" + this.getSubtype()
				+ ",nodeid=" + this.getNodeid() + ",linkport=" + this.getLinkPort()
				+ ",ipaddress=" + this.getIpAddress() + ",devicename=" + this.getDeviceName()
				+ ",nodeLevel="+this.getNodeLevel()+"]";
	}
	/**
	 * getNodeLevel:
	 * <p>
	 *
	 * @return  int
	 *          -
	 * @since   v1.01
	 */
	public int getNodeLevel() {
		return nodeLevel;
	}
	/**
	 * setNodeLevel:
	 * <p>
	 *
	 * @param   nodeLevel
	 *          -
	 * @since   v1.01
	 */
	public void setNodeLevel(int nodeLevel) {
		this.nodeLevel = nodeLevel;
	}
	
	
	
}
