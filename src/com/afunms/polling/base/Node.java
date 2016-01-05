/**
 * <p>Description:����ͼ���нڵ�ĸ���</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-11-25
 */

package com.afunms.polling.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.afunms.common.util.SysUtil;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.topology.util.NodeHelper;

public class Node implements Serializable
{
	/**
	 * serialVersionUID:
	 * <p>
	 *
	 * @since   v1.01
	 */
	private static final long serialVersionUID = 3764182772336633986L;
	protected int id;           //id
    protected int category;     //���
    protected String type;      //����    
    protected String ipAddress; //IP��ַ  
    protected String adminIp; 	//����IP��ַ
    protected String lastAlarm; //������󱨾���Ϣ    
    protected String lastTime;  //�ϴ���ѯʱ��
    protected String nextTime;  //�´���ѯʱ��    
    protected String sysDescr;  //ϵͳ����         
    protected String alias;     //ϵͳ����  
    protected boolean managed;  //�Ƿ񱻹���    
    protected boolean alarm;    //�Ƿ񱨾�    
    protected int normalTimes;
    protected int alarmlevel;
    protected int failTimes;    
    protected int status; //��ǰ״̬0=��������,1=����,2=�豸æ,3=�ػ�    
    protected List moidList;     //���б����Ӷ���       
    protected List alarmMessage; //������Ϣ
    protected int discoverstatus; //��η��ֵ�״̬
    protected String sysLocation;  //ϵͳλ��
    protected String sysContact;  //ϵͳ��ϵ��
    protected Hashtable alarmHash;//�����ǰ�澯������
    protected Hashtable alarmPksHash;//�����ǰ�澯������
    protected int transfer;//���䷽��0���ޣ�1������
    protected String assetid;//�豸���
    protected String location;//����λ��
    private int collecttype;//���ݲɼ���ʽ  1:snmp 2:shell
	int ostype;//����ϵͳ����  1:snmp 2:shell
	private String sendmobiles;
	private String sendemail;
	private String sendphone;
	
	private String bid;
	private int endpoint;//ĩ���豸
	private int supperid;//��Ӧ��id snow add at 2010-5-18
	 
	 
	public int getSupperid() {
		return supperid;
	}
	public void setSupperid(int supperid) {
		this.supperid = supperid;
	}
	
	public int getCollecttype() {
		return this.collecttype;
	}
	public void setCollecttype(int collecttype) {
		this.collecttype = collecttype;
	}
	
	public int getOstype() {
		return this.ostype;
	}
	public void setOstype(int ostype) {
		this.ostype = ostype;
	}
    
	public Node()
    {
		alarmMessage = new ArrayList();
		moidList = new ArrayList();
		lastAlarm = "";
		status = 1; //��ʼ��ʱ��״̬����������
		lastTime = SysUtil.getCurrentTime();
		alarmlevel = 0;
    }
    	
    public boolean equals(Object obj)
    {
       if (obj == null)
          return false;
       if (!(obj instanceof Node))
          return false;
       
       Node that = (Node) obj;
       if (this.id==that.id)    	                  
          return true;
       else 
          return false;
    }

    public int hashCode()
    {   	      	   
   	   int result = 31 + this.id;
   	   return result;
    }
	    
	public String toString()
	{
		StringBuffer info = new StringBuffer(100);
		info.append(id);
		info.append(".");
		info.append(NodeHelper.getNodeCategory(category));
		info.append("ip=");
		info.append(ipAddress);
		info.append(",alias=");
		info.append(alias);
		info.append(",sysDescr=");
		info.append(sysDescr);	
		info.append(",sysContact=");
		info.append(sysContact);
		info.append(",sysLocation=");
		info.append(sysLocation);
		return info.toString();		
	}

	/**
	 * ����Ŀ�����
	 */	
    public int getAvailability() 
    {
    	if(failTimes + normalTimes==0)
    	   return 0;
    	else
		   return (int)(normalTimes * 100/( failTimes + normalTimes));
	}
	
