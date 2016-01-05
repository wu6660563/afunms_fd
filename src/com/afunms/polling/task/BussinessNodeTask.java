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

import com.afunms.application.dao.Urlmonitor_realtimeDao;
import com.afunms.application.model.WebConfig;
import com.afunms.application.util.UrlDataCollector;
import com.afunms.business.dao.BusCollectTypeDao;
import com.afunms.business.dao.BusinessNodeDao;
import com.afunms.business.model.BusCollectType;
import com.afunms.business.model.BusinessNode;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.ShareData;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.Smscontent;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;


/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BussinessNodeTask extends MonitorTask{
	private Hashtable sendeddata = ShareData.getSendeddata();
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	public BussinessNodeTask() {
		//urldao = new WebConfigDao();
	}
	
	public void run() {
		Urlmonitor_realtimeDao realtimeManager = new Urlmonitor_realtimeDao();
		try {
			
			List bnodelist = new ArrayList();
			Hashtable url_ht = new Hashtable();
			BusinessNodeDao bnodedao = new BusinessNodeDao();
			try{
				bnodelist = bnodedao.getBussinessByFlag(1);
			}catch(Exception e){
				//e.printStackTrace();
			}finally{
				bnodedao.close();
			}
			
			Hashtable realHash = realtimeManager.getAllReal();
			Calendar date = Calendar.getInstance();
			
			if(null != bnodelist)
			for (int i = 0; i < bnodelist.size(); i++) {
				BusinessNode bnode = (BusinessNode) (bnodelist.get(i));
				BusCollectTypeDao dao = new BusCollectTypeDao();
				BusCollectType bct = null;
				try{
					bct = (BusCollectType)dao.getCollectTypeById(bnode.getCollecttype());
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					dao.close();
				}
				if(bct.getCollecttype().equals("http")){
					//HTTP�Ĳɼ���ʽ
					UrlDataCollector udc = new UrlDataCollector();
					String contentStr = "";
//					String str = udc.getUrlmonitor_realtime(bnode.getMethod(), true, true, "");
//					if (str != null && str.length() > 100) {
//						contentStr = str.substring(0, 100);
//					}
					Calendar coltime = Calendar.getInstance();
					String[] retValue = udc.getUrlmonitor_realtime(bnode.getMethod(), true, true, "");
					if(retValue != null && retValue.length == 2){
						insertsql(bnode,retValue,coltime);
						if(!"1".equals(retValue[0])){
						    try {
				            	  SmscontentDao eventdao = new SmscontentDao();
				            	  String eventdesc = bnode.getDesc()+"("+bnode.getName()+" ·��:"+bnode.getMethod()+")"+"�ķ��ʷ���ֹͣ";
				            	  eventdao.createEventWithReasion("poll",bnode.getBid()+"",bnode.getName()+"("+bnode.getMethod()+")",eventdesc,3,"bus","ping","���ڵķ��������Ӳ���");
				            } catch(Exception e) {
				            	  e.printStackTrace();
				            }finally{
				            	
				            }
				            Node node = PollingEngine.getInstance().getBusByID(bnode.getBid());
				            node.setAlarm(true);
						}
					}
				}
				
				
//				
//				//����ʷ�澯���
//				Web _web = (Web)PollingEngine.getInstance().getWebByID(uc.getId());	
//				if(_web == null){
//					return;
//				}
//				if(_web != null){
//					//��ʼ��WEB���ʷ����״̬
//					_web.setStatus(0);
//					_web.setAlarm(false);
//					_web.getAlarmMessage().clear();
//					Calendar _tempCal = Calendar.getInstance();				
//					Date _cc = _tempCal.getTime();
//					String _time = sdf.format(_cc);
//					_web.setLastTime(_time);
//				}
//				int url_id = uc.getId();
//				boolean old = false;
//				String str = "";
//				Integer smssign = new Integer(0);
//				Urlmonitor_realtime urold = null;
//				if (realHash.get(url_id) != null) {
//					old = true;
//					urold = (Urlmonitor_realtime) realHash.get(url_id);
//					str = urold.getPage_context();
//					smssign = urold.getSms_sign();
//				}
//				UrlDataCollector udc = new UrlDataCollector();
//				String contentStr = "";
//				if (str != null && str.length() > 100) {
//					contentStr = str.substring(0, 100);
//				}
//				Urlmonitor_realtime ur = udc.getUrlmonitor_realtime(uc, old,contentStr);
//				// ʵʱ����
//				ur.setUrl_id(url_id);
//				if (old == true) {
//					ur.setSms_sign(urold.getSms_sign());
//				} else {
//					ur.setSms_sign(smssign);
//				}
//
//				String time = sdf.format(date.getTime());
//				//if (uc.getMon_flag() == 1 && ur.getIs_canconnected() == 0) {
//				if (ur.getIs_canconnected() == 0) {
//					String errorcontent = time + "&" + url_id + "&"+ uc.getAlias() + "���Ӵ���" + ur.getReason();
//					
//		 			//��Ҫ�����ʼ��������ڵķ������Ƿ�����ͨ
//					Host host = (Host)PollingEngine.getInstance().getNodeByIP(uc.getIpAddress());
//					Vector ipPingData = (Vector)ShareData.getPingdata().get(uc.getIpAddress());
//					if(ipPingData != null){
//						Pingcollectdata pingdata = (Pingcollectdata)ipPingData.get(0);
//						Calendar tempCal = (Calendar)pingdata.getCollecttime();							
//						//Date cc = tempCal.getTime();
//						//String time = sdf.format(cc);		
//						String lastTime = time;
//						String pingvalue = pingdata.getThevalue();
//						if(pingvalue == null || pingvalue.trim().length()==0)pingvalue="0";
//						double pvalue = new Double(pingvalue);
//						if(pvalue == 0){
//							//�������������Ӳ���***********************************************
//							_web.setAlarm(true);
//							_web.setStatus(1);
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
//					createSMS("web", uc);
//				}else if (uc.getMon_flag() == 2
//						&& ur.getIs_refresh() == 0) {
//					String errorcontent = "";
//					if (ur.getIs_canconnected() == 0) {
//						errorcontent = time + "&" + url_id + "&"+ uc.getAlias() + "���Ӵ���" + ur.getReason();
//					} else {
//						errorcontent = time + "&" + url_id + "&"+ uc.getAlias() + "���Ӵ���ҳ�治ˢ��";
//					}
//					//��Ҫ�����ʼ��������ڵķ������Ƿ�����ͨ
//					Host host = (Host)PollingEngine.getInstance().getNodeByIP(uc.getIpAddress());
//					Vector ipPingData = (Vector)ShareData.getPingdata().get(uc.getIpAddress());
//					if(ipPingData != null){
//						Pingcollectdata pingdata = (Pingcollectdata)ipPingData.get(0);
//						Calendar tempCal = (Calendar)pingdata.getCollecttime();							
//						//Date cc = tempCal.getTime();
//						//String time = sdf.format(cc);		
//						String lastTime = time;
//						String pingvalue = pingdata.getThevalue();
//						if(pingvalue == null || pingvalue.trim().length()==0)pingvalue="0";
//						double pvalue = new Double(pingvalue);
//						if(pvalue == 0){
//							//�������������Ӳ���***********************************************
//							_web.setAlarm(true);
//							_web.setStatus(1);
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
//					
//					
//					
//					createSMS("web", uc);
//				} else {
//					// ur.setSms_sign(new Integer(0));
//				}
//				// ����realtime
//				String _pagecontext = ur.getPage_context();
//				if (_pagecontext != null && _pagecontext.length() > 100) {
//					ur.setPage_context(_pagecontext.substring(0, 100));
//				}
//				if (old == true) {
//					ur.setId(urold.getId());
//					ur.setMon_time(Calendar.getInstance());
//					
//					realtimeManager.update(ur);
//					realtimeManager = new Urlmonitor_realtimeDao();
//				}
//				if (old == false) {
//					
//					realtimeManager.save(ur);
//					realtimeManager = new Urlmonitor_realtimeDao();
//				}
//				// �������ʷ����
//				Urlmonitor_history uh = new Urlmonitor_history();
//				Urlmonitor_historyDao historyManager = new Urlmonitor_historyDao();
//				uh.setUrl_id(ur.getUrl_id());
//				uh.setIs_canconnected(ur.getIs_canconnected());
//				uh.setIs_refresh(ur.getIs_refresh());
//				uh.setIs_valid(ur.getIs_valid());
//				uh.setMon_time(ur.getMon_time());
//				uh.setReason(ur.getReason());
//				uh.setCondelay(ur.getCondelay());
//				historyManager.save(uh);
//				if (sendeddata.containsKey("webserver:" + url_id)) {
//					sendeddata.remove("webserver:" + url_id);
//				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}finally{
			realtimeManager.close();
			//urldao.close();
			System.out.println("********BussinessNodeTask Thread Count : "+Thread.activeCount());
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
	 			smscontent.setLevel("2");
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
		 			smscontent.setLevel("2");
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
	
    public boolean insertsql(BusinessNode bndoe,String[] retValue,Calendar coltime){
        boolean flag = true;
		String dbname = "bnode"+bndoe.getBid()+"_"+bndoe.getId();						
		Date cc = coltime.getTime();
		String time = sdf.format(cc);
		String sql = "insert into "+dbname+"(thevalue,responsetime,collecttime)values('"+retValue[0]+"','"+retValue[1]+"','"+time+"')";
		 //SysLogger.info(sql);
		DBManager dbmanager = null;
		try {
            dbmanager = new DBManager();
			dbmanager.executeUpdate(sql);
        } catch(Exception ex) {
            ex.printStackTrace();
		} finally {
				dbmanager.close();
		}
		return flag;
    }
    
}
