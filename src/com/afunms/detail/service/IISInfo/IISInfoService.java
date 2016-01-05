package com.afunms.detail.service.IISInfo;

import java.util.Hashtable;
import java.util.List;

import com.afunms.application.dao.IISConfigDao;
import com.afunms.application.model.IISVo;

/**
 * 
 * @author HONGLI  Mar 7, 2011
 *
 */
public class IISInfoService {
	
	
	/**
	 * �����ݿ��л��IIS�Ĳɼ���Ϣ
	 * @param nodeid
	 * @return
	 */
	public List<IISVo> getIISData(String nodeid){
		IISConfigDao iisConfigDao = new IISConfigDao();
		List<IISVo> iisdata = null;
		try {
			iisdata = iisConfigDao.getIISData(nodeid);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			iisConfigDao.close();
		}
		return iisdata;
	}
	
	/**
	 * �õ���ͨ��
	 * @param ip
	 * @return
	 */
	public String getIISPing(String ip){
		String pingvalue = "0";
		IISConfigDao iisConfigDao = new IISConfigDao();
		try {
			pingvalue = iisConfigDao.getPingvalue(ip);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			iisConfigDao.close();
		}
		return pingvalue;
	}
}
