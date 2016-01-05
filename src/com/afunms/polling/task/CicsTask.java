/*
 * Created on 2009-11-29
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.afunms.polling.task;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;

import com.afunms.application.dao.CicsConfigDao;
import com.afunms.application.model.CicsConfig;
import com.afunms.application.util.CicsHelper;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.event.dao.AlarmInfoDao;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.AlarmInfo;
import com.afunms.event.model.Smscontent;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Cics;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.polling.om.Task;


/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CicsTask extends MonitorTask {
	//private final static boolean  debug=false; 
	/**
	 * 
	 */
	public CicsTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		CicsConfigDao cicsConfigDao = new CicsConfigDao();
		try{
			List list=cicsConfigDao.getCicsByFlag(new Integer("1"));
    		int numThreads = 200;
    		try {
    			List numList = new ArrayList();
    			TaskXml taskxml = new TaskXml();
    			numList = taskxml.ListXml();
    			for (int i = 0; i < numList.size(); i++) {
    				Task task = new Task();
    				BeanUtils.copyProperties(task, numList.get(i));
    				if (task.getTaskname().equals("cicstask")){
    					numThreads = task.getPolltime().intValue();
    				}
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}

    		// �����̳߳�
    		ThreadPool threadPool = null;	
    		if(list != null && list.size()>0){
    			threadPool = new ThreadPool(list.size());	
        		// ��������    		
        		for (int i=0; i<list.size(); i++) {    			
            			threadPool.runTask(createTask((CicsConfig)list.get(i)));
        		}
        		// �ر��̳߳ز��ȴ������������
        		threadPool.join();
        		threadPool.close();
    		}
    		threadPool = null;

								
		}
		catch(Exception e){
			e.printStackTrace();
					
		}finally{
			cicsConfigDao.close();
			SysLogger.info("********CICS Thread Count : "+Thread.activeCount());
		}
		// TODO Auto-generated method stub
	}
	
    /**
    ��������
*/	
private static Runnable createTask(final CicsConfig cicsConfig) {
    return new Runnable() {
        public void run() {
        	CicsHelper cicsHelper=null;
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try { 
            	cicsHelper = new CicsHelper();
            	//����ʷ�澯���
            	Cics _cics = (Cics)PollingEngine.getInstance().getCicsByID(cicsConfig.getId());	
				if(_cics == null){
					return;
				}
				if(_cics != null){
					_cics.setStatus(1);
					_cics.setAlarm(false);
					_cics.getAlarmMessage().clear();
					Calendar _tempCal = Calendar.getInstance();				
					Date _cc = _tempCal.getTime();
					String _time = sdf.format(_cc);
					_cics.setLastTime(_time);
				}
				try{
					//�жϸ澯
					//System.out.println("=================�ж�CICS�澯");
				    if(cicsHelper.openGateway(_cics.getGateway())){//cics�������������ҿͻ��˼�����������
				    	cicsHelper.closeGateway();
					} else {
						//��Ҫ����CICS�������ڵķ������Ƿ�����ͨ
						Vector ipPingData = (Vector)ShareData.getPingdata().get(cicsConfig.getIpaddress());
						if(ipPingData != null){
							Pingcollectdata pingdata = (Pingcollectdata)ipPingData.get(0);
							String pingvalue = pingdata.getThevalue();
							if(pingvalue == null || pingvalue.trim().length()==0)pingvalue="0";
							double pvalue = new Double(pingvalue);
							if(pvalue == 0){
								//CICS���������Ӳ���***********************************************
								com.afunms.polling.node.Cics tnode=(com.afunms.polling.node.Cics)PollingEngine.getInstance().getCicsByIP(cicsConfig.getIpaddress());
								tnode.setAlarm(true);
								List alarmList = tnode.getAlarmMessage();
								if(alarmList == null)alarmList = new ArrayList();
								tnode.getAlarmMessage().add("CICS����ֹͣ,��Ϊ���ڵķ��������Ӳ���");
					              try{
					            	  SmscontentDao eventdao = new SmscontentDao();
					            	  String eventdesc = "CICS����("+tnode.getAlias()+" IP:"+cicsConfig.getIpaddress()+")"+"��CICS����ֹͣ";
					            	  eventdao.createEventWithReasion("poll",tnode.getId()+"",tnode.getAdminIp()+"("+tnode.getAdminIp()+")",eventdesc,3,"cics","ping","���ڵķ��������Ӳ���");
					            	  Pingcollectdata hostdata=null;
										hostdata=new Pingcollectdata();
										hostdata.setIpaddress(cicsConfig.getIpaddress());
										Calendar date=Calendar.getInstance();
										hostdata.setCollecttime(date);
										hostdata.setCategory("CicsPing");
										hostdata.setEntity("Utilization");
										hostdata.setSubentity("ConnectUtilization");
										hostdata.setRestype("dynamic");
										hostdata.setUnit("%");
										hostdata.setThevalue("0");	
										CicsConfigDao cicsConfigDao=new CicsConfigDao();
										try{
											cicsConfigDao.createHostData(hostdata);	        								
										}catch(Exception e){
											e.printStackTrace();
										}finally{
											cicsConfigDao.close();
										}
					              }catch(Exception e){
					            	  e.printStackTrace();
					              }
							}else{
								com.afunms.polling.node.Cics tnode=(com.afunms.polling.node.Cics)PollingEngine.getInstance().getCicsByIP(cicsConfig.getIpaddress());
								tnode.setAlarm(true);
								List alarmList = tnode.getAlarmMessage();
								if(alarmList == null)alarmList = new ArrayList();
								tnode.getAlarmMessage().add("CICS����ֹͣ");
    							Pingcollectdata hostdata=null;
    							hostdata=new Pingcollectdata();
    							hostdata.setIpaddress(cicsConfig.getIpaddress());
    							Calendar date=Calendar.getInstance();
    							hostdata.setCollecttime(date);
    							hostdata.setCategory("CicsPing");
    							hostdata.setEntity("Utilization");
    							hostdata.setSubentity("ConnectUtilization");
    							hostdata.setRestype("dynamic");
    							hostdata.setUnit("%");
    							hostdata.setThevalue("0");	
    							CicsConfigDao cicsConfigDao=new CicsConfigDao();
    							try{
    								cicsConfigDao.createHostData(hostdata);	        								
    							}catch(Exception e){
    								e.printStackTrace();
    							}finally{
    								cicsConfigDao.close();
    							}
    							createSMS("CICS����ֹͣ",cicsConfig,"ping");
							}
							
						} else {
							//CICS�ͻ��˼�������û�д�
							com.afunms.polling.node.Cics node=(com.afunms.polling.node.Cics)PollingEngine.getInstance().getCicsByID(cicsConfig.getId());
							node.setAlarm(true);
							List alaList = node.getAlarmMessage();
							if(alaList == null)alaList = new ArrayList();
							node.getAlarmMessage().add("CICS�ͻ��˼�������û�д�");
							//SmscontentDao eventdao = new SmscontentDao();
			            	//String eventdesc = "CICS�ͻ��˼�������(gateway:"+cicsConfig.getGateway()+")"+"û�д�";
			            	//eventdao.createEventWithReasion("poll",node.getId()+"",node.getAdminIp()+"("+node.getAdminIp()+")",eventdesc,2,"cics","open","���ڵķ��������Ӳ���");
							createSMS("CICS�ͻ��˼�������û�д�",cicsConfig,"open");
							//////////////
//							com.afunms.polling.node.Cics tnode=(com.afunms.polling.node.Cics)PollingEngine.getInstance().getCicsByID(cicsConfig.getId());
//							tnode.setAlarm(true);
//							List alarmList = tnode.getAlarmMessage();
//							if(alarmList == null)alarmList = new ArrayList();
//							tnode.getAlarmMessage().add("CICS����ֹͣ");
//							Pingcollectdata hostdata=null;
//							hostdata=new Pingcollectdata();
//							hostdata.setIpaddress(tnode.getIpaddress());
//							Calendar date=Calendar.getInstance();
//							hostdata.setCollecttime(date);
//							hostdata.setCategory("CicsPing");
//							hostdata.setEntity("Utilization");
//							hostdata.setSubentity("ConnectUtilization");
//							hostdata.setRestype("dynamic");
//							hostdata.setUnit("%");
//							hostdata.setThevalue("0");	
//							CicsConfigDao cicsConfigDao=new CicsConfigDao();
//							try{
//								cicsConfigDao.createHostData(hostdata);	        								
//							}catch(Exception e){
//								e.printStackTrace();
//							}finally{
//								cicsConfigDao.close();
//							}
//							createSMS("CICS����ֹͣ",cicsConfig,"ping");
						}
					}
				}catch(Exception ex){
					ex.printStackTrace();
					_cics.setAlarm(true);
					List alarmList = _cics.getAlarmMessage();
					if(alarmList == null)alarmList = new ArrayList();
					_cics.getAlarmMessage().add("CICS����ֹͣ");
					Pingcollectdata hostdata=null;
					hostdata=new Pingcollectdata();
					hostdata.setIpaddress(cicsConfig.getIpaddress());
					Calendar date=Calendar.getInstance();
					hostdata.setCollecttime(date);
					hostdata.setCategory("CicsPing");
					hostdata.setEntity("Utilization");
					hostdata.setSubentity("ConnectUtilization");
					hostdata.setRestype("dynamic");
					hostdata.setUnit("%");
					hostdata.setThevalue("0");	
					CicsConfigDao cicsConfigDao=new CicsConfigDao();
					try{
						cicsConfigDao.createHostData(hostdata);	        								
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						cicsConfigDao.close();
					}
					createSMS("����ֹͣ",cicsConfig,"ping");
				}finally{
				}
				           	 
            }catch(Exception exc){
            	exc.printStackTrace();
            }
        }
    };
}

