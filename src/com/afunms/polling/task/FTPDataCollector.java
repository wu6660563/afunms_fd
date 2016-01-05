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

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.application.dao.FTPConfigDao;
import com.afunms.application.dao.Ftpmonitor_historyDao;
import com.afunms.application.dao.Ftpmonitor_realtimeDao;
import com.afunms.application.manage.FTPManager;
import com.afunms.application.model.FTPConfig;
import com.afunms.application.model.Ftpmonitor_history;
import com.afunms.application.model.Ftpmonitor_realtime;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.ShareData;
import com.afunms.event.dao.AlarmInfoDao;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.AlarmInfo;
import com.afunms.event.model.EventList;
import com.afunms.event.model.Smscontent;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Ftp;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.Pingcollectdata;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class FTPDataCollector {
	private Hashtable sendeddata = ShareData.getSendeddata();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	
	
	public FTPDataCollector(){
	}
		
	public void collect_data(String id,Hashtable gatherHash) {
		Ftpmonitor_realtimeDao ftpmonitor_realtimeDao = null;
		Ftpmonitor_historyDao ftpmonitor_historyDao = null ;
		try{
				
			List<FTPConfig> ftpConfigList = new ArrayList<FTPConfig>();
			FTPManager ftpManager = new FTPManager();
			ftpConfigList = ftpManager.getFTPConfigListByMonflag(1);
			Calendar date=Calendar.getInstance();
			FTPConfig ftpConfig = null;
			FTPConfigDao ftpconfigdao = null;
				try{
					ftpmonitor_historyDao = new Ftpmonitor_historyDao();
					ftpmonitor_realtimeDao = new Ftpmonitor_realtimeDao();
					Hashtable realHash=ftpmonitor_realtimeDao.getAllReal();
					Integer iscanconnected = new Integer(0+"");
					Ftpmonitor_realtime ftpmonitor_realtimeold = new Ftpmonitor_realtime();
					String reason= "";
					ftpconfigdao = new FTPConfigDao();
					//FTPConfig ftpConfig = null;
					try{
						ftpConfig = (FTPConfig)ftpconfigdao.findByID(id);
					}catch(Exception e){
						
					}finally{
						ftpconfigdao.close();
					}
					Ftp ftp = (Ftp)PollingEngine.getInstance().getFtpByID(ftpConfig.getId());	
					if(ftp == null){
						return;
					}
				if(ftp != null){
					//初始化被监视的FTP状态
					ftp.setStatus(0);
					ftp.setAlarm(false);
					ftp.getAlarmMessage().clear();
					Calendar _tempCal = Calendar.getInstance();				
					Date _cc = _tempCal.getTime();
					String _time = sdf.format(_cc);
					ftp.setLastTime(_time);
				}
				
				
				Integer ftpConfig_id = ftpConfig.getId();
				Integer ftp_id = ftpConfig.getId();
				boolean old=false;
				String str = "";
				Integer smssign=new Integer(0);
				if(realHash !=null&& realHash.get(ftp_id)!=null){
					old=true;
					ftpmonitor_realtimeold=(Ftpmonitor_realtime)realHash.get(ftp_id);
					smssign=ftpmonitor_realtimeold.getSms_sign();
				}
				FtpUtil ftputil = new FtpUtil(ftpConfig.getIpaddress(),21,ftpConfig.getUsername(),ftpConfig.getPassword(),"",ResourceCenter.getInstance().getSysPath()+"/ftpdownload/",ftpConfig.getFilename());
				String time = sdf.format(date.getTime());
			
				boolean downloadflag = true;
				int downflagint = 0;
				boolean uploadsuccess=true;
				int uploadflagint = 0;
				try{
					if(gatherHash.containsKey("download")){
						downloadflag = ftputil.ftpOne();
						if(downloadflag)downflagint = 1;
					}
					
					if(gatherHash.containsKey("upload")){
						uploadsuccess=ftputil.uploadFile(ftpConfig.getIpaddress(), ftpConfig.getUsername(), ftpConfig.getPassword(), ResourceCenter.getInstance().getSysPath()+"/ftpupload/"+ftpConfig.getFilename());
						if(uploadsuccess)uploadflagint = 1;
					}
					
					if (downloadflag && uploadsuccess){
						reason="FTP服务有效";
						iscanconnected=new Integer(1);	
//					}else{
//						//需要增加邮件服务所在的服务器是否能连通
//						Host host = (Host)PollingEngine.getInstance().getNodeByIP(ftpConfig.getIpaddress());
//						Vector ipPingData = (Vector)ShareData.getPingdata().get(ftpConfig.getIpaddress());
//						if(ipPingData != null){
//							Pingcollectdata pingdata = (Pingcollectdata)ipPingData.get(0);
//							Calendar tempCal = (Calendar)pingdata.getCollecttime();							
//							Date cc = tempCal.getTime();
//							time = sdf.format(cc);		
//							String lastTime = time;
//							String pingvalue = pingdata.getThevalue();
//							if(pingvalue == null || pingvalue.trim().length()==0)pingvalue="0";
//							double pvalue = new Double(pingvalue);
//							if(pvalue == 0){
//								//主机服务器连接不上***********************************************
//								ftp.setAlarm(true);
//								ftp.setStatus(1);
//								//dbnode.setStatus(3);
//								List alarmList = ftp.getAlarmMessage();
//								if(alarmList == null)alarmList = new ArrayList();
//								ftp.getAlarmMessage().add("FTP下载服务无效");
//					            String sysLocation = "";
//					              try{
//					            	  SmscontentDao eventdao = new SmscontentDao();
//					            	  String eventdesc = "FTP服务("+ftpConfig.getName()+" IP:"+ftpConfig.getIpaddress()+")"+"的FTP服务停止";
//					            	  eventdao.createEventWithReasion("poll",ftpConfig.getId()+"",ftpConfig.getName()+"("+ftpConfig.getIpaddress()+")",eventdesc,3,"ftp","ping","所在的服务器连接不上");
//					              }catch(Exception e){
//					            	  e.printStackTrace();
//					              }
//							}else{
//								ftp.setAlarm(true);
//								ftp.setStatus(3);
//								//dbnode.setStatus(3);
//								List alarmList = ftp.getAlarmMessage();
//								if(alarmList == null)alarmList = new ArrayList();
//								ftp.getAlarmMessage().add("FTP下载服务无效");
//								reason = "FTP服务无效";
//								createEvent(ftpConfig, reason);
//							}
//							
//						}else{
//							ftp.setAlarm(true);
//							ftp.setStatus(3);
//							List alarmList = ftp.getAlarmMessage();
//							if(alarmList == null)alarmList = new ArrayList();
//							ftp.getAlarmMessage().add("FTP下载服务无效");
//							reason = "FTP服务无效";
//							createEvent(ftpConfig, reason);
//						}
					}
					
					
				}catch(Exception ex){
//					//不能进行FTP服务
//					ftp.setAlarm(true);
//					ftp.setStatus(3);
//					//dbnode.setStatus(3);
//					List alarmList = ftp.getAlarmMessage();
//					if(alarmList == null)alarmList = new ArrayList();
//					ftp.getAlarmMessage().add("FTP下载服务无效");
//					reason = "FTP服务无效";
//					createEvent(ftpConfig, reason);					
				}
				
				Vector ftpvector = new Vector();
				//开始设置采集值实体
				Interfacecollectdata interfacedata=new Interfacecollectdata();
				interfacedata.setIpaddress(ftp.getIpAddress());
				interfacedata.setCollecttime(date);
				interfacedata.setCategory("Ftp");
				interfacedata.setEntity("download");
				interfacedata.setSubentity(ftp.getId()+"");									
				interfacedata.setRestype("static");
				interfacedata.setUnit("");
				interfacedata.setThevalue(downflagint+"");
				interfacedata.setChname("下载服务");
				ftpvector.add(interfacedata);
				
				interfacedata=new Interfacecollectdata();
				interfacedata.setIpaddress(ftp.getIpAddress());
				interfacedata.setCollecttime(date);
				interfacedata.setCategory("Ftp");
				interfacedata.setEntity("upload");
				interfacedata.setSubentity(ftp.getId()+"");									
				interfacedata.setRestype("static");
				interfacedata.setUnit("");
				interfacedata.setThevalue(uploadflagint+"");
				interfacedata.setChname("上载服务");
				ftpvector.add(interfacedata);
				
				Hashtable collectHash = new Hashtable();
				collectHash.put("ftp", ftpvector);
				
//				mail.setStatus(3);
//				mail.setAlarm(true);
				
				//createEvent(mailconfig, reason);
			    try{
					AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
					List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(ftpConfig.getId()), AlarmConstant.TYPE_SERVICE, "ftp");
					for(int k = 0 ; k < list.size() ; k ++){
						AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(k);
						//对邮件服务值进行告警检测
						CheckEventUtil checkutil = new CheckEventUtil();
						checkutil.updateData(ftp,collectHash,"service","ftp",alarmIndicatorsnode);
						//}
					}
			    }catch(Exception e){
			    	e.printStackTrace();
			    }
				
				
				
				
				//保存进历史数据
				
				Ftpmonitor_history ftpMonitor_history = new Ftpmonitor_history();
				ftpMonitor_history.setFtp_id(ftpConfig.getId());
				ftpMonitor_history.setIs_canconnected(iscanconnected);
				ftpMonitor_history.setMon_time(Calendar.getInstance());
				ftpMonitor_history.setReason(reason);
				ftpmonitor_historyDao.save(ftpMonitor_history);	
				
				//UrlDataCollector udc = new UrlDataCollector();
				Ftpmonitor_realtime ftpmonitor_realtime = new Ftpmonitor_realtime();
				ftpmonitor_realtime.setFtp_id(ftpConfig.getId());
				ftpmonitor_realtime.setIs_canconnected(iscanconnected);
				ftpmonitor_realtime.setReason(reason);
				ftpmonitor_realtime.setMon_time(Calendar.getInstance());
				//实时数据
				//ur.setUrl_id(url_id);
				if(old==true){
					ftpmonitor_realtime.setSms_sign(1);
				}
				else{
					ftpmonitor_realtime.setSms_sign(smssign);
				}
				
				
				//保存realtime
				if (old == true) {
					ftpmonitor_realtime.setId(ftpmonitor_realtimeold.getId());
					ftpmonitor_realtimeDao.update(ftpmonitor_realtime);
				}
				if (old == false) {
					ftpmonitor_realtimeDao.save(ftpmonitor_realtime);
				}				
				
				//uh.setIs_refresh(ur.getIs_refresh());
				//uh.setIs_valid(ur.getIs_valid());
				
//				if(sendeddata.containsKey("ftpserver:"+ftpConfig.getId())){
//					sendeddata.remove("ftpserver:"+ftpConfig.getId());
//				}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					ftpmonitor_historyDao.close();
					ftpmonitor_realtimeDao.close();
				}	
				
				
			//}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				
			}		
	}
	
	public void createEvent(FTPConfig ftpConfig, String reason){
		Calendar date=Calendar.getInstance();
		String time = sdf.format(date.getTime());
		EventList event = new EventList();
		event.setEventtype("ftpserver");
		event.setEventlocation(ftpConfig.getIpaddress());
		event.setBusinessid(ftpConfig.getBid());
		event.setManagesign(new Integer(0));
		event.setReportman("monitorpc");
		event.setRecordtime(new GregorianCalendar());		
		String errorcontent=time+" "+ftpConfig.getName()+"(IP:"+ftpConfig.getIpaddress()+")：FTP服务故障";		 		
		event.setContent(errorcontent);
		Integer level = new Integer(2);
		event.setLevel1(level);
		//reason="FTP服务无效";
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
		createSMS("ftpserver",ftpConfig.getId()+"",errorcontent,ftpConfig.getIpaddress());
	}
	

	 public void createSMS(String ftpserver,String ftp_id,String errmsg,String ftpstr){
	 	//建立短信		 	
	 	//从内存里获得当前这个IP的PING的值
	 	Calendar date=Calendar.getInstance();
	 	try{
 			if (!sendeddata.containsKey(ftpserver+":"+ftp_id)){
 				//若不在，则建立短信，并且添加到发送列表里
	 			Smscontent smscontent = new Smscontent();
	 			smscontent.setMessage(errmsg);
	 			smscontent.setObjid(ftp_id);
	 			Calendar _tempCal = Calendar.getInstance();				
				Date _cc = _tempCal.getTime();
				String _time = sdf.format(_cc);
	 			smscontent.setRecordtime(_time);
	 			smscontent.setSubtype("ftp");
	 			smscontent.setLevel(3+"");
	 			//发送短信
	 			SmscontentDao smsmanager=new SmscontentDao();
	 			smsmanager.sendURLSmscontent(smscontent);
				sendeddata.put(ftpserver+":"+ftp_id,date);		 					 				
 			}else{
 				//若在，则从已发送短信列表里判断是否已经发送当天的短信
 				Calendar formerdate =(Calendar)sendeddata.get(ftpserver+":"+ftp_id);		 				
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
		 			smscontent.setMessage(errmsg);
		 			smscontent.setObjid(ftp_id);
		 			smscontent.setLevel(3+"");
		 			//发送短信
		 			SmscontentDao smsmanager=new SmscontentDao();
		 			smsmanager.sendURLSmscontent(smscontent);
		 			Calendar _tempCal = Calendar.getInstance();				
					Date _cc = _tempCal.getTime();
					String _time = sdf.format(_cc);
		 			smscontent.setRecordtime(_time);
		 			smscontent.setSubtype("ftp");
					//修改已经发送的短信记录	
					sendeddata.put(ftpserver+":"+ftp_id,date);	
		 		}else{
		 			//则写声音告警数据
					//向声音告警表里写数据
					AlarmInfo alarminfo = new AlarmInfo();
					alarminfo.setContent(errmsg);
					alarminfo.setIpaddress(errmsg);
					alarminfo.setLevel1(new Integer(2));
					alarminfo.setRecordtime(Calendar.getInstance());
					AlarmInfoDao alarmdao = new AlarmInfoDao();
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
	 }
	
	
}
