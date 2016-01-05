/**
 * <p>Description:probe the router table</p>
 * <p>Company: dhcc.com</p>
 * @author hukelei
 * @project afunms
 * @date 2009-05-15
 */

package com.afunms.discovery;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

import com.afunms.common.util.*;
import com.afunms.polling.task.ThreadPool;

public class IPRouterProbeThread_SOLO extends BaseThread
//public class IPRouterProbeThread implements Runnable
{
   private Host node;
   private static SnmpUtil snmp = SnmpUtil.getInstance();	
   
   public IPRouterProbeThread_SOLO(Host node)
   {
      this.node = node;
   }
   
   public void run()
   { 
	   if(DiscoverEngine.getInstance().getStopStatus() == 1)return;
	   SysLogger.info("��ʼ����"+node.getIpAddress()+"·�ɱ�����");
	   //�����ҳ��Ϸ�������
	   List subNetList = new ArrayList();
	   try{
		   subNetList = SnmpUtil.getInstance().getSubNetList(node.getIpAddress(),node.getCommunity()); 
	   }catch(Exception ex){
    	  ex.printStackTrace();
	   }
	   List netshieldList = DiscoverResource.getInstance().getNetshieldList();
	   //�����������ڻ�������Ϊ0,��Ը��豸���е�ַת�������
  	  	if(subNetList==null||subNetList.size()==0)   
  	  	{
  	  		node.updateCount(1);
  	  		setCompleted(true);
  	  		return; 	
  	  	}	
  	  	//SysLogger.info("������Ϊ : "+subNetList.size());  	  
  	  	for(int i=0;i<subNetList.size();i++)
  	  	{
  	  		SubNet subNet = (SubNet)subNetList.get(i);  
  	  		IfEntity ifEntity = node.getIfEntityByIndex(subNet.getIfIndex());
  	  		if(ifEntity==null) continue;    	  
    	  
  	  		if(!ifEntity.getSubNetList().contains(subNet))
  	  		{	  
  	  			ifEntity.getSubNetList().add(subNet);
  	  			DiscoverEngine.getInstance().addSubNet(subNet);    		     
  	  		}   
    	      	  
  	  		//===========���ں����豸,��Ϊ��ʼ��ȷ�������ĸ�����,������ſ���ȷ��=============
  	  		if(node.getLocalNet()==-1
    	    && NetworkUtil.isValidIP(subNet.getNetAddress(),subNet.getNetMask(),node.getIpAddress()))
  	  			node.setLocalNet(subNet.getId());   	  
      	}	
	          
  	  		//���ҳ��Ϸ���router
  	  		List routerList = SnmpUtil.getInstance().getRouterList(node.getIpAddress(),node.getCommunity()); 
  	  		node.setRouteList(routerList);
  	  		if(routerList==null||routerList.size()==0)   
  	  		{
  	  			//��·�ɱ��б�Ϊ�յ�ʱ��,��Ը��豸�ĵ�ַת������вɼ�����
  	  			SysLogger.info("�豸"+node.getIpAddress()+"·�ɱ��¼Ϊ��,����и��豸�ĵ�ַת������з�������");
  	  			//node.updateCount(1);
  	  			//ȡ����ARP����з���
  	  			node.updateCount(0);
  	  			setCompleted(true);
  	  			return; 	
  	  		}	
  	  		SysLogger.info("�豸"+node.getIpAddress()+"·�ɱ��¼��Ϊ: "+routerList.size());
  	  		List nextRouterList = new ArrayList();
  	  		List doRouterList = new ArrayList();
  	  		for(int i=0;i<routerList.size();i++){
  	  			IpRouter ipr = (IpRouter)routerList.get(i);	
  	  			String nextRouter = null;	   
  	  			if(ipr.getType()==4){
  	  				//indirect
  	  				nextRouter = ipr.getNextHop();
  	  			}    	 
  	  			else
  	  			{
  	  				continue;
  	  			}
	  	  		if(isHostExist(nextRouter)){
	  	  			continue;
	  	  		}
	  	  		DiscoverEngine.getInstance().getHostList();
  	  			//�жϸ�IP�Ƿ��Ѿ�����
  	  			if(nextRouterList.contains(nextRouter))continue;
  	  			nextRouterList.add(nextRouter);
  	  			doRouterList.add(ipr);
  	  		}
  	  		Date startdate = new Date();
  	  		//IPRouterProbeSubThread probeThread = null;
    		// �����̳߳�
    		ThreadPool threadPool = new ThreadPool(doRouterList.size());														
    		// ��������
  	  		for(int i=0;i<doRouterList.size();i++)
  	  		{
  	  			IpRouter ipr = (IpRouter)doRouterList.get(i);	
  	  			threadPool.runTask(RouterThread_SOLO.createTask(ipr, node));   	   
  	  		}
  	  		// �ر��̳߳ز��ȴ������������
    		threadPool.join();
    		threadPool.close();
    	  	threadPool = null;
    		Date enddate = new Date();
    		SysLogger.info("##############################");
			SysLogger.info("### "+node.getIpAddress()+" ����ʱ�� "+(enddate.getTime()-startdate.getTime())+"####");
			SysLogger.info("##############################");
    		
    		
  	  		//node.updateCount(1);
  	  		//ȡ����ARP����з���
  	  		node.updateCount(0);
  	  		SysLogger.info("������·�ɱ��,��ʼ����"+node.getIpAddress()+"�豸�ĵ�ַת����");
  	  		SysLogger.info("������·�ɱ��,��������"+node.getIpAddress()+"�豸�ĵ�ַת����");
  	  		DiscoverEngine.getInstance().addDiscoverdcount();
  	  		//snmp = null;
  	  		setCompleted(true);
   } //end_run
   
   /**
    * ȷ��һ���豸�Ƿ��Ѿ�����
    * ��ǰLinkֻ����ʼIP����ʼ�˿�����ID
    */
   private boolean isHostExist(String ip)
   {
	List hostList = DiscoverEngine.getInstance().getHostList();
	if(hostList == null)hostList = new ArrayList();
   	boolean exist = false;
   	for(int i=0;i<hostList.size();i++)
   	{
   		Host tmpNode = (Host)hostList.get(i);
   		if(tmpNode.getCategory()==5 || tmpNode.getCategory()==6 ) continue; //��ӡ�������ǽ
   		
   		if(tmpNode.getCategory()==2 || tmpNode.getCategory()==3 || tmpNode.getCategory()==7)//����·�ɽ������򽻻��������
   		{
				if(tmpNode.getIpAddress().equalsIgnoreCase(ip))
					{
						//existHost = tmpNode;
						SysLogger.info("�ѷ��ֵ��豸�б����Ѿ�����"+tmpNode.getCategory()+"���豸:"+ip);	
						exist = true;
						break;
					}
					else
					{
						//�жϱ���IP�Ƿ����
						List aliasIPs = tmpNode.getAliasIPs();
						if(aliasIPs != null && aliasIPs.size()>0){
							if(aliasIPs.contains(ip)){
								//existHost = tmpNode;
								SysLogger.info("�ѷ��ֵ��豸�б����Ѿ�����"+tmpNode.getCategory()+"���豸:"+ip);
								exist = true;
								break;
							}
						}
					}
   		}

   	}
   	//SysLogger.info("����Ϊ"+node.getCategory()+"���豸"+node.getIpAddress()+"Ŀǰ����Ϊ--"+exist);
   	  	
      	return exist;
   }
   
}