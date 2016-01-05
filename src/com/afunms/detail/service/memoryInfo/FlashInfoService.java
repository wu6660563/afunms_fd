package com.afunms.detail.service.memoryInfo;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.List;

import com.afunms.temp.dao.FlashTempDao;
import com.afunms.temp.dao.MemoryTempDao;
import com.afunms.temp.model.NodeTemp;

public class FlashInfoService {
	private String type;
	
	private String subtype;
	
	private String nodeid;
	

	/**
	 * @param type
	 * @param subtype
	 * @param nodeid
	 */
	public FlashInfoService(String nodeid, String type, String subtype) {
		this.nodeid = nodeid;
		this.type = type;
		this.subtype = subtype;
	}
	
	/**
	 * ��ȡ��ǰ�ڴ��ֵ
	 * @return
	 */
	public List<NodeTemp> getCurrPerFlashListInfo(){
		List<NodeTemp> nodeTempList = null;
		FlashTempDao flashTempDao = new FlashTempDao();
		try {
			nodeTempList = flashTempDao.getCurrFlashList(nodeid, type, subtype);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}finally{
			flashTempDao.close();
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
		}finally{
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
}
