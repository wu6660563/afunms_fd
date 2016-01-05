/*
 * Created on 2005-3-28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.afunms.polling.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.application.dao.EmailConfigDao;
import com.afunms.application.dao.Emailmonitor_historyDao;
import com.afunms.application.dao.Emailmonitor_realtimeDao;
import com.afunms.application.manage.MailMonitorManager;
import com.afunms.application.model.EmailMonitorConfig;
import com.afunms.application.model.Emailmonitor_history;
import com.afunms.application.model.Emailmonitor_realtime;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.ReceiveMail;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.sendMail;
import com.afunms.event.dao.AlarmInfoDao;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.AlarmInfo;
import com.afunms.event.model.EventList;
import com.afunms.event.model.Smscontent;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Host;
import com.afunms.polling.node.Mail;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.Pingcollectdata;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class MailDataCollector {
	private Hashtable sendeddata = ShareData.getSendeddata();
	private MailMonitorManager mailconf = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	
	
	public MailDataCollector(){
	}
		
	public void collect_data(String id,Hashtable gatherHash) {
		Emailmonitor_realtimeDao emailmonitor_realtimeDao = null;
		Emailmonitor_historyDao emailmonitor_historyDao = null;
		try{
			EmailConfigDao emailConfigDao = new EmailConfigDao();
			EmailMonitorConfig mailconfig = null;
			try{
				mailconfig = (EmailMonitorConfig)emailConfigDao.findByID(id);
			}catch(Exception e){
				
			}finally{
				emailConfigDao.close();
			}
			Calendar date=Calendar.getInstance();
		
			ReceiveMail receieveMail = new ReceiveMail();
			sendMail sendmail = new sendMail();
			
				try{
				emailmonitor_realtimeDao = new Emailmonitor_realtimeDao();
				
				
				Hashtable realHash= null;
				try{
					realHash = emailmonitor_realtimeDao.getAllReal();
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					emailmonitor_realtimeDao.close();
				}

				Emailmonitor_history emailmonitor_history = new Emailmonitor_history();
				Emailmonitor_realtime emailold = new Emailmonitor_realtime();
				Integer iscanconnected = new Integer(0);
				String reason= "";
				Mail mail = (Mail)PollingEngine.getInstance().getMailByID(mailconfig.getId());	
				if(mail == null){
					return;
				}
				if(mail != null){
					mail.setStatus(0);
					mail.setAlarm(false);
					mail.getAlarmMessage().clear();
					Calendar _tempCal = Calendar.getInstance();				
					Date _cc = _tempCal.getTime();
					String _time = sdf.format(_cc);
					mail.setLastTime(_time);
				}

				Integer email_id = mailconfig.getId();
				boolean old=false;
				String str = "";
				Integer smssign=new Integer(0);
				if(realHash !=null&& realHash.get(email_id)!=null){
					old=true;
					emailold=(Emailmonitor_realtime)realHash.get(email_id);
					smssign=emailold.getSms_sign();
				}
				boolean flag = true;
				boolean receieveflag = true;
				int intflag = 0;
				int intreceieveflag = 0;
				
				try{	
					if(gatherHash.containsKey("send")){
						try{
							if(configSendMail(sendmail, mailconfig)){
								flag = sendmail.sendmail();
								//intflag = 1;
							}
						}catch(Exception ex){
							flag = false;
							ex.printStackTrace();
						}
					}
					
					if(gatherHash.containsKey("receieve")){
						try{
							String mailserver = "";
							if (mailconfig.getAddress().indexOf("mail")>=0){
								//用的是MAIL服务
								mailserver = mailconfig.getAddress();
							}else if (mailconfig.getAddress().indexOf("smtp")>=0){
								//用的是SMTP服务
								mailserver = "smtp"+mailconfig.getAddress().substring(4);						
							}else{
								//用的是POP3服务
								mailserver = "pop3."+mailconfig.getAddress();
							}
							receieveflag = receieveMail.GetReceieveMail(mailserver, mailconfig.getUsername(), mailconfig.getPassword());
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}
						
					
					//System.out.println("flag:"+flag+"-----receieveflag:"+receieveflag);					
					if (flag &&  receieveflag){
						intflag = 1;
						intreceieveflag = 1;
						iscanconnected = 1;
						reason="服务有效";
						if(sendeddata.containsKey("emailserver:"+mailconfig.getId())){
							sendeddata.remove("emailserver:"+mailconfig.getId());
						}
						
					}else{
						if (flag == true && receieveflag == false){
							//邮件能发送但不能接收
							intflag = 1;
							reason="邮件接收服务无效";
							Vector mailvector = new Vector();
							//开始设置采集值实体
							Interfacecollectdata interfacedata=new Interfacecollectdata();
							interfacedata.setIpaddress(mail.getIpAddress());
							interfacedata.setCollecttime(date);
							interfacedata.setCategory("Mail");
							interfacedata.setEntity("Send");
							interfacedata.setSubentity(mail.getId()+"");									
							interfacedata.setRestype("static");
							interfacedata.setUnit("");
							interfacedata.setThevalue(intflag+"");
							interfacedata.setChname("发送服务");
							mailvector.add(interfacedata);
							
							interfacedata=new Interfacecollectdata();
							interfacedata.setIpaddress(mail.getIpAddress());
							interfacedata.setCollecttime(date);
							interfacedata.setCategory("Mail");
							interfacedata.setEntity("Receieve");
							interfacedata.setSubentity(mail.getId()+"");									
							interfacedata.setRestype("static");
							interfacedata.setUnit("");
							interfacedata.setThevalue(intreceieveflag+"");
							interfacedata.setChname("接收服务");
							mailvector.add(interfacedata);
							
							Hashtable collectHash = new Hashtable();
							collectHash.put("mail", mailvector);
							
//							mail.setStatus(3);
//							mail.setAlarm(true);
//							List alarmList = mail.getAlarmMessage();
//							if(alarmList == null)alarmList = new ArrayList();
							
							//createEvent(mailconfig, reason);
						    try{
								AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
								List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(mailconfig.getId()), AlarmConstant.TYPE_SERVICE, "mail");
								for(int k = 0 ; k < list.size() ; k ++){
									AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(k);
									//对邮件服务值进行告警检测
									CheckEventUtil checkutil = new CheckEventUtil();
									checkutil.updateData(mail,collectHash,"service","mail",alarmIndicatorsnode);
									//}
								}
						    }catch(Exception e){
						    	e.printStackTrace();
						    }							
							//createEvent(mailconfig, reason);													
						}else if (flag == false && receieveflag == true){
							intreceieveflag = 1;
							//邮件能接收但不能发送
							reason="邮件发送服务无效";
							Vector mailvector = new Vector();
							//开始设置采集值实体
							Interfacecollectdata interfacedata=new Interfacecollectdata();
							interfacedata.setIpaddress(mail.getIpAddress());
							interfacedata.setCollecttime(date);
							interfacedata.setCategory("Mail");
							interfacedata.setEntity("Send");
							interfacedata.setSubentity(mail.getId()+"");									
							interfacedata.setRestype("static");
							interfacedata.setUnit("");
							interfacedata.setThevalue(intflag+"");
							interfacedata.setChname("发送服务");
							mailvector.add(interfacedata);
							
							interfacedata=new Interfacecollectdata();
							interfacedata.setIpaddress(mail.getIpAddress());
							interfacedata.setCollecttime(date);
							interfacedata.setCategory("Mail");
							interfacedata.setEntity("Receieve");
							interfacedata.setSubentity(mail.getId()+"");									
							interfacedata.setRestype("static");
							interfacedata.setUnit("");
							interfacedata.setThevalue(intreceieveflag+"");
							interfacedata.setChname("接收服务");
							mailvector.add(interfacedata);
							
							Hashtable collectHash = new Hashtable();
							collectHash.put("mail", mailvector);
							
//							mail.setStatus(3);
//							mail.setAlarm(true);
//							List alarmList = mail.getAlarmMessage();
//							if(alarmList == null)alarmList = new ArrayList();
							
							//createEvent(mailconfig, reason);
						    try{
								AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
								List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(mailconfig.getId()), AlarmConstant.TYPE_SERVICE, "mail");
								for(int k = 0 ; k < list.size() ; k ++){
									AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(k);
									//对邮件服务值进行告警检测
									CheckEventUtil checkutil = new CheckEventUtil();
									checkutil.updateData(mail,collectHash,"service","mail",alarmIndicatorsnode);
									//}
								}
						    }catch(Exception e){
						    	e.printStackTrace();
						    }							
							//createEvent(mailconfig, reason);												
						}else{
							//邮件服务停止
							//需要增加邮件服务所在的服务器是否能连通
							//Host host = (Host)PollingEngine.getInstance().getNodeByIP(mailconfig.getIpaddress());
							Vector ipPingData = (Vector)ShareData.getPingdata().get(mailconfig.getIpaddress());
							if(ipPingData != null){
								Pingcollectdata pingdata = (Pingcollectdata)ipPingData.get(0);
								Calendar tempCal = (Calendar)pingdata.getCollecttime();							
								Date cc = tempCal.getTime();
								String time = sdf.format(cc);		
								String lastTime = time;
								String pingvalue = pingdata.getThevalue();
								if(pingvalue == null || pingvalue.trim().length()==0)pingvalue="0";
								double pvalue = new Double(pingvalue);
								if(pvalue == 0){
									//主机服务器连接不上***********************************************
									mail.setStatus(3);
									mail.setAlarm(true);
									//SysLogger.info(mail.getName()+"========================"+mail.isAlarm());
									//dbnode.setStatus(3);
									List alarmList = mail.getAlarmMessage();
									if(alarmList == null)alarmList = new ArrayList();
									mail.getAlarmMessage().add("邮件服务无效");
						            String sysLocation = "";
						              try{
						            	  SmscontentDao smsdao = new SmscontentDao();
						            	  String eventdesc = "邮件服务("+mailconfig.getName()+" IP:"+mailconfig.getAddress()+")"+"的邮件服务停止";
						            	  smsdao.createEventWithReasion("poll",mailconfig.getId()+"",mailconfig.getName()+"("+mailconfig.getAddress()+")",eventdesc,3,"mail","ping","所在的服务器连接不上");
						              }catch(Exception e){
						            	  e.printStackTrace();
						              }
								}else{
//									mail.setStatus(3);
//									mail.setAlarm(true);
//									List alarmList = mail.getAlarmMessage();
//									if(alarmList == null)alarmList = new ArrayList();
//									mail.getAlarmMessage().add("邮件服务无效");
//									reason="邮件服务无效";		
									Vector mailvector = new Vector();
									//开始设置采集值实体
									Interfacecollectdata interfacedata=new Interfacecollectdata();
									interfacedata.setIpaddress(mail.getIpAddress());
									interfacedata.setCollecttime(date);
									interfacedata.setCategory("Mail");
									interfacedata.setEntity("Send");
									interfacedata.setSubentity(mail.getId()+"");									
									interfacedata.setRestype("static");
									interfacedata.setUnit("");
									interfacedata.setThevalue(intflag+"");
									interfacedata.setChname("发送服务");
									mailvector.add(interfacedata);
									
									interfacedata=new Interfacecollectdata();
									interfacedata.setIpaddress(mail.getIpAddress());
									interfacedata.setCollecttime(date);
									interfacedata.setCategory("Mail");
									interfacedata.setEntity("Receieve");
									interfacedata.setSubentity(mail.getId()+"");									
									interfacedata.setRestype("static");
									interfacedata.setUnit("");
									interfacedata.setThevalue(intreceieveflag+"");
									interfacedata.setChname("接收服务");
									mailvector.add(interfacedata);
									
									Hashtable collectHash = new Hashtable();
									collectHash.put("mail", mailvector);
									
//									mail.setStatus(3);
//									mail.setAlarm(true);
									
									//createEvent(mailconfig, reason);
								    try{
										AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
										List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(mailconfig.getId()), AlarmConstant.TYPE_SERVICE, "mail");
										for(int k = 0 ; k < list.size() ; k ++){
											AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(k);
											//对邮件服务值进行告警检测
											CheckEventUtil checkutil = new CheckEventUtil();
											checkutil.updateData(mail,collectHash,"service","mail",alarmIndicatorsnode);
											//}
										}
								    }catch(Exception e){
								    	e.printStackTrace();
								    }
									//createEvent(mailconfig, reason);
								}
								
							}else{
//								mail.setStatus(3);
//								mail.setAlarm(true);
//								List alarmList = mail.getAlarmMessage();
//								if(alarmList == null)alarmList = new ArrayList();
//								mail.getAlarmMessage().add("邮件服务无效");
//								reason="邮件服务无效";	
								Vector mailvector = new Vector();
								//开始设置采集值实体
								Interfacecollectdata interfacedata=new Interfacecollectdata();
								interfacedata.setIpaddress(mail.getIpAddress());
								interfacedata.setCollecttime(date);
								interfacedata.setCategory("Mail");
								interfacedata.setEntity("Send");
								interfacedata.setSubentity(mail.getId()+"");									
								interfacedata.setRestype("static");
								interfacedata.setUnit("");
								interfacedata.setThevalue(intflag+"");
								interfacedata.setChname("发送服务");
								mailvector.add(interfacedata);
								
								interfacedata=new Interfacecollectdata();
								interfacedata.setIpaddress(mail.getIpAddress());
								interfacedata.setCollecttime(date);
								interfacedata.setCategory("Mail");
								interfacedata.setEntity("Receieve");
								interfacedata.setSubentity(mail.getId()+"");									
								interfacedata.setRestype("static");
								interfacedata.setUnit("");
								interfacedata.setThevalue(intreceieveflag+"");
								interfacedata.setChname("接收服务");
								mailvector.add(interfacedata);
								
								Hashtable collectHash = new Hashtable();
								collectHash.put("mail", mailvector);
								
//								mail.setStatus(3);
//								mail.setAlarm(true);
								
								//createEvent(mailconfig, reason);
							    try{
									AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
									List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(mailconfig.getId()), AlarmConstant.TYPE_SERVICE, "mail");
									for(int k = 0 ; k < list.size() ; k ++){
										AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(k);
										//对邮件服务值进行告警检测
										CheckEventUtil checkutil = new CheckEventUtil();
										checkutil.updateData(mail,collectHash,"service","mail",alarmIndicatorsnode);
										//}
									}
							    }catch(Exception e){
							    	e.printStackTrace();
							    }
								//createEvent(mailconfig, reason);
							}	
						}
					}										
				}catch(Exception ex){
//					//不能进行邮件服务					
//					EventList event = new EventList();
//					if (flag == true && receieveflag == false){
//						intflag = 1;
//						reason="邮件接收服务无效";
//						mail.getAlarmMessage().add("邮件接收服务无效");
//					}else if (flag == false && receieveflag == true){
//						intreceieveflag = 1;
//						reason="邮件发送服务无效";
//						mail.getAlarmMessage().add("邮件发送服务无效");
//					}
//					Vector mailvector = new Vector();
//					//开始设置采集值实体
//					Interfacecollectdata interfacedata=new Interfacecollectdata();
//					interfacedata.setIpaddress(mail.getIpAddress());
//					interfacedata.setCollecttime(date);
//					interfacedata.setCategory("Mail");
//					interfacedata.setEntity("Send");
//					interfacedata.setSubentity(mail.getId()+"");									
//					interfacedata.setRestype("static");
//					interfacedata.setUnit("");
//					interfacedata.setThevalue(intflag+"");
//					interfacedata.setChname("发送服务");
//					mailvector.add(interfacedata);
//					
//					interfacedata=new Interfacecollectdata();
//					interfacedata.setIpaddress(mail.getIpAddress());
//					interfacedata.setCollecttime(date);
//					interfacedata.setCategory("Mail");
//					interfacedata.setEntity("Receieve");
//					interfacedata.setSubentity(mail.getId()+"");									
//					interfacedata.setRestype("static");
//					interfacedata.setUnit("");
//					interfacedata.setThevalue(intreceieveflag+"");
//					interfacedata.setChname("接收服务");
//					mailvector.add(interfacedata);
//					
//					Hashtable collectHash = new Hashtable();
//					collectHash.put("mail", mailvector);
//					
//					mail.setStatus(3);
//					mail.setAlarm(true);
//					List alarmList = mail.getAlarmMessage();
//					if(alarmList == null)alarmList = new ArrayList();
//					
//					createEvent(mailconfig, reason);
//				    try{
//						AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
//						List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(mailconfig.getId()), AlarmConstant.TYPE_SERVICE, "mail");
//						for(int k = 0 ; k < list.size() ; k ++){
//							AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(k);
//							//对邮件服务值进行告警检测
//							CheckEventUtil checkutil = new CheckEventUtil();
//							checkutil.updateData(mail,collectHash,"service","mail",alarmIndicatorsnode);
//							//}
//						}
//				    }catch(Exception e){
//				    	e.printStackTrace();
//				    }
				}
				//保存进历史数据
				emailmonitor_history.setEmail_id(mailconfig.getId());
				emailmonitor_history.setIs_canconnected(iscanconnected);
				emailmonitor_history.setMon_time(Calendar.getInstance());
				emailmonitor_history.setReason(reason);
				emailmonitor_historyDao = new Emailmonitor_historyDao();
				try{
					emailmonitor_historyDao.save(emailmonitor_history);
				}catch(Exception e){
					
				}
				if (sendeddata.containsKey("emailserver:" + mailconfig.getId())) {
					sendeddata.remove("emailserver:" + mailconfig.getId());
				}
				
				//实时数据
				Emailmonitor_realtime emailmonitor_realtime = new Emailmonitor_realtime();
				emailmonitor_realtime.setEmail_id(mailconfig.getId());
				emailmonitor_realtime.setIs_canconnected(iscanconnected);
				emailmonitor_realtime.setMon_time(Calendar.getInstance());
				emailmonitor_realtime.setReason(reason);
				
				if(old==true){
					emailmonitor_realtime.setId(emailold.getId());
					emailmonitor_realtime.setSms_sign(1);
				}
				else{
					emailmonitor_realtime.setSms_sign(smssign);
				}
				//保存realtime
				if (old == true) {
					emailmonitor_realtimeDao = new Emailmonitor_realtimeDao();
					try{
						emailmonitor_realtimeDao.update(emailmonitor_realtime);
					}catch(Exception e){
						
					}finally{
						emailmonitor_realtimeDao.close();
					}
				}
				if (old == false) {
					emailmonitor_realtimeDao = new Emailmonitor_realtimeDao();
					try{
						emailmonitor_realtimeDao.save(emailmonitor_realtime);
					}catch(Exception e){
						
					}finally{
						emailmonitor_realtimeDao.close();
					}
					
				}
				
				}catch(Exception e){
					e.printStackTrace();
				}
				finally{
					emailmonitor_historyDao.close();
					emailmonitor_realtimeDao.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			finally{
			}
	}
	
	public sendMail createSendMail(){
		sendMail sendmail = new sendMail();
		return sendmail;
	}
	
	public ReceiveMail createReceiveMail(){
		ReceiveMail receiveMail = new ReceiveMail();
		return receiveMail;
	}
	
	public String configAddress(EmailMonitorConfig mailCnfig){
		String fromAddress = "";
		if (mailCnfig.getAddress().indexOf("mail")== -1 && mailCnfig.getAddress().indexOf("smtp")== -1){
			//为IP地址
			fromAddress = mailCnfig.getAddress();
		}else{
			//为接受服务器(pop) 或者 为发送服务器(smtp)
			fromAddress = mailCnfig.getAddress().substring(5,mailCnfig.getAddress().length());
		}
		return fromAddress;
	}
	
	public boolean configSendMail(sendMail sendmail, EmailMonitorConfig mailConfig) throws AddressException{
		Address[] ccAddress = {new InternetAddress("hukelei@dhcc.com.cn"),new InternetAddress("rhythm333@163.com")};
		sendmail.setMailaddress(configAddress(mailConfig));
		System.out.println(configAddress(mailConfig));
		sendmail.setSendmail(mailConfig.getUsername());
		sendmail.setSendpasswd(mailConfig.getPassword());
		sendmail.setToAddr(mailConfig.getRecivemail());
		sendmail.setBody("邮件服务测试");
		sendmail.setSubject("邮件服务设置");
		sendmail.setFromAddr(mailConfig.getUsername()+"@"+mailConfig.getAddress());
		sendmail.setCcAddress(ccAddress);
		return true;
	}
	
	public void createEvent(EmailMonitorConfig mailconfig, String reason){
		Calendar date=Calendar.getInstance();
		String time = sdf.format(date.getTime());
		EventList event = new EventList();
		event.setEventtype("mailserver");
		event.setEventlocation(mailconfig.getName());
		event.setBusinessid(mailconfig.getBid());
		event.setManagesign(new Integer(0));
		event.setReportman("系统轮询");
		event.setRecordtime(new GregorianCalendar());	
		String errorcontent = time+" "+"邮件服务("+mailconfig.getName()+" IP:"+mailconfig.getIpaddress()+")"+"的邮件服务停止";
		event.setContent(errorcontent);
		Integer level = new Integer(2);
		event.setLevel1(level);
		Vector eventtmpV = new Vector();
		eventtmpV.add(event);
		//EventListDao eventListDao = null ;
		try{
			//eventListDao = new EventListDao();
			//eventListDao.save(event);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//eventListDao.close();
		}
		try{
			createSMS("mailserver",mailconfig.getId()+"",errorcontent,mailconfig.getName());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	 public void createSMS(String mailserver,String mail_id,String errmsg,String mailstr){
	 	//建立短信		 	
	 	//从内存里获得当前这个IP的PING的值
	 	Calendar date=Calendar.getInstance();
	 	try{
 			if (!sendeddata.containsKey(mailserver+":"+mail_id)){
 				//若不在，则建立短信，并且添加到发送列表里
	 			Smscontent smscontent = new Smscontent();
	 			smscontent.setMessage(errmsg);
	 			smscontent.setObjid(mail_id);
	 			Calendar _tempCal = Calendar.getInstance();				
				Date _cc = _tempCal.getTime();
				String _time = sdf.format(_cc);
	 			smscontent.setRecordtime(_time);
	 			smscontent.setSubtype("mail");
	 			smscontent.setLevel(3+"");
	 			//发送短信
	 			SmscontentDao smsmanager=new SmscontentDao();
	 			smsmanager.sendURLSmscontent(smscontent);	
				sendeddata.put(mailserver+":"+mail_id,date);		 					 				
 			}else{
 				//若在，则从已发送短信列表里判断是否已经发送当天的短信 				
 				Calendar formerdate =(Calendar)sendeddata.get(mailserver+":"+mail_id);		 				
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
	 				//超过一天，则再发信息
		 			Smscontent smscontent = new Smscontent();
		 			//String time = sdf.format(date.getTime());
		 			smscontent.setMessage(errmsg.replace("&", " "));
		 			smscontent.setObjid(mail_id);
		 			Calendar _tempCal = Calendar.getInstance();				
					Date _cc = _tempCal.getTime();
					String _time = sdf.format(_cc);
		 			smscontent.setRecordtime(_time);
		 			smscontent.setSubtype("mail");
		 			smscontent.setLevel(3+"");
		 			//发送短信
		 			SmscontentDao smsmanager=new SmscontentDao();
		 			smsmanager.sendURLSmscontent(smscontent);
					//修改已经发送的短信记录	
					sendeddata.put(mailserver+":"+mail_id,date);	
		 		}else{
		 			//则写声音告警数据
					//向声音告警表里写数据
					AlarmInfo alarminfo = new AlarmInfo();
					alarminfo.setContent(errmsg.replace("&", " "));
					alarminfo.setIpaddress(mailstr);
					alarminfo.setLevel1(new Integer(2));
					alarminfo.setRecordtime(Calendar.getInstance());
					AlarmInfoDao alarmdao = new AlarmInfoDao();
					alarmdao.save(alarminfo);
		 		}
 			}	 			 			 			 			 	
	 	}catch(Exception e){
	 		e.printStackTrace();
	 	}
	 }
	
}
