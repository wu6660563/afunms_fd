/*
 * Created on 2005-4-22
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.afunms.polling.task;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;

import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SystemConstant;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.om.Task;
import com.afunms.polling.snmp.LoadAixFile;
import com.afunms.polling.snmp.LoadHpUnixFile;
import com.afunms.polling.snmp.LoadLinuxFile;
import com.afunms.polling.snmp.LoadScoOpenServerFile;
import com.afunms.polling.snmp.LoadScoUnixWareFile;
import com.afunms.polling.snmp.LoadSunOSFile;
import com.afunms.polling.snmp.LoadWindowsWMIFile;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;


//import com.dhcc.webnms.host.ShareData;

/**
 * @author hukelei@dhcc.com.cn
 *
 */
public class HostCollectDataTask extends MonitorTask {
		
	/**
	 * 
	 */
	public HostCollectDataTask() {
		super();
	}

	public void run() {
		try{
	    	HostNodeDao nodeDao = new HostNodeDao(); 
	    	//�õ������ӵ������������豸
	    	List nodeList = new ArrayList();
	    	try{
	    		nodeList = nodeDao.loadHostByFlag(1);   	    	
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}finally{
	    		nodeDao.close();
	    	}
			Vector vector=null;							
        		int numTasks = nodeList.size();
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
        		
        		final Hashtable alldata = new Hashtable();
        		// �����̳߳�
        		ThreadPool threadPool = null;	
        		if(nodeList != null && nodeList.size()>0){
        			threadPool = new ThreadPool(nodeList.size());	
        			// ��������
            		for (int i=0; i<nodeList.size(); i++) {
            			HostNode node = (HostNode)nodeList.get(i);
            			if(node.getEndpoint() == 2){
                    		//REMOTEPING���ӽڵ㣬����
                    		return;
                    	}
            			//��ֻ��PING TELNET SSH��ʽ��������,���������ݲ��ɼ�,����
            			if(node.getCollecttype() == SystemConstant.COLLECTTYPE_PING ||
            					node.getCollecttype() == SystemConstant.COLLECTTYPE_TELNETCONNECT||
            					node.getCollecttype() == SystemConstant.COLLECTTYPE_SSHCONNECT){
            				SysLogger.info("ֻPING TELNET SSH��ʽ��������,�������ݲ��ɼ�,����");
            			}else{
            				threadPool.runTask(createTask(node,alldata));
            			}
                	         	
            		}
            		// �ر��̳߳ز��ȴ������������
            		threadPool.join();
            		threadPool.close();
            		HostCollectDataManager hostdataManager=new HostCollectDataManager(); 
            		try{
            			hostdataManager.createMultiHostData(alldata,"host"); 
            		}catch(Exception e){
            			
            		}
            		hostdataManager = null;
            		alldata.clear();
        		}
        		threadPool = null;
        		
		}catch(Exception e){					 	
			e.printStackTrace();
		}finally{
			//System.out.println("********Host Thread Count : "+Thread.activeCount());
		}
				
	} 
	
