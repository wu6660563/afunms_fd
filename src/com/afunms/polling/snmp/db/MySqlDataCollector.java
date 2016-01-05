package com.afunms.polling.snmp.db;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import montnets.SmsDao;

import org.apache.commons.beanutils.BeanUtils;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.alarm.util.AlarmResourceCenter;
import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.DBTypeDao;
import com.afunms.application.model.DBTypeVo;
import com.afunms.application.model.DBVo;
import com.afunms.application.util.IpTranslation;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.EncryptUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SystemConstant;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.CheckEvent;
import com.afunms.event.model.EventList;
import com.afunms.event.model.Smscontent;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.DBNode;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.polling.snmp.LoadMySqlFile;
import com.afunms.system.util.TimeGratherConfigUtil;

public class MySqlDataCollector{

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public void collect_Data(NodeGatherIndicators nodeGatherIndicators) {
	    DBVo DBVo = new DBVo(); 
	    DBDao DBdao = new DBDao();
        try{
            DBVo = (DBVo)DBdao.findByID(nodeGatherIndicators.getNodeid());
        }catch(Exception e){
            
        }finally{
            DBdao.close();
        }
        if (DBVo == null) {
            return;
        }
        String serverIP = DBVo.getIpAddress();
        String userName = DBVo.getUser();
        String password = null;
        try {
            password = EncryptUtil.decode(DBVo.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        int port = Integer.parseInt(DBVo.getPort());
        String DBNames = DBVo.getDbName();
        //判断该数据库是否能连接上
        String[] dbs = DBNames.split(",");
        for (String DBName : dbs) {
            if (DBName == null || DBName.trim().length() == 0) {
                continue;
            }
            boolean mysqlIsOK = false;
            
            try {
                mysqlIsOK = DBdao.getMySqlIsOk(serverIP, userName, password, DBName);
            } catch (Exception e) {
                e.printStackTrace();
                mysqlIsOK = false;
            }
            Hashtable returnValue = new Hashtable();
            try {
                if (mysqlIsOK) {
                    // 修改为简单的全部都采集。
                    Hashtable<String, NodeGatherIndicators> gatherHash = new Hashtable<String, NodeGatherIndicators>();
                    gatherHash.put("config", nodeGatherIndicators);
                    gatherHash.put("tablestatus", nodeGatherIndicators);
                    gatherHash.put("process", nodeGatherIndicators);
                    gatherHash.put("maxusedconnect", nodeGatherIndicators);
                    gatherHash.put("lock", nodeGatherIndicators);
                    gatherHash.put("keyread", nodeGatherIndicators);
                    gatherHash.put("slow", nodeGatherIndicators);
                    gatherHash.put("opentable", nodeGatherIndicators);
                    gatherHash.put("handlerread", nodeGatherIndicators);
                    gatherHash.put("variables", nodeGatherIndicators);
                    gatherHash.put("status", nodeGatherIndicators);
                    gatherHash.put("scan", nodeGatherIndicators);
                    gatherHash.put("opentable", nodeGatherIndicators);
                    gatherHash.put("createdtmp", nodeGatherIndicators);
                    gatherHash.put("tmptable", nodeGatherIndicators);
                    returnValue = DBdao.getMYSQLData(serverIP, userName, password, DBName, gatherHash);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                DBdao.close();
            }
            System.out.println(serverIP + "=========" + DBName + "========" + mysqlIsOK);
            Pingcollectdata hostdata = null;
            hostdata = new Pingcollectdata();
            hostdata.setIpaddress(serverIP);
            Calendar date = Calendar.getInstance();
            hostdata.setCollecttime(date);
            hostdata.setCategory("MYPing");
            hostdata.setEntity("Utilization");
            hostdata.setSubentity("ConnectUtilization");
            hostdata.setRestype("dynamic");
            hostdata.setUnit("%");
            if (mysqlIsOK) {
                hostdata.setThevalue("100");
            } else {
                hostdata.setThevalue("0");
            }
            try {
                DBdao.createHostData(hostdata);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Hashtable hashtable = new Hashtable();
            hashtable.put(DBName, returnValue);
            if (mysqlIsOK) {
                hashtable.put("runningflag", "正在运行");
            } else {
                hashtable.put("runningflag", "<font color=red>服务停止</font>");
            }
            Hashtable hashtable2= new Hashtable();
            hashtable2.put(serverIP, hashtable);
            try {
                hashtable2.put("ping", hostdata);
                updateData(DBVo, hashtable2);
            } catch (RuntimeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
            if(mysqlIsOK){
                IpTranslation tranfer = new IpTranslation();
                String hex = tranfer.formIpToHex(serverIP);
                serverIP = hex+":"+DBVo.getId();
                try {
                    DBdao.clearTableData("nms_mysqlinfo", serverIP);
                } catch (RuntimeException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    DBdao.addMysql_nmsinfo(serverIP, hashtable, dbs);
                } catch (RuntimeException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else{
                //status 0： 服务停止  1：正在运行
                try {
                    DBdao.addOrUpdateMysql_nmsstatus(serverIP, "0");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
	}

	/**
	 * 更新告警信息    HONGLI
	 * @param vo 数据库实体
	 * @param collectingData 数据库实体中的各种数据信息
	 */
	public void updateData(Object vo , Object collectingData){
		DBVo mysql = (DBVo)vo;		
		Hashtable monitorValueHashtable = (Hashtable)((Hashtable)collectingData).get(mysql.getIpAddress());
		String[] dbs = mysql.getDbName().split(",");
		for (int k = 0; k < dbs.length; k++) {
			try {
                Hashtable mysqldHashtable = (Hashtable)monitorValueHashtable.get(dbs[k]);
                Vector val =  (Vector)mysqldHashtable.get("Val");//数据库详细信息
                java.util.Iterator iterator = val.iterator();//遍历
                
                String maxUsedConnections = "";  //服务器相应的最大连接数
                String threadsConnected = "";//当前打开的连接的数量
                String threadsCreated = "";//创建用来处理连接的线程数
                String openTables = "";//当前打开的表的数量
                while (iterator.hasNext()) {
                	Hashtable tempHashtable = (Hashtable)iterator.next();
                	String variableName = (String)tempHashtable.get("variable_name");
                	if(("Max_used_connections").equals(variableName)){
                		maxUsedConnections = (String)tempHashtable.get("value");
                	}
                	if(("Threads_connected").equals(variableName)){
                		threadsConnected = (String)tempHashtable.get("value");				
                	}
                	if(("Threads_created").equals(variableName)){
                		threadsCreated = (String)tempHashtable.get("value");		
                	}
                	if(("Open_tables").equals(variableName)){
                		openTables = (String)tempHashtable.get("value");		
                	}
                }
                Pingcollectdata pingcollectdata = (Pingcollectdata) mysqldHashtable.get("ping");
                AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(mysql.getId()), AlarmConstant.TYPE_DB, "mysql");//获取采集指标列表
                CheckEventUtil checkEventUtil = new CheckEventUtil();
                for(int i = 0 ; i < list.size() ; i ++){
                	AlarmIndicatorsNode alarmIndicatorsNode = (AlarmIndicatorsNode)list.get(i);
                	if("1".equals(alarmIndicatorsNode.getEnabled())){
                		String indicators = alarmIndicatorsNode.getName();
                		String value = "";//value 是指实际数据库中的值，如 缓冲区命中率    HONGLI
                		if("max_used_connections".equals(indicators)){
                			value = maxUsedConnections;//key 和DBDao collectSQLServerMonitItemsDetail 方法中的pages的key一致
                			SysLogger.info("#######HONG mysql-maxUsedConnections-->  "+maxUsedConnections+"");
                		}else if("threads_connected".equals(indicators)){
                			value = threadsConnected;
                		}else if("threads_created".equals(indicators)){
                			value = threadsCreated;
                		}else if("open_tables".equals(indicators)){
                			value = openTables;
                		}else if("ping".equals(indicators)){
                            value = pingcollectdata.getThevalue();
                        }else {					
                			continue;
                		}
                		if(value == null)continue;
                		checkEventUtil.checkEvent(mysql, alarmIndicatorsNode, value, dbs[k]);
                		
                	}
                	
                	
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
		}
	}
	
	/**
	 * @author HONG 给告警指标发送告警提示和告警短信
	 * @param subtype  
	 * @param subentity 
	 * @param ipaddress
	 * @param objid
	 * @param content
	 * @param flag
	 * @param checkday
	 * @param sIndex
	 * @param bids
	 * @param sysLocation
	 */
	public void createSMS(String subtype,String subentity,String ipaddress,
			String objid,String content,int flag,int checkday,String sIndex,String bids,String sysLocation){
	 	//建立短信		 	
	 	//从内存里获得当前这个IP的PING的值
	 	Calendar date=Calendar.getInstance();
	 	Hashtable sendeddata = ShareData.getSendeddata();
	 	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 	//System.out.println("端口事件--------------------");
	 	try{
 			if (!sendeddata.containsKey(subtype+":"+subentity+":"+ipaddress+":"+sIndex)) {
 				//若不在，则建立短信，并且添加到发送列表里
// 				SysLogger.info("######HONGLI mysql--若不在，则建立短信，并且添加到发送列表里");
	 			Smscontent smscontent = new Smscontent();
	 			String time = sdf.format(date.getTime());
	 			smscontent.setLevel(flag+"");
	 			smscontent.setObjid(objid);
	 			smscontent.setMessage(content);
	 			smscontent.setRecordtime(time);
	 			smscontent.setSubtype(subtype);
	 			smscontent.setSubentity(subentity);
	 			smscontent.setIp(ipaddress);
	 			//发送短信
	 			SmscontentDao smsmanager=new SmscontentDao();
	 			smsmanager.sendURLSmscontent(smscontent);	
				sendeddata.put(subtype+":"+subentity+":"+ipaddress+":"+sIndex,date);	
				
 			} else {
 				//若在，则从已发送短信列表里判断是否已经发送当天的短信
// 				SysLogger.info("######HONGLI mysql--若在，则从已发送短信列表里判断是否已经发送当天的短信");
// 				SysLogger.info("#######HONGLI mysql-subtype+\":\"+subentity+\":\"+ipaddress+\":\"+sIndex---"+subtype+":"+subentity+":"+ipaddress+":"+sIndex);
 				SmsDao smsDao = new SmsDao();
 				List list = new ArrayList();
 				String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
 				String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
 				try {
 					list = smsDao.findByEvent(content,startTime,endTime);
				} catch (RuntimeException e) {
					e.printStackTrace();
				} finally {
					smsDao.close();
				}
				if(list!=null&&list.size()>0){//短信列表里已经发送当天的短信
//					SysLogger.info("######HONGLI mysql--短信列表里已经发送当天的短信");
					Calendar formerdate =(Calendar)sendeddata.get(subtype+":"+subentity+":"+ipaddress+":"+sIndex);		 				
		 			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		 			Date last = null;
		 			Date current = null;
		 			Calendar sendcalen = formerdate;
		 			Date cc = sendcalen.getTime();
		 			String tempsenddate = formatter.format(cc);
		 			
		 			Calendar currentcalen = date;
		 			Date ccc = currentcalen.getTime();
		 			last = formatter.parse(tempsenddate);
		 			String currentsenddate = formatter.format(ccc);
		 			current = formatter.parse(currentsenddate);
		 			
		 			long subvalue = current.getTime()-last.getTime();	
		 			if(checkday == 1){
		 				//检查是否设置了当天发送限制,1为检查,0为不检查
		 				if (subvalue/(1000*60*60*24)>=1){
			 				//超过一天，则再发信息
				 			Smscontent smscontent = new Smscontent();
				 			String time = sdf.format(date.getTime());
				 			smscontent.setLevel(flag+"");
				 			smscontent.setObjid(objid);
				 			smscontent.setMessage(content);
				 			smscontent.setRecordtime(time);
				 			smscontent.setSubtype(subtype);
				 			smscontent.setSubentity(subentity);
				 			smscontent.setIp(ipaddress);//发送短信
				 			SmscontentDao smsmanager=new SmscontentDao();
				 			smsmanager.sendURLSmscontent(smscontent);
							//修改已经发送的短信记录	
							sendeddata.put(subtype+":"+subentity+":"+ipaddress+":"+sIndex,date);
				 		} else {
	                        //开始写事件
			 	            //String sysLocation = "";
//				 			SysLogger.info("#####HONGLI mysql--开始写事件");
			 				createEvent("poll",sysLocation,bids,content,flag,subtype,subentity,ipaddress,objid);
				 		}
		 			}
				} else {
 					Smscontent smscontent = new Smscontent();
 		 			String time = sdf.format(date.getTime());
 		 			smscontent.setLevel(flag+"");
 		 			smscontent.setObjid(objid);
 		 			smscontent.setMessage(content);
 		 			smscontent.setRecordtime(time);
 		 			smscontent.setSubtype(subtype);
 		 			smscontent.setSubentity(subentity);
 		 			smscontent.setIp(ipaddress);
 		 			//发送短信
 		 			SmscontentDao smsmanager=new SmscontentDao();
 		 			smsmanager.sendURLSmscontent(smscontent);	
 					sendeddata.put(subtype+":"+subentity+":"+ipaddress+":"+sIndex,date);
 				}
 				
 			}	 			 			 			 			 	
	 	}catch(Exception e){
	 		e.printStackTrace();
	 	}
	 }
	
	
	/**
	 * @author HONGLI 生成告警事件
	 * @param eventtype
	 * @param eventlocation
	 * @param bid
	 * @param content
	 * @param level1
	 * @param subtype
	 * @param subentity
	 * @param ipaddress
	 * @param objid
	 */
	private void createEvent(String eventtype,String eventlocation,String bid,String content,int level1,String subtype,String subentity,String ipaddress,String objid){
		//生成事件
		SysLogger.info("##############开始生成事件############");
		EventList eventlist = new EventList();
		eventlist.setEventtype(eventtype);
		eventlist.setEventlocation(eventlocation);
		eventlist.setContent(content);
		eventlist.setLevel1(level1);
		eventlist.setManagesign(0);
		eventlist.setBak("");
		eventlist.setRecordtime(Calendar.getInstance());
		eventlist.setReportman("系统轮询");
		eventlist.setBusinessid(bid);
		eventlist.setNodeid(Integer.parseInt(objid));
		eventlist.setOid(0);
		eventlist.setSubtype(subtype);
		eventlist.setSubentity(subentity);
		EventListDao eventlistdao = new EventListDao();
		try{
			eventlistdao.save(eventlist);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			eventlistdao.close();
		}
	}
	
	public void createFileNotExistSMS(String ipaddress) {}
	
	public void createSMS(String db, DBVo dbmonitorlist) {}
}
