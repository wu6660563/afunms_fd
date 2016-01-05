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
import com.afunms.polling.task.ThreadPool;
import com.afunms.topology.dao.DiscoverConfigDao;
import com.afunms.topology.model.DiscoverConfig;

public class IPNetProbeThread_SOLO_Runable 
//public class IPNetProbeThread implements Runnable
{
   private Host node;
   private List addressList;
   
   /**
   ��������
	 */	
	public static Runnable createTask(final Host node,final List addressList) {
   return new Runnable() {
       public void run() {
    	   if(DiscoverEngine.getInstance().getStopStatus() == 1)return;
    	   //snmp = SnmpUtil.getInstance();
    	   SysLogger.info("��ʼ�����豸"+node.getIpAddress()+"�ĵ�ַת����");	    
    	   Set shieldList = DiscoverResource.getInstance().getShieldSet();
    	   List netshieldList = DiscoverResource.getInstance().getNetshieldList();
    	   List netincludeList = DiscoverResource.getInstance().getNetincludeList();
    	   IfEntity ifEntity = null;
    	   IfEntity endifEntity = null;

           // �����̳߳�
    	   ThreadPool threadPool = new ThreadPool(addressList.size());	
        		
    	   for(int i=0;i<addressList.size();i++)
    	   {
    		   //###################################
    		   //������Ҫ������,�򲻽��н�������ARP��ɼ�
    		   //if(1==1)continue;
    		   //###################################   
    			 try{
    			   //node.updateCount(2);     	      	 
    			   IpAddress ipAddr = (IpAddress)addressList.get(i);
    			   threadPool.runTask(NetMediThread_SOLO.createTask(ipAddr, node)); 		   
    			   //SysLogger.info("��"+node.getIpAddress()+"�ĵ�ַת������IP "+ipAddr.getIpAddress()+",��ʼ����");
    			 }catch(Exception ex){
    				 ex.printStackTrace();
    			 }
          }//end_for
    	   // �ر��̳߳ز��ȴ������������
      	   threadPool.join();
      	 threadPool.close();
      	threadPool = null;
      		
    	   DiscoverEngine.getInstance().addDiscoverdcount(); 
    	   //snmp = null;
    	   //setCompleted(true);
       }
   };
	}
	
	/**
	    * ȷ��һ���豸�Ƿ��Ѿ�����
	    * ��ǰLinkֻ����ʼIP����ʼ�˿�����ID
	    */
	   private static boolean isHostExist(String ip)
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