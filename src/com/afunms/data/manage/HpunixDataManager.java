/*
 * @(#)HpunixDataManager.java     v1.01, Apr 2, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.data.manage;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.afunms.application.dao.ProcessGroupDao;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.SysLogger;
import com.afunms.config.dao.PortconfigDao;
import com.afunms.config.model.IpAlias;
import com.afunms.config.model.Nodeconfig;
import com.afunms.config.model.Nodecpuconfig;
import com.afunms.config.model.Portconfig;
import com.afunms.data.service.HpunixDataService;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.dao.SyslogDao;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.polling.api.I_HostCollectData;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.om.IpMac;
import com.afunms.polling.om.ProcessInfo;
import com.afunms.polling.om.Usercollectdata;
import com.afunms.system.model.User;
import com.afunms.temp.model.DeviceNodeTemp;
import com.afunms.temp.model.NodeTemp;
import com.afunms.temp.model.RouterNodeTemp;
import com.afunms.temp.model.SoftwareNodeTemp;
import com.afunms.temp.model.StorageNodeTemp;
import com.afunms.topology.model.HostNode;

/**
 * 
 * ClassName: HpunixDataManager.java
 * <p>
 * 
 * @author ��Ʒ��
 * @version v1.01
 * @since v1.01
 * @Date Apr 2, 2013 11:39:07 AM
 */
public class HpunixDataManager extends NodeDataManager {

	private static final SysLogger sysLogger = SysLogger
			.getLogger(HpunixDataManager.class.getName());

	public String execute(String action) {
		if ("getInterfaceInfo".equals(action)) {
			return getInterfaceInfo();
		} else if ("getPerformanceInfo".equals(action)) {
			return getPerformanceInfo();
		} else if ("getFileSystemInfo".equals(action)) {
			return getFileSystemInfo();
		} else if ("getConfigInfo".equals(action)) {
			return getConfigInfo();
		} else if ("getProcessInfo".equals(action)) {
			return getProcessInfo();
		} else if ("getServiceInfo".equals(action)) {
			return getServiceInfo();
		} else if ("getDeviceInfo".equals(action)) {
			return getDeviceInfo();
		} else if ("getStorageInfo".equals(action)) {
			return getStorageInfo();
		} else if ("getRouterInfo".equals(action)) {
			return getRouterInfo();
		} else if ("getSyslogInfo".equals(action)) {
			return getSyslogInfo();
		} else if ("getEventInfo".equals(action)) {
			return getEventInfo();
		}
		return ErrorMessage.getErrorMessage(ErrorMessage.ACTION_NO_FOUND);
	}

	/**
	 * ��ȡ Interface ��Ϣ
	 * 
	 * @return {@link String} ���� Interface ��Ϣ��ҳ��
	 */
	public String getInterfaceInfo() {
		getBaseInfo();
		HpunixDataService hpunixDataService = getHpunixDataService();
		String orderFlag = getParaValue("orderflag");
		if (orderFlag == null || orderFlag.trim().length() == 0) {
			orderFlag = "index";
		}
		String sorttype = request.getParameter("sorttype");
		if (sorttype == null || sorttype.trim().length() == 0) {
			sorttype = "desc";
		}
		Vector interfaceInfo = hpunixDataService.getInterfaceInfo(orderFlag,
				sorttype);
		
		//wupinlong add 2013/7/9 �˿ڹ���Ӧ��
        PortconfigDao portconfigDao = new PortconfigDao();
        List<Portconfig> list = null;
        try {
        	list = portconfigDao.loadByIpaddress(hpunixDataService.getNodeDTO().getIpaddress());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            portconfigDao.close();
        }
        Hashtable<String, Portconfig> hashtable = new Hashtable<String, Portconfig>();
        for (Portconfig portconfig : list) {
            hashtable.put(String.valueOf(portconfig.getPortindex()), portconfig);
        }
        request.setAttribute("portconfigHashtable", hashtable);

		request.setAttribute("orderFlag", orderFlag);
		request.setAttribute("sorttype", sorttype);
		request.setAttribute("interfaceInfo", interfaceInfo);
		return "/detail/host/hpunix/interface.jsp";
	}

