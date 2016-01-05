package com.afunms.detail.reomte.model;

public class InterfaceInfo {
	
	/**
	 * ����
	 */
	private String sindex;
	
	/**
	 * ����
	 */
	private String ifDescr;
	
	/**
	 * ����Ӧ��
	 */
	private String LinkUse;
	
	/**
	 * ÿ���ֽ���(M)
	 */
	private String ifSpeed;
	
	/**
	 * ״̬
	 */
	private String ifOperStatus;
	
	/**
	 * ���ڹ㲥���ݰ�
	 */
	private String ifOutBroadcastPkts;
	
	/**
	 * ��ڹ㲥���ݰ�
	 */
	private String ifInBroadcastPkts;
	
	/**
	 * ���ڶಥ���ݰ�
	 */
	private String ifOutMulticastPkts;
	
	/**
	 * ��ڶಥ���ݰ�
	 */
	private String ifInMulticastPkts;
	/**
	 * ��������
	 */
	private String outBandwidthUtilHdx;
	
	/**
	 * �������
	 */
	private String inBandwidthUtilHdx;

	//######################HONG ADD
	/**
	 *�˿�������  
	 */
	private String  allBandwidthUtilHdx;
	
	/**
	 * ����
	 */
	private String ifType;
	
	
	/**
	 * ������ݰ�
	 */
	private String ifMtu;
	

	/**
	 * Ԥ��״̬
	 */
	private String ifAdminStatus;
	

	/**
	 * 86�˿���ڴ���������
	 */
	private String InBandwidthUtilHdxPerc;
	

	/**
	 * 86�˿ڳ��ڴ���������
	 */
	private String OutBandwidthUtilHdxPerc;
	

	/**
	 * @return the sindex
	 */
	public String getSindex() {
		return sindex;
	}

	/**
	 * @param sindex the sindex to set
	 */
	public void setSindex(String sindex) {
		this.sindex = sindex;
	}

	/**
	 * @return the ifDescr
	 */
	public String getIfDescr() {
		return ifDescr;
	}

	/**
	 * @param ifDescr the ifDescr to set
	 */
	public void setIfDescr(String ifDescr) {
		this.ifDescr = ifDescr;
	}

	/**
	 * @return the linkUse
	 */
	public String getLinkUse() {
		return LinkUse;
	}

	/**
	 * @param linkUse the linkUse to set
	 */
	public void setLinkUse(String linkUse) {
		LinkUse = linkUse;
	}

	/**
	 * @return the ifSpeed
	 */
	public String getIfSpeed() {
		return ifSpeed;
	}

	/**
	 * @param ifSpeed the ifSpeed to set
	 */
	public void setIfSpeed(String ifSpeed) {
		this.ifSpeed = ifSpeed;
	}

	/**
	 * @return the ifOperStatus
	 */
	public String getIfOperStatus() {
		return ifOperStatus;
	}

	/**
	 * @param ifOperStatus the ifOperStatus to set
	 */
	public void setIfOperStatus(String ifOperStatus) {
		this.ifOperStatus = ifOperStatus;
	}

	/**
	 * @return the ifOutBroadcastPkts
	 */
	public String getIfOutBroadcastPkts() {
		return ifOutBroadcastPkts;
	}

	/**
	 * @param ifOutBroadcastPkts the ifOutBroadcastPkts to set
	 */
	public void setIfOutBroadcastPkts(String ifOutBroadcastPkts) {
		this.ifOutBroadcastPkts = ifOutBroadcastPkts;
	}

	/**
	 * @return the ifInBroadcastPkts
	 */
	public String getIfInBroadcastPkts() {
		return ifInBroadcastPkts;
	}

	/**
	 * @param ifInBroadcastPkts the ifInBroadcastPkts to set
	 */
	public void setIfInBroadcastPkts(String ifInBroadcastPkts) {
		this.ifInBroadcastPkts = ifInBroadcastPkts;
	}

	/**
	 * @return the ifOutMulticastPkts
	 */
	public String getIfOutMulticastPkts() {
		return ifOutMulticastPkts;
	}

	/**
	 * @param ifOutMulticastPkts the ifOutMulticastPkts to set
	 */
	public void setIfOutMulticastPkts(String ifOutMulticastPkts) {
		this.ifOutMulticastPkts = ifOutMulticastPkts;
	}

	/**
	 * @return the ifInMulticastPkts
	 */
	public String getIfInMulticastPkts() {
		return ifInMulticastPkts;
	}

	/**
	 * @param ifInMulticastPkts the ifInMulticastPkts to set
	 */
	public void setIfInMulticastPkts(String ifInMulticastPkts) {
		this.ifInMulticastPkts = ifInMulticastPkts;
	}

	/**
	 * @return the outBandwidthUtilHdx
	 */
	public String getOutBandwidthUtilHdx() {
		return outBandwidthUtilHdx;
	}

	/**
	 * @param outBandwidthUtilHdx the outBandwidthUtilHdx to set
	 */
	public void setOutBandwidthUtilHdx(String outBandwidthUtilHdx) {
		this.outBandwidthUtilHdx = outBandwidthUtilHdx;
	}

	/**
	 * @return the inBandwidthUtilHdx
	 */
	public String getInBandwidthUtilHdx() {
		return inBandwidthUtilHdx;
	}

	/**
	 * @param inBandwidthUtilHdx the inBandwidthUtilHdx to set
	 */
	public void setInBandwidthUtilHdx(String inBandwidthUtilHdx) {
		this.inBandwidthUtilHdx = inBandwidthUtilHdx;
	}

	public String getAllBandwidthUtilHdx() {
		return allBandwidthUtilHdx;
	}

	public void setAllBandwidthUtilHdx(String allBandwidthUtilHdx) {
		this.allBandwidthUtilHdx = allBandwidthUtilHdx;
	}

	public String getIfType() {
		return ifType;
	}

	public void setIfType(String ifType) {
		this.ifType = ifType;
	}

	public String getIfMtu() {
		return ifMtu;
	}

	public void setIfMtu(String ifMtu) {
		this.ifMtu = ifMtu;
	}

	public String getIfAdminStatus() {
		return ifAdminStatus;
	}

	public void setIfAdminStatus(String ifAdminStatus) {
		this.ifAdminStatus = ifAdminStatus;
	}

	public String getInBandwidthUtilHdxPerc() {
		return InBandwidthUtilHdxPerc;
	}

	public void setInBandwidthUtilHdxPerc(String inBandwidthUtilHdxPerc) {
		InBandwidthUtilHdxPerc = inBandwidthUtilHdxPerc;
	}

	public String getOutBandwidthUtilHdxPerc() {
		return OutBandwidthUtilHdxPerc;
	}

	public void setOutBandwidthUtilHdxPerc(String outBandwidthUtilHdxPerc) {
		OutBandwidthUtilHdxPerc = outBandwidthUtilHdxPerc;
	}
}
