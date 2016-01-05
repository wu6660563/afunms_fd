package com.afunms.detail.service.mqInfo;

import java.util.Hashtable;

import com.afunms.application.dao.MQConfigDao;

/**
 * @author HONGLI E-mail: ty564457881@163.com
 * @version ����ʱ�䣺Apr 8, 2011 3:06:33 PM
 * ��˵��
 */
public class MQInfoService {
	
	/**
	 * <p>�����ݿ��л�ȡmq�Ĳɼ���Ϣ</p>
	 * @param nodeid
	 * @return
	 */
	public Hashtable getMQDataHashtable(String nodeid) {
		Hashtable retHashtable = null;
		MQConfigDao mqConfigDao = new MQConfigDao();
		try{
			retHashtable = mqConfigDao.getMQDataHashtable(nodeid);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			mqConfigDao.close();
		}
		return retHashtable;
	}
}
