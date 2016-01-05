package com.afunms.polling.snmp.process;

/*
 * @author hukelei@dhcc.com.cn
 *
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtils;
import com.afunms.common.util.SysLogger;
import com.afunms.config.dao.ProcsDao;
import com.afunms.config.model.Procs;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.base.Node;
import com.afunms.polling.loader.HostLoader;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Processcollectdata;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.HostDataimportProcessRtTosql;
import com.gatherResulttosql.HostDatatempProcessRtTosql;

public class WindowsProcessSnmp extends SnmpMonitor {

	private Hashtable sendeddata = ShareData.getProcsendeddata();

	private static java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public WindowsProcessSnmp() {
	}

	public void collectData(Node node, MonitoredItem item) {
	}

	public void collectData(HostNode node) {
	}

	public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
		Hashtable returnHash = new Hashtable();
		Hashtable<String, Processcollectdata> processCountHash = new Hashtable<String, Processcollectdata>();
		Vector processVector = new Vector();
		HostLoader hostLoader = new HostLoader();
		Host host = hostLoader.loadOneByID(nodeGatherIndicators.getNodeid());
		if (host == null) {
			return null;
		}
		if (!host.isManaged()) {
			return null;
		}
		try {
			// Processcollectdata processdata = new Processcollectdata();
			Calendar date = Calendar.getInstance();
			// Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(
			// host.getIpAddress());
			// if (ipAllData == null)
			// ipAllData = new Hashtable();
			//
			// try {
			// SimpleDateFormat sdf = new SimpleDateFormat(
			// "yyyy-MM-dd HH:mm:ss");
			// com.afunms.polling.base.Node snmpnode =
			// (com.afunms.polling.base.Node) PollingEngine
			// .getInstance().getNodeByIP(host.getIpAddress());
			// Date cc = date.getTime();
			// String time = sdf.format(cc);
			// snmpnode.setLastTime(time);
			// } catch (Exception e) {
			//
			// }
			// -------------------------------------------------------------------------------------------process
			// start
			try {

				String[] oids = new String[] { "1.3.6.1.2.1.25.4.2.1.1",
						"1.3.6.1.2.1.25.4.2.1.2", "1.3.6.1.2.1.25.4.2.1.5",
						"1.3.6.1.2.1.25.4.2.1.6", "1.3.6.1.2.1.25.4.2.1.7",
						"1.3.6.1.2.1.25.5.1.1.2", "1.3.6.1.2.1.25.5.1.1.1",

				};
				String[] oids1 = new String[] { "1.3.6.1.2.1.25.2.2" };
				String[][] valueArray1 = null;
				try {
					valueArray1 = SnmpUtils.getTableData(host.getIpAddress(),
							host.getCommunity(), oids1, host.getSnmpversion(),
							3, 1000 * 30);
				} catch (Exception e) {
					valueArray1 = null;
					SysLogger.error(host.getIpAddress() + "_WindowsSnmp");
				}
				int allMemorySize = 0;
				if (valueArray1 != null) {
					for (int i = 0; i < valueArray1.length; i++) {
						String svb0 = valueArray1[i][0];
						if (svb0 == null)
							continue;
						allMemorySize = Integer.parseInt(svb0);
					}
				}

				String[][] valueArray = null;
				try {
					valueArray = snmp.getTableData(host.getIpAddress(), host
							.getCommunity(), oids);
				} catch (Exception e) {
					valueArray = null;
					SysLogger.error(host.getIpAddress() + "_WindowsSnmp");
				}
				Vector vecIndex = new Vector();

				List procslist = new ArrayList();
				ProcsDao procsdao = new ProcsDao();
				try {
					procslist = procsdao.loadByIp(host.getIpAddress());
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					procsdao.close();
				}

				List procs_list = new ArrayList();
				Hashtable procshash = new Hashtable();
				Vector procsV = new Vector();
				if (procslist != null && procslist.size() > 0) {
					for (int i = 0; i < procslist.size(); i++) {
						Procs procs = (Procs) procslist.get(i);
						procshash.put(procs.getProcname(), procs);
						procsV.add(procs.getProcname());
					}
				}
				long alltime = 0L;
				if (valueArray != null) {
					for (int i = 0; i < valueArray.length; i++) {
						String processCpu = valueArray[i][6].trim();
						alltime = alltime + Long.parseLong(processCpu);
					}
				}

				if (valueArray != null) {
					for (int i = 0; i < valueArray.length; i++) {
						if (allMemorySize != 0) {
							String vbstring0 = valueArray[i][0];
							String vbstring1 = valueArray[i][1];
							String vbstring2 = valueArray[i][2];
							String vbstring3 = valueArray[i][3];
							String vbstring4 = valueArray[i][4];
							String vbstring5 = valueArray[i][5];
							String vbstring6 = valueArray[i][6];
							String processIndex = vbstring0.trim();

							float value = 0.0f;
							value = Integer.parseInt(vbstring5.trim()) * 100.0f
									/ allMemorySize;

							String processName = vbstring1.trim();

							Processcollectdata processdata = new Processcollectdata();
							processdata.setIpaddress(host.getIpAddress());
							processdata.setCollecttime(date);
							processdata.setCategory("Process");
							processdata.setEntity("MemoryUtilization");
							processdata.setSubentity(processIndex);
							processdata.setRestype("dynamic");
							processdata.setUnit("%");
							processdata.setThevalue(Float.toString(value));
							processdata.setChname(processName);
							processVector.addElement(processdata);

							String processMemory = vbstring5.trim();
							processdata = new Processcollectdata();
							processdata.setIpaddress(host.getIpAddress());
							processdata.setCollecttime(date);
							processdata.setCategory("Process");
							processdata.setEntity("Memory");
							processdata.setSubentity(processIndex);
							processdata.setRestype("static");
							processdata.setUnit("K");
							processdata.setThevalue(processMemory);
							processdata.setChname(processName);
							processVector.addElement(processdata);

							String processType = vbstring3.trim();
							processdata = new Processcollectdata();
							processdata.setIpaddress(host.getIpAddress());
							processdata.setCollecttime(date);
							processdata.setCategory("Process");
							processdata.setEntity("Type");
							processdata.setSubentity(processIndex);
							processdata.setRestype("static");
							processdata.setUnit(" ");
							processdata.setThevalue(HOST_hrSWRun_hrSWRunType
									.get(processType).toString());
							processdata.setChname(processName);
							processVector.addElement(processdata);

							String processPath = vbstring2.trim();
							processdata = new Processcollectdata();
							processdata.setIpaddress(host.getIpAddress());
							processdata.setCollecttime(date);
							processdata.setCategory("Process");
							processdata.setEntity("Path");
							processdata.setSubentity(processIndex);
							processdata.setRestype("static");
							processdata.setUnit(" ");
							processdata.setThevalue(processPath);
							processdata.setChname(processName);
							processVector.addElement(processdata);

							String processStatus = vbstring4.trim();
							processdata = new Processcollectdata();
							processdata.setIpaddress(host.getIpAddress());
							processdata.setCollecttime(date);
							processdata.setCategory("Process");
							processdata.setEntity("Status");
							processdata.setSubentity(processIndex);
							processdata.setRestype("static");
							processdata.setUnit(" ");
							processdata.setThevalue(HOST_hrSWRun_hrSWRunStatus
									.get(processStatus).toString());
							processdata.setChname(processName);
							processVector.addElement(processdata);

							processdata = new Processcollectdata();
							processdata.setIpaddress(host.getIpAddress());
							processdata.setCollecttime(date);
							processdata.setCategory("Process");
							processdata.setEntity("Name");
							processdata.setSubentity(processIndex);
							processdata.setRestype("static");
							processdata.setUnit(" ");
							processdata.setThevalue(processName);
							processdata.setChname(processName);
							processVector.addElement(processdata);

							String processCpu = vbstring6.trim();
							processdata = new Processcollectdata();
							processdata.setIpaddress(host.getIpAddress());
							processdata.setCollecttime(date);
							processdata.setCategory("Process");
							processdata.setEntity("CpuTime");
							processdata.setSubentity(processIndex);
							processdata.setRestype("dynamic");
							processdata.setUnit("秒");
							processdata.setThevalue(((Long
									.parseLong(processCpu) / alltime) * 100)
									+ "");
							processdata.setChname(processName);
							processVector.addElement(processdata);

							processdata = processCountHash.get(processName);
							if (processdata != null) {
								Integer count = Integer.parseInt(processdata
										.getThevalue());
								count++;
								processdata.setThevalue(String.valueOf(count));
								processCountHash.put(processName, processdata);
							} else {
								processdata = new Processcollectdata();
								processdata.setIpaddress(host.getIpAddress());
								processdata.setCollecttime(date);
								processdata.setCategory("Process");
								processdata.setEntity("Num");
								processdata.setSubentity(processIndex);
								processdata.setRestype("dynamic");
								processdata.setUnit("个");
								processdata.setThevalue("1");
								processdata.setChname(processName);
								processCountHash.put(processName, processdata);
							}
						} else {
							throw new Exception("Process is 0");
						}
					}
				}

			} catch (Exception e) {

			}
		} catch (Exception e) {
		}

		Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(
				host.getIpAddress());
		if (ipAllData == null)
			ipAllData = new Hashtable();
		ipAllData.put("process", processVector);
		ShareData.getSharedata().put(host.getIpAddress(), ipAllData);
		returnHash.put("process", processVector);
		returnHash.put("importProcess", processCountHash);

		try {
			if (processVector != null && processVector.size() > 0) {
				AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
				List list = alarmIndicatorsUtil
						.getAlarmInicatorsThresholdForNode(host.getId() + "",
								"host", "windows");
				AlarmIndicatorsNode alarmIndicatorsNode2 = null;
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						AlarmIndicatorsNode alarmIndicatorsNode2_per = (AlarmIndicatorsNode) list
								.get(i);
						if (alarmIndicatorsNode2_per != null
								&& "process".equals(alarmIndicatorsNode2_per
										.getName())) {
							alarmIndicatorsNode2 = alarmIndicatorsNode2_per;
							break;
						}

					}
					NodeUtil nodeUtil = new NodeUtil();
					NodeDTO node = nodeUtil.conversionToNodeDTO(host);
					CheckEventUtil checkutil = new CheckEventUtil();
					checkutil.createProcessGroupEventList(node, processVector,
							alarmIndicatorsNode2);
				}
			}
		} catch (Exception e) {

		}

		// 把结果生成sql
		HostDatatempProcessRtTosql temptosql = new HostDatatempProcessRtTosql();
		temptosql.CreateResultTosql(returnHash, host);
		HostDataimportProcessRtTosql importtosql = new HostDataimportProcessRtTosql();
		importtosql.CreateResultTosql(returnHash, host);

		return returnHash;
	}
}