	public String getPerformanceInfo() {
		getBaseInfo();
		String curDayPingAvgValue = "0"; // ����ƽ����ͨ��
		String curDayPingMaxValue = "0"; // ���������ͨ��
		String curResponseTimeValue = "0";
		String curDayResponseTimeAvgValue = "0"; // ����ƽ����Ӧʱ��
		String curDayResponseTimeMaxValue = "0"; // ���������Ӧʱ��
		String curDayResponseTimeBarImage = "0"; // ������Ӧʱ��ͼƬ
		String curDayCPUAvgValue = "0"; // ����ƽ��CPU
		String curDayCPUMaxValue = "0"; // �������CPU
		String curDayCPUAvgImageInfo = ""; // ����ƽ��CPU�Ǳ���ͼ
		String curDayCPUMaxImageInfo = ""; // �������CPU�Ǳ���ͼ
		List<DeviceNodeTemp> currDeviceNodeTempList = null; // ��ǰ�豸��Ϣ�б�

		Hashtable<Integer, Hashtable<String, String>> curDayDiskValueHashtable = null;
		String curDayAmWindowsDiskChartInfo = ""; // ������״ͼʹ�õ�AMCHART��XML

		Hashtable curDayMaxMemoryValueHashtable = null; // �ڴ�
		Hashtable curDayAvgMemoryValueHashtable = null;

		Hashtable<String, String> curCPUPerValueHashtable = null; // cpu����
		// CPU��ϸ��Ϣ
		Hashtable<String, Hashtable<String, String>> curCPUDetailValueHashtable = null;
		String curCPUDetailAmChartInfo = "0"; // cpu��ϸ��Ϣͼ

		Hashtable<String, String> curPageDetailHashtable = null;
		String pageingUsed = "0";
		String curDayPageMaxValue = "0";
		String curDayPageAvgValue = "0";
		String curPageValueImageInfo = ""; // cpu��ϸ��Ϣͼ
		String curDayPageMaxValueImageInfo = ""; // cpu��ϸ��Ϣͼ
		String curDayPageAvgValueImageInfo = ""; // cpu��ϸ��Ϣͼ

		HpunixDataService hpunixDataService = getHpunixDataService();

		// curDayDiskValueHashtable = (Hashtable<Integer, Hashtable<String,
		// String>>) request
		// .getAttribute("curDayDiskValueHashtable");
		// curDayAmWindowsDiskChartInfo = HpunixDataService
		// .getAmWindowsDiskChartInfo(curDayDiskValueHashtable);

		Hashtable<String, String> curDayPingValuHashtable = hpunixDataService
				.getCurDayPingValueHashtableInfo();

		// ��ʼ��ȡ��ͨ��ֵ
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

		// ��ʼ��ȡ��Ӧʱ��ֵ
		Hashtable<String, String> curDayResponseTimeValueHashtable = hpunixDataService
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
				"����", "").replaceAll("%", "");
		curDayResponseTimeMaxValue = String.valueOf(Math.round(Double
				.valueOf(curDayResponseTimeMaxValue)));

		curDayResponseTimeAvgValue = curDayResponseTimeValueHashtable
				.get("avgpingcon");
		if (curDayResponseTimeAvgValue == null
				|| curDayResponseTimeAvgValue.trim().length() == 0) {
			curDayPingAvgValue = "0";
		}
		curDayResponseTimeAvgValue = curDayResponseTimeAvgValue.replaceAll(
				"����", "").replaceAll("%", "");
		curDayPingAvgValue = String.valueOf(Math.round(Double
				.valueOf(curDayPingAvgValue)));

		// ��ʼ��ȡ��Ӧʱ����״ͼ
		curDayResponseTimeBarImage = hpunixDataService
				.getCurDayResponseTimeBarImageInfo(curResponseTimeValue,
						curDayResponseTimeMaxValue, curDayResponseTimeAvgValue);

		// ��ʼ��ȡCPU��ͼƬ
		Hashtable<String, String> curDayCPUValueHashtable = hpunixDataService
				.getCurDayCPUValueHashtableInfo();

		curDayCPUMaxValue = curDayCPUValueHashtable.get("max");
		if (curDayCPUMaxValue == null || curDayCPUMaxValue.trim().length() == 0) {
			curDayCPUMaxValue = "0";
		}
		curDayCPUMaxValue = curDayCPUMaxValue.replaceAll("%", "").replaceAll(
				"����", "");
		curDayCPUMaxValue = String.valueOf(Math.round(Double
				.valueOf(curDayCPUMaxValue)));
		curDayCPUMaxImageInfo = hpunixDataService
				.getCurDayCPUMaxImageInfo(curDayCPUMaxValue);

		curDayCPUAvgValue = curDayCPUValueHashtable.get("avgcpucon");
		if (curDayCPUAvgValue == null || curDayCPUAvgValue.trim().length() == 0) {
			curDayCPUAvgValue = "0";
		}
		curDayCPUAvgValue = curDayCPUAvgValue.replaceAll("%", "");
		curDayCPUAvgValue = String.valueOf(Math.round(Double
				.valueOf(curDayCPUAvgValue)));
		curDayCPUAvgImageInfo = hpunixDataService
				.getCurDayCPUAvgImageInfo(curDayCPUAvgValue);

		// ��ȡ��ǰ�豸��Ϣ�б�
		currDeviceNodeTempList = hpunixDataService.getCurDeviceListInfo();

		// ��ȡ�����ڴ�
		Hashtable[] curDayAllMemoryValuHashtables = hpunixDataService
				.getCurDayAllMemoryValueHashtable();
		curDayMaxMemoryValueHashtable = curDayAllMemoryValuHashtables[1];
		curDayAvgMemoryValueHashtable = curDayAllMemoryValuHashtables[2];

