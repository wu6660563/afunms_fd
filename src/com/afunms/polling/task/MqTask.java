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

import com.afunms.application.dao.MQConfigDao;
import com.afunms.application.dao.MQchannelConfigDao;
import com.afunms.application.dao.TomcatDao;
import com.afunms.application.model.MQConfig;
import com.afunms.common.util.ShareData;
import com.afunms.event.dao.AlarmInfoDao;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.AlarmInfo;
import com.afunms.event.model.Smscontent;
import com.afunms.mq.MqManager;
import com.afunms.common.util.SysLogger;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.impl.ProcessMQData;
import com.afunms.polling.node.Host;
import com.afunms.polling.node.MQ;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.polling.om.Task;
import com.ibm.mq.pcf.CMQC;
import com.ibm.mq.pcf.CMQCFC;
import com.icss.ro.de.connector.mqimpl.MQNode;


/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MqTask extends MonitorTask {
	//private final static boolean  debug=false; 
	/**
	 * 
	 */
	public MqTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
//		TODO Auto-generated method stub
		//I_Dominoconf dominoconfManager = new DominoconfManager();
		//I_MonitorIpList monitorList=new MonitoriplistManager();
		//MqManager mq = null;
		MQConfigDao mqconfdao = new MQConfigDao();
		try{
			//mq = new MqManager();
			List list= new ArrayList();
			try{
				list = mqconfdao.getMQByFlag(new Integer("1"));
			}catch(Exception e){
				
			}finally{
				mqconfdao.close();
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

    		// 生成线程池
    		ThreadPool threadPool = null;	
    		if(list != null && list.size()>0){
    			threadPool = new ThreadPool(list.size());
    			// 运行任务    		
        		for (int i=0; i<list.size(); i++) {    			
            			threadPool.runTask(createTask((MQConfig)list.get(i)));
        		}
        		// 关闭线程池并等待所有任务完成
        		threadPool.join();
        		threadPool.close();
        		threadPool = null;
    		}
    		//将MQ信息入库
			ProcessMQData processMQData = new ProcessMQData();
			processMQData.saveMqData(list, ShareData.getMqdata());
		}
		catch(Exception e){
			e.printStackTrace();
					
		}finally{
			System.out.println("********MQ Thread Count : "+Thread.activeCount());
		}
		// TODO Auto-generated method stub
	}
	
    /**
    创建任务
*/	
private static Runnable createTask(final MQConfig mqconf) {
    return new Runnable() {
        public void run() {
        	MqManager mq=null;
        	MQNode node = new MQNode();
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	MQ _mq = (MQ)PollingEngine.getInstance().getMqByID(mqconf.getId());	
        	String pingValue = "0";//连通率
            try {                	
                //Thread.sleep(500);
            	Hashtable hash = null;
            	MQchannelConfigDao mqchannelconfigdao=new MQchannelConfigDao();
            	mq = new MqManager();
            	Hashtable mqchannels = new Hashtable();
            	try{
           		mqchannels = mqchannelconfigdao.getByAlarmflag(1);
            	}catch(Exception e){
            		
            	}finally{
            		mqchannelconfigdao.close();
            	}
            	if (mqchannels == null) mqchannels = new Hashtable();
            	//将历史告警清除
				
				if(_mq == null){
					return;
				}
				if(_mq != null){
					_mq.setStatus(0);
					_mq.setAlarm(false);
					_mq.getAlarmMessage().clear();
					Calendar _tempCal = Calendar.getInstance();				
					Date _cc = _tempCal.getTime();
					String _time = sdf.format(_cc);
					_mq.setLastTime(_time);
				}
    			node.setQmanager(mqconf.getManagername());
    		    node.setHost(mqconf.getIpaddress());
    			node.setPort(mqconf.getPortnum()+"");
    			node.setQueue("");
    			try{
    				mq.connMQ(node);
    			}catch(Exception e){
    				//e.printStackTrace();
    			}
            	Vector mqValue = new Vector();
            	Vector mqNameValue = new Vector();
            	List queueParas = new ArrayList();
            	List q_remote_ParaValues = new ArrayList();
            	List q_local_ParaValues = new ArrayList();
            	Hashtable rValue = new Hashtable();
        		//hash=dominosnmp.collectData();
				try{
					//获得MQ队列列表的状态值
					mqValue = mq.inquireChannelStatus("", 0);
					q_remote_ParaValues = mq.inquireQueue("", CMQC.MQQT_REMOTE);					
					q_local_ParaValues = mq.inquireQueue("", CMQC.MQQT_LOCAL);
					if(mqValue == null) mqValue = new Vector();
					if(q_remote_ParaValues == null)q_remote_ParaValues = new ArrayList();
					if(q_local_ParaValues == null)q_local_ParaValues = new ArrayList();	
					rValue.put("mqValue", mqValue);
					rValue.put("remote", q_remote_ParaValues);
					rValue.put("local", q_local_ParaValues);
					ShareData.addMqdata(mqconf, rValue);
					//判断告警
					if(mqValue != null && mqValue.size()>0){
						for(int i=0;i<mqValue.size();i++){
							Hashtable cAttr = (Hashtable)mqValue.get(i);
							String chlname = (String)cAttr.get("name");
							chlname = chlname.trim();
							String connName = (String)cAttr.get("connName");
							connName = connName.trim();
							String status = (String)cAttr.get("status");
							int stat = Integer.parseInt(status);							
							if (stat == CMQCFC.MQCHS_RUNNING){									
								//if (mqchannels.containsKey(mqconf.getIpaddress()+":"+chlname+":"+connName))
									//createSMS(chlname,mqconf);								
							}else{								
								//告警
								//判断告警设置里有没有需要监视该MQ通道
								if (mqchannels.containsKey(mqconf.getIpaddress()+":"+chlname+":"+connName)){
									_mq.setAlarm(true);
									_mq.setStatus(3);
									List alarmList = _mq.getAlarmMessage();
									if(alarmList == null)alarmList = new ArrayList();
									_mq.getAlarmMessage().add(mqconf.getName()+" ("+chlname+")"+"通道处于非正常运行状态");
									createSMS(chlname,mqconf,"chl");
								}
							}
						}
						pingValue = "100";//能正常ping通
					}else{//服务器不能连通或者MQ服务停止
						//需要增加邮件服务所在的服务器是否能连通
						Host host = (Host)PollingEngine.getInstance().getNodeByIP(mqconf.getIpaddress());
						Vector ipPingData = (Vector)ShareData.getPingdata().get(mqconf.getIpaddress());
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
								//主机服务器连接不上***********************************************
								com.afunms.polling.node.MQ tnode=(com.afunms.polling.node.MQ)PollingEngine.getInstance().getMqByIP(mqconf.getIpaddress());
								tnode.setAlarm(true);
								_mq.setStatus(3);
								List alarmList = tnode.getAlarmMessage();
								if(alarmList == null)alarmList = new ArrayList();
								tnode.getAlarmMessage().add("MQ服务停止,因为所在的服务器连接不上");
					            String sysLocation = "";
					              try{
					            	  SmscontentDao eventdao = new SmscontentDao();
					            	  String eventdesc = "MQ服务("+tnode.getAlias()+" IP:"+mqconf.getIpaddress()+")"+"的MQ服务停止";
					            	  eventdao.createEventWithReasion("poll",tnode.getId()+"",tnode.getAdminIp()+"("+tnode.getAdminIp()+")",eventdesc,3,"mq","ping","所在的服务器连接不上");
					            	  pingValue = "0";//不能正常ping通
//					            	  Pingcollectdata hostdata=null;
//										hostdata=new Pingcollectdata();
//										hostdata.setIpaddress(mqconf.getIpaddress());
//										Calendar date=Calendar.getInstance();
//										hostdata.setCollecttime(date);
//										hostdata.setCategory("MqPing");
//										hostdata.setEntity("Utilization");
//										hostdata.setSubentity("ConnectUtilization");
//										hostdata.setRestype("dynamic");
//										hostdata.setUnit("%");
//										hostdata.setThevalue("0");	
//										MQConfigDao mqdao=new MQConfigDao();
//										try{
//											mqdao.createHostData(hostdata);	        								
//										}catch(Exception e){
//											e.printStackTrace();
//										}finally{
//											mqdao.close();
//										}
					              }catch(Exception e){
					            	  e.printStackTrace();
					              }
							}else{
								com.afunms.polling.node.MQ tnode=(com.afunms.polling.node.MQ)PollingEngine.getInstance().getMqByIP(mqconf.getIpaddress());
								tnode.setAlarm(true);
								_mq.setStatus(3);
								List alarmList = tnode.getAlarmMessage();
								if(alarmList == null)alarmList = new ArrayList();
								tnode.getAlarmMessage().add("MQ服务停止");
								pingValue = "0";
//    							Pingcollectdata hostdata=null;
//    							hostdata=new Pingcollectdata();
//    							hostdata.setIpaddress(mqconf.getIpaddress());
//    							Calendar date=Calendar.getInstance();
//    							hostdata.setCollecttime(date);
//    							hostdata.setCategory("MqPing");
//    							hostdata.setEntity("Utilization");
//    							hostdata.setSubentity("ConnectUtilization");
//    							hostdata.setRestype("dynamic");
//    							hostdata.setUnit("%");
//    							hostdata.setThevalue("0");	
//    							MQConfigDao mqdao=new MQConfigDao();
//    							try{
//    								mqdao.createHostData(hostdata);	        								
//    							}catch(Exception e){
//    								e.printStackTrace();
//    							}finally{
//    								mqdao.close();
//    							}
    							createSMS("服务停止",mqconf,"ping");
								//reason = "FTP服务无效";
								//createEvent(ftpConfig, reason);
							}
							
						}else{
							com.afunms.polling.node.MQ tnode=(com.afunms.polling.node.MQ)PollingEngine.getInstance().getMqByID(mqconf.getId());
							tnode.setAlarm(true);
							_mq.setStatus(3);
							List alarmList = tnode.getAlarmMessage();
							if(alarmList == null)alarmList = new ArrayList();
							tnode.getAlarmMessage().add("MQ服务停止");
							pingValue = "0";
//							Pingcollectdata hostdata=null;
//							hostdata=new Pingcollectdata();
//							hostdata.setIpaddress(tnode.getIpaddress());
//							Calendar date=Calendar.getInstance();
//							hostdata.setCollecttime(date);
//							hostdata.setCategory("MqPing");
//							hostdata.setEntity("Utilization");
//							hostdata.setSubentity("ConnectUtilization");
//							hostdata.setRestype("dynamic");
//							hostdata.setUnit("%");
//							hostdata.setThevalue("0");	
//							MQConfigDao mqdao=new MQConfigDao();
//							try{
//								mqdao.createHostData(hostdata);	        								
//							}catch(Exception e){
//								e.printStackTrace();
//							}finally{
//								mqdao.close();
//							}
							createSMS("服务停止",mqconf,"ping");
							//reason = "FTP服务无效";
							//createEvent(ftpConfig, reason);
						}
					}
					//hostdataManager.createHostData(dominoconf.getIpaddress(),hash);
					//添加连通率信息
					Pingcollectdata hostdata=null;
					hostdata=new Pingcollectdata();
					hostdata.setIpaddress(mqconf.getIpaddress());
					Calendar date=Calendar.getInstance();
					hostdata.setCollecttime(date);
					hostdata.setCategory("MqPing");
					hostdata.setEntity("Utilization");
					hostdata.setSubentity("ConnectUtilization");
					hostdata.setRestype("dynamic");
					hostdata.setUnit("%");
					hostdata.setThevalue(pingValue);	
					MQConfigDao mqdao=new MQConfigDao();
					try{
						mqdao.createHostData(hostdata);	        								
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						mqdao.close();
					}
				}catch(Exception ex){
					ex.printStackTrace();
					_mq.setAlarm(true);
					_mq.setStatus(3);
					List alarmList = _mq.getAlarmMessage();
					if(alarmList == null)alarmList = new ArrayList();
					_mq.getAlarmMessage().add("MQ服务停止");
//					Pingcollectdata hostdata=null;
//					hostdata=new Pingcollectdata();
//					hostdata.setIpaddress(mqconf.getIpaddress());
//					Calendar date=Calendar.getInstance();
//					hostdata.setCollecttime(date);
//					hostdata.setCategory("MqPing");
//					hostdata.setEntity("Utilization");
//					hostdata.setSubentity("ConnectUtilization");
//					hostdata.setRestype("dynamic");
//					hostdata.setUnit("%");
//					hostdata.setThevalue("0");	
//					MQConfigDao mqdao=new MQConfigDao();
//					try{
//						mqdao.createHostData(hostdata);	        								
//					}catch(Exception e){
//						e.printStackTrace();
//					}finally{
//						mqdao.close();
//					}
					createSMS("服务停止",mqconf,"ping");
				}finally{
					mq.freeConn();
					mq=null;
	        		hash=null; 
				}
				           	 
            }catch(Exception exc){
            	exc.printStackTrace();
            }
        }
    };
}

