package com.afunms.detail.service.jbossInfo;

import java.util.Hashtable;

import com.afunms.application.dao.JBossConfigDao;

/**
 * <p>JBoss�ɼ���Ϣservice</p>
 * @author HONGLI  Mar 7, 2011
 */
public class JBossInfoService {
	
	/**
	 *  <p>��ȡ���ݿ��д洢�Ĳɼ���JBoss������Ϣ</p>
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