		// ��ȡCPU��ϸ��Ϣ
		curCPUPerValueHashtable = hpunixDataService
				.getCurCPUPerValueHashtable();
		curCPUDetailValueHashtable = hpunixDataService
				.getCurCPUDetailValueHashtable();
		curCPUDetailAmChartInfo = hpunixDataService.getCurCPUDetailAmChartInfo(
				curCPUPerValueHashtable, curCPUDetailValueHashtable);

		// ��ȡ��ҳ����ϸ��Ϣ
		pageingUsed = (String) request.getAttribute("pageingUsed");
		curPageDetailHashtable = hpunixDataService.getCurPageDetailHashtable();
		if (curPageDetailHashtable != null) {
			curDayPageMaxValue = (String) curPageDetailHashtable
					.get("maxvalue");
			curDayPageAvgValue = (String) curPageDetailHashtable
					.get("avgvalue");
		}
		if (pageingUsed == null || pageingUsed.equals("")) {
			pageingUsed = "0";
		}
		if (curDayPageMaxValue == null || curDayPageMaxValue.equals("")) {
			curDayPageMaxValue = "0";
		}
		if (curDayPageAvgValue == null || curDayPageAvgValue.equals("")) {
			curDayPageAvgValue = "0";
		}
		curPageValueImageInfo = hpunixDataService
				.getCurPageValueImageInfo(pageingUsed);
		curDayPageMaxValueImageInfo = hpunixDataService
				.getCurDayPageMaxValueImageInfo(curDayPageMaxValue);
		curDayPageAvgValueImageInfo = hpunixDataService
				.getCurDayPageAvgValueImageInfo(curDayPageAvgValue);

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

		// request.setAttribute("curDayAmWindowsDiskChartInfo",
		// curDayAmWindowsDiskChartInfo);

		request.setAttribute("curDayMaxMemoryValueHashtable",
				curDayMaxMemoryValueHashtable);
		request.setAttribute("curDayAvgMemoryValueHashtable",
				curDayAvgMemoryValueHashtable);

		request
				.setAttribute("curCPUPerValueHashtable",
						curCPUPerValueHashtable);
		request.setAttribute("curCPUDetailValueHashtable",
				curCPUDetailValueHashtable);
		request
				.setAttribute("curCPUDetailAmChartInfo",
						curCPUDetailAmChartInfo);

