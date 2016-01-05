/**
 * <p>Description:probe the router table</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-13
 */

package com.afunms.discovery;

import java.util.*;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.*;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.snmp.cpu.BDComCpuSnmp;
import com.afunms.polling.snmp.cpu.CiscoCpuSnmp;
import com.afunms.polling.snmp.cpu.DLinkCpuSnmp;
import com.afunms.polling.snmp.cpu.EnterasysCpuSnmp;
import com.afunms.polling.snmp.cpu.H3CCpuSnmp;
import com.afunms.polling.snmp.cpu.MaipuCpuSnmp;
import com.afunms.polling.snmp.cpu.NortelCpuSnmp;
import com.afunms.polling.snmp.cpu.RadwareCpuSnmp;
import com.afunms.polling.snmp.cpu.RedGiantCpuSnmp;
import com.afunms.polling.snmp.cpu.ZTECpuSnmp;
import com.afunms.polling.snmp.fan.CiscoFanSnmp;
import com.afunms.polling.snmp.fan.H3CFanSnmp;
import com.afunms.polling.snmp.flash.BDComFlashSnmp;
import com.afunms.polling.snmp.flash.CiscoFlashSnmp;
import com.afunms.polling.snmp.flash.H3CFlashSnmp;
import com.afunms.polling.snmp.interfaces.ArpSnmp;
import com.afunms.polling.snmp.interfaces.FdbSnmp;
import com.afunms.polling.snmp.interfaces.InterfaceSnmp;
import com.afunms.polling.snmp.interfaces.PackageSnmp;
import com.afunms.polling.snmp.interfaces.RouterSnmp;
import com.afunms.polling.snmp.memory.BDComMemorySnmp;
import com.afunms.polling.snmp.memory.CiscoMemorySnmp;
import com.afunms.polling.snmp.memory.DLinkMemorySnmp;
import com.afunms.polling.snmp.memory.EnterasysMemorySnmp;
import com.afunms.polling.snmp.memory.H3CMemorySnmp;
import com.afunms.polling.snmp.memory.MaipuMemorySnmp;
import com.afunms.polling.snmp.memory.NortelMemorySnmp;
import com.afunms.polling.snmp.memory.RedGiantMemorySnmp;
import com.afunms.polling.snmp.ping.PingSnmp;
import com.afunms.polling.snmp.power.CiscoPowerSnmp;
import com.afunms.polling.snmp.power.H3CPowerSnmp;
import com.afunms.polling.snmp.system.SystemSnmp;
import com.afunms.polling.snmp.temperature.BDComTemperatureSnmp;
import com.afunms.polling.snmp.temperature.CiscoTemperatureSnmp;
import com.afunms.polling.snmp.temperature.H3CTemperatureSnmp;
import com.afunms.polling.snmp.voltage.CiscoVoltageSnmp;
import com.afunms.polling.snmp.voltage.H3CVoltageSnmp;
import com.afunms.topology.dao.DiscoverConfigDao;
import com.afunms.topology.model.DiscoverConfig;

