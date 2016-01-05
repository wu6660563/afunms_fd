package com.afunms.biosreport.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.afunms.biosreport.dao.AlarmDealAndReportDao;
import com.afunms.biosreport.model.AlarmDealAndReportModel;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.SysLogger;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.dao.EventReportDao;
import com.afunms.event.model.EventList;
import com.afunms.event.model.EventReport;

/**
 * <p>
 * 	告警信息处理及报告报表数据服务
 * </p>
 * 
 * <p>
 * 	Description: 
 * 			告警信息处理以及报告相关数据从system_eventlist 和 system_eventreport 两张表中获取
 * 			
 * 			获取数据后的处理思路：
 * 
 * 			1. 监测报告表中 
 * </p>
 * 
 * @author yag
 * @company 
 */
public class AlarmDealAndReportServices {

	private static SysLogger logger = SysLogger.getLogger(AlarmDealAndReportServices.class.getName());

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	public void runDealReport(String start,String end) {
		
		// 如果开始时间和结束时间相等，则只执行处理未
		if(start.equals(end)) {
			executeUndealAlarm();
		}
	}
	
	private boolean timeIsAvailable(String start,String end) {
		return false;
	}
	
	private Calendar stringToCalendar(String strTime) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf.parse(strTime));
		return calendar;
	}
	
	private void alarmReportEngine(String start,String end) {
		
		// 处理未处理完的告警
		executeUndealAlarm();
		// 处理新的告警
		execute(start,end);
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 */
	public void execute(String start, String end) {
		//executeUndealAlarm();
		System.out.println("告警信息处理及报告报表数据 -- 开始生成");
		List events = getEventListByCondition(start,end);
		System.out.println("events size=" + events.size());
		for(int i=0; i<events.size(); i++) {			
			dealEvent((Map)events.get(i));
		}
		System.out.println("告警信息处理及报告报表数据 -- 生成完毕");
	}
	
	public void dealEvent(Map map) {
		int managesign = (Integer)map.get("managesign");
		AlarmDealAndReportModel mo = new AlarmDealAndReportModel();
		if(managesign > 1) {
			System.out.println("deal...22222");
			// 告警已处理，根据evnetid号获取其报告信息
			System.out.println("eventid = "+(Integer)map.get("id")+"");
			List reportList = getReportListByEventID((Integer)map.get("id") + "");
			if(reportList.size() > 0) {
				EventReport report = (EventReport) reportList.get(0);
				// 时间转换
				Calendar calendar = Calendar.getInstance();
				calendar = report.getReport_time();
				mo.setReportTime(sdf.format(calendar.getTime()));
				mo.setReportContent(report.getReport_content());
				mo.setReportman(report.getReport_man());
				
				calendar = report.getDeal_time();
				mo.setManagetime(sdf.format(calendar.getTime()));
				mo.setReportCount((Integer)map.get("alarm_count"));
			}
		} else {
			mo.setReportCount(0);	// 告警未处理，其报告个数为0
			// 告警没处理
		}
		// 保存告警处理结果
		mo.setEventid((Integer)map.get("id"));
		mo.setAlarmCount((Integer)map.get("alarm_count"));
		mo.setBussinessid((String)map.get("businessid"));
		mo.setContext((String)map.get("content"));
		mo.setEnventlocation((String)map.get("eventlocation"));
		mo.setLevel1((Integer)map.get("level1"));
		mo.setManagesign((Integer)map.get("managesign"));
		mo.setNodeid((Integer)map.get("nodeid"));
		mo.setRecordtime((String)map.get("recordtime"));
		mo.setSubentity((String)map.get("subentity"));
		mo.setSubtype((String)map.get("subtype"));
		
		saveAlarmDealAndReportModel(mo);
	}
	
	/**
	 * 	处理未产生的告警信息
	 *  
	 */
	public void executeUndealAlarm() {
		List undealReports = getUnReportAlarm();
		AlarmDealAndReportModel tmpReport = null;
		for (int i=0; i<undealReports.size(); i++) {
			tmpReport = (AlarmDealAndReportModel)undealReports.get(i);
			//EventReport newReport = getEventReportByEventID(tmpReport.getEventid());
			updateUnreportInfo(tmpReport);
		}
	}
	
	/**
	 * 从当前的表中获取未处理的告警处理信息
	 * 
	 * @return List
	 */
	public List getUnReportAlarm() {
		// 查询所有的未被处理的告警的信息 ，然后在去 告警处理表中 查找其信息进行更新
		// 当前更新的时候 未被处理的 已经产生了的告警，还在继续告警的 怎么办？
		// 在插入前看该告警是否产生，
		List unReportList = new ArrayList();
		AlarmDealAndReportDao dealDao = new AlarmDealAndReportDao();
		try{
			unReportList = dealDao.findUndealReport();
			System.out.println("unreport size:" + unReportList.size());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dealDao.close();
		}
		
		return unReportList;
//		for(int i=0; i<unReportList.size(); i++) {
//			updateUnreportInfo((AlarmDealAndReportModel)unReportList.get(i));
//		}
	}
	
	
	
	public void updateUnreportInfo(AlarmDealAndReportModel alarmReport) {
		int eventid = alarmReport.getEventid();
		if(eventid > 0){
			EventReportDao dao = new EventReportDao();
			EventReport report = null;
			try {
				report = (EventReport) dao.findByEventId(eventid + "");
			} catch (Exception e) {
				
			} finally {
				dao.close();
			}
			
			if(report != null) {
				alarmReport.setReportContent(report.getReport_content());
				alarmReport.setReportCount(alarmReport.getAlarmCount());
				alarmReport.setReportman(report.getReport_man());
				alarmReport.setReportTime(sdf.format(report.getReport_time().getTime()));
				
				AlarmDealAndReportDao dealDao = new AlarmDealAndReportDao();
				
				try {
					dealDao.updateReportInfo(alarmReport);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					dealDao.close();
				}
			}
		}
	}
	
	private EventReport getEventReportByEventID(int eventid) {
		EventReportDao dao = new EventReportDao();
		EventReport report = null;
		try {
			report = (EventReport) dao.findByEventId(eventid + "");
		} catch (Exception e) {
			
		} finally {
			dao.close();
		}
		
		return report;
	}
	
	/**
	 * 保存告警及报告信息
	 * 
	 * @param m
	 */
	public void saveAlarmDealAndReportModel(AlarmDealAndReportModel m){	
		AlarmDealAndReportDao dealDao = new AlarmDealAndReportDao();
		try{
			dealDao.save(m);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dealDao.close();
		}
	}
	
	/**
	 * 根据时间查询告警记录信息
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	private List getEventListByCondition(String start, String end) {
		String timeCondition = " where recordtime >='" + start 
							+ "' and recordtime <='" + end + "' ";
		return getEventListByCondition(timeCondition);
	}
	
	/**
	 * 根据条件查询告警信息
	 * 
	 * 应该再获取一个最大的告警时间与当前传入的end比较判断认定当前告警是否仍在继续告警。
	 * 
	 * @param condition
	 * @return
	 */
	private List getEventListByCondition(String condition) {
		
		List<Map> list = new ArrayList<Map>();
		DBManager db = new DBManager();
		ResultSet rs = null;
		
		String sql = "SELECT s.id,s.eventlocation,s.content,s.level1,s.managesign,s.recordtime,"
			+ " s.nodeid,s.businessid,s.subtype,s.managetime,s.subentity,COUNT(s.id) AS idsum,"
			+ " max(s.recordtime) as latest_time "
			+ " FROM system_eventlist AS s"
			+ condition
			+ " GROUP BY s.nodeid,s.subtype,s.subentity,s.level1,s.managesign";
		System.out.println("sql");
		try{
			rs = db.executeQuery(sql);
			if(rs != null) {
				while(rs.next()) {
					Map map = new HashMap();
					map.put("id", rs.getInt("id"));
					map.put("eventlocation", rs.getString("eventlocation"));
					map.put("content", rs.getString("content"));
					map.put("level1", rs.getInt("level1"));
					map.put("managesign", rs.getInt("managesign"));
					map.put("recordtime", rs.getString("recordtime"));
					map.put("nodeid", rs.getInt("nodeid"));
					map.put("businessid", rs.getString("businessid"));
					map.put("subtype", rs.getString("subtype"));
					map.put("managetime",rs.getString("managetime"));
					map.put("subentity", rs.getString("subentity"));
					map.put("alarm_count", rs.getInt("idsum"));
					map.put("latest_time", rs.getString("latest_time"));
					list.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null){
					rs.close();
				}
				if(db != null) {
					db.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return list;
	}
	
	public void testMap(Map map) {
		System.out.println("eventlocation" + map.get("eventlocation"));
		System.out.println("content" + map.get("content"));
		System.out.println("level1" + map.get("level1"));
		System.out.println("managesign" + map.get("managesign"));
		System.out.println("recordtime" + map.get("recordtime"));
		System.out.println("nodeid" + map.get("nodeid"));
		System.out.println("businessid" + map.get("businessid"));
		System.out.println("subtype" + map.get("subtype"));
		System.out.println("managetime" + map.get("managetime"));
		System.out.println("subentity" + map.get("subentity"));
		System.out.println("alarm_count" + map.get("alarm_count"));

	}
	
	/**
	 *  EventListDao获取告警信息
	 */
	
	private List getReportListByCondition(String condition) {
		
		List list = new ArrayList();
		EventReportDao eventReportDao = new EventReportDao();
		try{
			list = eventReportDao.findByCondition(condition);
		} catch (Exception e) {
			logger.info(this.getClass().getName() 
					+ "整理告警状态处理报告时获取告警信息数据出错"+e.getMessage());
		} finally {
			eventReportDao.close();
		}
		return list;
	}
	
	/**
	 *  EventListDao获取告警信息
	 */
	
	private List getReportListByEventID(String eventid) {
		String eventidCondition = " where eventid=" + eventid;
		return getReportListByCondition(eventidCondition);
	}
	
	public static void main(String[] args) {
		AlarmDealAndReportServices services = new AlarmDealAndReportServices();
		services.execute("2012-10-15 00:00:00","2012-10-28 23:59:59");
		//services.executeUndealAlarm();
	}
}
