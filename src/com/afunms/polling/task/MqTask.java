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

    		// �����̳߳�
    		ThreadPool threadPool = null;	
    		if(list != null && list.size()>0){
    			threadPool = new ThreadPool(list.size());
    			// ��������    		
        		for (int i=0; i<list.size(); i++) {    			
            			threadPool.runTask(createTask((MQConfig)list.get(i)));
        		}
        		// �ر��̳߳ز��ȴ������������
        		threadPool.join();
        		threadPool.close();
        		threadPool = null;
    		}
    		//��MQ��Ϣ���
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
    ��������
*/	
private static Runnable createTask(final MQConfig mqconf) {
    return new Runnable() {
        public void run() {
        	MqManager mq=null;
        	MQNode node = new MQNode();
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	MQ _mq = (MQ)PollingEngine.getInstance().getMqByID(mqconf.getId());	
        	String pingValue = "0";//��ͨ��
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
            	//����ʷ�澯���
				
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
					//���MQ�����б��״ֵ̬
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
					//�жϸ澯
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
								//�澯
								//�жϸ澯��������û����Ҫ���Ӹ�MQͨ��
								if (mqchannels.containsKey(mqconf.getIpaddress()+":"+chlname+":"+connName)){
									_mq.setAlarm(true);
									_mq.setStatus(3);
									List alarmList = _mq.getAlarmMessage();
									if(alarmList == null)alarmList = new ArrayList();
									_mq.getAlarmMessage().add(mqconf.getName()+" ("+chlname+")"+"ͨ�����ڷ���������״̬");
									createSMS(chlname,mqconf,"chl");
								}
							}
						}
						pingValue = "100";//������pingͨ
					}else{//������������ͨ����MQ����ֹͣ
						//��Ҫ�����ʼ��������ڵķ������Ƿ�����ͨ
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
								//�������������Ӳ���***********************************************
								com.afunms.polling.node.MQ tnode=(com.afunms.polling.node.MQ)PollingEngine.getInstance().getMqByIP(mqconf.getIpaddress());
								tnode.setAlarm(true);
								_mq.setStatus(3);
								List alarmList = tnode.getAlarmMessage();
								if(alarmList == null)alarmList = new ArrayList();
								tnode.getAlarmMessage().add("MQ����ֹͣ,��Ϊ���ڵķ��������Ӳ���");
					            String sysLocation = "";
					              try{
					            	  SmscontentDao eventdao = new SmscontentDao();
					            	  String eventdesc = "MQ����("+tnode.getAlias()+" IP:"+mqconf.getIpaddress()+")"+"��MQ����ֹͣ";
					            	  eventdao.createEventWithReasion("poll",tnode.getId()+"",tnode.getAdminIp()+"("+tnode.getAdminIp()+")",eventdesc,3,"mq","ping","���ڵķ��������Ӳ���");
					            	  pingValue = "0";//��������pingͨ
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
								tnode.getAlarmMessage().add("MQ����ֹͣ");
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
    							createSMS("����ֹͣ",mqconf,"ping");
								//reason = "FTP������Ч";
								//createEvent(ftpConfig, reason);
							}
							
						}else{
							com.afunms.polling.node.MQ tnode=(com.afunms.polling.node.MQ)PollingEngine.getInstance().getMqByID(mqconf.getId());
							tnode.setAlarm(true);
							_mq.setStatus(3);
							List alarmList = tnode.getAlarmMessage();
							if(alarmList == null)alarmList = new ArrayList();
							tnode.getAlarmMessage().add("MQ����ֹͣ");
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
							createSMS("����ֹͣ",mqconf,"ping");
							//reason = "FTP������Ч";
							//createEvent(ftpConfig, reason);
						}
					}
					//hostdataManager.createHostData(dominoconf.getIpaddress(),hash);
					//�����ͨ����Ϣ
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
					_mq.getAlarmMessage().add("MQ����ֹͣ");
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
					createSMS("����ֹͣ",mqconf,"ping");
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
 	//��������		 	
 	//���ڴ����õ�ǰ���IP��PING��ֵ
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	AlarmInfoDao alarminfomanager=new AlarmInfoDao();

	String ipaddress = mqconf.getIpaddress();
	Hashtable sendeddata = ShareData.getSendeddata();
 	Calendar date=Calendar.getInstance();
 	String time = sdf.format(date.getTime());
 	try{
		if (!sendeddata.containsKey(ipaddress+":"+chlname)){
			//�����ڣ��������ţ�������ӵ������б���
 			Smscontent smscontent = new Smscontent();
 			smscontent.setLevel("2");
 			smscontent.setObjid(mqconf.getId()+"");
 			if("chl".equalsIgnoreCase(flag)){
 				smscontent.setMessage(mqconf.getName()+" ("+chlname+")"+"ͨ�����ڷ���������״̬");
 			}else
 				smscontent.setMessage(mqconf.getName()+"(IP:"+mqconf.getIpaddress()+")����ֹͣ");
 			smscontent.setRecordtime(time);
 			smscontent.setSubtype("mq");
 			smscontent.setSubentity("ping");
 			smscontent.setIp(mqconf.getIpaddress());
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
 	 			smscontent.setObjid(mqconf.getId()+"");
 	 			if("chl".equalsIgnoreCase(flag)){
 	 				smscontent.setMessage(mqconf.getName()+" ("+chlname+")"+"ͨ�����ڷ���������״̬");
 	 			}else
 	 				smscontent.setMessage(mqconf.getName()+"(IP:"+mqconf.getIpaddress()+")����ֹͣ");
 	 			errmsg = smscontent.getMessage();
 	 			smscontent.setRecordtime(time);
 	 			smscontent.setSubtype("mq");
 	 			smscontent.setSubentity("ping");
 	 			smscontent.setIp(mqconf.getIpaddress());
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
				alarminfo.setIpaddress(mqconf.getIpaddress());
				alarminfo.setLevel1(new Integer(2));
				alarminfo.setRecordtime(Calendar.getInstance());
				alarminfomanager.save(alarminfo);
				
	 		}else{
	 			//��д�����澯����
				//�������澯����д����
	 			
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