    /**
        ��������
    */	
    private static Runnable createTask(final HostNode node,final Hashtable alldata) {
        return new Runnable() {
            public void run() {
                try {                	
                	Hashtable hashv = new Hashtable();
                	LoadAixFile aix=null;
                	LoadLinuxFile linux=null;
                	LoadHpUnixFile hpunix = null;
                	LoadScoUnixWareFile scounix=null;
                	LoadScoOpenServerFile scoopenserver=null;
                	LoadSunOSFile sununix = null;
                	LoadWindowsWMIFile windowswmi = null;
                	//I_HostCollectData hostdataManager=new HostCollectDataManager();
                	
//                	Node hostNode = PollingEngine.getInstance().getNodeByIP(node.getIpAddress());
//                	if(hostNode != null){
//                		hostNode.setAlarm(false);
//    					hostNode.setAlarmlevel(0);
//    					hostNode.getAlarmMessage().clear();
//                	}
                	
                	//ͨ��PING����������ͨ�Լ��
//    				PingUtil pingU=new PingUtil(node.getIpAddress());
//        			Integer[] packet=pingU.ping();
//        			vector=pingU.addhis(packet); 
//        			if(vector!=null){				
//        				ShareData.setPingdata(node.getIpAddress(),vector);
//        				hashv.put("ping", vector);
//        			}
        			
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
                					//linux = new LoadLinuxFile(node.getIpAddress());
                					//hashv=linux.getTelnetMonitorDetail();
                					//linux = null;
                					//alldata.put(node.getIpAddress(), hashv);
                					//hostdataManager.createHostData(node.getIpAddress(),hashv);
                				}catch(Exception e){
                					e.printStackTrace();
                				}
                			}else if(node.getOstype() == 7){
                				SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����ΪHPUNIX����������������");
                				//HPUNIX������
                				try{
//                					hpunix = new LoadHpUnixFile(node.getIpAddress());
//                					hashv=hpunix.getTelnetMonitorDetail();
//                					hpunix = null;
//                					if(hashv != null){
//                						alldata.put(node.getIpAddress(), hashv);
//                					}
                					//hostdataManager.createHostData(node.getIpAddress(),hashv);
                				}catch(Exception e){
                					e.printStackTrace();
                				}
                			}else if(node.getOstype() == 20){
                				SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����ΪSCOUNIXWARE����������������");
                				//LINUX������
                				try{
                					scounix = new LoadScoUnixWareFile(node.getIpAddress());
                					hashv=scounix.getTelnetMonitorDetail();
                					if(hashv!=null&&hashv.size()>0){
                    					try {
                    						alldata.put(node.getIpAddress(), hashv);
    									} catch (Exception e) {
    										e.printStackTrace();
    									}
                					}
                				}catch(Exception e){
                					e.printStackTrace();
                				}
                			}else if(node.getOstype() == 21){
                				SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����ΪSCOOPENSERVER����������������");
                				//LINUX������
                				try{
                					scoopenserver = new LoadScoOpenServerFile(node.getIpAddress());
                					hashv=scoopenserver.getTelnetMonitorDetail();
                					if(hashv!=null&&hashv.size()>0){
                    					try {
                    						alldata.put(node.getIpAddress(), hashv);
    									} catch (Exception e) {
    										e.printStackTrace();
    									}
                					}
                				}catch(Exception e){
                					e.printStackTrace();
                				}
                			}else if(node.getOstype() == 8){
                				SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����ΪSOLARIS����������������");
                				//SOLARIS������
//                				try{
//                					sununix = new LoadSunOSFile(node.getIpAddress());
//                					hashv=sununix.getTelnetMonitorDetail();
//                					sununix = null;
//                					alldata.put(node.getIpAddress(), hashv);
//                					//hostdataManager.createHostData(node.getIpAddress(),hashv);
//                				}catch(Exception e){
//                					e.printStackTrace();
//                				}
                			}else if(node.getOstype() == 5){
                				SysLogger.info("�ɼ�: ��ʼ��WMI��ʽ�ɼ�IP��ַΪ"+node.getIpAddress()+"����ΪWINDOWS����������������");
                				try{
                					windowswmi = new LoadWindowsWMIFile(node.getIpAddress());
                					hashv=windowswmi.getTelnetMonitorDetail();
                					windowswmi = null;
                					alldata.put(node.getIpAddress(), hashv);
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
        					alldata.put(node.getIpAddress(), hashv);
        					//hostdataManager.createHostData(node.getIpAddress(),hashv);
        				}catch(Exception e){
        					e.printStackTrace();
        				}
        				aix=null;
    					hashv=null;
                	} else{
                		//SNMP�ɼ���ʽ
//                    	if(node.getSysOid().equalsIgnoreCase("1.3.6.1.4.1.311.1.1.3.1.1") || 
//                    			node.getSysOid().equalsIgnoreCase("1.3.6.1.4.1.311.1.1.3.1.2")||
//                    			node.getSysOid().equalsIgnoreCase("1.3.6.1.4.1.311.1.1.3.1.3")||
//                    			node.getSysOid().equalsIgnoreCase("1.3.6.1.4.1.311.1.1.3.1")){
//                    		SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����ΪWINDOWS����������������");
//                    		//windows
////                    		WindowsSnmp windows=new WindowsSnmp();
////                    		try{
////                    			hashv=windows.collect_Data(node);
////                    			hostdataManager.createHostData(node.getIpAddress(),hashv);
////                    		}catch(Exception ex){
////                    			ex.printStackTrace();
////                    		}
////                    		windows=null;
////                    		vector=null;
//                    		
//                    	}else if(node.getOstype() == 9){
//                    		if(node.getCollecttype() == 1){
////        						//System.out.println("==================linux================");
////        						LinuxSnmp linuxSnmp = new LinuxSnmp();
////        						hashv = linuxSnmp.collect_Data(node);
////        						//System.out.println("==================linux SNMP end================");
////        						hostdataManager.createHostData(node.getIpAddress(),hashv);
//        					}else{
//        						
//        					}
//                    	}                		
                	} 
                }catch(Exception exc){
                	exc.printStackTrace();
                }

                //System.out.println("Task " + taskID + ": end");
            }
        };
    }
	
}
