/*
 * Created on 2010-06-24
 *
 */
package com.afunms.polling.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.dao.NodeGatherIndicatorsDao;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.impl.ProcessTomcatData;
import com.afunms.polling.node.Tomcat;
import com.afunms.polling.om.Task;
import com.afunms.util.DataGate;


public class M5TomcatTask extends MonitorTask {
	
	public void run() {
		SysLogger.info("#### ��ʼִ��TOMCAT��5���Ӳɼ����� ####");
		try{
			
//			Connection con = null;
//			PreparedStatement stmt = null;
//		 	try {
//		 		con=DataGate.getCon();
//			 	try{
//			 		stmt = con.prepareStatement("select * from system_eventlist");
//			 		stmt.execute();
//			 		stmt.close();
//			 	}catch(Exception e){
//			 		e.printStackTrace();
//			 	}finally{
//			 		try{
//			 			if (stmt != null)stmt.close();
//			 		}catch(Exception e){
//			 			
//			 		}
//			 	}	 		
//			}catch(Exception ex){	
//				ex.printStackTrace();
//			}finally{
//				try{
//				con.commit();
//				stmt.close();
//				DataGate.freeCon(con);
//				}catch(Exception ex){
//					
//				}
//			}
			NodeGatherIndicatorsDao indicatorsdao = new NodeGatherIndicatorsDao();
	    	List<NodeGatherIndicators> monitorItemList = new ArrayList<NodeGatherIndicators>(); 	
	    	Hashtable<String,Hashtable<String,NodeGatherIndicators>> urlHash = new Hashtable<String,Hashtable<String,NodeGatherIndicators>>();//�����Ҫ���ӵ�DB2ָ��  <dbid:Hashtable<name:NodeGatherIndicators>>
	    	List<Tomcat> tomcats = new ArrayList<Tomcat>();
	    	try{
	    		//��ȡ�����õ�TOMCAT���б�����ָ��
	    		monitorItemList = indicatorsdao.getByInterval("5", "m",1,"middleware","tomcat");
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}finally{
	    		indicatorsdao.close();
	    	}
	    	if(monitorItemList == null)monitorItemList = new ArrayList<NodeGatherIndicators>();
	    	for(int i=0;i<monitorItemList.size();i++){
	    		NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators)monitorItemList.get(i);

	    		//TOMCAT����
    			if(urlHash.containsKey(nodeGatherIndicators.getNodeid())){
    				//��dbid�Ѿ�����,���ȡԭ����,�ٰ��µ���ӽ�ȥ
    				Hashtable gatherHash = (Hashtable)urlHash.get(nodeGatherIndicators.getNodeid());
    				gatherHash.put(nodeGatherIndicators.getName(), nodeGatherIndicators);
    				urlHash.put(nodeGatherIndicators.getNodeid(), gatherHash);
    			}else{
    				//��dbid������,����µ���ӽ�ȥ
    				Hashtable gatherHash = new Hashtable();
    				gatherHash.put(nodeGatherIndicators.getName(), nodeGatherIndicators);
    				urlHash.put(nodeGatherIndicators.getNodeid(), gatherHash);
    			}
	    	}
        		int numThreads = 200;        		
        		try {
        			List numList = new ArrayList();
        			TaskXml taskxml = new TaskXml();
        			numList = taskxml.ListXml();
        			for (int k = 0; k < numList.size(); k++) {
        				Task task = new Task();
        				BeanUtils.copyProperties(task, numList.get(k));
        				if (task.getTaskname().equals("netthreadnum")){
        					numThreads = task.getPolltime().intValue();
        				}
        			}

        		} catch (Exception e) {
        			e.printStackTrace();
        		}
        		
        		//����TOMCAT�ɼ��߳�
        		if(urlHash != null && urlHash.size()>0){
        			// �����̳߳�
            		ThreadPool dbthreadPool = new ThreadPool(urlHash.size());
        			//������Ҫ�ɼ���Wasָ��
        			for(Enumeration enumeration = urlHash.keys(); enumeration.hasMoreElements();){
						String dbid = (String)enumeration.nextElement();
						Tomcat node = (Tomcat)PollingEngine.getInstance().getTomcatByID(Integer.parseInt(dbid));
						tomcats.add(node);
						if(!node.isManaged())continue;
						Hashtable<String,NodeGatherIndicators> gatherHash = (Hashtable<String,NodeGatherIndicators>)urlHash.get(dbid);
						dbthreadPool.runTask(createTOMCATTask(dbid,gatherHash));
					}
        			// �ر��̳߳ز��ȴ������������
            		dbthreadPool.join();
            		dbthreadPool.close();
            		dbthreadPool = null;
        		}
        		//���ɼ�����tomcat��Ϣ���
        		ProcessTomcatData processTomcatData = new ProcessTomcatData();
        		processTomcatData.saveTomcatData(tomcats, ShareData.getTomcatdata());
	    								
		}catch(Exception e){					 	
			e.printStackTrace();
		}finally{
			//SysLogger.info("#### M5TOMCATTask Thread Count : "+Thread.activeCount()+" ####");
		}
	}
	
	/**
    ����WEBLOGIC�ɼ�����
	 */	
	private static Runnable createTOMCATTask(final String dbid,final Hashtable gatherHash) {
    return new Runnable() {
        public void run() {
            try {                	
//            	HostCollectDataManager hostdataManager=new HostCollectDataManager(); 
            	TomcatDataCollector tomcatcollector = new TomcatDataCollector();
            	tomcatcollector.collect_data(dbid, gatherHash);
            }catch(Exception exc){
            	
            }
        }
    };
	}
	
}
