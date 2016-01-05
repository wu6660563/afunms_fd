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

public class NetMediThread_SOLO 
//public class IPNetProbeThread implements Runnable
{
   private Host node;
   private List addressList;
   
   /**
   ��������
	 */	
	public static Runnable createTask(final IpAddress ipAddr,final Host node) {
   return new Runnable() {
       public void run() {
    	   if(DiscoverEngine.getInstance().getStopStatus() == 1)return;
    	   SnmpUtil snmp = SnmpUtil.getInstance();
    	   Set shieldList = DiscoverResource.getInstance().getShieldSet();
    	   List netshieldList = DiscoverResource.getInstance().getNetshieldList();
    	   List netincludeList = DiscoverResource.getInstance().getNetincludeList();
    	   IfEntity ifEntity = null;
    	   IfEntity endifEntity = null;
    	   try{
    		   node.updateCount(2);     	      	    		   
    		   //�жϸ�IP�Ƿ����Ѿ����ε�IP��ַ
    		   SysLogger.info("��"+node.getIpAddress()+"�ĵ�ַת������IP "+ipAddr.getIpAddress()+",��ʼ����");
    		 //�жϸ�IP�Ƿ���ֻ��Ҫ���ֵ�������
       		  int netincludeflag = 0;
       		  if(netincludeList != null && netincludeList.size()>0){
       			   long longip = DiscoverEngine.getInstance().ip2long(ipAddr.getIpAddress());
       			   for(int k=0;k<netincludeList.size();k++){
       				   Vector netinclude = (Vector)netincludeList.get(k);
       				   if(netinclude != null && netinclude.size()==2){
       					   try{
       						   if(longip>=((Long)netinclude.get(0)).longValue() && longip<=((Long)netinclude.get(1)).longValue()){
       							   SysLogger.info("�豸IP "+ipAddr.getIpAddress()+"������Ҫ���ֵ�����");
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
    		   
    		   SysLogger.info("��ʼ����IP "+ipAddr.getIpAddress()+"�Ƿ�����");
    		   if(shieldList != null && shieldList.size()>0){
    			   if(shieldList.contains(ipAddr.getIpAddress()))return;
    		   }
    		   
    		   //�жϸ�IP�Ƿ��ڱ����ε�������
    		   int netshieldflag = 0;
    		   if(netshieldList != null && netshieldList.size()>0){
    			   long longip = DiscoverEngine.getInstance().ip2long(ipAddr.getIpAddress());
    			   for(int k=0;k<netshieldList.size();k++){
    				   Vector netshield = (Vector)netshieldList.get(k);
    				   if(netshield != null && netshield.size()==2){
    					   //SysLogger.info(netshield.get(0)+"==="+netshield.get(1)+"==="+longip);
    					   try{
    						   if(longip>=((Long)netshield.get(0)).longValue() && longip<=((Long)netshield.get(1)).longValue()){
    							   SysLogger.info("�豸IP "+ipAddr.getIpAddress()+"���ڱ���������");
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
         	//�жϸ�IP�Ƿ��Ѿ�����
         	//if(DiscoverEngine.getInstance().getExistIpList().contains(ipAddr)) continue;
    		SysLogger.info("��������IP "+ipAddr.getIpAddress()+"�Ƿ�����");
    		
         	if(node.getIfEntityList() != null){
         		for(int j=0;j<node.getIfEntityList().size();j++)
         		{
         			//Ĭ�������,��Ҫһ��ȱʡ�����ӹ�ϵ,���Ҳ���������·��ʱ��,��VLAN�Ľӿ�������
         			ifEntity = (IfEntity)node.getIfEntityList().get(j);
         			SysLogger.info("�ж��豸IP "+ipAddr.getIpAddress()+"��ifIndex:"+ipAddr.getIfIndex()+" nodeIfIndex:"+ifEntity.getIndex()+"�豸"+node.getIpAddress());
         			if(ifEntity.getIndex().equals(ipAddr.getIfIndex()))	
         				break;
         		}
         	}
    	     SysLogger.info("####begin NetMediaThread getSysOid ##########"+ipAddr.getIpAddress());     
         	 String community = snmp.getCommunity(ipAddr.getIpAddress());  
         	SysLogger.info("####end NetMediaThread getSysOid ##########"+ipAddr.getIpAddress());
    	     if(community==null){
    	    	 DiscoverEngine.getInstance().getFaildIpList().add(ipAddr.getIpAddress());
    	    	 return;
    	     }
    	     
    	      //�ж��Ѿ����ֵ��豸�б����Ƿ��Ѿ����ڸ�IP(���߸�IP��Ӧ�Ĺ���IP)
    	     SysLogger.info("��ʼ�ж�IP "+ipAddr.getIpAddress()+"�Ƿ��Ѿ����Ѿ����ֵ�IP����IP����");
    	      List hostList = DiscoverEngine.getInstance().getHostList();
    	      Host existHost = null;
    	      if(hostList != null && hostList.size()>0){
    		    	for(int k=0;k<hostList.size();k++)
    		    	{
    		    		Host tmpNode = (Host)hostList.get(k);
    		    		if(tmpNode.getIpAddress().equalsIgnoreCase(ipAddr.getIpAddress())){
    		    			existHost = tmpNode;
    		    			SysLogger.info("�ѷ��ֵ��豸�б����Ѿ�����"+tmpNode.getCategory()+"���豸:"+ipAddr.getIpAddress());		    			
    		    			break;
    		    		}else{
    		    			//�жϱ���IP�Ƿ����
    		    			List aliasIPs = tmpNode.getAliasIPs();
    		    			if(aliasIPs != null && aliasIPs.size()>0){
    		    				if(aliasIPs.contains(ipAddr.getIpAddress())){
    		    					existHost = tmpNode;
    		    					SysLogger.info("�ѷ��ֵ��豸�б����Ѿ�����"+tmpNode.getCategory()+"���豸:"+ipAddr.getIpAddress());
    		    					break;
    		    				}
    		    			}
    		    		}
//    	  					//�ж�ϵͳ����
//    	  					Hashtable sysGroupProperty = snmp.getSysGroup(ipAddr.getIpAddress(),community);
//    	  					if(sysGroupProperty != null){
//    	  						if(sysGroupProperty.containsKey("sysName")){
//    	  							String sysName = (String)sysGroupProperty.get("sysName");
//    	  							if(sysName != null && sysName.length()>0){
//    	  								if(sysName.equalsIgnoreCase(tmpNode.getSysName())){
//    	  									existHost = tmpNode;
//    	  									break;
//    	  								}
//    	  							}
//    	  						}
//    	  					}
    		    	}
    	      }
    	      SysLogger.info("�����ж�IP "+ipAddr.getIpAddress()+"�Ƿ��Ѿ����Ѿ����ֵ�IP����IP����");
    	      
    	      //�ж��Ѿ�����ʧ�ܵ��豸�б����Ƿ��Ѿ����ڸ�IP
    		  SysLogger.info("��ʼ�ж�IP "+ipAddr.getIpAddress()+"�Ƿ��Ѿ����Ѿ�����ʧ�ܵ�IP����IP����");
    		  List faildIpList = DiscoverEngine.getInstance().getFaildIpList();
    	      //existHost = null;
    	      if(faildIpList != null && faildIpList.size()>0){
    	    	  if(faildIpList.contains(ipAddr.getIpAddress()))return;
    	      }
    	      SysLogger.info("�����ж�IP "+ipAddr.getIpAddress()+"�Ƿ��Ѿ����Ѿ�����ʧ�ܵ�IP����IP����");
    	      
    	      String sysOid = "";
    	      int deviceType = 0;
    	      SysLogger.info("��ʼ��ȡ�豸 "+ipAddr.getIpAddress()+"������");
    	      if(existHost != null){
    	    	  //����IP�Ѿ�����
    	    	  sysOid = existHost.getSysOid();
    	    	  deviceType = existHost.getCategory();
    	    	  return;
    	      }else{
    	    	  //������
    	    	  sysOid = SnmpUtil.getInstance().getSysOid(ipAddr.getIpAddress(),community);
    	    	  deviceType = SnmpUtil.getInstance().checkDevice(ipAddr.getIpAddress(),community,sysOid);
    	      }
    	      SysLogger.info("������ȡ�豸 "+ipAddr.getIpAddress()+"������,����Ϊ"+deviceType); 

    	     	     
    	     if(deviceType==0) //δ֪�豸
    	     {	    	 
    	    	 if(NetworkUtil.checkService(ipAddr.getIpAddress()))
    	    		deviceType=4;
    	    	 else
    	    	 {
    		    	 DiscoverEngine.getInstance().getExistIpList().add(ipAddr.getIpAddress());
    		    	 DiscoverEngine.getInstance().getFaildIpList().add(ipAddr.getIpAddress());
    		    	 SysLogger.info("һ��֧��SNMP,������ȷ�������͵��豸:" + ipAddr.getIpAddress() + ",community=" + community + ",sysOid=" + sysOid);
    		    	 return;	    		 	    		 
    	    	 }	    	 
    	     }	     	     	     
        	 boolean isValid = false;		     
    	     SubNet subnet = null; 
    	     
    	     //��ʼ��һ���½ڵ�
    	     Host host = new Host();
    	     host = DiscoverEngine.getInstance().getHostByIP(ipAddr.getIpAddress());
    	     if(host == null){
    	    	 //�жϱ���IP�Ƿ����
    	    	 Host aliashost = null;
    	    	 aliashost = DiscoverEngine.getInstance().getHostByAliasIP(ipAddr.getIpAddress()); 
    	    	 if(aliashost != null){
    	    		 host = new Host();
    	    		 host.setBridgestpList(aliashost.getBridgestpList());
    	    		 host.setCdpList(aliashost.getCdpList());
    	    		 host.setIfEntityList(aliashost.getIfEntityList());
    	    	 }
    	     }	     
    	     if(host == null)host = new Host();
    	     
    	     if(node.getCategory()==1||node.getCategory()==2) //����Ƕ��㽻����,���Ľӿ�û���������� 
    	     {	 	    	
    	        try{
    	        	subnet = ifEntity.isValidIP(ipAddr.getIpAddress());
    	        }catch(Exception e){
    	        	//e.printStackTrace();
    	        }
    	        if(subnet!=null) isValid = true;
    	        if(!isValid)  //�᲻����������??
    	        {
    	    	    SysLogger.info("NetMedia:�ڵ�" + node.getIpAddress()+ "�ϵ�" + ipAddr.getIpAddress() + "������Ӧ�ӿ�������������");
    	 	        host.setNetMask("255.255.255.0");
    	            host.setLocalNet(0);		        
    	        }
    	        else 
    	        {	        		        	
    	        	host.setNetMask(subnet.getNetMask());
                    host.setLocalNet(subnet.getId());
    	        }
    	     }   
    	     else //���㽻����
    	     {	 
    			 host.setNetMask(node.getNetMask());  //�Զ��㽻��,���������븸�ڵ���ͬ
    			 host.setLocalNet(node.getLocalNet());		        
    	     }
    	     
    	     if(existHost == null){
    		     host.setCategory(deviceType); 
    	         host.setIpAddress(ipAddr.getIpAddress());                                
    	         host.setCommunity(community);
    	         host.setWritecommunity(DiscoverEngine.getInstance().getWritecommunity());
    	         host.setSnmpversion(DiscoverEngine.getInstance().getSnmpversion());
    	         host.setSysOid(sysOid);         
    	         host.setLayer(node.getLayer() + 1);
    	         
    	         if(node.isRouter())
    	            host.setSuperNode(node.getId());
    	         else
    	        	host.setSuperNode(node.getSuperNode());
    	     }else{
    	    	 host = existHost;
    	     }

             SysLogger.info("�ӵ�ַת�����з����豸IP:"+host.getIpAddress()+" �豸����:"+deviceType);         
             if(deviceType==4 || deviceType==5 || deviceType==6 ) //���������ӡ��
             {
            	 DiscoverEngine.getInstance().addHost_SOLO(host,null);	
            	 return;
             }
             if(deviceType==2 || deviceType==3 || deviceType==7){
            	 //�����豸
            	 //����STP����
            	 if(host.getBridgestpList() == null){
            		 try{
            			 host.setBridgestpList(snmp.getBridgeStpList(ipAddr.getIpAddress(), community));
            		 }catch(Exception e){
            			 SysLogger.info("��ȡ�豸"+ipAddr.getIpAddress()+"��STP�����ݳ���"+e.getMessage()); 
            		 }
            	 }
            	 //���ýӿ�����
            	 if(host.getIfEntityList() == null){
            		 try{
            			 host.setIfEntityList(snmp.getIfEntityList(ipAddr.getIpAddress(), community, deviceType));
            		 }catch(Exception e){
            			 SysLogger.info("��ȡ�豸"+ipAddr.getIpAddress()+"�Ľӿ����ݳ���"+e.getMessage()); 
            		 }
            	 }
            	 /*
            	//����fdb�ӿ�����
            	 if(host.getFdbList() == null){
            		 try{
            			 host.setFdbList(snmp.getFdbTable(ipAddr.getIpAddress(), community));
            		 }catch(Exception e){
            			 SysLogger.info("��ȡ�豸"+ipAddr.getIpAddress()+"��FDB���ݳ���"+e.getMessage()); 
            		 }
            	 }
            	 */
     			//����H3C�Ľ����豸,ȡ��NDP��Ϣ
    			 if(host.getSysOid().indexOf("1.3.6.1.4.1.25506")>=0 || host.getSysOid().indexOf("1.3.6.1.4.1.2011")>=0){
    	        		//H3C�Ľ����豸,ȡ��NDP��Ϣ
    	        		 SysLogger.info("==========================================================");
    	        		 SysLogger.info(node.getIpAddress() + "��ARP���з���H3C������:" + host.getIpAddress());
    	        		 SysLogger.info("=========================================================="); 
    	        		 
    	              	if(host.getNdpHash() == null || host.getNdpHash().size()==0)
    	              		host.setNdpHash(snmp.getNDPTable(host.getIpAddress(),host.getCommunity()));
    			 }
    			 
    		     if(host.getIfEntityList() != null){
    		     		for(int j=0;j<host.getIfEntityList().size();j++)
    		     		{
    		     			//Ĭ�������,��Ҫһ��ȱʡ�����ӹ�ϵ,���Ҳ���������·��ʱ��,��VLAN�Ľӿ�������
    		     			endifEntity = (IfEntity)host.getIfEntityList().get(j);
    		     			//SysLogger.info("�ж��豸IP "+ipAddr.getIpAddress()+"��ifIndex:"+ipAddr.getIfIndex()+" nodeIfIndex:"+ifEntity.getIndex()+"�豸"+node.getIpAddress());
    		     			if(endifEntity.getIpAddress().equalsIgnoreCase(ipAddr.getIpAddress()))	
    		     				break;
    		     		}
    		     }
    			  SysLogger.info("###################################");
    			  SysLogger.info(node.getIpAddress() + "��ARP���з��ֽ�����:" + host.getIpAddress());
    			  SysLogger.info("###################################");
                  Link link = new Link();	
                  try{
    	         	  link.setStartId(node.getId());          
    	         	  link.setStartIndex(ifEntity.getIndex());
    	         	  link.setStartIp(ifEntity.getIpAddress());
    	         	  link.setStartPhysAddress(ifEntity.getPhysAddress()); //��¼������mac
    	         	  link.setStartDescr(ifEntity.getDescr());
    	         	  link.setVlanStartIndex(ifEntity.getIndex());
    	         	  link.setEndIp(host.getIpAddress());
    	         	  link.setEndId(host.getId());
    	         	  link.setEndIndex(endifEntity.getIndex());
    	         	  link.setEndDescr(endifEntity.getDescr());
    	         	  link.setEndPhysAddress(endifEntity.getPhysAddress());
    	         	  link.setVlanEndIp(ipAddr.getIpAddress());//���õ�ǰVLAN��ַ
    	         	  link.setVlanEndIndex(endifEntity.getIndex());
    	         	  link.setFindtype(SystemConstant.ISMac);//����MAC����
    	         	  link.setSublinktype(SystemConstant.NONEPHYSICALLINK);//���߼����ӹ�ϵ
                  }catch(Exception e){
                	  
                  }
             	  DiscoverEngine.getInstance().addHost_SOLO(host,link);
             }
    	   }catch(Exception ex){
    		   ex.printStackTrace();
    	   }
       }
   };
	}
}