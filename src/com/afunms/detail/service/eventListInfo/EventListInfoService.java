package com.afunms.detail.service.eventListInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.afunms.event.dao.EventListDao;
import com.afunms.event.model.EventList;

public class EventListInfoService {
	
	private String type;
	
	private String subtype;
	
	private String nodeid;
	

	/**
	 * @param type
	 * @param subtype
	 * @param nodeid
	 */
	public EventListInfoService(String nodeid, String type, String subtype) {
		this.nodeid = nodeid;
		this.type = type;
		this.subtype = subtype;
	}
	
	/**
	 * ��ȡ�澯��Ϣ
	 * @param startdate		��ʼ����
	 * @param todate		��������
	 * @param level1		�澯�ȼ�
	 * @param status		����״̬
	 * @return
	 */
	public List<EventList> getEventListInfo(String startdate, String todate, String level1, String eventlocation, String subentity, String status){
		return getAlarmSQLCondition(startdate, todate, level1, eventlocation, subentity, status);
	}
	
	/**
	 * ��ȡ�澯ͳ����Ϣ
	 * @param startdate		��ʼ����
	 * @param todate		��������
	 * @param level1		�澯�ȼ�
	 * @param status		����״̬
	 * @return
	 */
	public List<Object> getCurrSummaryEventListInfo(String startdate, String todate, String level1, String status){
		return getSummaryEventListInfo(startdate, todate, level1, status);
	}
	 
	/**
	 * ��ȡ�澯��Ϣ
	 * @param startdate		��ʼ����
	 * @param todate		��������
	 * @param level1		�澯�ȼ�
	 * @param status		����״̬
	 * @return
	 */
	private List<Object> getSummaryEventListInfo(String startdate, String todate, String level1, String status){
		String startTime = "";		// ��ʼʱ��
		String toTime = "";			// ����ʱ��
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if(startdate == null || "".equals(startdate.trim())){
			startdate = simpleDateFormat.format(date);
		}
		startTime = startdate + " 00:00:00";
		if(todate == null || "".equals(todate.trim())){
			todate = simpleDateFormat.format(date);
		}
		toTime = todate + " 23:59:59";
		List<Object> eventList = null;
		EventListDao eventListDao = new EventListDao();
		try {
			eventList = eventListDao.getSummary(startTime, toTime, nodeid, type, level1, null, status);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			eventListDao.close();
		}
		return eventList;
	}
	
	
	/**
	 * ��ȡ�澯��Ϣ
	 * @param startdate		��ʼ����
	 * @param todate		��������
	 * @param level1		�澯�ȼ�
	 * @param status		����״̬
	 * @return
	 */
	private List<EventList> getAlarmSQLCondition(String startdate, String todate, String level1, String eventlocation, String subentity, String status){
		String startTime = "";		// ��ʼʱ��
		String toTime = "";			// ����ʱ��
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if(startdate == null || "".equals(startdate.trim())){
			startdate = simpleDateFormat.format(date);
		}
		startTime = startdate + " 00:00:00";
		if(todate == null || "".equals(todate.trim())){
			todate = simpleDateFormat.format(date);
		}
		toTime = todate + " 23:59:59";
		List<EventList> eventList = null;
		EventListDao eventListDao = new EventListDao();
		try {
			eventList = eventListDao.getEventList(startTime, toTime, nodeid, type, level1, eventlocation, subentity, null, status);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			eventListDao.close();
		}
		return eventList;
	}
	
	



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
