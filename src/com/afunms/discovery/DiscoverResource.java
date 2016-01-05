/**
 * <p>Description:topology discovery resource</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-12
 */

package com.afunms.discovery;

import java.util.*;

import com.afunms.common.util.SysLogger;

public class DiscoverResource
{
    private int maxThreads;    //tomcat����߳���
    private int perThreadIps;  //ÿ���̴߳����IP��
    private Set communitySet;  //ȫ�ֹ�ͬ�弯��
    private Set shieldSet;     //Ҫ���ε����μ���
    private Map SpecifiedCommunity;  //ָ���豸��community,keyΪIP��Ϣ��valueΪ��������Ϣ
    private Map deviceType;       //��֪�豸
    private List serviceList;
    private String community;//��������
    private List netshieldList;//��Ҫ���ε�����
    private List netincludeList;//ֻ�跢������
    private Set failedList;     //Ҫ���ε����μ���

	private static DiscoverResource instance = new DiscoverResource();
    public static DiscoverResource getInstance()
    {
    	if(instance == null) instance = new DiscoverResource();//�������·���,��Ҫ���³�ʼ������
       return instance;
    }
    
	private DiscoverResource()
	{		
	}
    

	//ж��
	public void unload() 
	{
		instance = null;
		SysLogger.info("DiscoverResource.unload()");
	}
	
	public List getNetshieldList() 
	{
		return netshieldList;
	}
	
	public void setNetshieldList(List netshieldList) 
	{
		this.netshieldList = netshieldList;
	}
	
	public List getNetincludeList() 
	{
		return netincludeList;
	}
	
	public void setNetincludeList(List netincludeList) 
	{
		this.netincludeList = netincludeList;
	}
	
	public String getCommunity() 
	{
		return community;
	}
	
	public void setCommunity(String community) 
	{
		this.community = community;
	}
	
	public Map getDeviceType() 
	{
		return deviceType;
	}
	
	public void setDeviceType(Map deviceType) 
	{
		this.deviceType = deviceType;
	}
	
	public Set getCommunitySet() 
	{
		return communitySet;
	}
	
	public void setCommunitySet(Set communitySet) 
	{
		this.communitySet = communitySet;
	}
	
	public int getMaxThreads() 
	{
		return maxThreads;
	}
	
	public void setMaxThreads(int maxThreads) 
	{
		this.maxThreads = maxThreads;
	}
	
	public int getPerThreadIps() 
	{
		return perThreadIps;
	}
	
	public void setPerThreadIps(int perThreadIps) 
	{
		this.perThreadIps = perThreadIps;
	}
	
	public Set getShieldSet() 
	{	
		return shieldSet;
	}
	
	public void setShieldSet(Set shieldSet) 
	{
		this.shieldSet = shieldSet;
	}
	
	public Map getSpecifiedCommunity() 
	{
		return SpecifiedCommunity;
	}
	
	public void setSpecifiedCommunity(Map SpecifiedCommunity) 
	{
		this.SpecifiedCommunity = SpecifiedCommunity;
	}  
	
	public List getServiceList() 
	{
		return serviceList;
	}
	
	public void setServiceList(List serviceList) 
	{
		this.serviceList = serviceList;
	}

	public Set getFailedList() {
		return failedList;
	}

	public void setFailedList(Set failedList) {
		this.failedList = failedList;
	}	    
}