		request.setAttribute("curDayPageMaxValue", curDayPageMaxValue);
		request.setAttribute("curDayPageAvgValue", curDayPageAvgValue);
		request.setAttribute("curPageValueImageInfo", curPageValueImageInfo);
		request.setAttribute("curDayPageMaxValueImageInfo",
				curDayPageMaxValueImageInfo);
		request.setAttribute("curDayPageAvgValueImageInfo",
				curDayPageAvgValueImageInfo);
		return "/detail/host/hpunix/performance.jsp";
	}

	public String getFileSystemInfo() {
		getBaseInfo();
		HpunixDataService hpunixDataService = getHpunixDataService();
		Hashtable<Integer, Hashtable<String, String>> curDiskValueHashtableInfo = hpunixDataService
				.getCurDiskValueHashtableInfo();
		String curDiskValueImageInfo = hpunixDataService
				.getCurDiskAmChartInfo(curDiskValueHashtableInfo);

		List<Hashtable<String, String>> curDsikPerfInfo = hpunixDataService
				.getCurDiskPerfInfo();

		List<Hashtable<String, String>> curDsikPerfIoInfo = null;
		// TODO disk IO û����
		Hashtable<String, String> curPagePerfInfo = hpunixDataService
				.getCurPagePerfInfo();
		request.setAttribute("curDiskValueHashtableInfo",
				curDiskValueHashtableInfo);
		request.setAttribute("curDiskValueImageInfo", curDiskValueImageInfo);
		request.setAttribute("curDsikPerfInfo", curDsikPerfInfo);
		request.setAttribute("curDsikPerfIoInfo", curDsikPerfIoInfo);
		request.setAttribute("curPagePerfInfo", curPagePerfInfo);
		return "/detail/host/hpunix/filesystem.jsp";
	}

	public String getConfigInfo() {
		getBaseInfo();
		HpunixDataService hpunixDataService =getHpunixDataService();
		List<Nodecpuconfig> curCPUConfigInfo = hpunixDataService
				.getCurCPUConfigInfo();

		List<Hashtable<String, String>> curNetmediaConfigInfo = hpunixDataService
				.getCurNetmediaConfigInfo();

		Vector<Usercollectdata> curUserConfigInfo = hpunixDataService
				.getCurUserConfigInfo();
		request.setAttribute("curCPUConfigInfo", curCPUConfigInfo);
		request.setAttribute("curNetmediaConfigInfo", curNetmediaConfigInfo);
		request.setAttribute("curUserConfigInfo", curUserConfigInfo);
		return "/detail/host/hpunix/config.jsp";
	}

	public String getArpInfo() {
		getBaseInfo();
		HpunixDataService hpunixDataService =getHpunixDataService();
		List<IpMac> ipMacList = hpunixDataService.getCurAllIpMacInfo();
		request.setAttribute("ipMacList", ipMacList);
		return "/detail/host/hpunix/arp.jsp";
	}

	public String getSoftwareInfo() {
		getBaseInfo();
		HpunixDataService hpunixDataService =getHpunixDataService();
		List<SoftwareNodeTemp> softwareNodeTempList = hpunixDataService
				.getSoftwareInfo();
		request.setAttribute("softwareNodeTempList", softwareNodeTempList);
		return "/detail/host/hpunix/software.jsp";
	}

	public String getServiceInfo() {
		getBaseInfo();
		HpunixDataService hpunixDataService = getHpunixDataService();
		List<Hashtable<String, String>> serviceNodeTempList = hpunixDataService
				.getServiceInfo();
		request.setAttribute("serviceNodeTempList", serviceNodeTempList);
		return "/detail/host/hpunix/service.jsp";
	}

	public String getDeviceInfo() {
		getBaseInfo();
		HpunixDataService hpunixDataService = getHpunixDataService();
		List<DeviceNodeTemp> deviceNodeTempList = hpunixDataService
				.getDeviceInfo();
		request.setAttribute("deviceNodeTempList", deviceNodeTempList);
		return "/detail/host/hpunix/device.jsp";
	}

	public String getStorageInfo() {
		getBaseInfo();
		HpunixDataService hpunixDataService = getHpunixDataService();
		List<StorageNodeTemp> storageNodeTempList = hpunixDataService
				.getStorageInfo();
		request.setAttribute("storageNodeTempList", storageNodeTempList);
		return "/detail/host/windows/storage.jsp";
	}

	public String getRouterInfo() {
		getBaseInfo();
		HpunixDataService hpunixDataService = getHpunixDataService();
		List<RouterNodeTemp> routerNodeTempList = hpunixDataService
				.getCurRouterInfo();
		request.setAttribute("routerNodeTempList", routerNodeTempList);
		return "/detail/net/router.jsp";
	}

	public String getIpListInfo() {
		getBaseInfo();
		HpunixDataService hpunixDataService = getHpunixDataService();
		List<IpAlias> ipAliasList = hpunixDataService.getCurIpListInfo();
		request.setAttribute("ipAliasList", ipAliasList);
		return "/detail/net/iplist.jsp";
	}

	public String getSyslogInfo() {
		getBaseInfo();
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
		List list = null;
		String priorityname = getParaValue("priorityname");
		if (priorityname == null) {
			priorityname = "all";
		}
		NodeDTO node = (NodeDTO) request.getAttribute("node");
		SyslogDao syslogdao = new SyslogDao();
		try {
			list = syslogdao.getQuery(node.getIpaddress(), starttime, totime,
					priorityname);
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("list", list);
		request.setAttribute("priorityname", priorityname);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		return "/detail/host/hpunix/syslog.jsp";
	}

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
		return "/detail/host/hpunix/event.jsp";
	}

	public void getBaseInfo() {
		String category = ""; // ���
		String mac = "";
		String sysDescr = ""; // ����
		String maxAlarmLevel = ""; // ���澯�ȼ�
		String sysObjectID = ""; // OID
		String sysUpTime = ""; // ����ʱ��
		String sysName = ""; // ����
		String sysLocation = ""; // λ��
		String curPingValue = "0"; // ��ǰ��ͨ��
		String curResponseTimeValue = "0"; // ��ǰ��Ӧʱ��
		String curPhysicalMemoryValue = "0"; // ��ǰ�����ڴ�ֵ
		String curSwapMemoryValue = "0"; // ��ǰ�����ڴ�ֵ
		String curCPUValue = "0"; // ��ǰCPUֵ
		String curPingImage = ""; // ��ǰ��ͨ��ͼƬ
		String curPhysicalMemoryImage = ""; // ��ǰ�ڴ�ͼƬ
		String curCPUImage = ""; // ��ǰCPUͼƬ

		Double curCPUValueDouble = 0D; // ��ǰCPUֵ
		String CSDVersion = ""; // �汾
		String processorNum = "0"; // CPU����

		Hashtable<String, String> curPageValueHashtable = null;
		String pageingUsed = "0"; // ��ҳ��
		String totalPageing = "0"; // �ܻ�ҳ�ռ�

		Hashtable<Integer, Hashtable<String, String>> curDayMemoryValueHashtable = null; // �ڴ�
		Hashtable<Integer, Hashtable<String, String>> curDayDiskValueHashtable = null; // ����
		String curDayAmMemoryChartInfo = ""; // �ڴ���״ͼʹ�õ�AMCHART��XML
		String curDayAmDiskChartInfo = ""; // ������״ͼʹ�õ�AMCHART��XML

		String totalPhysicalMemorySize = "0"; // �������ڴ��С
		String totalSwapMemorySize = "0"; // �ܽ����ڴ��С

		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		HpunixDataService hpunixDataService = new HpunixDataService(nodeid,
				type, subtype);
		NodeDTO node = hpunixDataService.getNodeDTO();
		HostNode hostNode = (HostNode) hpunixDataService.getBaseVo();
		category = String.valueOf(hostNode.getCategory());
		maxAlarmLevel = hpunixDataService.getMaxAlarmLevel();
		String supperName = hpunixDataService.getSupperName();

		// ��ȡ�豸��������Ϣ
		Nodeconfig nodeconfig = hpunixDataService.getNodeConfig();
		mac = nodeconfig.getMac();
		processorNum = nodeconfig.getNumberOfProcessors();
		CSDVersion = nodeconfig.getCSDVersion();

		curPageValueHashtable = hpunixDataService.getCurPageValueHashtable();
		if (curPageValueHashtable != null) {
			pageingUsed = curPageValueHashtable.get("Percent_Used");
			totalPageing = curPageValueHashtable.get("Total_Paging_Space");
			if (pageingUsed == null) {
				pageingUsed = "0";
			}
			if (totalPageing == null) {
				totalPageing = "0";
			}
		}
		pageingUsed = pageingUsed.replaceAll("%", "");

		List<NodeTemp> systemList = hpunixDataService.getSystemInfo();
		if (systemList != null && systemList.size() > 0) {
			for (int i = 0; i < systemList.size(); i++) {
				NodeTemp nodetemp = (NodeTemp) systemList.get(i);
				if ("SysDescr".equalsIgnoreCase(nodetemp.getSindex())) {
					sysDescr = nodetemp.getThevalue();
				}
				if ("SysObjectID".equalsIgnoreCase(nodetemp.getSindex())) {
					sysObjectID = nodetemp.getThevalue();
				}
				if ("SysUpTime".equalsIgnoreCase(nodetemp.getSindex())) {
					sysUpTime = nodetemp.getThevalue();
				}
				if ("SysName".equalsIgnoreCase(nodetemp.getSindex())) {
					sysName = nodetemp.getThevalue();
				}
				if ("SysLocation".equalsIgnoreCase(nodetemp.getSindex())) {
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

		// ��ǰ��ͨ�ʺ���Ӧʱ��
		List<NodeTemp> pingList = hpunixDataService.getPingInfo();
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
				}
			}
		}
		curResponseTimeValue = curResponseTimeValue.replaceAll("����", "")
				.replaceAll("%", "");
		curPingImage = hpunixDataService.getPingImageInfo(curPingValue);

		// ��ǰ�����ڴ�ͽ����ڴ�
		List<NodeTemp> curMemoryValueList = hpunixDataService.getMemoryInfo();
		if (curMemoryValueList != null && curMemoryValueList.size() > 0) {
			for (int i = 0; i < curMemoryValueList.size(); i++) {
				NodeTemp nodeTemp = (NodeTemp) curMemoryValueList.get(i);
				if ("SwapMemory".equalsIgnoreCase(nodeTemp.getSindex())
						&& "Utilization".equalsIgnoreCase(nodeTemp
								.getSubentity())) {
					curSwapMemoryValue = Math.round(Float.parseFloat(nodeTemp
							.getThevalue()))
							+ "";
				} else if ("PhysicalMemory".equalsIgnoreCase(nodeTemp
						.getSindex())
						&& "Utilization".equalsIgnoreCase(nodeTemp
								.getSubentity())) {
					curPhysicalMemoryValue = Math.round(Float
							.parseFloat(nodeTemp.getThevalue()))
							+ "";
				} else if ("PhysicalMemory".equalsIgnoreCase(nodeTemp
						.getSindex())
						&& "Capability".equalsIgnoreCase(nodeTemp
								.getSubentity())) {
					totalPhysicalMemorySize = nodeTemp.getThevalue()
							+ nodeTemp.getUnit();
				} else if ("SwapMemory".equalsIgnoreCase(nodeTemp.getSindex())
						&& "Capability".equalsIgnoreCase(nodeTemp
								.getSubentity())) {
					totalSwapMemorySize = nodeTemp.getThevalue()
							+ nodeTemp.getUnit();
				}
			}
			curPhysicalMemoryImage = hpunixDataService
					.getMemoryImageInfo(Double.valueOf(curPhysicalMemoryValue));
		}

		// ��ȡ��ǰCPU������
		curCPUValue = hpunixDataService.getCPUInfo();
		if (curCPUValue != null && curCPUValue.length() > 0) {
			curCPUValueDouble = Double.valueOf(curCPUValue);
			curCPUImage = hpunixDataService.getCPUImageInfo(curCPUValueDouble
					.intValue());
		}

		// �ڴ�ֵ
		curDayMemoryValueHashtable = hpunixDataService
				.getCurDayMemoryValueHashtable();
		// ����ֵ
		curDayDiskValueHashtable = hpunixDataService
				.getCurDayDiskValueHashtable();
		// �ڴ�ͼ
		curDayAmMemoryChartInfo = hpunixDataService
				.getAmMemoryChartInfo(curDayMemoryValueHashtable);
		// ����ͼ
		curDayAmDiskChartInfo = hpunixDataService
				.getAmDiskChartInfo(curDayDiskValueHashtable);

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
		request.setAttribute("curPhysicalMemoryValue", curPhysicalMemoryValue);
		request.setAttribute("curSwapMemoryValue", curSwapMemoryValue);
		request.setAttribute("curMemoryValueList", curMemoryValueList);
		request.setAttribute("curCPUValue", curCPUValue);
		request.setAttribute("curPingImage", curPingImage);
		request.setAttribute("curMemoryImage", curPhysicalMemoryImage);
		request.setAttribute("curCPUImage", curCPUImage);

		request.setAttribute("curDayMemoryValueHashtable",
				curDayMemoryValueHashtable);
		request.setAttribute("curDayDiskValueHashtable",
				curDayDiskValueHashtable);
		request
				.setAttribute("curDayAmMemoryChartInfo",
						curDayAmMemoryChartInfo);
		request.setAttribute("curDayAmDiskChartInfo", curDayAmDiskChartInfo);

		request.setAttribute("CSDVersion", CSDVersion);
		request.setAttribute("processorNum", processorNum);
		request.setAttribute("pageingUsed", pageingUsed);
		request.setAttribute("totalPageing", totalPageing);

		request
				.setAttribute("totalPhysicalMemorySize",
						totalPhysicalMemorySize);
		request.setAttribute("totalSwapMemorySize", totalSwapMemorySize);
	}

	private String getProcessInfo() {
		getBaseInfo();
		Hashtable maxhash = new Hashtable();// "Cpu"--max
		Hashtable processhash = new Hashtable();
		NodeDTO nodeDTO = (NodeDTO) request.getAttribute("node");

		I_HostCollectData hostmanager = new HostCollectDataManager();
		I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();
		// ��order��������Ϣ����
		String order = "MemoryUtilization";
		if ((request.getParameter("orderflag") != null)
				&& (!request.getParameter("orderflag").equals(""))) {
			order = request.getParameter("orderflag");
		}
		if (order.equalsIgnoreCase("CpuTime")) {
			((HostLastCollectDataManager) hostlastmanager).setCpuTime(true);
		}
		try {
			processhash = hostlastmanager.getProcess(nodeDTO.getIpaddress(),
					"Process", order, null, null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// gzm
		Hashtable phash;// ԭ�����ڼ�����м����
		Hashtable newPHash = new Hashtable();// �м���������ڴ洢�Լ�д�Ĵ���
		Hashtable newProcessHash = new Hashtable();// �洢������hashtable
		Hashtable detailHash = new Hashtable();
		int num = 0;
		Pattern p1 = Pattern.compile("(\\d+):(\\d+)");
		if (processhash != null) {
			for (int m = 0; m < processhash.size(); m++) {
				phash = (Hashtable) processhash.get(new Integer(m));
				if (phash != null) {
					String Name = ((String) phash.get("Name")).trim();
					if (newProcessHash.containsKey(Name)) {
						ProcessInfo totalProcess = (ProcessInfo) newProcessHash
								.get(Name);
						String CpuTime = (String) phash.get("CpuTime");
						String CpuUtilization = (String) phash
								.get("CpuUtilization");
						String Memory = (String) phash.get("Memory");
						String MemoryUtilization = (String) phash
								.get("MemoryUtilization");
						String threadCount = (String) phash.get("ThreadCount");
						String handleCount = (String) phash.get("HandleCount");
						if (CpuTime != null) {
							if (CpuTime.indexOf(":") != -1) {
								Matcher matcher = p1.matcher(CpuTime);
								if (matcher.find()) {
									String t1 = matcher.group(1);
									String t2 = matcher.group(2);
									float sumOfCPU = Float.parseFloat(t1) * 60
											+ Float.parseFloat(t2);
									totalProcess
											.setCpuTime(sumOfCPU
													+ (Float) totalProcess
															.getCpuTime());
								}
							} else {
								float sumOfCPU = Float.parseFloat(CpuTime
										.replace("��", ""));
								totalProcess.setCpuTime(sumOfCPU
										+ (Float) totalProcess.getCpuTime());
							}
						}
						Float sumOfCpuUtilization = 0f;
						if (CpuUtilization != null
								&& CpuUtilization.trim().length() > 0) {
							sumOfCpuUtilization = Float
									.parseFloat(CpuUtilization.substring(0,
											CpuUtilization.length() - 1));
							totalProcess.setCpuUtilization(sumOfCpuUtilization
									+ (Float) totalProcess.getCpuUtilization());
						} else {
							totalProcess.setCpuUtilization("-");
						}

						Float sumOfMem = Float.valueOf("0");
						if (Memory.trim().length() > 1) {
							sumOfMem = Float.parseFloat(Memory.substring(0,
									Memory.length() - 1));
						}
						totalProcess.setMemory(sumOfMem
								+ (Float) totalProcess.getMemory());
						NumberFormat numberFormat = new DecimalFormat();
						numberFormat.setMaximumFractionDigits(0);

						Float sumOfMemUtilization = Float.valueOf("0");
						if (MemoryUtilization.trim().length() > 1) {
							sumOfMemUtilization = Float
									.parseFloat(MemoryUtilization.substring(0,
											MemoryUtilization.length() - 1));
						}
						Float memoryUtilization = sumOfMemUtilization
								+ (Float) totalProcess.getMemoryUtilization();
						memoryUtilization = Float.parseFloat(numberFormat
								.format(memoryUtilization));
						totalProcess.setMemoryUtilization(memoryUtilization);

						if (threadCount != null) {
							totalProcess.setThreadCount(threadCount);
						}
						if (handleCount != null) {
							totalProcess.setHandleCount(handleCount);
						}
						ProcessInfo processInfo = new ProcessInfo();
						processInfo.setName((String) phash.get("Name"));
						processInfo.setType((String) phash.get("Type"));
						processInfo.setStatus((String) phash.get("Status"));
						if (CpuTime != null) {
							if (CpuTime.indexOf(":") != -1) {
								Matcher matcher2 = p1.matcher(CpuTime);
								if (matcher2.find()) {
									String t1 = matcher2.group(1);
									String t2 = matcher2.group(2);
									float sumOfCPU = Float.parseFloat(t1) * 60
											+ Float.parseFloat(t2);
									processInfo.setCpuTime(sumOfCPU);
								}
							} else {
								float sumOfCPU = Float.parseFloat(CpuTime
										.replace("��", ""));
								processInfo.setCpuTime(sumOfCPU);
							}
						}

						processInfo.setUSER((String) phash.get("USER"));
						processInfo.setStartTime((String) phash
								.get("StartTime"));

						processInfo.setCpuUtilization(sumOfCpuUtilization);

						processInfo.setPid((String) phash.get("process_id"));
						processInfo.setMemoryUtilization(sumOfMemUtilization);
						processInfo.setMemory(sumOfMem);
						processInfo.setThreadCount(threadCount);
						processInfo.setHandleCount(handleCount);
						((Vector) detailHash.get(((String) phash.get("Name"))
								.trim())).add(processInfo);

					} else {
						ProcessInfo processInfo = new ProcessInfo();
						processInfo.setName(Name);
						processInfo.setUSER((String) phash.get("USER"));
						processInfo.setType((String) phash.get("Type"));
						processInfo.setStatus((String) phash.get("Status"));

						String CpuTime = (String) phash.get("CpuTime");
						if (CpuTime != null) {
							if (CpuTime.indexOf(":") != -1) {
								Matcher matcher = p1.matcher(CpuTime);
								if (matcher.find()) {
									String t1 = matcher.group(1);
									String t2 = matcher.group(2);
									float sumOfCPU = Float.parseFloat(t1) * 60
											+ Float.parseFloat(t2);
									processInfo.setCpuTime(sumOfCPU);
								}
							} else {
								float sumOfCPU = Float.parseFloat(CpuTime
										.replace("��", ""));
								processInfo.setCpuTime(sumOfCPU);
							}
						}

						String MemoryUtilization = (String) phash
								.get("MemoryUtilization");
						Float sumOfMemUtilization = Float.valueOf("0");
						if (MemoryUtilization.trim().length() > 1) {
							sumOfMemUtilization = Float
									.parseFloat(MemoryUtilization.substring(0,
											MemoryUtilization.length() - 1));
						}
						processInfo.setMemoryUtilization(sumOfMemUtilization);

						String Memory = (String) phash.get("Memory");
						Float sumOfMem = Float.valueOf("0");
						if (Memory.trim().length() > 1) {
							sumOfMem = Float.parseFloat(Memory.substring(0,
									Memory.length() - 1));
						}
						processInfo.setMemory(sumOfMem);

						String CpuUtilization = (String) phash
								.get("CpuUtilization");
						if (CpuUtilization != null
								&& CpuUtilization.trim().length() > 0) {
							Float sumOfCpuUtilization = Float
									.parseFloat(CpuUtilization.substring(0,
											CpuUtilization.length() - 1));
							processInfo.setCpuUtilization(sumOfCpuUtilization);
						} else {
							processInfo.setCpuUtilization("-");
						}
						processInfo.setPid((String) phash.get("process_id"));
						String threadCount = (String) phash.get("ThreadCount");
						processInfo.setThreadCount(threadCount);
						String handleCount = (String) phash.get("HandleCount");
						processInfo.setHandleCount(handleCount);
						ProcessInfo newProcessInfo = processInfo.clone();
						newProcessHash.put(Name, processInfo);
						Vector detailVect = new Vector();
						detailVect.add(newProcessInfo);
						detailHash.put(Name, detailVect);
					}
				}
			}
		}
		Enumeration newProEnu = newProcessHash.keys();
		while (newProEnu.hasMoreElements()) {
			String processName = (String) newProEnu.nextElement();
			ProcessInfo p = (ProcessInfo) newProcessHash.get(processName);
			Vector v = (Vector) detailHash.get(processName);
			p.setCount(v.size());
		}

		String processName = this.getParaValue("processName");

		Vector detailVect = null;
		String layer = this.getParaValue("layer");
		if (processName != null) {
			if (detailHash.containsKey(processName)) {
				detailVect = (Vector) detailHash.get(processName);
			}
		}

		List list = new ArrayList();
		String orderflag = this.getParaValue("orderflag");
		if (detailVect == null) {
			Collection collection = newProcessHash.values();
			Iterator it = collection.iterator();

			while (it.hasNext()) {
				ProcessInfo info = (ProcessInfo) it.next();
				list.add(info);
			}
		} else {
			list = detailVect;
		}
		sort(list, orderflag);
		dateFormat(list);
		// gzm//
		
		//wupinlong add ��ӹؼ��ģ����ùؼ���Ϊ���Բ鿴��ʷ��¼
        ProcessGroupDao groupDao = new ProcessGroupDao();
        Hashtable<String, String> procNameHash = null;
        try {
			procNameHash = groupDao.findByImport(nodeDTO.getNodeid());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			groupDao.close();
		}
		request.setAttribute("processhash", processhash);
		request.setAttribute("newProcessHash", newProcessHash);
		request.setAttribute("detailVect", detailVect);
		request.setAttribute("list", list);
		request.setAttribute("layer", layer);
		request.setAttribute("processName", processName);
		//wupinlong add
        request.setAttribute("procNameHash", procNameHash);
		return "/detail/host/hpunix/process.jsp";
	}

	private void dateFormat(List list) {
		for (int i = 0; i < list.size(); i++) {
			ProcessInfo processInfo = (ProcessInfo) list.get(i);
			Float CpuTime = (Float) processInfo.getCpuTime();
			// System.out.println("CpuTime====="+CpuTime);
			int int_CpuTime = CpuTime.intValue();
			int fenzhong = int_CpuTime / 60;
			int miaozhong = int_CpuTime % 60;
			String s_CpuTime = fenzhong + ":" + miaozhong + "��";
			processInfo.setCpuTime(s_CpuTime);
			processInfo.setCpuUtilization(processInfo.getCpuUtilization()
					.toString()
					+ "%");
			processInfo.setMemory(processInfo.getMemory() + "K");
			processInfo.setMemoryUtilization(processInfo.getMemoryUtilization()
					+ "%");
		}
	}

	private HpunixDataService getHpunixDataService() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		return new HpunixDataService(nodeid, type, subtype);
	}

	private List sort(List list, String type) {
		if (type.equals("CpuTime")) {
			Collections.sort(list, new Comparator() {
				public int compare(Object o1, Object o2) {
					ProcessInfo p1 = (ProcessInfo) o1;
					ProcessInfo p2 = (ProcessInfo) o2;
					if ((Float) p1.getCpuTime() <= (Float) p2.getCpuTime())
						return 1;
					else
						return -1;
				}
			});
		}
		if (type.equals("CpuUtilization")) {
			Collections.sort(list, new Comparator() {
				public int compare(Object o1, Object o2) {
					ProcessInfo p1 = (ProcessInfo) o1;
					ProcessInfo p2 = (ProcessInfo) o2;
					if ((Float) p1.getCpuUtilization() <= (Float) p2
							.getCpuUtilization())
						return 1;
					else
						return -1;
				}
			});
		}
		if (type.equals("Memory") || type.equals("")) {
			Collections.sort(list, new Comparator() {
				public int compare(Object o1, Object o2) {
					ProcessInfo p1 = (ProcessInfo) o1;
					ProcessInfo p2 = (ProcessInfo) o2;
					if ((Float) p1.getMemory() <= (Float) p2.getMemory())
						return 1;
					else
						return -1;
				}
			});
		}
		if (type.equals("MemoryUtilization")) {
			Collections.sort(list, new Comparator() {
				public int compare(Object o1, Object o2) {
					ProcessInfo p1 = (ProcessInfo) o1;
					ProcessInfo p2 = (ProcessInfo) o2;
					if ((Float) p1.getMemoryUtilization() <= (Float) p2
							.getMemoryUtilization())
						return 1;
					else
						return -1;
				}
			});
		}
		return list;
	}
}
