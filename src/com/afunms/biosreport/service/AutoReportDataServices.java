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

public class AutoReportDataServices {

	public int i=0;
	private static SimpleDateFormat sdf = new SimpleDateFormat(
		"yyyy-MM-dd HH:mm:ss");

	private static SysLogger logger = SysLogger.getLogger(AutoReportDataServices.class.getName());

//	
//	public void start(){
//		// 周时间
//		logger.info("皕杰综合报表 数据按周为周期产生开始运行... ... ");
//		Timer biosTimer = new Timer();
//		biosTimer.schedule(new biosReportTask(), 1000*10, 1000*20*2);
//	}
//	
//class biosReportTask extends java.util.TimerTask{
//
//   	 private AutoReportDataService service = new  AutoReportDataService();
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			service.insert();
//		}
//   }
	
	/**
	 * 判断当前是否适合采集 
	 */
	public void insert(){
		String times[] = getMonday(sdf.format(new Date()));
		String start = "2012-12-24 00:00:00";
		String end = "2012-12-30 23:59:59";
		
		
		insertNetDeviceData(start,end);
		insertWindowsServerData(start,end);
		insertAixServerData(start,end);
		insertDbOracleData(start,end);
//		insertLinkPortData(start,end);	// 插入链路统计数据
	}
	
	private String[] getMonday(String tmp){
		
		String[] times = new String[2];
		long day = 1000*60*60*24;
		
		String time = tmp.substring(0, 9);
		SimpleDateFormat lsdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(lsdf.parse(time));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		System.out.println(dayOfWeek);
		int value = 0;
		if(2 == dayOfWeek ){// 周一
			value = 0;
		}else if(1 == dayOfWeek ){// 周天
			value = 6;
		}else{
			value = dayOfWeek-2;
		}
		System.out.println("value" + value);
		Calendar newd = Calendar.getInstance();
		times[0] = lsdf.format(new Date(cal.getTimeInMillis()- (value+7) * day));
		times[1] = lsdf.format(new Date(cal.getTimeInMillis()-( value* day)));
		
		return times;
	}
	
	private boolean isRun(String time){
		if("".equals(time))
			return false;
		
		Date now = new Date();
		
		Date lastTime = new Date();
		long week = 1000*60*60*24*7;
		try {
			lastTime = sdf.parse(time);
		//	now = sdf.parse("2012-04-30 00:01:01");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(lastTime);
		Calendar nowCal = Calendar.getInstance();
		nowCal.setTime(now);
		long newTime = cal.getTimeInMillis()+week;
		long nowTime = nowCal.getTimeInMillis();
		boolean run = false;
		if((nowTime - newTime)<0){
			run = false;
		}else{
			run = true;
		}
		//boolean run = now.before(new Date(cal.getTimeInMillis()+week));
		
		System.out.println("预测时间 :" + newTime);
		System.out.println("now :" + nowTime);
		System.out.println("差值 :" + week);
		
		System.out.println("时间差 :" + (nowTime - newTime));
	
		return run;
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
		
//		WindowsServerReportDao dao = null;
//		try{
//			dao = new WindowsServerReportDao();
//			NetDeviceReportDao daoForTime = null;
//			try{
//				daoForTime = new NetDeviceReportDao();
//				if(!isRun(daoForTime.getLatestTime("system_bios_windows_server")))
//					return ;
//				
//			}catch(Exception e){
//				
//			}finally{
//				daoForTime.close();
//			}
//			
//			List<WindowsServerReportModel> list = dao.getDevice(start, end);
//			for(WindowsServerReportModel n : list){
//				WindowsServerReportDao netDao = null;
//				try{
//					netDao = new WindowsServerReportDao();
//					netDao.insertData(n);
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//				finally{
//					netDao.close();
//				}
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally{
//			dao.close();
//		}
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
		
//		AixServerReportDao dao = null;
//		try{
//			dao = new AixServerReportDao();
			
//			NetDeviceReportDao daoForTime = null;
//			try{
//				daoForTime = new NetDeviceReportDao();
//				if(!isRun(daoForTime.getLatestTime("system_bios_aix_server")))
//					return ;
//				
//			}catch(Exception e){
//				
//			}finally{
//				daoForTime.close();
//			}
//			
//			List<AixServerReportModel> list = dao.getDevice(start, end);
//			for(AixServerReportModel n : list){
//				AixServerReportDao netDao = null;
//				try{
//					netDao = new AixServerReportDao();
//					netDao.insertData(n);
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//				finally{
//					netDao.close();
//				}
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally{
//			dao.close();
//		}
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
	
}
