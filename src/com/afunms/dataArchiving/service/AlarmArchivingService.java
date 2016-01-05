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
 * �澯��Ϣѹ���鵵����ҵ����
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
    	logger.info("�澯���ݿ�ʼ�鵵��1���Ӻ�ʼִ�У�ÿ��1Сʱִ��һ�� ......");
    	Timer archiveTimer = new Timer();
    	// 10���ִ�У����3����
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
    *  �澯�鵵���ķ���
    *  ���ݹ鵵���ͣ�Сʱ���죩��Ȼ����㵱ǰ�Ƿ���Խ��й鵵��
    *  �ٽ���ɾ���ж�
    */
   private void alarmArchive(){
    	AlarmArchivingKindDao kindDao = new AlarmArchivingKindDao();
    	try{
    		// ��ȡ�鵵��������
    		List<AlarmArchivingKind> kindList = kindDao.getArchivingKind();
    		if(!(kindList.size()>0)){
    			logger.info("�澯���ݹ鵵���ͱ�����Ϊ�� �� ��ǰû�л�ȡ���澯�������ݣ�����");
    			return ;
    		}
    		Calendar latestTime = Calendar.getInstance();
    		/**
    		 * �Ը澯���ͽ��б��� �鵵��
    		 */
    		for(AlarmArchivingKind kind : kindList){
    				int i=0;
    				while(fatherDataIsOK(kind)){
    						kindDao.archiveDataByKind(kind);
    				}
    			
    			/////////    ɾ������---BEGIN  ////////////
    			//kindDao.deleteDataByKind(kind);
    		}
    	}catch(Exception e){
    		logger.error("�澯���ݹ鵵����" );
    		e.printStackTrace();
    	}finally{
    		kindDao.close();
    	}
    }
//    public  void alarmArchiving(){
//	
//    	// �����ݿ��ж�ȡ��ʼץȡ��ʱ��
//    	String startDate = "";// �ٶ������ݿ��й鵵ʱ����ȡ���µ�
//    	// ����ʱ��+1 > ��ǰʱ�䣬�򲻹鵵
//    	if(!(startDate == null)){
//    		
//    	}else{
//    		// �����ݿ��ж�ȡ�����µ�ʱ��...���߰���Ĭ�ϵĿ�ʼִ��
//    	}
//    //	List<AlarmArchivingKind> archivingKindList = archivingDao.getArchivingKind();
//    	if(!(archivingKindList.size() >0)){
//    		return ;
//    	}
//    	Calendar latestTime = Calendar.getInstance();
//    	Calendar fatherLatestTime = Calendar.getInstance();
//    	for(AlarmArchivingKind kind : archivingKindList){
//    		if("HOUR".equals(kind.getType())){
//    			// ����Сʱ�鵵
//    			latestTime = (Calendar)archivingDao.getLatestTime(kind.getId());
//    			if(latestTime != null){
//    				// ��ʼ������ǰ�Ĺ鵵�Ƿ���father_id , ����У��������ʱ������ӹ鵵ʱ���ж�
//    				Date now = new Date();
//    				
//    				if(now.getTime() - latestTime.getTime().getTime() > intervalHour){
//    					// ����hour����Ĭ��Ϊ��ǰ��С�澯�鵵��λ�������ǰʱ����鵵ʱ������1Сʱ��
//    					// ����й鵵
//    					if(kind.getFatherId()>0){
//    						// ��ȡԴ���ݵ����¹鵵ʱ��
//    						fatherLatestTime = archivingDao.getLatestTime(kind.getFatherId());
//    						/**
//    						 *  �жϵ�ǰ�鵵�����Դ�����Ƿ��Ѿ��鵵����,Դ���ݵĵ�ǰ�鵵ʱ�� > ��ǰ�鵵���ϴι鵵ʱ��
//    						 *  + ����鵵��������ܹ����й鵵������
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
     * �����Ƿ��ܹ����й鵵
     * 
     * ���ݵ�ǰʱ�������¹鵵ʱ��ļ���뵱ǰ�鵵���͵Ĺ鵵ʱ�������бȽ�
     * 	
     * @param kind
     * @return true(����鵵)
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
    		
    		// ��ǰ��߼���Ĺ鵵��������ɾ�������Ӽ��鵵������
    		if(kind.getChildrenId() == kind.getId()){
    			return true;
    		}
    		// ��ȡ�Ӽ��Ĺ鵵ʱ�� ��ǰʱ��-�Ӽ����ʱ��<��ǰʱ��-����ʱ��
    		// ɾ��hour: day  ����day��Ҫ00-��ǰʱ�� ��Ҫ�����������ϴ�+��� 
    		// ɾ�������䣺��ǰ-����֮ǰ�ġ�
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
     * �жϵ�ǰ�鵵��Դ�����Ƿ��Ѿ��鵵��
     * 
     * @param kind
     * @return true(�ѹ鵵�ã�
     */
    private boolean fatherDataIsOK(AlarmArchivingKind kind){
    	
    	if(kind.getLatestArchiveTime() == null){
    		return false;
    	}
    	
    	logger.info("-------------�жϵ�ǰ�鵵�����Ƿ�鵵��  "+kind.getName());
    	if(isArchivedNow(kind)){
  //  		System.out.println("////// isArchivedNow ����true");
    		if(kind.getId() == kind.getFatherId()){
    			// ��ʾ��ǰ�鵵���͵�Դ�����Ѿ�����
    			return true;
    		}else{
    			Calendar fatherLatestTime = Calendar.getInstance();
    			AlarmArchivingKindDao kindDao = new AlarmArchivingKindDao();
    			try{
    				fatherLatestTime = kindDao.getLatestTime(kind.getFatherId());
    				/**
    				 * ���Ҫ�鵵�����һ������ݣ���������1:03 + 1��ļ�� < Сʱ������10:50
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
     * �ж��Ƿ��ܹ�ɾ������
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
     * �鵵�������ɾ��
     * 
     * @param kind
     */
    private void delete(AlarmArchivingKind kind){
    	// 1. ����Դ���ݲ���ʱ��ʹ���ʱ��ȽϽ���ɾ�� 
    	// 		kind�д��һ��Դ�������ʱ���¼
    	// 2. �ж�ɾ�������Ƿ񻹱��¼��鵵������
    	// long + retentionTime < �鵵ʱ�� 
    	// �¼��鵵ʱ��-interval  long--- --------*}----�¼��鵵ʱ��
    	// �¼��鵵ʱ�� - retentionTime ��Ϊɾ�����ݵ�����ʱ��
    	/// ���father_id == id ��Ϊ��ߵȼ��Ĺ鵵����Ҫɾ������Դ����
    	// ��������Ĺ鵵�Ƿ���ɣ�kind.	last_archive_time-�ɼ���� 
    	Date now = new Date();
    	Date longest = new Date();
    	Date nextChildArchivingTime = new Date();// �¼��鵵ʱ��
    	if(now.getTime() - longest.getTime() > Long.parseLong(kind.getRetentionTime())
    			){
    		
    	}
    	
    	// ����ԭʼ�����ݽ���ɾ��
    	if(kind.getId() == kind.getFatherId()){
    		// �ϴι鵵ʱ�� - �ɼ���� > ��ǰʱ�� - Դ���ݵĴ���ʱ��
    		// �򰴵�ǰʱ�� - Դ���ݵĴ���ʱ�� ��Ϊɾ�����ݵ����ʱ��
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
//        				System.out.println("��ʼ�鵵");
//        				service.alarmArchive();
//        			}else{
//        				System.out.println("��ǰû���鵵ʱ��"+kind.getType());
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
