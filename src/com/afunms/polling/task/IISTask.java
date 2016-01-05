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

import com.afunms.application.dao.DominoConfigDao;
import com.afunms.application.dao.IISConfigDao;
import com.afunms.application.model.IISConfig;
import com.afunms.common.util.ShareData;
import com.afunms.event.dao.AlarmInfoDao;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.AlarmInfo;
import com.afunms.event.model.Smscontent;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.impl.ProcessIISData;
import com.afunms.polling.node.Domino;
import com.afunms.polling.node.Host;
import com.afunms.polling.node.IIS;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.polling.om.Task;
import com.afunms.polling.snmp.IISSnmp;


/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IISTask extends MonitorTask {
	//private final static boolean  debug=false; 
	/**
	 * 
	 */
	public IISTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		
		try{
			List list=new ArrayList();
			List<IISConfig> iisconfigList = new ArrayList<IISConfig>();
			IISConfigDao configdao = new IISConfigDao();
			try{
				list = configdao.getIISByFlag(new Integer("1"));
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				configdao.close();
			}
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
        				IISConfig iisConfig = (IISConfig)list.get(i);
            			threadPool.runTask(createTask(iisConfig));
            			iisconfigList.add(iisConfig);
        		}
        		// 关闭线程池并等待所有任务完成
        		threadPool.join();
        		threadPool.close();
    		}
    		threadPool = null;
    		//将采集的IIS信息入库
    		ProcessIISData processIIsData = new ProcessIISData();
    		processIIsData.saveIISData(iisconfigList,ShareData.getIisdata());
		}
		catch(Exception e){
			e.printStackTrace();
					
		}finally{
			System.out.println("********IIS Thread Count : "+Thread.activeCount());
		}
		// TODO Auto-generated method stub
	}
	
    /**
    创建任务
*/	
private static Runnable createTask(final IISConfig iisconf) {
    return new Runnable() {
        public void run() {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	List list = null;
            try {  
            	
				IIS _iis = (IIS)PollingEngine.getInstance().getIisByID(iisconf.getId());	
				if(_iis == null){
					return;
				}
				if(_iis != null){
					_iis.setStatus(0);
					_iis.setAlarm(false);
					_iis.getAlarmMessage().clear();
					Calendar _tempCal = Calendar.getInstance();				
					Date _cc = _tempCal.getTime();
					String _time = sdf.format(_cc);
					_iis.setLastTime(_time);
				}
				
				IISSnmp iissnmp = new IISSnmp();
				
    			try{
    				list = iissnmp.collect_Data(iisconf);
    			}catch(Exception e){
    				e.printStackTrace();
    			}
				try{
					
					//判断告警
					if(list != null && list.size()>0){
						ShareData.setIisdata(iisconf.getIpaddress(), list);
						Pingcollectdata hostdata=null;
						hostdata=new Pingcollectdata();
						hostdata.setIpaddress(iisconf.getIpaddress());
						Calendar date=Calendar.getInstance();
						hostdata.setCollecttime(date);
						hostdata.setCategory("IISPing");
						hostdata.setEntity("Utilization");
						hostdata.setSubentity("ConnectUtilization");
						hostdata.setRestype("dynamic");
						hostdata.setUnit("%");
						hostdata.setThevalue("100");	
						IISConfigDao iisdao=new IISConfigDao();
						try{
							iisdao.createHostData(hostdata);
							Vector v = new Vector();
							v.add(hostdata);
							ShareData.setIISPingdata(iisconf.getIpaddress(), v);
						}catch(Exception e){
							e.printStackTrace();
						}finally{
							iisdao.close();
						}
					}else{
						//需要增加邮件服务所在的服务器是否能连通
						Host host = (Host)PollingEngine.getInstance().getNodeByIP(iisconf.getIpaddress());
						Vector ipPingData = (Vector)ShareData.getPingdata().get(iisconf.getIpaddress());
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
								com.afunms.polling.node.IIS tnode=(com.afunms.polling.node.IIS)PollingEngine.getInstance().getIisByIP(iisconf.getIpaddress());
								tnode.setAlarm(true);
								tnode.setStatus(3);
								List alarmList = tnode.getAlarmMessage();
								if(alarmList == null)alarmList = new ArrayList();
								tnode.getAlarmMessage().add("IIS服务停止,因为所在的服务器连接不上");
					            String sysLocation = "";
					              try{
					            	  SmscontentDao eventdao = new SmscontentDao();
					            	  String eventdesc = "IIS服务("+tnode.getAlias()+" IP:"+tnode.getAdminIp()+")"+"的IIS服务停止";
					            	  eventdao.createEventWithReasion("poll",tnode.getId()+"",tnode.getAdminIp()+"("+tnode.getAdminIp()+")",eventdesc,3,"iis","ping","所在的服务器连接不上");
					            	  Pingcollectdata hostdata=null;
										hostdata=new Pingcollectdata();
										hostdata.setIpaddress(iisconf.getIpaddress());
										Calendar date=Calendar.getInstance();
										hostdata.setCollecttime(date);
										hostdata.setCategory("IISPing");
										hostdata.setEntity("Utilization");
										hostdata.setSubentity("ConnectUtilization");
										hostdata.setRestype("dynamic");
										hostdata.setUnit("%");
										hostdata.setThevalue("0");	
										IISConfigDao iisdao=new IISConfigDao();
										try{
											iisdao.createHostData(hostdata);	        								
										}catch(Exception e){
											e.printStackTrace();
										}finally{
											iisdao.close();
										}
										Vector v = new Vector();
										v.add(hostdata);
										ShareData.setIISPingdata(tnode.getIpaddress(), v);
					              }catch(Exception e){
					            	  e.printStackTrace();
					              }
							}else{
								com.afunms.polling.node.IIS tnode=(com.afunms.polling.node.IIS)PollingEngine.getInstance().getIisByIP(iisconf.getIpaddress());
								tnode.setAlarm(true);
								tnode.setStatus(3);
								List alarmList = tnode.getAlarmMessage();
								if(alarmList == null)alarmList = new ArrayList();
								tnode.getAlarmMessage().add("IIS服务停止");
    							Pingcollectdata hostdata=null;
    							hostdata=new Pingcollectdata();
    							hostdata.setIpaddress(iisconf.getIpaddress());
    							Calendar date=Calendar.getInstance();
    							hostdata.setCollecttime(date);
    							hostdata.setCategory("IISPing");
    							hostdata.setEntity("Utilization");
    							hostdata.setSubentity("ConnectUtilization");
    							hostdata.setRestype("dynamic");
    							hostdata.setUnit("%");
    							hostdata.setThevalue("0");	
    							IISConfigDao iisdao=new IISConfigDao();
								try{
									iisdao.createHostData(hostdata);	        								
								}catch(Exception e){
									e.printStackTrace();
								}finally{
									iisdao.close();
								}
								Vector v = new Vector();
								v.add(hostdata);
								ShareData.setIISPingdata(tnode.getIpaddress(), v);
    							createSMS("IIS服务停止",iisconf,"ping");
								//reason = "FTP服务无效";
								//createEvent(ftpConfig, reason);
							}
							
						}else{
							com.afunms.polling.node.IIS tnode=(com.afunms.polling.node.IIS)PollingEngine.getInstance().getIisByID(iisconf.getId());
							tnode.setAlarm(true);
							tnode.setStatus(3);
							List alarmList = tnode.getAlarmMessage();
							if(alarmList == null)alarmList = new ArrayList();
							tnode.getAlarmMessage().add("IIS服务停止");
							Pingcollectdata hostdata=null;
							hostdata=new Pingcollectdata();
							hostdata.setIpaddress(iisconf.getIpaddress());
							Calendar date=Calendar.getInstance();
							hostdata.setCollecttime(date);
							hostdata.setCategory("IISPing");
							hostdata.setEntity("Utilization");
							hostdata.setSubentity("ConnectUtilization");
							hostdata.setRestype("dynamic");
							hostdata.setUnit("%");
							hostdata.setThevalue("0");	
							IISConfigDao iisdao=new IISConfigDao();
							try{
								iisdao.createHostData(hostdata);	        								
							}catch(Exception e){
								e.printStackTrace();
							}finally{
								iisdao.close();
							}
							Vector v = new Vector();
							v.add(hostdata);
							ShareData.setIISPingdata(tnode.getIpaddress(), v);
							createSMS("IIS服务停止",iisconf,"ping");
							//reason = "FTP服务无效";
							//createEvent(ftpConfig, reason);
						}
					}
					//hostdataManager.createHostData(dominoconf.getIpaddress(),hash);
				}catch(Exception ex){
					ex.printStackTrace();
					_iis.setAlarm(true);
					_iis.setStatus(3);
					List alarmList = _iis.getAlarmMessage();
					if(alarmList == null)alarmList = new ArrayList();
					_iis.getAlarmMessage().add("IIS服务停止");
					Pingcollectdata hostdata=null;
					hostdata=new Pingcollectdata();
					hostdata.setIpaddress(iisconf.getIpaddress());
					Calendar date=Calendar.getInstance();
					hostdata.setCollecttime(date);
					hostdata.setCategory("IISPing");
					hostdata.setEntity("Utilization");
					hostdata.setSubentity("ConnectUtilization");
					hostdata.setRestype("dynamic");
					hostdata.setUnit("%");
					hostdata.setThevalue("0");	
					IISConfigDao iisdao=new IISConfigDao();
					try{
						iisdao.createHostData(hostdata);	        								
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						iisdao.close();
					}
					Vector v = new Vector();
					v.add(hostdata);
					ShareData.setIISPingdata(iisconf.getIpaddress(), v);
					
//					String pingvalue = "0";
//					Hashtable allissdata = (Hashtable)ShareData.getIisdata();
//					if(allissdata != null && allissdata.size()>0){
//						Vector vec = (Vector)allissdata.get(iisconf.getIpaddress());
//						if(vec != null && vec.size()>0){
//							Pingcollectdata iispingdata = (Pingcollectdata)v.get(0);
//							if(iispingdata != null){
//								pingvalue = iispingdata.getThevalue();
//							}
//						}
//					}
					
					createSMS("IIS服务停止",iisconf,"ping");
				}finally{
	        		list=null; 
				}
				           	 
            }catch(Exception exc){
            	exc.printStackTrace();
            }
        }
    };
}