public class CDPSubThread 
//public class IPNetProbeThread implements Runnable
{
   private Host node;
   private List addressList;
   
   /**
   ��������
	 */	
	public static Runnable createTask(final CdpCachEntryInterface cdp,final Host node) {
   return new Runnable() {
       public void run() {
    	   if(DiscoverEngine.getInstance().getStopStatus() == 1)return;
    	   SnmpUtil snmp = SnmpUtil.getInstance();
    	   Set shieldList = DiscoverResource.getInstance().getShieldSet();
    	   List netshieldList = DiscoverResource.getInstance().getNetshieldList();
    	   List netincludeList = DiscoverResource.getInstance().getNetincludeList();
    	   try{
    		  String cdpip = cdp.getIp();
    		  String cdpportdesc = cdp.getPortdesc();
    		  DiscoverConfigDao dao = new DiscoverConfigDao();

			  List faildIpList = DiscoverEngine.getInstance().getFaildIpList();
		      //existHost = null;
		      if(faildIpList != null && faildIpList.size()>0)
		      {
		    	  for(int i=0;i<faildIpList.size();i++){
		    		  SysLogger.info(" �ɼ�ʧ�ܵ�IP����    "+(String)faildIpList.get(i)+" ");
		    	  }
		    	  if(faildIpList.contains(cdpip))return;
		      }
    		  
    		//�жϸ�IP�Ƿ���ֻ��Ҫ���ֵ�������
      		  int netincludeflag = 0;
      		  if(netincludeList != null && netincludeList.size()>0)
      		  {
      			   long longip = DiscoverEngine.getInstance().ip2long(cdpip);
      			   for(int k=0;k<netincludeList.size();k++)
      			   {
      				   Vector netinclude = (Vector)netincludeList.get(k);
      				   if(netinclude != null && netinclude.size()==2)
      				   {
      					   try{
      						   if(longip>=((Long)netinclude.get(0)).longValue() && longip<=((Long)netinclude.get(1)).longValue())
      						   {
      							   SysLogger.info("�豸IP "+cdpip+"������Ҫ���ֵ�����");
      							   netincludeflag = 1;
      							   break;
      						   }
      					   }catch(Exception ex){
      						   ex.printStackTrace();
      					   }
      				   }
      			   }
      			   if(netincludeflag == 0)return;
      		   }
    		  
    		  //�жϸ�IP�Ƿ��ڱ����ε�������
      		  int netshieldflag = 0;
      		  if(netshieldList != null && netshieldList.size()>0)
      		  {
      			   long longip = DiscoverEngine.getInstance().ip2long(cdpip);
      			   for(int k=0;k<netshieldList.size();k++)
      			   {
      				   Vector netshield = (Vector)netshieldList.get(k);
      				   if(netshield != null && netshield.size()==2)
      				   {
      					   try{
      						   if(longip>=((Long)netshield.get(0)).longValue() && longip<=((Long)netshield.get(1)).longValue())
      						   {
      							   SysLogger.info("�豸IP "+cdpip+"���ڱ���������");
      							   netshieldflag = 1;
      							   break;
      						   }
      					   }catch(Exception ex){
      						   ex.printStackTrace();
      					   }
      				   }
      			   }
      			   if(netshieldflag == 1)return;
      		   }
    		
    		  
    		  
    		  //���˵����ε��豸
    		  //�жϸ�IP�Ƿ����Ѿ����ε�IP��ַ
    		  
//    		  List shieldList = new ArrayList();
//    		  try{
//    			  shieldList = dao.listByFlag("shield");
//    		  }catch(Exception e){
//    			  e.printStackTrace();
//    		  }finally{
//    			  dao.close();
//    		  }
    		  int isShield = 0;
    		  for(int sh=0;sh<shieldList.size();sh++)
    		  {
    			  if(shieldList.contains(cdpip))return;
//    			  DiscoverConfig vo = (DiscoverConfig)shieldList.(sh);            
//    			  if(vo.getAddress().equalsIgnoreCase(cdpip)) 
//    			  {
//    				  isShield = 1;
//    				  break;
//    			  }
    		  }
//    		  if(isShield == 1) continue;
      	
      	
    		  SysLogger.info(node.getIpAddress()+"-----cdpIP:"+cdpip+"=======cdpdesc:"+cdpportdesc);    	  
    	  
    	  
    		  IfEntity nodeifEntity = null;
    		  IfEntity host_ifEntity = null;
    		  SysLogger.info(node.getIpAddress()+" CDP���� "+cdpportdesc);

    		  //if(cdplist==null || cdplist.size()==0)continue;
		  
    		  //�ж��Ѿ����ֵ��豸�б����Ƿ��Ѿ����ڸ�IP(���߸�IP��Ӧ�Ĺ���IP)
    		  SysLogger.info("��ʼ�ж�IP "+cdpip+"�Ƿ��Ѿ����Ѿ����ֵ�IP����IP����");
		      List hostList = DiscoverEngine.getInstance().getHostList();
		      Host existHost = null;
		      if(hostList != null && hostList.size()>0)
		      {
			    	for(int k=0;k<hostList.size();k++)
			    	{
			    		Host tmpNode = (Host)hostList.get(k);
			    		if(tmpNode.getIpAddress().equalsIgnoreCase(cdpip)){
			    			existHost = tmpNode;
			    			SysLogger.info("�ѷ��ֵ��豸�б����Ѿ�����"+tmpNode.getCategory()+"���豸:"+cdpip);		    			
			    			break;
			    		}
			    		else
			    		{
			    			//�жϱ���IP�Ƿ����
			    			List aliasIPs = tmpNode.getAliasIPs();
			    			if(aliasIPs != null && aliasIPs.size()>0)
			    			{
			    				if(aliasIPs.contains(cdpip))
			    				{
			    					existHost = tmpNode;
			    					SysLogger.info("�ѷ��ֵ��豸�б����Ѿ�����"+tmpNode.getCategory()+"���豸:"+cdpip);
			    					break;
			    				}
			    			}
			    		}
			    		
			    		
//  	  					//�ж�ϵͳ����
//			    		SysLogger.info("####$$$$ begin CDPSubThread getSysOid ##########"+cdpip); 
//			    		String community = SnmpUtil.getInstance().getCommunity(cdpip);  
//			    		SysLogger.info("####$$$$ end CDPSubThread getSysOid ##########"+cdpip); 
//			    		if(community==null){
//			    			  faildIpList.add(cdpip);
//			    			  DiscoverEngine.getInstance().getFaildIpList().add(cdpip);
//			    			continue;
//			    		}
//  	  					Hashtable sysGroupProperty = snmp.getSysGroup(cdpip,community);
//  	  					if(sysGroupProperty != null)
//  	  					{
//  	  						if(sysGroupProperty.containsKey("sysName"))
//  	  						{
//  	  							String sysName = (String)sysGroupProperty.get("sysName");
//  	  							if(sysName != null && sysName.length()>0)
//  	  							{
//  	  								if(sysName.equalsIgnoreCase(tmpNode.getSysName()))
//  	  								{
//  	  									existHost = tmpNode;
//  	  									break;
//  	  								}
//  	  							}
//  	  						}
//  	  					}
			    	}
		      }
		      SysLogger.info("�����ж�IP "+cdpip+"�Ƿ��Ѿ����Ѿ����ֵ�IP����IP����");
		      
		      //�ж��Ѿ�����ʧ�ܵ��豸�б����Ƿ��Ѿ����ڸ�IP
			  SysLogger.info("��ʼ�ж�IP "+cdpip+"�Ƿ��Ѿ����Ѿ�����ʧ�ܵ�IP����IP����");

		      SysLogger.info("�����ж�IP "+cdpip+"�Ƿ��Ѿ����Ѿ�����ʧ�ܵ�IP����IP����");
		      List cdplist = null;
		      Host host = new Host();
		      if(existHost != null)
		      {
		    	  host = existHost;
		    	  cdplist = host.getCdpList();
		    	  return;
		      }
		      else
		      {
	    		  String community = SnmpUtil.getInstance().getCommunity(cdpip);      	  
	    		  if(community==null){
	    			  //faildIpList.add(cdpip);
	    			  DiscoverEngine.getInstance().getFaildIpList().add(cdpip);
	    			  return;      	      
	    		  }
	    		  String sysOid = SnmpUtil.getInstance().getSysOid(cdpip,community);
	    		  if(sysOid == null){
	    			  //faildIpList.add(cdpip);
	    			  DiscoverEngine.getInstance().getFaildIpList().add(cdpip);
	    			  return;
	    		  }
	    		  int deviceType = SnmpUtil.getInstance().checkDevice(cdpip,community,sysOid);
			  
	    		  
	    		  try{
	    			  cdplist = SnmpUtil.getInstance().getCiscoCDPList(cdpip,community);
	    		  }catch(Exception e){
	    			  e.printStackTrace();
	    		  }
		    	  host.setCategory(deviceType); 
		    	  host.setCommunity(community);
		    	  host.setWritecommunity(DiscoverEngine.getInstance().getWritecommunity());
		    	  host.setSnmpversion(DiscoverEngine.getInstance().getSnmpversion());
		    	  host.setSysOid(sysOid); 
	          
		    	  host.setSuperNode(node.getId());
		    	  host.setLocalNet(node.getLocalNet());
		    	  //host.setNetMask(subnet.getNetMask());
		    	  host.setIpAddress(cdpip);
		    	  host.setLayer(node.getLayer() + 1);
		    	  try{
		    		  host.setIfEntityList(SnmpUtil.getInstance().getIfEntityList(cdpip, community, deviceType));
		    	  }catch(Exception e){
		    		  
		    	  }
		      }
		      try{
		    	  host_ifEntity = host.getIfEntityByDesc(cdpportdesc);
		      }catch(Exception e){
			  
		      }
		      if(cdplist == null || cdplist.size()==0)
		      {
		    	  for(int k=0;k<cdplist.size();k++)
		    	  {
		    		  CdpCachEntryInterface node_cdp = (CdpCachEntryInterface)cdplist.get(k);
		    		  String node_cdpip = node_cdp.getIp();
		    		  String node_cdpportdesc = node_cdp.getPortdesc();
		    		  if(node_cdpip != null && node_cdpip.trim().length()>0)
		    		  {
		    			  //��Ҫ����IP�������ж�
		    			  if(node.getAliasIPs().contains(node_cdpip))
		    			  {
		    				  nodeifEntity = node.getIfEntityByDesc(node_cdpportdesc);
		    				  break;
		    			  }
		    		  }
		    	  }
		      }
		      //if(isHostExist(host,link)) return;
		      int sublinktype = SystemConstant.NONEPHYSICALLINK;
		      if(nodeifEntity != null && host_ifEntity != null)
		      {
		    	  sublinktype = SystemConstant.BOTHPHYSICALLINK;
		      }else if(nodeifEntity != null && host_ifEntity == null )
		      {
		    	  sublinktype = SystemConstant.STARTPHYSICALLINK;
		      }else if(nodeifEntity == null && host_ifEntity != null){
		    	  sublinktype = SystemConstant.ENDPHYSICALLINK;
		      }
		      if(nodeifEntity == null){
		    	  SysLogger.info("#############"+node.getIpAddress()+"û����ص�CDP���ӹ�ϵ####################");
		    	  nodeifEntity = node.getIfEntityByIP(node.getIpAddress());
		      }
		      Link link = new Link();
		      link.setStartId(node.getId());
		      link.setStartIndex(nodeifEntity.getIndex());
		      if(nodeifEntity.getIpAddress() != null && nodeifEntity.getIpAddress().trim().length()>0)
		    	  link.setStartIp(nodeifEntity.getIpAddress());
		      else	      
		    	  link.setStartIp(node.getIpAddress());
		      link.setStartPort(nodeifEntity.getIndex());
		      link.setStartPhysAddress(nodeifEntity.getPhysAddress());       
		      link.setStartDescr(nodeifEntity.getDescr()); 	   
		      link.setEndIp(cdpip);
		      if(host_ifEntity != null ){
		    	  link.setEndIndex(host_ifEntity.getIndex());
		    	  link.setEndDescr(host_ifEntity.getDescr());
		    	  link.setEndPhysAddress(host_ifEntity.getPhysAddress());
		    	  link.setEndPort(host_ifEntity.getPort());
		      }
		      link.setFindtype(SystemConstant.ISCDP);//����CDP����
		      link.setSublinktype(sublinktype);
		      try{
		    	  DiscoverEngine.getInstance().addHost(host,link);
		      }catch(Exception e){			  
		    	  e.printStackTrace();
		    	  return;
			  /*
			  node.updateCount(1);
			  setCompleted(true);
			  return;
			  */
		      }
    	   }catch(Exception ex){
    		   ex.printStackTrace();
    	   }
       }
   };
	}
}