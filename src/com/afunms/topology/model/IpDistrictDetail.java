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
public class IpDistrictDetail extends BaseVo{
	
	/**
	 * Ĭ��id (�Ա������־û��������ݿ���ʹ�� �� ��ʱû��ʹ��)
	 */
	private int id; 
	
	/**
	 * �����豸ip
	 */
	private String district;
	
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
	private String unOnlineToatl;

	/**
	 * 
	 */
	public IpDistrictDetail() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param district
	 * @param ipTotal
	 * @param usedTotal
	 * @param unusedTotal
	 * @param isOnlineTotal
	 * @param unOnlineToatl
	 */
	public IpDistrictDetail(int id, String district, String ipTotal,
			String usedTotal, String unusedTotal, String isOnlineTotal,
			String unOnlineToatl) {
		this.id = id;
		this.district = district;
		this.ipTotal = ipTotal;
		this.usedTotal = usedTotal;
		this.unusedTotal = unusedTotal;
		this.isOnlineTotal = isOnlineTotal;
		this.unOnlineToatl = unOnlineToatl;
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
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}

	/**
	 * @param district the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
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
	 * @return the unOnlineToatl
	 */
	public String getUnOnlineToatl() {
		return unOnlineToatl;
	}

	/**
	 * @param unOnlineToatl the unOnlineToatl to set
	 */
	public void setUnOnlineToatl(String unOnlineToatl) {
		this.unOnlineToatl = unOnlineToatl;
	}

	
	

	
	
}