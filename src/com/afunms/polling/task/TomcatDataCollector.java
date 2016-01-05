package com.afunms.polling.task;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

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
public class TomcatDataCollector {
	
	private Hashtable sendeddata = ShareData.getSendeddata();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public TomcatDataCollector() {

	}
	
	public void collect_data(String id,Hashtable gatherHash) {
		
		
		Hashtable data_ht = new Hashtable();
		String jvm_utilization="0";
		try {
			ServerStream serverstream = new ServerStream();
			Hashtable returnVal = new Hashtable();
			String ipaddress = "";
			Tomcat node = new Tomcat();
			TomcatDao dao = new TomcatDao();
			try{
				node = (Tomcat)dao.findByID(id);
			}catch(Exception e){
			}finally{
				dao.close();
			}
			try {
				com.afunms.polling.node.Tomcat tc = new com.afunms.polling.node.Tomcat();
				BeanUtils.copyProperties(tc, node);
				ipaddress = tc.getIpAddress();
				com.afunms.polling.node.Tomcat tnode=(com.afunms.polling.node.Tomcat)PollingEngine.getInstance().getTomcatByIP(ipaddress);
				Calendar date=Calendar.getInstance();
				Date cc = date.getTime();
	 			String tempsenddate = sdf.format(cc);
	 			//��ʼ��Tomcat�����״̬
				tnode.setLastTime(tempsenddate);
				tnode.setAlarm(false);
				tnode.getAlarmMessage().clear();
				tnode.setStatus(0);
				
				StringBuffer tmp = new StringBuffer();
				tmp.append(tc.getIpAddress());
				tmp.append(",");
				tmp.append(tc.getPort());
				tmp.append(",");
				tmp.append(tc.getUser());
				tmp.append(" , ");
				tmp.append(tc.getPassword());
				returnVal.put(String.valueOf(0), tmp.toString());	        					
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			String liststr = serverstream.validServer(returnVal);
			if ("".equals(liststr)) {
					
					try {
						com.afunms.polling.node.Tomcat tc = new com.afunms.polling.node.Tomcat();
						BeanUtils.copyProperties(tc, node);
						//if (data_ht==null){
							//��Ҫ�����ʼ��������ڵķ������Ƿ�����ͨ
							Host host = (Host)PollingEngine.getInstance().getNodeByIP(node.getIpAddress());
							Vector ipPingData = (Vector)ShareData.getPingdata().get(node.getIpAddress());
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
									com.afunms.polling.node.Tomcat tnode=(com.afunms.polling.node.Tomcat)PollingEngine.getInstance().getTomcatByIP(ipaddress);
									tnode.setAlarm(true);
									tnode.setStatus(1);
									List alarmList = tnode.getAlarmMessage();
									if(alarmList == null)alarmList = new ArrayList();
									tnode.getAlarmMessage().add("TOMCAT����ֹͣ");
						            String sysLocation = "";
						              try{
						            	  SmscontentDao eventdao = new SmscontentDao();
						            	  String eventdesc = "TOMCAT����("+tnode.getAlias()+" IP:"+tnode.getAdminIp()+")"+"��TOMCAT����ֹͣ";
						            	  eventdao.createEventWithReasion("poll",tnode.getId()+"",tnode.getAdminIp()+"("+tnode.getAdminIp()+")",eventdesc,3,"tomcat","ping","���ڵķ��������Ӳ���");
						              }catch(Exception e){
						            	  e.printStackTrace();
						              }
								}else{
									com.afunms.polling.node.Tomcat tnode=(com.afunms.polling.node.Tomcat)PollingEngine.getInstance().getTomcatByIP(ipaddress);
									tnode.setAlarm(true);
									tnode.setStatus(3);
									List alarmList = tnode.getAlarmMessage();
									if(alarmList == null)alarmList = new ArrayList();
									tnode.getAlarmMessage().add("TOMCAT����ֹͣ");
        							Pingcollectdata hostdata=null;
        							hostdata=new Pingcollectdata();
        							hostdata.setIpaddress(ipaddress);
        							Calendar date=Calendar.getInstance();
        							hostdata.setCollecttime(date);
        							hostdata.setCategory("TomcatPing");
        							hostdata.setEntity("Utilization");
        							hostdata.setSubentity("ConnectUtilization");
        							hostdata.setRestype("dynamic");
        							hostdata.setUnit("%");
        							hostdata.setThevalue("0");	
        							TomcatDao tomcatdao=new TomcatDao();
        							try{
        								tomcatdao.createHostData(hostdata);	        								
        							}catch(Exception e){
        								e.printStackTrace();
        							}finally{
        								tomcatdao.close();
        							}
								}
								
							}else{
								com.afunms.polling.node.Tomcat tnode=(com.afunms.polling.node.Tomcat)PollingEngine.getInstance().getTomcatByIP(ipaddress);
								tnode.setAlarm(true);
								tnode.setStatus(3);
								List alarmList = tnode.getAlarmMessage();
								if(alarmList == null)alarmList = new ArrayList();
								tnode.getAlarmMessage().add("TOMCAT����ֹͣ");
    							Pingcollectdata hostdata=null;
    							hostdata=new Pingcollectdata();
    							hostdata.setIpaddress(ipaddress);
    							Calendar date=Calendar.getInstance();
    							hostdata.setCollecttime(date);
    							hostdata.setCategory("TomcatPing");
    							hostdata.setEntity("Utilization");
    							hostdata.setSubentity("ConnectUtilization");
    							hostdata.setRestype("dynamic");
    							hostdata.setUnit("%");
    							hostdata.setThevalue("0");	
    							TomcatDao tomcatdao=new TomcatDao();
    							try{
    								tomcatdao.createHostData(hostdata);	        								
    							}catch(Exception e){
    								e.printStackTrace();
    							}finally{
    								tomcatdao.close();
    							}
							}	        							
						//}		
						 createSMS("tomcat",tc);
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
					hostdata.setCategory("TomcatPing");
					hostdata.setEntity("Utilization");
					hostdata.setSubentity("ConnectUtilization");
					hostdata.setRestype("dynamic");
					hostdata.setUnit("%");
					hostdata.setThevalue("100");
					TomcatDao tomcatdao=new TomcatDao();
					try{
						tomcatdao.createHostData(hostdata);	        								
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						tomcatdao.close();
					}
				}
				String[] pos_s = liststr.split(",");
				for (int list_i = 0; list_i < pos_s.length-1; list_i++) {
					String tmps = returnVal.get(pos_s[list_i]).toString();
					String[] serverinfo = tmps.split(",");
					serverstream.foundData(serverinfo[0],serverinfo[1],serverinfo[2],serverinfo[3]);	        					
					data_ht = serverstream.data_ht;
					TomcatDao tomcatdao=new TomcatDao();
					Hashtable sendeddata = ShareData.getSendeddata();
					
					try {
						Calendar time =Calendar.getInstance();
						String lasttime=data_ht.get("mon_time").toString();
						String nexttime=data_ht.get("nexttime").toString();
						
						String server = data_ht.get("server").toString();
						String jvm =  data_ht.get("jvm").toString();

						String ip =  data_ht.get("ip").toString();
						String port =  data_ht.get("port").toString();
						String portsum1 =  data_ht.get("portsum1").toString();
						String portsum2 =  data_ht.get("portsum2").toString();
						String portdetail1 =  data_ht.get("portdetail1").toString();
						String portdetail2 =  data_ht.get("portdetail2").toString();	
						if(data_ht!=null){
							int jvm_memoryuiltillize=0;
							
							String [] temjvm=jvm.split(",");
							double freememory=Double.parseDouble(temjvm[0].trim());
							double totalmemory=(double)Double.parseDouble(temjvm[1].trim());
							double maxmemory=(double)Double.parseDouble(temjvm[2].trim());
							
							jvm_memoryuiltillize=(int)Math.rint((totalmemory-freememory)*100/totalmemory);
							jvm_utilization=String.valueOf(jvm_memoryuiltillize);
							
							Pingcollectdata hostdata=null;
							hostdata=new Pingcollectdata();
							hostdata.setIpaddress(ip);
							Calendar date=Calendar.getInstance();
							hostdata.setCollecttime(date);
							hostdata.setCategory("tomcat_jvm");
							hostdata.setEntity("Utilization");
							hostdata.setSubentity("jvm_utilization");
							hostdata.setRestype("dynamic");
							hostdata.setUnit("%");
							hostdata.setThevalue(jvm_utilization);
							try{
								tomcatdao.createHostData(hostdata);
								
								if(sendeddata.containsKey("tomcat"+":"+ip))
									sendeddata.remove("tomcat"+":"+ip);
							}catch(Exception e){
								e.printStackTrace();
							}
						}
						
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						tomcatdao.close();
					}
				}
				try{
					data_ht.put("jvm_utilization", jvm_utilization);
					ShareData.setTomcatdata(node.getIpAddress(), data_ht);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				data_ht = null;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		}
	}

	 public  void createSMS(String web, com.afunms.polling.node.Tomcat node){
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
		 			smscontent.setMessage(node.getIpAddress()+"��TOMCAT����ֹͣ");
		 			smscontent.setRecordtime(time);
		 			smscontent.setSubtype("tomcat");
		 			smscontent.setSubentity("ping");
		 			smscontent.setIp(node.getIpAddress());
		 			
		 			//smscontent.setMessage("db&"+time+"&"+dbmonitorlist.getId()+"&"+db+"("+dbmonitorlist.getDbName()+" IP:"+dbmonitorlist.getIpAddress()+")"+"�����ݿ����ֹͣ");
		 			//���Ͷ���
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
			 			smscontent.setMessage(node.getAlias()+" ("+node.getIpAddress()+")"+"�ķ��ʷ���ֹͣ");
			 			smscontent.setRecordtime(time);
			 			smscontent.setSubtype("tomcat");
			 			smscontent.setSubentity("ping");
			 			smscontent.setIp(node.getIpAddress());
			 			//smscontent.setMessage("db&"+time+"&"+dbmonitorlist.getId()+"&"+db+"("+dbmonitorlist.getDbName()+" IP:"+dbmonitorlist.getIpAddress()+")"+"�����ݿ����ֹͣ");
			 			//���Ͷ���
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