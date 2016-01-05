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
import com.afunms.application.model.DBTypeVo;
import com.afunms.application.model.DBVo;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.dao.NodeGatherIndicatorsDao;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.polling.om.Task;
import com.afunms.polling.snmp.db.InformixDataCollector;
import com.afunms.polling.snmp.db.SybaseDataCollector;


public class M30InformixTask extends MonitorTask {
	
	@Override
	public void run() {
		SysLogger.info("#### 开始执行Informix的30分钟采集任务 ####");
		try{
			
	    	List<NodeGatherIndicators> monitorItemList = new ArrayList<NodeGatherIndicators>(); 	
	    	Hashtable<String,Hashtable<String,NodeGatherIndicators>> informixHash = new Hashtable<String,Hashtable<String,NodeGatherIndicators>>();//存放需要监视的DB2指标  <dbid:Hashtable<name:NodeGatherIndicators>>

	    	Hashtable dbhash = new Hashtable();
			
			try{	
				DBTypeVo db2typevo = null;
				DBTypeDao typedao = null;
				try {
					typedao = new DBTypeDao();
					db2typevo = (DBTypeVo) typedao.findByDbtype("informix");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (typedao != null)
						typedao.close();
				}
				List db2list = new ArrayList();
				DBDao dbdao = null;
				try {
					dbdao = new DBDao();
					db2list = dbdao.getDbByTypeMonFlag(db2typevo.getId(), 1);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (dbdao != null)
						dbdao.close();
				}
				//取得db2采集
				if (db2list != null) {
					for (int i = 0; i < db2list.size(); i++) {
						DBVo dbmonitorlist = (DBVo)db2list.get(i);
						dbhash.put(dbmonitorlist.getId()+"", dbmonitorlist);
					}
				}
			}catch(Exception e){
				
			}
			NodeGatherIndicatorsDao indicatorsdao = new NodeGatherIndicatorsDao();
	    	try{
	    		//获取被启用的Informix所有被监视指标
	    		monitorItemList = indicatorsdao.getByInterval("30", "m",1,"db","informix");
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
	    		//INFORMIX数据库
    			if(informixHash.containsKey(nodeGatherIndicators.getNodeid())){
    				//若dbid已经存在,则获取原来的,再把新的添加进去
    				Hashtable gatherHash = (Hashtable)informixHash.get(nodeGatherIndicators.getNodeid());
    				gatherHash.put(nodeGatherIndicators.getName(), nodeGatherIndicators);
    				informixHash.put(nodeGatherIndicators.getNodeid(), gatherHash);
    			}else{
    				//若dbid不存在,则把新的添加进去
    				Hashtable gatherHash = new Hashtable();
    				gatherHash.put(nodeGatherIndicators.getName(), nodeGatherIndicators);
    				informixHash.put(nodeGatherIndicators.getNodeid(), gatherHash);
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
        		
        		//生成INFORMIX采集线程
        		if(informixHash != null && informixHash.size()>0){
        			// 生成线程池
            		ThreadPool dbthreadPool = new ThreadPool(informixHash.size());
        			//存在需要采集的INFORMIX指标
        			for(Enumeration enumeration = informixHash.keys(); enumeration.hasMoreElements();){
						String dbid = (String)enumeration.nextElement();
						Hashtable<String,NodeGatherIndicators> gatherHash = (Hashtable<String,NodeGatherIndicators>)informixHash.get(dbid);
						dbthreadPool.runTask(createInformixTask(dbid,gatherHash));
					}
        			// 关闭线程池并等待所有任务完成
            		dbthreadPool.join();
            		dbthreadPool.close();
            		dbthreadPool = null;
        		}
	    								
		}catch(Exception e){					 	
			e.printStackTrace();
		}finally{
			SysLogger.info("#### M30INFORMIXTask Thread Count : "+Thread.activeCount()+" ####");
		}
	}
	
	/**
    创建INFORMIX采集任务
	 */	
	private static Runnable createInformixTask(final String dbid,final Hashtable gatherHash) {
    return new Runnable() {
        public void run() {
            try {                	
            	InformixDataCollector informixcollector = new InformixDataCollector();
            	SysLogger.info("##############################");
            	SysLogger.info("### 开始采集ID为"+dbid+"的INFORMIX数据 ");
            	SysLogger.info("##############################");
            	//informixcollector.collect_data(dbid, gatherHash);
            }catch(Exception exc){
            	
            }
        }
    };
	}
	
}