    /**
     * ��moid�ҵ�һ�������Ӷ���
     */
    public MonitoredItem getItemByMoid(String moid)
    {
    	MonitoredItem result = null;
        for(int i=0;i<moidList.size();i++)
        {
        	MonitoredItem tmp = (MonitoredItem)moidList.get(i); 
            if(tmp.getMoid().equals(moid))
            {
            	result = tmp;
            	break;
            }	
        }
        return result;
    }
      
	/**
	 * Ϊ����ͼ��ʾ�ṩ��Ϣ
	 */
	public String getShowMessage()
	{
		//Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(ipAddress);
		//if(ipAllData == null)ipAllData = new Hashtable();
		//Vector ipPingData = (Vector)ShareData.getPingdata().get(ipAddress);
		
		StringBuffer msg = new StringBuffer(200);
		msg.append("<font color='green'>����:");		
		msg.append(NodeHelper.getNodeCategory(category));
		msg.append("</font><br>");
		msg.append("�豸��ǩ:");
		msg.append(alias);
		msg.append("<br>");
		String ipaddress[] = ipAddress.split(":");
		msg.append("IP��ַ:");
		//System.out.println("ipAddress==============="+ipAddress);
		//System.out.println("ipaddress.length==============="+ipaddress.length);
		if(ipaddress.length==1){
			msg.append(ipaddress[0]);
			msg.append("<br>");
		} else {
			msg.append("<br>");
			for(int i=0;i<ipaddress.length;i++){
				msg.append(ipaddress[i]);
				msg.append("<br>");
			}
		}
		/*
		if(status==0||status>1)
		{			
			msg.append("<font color='red'>");
			if(status==0)
			   msg.append("��������</font>");
			else if(status==2)
			   msg.append("�豸æ</font>");
			else
			   msg.append("Ping��ͨ</font>");	
			return msg.toString();
		}	
		
		alarm = false;
		StringBuffer alarmMsg = new StringBuffer(100);
		//SysLogger.info("############################################");
        for(int i=0;i<moidList.size();i++)
        {    
        	
        	NodeMonitor nm = (NodeMonitor)moidList.get(i);
        	if(nm.getNodetype().equalsIgnoreCase("net")){
        		//�����豸
        		if(nm.getCategory().equals("cpu")){
        			//CPU������
        			SysLogger.info("limenvalue0:"+nm.getLimenvalue0()+"   cpuvalue:"+cpuvalue);
        			if(nm.getLimenvalue0()<cpuvalue){
        				//�����澯
        				alarmMsg.append(nm.getAlarmInfo());
        				alarm = true;
        			}
        			msg.append(nm.getDescr() + ":" + cpuvalue + nm.getUnit() + "<br>");
        		}else if(nm.getCategory().equals("ping")){
        			//������ͨ��
        			msg.append(nm.getDescr() + ":" + pingvalue + nm.getUnit() + "<br>");
        		}else if(nm.getCategory().equals("interface")){
        			//�ӿ���Ϣ
        			if(nm.getSubentity().equals("AllInBandwidthUtilHdx")){
        				//�������
        				if(inhdx != null )msg.append(nm.getDescr() + ":" + inhdx + nm.getUnit() + "<br>");
        			}else if(nm.getSubentity().equals("AllOutBandwidthUtilHdx")){
        				//��������
        				if(outhdx != null )msg.append(nm.getDescr() + ":" + outhdx + nm.getUnit() + "<br>");
        			}
        			
        			//msg.append(nm.getDescr() + ":" + pingvalue + nm.getUnit() + "<br>");	
        		}
        	}
        	
        	
        }
	*/
		if(alarm)
		{	
		    msg.append("<font color='red'>--������Ϣ:--</font><br>");
		    //msg.append(alarmMsg.toString());
		    //SysLogger.info(alarmMsg.toString());
		}   
		msg.append("����ʱ��:" + lastTime);	
		//SysLogger.info(ipAddress+"-----------"+lastTime);
        return msg.toString();		
	}
	
	public int getFailTimes() 
	{
		return failTimes;
	}

	public void setFailTimes(int failTimes) 
	{
		this.failTimes = failTimes;
	}

	public int getNormalTimes() 
	{
		return normalTimes;
	}

