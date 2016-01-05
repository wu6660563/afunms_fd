/*
 * Created on 2005-4-22
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.afunms.polling.task;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;

import com.afunms.common.util.Arith;
import com.afunms.common.util.SysLogger;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.base.LinkRoad;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.Task;
import com.afunms.polling.om.UtilHdx;
import com.afunms.topology.dao.LinkDao;
import com.afunms.topology.model.Link;
import com.afunms.topology.model.NodeMonitor;
import com.afunms.common.util.ShareData;
import com.ibm.db2.jcc.c.i;

/**
 * @author hukelei
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CheckLinkTask extends MonitorTask {
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		SysLogger.info("#####开始检查链路####");
		try{
			//获取需要检查的链路
			List linkList = PollingEngine.getInstance().getLinkList();
			
			//获取运行的线程个数
    		int numThreads = 200;
    		try {
    			List numList = new ArrayList();
    			TaskXml taskxml = new TaskXml();
    			numList = taskxml.ListXml();
    			for (int i = 0; i < numList.size(); i++) {
    				Task task = new Task();
    				BeanUtils.copyProperties(task, numList.get(i));
    				if (task.getTaskname().equals("pingthreadnum")){
    					numThreads = task.getPolltime().intValue();
    				}
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		

		  
	  		// 生成线程池
	  		ThreadPool threadPool = null;		
    		if(linkList != null && linkList.size()>0){
    			threadPool = new ThreadPool(linkList.size());	
    	  		// 运行任务
    	  		if(ShareData.getRunflag() == 0){
    	  			//第一次启动
    	  			ShareData.setRunflag(1);
    	  			SysLogger.info("### 系统第一次启动,不进行链路检测,跳过 ###");
    	  		}else{
    	  			//先集中采集所有链路节点的数据集合存到内存中 线程中不再做单个链路节点的数据采集
    	  			getLinknodeInterfaceData(linkList);
    	  			for (int i=0; i<linkList.size(); i++) {
    	      			threadPool.runTask(createTask((LinkRoad)linkList.get(i)));
    	  			}
    	  		}
    	  		// 关闭线程池并等待所有任务完成
    	  		threadPool.join();
    	  		threadPool.close();
    		}
    		threadPool = null;
    		
    		//批量保存link数据
    		processLinkData();
		}
		catch(Exception e){
			SysLogger.error("error in CheckLinkTask!"+e.getMessage(), e);
	  	}
		finally{
			SysLogger.info("********CheckLinkTask Thread Count : "+Thread.activeCount());
		}		
	}

	/**
	 * 先集中采集所有链路节点的数据集合存到内存中 
	 * @param linkList
	 */
	public static Hashtable getLinknodeInterfaceData(List linkList) {
		I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();
		String[] netInterfaceItem = { "index", "ifDescr",
				"ifSpeed", "ifOperStatus", "ifOutBroadcastPkts",
				"ifInBroadcastPkts", "ifOutMulticastPkts",
				"ifInMulticastPkts", "OutBandwidthUtilHdx",
				"InBandwidthUtilHdx", "InBandwidthUtilHdxPerc",
				"OutBandwidthUtilHdxPerc" };
		List<String> ipList = new ArrayList<String>();
		for(int m=0; m<linkList.size(); m++){
			Object obj = linkList.get(m);
			Host host1 = null;
			Host host2 = null;
			if(obj instanceof LinkRoad){
				LinkRoad lr = (LinkRoad)linkList.get(m);
				host1 = (Host) PollingEngine.getInstance().getNodeByID(lr.getStartId());
				host2 = (Host) PollingEngine.getInstance().getNodeByID(lr.getEndId());
			}
			if(obj instanceof Link){
				Link lr = (Link)linkList.get(m);
				host1 = (Host) PollingEngine.getInstance().getNodeByID(lr.getStartId());
				host2 = (Host) PollingEngine.getInstance().getNodeByID(lr.getEndId());
			}
			if(host1 != null){
				ipList.add(host1.getIpAddress());
			}
			if(host2 != null){
				ipList.add(host2.getIpAddress());
			}
		}
		//排除重复元素
		HashSet h = new HashSet(ipList);   
		ipList.clear();   
		ipList.addAll(h); 
		
		Hashtable interfaceHash = hostlastmanager.getInterfaces(ipList,netInterfaceItem,"index","",""); 
		//存入内存
		ShareData.setAllLinknodeInterfaceData(interfaceHash);
		return interfaceHash;
	}

	/**
	 * 批量保存link数据
	 */
	private void processLinkData() {
		if(ShareData.getAllLinkData() != null && !ShareData.getAllLinkData().isEmpty()){
			LinkDao linkDao = null;
			try{
				linkDao = new LinkDao();
				linkDao.processlinkData();
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(linkDao != null){
					linkDao.close();
				}
			}
		}
	}

	/**
	创建任务
	*/	
	private Runnable createTask(final LinkRoad linkroad) {
		return new Runnable() {
		    public void run() {
		    	//开始运行任务
				try {
					//需要做分布式判断
					String runmodel = PollingEngine.getCollectwebflag(); 
					//链路节点的数据集合
					Hashtable interfaceHash = ShareData.getAllLinknodeInterfaceData();
					int flag = 0;
					LinkRoad lr = linkroad;
					Host host1 = (Host) PollingEngine.getInstance().getNodeByID(
							lr.getStartId());
					Host host2 = (Host) PollingEngine.getInstance().getNodeByID(
							lr.getEndId());
					
//					IfEntity if1 = host1.getIfEntityByIndex(lr.getStartIndex());
//					IfEntity if2 = host2.getIfEntityByIndex(lr.getEndIndex());

					// yangjun
					String start_inutilhdx = "0";
					String start_oututilhdx = "0";
					String start_inutilhdxperc = "0";
					String start_oututilhdxperc = "0";
					String end_inutilhdx = "0";
					String end_oututilhdx = "0";
					String end_inutilhdxperc = "0";
					String end_oututilhdxperc = "0";
					String starOper = "";
					String endOper = "";
					// end
//					if (if1 == null || if2 == null) {
//						lr.setAlarm(true);
//						flag = 1;
//					}
					if (host1 != null && host2 != null) {
						I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();

						Vector vector1 = new Vector();
						Vector vector2 = new Vector();
						String[] netInterfaceItem = { "index", "ifDescr",
								"ifSpeed", "ifOperStatus", "ifOutBroadcastPkts",
								"ifInBroadcastPkts", "ifOutMulticastPkts",
								"ifInMulticastPkts", "OutBandwidthUtilHdx",
								"InBandwidthUtilHdx", "InBandwidthUtilHdxPerc",
								"OutBandwidthUtilHdxPerc" };
							if("0".equals(runmodel)){
						       	//采集与访问是集成模式	
								vector1 = hostlastmanager.getInterface_share(host1
										.getIpAddress(), netInterfaceItem, "index", "",
										"");
								vector2 = hostlastmanager.getInterface_share(host2
										.getIpAddress(), netInterfaceItem, "index", "",
										"");
							}else{
								//采集与访问是分离模式
//								vector1 = hostlastmanager.getInterface(host1.getIpAddress(),netInterfaceItem,"index","",""); 
//								vector2 = hostlastmanager.getInterface(host2.getIpAddress(),netInterfaceItem,"index","",""); 
								if(interfaceHash != null && interfaceHash.containsKey(host1.getIpAddress())){
									vector1 = (Vector)interfaceHash.get(host1.getIpAddress());
								}
								if(interfaceHash != null && interfaceHash.containsKey(host2.getIpAddress())){
									vector2 = (Vector)interfaceHash.get(host2.getIpAddress());
								}
							}
						
							
						// SysLogger.info("#####开始检查链路端口####");
						if (vector1 != null && vector1.size() > 0) {
							for (int k = 0; k < vector1.size(); k++) {
								String[] strs = (String[]) vector1.get(k);
								String index = strs[0];
								String ifOutNUcastPkts = strs[4]
										.replaceAll("个", "");
								String ifInNUcastPkts = strs[5].replaceAll("个", "");
								String ifOutUcastPkts = strs[6].replaceAll("个", "");
								String ifInUcastPkts = strs[7].replaceAll("个", "");
								// SysLogger.info(host1.getIpAddress()+"===="+ifOutNUcastPkts+"=="+ifInNUcastPkts+"==="+ifOutUcastPkts+"==="+ifInUcastPkts);
								if (index.equalsIgnoreCase(lr.getStartIndex())) {
									starOper = strs[3];
									start_oututilhdx = strs[8].replaceAll("KB/秒", "").replaceAll("kb/s", "").replaceAll("kb/秒", "").replaceAll("KB/S", "");// yangjun
									start_inutilhdx = strs[9].replaceAll("KB/秒", "").replaceAll("kb/s", "").replaceAll("kb/秒", "").replaceAll("KB/S", "");// yangjun
									start_oututilhdxperc = strs[10].replaceAll("%",
											"");// yangjun
									start_inutilhdxperc = strs[11].replaceAll("%",
											"");// yangjun
//									else {
//										// SysLogger.info("#####开始检查链路端口数据包####");
//										List molist = host1.getMoidList();
//										// SysLogger.info(host1.getIpAddress()+"====molist:
//										// "+molist.size());
//										if (molist != null && molist.size() > 0) {
//											for (int m = 0; m < molist.size(); m++) {
//												NodeMonitor nm = (NodeMonitor) molist
//														.get(m);
//												// SysLogger.info(host1.getIpAddress()+"==index:"+index+"===Subentity:"+nm.getSubentity());
//												if (nm
//														.getSubentity()
//														.equalsIgnoreCase(
//																"ifInMulticastPkts")) {
//													int pktflag = checkEvent(
//															host1,
//															index,
//															"ifInMulticastPkts",
//															Double
//																	.parseDouble(ifInUcastPkts),
//															nm);
//													if (pktflag > 0) {
//														// 有告警产生
//														lr.setAlarm(true);
//														flag = 1;
//														break;
//													}
//												} else if (nm
//														.getSubentity()
//														.equalsIgnoreCase(
//																"ifInBroadcastPkts")) {
//													int pktflag = checkEvent(
//															host1,
//															index,
//															"ifInBroadcastPkts",
//															Double
//																	.parseDouble(ifInNUcastPkts),
//															nm);
//													if (pktflag > 0) {
//														// 有告警产生
//														lr.setAlarm(true);
//														flag = 1;
//														break;
//													}
//												} else if (nm
//														.getSubentity()
//														.equalsIgnoreCase(
//																"ifOutMulticastPkts")) {
//													int pktflag = checkEvent(
//															host1,
//															index,
//															"ifOutMulticastPkts",
//															Double
//																	.parseDouble(ifOutUcastPkts),
//															nm);
//													if (pktflag > 0) {
//														// 有告警产生
//														lr.setAlarm(true);
//														flag = 1;
//														break;
//													}
//												} else if (nm
//														.getSubentity()
//														.equalsIgnoreCase(
//																"ifOutBroadcastPkts")) {
//													int pktflag = checkEvent(
//															host1,
//															index,
//															"ifOutBroadcastPkts",
//															Double
//																	.parseDouble(ifOutNUcastPkts),
//															nm);
//													if (pktflag > 0) {
//														// 有告警产生
//														lr.setAlarm(true);
//														flag = 1;
//														break;
//													}
//												}
//
//											}
//										}
//									}
//									if (flag == 1)break;
								}
							}
						}
						if (vector2 != null && vector2.size() > 0) {
							for (int k = 0; k < vector2.size(); k++) {
								String[] strs = (String[]) vector2.get(k);
								String index = strs[0];
								String ifOutNUcastPkts = strs[4]
										.replaceAll("个", "");
								String ifInNUcastPkts = strs[5].replaceAll("个", "");
								String ifOutUcastPkts = strs[6].replaceAll("个", "");
								String ifInUcastPkts = strs[7].replaceAll("个", "");
								if (index.equalsIgnoreCase(lr.getEndIndex())) {
									endOper = strs[3];
									end_oututilhdx = strs[8].replaceAll("KB/秒", "").replaceAll("kb/s", "").replaceAll("kb/秒", "").replaceAll("KB/S", "");// yangjun
									end_inutilhdx = strs[9].replaceAll("KB/秒", "").replaceAll("kb/s", "").replaceAll("kb/秒", "").replaceAll("KB/S", "");// yangjun
									end_oututilhdxperc = strs[10].replaceAll("%",
											"");// yangjun
									end_inutilhdxperc = strs[11]
											.replaceAll("%", "");// yangjun
//									else {
//										List molist = host2.getMoidList();
//										if (molist != null && molist.size() > 0) {
//											for (int m = 0; m < molist.size(); m++) {
//												NodeMonitor nm = (NodeMonitor) molist
//														.get(m);
//												if (nm.getSubentity().equals(
//														"ifInUcastPkts")) {
//													int pktflag = checkEvent(
//															host2,
//															index,
//															"ifInUcastPkts",
//															Double
//																	.parseDouble(ifInUcastPkts),
//															nm);
//													if (pktflag > 0) {
//														// 有告警产生
//														lr.setAlarm(true);
//														flag = 1;
//														break;
//													}
//												} else if (nm.getSubentity()
//														.equals("ifInNUcastPkts")) {
//													int pktflag = checkEvent(
//															host2,
//															index,
//															"ifInNUcastPkts",
//															Double
//																	.parseDouble(ifInNUcastPkts),
//															nm);
//													if (pktflag > 0) {
//														// 有告警产生
//														lr.setAlarm(true);
//														flag = 1;
//														break;
//													}
//												} else if (nm.getSubentity()
//														.equals("ifOutUcastPkts")) {
//													int pktflag = checkEvent(
//															host2,
//															index,
//															"ifOutUcastPkts",
//															Double
//																	.parseDouble(ifOutUcastPkts),
//															nm);
//													if (pktflag > 0) {
//														// 有告警产生
//														lr.setAlarm(true);
//														flag = 1;
//														break;
//													}
//												} else if (nm.getSubentity()
//														.equals("ifOutNUcastPkts")) {
//													int pktflag = checkEvent(
//															host2,
//															index,
//															"ifOutNUcastPkts",
//															Double
//																	.parseDouble(ifOutNUcastPkts),
//															nm);
//													if (pktflag > 0) {
//														// 有告警产生
//														lr.setAlarm(true);
//														flag = 1;
//														break;
//													}
//												}
//
//											}
//										}
//									}
//									if (flag == 1)break;
								}
							}
						}
						// yangjun
						if ("down".equalsIgnoreCase(starOper)) {
							lr.setAlarm(true);
							flag = 1;
						} 
						if ("down".equalsIgnoreCase(endOper)) {
							lr.setAlarm(true);
							flag = 1;
						} 
						int downspeed = (Integer.parseInt(start_oututilhdx) + Integer
								.parseInt(end_inutilhdx.replaceAll("KB/秒", "").replaceAll("kb/s", "").replaceAll("kb/秒", "").replaceAll("KB/S", ""))) / 2;
						int upspeed = (Integer.parseInt(start_inutilhdx) + Integer
								.parseInt(end_oututilhdx.replaceAll("KB/秒", "").replaceAll("kb/s", "").replaceAll("kb/秒", "").replaceAll("KB/S", ""))) / 2;
						
						//将链路状态插入数据库
						Calendar date=Calendar.getInstance();
						int linkflag = 100;
						if ("down".equalsIgnoreCase(starOper)||"down".equalsIgnoreCase(endOper)) {
							linkflag = 0;
						}
//						System.out.println("starOper----->"+starOper);
						Interfacecollectdata interfacelink=new Interfacecollectdata();
						interfacelink.setIpaddress("");
						interfacelink.setCollecttime(date);
						interfacelink.setCategory("Ping");
						interfacelink.setEntity("Utilization");
						interfacelink.setSubentity("ConnectUtilization");
						interfacelink.setRestype("dynamic");
						interfacelink.setUnit("%");	
						interfacelink.setChname("");
						interfacelink.setThevalue(linkflag+"");
						Vector linkstatusv = new Vector();
						linkstatusv.add(interfacelink);
						
						//将上下行流速插入数据库,待完成
						Vector v = new Vector();
						
						UtilHdx uputilhdx=new UtilHdx();
						uputilhdx.setIpaddress("");
						uputilhdx.setCollecttime(date);
						uputilhdx.setCategory("");
						uputilhdx.setEntity("UP");
						uputilhdx.setSubentity("");
						uputilhdx.setRestype("dynamic");
						uputilhdx.setUnit("Kb/秒");	
						uputilhdx.setChname("");										   	
						DecimalFormat df=new DecimalFormat("#.##");//yangjun 
						uputilhdx.setThevalue(df.format(upspeed));
						v.add(uputilhdx);
						
						UtilHdx downutilhdx=new UtilHdx();
						downutilhdx.setIpaddress("");
						downutilhdx.setCollecttime(date);
						downutilhdx.setCategory("");
						downutilhdx.setEntity("DOWN");
						downutilhdx.setSubentity("");
						downutilhdx.setRestype("dynamic");
						downutilhdx.setUnit("Kb/秒");	
						downutilhdx.setChname("");
						downutilhdx.setThevalue(df.format(downspeed));
						v.add(downutilhdx);
						
						
						
						if (lr.getMaxSpeed() != null
								&& !"".equals(lr.getMaxSpeed())&& !"null".equals(lr.getMaxSpeed())) {
							if (upspeed > Integer.parseInt(lr.getMaxSpeed())) {
								// 有告警产生
								lr.setAlarm(true);
								flag = 1;
							}
							if (downspeed > Integer.parseInt(lr.getMaxSpeed())) {
								// 有告警产生
								lr.setAlarm(true);
								flag = 1;
							}
						} else {// 链路没有设定阀值的情况下，由端口阀值获得

						}
						double downperc = 0;
						try {
							if (start_oututilhdxperc != null
									&& start_oututilhdxperc.trim().length() > 0
									&& end_inutilhdxperc != null
									&& end_inutilhdxperc.trim().length() > 0)
								downperc = Arith.div((Double
										.parseDouble(start_oututilhdxperc) + Double
										.parseDouble(end_inutilhdxperc)), 2);
						} catch (Exception e) {
							e.printStackTrace();
						}
						double upperc = 0;
						try {
							if (start_inutilhdxperc != null
									&& start_inutilhdxperc.trim().length() > 0
									&& end_oututilhdxperc != null
									&& end_oututilhdxperc.trim().length() > 0)
								upperc = Arith.div((Double
										.parseDouble(start_inutilhdxperc) + Double
										.parseDouble(end_oututilhdxperc)), 2);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (lr.getMaxPer() != null && !"".equals(lr.getMaxPer())&& !"null".equals(lr.getMaxPer())) {
							if (upperc > Double.parseDouble(lr.getMaxPer())) {
	                            //有告警产生
								lr.setAlarm(true);
								flag = 1;
							}
							if (downperc > Double.parseDouble(lr.getMaxPer())) {
	                            //有告警产生
								lr.setAlarm(true);
								flag = 1;
							}
						} else {// 链路没有设定阀值的情况下，由端口阀值获得
							
						}
						
						Interfacecollectdata utilhdxperc=new Interfacecollectdata();
						utilhdxperc.setIpaddress("");
						utilhdxperc.setCollecttime(date);
						utilhdxperc.setCategory("");
						utilhdxperc.setEntity("");
						utilhdxperc.setSubentity("");
						utilhdxperc.setRestype("dynamic");
						utilhdxperc.setUnit("%");	
						utilhdxperc.setChname("");
						utilhdxperc.setThevalue(df.format(upperc+downperc));
						Vector utilhdxpercv = new Vector();
						utilhdxpercv.add(utilhdxperc);
						
//						LinkDao linkdao = new LinkDao();
//						try{
//							linkdao.saveutil(v, lr.getId());
//							linkdao.savelinkstatus(linkstatusv, lr.getId());
//							linkdao.savelinkutilperc(utilhdxpercv, lr.getId());
//							linkdao.savelink(v, linkstatusv, utilhdxpercv, lr.getId());
							//到外面保存
//						}catch(Exception e){
//							e.printStackTrace();
//						}finally{
//							linkdao.close();
//						}
						Hashtable linkdataHash = new Hashtable();
						linkdataHash.put("util", v);
						linkdataHash.put("linkstatus", linkstatusv);
						linkdataHash.put("linkutilperc", utilhdxpercv);
						linkdataHash.put("linkid", lr.getId());
						ShareData.getAllLinkData().put(lr.getId(), linkdataHash);
						// end
					}
					if (flag == 0) {
						lr.setAlarm(false);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		        //结束运行任务
		    }
		};
	}
	
	private int checkEvent(Host host, String index, String subentity,
			double thevalue, NodeMonitor nm) {
		int level = 0;
		int returnflag = 0;
		// boolean flag = false;
		Vector limenV = new Vector();
		if (subentity.equalsIgnoreCase("ping")) {
		} else {
			// SysLogger.info(host.getIpAddress()+"==index:"+index+"===thevalue:"+thevalue+"====nmValue:"+nm.getLimenvalue0()+":"+nm.getLimenvalue1());
			if (thevalue >= nm.getLimenvalue0()) {
				// 大于一级
				if (thevalue >= nm.getLimenvalue1()) {
					// 大于二级
					if (thevalue >= nm.getLimenvalue2()) {
						// 大于三级
						level = 3;
					} else {
						level = 2;
					}
				} else {
					level = 1;
				}
			} else {
				level = 0;
			}
		}
		// SysLogger.info(host.getIpAddress()+"===index:"+index+"===level:"+level);
		if (level == 0)
			return level;
		if (host.getAlarmPksHash() != null && host.getAlarmPksHash().size() > 0) {
			// SysLogger.info(host.getIpAddress()+"------AlarmPksHash###"+host.getAlarmPksHash().size());
			// 判断是否已经含有该事件类型
			Hashtable subentityhash = (Hashtable) host.getAlarmPksHash().get(
					subentity + ":" + index);
			if (subentityhash != null && subentityhash.size() > 0) {
				// 含有该事件类型,判断是否是同级别告警
				if (subentityhash.get(level) != null) {
					// 同级别存在,判断超过次数
					int times = (Integer) subentityhash.get(level);
					int limentimes = 0;
					if (level == 1) {
						// 第一级别
						limentimes = nm.getTime0();
					} else if (level == 2) {
						// 第二级别
						limentimes = nm.getTime1();
					} else {
						// 第三级别
						limentimes = nm.getTime2();
					}
					if (times >= limentimes) {
						// 产生事件
						// flag = true;
						returnflag = level;
					} else {
						// 不产生事件,但需要把告警产生次数加1
						subentityhash = new Hashtable();
						subentityhash.put(level, times + 1);
						// SysLogger.info("put====subentity:"+subentity+"===level:"+level);
						host.getAlarmPksHash().put(subentity + ":" + index,
								subentityhash);
						SysLogger.info(nm.getIp() + " put====subentity:"
								+ subentity + ":" + index + "===level:" + level
								+ "==times:" + (times + 1));
					}

				} else {
					// 同级别不存在,则第一次产生该级别的告警,存入,同时清除该事件类型的其他级别告警
					subentityhash = new Hashtable();
					subentityhash.put(level, 1);
					host.getAlarmPksHash().put(subentity + ":" + index,
							subentityhash);
					SysLogger.info(nm.getIp() + " put====subentity:"
							+ subentity + ":" + index + "===level:" + level
							+ "===times:" + 1);
				}
			} else {
				// 第一次
				subentityhash = new Hashtable();
				subentityhash.put(level, 1);
				host.getAlarmPksHash().put(subentity + ":" + index,
						subentityhash);
				SysLogger.info(nm.getIp() + " put====subentity:" + subentity
						+ ":" + index + "===level:" + level + "===times:" + 1);
			}
		} else {
			// 第一次
			host.setAlarmPksHash(new Hashtable());
			// this.alarmHash = new Hashtable();
			Hashtable subentityhash = new Hashtable();
			subentityhash.put(level, 1);
			host.getAlarmPksHash().put(subentity + ":" + index, subentityhash);
			SysLogger.info(nm.getIp() + " put====subentity:" + subentity + ":"
					+ index + "===level:" + level + "===times:" + 1);
		}
		return returnflag;

	}
}
