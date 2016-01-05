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
 * 	�澯��Ϣ�������汨�����ݷ���
 * </p>
 * 
 * <p>
 * 	Description: 
 * 			�澯��Ϣ�����Լ�����������ݴ�system_eventlist �� system_eventreport ���ű��л�ȡ
 * 			
 * 			��ȡ���ݺ�Ĵ���˼·��
 * 
 * 			1. ��ⱨ����� 
 * </p>
 * 
 * @author yag
 * @company 
 */
public class AlarmDealAndReportServices {

	private static SysLogger logger = SysLogger.getLogger(AlarmDealAndReportServices.class.getName());

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	public void runDealReport(String start,String end) {
		
		// �����ʼʱ��ͽ���ʱ����ȣ���ִֻ�д���δ
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
		
		// ����δ������ĸ澯
		executeUndealAlarm();
		// �����µĸ澯
		execute(start,end);
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 */
	public void execute(String start, String end) {
		//executeUndealAlarm();
		System.out.println("�澯��Ϣ�������汨������ -- ��ʼ����");
		List events = getEventListByCondition(start,end);
		System.out.println("events size=" + events.size());
		for(int i=0; i<events.size(); i++) {			
			dealEvent((Map)events.get(i));
		}
		System.out.println("�澯��Ϣ�������汨������ -- �������");
	}
	
	public void dealEvent(Map map) {
		int managesign = (Integer)map.get("managesign");
		AlarmDealAndReportModel mo = new AlarmDealAndReportModel();
		if(managesign > 1) {
			System.out.println("deal...22222");
			// �澯�Ѵ�������evnetid�Ż�ȡ�䱨����Ϣ
			System.out.println("eventid = "+(Integer)map.get("id")+"");
			List reportList = getReportListByEventID((Integer)map.get("id") + "");
			if(reportList.size() > 0) {
				EventReport report = (EventReport) reportList.get(0);
				// ʱ��ת��
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
			mo.setReportCount(0);	// �澯δ�����䱨�����Ϊ0
			// �澯û����
		}
		// ����澯������
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
	 * 	����δ�����ĸ澯��Ϣ
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
	 * �ӵ�ǰ�ı��л�ȡδ����ĸ澯������Ϣ
	 * 
	 * @return List
	 */
	public List getUnReportAlarm() {
		// ��ѯ���е�δ������ĸ澯����Ϣ ��Ȼ����ȥ �澯������� ��������Ϣ���и���
		// ��ǰ���µ�ʱ�� δ������� �Ѿ������˵ĸ澯�����ڼ����澯�� ��ô�죿
		// �ڲ���ǰ���ø澯�Ƿ������
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
	 * ����澯��������Ϣ
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
	 * ����ʱ���ѯ�澯��¼��Ϣ
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
	 * ����������ѯ�澯��Ϣ
	 * 
	 * Ӧ���ٻ�ȡһ�����ĸ澯ʱ���뵱ǰ�����end�Ƚ��ж��϶���ǰ�澯�Ƿ����ڼ����澯��
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
	 *  EventListDao��ȡ�澯��Ϣ
	 */
	
	private List getReportListByCondition(String condition) {
		
		List list = new ArrayList();
		EventReportDao eventReportDao = new EventReportDao();
		try{
			list = eventReportDao.findByCondition(condition);
		} catch (Exception e) {
			logger.info(this.getClass().getName() 
					+ "����澯״̬������ʱ��ȡ�澯��Ϣ���ݳ���"+e.getMessage());
		} finally {
			eventReportDao.close();
		}
		return list;
	}
	
	/**
	 *  EventListDao��ȡ�澯��Ϣ
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
