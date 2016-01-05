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
	
	
	
	
//	DatagramPacket packet;// ʵ������ͨ�ţ�
	DatagramSocket socket;// �շ����ݣ�
	
	static public int sport = 514;// �������˶˿�514��
//	int		processId;//����id
//	String	processName;//������
//	String	processIdStr;//����id�ַ�	
//	int facility;//�¼���Դ����
//	int priority;//���ȼ�����
//	String facilityName;//�¼���Դ����
//	String priorityName;//���ȼ�����
//	String hostname;//��������
//	String username;//��½�û�
//	Calendar timestamp;//ʱ���
//	String message;//����Ϣ����
//	String ipaddress;//IP��ַ
//	String businessid;//ҵ��ID
//	boolean sign=false;
//	ArrayList nodeflist = new ArrayList();
//	int eventid;//�¼�ID
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
			SysLogger.info("����������syslog�˿ڣ�" + socket.getLocalPort());						
			
		} catch (SocketException e) {
			SysLogger.info("Syslog������������ʧ�ܣ���ȷ�϶˿��Ƿ���ʹ�ã�");
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
			DatagramPacket packet = new DatagramPacket(b, b.length);// ����һ���������ݵģ�
			
			try {
				socket.receive(packet);// �������ݣ�
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Thread thread = new Thread(createTask(packet));
			thread.start();
		}
	}
	
	public Runnable createTask(final DatagramPacket packet){
		return new Runnable(){
			
			int		processId;//����id
			String	processName;//������
			String	processIdStr="";//����id�ַ�	
			int facility;//�¼���Դ����
			int priority;//���ȼ�����
			String facilityName;//�¼���Դ����
			String priorityName;//���ȼ�����
			String hostname;//��������
			String username;//��½�û�
			Calendar timestamp;//ʱ���
			String message;//����Ϣ����
			String ipaddress;//IP��ַ
			String businessid;//ҵ��ID
			boolean sign=false;
			ArrayList nodeflist = new ArrayList();
			int eventid;//�¼�ID
			
			public void run(){
				InetAddress address;// �ͻ���IP��ַ��
				int cport;// �ͻ��˶˿ڣ�
				SysLogger.info(new String(packet.getData()));
				cport = packet.getPort();// ��ȡ�ͻ��˵Ķ˿ڣ�
				address = packet.getAddress(); // ��ȡ�ͻ��˵�IP��ַ
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
						//�����豸��SYSLOG
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
				//��������ȼ�������Ϣ��ʽ���Ϸ�
				if ( lbIdx < 0 || rbIdx < 0 
						|| lbIdx >= (rbIdx - 1) )
					{
					System.err.println
						( "BAD MSG {" + message + "}" );
					return;
					}
				
				//�Ƿ����ȼ��ǺϷ�����
				int priCode = 0;
				String priStr =
					message.substring( lbIdx + 1, rbIdx );
				try { priCode = Integer.parseInt( priStr ); }
				catch ( NumberFormatException ex )
					{
					//SysLogger.info(ex.getMessage());
					return;
					}
				//���¼���Դ���¼��ȼ�
				
				int facility = SyslogDefs.extractFacility( priCode );
				int priority = SyslogDefs.extractPriority( priCode );

//				SysLogger.info("facility---"+SyslogDefs.getFacilityName(facility));
//				SysLogger.info("priority---"+SyslogDefs.getPriorityName(priority));
//				SysLogger.info("sign---"+sign+"    priority---"+priority);
				if(sign && nodeflist.contains(priority+"")){
					//SysLogger.info("##################"+priority);
					//����Ϣ����
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
				//ִ�б���				
				try{
					/*
					//�ɼ������豸�澯��Ϣ�������ݿ�
					syslogManager.createSyslogData(syslog);
					Equipment equipment = equipmanager.getByip(ipaddress);
					//�жϸü����SYSLOG�Ƿ��Ѿ�����Ϊ�澯��ֵ			
					//Syslogconf syslogconf = (Syslogconf)syslogconfManager.getByip(ipaddress);
					
					
					
					
					Syslogconf syslogconf = (Syslogconf)ShareData.getAllsyslogconfdata().get(ipaddress);
					if(syslogconf != null){
						if(syslogconf.getLimenvalues() != null){
						String[] levels = syslogconf.getLimenvalues().split(",");
						if(levels != null && levels.length>0){
							for(int i=0;i<levels.length;i++){
								if(levels[i].equalsIgnoreCase(syslog.getPriorityname().toLowerCase())){
									//���Ѿ����õĸ澯��ֵ����ǰ�ɼ���SYSLOG�������,��澯
									
						 			Smscontent smscontent = new Smscontent();
						 			//String time1 = sdf.format(date.getTime());
						 			smscontent.setMessage(time+"&"+equipment.getEquipname()+"&"+ipaddress+"&"+syslog.getPriorityname()+":"+message+"&level=2");
						 			//���Ͷ���
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
					//����澯��
					//String AlarmDate=timestamp.substring(0,11);
					//String AlarmTime=timestamp.substring(11);
					//�豸����
					//String type=WhichType.getType(syslog.getHostname());
					
					//DoWithAlert.getInstance().insertToAlarmTable(syslog.getHostname(),syslog.getHostname(),AlarmDate,AlarmTime
					//		,"1","syslog",syslog.getMessage(),"0",type);
					
				}
				catch(Exception e){
					//SysLogger.info(e.getMessage());
					SysLogger.info("����Syslog��Ϣ�����������ݿ�û��������");
					e.printStackTrace();
				}
				finally{
					//db.dbClose();
				}
			}
			
			
			public synchronized void processMessage( String message){
				int lbIdx = message.indexOf( '<' );
				int rbIdx = message.indexOf( '>' );
				//��������ȼ�������Ϣ��ʽ���Ϸ�
				if ( lbIdx < 0 || rbIdx < 0 
						|| lbIdx >= (rbIdx - 1) )
					{
					System.err.println
						( "BAD MSG {" + message + "}" );
					return;
					}
				
				//�Ƿ����ȼ��ǺϷ�����
				int priCode = 0;
				String priStr =message.substring( lbIdx + 1, rbIdx );
				try { 
					priCode = Integer.parseInt( priStr ); 
				}catch ( NumberFormatException ex ){
					ex.printStackTrace();
					//SysLogger.info(ex.getMessage());
					return;
				}
				//���¼���Դ���¼��ȼ�
				
				int facility = SyslogDefs.extractFacility( priCode );
				int priority = SyslogDefs.extractPriority( priCode );
				//SysLogger.info("facility--------"+facility);
				//SysLogger.info("priority--------"+priority);
				
				//SysLogger.info("facility---"+SyslogDefs.getFacilityName(facility));
				//SysLogger.info("priority---"+SyslogDefs.getPriorityName(priority));
				//����Ϣ����
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
								//��õ�½�û���Ϣ
								username = allMessages[2].trim();
								
								if(message.indexOf("Ŀ���û���")>=0 && message.indexOf("Ŀ����")>=0 && message.indexOf("Ŀ���¼")>=0){
									//�жϵ�½�û�
			//System.out.println("--------"+message);						
									String bname = message.substring(message.indexOf("Ŀ���û���")+6, message.indexOf("Ŀ����")).trim();
									String dname = message.substring(message.indexOf("Ŀ����")+4, message.indexOf("Ŀ���¼")).trim();
									username = dname+"\\"+bname;						
								}else{
									if(message.indexOf("�û���: ��: ��¼ ID:")<0){
										if(message.indexOf("�û���:")>=0 && message.indexOf("��:")>=0 && message.indexOf("��¼ ID:")>=0){
											String bname = message.substring(message.indexOf("�û���:")+4, message.indexOf("��:")).trim();
											String dname = message.substring(message.indexOf("��:")+2, message.indexOf("��¼ ID:")).trim();
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
								//windows��SYSLOG
								processName = allMessages[0];
								processId = Integer.parseInt(allMessages[1].trim());
								eventid = Integer.parseInt(allMessages[1].trim());
								message = allMessages[2];
							}else if (message.split(":").length == 5){
								//AIXϵͳ					
								processName = allMessages[3];
								message = allMessages[4];	
							}else if (message.split(":").length == 4){
								//
								processName = allMessages[0];
								eventid = Integer.parseInt(allMessages[1].trim());
								processId = Integer.parseInt(allMessages[1].trim());
								
								message = allMessages[3];								
							}else if (message.split(":").length == 6){
								//AIXϵͳ��SYSLOG
								String proc = allMessages[3];
								lbIdx = proc.indexOf( '[' );
								rbIdx = proc.indexOf( ']' );
								//��ΪFTP,�򲻴��������Ϣ
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
								
								//��Զ��":"�����
								processName = allMessages[0];//��Դ,����������
								processId = Integer.parseInt(allMessages[1].trim());//����ID
								eventid = Integer.parseInt(allMessages[1].trim());//�¼�ID
								if(allMessages.length>=2){
									for(int k=2;k<allMessages.length;k++){
										message = message + allMessages[k];
									}
								}
								//SysLogger.info("��Զ��===== "+allMessages.length+"===="+message);
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
	
				//SysLogger.info(time+"���յ�"+ipaddress+"һ��Syslog��Ϣ");
				//System.out.println(time+"���յ�"+hostname+"һ��Syslog��Ϣ");
				//�豸����
				//String devicetype="switcher";
				
				
				
				/*
				DBOpration db=new DBOpration("ExecuteCollectSyslog.java");
				try{
					Vector typevc=new Vector();
					typevc=db.executeQueryVt("select devicetype from syslog_monitor_host where hostip='"+hostname+"'");
					//System.out.println("typevc.size()="+typevc.size());
					//�������Ҫ��ص��豸��ɼ�
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
					
					//ִ�б���
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
					//�ɼ������豸�澯��Ϣ�������ݿ�
					syslogManager.createSyslogData(syslog);
					
					Equipment equipment = equipmanager.getByip(ipaddress);
					//�жϸü����SYSLOG�Ƿ��Ѿ�����Ϊ�澯��ֵ			
					//Syslogconf syslogconf = (Syslogconf)syslogconfManager.getByip(ipaddress);
					
					
					
					
					Syslogconf syslogconf = (Syslogconf)ShareData.getAllsyslogconfdata().get(ipaddress);
					if(syslogconf != null){
						if(syslogconf.getLimenvalues() != null){
						String[] levels = syslogconf.getLimenvalues().split(",");
						if(levels != null && levels.length>0){
							for(int i=0;i<levels.length;i++){
								if(levels[i].equalsIgnoreCase(syslog.getPriorityname().toLowerCase())){
									//���Ѿ����õĸ澯��ֵ����ǰ�ɼ���SYSLOG�������,��澯
									
						 			Smscontent smscontent = new Smscontent();
						 			//String time1 = sdf.format(date.getTime());
						 			smscontent.setMessage(time+"&"+equipment.getEquipname()+"&"+ipaddress+"&"+syslog.getPriorityname()+":"+message+"&level=2");
						 			//���Ͷ���
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
					//����澯��
					//String AlarmDate=timestamp.substring(0,11);
					//String AlarmTime=timestamp.substring(11);
					//�豸����
					//String type=WhichType.getType(syslog.getHostname());
					
					//DoWithAlert.getInstance().insertToAlarmTable(syslog.getHostname(),syslog.getHostname(),AlarmDate,AlarmTime
					//		,"1","syslog",syslog.getMessage(),"0",type);
					
				}
				catch(Exception e){
					SysLogger.info("����Syslog��Ϣ�����������ݿ�û��������");
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
