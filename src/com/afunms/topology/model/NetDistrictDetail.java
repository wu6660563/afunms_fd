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
 * ����Ϊ��װip���������뵱ǰ����ƥ���ҵ��model�� û�д洢����Ӧ�����ݿ⵱��
 * @author nielin
 * @date 2010-05-12
 *
 */
public class NetDistrictDetail extends BaseVo{
	
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
	 * ��ʹ��
	 */
	private String usedTotal;
	
	/**
	 * δʹ��
	 */
	private String unusedTotal;
	
	/**
	 * ��������
	 */
	private String isOnlineTotal;
	
	/**
	 * ��������
	 */
	private String unOnlineToatal;

	/**
	 * 
	 */
	public NetDistrictDetail() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param districtId
	 * @param ipDistrictId
	 * @param districtName
	 * @param netDistrictName
	 * @param ipTotal
	 * @param usedTotal
	 * @param unusedTotal
	 * @param isOnlineTotal
	 * @param unOnlineToatal
	 */
	public NetDistrictDetail(int id, String districtId, String ipDistrictId,
			String districtName, String netDistrictName, String ipTotal,
			String usedTotal, String unusedTotal, String isOnlineTotal,
			String unOnlineToatal) {
		this.id = id;
		this.districtId = districtId;
		this.ipDistrictId = ipDistrictId;
		this.districtName = districtName;
		this.netDistrictName = netDistrictName;
		this.ipTotal = ipTotal;
		this.usedTotal = usedTotal;
		this.unusedTotal = unusedTotal;
		this.isOnlineTotal = isOnlineTotal;
		this.unOnlineToatal = unOnlineToatal;
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
	 * @return the usedTotal
	 */
	public String getUsedTotal() {
		return usedTotal;
	}

	/**
	 * @param usedTotal the usedTotal to set
	 */
	public void setUsedTotal(String usedTotal) {
		this.usedTotal = usedTotal;
	}

	/**
	 * @return the unusedTotal
	 */
	public String getUnusedTotal() {
		return unusedTotal;
	}

	/**
	 * @param unusedTotal the unusedTotal to set
	 */
	public void setUnusedTotal(String unusedTotal) {
		this.unusedTotal = unusedTotal;
	}

	/**
	 * @return the isOnlineTotal
	 */
	public String getIsOnlineTotal() {
		return isOnlineTotal;
	}

	/**
	 * @param isOnlineTotal the isOnlineTotal to set
	 */
	public void setIsOnlineTotal(String isOnlineTotal) {
		this.isOnlineTotal = isOnlineTotal;
	}

	/**
	 * @return the unOnlineToatal
	 */
	public String getUnOnlineToatal() {
		return unOnlineToatal;
	}

	/**
	 * @param unOnlineToatal the unOnlineToatal to set
	 */
	public void setUnOnlineToatal(String unOnlineToatal) {
		this.unOnlineToatal = unOnlineToatal;
	}

	
	
}