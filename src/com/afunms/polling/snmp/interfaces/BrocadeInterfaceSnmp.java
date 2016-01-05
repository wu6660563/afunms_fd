package com.afunms.polling.snmp.interfaces;

/*
 * @author hukelei@dhcc.com.cn
 *
 */

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import montnets.SmsDao;

import com.afunms.alarm.send.SendMailAlarm;
import com.afunms.application.model.BrocadeInterface;
import com.afunms.common.util.Arith;
import com.afunms.common.util.Huawei3comtelnetUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtils;
import com.afunms.common.util.SysLogger;
import com.afunms.config.dao.PortconfigDao;
import com.afunms.config.model.Huaweitelnetconf;
import com.afunms.config.model.Portconfig;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.EventList;
import com.afunms.event.model.Smscontent;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.base.Node;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.AllUtilHdx;
import com.afunms.polling.om.DiscardsPerc;
import com.afunms.polling.om.ErrorsPerc;
import com.afunms.polling.om.InPkts;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.OutPkts;
import com.afunms.polling.om.Packs;
import com.afunms.polling.om.Task;
import com.afunms.polling.om.UtilHdx;
import com.afunms.polling.om.UtilHdxPerc;
import com.afunms.polling.snmp.SnmpMibConstants;
import com.afunms.polling.task.TaskXml;
import com.afunms.polling.telnet.Huawei3comvpn;
import com.afunms.topology.model.HostNode;
import com.ibm.ws.management.resources.nodeutils;


