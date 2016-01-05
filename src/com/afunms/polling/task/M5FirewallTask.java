/*
 * Created on 2010-06-24
 *
 */
package com.afunms.polling.task;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.dao.NodeGatherIndicatorsDao;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Task;
import com.afunms.polling.snmp.cpu.BDComCpuSnmp;
import com.afunms.polling.snmp.cpu.CiscoCpuSnmp;
import com.afunms.polling.snmp.cpu.DLinkCpuSnmp;
import com.afunms.polling.snmp.cpu.EnterasysCpuSnmp;
import com.afunms.polling.snmp.cpu.H3CCpuSnmp;
import com.afunms.polling.snmp.cpu.MaipuCpuSnmp;
import com.afunms.polling.snmp.cpu.NortelCpuSnmp;
import com.afunms.polling.snmp.cpu.RadwareCpuSnmp;
import com.afunms.polling.snmp.cpu.RedGiantCpuSnmp;
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
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;





/**
 * @author nielin
 * @date 2010-06-24 
 * 
 * �������豸���вɼ�
 *
 */
public class M5FirewallTask extends MonitorTask {
	
	public void run() {
		try{
			NodeGatherIndicatorsDao indicatorsdao = new NodeGatherIndicatorsDao();
	    	List<NodeGatherIndicators> monitorItemList = new ArrayList<NodeGatherIndicators>();
	    	try{
	    		//��ȡ�����õ����б����ӷ���ǽָ��
	    		monitorItemList = indicatorsdao.getByIntervalAndType("5", "m",1,"firewall");
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
        		
        		// �����̳߳�
        		ThreadPool threadPool = null;
        		if(monitorItemList != null && monitorItemList.size()>0){
        			threadPool = new ThreadPool(monitorItemList.size());
        			// ��������
            		for (int i=0; i<monitorItemList.size(); i++) {
            			NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators)monitorItemList.get(i);
            			//Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicators.getNodeid()));
            			//���˵������ӵ��豸
            			if(!nodehash.containsKey(nodeGatherIndicators.getNodeid()))continue;
            			threadPool.runTask(createTask(nodeGatherIndicators));           		
            		}
            		// �ر��̳߳ز��ȴ������������
            		threadPool.join();
            		threadPool.close();
            		threadPool = null;
        		}
        		 											
		}catch(Exception e){					 	
			e.printStackTrace();
		}finally{
			SysLogger.info("********M5FirewallTask Thread Count : "+Thread.activeCount());
		}
	}
	
	/**
    ��������
	 */	
	private static Runnable createTask(final NodeGatherIndicators alarmIndicatorsNode) {
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
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","cisco","cpu");
                    		}catch(Exception e){
                    			e.printStackTrace();
                    		}
            			}
            		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("h3c")){
            			//H3C��CPU
            			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
            			if(node != null){
            				H3CCpuSnmp h3ccpusnmp = null;
                			try{
                				h3ccpusnmp = (H3CCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.H3CCpuSnmp").newInstance();
                    			returnHash = h3ccpusnmp.collect_Data(alarmIndicatorsNode);
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","h3c","cpu");
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
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","enterasys","cpu");
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
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","radware","cpu");
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
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","maipu","cpu");
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
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","redgiant","cpu");
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
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","northtel","cpu");
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
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","dlink","cpu");
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
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","bdcom","cpu");
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
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","cisco","memory");
                    		}catch(Exception e){
                    			e.printStackTrace();
                    		}
            			}
            		}else if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("h3c")){ 
            			//h3c��MEMORY
            			Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
            			if(node != null){
            				H3CMemorySnmp h3cmemorysnmp = null;
                			try{
                				h3cmemorysnmp = (H3CMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.H3CMemorySnmp").newInstance();
                    			returnHash = h3cmemorysnmp.collect_Data(alarmIndicatorsNode);
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","h3c","memory");
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
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","entrasys","memory");
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
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","entrasys","memory");
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
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","maipu","memory");
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
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","redgiant","memory");
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
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","northtel","memory");
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
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","dlink","memory");
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
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,"net","bdcom","memory");
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
                			//IP-MAC���������ݿ�
                			//hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"ipmac");
                		}catch(Exception e){
                			e.printStackTrace();
                		}
            		}
            	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("utilhdx")){
//            		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
//            		if(node != null){
//            			UtilHdxSnmp utilhdxsnmp = null;
//            			try{
//            				utilhdxsnmp = (UtilHdxSnmp)Class.forName("com.afunms.polling.snmp.interfaces.UtilHdxSnmp").newInstance();
//                			returnHash = utilhdxsnmp.collect_Data(alarmIndicatorsNode);
//                			//�������ݿ�
//                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"utilhdx");
//                		}catch(Exception e){
//                			e.printStackTrace();
//                		}
//            		}
            	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("interface")){
            		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
            		if(node != null){
            			InterfaceSnmp interfacesnmp = null;
            			try{
//            				SysLogger.info("############################################################");
//            				SysLogger.info("##########��ʼ�ɼ�"+node.getIpAddress()+" �ӿ���Ϣ...##########");
//            				SysLogger.info("############################################################");
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
                			}
                			//�������ݿ�
                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"interface");
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
                			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"packs");
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
                    			//FDB���������ݿ�
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
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"flash");
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
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"temperature");
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
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"temperature");
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
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"temperature");
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
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"fan");
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
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"fan");
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
                    			//FDB���������ݿ�
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"power");
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
                    			//FDB���������ݿ�
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"power");
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
                    			//FDB���������ݿ�
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"voltage");
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
                    			//FDB���������ݿ�
                    			hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"voltage");
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
