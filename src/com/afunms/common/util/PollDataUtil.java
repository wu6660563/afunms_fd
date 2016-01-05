/**
 * <p>Description:network utilities</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-11
 */

package com.afunms.common.util;

import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

import org.apache.commons.beanutils.BeanUtils;

import com.afunms.polling.PollingEngine;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Task;
import com.afunms.polling.snmp.cpu.BDComCpuSnmp;
import com.afunms.polling.snmp.cpu.CiscoCpuSnmp;
import com.afunms.polling.snmp.cpu.DLinkCpuSnmp;
import com.afunms.polling.snmp.cpu.EnterasysCpuSnmp;
import com.afunms.polling.snmp.cpu.H3CCpuSnmp;
import com.afunms.polling.snmp.cpu.LinuxCpuSnmp;
import com.afunms.polling.snmp.cpu.MaipuCpuSnmp;
import com.afunms.polling.snmp.cpu.NortelCpuSnmp;
import com.afunms.polling.snmp.cpu.RadwareCpuSnmp;
import com.afunms.polling.snmp.cpu.RedGiantCpuSnmp;
import com.afunms.polling.snmp.cpu.WindowsCpuSnmp;
import com.afunms.polling.snmp.cpu.ZTECpuSnmp;
import com.afunms.polling.snmp.device.LinuxDeviceSnmp;
import com.afunms.polling.snmp.device.WindowsDeviceSnmp;
import com.afunms.polling.snmp.disk.LinuxDiskSnmp;
import com.afunms.polling.snmp.disk.WindowsDiskSnmp;
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
import com.afunms.polling.snmp.memory.LinuxPhysicalMemorySnmp;
import com.afunms.polling.snmp.memory.MaipuMemorySnmp;
import com.afunms.polling.snmp.memory.NortelMemorySnmp;
import com.afunms.polling.snmp.memory.RedGiantMemorySnmp;
import com.afunms.polling.snmp.memory.WindowsPhysicalMemorySnmp;
import com.afunms.polling.snmp.memory.WindowsVirtualMemorySnmp;
import com.afunms.polling.snmp.ping.PingSnmp;
import com.afunms.polling.snmp.power.CiscoPowerSnmp;
import com.afunms.polling.snmp.power.H3CPowerSnmp;
import com.afunms.polling.snmp.process.LinuxProcessSnmp;
import com.afunms.polling.snmp.process.WindowsProcessSnmp;
import com.afunms.polling.snmp.service.WindowsServiceSnmp;
import com.afunms.polling.snmp.software.LinuxSoftwareSnmp;
import com.afunms.polling.snmp.software.WindowsSoftwareSnmp;
import com.afunms.polling.snmp.storage.LinuxStorageSnmp;
import com.afunms.polling.snmp.storage.WindowsStorageSnmp;
import com.afunms.polling.snmp.system.SystemSnmp;
import com.afunms.polling.snmp.temperature.BDComTemperatureSnmp;
import com.afunms.polling.snmp.temperature.CiscoTemperatureSnmp;
import com.afunms.polling.snmp.temperature.H3CTemperatureSnmp;
import com.afunms.polling.snmp.voltage.CiscoVoltageSnmp;
import com.afunms.polling.snmp.voltage.H3CVoltageSnmp;
import com.afunms.polling.task.TaskXml;
import com.afunms.polling.task.ThreadPool;
import com.afunms.sysset.model.Service;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;
import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.discovery.DiscoverResource;
import com.afunms.indicators.dao.NodeGatherIndicatorsDao;
import com.afunms.indicators.model.NodeGatherIndicators;

public class PollDataUtil 
{		
    public PollDataUtil()
    {
    }
    
