package com.gatherdb;


import java.util.TimerTask;
import org.apache.log4j.Logger;
import com.gatherdb.GathersqlListManager;






/**
 * 
 * 
 * ��ʱ�Ѷ���������ݲ������ݿ�
 * @author Administrator
 *
 */
public class GathersqlRun extends TimerTask{
	
 
	public  Logger logger = Logger.getLogger(GathersqlRun.class);
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//System.out.println("===######=====��ʼ��ʱ�������=="+GathersqlListManager.queue.size());
//		if(GathersqlListManager.queue.size()>0)
//		{
//		 DBManager pollmg = new DBManager();// ���ݿ�������
//		 pollmg.excuteBatchSql(GathersqlListManager.queue);
//		 pollmg.close();
//		}
		
//		if(!GathersqlListManager.idbstatus)
//		{
//		   //System.out.println(GathersqlListManager.idbstatus);
//		   GathersqlListManager.Addsql("DHCC-DB");
//		}
//		//System.out.println("=idbstatus=="+GathersqlListManager.idbstatus);
//		
//		//System.out.println("===######=====���ݶ�ʱ������==");
	}

}
