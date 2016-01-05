/*
 * Created on 2010-06-24
 *
 */
package com.afunms.polling.task;

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
import com.afunms.polling.impl.ProcessWeblogicData;
import com.afunms.polling.node.Was;
import com.afunms.polling.om.Task;


public class M5WasTask extends MonitorTask {
	
	public void run() {
		SysLogger.info("#### ��ʼִ��Was��5���Ӳɼ����� ���� ��ʼ���� ####");
		try{
			NodeGatherIndicatorsDao indicatorsdao = new NodeGatherIndicatorsDao();
	    	List<NodeGatherIndicators> monitorItemList = new ArrayList<NodeGatherIndicators>(); 	
	    	Hashtable<String,Hashtable<String,NodeGatherIndicators>> urlHash = new Hashtable<String,Hashtable<String,NodeGatherIndicators>>();//�����Ҫ���ӵ�DB2ָ��  <dbid:Hashtable<name:NodeGatherIndicators>>
	    	try{
	    		//��ȡ�����õ�WEBLOGIC���б�����ָ��
	    		monitorItemList = indicatorsdao.getByInterval("5", "m",1,"middleware","was");
	    		
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}finally{
	    		indicatorsdao.close();
	    	}
	    	if(monitorItemList == null)monitorItemList = new ArrayList<NodeGatherIndicators>();
	    	for(int i=0;i<monitorItemList.size();i++){
	    		NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators)monitorItemList.get(i);

	    		//Was����
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
        		//����Was�ɼ��߳�
        		if(urlHash != null && urlHash.size()>0){
        			// �����̳߳�
            		ThreadPool dbthreadPool = new ThreadPool(urlHash.size());
        			//������Ҫ�ɼ���Wasָ��
        			for(Enumeration enumeration = urlHash.keys(); enumeration.hasMoreElements();){
						String dbid = (String)enumeration.nextElement();
						Was node = (Was)PollingEngine.getInstance().getWasByID(Integer.parseInt(dbid));
						//if(!node.isManaged())continue;
						Hashtable<String,NodeGatherIndicators> gatherHash = (Hashtable<String,NodeGatherIndicators>)urlHash.get(dbid);
						dbthreadPool.runTask(createWasTask(dbid,gatherHash));
					}
        			// �ر��̳߳ز��ȴ������������
            		dbthreadPool.join();
            		dbthreadPool.close();
            		dbthreadPool = null;
        		}
		}catch(Exception e){					 	
			e.printStackTrace();
		}finally{
			//SysLogger.info("#### M5WasTask Thread Count : "+Thread.activeCount()+" ####");
		}
	}
	
	/**
    ����WAS�ɼ�����
	 */	
	private static Runnable createWasTask(final String dbid,final Hashtable gatherHash) {
    return new Runnable() {
        public void run() {
            try {                	
//            	HostCollectDataManager hostdataManager=new HostCollectDataManager(); 
            	WasDataCollector wascollector = new WasDataCollector();
            	//wascollector.collect_Data(dbid, gatherHash);
            }catch(Exception exc){
            	
            }
        }
    };
	}
	
}
