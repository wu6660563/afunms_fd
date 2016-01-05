/*
 * @(#)GatherReportManager.java     v1.01, Oct 30, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.capreport.manage;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.send.SendSMSAlarmUtil;
import com.afunms.alarm.service.NodeAlarmService;
import com.afunms.alarm.sms.service.SendSMSAlarmByFDDBService;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.SystemConstant;
import com.afunms.config.dao.DiskconfigDao;
import com.afunms.config.model.Diskconfig;
import com.afunms.detail.service.cpuInfo.CpuInfoService;
import com.afunms.detail.service.diskInfo.DiskInfoService;
import com.afunms.detail.service.memoryInfo.MemoryInfoService;
import com.afunms.detail.service.pingInfo.PingInfoService;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.om.Diskcollectdata;
import com.afunms.system.model.User;
import com.afunms.temp.model.NodeTemp;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.model.MonitorNodeDTO;

/**
 * ClassName: GatherReportManager.java
 * <p>
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Oct 30, 2013 10:44:20 AM
 * @mail wupinlong@dhcc.com.cn
 */
public class GatherReportManager extends BaseManager implements
		ManagerInterface {

	private static NumberFormat numberFormat = new DecimalFormat();
	static {
		numberFormat.setMaximumFractionDigits(0);
	}

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public String execute(String action) {
		if ("netsortList".equals(action)) {
			return netsortList();
		} else if ("hostsortList".equals(action)) {
			return hostsortList();
		} else if("test".equals(action)){
			return test();
		}
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
	}
	
	public String test(){
//		SVGBuild svgBuild = new SVGBuild("1700","1200");
//		svgBuild.test();
//		System.out.println("生成完成！！！！");
		
		SendSMSAlarmUtil util = new SendSMSAlarmUtil();
		util.sendSMSAlarm("admin", "18802007610", "测试发送告警！");
		return null;
	}

	public String hostsortList() {
		// 网络设备按指标排序
		String jsp = "/capreport/gatherReport/hostgathersort.jsp";
		setTarget(jsp);

		String cpuTemp = request.getParameter("cpu");
		Double cpuused = 0d;
		if (cpuTemp != null && !"".equals(cpuTemp)) {
			cpuused = Double.valueOf(cpuTemp);
		}
		String memoryTemp = request.getParameter("memory");
		Double memoryused = 0d;
		if (memoryTemp != null && !"".equals(memoryTemp)) {
			memoryused = Double.valueOf(memoryTemp);
		}
		String diskTemp = request.getParameter("disk");
		Double diskused = 0d;
		if (diskTemp != null && !"".equals(diskTemp)) {
			diskused = Double.valueOf(diskTemp);
		}
		String cpuorder = request.getParameter("cpuorder");
		String memoryorder = request.getParameter("memoryorder");
		String diskorder = request.getParameter("diskorder");

		String category = "host";
		List monitornodelist = getMonitorListByCategory(category);

		List list = new ArrayList();

		String date = sdf.format(new Date());

		String starttime = date + " 00:00:00";
		String totime = date + " 23:59:59";
		NodeUtil nodeUtil = new NodeUtil();

		Hashtable<String, String> sindexHash = new Hashtable<String, String>();
		DiskconfigDao diskDao = null;
		if (monitornodelist != null) {
			// monitornetlist 网络设备监控列表
			// 取出可用性
			PingInfoService pingInfoService = new PingInfoService();
			List<NodeTemp> pingInfoList = pingInfoService
					.getPingInfo(monitornodelist);

			// 取出网络设备的CPU利用率信息
			CpuInfoService cpuInfoService = new CpuInfoService();
			List<NodeTemp> cpuInfoList = cpuInfoService
					.getCpuPerListInfo(monitornodelist);

			// 取出网络设备的内存利用率信息
			MemoryInfoService memoryInfoService = new MemoryInfoService();
			List<NodeTemp> memoryList = memoryInfoService
					.getMemoryInfo(monitornodelist);

			DiskInfoService diskInfoService = new DiskInfoService();

			for (int i = 0; i < monitornodelist.size(); i++) {
				HostNode hostNode = (HostNode) monitornodelist.get(i);
				MonitorNodeDTO monitorNodeDTO = getMonitorNodeDTOByHostNode(hostNode);

				// 设置可用性
				if (pingInfoList != null) {
					for (int j = 0; j < pingInfoList.size(); j++) {
						NodeTemp nodeTemp = pingInfoList.get(j);
						if ((monitorNodeDTO.getId() + "").equals(nodeTemp
								.getNodeid())) {
							if (nodeTemp.getThevalue() != null
									&& Double.parseDouble(nodeTemp
											.getThevalue()) != 0) {
								monitorNodeDTO.setPingValue("100");
							} else {
								monitorNodeDTO.setPingValue("0");
							}
						}
					}
				}

				// 设置网络设备的CPU利用率信息
				if (cpuInfoList != null) {
					for (int j = 0; j < cpuInfoList.size(); j++) {
						NodeTemp nodeTemp = cpuInfoList.get(j);
						if ((monitorNodeDTO.getId() + "").equals(nodeTemp
								.getNodeid())) {
							monitorNodeDTO.setCpuValue(numberFormat
									.format(Double.parseDouble(nodeTemp
											.getThevalue())));
						}
					}
				}

				// 设置网络设备的内存利用率信息
				if (memoryList != null) {
					for (int j = 0; j < memoryList.size(); j++) {
						NodeTemp nodeTemp = memoryList.get(j);
						if ((monitorNodeDTO.getId() + "").equals(nodeTemp
								.getNodeid())) {
							monitorNodeDTO.setMemoryValue(numberFormat
									.format(Double.parseDouble(nodeTemp
											.getThevalue())));
						}
					}
				}

				// 设定cpu和内存的告警颜色
				double cpuValueDouble = 0;
				double memoryValueDouble = 0;
				String cpuValueColor = "green"; // cpu 颜色
				String memoryValueColor = "green"; // memory 颜色
				String diskValueColor = "green"; // 磁盘 颜色
				String cpuValueStr = monitorNodeDTO.getCpuValue();
				String memoryValueStr = monitorNodeDTO.getMemoryValue();
				if (cpuValueStr != null) {
					cpuValueDouble = Double.parseDouble(cpuValueStr);
				}
				if (memoryValueStr != null) {
					memoryValueDouble = Double.parseDouble(memoryValueStr);
					memoryValueDouble = Double.parseDouble(numberFormat
							.format(memoryValueDouble));
				}
				// 设定cpu和内存的告警颜色
				NodeDTO node = nodeUtil.conversionToNodeDTO(hostNode);
				AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
				List alarmIndicatorsList = alarmIndicatorsUtil
						.getAlarmInicatorsThresholdForNode(String
								.valueOf(monitorNodeDTO.getId()), node
								.getType(), node.getSubtype());
				if (alarmIndicatorsList != null) {
					for (int j = 0; j < alarmIndicatorsList.size(); j++) {
						AlarmIndicatorsNode alarmIndicatorsNode = (AlarmIndicatorsNode) alarmIndicatorsList
								.get(j);
						if ("cpu".equals(alarmIndicatorsNode.getName())) {
							if (cpuValueDouble > Double
									.valueOf(alarmIndicatorsNode
											.getLimenvalue2())) {
								cpuValueColor = "red";
							} else if (cpuValueDouble > Double
									.valueOf(alarmIndicatorsNode
											.getLimenvalue1())) {
								cpuValueColor = "orange";
							} else if (cpuValueDouble > Double
									.valueOf(alarmIndicatorsNode
											.getLimenvalue0())) {
								cpuValueColor = "yellow";
							} else {
								cpuValueColor = "green";
							}
						}
						if ("physicalmemory".equals(alarmIndicatorsNode
								.getName())
								|| "memory".equals(alarmIndicatorsNode
										.getName())) {
							if (memoryValueDouble >= Double
									.valueOf(alarmIndicatorsNode
											.getLimenvalue2())) {
								memoryValueColor = "red";
							} else if (memoryValueDouble >= Double
									.valueOf(alarmIndicatorsNode
											.getLimenvalue1())) {
								memoryValueColor = "orange";
							} else if (memoryValueDouble >= Double
									.valueOf(alarmIndicatorsNode
											.getLimenvalue0())) {
								memoryValueColor = "yellow";
							} else {
								memoryValueColor = "green";
							}
						}
					}
				}
				monitorNodeDTO.setCpuValueColor(cpuValueColor);
				monitorNodeDTO.setMemoryValueColor(memoryValueColor);

				// 取到服务器磁盘信息
				List diskList = diskInfoService.getDiskNodeTempList(hostNode);
				List<Diskcollectdata> diskcollectList = new ArrayList<Diskcollectdata>();
				if (diskList != null && diskList.size() > 0) {
					// 说明有输入磁盘利用率过滤条件
					for (int j = 0; j < diskList.size(); j++) {
						Diskcollectdata diskcollectdata = (Diskcollectdata) diskList
								.get(j);
						if (Double.valueOf(diskcollectdata.getThevalue()) >= diskused) {
							Diskconfig diskconfig = null;
							try {
								diskDao = new DiskconfigDao();
								diskconfig = diskDao.getByipandName(hostNode
										.getIpAddress(), diskcollectdata
										.getSubentity());
							} catch (Exception e) {
								e.printStackTrace();
							} finally{
								diskDao.close();
							}
							if(diskconfig != null){
								if (Double.valueOf(diskcollectdata.getThevalue()) > diskconfig
										.getLimenvalue2()) {
									diskValueColor = "red";
								} else if (Double.valueOf(diskcollectdata
										.getThevalue()) > diskconfig
										.getLimenvalue1()) {
									diskValueColor = "orange";
								} else if (Double.valueOf(diskcollectdata
										.getThevalue()) > diskconfig
										.getLimenvalue()) {
									diskValueColor = "yellow";
								} else {
									diskValueColor = "green";
								}
							}
							diskcollectdata.setThevalue(numberFormat
									.format(Double.parseDouble(diskcollectdata
											.getThevalue())));
							sindexHash.put(diskcollectdata.getId()+":"+diskcollectdata.getSubentity(),
									diskValueColor);
							diskcollectList.add(diskcollectdata);
						}
					}
					monitorNodeDTO.setHardDisk(diskcollectList);
				}

				// 初始化是0，如果前台未填写，表示0
				if (Double.valueOf(monitorNodeDTO.getCpuValue()) >= cpuused
						&& Double.valueOf(monitorNodeDTO.getMemoryValue()) >= memoryused && monitorNodeDTO.getHardDisk() != null && monitorNodeDTO.getHardDisk().size() > 0) {
					list.add(monitorNodeDTO);
				}
			}

			// 排序
			Integer cpuORD = 0;
			if (cpuorder != null && !"".equals(cpuorder)) {
				cpuORD = Integer.valueOf(cpuorder);
			}
			Integer memoryORD = 0;
			if (memoryorder != null && !"".equals(memoryorder)) {
				memoryORD = Integer.valueOf(memoryorder);
			}
			Integer diskORD = 0;
			if (diskorder != null && !"".equals(diskorder)) {
				diskORD = Integer.valueOf(diskorder);
			}
			final String[] orders = new String[3];
			if (cpuORD != 0 && memoryORD != 0 && diskORD != 0) {
				orders[cpuORD - 1] = "cpuorder";
				orders[memoryORD - 1] = "memoryorder";
				orders[diskORD - 1] = "diskorder";
			} else {
				orders[0] = "cpuorder";
				orders[1] = "memoryorder";
				orders[2] = "diskorder";
			}
			sort(list, orders, "host");
		}
		request.setAttribute("list", list);
		request.setAttribute("sindexHash", sindexHash);
		return jsp;
	}

	private List sort(List list, final String[] orders, final String type) {
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				int result = 0;
				MonitorNodeDTO p1 = (MonitorNodeDTO) o1;
				MonitorNodeDTO p2 = (MonitorNodeDTO) o2;
				Double cpuDouble1 = Double.valueOf(p1.getCpuValue());
				Double cpuDouble2 = Double.valueOf(p2.getCpuValue());

				Hashtable<String, Double[]> hashtable = new Hashtable<String, Double[]>();
				hashtable.put("cpuorder",
						new Double[] { cpuDouble1, cpuDouble2 });

				Double memoryDouble1 = Double.valueOf(p1.getMemoryValue());
				Double memoryDouble2 = Double.valueOf(p2.getMemoryValue());
				hashtable.put("memoryorder", new Double[] { memoryDouble1,
						memoryDouble2 });

				// disk，需要找到两个List中里面最大的进行比较
				if ("host".equals(type)) {
					Double numTemp1 = 0d;
					if (p1.getHardDisk() != null && p1.getHardDisk().size() > 0) {
						for (int i = 0; i < p1.getHardDisk().size(); i++) {
							Diskcollectdata diskcollectdata = (Diskcollectdata) p1
									.getHardDisk().get(i);
							if (Double.parseDouble(diskcollectdata
									.getThevalue()) > numTemp1) {
								numTemp1 = Double.parseDouble(diskcollectdata
										.getThevalue());
							}
						}
					}
					Double numTemp2 = 0d;
					if (p2.getHardDisk() != null && p2.getHardDisk().size() > 0) {

						for (int i = 0; i < p2.getHardDisk().size(); i++) {
							Diskcollectdata diskcollectdata = (Diskcollectdata) p2
									.getHardDisk().get(i);
							if (Double.parseDouble(diskcollectdata
									.getThevalue()) > numTemp2) {
								numTemp2 = Double.parseDouble(diskcollectdata
										.getThevalue());
							}
						}
					}
					hashtable.put("diskorder", new Double[] { numTemp1,
							numTemp2 });
				}

				for (String order : orders) {
					Double[] doubles = hashtable.get(order);
					if (doubles[0] < doubles[1]) {
						return 1;
					} else if (doubles[0] == doubles[1]) {
						return 0;
					} else {
						return -1;
					}
				}
				return result;
			}
		});
		return list;
	}

	public String netsortList() {
		// 网络设备按指标排序
		String jsp = "/capreport/gatherReport/netgathersort.jsp";
		setTarget(jsp);

		String cpuTemp = request.getParameter("cpu");
		Double cpuused = 0d;
		if (cpuTemp != null && !"".equals(cpuTemp)) {
			cpuused = Double.valueOf(cpuTemp);
		}
		String memoryTemp = request.getParameter("memory");
		Double memoryused = 0d;
		if (memoryTemp != null && !"".equals(memoryTemp)) {
			memoryused = Double.valueOf(memoryTemp);
		}
		String cpuorder = request.getParameter("cpuorder");

		String category = "net";
		List monitornodelist = getMonitorListByCategory(category);

		List list = new ArrayList();

		String date = sdf.format(new Date());

		String starttime = date + " 00:00:00";
		String totime = date + " 23:59:59";
		NodeUtil nodeUtil = new NodeUtil();
		if (monitornodelist != null) {
			// monitornetlist 网络设备监控列表
			// 取出可用性
			PingInfoService pingInfoService = new PingInfoService();
			List<NodeTemp> pingInfoList = pingInfoService
					.getPingInfo(monitornodelist);

			// 取出网络设备的CPU利用率信息
			CpuInfoService cpuInfoService = new CpuInfoService();
			List<NodeTemp> cpuInfoList = cpuInfoService
					.getCpuPerListInfo(monitornodelist);

			// 取出网络设备的内存利用率信息
			MemoryInfoService memoryInfoService = new MemoryInfoService();
			List<NodeTemp> memoryList = memoryInfoService
					.getMemoryInfo(monitornodelist);

			for (int i = 0; i < monitornodelist.size(); i++) {
				HostNode hostNode = (HostNode) monitornodelist.get(i);
				MonitorNodeDTO monitorNodeDTO = getMonitorNodeDTOByHostNode(hostNode);

				// 设置可用性
				if (pingInfoList != null) {
					for (int j = 0; j < pingInfoList.size(); j++) {
						NodeTemp nodeTemp = pingInfoList.get(j);
						if ((monitorNodeDTO.getId() + "").equals(nodeTemp
								.getNodeid())) {
							if (nodeTemp.getThevalue() != null
									&& Double.parseDouble(nodeTemp
											.getThevalue()) != 0) {
								monitorNodeDTO.setPingValue("100");
							} else {
								monitorNodeDTO.setPingValue("0");
							}
						}
					}
				}

				// 设置网络设备的CPU利用率信息
				if (cpuInfoList != null) {
					for (int j = 0; j < cpuInfoList.size(); j++) {
						NodeTemp nodeTemp = cpuInfoList.get(j);
						if ((monitorNodeDTO.getId() + "").equals(nodeTemp
								.getNodeid())) {
							monitorNodeDTO.setCpuValue(numberFormat
									.format(Double.parseDouble(nodeTemp
											.getThevalue())));
						}
					}
				}

				// 设置网络设备的内存利用率信息
				if (memoryList != null) {
					for (int j = 0; j < memoryList.size(); j++) {
						NodeTemp nodeTemp = memoryList.get(j);
						if ((monitorNodeDTO.getId() + "").equals(nodeTemp
								.getNodeid())) {
							monitorNodeDTO.setMemoryValue(numberFormat
									.format(Double.parseDouble(nodeTemp
											.getThevalue())));
						}
					}
				}

				// 设定cpu和内存的告警颜色
				double cpuValueDouble = 0;
				double memoryValueDouble = 0;
				String cpuValueColor = "green"; // cpu 颜色
				String memoryValueColor = "green"; // memory 颜色
				String cpuValueStr = monitorNodeDTO.getCpuValue();
				String memoryValueStr = monitorNodeDTO.getMemoryValue();
				if (cpuValueStr != null) {
					cpuValueDouble = Double.parseDouble(cpuValueStr);
				}
				if (memoryValueStr != null) {
					memoryValueDouble = Double.parseDouble(memoryValueStr);
					memoryValueDouble = Double.parseDouble(numberFormat
							.format(memoryValueDouble));
				}
				// 设定cpu和内存的告警颜色
				NodeDTO node = nodeUtil.conversionToNodeDTO(hostNode);
				AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
				List alarmIndicatorsList = alarmIndicatorsUtil
						.getAlarmInicatorsThresholdForNode(String
								.valueOf(monitorNodeDTO.getId()), node
								.getType(), node.getSubtype());
				if (alarmIndicatorsList != null) {
					for (int j = 0; j < alarmIndicatorsList.size(); j++) {
						AlarmIndicatorsNode alarmIndicatorsNode = (AlarmIndicatorsNode) alarmIndicatorsList
								.get(j);
						if ("cpu".equals(alarmIndicatorsNode.getName())) {
							if (cpuValueDouble > Double
									.valueOf(alarmIndicatorsNode
											.getLimenvalue2())) {
								cpuValueColor = "red";
							} else if (cpuValueDouble > Double
									.valueOf(alarmIndicatorsNode
											.getLimenvalue1())) {
								cpuValueColor = "orange";
							} else if (cpuValueDouble > Double
									.valueOf(alarmIndicatorsNode
											.getLimenvalue0())) {
								cpuValueColor = "yellow";
							} else {
								cpuValueColor = "green";
							}
						}
						if ("physicalmemory".equals(alarmIndicatorsNode
								.getName())
								|| "memory".equals(alarmIndicatorsNode
										.getName())) {
							if (memoryValueDouble >= Double
									.valueOf(alarmIndicatorsNode
											.getLimenvalue2())) {
								memoryValueColor = "red";
							} else if (memoryValueDouble >= Double
									.valueOf(alarmIndicatorsNode
											.getLimenvalue1())) {
								memoryValueColor = "orange";
							} else if (memoryValueDouble >= Double
									.valueOf(alarmIndicatorsNode
											.getLimenvalue0())) {
								memoryValueColor = "yellow";
							} else {
								memoryValueColor = "green";
							}
						}
					}
				}
				monitorNodeDTO.setCpuValueColor(cpuValueColor);
				monitorNodeDTO.setMemoryValueColor(memoryValueColor);
				// 初始化是0，如果前台未填写，表示0
				if (Double.valueOf(monitorNodeDTO.getCpuValue()) >= cpuused
						&& Double.valueOf(monitorNodeDTO.getMemoryValue()) >= memoryused) {
					list.add(monitorNodeDTO);
				}
			}
		}
		request.setAttribute("list", list);
		return jsp;
	}

	/**
	 * 通过 hostNode 来组装 MonitorNodeDTO
	 * 
	 * @param hostNode
	 * @return
	 */
	public MonitorNodeDTO getMonitorNodeDTOByHostNode(HostNode hostNode) {

		MonitorNodeDTO monitorNodeDTO = new MonitorNodeDTO();
		NodeUtil nodeUtil = new NodeUtil();
		NodeDTO node = nodeUtil.conversionToNodeDTO(hostNode);

		int nodeId = hostNode.getId(); // 设备ID
		int alarmMaxLevel = 0; // 告警等级
		String alias = hostNode.getAlias(); // 别名
		String categoryString = ""; // 类型
		String ipAddress = hostNode.getIpAddress(); // IP
		String pingValue = "0"; // ping 默认为 0
		String cpuValue = "0"; // cpu 默认为 0
		String memoryValue = "0"; // memory 默认为 0
		String inutilhdxValue = "0"; // inutilhdx 默认为 0
		String oututilhdxValue = "0"; // oututilhdx 默认为 0
		int interfaceNum = 0; // 接口数量
		String collectType = ""; // 采集类型
		String type = node.getType();
		String subtype = node.getSubtype();

		String cpuValueColor = "green"; // cpu 颜色
		String memoryValueColor = "green"; // memory 颜色

		double cpuValueDouble = 0;
		double memeryValueDouble = 0;

		// 获取告警等级
		NodeAlarmService nodeAlarmService = new NodeAlarmService();
		alarmMaxLevel = nodeAlarmService.getMaxAlarmLevel(node);

		// 获取类型
		int category = hostNode.getCategory(); // 类型
		if (category == 1) {
			categoryString = "路由器";
		} else if (category == 2) {
			categoryString = "路由交换机";
		} else if (category == 3) {
			categoryString = "交换机";
		} else if (category == 4) {
			categoryString = "服务器";
		} else if (category == 7) {
			categoryString = "无线路由器";
		} else if (category == 8) {
			categoryString = "防火墙";
		} else if (category == 9) {
			categoryString = "ATM";
		} else if (category == 10) {
			categoryString = "邮件安全网关";
		} else if (category == 11) {
			categoryString = "F5";
		} else if (category == 12) {
			categoryString = "VPN";
		}

		if (SystemConstant.COLLECTTYPE_SNMP == hostNode.getCollecttype()) {
			collectType = "SNMP";
		} else if (SystemConstant.COLLECTTYPE_PING == hostNode.getCollecttype()) {
			collectType = "PING";
		} else if (SystemConstant.COLLECTTYPE_REMOTEPING == hostNode
				.getCollecttype()) {
			collectType = "REMOTEPING";
		} else if (SystemConstant.COLLECTTYPE_SHELL == hostNode
				.getCollecttype()) {
			collectType = "代理";
		} else if (SystemConstant.COLLECTTYPE_SSH == hostNode.getCollecttype()) {
			collectType = "SSH";
		} else if (SystemConstant.COLLECTTYPE_TELNET == hostNode
				.getCollecttype()) {
			collectType = "TELNET";
		} else if (SystemConstant.COLLECTTYPE_WMI == hostNode.getCollecttype()) {
			collectType = "WMI";
		} else if (SystemConstant.COLLECTTYPE_DATAINTERFACE == hostNode
				.getCollecttype()) {
			collectType = "接口";
		}

		// 设置id
		monitorNodeDTO.setId(nodeId);
		// 设置名称
		monitorNodeDTO.setAlias(alias);
		// 设置ip
		monitorNodeDTO.setIpAddress(ipAddress);
		monitorNodeDTO.setStatus(alarmMaxLevel + "");
		monitorNodeDTO.setCategory(categoryString);
		monitorNodeDTO.setCpuValue(cpuValue);
		monitorNodeDTO.setMemoryValue(memoryValue);
		monitorNodeDTO.setInutilhdxValue(inutilhdxValue);
		monitorNodeDTO.setOututilhdxValue(oututilhdxValue);
		monitorNodeDTO.setPingValue(pingValue);
		monitorNodeDTO.setCollectType(collectType);
		monitorNodeDTO.setCpuValueColor(cpuValueColor);
		monitorNodeDTO.setMemoryValueColor(memoryValueColor);
		monitorNodeDTO.setType(type);
		monitorNodeDTO.setSubtype(subtype);
		return monitorNodeDTO;
	}

	/**
	 * 根据不同的category查找设备 getMonitorListByCategory:
	 * <p>
	 * 
	 * @param category
	 * @return
	 * 
	 * @since v1.01
	 */
	public List getMonitorListByCategory(String category) {
		String where = "";
		if ("node".equals(category)) {
			where = " where managed=1";
		} else if ("net_server".equals(category)) {
			where = " where managed=1 and category=4";
		} else if ("host".equals(category)) {
			where = " where managed=1 and category=4";
		} else if ("net".equals(category)) {
			where = " where managed=1 and (category=1 or category=2 or category=3 or category=7) ";
		} else if ("net_router".equals(category)) {
			where = " where managed=1 and category=1";
		} else if ("net_switch".equals(category)) {
			where = " where managed=1 and (category=2 or category=3 or category=7) ";
		} else if ("safeequip".equals(category)) {
			where = " where managed=1 and (category=8) ";
		} else if ("net_firewall".equals(category)) {
			where = " where managed=1 and (category=8) ";
		} else if ("net_atm".equals(category)) {
			where = " where managed=1 and (category=9) ";
		} else if ("net_gateway".equals(category)) {
			where = " where managed=1 and (category=10) ";
		} else if ("net_f5".equals(category)) {
			where = " where managed=1 and (category=11) ";
		} else if ("net_vpn".equals(category)) {
			where = " where managed=1 and (category=12) ";
		} else {
			where = " where managed=1";
		}
		where = where + getBidSql();
		String key = getParaValue("key");

		String value = getParaValue("value");
		if (key != null && key.trim().length() > 0 && value != null
				&& value.trim().length() > 0) {
			where = where + " and " + key + "='" + value + "'";
		}
		HostNodeDao dao = new HostNodeDao();
		List list = null;
		try {
			list = dao.findByCondition(where);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		return list;
	}

	/**
	 * 获得业务权限的 SQL 语句
	 * 
	 * @author nielin
	 * @date 2010-08-09
	 * @return
	 */
	public String getBidSql() {
		User current_user = (User) session
				.getAttribute(SessionConstant.CURRENT_USER);

		StringBuffer s = new StringBuffer();
		int _flag = 0;
		if (current_user.getBusinessids() != null) {
			if (current_user.getBusinessids() != "-1") {
				String[] bids = current_user.getBusinessids().split(",");
				if (bids.length > 0) {
					for (int i = 0; i < bids.length; i++) {
						if (bids[i].trim().length() > 0) {
							if (_flag == 0) {
								s.append(" and ( bid like '%," + bids[i].trim()
										+ ",%' ");
								_flag = 1;
							} else {
								// flag = 1;
								s.append(" or bid like '%," + bids[i].trim()
										+ ",%' ");
							}
						}
					}
					s.append(") ");
				}

			}
		}

		String sql = "";
		if (current_user.getRole() == 0) {
			sql = "";
		} else {
			sql = s.toString();
		}

		return sql;
	}

}
