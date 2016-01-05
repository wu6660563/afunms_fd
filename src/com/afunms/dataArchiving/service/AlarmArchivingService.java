package com.afunms.dataArchiving.service;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import com.afunms.common.util.SysLogger;
import com.afunms.dataArchiving.dao.AlarmArchivingKindDao;
import com.afunms.dataArchiving.model.AlarmArchivingKind;
import com.afunms.event.dao.CheckEventDao;

/**
 * 告警信息压缩归档处理业务类
 * 
 * @author Administrator
 *
 */
public class AlarmArchivingService {

    private static SimpleDateFormat sdf = new SimpleDateFormat(
		"yyyy-MM-dd HH:mm:ss");

    private static SysLogger logger = 
    	SysLogger.getLogger(AlarmArchivingService.class.getName());

    private static AlarmArchivingService instance = null;
    
    private AlarmArchivingService() {
    }
    
    public static AlarmArchivingService getInstance() {
    	if(null == instance) {
    		instance = new AlarmArchivingService();
    	}
    	return instance;
    }
    
    
    public  void start(){
    	logger.info("告警数据开始归档，1分钟后开始执行，每过1小时执行一次 ......");
    	Timer archiveTimer = new Timer();
    	// 10秒后执行，间隔3分钟
    	archiveTimer.schedule(new archivingTask(), 1000*60,1000*60*60);
    }
    
     class archivingTask extends java.util.TimerTask{

    	 private AlarmArchivingService service = new  AlarmArchivingService();
		@Override
		public void run() {
			// TODO Auto-generated method stub
			service.alarmArchive();
		}
    }
    
   /**
    *  告警归档核心方法
    *  根据归档类型（小时、天），然后计算当前是否可以进行归档，
    *  再进行删除判断
    */
   private void alarmArchive(){
    	AlarmArchivingKindDao kindDao = new AlarmArchivingKindDao();
    	try{
    		// 获取归档类型数据
    		List<AlarmArchivingKind> kindList = kindDao.getArchivingKind();
    		if(!(kindList.size()>0)){
    			logger.info("告警数据归档类型表数据为空 或 当前没有获取到告警类型数据！！！");
    			return ;
    		}
    		Calendar latestTime = Calendar.getInstance();
    		/**
    		 * 对告警类型进行遍历 归档。
    		 */
    		for(AlarmArchivingKind kind : kindList){
    				int i=0;
    				while(fatherDataIsOK(kind)){
    						kindDao.archiveDataByKind(kind);
    				}
    			
    			/////////    删除数据---BEGIN  ////////////
    			//kindDao.deleteDataByKind(kind);
    		}
    	}catch(Exception e){
    		logger.error("告警数据归档错误！" );
    		e.printStackTrace();
    	}finally{
    		kindDao.close();
    	}
    }
//    public  void alarmArchiving(){
//	
//    	// 从数据库中读取开始抓取的时间
//    	String startDate = "";// 假定从数据库中归档时间表读取最新的
//    	// 最新时间+1 > 当前时间，则不归档
//    	if(!(startDate == null)){
//    		
//    	}else{
//    		// 从数据库中读取次最新的时间...或者按照默认的开始执行
//    	}
//    //	List<AlarmArchivingKind> archivingKindList = archivingDao.getArchivingKind();
//    	if(!(archivingKindList.size() >0)){
//    		return ;
//    	}
//    	Calendar latestTime = Calendar.getInstance();
//    	Calendar fatherLatestTime = Calendar.getInstance();
//    	for(AlarmArchivingKind kind : archivingKindList){
//    		if("HOUR".equals(kind.getType())){
//    			// 按照小时归档
//    			latestTime = (Calendar)archivingDao.getLatestTime(kind.getId());
//    			if(latestTime != null){
//    				// 开始检索当前的归档是否有father_id , 如果有，则在最近时间中添加归档时间判断
//    				Date now = new Date();
//    				
//    				if(now.getTime() - latestTime.getTime().getTime() > intervalHour){
//    					// 对于hour，先默认为当前最小告警归档单位，如果当前时间与归档时间差大于1小时，
//    					// 则进行归档
//    					if(kind.getFatherId()>0){
//    						// 获取源数据的最新归档时间
//    						fatherLatestTime = archivingDao.getLatestTime(kind.getFatherId());
//    						/**
//    						 *  判断当前归档所需的源数据是否已经归档产生,源数据的当前归档时间 > 当前归档的上次归档时间
//    						 *  + 上其归档间隔，则能够进行归档操作。
//    						 */
//    						
//    					}
//    					String startTime = sdf.format(latestTime);
//    					String endTime = sdf.format(latestTime.getTime().getTime()+intervalHour);
//    					try {
//							archivingDao.archivingByHour(startTime,endTime);
//						} catch (SQLException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//    				}
//    			}
//    		}
//    		
//    	}
//    	
//    }
//    
    /**
     * 现在是否能够进行归档
     * 
     * 根据当前时间与最新归档时间的间隔与当前归档类型的归档时间间隔进行比较
     * 	
     * @param kind
     * @return true(允许归档)
     */
    private boolean isArchivedNow(AlarmArchivingKind kind){
    	long result = new Date().getTime() - kind.getLatestArchiveTime().getTime().getTime() 
    					- Long.parseLong(kind.getArchiveInterval());
    	
    	if(new Date().getTime() - kind.getLatestArchiveTime().getTime().getTime() 
    			> Long.parseLong(kind.getArchiveInterval())){
    		return true;
    	}
    	return false;
    }
    private boolean isDeleteNow(AlarmArchivingKind kind){
    	if(kind.getLatestArchiveTime().getTime().getTime() > new Date().getTime()
    			- Long.parseLong(kind.getRetentionTime())){
    		
    		// 当前最高级别的归档，其数据删除不受子级归档的依赖
    		if(kind.getChildrenId() == kind.getId()){
    			return true;
    		}
    		// 获取子级的归档时间 当前时间-子级间隔时间<当前时间-存留时间
    		// 删除hour: day  今天day需要00-当前时间 需要的数据区域：上次+间隔 
    		// 删除的区间：当前-存留之前的。
    		AlarmArchivingKindDao kindDao = new AlarmArchivingKindDao();
    		try{
    			Calendar fatherLatestTime = kindDao.getLatestTime(kind.getFatherId());
    			if(new Date().getTime() - Long.parseLong(kind.getRetentionTime()) 
    					< fatherLatestTime.getTime().getTime()){
    				return true;
    			}
    		}catch(Exception e){
    			e.printStackTrace();
    		}finally{
    			kindDao.close();
    		}
    	}
    	return false;
    }
    
