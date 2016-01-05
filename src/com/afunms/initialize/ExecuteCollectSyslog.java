/**
 * <p>Description:action center,at the same time, the control legal power</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-06
 */

package com.afunms.initialize;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SyslogDefs;
import com.afunms.event.dao.NetSyslogDao;
import com.afunms.event.dao.SyslogDao;
import com.afunms.event.model.NetSyslog;
import com.afunms.event.model.Syslog;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Host;
import com.afunms.topology.dao.NetSyslogNodeRuleDao;
import com.afunms.topology.model.NetSyslogNodeRule;

public class ExecuteCollectSyslog extends TimerTask {
	
	
	
	
//	DatagramPacket packet;// 实现数据通信；
	DatagramSocket socket;// 收发数据；
	
	static public int sport = 514;// 服务器端端口514；
//	int		processId;//进程id
//	String	processName;//进程名
//	String	processIdStr;//进程id字符	
//	int facility;//事件来源编码
//	int priority;//优先级编码
//	String facilityName;//事件来源名称
//	String priorityName;//优先级名称
//	String hostname;//主机名称
//	String username;//登陆用户
//	Calendar timestamp;//时间戳
//	String message;//得消息内容
//	String ipaddress;//IP地址
//	String businessid;//业务ID
//	boolean sign=false;
//	ArrayList nodeflist = new ArrayList();
//	int eventid;//事件ID
	/*
	I_SyslogCollectData syslogManager = new SyslogDataManager();
	I_Syslogconf syslogconfManager = new SyslogconfManager();
	I_Equipment equipmanager=new EquipmentManager();
	private I_Smscontent smsmanager=new SmscontentManager();
	*/
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public ExecuteCollectSyslog(){
		try {
			socket = new DatagramSocket(sport);
			SysLogger.info("已启动监听syslog端口：" + socket.getLocalPort());						
			
		} catch (SocketException e) {
			SysLogger.info("Syslog监听程序启动失败，请确认端口是否在使用！");
			e.printStackTrace();
		}
	}

