package com.afunms.polling.task;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.application.dao.Apachemonitor_historyDao;
import com.afunms.application.dao.Apachemonitor_realtimeDao;
import com.afunms.application.jbossmonitor.HttpClientJBoss;
import com.afunms.application.manage.ApacheManager;
import com.afunms.application.model.ApacheConfig;
import com.afunms.application.model.Apachemonitor_history;
import com.afunms.application.model.Apachemonitor_realtime;
import com.afunms.common.util.ShareData;
import com.afunms.event.dao.AlarmInfoDao;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.AlarmInfo;
import com.afunms.event.model.EventList;
import com.afunms.event.model.Smscontent;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.impl.ProcessApacheData;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Pingcollectdata;

public class ApacheTask extends MonitorTask{
	private Hashtable sendeddata = ShareData.getSendeddata();
	private ApacheManager apacheconf = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	
	
	public ApacheTask(){
		super();
	}
		
	public void run(){
		try{
			List<ApacheConfig> apacheConfigList = new ArrayList<ApacheConfig>();
			ApacheManager apacheManager = new ApacheManager();
			apacheConfigList = apacheManager.getApacheConfigListByMonflag(1);
			Calendar date=Calendar.getInstance();
			for (int i = 0; i < apacheConfigList.size(); i++) {
				Hashtable hst = null;
				SmscontentDao eventdao=null;
				Apachemonitor_realtimeDao apachemonitor_realtimeDao = null;
				Apachemonitor_historyDao apachemonitor_historyDao = null ;
				try{
					apachemonitor_historyDao = new Apachemonitor_historyDao();
					apachemonitor_realtimeDao = new Apachemonitor_realtimeDao();
					Hashtable realHash=apachemonitor_realtimeDao.getAllReal();
					Integer iscanconnected = new Integer(0+"");
					Apachemonitor_realtime apachemonitor_realtimeold = new Apachemonitor_realtime();
					String reason= "";
					ApacheConfig apacheConfig = apacheConfigList.get(i);
					/*//System.out.println(dnsConfig+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					//Dns dns = (Dns)PollingEngine.getInstance().getDnsByID(dnsConfig.getId());	
					System.out.println(dns+"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
					if(dns == null){
						continue;
					}
				if(dns != null){
					dns.setStatus(1);
					dns.setAlarm(false);
					dns.getAlarmMessage().clear();
					Calendar _tempCal = Calendar.getInstance();				
					Date _cc = _tempCal.getTime();
					String _time = sdf.format(_cc);
					dns.setLastTime(_time);
				}*/
					hst = apacheManager.collectapachedata(apacheConfig);
	        		if(hst == null) {
	        			hst = new Hashtable();
	        		
	        		}
				
				Integer apacheConfig_id = apacheConfig.getId();
				Integer apache_id = apacheConfig.getId();
				boolean old=false;
				String str = "";
				Integer smssign=new Integer(0);
				if(realHash !=null&& realHash.get(apache_id)!=null){
					old=true;
					apachemonitor_realtimeold=(Apachemonitor_realtime)realHash.get(apache_id);
					smssign=apachemonitor_realtimeold.getSms_sign();
				}
				HttpClientJBoss apache = new HttpClientJBoss();
			   	String src=apache.getGetResponseWithHttpClient("http://"+apacheConfig.getIpaddress()+":"+apacheConfig.getPort()+"/server-status", "GBK");
			   	String time = sdf.format(date.getTime());
			   	try{					
					if (src.contains("Version") ){
						reason="Apache������Ч";
						iscanconnected=new Integer(1);						
					}else{
						//��Ҫ�����ʼ��������ڵķ������Ƿ�����ͨ
						reason="Apache������Ч";
						Host host = (Host)PollingEngine.getInstance().getNodeByIP(apacheConfig.getIpaddress());
						Vector ipPingData = (Vector)ShareData.getPingdata().get(apacheConfig.getIpaddress());
					      eventdao = new SmscontentDao();
		            	  String eventdesc = "Apache����(IP:"+apacheConfig.getIpaddress()+")"+"��Apache����ֹͣ";
		            	  eventdao.createEventWithReasion("poll",apacheConfig.getId()+"",apacheConfig.getAlias()+"("+apacheConfig.getIpaddress()+")",eventdesc,3,"apache","ping","���ڵķ��������Ӳ���");
						if(ipPingData != null){
							Pingcollectdata pingdata = (Pingcollectdata)ipPingData.get(0);
							Calendar tempCal = (Calendar)pingdata.getCollecttime();							
							Date cc = tempCal.getTime();
							time = sdf.format(cc);		
							String lastTime = time;
							String pingvalue = pingdata.getThevalue();
							if(pingvalue == null || pingvalue.trim().length()==0)pingvalue="0";
							double pvalue = new Double(pingvalue);
							//
				
					      if(pvalue == 0){
								/*//�������������Ӳ���***********************************************
								dns.setAlarm(true);
								//dbnode.setStatus(3);
								List alarmList = dns.getAlarmMessage();
								if(alarmList == null)alarmList = new ArrayList();
								dns.getAlarmMessage().add("DNS������Ч");
					            String sysLocation = "";*/
					              try{
					            	 /* SmscontentDao eventdao = new SmscontentDao();
					            	  String eventdesc = "DNS����(IP:"+dnsConfig.getDnsip()+")"+"��DNS����ֹͣ";
					            	  //System.out.println(dnsConfig.getId()+dnsConfig.getDns()+"dnsConfig.getDns()!!!!!!!!!!!!!!");
					            	  eventdao.createEventWithReasion("poll",dnsConfig.getId()+"",dnsConfig.getDns()+"("+dnsConfig.getDnsip()+")",eventdesc,3,"dns","ping","���ڵķ��������Ӳ���");*/
					              }catch(Exception e){
					            	  e.printStackTrace();
					              }
					             
							}else{
								/*dns.setAlarm(true);
								//dbnode.setStatus(3);
								List alarmList = dns.getAlarmMessage();
								if(alarmList == null)alarmList = new ArrayList();
								dns.getAlarmMessage().add("DNS������Ч");
								reason = "DNS������Ч";
								createEvent(dnsConfig, reason);*/
							}
							
						}else{
							/*dns.setAlarm(true);
							//dbnode.setStatus(3);
							List alarmList = dns.getAlarmMessage();
							if(alarmList == null)alarmList = new ArrayList();
							dns.getAlarmMessage().add("DNS������Ч");
							reason = "DNS������Ч";
							createEvent(dnsConfig, reason);*/
						}
						
						
						
						
					}
					
				}catch(Exception ex){
					//���ܽ���FTP����
					/*dns.setAlarm(true);
					//dbnode.setStatus(3);
					List alarmList = dns.getAlarmMessage();
					if(alarmList == null)alarmList = new ArrayList();
					dns.getAlarmMessage().add("DNS������Ч");
					reason = "DNS������Ч";
					createEvent(dnsConfig, reason);*/
					
				}
				
				//�������ʷ����
				
				Apachemonitor_history apacheMonitor_history = new Apachemonitor_history();
				apacheMonitor_history.setApache_id(apacheConfig.getId());
				apacheMonitor_history.setIs_canconnected(iscanconnected);
				apacheMonitor_history.setMon_time(Calendar.getInstance());
				apacheMonitor_history.setReason(reason);
				apachemonitor_historyDao.save(apacheMonitor_history);	
				
				//UrlDataCollector udc = new UrlDataCollector();
				Apachemonitor_realtime apachemonitor_realtime = new Apachemonitor_realtime();
				apachemonitor_realtime.setApache_id(apacheConfig.getId());
				apachemonitor_realtime.setIs_canconnected(iscanconnected);
				apachemonitor_realtime.setReason(reason);
				apachemonitor_realtime.setMon_time(Calendar.getInstance());
				//ʵʱ����
				//ur.setUrl_id(url_id);
				if(old==true){
					apachemonitor_realtime.setSms_sign(1);
				}
				else{
					apachemonitor_realtime.setSms_sign(smssign);
				}
				
				
				//����realtime
				if (old == true) {
					apachemonitor_realtime.setId(apachemonitor_realtimeold.getId());
					apachemonitor_realtimeDao.update(apachemonitor_realtime);
				}
				if (old == false) {
					apachemonitor_realtimeDao.save(apachemonitor_realtime);
				}				
				
				//uh.setIs_refresh(ur.getIs_refresh());
				//uh.setIs_valid(ur.getIs_valid());
				
//				if(sendeddata.containsKey("apacheserver:"+apacheConfig.getId())){
//					sendeddata.remove("apacheserver:"+apacheConfig.getId());
//				}
				if(sendeddata.containsKey("apache"+":"+apacheConfig.getIpaddress())){
					sendeddata.remove("apache"+":"+apacheConfig.getIpaddress());
				}
				try{
				    com.afunms.common.util.ShareData.setApachedata("apache:"+apacheConfig.getIpaddress(),hst);	
				}catch(Exception ex){
					ex.printStackTrace();
				} 
				hst = null;
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					apachemonitor_historyDao.close();
					apachemonitor_realtimeDao.close();
					
				}	
			}
			//���ɼ�����Apache Http Server ��Ϣ���
			ProcessApacheData processApacheData = new ProcessApacheData();
			processApacheData.saveApacheData(apacheConfigList,ShareData.getApachedata());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}		
	}
	
