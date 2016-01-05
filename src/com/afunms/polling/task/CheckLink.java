package com.afunms.polling.task;

import java.util.*;

import com.afunms.common.util.Arith;
import com.afunms.common.util.SnmpUtil;
import com.afunms.common.util.SysLogger;
import com.afunms.discovery.IpAddress;
import com.afunms.discovery.IpRouter;
import com.afunms.polling.base.*;
import com.afunms.polling.node.*;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.topology.model.NodeMonitor;

public class CheckLink extends BaseTask {
	public void executeTask() {
		SysLogger.info("#####��ʼ�����·####");
		List linkList = PollingEngine.getInstance().getLinkList();
		for (int i = 0; i < linkList.size(); i++) {
			try {
				int flag = 0;
				LinkRoad lr = (LinkRoad) linkList.get(i);
				Host host1 = (Host) PollingEngine.getInstance().getNodeByID(
						lr.getStartId());
				Host host2 = (Host) PollingEngine.getInstance().getNodeByID(
						lr.getEndId());
				IfEntity if1 = host1.getIfEntityByIndex(lr.getStartIndex());
				IfEntity if2 = host2.getIfEntityByIndex(lr.getEndIndex());

				// yangjun
				String start_inutilhdx = "0";
				String start_oututilhdx = "0";
				String start_inutilhdxperc = "0";
				String start_oututilhdxperc = "0";
				String end_inutilhdx = "0";
				String end_oututilhdx = "0";
				String end_inutilhdxperc = "0";
				String end_oututilhdxperc = "0";
				// end
				if (if1 == null || if2 == null) {
					lr.setAlarm(true);
					flag = 1;
				}
				if (if1 != null && if2 != null) {
					// �ж����ӵĶ˿��Ƿ�DOWN
					I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();

					Vector vector1 = new Vector();
					String[] netInterfaceItem = { "index", "ifDescr",
							"ifSpeed", "ifOperStatus", "ifOutBroadcastPkts",
							"ifInBroadcastPkts", "ifOutMulticastPkts",
							"ifInMulticastPkts", "OutBandwidthUtilHdx",
							"InBandwidthUtilHdx", "InBandwidthUtilHdxPerc",
							"OutBandwidthUtilHdxPerc" };
					try {
						vector1 = hostlastmanager.getInterface_share(host1
								.getIpAddress(), netInterfaceItem, "index", "",
								"");
					} catch (Exception e) {
						e.printStackTrace();
					}
					Vector vector2 = new Vector();
					try {
						vector2 = hostlastmanager.getInterface_share(host2
								.getIpAddress(), netInterfaceItem, "index", "",
								"");
					} catch (Exception e) {
						e.printStackTrace();
					}
					// SysLogger.info("#####��ʼ�����·�˿�####");
					if (vector1 != null && vector1.size() > 0) {
						for (int k = 0; k < vector1.size(); k++) {
							String[] strs = (String[]) vector1.get(k);
							String ifOperStatus = strs[3];
							String index = strs[0];
							String ifOutNUcastPkts = strs[4]
									.replaceAll("��", "");
							String ifInNUcastPkts = strs[5].replaceAll("��", "");
							String ifOutUcastPkts = strs[6].replaceAll("��", "");
							String ifInUcastPkts = strs[7].replaceAll("��", "");
							// SysLogger.info(host1.getIpAddress()+"===="+ifOutNUcastPkts+"=="+ifInNUcastPkts+"==="+ifOutUcastPkts+"==="+ifInUcastPkts);
							if (index.equalsIgnoreCase(if1.getIndex())) {
								start_oututilhdx = strs[8].replaceAll("KB/��",
										"");// yangjun
								start_inutilhdx = strs[9]
										.replaceAll("KB/��", "");// yangjun
								start_oututilhdxperc = strs[10].replaceAll("%",
										"");// yangjun
								start_inutilhdxperc = strs[11].replaceAll("%",
										"");// yangjun
								if ("down".equalsIgnoreCase(ifOperStatus)) {
									lr.setAlarm(true);
									flag = 1;
								} else {
									// SysLogger.info("#####��ʼ�����·�˿����ݰ�####");
									List molist = host1.getMoidList();
									// SysLogger.info(host1.getIpAddress()+"====molist:
									// "+molist.size());
									if (molist != null && molist.size() > 0) {
										for (int m = 0; m < molist.size(); m++) {
											NodeMonitor nm = (NodeMonitor) molist
													.get(m);
											// SysLogger.info(host1.getIpAddress()+"==index:"+index+"===Subentity:"+nm.getSubentity());
											if (nm
													.getSubentity()
													.equalsIgnoreCase(
															"ifInMulticastPkts")) {
												int pktflag = checkEvent(
														host1,
														index,
														"ifInMulticastPkts",
														Double
																.parseDouble(ifInUcastPkts),
														nm);
												if (pktflag > 0) {
													// �и澯����
													lr.setAlarm(true);
													flag = 1;
													break;
												}
											} else if (nm
													.getSubentity()
													.equalsIgnoreCase(
															"ifInBroadcastPkts")) {
												int pktflag = checkEvent(
														host1,
														index,
														"ifInBroadcastPkts",
														Double
																.parseDouble(ifInNUcastPkts),
														nm);
												if (pktflag > 0) {
													// �и澯����
													lr.setAlarm(true);
													flag = 1;
													break;
												}
											} else if (nm
													.getSubentity()
													.equalsIgnoreCase(
															"ifOutMulticastPkts")) {
												int pktflag = checkEvent(
														host1,
														index,
														"ifOutMulticastPkts",
														Double
																.parseDouble(ifOutUcastPkts),
														nm);
												if (pktflag > 0) {
													// �и澯����
													lr.setAlarm(true);
													flag = 1;
													break;
												}
											} else if (nm
													.getSubentity()
													.equalsIgnoreCase(
															"ifOutBroadcastPkts")) {
												int pktflag = checkEvent(
														host1,
														index,
														"ifOutBroadcastPkts",
														Double
																.parseDouble(ifOutNUcastPkts),
														nm);
												if (pktflag > 0) {
													// �и澯����
													lr.setAlarm(true);
													flag = 1;
													break;
												}
											}

										}
									}
								}
								if (flag == 1)
									break;

							}
						}
					}
					if (vector2 != null && vector2.size() > 0) {
						for (int k = 0; k < vector2.size(); k++) {
							String[] strs = (String[]) vector2.get(k);
							String ifOperStatus = strs[3];
							String index = strs[0];
							String ifOutNUcastPkts = strs[4]
									.replaceAll("��", "");
							String ifInNUcastPkts = strs[5].replaceAll("��", "");
							String ifOutUcastPkts = strs[6].replaceAll("��", "");
							String ifInUcastPkts = strs[7].replaceAll("��", "");
							if (index.equalsIgnoreCase(if2.getIndex())) {
								end_oututilhdx = strs[8].replaceAll("KB/��", "");// yangjun
								end_inutilhdx = strs[9].replaceAll("KB/��", "");// yangjun
								end_oututilhdxperc = strs[10].replaceAll("%",
										"");// yangjun
								end_inutilhdxperc = strs[11]
										.replaceAll("%", "");// yangjun
								if ("down".equalsIgnoreCase(ifOperStatus)) {
									lr.setAlarm(true);
									flag = 1;
								} else {
									List molist = host2.getMoidList();
									if (molist != null && molist.size() > 0) {
										for (int m = 0; m < molist.size(); m++) {
											NodeMonitor nm = (NodeMonitor) molist
													.get(m);
											if (nm.getSubentity().equals(
													"ifInUcastPkts")) {
												int pktflag = checkEvent(
														host2,
														index,
														"ifInUcastPkts",
														Double
																.parseDouble(ifInUcastPkts),
														nm);
												if (pktflag > 0) {
													// �и澯����
													lr.setAlarm(true);
													flag = 1;
													break;
												}
											} else if (nm.getSubentity()
													.equals("ifInNUcastPkts")) {
												int pktflag = checkEvent(
														host2,
														index,
														"ifInNUcastPkts",
														Double
																.parseDouble(ifInNUcastPkts),
														nm);
												if (pktflag > 0) {
													// �и澯����
													lr.setAlarm(true);
													flag = 1;
													break;
												}
											} else if (nm.getSubentity()
													.equals("ifOutUcastPkts")) {
												int pktflag = checkEvent(
														host2,
														index,
														"ifOutUcastPkts",
														Double
																.parseDouble(ifOutUcastPkts),
														nm);
												if (pktflag > 0) {
													// �и澯����
													lr.setAlarm(true);
													flag = 1;
													break;
												}
											} else if (nm.getSubentity()
													.equals("ifOutNUcastPkts")) {
												int pktflag = checkEvent(
														host2,
														index,
														"ifOutNUcastPkts",
														Double
																.parseDouble(ifOutNUcastPkts),
														nm);
												if (pktflag > 0) {
													// �и澯����
													lr.setAlarm(true);
													flag = 1;
													break;
												}
											}

										}
									}
								}
								if (flag == 1)
									break;
							}
						}
					}
					// yangjun
					int downspeed = (Integer.parseInt(start_oututilhdx) + Integer
							.parseInt(end_inutilhdx)) / 2;
					int upspeed = (Integer.parseInt(start_inutilhdx) + Integer
							.parseInt(end_oututilhdx)) / 2;
					if (lr.getMaxSpeed() != null
							&& !"".equals(lr.getMaxSpeed())&& !"null".equals(lr.getMaxSpeed())) {
						if (upspeed > Integer.parseInt(lr.getMaxSpeed())) {
							// �и澯����
							lr.setAlarm(true);
							flag = 1;
						}
						if (downspeed > Integer.parseInt(lr.getMaxSpeed())) {
							// �и澯����
							lr.setAlarm(true);
							flag = 1;
						}
					} else {// ��·û���趨��ֵ������£��ɶ˿ڷ�ֵ���

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
                            //�и澯����
							lr.setAlarm(true);
							flag = 1;
						}
						if (downperc > Double.parseDouble(lr.getMaxPer())) {
                            //�и澯����
							lr.setAlarm(true);
							flag = 1;
						}
					} else {// ��·û���趨��ֵ������£��ɶ˿ڷ�ֵ���
						
					}
					// end
				}
				if (flag == 0) {
					lr.setAlarm(false);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}//end of for(linkList)
	}

	public boolean timeRestricted() {
		return true;
	}

	private boolean linkExist(LinkRoad link) {
		// ��Ҫ�ж��ǹ���IP����IP����
		Host host1 = (Host) PollingEngine.getInstance().getNodeByID(
				link.getStartId());
		Host host2 = (Host) PollingEngine.getInstance().getNodeByID(
				link.getEndId());
		IfEntity if1 = host1.getIfEntityByIndex(link.getStartIndex());
		IfEntity if2 = host2.getIfEntityByIndex(link.getEndIndex());
		// SysLogger.info(host1.getIpAddress()+"/"+if1.getOperStatus()+"<--->"+host2.getIpAddress()+"/"+if2.getOperStatus());
		if (if1.getOperStatus() == 2 || if2.getOperStatus() == 2)
			return false;
		if (host1.getCategory() == 4 || host2.getCategory() == 4) // ��һ���Ƿ���������ֻ��齻�����˿��Ƿ�down
		{
			if (host1.getCategory() == 4 && if2.getOperStatus() == 2)
				return false;
			else if (host2.getCategory() == 4 && if1.getOperStatus() == 2)
				return false;
			else
				return true;
		}
		/*
		 * if(routerExist(host1,link.getStartIp(),link.getStartIndex(),host2,link.getEndIp(),link.getEndIndex()))
		 * return true; else
		 * if(routerExist(host2,link.getEndIp(),link.getEndIndex(),host1,link.getStartIp(),link.getStartIndex()))
		 * return true; else
		 * if(arpExist(host1,link.getStartIp(),link.getStartIndex(),host2,link.getEndIp(),link.getEndIndex()))
		 * return true; else
		 * if(arpExist(host2,link.getEndIp(),link.getEndIndex(),host1,link.getStartIp(),link.getStartIndex()))
		 * return true; else if(fdbExist(if1.getPhysAddress(),host2)) return
		 * true; else if(fdbExist(if2.getPhysAddress(),host1)) return true;
		 */
		return true;
	}

	/**
	 * ·�ɴ���
	 */
	private boolean routerExist(Host host1, String ifIp1, String ifIndex1,
			Host host2, String ifIp2, String ifIndex2) {
		// ���ﲻ����host1.getIpAddress(),��Ϊ����ڿ����Ѿ�down��
		List routerList = SnmpUtil.getInstance().getRouterList(ifIp1,
				host1.getCommunity());
		if (routerList == null || routerList.size() == 0)
			return false;

		boolean result = false;

		/**
		 * ��ʵ����Ӧ�ò�챾�˵�ip��ifIndex�Ƿ�һ�¡� ����Ĭ������һ�¡�
		 */
		for (int i = 0; i < routerList.size(); i++) {
			IpRouter ipr = (IpRouter) routerList.get(i);
			if (!ipr.getIfIndex().equals(ifIndex1))
				continue;

			String nextRouter = null;
			if (ipr.getType() == 4) // indirect
				nextRouter = ipr.getNextHop();
			else {
				if (ipr.getProto() == 2)
					nextRouter = ipr.getDest();
				else
					nextRouter = ipr.getNextHop();
			}

			if (!nextRouter.equals(ifIp2))
				continue;

			com.afunms.polling.node.IfEntity ifEntity = host2
					.getIfEntityByIndex(ifIndex2);
			if (ifEntity != null && ifEntity.getIpAddress().equals(ifIp2)) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * ARP����
	 */
	private boolean arpExist(Host host1, String ifIp1, String ifIndex1,
			Host host2, String ifIp2, String ifIndex2) {
		List addressList = SnmpUtil.getInstance().getIpNetToMediaTable(ifIp1,
				host1.getCommunity());
		if (addressList == null || addressList.size() == 0)
			return false;

		boolean result = false;
		for (int i = 0; i < addressList.size(); i++) {
			IpAddress ipAddr = (IpAddress) addressList.get(i);

			if (!ipAddr.getIfIndex().equals(ifIndex1))
				continue;
			if (!ipAddr.getIpAddress().equals(ifIp2))
				continue;

			com.afunms.polling.node.IfEntity ifEntity = host2
					.getIfEntityByIndex(ifIndex2);
			if (ifEntity != null) {
				if (ifEntity.getIpAddress().equals(ifIp2)
						|| ifEntity.getIpAddress().equals("")) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * ARP����
	 */
	private boolean fdbExist(String mac1, Host host2) {
		if (SnmpUtil.getInstance().macInFdbTable(mac1, host2.getIpAddress(),
				host2.getCommunity()))
			return true;
		else
			return false;
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
				// ����һ��
				if (thevalue >= nm.getLimenvalue1()) {
					// ���ڶ���
					if (thevalue >= nm.getLimenvalue2()) {
						// ��������
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
			// �ж��Ƿ��Ѿ����и��¼�����
			Hashtable subentityhash = (Hashtable) host.getAlarmPksHash().get(
					subentity + ":" + index);
			if (subentityhash != null && subentityhash.size() > 0) {
				// ���и��¼�����,�ж��Ƿ���ͬ����澯
				if (subentityhash.get(level) != null) {
					// ͬ�������,�жϳ�������
					int times = (Integer) subentityhash.get(level);
					int limentimes = 0;
					if (level == 1) {
						// ��һ����
						limentimes = nm.getTime0();
					} else if (level == 2) {
						// �ڶ�����
						limentimes = nm.getTime1();
					} else {
						// ��������
						limentimes = nm.getTime2();
					}
					if (times >= limentimes) {
						// �����¼�
						// flag = true;
						returnflag = level;
					} else {
						// �������¼�,����Ҫ�Ѹ澯����������1
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
					// ͬ���𲻴���,���һ�β����ü���ĸ澯,����,ͬʱ������¼����͵���������澯
					subentityhash = new Hashtable();
					subentityhash.put(level, 1);
					host.getAlarmPksHash().put(subentity + ":" + index,
							subentityhash);
					SysLogger.info(nm.getIp() + " put====subentity:"
							+ subentity + ":" + index + "===level:" + level
							+ "===times:" + 1);
				}
			} else {
				// ��һ��
				subentityhash = new Hashtable();
				subentityhash.put(level, 1);
				host.getAlarmPksHash().put(subentity + ":" + index,
						subentityhash);
				SysLogger.info(nm.getIp() + " put====subentity:" + subentity
						+ ":" + index + "===level:" + level + "===times:" + 1);
			}
		} else {
			// ��һ��
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