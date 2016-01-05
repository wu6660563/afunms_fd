/**
 * <p>Description:mapping table NMS_TOPO_NODE</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-12
 */

package com.afunms.topology.model;

import com.afunms.common.base.BaseVo;

/**
 * ����Ϊ��װip���������뵱ǰ����ƥ��ĳ־û�model�� �洢����Ӧ�����ݿ� nms_ip_district_match ����
 * @author nielin
 * @date 2010-05-13
 *
 */
public class IpDistrictMatchConfig extends BaseVo{
	
	/**
	 * Ĭ��id (�Ա������־û��������ݿ���ʹ�� �� ��ʱû��ʹ��)
	 */
	private int id; 
	
	/**
	 * �����豸ip
	 */
	private String relateipaddr;
	
	/**
	 * �豸ipaddress
	 */
	private String nodeIp;
	
	/**
	 * �豸����
	 */
	private String nodeName;
	
	/**
	 * ��ǰ����״̬
	 */
	private String isOnline;
	
	/**
	 * ��������
	 */
	private String originalDistrict;
	
	/**
	 * ��ǰ�ж�����
	 */
	private String currentDistrict;
	
	/**
	 * �Ƿ�ƥ��
	 */
	private String isMatch;
	
	/**
	 * �ɼ�ʱ��
	 */
	private String time;

	/**
	 * 
	 */
	public IpDistrictMatchConfig() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param relateipaddr
	 * @param nodeIp
	 * @param nodeName
	 * @param isOnline
	 * @param originalDistrict
	 * @param currentDistrict
	 * @param isMatch
	 * @param date
	 */
	public IpDistrictMatchConfig(int id, String relateipaddr, String nodeIp,
			String nodeName, String isOnline, String originalDistrict,
			String currentDistrict, String isMatch, String time) {
		this.id = id;
		this.relateipaddr = relateipaddr;
		this.nodeIp = nodeIp;
		this.nodeName = nodeName;
		this.isOnline = isOnline;
		this.originalDistrict = originalDistrict;
		this.currentDistrict = currentDistrict;
		this.isMatch = isMatch;
		this.time = time;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the relateipaddr
	 */
	public String getRelateipaddr() {
		return relateipaddr;
	}

	/**
	 * @param relateipaddr the relateipaddr to set
	 */
	public void setRelateipaddr(String relateipaddr) {
		this.relateipaddr = relateipaddr;
	}

	/**
	 * @return the nodeIp
	 */
	public String getNodeIp() {
		return nodeIp;
	}

	/**
	 * @param nodeIp the nodeIp to set
	 */
	public void setNodeIp(String nodeIp) {
		this.nodeIp = nodeIp;
	}

	/**
	 * @return the nodeName
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * @param nodeName the nodeName to set
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * @return the isOnline
	 */
	public String getIsOnline() {
		return isOnline;
	}

	/**
	 * @param isOnline the isOnline to set
	 */
	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}

	/**
	 * @return the originalDistrict
	 */
	public String getOriginalDistrict() {
		return originalDistrict;
	}

	/**
	 * @param originalDistrict the originalDistrict to set
	 */
	public void setOriginalDistrict(String originalDistrict) {
		this.originalDistrict = originalDistrict;
	}

	/**
	 * @return the currentDistrict
	 */
	public String getCurrentDistrict() {
		return currentDistrict;
	}

	/**
	 * @param currentDistrict the currentDistrict to set
	 */
	public void setCurrentDistrict(String currentDistrict) {
		this.currentDistrict = currentDistrict;
	}

	/**
	 * @return the isMatch
	 */
	public String getIsMatch() {
		return isMatch;
	}

	/**
	 * @param isMatch the isMatch to set
	 */
	public void setIsMatch(String isMatch) {
		this.isMatch = isMatch;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param date the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	

	

	
}