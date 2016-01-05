/*
 * Created on 2010-06-24
 *
 */
package com.afunms.polling.task;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.ParserXmlUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SystemConstant;
import com.afunms.indicators.dao.NodeGatherIndicatorsDao;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.Task;
import com.afunms.polling.snmp.cpu.LinuxCpuSnmp;
import com.afunms.polling.snmp.cpu.WindowsCpuSnmp;
import com.afunms.polling.snmp.device.LinuxDeviceSnmp;
import com.afunms.polling.snmp.device.WindowsDeviceSnmp;
import com.afunms.polling.snmp.disk.LinuxDiskSnmp;
import com.afunms.polling.snmp.disk.WindowsDiskSnmp;
import com.afunms.polling.snmp.interfaces.ArpSnmp;
import com.afunms.polling.snmp.interfaces.InterfaceSnmp;
import com.afunms.polling.snmp.interfaces.PackageSnmp;
import com.afunms.polling.snmp.memory.LinuxPhysicalMemorySnmp;
import com.afunms.polling.snmp.memory.WindowsPhysicalMemorySnmp;
import com.afunms.polling.snmp.memory.WindowsVirtualMemorySnmp;
import com.afunms.polling.snmp.ping.PingSnmp;
import com.afunms.polling.snmp.process.LinuxProcessSnmp;
import com.afunms.polling.snmp.process.WindowsProcessSnmp;
import com.afunms.polling.snmp.service.WindowsServiceSnmp;
import com.afunms.polling.snmp.software.LinuxSoftwareSnmp;
import com.afunms.polling.snmp.software.WindowsSoftwareSnmp;
import com.afunms.polling.snmp.storage.LinuxStorageSnmp;
import com.afunms.polling.snmp.storage.WindowsStorageSnmp;
import com.afunms.polling.snmp.system.SystemSnmp;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;


public class M4HostTask extends MonitorTask {
	
	public void run() {
		try{
			
			HostNodeDao nodeDao = new HostNodeDao(); 
			Hashtable otherHash = new Hashtable();
	    	//�õ������ӵ������������豸
	    	List nodeList = new ArrayList();
	    	try{
	    		nodeList = nodeDao.loadHostByFlag(1);   	    	
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}finally{
	    		nodeDao.close();
	    	}
	    	if(nodeList != null)
	    	{
		    	for(int i = nodeList.size() - 1 ; i >= 0 ; i--)
		    	{    		
		    		HostNode node = (HostNode)nodeList.get(i);	
		    		// 3 ����Ϊ �� telnet Э����вɼ�
		    		if(node.getEndpoint() == 3){
		    			otherHash.put(node.getId(), node.getId());
		    		}
//		    		if(node.getEndpoint() != 3)
//		    		{
//		    			nodeList.remove(i);
//		    		}
		    	}
	    	}
			
			NodeGatherIndicatorsDao indicatorsdao = new NodeGatherIndicatorsDao();
	    	List<NodeGatherIndicators> monitorItemList = new ArrayList<NodeGatherIndicators>();
	    	try{
	    		//��ȡ�����õ����б�����ָ��
	    		monitorItemList = indicatorsdao.getByIntervalAndType("4", "m",1,"host");
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
        			nodelist = nodedao.loadHostByFlag(1);
        		}catch(Exception e){
        			e.printStackTrace();
        		}finally{
        			nodedao.close();
        		}
        		Hashtable nodehash = new Hashtable();
        		if(nodelist != null && nodelist.size()>0){
        			for(int i=0;i<nodelist.size();i++){
        				HostNode node = (HostNode)nodelist.get(i);
        				//if(node.getCollecttype() == 1){
        					//SNMP�ɼ���ʽ
        					nodehash.put(node.getId()+"", node.getId());
        			//	}
        					
        			}
        		} 
        		
        		final Hashtable alldata = new Hashtable();
        		
        		Date _startdate = new Date();
        		Hashtable docollcetHash = new Hashtable();
        		for (int i=0; i<monitorItemList.size(); i++) {
        			NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators)monitorItemList.get(i);
        			//SysLogger.info(nodeGatherIndicators.getName()+"===="+nodeGatherIndicators.getNodeid());
        			if(docollcetHash.containsKey(nodeGatherIndicators.getNodeid())){
            			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicators.getNodeid()));
            			//���˵������ӵ��豸
            			if(!nodehash.containsKey(nodeGatherIndicators.getNodeid()))continue;
            			if(!nodeGatherIndicators.getName().equalsIgnoreCase("ping")){
            				if(node.getCollecttype() != 1)continue;
            			}
        				List tempList = (List)docollcetHash.get(nodeGatherIndicators.getNodeid());
        				tempList.add(nodeGatherIndicators);
        				docollcetHash.put(nodeGatherIndicators.getNodeid(), tempList);
        			}else{
        				Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicators.getNodeid()));
            			//���˵������ӵ��豸
        				//SysLogger.info(nodeGatherIndicators.getName()+" $$$ "+nodeGatherIndicators.getNodeid());
            			if(!nodehash.containsKey(nodeGatherIndicators.getNodeid()))continue;
            			if(!nodeGatherIndicators.getName().equalsIgnoreCase("ping")){
            				if(node.getCollecttype() != 1)continue;
            			}
        				List tempList = new ArrayList();
        				tempList.add(nodeGatherIndicators);
        				docollcetHash.put(nodeGatherIndicators.getNodeid(), tempList);
        			}          		
        		}
        		
        		if(docollcetHash != null){
        			numThreads = docollcetHash.size();
        			if(numThreads > 200){
        				numThreads = 200;
        			}
        		}
        		// �����̳߳�,��Ŀ���ݱ����ӵ��豸����
        		ThreadPool threadPool = null;													
        		// ��������
        		if(docollcetHash != null && docollcetHash.size()>0){
        			threadPool = new ThreadPool(docollcetHash.size());	
        			Enumeration newProEnu = docollcetHash.keys();
        			while(newProEnu.hasMoreElements())
        			{
        				String nodeid = (String)newProEnu.nextElement();
        				List dolist = (List)docollcetHash.get(nodeid);
        				threadPool.runTask(createTask(nodeid,dolist,alldata));
        	
        			}
        			// �ر��̳߳ز��ȴ������������
            		threadPool.join();             		
            		threadPool.close();
            		//threadPool.destroy();
            		//�Ƚ��������豸�Ľӿ������ļ��Ľ���
            		Hashtable pingHash=new Hashtable();
            		
//            		Hashtable cpuHash = new Hashtable();
//            		Hashtable memHash = new Hashtable();
//            		Hashtable phymemHash = new Hashtable();
//            		Hashtable virmemHash = new Hashtable();
//            		Hashtable diskHash = new Hashtable();
//            		ParserXmlUtil xmlutil = new ParserXmlUtil();
//            		try{
//            			pingHash=xmlutil.parserNetWorkXml(ResourceCenter.getInstance().getSysPath()+ "linuxserver/host_ping.xml","ping");
//            			
//            			cpuHash = xmlutil.parserNetWorkXml(ResourceCenter.getInstance().getSysPath()+ "linuxserver/host_cpu.xml","cpu");
//            			memHash = xmlutil.parserNetWorkXml(ResourceCenter.getInstance().getSysPath()+ "linuxserver/host_mem.xml","memory");
//            			phymemHash = xmlutil.parserNetWorkXml(ResourceCenter.getInstance().getSysPath()+ "linuxserver/host_mem.xml","PhysicalMemory");
//            			virmemHash = xmlutil.parserNetWorkXml(ResourceCenter.getInstance().getSysPath()+ "linuxserver/host_mem.xml","VirtualMemory");
//            			diskHash = xmlutil.parserNetWorkXml(ResourceCenter.getInstance().getSysPath()+ "linuxserver/host_disk.xml","disk");
//            		}catch(Exception e){
//            			e.printStackTrace();
//            		}
//            		//�����ݴ�ŵ��ڴ�
//            		dataToMem(pingHash, alldata, "ping", "ping");
//            		
//            		dataToMem(cpuHash, alldata, "cpu", "cpu");
//            		dataToMem(memHash, alldata, "memory", "memory");
//            		dataToMem(phymemHash, alldata, "memory", "physicalmemory");
//            		dataToMem(virmemHash, alldata, "memory", "virtualmemory");
//            		dataToMem(diskHash, alldata, "disk", "disk");
            		
            		
            		HostCollectDataManager hostdataManager=new HostCollectDataManager(); 
            		try{
            			hostdataManager.createHostItemData(alldata, "host");
            		}catch(Exception e){
            			
            		}
            		hostdataManager = null;	
        		}
        		threadPool = null;
        		
        		
        		
        		
        		
