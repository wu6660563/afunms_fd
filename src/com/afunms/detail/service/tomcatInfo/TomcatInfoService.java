package com.afunms.detail.service.tomcatInfo;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.application.dao.TomcatDao;
import com.afunms.node.model.PerformanceInfo;
import com.afunms.node.service.PerformanceInfoService;

public class TomcatInfoService {

    private static final String PING_TABLE = "tomcatping";

    private static final String JVM_TABLE = "tomcatjvm";
    /**
	 * <p>从数据库中获取tomcat的采集信息</p>
	 * @param nodeid
	 * @return
	 */
	public Hashtable getTomcatDataHashtable(String nodeid) {
		Hashtable retHashtable = null;
		TomcatDao tomcatDao = new TomcatDao();
		try{
			// retHashtable = tomcatDao.getTomcatDataHashtable(nodeid);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			tomcatDao.close();
		}
		return retHashtable;
	}
    
    public Hashtable<String, List<Vector<String>>> getPingInfo(String nodeid, String startTime, String endTime) {
        PerformanceInfoService service = new PerformanceInfoService();
        List<PerformanceInfo> list = service.getPerformance(PING_TABLE + nodeid, startTime, endTime);
        List<Vector<String>> infoList = new ArrayList<Vector<String>>();
        for (PerformanceInfo performanceInfo : list) {
            Vector<String> vector = new Vector<String>();
            vector.add(0, performanceInfo.getThevalue());
            vector.add(1, performanceInfo.getCollecttime());
            vector.add(2, performanceInfo.getUnit());
            infoList.add(vector);
        }
        Hashtable<String, List<Vector<String>>> hashtable = new Hashtable<String, List<Vector<String>>>(); 
        hashtable.put("list", infoList);
        return hashtable;
	}

    public Hashtable<String, List<Vector<String>>> getJVMMemoryInfo(String nodeid, String startTime, String endTime) {
        PerformanceInfoService service = new PerformanceInfoService();
        List<PerformanceInfo> list = service.getPerformance(JVM_TABLE + nodeid, startTime, endTime);
        List<Vector<String>> infoList = new ArrayList<Vector<String>>();
        for (PerformanceInfo performanceInfo : list) {
            Vector<String> vector = new Vector<String>();
            vector.add(0, performanceInfo.getThevalue());
            vector.add(1, performanceInfo.getCollecttime());
            vector.add(2, performanceInfo.getUnit());
            infoList.add(vector);
        }
        Hashtable<String, List<Vector<String>>> hashtable = new Hashtable<String, List<Vector<String>>>(); 
        hashtable.put("list", infoList);
        return hashtable;
    }

    public boolean savePingInfo(String nodeid, PerformanceInfo info) {
        return save(PING_TABLE + nodeid, info);
    }

    public boolean saveJVMInfo(String nodeid, PerformanceInfo info) {
        return save(JVM_TABLE + nodeid, info);
    }

    private boolean save(String table, PerformanceInfo info) {
        PerformanceInfoService service = new PerformanceInfoService();
        return service.save(table, info);
    }
}
