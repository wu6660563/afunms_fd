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

import org.apache.commons.net.ftp.FTPClient;

import com.afunms.application.dao.Ftpmonitor_historyDao;
import com.afunms.application.dao.Ftpmonitor_realtimeDao;
import com.afunms.application.manage.FTPManager;
import com.afunms.application.model.FTPConfig;
import com.afunms.application.model.Ftpmonitor_history;
import com.afunms.application.model.Ftpmonitor_realtime;
import com.afunms.common.util.ShareData;
import com.afunms.event.dao.AlarmInfoDao;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.AlarmInfo;
import com.afunms.event.model.EventList;
import com.afunms.event.model.Smscontent;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Ftp;
import com.afunms.polling.node.Host;
import com.afunms.polling.node.Mail;
import com.afunms.polling.om.Pingcollectdata;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class FTPTask extends MonitorTask {
	private Hashtable sendeddata = ShareData.getSendeddata();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	
	
	public FTPTask(){
		super();
	}
		
	public void run(){
		Ftpmonitor_realtimeDao ftpmonitor_realtimeDao = null;
		Ftpmonitor_historyDao ftpmonitor_historyDao = null ;
			try{
				
			List<FTPConfig> ftpConfigList = new ArrayList<FTPConfig>();
//			FTPManager ftpManager = new FTPManager();
//			ftpConfigList = ftpManager.getFTPConfigListByMonflag(1);
			Calendar date=Calendar.getInstance();
			//System.out.println("realHash is "+realHash.size());
			for (int i = 0; i < ftpConfigList.size(); i++) {
				try{
					ftpmonitor_historyDao = new Ftpmonitor_historyDao();
					ftpmonitor_realtimeDao = new Ftpmonitor_realtimeDao();
					Hashtable realHash=ftpmonitor_realtimeDao.getAllReal();
					Integer iscanconnected = new Integer(0+"");
					Ftpmonitor_realtime ftpmonitor_realtimeold = new Ftpmonitor_realtime();
					String reason= "";
					FTPConfig ftpConfig = ftpConfigList.get(i);
					Ftp ftp = (Ftp)PollingEngine.getInstance().getFtpByID(ftpConfig.getId());	
					if(ftp == null){
						continue;
					}
				if(ftp != null){
					//��ʼ�������ӵ�FTP״̬
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
			
				boolean downloadflag = false;
				boolean uploadsuccess=false;
				try{
					downloadflag = ftputil.ftpOne();
					uploadsuccess=ftputil.uploadFile(ftpConfig.getIpaddress(), ftpConfig.getUsername(), ftpConfig.getPassword(), ResourceCenter.getInstance().getSysPath()+"/ftpupload/ftpupload.txt");
					System.out.println("sucess!sucess!sucess!sucess!!!!!!!!!!!!!!!!"+uploadsuccess+"flag!"+downloadflag);
					if (downloadflag && uploadsuccess){
						reason="FTP������Ч";
						iscanconnected=new Integer(1);	
					}else{
						//��Ҫ�����ʼ��������ڵķ������Ƿ�����ͨ
						Host host = (Host)PollingEngine.getInstance().getNodeByIP(ftpConfig.getIpaddress());
						Vector ipPingData = (Vector)ShareData.getPingdata().get(ftpConfig.getIpaddress());
						if(ipPingData != null){
							Pingcollectdata pingdata = (Pingcollectdata)ipPingData.get(0);
							Calendar tempCal = (Calendar)pingdata.getCollecttime();							
							Date cc = tempCal.getTime();
							time = sdf.format(cc);		
							String lastTime = time;
							String pingvalue = pingdata.getThevalue();
							if(pingvalue == null || pingvalue.trim().length()==0)pingvalue="0";
							double pvalue = new Double(pingvalue);
							if(pvalue == 0){
								//�������������Ӳ���***********************************************
								ftp.setAlarm(true);
								ftp.setStatus(1);
								//dbnode.setStatus(3);
								List alarmList = ftp.getAlarmMessage();
								if(alarmList == null)alarmList = new ArrayList();
								ftp.getAlarmMessage().add("FTP���ط�����Ч");
					            String sysLocation = "";
					              try{
					            	  SmscontentDao eventdao = new SmscontentDao();
					            	  String eventdesc = "FTP����("+ftpConfig.getName()+" IP:"+ftpConfig.getIpaddress()+")"+"��FTP����ֹͣ";
					            	  eventdao.createEventWithReasion("poll",ftpConfig.getId()+"",ftpConfig.getName()+"("+ftpConfig.getIpaddress()+")",eventdesc,3,"ftp","ping","���ڵķ��������Ӳ���");
					              }catch(Exception e){
					            	  e.printStackTrace();
					              }
							}else{
								ftp.setAlarm(true);
								ftp.setStatus(3);
								//dbnode.setStatus(3);
								List alarmList = ftp.getAlarmMessage();
								if(alarmList == null)alarmList = new ArrayList();
								ftp.getAlarmMessage().add("FTP���ط�����Ч");
								reason = "FTP������Ч";
								createEvent(ftpConfig, reason);
							}
							
						}else{
							ftp.setAlarm(true);
							ftp.setStatus(3);
							List alarmList = ftp.getAlarmMessage();
							if(alarmList == null)alarmList = new ArrayList();
							ftp.getAlarmMessage().add("FTP���ط�����Ч");
							reason = "FTP������Ч";
							createEvent(ftpConfig, reason);
						}
					}
					
				}catch(Exception ex){
					//���ܽ���FTP����
					ftp.setAlarm(true);
					ftp.setStatus(3);
					//dbnode.setStatus(3);
					List alarmList = ftp.getAlarmMessage();
					if(alarmList == null)alarmList = new ArrayList();
					ftp.getAlarmMessage().add("FTP���ط�����Ч");
					reason = "FTP������Ч";
					createEvent(ftpConfig, reason);
					
				}
				//�������ʷ����
				
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
				//ʵʱ����
				//ur.setUrl_id(url_id);
				if(old==true){
					ftpmonitor_realtime.setSms_sign(1);
				}
				else{
					ftpmonitor_realtime.setSms_sign(smssign);
				}
				
				
				//����realtime
				if (old == true) {
					ftpmonitor_realtime.setId(ftpmonitor_realtimeold.getId());
					ftpmonitor_realtimeDao.update(ftpmonitor_realtime);
				}
				if (old == false) {
					ftpmonitor_realtimeDao.save(ftpmonitor_realtime);
				}				
				
				//uh.setIs_refresh(ur.getIs_refresh());
				//uh.setIs_valid(ur.getIs_valid());
				
				if(sendeddata.containsKey("ftpserver:"+ftpConfig.getId())){
					sendeddata.remove("ftpserver:"+ftpConfig.getId());
				}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					ftpmonitor_historyDao.close();
					ftpmonitor_realtimeDao.close();
				}	
				
				
			}
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
		String errorcontent=time+" "+ftpConfig.getName()+"(IP:"+ftpConfig.getIpaddress()+")��FTP�������";		 		
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
		createSMS("ftpserver",ftpConfig.getId()+"",errorcontent,ftpConfig.getIpaddress());
	}
	

	 public void createSMS(String ftpserver,String ftp_id,String errmsg,String ftpstr){
	 	//��������		 	
	 	//���ڴ����õ�ǰ���IP��PING��ֵ
	 	Calendar date=Calendar.getInstance();
	 	try{
 			if (!sendeddata.containsKey(ftpserver+":"+ftp_id)){
 				//�����ڣ��������ţ�������ӵ������б���
	 			Smscontent smscontent = new Smscontent();
	 			smscontent.setMessage(errmsg);
	 			smscontent.setObjid(ftp_id);
	 			Calendar _tempCal = Calendar.getInstance();				
				Date _cc = _tempCal.getTime();
				String _time = sdf.format(_cc);
	 			smscontent.setRecordtime(_time);
	 			smscontent.setSubtype("ftp");
	 			smscontent.setLevel(3+"");
	 			//���Ͷ���
	 			SmscontentDao smsmanager=new SmscontentDao();
	 			smsmanager.sendURLSmscontent(smscontent);
				sendeddata.put(ftpserver+":"+ftp_id,date);		 					 				
 			}else{
 				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
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
	 				//����һ�죬���ٷ���Ϣ
		 			Smscontent smscontent = new Smscontent();
		 			//String time = sdf.format(date.getTime());
		 			smscontent.setMessage(errmsg);
		 			smscontent.setObjid(ftp_id);
		 			smscontent.setLevel(3+"");
		 			//���Ͷ���
		 			SmscontentDao smsmanager=new SmscontentDao();
		 			smsmanager.sendURLSmscontent(smscontent);
		 			Calendar _tempCal = Calendar.getInstance();				
					Date _cc = _tempCal.getTime();
					String _time = sdf.format(_cc);
		 			smscontent.setRecordtime(_time);
		 			smscontent.setSubtype("ftp");
					//�޸��Ѿ����͵Ķ��ż�¼	
					sendeddata.put(ftpserver+":"+ftp_id,date);	
		 		}else{
		 			//��д�����澯����
					//�������澯����д����
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
