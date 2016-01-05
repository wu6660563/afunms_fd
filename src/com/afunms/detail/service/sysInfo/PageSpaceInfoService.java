package com.afunms.detail.service.sysInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import com.afunms.detail.reomte.model.PageSpaceInfo;
import com.afunms.polling.api.I_HostCollectData;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.temp.dao.OthersTempDao;

public class PageSpaceInfoService {
	
	private String type;
	
	private String subtype;
	
	private String nodeid;
	

	/**
	 * @param type
	 * @param subtype
	 * @param nodeid
	 */
	public PageSpaceInfoService(String nodeid, String type, String subtype) {
		this.nodeid = nodeid;
		this.type = type;
		this.subtype = subtype;
	}
	
	
	public List<PageSpaceInfo> getCurrPageSpaceInfo(){
		return getCurrPageSpaceInfo(null);
	}
	
	public List<PageSpaceInfo> getCurrPageSpaceInfo(String[] subentities){
		OthersTempDao othersTempDao = new OthersTempDao();
		List<PageSpaceInfo> pageSpacefInfoList = null;
		try {
			pageSpacefInfoList = othersTempDao.getPageSpaceInfoList(nodeid, type, subtype, subentities);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			othersTempDao.close();
		}
		return pageSpacefInfoList;
	}
	
	public Hashtable<String, String> getCurDayPageDetailValueHashtableInfo(String ipaddress) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currDay = simpleDateFormat.format(new Date());
        String startTime = currDay + " 00:00:00";
        String toTime = currDay + " 23:59:59";
        return getPageDetailValueHashtableInfo(ipaddress, startTime, toTime);
    }

    public Hashtable<String, String> getPageDetailValueHashtableInfo(String ipaddress,
            String startTime, String toTime) {
        Hashtable<String, String> pageDetailValueHashtable = null;
        I_HostCollectData hostmanager=new HostCollectDataManager();
        try{
            pageDetailValueHashtable = hostmanager.getPageingDetail(ipaddress, startTime, toTime);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return pageDetailValueHashtable;
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
