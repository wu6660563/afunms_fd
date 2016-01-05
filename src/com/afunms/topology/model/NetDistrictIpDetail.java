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
 * ����Ϊ��װip�������ε�ҵ��model�� û�д洢����Ӧ�����ݿ⵱��
 * @author nielin
 * @date 2010-05-12
 *
 */
public class NetDistrictIpDetail extends BaseVo{
	
	/**
	 * Ĭ��id (�Ա������־û��������ݿ���ʹ�� �� ��ʱû��ʹ��)
	 */
	private int id; 
	
	/**
	 * ������������ id
	 */
	private String districtId;
	
	/**
	 * ��������������� id
	 */
	private String ipDistrictId;
	
	/**
	 * ��������������
	 */
	private String districtName;
	
	/**
	 * ������
	 */
	private String netDistrictName;
	
	/**
	 * ����ip����
	 */
	private String ipTotal;
	
	/**
	 * �豸ip
	 */
	private String ipaddress;
	
	/**
	 * �Ƿ����
	 */
	private String isUsed;
	
	/**
	 * �Ƿ�����
	 */
	private String isOnline;

	/**
	 * 
	 */
	public NetDistrictIpDetail() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param districtId
	 * @param ipDistrictId
	 * @param districtName
	 * @param netDistrictName
	 * @param ipTotal
	 * @param ipaddress
	 * @param isUsed
	 * @param isOnline
	 */
	public NetDistrictIpDetail(int id, String districtId, String ipDistrictId,
			String districtName, String netDistrictName, String ipTotal,
			String ipaddress, String isUsed, String isOnline) {
		this.id = id;
		this.districtId = districtId;
		this.ipDistrictId = ipDistrictId;
		this.districtName = districtName;
		this.netDistrictName = netDistrictName;
		this.ipTotal = ipTotal;
		this.ipaddress = ipaddress;
		this.isUsed = isUsed;
		this.isOnline = isOnline;
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
	 * @return the districtId
	 */
	public String getDistrictId() {
		return districtId;
	}

	/**
	 * @param districtId the districtId to set
	 */
	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	/**
	 * @return the ipDistrictId
	 */
	public String getIpDistrictId() {
		return ipDistrictId;
	}

	/**
	 * @param ipDistrictId the ipDistrictId to set
	 */
	public void setIpDistrictId(String ipDistrictId) {
		this.ipDistrictId = ipDistrictId;
	}

	/**
	 * @return the districtName
	 */
	public String getDistrictName() {
		return districtName;
	}

	/**
	 * @param districtName the districtName to set
	 */
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	/**
	 * @return the netDistrictName
	 */
	public String getNetDistrictName() {
		return netDistrictName;
	}

	/**
	 * @param netDistrictName the netDistrictName to set
	 */
	public void setNetDistrictName(String netDistrictName) {
		this.netDistrictName = netDistrictName;
	}

	/**
	 * @return the ipTotal
	 */
	public String getIpTotal() {
		return ipTotal;
	}

	/**
	 * @param ipTotal the ipTotal to set
	 */
	public void setIpTotal(String ipTotal) {
		this.ipTotal = ipTotal;
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
	 * @return the isUsed
	 */
	public String getIsUsed() {
		return isUsed;
	}

	/**
	 * @param isUsed the isUsed to set
	 */
	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
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
	
	
	
}