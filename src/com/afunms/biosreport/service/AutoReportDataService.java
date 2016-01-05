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
		// 周时间
		logger.info("皕杰综合报表 数据按周为周期产生开始运行,2分后开始运行，间2小时... ... ");
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
		// 报表中的最近时间
		String maxTimeInReport = getMaxTime();
		Date nowDate = new Date();
		
		logger.info("皕杰报表中最新时间：" + maxTimeInReport);
		Date maxInReport;
		try {
			maxInReport = sdf.parse(maxTimeInReport);
		} catch (ParseException e1) {
			logger.error("日期转换失败", e1);
			return;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(maxInReport);
		cal.add(Calendar.HOUR_OF_DAY, 13);

		logger.info("当前时间：" + sdf.format(nowDate));
		
		int days = this.getIntervalDays(nowDate, cal.getTime());
		
		if (days < 10) {
			logger.info("皕杰综合报表 当前不生成数据.");
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
	 * 计算天数间隔 
	 * 
	 * @param startDay
	 * @param endDay
	 * @return 如果开始时间比结束时间迟，则返回负数间隔天数
	 */
	private int getIntervalDays(Date startDay, Date endDay) {		
		// 根据毫秒间隔数计算间隔天数
		return (int)((startDay.getTime() - endDay.getTime()) / (1000 * 60 * 60 * 24));
	}
	
	/**
	 * 判断当前是否适合采集 
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
			logger.info("getMaxTime出错" ,e);
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
	 * 插入windows-server数据
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
	 * 插入aix-server数据
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
	 * 插入db_oracle数据
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
		 * 插入link device数据
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
		 * 获取传入时间的周
		 * @param time 时间格式为yyyy-MM-dd
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
		 * 传入时间间隔 获取距离当前时间的时间
		 * @param interal
		 * @return
		 */
		public static String getDayBy(int interal) {
			 Calendar cal = Calendar.getInstance();
	         //  日期的DATE减去10  就是往后推10 天 同理 +10 就是往后推十天
	            cal.add(Calendar.DATE, interal); 
	            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd"); 

	            String str = sf.format(cal.getTime()); 
	 
	            return str; 
		}
		
		
		public static String getDayByInterval(Date date, int interal) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			//  日期的DATE减去10  就是往后推10 天 同理 +10 就是往后推十天
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
