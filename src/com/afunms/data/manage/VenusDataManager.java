/*
 * @(#)VenusDataManager.java     v1.01, Mar 26, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.data.manage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.common.base.ErrorMessage;
import com.afunms.common.util.SessionConstant;
import com.afunms.config.dao.PortconfigDao;
import com.afunms.config.model.IpAlias;
import com.afunms.config.model.Portconfig;
import com.afunms.data.service.TomcatDataService;
import com.afunms.data.service.VenusDataService;
import com.afunms.detail.reomte.model.InterfaceInfo;
import com.afunms.event.dao.EventListDao;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.polling.om.IpMac;
import com.afunms.system.model.User;
import com.afunms.temp.model.DeviceNodeTemp;
import com.afunms.temp.model.NodeTemp;
import com.afunms.temp.model.RouterNodeTemp;
import com.afunms.topology.model.HostNode;

/**
 * 
 * ClassName: VenusDataManager.java
 * <p>
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Mar 26, 2013 11:16:37 AM
 */

public class VenusDataManager extends NodeDataManager {

	/**
	 * execute:
	 * 
	 * @param action
	 * @return
	 * 
	 * @since v1.01
	 * @see com.afunms.common.base.ManagerInterface#execute(java.lang.String)
	 */
	public String execute(String action) {
		if ("getInterfaceInfo".equals(action)) {
			return getInterfaceInfo();
		} else if ("getPerformanceInfo".equals(action)) {
			return getPerformanceInfo();
		} else if ("getARPInfo".equals(action)) {
			return getArpInfo();
		} else if ("getRouterInfo".equals(action)) {
			return getRouterInfo();
		} else if ("getIpListInfo".equals(action)) {
			return getIpListInfo();
		} else if ("getEventInfo".equals(action)) {
			return getEventInfo();
		}
		return ErrorMessage.getErrorMessage(ErrorMessage.ACTION_NO_FOUND);
	}