    public void collectHostData(NodeGatherIndicators nodeGatherIndicatorsNode){
    	try {                	
        	Vector vector=null;
        	Hashtable hashv = new Hashtable();
        	Hashtable returnHash = new Hashtable();
        	HostCollectDataManager hostdataManager=new HostCollectDataManager(); 
        	
        	if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("cpu")){
        		//CPU�Ĳɼ�
        		if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("windows")){
        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
        			if(node != null){
        				WindowsCpuSnmp windowscpusnmp = null;
            			try{
                			windowscpusnmp = (WindowsCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.WindowsCpuSnmp").newInstance();
                			returnHash = windowscpusnmp.collect_Data(nodeGatherIndicatorsNode);
                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","cpu");
                		}catch(Exception e){
                			e.printStackTrace();
                		}
        			}
        			
        		}else if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("linux")){
        			//LINUX��CPU
        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
        			if(node != null){
        				LinuxCpuSnmp linuxcpusnmp = null;
            			try{
            				linuxcpusnmp = (LinuxCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.LinuxCpuSnmp").newInstance();
                			returnHash = linuxcpusnmp.collect_Data(nodeGatherIndicatorsNode);
                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","linux","cpu");
                		}catch(Exception e){
                			e.printStackTrace();
                		}
        			}
        		}
        	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("memory")){
        	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("disk")){
        		//DISK�Ĳɼ�
        		if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("windows")){
        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
        			if(node != null){
        				WindowsDiskSnmp windowdisksnmp = null;
            			try{
            				windowdisksnmp = (WindowsDiskSnmp)Class.forName("com.afunms.polling.snmp.disk.WindowsDiskSnmp").newInstance();
                			returnHash = windowdisksnmp.collect_Data(nodeGatherIndicatorsNode);
                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","disk");
                		}catch(Exception e){
                			e.printStackTrace();
                		}
        			}
        			
        		}else if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("linux")){
        			//LINUX��DISK
        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
        			if(node != null){
        				LinuxDiskSnmp linuxdisksnmp = null;
            			try{
            				linuxdisksnmp = (LinuxDiskSnmp)Class.forName("com.afunms.polling.snmp.disk.LinuxDiskSnmp").newInstance();
                			returnHash = linuxdisksnmp.collect_Data(nodeGatherIndicatorsNode);
                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","linux","disk");
                		}catch(Exception e){
                			//e.printStackTrace();
                		}
        			}
        		}
        	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("process")){
        		//�洢��Ϣ�Ĳɼ�
        		if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("windows")){
        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
        			if(node != null){
        				WindowsProcessSnmp windowsprocesssnmp = null;
            			try{
            				windowsprocesssnmp = (WindowsProcessSnmp)Class.forName("com.afunms.polling.snmp.process.WindowsProcessSnmp").newInstance();
                			returnHash = windowsprocesssnmp.collect_Data(nodeGatherIndicatorsNode);
                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","process");
                		}catch(Exception e){
                			e.printStackTrace();
                		}
        			}         			
        		}else if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("linux")){
        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
        			if(node != null){
        				LinuxProcessSnmp linuxprocesssnmp = null;
            			try{
            				linuxprocesssnmp = (LinuxProcessSnmp)Class.forName("com.afunms.polling.snmp.process.LinuxProcessSnmp").newInstance();
                			returnHash = linuxprocesssnmp.collect_Data(nodeGatherIndicatorsNode);
                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","linux","process");
                		}catch(Exception e){
                			//e.printStackTrace();
                		}
        			}
        		}
        	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("service")){
        		//����Ĳɼ�
        		if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("windows")){
        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
        			if(node != null){
        				WindowsServiceSnmp windowservicesnmp = null;
            			try{
            				windowservicesnmp = (WindowsServiceSnmp)Class.forName("com.afunms.polling.snmp.service.WindowsServiceSnmp").newInstance();
                			returnHash = windowservicesnmp.collect_Data(nodeGatherIndicatorsNode);
                			//Ŀǰ����û��ÿ�ζ��������ݿ�,��Ҫ�ֹ�ͬ��
                			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","service");
                		}catch(Exception e){
                			e.printStackTrace();
                		}
        			}          			
        		}
        		
        	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("software")){
        		//��װ������Ĳɼ�
        		if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("windows")){
        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
        			if(node != null){
        				WindowsSoftwareSnmp windowssoftwaresnmp = null;
            			try{
            				windowssoftwaresnmp = (WindowsSoftwareSnmp)Class.forName("com.afunms.polling.snmp.software.WindowsSoftwareSnmp").newInstance();
                			returnHash = windowssoftwaresnmp.collect_Data(nodeGatherIndicatorsNode);
                			//Ŀǰ���û��ÿ�ζ��������ݿ�,��Ҫ�ֹ�ͬ��
                			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","service");
                		}catch(Exception e){
                			e.printStackTrace();
                		}
        			}
        		
        		}if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("linux")){
        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
        			if(node != null){
        				LinuxSoftwareSnmp linuxsoftwaresnmp = null;
            			try{
            				linuxsoftwaresnmp = (LinuxSoftwareSnmp)Class.forName("com.afunms.polling.snmp.software.LinuxSoftwareSnmp").newInstance();
                			returnHash = linuxsoftwaresnmp.collect_Data(nodeGatherIndicatorsNode);
                			//Ŀǰ���û��ÿ�ζ��������ݿ�,��Ҫ�ֹ�ͬ��
                			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","service");
                		}catch(Exception e){
                			e.printStackTrace();
                		}
        			}	
        		}
        	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("hardware")){
        		//��װ������Ĳɼ�
        		if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("windows")){
        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
        			if(node != null){
        				WindowsDeviceSnmp windowsdevicesnmp = null;
            			try{
            				windowsdevicesnmp = (WindowsDeviceSnmp)Class.forName("com.afunms.polling.snmp.device.WindowsDeviceSnmp").newInstance();
                			returnHash = windowsdevicesnmp.collect_Data(nodeGatherIndicatorsNode);
                			//Ŀǰ�豸��Ϣû��ÿ�ζ��������ݿ�,��Ҫ�ֹ�ͬ��
                			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","service");
                		}catch(Exception e){
                			e.printStackTrace();
                		}
        			}
        			
        		}else if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("linux")){
        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
        			if(node != null){
        				LinuxDeviceSnmp linuxdevicesnmp = null;
            			try{
            				linuxdevicesnmp = (LinuxDeviceSnmp)Class.forName("com.afunms.polling.snmp.device.LinuxDeviceSnmp").newInstance();
                			returnHash = linuxdevicesnmp.collect_Data(nodeGatherIndicatorsNode);
                			//Ŀǰ�豸��Ϣû��ÿ�ζ��������ݿ�,��Ҫ�ֹ�ͬ��
                			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","service");
                		}catch(Exception e){
                			e.printStackTrace();
                		}
        			}
        			
        		}
        	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("storage")){
        		//�洢��Ϣ�Ĳɼ�
        		if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("windows")){
        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
        			if(node != null){
        				WindowsStorageSnmp windowsstoragesnmp = null;
            			try{
            				windowsstoragesnmp = (WindowsStorageSnmp)Class.forName("com.afunms.polling.snmp.storage.WindowsStorageSnmp").newInstance();
                			returnHash = windowsstoragesnmp.collect_Data(nodeGatherIndicatorsNode);
                			//Ŀǰ�洢��Ϣû��ÿ�ζ��������ݿ�,��Ҫ�ֹ�ͬ��
                			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","service");
                		}catch(Exception e){
                			e.printStackTrace();
                		}
        			}	
        		}else if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("linux")){
        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
        			if(node != null){
        				LinuxStorageSnmp linuxstoragesnmp = null;
            			try{
            				linuxstoragesnmp = (LinuxStorageSnmp)Class.forName("com.afunms.polling.snmp.storage.LinuxStorageSnmp").newInstance();
                			returnHash = linuxstoragesnmp.collect_Data(nodeGatherIndicatorsNode);
                			//Ŀǰ�洢��Ϣû��ÿ�ζ��������ݿ�,��Ҫ�ֹ�ͬ��
                			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","service");
                		}catch(Exception e){
                			e.printStackTrace();
                		}
        			}
        			
        		}
        	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("physicalmemory")){
        		//�洢��Ϣ�Ĳɼ�
        		if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("windows")){
        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
        			if(node != null){
        				WindowsPhysicalMemorySnmp windowsphysicalsnmp = null;
            			try{
            				windowsphysicalsnmp = (WindowsPhysicalMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.WindowsPhysicalMemorySnmp").newInstance();
                			returnHash = windowsphysicalsnmp.collect_Data(nodeGatherIndicatorsNode);
                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","physicalmemory");
                		}catch(Exception e){
                			e.printStackTrace();
                		}
        			}
        		}else if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("linux")){
        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
        			if(node != null){
        				LinuxPhysicalMemorySnmp linuxphysicalsnmp = null;
            			try{
            				linuxphysicalsnmp = (LinuxPhysicalMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.LinuxPhysicalMemorySnmp").newInstance();
                			returnHash = linuxphysicalsnmp.collect_Data(nodeGatherIndicatorsNode);
                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","linux","physicalmemory");
                		}catch(Exception e){
                			e.printStackTrace();
                		}
        			}
        			
        		}
        	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("virtualmemory")){
        		//�洢��Ϣ�Ĳɼ�
        		if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("windows")){
        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
        			if(node != null){
        				WindowsVirtualMemorySnmp windowsvirtualsnmp = null;
            			try{
            				windowsvirtualsnmp = (WindowsVirtualMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.WindowsVirtualMemorySnmp").newInstance();
                			returnHash = windowsvirtualsnmp.collect_Data(nodeGatherIndicatorsNode);
                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","virtualmemory");
                		}catch(Exception e){
                			e.printStackTrace();
                		}
        			}
        			
        		}
        	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("systemgroup")){
        		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
        		if(node != null){
        			SystemSnmp systemsnmp = null;
        			try{
        				systemsnmp = (SystemSnmp)Class.forName("com.afunms.polling.snmp.system.SystemSnmp").newInstance();
            			returnHash = systemsnmp.collect_Data(nodeGatherIndicatorsNode);
            			//IP-MAC���������ݿ�
            			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"ipmac");
            		}catch(Exception e){
            			e.printStackTrace();
            		}
        		}
        	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("utilhdx")){
//        		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        		if(node != null){
//        			UtilHdxSnmp utilhdxsnmp = null;
//        			try{
//        				utilhdxsnmp = (UtilHdxSnmp)Class.forName("com.afunms.polling.snmp.interfaces.UtilHdxSnmp").newInstance();
//            			returnHash = utilhdxsnmp.collect_Data(alarmIndicatorsNode);
//            			//�������ݿ�
//            			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"utilhdx");
//            		}catch(Exception e){
//            			e.printStackTrace();
//            		}
//        		}
        	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("interface")){
        		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
        		if(node != null){
        			InterfaceSnmp interfacesnmp = null;
        			try{
        				//SysLogger.info("��ʼ�ɼ�"+node.getIpAddress()+" �ӿ���Ϣ...");
        				interfacesnmp = (InterfaceSnmp)Class.forName("com.afunms.polling.snmp.interfaces.InterfaceSnmp").newInstance();
            			returnHash = interfacesnmp.collect_Data(nodeGatherIndicatorsNode);
            			if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("windows")){
            				//�Գ���������ֵ���и澯���
                		    try{
                				AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                				List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(node.getId()), AlarmConstant.TYPE_HOST, "windows");
                				for(int i = 0 ; i < list.size() ; i ++){
                					AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
                					if(alarmIndicatorsnode.getName().equalsIgnoreCase("AllInBandwidthUtilHdx") || alarmIndicatorsnode.getName().equalsIgnoreCase("AllOutBandwidthUtilHdx")
                							|| alarmIndicatorsnode.getName().equalsIgnoreCase("utilhdx")){
                						//���ܳ��������ֵ���и澯���
                    					CheckEventUtil checkutil = new CheckEventUtil();
                    					checkutil.updateData(node,returnHash,"host","windows",alarmIndicatorsnode);
                					}
                				}
                		    }catch(Exception e){
                		    	e.printStackTrace();
                		    }
            			}else if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("linux")){
            				//�Գ���������ֵ���и澯���
                		    try{
                				AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                				List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(node.getId()), AlarmConstant.TYPE_HOST, "linux");
                				for(int i = 0 ; i < list.size() ; i ++){
                					AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
                					if(alarmIndicatorsnode.getName().equalsIgnoreCase("AllInBandwidthUtilHdx") || alarmIndicatorsnode.getName().equalsIgnoreCase("AllOutBandwidthUtilHdx")
                							|| alarmIndicatorsnode.getName().equalsIgnoreCase("utilhdx")){
                						//���ܳ��������ֵ���и澯���
                    					CheckEventUtil checkutil = new CheckEventUtil();
                    					checkutil.updateData(node,returnHash,"host","linux",alarmIndicatorsnode);
                					}
                				}
                		    }catch(Exception e){
                		    	e.printStackTrace();
                		    }
            			}
            			//�������ݿ�
            			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,nodeGatherIndicatorsNode.getType(),nodeGatherIndicatorsNode.getSubtype(),"interface");
            		}catch(Exception e){
            			e.printStackTrace();
            		}
        		}
        	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("packs")){
        		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
        		if(node != null){
        			PackageSnmp packagesnmp = null;
        			try{
        				packagesnmp = (PackageSnmp)Class.forName("com.afunms.polling.snmp.interfaces.PackageSnmp").newInstance();
            			returnHash = packagesnmp.collect_Data(nodeGatherIndicatorsNode);
            			//�������ݿ�
            			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,nodeGatherIndicatorsNode.getType(),nodeGatherIndicatorsNode.getSubtype(),"packs");
            		}catch(Exception e){
            			e.printStackTrace();
            		}
        		}
        	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("ping")){
        		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
        		if(node != null){
        			PingSnmp pingsnmp = null;
        			try{
        				pingsnmp = (PingSnmp)Class.forName("com.afunms.polling.snmp.ping.PingSnmp").newInstance();
            			returnHash = pingsnmp.collect_Data(nodeGatherIndicatorsNode);
            			//�ڲɼ��������Ѿ��������ݿ�
            			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"utilhdx");
            		}catch(Exception e){
            			e.printStackTrace();
            		}
        		}
        	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("ipmac")){
        		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
        		if(node != null){
        			ArpSnmp arpsnmp = null;
        			try{
        				arpsnmp = (ArpSnmp)Class.forName("com.afunms.polling.snmp.interfaces.ArpSnmp").newInstance();
            			returnHash = arpsnmp.collect_Data(nodeGatherIndicatorsNode);
            			//IP-MAC���������ݿ�
            			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"ipmac");
            		}catch(Exception e){
            			e.printStackTrace();
            		}
        		}
        	}
    		
        }catch(Exception exc){
        	
        }
    }
    public void collectNetData(String nodeid){
    	try{	
			NodeGatherIndicatorsDao indicatorsdao = new NodeGatherIndicatorsDao();
	    	List<NodeGatherIndicators> monitorItemList = new ArrayList<NodeGatherIndicators>();
	    	try{
	    		//��ȡ�����õ����б�����ָ��
	    		monitorItemList = indicatorsdao.getByNodeidAndType(nodeid,1,"net");
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}finally{
	    		indicatorsdao.close();
	    	}
	    	if(monitorItemList == null)monitorItemList = new ArrayList<NodeGatherIndicators>();

        		int numThreads = 200;        		
        		try {
        			List numList = new ArrayList();
        			TaskXml taskxml = new TaskXml();
        			numList = taskxml.ListXml();
        			for (int i = 0; i < numList.size(); i++) {
        				Task task = new Task();
        				BeanUtils.copyProperties(task, numList.get(i));
        				if (task.getTaskname().equals("netthreadnum")){
        					numThreads = task.getPolltime().intValue();
        				}
        			}

        		} catch (Exception e) {
        			e.printStackTrace();
        		}
        		HostNodeDao nodedao = new HostNodeDao();
        		List nodelist = new ArrayList();
        		try{
        			nodelist = nodedao.loadMonitorNet();
        		}catch(Exception e){
        			
        		}finally{
        			nodedao.close();
        		}
        		Hashtable nodehash = new Hashtable();
        		if(nodelist != null && nodelist.size()>0){
        			for(int i=0;i<nodelist.size();i++){
        				HostNode node = (HostNode)nodelist.get(i);
        				nodehash.put(node.getId()+"", node.getId());
        			}
        		}        		
        		final Hashtable alldata = new Hashtable();
        		// �����̳߳�
        		ThreadPool threadPool = null;
        		if(monitorItemList != null && monitorItemList.size()>0){
        			threadPool = new ThreadPool(monitorItemList.size());
        			// ��������
            		for (int i=0; i<monitorItemList.size(); i++) {
            			NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators)monitorItemList.get(i);
            			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicators.getNodeid()));
            			//���˵������ӵ��豸
            			if(!nodehash.containsKey(nodeGatherIndicators.getNodeid()))continue;
            			threadPool.runTask(createTask(nodeGatherIndicators,alldata));           		
            		}       		
            		// �ر��̳߳ز��ȴ������������
            		threadPool.join();
            		threadPool.close();
            		threadPool = null;
        		}
        		 
        		
        		HostCollectDataManager hostdataManager=new HostCollectDataManager(); 
        		Date startdate = new Date();
        		hostdataManager.createHostItemData(alldata, "net");
        		Date enddate = new Date();
        		SysLogger.info("##############################");
				SysLogger.info("### ���������豸���ʱ�� "+(enddate.getTime()-startdate.getTime())+"####");
				SysLogger.info("##############################");
        		
		}catch(Exception e){					 	
			e.printStackTrace();
		}finally{
			//SysLogger.info("********M5Task Thread Count : "+Thread.activeCount());
		}
    }