    /**
     * 判断当前归档的源数据是否已经归档好
     * 
     * @param kind
     * @return true(已归档好）
     */
    private boolean fatherDataIsOK(AlarmArchivingKind kind){
    	
    	if(kind.getLatestArchiveTime() == null){
    		return false;
    	}
    	
    	logger.info("-------------判断当前归档类型是否归档好  "+kind.getName());
    	if(isArchivedNow(kind)){
  //  		System.out.println("////// isArchivedNow 返回true");
    		if(kind.getId() == kind.getFatherId()){
    			// 表示当前归档类型的源数据已经产生
    			return true;
    		}else{
    			Calendar fatherLatestTime = Calendar.getInstance();
    			AlarmArchivingKindDao kindDao = new AlarmArchivingKindDao();
    			try{
    				fatherLatestTime = kindDao.getLatestTime(kind.getFatherId());
    				/**
    				 * 如果要归档今天的一天的数据：昨天晚上1:03 + 1天的间隔 < 小时的最新10:50
    				 */
    				long result = kind.getLatestArchiveTime().getTime().getTime() + Long.parseLong(kind.getArchiveInterval()) 
					- fatherLatestTime.getTime().getTime();
    				
    //				System.out.println("latest + interval - father-latest = "+result);
    				if(kind.getLatestArchiveTime().getTime().getTime() + Long.parseLong(kind.getArchiveInterval()) 
    						< fatherLatestTime.getTime().getTime()){
    					return true;
    				}else{
    					return false;
    				}
    			}catch(Exception e){
    				e.printStackTrace();
    			}finally{
    				//kindDao.close();
    			}
    		}
    	}
    	return false;
    }
    
    /**
     * 判断是否能够删除数据
     * 
     * @param kind
     * @return
     */
    private boolean deleteData(AlarmArchivingKind kind){
    	if("HOUR"==kind.getType()){
    		
    	}
   		if("DAY"==kind.getType()){
   			
   		}
    	
   		return false;
    }
    
    
    /**
     * 归档后的数据删除
     * 
     * @param kind
     */
    private void delete(AlarmArchivingKind kind){
    	// 1. 根据源数据插入时间和存留时间比较进行删除 
    	// 		kind中存放一个源数据最久时间记录
    	// 2. 判断删除数据是否还被下级归档所依赖
    	// long + retentionTime < 归档时间 
    	// 下级归档时间-interval  long--- --------*}----下级归档时间
    	// 下级归档时间 - retentionTime 作为删除数据的最新时间
    	/// 如果father_id == id 则为最高等级的归档，需要删除整个源数据
    	// 看下自身的归档是否完成，kind.	last_archive_time-采集间隔 
    	Date now = new Date();
    	Date longest = new Date();
    	Date nextChildArchivingTime = new Date();// 下级归档时间
    	if(now.getTime() - longest.getTime() > Long.parseLong(kind.getRetentionTime())
    			){
    		
    	}
    	
    	// 对最原始的数据进行删除
    	if(kind.getId() == kind.getFatherId()){
    		// 上次归档时间 - 采集间隔 > 当前时间 - 源数据的存留时间
    		// 则按当前时间 - 源数据的存留时间 作为删除数据的最近时间
    	}
    	if(true){
    		
    	}
    }
    
        public static void main(String[] args){
//        	AlarmArchivingService service = new AlarmArchivingService();
//        	AlarmArchivingKindDao kinddao = new AlarmArchivingKindDao();
//        	try{
//        		List<AlarmArchivingKind> list = kinddao.getArchivingKind();
//        		for(AlarmArchivingKind kind : list){
//        			if(service.fatherDataIsOK(kind)){
//        				System.out.println("开始归档");
//        				service.alarmArchive();
//        			}else{
//        				System.out.println("当前没到归档时间"+kind.getType());
//        			}
//        		}
//        	}catch(Exception e ){
//        		
//        	}finally{
//        		kinddao.close();
//        	}
        	String now = sdf.format(new Date());
        	System.out.println("now : "+now);
        }
}
