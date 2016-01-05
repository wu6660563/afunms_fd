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

import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.DBTypeDao;
import com.afunms.application.dao.OraclePartsDao;
import com.afunms.application.model.DBTypeVo;
import com.afunms.application.model.OracleEntity;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.dao.NodeGatherIndicatorsDao;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.polling.om.Task;
import com.afunms.polling.snmp.db.OracleDataCollector;


public class M5ORACLETask extends MonitorTask {
	
	public void run() {
		SysLogger.info("#### ��ʼִ��ORACLE��5���Ӳɼ����� ####");
		try{
			NodeGatherIndicatorsDao indicatorsdao = new NodeGatherIndicatorsDao();
	    	List<NodeGatherIndicators> monitorItemList = new ArrayList<NodeGatherIndicators>(); 	
	    	Hashtable<String,Hashtable<String,NodeGatherIndicators>> oracleHash = new Hashtable<String,Hashtable<String,NodeGatherIndicators>>();//�����Ҫ���ӵ�DB2ָ��  <dbid:Hashtable<name:NodeGatherIndicators>>

	    	Hashtable dbhash = new Hashtable();
			OraclePartsDao siddao = null;
			try{	
				List oraclesidlist = new ArrayList();
				try {
//					dbdao = new DBDao();
//					db2list = dbdao.getDbByTypeMonFlag(db2typevo.getId(), 1);
					siddao = new OraclePartsDao();
					oraclesidlist = siddao.findByCondition(" where managed=1 ");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (siddao != null)
						siddao.close();
				}
				//ȡ��oracle�ɼ�
				if (oraclesidlist != null) {
					for (int i = 0; i < oraclesidlist.size(); i++) {
						OracleEntity dbmonitorlist = (OracleEntity)oraclesidlist.get(i);
						dbhash.put(dbmonitorlist.getId()+"", dbmonitorlist);
					}
				}
			}catch(Exception e){
				
			}
	    	try{
	    		//��ȡ�����õ�ORACLE���б�����ָ��
	    		monitorItemList = indicatorsdao.getByInterval("5", "m",1,"db","oracle");
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}finally{
	    		indicatorsdao.close();
	    	}
	    	if(monitorItemList == null)monitorItemList = new ArrayList<NodeGatherIndicators>();
	    	for(int i=0;i<monitorItemList.size();i++){
	    		NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators)monitorItemList.get(i);
	    		//�жϸ����ݿ��Ƿ񱻼���
	    		if(!dbhash.containsKey(nodeGatherIndicators.getNodeid()))continue;
	    		//ORACLE���ݿ�
    			if(oracleHash.containsKey(nodeGatherIndicators.getNodeid())){
    				//��dbid�Ѿ�����,���ȡԭ����,�ٰ��µ���ӽ�ȥ
    				Hashtable gatherHash = (Hashtable)oracleHash.get(nodeGatherIndicators.getNodeid());
    				gatherHash.put(nodeGatherIndicators.getName(), nodeGatherIndicators);
    				oracleHash.put(nodeGatherIndicators.getNodeid(), gatherHash);
    			}else{
    				//��dbid������,����µ���ӽ�ȥ
    				Hashtable gatherHash = new Hashtable();
    				gatherHash.put(nodeGatherIndicators.getName(), nodeGatherIndicators);
    				oracleHash.put(nodeGatherIndicators.getNodeid(), gatherHash);
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
        		
        		//����ORACLE�ɼ��߳�
        		if(oracleHash != null && oracleHash.size()>0){
        			// �����̳߳�
            		ThreadPool dbthreadPool = new ThreadPool(oracleHash.size());
        			//������Ҫ�ɼ���ORACLEָ��
        			for(Enumeration enumeration = oracleHash.keys(); enumeration.hasMoreElements();){
						String dbid = (String)enumeration.nextElement();
						Hashtable<String,NodeGatherIndicators> gatherHash = (Hashtable<String,NodeGatherIndicators>)oracleHash.get(dbid);
						dbthreadPool.runTask(createORACLETask(dbid,gatherHash));
					}
        			// �ر��̳߳ز��ȴ������������
            		dbthreadPool.join();
            		dbthreadPool.close();
            		dbthreadPool = null;
        		}							
		}catch(Exception e){					 	
			e.printStackTrace();
		}finally{
			SysLogger.info("#### M5ORACLETask Thread Count : "+Thread.activeCount()+" ####");
		}
	}
	
	/**
    ����ORACLE�ɼ�����
	 */	
	private static Runnable createORACLETask(final String dbid,final Hashtable gatherHash) {
    return new Runnable() {
        public void run() {
            try {
            	OracleDataCollector oraclecollector = new OracleDataCollector();
            	SysLogger.info("##############################");
            	SysLogger.info("### ��ʼ�ɼ�IDΪ"+dbid+"��ORACLE���� ");
            	SysLogger.info("##############################");
//            	oraclecollector.collect_data(dbid, gatherHash);
            }catch(Exception exc){
            	exc.printStackTrace();
            }
        }
    };
	}
	
}
