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

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.application.dao.Urlmonitor_historyDao;
import com.afunms.application.dao.Urlmonitor_realtimeDao;
import com.afunms.application.dao.WebConfigDao;
import com.afunms.application.model.Urlmonitor_history;
import com.afunms.application.model.Urlmonitor_realtime;
import com.afunms.application.model.WebConfig;
import com.afunms.application.util.UrlDataCollector;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.Smscontent;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Web;
import com.afunms.polling.om.Pingcollectdata;

public class WebDataCollector {
	WebConfigDao urldao = null;
	private Hashtable sendeddata = ShareData.getSendeddata();
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	public WebDataCollector() {
	}
	
	public void collect_data(String urlid,Hashtable gatherHash) {
		Urlmonitor_realtimeDao realtimeManager = new Urlmonitor_realtimeDao();
		try {
			
			List url_list = new ArrayList();
			urldao = new WebConfigDao();
			Hashtable realHash = realtimeManager.getAllReal();
			Calendar date = Calendar.getInstance();
			WebConfig uc = null;
			try{
				uc = (WebConfig) urldao.findByID(urlid);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				urldao.close();
			}
			
			if(uc != null){
			
			//����ʷ�澯���
			Web _web = (Web)PollingEngine.getInstance().getWebByID(uc.getId());	
			if(_web == null){
				return;
			}
			if(_web != null){
				//��ʼ��WEB���ʷ����״̬
				_web.setStatus(0);
				_web.setAlarm(false);
				_web.getAlarmMessage().clear();
				Calendar _tempCal = Calendar.getInstance();				
				Date _cc = _tempCal.getTime();
				String _time = sdf.format(_cc);
				_web.setLastTime(_time);
			}
			
			int url_id = uc.getId();
			boolean old = false;
			String str = "";
			Integer smssign = new Integer(0);
			Urlmonitor_realtime urold = null;
			if (realHash.get(url_id) != null) {
				old = true;
				urold = (Urlmonitor_realtime) realHash.get(url_id);
				str = urold.getPage_context();
				smssign = urold.getSms_sign();
			}
			UrlDataCollector udc = new UrlDataCollector();
			String contentStr = "";
			if (str != null && str.length() > 100) {
				contentStr = str.substring(0, 100);
			}

			Urlmonitor_realtime ur = udc.getUrlmonitor_realtime(uc, old,contentStr);
			// ʵʱ����
			ur.setUrl_id(url_id);
			if (old == true) {
				ur.setSms_sign(urold.getSms_sign());
			} else {
				ur.setSms_sign(smssign);
			}
			int pingflag = 0;
			int pageflag = 1;
			int keywordflag = 0;
			int condelayflag = 0;
			pingflag = ur.getIs_canconnected();
			//SysLogger.info(uc.getStr()+"@@@@@@@@@@@@@@@@@@@"+ur.getIs_canconnected());
				if (ur.getIs_canconnected() == 0) {
					//���Ӳ���
					//pingflag = 0;
//		 			//��Ҫ���ӷ������ڵķ������Ƿ�����ͨ
//					Vector ipPingData = (Vector)ShareData.getPingdata().get(uc.getIpAddress());
//					if(ipPingData != null){
//						Pingcollectdata pingdata = (Pingcollectdata)ipPingData.get(0);
//						String pingvalue = pingdata.getThevalue();
//						if(pingvalue == null || pingvalue.trim().length()==0)pingvalue="0";
//						double pvalue = new Double(pingvalue);
//						if(pvalue == 0){
//							//�������������Ӳ���***********************************************
//							_web.setAlarm(true);
//							_web.setStatus(3);
//							List alarmList = _web.getAlarmMessage();
//							if(alarmList == null)alarmList = new ArrayList();
//							_web.getAlarmMessage().add("WEB���ʷ���ֹͣ");
//				            String sysLocation = "";
//				              try{
//				            	  SmscontentDao eventdao = new SmscontentDao();
//				            	  String eventdesc = "WEB����("+uc.getAlias()+" ·��:"+uc.getStr()+")"+"�ķ��ʷ���ֹͣ";
//				            	  eventdao.createEventWithReasion("poll",uc.getId()+"",uc.getAlias()+"("+uc.getStr()+")",eventdesc,3,"web","ping","���ڵķ��������Ӳ���");
//				              }catch(Exception e){
//				            	  e.printStackTrace();
//				              }
//						}else{
//							_web.setAlarm(true);
//							_web.setStatus(3);
//							List alarmList = _web.getAlarmMessage();
//							if(alarmList == null)alarmList = new ArrayList();
//							_web.getAlarmMessage().add("WEB���ʷ���ֹͣ");
//						}
//						
//					}else{
//						_web.setAlarm(true);
//						_web.setStatus(3);
//						//dbnode.setStatus(3);
//						List alarmList = _web.getAlarmMessage();
//						if(alarmList == null)alarmList = new ArrayList();
//						_web.getAlarmMessage().add("WEB���ʷ���ֹͣ");
//					}				
//					ur.setKey_exist("WEB���ʷ���ֹͣ");
//					ur.setChange_rate("0.0");
//					createSMS("web", uc);
				} else if (uc.getMon_flag() == 2 && ur.getIs_refresh() == 0) {
					//���Ӳ���
					//pingflag = 0;
//					//��Ҫ����WEB�������ڵķ������Ƿ�����ͨ
//					Vector ipPingData = (Vector)ShareData.getPingdata().get(uc.getIpAddress());
//					if(ipPingData != null){
//						Pingcollectdata pingdata = (Pingcollectdata)ipPingData.get(0);
//						String pingvalue = pingdata.getThevalue();
//						if(pingvalue == null || pingvalue.trim().length()==0)pingvalue="0";
//						double pvalue = new Double(pingvalue);
//						if(pvalue == 0){
//							//�������������Ӳ���***********************************************
//							_web.setAlarm(true);
//							_web.setStatus(3);
//							List alarmList = _web.getAlarmMessage();
//							if(alarmList == null)alarmList = new ArrayList();
//							_web.getAlarmMessage().add("WEB���ʷ���ֹͣ");
//				            String sysLocation = "";
//				              try{
//				            	  SmscontentDao eventdao = new SmscontentDao();
//				            	  String eventdesc = "WEB����("+uc.getAlias()+" ·��:"+uc.getStr()+")"+"�ķ��ʷ���ֹͣ";
//				            	  eventdao.createEventWithReasion("poll",uc.getId()+"",uc.getAlias()+"("+uc.getStr()+")",eventdesc,3,"web","ping","���ڵķ��������Ӳ���");
//				              }catch(Exception e){
//				            	  e.printStackTrace();
//				              }
//						}else{
//							_web.setAlarm(true);
//							_web.setStatus(3);
//							List alarmList = _web.getAlarmMessage();
//							if(alarmList == null)alarmList = new ArrayList();
//							_web.getAlarmMessage().add("WEB���ʷ���ֹͣ");
//						}
//						
//					}else{
//						_web.setAlarm(true);
//						_web.setStatus(3);
//						List alarmList = _web.getAlarmMessage();
//						if(alarmList == null)alarmList = new ArrayList();
//						_web.getAlarmMessage().add("WEB���ʷ���ֹͣ");
//					}
//					ur.setKey_exist("WEB���ʷ���ֹͣ");
//					ur.setChange_rate("0.0");
//					createSMS("web", uc);
				} else {
					//pingflag = 1;
					condelayflag = ur.getCondelay();
					String[] keyword = uc.getKeyword().trim().split(",");
					
					//�ж�http���ݰ���С
					if(gatherHash.containsKey("pagesize")){
						if(Integer.parseInt(ur.getPagesize())<Integer.parseInt(uc.getPagesize_min())){
							pageflag = 0;
//							_web.setAlarm(true);
//							_web.setStatus(3);
//							List alarmList = _web.getAlarmMessage();
//							if(alarmList == null)alarmList = new ArrayList();
//							_web.getAlarmMessage().add("WEB���ʷ��񷵻ص����ݰ�С��ʵ��ֵ");
//				            String sysLocation = "";
//				              try{
//				            	  SmscontentDao eventdao = new SmscontentDao();
//				            	  String eventdesc = "WEB����("+uc.getAlias()+" ·��:"+uc.getStr()+")"+"�ķ�����ʷ������ݰ���СΪ"+ur.getPagesize()+"KB С��ʵ��ֵ"+uc.getPagesize_min();
//				            	  eventdao.createEventWithReasion("poll",uc.getId()+"",uc.getAlias()+"("+uc.getStr()+")",eventdesc,3,"web","http","");
//				              }catch(Exception e){
//				            	  e.printStackTrace();
//				              }
						}else{
							pageflag = 1;
						}
					}
					//�ؼ��ּ��
					if(gatherHash.containsKey("keyword")){
						String result = "WEB���ʷ���("+uc.getStr()+")δ��⵽�ؼ��� ";
						String key = "";
						int times = 0;
						if(keyword!=null&&keyword.length>0){
							//if(ur.getKey_exist()!=null&&!"".equals(ur.getKey_exist())){
							if(ur.getKey_exist()!=null&&!"".equals(ur.getPage_context())){
								for(int j=0;j<keyword.length;j++){
									if(!ur.getPage_context().contains(keyword[j])){
										key = key+keyword[j]+";";
										times++;
									}
								}
							}
						}
						
						keywordflag = times*100/keyword.length;
						
						ur.setChange_rate(""+times*100/keyword.length);
						if("".equals(key)){
							ur.setKey_exist("���йؼ��ֳɹ����!");
						} else {
							ur.setKey_exist(result+key);
//							_web.setAlarm(true);
//							_web.setStatus(3);
//							List alarmList = _web.getAlarmMessage();
//							if(alarmList == null)alarmList = new ArrayList();
//							_web.getAlarmMessage().add("WEB���ʷ���δ��⵽�ؼ���");
//				            String sysLocation = "";
//				              try{
//				            	  SmscontentDao eventdao = new SmscontentDao();
//				            	  String eventdesc = "WEB����("+uc.getAlias()+" ·��:"+uc.getStr()+")"+"δ��⵽�ؼ��֣�"+key;
//				            	  eventdao.createEventWithReasion("poll",uc.getId()+"",uc.getAlias()+"("+uc.getStr()+")",eventdesc,3,"web","http","");
//				              }catch(Exception e){
//				            	  e.printStackTrace();
//				              }
						}
					}else{
						ur.setKey_exist("δ���ùؼ��ּ��!");
					}
				}
				
				Vector webvector = new Vector();
				
				Pingcollectdata hostdata=null;
				hostdata=new Pingcollectdata();
				hostdata.setIpaddress(uc.getIpAddress());
				//Calendar date=Calendar.getInstance();
				hostdata.setCollecttime(date);
				hostdata.setCategory("WEBPing");
				hostdata.setEntity("Utilization");
				hostdata.setSubentity("ConnectUtilization");
				hostdata.setRestype("dynamic");
				hostdata.setUnit("%");
				if(pingflag == 1){
					hostdata.setThevalue("100");
				}else{
					hostdata.setThevalue("0");
				}
				webvector.add(hostdata);
				

				
				//ҳ���С
				hostdata=new Pingcollectdata();
				hostdata.setIpaddress(uc.getIpAddress());
				//Calendar date=Calendar.getInstance();
				hostdata.setCollecttime(date);
				hostdata.setCategory("WEBPagesize");
				hostdata.setEntity("WEBPagesize");
				hostdata.setSubentity("ConnectUtilization");
				hostdata.setRestype("dynamic");
				hostdata.setUnit("");
				if(pageflag == 0){
					//С�����õ�ҳ���С
					hostdata.setThevalue("0");
				}else{
					hostdata.setThevalue("1");
				}
				webvector.add(hostdata);
				
				//�ؼ���
				hostdata=new Pingcollectdata();
				hostdata.setIpaddress(uc.getIpAddress());
				//Calendar date=Calendar.getInstance();
				hostdata.setCollecttime(date);
				hostdata.setCategory("WEBKeyword");
				hostdata.setEntity("WEBKeyword");
				hostdata.setSubentity("ConnectUtilization");
				hostdata.setRestype("dynamic");
				hostdata.setUnit("%");
				hostdata.setThevalue(keywordflag+"");
				webvector.add(hostdata);
				
				
				//��Ӧʱ��
				hostdata=new Pingcollectdata();
				hostdata.setIpaddress(uc.getIpAddress());
				//Calendar date=Calendar.getInstance();
				hostdata.setCollecttime(date);
				hostdata.setCategory("WEBResponsetime");
				hostdata.setEntity("WEBResponsetime");
				hostdata.setSubentity("WEBResponsetime");
				hostdata.setRestype("dynamic");
				hostdata.setUnit("ms");
				hostdata.setThevalue(condelayflag+"");
				webvector.add(hostdata);
				
				Hashtable collectHash = new Hashtable();
				collectHash.put("url", webvector);
			    try{
					AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
					List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(uc.getId()), AlarmConstant.TYPE_SERVICE, "url");
					for(int k = 0 ; k < list.size() ; k ++){
						AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(k);
						//��SOCKET����ֵ���и澯���
						CheckEventUtil checkutil = new CheckEventUtil();
						checkutil.updateData(_web,collectHash,"service","url",alarmIndicatorsnode);
						//}
					}
			    }catch(Exception e){
			    	e.printStackTrace();
			    }
				
				// ����realtime
				String _pagecontext = ur.getPage_context();
				if (_pagecontext != null && _pagecontext.length() > 100) {
					ur.setPage_context(_pagecontext.substring(0, 100));
				}
				realtimeManager.close();
				realtimeManager = new Urlmonitor_realtimeDao();
				if (old == true) {
					ur.setId(urold.getId());
					ur.setMon_time(Calendar.getInstance());					
					realtimeManager.update(ur);
				}
				if (old == false) {					
					realtimeManager.save(ur);
				}
				
				// �������ʷ����
				Urlmonitor_history uh = new Urlmonitor_history();
				Urlmonitor_historyDao historyManager = new Urlmonitor_historyDao();
				uh.setUrl_id(ur.getUrl_id());
				//SysLogger.info(uc.getStr()+"========="+ur.getIs_canconnected());
				uh.setIs_canconnected(ur.getIs_canconnected());
				uh.setIs_refresh(ur.getIs_refresh());
				uh.setIs_valid(ur.getIs_valid());
				uh.setMon_time(ur.getMon_time());
				uh.setReason(ur.getReason());
				uh.setCondelay(ur.getCondelay());
				uh.setKey_exist(ur.getKey_exist());
				uh.setPagesize(ur.getPagesize());
				uh.setChange_rate(ur.getChange_rate());
				try{
					historyManager.save(uh);
				}catch(Exception e){
					
				}finally{
					historyManager.close();
				}

//				if (sendeddata.containsKey("webserver:" + url_id)) {
//					sendeddata.remove("webserver:" + url_id);
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}finally{
			realtimeManager.close();
			urldao.close();
			SysLogger.info("********URL Thread Count : "+Thread.activeCount());
		}
	}
	 public void createSMS(String web,WebConfig webconfig){
	 	//��������		 	
	 	//���ڴ����õ�ǰ���IP��PING��ֵ
	 	Calendar date=Calendar.getInstance();
	 	try{
 			if (!sendeddata.containsKey(web+":"+webconfig.getId())){
 				//�����ڣ��������ţ�������ӵ������б���
	 			Smscontent smscontent = new Smscontent();
	 			String time = sdf.format(date.getTime());
	 			smscontent.setLevel("3");
	 			smscontent.setObjid(webconfig.getId()+"");
	 			smscontent.setMessage(webconfig.getAlias()+" ("+webconfig.getStr()+")"+"�ķ��ʷ���ֹͣ");
	 			smscontent.setRecordtime(time);
	 			smscontent.setSubtype("web");
	 			smscontent.setSubentity("ping");
	 			smscontent.setIp(webconfig.getStr());
	 			//���Ͷ���
	 			SmscontentDao smsmanager=new SmscontentDao();
	 			smsmanager.sendURLSmscontent(smscontent);	
				sendeddata.put(web+":"+webconfig.getId(),date);	
 			}else{
 				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
 				Calendar formerdate =(Calendar)sendeddata.get(web+":"+webconfig.getId());		 				
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
		 			String time = sdf.format(date.getTime());
		 			smscontent.setLevel("3");
		 			smscontent.setObjid(webconfig.getId()+"");
		 			smscontent.setMessage(webconfig.getAlias()+" ("+webconfig.getStr()+")"+"�ķ��ʷ���ֹͣ");
		 			smscontent.setRecordtime(time);
		 			smscontent.setSubtype("web");
		 			smscontent.setSubentity("ping");
		 			smscontent.setIp(webconfig.getStr());
		 			//���Ͷ���
		 			SmscontentDao smsmanager=new SmscontentDao();
		 			smsmanager.sendURLSmscontent(smscontent);
					//�޸��Ѿ����͵Ķ��ż�¼	
		 			sendeddata.put(web+":"+webconfig.getId(),date);	
		 		}	
 			}	 			 			 			 			 	
	 	}catch(Exception e){
	 		e.printStackTrace();
	 	}
	 }
	

	 
}