//        		// �����̳߳�
//        		ThreadPool threadPool = null;
//        		//������Ӧ��Ŀ���߳���
//        		if(monitorItemList != null && monitorItemList.size()>0){
//        			threadPool = new ThreadPool(monitorItemList.size());
//            		// ��������
//            		for (int i=0; i<monitorItemList.size(); i++) {
//            			NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators)monitorItemList.get(i);
//            			//���˵�TELNET���豸�ɼ�ָ��
//            			if(otherHash.containsKey(Integer.parseInt(nodeGatherIndicators.getNodeid()))) continue;
//            			//HostNode node = (HostNode)monitorItemList.get(i);
//            			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicators.getNodeid()));
//            			if(!node.isManaged())continue;
//            			//�����豸����ͨ��SNMP�ɼ��ķ�ʽ,����˵�
//            			if(!nodeGatherIndicators.getName().equalsIgnoreCase("ping")){
//            				if(node.getCollecttype() != 1)continue;
//            			}       			
//            			threadPool.runTask(createTask(nodeGatherIndicators,alldata));           		
//            		}
//            		// �ر��̳߳ز��ȴ������������
//            		threadPool.join();
//            		threadPool.close();
//            		threadPool = null;
//            		
//            		HostCollectDataManager hostdataManager=new HostCollectDataManager(); 
//            		try{
//            			hostdataManager.createHostItemData(alldata, "host");
//            		}catch(Exception e){
//            			
//            		}
//            		hostdataManager = null;
//        		}
		}catch(Exception e){					 	
			e.printStackTrace();
		}finally{
			SysLogger.info("********M4HostTask Thread Count : "+Thread.activeCount());
		}
	}
	/**
	 * @author wxy add
	 * ���ݽӿ�ģʽ����Xml�ļ��н��������ݷ����ڴ�
	 * @date 20111-6-10
	 */
	
	private void dataToMem(Hashtable allsystemdataHash,Hashtable alldata,String ipAllDataName,String ipdataName){	
		if(allsystemdataHash != null && allsystemdataHash.size()>0){
		Enumeration newProEnu = allsystemdataHash.keys();
		while(newProEnu.hasMoreElements())
		{
			String nodeip = (String)newProEnu.nextElement();
			
			Host host = (Host)PollingEngine.getInstance().getNodeByIP(nodeip);
			if (host==null) continue;
			if (host.getCollecttype() == SystemConstant.COLLECTTYPE_DATAINTERFACE) {//�ӿ�ģʽ
				Vector systemVector = (Vector)allsystemdataHash.get(nodeip);
			//	if(systemVector != null && systemVector.size()>0){//�����ﲻ���ж��Ǳ�֤������������� wxy add
					
				if(ipAllDataName.equals("ping")){
						
					    ShareData.getPingdata().put(nodeip, systemVector);
					    
					}else if (!ipdataName.equals("physicalmemory")&&!ipdataName.equals("virtualmemory")){//���������ڴ治���ڴ�
						
					Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(nodeip);
					
					if(ipAllData == null)ipAllData = new Hashtable();
					ipAllData.put(ipAllDataName,systemVector);
					
				    ShareData.getSharedata().put(nodeip, ipAllData);
					}
				if(!ipdataName.equals("memory")){//�������ڴ治���
				    Hashtable dataHash = new Hashtable();
				    dataHash.put(ipAllDataName, systemVector);
					if(alldata.containsKey(nodeip)){
	    				Hashtable ipdata = (Hashtable)alldata.get(nodeip);
	    				if(ipdata != null&&ipdata.size()>0){
	    					ipdata.put(ipdataName, dataHash);
	    				}else{
	    					ipdata = new Hashtable();
	    					ipdata.put(ipdataName, dataHash);
	    				}
	    				alldata.put(nodeip, ipdata);
	    				
	    			}else{
	    				Hashtable ipdata = new Hashtable();
	    				ipdata.put(ipdataName, dataHash);
	    				alldata.put(nodeip, ipdata);
	    				
	    			}
			//	}
				}else {
					
				}
			}
	
		}
	}
	}
	/**
    ��������
	 */	
	private static Runnable createTask(final String nodeid,final List list,final Hashtable alldata) {
    return new Runnable() {
        public void run() {
            try {                	
            	Vector vector=null;
            	Hashtable hashv = new Hashtable();
            	Hashtable returnHash = new Hashtable();
            	HostCollectDataManager hostdataManager=new HostCollectDataManager(); 
            	NodeGatherIndicators nodeGatherIndicatorsNode = null;
            	Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeid));
            	if(host.getCollecttype() == SystemConstant.COLLECTTYPE_DATAINTERFACE){
            		//�ӿ����ݷ�ʽ���вɼ�,��ǰ��Ҫ�ɼ�ϵͳ��Ϣ���ӿ���Ϣ
            		//�����ļ�����
            		Hashtable interfacedataHash = new Hashtable();
            		ParserXmlUtil xmlutil = new ParserXmlUtil();
            		try{
            			SysLogger.info("��ʼ����"+ResourceCenter.getInstance().getSysPath()+ "linuxserver/host_"+host.getIpAddress()+".xml");
            			interfacedataHash = xmlutil.parserInterfaceXml(ResourceCenter.getInstance().getSysPath()+ "linuxserver/host_"+host.getIpAddress()+".xml",host.getIpAddress());
            			
            		}catch(Exception e){
            			
            		}
            		if(interfacedataHash != null && interfacedataHash.size()>0){
            			if(alldata.containsKey(host.getIpAddress())){
            				Hashtable ipdata = (Hashtable)alldata.get(host.getIpAddress());
            				if(ipdata != null){
            					ipdata.put("interface", interfacedataHash);
            				}else{
            					ipdata = new Hashtable();
            					ipdata.put("interface", interfacedataHash);
            				}
            				alldata.put(host.getIpAddress(), ipdata);
            				
            			}else{
            				Hashtable ipdata = new Hashtable();
            				ipdata.put("interface", interfacedataHash);
            				alldata.put(host.getIpAddress(), ipdata);
            				
            			}
            			Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
        				if(ipAllData == null)ipAllData = new Hashtable();
        				
        			    ShareData.getSharedata().put(host.getIpAddress(), interfacedataHash);
            		}
            		return;
            	}   
            	if(list != null && list.size()>0){
            		for(int k=0;k<list.size();k++){
            			nodeGatherIndicatorsNode = (NodeGatherIndicators)list.get(k);
            			if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("cpu")){
                    		//CPU�Ĳɼ�
                    		if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("windows")){
                    			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
                    			if(node != null){
                    				if(node.isManaged()){
                    					WindowsCpuSnmp windowscpusnmp = null;
                            			try{
                                			windowscpusnmp = (WindowsCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.WindowsCpuSnmp").newInstance();
                                			returnHash = windowscpusnmp.collect_Data(nodeGatherIndicatorsNode);
                                			windowscpusnmp = null;
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
                                			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","cpu");
                                		}catch(Exception e){
                                			e.printStackTrace();
                                		}
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
                            			linuxcpusnmp = null;
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
                            			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","linux","cpu");
                            		}catch(Exception e){
                            			e.printStackTrace();
                            		}
                    			}
                    		}
                    	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("disk")){
                    		//DISK�Ĳɼ�
                    		if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("windows")){
                    			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
                    			if(node != null){
                    				WindowsDiskSnmp windowdisksnmp = null;
                        			try{
                        				windowdisksnmp = (WindowsDiskSnmp)Class.forName("com.afunms.polling.snmp.disk.WindowsDiskSnmp").newInstance();
                            			returnHash = windowdisksnmp.collect_Data(nodeGatherIndicatorsNode);
                            			windowdisksnmp = null;
                            			if(alldata.containsKey(node.getIpAddress())){
                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                            				if(ipdata != null){
                            					ipdata.put("disk", returnHash);
                            				}else{
                            					ipdata = new Hashtable();
                            					ipdata.put("disk", returnHash);
                            				}
                            				alldata.put(node.getIpAddress(), ipdata);
                            			}else{
                            				Hashtable ipdata = new Hashtable();
                            				ipdata.put("disk", returnHash);
                            				alldata.put(node.getIpAddress(), ipdata);
                            			}
                            			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","disk");
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
                            			linuxdisksnmp = null;
                            			if(alldata.containsKey(node.getIpAddress())){
                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                            				if(ipdata != null){
                            					ipdata.put("disk", returnHash);
                            				}else{
                            					ipdata = new Hashtable();
                            					ipdata.put("disk", returnHash);
                            				}
                            				alldata.put(node.getIpAddress(), ipdata);
                            			}else{
                            				Hashtable ipdata = new Hashtable();
                            				ipdata.put("disk", returnHash);
                            				alldata.put(node.getIpAddress(), ipdata);
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
                    			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
                    			if(node != null){
                    				WindowsProcessSnmp windowsprocesssnmp = null;
                        			try{
                        				windowsprocesssnmp = (WindowsProcessSnmp)Class.forName("com.afunms.polling.snmp.process.WindowsProcessSnmp").newInstance();
                            			returnHash = windowsprocesssnmp.collect_Data(nodeGatherIndicatorsNode);
                            			windowsprocesssnmp = null;
                            			if(alldata.containsKey(node.getIpAddress())){
                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                            				if(ipdata != null){
                            					ipdata.put("process", returnHash);
                            				}else{
                            					ipdata = new Hashtable();
                            					ipdata.put("process", returnHash);
                            				}
                            				alldata.put(node.getIpAddress(), ipdata);
                            			}else{
                            				Hashtable ipdata = new Hashtable();
                            				ipdata.put("process", returnHash);
                            				alldata.put(node.getIpAddress(), ipdata);
                            			}
                            			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","process");
                            		}catch(Exception e){
                            			e.printStackTrace();
                            		}
                    			}         			
                    		}else if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("linux")){
                    			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
                    			if(node != null){
                    				LinuxProcessSnmp linuxprocesssnmp = null;
                        			try{
                        				SysLogger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                        				SysLogger.info("&&&&&& ��ʼ�ɼ�LINUX "+nodeGatherIndicatorsNode.getNodeid()+"  "+nodeGatherIndicatorsNode.getName() +"   &&&&&&&&&&&");
                        				SysLogger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                        				linuxprocesssnmp = (LinuxProcessSnmp)Class.forName("com.afunms.polling.snmp.process.LinuxProcessSnmp").newInstance();
                            			returnHash = linuxprocesssnmp.collect_Data(nodeGatherIndicatorsNode);
                            			linuxprocesssnmp = null;
                            			if(alldata.containsKey(node.getIpAddress())){
                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                            				if(ipdata != null){
                            					ipdata.put("process", returnHash);
                            				}else{
                            					ipdata = new Hashtable();
                            					ipdata.put("process", returnHash);
                            				}
                            				alldata.put(node.getIpAddress(), ipdata);
                            			}else{
                            				Hashtable ipdata = new Hashtable();
                            				ipdata.put("process", returnHash);
                            				alldata.put(node.getIpAddress(), ipdata);
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
                    			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
                    			if(node != null){
                    				WindowsServiceSnmp windowservicesnmp = null;
                        			try{
                        				windowservicesnmp = (WindowsServiceSnmp)Class.forName("com.afunms.polling.snmp.service.WindowsServiceSnmp").newInstance();
                            			returnHash = windowservicesnmp.collect_Data(nodeGatherIndicatorsNode);
                            			windowservicesnmp = null;
                            			if(alldata.containsKey(node.getIpAddress())){
                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                            				if(ipdata != null) {
                            					ipdata.put("service", returnHash);
                            				} else {
                            					ipdata = new Hashtable();
                            					ipdata.put("service", returnHash);
                            				}
                            				alldata.put(node.getIpAddress(), ipdata);
                            			} else {
                            				Hashtable ipdata = new Hashtable();
                            				ipdata.put("service", returnHash);
                            				alldata.put(node.getIpAddress(), ipdata);
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
                    			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
                    			if(node != null){
                    				WindowsSoftwareSnmp windowssoftwaresnmp = null;
                        			try{
                        				windowssoftwaresnmp = (WindowsSoftwareSnmp)Class.forName("com.afunms.polling.snmp.software.WindowsSoftwareSnmp").newInstance();
                            			returnHash = windowssoftwaresnmp.collect_Data(nodeGatherIndicatorsNode);
                            			windowssoftwaresnmp = null;
                            			if(alldata.containsKey(node.getIpAddress())){
                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                            				if(ipdata != null) {
                            					ipdata.put("software", returnHash);
                            				} else {
                            					ipdata = new Hashtable();
                            					ipdata.put("software", returnHash);
                            				}
                            				alldata.put(node.getIpAddress(), ipdata);
                            			} else {
                            				Hashtable ipdata = new Hashtable();
                            				ipdata.put("software", returnHash);
                            				alldata.put(node.getIpAddress(), ipdata);
                            			}
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
                            			linuxsoftwaresnmp = null;
                            			if(alldata.containsKey(node.getIpAddress())){
                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                            				if(ipdata != null) {
                            					ipdata.put("software", returnHash);
                            				} else {
                            					ipdata = new Hashtable();
                            					ipdata.put("software", returnHash);
                            				}
                            				alldata.put(node.getIpAddress(), ipdata);
                            			} else {
                            				Hashtable ipdata = new Hashtable();
                            				ipdata.put("software", returnHash);
                            				alldata.put(node.getIpAddress(), ipdata);
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
                    			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
                    			if(node != null){
                    				WindowsDeviceSnmp windowsdevicesnmp = null;
                        			try{
                        				windowsdevicesnmp = (WindowsDeviceSnmp)Class.forName("com.afunms.polling.snmp.device.WindowsDeviceSnmp").newInstance();
                            			returnHash = windowsdevicesnmp.collect_Data(nodeGatherIndicatorsNode);
                            			windowsdevicesnmp = null;
                            			if(alldata.containsKey(node.getIpAddress())){
                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                            				if(ipdata != null) {
                            					ipdata.put("hardware", returnHash);
                            				} else {
                            					ipdata = new Hashtable();
                            					ipdata.put("hardware", returnHash);
                            				}
                            				alldata.put(node.getIpAddress(), ipdata);
                            			} else {
                            				Hashtable ipdata = new Hashtable();
                            				ipdata.put("hardware", returnHash);
                            				alldata.put(node.getIpAddress(), ipdata);
                            			}
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
                            			linuxdevicesnmp = null;
                            			if(alldata.containsKey(node.getIpAddress())){
                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                            				if(ipdata != null) {
                            					ipdata.put("hardware", returnHash);
                            				} else {
                            					ipdata = new Hashtable();
                            					ipdata.put("hardware", returnHash);
                            				}
                            				alldata.put(node.getIpAddress(), ipdata);
                            			} else {
                            				Hashtable ipdata = new Hashtable();
                            				ipdata.put("hardware", returnHash);
                            				alldata.put(node.getIpAddress(), ipdata);
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
                    			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
                    			if(node != null){
                    				WindowsStorageSnmp windowsstoragesnmp = null;
                        			try{
                        				windowsstoragesnmp = (WindowsStorageSnmp)Class.forName("com.afunms.polling.snmp.storage.WindowsStorageSnmp").newInstance();
                            			returnHash = windowsstoragesnmp.collect_Data(nodeGatherIndicatorsNode);
                            			windowsstoragesnmp = null;
                            			if(alldata.containsKey(node.getIpAddress())){
                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                            				if(ipdata != null) {
                            					ipdata.put("storage", returnHash);
                            				} else {
                            					ipdata = new Hashtable();
                            					ipdata.put("storage", returnHash);
                            				}
                            				alldata.put(node.getIpAddress(), ipdata);
                            			} else {
                            				Hashtable ipdata = new Hashtable();
                            				ipdata.put("storage", returnHash);
                            				alldata.put(node.getIpAddress(), ipdata);
                            			}
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
                            			linuxstoragesnmp = null;
                            			if(alldata.containsKey(node.getIpAddress())){
                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                            				if(ipdata != null) {
                            					ipdata.put("storage", returnHash);
                            				} else {
                            					ipdata = new Hashtable();
                            					ipdata.put("storage", returnHash);
                            				}
                            				alldata.put(node.getIpAddress(), ipdata);
                            			} else {
                            				Hashtable ipdata = new Hashtable();
                            				ipdata.put("storage", returnHash);
                            				alldata.put(node.getIpAddress(), ipdata);
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
                    			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
                    			if(node != null){
                    				WindowsPhysicalMemorySnmp windowsphysicalsnmp = null;
                        			try{
                        				windowsphysicalsnmp = (WindowsPhysicalMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.WindowsPhysicalMemorySnmp").newInstance();
                            			returnHash = windowsphysicalsnmp.collect_Data(nodeGatherIndicatorsNode);
                            			windowsphysicalsnmp = null;
                            			if(alldata.containsKey(node.getIpAddress())){
                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                            				if(ipdata != null){
                            					ipdata.put("physicalmemory", returnHash);
                            				}else{
                            					ipdata = new Hashtable();
                            					ipdata.put("physicalmemory", returnHash);
                            				}
                            				alldata.put(node.getIpAddress(), ipdata);
                            			}else{
                            				Hashtable ipdata = new Hashtable();
                            				ipdata.put("physicalmemory", returnHash);
                            				alldata.put(node.getIpAddress(), ipdata);
                            			}
                            			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","physicalmemory");
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
                            			linuxphysicalsnmp = null;
                            			if(alldata.containsKey(node.getIpAddress())){
                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                            				if(ipdata != null){
                            					ipdata.put("physicalmemory", returnHash);
                            				}else{
                            					ipdata = new Hashtable();
                            					ipdata.put("physicalmemory", returnHash);
                            				}
                            				alldata.put(node.getIpAddress(), ipdata);
                            			}else{
                            				Hashtable ipdata = new Hashtable();
                            				ipdata.put("physicalmemory", returnHash);
                            				alldata.put(node.getIpAddress(), ipdata);
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
                    			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
                    			if(node != null){
                    				WindowsVirtualMemorySnmp windowsvirtualsnmp = null;
                        			try{
                        				windowsvirtualsnmp = (WindowsVirtualMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.WindowsVirtualMemorySnmp").newInstance();
                            			returnHash = windowsvirtualsnmp.collect_Data(nodeGatherIndicatorsNode);
                            			windowsvirtualsnmp = null;
                            			if(alldata.containsKey(node.getIpAddress())){
                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                            				if(ipdata != null){
                            					ipdata.put("virtualmemory", returnHash);
                            				}else{
                            					ipdata = new Hashtable();
                            					ipdata.put("virtualmemory", returnHash);
                            				}
                            				alldata.put(node.getIpAddress(), ipdata);
                            			}else{
                            				Hashtable ipdata = new Hashtable();
                            				ipdata.put("virtualmemory", returnHash);
                            				alldata.put(node.getIpAddress(), ipdata);
                            			}
                            			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","virtualmemory");
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
                        			systemsnmp = null;
                        			if(alldata.containsKey(node.getIpAddress())){
                        				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                        				if(ipdata != null) {
                        					ipdata.put("systemgroup", returnHash);
                        				} else {
                        					ipdata = new Hashtable();
                        					ipdata.put("systemgroup", returnHash);
                        				}
                        				alldata.put(node.getIpAddress(), ipdata);
                        			} else {
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
                    	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("interface")){
                    		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
                    		if(node != null){
                    			InterfaceSnmp interfacesnmp = null;
                    			try{
                    				//SysLogger.info("��ʼ�ɼ�"+node.getIpAddress()+" �ӿ���Ϣ...");
                    				interfacesnmp = (InterfaceSnmp)Class.forName("com.afunms.polling.snmp.interfaces.InterfaceSnmp").newInstance();
                        			returnHash = interfacesnmp.collect_Data(nodeGatherIndicatorsNode);               			
                        			interfacesnmp = null;
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
                        			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,nodeGatherIndicatorsNode.getType(),nodeGatherIndicatorsNode.getSubtype(),"interface");
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
                        			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,nodeGatherIndicatorsNode.getType(),nodeGatherIndicatorsNode.getSubtype(),"packs");
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
                    	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("ipmac")){
                    		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
                    		if(node != null){
                    			ArpSnmp arpsnmp = null;
                    			try{
                    				arpsnmp = (ArpSnmp)Class.forName("com.afunms.polling.snmp.interfaces.ArpSnmp").newInstance();
                        			returnHash = arpsnmp.collect_Data(nodeGatherIndicatorsNode);
//                        			IP-MAC�������ݿ�
                        			if(alldata.containsKey(node.getIpAddress())){
                        				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                        				if(ipdata != null) {
                        					ipdata.put("ipmac", returnHash);
                        				} else {
                        					ipdata = new Hashtable();
                        					ipdata.put("ipmac", returnHash);
                        				}
                        				alldata.put(node.getIpAddress(), ipdata);
                        			} else {
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
                    	}
            		}
            	}
            	//SysLogger.info(nodeGatherIndicatorsNode.getNodeid()+"===="+nodeGatherIndicatorsNode.getName()+"==="+nodeGatherIndicatorsNode.getType()+"==="+nodeGatherIndicatorsNode.getSubtype());
            	
        		
            }catch(Exception exc){
            	
            }
        }
    };
	}
	
	/**
    ��������
	 */	
	private static Runnable createTask(final NodeGatherIndicators nodeGatherIndicatorsNode,final Hashtable alldata) {
    return new Runnable() {
        public void run() {
            try {                	
            	Vector vector=null;
            	Hashtable hashv = new Hashtable();
            	Hashtable returnHash = new Hashtable();
            	HostCollectDataManager hostdataManager=new HostCollectDataManager(); 
            	
            	//SysLogger.info(nodeGatherIndicatorsNode.getNodeid()+"===="+nodeGatherIndicatorsNode.getName()+"==="+nodeGatherIndicatorsNode.getType()+"==="+nodeGatherIndicatorsNode.getSubtype());
            	if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("cpu")){
            		//CPU�Ĳɼ�
            		if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("windows")){
            			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            			if(node != null){
            				if(node.isManaged()){
            					WindowsCpuSnmp windowscpusnmp = null;
                    			try{
                        			windowscpusnmp = (WindowsCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.WindowsCpuSnmp").newInstance();
                        			returnHash = windowscpusnmp.collect_Data(nodeGatherIndicatorsNode);
                        			windowscpusnmp = null;
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
                        			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","cpu");
                        		}catch(Exception e){
                        			e.printStackTrace();
                        		}
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
                    			linuxcpusnmp = null;
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
                    			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","linux","cpu");
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
                    			windowdisksnmp = null;
                    			if(alldata.containsKey(node.getIpAddress())){
                    				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                    				if(ipdata != null){
                    					ipdata.put("disk", returnHash);
                    				}else{
                    					ipdata = new Hashtable();
                    					ipdata.put("disk", returnHash);
                    				}
                    				alldata.put(node.getIpAddress(), ipdata);
                    			}else{
                    				Hashtable ipdata = new Hashtable();
                    				ipdata.put("disk", returnHash);
                    				alldata.put(node.getIpAddress(), ipdata);
                    			}
                    			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","disk");
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
                    			linuxdisksnmp = null;
                    			if(alldata.containsKey(node.getIpAddress())){
                    				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                    				if(ipdata != null){
                    					ipdata.put("disk", returnHash);
                    				}else{
                    					ipdata = new Hashtable();
                    					ipdata.put("disk", returnHash);
                    				}
                    				alldata.put(node.getIpAddress(), ipdata);
                    			}else{
                    				Hashtable ipdata = new Hashtable();
                    				ipdata.put("disk", returnHash);
                    				alldata.put(node.getIpAddress(), ipdata);
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
            			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            			if(node != null){
            				WindowsProcessSnmp windowsprocesssnmp = null;
                			try{
                				windowsprocesssnmp = (WindowsProcessSnmp)Class.forName("com.afunms.polling.snmp.process.WindowsProcessSnmp").newInstance();
                    			returnHash = windowsprocesssnmp.collect_Data(nodeGatherIndicatorsNode);
                    			windowsprocesssnmp = null;
                    			if(alldata.containsKey(node.getIpAddress())){
                    				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                    				if(ipdata != null){
                    					ipdata.put("process", returnHash);
                    				}else{
                    					ipdata = new Hashtable();
                    					ipdata.put("process", returnHash);
                    				}
                    				alldata.put(node.getIpAddress(), ipdata);
                    			}else{
                    				Hashtable ipdata = new Hashtable();
                    				ipdata.put("process", returnHash);
                    				alldata.put(node.getIpAddress(), ipdata);
                    			}
                    			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","process");
                    		}catch(Exception e){
                    			e.printStackTrace();
                    		}
            			}         			
            		}else if(nodeGatherIndicatorsNode.getSubtype().equalsIgnoreCase("linux")){
            			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            			if(node != null){
            				LinuxProcessSnmp linuxprocesssnmp = null;
                			try{
                				SysLogger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                				SysLogger.info("&&&&&& ��ʼ�ɼ�LINUX "+nodeGatherIndicatorsNode.getNodeid()+"  "+nodeGatherIndicatorsNode.getName() +"   &&&&&&&&&&&");
                				SysLogger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                				linuxprocesssnmp = (LinuxProcessSnmp)Class.forName("com.afunms.polling.snmp.process.LinuxProcessSnmp").newInstance();
                    			returnHash = linuxprocesssnmp.collect_Data(nodeGatherIndicatorsNode);
                    			linuxprocesssnmp = null;
                    			if(alldata.containsKey(node.getIpAddress())){
                    				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                    				if(ipdata != null){
                    					ipdata.put("process", returnHash);
                    				}else{
                    					ipdata = new Hashtable();
                    					ipdata.put("process", returnHash);
                    				}
                    				alldata.put(node.getIpAddress(), ipdata);
                    			}else{
                    				Hashtable ipdata = new Hashtable();
                    				ipdata.put("process", returnHash);
                    				alldata.put(node.getIpAddress(), ipdata);
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
            			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            			if(node != null){
            				WindowsServiceSnmp windowservicesnmp = null;
                			try{
                				windowservicesnmp = (WindowsServiceSnmp)Class.forName("com.afunms.polling.snmp.service.WindowsServiceSnmp").newInstance();
                    			returnHash = windowservicesnmp.collect_Data(nodeGatherIndicatorsNode);
                    			windowservicesnmp = null;
                    			if(alldata.containsKey(node.getIpAddress())){
                    				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                    				if(ipdata != null) {
                    					ipdata.put("service", returnHash);
                    				} else {
                    					ipdata = new Hashtable();
                    					ipdata.put("service", returnHash);
                    				}
                    				alldata.put(node.getIpAddress(), ipdata);
                    			} else {
                    				Hashtable ipdata = new Hashtable();
                    				ipdata.put("service", returnHash);
                    				alldata.put(node.getIpAddress(), ipdata);
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
            			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            			if(node != null){
            				WindowsSoftwareSnmp windowssoftwaresnmp = null;
                			try{
                				windowssoftwaresnmp = (WindowsSoftwareSnmp)Class.forName("com.afunms.polling.snmp.software.WindowsSoftwareSnmp").newInstance();
                    			returnHash = windowssoftwaresnmp.collect_Data(nodeGatherIndicatorsNode);
                    			windowssoftwaresnmp = null;
                    			if(alldata.containsKey(node.getIpAddress())){
                    				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                    				if(ipdata != null) {
                    					ipdata.put("software", returnHash);
                    				} else {
                    					ipdata = new Hashtable();
                    					ipdata.put("software", returnHash);
                    				}
                    				alldata.put(node.getIpAddress(), ipdata);
                    			} else {
                    				Hashtable ipdata = new Hashtable();
                    				ipdata.put("software", returnHash);
                    				alldata.put(node.getIpAddress(), ipdata);
                    			}
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
                    			linuxsoftwaresnmp = null;
                    			if(alldata.containsKey(node.getIpAddress())){
                    				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                    				if(ipdata != null) {
                    					ipdata.put("software", returnHash);
                    				} else {
                    					ipdata = new Hashtable();
                    					ipdata.put("software", returnHash);
                    				}
                    				alldata.put(node.getIpAddress(), ipdata);
                    			} else {
                    				Hashtable ipdata = new Hashtable();
                    				ipdata.put("software", returnHash);
                    				alldata.put(node.getIpAddress(), ipdata);
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
            			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            			if(node != null){
            				WindowsDeviceSnmp windowsdevicesnmp = null;
                			try{
                				windowsdevicesnmp = (WindowsDeviceSnmp)Class.forName("com.afunms.polling.snmp.device.WindowsDeviceSnmp").newInstance();
                    			returnHash = windowsdevicesnmp.collect_Data(nodeGatherIndicatorsNode);
                    			windowsdevicesnmp = null;
                    			if(alldata.containsKey(node.getIpAddress())){
                    				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                    				if(ipdata != null) {
                    					ipdata.put("hardware", returnHash);
                    				} else {
                    					ipdata = new Hashtable();
                    					ipdata.put("hardware", returnHash);
                    				}
                    				alldata.put(node.getIpAddress(), ipdata);
                    			} else {
                    				Hashtable ipdata = new Hashtable();
                    				ipdata.put("hardware", returnHash);
                    				alldata.put(node.getIpAddress(), ipdata);
                    			}
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
                    			linuxdevicesnmp = null;
                    			if(alldata.containsKey(node.getIpAddress())){
                    				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                    				if(ipdata != null) {
                    					ipdata.put("hardware", returnHash);
                    				} else {
                    					ipdata = new Hashtable();
                    					ipdata.put("hardware", returnHash);
                    				}
                    				alldata.put(node.getIpAddress(), ipdata);
                    			} else {
                    				Hashtable ipdata = new Hashtable();
                    				ipdata.put("hardware", returnHash);
                    				alldata.put(node.getIpAddress(), ipdata);
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
            			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            			if(node != null){
            				WindowsStorageSnmp windowsstoragesnmp = null;
                			try{
                				windowsstoragesnmp = (WindowsStorageSnmp)Class.forName("com.afunms.polling.snmp.storage.WindowsStorageSnmp").newInstance();
                    			returnHash = windowsstoragesnmp.collect_Data(nodeGatherIndicatorsNode);
                    			windowsstoragesnmp = null;
                    			if(alldata.containsKey(node.getIpAddress())){
                    				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                    				if(ipdata != null) {
                    					ipdata.put("storage", returnHash);
                    				} else {
                    					ipdata = new Hashtable();
                    					ipdata.put("storage", returnHash);
                    				}
                    				alldata.put(node.getIpAddress(), ipdata);
                    			} else {
                    				Hashtable ipdata = new Hashtable();
                    				ipdata.put("storage", returnHash);
                    				alldata.put(node.getIpAddress(), ipdata);
                    			}
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
                    			linuxstoragesnmp = null;
                    			if(alldata.containsKey(node.getIpAddress())){
                    				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                    				if(ipdata != null) {
                    					ipdata.put("storage", returnHash);
                    				} else {
                    					ipdata = new Hashtable();
                    					ipdata.put("storage", returnHash);
                    				}
                    				alldata.put(node.getIpAddress(), ipdata);
                    			} else {
                    				Hashtable ipdata = new Hashtable();
                    				ipdata.put("storage", returnHash);
                    				alldata.put(node.getIpAddress(), ipdata);
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
            			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            			if(node != null){
            				WindowsPhysicalMemorySnmp windowsphysicalsnmp = null;
                			try{
                				windowsphysicalsnmp = (WindowsPhysicalMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.WindowsPhysicalMemorySnmp").newInstance();
                    			returnHash = windowsphysicalsnmp.collect_Data(nodeGatherIndicatorsNode);
                    			windowsphysicalsnmp = null;
                    			if(alldata.containsKey(node.getIpAddress())){
                    				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                    				if(ipdata != null){
                    					ipdata.put("physicalmemory", returnHash);
                    				}else{
                    					ipdata = new Hashtable();
                    					ipdata.put("physicalmemory", returnHash);
                    				}
                    				alldata.put(node.getIpAddress(), ipdata);
                    			}else{
                    				Hashtable ipdata = new Hashtable();
                    				ipdata.put("physicalmemory", returnHash);
                    				alldata.put(node.getIpAddress(), ipdata);
                    			}
                    			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","physicalmemory");
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
                    			linuxphysicalsnmp = null;
                    			if(alldata.containsKey(node.getIpAddress())){
                    				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                    				if(ipdata != null){
                    					ipdata.put("physicalmemory", returnHash);
                    				}else{
                    					ipdata = new Hashtable();
                    					ipdata.put("physicalmemory", returnHash);
                    				}
                    				alldata.put(node.getIpAddress(), ipdata);
                    			}else{
                    				Hashtable ipdata = new Hashtable();
                    				ipdata.put("physicalmemory", returnHash);
                    				alldata.put(node.getIpAddress(), ipdata);
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
            			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            			if(node != null){
            				WindowsVirtualMemorySnmp windowsvirtualsnmp = null;
                			try{
                				windowsvirtualsnmp = (WindowsVirtualMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.WindowsVirtualMemorySnmp").newInstance();
                    			returnHash = windowsvirtualsnmp.collect_Data(nodeGatherIndicatorsNode);
                    			windowsvirtualsnmp = null;
                    			if(alldata.containsKey(node.getIpAddress())){
                    				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                    				if(ipdata != null){
                    					ipdata.put("virtualmemory", returnHash);
                    				}else{
                    					ipdata = new Hashtable();
                    					ipdata.put("virtualmemory", returnHash);
                    				}
                    				alldata.put(node.getIpAddress(), ipdata);
                    			}else{
                    				Hashtable ipdata = new Hashtable();
                    				ipdata.put("virtualmemory", returnHash);
                    				alldata.put(node.getIpAddress(), ipdata);
                    			}
                    			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"host","windows","virtualmemory");
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
                			systemsnmp = null;
                			if(alldata.containsKey(node.getIpAddress())){
                				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                				if(ipdata != null) {
                					ipdata.put("systemgroup", returnHash);
                				} else {
                					ipdata = new Hashtable();
                					ipdata.put("systemgroup", returnHash);
                				}
                				alldata.put(node.getIpAddress(), ipdata);
                			} else {
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
            	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("interface")){
            		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            		if(node != null){
            			InterfaceSnmp interfacesnmp = null;
            			try{
            				//SysLogger.info("��ʼ�ɼ�"+node.getIpAddress()+" �ӿ���Ϣ...");
            				interfacesnmp = (InterfaceSnmp)Class.forName("com.afunms.polling.snmp.interfaces.InterfaceSnmp").newInstance();
                			returnHash = interfacesnmp.collect_Data(nodeGatherIndicatorsNode);               			
                			interfacesnmp = null;
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
                			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,nodeGatherIndicatorsNode.getType(),nodeGatherIndicatorsNode.getSubtype(),"interface");
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
                			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,nodeGatherIndicatorsNode.getType(),nodeGatherIndicatorsNode.getSubtype(),"packs");
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
            	}else if(nodeGatherIndicatorsNode.getName().equalsIgnoreCase("ipmac")){
            		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
            		if(node != null){
            			ArpSnmp arpsnmp = null;
            			try{
            				arpsnmp = (ArpSnmp)Class.forName("com.afunms.polling.snmp.interfaces.ArpSnmp").newInstance();
                			returnHash = arpsnmp.collect_Data(nodeGatherIndicatorsNode);
//                			IP-MAC�������ݿ�
                			if(alldata.containsKey(node.getIpAddress())){
                				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                				if(ipdata != null) {
                					ipdata.put("ipmac", returnHash);
                				} else {
                					ipdata = new Hashtable();
                					ipdata.put("ipmac", returnHash);
                				}
                				alldata.put(node.getIpAddress(), ipdata);
                			} else {
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
            	}
        		
            }catch(Exception exc){
            	
            }
        }
    };
	}
}