public static void createSMS(String chlname,MQConfig mqconf,String flag){
 	//建立短信		 	
 	//从内存里获得当前这个IP的PING的值
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	AlarmInfoDao alarminfomanager=new AlarmInfoDao();

	String ipaddress = mqconf.getIpaddress();
	Hashtable sendeddata = ShareData.getSendeddata();
 	Calendar date=Calendar.getInstance();
 	String time = sdf.format(date.getTime());
 	try{
		if (!sendeddata.containsKey(ipaddress+":"+chlname)){
			//若不在，则建立短信，并且添加到发送列表里
 			Smscontent smscontent = new Smscontent();
 			smscontent.setLevel("2");
 			smscontent.setObjid(mqconf.getId()+"");
 			if("chl".equalsIgnoreCase(flag)){
 				smscontent.setMessage(mqconf.getName()+" ("+chlname+")"+"通道处于非正常运行状态");
 			}else
 				smscontent.setMessage(mqconf.getName()+"(IP:"+mqconf.getIpaddress()+")服务停止");
 			smscontent.setRecordtime(time);
 			smscontent.setSubtype("mq");
 			smscontent.setSubentity("ping");
 			smscontent.setIp(mqconf.getIpaddress());
 			//发送短信
 			SmscontentDao smsmanager=new SmscontentDao();
 			smsmanager.sendURLSmscontent(smscontent);	
			sendeddata.put(ipaddress+":"+chlname,date);		 					 				
		}else{
			//若在，则从已发送短信列表里判断是否已经发送当天的短信
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
 				//超过一天，则再发信息
 				Smscontent smscontent = new Smscontent();
 	 			smscontent.setLevel("2");
 	 			smscontent.setObjid(mqconf.getId()+"");
 	 			if("chl".equalsIgnoreCase(flag)){
 	 				smscontent.setMessage(mqconf.getName()+" ("+chlname+")"+"通道处于非正常运行状态");
 	 			}else
 	 				smscontent.setMessage(mqconf.getName()+"(IP:"+mqconf.getIpaddress()+")服务停止");
 	 			errmsg = smscontent.getMessage();
 	 			smscontent.setRecordtime(time);
 	 			smscontent.setSubtype("mq");
 	 			smscontent.setSubentity("ping");
 	 			smscontent.setIp(mqconf.getIpaddress());
 	 			//发送短信
 	 			SmscontentDao smsmanager=new SmscontentDao();
 	 			smsmanager.sendURLSmscontent(smscontent);	
				//修改已经发送的短信记录	
				sendeddata.put(ipaddress+":"+chlname,date);	
	 			//则写声音告警数据
				//向声音告警表里写数据
				//只有在发短信后才产生语音告警
				
				AlarmInfo alarminfo = new AlarmInfo();
				alarminfo.setContent(smscontent.getMessage());
				alarminfo.setIpaddress(mqconf.getIpaddress());
				alarminfo.setLevel1(new Integer(2));
				alarminfo.setRecordtime(Calendar.getInstance());
				alarminfomanager.save(alarminfo);
				
	 		}else{
	 			//则写声音告警数据
				//向声音告警表里写数据
	 			
				AlarmInfo alarminfo = new AlarmInfo();
				alarminfo.setContent(errmsg);
				alarminfo.setIpaddress(mqconf.getIpaddress());
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