//    public void collectNetData(NodeGatherIndicators alarmIndicatorsNode){
//    	try {                	
//        	Vector vector=null;
//        	Hashtable hashv = new Hashtable();
//        	Hashtable returnHash = new Hashtable();
//        	HostCollectDataManager hostdataManager=new HostCollectDataManager(); 
//        	
//        	if(alarmIndicatorsNode.getName().equalsIgnoreCase("cpu")){
//        		//CPU�Ĳɼ�
//        		if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
//        			//CISCO��CPU
//        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        			if(node != null){
//        				CiscoCpuSnmp ciscocpusnmp = null;
//            			try{
//            				ciscocpusnmp = (CiscoCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.CiscoCpuSnmp").newInstance();
//                			returnHash = ciscocpusnmp.collect_Data(alarmIndicatorsNode);
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","cisco","cpu");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}
//        		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("h3c")){
//        			//H3C��CPU
//        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        			if(node != null){
//        				H3CCpuSnmp h3ccpusnmp = null;
//            			try{
//            				h3ccpusnmp = (H3CCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.H3CCpuSnmp").newInstance();
//                			returnHash = h3ccpusnmp.collect_Data(alarmIndicatorsNode);
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","h3c","cpu");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}
//        		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("entrasys")){
//        			//������CPU
//        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        			if(node != null){
//        				EnterasysCpuSnmp enterasyscpusnmp = null;
//            			try{
//            				enterasyscpusnmp = (EnterasysCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.EnterasysCpuSnmp").newInstance();
//                			returnHash = enterasyscpusnmp.collect_Data(alarmIndicatorsNode);
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","enterasys","cpu");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}
//        		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("radware")){
//        			//radware��CPU
//        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        			if(node != null){
//        				RadwareCpuSnmp radwarecpusnmp = null;
//            			try{
//            				radwarecpusnmp = (RadwareCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.RadwareCpuSnmp").newInstance();
//                			returnHash = radwarecpusnmp.collect_Data(alarmIndicatorsNode);
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","radware","cpu");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}
//        		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("maipu")){
//        			//maipu��CPU
//        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        			if(node != null){
//        				MaipuCpuSnmp maipucpusnmp = null;
//            			try{
//            				maipucpusnmp = (MaipuCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.MaipuCpuSnmp").newInstance();
//                			returnHash = maipucpusnmp.collect_Data(alarmIndicatorsNode);
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","maipu","cpu");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}
//        		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("redgiant")){
//        			//redgiant��CPU
//        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        			if(node != null){
//        				RedGiantCpuSnmp redgiantcpusnmp = null;
//            			try{
//            				redgiantcpusnmp = (RedGiantCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.RedGiantCpuSnmp").newInstance();
//                			returnHash = redgiantcpusnmp.collect_Data(alarmIndicatorsNode);
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","redgiant","cpu");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}
//        		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("northtel")){
//        			//�����CPU
//        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        			if(node != null){
//        				NortelCpuSnmp nortelcpusnmp = null;
//            			try{
//            				nortelcpusnmp = (NortelCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.NortelCpuSnmp").newInstance();
//                			returnHash = nortelcpusnmp.collect_Data(alarmIndicatorsNode);
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","northtel","cpu");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}
//        		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("dlink")){
//        			//dlink��CPU
//        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        			if(node != null){
//        				DLinkCpuSnmp dlinkcpusnmp = null;
//            			try{
//            				dlinkcpusnmp = (DLinkCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.DLinkCpuSnmp").newInstance();
//                			returnHash = dlinkcpusnmp.collect_Data(alarmIndicatorsNode);
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","dlink","cpu");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}
//        		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("bdcom")){
//        			//�����CPU
//        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        			if(node != null){
//        				BDComCpuSnmp bdcomcpusnmp = null;
//            			try{
//            				bdcomcpusnmp = (BDComCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.BDComCpuSnmp").newInstance();
//                			returnHash = bdcomcpusnmp.collect_Data(alarmIndicatorsNode);
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","bdcom","cpu");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}
//        		}
//        	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("memory")){
//        		//�ڴ�Ĳɼ�
//        		if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
//            		//CISCO��MEMORY
//        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        			if(node != null){
//        				CiscoMemorySnmp ciscomemorysnmp = null;
//            			try{
//            				ciscomemorysnmp = (CiscoMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.CiscoMemorySnmp").newInstance();
//                			returnHash = ciscomemorysnmp.collect_Data(alarmIndicatorsNode);
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","cisco","memory");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}
//        		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("h3c")){ 
//        			//h3c��MEMORY
//        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        			if(node != null){
//        				H3CMemorySnmp h3cmemorysnmp = null;
//            			try{
//            				h3cmemorysnmp = (H3CMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.H3CMemorySnmp").newInstance();
//                			returnHash = h3cmemorysnmp.collect_Data(alarmIndicatorsNode);
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","h3c","memory");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}
//        		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("entrasys")){ 
//        			//entrasys��MEMORY
//        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        			if(node != null){
//        				EnterasysMemorySnmp enterasysmemorysnmp = null;
//            			try{
//            				enterasysmemorysnmp = (EnterasysMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.EnterasysMemorySnmp").newInstance();
//                			returnHash = enterasysmemorysnmp.collect_Data(alarmIndicatorsNode);
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","entrasys","memory");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}
//        		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("radware")){ 
//        			//radware��MEMORY
//        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        			if(node != null){
//        				EnterasysMemorySnmp enterasysmemorysnmp = null;
//            			try{
//            				enterasysmemorysnmp = (EnterasysMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.EnterasysMemorySnmp").newInstance();
//                			returnHash = enterasysmemorysnmp.collect_Data(alarmIndicatorsNode);
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","entrasys","memory");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}
//        		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("maipu")){ 
//        			//maipu��MEMORY
//        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        			if(node != null){
//        				MaipuMemorySnmp maipumemorysnmp = null;
//            			try{
//            				maipumemorysnmp = (MaipuMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.MaipuMemorySnmp").newInstance();
//                			returnHash = maipumemorysnmp.collect_Data(alarmIndicatorsNode);
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","maipu","memory");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}
//        		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("redgiant")){ 
//        			//redgiant��MEMORY
//        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        			if(node != null){
//        				RedGiantMemorySnmp redmemorysnmp = null;
//            			try{
//            				redmemorysnmp = (RedGiantMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.RedGiantMemorySnmp").newInstance();
//                			returnHash = redmemorysnmp.collect_Data(alarmIndicatorsNode);
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","redgiant","memory");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}
//        		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("northtel")){ 
//        			//northtel��MEMORY
//        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        			if(node != null){
//        				NortelMemorySnmp nortelmemorysnmp = null;
//            			try{
//            				nortelmemorysnmp = (NortelMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.NortelMemorySnmp").newInstance();
//                			returnHash = nortelmemorysnmp.collect_Data(alarmIndicatorsNode);
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","northtel","memory");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}
//        		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("dlink")){ 
//        			//dlink��MEMORY
//        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        			if(node != null){
//        				DLinkMemorySnmp dlinkmemorysnmp = null;
//            			try{
//            				dlinkmemorysnmp = (DLinkMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.DLinkMemorySnmp").newInstance();
//                			returnHash = dlinkmemorysnmp.collect_Data(alarmIndicatorsNode);
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","dlink","memory");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}
//        		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("bdcom")){ 
//        			//bdcom��MEMORY
//        			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        			if(node != null){
//        				BDComMemorySnmp bdcommemorysnmp = null;
//            			try{
//            				bdcommemorysnmp = (BDComMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.BDComMemorySnmp").newInstance();
//                			returnHash = bdcommemorysnmp.collect_Data(alarmIndicatorsNode);
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","bdcom","memory");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}
//        		}
//        	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("systemgroup")){
//        		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        		if(node != null){
//        			SystemSnmp systemsnmp = null;
//        			try{
//        				systemsnmp = (SystemSnmp)Class.forName("com.afunms.polling.snmp.system.SystemSnmp").newInstance();
//            			returnHash = systemsnmp.collect_Data(alarmIndicatorsNode);
//            			//IP-MAC���������ݿ�
//            			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"ipmac");
//            		}catch(Exception e){
//            			e.printStackTrace();
//            		}
//        		}
//        	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("utilhdx")){
////        		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
////        		if(node != null){
////        			UtilHdxSnmp utilhdxsnmp = null;
////        			try{
////        				utilhdxsnmp = (UtilHdxSnmp)Class.forName("com.afunms.polling.snmp.interfaces.UtilHdxSnmp").newInstance();
////            			returnHash = utilhdxsnmp.collect_Data(alarmIndicatorsNode);
////            			//�������ݿ�
////            			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"utilhdx");
////            		}catch(Exception e){
////            			e.printStackTrace();
////            		}
////        		}
//        	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("interface")){
//        		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        		if(node != null){
//        			InterfaceSnmp interfacesnmp = null;
//        			try{
////        				SysLogger.info("############################################################");
////        				SysLogger.info("##########��ʼ�ɼ�"+node.getIpAddress()+" �ӿ���Ϣ...##########");
////        				SysLogger.info("############################################################");
//        				interfacesnmp = (InterfaceSnmp)Class.forName("com.afunms.polling.snmp.interfaces.InterfaceSnmp").newInstance();
//            			returnHash = interfacesnmp.collect_Data(alarmIndicatorsNode);
//            			
//            			if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("h3c")){
//            				//�Գ���������ֵ���и澯���
//                		    try{
//                				AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
//                				List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(node.getId()), AlarmConstant.TYPE_NET, "h3c");
//                				for(int i = 0 ; i < list.size() ; i ++){
//                					AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
//                					if(alarmIndicatorsnode.getName().equalsIgnoreCase("AllInBandwidthUtilHdx") || alarmIndicatorsnode.getName().equalsIgnoreCase("AllOutBandwidthUtilHdx")
//                							|| alarmIndicatorsnode.getName().equalsIgnoreCase("utilhdx")){
//                						//���ܳ��������ֵ���и澯���
//                    					CheckEventUtil checkutil = new CheckEventUtil();
//                    					checkutil.updateData(node,returnHash,"net","h3c",alarmIndicatorsnode);
//                					}
//                				}
//                		    }catch(Exception e){
//                		    	e.printStackTrace();
//                		    }
//            			}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
//            				//�Գ���������ֵ���и澯���
//                		    try{
//                				AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
//                				List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(node.getId()), AlarmConstant.TYPE_NET, "cisco");
//                				for(int i = 0 ; i < list.size() ; i ++){
//                					AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
//                					if(alarmIndicatorsnode.getName().equalsIgnoreCase("AllInBandwidthUtilHdx") || alarmIndicatorsnode.getName().equalsIgnoreCase("AllOutBandwidthUtilHdx")
//                							|| alarmIndicatorsnode.getName().equalsIgnoreCase("utilhdx")){
//                						//���ܳ��������ֵ���и澯���
//                    					CheckEventUtil checkutil = new CheckEventUtil();
//                    					checkutil.updateData(node,returnHash,"net","cisco",alarmIndicatorsnode);
//                					}
//                				}
//                		    }catch(Exception e){
//                		    	e.printStackTrace();
//                		    }
//            			}
//            			//�������ݿ�
//            			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"interface");
//            		}catch(Exception e){
//            			e.printStackTrace();
//            		}
//        		}
//        	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("packs")){
//        		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        		if(node != null){
//        			PackageSnmp packagesnmp = null;
//        			try{
//        				packagesnmp = (PackageSnmp)Class.forName("com.afunms.polling.snmp.interfaces.PackageSnmp").newInstance();
//            			returnHash = packagesnmp.collect_Data(alarmIndicatorsNode);
//            			//�������ݿ�
//            			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"packs");
//            		}catch(Exception e){
//            			e.printStackTrace();
//            		}
//        		}
//        	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("ping")){
//        		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        		if(node != null){
//        			PingSnmp pingsnmp = null;
//        			try{
//        				pingsnmp = (PingSnmp)Class.forName("com.afunms.polling.snmp.ping.PingSnmp").newInstance();
//            			returnHash = pingsnmp.collect_Data(alarmIndicatorsNode);
//            			//�ڲɼ��������Ѿ��������ݿ�
//            			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"utilhdx");
//            		}catch(Exception e){
//            			e.printStackTrace();
//            		}
//        		}
//        	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("ipmac")){
//        		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        		if(node != null){
//        			ArpSnmp arpsnmp = null;
//        			try{
//        				arpsnmp = (ArpSnmp)Class.forName("com.afunms.polling.snmp.interfaces.ArpSnmp").newInstance();
//            			returnHash = arpsnmp.collect_Data(alarmIndicatorsNode);
//            			//IP-MAC���������ݿ�
//            			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"ipmac");
//            		}catch(Exception e){
//            			e.printStackTrace();
//            		}
//        		}
//        	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("router")){
//        		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        		if(node != null){
//        			RouterSnmp routersnmp = null;
//        			try{
//        				routersnmp = (RouterSnmp)Class.forName("com.afunms.polling.snmp.interfaces.RouterSnmp").newInstance();
//            			returnHash = routersnmp.collect_Data(alarmIndicatorsNode);
//            			//·�ɱ��������ݿ�
//            			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"ipmac");
//            		}catch(Exception e){
//            			e.printStackTrace();
//            		}
//        		}
//        	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("fdb")){
//        		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        		if(node != null){
//        			FdbSnmp fdbsnmp = null;
//        			try{
//        				//SysLogger.info("��ʼ�ɼ�FDB��################");
//        				fdbsnmp = (FdbSnmp)Class.forName("com.afunms.polling.snmp.interfaces.FdbSnmp").newInstance();
//            			returnHash = fdbsnmp.collect_Data(alarmIndicatorsNode);
//            			//FDB���������ݿ�
//            			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"ipmac");
//            		}catch(Exception e){
//            			e.printStackTrace();
//            		}
//        		}
//        		
//        	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("flash")){
//        		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        		if(node != null){
//        			if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
//        				//CISCO����
//        				CiscoFlashSnmp flashsnmp = null;
//            			try{
//            				//SysLogger.info("��ʼ�ɼ�FLASH��Ϣ################");
//            				flashsnmp = (CiscoFlashSnmp)Class.forName("com.afunms.polling.snmp.flash.CiscoFlashSnmp").newInstance();
//                			returnHash = flashsnmp.collect_Data(alarmIndicatorsNode);
//                			//FDB���������ݿ�
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"flash");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("h3c")){
//        				//CISCO����
//        				H3CFlashSnmp flashsnmp = null;
//            			try{
//            				//SysLogger.info("��ʼ�ɼ�h3c��FLASH��Ϣ################");
//            				flashsnmp = (H3CFlashSnmp)Class.forName("com.afunms.polling.snmp.flash.H3CFlashSnmp").newInstance();
//                			returnHash = flashsnmp.collect_Data(alarmIndicatorsNode);
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"flash");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("bdcom")){
//        				//BDCOM����
//        				BDComFlashSnmp flashsnmp = null;
//            			try{
//            				//SysLogger.info("��ʼ�ɼ�h3c��FLASH��Ϣ################");
//            				flashsnmp = (BDComFlashSnmp)Class.forName("com.afunms.polling.snmp.flash.BDComFlashSnmp").newInstance();
//                			returnHash = flashsnmp.collect_Data(alarmIndicatorsNode);
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"flash");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}
//        			
//        		}
//        	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("temperature")){
//        		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        		if(node != null){
//        			if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
//        				//CISCO�¶�
//        				CiscoTemperatureSnmp tempersnmp = null;
//            			try{
//            				//SysLogger.info("��ʼ�ɼ��¶���Ϣ################");
//            				tempersnmp = (CiscoTemperatureSnmp)Class.forName("com.afunms.polling.snmp.temperature.CiscoTemperatureSnmp").newInstance();
//                			returnHash = tempersnmp.collect_Data(alarmIndicatorsNode);
//                			//FDB���������ݿ�
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"temperature");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("h3c")){
//        				//h3c�¶�
//        				H3CTemperatureSnmp tempersnmp = null;
//            			try{
//            				//SysLogger.info("��ʼ�ɼ�H3C�¶���Ϣ################");
//            				tempersnmp = (H3CTemperatureSnmp)Class.forName("com.afunms.polling.snmp.temperature.H3CTemperatureSnmp").newInstance();
//                			returnHash = tempersnmp.collect_Data(alarmIndicatorsNode);
//                			//FDB���������ݿ�
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"temperature");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("bdcom")){
//        				//bdcom�¶�
//        				BDComTemperatureSnmp tempersnmp = null;
//            			try{
//            				//SysLogger.info("��ʼ�ɼ�BDCOM�¶���Ϣ################");
//            				tempersnmp = (BDComTemperatureSnmp)Class.forName("com.afunms.polling.snmp.temperature.BDComTemperatureSnmp").newInstance();
//                			returnHash = tempersnmp.collect_Data(alarmIndicatorsNode);
//                			//FDB���������ݿ�
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"temperature");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}
//        			
//        		}
//        	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("fan")){
//        		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        		if(node != null){
//        			if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
//        				//CISCO����
//        				CiscoFanSnmp fansnmp = null;
//            			try{
//            				//SysLogger.info("��ʼ�ɼ��¶���Ϣ################");
//            				fansnmp = (CiscoFanSnmp)Class.forName("com.afunms.polling.snmp.fan.CiscoFanSnmp").newInstance();
//                			returnHash = fansnmp.collect_Data(alarmIndicatorsNode);
//                			//FDB���������ݿ�
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"fan");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("h3c")){
//        				//H3C����
//        				H3CFanSnmp fansnmp = null;
//            			try{
//            				//SysLogger.info("��ʼ�ɼ��¶���Ϣ################");
//            				fansnmp = (H3CFanSnmp)Class.forName("com.afunms.polling.snmp.fan.H3CFanSnmp").newInstance();
//                			returnHash = fansnmp.collect_Data(alarmIndicatorsNode);
//                			//FDB���������ݿ�
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"fan");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}
//        		}
//        	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("power")){
//        		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        		if(node != null){
//        			if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
//        				//CISCO��Դ
//        				CiscoPowerSnmp powersnmp = null;
//            			try{
//            				//SysLogger.info("��ʼ�ɼ�CISCO��Դ��Ϣ################");
//            				powersnmp = (CiscoPowerSnmp)Class.forName("com.afunms.polling.snmp.power.CiscoPowerSnmp").newInstance();
//                			returnHash = powersnmp.collect_Data(alarmIndicatorsNode);
//                			//FDB���������ݿ�
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"power");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("h3c")){
//        				//H3C��Դ
//        				H3CPowerSnmp powersnmp = null;
//            			try{
//            				//SysLogger.info("��ʼ�ɼ�H3C��Դ��Ϣ################");
//            				powersnmp = (H3CPowerSnmp)Class.forName("com.afunms.polling.snmp.power.H3CPowerSnmp").newInstance();
//                			returnHash = powersnmp.collect_Data(alarmIndicatorsNode);
//                			//FDB���������ݿ�
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"power");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}
//        		}
//        	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("voltage")){
//        		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//        		if(node != null){
//        			if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
//        				//CISCO��ѹ
//        				CiscoVoltageSnmp voltagesnmp = null;
//            			try{
//            				//SysLogger.info("��ʼ�ɼ�CISCO��ѹ��Ϣ################");
//            				voltagesnmp = (CiscoVoltageSnmp)Class.forName("com.afunms.polling.snmp.voltage.CiscoVoltageSnmp").newInstance();
//                			returnHash = voltagesnmp.collect_Data(alarmIndicatorsNode);
//                			//FDB���������ݿ�
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"voltage");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("h3c")){
//        				//H3C��ѹ
//        				H3CVoltageSnmp voltagesnmp = null;
//            			try{
//            				//SysLogger.info("��ʼ�ɼ�H3C��ѹ��Ϣ################");
//            				voltagesnmp = (H3CVoltageSnmp)Class.forName("com.afunms.polling.snmp.voltage.H3CVoltageSnmp").newInstance();
//                			returnHash = voltagesnmp.collect_Data(alarmIndicatorsNode);
//                			//FDB���������ݿ�
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"voltage");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//        			}
//        		}
//        	}
//    		
//        }catch(Exception exc){
//        	
//        }
//    }
    /**
     * ���һ��ip�Ƿ�Ϊ�Ϸ���ip
     */
    public static boolean checkIp(String ipAddress)
    {
        boolean isValid = true;
    	try
		{
    		StringTokenizer st = new StringTokenizer(ipAddress,".");
    		int len = st.countTokens();
		    if(len!=4) return false;
		    	
		    int ipSegment = 0;
		    for(int i=0;i<len;i++)
		    {
		    	ipSegment = Integer.parseInt(st.nextToken());
		    	if(ipSegment<0 || ipSegment > 255)
		    	{
		    		isValid = false; 
		    	  	break;
		    	}
		    }		    
		}
    	catch(Exception e)
		{
    		SysLogger.error("��Ч��IP��ַ:" + ipAddress);
    		isValid = false;		 
		}
    	return isValid;
    }
    
    /**
     * ����ip
     */
    public static int[] parseIp(String ipAddress)
    {
       if(!checkIp(ipAddress)) return null;
       int[] ipSegment = new int[4];
		
       StringTokenizer st = new StringTokenizer(ipAddress,".");
	   for(int i=0;i<4;i++)
	   {
	   	  ipSegment[i] = Integer.parseInt(st.nextToken());
	   }		    
       return ipSegment;    
    }

    /**
     * ����ȷ��·�ɱ���һ���е�dest�������ַ������·���豸��ַ
     */
    public static boolean isNetAddress(String ipAddress,String netMask)
    {
        int[] ips = parseIp(ipAddress);
        int[] masks = parseIp(netMask);
        String result = null;
        for(int i=0;i<4;i++)
        {
        	if(result == null)
               result = "" + (ips[i]&masks[i]);
        	else
        	   result += "." + (ips[i]&masks[i]);	
        }
        boolean res = false;
        if(result.equals(ipAddress))
           res = true; 
        return res;	
    }
    
	/**
	 * ͨ����������ȡ�øö������й��ж��ٸ�IP��ַ
	 */
	public static int getIpTotalFromMask(String netMask)
	{		
		int[] masks = parseIp(netMask);		
		if(masks==null) return 0;
		   			
		int ipTotal = 0;		
		for(int i=0; i<4 ; i++)
		{
			if (masks[i]!=255)
			{				
				if (i==2)
				   ipTotal = (255 - masks[i])*256 + 256;
				else if (i==3)
				   ipTotal = 256 -  masks[i];					
				break;
			}
		}
		return ipTotal;
	}

    /**
     * �ѱ�׼ipת����ʮ����ip
     */
    public static long ip2long(String ipAddress) 
    {               
       int[] ipSegment = parseIp(ipAddress);
       if(ipSegment==null) return 0;
       
       long longIp = 0;
       int k = 24;
       for(int i = 0; i < ipSegment.length; i++)
       {              
          longIp += ((long)ipSegment[i]) << k;
          k -= 8;        
       }
       return longIp;
    }

    public static void main(String[] args)
    {
       System.out.print("long=" + NetworkUtil.ip2long("192.168.101.188"));	
    }   
    
    /**
     * ��ʮ����ipת���ɱ�׼��IP
     */
	public static String long2ip(long ip)
    {
        int b[] = new int[4];
        b[0] = (int)(ip >> 24 & 255L);
        b[1] = (int)(ip >> 16 & 255L);
        b[2] = (int)(ip >> 8 & 255L);
        b[3] = (int)(ip & 255L);
        String strIP = Integer.toString(b[0]) + "." + Integer.toString(b[1]) + "." + Integer.toString(b[2]) + "." + Integer.toString(b[3]);        
        return strIP;
    }

	/** 
	 * ���һ��ip�Ƿ���һ����Ч��������ַ(�Ƿ�������ķ�Χ��)
	 * netAddress:����IP��ַ
	 * netMask:��������
	 * ipAddress:����IP��ַ
	 */
    public static boolean isValidIP(String netAddress, String netMask,String ipAddress)
    {
        boolean returnbool = false;
        long netiplong = ip2long(netAddress);
        long ipAddrlong = ip2long(ipAddress);
        long allIpNum = (new Long("4294967296")).longValue();
        long netmaskLong = ip2long(netMask);
        long NetmaskTotalIp = allIpNum - netmaskLong;
        if(ipAddrlong > netiplong && ipAddrlong < netiplong + NetmaskTotalIp)
            returnbool = true;
        return returnbool;
    }
		
	/**
	 * ��ʱֻ��255.255.255.*��������в���
	 */
	public static boolean isValidNetMask(String netMask)
	{
		if("0.0.0.0".equals(netMask)||"255.255.255.255".equals(netMask)) return false;
			
        int[] ipSegment = parseIp(netMask);
        if(ipSegment==null) return false;

        if( ipSegment[0]==255 && ipSegment[1]==255 )
	       return true;
	    else	    
	       return false;
	}
	
	/** 
	 * getTheNetworkAddr����
	 * @param   ipAddress ��ַ;netMask ��������    
	 * @return  ͨ�������������IP������������������ַ,��IpAddressΪ192.168.1.200,maskΪ255.255.255.0
	 * ��ö�����������ַΪ192.168.1.0
	 */
    public static String getNetAddress(String ipAddress, String netMask)
    {		
		long ipAddrLong = ip2long(ipAddress);
		long netaskLong = ip2long(netMask);	    	    	    	    
	    long tmpLong = ipAddrLong & netaskLong;	    	    	    	    	    	    	    
				
		return long2ip(tmpLong);
    }
	   
  /**
   * ȷ�����������еķ��� 
   */
   public static boolean checkService(String ipAddress) 
   {	   
       List list = DiscoverResource.getInstance().getServiceList();
       boolean result = false;
       for(int i=0;i<list.size();i++)
       {	     	   
          Service vo = (Service)list.get(i); 
          if(vo.getPort()==23) continue; //�����telnet,��Ϊ�����豸Ҳ��telnet
          
          Socket socket = new Socket();          
	      try
	      {
              InetAddress addr = InetAddress.getByName(ipAddress);
              SocketAddress sockaddr = new InetSocketAddress(addr,vo.getPort());                                  
              socket.connect(sockaddr, vo.getTimeOut());
    	      Service newVo = new Service();
    	      newVo.setPort(vo.getPort());
    	      newVo.setService(vo.getService());
    	      result = true; 
    	      break;
	      }
	 	  catch(SocketTimeoutException ste)
		  {	 		  
		  }
	 	  catch(IOException ioe)
	 	  {	 		
	 	  }	      
   	      finally
   	      {
   	    	  try
   	    	  {
   	    	     socket.close();
   	    	  }
   	    	  catch(IOException ioe){}
   	      }	       	      
       }   
   	   return result;
   }
      
   /**
    * ȷ��ĳ����ַ�ǲ�����Ҫ���ε�������
    */
   public static boolean isShieldAddress(String address)
   {
	   if(DiscoverResource.getInstance().getShieldSet().size()==0)
		  return false;
	   	   	   
	   boolean result = false;
	   Iterator iterator = DiscoverResource.getInstance().getShieldSet().iterator();
	   while(iterator.hasNext())
	   {		   
		   String netAddress = (String)iterator.next();
		   if(address.indexOf(netAddress)!=-1)
		   {
			   result = true;
			   break;
		   }
	   }	   
	   return result;
   }
   
   public static String getTheFdbOid(String mac)
   {
       String returnStr = "1.3.6.1.2.1.17.4.3.1.2";
       String[] macSegment = new String[6];
		
       try
       {
           StringTokenizer st = new StringTokenizer(mac,":");
	       for(int i=0;i<6;i++)
              macSegment[i] = st.nextToken();
       }
       catch(NoSuchElementException e)
       {
    	   macSegment = null;
       }
       if(macSegment==null) return null;
       
       for(int i = 0; i < 6; i++)
       {
           int tmpInt = transHexToInt(macSegment[i]);
           returnStr = returnStr + "." + tmpInt;
       }      
       return returnStr;
   }

   public static int transHexToInt(String hexStr)
   {
       int totalInt = 0;
       try
       {
           char tmpChars[] = hexStr.toLowerCase().toCharArray();
           if(tmpChars[0] == 'a')
               totalInt += 10;
           if(tmpChars[0] == 'b')
               totalInt += 11;
           if(tmpChars[0] == 'c')
               totalInt += 12;
           if(tmpChars[0] == 'd')
               totalInt += 13;
           if(tmpChars[0] == 'e')
               totalInt += 14;
           if(tmpChars[0] == 'f')
               totalInt += 15;
           if(tmpChars[0] < 'a')
               totalInt += tmpChars[0] - 48;
           totalInt *= 16;
           if(tmpChars[1] == 'a')
               totalInt += 10;
           if(tmpChars[1] == 'b')
               totalInt += 11;
           if(tmpChars[1] == 'c')
               totalInt += 12;
           if(tmpChars[1] == 'd')
               totalInt += 13;
           if(tmpChars[1] == 'e')
               totalInt += 14;
           if(tmpChars[1] == 'f')
               totalInt += 15;
           if(tmpChars[1] < 'a')
               totalInt += tmpChars[1] - 48;
       }
       catch(Exception exception) { }
       return totalInt;
   }
   
   public static int ping(String ipAddress)
   {
	  String line = null;
	  String pingInfo = null;
      try
      {
    	 StringBuffer sb = new StringBuffer(300); 
         Process process = Runtime.getRuntime().exec("ping -n 1 " + ipAddress);
         BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
         while((line = in.readLine()) != null)
            sb.append(line);

         process.destroy();
         in.close();
         pingInfo = sb.toString();
      }
      catch (IOException ioe)
      {
         pingInfo = null;
      }
            
      if (pingInfo == null || pingInfo.indexOf("Destination host unreachable") != -1
    	  ||pingInfo.indexOf("Unknown host") != -1 ||pingInfo.indexOf("Request timed out.") != -1)
         return 0;   
      else
    	 return 1;
   } 
   
   public static String pingReport(String ipAddress)
   {
	  String line = null;
	  String pingInfo = null;
      try
      {
    	 StringBuffer sb = new StringBuffer(300); 
         Process process = Runtime.getRuntime().exec("ping " + ipAddress);
         BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
         while((line = in.readLine()) != null)
            sb.append(line);

         process.destroy();
         in.close();
         pingInfo = sb.toString();
      }
      catch (IOException ioe)
      {
         pingInfo = null;
      }           
      return pingInfo;   
   }
   
   public static boolean checkService(String ipAddress,int port) 
   {	   
       boolean result = false;
       Socket socket = new Socket();
	   try
	   {		   
           InetAddress addr = InetAddress.getByName(ipAddress);
           SocketAddress sockaddr = new InetSocketAddress(addr,port);                                  
           socket.connect(sockaddr, 500);
    	   result = true;     	           	      
	   }
	   catch(SocketTimeoutException ste)
	   {	 		  
		   result = false;
	   }
	   catch(IOException ioe)
	   {	 		
		   result = false;
	   }	      
   	   finally
   	   {
	       try
	       {
	    	   socket.close();
	       }
	       catch(IOException ioe){}	       	      
       }   
   	   return result;
   }
   
   /**
   ��������
	 */	
	private static Runnable createTask(final NodeGatherIndicators alarmIndicatorsNode,final Hashtable alldata) {
   return new Runnable() {
       public void run() {
           try {                	
           	Vector vector=null;
           	Hashtable hashv = new Hashtable();
           	Hashtable returnHash = new Hashtable();
           	HostCollectDataManager hostdataManager=new HostCollectDataManager(); 
           	
           	if(alarmIndicatorsNode.getName().equalsIgnoreCase("cpu")){
           		//CPU�Ĳɼ�
           		if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
           			//CISCO��CPU
           			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           			if(node != null){
           				CiscoCpuSnmp ciscocpusnmp = null;
               			try{
               				ciscocpusnmp = (CiscoCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.CiscoCpuSnmp").newInstance();
                   			returnHash = ciscocpusnmp.collect_Data(alarmIndicatorsNode);
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("cpu", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("cpu", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("cpu", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","cisco","cpu");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}
           		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("h3c")){
           			//H3C��CPU
           			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           			SysLogger.info("&&&&&&&&&&& ��ʼ�ɼ�H3C"+node.getIpAddress()+"��MEMORY &&&&&&&&&&&&&");
           			if(node != null){
           				H3CCpuSnmp h3ccpusnmp = null;
               			try{
               				h3ccpusnmp = (H3CCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.H3CCpuSnmp").newInstance();
                   			returnHash = h3ccpusnmp.collect_Data(alarmIndicatorsNode);
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("cpu", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("cpu", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("cpu", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","h3c","cpu");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}
           		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("zte")){
           			//���˵�CPU
           			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           			if(node != null){
           				ZTECpuSnmp ztecpusnmp = null;
               			try{
               				ztecpusnmp = (ZTECpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.ZTECpuSnmp").newInstance();
                   			returnHash = ztecpusnmp.collect_Data(alarmIndicatorsNode);
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("cpu", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("cpu", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("cpu", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","h3c","cpu");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}
           		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("entrasys")){
           			//������CPU
           			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           			if(node != null){
           				EnterasysCpuSnmp enterasyscpusnmp = null;
               			try{
               				enterasyscpusnmp = (EnterasysCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.EnterasysCpuSnmp").newInstance();
                   			returnHash = enterasyscpusnmp.collect_Data(alarmIndicatorsNode);
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("cpu", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("cpu", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("cpu", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","enterasys","cpu");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}
           		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("radware")){
           			//radware��CPU
           			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           			if(node != null){
           				RadwareCpuSnmp radwarecpusnmp = null;
               			try{
               				radwarecpusnmp = (RadwareCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.RadwareCpuSnmp").newInstance();
                   			returnHash = radwarecpusnmp.collect_Data(alarmIndicatorsNode);
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("cpu", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("cpu", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("cpu", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","radware","cpu");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}
           		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("maipu")){
           			//maipu��CPU
           			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           			if(node != null){
           				MaipuCpuSnmp maipucpusnmp = null;
               			try{
               				maipucpusnmp = (MaipuCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.MaipuCpuSnmp").newInstance();
                   			returnHash = maipucpusnmp.collect_Data(alarmIndicatorsNode);
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("cpu", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("cpu", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("cpu", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","maipu","cpu");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}
           		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("redgiant")){
           			//redgiant��CPU
           			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           			if(node != null){
           				RedGiantCpuSnmp redgiantcpusnmp = null;
               			try{
               				redgiantcpusnmp = (RedGiantCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.RedGiantCpuSnmp").newInstance();
                   			returnHash = redgiantcpusnmp.collect_Data(alarmIndicatorsNode);
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("cpu", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("cpu", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("cpu", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","redgiant","cpu");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}
           		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("northtel")){
           			//�����CPU
           			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           			if(node != null){
           				NortelCpuSnmp nortelcpusnmp = null;
               			try{
               				nortelcpusnmp = (NortelCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.NortelCpuSnmp").newInstance();
                   			returnHash = nortelcpusnmp.collect_Data(alarmIndicatorsNode);
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("cpu", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("cpu", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("cpu", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","northtel","cpu");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}
           		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("dlink")){
           			//dlink��CPU
           			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           			if(node != null){
           				DLinkCpuSnmp dlinkcpusnmp = null;
               			try{
               				dlinkcpusnmp = (DLinkCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.DLinkCpuSnmp").newInstance();
                   			returnHash = dlinkcpusnmp.collect_Data(alarmIndicatorsNode);
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("cpu", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("cpu", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("cpu", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","dlink","cpu");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}
           		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("bdcom")){
           			//�����CPU
           			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           			if(node != null){
           				BDComCpuSnmp bdcomcpusnmp = null;
               			try{
               				bdcomcpusnmp = (BDComCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.BDComCpuSnmp").newInstance();
                   			returnHash = bdcomcpusnmp.collect_Data(alarmIndicatorsNode);
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("cpu", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("cpu", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("cpu", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","bdcom","cpu");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}
           		}
           	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("memory")){
           		//�ڴ�Ĳɼ�
           		if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
               		//CISCO��MEMORY
           			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           			if(node != null){
           				CiscoMemorySnmp ciscomemorysnmp = null;
               			try{
               				ciscomemorysnmp = (CiscoMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.CiscoMemorySnmp").newInstance();
                   			returnHash = ciscomemorysnmp.collect_Data(alarmIndicatorsNode);
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("memory", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("memory", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("memory", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","cisco","memory");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}
           		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("h3c")){ 
           			//h3c��MEMORY
           			
           			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           			SysLogger.info("&&&&&&&&&&& ��ʼ�ɼ�H3C"+node.getIpAddress()+"��MEMORY &&&&&&&&&&&&&");
           			if(node != null){
           				H3CMemorySnmp h3cmemorysnmp = null;
               			try{
               				h3cmemorysnmp = (H3CMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.H3CMemorySnmp").newInstance();
                   			returnHash = h3cmemorysnmp.collect_Data(alarmIndicatorsNode);
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("memory", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("memory", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("memory", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","h3c","memory");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}
           		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("entrasys")){ 
           			//entrasys��MEMORY
           			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           			if(node != null){
           				EnterasysMemorySnmp enterasysmemorysnmp = null;
               			try{
               				enterasysmemorysnmp = (EnterasysMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.EnterasysMemorySnmp").newInstance();
                   			returnHash = enterasysmemorysnmp.collect_Data(alarmIndicatorsNode);
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("memory", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("memory", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("memory", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","entrasys","memory");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}
           		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("radware")){ 
           			//radware��MEMORY
           			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           			if(node != null){
           				EnterasysMemorySnmp enterasysmemorysnmp = null;
               			try{
               				enterasysmemorysnmp = (EnterasysMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.EnterasysMemorySnmp").newInstance();
                   			returnHash = enterasysmemorysnmp.collect_Data(alarmIndicatorsNode);
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("memory", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("memory", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("memory", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","entrasys","memory");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}
           		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("maipu")){ 
           			//maipu��MEMORY
           			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           			if(node != null){
           				MaipuMemorySnmp maipumemorysnmp = null;
               			try{
               				maipumemorysnmp = (MaipuMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.MaipuMemorySnmp").newInstance();
                   			returnHash = maipumemorysnmp.collect_Data(alarmIndicatorsNode);
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("memory", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("memory", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("memory", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","maipu","memory");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}
           		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("redgiant")){ 
           			//redgiant��MEMORY
           			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           			if(node != null){
           				RedGiantMemorySnmp redmemorysnmp = null;
               			try{
               				redmemorysnmp = (RedGiantMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.RedGiantMemorySnmp").newInstance();
                   			returnHash = redmemorysnmp.collect_Data(alarmIndicatorsNode);
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("memory", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("memory", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("memory", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","redgiant","memory");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}
           		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("northtel")){ 
           			//northtel��MEMORY
           			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           			if(node != null){
           				NortelMemorySnmp nortelmemorysnmp = null;
               			try{
               				nortelmemorysnmp = (NortelMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.NortelMemorySnmp").newInstance();
                   			returnHash = nortelmemorysnmp.collect_Data(alarmIndicatorsNode);
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("memory", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("memory", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("memory", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","northtel","memory");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}
           		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("dlink")){ 
           			//dlink��MEMORY
           			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           			if(node != null){
           				DLinkMemorySnmp dlinkmemorysnmp = null;
               			try{
               				dlinkmemorysnmp = (DLinkMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.DLinkMemorySnmp").newInstance();
                   			returnHash = dlinkmemorysnmp.collect_Data(alarmIndicatorsNode);
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("memory", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("memory", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("memory", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","dlink","memory");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}
           		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("bdcom")){ 
           			//bdcom��MEMORY
           			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           			if(node != null){
           				BDComMemorySnmp bdcommemorysnmp = null;
               			try{
               				bdcommemorysnmp = (BDComMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.BDComMemorySnmp").newInstance();
                   			returnHash = bdcommemorysnmp.collect_Data(alarmIndicatorsNode);
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("memory", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("memory", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("memory", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","bdcom","memory");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}
           		}
           	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("systemgroup")){
           		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           		if(node != null){
           			SystemSnmp systemsnmp = null;
           			try{
           				systemsnmp = (SystemSnmp)Class.forName("com.afunms.polling.snmp.system.SystemSnmp").newInstance();
               			returnHash = systemsnmp.collect_Data(alarmIndicatorsNode);
               			if(alldata.containsKey(node.getIpAddress())){
               				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
               				if(ipdata != null){
               					ipdata.put("systemgroup", returnHash);
               				}else{
               					ipdata = new Hashtable();
               					ipdata.put("systemgroup", returnHash);
               				}
               				alldata.put(node.getIpAddress(), ipdata);
               			}else{
               				Hashtable ipdata = new Hashtable();
               				ipdata.put("systemgroup", returnHash);
               				alldata.put(node.getIpAddress(), ipdata);
               			}
               			//IP-MAC���������ݿ�
               			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"ipmac");
               		}catch(Exception e){
               			e.printStackTrace();
               		}
           		}
           	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("utilhdx")){
//           		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//           		if(node != null){
//           			UtilHdxSnmp utilhdxsnmp = null;
//           			try{
//           				utilhdxsnmp = (UtilHdxSnmp)Class.forName("com.afunms.polling.snmp.interfaces.UtilHdxSnmp").newInstance();
//               			returnHash = utilhdxsnmp.collect_Data(alarmIndicatorsNode);
//               			//�������ݿ�
//               			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"utilhdx");
//               		}catch(Exception e){
//               			e.printStackTrace();
//               		}
//           		}
           	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("interface")){
           		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           		if(node != null){
           			InterfaceSnmp interfacesnmp = null;
           			try{
//           				SysLogger.info("############################################################");
//           				SysLogger.info("##########��ʼ�ɼ�"+node.getIpAddress()+" �ӿ���Ϣ...##########");
//           				SysLogger.info("############################################################");
           				interfacesnmp = (InterfaceSnmp)Class.forName("com.afunms.polling.snmp.interfaces.InterfaceSnmp").newInstance();
               			returnHash = interfacesnmp.collect_Data(alarmIndicatorsNode);            			
               			if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("h3c")){
               				//�Գ���������ֵ���и澯���
                   		    try{
                   				AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                   				List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(node.getId()), AlarmConstant.TYPE_NET, "h3c");
                   				for(int i = 0 ; i < list.size() ; i ++){
                   					AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
                   					if(alarmIndicatorsnode.getName().equalsIgnoreCase("AllInBandwidthUtilHdx") || alarmIndicatorsnode.getName().equalsIgnoreCase("AllOutBandwidthUtilHdx")
                   							|| alarmIndicatorsnode.getName().equalsIgnoreCase("utilhdx")){
                   						//���ܳ��������ֵ���и澯���
                       					CheckEventUtil checkutil = new CheckEventUtil();
                       					checkutil.updateData(node,returnHash,"net","h3c",alarmIndicatorsnode);
                   					}
                   				}
                   		    }catch(Exception e){
                   		    	e.printStackTrace();
                   		    }
               			}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
               				//�Գ���������ֵ���и澯���
                   		    try{
                   				AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                   				List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(node.getId()), AlarmConstant.TYPE_NET, "cisco");
                   				for(int i = 0 ; i < list.size() ; i ++){
                   					AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
                   					if(alarmIndicatorsnode.getName().equalsIgnoreCase("AllInBandwidthUtilHdx") || alarmIndicatorsnode.getName().equalsIgnoreCase("AllOutBandwidthUtilHdx")
                   							|| alarmIndicatorsnode.getName().equalsIgnoreCase("utilhdx")){
                   						//���ܳ��������ֵ���и澯���
                       					CheckEventUtil checkutil = new CheckEventUtil();
                       					checkutil.updateData(node,returnHash,"net","cisco",alarmIndicatorsnode);
                   					}
                   				}
                   		    }catch(Exception e){
                   		    	e.printStackTrace();
                   		    }
               			}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("zte")){
               				//�Գ���������ֵ���и澯���
                   		    try{
                   				AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                   				List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(node.getId()), AlarmConstant.TYPE_NET, "zte");
                   				for(int i = 0 ; i < list.size() ; i ++){
                   					AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
                   					if(alarmIndicatorsnode.getName().equalsIgnoreCase("AllInBandwidthUtilHdx") || alarmIndicatorsnode.getName().equalsIgnoreCase("AllOutBandwidthUtilHdx")
                   							|| alarmIndicatorsnode.getName().equalsIgnoreCase("utilhdx")){
                   						//���ܳ��������ֵ���и澯���
                       					CheckEventUtil checkutil = new CheckEventUtil();
                       					checkutil.updateData(node,returnHash,"net","zte",alarmIndicatorsnode);
                   					}
                   				}
                   		    }catch(Exception e){
                   		    	e.printStackTrace();
                   		    }
               			}
               			//�������ݿ�
               			if(alldata.containsKey(node.getIpAddress())){
               				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
               				if(ipdata != null){
               					ipdata.put("interface", returnHash);
               				}else{
               					ipdata = new Hashtable();
               					ipdata.put("interface", returnHash);
               				}
               				alldata.put(node.getIpAddress(), ipdata);
               			}else{
               				Hashtable ipdata = new Hashtable();
               				ipdata.put("interface", returnHash);
               				alldata.put(node.getIpAddress(), ipdata);
               			}
               			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"interface");

           			}catch(Exception e){
               			e.printStackTrace();
               		}
           		}
           	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("packs")){
           		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           		if(node != null){
           			PackageSnmp packagesnmp = null;
           			try{
           				packagesnmp = (PackageSnmp)Class.forName("com.afunms.polling.snmp.interfaces.PackageSnmp").newInstance();
               			returnHash = packagesnmp.collect_Data(alarmIndicatorsNode);
               			//�������ݿ�
               			if(alldata.containsKey(node.getIpAddress())){
               				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
               				if(ipdata != null){
               					ipdata.put("packs", returnHash);
               				}else{
               					ipdata = new Hashtable();
               					ipdata.put("packs", returnHash);
               				}
               				alldata.put(node.getIpAddress(), ipdata);
               			}else{
               				Hashtable ipdata = new Hashtable();
               				ipdata.put("packs", returnHash);
               				alldata.put(node.getIpAddress(), ipdata);
               			}
               			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"packs");
               		}catch(Exception e){
               			e.printStackTrace();
               		}
           		}
           	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("ping")){
           		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           		if(node != null){
           			PingSnmp pingsnmp = null;
           			try{
           				pingsnmp = (PingSnmp)Class.forName("com.afunms.polling.snmp.ping.PingSnmp").newInstance();
               			returnHash = pingsnmp.collect_Data(alarmIndicatorsNode);
               			if(alldata.containsKey(node.getIpAddress())){
               				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
               				if(ipdata != null){
               					ipdata.put("ping", returnHash);
               				}else{
               					ipdata = new Hashtable();
               					ipdata.put("ping", returnHash);
               				}
               				alldata.put(node.getIpAddress(), ipdata);
               			}else{
               				Hashtable ipdata = new Hashtable();
               				ipdata.put("ping", returnHash);
               				alldata.put(node.getIpAddress(), ipdata);
               			}
               			//�ڲɼ��������Ѿ��������ݿ�
               			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"utilhdx");
               		}catch(Exception e){
               			e.printStackTrace();
               		}
           		}
           	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("ipmac")){
           		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           		if(node != null){
           			ArpSnmp arpsnmp = null;
           			try{
           				arpsnmp = (ArpSnmp)Class.forName("com.afunms.polling.snmp.interfaces.ArpSnmp").newInstance();
               			returnHash = arpsnmp.collect_Data(alarmIndicatorsNode);
               			if(alldata.containsKey(node.getIpAddress())){
               				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
               				if(ipdata != null){
               					ipdata.put("ipmac", returnHash);
               				}else{
               					ipdata = new Hashtable();
               					ipdata.put("ipmac", returnHash);
               				}
               				alldata.put(node.getIpAddress(), ipdata);
               			}else{
               				Hashtable ipdata = new Hashtable();
               				ipdata.put("ipmac", returnHash);
               				alldata.put(node.getIpAddress(), ipdata);
               			}
               			//IP-MAC���������ݿ�
               			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"ipmac");
               		}catch(Exception e){
               			e.printStackTrace();
               		}
           		}
           	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("router")){
           		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           		if(node != null){
           			RouterSnmp routersnmp = null;
           			try{
           				routersnmp = (RouterSnmp)Class.forName("com.afunms.polling.snmp.interfaces.RouterSnmp").newInstance();
               			returnHash = routersnmp.collect_Data(alarmIndicatorsNode);
               			if(alldata.containsKey(node.getIpAddress())){
               				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
               				if(ipdata != null){
               					ipdata.put("iprouter", returnHash);
               				}else{
               					ipdata = new Hashtable();
               					ipdata.put("iprouter", returnHash);
               				}
               				alldata.put(node.getIpAddress(), ipdata);
               			}else{
               				Hashtable ipdata = new Hashtable();
               				ipdata.put("iprouter", returnHash);
               				alldata.put(node.getIpAddress(), ipdata);
               			}
               			//·�ɱ��������ݿ�
               			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"ipmac");
               		}catch(Exception e){
               			e.printStackTrace();
               		}
           		}
           	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("fdb")){
           		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           		if(node != null){
           			FdbSnmp fdbsnmp = null;
           			try{
           				//SysLogger.info("��ʼ�ɼ�FDB��################");
           				fdbsnmp = (FdbSnmp)Class.forName("com.afunms.polling.snmp.interfaces.FdbSnmp").newInstance();
               			returnHash = fdbsnmp.collect_Data(alarmIndicatorsNode);
               			if(alldata.containsKey(node.getIpAddress())){
               				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
               				if(ipdata != null){
               					ipdata.put("fdb", returnHash);
               				}else{
               					ipdata = new Hashtable();
               					ipdata.put("fdb", returnHash);
               				}
               				alldata.put(node.getIpAddress(), ipdata);
               			}else{
               				Hashtable ipdata = new Hashtable();
               				ipdata.put("fdb", returnHash);
               				alldata.put(node.getIpAddress(), ipdata);
               			}
               			//FDB���������ݿ�
               			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"ipmac");
               		}catch(Exception e){
               			e.printStackTrace();
               		}
           		}
           		
           	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("flash")){
           		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           		if(node != null){
           			if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
           				//CISCO����
           				CiscoFlashSnmp flashsnmp = null;
               			try{
               				//SysLogger.info("��ʼ�ɼ�FLASH��Ϣ################");
               				flashsnmp = (CiscoFlashSnmp)Class.forName("com.afunms.polling.snmp.flash.CiscoFlashSnmp").newInstance();
                   			returnHash = flashsnmp.collect_Data(alarmIndicatorsNode);
                   			//flash���������ݿ�
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("flash", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("flash", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("flash", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"flash");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("h3c")){
           				//CISCO����
           				H3CFlashSnmp flashsnmp = null;
               			try{
               				//SysLogger.info("��ʼ�ɼ�h3c��FLASH��Ϣ################");
               				flashsnmp = (H3CFlashSnmp)Class.forName("com.afunms.polling.snmp.flash.H3CFlashSnmp").newInstance();
                   			returnHash = flashsnmp.collect_Data(alarmIndicatorsNode);
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("flash", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("flash", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("flash", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"flash");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("bdcom")){
           				//BDCOM����
           				BDComFlashSnmp flashsnmp = null;
               			try{
               				//SysLogger.info("��ʼ�ɼ�h3c��FLASH��Ϣ################");
               				flashsnmp = (BDComFlashSnmp)Class.forName("com.afunms.polling.snmp.flash.BDComFlashSnmp").newInstance();
                   			returnHash = flashsnmp.collect_Data(alarmIndicatorsNode);
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("flash", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("flash", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("flash", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"flash");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}
           			
           		}
           	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("temperature")){
           		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           		if(node != null){
           			if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
           				//CISCO�¶�
           				CiscoTemperatureSnmp tempersnmp = null;
               			try{
               				//SysLogger.info("��ʼ�ɼ��¶���Ϣ################");
               				tempersnmp = (CiscoTemperatureSnmp)Class.forName("com.afunms.polling.snmp.temperature.CiscoTemperatureSnmp").newInstance();
                   			returnHash = tempersnmp.collect_Data(alarmIndicatorsNode);
                   			//FDB���������ݿ�
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("temperature", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("temperature", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("temperature", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"temperature");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("h3c")){
           				//h3c�¶�
           				H3CTemperatureSnmp tempersnmp = null;
               			try{
               				//SysLogger.info("��ʼ�ɼ�H3C�¶���Ϣ################");
               				tempersnmp = (H3CTemperatureSnmp)Class.forName("com.afunms.polling.snmp.temperature.H3CTemperatureSnmp").newInstance();
                   			returnHash = tempersnmp.collect_Data(alarmIndicatorsNode);
                   			//FDB���������ݿ�
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("temperature", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("temperature", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"temperature");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("bdcom")){
           				//bdcom�¶�
           				BDComTemperatureSnmp tempersnmp = null;
               			try{
               				//SysLogger.info("��ʼ�ɼ�BDCOM�¶���Ϣ################");
               				tempersnmp = (BDComTemperatureSnmp)Class.forName("com.afunms.polling.snmp.temperature.BDComTemperatureSnmp").newInstance();
                   			returnHash = tempersnmp.collect_Data(alarmIndicatorsNode);
                   			//FDB���������ݿ�
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("temperature", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("temperature", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("temperature", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"temperature");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}
           			
           		}
           	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("fan")){
           		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           		if(node != null){
           			if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
           				//CISCO����
           				CiscoFanSnmp fansnmp = null;
               			try{
               				//SysLogger.info("��ʼ�ɼ��¶���Ϣ################");
               				fansnmp = (CiscoFanSnmp)Class.forName("com.afunms.polling.snmp.fan.CiscoFanSnmp").newInstance();
                   			returnHash = fansnmp.collect_Data(alarmIndicatorsNode);
                   			//FDB���������ݿ�
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("fan", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("fan", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("fan", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"fan");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("h3c")){
           				//H3C����
           				H3CFanSnmp fansnmp = null;
               			try{
               				//SysLogger.info("��ʼ�ɼ��¶���Ϣ################");
               				fansnmp = (H3CFanSnmp)Class.forName("com.afunms.polling.snmp.fan.H3CFanSnmp").newInstance();
                   			returnHash = fansnmp.collect_Data(alarmIndicatorsNode);
                   			//FDB���������ݿ�
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("fan", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("fan", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("fan", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"fan");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}
           		}
           	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("power")){
           		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           		if(node != null){
           			if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
           				//CISCO��Դ
           				CiscoPowerSnmp powersnmp = null;
               			try{
               				//SysLogger.info("��ʼ�ɼ�CISCO��Դ��Ϣ################");
               				powersnmp = (CiscoPowerSnmp)Class.forName("com.afunms.polling.snmp.power.CiscoPowerSnmp").newInstance();
                   			returnHash = powersnmp.collect_Data(alarmIndicatorsNode);
                   			//�������ݿ�
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("power", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("power", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("power", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"power");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("h3c")){
           				//H3C��Դ
           				H3CPowerSnmp powersnmp = null;
               			try{
               				//SysLogger.info("��ʼ�ɼ�H3C��Դ��Ϣ################");
               				powersnmp = (H3CPowerSnmp)Class.forName("com.afunms.polling.snmp.power.H3CPowerSnmp").newInstance();
                   			returnHash = powersnmp.collect_Data(alarmIndicatorsNode);
                   			//�������ݿ�
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("power", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("power", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("power", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"power");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}
           		}
           	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("voltage")){
           		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
           		if(node != null){
           			if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
           				//CISCO��ѹ
           				CiscoVoltageSnmp voltagesnmp = null;
               			try{
               				//SysLogger.info("��ʼ�ɼ�CISCO��ѹ��Ϣ################");
               				voltagesnmp = (CiscoVoltageSnmp)Class.forName("com.afunms.polling.snmp.voltage.CiscoVoltageSnmp").newInstance();
                   			returnHash = voltagesnmp.collect_Data(alarmIndicatorsNode);
                   			//�������ݿ�
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("voltage", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("voltage", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("voltage", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"voltage");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("h3c")){
           				//H3C��ѹ
           				H3CVoltageSnmp voltagesnmp = null;
               			try{
               				//SysLogger.info("��ʼ�ɼ�H3C��ѹ��Ϣ################");
               				voltagesnmp = (H3CVoltageSnmp)Class.forName("com.afunms.polling.snmp.voltage.H3CVoltageSnmp").newInstance();
                   			returnHash = voltagesnmp.collect_Data(alarmIndicatorsNode);
                   			//�������ݿ�
                   			if(alldata.containsKey(node.getIpAddress())){
                   				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                   				if(ipdata != null){
                   					ipdata.put("voltage", returnHash);
                   				}else{
                   					ipdata = new Hashtable();
                   					ipdata.put("voltage", returnHash);
                   				}
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}else{
                   				Hashtable ipdata = new Hashtable();
                   				ipdata.put("voltage", returnHash);
                   				alldata.put(node.getIpAddress(), ipdata);
                   			}
                   			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"voltage");
                   		}catch(Exception e){
                   			e.printStackTrace();
                   		}
           			}
           		}
           	}
       		
           }catch(Exception exc){
           	
           }
       }
   };
	}
}
