package com.afunms.biosreport.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import com.afunms.biosreport.dao.AixServerReportDao;
import com.afunms.biosreport.dao.DbOracleReportDao;
import com.afunms.biosreport.dao.DeviceLinkPortReportDao;
import com.afunms.biosreport.dao.NetDeviceReportDao;
import com.afunms.biosreport.dao.WindowsServerReportDao;
import com.afunms.biosreport.model.AixServerReportModel;
import com.afunms.biosreport.model.DbOracleReportModel;
import com.afunms.biosreport.model.NetDeviceReport;
import com.afunms.biosreport.model.WindowsServerReportModel;
import com.afunms.common.util.SysLogger;

public class AutoReportDataService {


	private static SimpleDateFormat sdf = new SimpleDateFormat(
		"yyyy-MM-dd HH:mm:ss");

	private static SysLogger logger = SysLogger.getLogger(AutoReportDataService.class.getName());

	private static AutoReportDataService instance = null;
	public static AutoReportDataService getInstance() {
		if(null == instance) {
			instance = new AutoReportDataService();
		}
		return instance;
	}
	
	private AutoReportDataService(){
	}
	
	public void start(){
		// ��ʱ��
		logger.info("�z���ۺϱ��� ���ݰ���Ϊ���ڲ�����ʼ����,2�ֺ�ʼ���У���2Сʱ... ... ");
		Timer biosTimer = new Timer();
		biosTimer.schedule(new biosReportTask(), 1000*60*2, 1000*60*60*2);
	}
	
class biosReportTask extends java.util.TimerTask{

   private AutoReportDataService service = new  AutoReportDataService();
		@Override
		public void run() {
			service.executes();
		}
   }

