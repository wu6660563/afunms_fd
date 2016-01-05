package com.afunms.detail.service.jbossInfo;

import java.util.Hashtable;

import com.afunms.application.dao.JBossConfigDao;

/**
 * <p>JBoss采集信息service</p>
 * @author HONGLI  Mar 7, 2011
 */
public class JBossInfoService {
	
	/**
	 *  <p>获取数据库中存储的采集的JBoss数据信息</p>
	 * @param nodeid
	 * @return
	 */
	public Hashtable<String, String> getJBossData(String nodeid){
		Hashtable<String, String> jbossData = null;
		JBossConfigDao jBossConfigDao = null;
		try{
			jBossConfigDao = new JBossConfigDao();
			jbossData = jBossConfigDao.getJBossData(nodeid);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			jBossConfigDao.close();
		}
		return jbossData;
	}
}
