package com.afunms.indicators.model;

import com.afunms.common.base.BaseVo;

/**
 * ����Ϊ��׼���ܼ��ָ��
 * @author Administrator
 *
 */

public class NodeDTO extends BaseVo{
	
	private int id;		
	
	private String nodeid;				// �豸id
	
	private String name;				// �豸����
	
	private String ipaddress;			// �豸ipaddress
	
	private String type;				// �豸����
	
	private String subtype;				// �豸������
	
	private String sysOid;				// ϵͳoid
	
	private String businessId;			// ҵ��id
	
	private String businessName;		// ҵ������

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
	 * @return the nodeid
	 */
	public String getNodeid() {
		return nodeid;
	}

	/**
	 * @param nodeid the nodeid to set
	 */
	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the ipaddress
	 */
	public String getIpaddress() {
		return ipaddress;
	}

	/**
	 * @param ipaddress the ipaddress to set
	 */
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the subtype
	 */
	public String getSubtype() {
		return subtype;
	}

	/**
	 * @param subtype the subtype to set
	 */
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	/**
	 * @return the sysOid
	 */
	public String getSysOid() {
		return sysOid;
	}

	/**
	 * @param sysOid the sysOid to set
	 */
	public void setSysOid(String sysOid) {
		this.sysOid = sysOid;
	}

	/**
	 * @return the businessId
	 */
	public String getBusinessId() {
		return businessId;
	}

	/**
	 * @param businessId the businessId to set
	 */
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	/**
	 * @return the businessName
	 */
	public String getBusinessName() {
		return businessName;
	}

	/**
	 * @param businessName the businessName to set
	 */
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	
}
