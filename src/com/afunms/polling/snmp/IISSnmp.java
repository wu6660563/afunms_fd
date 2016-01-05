package com.afunms.polling.snmp;

/*
 * @author hukelei@dhcc.com.cn
 *
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.TableEvent;

import com.afunms.application.model.DominoCache;
import com.afunms.application.model.DominoDb;
import com.afunms.application.model.DominoHttp;
import com.afunms.application.model.DominoLdap;
import com.afunms.application.model.DominoMail;
import com.afunms.application.model.DominoMem;
import com.afunms.application.model.IISConfig;
import com.afunms.application.model.IISVo;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.config.model.Procs;
import com.afunms.monitor.executor.base.MonitorInterface;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.topology.model.HostNode;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class IISSnmp extends SnmpMonitor implements MonitorInterface {
	private  String host="1.1.1.1";
	private Hashtable sendeddata = ShareData.getProcsendeddata();
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 
	 */
	public IISSnmp() {
	}

	   public void collectData(Node node,MonitoredItem item){
		   
	   }
	   public Hashtable collect_Data(HostNode node){
		   return null;
	   }
	   public void collectData(HostNode node){
		   
	   }
	/* (non-Javadoc)
	 * @see com.dhcc.webnms.host.snmp.AbstractSnmp#collectData()
	 */
	public List collect_Data(IISConfig iisconf) {		
		List list = new ArrayList();
		try {
			
			Calendar date=Calendar.getInstance();
			
			  try{
				  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				  com.afunms.polling.base.Node snmpnode = (com.afunms.polling.base.Node)PollingEngine.getInstance().getDominoByIP(iisconf.getIpaddress());
				  Date cc = date.getTime();
				  String time = sdf.format(cc);
				  snmpnode.setLastTime(time);
			  }catch(Exception e){
				  
			  }			
			try{	
				String[] oids =                
						  new String[] {  
							  "1.3.6.1.4.1.311.1.7.3.1.1",	//totalBytesSentHighWord
							  "1.3.6.1.4.1.311.1.7.3.1.2",	//totalBytesSentLowWord
							  "1.3.6.1.4.1.311.1.7.3.1.3",	//totalBytesReceivedHighWord
							  "1.3.6.1.4.1.311.1.7.3.1.4",	//totalBytesReceivedLowWord
							  "1.3.6.1.4.1.311.1.7.3.1.5",	//totalFilesSent
							  "1.3.6.1.4.1.311.1.7.3.1.6",	//totalFilesReceived
							  "1.3.6.1.4.1.311.1.7.3.1.7",	//currentAnonymousUsers
							  "1.3.6.1.4.1.311.1.7.3.1.9",	//totalAnonymousUsers
							  "1.3.6.1.4.1.311.1.7.3.1.11",	//maxAnonymousUsers
							  "1.3.6.1.4.1.311.1.7.3.1.13",	//currentConnections
							  "1.3.6.1.4.1.311.1.7.3.1.14",	//maxConnections
							  "1.3.6.1.4.1.311.1.7.3.1.15",	//connectionAttempts
							  "1.3.6.1.4.1.311.1.7.3.1.16",	//logonAttempts
							  "1.3.6.1.4.1.311.1.7.3.1.18",	//totalGets
							  "1.3.6.1.4.1.311.1.7.3.1.19",	//totalPosts
							  "1.3.6.1.4.1.311.1.7.3.1.43"};	//totalNotFoundErrors 
				String[][] valueArray = null;
				try {
					valueArray = snmp.getTableData(iisconf.getIpaddress(),iisconf.getCommunity(),oids);
				} catch(Exception e){
					e.printStackTrace();
					SysLogger.error(iisconf.getIpaddress() + "_IISSnmp");
				}
				if(valueArray != null){
				   	  for(int i=0;i<valueArray.length;i++)
				   	  {
				   		IISVo iisvo = new IISVo();
				   		iisvo.setTotalBytesSentHighWord(valueArray[i][0]);
				   		iisvo.setTotalBytesSentLowWord(valueArray[i][1]);
				   		iisvo.setTotalBytesReceivedHighWord(valueArray[i][2]);
				   		iisvo.setTotalBytesReceivedLowWord(valueArray[i][3]);
				   		
				   		iisvo.setTotalFilesSent(valueArray[i][4]);
				   		iisvo.setTotalFilesReceived(valueArray[i][5]);
				   		iisvo.setCurrentAnonymousUsers(valueArray[i][6]);
				   		iisvo.setTotalAnonymousUsers(valueArray[i][7]);
				   		iisvo.setMaxAnonymousUsers(valueArray[i][8]);
				   		iisvo.setCurrentConnections(valueArray[i][9]);
				   		iisvo.setMaxConnections(valueArray[i][10]);
				   		iisvo.setConnectionAttempts(valueArray[i][11]);
				   		iisvo.setLogonAttempts(valueArray[i][12]);
				   		iisvo.setTotalGets(valueArray[i][13]);
				   		iisvo.setTotalPosts(valueArray[i][14]);
				   		iisvo.setTotalNotFoundErrors(valueArray[i][15]);
				   		
				   		list.add(iisvo);
				   		for(int j=0;j<16;j++){
				   			String sValue = valueArray[i][j];
				   			//SysLogger.info(sValue+"=========j:"+j);
				   		}
				   	  }
				}
		  }
		  catch(Exception e){
			  e.printStackTrace();
			  list=null;
		  }
		}catch(Exception e){
			list=null;
			e.printStackTrace();
			return null;
		}finally{
			}

		return list;
	}
	
	
	 public void createSMS(Procs procs){
		 	Procs lastprocs = null;
		 	//建立短信	
		 	procs.setCollecttime(Calendar.getInstance());
		 	//从已经发送的短信列表里获得当前该PROC已经发送的短信
		 	lastprocs = (Procs)sendeddata.get(procs.getIpaddress()+":"+procs.getProcname());	
		 	/*
		 	try{		 				 		
		 		if (lastprocs==null){
		 			//内存中不存在	,表明没发过短信,则发短信
		 			Equipment equipment = equipmentManager.getByip(procs.getIpaddress());
		 			Smscontent smscontent = new Smscontent();
		 			String time = sdf.format(procs.getCollecttime().getTime());
		 			smscontent.setMessage(time+"&"+equipment.getEquipname()+"&"+equipment.getIpaddress()+"&"+procs.getProcname()+"&进程丢失&level=2");
		 			//发送短信
		 			Vector tosend = new Vector();
		 			tosend.add(smscontent);		 			
		 			smsmanager.sendSmscontent(tosend);
		 			//把该进程信息添加到已经发送的进程短信列表里,以IP:进程名作为key
		 			sendeddata.put(procs.getIpaddress()+":"+procs.getProcname(),procs);		 						 				
		 		}else{
		 			//若已经发送的短信列表存在这个IP的PROC进程
		 			//若在，则从已发送短信列表里判断是否已经发送当天的短信		 				
		 			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		 			Date last = null;
		 			Date current = null;
		 			Calendar sendcalen = (Calendar)lastprocs.getCollecttime();
		 			Date cc = sendcalen.getTime();
		 			String tempsenddate = formatter.format(cc);
		 			
		 			Calendar currentcalen = (Calendar)procs.getCollecttime();
		 			cc = currentcalen.getTime();
		 			last = formatter.parse(tempsenddate);
		 			String currentsenddate = formatter.format(cc);
		 			current = formatter.parse(currentsenddate);
		 			
		 			long subvalue = current.getTime()-last.getTime();			 			
		 			
		 			if (subvalue/(1000*60*60*24)>=1){
		 				//超过一天，则再发信息
			 			Smscontent smscontent = new Smscontent();
			 			String time = sdf.format(procs.getCollecttime().getTime());
			 			Equipment equipment = equipmentManager.getByip(procs.getIpaddress());
			 			if (equipment == null){
			 				return;
			 			}else
			 				smscontent.setMessage(time+"&"+equipment.getEquipname()+"&"+equipment.getIpaddress()+"&"+procs.getProcname()+"&进程丢失&level=2");
			 			
			 			//发送短信
			 			Vector tosend = new Vector();
			 			tosend.add(smscontent);		 			
			 			smsmanager.sendSmscontent(tosend);
			 			//把该进程信息添加到已经发送的进程短信列表里,以IP:进程名作为key
			 			sendeddata.put(procs.getIpaddress()+":"+procs.getProcname(),procs);		 						 				
			 		}else{
			 			//没超过一天,则只写事件
			 			Vector eventtmpV = new Vector();
						Eventlist event = new Eventlist();
						  Monitoriplist monitoriplist = (Monitoriplist)monitormanager.getByIpaddress(host);
						  event.setEventtype("host");
						  event.setEventlocation(host);
						  event.setManagesign(new Integer(0));
						  event.setReportman("monitorpc");
						  event.setRecordtime(Calendar.getInstance());
						  event.setLevel1(new Integer(1));
						  event.setEquipment(equipmentManager.getByip(monitoriplist.getIpaddress()));
						  event.setNetlocation(equipmentManager.getByip(monitoriplist.getIpaddress()).getNetlocation());
						  String time = sdf.format(Calendar.getInstance().getTime());
						  event.setContent(monitoriplist.getEquipname()+"&"+monitoriplist.getIpaddress()+"&"+time+"进程"+procs.getProcname()+"丢失&level=1");
						  eventtmpV.add(event);
						  try{
							  eventmanager.createEventlist(eventtmpV);
						  }catch(Exception e){
							  e.printStackTrace();
						  }
						  
			 		}
		 		}
		 	}catch(Exception e){
		 		e.printStackTrace();
		 	}
		 	*/
		 }
		public int getInterval(float d,String t){
			int interval=0;
			  if(t.equals("d"))
				 interval =(int) d*24*60*60; //天数
			  else if(t.equals("h"))
				 interval =(int) d*60*60;    //小时
			  else if(t.equals("m"))
				 interval = (int)d*60;       //分钟
			else if(t.equals("s"))
						 interval =(int) d;       //秒
			return interval;
}
}





