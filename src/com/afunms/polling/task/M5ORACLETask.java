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
		SysLogger.info("#### 开始执行ORACLE的5分钟采集任务 ####");
		try{
			NodeGatherIndicatorsDao indicatorsdao = new NodeGatherIndicatorsDao();
	    	List<NodeGatherIndicators> monitorItemList = new ArrayList<NodeGatherIndicators>(); 	
	    	Hashtable<String,Hashtable<String,NodeGatherIndicators>> oracleHash = new Hashtable<String,Hashtable<String,NodeGatherIndicators>>();//存放需要监视的DB2指标  <dbid:Hashtable<name:NodeGatherIndicators>>

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
				//取得oracle采集
				if (oraclesidlist != null) {
					for (int i = 0; i < oraclesidlist.size(); i++) {
						OracleEntity dbmonitorlist = (OracleEntity)oraclesidlist.get(i);
						dbhash.put(dbmonitorlist.getId()+"", dbmonitorlist);
					}
				}
			}catch(Exception e){
				
			}
	    	try{
	    		//获取被启用的ORACLE所有被监视指标
	    		monitorItemList = indicatorsdao.getByInterval("5", "m",1,"db","oracle");
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}finally{
	    		indicatorsdao.close();
	    	}
	    	if(monitorItemList == null)monitorItemList = new ArrayList<NodeGatherIndicators>();
	    	for(int i=0;i<monitorItemList.size();i++){
	    		NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators)monitorItemList.get(i);
	    		//判断该数据库是否被监视
	    		if(!dbhash.containsKey(nodeGatherIndicators.getNodeid()))continue;
	    		//ORACLE数据库
    			if(oracleHash.containsKey(nodeGatherIndicators.getNodeid())){
    				//若dbid已经存在,则获取原来的,再把新的添加进去
    				Hashtable gatherHash = (Hashtable)oracleHash.get(nodeGatherIndicators.getNodeid());
    				gatherHash.put(nodeGatherIndicators.getName(), nodeGatherIndicators);
    				oracleHash.put(nodeGatherIndicators.getNodeid(), gatherHash);
    			}else{
    				//若dbid不存在,则把新的添加进去
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
        		
        		//生成ORACLE采集线程
        		if(oracleHash != null && oracleHash.size()>0){
        			// 生成线程池
            		ThreadPool dbthreadPool = new ThreadPool(oracleHash.size());
        			//存在需要采集的ORACLE指标
        			for(Enumeration enumeration = oracleHash.keys(); enumeration.hasMoreElements();){
						String dbid = (String)enumeration.nextElement();
						Hashtable<String,NodeGatherIndicators> gatherHash = (Hashtable<String,NodeGatherIndicators>)oracleHash.get(dbid);
						dbthreadPool.runTask(createORACLETask(dbid,gatherHash));
					}
        			// 关闭线程池并等待所有任务完成
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
    创建ORACLE采集任务
	 */	
	private static Runnable createORACLETask(final String dbid,final Hashtable gatherHash) {
    return new Runnable() {
        public void run() {
            try {
            	OracleDataCollector oraclecollector = new OracleDataCollector();
            	SysLogger.info("##############################");
            	SysLogger.info("### 开始采集ID为"+dbid+"的ORACLE数据 ");
            	SysLogger.info("##############################");
//            	oraclecollector.collect_data(dbid, gatherHash);
            }catch(Exception exc){
            	exc.printStackTrace();
            }
        }
    };
	}
	
}
