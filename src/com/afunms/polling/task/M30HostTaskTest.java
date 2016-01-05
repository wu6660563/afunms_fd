/*
 * Created on 2010-06-24
 *
 */
package com.afunms.polling.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.TimerTask;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;
import org.python.modules.thread;

import com.afunms.alarm.dao.AlarmPortDao;
import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.Huawei3comtelnetUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.config.dao.PortconfigDao;
import com.afunms.config.model.Portconfig;
import com.afunms.discovery.IpAddress;
import com.afunms.discovery.Node;
import com.afunms.event.dao.CheckEventDao;
import com.afunms.event.model.CheckEvent;
import com.afunms.indicators.dao.NodeGatherIndicatorsDao;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Pingcollectdata;
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
import com.afunms.topology.dao.ConnectTypeConfigDao;
import com.afunms.topology.dao.DiscoverConfigDao;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.ConnectTypeConfig;
import com.afunms.topology.model.HostNode;





/**
 * @author nielin
 * @date 2010-05-16 
 * 
 * �Է��������вɼ�
 *
 */
public class M30HostTaskTest extends MonitorTask {
	private String nodeid;//�ɼ��Ľڵ�ID
	
	public String getNodeid() {
		return nodeid;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	public void run(){
		if(nodeid == null){//�ýڵ㲻����,nodeidδ����ʼ��
			SysLogger.info("�ýڵ㲻����,nodeidδ����ʼ��");
			return;
		}
		Host node = (Host) PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeid)); 
		if(node == null){//�ýڵ���ȡ���ɼ�
			SysLogger.info("�ýڵ�nodeid:"+nodeid+"��ȡ���ɼ�");
			return;
		}
    	try {                	
        	Hashtable returnHash = new Hashtable();
        	AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
        	CheckEventUtil checkutil = new CheckEventUtil();
        	InterfaceSnmp interfacesnmp = null;
        	NodeGatherIndicators nodeGatherIndicatorsNode = null;
        	//ȡ�������ӽڵ������ָ��
        	//List list = TaskUtil.getGatherIndicatorsList(nodeid, "30", "m", 1, "host");
        	List list = (List)ShareData.getGatherHash().get(nodeid+":host:30:m");
        	for(int k=0; k<list.size(); k++){
    			nodeGatherIndicatorsNode = (NodeGatherIndicators)list.get(k);
    			//SysLogger.info(node.getIpAddress()+" ��ʼ�ɼ�ָ�꣺"+nodeGatherIndicatorsNode.getName());
    			if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("cpu")){
            		//CPU�Ĳɼ�
            		if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("windows")){
            			//Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            			if(node != null){
            				if(node.isManaged()){
            					WindowsCpuSnmp windowscpusnmp = null;
                    			try{
                        			windowscpusnmp = (WindowsCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.WindowsCpuSnmp").newInstance();
                        			returnHash = windowscpusnmp.collect_Data(nodeGatherIndicatorsNode);
                        			windowscpusnmp = null;
                        			if(ShareData.getM30HostAlldata().containsKey(node.getIpAddress())){
    	                				Hashtable ipdata = (Hashtable)ShareData.getM30HostAlldata().get(node.getIpAddress());
    	                				if(ipdata != null){
    	                					ipdata.put("cpu", returnHash);
    	                				}else{
    	                					ipdata = new Hashtable();
    	                					ipdata.put("cpu", returnHash);
    	                				}
    	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
    	                			}else{
    	                				Hashtable ipdata = new Hashtable();
    	                				ipdata.put("cpu", returnHash);
    	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
    	                			}
                        			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","cpu");
                        		}catch(Exception e){
                        			e.printStackTrace();
                        		}
            				}
            				
            			}
            			
            		}else if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("linux")){
            			//LINUX��CPU
            			//Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            			if(node != null){
            				LinuxCpuSnmp linuxcpusnmp = null;
                			try{
                				linuxcpusnmp = (LinuxCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.LinuxCpuSnmp").newInstance();
                    			returnHash = linuxcpusnmp.collect_Data(nodeGatherIndicatorsNode);
                    			linuxcpusnmp = null;
                    			if(ShareData.getM30HostAlldata().containsKey(node.getIpAddress())){
	                				Hashtable ipdata = (Hashtable)ShareData.getM30HostAlldata().get(node.getIpAddress());
	                				if(ipdata != null){
	                					ipdata.put("cpu", returnHash);
	                				}else{
	                					ipdata = new Hashtable();
	                					ipdata.put("cpu", returnHash);
	                				}
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}else{
	                				Hashtable ipdata = new Hashtable();
	                				ipdata.put("cpu", returnHash);
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}
                    			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","linux","cpu");
                    		}catch(Exception e){
                    			e.printStackTrace();
                    		}
            			}
            		}
            	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("disk")){
            		//DISK�Ĳɼ�
            		if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("windows")){
            			//Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            			if(node != null){
            				WindowsDiskSnmp windowdisksnmp = null;
                			try{
                				windowdisksnmp = (WindowsDiskSnmp)Class.forName("com.afunms.polling.snmp.disk.WindowsDiskSnmp").newInstance();
                    			returnHash = windowdisksnmp.collect_Data(nodeGatherIndicatorsNode);
                    			windowdisksnmp = null;
                    			if(ShareData.getM30HostAlldata().containsKey(node.getIpAddress())){
	                				Hashtable ipdata = (Hashtable)ShareData.getM30HostAlldata().get(node.getIpAddress());
	                				if(ipdata != null){
	                					ipdata.put("disk", returnHash);
	                				}else{
	                					ipdata = new Hashtable();
	                					ipdata.put("disk", returnHash);
	                				}
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}else{
	                				Hashtable ipdata = new Hashtable();
	                				ipdata.put("disk", returnHash);
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}
                    			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","disk");
                    		}catch(Exception e){
                    			e.printStackTrace();
                    		}
            			}
            			
            		}else if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("linux")){
            			//LINUX��DISK
            			//Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            			if(node != null){
            				LinuxDiskSnmp linuxdisksnmp = null;
                			try{
                				linuxdisksnmp = (LinuxDiskSnmp)Class.forName("com.afunms.polling.snmp.disk.LinuxDiskSnmp").newInstance();
                    			returnHash = linuxdisksnmp.collect_Data(nodeGatherIndicatorsNode);
                    			linuxdisksnmp = null;
                    			if(ShareData.getM30HostAlldata().containsKey(node.getIpAddress())){
	                				Hashtable ipdata = (Hashtable)ShareData.getM30HostAlldata().get(node.getIpAddress());
	                				if(ipdata != null){
	                					ipdata.put("disk", returnHash);
	                				}else{
	                					ipdata = new Hashtable();
	                					ipdata.put("disk", returnHash);
	                				}
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}else{
	                				Hashtable ipdata = new Hashtable();
	                				ipdata.put("disk", returnHash);
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}
                    			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","linux","disk");
                    		}catch(Exception e){
                    			//e.printStackTrace();
                    		}
            			}
            		}
            	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("process")){
            		//�洢��Ϣ�Ĳɼ�
            		if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("windows")){
            			//Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            			if(node != null){
            				WindowsProcessSnmp windowsprocesssnmp = null;
                			try{
                				windowsprocesssnmp = (WindowsProcessSnmp)Class.forName("com.afunms.polling.snmp.process.WindowsProcessSnmp").newInstance();
                    			returnHash = windowsprocesssnmp.collect_Data(nodeGatherIndicatorsNode);
                    			windowsprocesssnmp = null;
                    			if(ShareData.getM30HostAlldata().containsKey(node.getIpAddress())){
	                				Hashtable ipdata = (Hashtable)ShareData.getM30HostAlldata().get(node.getIpAddress());
	                				if(ipdata != null){
	                					ipdata.put("process", returnHash);
	                				}else{
	                					ipdata = new Hashtable();
	                					ipdata.put("process", returnHash);
	                				}
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}else{
	                				Hashtable ipdata = new Hashtable();
	                				ipdata.put("process", returnHash);
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}
                    			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","process");
                    		}catch(Exception e){
                    			e.printStackTrace();
                    		}
            			}         			
            		}else if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("linux")){
            			//Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            			if(node != null){
            				LinuxProcessSnmp linuxprocesssnmp = null;
                			try{
                				SysLogger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                				SysLogger.info("&&&&&& ��ʼ�ɼ�LINUX "+nodeGatherIndicatorsNode.getNodeid()+"  "+nodeGatherIndicatorsNode.getName() +"   &&&&&&&&&&&");
                				SysLogger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                				linuxprocesssnmp = (LinuxProcessSnmp)Class.forName("com.afunms.polling.snmp.process.LinuxProcessSnmp").newInstance();
                    			returnHash = linuxprocesssnmp.collect_Data(nodeGatherIndicatorsNode);
                    			linuxprocesssnmp = null;
                    			if(ShareData.getM30HostAlldata().containsKey(node.getIpAddress())){
	                				Hashtable ipdata = (Hashtable)ShareData.getM30HostAlldata().get(node.getIpAddress());
	                				if(ipdata != null){
	                					ipdata.put("process", returnHash);
	                				}else{
	                					ipdata = new Hashtable();
	                					ipdata.put("process", returnHash);
	                				}
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}else{
	                				Hashtable ipdata = new Hashtable();
	                				ipdata.put("process", returnHash);
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}
                    			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","linux","process");
                    		}catch(Exception e){
                    			//e.printStackTrace();
                    		}
            			}
            		}
            	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("service")){
            		//����Ĳɼ�
            		if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("windows")){
            			//Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            			if(node != null){
            				WindowsServiceSnmp windowservicesnmp = null;
                			try{
                				windowservicesnmp = (WindowsServiceSnmp)Class.forName("com.afunms.polling.snmp.service.WindowsServiceSnmp").newInstance();
                    			returnHash = windowservicesnmp.collect_Data(nodeGatherIndicatorsNode);
                    			windowservicesnmp = null;
                    			if(ShareData.getM30HostAlldata().containsKey(node.getIpAddress())){
	                				Hashtable ipdata = (Hashtable)ShareData.getM30HostAlldata().get(node.getIpAddress());
	                				if(ipdata != null){
	                					ipdata.put("service", returnHash);
	                				}else{
	                					ipdata = new Hashtable();
	                					ipdata.put("service", returnHash);
	                				}
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}else{
	                				Hashtable ipdata = new Hashtable();
	                				ipdata.put("service", returnHash);
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}
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
            			//Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            			if(node != null){
            				WindowsSoftwareSnmp windowssoftwaresnmp = null;
                			try{
                				windowssoftwaresnmp = (WindowsSoftwareSnmp)Class.forName("com.afunms.polling.snmp.software.WindowsSoftwareSnmp").newInstance();
                    			returnHash = windowssoftwaresnmp.collect_Data(nodeGatherIndicatorsNode);
                    			windowssoftwaresnmp = null;
                    			if(ShareData.getM30HostAlldata().containsKey(node.getIpAddress())){
	                				Hashtable ipdata = (Hashtable)ShareData.getM30HostAlldata().get(node.getIpAddress());
	                				if(ipdata != null){
	                					ipdata.put("software", returnHash);
	                				}else{
	                					ipdata = new Hashtable();
	                					ipdata.put("software", returnHash);
	                				}
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}else{
	                				Hashtable ipdata = new Hashtable();
	                				ipdata.put("software", returnHash);
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}
                    			//Ŀǰ���û��ÿ�ζ��������ݿ�,��Ҫ�ֹ�ͬ��
                    			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","service");
                    		}catch(Exception e){
                    			e.printStackTrace();
                    		}
            			}
            		
            		}if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("linux")){
            			//Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            			if(node != null){
            				LinuxSoftwareSnmp linuxsoftwaresnmp = null;
                			try{
                				linuxsoftwaresnmp = (LinuxSoftwareSnmp)Class.forName("com.afunms.polling.snmp.software.LinuxSoftwareSnmp").newInstance();
                    			returnHash = linuxsoftwaresnmp.collect_Data(nodeGatherIndicatorsNode);
                    			linuxsoftwaresnmp = null;
                    			if(ShareData.getM30HostAlldata().containsKey(node.getIpAddress())){
	                				Hashtable ipdata = (Hashtable)ShareData.getM30HostAlldata().get(node.getIpAddress());
	                				if(ipdata != null){
	                					ipdata.put("software", returnHash);
	                				}else{
	                					ipdata = new Hashtable();
	                					ipdata.put("software", returnHash);
	                				}
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}else{
	                				Hashtable ipdata = new Hashtable();
	                				ipdata.put("software", returnHash);
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}
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
            			//Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            			if(node != null){
            				WindowsDeviceSnmp windowsdevicesnmp = null;
                			try{
                				windowsdevicesnmp = (WindowsDeviceSnmp)Class.forName("com.afunms.polling.snmp.device.WindowsDeviceSnmp").newInstance();
                    			returnHash = windowsdevicesnmp.collect_Data(nodeGatherIndicatorsNode);
                    			windowsdevicesnmp = null;
                    			if(ShareData.getM30HostAlldata().containsKey(node.getIpAddress())){
	                				Hashtable ipdata = (Hashtable)ShareData.getM30HostAlldata().get(node.getIpAddress());
	                				if(ipdata != null){
	                					ipdata.put("hardware", returnHash);
	                				}else{
	                					ipdata = new Hashtable();
	                					ipdata.put("hardware", returnHash);
	                				}
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}else{
	                				Hashtable ipdata = new Hashtable();
	                				ipdata.put("hardware", returnHash);
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}
                    			//Ŀǰ�豸��Ϣû��ÿ�ζ��������ݿ�,��Ҫ�ֹ�ͬ��
                    			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","service");
                    		}catch(Exception e){
                    			e.printStackTrace();
                    		}
            			}
            			
            		}else if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("linux")){
            			//Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            			if(node != null){
            				LinuxDeviceSnmp linuxdevicesnmp = null;
                			try{
                				linuxdevicesnmp = (LinuxDeviceSnmp)Class.forName("com.afunms.polling.snmp.device.LinuxDeviceSnmp").newInstance();
                    			returnHash = linuxdevicesnmp.collect_Data(nodeGatherIndicatorsNode);
                    			linuxdevicesnmp = null;
                    			if(ShareData.getM30HostAlldata().containsKey(node.getIpAddress())){
	                				Hashtable ipdata = (Hashtable)ShareData.getM30HostAlldata().get(node.getIpAddress());
	                				if(ipdata != null){
	                					ipdata.put("hardware", returnHash);
	                				}else{
	                					ipdata = new Hashtable();
	                					ipdata.put("hardware", returnHash);
	                				}
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}else{
	                				Hashtable ipdata = new Hashtable();
	                				ipdata.put("hardware", returnHash);
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}
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
            			//Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            			if(node != null){
            				WindowsStorageSnmp windowsstoragesnmp = null;
                			try{
                				windowsstoragesnmp = (WindowsStorageSnmp)Class.forName("com.afunms.polling.snmp.storage.WindowsStorageSnmp").newInstance();
                    			returnHash = windowsstoragesnmp.collect_Data(nodeGatherIndicatorsNode);
                    			windowsstoragesnmp = null;
                    			if(ShareData.getM30HostAlldata().containsKey(node.getIpAddress())){
	                				Hashtable ipdata = (Hashtable)ShareData.getM30HostAlldata().get(node.getIpAddress());
	                				if(ipdata != null){
	                					ipdata.put("storage", returnHash);
	                				}else{
	                					ipdata = new Hashtable();
	                					ipdata.put("storage", returnHash);
	                				}
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}else{
	                				Hashtable ipdata = new Hashtable();
	                				ipdata.put("storage", returnHash);
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}
                    			//Ŀǰ�洢��Ϣû��ÿ�ζ��������ݿ�,��Ҫ�ֹ�ͬ��
                    			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","service");
                    		}catch(Exception e){
                    			e.printStackTrace();
                    		}
            			}	
            		}else if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("linux")){
            			//Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            			if(node != null){
            				LinuxStorageSnmp linuxstoragesnmp = null;
                			try{
                				linuxstoragesnmp = (LinuxStorageSnmp)Class.forName("com.afunms.polling.snmp.storage.LinuxStorageSnmp").newInstance();
                    			returnHash = linuxstoragesnmp.collect_Data(nodeGatherIndicatorsNode);
                    			linuxstoragesnmp = null;
                    			if(ShareData.getM30HostAlldata().containsKey(node.getIpAddress())){
	                				Hashtable ipdata = (Hashtable)ShareData.getM30HostAlldata().get(node.getIpAddress());
	                				if(ipdata != null){
	                					ipdata.put("storage", returnHash);
	                				}else{
	                					ipdata = new Hashtable();
	                					ipdata.put("storage", returnHash);
	                				}
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}else{
	                				Hashtable ipdata = new Hashtable();
	                				ipdata.put("storage", returnHash);
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}
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
            			//Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            			if(node != null){
            				WindowsPhysicalMemorySnmp windowsphysicalsnmp = null;
                			try{
                				windowsphysicalsnmp = (WindowsPhysicalMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.WindowsPhysicalMemorySnmp").newInstance();
                    			returnHash = windowsphysicalsnmp.collect_Data(nodeGatherIndicatorsNode);
                    			windowsphysicalsnmp = null;
                    			if(ShareData.getM30HostAlldata().containsKey(node.getIpAddress())){
	                				Hashtable ipdata = (Hashtable)ShareData.getM30HostAlldata().get(node.getIpAddress());
	                				if(ipdata != null){
	                					ipdata.put("physicalmemory", returnHash);
	                				}else{
	                					ipdata = new Hashtable();
	                					ipdata.put("physicalmemory", returnHash);
	                				}
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}else{
	                				Hashtable ipdata = new Hashtable();
	                				ipdata.put("physicalmemory", returnHash);
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}
                    			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","physicalmemory");
                    		}catch(Exception e){
                    			e.printStackTrace();
                    		}
            			}
            		}else if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("linux")){
            			//Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            			if(node != null){
            				LinuxPhysicalMemorySnmp linuxphysicalsnmp = null;
                			try{
                				linuxphysicalsnmp = (LinuxPhysicalMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.LinuxPhysicalMemorySnmp").newInstance();
                    			returnHash = linuxphysicalsnmp.collect_Data(nodeGatherIndicatorsNode);
                    			linuxphysicalsnmp = null;
                    			if(ShareData.getM30HostAlldata().containsKey(node.getIpAddress())){
	                				Hashtable ipdata = (Hashtable)ShareData.getM30HostAlldata().get(node.getIpAddress());
	                				if(ipdata != null){
	                					ipdata.put("physicalmemory", returnHash);
	                				}else{
	                					ipdata = new Hashtable();
	                					ipdata.put("physicalmemory", returnHash);
	                				}
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}else{
	                				Hashtable ipdata = new Hashtable();
	                				ipdata.put("physicalmemory", returnHash);
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}
                    			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","linux","physicalmemory");
                    		}catch(Exception e){
                    			e.printStackTrace();
                    		}
            			}
            			
            		}
            	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("virtualmemory")){
            		//�洢��Ϣ�Ĳɼ�
            		if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("windows")){
            			//Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            			if(node != null){
            				WindowsVirtualMemorySnmp windowsvirtualsnmp = null;
                			try{
                				windowsvirtualsnmp = (WindowsVirtualMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.WindowsVirtualMemorySnmp").newInstance();
                    			returnHash = windowsvirtualsnmp.collect_Data(nodeGatherIndicatorsNode);
                    			windowsvirtualsnmp = null;
                    			if(ShareData.getM30HostAlldata().containsKey(node.getIpAddress())){
	                				Hashtable ipdata = (Hashtable)ShareData.getM30HostAlldata().get(node.getIpAddress());
	                				if(ipdata != null){
	                					ipdata.put("virtualmemory", returnHash);
	                				}else{
	                					ipdata = new Hashtable();
	                					ipdata.put("virtualmemory", returnHash);
	                				}
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}else{
	                				Hashtable ipdata = new Hashtable();
	                				ipdata.put("virtualmemory", returnHash);
	                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
	                			}
                    			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","virtualmemory");
                    		}catch(Exception e){
                    			e.printStackTrace();
                    		}
            			}
            			
            		}
            	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("systemgroup")){
            		//Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            		if(node != null){
            			SystemSnmp systemsnmp = null;
            			try{
            				systemsnmp = (SystemSnmp)Class.forName("com.afunms.polling.snmp.system.SystemSnmp").newInstance();
                			returnHash = systemsnmp.collect_Data(nodeGatherIndicatorsNode);
                			systemsnmp = null;
                			if(ShareData.getM30HostAlldata().containsKey(node.getIpAddress())){
                				Hashtable ipdata = (Hashtable)ShareData.getM30HostAlldata().get(node.getIpAddress());
                				if(ipdata != null){
                					ipdata.put("systemgroup", returnHash);
                				}else{
                					ipdata = new Hashtable();
                					ipdata.put("systemgroup", returnHash);
                				}
                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
                			}else{
                				Hashtable ipdata = new Hashtable();
                				ipdata.put("systemgroup", returnHash);
                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
                			}
                			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"ipmac");
                		}catch(Exception e){
                			e.printStackTrace();
                		}
            		}
            	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("interface")){
            		//Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            		if(node != null){
            			//InterfaceSnmp interfacesnmp = null;
            			try{
            				//SysLogger.info("��ʼ�ɼ�"+node.getIpAddress()+" �ӿ���Ϣ...");
            				interfacesnmp = (InterfaceSnmp)Class.forName("com.afunms.polling.snmp.interfaces.InterfaceSnmp").newInstance();
                			returnHash = interfacesnmp.collect_Data(nodeGatherIndicatorsNode);               			
                			interfacesnmp = null;
                			if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("windows")){
                				//�Գ���������ֵ���и澯���
                    		    try{
                    				//AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                    				List alllist = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(node.getId()), AlarmConstant.TYPE_HOST, "windows");
                    				for(int i = 0 ; i < alllist.size() ; i ++){
                    					AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)alllist.get(i);
                    					if(alarmIndicatorsnode.getName().equalsIgnoreCase("AllInBandwidthUtilHdx") || alarmIndicatorsnode.getName().equalsIgnoreCase("AllOutBandwidthUtilHdx")
                    							|| alarmIndicatorsnode.getName().equalsIgnoreCase("utilhdx")){
                    						//���ܳ��������ֵ���и澯���
                        					//CheckEventUtil checkutil = new CheckEventUtil();
                        					checkutil.updateData(node,returnHash,"host","windows",alarmIndicatorsnode);
                    					}
                    				}
                    				alllist = null;
                    		    }catch(Exception e){
                    		    	e.printStackTrace();
                    		    }
                			}else if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("linux")){
                				//�Գ���������ֵ���и澯���
                    		    try{
                    				List alllist = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(node.getId()), AlarmConstant.TYPE_HOST, "linux");
                    				for(int i = 0 ; i < alllist.size() ; i ++){
                    					AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)alllist.get(i);
                    					if(alarmIndicatorsnode.getName().equalsIgnoreCase("AllInBandwidthUtilHdx") || alarmIndicatorsnode.getName().equalsIgnoreCase("AllOutBandwidthUtilHdx")
                    							|| alarmIndicatorsnode.getName().equalsIgnoreCase("utilhdx")){
                    						//���ܳ��������ֵ���и澯���
                        					checkutil.updateData(node,returnHash,"host","linux",alarmIndicatorsnode);
                    					}
                    				}
                    				alllist = null;
                    		    }catch(Exception e){
                    		    	e.printStackTrace();
                    		    }
                			}
                			//�������ݿ�
                			if(ShareData.getM30HostAlldata().containsKey(node.getIpAddress())){
                				Hashtable ipdata = (Hashtable)ShareData.getM30HostAlldata().get(node.getIpAddress());
                				if(ipdata != null){
                					ipdata.put("interface", returnHash);
                				}else{
                					ipdata = new Hashtable();
                					ipdata.put("interface", returnHash);
                				}
                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
                			}else{
                				Hashtable ipdata = new Hashtable();
                				ipdata.put("interface", returnHash);
                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
                			}
                			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,nodeGatherIndicatorsNode.getType(),nodeGatherIndicatorsNode.getSubtype(),"interface");
                		}catch(Exception e){
                			e.printStackTrace();
                		}
            		}
            	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("packs")){
            		//Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            		if(node != null){
            			PackageSnmp packagesnmp = null;
            			try{
            				packagesnmp = (PackageSnmp)Class.forName("com.afunms.polling.snmp.interfaces.PackageSnmp").newInstance();
                			returnHash = packagesnmp.collect_Data(nodeGatherIndicatorsNode);
                			//�������ݿ�
                			if(ShareData.getM30HostAlldata().containsKey(node.getIpAddress())){
                				Hashtable ipdata = (Hashtable)ShareData.getM30HostAlldata().get(node.getIpAddress());
                				if(ipdata != null){
                					ipdata.put("packs", returnHash);
                				}else{
                					ipdata = new Hashtable();
                					ipdata.put("packs", returnHash);
                				}
                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
                			}else{
                				Hashtable ipdata = new Hashtable();
                				ipdata.put("packs", returnHash);
                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
                			}
                			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,nodeGatherIndicatorsNode.getType(),nodeGatherIndicatorsNode.getSubtype(),"packs");
                		}catch(Exception e){
                			e.printStackTrace();
                		}
            		}
            	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("ping")){
            		//Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            		if(node != null){
            			PingSnmp pingsnmp = null;
            			try{
            				pingsnmp = (PingSnmp)Class.forName("com.afunms.polling.snmp.ping.PingSnmp").newInstance();
                			returnHash = pingsnmp.collect_Data(nodeGatherIndicatorsNode);
                			if(ShareData.getM30HostAlldata().containsKey(node.getIpAddress())){
                				Hashtable ipdata = (Hashtable)ShareData.getM30HostAlldata().get(node.getIpAddress());
                				if(ipdata != null){
                					ipdata.put("ping", returnHash);
                				}else{
                					ipdata = new Hashtable();
                					ipdata.put("ping", returnHash);
                				}
                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
                			}else{
                				Hashtable ipdata = new Hashtable();
                				ipdata.put("ping", returnHash);
                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
                			}
                			//�ڲɼ��������Ѿ��������ݿ�
                			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"utilhdx");
                		}catch(Exception e){
                			e.printStackTrace();
                		}
            		}
            	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("ipmac")){
            		//Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            		if(node != null){
            			ArpSnmp arpsnmp = null;
            			try{
            				arpsnmp = (ArpSnmp)Class.forName("com.afunms.polling.snmp.interfaces.ArpSnmp").newInstance();
                			returnHash = arpsnmp.collect_Data(nodeGatherIndicatorsNode);
                			//IP-MAC�������ݿ�
                			if(ShareData.getM30HostAlldata().containsKey(node.getIpAddress())){
                				Hashtable ipdata = (Hashtable)ShareData.getM30HostAlldata().get(node.getIpAddress());
                				if(ipdata != null){
                					ipdata.put("ipmac", returnHash);
                				}else{
                					ipdata = new Hashtable();
                					ipdata.put("ipmac", returnHash);
                				}
                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
                			}else{
                				Hashtable ipdata = new Hashtable();
                				ipdata.put("ipmac", returnHash);
                				ShareData.getM30HostAlldata().put(node.getIpAddress(), ipdata);
                			}
                			
                			//IP-MAC���������ݿ�
                			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"ipmac");
                		}catch(Exception e){
                			e.printStackTrace();
                		}
            		}
            	}
    		        		
        		
        	}
        	checkutil = null;
        	alarmIndicatorsUtil = null;
        	list = null;
        }catch(Exception exc){
        	exc.printStackTrace();
        }finally{
        	int m30HostCollectedSize = -1;
        	if(!nodeid.equals("")){
        		m30HostCollectedSize = ShareData.addM30HostCollectedSize();
        	}
        	int needCollectNodesSize = ShareData.getM30HostTimerMap().keySet().size();
        	//System.out.println("####nodeid:"+nodeid+"####needCollectNodesSize:"+needCollectNodesSize+"====ShareData.getM5CollectedSize():"+m5CollectedSize);
        	//�ж�����Task�Ƿ��������
			if(needCollectNodesSize == m30HostCollectedSize){//��Ҫ�ɼ����豸���� �� �Ѳɼ����豸������ȣ��򱣴�
	    		ShareData.setM30HostCollectedSize(0);
				Date _enddate = new Date();
	        	HostCollectDataManager hostdataManager=new HostCollectDataManager(); 
	    		Date startdate = new Date();
	    		try{
	    			hostdataManager.createHostItemData(ShareData.getM30HostAlldata(),"host");  
	    		}catch(Exception e){
	    			
	    		}
	    		Date enddate = new Date();
	    		SysLogger.info("##############################");
	    		SysLogger.info("### ���з�������SNMP�����ʱ�� "+(enddate.getTime()-startdate.getTime())+"####");
	    		SysLogger.info("##############################");
	    		ShareData.setM30HostAlldata(new Hashtable());//���
				//��ѯ���ݿ����� �������ݸ��µ��ڴ���
				//updateConnectTypeConfig();
				SysLogger.info("********M30HostTask Thread Count : "+Thread.activeCount());
//				System.gc();//ÿ����ӵ���һ����������
			}
		}
	}
	
	/**
	 * ��ѯ���ݿ����� �������ݸ��µ��ڴ���
	 */
	public void updateConnectTypeConfig(){
		 ConnectTypeConfigDao connectTypeConfigDao = new ConnectTypeConfigDao();
	        Hashtable connectConfigHashtable = new Hashtable();
			List configList = new ArrayList();
			try{
				configList = connectTypeConfigDao.loadAll();
			}catch(Exception e){
				
			}finally{
				connectTypeConfigDao.close();
				connectTypeConfigDao = null;
			}
			if(configList != null && configList.size()>0){
				for(int i=0;i<configList.size();i++){
					ConnectTypeConfig connectTypeConfig = (ConnectTypeConfig)configList.get(i);
					connectConfigHashtable.put(connectTypeConfig.getNode_id(), connectTypeConfig);
				}
			}
			
			ShareData.getConnectConfigHashtable().put("connectConfigHashtable", connectConfigHashtable);

			//װ�ظ澯��ʷ
			Hashtable checkEventHashtable = new Hashtable();
			CheckEventDao checkeventdao = new CheckEventDao();
			List eventlist = new ArrayList();
			try{
				eventlist = checkeventdao.loadAll();
			}catch(Exception e){
				
			}finally{
				checkeventdao.close();
			}
			if(eventlist != null && eventlist.size()>0){
				CheckEvent vo = null;
				for(int i=0;i<eventlist.size();i++){
					vo = (CheckEvent)eventlist.get(i);
					checkEventHashtable.put(vo.getName(), vo.getAlarmlevel());
				}
			}
			ShareData.setCheckEventHash(checkEventHashtable);
			
			List portconfiglist = new ArrayList();
			PortconfigDao configdao = new PortconfigDao(); 			
			Portconfig portconfig = null;
			Hashtable portconfigHash = new Hashtable();
			try {
				portconfiglist = configdao.getAllBySms();
			} catch (RuntimeException e) {
				e.printStackTrace();
			} finally {
				configdao.close();
			}
			if(portconfiglist != null && portconfiglist.size()>0){
				for(int i=0;i<portconfiglist.size();i++){
					portconfig = (Portconfig)portconfiglist.get(i);
					if(portconfigHash.containsKey(portconfig.getIpaddress())){
						List portlist = (List)portconfigHash.get(portconfig.getIpaddress());
						portlist.add(portconfig);
						portconfigHash.put(portconfig.getIpaddress(), portlist);
					}else{
						List portlist = new ArrayList();
						portlist.add(portconfig);
						portconfigHash.put(portconfig.getIpaddress(), portlist);
					}
				}
			} 
			ShareData.setPortConfigHash(portconfigHash);
	}
	
	/**
	 * ��ȡ����5���Ӳɼ��ڵ�Ĳɼ�ָ�꼯�� 
	 * key:nodeid  value:�ɼ�ָ�꼯��
	 * @return
	 */
	public static Hashtable getDocollcetHash(){
		NodeGatherIndicatorsDao indicatorsdao = new NodeGatherIndicatorsDao();
		List<NodeGatherIndicators> monitorItemList = new ArrayList<NodeGatherIndicators>();
		try {
			// ��ȡ�����õ����б�����ָ��
			monitorItemList = indicatorsdao.getByIntervalAndType("30", "m", 1,"host");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			indicatorsdao.close();
		}
		if (monitorItemList == null)
			monitorItemList = new ArrayList<NodeGatherIndicators>();
		HostNodeDao nodedao = new HostNodeDao();
		List nodelist = new ArrayList();
		try {
			nodelist = nodedao.loadMonitorByMonCategory(1,4);
		} catch (Exception e) {

		} finally {
			nodedao.close();
		}
		Hashtable nodehash = new Hashtable();
		if (nodelist != null && nodelist.size() > 0) {
			for (int i = 0; i < nodelist.size(); i++) {
				HostNode node = (HostNode) nodelist.get(i);
				nodehash.put(node.getId() + "", node.getId());
			}
		}
		Hashtable docollcetHash = new Hashtable();
		
		Date _startdate = new Date();
		for (int i = 0; i < monitorItemList.size(); i++) {
			NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators) monitorItemList
					.get(i);
			if (docollcetHash.containsKey(nodeGatherIndicators.getNodeid())) {
				Host node = (Host) PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicators.getNodeid()));
				// ���˵������ӵ��豸
				if (!nodehash.containsKey(nodeGatherIndicators.getNodeid()))
					continue;
				List tempList = (List) docollcetHash.get(nodeGatherIndicators
						.getNodeid());
				tempList.add(nodeGatherIndicators);
				docollcetHash.put(nodeGatherIndicators.getNodeid(), tempList);
			} else {
				Host node = (Host) PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicators.getNodeid()));
				// ���˵������ӵ��豸
				if (!nodehash.containsKey(nodeGatherIndicators.getNodeid()))
					continue;
				List tempList = new ArrayList();
				tempList.add(nodeGatherIndicators);
				docollcetHash.put(nodeGatherIndicators.getNodeid(), tempList);
			}
		}
		return docollcetHash;
	}
}
