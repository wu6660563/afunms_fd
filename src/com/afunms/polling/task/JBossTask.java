package com.afunms.polling.task;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.application.dao.JBossConfigDao;
import com.afunms.application.dao.JBossmonitor_historyDao;
import com.afunms.application.dao.JBossmonitor_realtimeDao;
import com.afunms.application.jbossmonitor.HttpClientJBoss;
import com.afunms.application.manage.JBossManager;
import com.afunms.application.model.JBossConfig;
import com.afunms.application.model.JBossmonitor_history;
import com.afunms.application.model.JBossmonitor_realtime;
import com.afunms.common.util.ShareData;
import com.afunms.event.dao.AlarmInfoDao;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.AlarmInfo;
import com.afunms.event.model.EventList;
import com.afunms.event.model.Smscontent;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.impl.ProcessJBossData;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Pingcollectdata;

public class JBossTask extends MonitorTask{
	private Hashtable sendeddata = ShareData.getSendeddata();
	private JBossManager jbossconf = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	
	
	public JBossTask(){
		super();
	}
		
	public void run(){
		try{
			List<JBossConfig> jbossConfigList = new ArrayList<JBossConfig>();
			JBossConfigDao jbossConfigDao = new JBossConfigDao();
			//JBossManager jbossManager = new JBossManager();
			try{
				jbossConfigList = jbossConfigDao.getJBossConfigListByMonFlag(1);
			}catch(Exception e){
				
			}finally{
				jbossConfigDao.close();
			}
			Calendar date=Calendar.getInstance();
			for (int i = 0; i < jbossConfigList.size(); i++) {
				Hashtable hst = null;
				JBossmonitor_realtimeDao jbossmonitor_realtimeDao = null;
				JBossmonitor_historyDao jbossmonitor_historyDao = null ;
				SmscontentDao eventdao=null;
				try{
					jbossmonitor_historyDao = new JBossmonitor_historyDao();
					jbossmonitor_realtimeDao = new JBossmonitor_realtimeDao();
					Hashtable realHash=jbossmonitor_realtimeDao.getAllReal();
					Integer iscanconnected = new Integer(0+"");
					JBossmonitor_realtime jbossmonitor_realtimeold = new JBossmonitor_realtime();
					String reason= "";
					JBossConfig jbossConfig = jbossConfigList.get(i);
					com.afunms.polling.node.JBossConfig _jboss = (com.afunms.polling.node.JBossConfig)PollingEngine.getInstance().getJBossByID(jbossConfig.getId());	
					//��ո澯��Ϣ
					_jboss.getAlarmMessage().clear();
					_jboss.setAlarm(false);
					
					/*//System.out.println(dnsConfig+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					Dns dns = (Dns)PollingEngine.getInstance().getDnsByID(dnsConfig.getId());	
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
					JBossManager jbossManager = new JBossManager();
					hst = jbossManager.collectjbossdata(jbossConfig);
	        		if(hst == null) {
	        			hst = new Hashtable();
	        		
	        		}
	        		jbossManager = null;
	        		
				Integer jbossConfig_id = jbossConfig.getId();
				Integer jboss_id = jbossConfig.getId();
				boolean old=false;
				String str = "";
				Integer smssign=new Integer(0);
				if(realHash !=null&& realHash.get(jboss_id)!=null){
					old=true;
					jbossmonitor_realtimeold=(JBossmonitor_realtime)realHash.get(jboss_id);
					smssign=jbossmonitor_realtimeold.getSms_sign();
				}
				HttpClientJBoss jboss = new HttpClientJBoss();
			   	String src=jboss.getGetResponseWithHttpClient("http://"+jbossConfig.getIpaddress()+":"+jbossConfig.getPort()+"/web-console/ServerInfo.jsp", "GBK");
			   	String time = sdf.format(date.getTime());
			   	try{					
					if (src.contains("Version") ){
						reason="JBOSS������Ч";
						iscanconnected=new Integer(1);						
					}else{
						//��Ҫ����JBOSS�������ڵķ������Ƿ�����ͨ
						reason="JBOSS������Ч";
						//Host host = (Host)PollingEngine.getInstance().getNodeByIP(jbossConfig.getIpaddress());
						Vector ipPingData = (Vector)ShareData.getPingdata().get(jbossConfig.getIpaddress());
					      
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
								//�������������Ӳ���***********************************************
					    	  _jboss.setAlarm(true);
					    	  _jboss.setStatus(3);
								List alarmList = _jboss.getAlarmMessage();
								if(alarmList == null)alarmList = new ArrayList();
								_jboss.getAlarmMessage().add("JBoss������Ч");
					            String sysLocation = "";
					              try{
					            	  eventdao = new SmscontentDao();
					            	  String eventdesc = "JBOSS����(IP:"+jbossConfig.getIpaddress()+")"+"��JBOSS����ֹͣ";
					            	  eventdao.createEventWithReasion("poll",jbossConfig.getId()+"",jbossConfig.getAlias()+"("+jbossConfig.getIpaddress()+")",eventdesc,3,"jboss","ping","���ڵķ��������Ӳ���");
					            	  
					            	 /* SmscontentDao eventdao = new SmscontentDao();
					            	  String eventdesc = "DNS����(IP:"+dnsConfig.getDnsip()+")"+"��DNS����ֹͣ";
					            	  //System.out.println(dnsConfig.getId()+dnsConfig.getDns()+"dnsConfig.getDns()!!!!!!!!!!!!!!");
					            	  eventdao.createEventWithReasion("poll",dnsConfig.getId()+"",dnsConfig.getDns()+"("+dnsConfig.getDnsip()+")",eventdesc,3,"dns","ping","���ڵķ��������Ӳ���");*/
					              }catch(Exception e){
					            	  e.printStackTrace();
					              }
					             
							}else{
								_jboss.setAlarm(true);
								_jboss.setStatus(3);
								List alarmList = _jboss.getAlarmMessage();
								if(alarmList == null)alarmList = new ArrayList();
								_jboss.getAlarmMessage().add("JBOSS������Ч");
								reason = "JBOSS������Ч";
								createEvent(jbossConfig, reason);
							}
							
						}else{
							_jboss.setAlarm(true);
							_jboss.setStatus(3);
							List alarmList = _jboss.getAlarmMessage();
							if(alarmList == null)alarmList = new ArrayList();
							_jboss.getAlarmMessage().add("JBOSS������Ч");
							reason = "DNS������Ч";
							createEvent(jbossConfig, reason);
						}
						
						
						
						
					}
					
				}catch(Exception ex){
					//���ܽ���FTP����
					_jboss.setAlarm(true);
					_jboss.setStatus(3);
					List alarmList = _jboss.getAlarmMessage();
					if(alarmList == null)alarmList = new ArrayList();
					_jboss.getAlarmMessage().add("JBOSS������Ч");
					reason = "JBOSS������Ч";
					createEvent(jbossConfig, reason);
					
				}
				
				//�������ʷ����
				
				JBossmonitor_history jbossMonitor_history = new JBossmonitor_history();
				jbossMonitor_history.setJboss_id(jbossConfig.getId());
				jbossMonitor_history.setIs_canconnected(iscanconnected);
				jbossMonitor_history.setMon_time(Calendar.getInstance());
				jbossMonitor_history.setReason(reason);
				jbossmonitor_historyDao.save(jbossMonitor_history);	
				
				//UrlDataCollector udc = new UrlDataCollector();
				JBossmonitor_realtime jbossmonitor_realtime = new JBossmonitor_realtime();
				jbossmonitor_realtime.setJboss_id(jbossConfig.getId());
				jbossmonitor_realtime.setIs_canconnected(iscanconnected);
				jbossmonitor_realtime.setReason(reason);
				jbossmonitor_realtime.setMon_time(Calendar.getInstance());
				//ʵʱ����
				//ur.setUrl_id(url_id);
				if(old==true){
					jbossmonitor_realtime.setSms_sign(1);
				}
				else{
					jbossmonitor_realtime.setSms_sign(smssign);
				}
				
				
				//����realtime
				if (old == true) {
					jbossmonitor_realtime.setId(jbossmonitor_realtimeold.getId());
					jbossmonitor_realtimeDao.update(jbossmonitor_realtime);
				}
				if (old == false) {
					jbossmonitor_realtimeDao.save(jbossmonitor_realtime);
				}				
				
				if(sendeddata.containsKey("jbossserver:"+jbossConfig.getId())){
					sendeddata.remove("jbossserver:"+jbossConfig.getId());
				}
				try{
				    com.afunms.common.util.ShareData.setJbossdata("jboss"+":"+jbossConfig.getIpaddress(),hst);	
				}catch(Exception ex){
					ex.printStackTrace();
				} 
				hst = null;
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					jbossmonitor_historyDao.close();
					jbossmonitor_realtimeDao.close();
					
				}	
			}
			//���ɼ�����JBOSS��Ϣ���
			ProcessJBossData processJBossData = new ProcessJBossData();
			processJBossData.saveJBossData(jbossConfigList,ShareData.getJbossdata());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}		
	}
	
	public void createEvent(JBossConfig jbossConfig, String reason){
		Calendar date=Calendar.getInstance();
		String time = sdf.format(date.getTime());
		EventList event = new EventList();
		event.setEventtype("jbossserver");
		event.setEventlocation(jbossConfig.getIpaddress());
		event.setBusinessid(jbossConfig.getNetid());
		event.setManagesign(new Integer(0));
		event.setReportman("monitorpc");
		event.setRecordtime(new GregorianCalendar());		
		String errorcontent=time+" "+jbossConfig.getAlias()+"(IP:"+jbossConfig.getIpaddress()+")��JBOSS�������";		 		
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
		createSMS("jbossserver",jbossConfig.getId()+"",errorcontent,jbossConfig.getIpaddress());
	}
	

	 public void createSMS(String jbossserver,String jboss_id,String errmsg,String ipstr){
	 	//��������		 	
	 	//���ڴ����õ�ǰ���IP��PING��ֵ
	 	Calendar date=Calendar.getInstance();
	 	AlarmInfoDao alarmdao=null;
	 	try{
 			if (!sendeddata.containsKey(jbossserver+":"+jboss_id)){
 				//�����ڣ��������ţ�������ӵ������б���
	 			Smscontent smscontent = new Smscontent();
	 			smscontent.setMessage(errmsg);
	 			smscontent.setObjid(jboss_id);
	 			Calendar _tempCal = Calendar.getInstance();				
				Date _cc = _tempCal.getTime();
				String _time = sdf.format(_cc);
	 			smscontent.setRecordtime(_time);
	 			smscontent.setSubtype("jboss");
	 			smscontent.setLevel(3+"");
	 			//���Ͷ���
	 			SmscontentDao smsmanager=new SmscontentDao();
	 			smsmanager.sendURLSmscontent(smscontent);
				sendeddata.put(jbossserver+":"+jboss_id,date);		 					 				
 			}else{
 				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
 				Calendar formerdate =(Calendar)sendeddata.get(jbossserver+":"+jboss_id);		 				
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
		 			smscontent.setObjid(jboss_id);
		 			smscontent.setLevel(3+"");
		 			//���Ͷ���
		 			SmscontentDao smsmanager=new SmscontentDao();
		 			smsmanager.sendURLSmscontent(smscontent);
		 			Calendar _tempCal = Calendar.getInstance();				
					Date _cc = _tempCal.getTime();
					String _time = sdf.format(_cc);
		 			smscontent.setRecordtime(_time);
		 			smscontent.setSubtype("jboss");
					//�޸��Ѿ����͵Ķ��ż�¼	
					sendeddata.put(jbossserver+":"+jboss_id,date);	
		 		}else{
		 			//��д�����澯����
					//�������澯����д����
					AlarmInfo alarminfo = new AlarmInfo();
					alarminfo.setContent(errmsg);
					alarminfo.setIpaddress(ipstr);
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
