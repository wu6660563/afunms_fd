package com.afunms.detail.service.apacheInfo;

import java.util.Hashtable;

import com.afunms.application.dao.ApacheConfigDao;
import com.afunms.application.dao.TomcatDao;
/**
 * <p>�м��Apache Http Server service</p>
 * @author HONGLI  Mar 8, 2011
 *
 */
public class ApacheInfoService {
	
	
	/**
	 * <p>�����ݿ��л�ȡApache HttpServer�Ĳɼ���Ϣ</p>
	 * @param nodeid
	 * @return
	 */
	public Hashtable getApacheDataHashtable(String nodeid) {
		Hashtable retHashtable = null;
		ApacheConfigDao apacheConfigDao = new ApacheConfigDao();
		try{
			retHashtable = apacheConfigDao.getApacheDataHashtable(nodeid);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			apacheConfigDao.close();
		}
		return retHashtable;
	}
}
