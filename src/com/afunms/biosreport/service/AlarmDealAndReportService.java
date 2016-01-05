package com.afunms.biosreport.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.afunms.common.util.DBManager;
import com.afunms.common.util.SysLogger;
import com.afunms.event.dao.CheckEventDao;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.dao.EventReportDao;

/**
 * 告警处理状态以及报告服务类
 * 
 * @author yag
 *
 */
public class AlarmDealAndReportService {

	private static SysLogger logger = SysLogger.getLogger(AlarmDealAndReportService.class.getName());

	
	/**
	 *  EventListDao获取告警信息
	 */
	
	private List getEventListAll() {
		
		List list = new ArrayList();
		EventListDao eventlist = null;
		
		try{
			eventlist = new EventListDao();
			list = eventlist.loadAll();
		} catch (Exception e) {
			logger.info(this.getClass().getName() 
					+ "整理告警状态处理报告时获取告警信息数据出错"+e.getMessage());
		} finally {
			eventlist.close();
		}
		
		return list;
	}
	
	
	/**
	 *  EventListDao获取告警信息
	 */
	
	private List getEventListByTime(String start, String end) {
		
		List list = new ArrayList();
		EventListDao eventlist = null;
		
		try{
			eventlist = new EventListDao();
			list = eventlist.loadAll();
		} catch (Exception e) {
			logger.info(this.getClass().getName() 
					+ "整理告警状态处理报告时获取告警信息数据出错"+e.getMessage());
		} finally {
			eventlist.close();
		}
		
		return list;
	}
	/**
	 *  EventListDao获取告警信息
	 */
	
	private List getEventReportListByTime(String start, String end) {
		
		List list = new ArrayList();
		EventReportDao eventReportDao = new EventReportDao();
		try{
			//eventlist = new EventListDao();
			list = eventReportDao.loadAll();
		} catch (Exception e) {
			logger.info(this.getClass().getName() 
					+ "整理告警状态处理报告时获取告警信息数据出错"+e.getMessage());
		} finally {
			eventReportDao.close();
		}
		
		return list;
	}
	
	/**
	 * 根据条件查询告警信息
	 * 
	 * @param condition
	 * @return
	 */
	private List getEventListByCondition(String condition) {
		
		List<Map> list = new ArrayList<Map>();
		DBManager db = new DBManager();
		ResultSet rs = null;
		
		String sql = "SELECT s.eventlocation,s.content,s.level1,s.managesign,s.recordtime,"
			+ "s.nodeid,s.businessid,s.subtype,s.managetime,s.subentity,COUNT(s.id) AS idsum"
			+ "FROM system_eventlist AS s"
			+ "WHERE 1=1 "
			+ " and " + condition
			+ "GROUP BY s.nodeid,s.subtype,s.subentity,s.level1";
		
		try{
			rs = db.executeQuery(sql);
			if(rs != null) {
				while(rs.next()) {
					Map map = new HashMap();
					map.put("eventlocation", rs.getString("eventlocation"));
					map.put("content", rs.getString("content"));
					map.put("level1", rs.getInt("level1"));
					map.put("", rs.getInt("managesign"));
					map.put("recordtime", rs.getString("recordtime"));
					map.put("nodeid", rs.getInt("nodeid"));
					map.put("businessid", rs.getString("businessid"));
					map.put("subtype", rs.getString("subtype"));
					map.put("subentity", rs.getString("subentity"));
					map.put("alarm_count", rs.getInt("idsum"));
					list.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				db.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return list;
	}
	
	/**
	 * EventReportDao获取报告信息
	 */
	private List getEventReport(String start,String end) {
		List list = new ArrayList();
		DBManager db = new DBManager();
		ResultSet rs = null;
		
		String sql = "SELECT s.id as eventid,report_man,report_content,deal_time," +
				"report_time,COUNT(*) AS report_count"
				+ " FROM system_eventreport AS r, system_eventlist AS s "
				+ " WHERE r.eventid = s.id AND s.recordtime >='" + start 
				+ "' AND s.recordtime <='"+ end +"'"
				+ " GROUP BY r.report_content";
		
		try{
			rs = db.executeQuery(sql);
			if(rs != null) {
				while(rs.next()) {
					Map map = new HashMap();
					map.put("eventid", rs.getString("eventid"));
					map.put("report_man", rs.getString("report_man"));
					map.put("report_content", rs.getInt("report_content"));
					map.put("deal_time", rs.getInt("deal_time"));
					map.put("report_time", rs.getString("report_time"));
					map.put("report_count", rs.getInt("report_count"));
					list.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				db.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return list;
	}
}