/**
 * @author HONGLI
 * ���ƽ������ӿ���Ϣ�ɼ�<δ���>
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BrocadeInterfaceSnmp extends SnmpMonitor {
	
	private static Hashtable ifEntity_ifStatus = null;
	static {
		ifEntity_ifStatus = new Hashtable();
		ifEntity_ifStatus.put("1", "up");
		ifEntity_ifStatus.put("2", "down");
		ifEntity_ifStatus.put("3", "testing");
		ifEntity_ifStatus.put("5", "unknow");
		ifEntity_ifStatus.put("7", "unknow");
	};
	
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 
	 */
	public BrocadeInterfaceSnmp() {
	}

	   public void collectData(Node node,MonitoredItem item){
		   
	   }
	   public void collectData(HostNode node){
		   
	   }
	/* (non-Javadoc)
	 * @see com.dhcc.webnms.host.snmp.AbstractSnmp#collectData()
	 */
	public Hashtable collect_Data(NodeGatherIndicators alarmIndicatorsNode) {
			Hashtable returnHash=new Hashtable();
			Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
			List<BrocadeInterface> interfaceList = new ArrayList<BrocadeInterface>();
			Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
			if(ipAllData == null)ipAllData = new Hashtable();
			try {
				Calendar date=Calendar.getInstance();
			
				try{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					com.afunms.polling.base.Node snmpnode = (com.afunms.polling.base.Node)PollingEngine.getInstance().getNodeByIP(host.getIpAddress());
					Date cc = date.getTime();
					String time = sdf.format(cc);
					snmpnode.setLastTime(time);
				}catch(Exception e){
				  
				}
			  //-------------------------------------------------------------------------------------------interface start			
				Hashtable hash=ShareData.getOctetsdata(host.getIpAddress());
			  if (hash==null)hash=new Hashtable();
			  String[] oids = new String[] {               //oid������Ϣ �ο��ļ� brocade_mib_ref_guide_261_53_0000521_02.pdf
					"1.3.6.1.4.1.1588.2.1.1.1.6.2.1.1", //swFCPortIndex
					"1.3.6.1.4.1.1588.2.1.1.1.6.2.1.2",//swFCPortType
					"1.3.6.1.4.1.1588.2.1.1.1.6.2.1.3",//swFCPortPhyState
					"1.3.6.1.4.1.1588.2.1.1.1.6.2.1.4",//swFCPortOpStatus 
//					"1.3.6.1.4.1.1588.2.1.1.1.6.2.1.5",//swFCPortAdmStatus 
					"1.3.6.1.4.1.1588.2.1.1.1.6.2.1.6",//swFCPortLinkState
					"1.3.6.1.4.1.1588.2.1.1.1.6.2.1.7",//swFCPortTxType
					"1.3.6.1.4.1.1588.2.1.1.1.6.2.1.11",//swFCPortTxWords	
					"1.3.6.1.4.1.1588.2.1.1.1.6.2.1.12",//swFCPortRxWords	
					"1.3.6.1.4.1.1588.2.1.1.1.6.2.1.13",//swFCPortTxFrames	
					"1.3.6.1.4.1.1588.2.1.1.1.6.2.1.14",//swFCPortRxFrames			
				};
				String[][] valueArray = null;   
				try {
					valueArray = SnmpUtils.getTableData(host.getIpAddress(), host.getCommunity(), oids, host.getSnmpversion(), 3, 1000*30);
				} catch(Exception e){
					e.printStackTrace();
				}
				if(valueArray != null){
					for(int i=0;i<valueArray.length;i++){
						String swFCPortIndex = valueArray[i][0];
						String swFCPortType = valueArray[i][1];
						String swFCPortPhyState = valueArray[i][2];
						String swFCPortOpStatus = valueArray[i][3];
//						String swFCPortAdmStatus = valueArray[i][4];
						String swFCPortLinkState = valueArray[i][4];
						String swFCPortTxType = valueArray[i][5];
						String swFCPortTxWords = valueArray[i][6];
						String swFCPortRxWords = valueArray[i][7];
						String swFCPortTxFrames = valueArray[i][8];
						String swFCPortRxFrames = valueArray[i][9];
						
						String swFCPortRxFramesNum = "0";//Frames���յ�����
						String swFCPortRxWordsNum = "0";//words���յ�����
						String swFCPortTxFramesNum = "0";//Frames���������
						String swFCPortTxWordsNum = "0";//words���������
						DecimalFormat df=new DecimalFormat("#.#");
						
						//��������봫����
						List<BrocadeInterface> lastInterfaceList = (ArrayList)ipAllData.get("interfaceList");
						if(lastInterfaceList != null){
							for(BrocadeInterface brocadeInterface : lastInterfaceList){
								if(swFCPortIndex != null && brocadeInterface.getSwFCPortIndex().equals(swFCPortIndex)){//��ͬindex
//									System.out.println("swFCPortIndex==="+swFCPortIndex);
									swFCPortRxFramesNum = df.format(Double.parseDouble(swFCPortRxFrames) - Double.parseDouble(brocadeInterface.getSwFCPortRxFrames()));
									swFCPortRxWordsNum = df.format(Double.parseDouble(swFCPortRxWords) - Double.parseDouble(brocadeInterface.getSwFCPortRxWords()));
									swFCPortTxFramesNum = df.format(Double.parseDouble(swFCPortTxFrames) - Double.parseDouble(brocadeInterface.getSwFCPortTxFrames()));
									swFCPortTxWordsNum = df.format(Double.parseDouble(swFCPortTxWords) - Double.parseDouble(brocadeInterface.getSwFCPortTxWords()));
									
								}
							}
						}
						
						//�˿�״̬
						if("2".equals(swFCPortOpStatus)){
							swFCPortOpStatus = "down";
						}else if("1".equals(swFCPortOpStatus)){
							swFCPortOpStatus = "up";
						}else if("3".equals(swFCPortOpStatus)){
							swFCPortOpStatus = "test";
						}
						
						
						//���ݴ������
						if("2".equals(swFCPortTxType)){
							swFCPortTxType = "Long wave laser";
						}else if("3".equals(swFCPortTxType)){
							swFCPortTxType = "Short wave laser";
						}else if("4".equals(swFCPortTxType)){
							swFCPortTxType = "Long wave LED";
						}else if("5".equals(swFCPortTxType)){
							swFCPortTxType = "Copper (electrical)";
						}
//						System.out.println("i=="+i+"   swFCPortRxFrames=="+swFCPortTxFrames);
				   		BrocadeInterface brocadeInterface = new BrocadeInterface();
				   		brocadeInterface.setSwFCPortIndex(swFCPortIndex);
				   		brocadeInterface.setSwFCPortAdmStatus("");
				   		brocadeInterface.setSwFCPortLinkState(swFCPortLinkState);
				   		brocadeInterface.setSwFCPortOpStatus(swFCPortOpStatus);
				   		brocadeInterface.setSwFCPortPhyState(swFCPortPhyState);
				   		brocadeInterface.setSwFCPortRxFrames(swFCPortRxFrames);
				   		brocadeInterface.setSwFCPortRxWords(swFCPortRxWords);
				   		brocadeInterface.setSwFCPortTxFrames(swFCPortTxFrames);
				   		brocadeInterface.setSwFCPortTxType(swFCPortTxType);
				   		brocadeInterface.setSwFCPortTxWords(swFCPortTxWords);
				   		brocadeInterface.setSwFCPortType(swFCPortType);
				   		brocadeInterface.setSwFCPortRxFramesNum(swFCPortRxFramesNum);
				   		brocadeInterface.setSwFCPortTxFramesNum(swFCPortTxFramesNum);
				   		brocadeInterface.setSwFCPortRxWordsNum(swFCPortRxWordsNum);
				   		brocadeInterface.setSwFCPortTxWordsNum(swFCPortTxWordsNum);
						interfaceList.add(brocadeInterface);
				   	}
				}
			  }catch(Exception e){
				  e.printStackTrace();
			}
	    
		ipAllData.put("interfaceList", interfaceList);
	    returnHash.put("interfaceList", interfaceList);
	    host=null;
	    interfaceList=null;
	    ipAllData=null;
	   
	    
	    return returnHash;
	}
	
	public int getInterval(float d,String t){
		int interval=0;
		  if(t.equals("d"))
			 interval =(int) d*24*60*60; //����
		  else if(t.equals("h"))
			 interval =(int) d*60*60;    //Сʱ
		  else if(t.equals("m"))
			 interval = (int)d*60;       //����
		else if(t.equals("s"))
					 interval =(int) d;       //��
		return interval;
	}
	
	public void createSMS(String subtype,String subentity,String ipaddress,String objid,String content,int flag,int checkday,String sIndex,String bids){
	 	//��������		 	
	 	//���ڴ����õ�ǰ���IP��PING��ֵ
	 	Calendar date=Calendar.getInstance();
	 	Hashtable sendeddata = ShareData.getSendeddata();
	 	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 	//System.out.println("�˿��¼�--------------------");
	 	try{
 			if (!sendeddata.containsKey(subtype+":"+subentity+":"+ipaddress+":"+sIndex)) {
 				//�����ڣ��������ţ�������ӵ������б���
	 			Smscontent smscontent = new Smscontent();
	 			String time = sdf.format(date.getTime());
	 			smscontent.setLevel(flag+"");
	 			smscontent.setObjid(objid);
	 			smscontent.setMessage(content);
	 			smscontent.setRecordtime(time);
	 			smscontent.setSubtype(subtype);
	 			smscontent.setSubentity(subentity);
	 			smscontent.setIp(ipaddress);
	 			//���Ͷ���
	 			SmscontentDao smsmanager=new SmscontentDao();
	 			smsmanager.sendURLSmscontent(smscontent);	
				sendeddata.put(subtype+":"+subentity+":"+ipaddress+":"+sIndex,date);	
				
 			} else {
 				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
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
				if(list!=null&&list.size()>0){//�����б����Ѿ����͵���Ķ���
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
		 				//����Ƿ������˵��췢������,1Ϊ���,0Ϊ�����
		 				if (subvalue/(1000*60*60*24)>=1){
			 				//����һ�죬���ٷ���Ϣ
				 			Smscontent smscontent = new Smscontent();
				 			String time = sdf.format(date.getTime());
				 			smscontent.setLevel(flag+"");
				 			smscontent.setObjid(objid);
				 			smscontent.setMessage(content);
				 			smscontent.setRecordtime(time);
				 			smscontent.setSubtype(subtype);
				 			smscontent.setSubentity(subentity);
				 			smscontent.setIp(ipaddress);//���Ͷ���
				 			SmscontentDao smsmanager=new SmscontentDao();
				 			smsmanager.sendURLSmscontent(smscontent);
							//�޸��Ѿ����͵Ķ��ż�¼	
							sendeddata.put(subtype+":"+subentity+":"+ipaddress+":"+sIndex,date);
				 		} else {
	                        //��ʼд�¼�
			 	            String sysLocation = "";
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
 		 			//���Ͷ���
 		 			SmscontentDao smsmanager=new SmscontentDao();
 		 			smsmanager.sendURLSmscontent(smscontent);	
 					sendeddata.put(subtype+":"+subentity+":"+ipaddress+":"+sIndex,date);
 				}
 				
 			}	 			 			 			 			 	
	 	}catch(Exception e){
	 		e.printStackTrace();
	 	}
	 }
	
	private void createEvent(String eventtype,String eventlocation,String bid,String content,int level1,String subtype,String subentity,String ipaddress,String objid){
		//�����¼�
		SysLogger.info("##############��ʼ�����¼�############");
		EventList eventlist = new EventList();
		eventlist.setEventtype(eventtype);
		eventlist.setEventlocation(eventlocation);
		eventlist.setContent(content);
		eventlist.setLevel1(level1);
		eventlist.setManagesign(0);
		eventlist.setBak("");
		eventlist.setRecordtime(Calendar.getInstance());
		eventlist.setReportman("ϵͳ��ѯ");
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
}