	private void executes() {
		// �����е����ʱ��
		String maxTimeInReport = getMaxTime();
		Date nowDate = new Date();
		
		logger.info("�z�ܱ���������ʱ�䣺" + maxTimeInReport);
		Date maxInReport;
		try {
			maxInReport = sdf.parse(maxTimeInReport);
		} catch (ParseException e1) {
			logger.error("����ת��ʧ��", e1);
			return;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(maxInReport);
		cal.add(Calendar.HOUR_OF_DAY, 13);

		logger.info("��ǰʱ�䣺" + sdf.format(nowDate));
		
		int days = this.getIntervalDays(nowDate, cal.getTime());
		
		if (days < 10) {
			logger.info("�z���ۺϱ��� ��ǰ����������.");
			return;
		}
		
		String start = "";
		String end = "";
		start = getDayByInterval(maxInReport,4);
		end = getDayByInterval(maxInReport,10);
		logger.info("start:" + start +",end:" + end);
		
		insert(start,end);
	}
	
	/**
	 * ����������� 
	 * 
	 * @param startDay
	 * @param endDay
	 * @return �����ʼʱ��Ƚ���ʱ��٣��򷵻ظ����������
	 */
	private int getIntervalDays(Date startDay, Date endDay) {		
		// ���ݺ�����������������
		return (int)((startDay.getTime() - endDay.getTime()) / (1000 * 60 * 60 * 24));
	}
	
	/**
	 * �жϵ�ǰ�Ƿ��ʺϲɼ� 
	 */
	private  void insert(String start,String end){
		if(null == start || start.trim().length() == 0
				|| null == end || end.trim().length() == 0){
			return ;
		}
		
		start += " 00:00:00";
		end += " 23:59:59";
		
		insertNetDeviceData(start,end);
		insertWindowsServerData(start,end);
		insertAixServerData(start,end);
		insertDbOracleData(start,end);
	}
	
	private String getMaxTime() {
		NetDeviceReportDao dao = new NetDeviceReportDao();
		String maxTime = "";
		try {
			maxTime = dao.getLatestTime("system_bios_net");
		} catch (RuntimeException e) {
			logger.info("getMaxTime����" ,e);
		} finally {
			dao.close();
		}
		return maxTime;
	}

	
	
	/**
	 * 
	 * @param start
	 * @param end
	 */
	public void insertNetDeviceData(String start, String end){
		NetDeviceReportDao dao = null;
		List<NetDeviceReport> list  = null;
		try{
			dao = new NetDeviceReportDao();
			list = dao.getDevice(start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		
		for(NetDeviceReport n : list){
			NetDeviceReportDao netDao = null;
			try{
				netDao = new NetDeviceReportDao();
				netDao.insertData(n);
			}catch(Exception e){
				e.printStackTrace();
			}
			finally{
				netDao.close();
			}
		}
	}
	/**
	 * ����windows-server����
	 * @param start
	 * @param end
	 */
	public void insertWindowsServerData(String start, String end){	
		WindowsServerReportDao dao = null;
		List<WindowsServerReportModel> list  = null;
		try{
			dao = new WindowsServerReportDao();
			list = dao.getDevice(start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		
		for(WindowsServerReportModel n : list){
			WindowsServerReportDao netDao = null;
			try{
				netDao = new WindowsServerReportDao();
				netDao.insertData(n);
			}catch(Exception e){
				e.printStackTrace();
			}
			finally{
				netDao.close();
			}
		}
	}
	
	/**
	 * ����aix-server����
	 * @param start
	 * @param end
	 */
	public void insertAixServerData(String start, String end){	
		AixServerReportDao dao = null;
		List<AixServerReportModel> list  = null;
		try{
			dao = new AixServerReportDao();
			list = dao.getDevice(start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		
		for(AixServerReportModel n : list){
			AixServerReportDao netDao = null;
			try{
				netDao = new AixServerReportDao();
				netDao.insertData(n);
			}catch(Exception e){
				e.printStackTrace();
			}
			finally{
				netDao.close();
			}
		}
	}
	
	/**
	 * ����db_oracle����
	 * @param start
	 * @param end
	 */
	public void insertDbOracleData(String start, String end){
		
		DbOracleReportDao dao = null;
		List<DbOracleReportModel> list  = null;
		try{
			dao = new DbOracleReportDao();
			list = dao.getDevice(start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		
		for(DbOracleReportModel n : list){
			DbOracleReportDao netDao = null;
			try{
				netDao = new DbOracleReportDao();
				netDao.insertData(n);
			}catch(Exception e){
				e.printStackTrace();
			}
			finally{
				netDao.close();
			}
		}
	}
		/**
		 * ����link device����
		 * @param start
		 * @param end
		 * add 2012-09-26
		 */
		public void insertLinkPortData(String start, String end){
			DeviceLinkPortReportDao dao = new DeviceLinkPortReportDao();
	        try{
	        	dao.execute(start, end);
	        } catch (Exception e){
	        	
	        } finally {
	        	dao.close();
	        }		
		}
		/**
		 * ��ȡ����ʱ�����
		 * @param time ʱ���ʽΪyyyy-MM-dd
		 * @return
		 * @throws ParseException
		 */
		public static int dayForWeek(String time) throws ParseException {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
			Calendar c = Calendar.getInstance();  
			c.setTime(format.parse(time));  
			int dayForWeek = 0;  
			if(c.get(Calendar.DAY_OF_WEEK) == 1){  
			  dayForWeek = 7;  
			}else{  
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;  
			}  
			return dayForWeek; 

		}

		/**
		 * ����ʱ���� ��ȡ���뵱ǰʱ���ʱ��
		 * @param interal
		 * @return
		 */
		public static String getDayBy(int interal) {
			 Calendar cal = Calendar.getInstance();
	         //  ���ڵ�DATE��ȥ10  ����������10 �� ͬ�� +10 ����������ʮ��
	            cal.add(Calendar.DATE, interal); 
	            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd"); 

	            String str = sf.format(cal.getTime()); 
	 
	            return str; 
		}
		
		
		public static String getDayByInterval(Date date, int interal) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			//  ���ڵ�DATE��ȥ10  ����������10 �� ͬ�� +10 ����������ʮ��
			cal.add(Calendar.DATE, interal); 
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd"); 
			
			String str = sf.format(cal.getTime()); 
			
			return str; 
		}
		
		
		
		public static void main(String[] args) throws ParseException {
			AutoReportDataService auto = new AutoReportDataService();
			Date now = new Date();
			Date end = sdf.parse("2013-01-28 00:10:10");
			Date start = sdf.parse("2013-01-16 11:59:59");
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(start);
			calendar.add(Calendar.HOUR_OF_DAY, 13);
			start = calendar.getTime();
			logger.info(sdf.format(start));
			int interval = auto.getIntervalDays(end, start);
		//	String interval = auto.getDayByInterval(start, 12);
		//	String interval = auto.getDayByInterval(start, 5);
			logger.info(interval);		
		
			auto.executes();
		}
}