	public void setNormalTimes(int normalTimes) 
	{
		this.normalTimes = normalTimes;
	}
            	
    public boolean isAlarm() 
    {
		return alarm;
	}

	public void setAlarm(boolean alarm) 
	{
		this.alarm = alarm;
	}

	public String getAlias() 
	{
		return alias;
	}

	public void setAlias(String alias) 
	{
		this.alias = alias;
	}

	public List getAlarmMessage() 
	{
		return alarmMessage;
	}
	
	public int getCategory() 
	{
		return category;
	}

	public void setCategory(int category) 
	{
		this.category = category;
	}

	public int getId() 
	{
		return id;
	}

	public void setId(int id) 
	{
		this.id = id;
	}

	public String getIpAddress() 
	{
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) 
	{
		this.ipAddress = ipAddress;
	}

	public String getAdminIp() {
		return adminIp;
	}

	public void setAdminIp(String adminIp) {
		this.adminIp = adminIp;
	}
	public String getSysDescr() 
	{
		return sysDescr;
	}

	public void setSysDescr(String sysDescr) 
	{
		this.sysDescr = sysDescr;
	}
	
	public String getSysContact() 
	{
		return sysContact;
	}

	public void setSysContact(String sysContact) 
	{
		this.sysContact = sysContact;
	}
	
	public String getSysLocation() 
	{
		return sysLocation;
	}

	public void setSysLocation(String sysLocation) 
	{
		this.sysLocation = sysLocation;
	}

	public void setMoidList(List list) 
	{
		this.moidList = list;
	}

	public List getMoidList() 
	{
		return moidList;
	}
	
	public String getLastAlarm() 
	{
		return lastAlarm;
	}

	public void setLastAlarm(String lastAlarm) 
	{
		this.lastAlarm = lastAlarm;
	}
	
	public String getType() 
	{
		return type;
	}

	public void setType(String type) 
	{
		this.type = type;
	}

	public boolean isManaged() 
	{
		return managed;
	}

	public void setManaged(boolean managed) 
	{
		this.managed = managed;
	}

	public int getStatus() 
	{
		return status;
	}

	public void setStatus(int status) 
	{
		this.status = status;
	}
	
	public int getDiscoverstatus() 
	{
		return discoverstatus;
	}

	public void setDiscoverstatus(int discoverstatus) 
	{
		this.discoverstatus = discoverstatus;
	}

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	public String getNextTime() {
		return nextTime;
	}

	public void setNextTime(String nextTime) {
		this.nextTime = nextTime;
	}
	public Hashtable getAlarmHash() {
		return alarmHash;
	}

	public void setAlarmHash(Hashtable alarmHash) {
		this.alarmHash = alarmHash;
	}
	
	public Hashtable getAlarmPksHash() {
		return alarmPksHash;
	}

	public void setAlarmPksHash(Hashtable alarmPksHash) {
		this.alarmPksHash = alarmPksHash;
	}
	
	public int getTransfer() {
		return transfer;
	}
	public void setTransfer(int transfer) {
		this.transfer = transfer;
	}
	
	public String getAssetid() {
		return assetid;
	}
	public void setAssetid(String assetid) {
		this.assetid = assetid;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getAlarmlevel() 
	{
		return alarmlevel;
	}

	public void setAlarmlevel(int alarmlevel) 
	{
		this.alarmlevel = alarmlevel;
	}
	public String getSendmobiles() {
		return sendmobiles;
	}	
	public void setSendmobiles(String sendmobiles) {
		this.sendmobiles = sendmobiles;
	}
	public String getSendemail() {
		return sendemail;
	}	
	public void setSendemail(String sendemail) {
		this.sendemail = sendemail;
	}
	public String getSendphone() {
		return sendphone;
	}	
	public void setSendphone(String sendphone) {
		this.sendphone = sendphone;
	}
	public String getBid() {
		return bid;
	}	
	public void setBid(String bid) {
		this.bid = bid;
	}
	public int getEndpoint() {
		return this.endpoint;
	}
	public void setEndpoint(int endpoint) {
		this.endpoint = endpoint;
	}
}

