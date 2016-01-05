package com.afunms.detail.service.processInfo;

import java.util.Hashtable;
import java.util.List;

import com.afunms.detail.reomte.model.ProcessInfo;
import com.afunms.temp.dao.ProcessTempDao;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;


public class ProcessInfoService {
	
	private String type;
	
	private String subtype;
	
	private String nodeid;
	
	private HostNode hostNode;
	/**
	 * @param type
	 * @param subtype
	 * @param nodeid
	 */
	public ProcessInfoService(String nodeid, String type, String subtype) {
		this.nodeid = nodeid;
		this.type = type;
		this.subtype = subtype;
		HostNodeDao hostNodeDao = new HostNodeDao();
        try {
            hostNode = (HostNode)hostNodeDao.findByID(nodeid);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            hostNodeDao.close();
        }
	}
	
	public List<ProcessInfo> getCountProcessInfoByName(){
		ProcessTempDao processTempDao = new ProcessTempDao(hostNode.getIpAddress());
		List<ProcessInfo> list = null;
		try {
			list = processTempDao.countProcessInfoByName(nodeid, type, subtype);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			processTempDao.close();
		}
		return list;
	}
	
	public List<ProcessInfo> getCurrProcessInfo(){
		ProcessTempDao processTempDao = new ProcessTempDao(hostNode.getIpAddress());
		List<ProcessInfo> list = null;
		try {
			list = processTempDao.getProcessInfo(nodeid, type, subtype);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			processTempDao.close();
		}
		return list;
	}
	
	public List<ProcessInfo> getCurrProcessDetailInfo(String processName){
		ProcessTempDao processTempDao = new ProcessTempDao(hostNode.getIpAddress());
		List<ProcessInfo> list = null;
		try {
			list = processTempDao.getProcessDetailInfoByName(nodeid, type, subtype, processName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			processTempDao.close();
		}
		return list;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public Hashtable getProcessInfo(String order) {
		Hashtable retHashtable = null;
		ProcessTempDao processTempDao = new ProcessTempDao(hostNode.getIpAddress());
		try {
			retHashtable = processTempDao.getProcessInfo(nodeid, type, subtype, order);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			processTempDao.close();
		}
		return retHashtable;
	}

}
