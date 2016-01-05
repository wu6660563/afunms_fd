package com.afunms.detail.service.pingInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.polling.api.I_HostCollectData;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.temp.dao.PingTempDao;
import com.afunms.temp.dao.SystemTempDao;
import com.afunms.temp.model.NodeTemp;

public class PingInfoService {
	
	private String type;
	
	private String subtype;
	
	private String nodeid;
	

	public PingInfoService() {
		super();
	}

	/**
	 * @param type
	 * @param subtype
	 * @param nodeid
	 */
	public PingInfoService(String nodeid, String type, String subtype) {
		this.nodeid = nodeid;
		this.type = type;
		this.subtype = subtype;
	}
	
	public String getCurrDayPingAvgInfo(String ipaddress){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String currDay = simpleDateFormat.format(new Date());
		String startTime = currDay + " 00:00:00";
		String toTime = currDay + " 23:59:59";
		return getPingAvgInfo(ipaddress, startTime, toTime);
	}

	public String getCurDayResponseTimeAvgInfo(String ipaddress){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currDay = simpleDateFormat.format(new Date());
        String startTime = currDay + " 00:00:00";
        String toTime = currDay + " 23:59:59";
        return getResponseTimeAvgInfo(ipaddress, startTime, toTime);
    }

	public Hashtable<String, String> getCurDayPingValueHashtableInfo(String ipaddress){
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currDay = simpleDateFormat.format(new Date());
        String startTime = currDay + " 00:00:00";
        String toTime = currDay + " 23:59:59";
        return getPingValueHashTableInfo(ipaddress, startTime, toTime);
    }

	public Hashtable<String, String> getPingValueHashTableInfo(String ipaddress, String startTime, String toTime){
        I_HostCollectData hostmanager = new HostCollectDataManager();
        Hashtable ConnectUtilizationhash = new Hashtable();
        try{
            ConnectUtilizationhash = hostmanager.getCategory(ipaddress, "Ping", "ConnectUtilization", startTime, toTime);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return ConnectUtilizationhash;
    }

	public Hashtable<String, String> getCurDayResponseTimeValueHashtableInfo(String ipaddress){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currDay = simpleDateFormat.format(new Date());
        String startTime = currDay + " 00:00:00";
        String toTime = currDay + " 23:59:59";
        return getResponseTimeValueHashTableInfo(ipaddress, startTime, toTime);
    }

    public Hashtable<String, String> getResponseTimeValueHashTableInfo(String ipaddress, String startTime, String toTime){
        I_HostCollectData hostmanager = new HostCollectDataManager();
        Hashtable ConnectUtilizationhash = new Hashtable();
        try{
            ConnectUtilizationhash = hostmanager.getCategory(ipaddress, "Ping", "ResponseTime", startTime, toTime);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return ConnectUtilizationhash;
    }

    public String getPingAvgInfo(String ipaddress, String startTime, String toTime){
		String pingconavg = "0";
		I_HostCollectData hostmanager = new HostCollectDataManager();
		Hashtable ConnectUtilizationhash = new Hashtable();
		try{
			ConnectUtilizationhash = hostmanager.getCategory(ipaddress, "Ping", "ConnectUtilization", startTime, toTime);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		if (ConnectUtilizationhash.get("avgpingcon")!=null){
			pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg == null){
				pingconavg = "0";
			}
			pingconavg = pingconavg.replace("%", "");
		}
		return pingconavg;
	}

	public String getResponseTimeAvgInfo(String ipaddress, String startTime, String toTime){
        String responseTimeAvg = "0";
        I_HostCollectData hostmanager = new HostCollectDataManager();
        Hashtable ConnectUtilizationhash = new Hashtable();
        try{
            ConnectUtilizationhash = hostmanager.getCategory(ipaddress, "Ping", "ResponseTime", startTime, toTime);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        if (ConnectUtilizationhash.get("avgpingcon")!=null){
            responseTimeAvg = (String)ConnectUtilizationhash.get("avgpingcon");
            if(responseTimeAvg == null){
                responseTimeAvg = "0";
            }
            responseTimeAvg = responseTimeAvg.replace("����", "").replaceAll("%","");
        }
        return responseTimeAvg;
    }

	public List<NodeTemp> getCurPingInfo(){
        return getCurrPingInfo(null);
    }

	public List<NodeTemp> getCurrPingInfo(String[] subentities){
		PingTempDao pingTempDao = new PingTempDao();
		List<NodeTemp> nodeTempList = null;
		try {
			nodeTempList = pingTempDao.getNodeTempList(nodeid, type, subtype, subentities);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pingTempDao.close();
		}
		return nodeTempList;
	}
	
	/**
	 * �õ�ʵʱ����ͨ�ʺ���Ӧʱ��
	 * @return
	 */
	public Vector getPingInfo(){
		Vector retVector = null;
		PingTempDao pingTempDao = new PingTempDao();
		try {
			retVector = pingTempDao.getPingInfo(nodeid, type, subtype);
		} catch (Exception e) { 
			e.printStackTrace();
		} finally {
			pingTempDao.close();
		}
		return retVector;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * ���ݼ�ص�Node�б�õ�ƽ����ͨ���б�
	 * @param monitorNodelist
	 * @return
	 */
	public List<NodeTemp> getPingInfo(List monitorNodelist) {
		if(monitorNodelist == null || monitorNodelist.size() == 0){
			return null;
		}
		PingTempDao pingTempDao = new PingTempDao();
		List<NodeTemp> nodeTempList = null;
		try {
			nodeTempList = pingTempDao.getNodeTempList(monitorNodelist);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pingTempDao.close();
		}
		return nodeTempList;
	}
}
