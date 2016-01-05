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

public class RouterThread_SOLO 
//public class IPNetProbeThread implements Runnable
{
   private Host node;
   private List addressList;
   
   /**
   ��������
	 */	
	public static Runnable createTask(final IpRouter ipr,final Host node) {
   return new Runnable() {
       public void run() {
       	try {  
       		if(DiscoverEngine.getInstance().getStopStatus() == 1)return;
       		SnmpUtil snmp = SnmpUtil.getInstance();
    	   SysLogger.info("### ��ʼ�����豸"+ipr.getNextHop()+"��·�ɱ� ###");	    
    	   Set shieldList = DiscoverResource.getInstance().getShieldSet();
    	   List netshieldList = DiscoverResource.getInstance().getNetshieldList();
    	   List netincludeList = DiscoverResource.getInstance().getNetincludeList();
    	   IfEntity ifEntity = null;
    	   IfEntity endifEntity = null;
    	  			SubNet subnet = null;
          	 
    	  			String nextRouter = ipr.getNextHop();	   
//    	  			if(ipr.getType()==4){
//    	  				//indirect
//    	  				nextRouter = ipr.getNextHop();
//    	  			}    	 
//    	  			else
//    	  			{
//    	  				continue;
//    	  			}
    	  			SysLogger.info(node.getIpAddress()+"·�ɱ�####ifindex:"+ipr.getIfIndex()+"    nexthop:"+ipr.getNextHop()+"   type:"+ipr.getType()+"   proto:"+ipr.getProto());
    	  			if(nextRouter != null && nextRouter.trim().length()>0){
    	  				SysLogger.info(node.getIpAddress()+"������.0.0��β��·�ɱ�...");
    	  				if(nextRouter.endsWith(".0.0")){
    	  					return;
    	  				}
    	  				SysLogger.info(node.getIpAddress()+"������.0��β��·�ɱ�...");
    	  				if(nextRouter.endsWith(".0")){
    	  					return;
    	  				}
    	  			}
    	  			//�жϸ�IP�Ƿ��Ѿ�����
//    	  			if(nextRouterList.contains(nextRouter))continue;
//    	  			nextRouterList.add(nextRouter);
          
    	  			ifEntity = node.getIfEntityByIndex(ipr.getIfIndex());  	      
    	  			if(ifEntity==null)
    	  			{
    	  				if(node.getIfEntityList() != null && node.getIfEntityList().size()>0)
    	  				{
    	  					ifEntity = (IfEntity)node.getIfEntityList().get(0);
    	  					SysLogger.info(node.getIpAddress()+"·�ɱ��л�ȡ�˿ڲ�����,���õ�һ���˿���������: IP��ַ : "+ifEntity.getIndex());
    	  					ipr.setIfIndex(ifEntity.getIndex());
    	  				}
    	  				else
    	  				{
    	  					SysLogger.info(node.getIpAddress()+"·�ɱ��л�ȡ�˿ڲ�����,�ҽӿ��б�ҲΪ�� ��һ��IP:"+nextRouter);
    	  					return;
    	  				}     
    	  			}
//    	  			subnet = ifEntity.isValidIP(nextRouter);          
//    	  			if(subnet==null){
//    	  				SysLogger.info("��"+node.getIpAddress()+"·�ɱ��л�ȡ��Ҫ�����豸,�˿�:"+ipr.getIfIndex()+" IP��ַ : "+nextRouter+" �����������ڱ�����"); 
//    	  				continue;
//    	  			}
    	  			SysLogger.info("��"+node.getIpAddress()+"·�ɱ��л�ȡ��Ҫ�����豸,�˿�:"+ipr.getIfIndex()+" IP��ַ : "+nextRouter);   
    	  			
    	  			String community = SnmpUtil.getInstance().getCommunity(nextRouter);     	  
    	  			if(community==null) return;
    	  			//�ж��Ѿ����ֵ��豸�б����Ƿ��Ѿ���IP(���߸�IP��Ӧ�Ĺ���IP)
    	  			List hostList = DiscoverEngine.getInstance().getHostList();
    	  			Host existHost = null;
    	  			if(hostList != null && hostList.size()>0)
    	  			{
    	  				for(int k=0;k<hostList.size();k++)
    	  				{
    	  					Host tmpNode = (Host)hostList.get(k);
    	  					if(tmpNode.getIpAddress().equalsIgnoreCase(nextRouter))
    	  					{
    	  						existHost = tmpNode;
    	  						SysLogger.info("�ѷ��ֵ��豸�б����Ѿ�����"+tmpNode.getCategory()+"���豸:"+nextRouter);		    			
    	  						break;
    	  					}
    	  					else
    	  					{
    	  						//�жϱ���IP�Ƿ����
    	  						List aliasIPs = tmpNode.getAliasIPs();
    	  						if(aliasIPs != null && aliasIPs.size()>0){
    	  							if(aliasIPs.contains(nextRouter)){
    	  								existHost = tmpNode;
    	  								SysLogger.info("�ѷ��ֵ��豸�б����Ѿ�����"+tmpNode.getCategory()+"���豸:"+nextRouter);
    	  								break;
    	  							}
    	  						}
    	  					}
    	  					//�ж�ϵͳ����
    	  					Hashtable sysGroupProperty = snmp.getSysGroup(nextRouter,community);
    	  					if(sysGroupProperty != null){
    	  						if(sysGroupProperty.containsKey("sysName")){
    	  							String sysName = (String)sysGroupProperty.get("sysName");
    	  							if(sysName != null && sysName.length()>0){
    	  								if(sysName.equalsIgnoreCase(tmpNode.getSysName())){
    	  									existHost = tmpNode;
    	  									SysLogger.info("�ѷ��ֵ��豸�б����Ѿ���������Ϊ"+sysName+"���豸:"+nextRouter);
    	  									break;
    	  								}
    	  							}
    	  						}
    	  					}
    	  				}
    	  			}
          
    	  			//�жϸ�IP�Ƿ��ڱ����ε�������
    	  			int netshieldflag = 0;
    	  			if(netshieldList != null && netshieldList.size()>0)
    	  			{
    	  				long longip = DiscoverEngine.getInstance().ip2long(nextRouter);
    	  				for(int k=0;k<netshieldList.size();k++)
    	  				{
    	  					Vector netshield = (Vector)netshieldList.get(k); 
    	  					if(netshield != null && netshield.size()==2)
    	  					{
    	  						try{
    	  							if(longip>=((Long)netshield.get(0)).longValue() && longip<=((Long)netshield.get(1)).longValue())
    	  							{
    	  								SysLogger.info("�豸IP "+nextRouter+"���ڱ���������");
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
          
          
    	  			String sysOid = "";
    	  			int deviceType = 0;
    	  			if(existHost != null){
    	  				//����IP�Ѿ�����	  				
    	  				sysOid = existHost.getSysOid();
    	  				deviceType = existHost.getCategory();
    	  				return;
    	  			}
    	  			else
    	  			{
    	  				//������
    	  				sysOid = SnmpUtil.getInstance().getSysOid(nextRouter,community);
    	  				deviceType = SnmpUtil.getInstance().checkDevice(nextRouter,community,sysOid);
    	  			}
          
//    	  			if(deviceType!=1&&deviceType!=2)
//    	  			{	 			  
//    	  				//Ϊʲôһ����Ҫ��·���豸���ܽ�����һ������????��ȷ��
//    	  				DiscoverEngine.getInstance().getExistIpList().add(nextRouter);
//    	  				SysLogger.info("==========================================");
//    	  				SysLogger.info("�豸IP "+nextRouter+"����·���豸,ֹͣ��һ������");
//    	  				SysLogger.info("==========================================");
//    	  				continue;//����·���豸����ֹͣ
//    	      
//    	  			}
    	  			Host host = new Host();
    	  			if(existHost != null){
    	  				host = existHost;
    	  				return;
    	  			}else{
    	  				host.setCategory(deviceType); 
    	  				host.setCommunity(community);
    	  				host.setWritecommunity(DiscoverEngine.getInstance().getWritecommunity());
    	  				host.setSnmpversion(DiscoverEngine.getInstance().getSnmpversion());
    	  				host.setSysOid(sysOid); 
    	  				host.setRouter(true);
    	  				host.setSuperNode(-1);
    	  				if(subnet == null){
    	  					host.setLocalNet(1);
    	  					host.setNetMask("255.255.255.0");
    	  				}else{
    	  					host.setLocalNet(subnet.getId());
    	  					host.setNetMask(subnet.getNetMask());
    	  				}
    	  				
    	  				host.setIpAddress(nextRouter);
    	  				host.setLayer(node.getLayer() + 1);
    	  			}
          
    	  
    	  			/**
    	  			 * ��һ���϶���·�ɻ����㽻��
    	  			 */
    	  			Link link = new Link();		            		           
    	  			link.setStartId(node.getId());          
    	  			link.setStartIndex(ifEntity.getIndex());
    	  			link.setStartIp(ifEntity.getIpAddress());		 
    	  			link.setStartPort(ifEntity.getIndex());
    	  			link.setStartPhysAddress(ifEntity.getPhysAddress());       
    	  			link.setStartDescr(ifEntity.getDescr()); 	   
    	  			link.setEndIp(nextRouter);
    	  			link.setFindtype(SystemConstant.ISRouter);//����·�ɷ���
    	  			link.setSublinktype(SystemConstant.NONEPHYSICALLINK);//��·�ɱ��ֵ������п������߼����ӹ�ϵ
    	  			SysLogger.info("��"+node.getIpAddress()+"·�ɱ��л�ȡ�豸,�˿�:"+ipr.getIfIndex()+" IP��ַ : "+nextRouter+",��ʼ�������豸");
    	  			DiscoverEngine.getInstance().addHost_SOLO(host,link);	   
    	  			DiscoverEngine.getInstance().addDiscoverdcount(); 
    	  			snmp = null;       		
           }catch(Exception exc){
           	
           }
       }
   };
	}
}