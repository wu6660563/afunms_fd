/*
 * Created on 2005-4-22
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

import com.afunms.application.dao.DominoConfigDao;
import com.afunms.application.model.DominoConfig;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.event.dao.AlarmInfoDao;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.AlarmInfo;
import com.afunms.event.model.Smscontent;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.node.Domino;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.polling.om.Task;
import com.afunms.polling.snmp.DominoSnmp;


/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DominoTask extends MonitorTask {
	//private final static boolean  debug=false; 
	/**
	 * 
	 */
	public DominoTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		DominoConfigDao configdao = new DominoConfigDao();
		try{
			List list=null;
			try{
				list = configdao.getDominoByFlag(new Integer("1"));
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				configdao.close();
			}
			Vector vector=null;
    		int numTasks = list.size();
    		int numThreads = 200;
    		try {
    			List numList = new ArrayList();
    			TaskXml taskxml = new TaskXml();
    			numList = taskxml.ListXml();
    			for (int i = 0; i < numList.size(); i++) {
    				Task task = new Task();
    				BeanUtils.copyProperties(task, numList.get(i));
    				if (task.getTaskname().equals("hostthreadnum")){
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
        			//SysLogger.info("----------��ʼ�ɼ�");
            			threadPool.runTask(createTask((DominoConfig)list.get(i)));
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
			System.out.println("********Domino Thread Count : "+Thread.activeCount());
		}
		// TODO Auto-generated method stub
	}
	
    /**
    ��������
*/	
private static Runnable createTask(final DominoConfig dominoconf) {
    return new Runnable() {
        public void run() {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	Hashtable hash = null;
            try {                	
				Domino _domino = (Domino)PollingEngine.getInstance().getDominoByID(dominoconf.getId());	
				if(_domino == null){
					return;
				}
				if(_domino != null){
					_domino.setStatus(0);
					_domino.setAlarm(false);
					_domino.getAlarmMessage().clear();
					Calendar _tempCal = Calendar.getInstance();				
					Date _cc = _tempCal.getTime();
					String _time = sdf.format(_cc);
					_domino.setLastTime(_time);
				}
				DominoSnmp dominosnmp = new DominoSnmp();
				
    			try{
    				hash = dominosnmp.collect_Data(dominoconf);
    			}catch(Exception e){
    				e.printStackTrace();
    			}
				try{
					ShareData.setDominodata(dominoconf.getIpaddress(), hash);
					//�жϸ澯
					if(hash != null && hash.size()>0){
					HostCollectDataManager manager=new HostCollectDataManager();
					 manager.createHostItemData(hash,dominoconf.getIpaddress());
					}else{
						//��Ҫ�����ʼ��������ڵķ������Ƿ�����ͨ
						Host host = (Host)PollingEngine.getInstance().getNodeByIP(dominoconf.getIpaddress());
						Vector ipPingData = (Vector)ShareData.getPingdata().get(dominoconf.getIpaddress());
						if(ipPingData != null){
							Pingcollectdata pingdata = (Pingcollectdata)ipPingData.get(0);
							Calendar tempCal = (Calendar)pingdata.getCollecttime();							
							Date cc = tempCal.getTime();
							String _time = sdf.format(cc);		
							String lastTime = _time;
							String pingvalue = pingdata.getThevalue();
							if(pingvalue == null || pingvalue.trim().length()==0)pingvalue="0";
							double pvalue = new Double(pingvalue);
							if(pvalue == 0){
								//�������������Ӳ���***********************************************
								com.afunms.polling.node.Domino tnode=(com.afunms.polling.node.Domino)PollingEngine.getInstance().getDominoByIP(dominoconf.getIpaddress());
								tnode.setAlarm(true);
								tnode.setStatus(3);
								List alarmList = tnode.getAlarmMessage();
								if(alarmList == null)alarmList = new ArrayList();
								tnode.getAlarmMessage().add("DOMINO����ֹͣ,��Ϊ���ڵķ��������Ӳ���");
					            String sysLocation = "";
					            SmscontentDao eventdao = new SmscontentDao();
					              try{
					            	  
					            	  String eventdesc = "DOMINO����("+tnode.getAlias()+" IP:"+tnode.getAdminIp()+")"+"��DOMINO����ֹͣ";
					            	  eventdao.createEventWithReasion("poll",tnode.getId()+"",tnode.getAdminIp()+"("+tnode.getAdminIp()+")",eventdesc,3,"domino","ping","���ڵķ��������Ӳ���");
					            	  Pingcollectdata hostdata=null;
										hostdata=new Pingcollectdata();
										hostdata.setIpaddress(dominoconf.getIpaddress());
										Calendar date=Calendar.getInstance();
										hostdata.setCollecttime(date);
										hostdata.setCategory("DominoPing");
										hostdata.setEntity("Utilization");
										hostdata.setSubentity("ConnectUtilization");
										hostdata.setRestype("dynamic");
										hostdata.setUnit("%");
										hostdata.setThevalue("0");	
										DominoConfigDao dominodao=new DominoConfigDao();
										try{
											dominodao.createHostData(hostdata);	        								
										}catch(Exception e){
											e.printStackTrace();
										}finally{
											dominodao.close();
										}
					              }catch(Exception e){
					            	  e.printStackTrace();
					              }finally{
					            	  
					              }
							}else{
								com.afunms.polling.node.Domino tnode=(com.afunms.polling.node.Domino)PollingEngine.getInstance().getDominoByIP(dominoconf.getIpaddress());
								tnode.setAlarm(true);
								tnode.setStatus(3);
								List alarmList = tnode.getAlarmMessage();
								if(alarmList == null)alarmList = new ArrayList();
								tnode.getAlarmMessage().add("Domino����ֹͣ");
    							Pingcollectdata hostdata=null;
    							hostdata=new Pingcollectdata();
    							hostdata.setIpaddress(dominoconf.getIpaddress());
    							Calendar date=Calendar.getInstance();
    							hostdata.setCollecttime(date);
    							hostdata.setCategory("DominoPing");
    							hostdata.setEntity("Utilization");
    							hostdata.setSubentity("ConnectUtilization");
    							hostdata.setRestype("dynamic");
    							hostdata.setUnit("%");
    							hostdata.setThevalue("0");	
    							DominoConfigDao dominodao=new DominoConfigDao();
    							try{
    								dominodao.createHostData(hostdata);	        								
    							}catch(Exception e){
    								e.printStackTrace();
    							}finally{
    								dominodao.close();
    							}
    							createSMS("Domino����ֹͣ",dominoconf,"ping");
								//reason = "FTP������Ч";
								//createEvent(ftpConfig, reason);
							}
							
						}else{
							com.afunms.polling.node.Domino tnode=(com.afunms.polling.node.Domino)PollingEngine.getInstance().getDominoByID(dominoconf.getId());
							tnode.setAlarm(true);
							tnode.setStatus(3);
							List alarmList = tnode.getAlarmMessage();
							if(alarmList == null)alarmList = new ArrayList();
							tnode.getAlarmMessage().add("Domino����ֹͣ");
							Pingcollectdata hostdata=null;
							hostdata=new Pingcollectdata();
							hostdata.setIpaddress(dominoconf.getIpaddress());
							Calendar date=Calendar.getInstance();
							hostdata.setCollecttime(date);
							hostdata.setCategory("DominoPing");
							hostdata.setEntity("Utilization");
							hostdata.setSubentity("ConnectUtilization");
							hostdata.setRestype("dynamic");
							hostdata.setUnit("%");
							hostdata.setThevalue("0");	
							DominoConfigDao dominodao=new DominoConfigDao();
							try{
								dominodao.createHostData(hostdata);	        								
							}catch(Exception e){
								e.printStackTrace();
							}finally{
								dominodao.close();
							}
							createSMS("Domino����ֹͣ",dominoconf,"ping");
							//reason = "FTP������Ч";
							//createEvent(ftpConfig, reason);
						}
					}
					//hostdataManager.createHostData(dominoconf.getIpaddress(),hash);
				}catch(Exception ex){
					ex.printStackTrace();
					_domino.setAlarm(true);
					_domino.setStatus(3);
					List alarmList = _domino.getAlarmMessage();
					if(alarmList == null)alarmList = new ArrayList();
					_domino.getAlarmMessage().add("Domino����ֹͣ");
					Pingcollectdata hostdata=null;
					hostdata=new Pingcollectdata();
					hostdata.setIpaddress(dominoconf.getIpaddress());
					Calendar date=Calendar.getInstance();
					hostdata.setCollecttime(date);
					hostdata.setCategory("DominoPing");
					hostdata.setEntity("Utilization");
					hostdata.setSubentity("ConnectUtilization");
					hostdata.setRestype("dynamic");
					hostdata.setUnit("%");
					hostdata.setThevalue("0");	
					DominoConfigDao dominodao=new DominoConfigDao();
					try{
						dominodao.createHostData(hostdata);	        								
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						dominodao.close();
					}
					createSMS("Domino����ֹͣ",dominoconf,"ping");
				}finally{
	        		hash=null; 
				}
				           	 
            }catch(Exception exc){
            	exc.printStackTrace();
            }
        }
    };
}

public static void createSMS(String chlname,DominoConfig dominoconf,String flag){
 	//��������		 	
 	//���ڴ����õ�ǰ���IP��PING��ֵ
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	AlarmInfoDao alarminfomanager=new AlarmInfoDao();

	String ipaddress = dominoconf.getIpaddress();
	Hashtable sendeddata = ShareData.getSendeddata();
 	Calendar date=Calendar.getInstance();
 	String time = sdf.format(date.getTime());
 	try{
		if (!sendeddata.containsKey(ipaddress+":"+chlname)){
			//�����ڣ��������ţ�������ӵ������б���
 			Smscontent smscontent = new Smscontent();
 			smscontent.setLevel("2");
 			smscontent.setObjid(dominoconf.getId()+"");
 			smscontent.setMessage(dominoconf.getName()+"(IP:"+dominoconf.getIpaddress()+")DOMINO����ֹͣ");
 			smscontent.setRecordtime(time);
 			smscontent.setSubtype("domino");
 			smscontent.setSubentity("ping");
 			smscontent.setIp(dominoconf.getIpaddress());
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
 	 			smscontent.setObjid(dominoconf.getId()+"");
 	 			smscontent.setMessage(dominoconf.getName()+"(IP:"+dominoconf.getIpaddress()+")DOMINO����ֹͣ");
 	 			errmsg = smscontent.getMessage();
 	 			smscontent.setRecordtime(time);
 	 			smscontent.setSubtype("domino");
 	 			smscontent.setSubentity("ping");
 	 			smscontent.setIp(dominoconf.getIpaddress());
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
				alarminfo.setIpaddress(dominoconf.getIpaddress());
				alarminfo.setLevel1(new Integer(2));
				alarminfo.setRecordtime(Calendar.getInstance());
				alarminfomanager.save(alarminfo);
				
	 		}else{
	 			//��д�����澯����
				//�������澯����д����
	 			
				AlarmInfo alarminfo = new AlarmInfo();
				alarminfo.setContent(errmsg);
				alarminfo.setIpaddress(dominoconf.getIpaddress());
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
