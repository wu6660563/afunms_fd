package com.afunms.detail.service.serviceInfo;

import java.util.List;

import com.afunms.temp.dao.OthersTempDao;
import com.afunms.temp.dao.ServiceTempDao;
import com.afunms.temp.model.ServiceNodeTemp;


public class ServiceInfoService {
	
	private String type;
	
	private String subtype;
	
	private String nodeid;
	

	/**
	 * @param type
	 * @param subtype
	 * @param nodeid
	 */
	public ServiceInfoService(String nodeid, String type, String subtype) {
		this.nodeid = nodeid;
		this.type = type;
		this.subtype = subtype;
	}
	
	public List<ServiceNodeTemp> getCurrServiceInfo(){
		ServiceTempDao serviceTempDao = new ServiceTempDao();
		List<ServiceNodeTemp> list = null;
		try {
			list = serviceTempDao.getNodeTempList(nodeid, type, subtype);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			serviceTempDao.close();
		}
		return list;
	}
	
	/**
	 * <p>�õ�servicelist</p> 
	 * <p>��nms_other_data_temp</p>
	 * <p>windows wmi</p>
	 * @return
	 */
	public List getServicelistInfo(){
		ServiceTempDao serviceTempDao = null;
		List retList = null;
		try{
			serviceTempDao = new ServiceTempDao();
			retList = serviceTempDao.getServicelistInfo(nodeid, type, subtype);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			serviceTempDao.close();
		}
		return retList;
	}
	
	/**
	 * �õ����豸�������ֶ���Ϣ��ɵķ����б�
	 * <p>windows snmp</p>
	 * @return
	 */
	public List getServicelistInfoAll(){
		ServiceTempDao serviceTempDao = null;
		List retList = null;
		try{
			serviceTempDao = new ServiceTempDao();
			retList = serviceTempDao.getServicelistInfoAll(nodeid, type, subtype);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			serviceTempDao.close();
		}
		return retList;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