	public void createEvent(ApacheConfig apacheConfig, String reason){
		Calendar date=Calendar.getInstance();
		String time = sdf.format(date.getTime());
		EventList event = new EventList();
		event.setEventtype("apacheserver");
		event.setEventlocation(apacheConfig.getIpaddress());
		event.setBusinessid(apacheConfig.getNetid());
		event.setManagesign(new Integer(0));
		event.setReportman("monitorpc");
		event.setRecordtime(new GregorianCalendar());		
		String errorcontent=time+" "+apacheConfig.getUsername()+"(IP:"+apacheConfig.getIpaddress()+")��Apache�������";		 		
		event.setContent(errorcontent);
		Integer level = new Integer(2);
		event.setLevel1(level);
		//reason="FTP������Ч";
		//EventListDao eventListDao = null ;
		try{
			//eventListDao = new EventListDao();
			//eventListDao.save(event);
		}catch(Exception e){
			
		}finally{
			//eventListDao.close();
		}
		
		Vector eventtmpV = new Vector();
		eventtmpV.add(event);
		createSMS("apacheserver",apacheConfig.getId()+"",errorcontent,apacheConfig.getIpaddress());
	}
	

	 public void createSMS(String apacheserver,String apache_id,String errmsg,String ftpstr){
	 	//��������		 	
	 	//���ڴ����õ�ǰ���IP��PING��ֵ
	 	Calendar date=Calendar.getInstance();
	 	AlarmInfoDao alarmdao=null;
	 	try{
 			if (!sendeddata.containsKey(apacheserver+":"+apache_id)){
 				//�����ڣ��������ţ�������ӵ������б���
	 			Smscontent smscontent = new Smscontent();
	 			smscontent.setMessage(errmsg);
	 			smscontent.setObjid(apache_id);
	 			Calendar _tempCal = Calendar.getInstance();				
				Date _cc = _tempCal.getTime();
				String _time = sdf.format(_cc);
	 			smscontent.setRecordtime(_time);
	 			smscontent.setSubtype("apache");
	 			smscontent.setLevel(3+"");
	 			//���Ͷ���
	 			SmscontentDao smsmanager=new SmscontentDao();
	 			smsmanager.sendURLSmscontent(smscontent);
				sendeddata.put(apacheserver+":"+apache_id,date);		 					 				
 			}else{
 				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
 				Calendar formerdate =(Calendar)sendeddata.get(apacheserver+":"+apache_id);		 				
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
	 			
	 			long subvalue = current.getTime()-last.getTime();			 			
	 			if (subvalue/(1000*60*60*24)>=1){
	 				//����һ�죬���ٷ���Ϣ
		 			Smscontent smscontent = new Smscontent();
		 			//String time = sdf.format(date.getTime());
		 			smscontent.setMessage(errmsg);
		 			smscontent.setObjid(apache_id);
		 			smscontent.setLevel(3+"");
		 			//���Ͷ���
		 			SmscontentDao smsmanager=new SmscontentDao();
		 			smsmanager.sendURLSmscontent(smscontent);
		 			Calendar _tempCal = Calendar.getInstance();				
					Date _cc = _tempCal.getTime();
					String _time = sdf.format(_cc);
		 			smscontent.setRecordtime(_time);
		 			smscontent.setSubtype("apache");
					//�޸��Ѿ����͵Ķ��ż�¼	
					sendeddata.put(apacheserver+":"+apache_id,date);	
		 		}else{
		 			//��д�����澯����
					//�������澯����д����
					AlarmInfo alarminfo = new AlarmInfo();
					alarminfo.setContent(errmsg);
					alarminfo.setIpaddress(errmsg);
					alarminfo.setLevel1(new Integer(2));
					alarminfo.setRecordtime(Calendar.getInstance());
					alarmdao = new AlarmInfoDao();
	                alarmdao.save(alarminfo);
		 			
		 			/*
					Calendar tempCal = Calendar.getInstance();						
					java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String time = sdf.format(cc);					

					String queryStr = "insert into alarminfor(content,ipaddress,level1,recordtime) values('"+errmsg+"','"+ftpstr+"',2,to_date('"+time+"','YYYY-MM-DD HH24:MI:SS'))";					
					Connection con = null;			
					PreparedStatement stmt = null;
					ResultSet rs = null;
					try{
						con=DataGate.getCon();
						stmt = con.prepareStatement(queryStr);
						stmt.execute();
						stmt.close();						
					}catch(Exception ex){
						ex.printStackTrace();
						//rs.close();
					}finally{
						try{
						stmt.close();
						DataGate.freeCon(con);
						}catch(Exception exp){
							//
						}
					}
					*/
	
		 		}
 			}	 			 			 			 			 	
	 	}catch(Exception e){
	 		e.printStackTrace();
	 	}
	 	finally
	 	{
	 		alarmdao.close();
	 	}
	 }

}
