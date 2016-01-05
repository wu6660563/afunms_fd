package com.afunms.detail.service.memoryInfo;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.polling.api.I_HostCollectData;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.temp.dao.CpuTempDao;
import com.afunms.temp.dao.DiskTempDao;
import com.afunms.temp.dao.MemoryTempDao;
import com.afunms.temp.model.NodeTemp;

public class MemoryInfoService {
	private String type;
	
	private String subtype;
	
	private String nodeid;
	

	/**
	 * @param type
	 * @param subtype
	 * @param nodeid
	 */
	public MemoryInfoService(String nodeid, String type, String subtype) {
		this.nodeid = nodeid;
		this.type = type;
		this.subtype = subtype;
	}
	
	public MemoryInfoService() {
	}

	/**
	 * ��ȡ��ǰ�ڴ��ֵ
	 * @return
	 */
	public List<NodeTemp> getCurrPerMemoryListInfo(){
		List<NodeTemp> nodeTempList = null;
		MemoryTempDao memoryTempDao = new MemoryTempDao();
		try {
			nodeTempList = memoryTempDao.getCurrMemoryListInfo(nodeid, type, subtype);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}finally{
			memoryTempDao.close();
		}
		return nodeTempList;
	}
	
	/**
	 * �õ�����sindex
	 * @return
	 */
	public List getCurrMemorySindex(){
		List sindexList = null;
		MemoryTempDao memoryTempDao = new MemoryTempDao();
		try {
				sindexList = memoryTempDao.getCurrMemorySindex(nodeid, type, subtype);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally{
			memoryTempDao.close();
		}
		return sindexList;
	}
	
	/**
	 * ��ȡ��ǰ�ڴ����Ϣ
	 * @param sindex
	 * @return
	 */
	public List<NodeTemp> getCurrMemoryListInfo(String sindex){
		List<NodeTemp> nodeTempList = null;
		MemoryTempDao memoryTempDao = new MemoryTempDao();
		try {
			nodeTempList = memoryTempDao.getCurrMemoryListInfo(nodeid, type, subtype,sindex);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}finally{
			memoryTempDao.close();
		}
		return nodeTempList;
	}
	
	/**
	 * ��ȡ��ǰ�ڴ����Ϣ
	 * @return
	 */
	public Hashtable getCurrMemoryListInfo(){
		Hashtable memoryHashtable = new Hashtable();
		List sindexsList = getCurrMemorySindex();
		DecimalFormat df=new DecimalFormat("#.##");
		for(int i=0;i<sindexsList.size();i++){
			Hashtable memoryItemHashtable = new Hashtable();
			List<NodeTemp> diskList = getCurrMemoryListInfo(String.valueOf(sindexsList.get(i)));
			for(int j=0;j<diskList.size();j++){
				NodeTemp nodeTemp = diskList.get(j);
				String subentity = nodeTemp.getSubentity();
				String thevalue = nodeTemp.getThevalue();
				String unit = nodeTemp.getUnit();
				memoryItemHashtable.put(subentity, df.format(Double.valueOf(thevalue))+unit);
			}
			memoryItemHashtable.put("name", String.valueOf(sindexsList.get(i)));
			memoryHashtable.put(i, memoryItemHashtable);
		}
		return memoryHashtable;
	}
	
	public Vector getMemoryInfo(){
		Vector retVector = new Vector();
		MemoryTempDao memoryTempDao = new MemoryTempDao();
		try {
			retVector = memoryTempDao.getMemoryInfo(nodeid, type, subtype);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			memoryTempDao.close();
		}
		return retVector;
	}

	/**
	 * ���ݼ�ص�Node�б�õ��ڴ��������б�
	 * @param monitornodelist
	 * @return
	 */
	public List<NodeTemp> getMemoryInfo(List monitornodelist) {
		if(monitornodelist == null || monitornodelist.size() == 0){
			return null;
		}
		List<NodeTemp> nodeTempList = null;
		MemoryTempDao memoryTempDao = new MemoryTempDao();
		try {
			nodeTempList = memoryTempDao.getMemoryInfo(monitornodelist);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}finally{
			memoryTempDao.close();
		}
		return nodeTempList;
	}

	public Hashtable<Integer, Hashtable<String, String>> getCurDayMemoryValueHashtableInfo(String ipaddress) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currDay = simpleDateFormat.format(new Date());
        String startTime = currDay + " 00:00:00";
        String toTime = currDay + " 23:59:59";
        return getMemoryValueHashtableInfo(ipaddress, startTime, toTime);
    }

    public Hashtable<Integer, Hashtable<String, String>> getMemoryValueHashtableInfo(String ipaddress,
            String startTime, String toTime) {
        I_HostLastCollectData hostlastmanager=new HostLastCollectDataManager();
        Hashtable memoryValueHashtable = new Hashtable();
        try {
            memoryValueHashtable = hostlastmanager.getMemory(ipaddress,
                    "Memory", startTime, toTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return memoryValueHashtable;
    }

    public Hashtable[] getCurDayAllMemoryValueHashtableInfo(String ipaddress) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currDay = simpleDateFormat.format(new Date());
        String startTime = currDay + " 00:00:00";
        String toTime = currDay + " 23:59:59";
        return getAllMemoryValueHashtableInfo(ipaddress, startTime, toTime);
    }

    public Hashtable[] getAllMemoryValueHashtableInfo(String ipaddress,
            String startTime, String toTime) {
        I_HostCollectData hostmanager=new HostCollectDataManager();
        Hashtable[] memoryValueHashtables =null;
        try{
            memoryValueHashtables = hostmanager.getMemory(ipaddress,"Memory",startTime,toTime);
        }catch(Exception e){
            e.printStackTrace();
        }
        return memoryValueHashtables;
    }
}