	/**
	 * getInterfaceInfo:
	 * <p>
	 * 获取 Interface 信息
	 * 
	 * @return {@link String} - Interface 信息的页面
	 * 
	 * @since v1.01
	 */
	public String getInterfaceInfo() {
		getBaseInfo();
		VenusDataService dataService = getDataService();
		String orderFlag = getParaValue("orderflag");
		String important = getParaValue("important");
		if (orderFlag == null || orderFlag.trim().length() == 0) {
			orderFlag = "index";
		}

		Vector interfaceInfo = null;
		interfaceInfo = dataService.getInterfaceInfo(orderFlag);
		PortconfigDao portconfigDao = new PortconfigDao();
		List<Portconfig> list = null;
		try {
			if ("0".equals(important)) {
				list = portconfigDao.loadByIpaddress(dataService.getNodeDTO()
						.getIpaddress());
			} else {
				important = "1";
				list = portconfigDao.findByImportant(dataService.getNodeDTO()
						.getIpaddress(), important);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			portconfigDao.close();
		}
		Hashtable<String, Portconfig> hashtable = new Hashtable<String, Portconfig>();
		for (Portconfig portconfig : list) {
			hashtable
					.put(String.valueOf(portconfig.getPortindex()), portconfig);
		}
		if (!"0".equals(important)) {
			Vector newInterfaceInfo = new Vector();
			for (Object object : interfaceInfo) {
				String[] perInterfaceInfo = (String[]) object;
				if (hashtable.containsKey(perInterfaceInfo[0])) {
					newInterfaceInfo.add(perInterfaceInfo);
				}
			}
			interfaceInfo = newInterfaceInfo;
		}
		request.setAttribute("orderFlag", orderFlag);
		request.setAttribute("important", important);
		request.setAttribute("interfaceInfo", interfaceInfo);
		request.setAttribute("portconfigHashtable", hashtable);
		return "/detail/firewall/venus/interface.jsp";
	}

	/**
	 * getPerformanceInfo:
	 * <p>
	 * 获取性能信息
	 * 
	 * @return {@link String} - 性能信息页面
	 * 
	 * @since v1.01
	 */
	public String getPerformanceInfo() {
		getBaseInfo();
		String curDayPingAvgValue = "0"; // 当天平均连通率
		String curDayPingMaxValue = "0"; // 当天最大连通率
		String curResponseTimeValue = "0";
		String curDayResponseTimeAvgValue = "0"; // 当天平均响应时间
		String curDayResponseTimeMaxValue = "0"; // 当天最大响应时间
		String curDayResponseTimeBarImage = "0"; // 当天响应时间图片
		String curDayCPUAvgValue = "0"; // 当天平均CPU
		String curDayCPUMaxValue = "0"; // 当天最大CPU
		String curDayCPUAvgImageInfo = ""; // 当天平均CPU仪表盘图
		String curDayCPUMaxImageInfo = ""; // 当天最大CPU仪表盘图
		List<DeviceNodeTemp> currDeviceNodeTempList = null; // 当前设备信息列表
		String curAllInBandwidthUtilHdxValue = "0"; // 当前入口总流速
		String curDayAllInBandwidthUtilHdxMaxValue = "0"; // 当天入口总流速最大值
		String curDayAllInBandwidthUtilHdxAvgValue = "0"; // 当天入口总流速平均值
		String curAllOutBandwidthUtilHdxValue = "0";// 当前出口总流速
		String curDayAllOutBandwidthUtilHdxMaxValue = "0"; // 当天出口总流速最大值
		String curDayAllOutBandwidthUtilHdxAvgValue = "0"; // 当天出口总流速平均值

		VenusDataService dataService = getDataService();

		Hashtable<String, String> curDayPingValuHashtable = dataService
				.getCurDayPingValueHashtableInfo();

		// 开始获取连通率值
		curDayPingMaxValue = curDayPingValuHashtable.get("pingmax");
		if (curDayPingMaxValue == null
				|| curDayPingMaxValue.trim().length() == 0) {
			curDayPingMaxValue = "0";
		}
		curDayPingMaxValue = curDayPingMaxValue.replaceAll("%", "");
		curDayPingMaxValue = String.valueOf(Math.round(Double
				.valueOf(curDayPingMaxValue)));

		curDayPingAvgValue = curDayPingValuHashtable.get("avgpingcon");
		if (curDayPingAvgValue == null
				|| curDayPingAvgValue.trim().length() == 0) {
			curDayPingAvgValue = "0";
		}
		curDayPingAvgValue = curDayPingAvgValue.replaceAll("%", "");
		curDayPingAvgValue = String.valueOf(Math.round(Double
				.valueOf(curDayPingAvgValue)));

		// 开始获取响应时间值
		Hashtable<String, String> curDayResponseTimeValueHashtable = dataService
				.getCurDayResponseTimeValueHashtableInfo();

		curResponseTimeValue = (String) request
				.getAttribute("curResponseTimeValue");
		curDayResponseTimeMaxValue = curDayResponseTimeValueHashtable
				.get("pingmax");
		if (curDayResponseTimeMaxValue == null
				|| curDayResponseTimeMaxValue.trim().length() == 0) {
			curDayResponseTimeMaxValue = "0";
		}
		curDayResponseTimeMaxValue = curDayResponseTimeMaxValue.replaceAll(
				"毫秒", "").replaceAll("%", "");
		curDayResponseTimeMaxValue = String.valueOf(Math.round(Double
				.valueOf(curDayResponseTimeMaxValue)));

		curDayResponseTimeAvgValue = curDayResponseTimeValueHashtable
				.get("avgpingcon");
		if (curDayResponseTimeAvgValue == null
				|| curDayResponseTimeAvgValue.trim().length() == 0) {
			curDayResponseTimeAvgValue = "0";
		}
		curDayResponseTimeAvgValue = curDayResponseTimeAvgValue.replaceAll(
				"毫秒", "").replaceAll("%", "");
		curDayResponseTimeAvgValue = String.valueOf(Math.round(Double
				.valueOf(curDayResponseTimeAvgValue)));

		// 开始获取响应时间柱状图
		curDayResponseTimeBarImage = dataService
				.getCurDayResponseTimeBarImageInfo(curResponseTimeValue,
						curDayResponseTimeMaxValue, curDayResponseTimeAvgValue);

		// 开始获取CPU的图片
		Hashtable<String, String> curDayCPUValueHashtable = dataService
				.getCurDayCPUValueHashtableInfo();

		curDayCPUMaxValue = curDayCPUValueHashtable.get("max");
		if (curDayCPUMaxValue == null || curDayCPUMaxValue.trim().length() == 0) {
			curDayCPUMaxValue = "0";
		}
		curDayCPUMaxValue = curDayCPUMaxValue.replaceAll("%", "");
		curDayCPUMaxValue = String.valueOf(Math.round(Double
				.valueOf(curDayCPUMaxValue)));
		curDayCPUMaxImageInfo = dataService
				.getCurDayCPUMaxImageInfo(curDayCPUMaxValue);

		curDayCPUAvgValue = curDayCPUValueHashtable.get("avgcpucon");
		if (curDayCPUAvgValue == null || curDayCPUAvgValue.trim().length() == 0) {
			curDayCPUAvgValue = "0";
		}
		curDayCPUAvgValue = curDayCPUAvgValue.replaceAll("%", "");
		curDayCPUAvgValue = String.valueOf(Math.round(Double
				.valueOf(curDayCPUAvgValue)));
		curDayCPUAvgImageInfo = dataService
				.getCurDayCPUAvgImageInfo(curDayCPUAvgValue);

		// 获取当前设备信息列表
		currDeviceNodeTempList = dataService.getCurDeviceListInfo();

		// 获取综合流速信息
		List<InterfaceInfo> curAllBandwidthUtilHdxList = dataService
				.getCurAllBandwidthUtilHdxInfo();
		if (curAllBandwidthUtilHdxList != null) {
			for (InterfaceInfo interfaceInfo : curAllBandwidthUtilHdxList) {
				curAllInBandwidthUtilHdxValue = interfaceInfo
						.getInBandwidthUtilHdx();
				curAllOutBandwidthUtilHdxValue = interfaceInfo
						.getInBandwidthUtilHdx();
			}
		}
		Hashtable<String, Integer> curDayAllBandwidthUtilHdxHashtable = dataService
				.getCurDayAllBandwidthUtilHdxHashtable();
		if (curDayAllBandwidthUtilHdxHashtable != null
				&& curDayAllBandwidthUtilHdxHashtable.size() > 0) {
			if (curDayAllBandwidthUtilHdxHashtable.containsKey("maxin")) {
				curDayAllInBandwidthUtilHdxMaxValue = String
						.valueOf(curDayAllBandwidthUtilHdxHashtable
								.get("maxin"));
			}
			if (curDayAllBandwidthUtilHdxHashtable.containsKey("agvin")) {
				curDayAllInBandwidthUtilHdxAvgValue = String
						.valueOf(curDayAllBandwidthUtilHdxHashtable
								.get("agvin"));
			}
			if (curDayAllBandwidthUtilHdxHashtable.containsKey("maxout")) {
				curDayAllOutBandwidthUtilHdxMaxValue = String
						.valueOf(curDayAllBandwidthUtilHdxHashtable
								.get("maxout"));
			}
			if (curDayAllBandwidthUtilHdxHashtable.containsKey("agvout")) {
				curDayAllOutBandwidthUtilHdxAvgValue = String
						.valueOf(curDayAllBandwidthUtilHdxHashtable
								.get("agvout"));
			}
		}

		request.setAttribute("curDayPingMaxValue", curDayPingMaxValue);
		request.setAttribute("curDayPingAvgValue", curDayPingAvgValue);

		request.setAttribute("curDayResponseTimeMaxValue",
				curDayResponseTimeMaxValue);
		request.setAttribute("curDayResponseTimeAvgValue",
				curDayResponseTimeAvgValue);
		request.setAttribute("curDayResponseTimeBarImage",
				curDayResponseTimeBarImage);

		request.setAttribute("curDayCPUMaxValue", curDayCPUMaxValue);
		request.setAttribute("curDayCPUAvgValue", curDayCPUAvgValue);
		request.setAttribute("curDayCPUMaxImageInfo", curDayCPUMaxImageInfo);
		request.setAttribute("curDayCPUAvgImageInfo", curDayCPUAvgImageInfo);

		request.setAttribute("currDeviceNodeTempList", currDeviceNodeTempList);

		request.setAttribute("curAllInBandwidthUtilHdxValue",
				curAllInBandwidthUtilHdxValue);
		request.setAttribute("curAllOutBandwidthUtilHdxValue",
				curAllOutBandwidthUtilHdxValue);
		request.setAttribute("curDayAllInBandwidthUtilHdxMaxValue",
				curDayAllInBandwidthUtilHdxMaxValue);
		request.setAttribute("curDayAllInBandwidthUtilHdxAvgValue",
				curDayAllInBandwidthUtilHdxAvgValue);
		request.setAttribute("curDayAllOutBandwidthUtilHdxMaxValue",
				curDayAllOutBandwidthUtilHdxMaxValue);
		request.setAttribute("curDayAllOutBandwidthUtilHdxAvgValue",
				curDayAllOutBandwidthUtilHdxAvgValue);
		return "/detail/firewall/venus/performance.jsp";
	}

	/**
	 * getArpInfo:
	 * <p>
	 * 获取 ARP 信息
	 * 
	 * @return {@link String} - ARP 信息页面
	 * 
	 * @since v1.01
	 */
	public String getArpInfo() {
		getBaseInfo();
		VenusDataService netDataService = getDataService();
		List<IpMac> ipMacList = netDataService.getCurAllIpMacInfo();
		request.setAttribute("ipMacList", ipMacList);
		return "/detail/firewall/venus/arp.jsp";
	}

	/**
	 * getRouterInfo:
	 * <p>
	 * 获取路由信息
	 * 
	 * @return {@link String} - 返回路由信息页面
	 * 
	 * @since v1.01
	 */
	public String getRouterInfo() {
		getBaseInfo();
		VenusDataService dataService = getDataService();
		List<RouterNodeTemp> routerNodeTempList = dataService
				.getCurRouterInfo();
		request.setAttribute("routerNodeTempList", routerNodeTempList);
		return "/detail/firewall/venus/router.jsp";
	}

	/**
	 * getIpListInfo:
	 * <p>
	 * 获取 ipList 信息
	 * 
	 * @return {@link String} - 返回 ipList 信息页面
	 * 
	 * @since v1.01
	 */
	public String getIpListInfo() {
		getBaseInfo();
		VenusDataService dataService = getDataService();
		List<IpAlias> ipAliasList = dataService.getCurIpListInfo();
		request.setAttribute("ipAliasList", ipAliasList);
		return "/detail/firewall/venus/iplist.jsp";
	}

	/**
	 * getEventInfo:
	 * <p>
	 * 获取告警信息
	 * 
	 * @return {@link String} - 返回告警信息页面
	 * 
	 * @since v1.01
	 */
	public String getEventInfo() {
		getBaseInfo();
		String nodeid = getNodeid();
		int status = getParaIntValue("status");
		int level1 = getParaIntValue("level1");
		if (status == -1) {
			status = 99;
		}
		if (level1 == -1) {
			level1 = 99;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startdate = request.getParameter("startdate");
		if (startdate == null) {
			startdate = sdf.format(new Date());
		}
		String todate = request.getParameter("todate");
		if (todate == null) {
			todate = sdf.format(new Date());
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String bid = ((User) session.getAttribute(SessionConstant.CURRENT_USER))
				.getBusinessids();
		List list = null;
		EventListDao dao = new EventListDao();
		try {
			list = dao.getQuery(starttime, totime, status + "", level1 + "",
					bid, Integer.valueOf(nodeid));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		request.setAttribute("status", status);
		request.setAttribute("level1", level1);
		request.setAttribute("list", list);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		return "/detail/firewall/venus/event.jsp";
	}

	public void getBaseInfo() {
		String category = ""; // 类别
		String mac = "";
		String sysDescr = ""; // 描述
		String maxAlarmLevel = ""; // 最大告警等级
		String sysObjectID = ""; // OID
		String sysUpTime = ""; // 启动时间
		String sysName = ""; // 名称
		String sysLocation = ""; // 位置
		String curPingValue = "0"; // 当前连通率
		String curResponseTimeValue = "0"; // 当前响应时间
		String curMemoryValue = "0"; // 当前内存值
		String curCPUValue = "0"; // 当前CPU值
		String curPingImage = ""; // 当前连通率图片
		String curMemoryImage = ""; // 当前内存图片
		String curCPUImage = ""; // 当前CPU图片

		Double curMemoryValueDouble = 0D; // 当前内存值
		Double curCPUValueDouble = 0D; // 当前CPU值

		String nodeid = getNodeid();
		VenusDataService dataService = getDataService();
		NodeDTO node = dataService.getNodeDTO();
		HostNode hostNode = (HostNode) dataService.getBaseVo();
		category = String.valueOf(hostNode.getCategory());
		maxAlarmLevel = dataService.getMaxAlarmLevel();
		mac = hostNode.getBridgeAddress();
		String supperName = dataService.getSupperName();

		List<NodeTemp> systemList = dataService.getSystemInfo();
		if (systemList != null && systemList.size() > 0) {
			for (int i = 0; i < systemList.size(); i++) {
				NodeTemp nodetemp = (NodeTemp) systemList.get(i);
				if ("sysDescr".equals(nodetemp.getSindex())) {
					sysDescr = nodetemp.getThevalue();
				}
				if ("sysObjectID".equals(nodetemp.getSindex())) {
					sysObjectID = nodetemp.getThevalue();
				}
				if ("sysUpTime".equals(nodetemp.getSindex())) {
					sysUpTime = nodetemp.getThevalue();
				}
				if ("sysName".equals(nodetemp.getSindex())) {
					sysName = nodetemp.getThevalue();
				}
				if ("sysLocation".equals(nodetemp.getSindex())) {
					sysLocation = nodetemp.getThevalue();
				}
			}
		}
		if (sysObjectID == null || sysObjectID.trim().length() == 0) {
			sysObjectID = node.getSysOid();
		}
		if (sysObjectID == null || sysObjectID.trim().length() == 0) {
			sysObjectID = hostNode.getSysOid();
		}

		List<NodeTemp> pingList = dataService.getPingInfo();
		if (pingList != null && pingList.size() > 0) {
			for (int i = 0; i < pingList.size(); i++) {
				NodeTemp nodetemp = (NodeTemp) pingList.get(i);
				if ("ConnectUtilization".equals(nodetemp.getSindex())) {
					curPingValue = nodetemp.getThevalue();
					if (curPingValue == null
							|| curPingValue.trim().length() == 0) {
						curPingValue = "0";
					}
					curPingValue.replaceAll("%", "");

				} else if ("ResponseTime".equals(nodetemp.getSindex())) {
					curResponseTimeValue = nodetemp.getThevalue();
					if (curResponseTimeValue == null) {
						curResponseTimeValue = "0";
					}
				}
			}
		}
		curResponseTimeValue = curResponseTimeValue.replaceAll("毫秒", "")
				.replaceAll("%", "");
		curPingImage = dataService.getPingImageInfo(curPingValue);

		List<NodeTemp> curMemoryValueList = dataService.getMemoryInfo();
		if (curMemoryValueList != null && curMemoryValueList.size() > 0) {
			for (int i = 0; i < curMemoryValueList.size(); i++) {
				NodeTemp nodeTemp = (NodeTemp) curMemoryValueList.get(i);
				if (nodeTemp.getThevalue() != null) {
					curMemoryValueDouble += Double.valueOf(nodeTemp
							.getThevalue());
				}
			}
			if (curMemoryValueList.size() > 0) {
				curMemoryValueDouble = curMemoryValueDouble
						/ curMemoryValueList.size();
			}
			curMemoryValue = String.valueOf(Math.round(curMemoryValueDouble));
		}
		curMemoryImage = dataService.getMemoryImageInfo(Double
				.valueOf(curMemoryValue));

		curCPUValue = dataService.getCPUInfo();
		if (curCPUValue != null && curCPUValue.length() > 0) {
			curCPUValueDouble = Double.valueOf(curCPUValue);
		}
		curCPUImage = dataService.getCPUImageInfo(curCPUValueDouble.intValue());

		request.setAttribute("node", node);
		request.setAttribute("hostNode", hostNode);
		request.setAttribute("nodeid", nodeid);
		request.setAttribute("maxAlarmLevel", maxAlarmLevel);
		request.setAttribute("category", category);
		request.setAttribute("mac", mac);
		request.setAttribute("supperName", supperName);
		request.setAttribute("sysDescr", sysDescr);
		request.setAttribute("sysObjectID", sysObjectID);
		request.setAttribute("sysUpTime", sysUpTime);
		request.setAttribute("sysName", sysName);
		request.setAttribute("sysLocation", sysLocation);
		request.setAttribute("curPingValue", curPingValue);
		request.setAttribute("curResponseTimeValue", curResponseTimeValue);
		request.setAttribute("curMemoryValue", curMemoryValue);
		request.setAttribute("curMemoryValueList", curMemoryValueList);
		request.setAttribute("curCPUValue", curCPUValue);
		request.setAttribute("curPingImage", curPingImage);
		request.setAttribute("curMemoryImage", curMemoryImage);
		request.setAttribute("curCPUImage", curCPUImage);
	}

	/**
	 * getDataService:
	 * <p>
	 * 获取数据服务类
	 * 
	 * @return {@link TomcatDataService} - Tomcat 数据库服务了
	 * 
	 * @since v1.01
	 */
	public VenusDataService getDataService() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		VenusDataService dataService = new VenusDataService(nodeid, type,
				subtype);
		return dataService;
	}
}
