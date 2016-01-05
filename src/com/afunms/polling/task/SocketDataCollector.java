package com.afunms.polling.task;

import java.sql.Connection;
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
import com.afunms.application.dao.*;
import com.afunms.application.model.*;
import com.afunms.application.tomcatmonitor.ServerStream;

import com.afunms.event.model.*;
import com.afunms.event.dao.*;
import com.afunms.event.api.*;



import com.afunms.common.util.*;

import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.*;


import org.apache.commons.beanutils.BeanUtils;



/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SocketDataCollector {
	
	private Hashtable sendeddata = ShareData.getSendeddata();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public SocketDataCollector() {
	}
	
	public void collect_data(String id,Hashtable gatherHash) {
		
		PSTypeDao dao = new PSTypeDao();
		try{
			//Hashtable data_ht = new Hashtable();
    		Socketmonitor_realtimeDao realtimeManager = new Socketmonitor_realtimeDao();
    			try {
    				ServerStream serverstream = new ServerStream();
    				Hashtable returnVal = new Hashtable();				
    				boolean flag =false;
    				String ipaddress = "";
        			PSTypeVo vo= new PSTypeVo();
        			vo=(PSTypeVo)dao.findByID(id);
        			if(vo.getMonflag() == 0)return;
        			com.afunms.polling.node.SocketService tnode=(com.afunms.polling.node.SocketService)PollingEngine.getInstance().getSocketByID(vo.getId());
        			if(tnode == null)return;
        			if(!tnode.isManaged())return;
    				try {
    					com.afunms.polling.node.SocketService socket = new com.afunms.polling.node.SocketService();
						BeanUtils.copyProperties(socket, tnode);
						ipaddress = socket.getIpAddress();
						Calendar date=Calendar.getInstance();
						Date cc = date.getTime();
			 			String tempsenddate = sdf.format(cc);
			 			//��ʼ��Socket��״̬
						tnode.setLastTime(tempsenddate);
						tnode.setAlarm(false);
						tnode.getAlarmMessage().clear();
						tnode.setStatus(0);
						SocketService socketsv = new SocketService();
						try{
							flag = socketsv.checkService(tnode.getIpaddress(), Integer.parseInt(tnode.getPort()), tnode.getTimeout());
							boolean old = false;
							Hashtable realHash = realtimeManager.getAllReal();
							Integer smssign = new Integer(0);
							Socketmonitor_realtime urold = null;
							if (realHash.get(tnode.getId()) != null) {
								old = true;
								urold = (Socketmonitor_realtime) realHash.get(tnode.getId());
							}
							//����realtime
							
							if (old == true) {
								urold.setMon_time(Calendar.getInstance());
								realtimeManager.update(urold);
							}
							if (old == false) {
								urold = new Socketmonitor_realtime();
								urold.setId(urold.getId());
								urold.setSocket_id(tnode.getId());
								if(flag){
									urold.setIs_canconnected(1);
								}else
									urold.setIs_canconnected(0);
								urold.setMon_time(Calendar.getInstance());
								realtimeManager.save(urold);
							}
						}catch(Exception e){
							e.printStackTrace();
						}finally{
							realtimeManager.close();
						}
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
    				
    				if (flag == false) {
    					
    					try {
							com.afunms.polling.node.SocketService tc = new com.afunms.polling.node.SocketService();
							BeanUtils.copyProperties(tc, tnode);
    							//��Ҫ�����ʼ��������ڵķ������Ƿ�����ͨ
    							Host host = (Host)PollingEngine.getInstance().getNodeByIP(tnode.getIpAddress());
    							Vector ipPingData = (Vector)ShareData.getPingdata().get(tnode.getIpAddress());
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
//    									com.afunms.polling.node.SocketService _tnode=(com.afunms.polling.node.SocketService)PollingEngine.getInstance().getSocketByID(vo.getId());
//    									_tnode.setAlarm(true);
//    									_tnode.setStatus(1);
//    									List alarmList = _tnode.getAlarmMessage();
//    									if(alarmList == null)alarmList = new ArrayList();
//    									_tnode.getAlarmMessage().add("SOCKET����ֹͣ");
//    						            String sysLocation = "";
//    						              try{
//    						            	  SmscontentDao eventdao = new SmscontentDao();
//    						            	  String eventdesc = "Socket����("+_tnode.getAlias()+" IP:"+_tnode.getIpaddress()+")"+"��SOCKET����ֹͣ";
//    						            	  eventdao.createEventWithReasion("poll",_tnode.getId()+"",_tnode.getIpaddress()+"("+_tnode.getIpaddress()+")",eventdesc,3,"socket","ping","���ڵķ��������Ӳ���");
//    						              }catch(Exception e){
//    						            	  e.printStackTrace();
//    						              }
    						              Pingcollectdata hostdata=null;
    	        							hostdata=new Pingcollectdata();
    	        							hostdata.setIpaddress(ipaddress);
    	        							Calendar date=Calendar.getInstance();
    	        							hostdata.setCollecttime(date);
    	        							hostdata.setCategory("SOCKETPing");
    	        							hostdata.setEntity("Utilization");
    	        							hostdata.setSubentity("ConnectUtilization");
    	        							hostdata.setRestype("dynamic");
    	        							hostdata.setUnit("%");
    	        							hostdata.setThevalue("0");	
    	        							PSTypeDao socketdao=new PSTypeDao();
		        							try{
		        								socketdao.createHostData(hostdata,vo.getPort());	        								
		        							}catch(Exception e){
		        								e.printStackTrace();
		        							}finally{
		        								socketdao.close();
		        							}
    								}else{
//    									com.afunms.polling.node.SocketService _tnode=(com.afunms.polling.node.SocketService)PollingEngine.getInstance().getSocketByIP(ipaddress);
//    									_tnode.setAlarm(true);
//    									_tnode.setStatus(3);
//    									List alarmList = _tnode.getAlarmMessage();
//    									if(alarmList == null)alarmList = new ArrayList();
//    									_tnode.getAlarmMessage().add("SOCKET����ֹͣ");
	        							Pingcollectdata hostdata=null;
	        							hostdata=new Pingcollectdata();
	        							hostdata.setIpaddress(ipaddress);
	        							Calendar date=Calendar.getInstance();
	        							hostdata.setCollecttime(date);
	        							hostdata.setCategory("SOCKETPing");
	        							hostdata.setEntity("Utilization");
	        							hostdata.setSubentity("ConnectUtilization");
	        							hostdata.setRestype("dynamic");
	        							hostdata.setUnit("%");
	        							hostdata.setThevalue("0");	
	        							PSTypeDao socketdao=new PSTypeDao();
	        							try{
	        								socketdao.createHostData(hostdata,vo.getPort());	        								
	        							}catch(Exception e){
	        								e.printStackTrace();
	        							}finally{
	        								socketdao.close();
	        							}
    								}
    								
    							}else{
//    								com.afunms.polling.node.SocketService _tnode=(com.afunms.polling.node.SocketService)PollingEngine.getInstance().getSocketByIP(ipaddress);
//    								_tnode.setAlarm(true);
//    								_tnode.setStatus(3);
//									List alarmList = _tnode.getAlarmMessage();
//									if(alarmList == null)alarmList = new ArrayList();
//									_tnode.getAlarmMessage().add("SOCKET����ֹͣ");
									Pingcollectdata hostdata=null;
        							hostdata=new Pingcollectdata();
        							hostdata.setIpaddress(ipaddress);
        							Calendar date=Calendar.getInstance();
        							hostdata.setCollecttime(date);
        							hostdata.setCategory("SOCKETPing");
        							hostdata.setEntity("Utilization");
        							hostdata.setSubentity("ConnectUtilization");
        							hostdata.setRestype("dynamic");
        							hostdata.setUnit("%");
        							hostdata.setThevalue("0");	
        							PSTypeDao socketdao=new PSTypeDao();
        							try{
        								socketdao.createHostData(hostdata,vo.getPort());	        								
        							}catch(Exception e){
        								e.printStackTrace();
        							}finally{
        								socketdao.close();
        							}
    							}	        								
							 //createSMS("socket",tc);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    				}else{
    					Pingcollectdata hostdata=null;
						hostdata=new Pingcollectdata();
						hostdata.setIpaddress(ipaddress);
						Calendar date=Calendar.getInstance();
						hostdata.setCollecttime(date);
						hostdata.setCategory("SOCKETPing");
						hostdata.setEntity("Utilization");
						hostdata.setSubentity("ConnectUtilization");
						hostdata.setRestype("dynamic");
						hostdata.setUnit("%");
						hostdata.setThevalue("100");	
						PSTypeDao socketdao=new PSTypeDao();
						try{
							socketdao.createHostData(hostdata,tnode.getPort());	        								
						}catch(Exception e){
							e.printStackTrace();
						}finally{
							socketdao.close();
						}
    				}
    				//�ж�SOCKET���Ӹ澯
					Pingcollectdata hostdata=null;
					hostdata=new Pingcollectdata();
					hostdata.setIpaddress(ipaddress);
					Calendar date=Calendar.getInstance();
					hostdata.setCollecttime(date);
					hostdata.setCategory("SOCKETPing");
					hostdata.setEntity("Utilization");
					hostdata.setSubentity("ConnectUtilization");
					hostdata.setRestype("dynamic");
					hostdata.setUnit("%");
					if(flag){
						hostdata.setThevalue("1");
					}else{
						hostdata.setThevalue("0");
					}
					
					Vector socketv = new Vector();
					socketv.add(hostdata);
					Hashtable collectHash = new Hashtable();
					collectHash.put("socket", socketv);
				    try{
						AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
						List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(vo.getId()), AlarmConstant.TYPE_SERVICE, "socket");
						for(int k = 0 ; k < list.size() ; k ++){
							AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(k);
							//��SOCKET����ֵ���и澯���
							CheckEventUtil checkutil = new CheckEventUtil();
//							SysLogger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//							SysLogger.info("$$$$$$$$$$ ��SOCKET����ֵ���и澯��� ");
//							SysLogger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
							checkutil.updateData(tnode,collectHash,"service","socket",alarmIndicatorsnode);
							//}
						}
				    }catch(Exception e){
				    	e.printStackTrace();
				    }
    		} catch (Exception e) {
    			e.printStackTrace();
    		}finally{
    			//System.out.println("********SOCKET Thread Count : "+Thread.activeCount());
    		}
								
		}
		catch(Exception e){
			e.printStackTrace();
					
		}finally{
			dao.close();
			//System.out.println("********SocketService Thread Count : "+Thread.activeCount());
		}
		// TODO Auto-generated method stub
	}
	 public  void createSMS(String web, com.afunms.polling.node.SocketService node){
		 	//��������		 	
		 	//���ڴ����õ�ǰ���IP��PING��ֵ
		 	Calendar date=Calendar.getInstance();
		 	try{
	 			if (!sendeddata.containsKey(web+":"+node.getId())){
	 				//�����ڣ��������ţ�������ӵ������б���
		 			Smscontent smscontent = new Smscontent();
		 			String time = sdf.format(date.getTime());
		 			smscontent.setLevel("2");
		 			smscontent.setObjid(node.getId()+"");
		 			smscontent.setMessage(node.getIpAddress()+"��"+node.getPortdesc()+"����ֹͣ");
		 			smscontent.setRecordtime(time);
		 			smscontent.setSubtype("socket");
		 			smscontent.setSubentity("ping");
		 			smscontent.setIp(node.getIpAddress());//���Ͷ���
		 			SmscontentDao smsmanager=new SmscontentDao();
		 			smsmanager.sendURLSmscontent(smscontent);	
					sendeddata.put(web+":"+node.getId(),date);		 					 				
	 			}else{
	 				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
	 				Calendar formerdate =(Calendar)sendeddata.get(web+":"+node.getId());		 				
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
			 			smscontent.setObjid(node.getId()+"");
			 			smscontent.setMessage(node.getIpAddress()+"��"+node.getPortdesc()+"����ֹͣ");
			 			smscontent.setRecordtime(time);
			 			smscontent.setSubtype("socket");
			 			smscontent.setSubentity("ping");
			 			smscontent.setIp(node.getIpAddress());//���Ͷ���
			 			SmscontentDao smsmanager=new SmscontentDao();
			 			smsmanager.sendURLSmscontent(smscontent);
						//�޸��Ѿ����͵Ķ��ż�¼	
			 			sendeddata.put(web+":"+node.getId(),date);	
			 		}	
	 			}	 			 			 			 			 	
		 	}catch(Exception e){
		 		e.printStackTrace();
		 	}
		 }
}