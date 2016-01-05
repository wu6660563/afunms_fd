package com.afunms.biosreport.model;

import com.afunms.common.base.BaseVo;

/**
 * �˿���������ʵ��
 * 
 * @author yag
 *
 */
public class DeviceLinkPortReport extends BaseVo{

	private int	id;	
	private String startDeviceIP;	// �����豸IP
	private String endDeviceIP;		// �����豸IP
	private String startDeviceName;	//	�����豸����
	private	String endDeviceName;	// �����豸����
	private String startPort;		// �����豸���Ӷ˿ں�			
	private	String endPort;			// �����豸���Ӷ˿ں�
	
	private String startUtilhdx;	// �����豸�˿����� ��λ��KB/s��	
	private String endUtilhdx;		// �����豸�˿�����
	
	private String startUtilhdxperc; // �˿ڴ���������	 ��λ��%��
	private String endUtilhdxperc;
	
	private String collecttime;		// ʱ���ţ�����Ϊ�졢�ܵ�
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStartDeviceIP() {
		return startDeviceIP;
	}
	public void setStartDeviceIP(String startDeviceIP) {
		this.startDeviceIP = startDeviceIP;
	}
	public String getEndDeviceIP() {
		return endDeviceIP;
	}
	public void setEndDeviceIP(String endDeviceIP) {
		this.endDeviceIP = endDeviceIP;
	}
	public String getStartDeviceName() {
		return startDeviceName;
	}
	public void setStartDeviceName(String startDeviceName) {
		this.startDeviceName = startDeviceName;
	}
	public String getEndDeviceName() {
		return endDeviceName;
	}
	public void setEndDeviceName(String endDeviceName) {
		this.endDeviceName = endDeviceName;
	}
	public String getStartPort() {
		return startPort;
	}
	public void setStartPort(String startPort) {
		this.startPort = startPort;
	}
	public String getEndPort() {
		return endPort;
	}
	public void setEndPort(String endPort) {
		this.endPort = endPort;
	}
	public String getStartUtilhdx() {
		return startUtilhdx;
	}
	
	public void setStartUtilhdx(String startUtilhdx) {
		this.startUtilhdx = startUtilhdx;
	}
	
	public String getEndUtilhdx() {
		return endUtilhdx;
	}
	public void setEndUtilhdx(String endUtilhdx) {
		this.endUtilhdx = endUtilhdx;
	}
	public String getStartUtilhdxperc() {
		return startUtilhdxperc;
	}
	public void setStartUtilhdxperc(String startUtilhdxperc) {
		this.startUtilhdxperc = startUtilhdxperc;
	}
	
	/**
	 * 	��ȡ�ַ�����С�����ǰ��λ�Ĳ���
	 * 
	 * @param str
	 * @return
	 */
	public String getBeforePoint2(String str){
		if(str == null || str.trim().length()<1){
			return "0";
		}
		int pos = str.indexOf('.');
		if( -1 != pos){
			if((str.length() -pos -1) >= 2){
				str = str.substring(0,pos+3);
			}else{// .0
				str = str.substring(0,str.length());
			}
		}
		return str;
	}
	
	
	public String getEndUtilhdxperc() {
		return endUtilhdxperc;
	}
	public void setEndUtilhdxperc(String endUtilhdxperc) {
		this.endUtilhdxperc = endUtilhdxperc;
	}
	
	public String getCollecttime() {
		return collecttime;
	}
	public void setCollecttime(String collecttime) {
		this.collecttime = collecttime;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getClass().getName()+"startIP=" + this.getStartDeviceIP()
			+ ", startName=" + this.getStartDeviceName()
			+ ", startPort=" + this.getStartPort()
			+ ", startUtilhdx=" + this.getStartUtilhdx()
			+ ", startUtilhdxperc=" + this.getStartUtilhdxperc()
			+ ", endIP=" + this.getEndDeviceIP()
			+ ", endName=" + this.getEndDeviceName()
			+ ", endPort=" + this.getEndPort()
			+ ", endUtilhdx=" + this.getEndUtilhdx()
			+ ", endUtilhdxperc=" + this.getEndUtilhdxperc()
			+ "]";
	}
	
	public static void main(String[] args) {
		DeviceLinkPortReport de = new DeviceLinkPortReport();
		String s = de.getBeforePoint2("23.2  ");
		System.out.println(s);
	}
	
}