public static void createSMS(String chlname,IISConfig iisconf,String flag){
 	//建立短信		 	
 	//从内存里获得当前这个IP的PING的值
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	AlarmInfoDao alarminfomanager=new AlarmInfoDao();

	String ipaddress = iisconf.getIpaddress();
	Hashtable sendeddata = ShareData.getSendeddata();
 	Calendar date=Calendar.getInstance();
 	String time = sdf.format(date.getTime());
 	try{
		if (!sendeddata.containsKey(ipaddress+":"+chlname)){
			//若不在，则建立短信，并且添加到发送列表里
 			Smscontent smscontent = new Smscontent();
 			smscontent.setLevel("2");
 			smscontent.setObjid(iisconf.getId()+"");
 			smscontent.setMessage(iisconf.getName()+"(IP:"+iisconf.getIpaddress()+")IIS服务停止");
 			smscontent.setRecordtime(time);
 			smscontent.setSubtype("iis");
 			smscontent.setSubentity("ping");
 			smscontent.setIp(iisconf.getIpaddress());
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
 	 			smscontent.setObjid(iisconf.getId()+"");
 	 			smscontent.setMessage(iisconf.getName()+"(IP:"+iisconf.getIpaddress()+")IIS服务停止");
 	 			errmsg = smscontent.getMessage();
 	 			smscontent.setRecordtime(time);
 	 			smscontent.setSubtype("iis");
 	 			smscontent.setSubentity("ping");
 	 			smscontent.setIp(iisconf.getIpaddress());
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
				alarminfo.setIpaddress(iisconf.getIpaddress());
				alarminfo.setLevel1(new Integer(2));
				alarminfo.setRecordtime(Calendar.getInstance());
				alarminfomanager.save(alarminfo);
				
	 		}else{
	 			//则写声音告警数据
				//向声音告警表里写数据
	 			
				AlarmInfo alarminfo = new AlarmInfo();
				alarminfo.setContent(errmsg);
				alarminfo.setIpaddress(iisconf.getIpaddress());
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
