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
import com.afunms.common.util.SystemConstant;
import com.afunms.config.dao.PortconfigDao;
import com.afunms.config.model.Portconfig;
import com.afunms.discovery.IpAddress;
import com.afunms.discovery.Node;
import com.afunms.event.dao.CheckEventDao;
import com.afunms.event.model.CheckEvent;
import com.afunms.indicators.dao.NodeGatherIndicatorsDao;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.polling.om.Task;
import com.afunms.polling.snmp.LoadAixFile;
import com.afunms.polling.snmp.LoadHpUnixFile;
import com.afunms.polling.snmp.LoadLinuxFile;
import com.afunms.polling.snmp.LoadSunOSFile;
import com.afunms.polling.snmp.LoadWindowsWMIFile;
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
public class HostCollectDataTaskTest extends MonitorTask {
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
        	
        	Hashtable hashv = new Hashtable();
        	LoadAixFile aix=null;
        	LoadLinuxFile linux=null;
        	LoadHpUnixFile hpunix = null;
        	LoadSunOSFile sununix = null;
        	LoadWindowsWMIFile windowswmi = null;
			
        	I_HostLastCollectData hostlastdataManager=new HostLastCollectDataManager();
        	if(node.getCollecttype() == SystemConstant.COLLECTTYPE_SHELL){
        		//SHELL��ȡ��ʽ        		
        		try{
        			if(node.getOstype() == 6){
        				SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����ΪAIX����������������");
        				//AIX������
        				try{
        					//aix = new LoadAixFile(node.getIpAddress());
        					//hashv=aix.getTelnetMonitorDetail();
        					//aix = null;
        					//ShareData.getM5AgentHostAlldata().put(node.getIpAddress(), hashv);
        					//Vector pv = (Vector)hashv.get("process");
        					//alldata.put(node.getIpAddress(), hashv);
        					//hostdataManager.createHostData(node.getIpAddress(),hashv);
        				}catch(Exception e){
        					e.printStackTrace();
        				}
        			}else if(node.getOstype() == 9){
        				SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����ΪLINUX����������������");
        				//LINUX������
        				try{
//        					linux = new LoadLinuxFile(node.getIpAddress());
//        					hashv=linux.getTelnetMonitorDetail();
//        					linux = null;
//        					ShareData.getM5AgentHostAlldata().put(node.getIpAddress(), hashv);
        					//alldata.put(node.getIpAddress(), hashv);
        					//hostdataManager.createHostData(node.getIpAddress(),hashv);
        				}catch(Exception e){
        					e.printStackTrace();
        				}
        			}else if(node.getOstype() == 7){
        				SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����ΪHPUNIX����������������");
        				//HPUNIX������
        				try{
//        					hpunix = new LoadHpUnixFile(node.getIpAddress());
//        					hashv=hpunix.getTelnetMonitorDetail();
//        					hpunix = null;
//        					if(hashv != null){
//        						ShareData.getM5AgentHostAlldata().put(node.getIpAddress(), hashv);
//        						//alldata.put(node.getIpAddress(), hashv);
//        					}
        					//hostdataManager.createHostData(node.getIpAddress(),hashv);
        				}catch(Exception e){
        					e.printStackTrace();
        				}
        			}else if(node.getOstype() == 8){
        				SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����ΪSOLARIS����������������");
        				//SOLARIS������
//        				try{
//        					sununix = new LoadSunOSFile(node.getIpAddress());
//        					hashv=sununix.getTelnetMonitorDetail();
//        					sununix = null;
//        					ShareData.getM5AgentHostAlldata().put(node.getIpAddress(), hashv);
//        					//alldata.put(node.getIpAddress(), hashv);
//        					//hostdataManager.createHostData(node.getIpAddress(),hashv);
//        				}catch(Exception e){
//        					e.printStackTrace();
//        				}
        			}else if(node.getOstype() == 5){
        				SysLogger.info("�ɼ�: ��ʼ��WMI��ʽ�ɼ�IP��ַΪ"+node.getIpAddress()+"����ΪWINDOWS����������������");
        				try{
        					windowswmi = new LoadWindowsWMIFile(node.getIpAddress());
        					hashv=windowswmi.getTelnetMonitorDetail();
        					windowswmi = null;
        					ShareData.getM5AgentHostAlldata().put(node.getIpAddress(), hashv);
        					//alldata.put(node.getIpAddress(), hashv);
        					//hostdataManager.createHostData(node.getIpAddress(),hashv);
        				}catch(Exception e){
        					e.printStackTrace();
        				}               				
        			}

        		}catch(Exception e){
        			e.printStackTrace();
        		}
				aix=null;
				hashv=null;
        	}else if(node.getCollecttype() == SystemConstant.COLLECTTYPE_WMI){
        		//WINDOWS�µ�WMI�ɼ���ʽ
        		SysLogger.info("�ɼ�: ��ʼ��WMI��ʽ�ɼ�IP��ַΪ"+node.getIpAddress()+"����ΪWINDOWS����������������");
				try{
					windowswmi = new LoadWindowsWMIFile(node.getIpAddress());
					hashv=windowswmi.getTelnetMonitorDetail();
					windowswmi = null;
					ShareData.getM5AgentHostAlldata().put(node.getIpAddress(), hashv);
					//alldata.put(node.getIpAddress(), hashv);
					//hostdataManager.createHostData(node.getIpAddress(),hashv);
				}catch(Exception e){
					e.printStackTrace();
				}
				aix=null;
				hashv=null;
        	}
    	}catch(Exception exc){
        	exc.printStackTrace();
        }finally{
        	int m5AgentHostCollectedSize = -1;
        	if(!nodeid.equals("")){
        		m5AgentHostCollectedSize = ShareData.addM5AgentHostCollectedSize();
        	}
        	int needCollectNodesSize = ShareData.getM5AgentHostTimerMap().keySet().size();
        	//System.out.println("####nodeid:"+nodeid+"####needCollectNodesSize:"+needCollectNodesSize+"====ShareData.getM5CollectedSize():"+m5CollectedSize);
        	//�ж�����Task�Ƿ��������
			if(needCollectNodesSize == m5AgentHostCollectedSize){//��Ҫ�ɼ����豸���� �� �Ѳɼ����豸������ȣ��򱣴�
	    		ShareData.setM5AgentHostCollectedSize(0);
				Date _enddate = new Date();
				
				HostCollectDataManager hostdataManager=new HostCollectDataManager(); 
        		try{
        			Date startdate = new Date();
        			hostdataManager.createMultiHostData(ShareData.getM5AgentHostAlldata(),"host"); 
    	    		Date enddate = new Date();
    	    		SysLogger.info("##############################");
    	    		SysLogger.info("### ���з�������SNMP�����ʱ�� "+(enddate.getTime()-startdate.getTime())+"####");
    	    		SysLogger.info("##############################");
        		}catch(Exception e){
        			
        		}
        		hostdataManager = null;
	    		
	    		ShareData.setM5AgentHostAlldata(new Hashtable());//���
				SysLogger.info("********M5AgentHostTask Thread Count : "+Thread.activeCount());
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
			monitorItemList = indicatorsdao.getByIntervalAndType("5", "m", 1,"host");
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