	public void run() {
		if (socket == null){
			return;
		}
		while(true){
			byte[] b = new byte[1024];
			//byte[] b = new byte[2048];
			DatagramPacket packet = new DatagramPacket(b, b.length);// 生成一个接收数据的；
			
			try {
				socket.receive(packet);// 接收数据；
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Thread thread = new Thread(createTask(packet));
			thread.start();
		}
	}
	
	public Runnable createTask(final DatagramPacket packet){
		return new Runnable(){
			
			int		processId;//进程id
			String	processName;//进程名
			String	processIdStr="";//进程id字符	
			int facility;//事件来源编码
			int priority;//优先级编码
			String facilityName;//事件来源名称
			String priorityName;//优先级名称
			String hostname;//主机名称
			String username;//登陆用户
			Calendar timestamp;//时间戳
			String message;//得消息内容
			String ipaddress;//IP地址
			String businessid;//业务ID
			boolean sign=false;
			ArrayList nodeflist = new ArrayList();
			int eventid;//事件ID
			
			public void run(){
				InetAddress address;// 客户端IP地址；
				int cport;// 客户端端口；
				SysLogger.info(new String(packet.getData()));
				cport = packet.getPort();// 获取客户端的端口；
				address = packet.getAddress(); // 获取客户端的IP地址
				hostname = address.getHostName();					
				ipaddress = address.getHostAddress();
				//SysLogger.info(ipaddress+"======ipaddress=========="+ hostname +"================hostname========");
				Host host = null;
				try{
					host = (Host)PollingEngine.getInstance().getNodeByIP(ipaddress);
				}catch(Exception e){
					e.printStackTrace();
				}
				if(host != null){
					hostname = host.getAlias();
					
					nodeflist.clear();
					
				    NetSyslogNodeRule noderule = null;
				    NetSyslogNodeRuleDao nodeRuleDao = new NetSyslogNodeRuleDao();
				    try{
				    	noderule = (NetSyslogNodeRule)nodeRuleDao.findByIpaddress(ipaddress);
				    }catch(Exception e){
				    	e.printStackTrace();
				    }finally{
				    	nodeRuleDao.close();
				    }
				    if(noderule == null){
				    	sign = false;
				    }else{
						String nodefacility = noderule.getFacility();
						String[] nodefacilitys = nodefacility.split(",");
						if(nodefacilitys != null && nodefacilitys.length>0){
							for(int i = 0;i<nodefacilitys.length;i++){
								nodeflist.add(nodefacilitys[i]);
							}
						}
						sign = true;
					}
					String bids = ","+host.getBid();
					businessid = bids;
					//SysLogger.info(packet.toString()+"&&&&");
					String sfc = new String(packet.getData());	
					//SysLogger.info(sfc);
					if(host.getCategory() == 1 || host.getCategory() == 2 || host.getCategory() == 3 || host.getCategory() == 7 || host.getCategory() == 8){
						//网络设备的SYSLOG
						this.processNetMessage(sfc.trim(),host.getCategory());
					}else{
						this.processMessage(sfc.trim());
					}
				}
			}
			
			
			public synchronized void processNetMessage( String message,int category)
			{

				int lbIdx = message.indexOf( '<' );
				int rbIdx = message.indexOf( '>' );
				//如果无优先级，则消息格式不合法
				if ( lbIdx < 0 || rbIdx < 0 
						|| lbIdx >= (rbIdx - 1) )
					{
					System.err.println
						( "BAD MSG {" + message + "}" );
					return;
					}
				
				//是否优先级是合法数字
				int priCode = 0;
				String priStr =
					message.substring( lbIdx + 1, rbIdx );
				try { priCode = Integer.parseInt( priStr ); }
				catch ( NumberFormatException ex )
					{
					//SysLogger.info(ex.getMessage());
					return;
					}
				//得事件来源和事件等级
				
				int facility = SyslogDefs.extractFacility( priCode );
				int priority = SyslogDefs.extractPriority( priCode );

//				SysLogger.info("facility---"+SyslogDefs.getFacilityName(facility));
//				SysLogger.info("priority---"+SyslogDefs.getPriorityName(priority));
//				SysLogger.info("sign---"+sign+"    priority---"+priority);
				if(sign && nodeflist.contains(priority+"")){
					//SysLogger.info("##################"+priority);
					//得消息内容
					timestamp = Calendar.getInstance();
					message =
						message.substring
							( rbIdx + 1, (message.length()) );
					NetSyslog syslog=new NetSyslog();
					syslog.setFacility(facility);
					syslog.setPriority(priority);
					syslog.setFacilityName(SyslogDefs.getFacilityName(facility));
					syslog.setPriorityName(SyslogDefs.getPriorityName(priority));
					syslog.setRecordtime(timestamp);
					syslog.setHostname(hostname);
					syslog.setMessage(message);
					syslog.setIpaddress(ipaddress);
					syslog.setBusinessid(businessid);
					syslog.setCategory(category);
					NetSyslogDao sdao = new NetSyslogDao();
					try{
						sdao.save(syslog);
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						sdao.close();
					}
				}
				
				SimpleDateFormat dfYear = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
				timestamp=Calendar.getInstance();
				Date cc = timestamp.getTime();
				String time = dfYear.format(cc);					
				//执行保存				
				try{
					/*
					//采集来的设备告警信息插入数据库
					syslogManager.createSyslogData(syslog);
					Equipment equipment = equipmanager.getByip(ipaddress);
					//判断该级别的SYSLOG是否已经设置为告警阀值			
					//Syslogconf syslogconf = (Syslogconf)syslogconfManager.getByip(ipaddress);
					
					
					
					
					Syslogconf syslogconf = (Syslogconf)ShareData.getAllsyslogconfdata().get(ipaddress);
					if(syslogconf != null){
						if(syslogconf.getLimenvalues() != null){
						String[] levels = syslogconf.getLimenvalues().split(",");
						if(levels != null && levels.length>0){
							for(int i=0;i<levels.length;i++){
								if(levels[i].equalsIgnoreCase(syslog.getPriorityname().toLowerCase())){
									//若已经设置的告警阀值跟当前采集的SYSLOG级别相等,则告警
									
						 			Smscontent smscontent = new Smscontent();
						 			//String time1 = sdf.format(date.getTime());
						 			smscontent.setMessage(time+"&"+equipment.getEquipname()+"&"+ipaddress+"&"+syslog.getPriorityname()+":"+message+"&level=2");
						 			//发送短信
						 			Vector tosend = new Vector();
						 			tosend.add(smscontent);
						 			smsmanager.sendSmscontent(tosend);
									
								}
							}
						}
						}
					}
					*/
					/*
					if(syslog.getPriorityname().equals("")){
						
					}
					*/
					int tt=0;
					//tt=db.executeUpdate(sql);
					//插入告警表
					//String AlarmDate=timestamp.substring(0,11);
					//String AlarmTime=timestamp.substring(11);
					//设备类型
					//String type=WhichType.getType(syslog.getHostname());
					
					//DoWithAlert.getInstance().insertToAlarmTable(syslog.getHostname(),syslog.getHostname(),AlarmDate,AlarmTime
					//		,"1","syslog",syslog.getMessage(),"0",type);
					
				}
				catch(Exception e){
					//SysLogger.info(e.getMessage());
					SysLogger.info("保存Syslog信息出错，可能数据库没有启动！");
					e.printStackTrace();
				}
				finally{
					//db.dbClose();
				}
			}
			
			
			public synchronized void processMessage( String message){
				int lbIdx = message.indexOf( '<' );
				int rbIdx = message.indexOf( '>' );
				//如果无优先级，则消息格式不合法
				if ( lbIdx < 0 || rbIdx < 0 
						|| lbIdx >= (rbIdx - 1) )
					{
					System.err.println
						( "BAD MSG {" + message + "}" );
					return;
					}
				
				//是否优先级是合法数字
				int priCode = 0;
				String priStr =message.substring( lbIdx + 1, rbIdx );
				try { 
					priCode = Integer.parseInt( priStr ); 
				}catch ( NumberFormatException ex ){
					ex.printStackTrace();
					//SysLogger.info(ex.getMessage());
					return;
				}
				//得事件来源和事件等级
				
				int facility = SyslogDefs.extractFacility( priCode );
				int priority = SyslogDefs.extractPriority( priCode );
				//SysLogger.info("facility--------"+facility);
				//SysLogger.info("priority--------"+priority);
				
				//SysLogger.info("facility---"+SyslogDefs.getFacilityName(facility));
				//SysLogger.info("priority---"+SyslogDefs.getPriorityName(priority));
				//得消息内容
				if(sign && nodeflist.contains(priority+"")){
					message =message.substring( rbIdx + 1, (message.length()) );
					message = message.replaceAll("\"", "'");
					boolean stdMsg = true;
					String[] allMessages = new String[message.split(":").length];
					allMessages=message.split(":");	
					try{
						if(allMessages[0].trim().equals("Security")||allMessages[0].trim().equals("W3SVC")){
							processName = allMessages[0];
							processId=0;
							eventid = Integer.parseInt(allMessages[1].trim());
							message = message.substring(allMessages[0].length()+allMessages[1].length()+2);
							if(allMessages[0].trim().equals("Security")){
								//获得登陆用户信息
								username = allMessages[2].trim();
								
								if(message.indexOf("目标用户名")>=0 && message.indexOf("目标域")>=0 && message.indexOf("目标登录")>=0){
									//判断登陆用户
			//System.out.println("--------"+message);						
									String bname = message.substring(message.indexOf("目标用户名")+6, message.indexOf("目标域")).trim();
									String dname = message.substring(message.indexOf("目标域")+4, message.indexOf("目标登录")).trim();
									username = dname+"\\"+bname;						
								}else{
									if(message.indexOf("用户名: 域: 登录 ID:")<0){
										if(message.indexOf("用户名:")>=0 && message.indexOf("域:")>=0 && message.indexOf("登录 ID:")>=0){
											String bname = message.substring(message.indexOf("用户名:")+4, message.indexOf("域:")).trim();
											String dname = message.substring(message.indexOf("域:")+2, message.indexOf("登录 ID:")).trim();
											username = dname+"\\"+bname;
										}
									}
								}
								
			//System.out.println("================================================"+username);
								/*
								String[] user = message.trim().split(":");
								if(user != null && user.length>0){
									username = user[0];
								}
								*/
							}
							
						} else{
							//SysLogger.info("###################################"+allMessages.length);
							if(message.split(":").length == 3){			
								//windows的SYSLOG
								processName = allMessages[0];
								processId = Integer.parseInt(allMessages[1].trim());
								eventid = Integer.parseInt(allMessages[1].trim());
								message = allMessages[2];
							}else if (message.split(":").length == 5){
								//AIX系统					
								processName = allMessages[3];
								message = allMessages[4];	
							}else if (message.split(":").length == 4){
								//
								processName = allMessages[0];
								eventid = Integer.parseInt(allMessages[1].trim());
								processId = Integer.parseInt(allMessages[1].trim());
								
								message = allMessages[3];								
							}else if (message.split(":").length == 6){
								//AIX系统的SYSLOG
								String proc = allMessages[3];
								lbIdx = proc.indexOf( '[' );
								rbIdx = proc.indexOf( ']' );
								//若为FTP,则不处理相关信息
								if(allMessages[4].trim().equals("ftp"))return;
								if(lbIdx > -1){
									processName = proc.substring( 0, lbIdx );
									processId = Integer.parseInt(proc.substring( lbIdx + 1, rbIdx ));
									eventid = Integer.parseInt(proc.substring( lbIdx + 1, rbIdx ));				
								}else{
									processName = proc;
									processId=0;
									eventid=0;
								}
								message = allMessages[5];		
							}else{
								
								//针对多个":"的情况
								processName = allMessages[0];//来源,即进程名称
								processId = Integer.parseInt(allMessages[1].trim());//进程ID
								eventid = Integer.parseInt(allMessages[1].trim());//事件ID
								if(allMessages.length>=2){
									for(int k=2;k<allMessages.length;k++){
										message = message + allMessages[k];
									}
								}
								//SysLogger.info("针对多个===== "+allMessages.length+"===="+message);
							}							
						}
					}catch(Exception ex){
						//SysLogger.info(ex.getMessage());
					}
				timestamp = Calendar.getInstance();
				/*
				if ( message.length() < 16 ){
					stdMsg = false;
				}
				else if (	   message.charAt(3)	!= ' '
							|| message.charAt(6)	!= ' '
							|| message.charAt(9)	!= ':'
							|| message.charAt(12)	!= ':'
							|| message.charAt(15)	!= ' ' )
					{
					stdMsg = false;
					}
	
				if ( ! stdMsg )
					{
					try {
						timestamp = Calendar.getInstance();
					}
					catch ( IllegalArgumentException ex )
						{
						System.err.println( "ERROR INTERNAL DATE ERROR!" );
						timestamp = null;
						}
					}
				else
					{
					//timestamp = message.substring( 0, 15 );
					message = message.substring( 16 );
					}		
				lbIdx = message.indexOf( '[' );
				rbIdx = message.indexOf( ']' );
				int colonIdx = message.indexOf( ':' );
				int spaceIdx = message.indexOf( ' ' );
	
	
				if ( lbIdx < (rbIdx - 1)
						&& colonIdx == (rbIdx + 1)
						&& spaceIdx == (colonIdx + 1) )
					{
					processName = message.substring( 0, lbIdx );
					processIdStr = message.substring( lbIdx + 1, rbIdx );
					message = message.substring( colonIdx + 2 );
	
					try { processId = Integer.parseInt( processIdStr ); }
					catch ( NumberFormatException ex )
						{
						System.err.println
							( "ERROR Bad process id '" + processIdStr + "'" );
						processId = 0;
						}
					}
				else if ( lbIdx < 0 && rbIdx < 0
							&& colonIdx > 0 && spaceIdx == (colonIdx + 1) )
					{
					processName = message.substring( 0, colonIdx );
					message = message.substring( colonIdx + 2 );
					}
				*/
				SimpleDateFormat dfYear = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
				timestamp=Calendar.getInstance();
				Date cc = timestamp.getTime();
				String time = dfYear.format(cc);					
	
				//SysLogger.info(time+"接收到"+ipaddress+"一条Syslog信息");
				//System.out.println(time+"接收到"+hostname+"一条Syslog信息");
				//设备类型
				//String devicetype="switcher";
				
				
				
				/*
				DBOpration db=new DBOpration("ExecuteCollectSyslog.java");
				try{
					Vector typevc=new Vector();
					typevc=db.executeQueryVt("select devicetype from syslog_monitor_host where hostip='"+hostname+"'");
					//System.out.println("typevc.size()="+typevc.size());
					//如果是需要监控的设备则采集
					if(typevc.size()<1)
					{
						return ;
					}else
					{
						devicetype=((Vector)typevc.elementAt(0)).elementAt(0).toString();
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
				*/
				
					Syslog syslog=new Syslog();
					syslog.setFacility(facility);
					syslog.setPriority(priority);
					syslog.setFacilityName(SyslogDefs.getFacilityName(facility));
					syslog.setPriorityName(SyslogDefs.getPriorityName(priority));
					syslog.setRecordtime(timestamp);
					syslog.setProcessid(processId);
					syslog.setProcessidstr(processIdStr);
					syslog.setProcessname(processName);
					syslog.setHostname(hostname);	
					if(username != null && username.trim().length()>0){
						syslog.setUsername(username);
					}else
						syslog.setUsername(hostname);
					syslog.setMessage(message);
					syslog.setIpaddress(ipaddress);
					syslog.setEventid(eventid);
					
					//执行保存
					SyslogDao sdao = null;
					try{
						sdao = new SyslogDao();
						sdao.createSyslogData(syslog);
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						sdao.close();
					}
					
					NetSyslog  _syslog=new NetSyslog();
					_syslog.setFacility(facility);
					_syslog.setPriority(priority);
					_syslog.setFacilityName(SyslogDefs.getFacilityName(facility));
					_syslog.setPriorityName(SyslogDefs.getPriorityName(priority));
					_syslog.setRecordtime(timestamp);
					_syslog.setHostname(hostname);
					_syslog.setMessage(message);
					_syslog.setIpaddress(ipaddress);
					_syslog.setBusinessid(businessid);
					_syslog.setCategory(4);
					NetSyslogDao _sdao = new NetSyslogDao();
					try{
						_sdao.save(_syslog);
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						_sdao.close();
					}
					
				}
				try{
					/*
					//采集来的设备告警信息插入数据库
					syslogManager.createSyslogData(syslog);
					
					Equipment equipment = equipmanager.getByip(ipaddress);
					//判断该级别的SYSLOG是否已经设置为告警阀值			
					//Syslogconf syslogconf = (Syslogconf)syslogconfManager.getByip(ipaddress);
					
					
					
					
					Syslogconf syslogconf = (Syslogconf)ShareData.getAllsyslogconfdata().get(ipaddress);
					if(syslogconf != null){
						if(syslogconf.getLimenvalues() != null){
						String[] levels = syslogconf.getLimenvalues().split(",");
						if(levels != null && levels.length>0){
							for(int i=0;i<levels.length;i++){
								if(levels[i].equalsIgnoreCase(syslog.getPriorityname().toLowerCase())){
									//若已经设置的告警阀值跟当前采集的SYSLOG级别相等,则告警
									
						 			Smscontent smscontent = new Smscontent();
						 			//String time1 = sdf.format(date.getTime());
						 			smscontent.setMessage(time+"&"+equipment.getEquipname()+"&"+ipaddress+"&"+syslog.getPriorityname()+":"+message+"&level=2");
						 			//发送短信
						 			Vector tosend = new Vector();
						 			tosend.add(smscontent);
						 			smsmanager.sendSmscontent(tosend);
									
								}
							}
						}
						}
					}
					*/
					/*
					if(syslog.getPriorityname().equals("")){
						
					}
					*/
					int tt=0;
					//tt=db.executeUpdate(sql);
					//插入告警表
					//String AlarmDate=timestamp.substring(0,11);
					//String AlarmTime=timestamp.substring(11);
					//设备类型
					//String type=WhichType.getType(syslog.getHostname());
					
					//DoWithAlert.getInstance().insertToAlarmTable(syslog.getHostname(),syslog.getHostname(),AlarmDate,AlarmTime
					//		,"1","syslog",syslog.getMessage(),"0",type);
					
				}
				catch(Exception e){
					SysLogger.info("保存Syslog信息出错，可能数据库没有启动！");
					e.printStackTrace();
				}
				finally{
					//db.dbClose();
				}
			}
		};
	}
	

	
	
	
	public void close() {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