public static void createSMS(String chlname,CicsConfig cicsConfig,String flag){
 	//��������		 	
 	//���ڴ����õ�ǰ���IP��PING��ֵ
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	AlarmInfoDao alarminfomanager=new AlarmInfoDao();

	String ipaddress = cicsConfig.getIpaddress();
	Hashtable sendeddata = ShareData.getSendeddata();
 	Calendar date=Calendar.getInstance();
 	String time = sdf.format(date.getTime());
 	try{
		if (!sendeddata.containsKey(ipaddress+":"+chlname)){
			//�����ڣ��������ţ�������ӵ������б���
 			Smscontent smscontent = new Smscontent();
 			smscontent.setLevel("2");
 			smscontent.setObjid(cicsConfig.getId()+"");
 			if("chl".equalsIgnoreCase(flag)){
 				smscontent.setMessage(cicsConfig.getRegion_name()+" ("+chlname+")"+"ͨ�����ڷ���������״̬");
 			}else
 				smscontent.setMessage(cicsConfig.getRegion_name()+"(IP:"+cicsConfig.getIpaddress()+")����ֹͣ");
 			smscontent.setRecordtime(time);
 			smscontent.setSubtype("cics");
 			smscontent.setSubentity("ping");
 			smscontent.setIp(cicsConfig.getIpaddress());
 			//���Ͷ���
 			SmscontentDao smsmanager=new SmscontentDao();
 			smsmanager.sendURLSmscontent(smscontent);	
			sendeddata.put(ipaddress+":"+chlname,date);		 					 				
		}else{
			//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
			Calendar formerdate =(Calendar)sendeddata.get(ipaddress+":"+chlname);		 				
 			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
 			Date last = null;
 			Date current = null;
 			Calendar sendcalen = formerdate;
 			Date cc = sendcalen.getTime();
 			String tempsenddate = formatter.format(cc);
 			
 			Calendar currentcalen = date;
 			cc = currentcalen.getTime();
 			last = formatter.parse(tempsenddate);
 			String currentsenddate = formatter.format(cc);
 			current = formatter.parse(currentsenddate);
 			String errmsg = "";
 			long subvalue = current.getTime()-last.getTime();			 			
 			if (subvalue/(1000*60*60*24)>=1){
 				//����һ�죬���ٷ���Ϣ
 				Smscontent smscontent = new Smscontent();
 	 			smscontent.setLevel("2");
 	 			smscontent.setObjid(cicsConfig.getId()+"");
 	 			if("chl".equalsIgnoreCase(flag)){
 	 				smscontent.setMessage(cicsConfig.getRegion_name()+" ("+chlname+")"+"ͨ�����ڷ���������״̬");
 	 			}else
 	 				smscontent.setMessage(cicsConfig.getRegion_name()+"(IP:"+cicsConfig.getIpaddress()+")����ֹͣ");
 	 			errmsg = smscontent.getMessage();
 	 			smscontent.setRecordtime(time);
 	 			smscontent.setSubtype("cics");
 	 			smscontent.setSubentity("ping");
 	 			smscontent.setIp(cicsConfig.getIpaddress());
 	 			//���Ͷ���
 	 			SmscontentDao smsmanager=new SmscontentDao();
 	 			smsmanager.sendURLSmscontent(smscontent);	
				//�޸��Ѿ����͵Ķ��ż�¼	
				sendeddata.put(ipaddress+":"+chlname,date);	
	 			//��д�����澯����
				//�������澯����д����
				//ֻ���ڷ����ź�Ų��������澯
				
				AlarmInfo alarminfo = new AlarmInfo();
				alarminfo.setContent(smscontent.getMessage());
				alarminfo.setIpaddress(cicsConfig.getIpaddress());
				alarminfo.setLevel1(new Integer(2));
				alarminfo.setRecordtime(Calendar.getInstance());
				alarminfomanager.save(alarminfo);
				
	 		}else{
	 			//��д�����澯����
				//�������澯����д����
	 			
				AlarmInfo alarminfo = new AlarmInfo();
				alarminfo.setContent(errmsg);
				alarminfo.setIpaddress(cicsConfig.getIpaddress());
				alarminfo.setLevel1(new Integer(2));
				alarminfo.setRecordtime(Calendar.getInstance());
				alarminfomanager.save(alarminfo);
				

	 		}
		}	 			 			 			 			 	
 	}catch(Exception e){
 		e.printStackTrace();
 	}
 }

}
