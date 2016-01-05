/*
 * @(#)H3CFireWallDataService.java     v1.01, Aug 19, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.data.service;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.alarm.service.NodeAlarmService;
import com.afunms.common.base.BaseVo;
import com.afunms.common.util.CommonUtil;
import com.afunms.common.util.CreateBarPic;
import com.afunms.common.util.CreateMetersPic;
import com.afunms.common.util.DateE;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.TitleModel;
import com.afunms.config.dao.SupperDao;
import com.afunms.config.model.IpAlias;
import com.afunms.config.model.Supper;
import com.afunms.detail.net.service.NetService;
import com.afunms.detail.reomte.model.InterfaceInfo;
import com.afunms.detail.service.cpuInfo.CpuInfoService;
import com.afunms.detail.service.deviceInfo.DeviceInfoService;
import com.afunms.detail.service.interfaceInfo.InterfaceInfoService;
import com.afunms.detail.service.ipListInfo.IpListInfoService;
import com.afunms.detail.service.ipMacInfo.IpMacInfoService;
import com.afunms.detail.service.memoryInfo.MemoryInfoService;
import com.afunms.detail.service.pingInfo.PingInfoService;
import com.afunms.detail.service.routerInfo.RouterInfoService;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.om.IpMac;
import com.afunms.temp.model.DeviceNodeTemp;
import com.afunms.temp.model.NodeTemp;
import com.afunms.temp.model.RouterNodeTemp;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;

/**
 * 
 * ClassName: H3CFireWallDataService.java
 * <p>
 * {@link H3CFireWallDataService} H3C����ǽ����ϸ��Ϣ
 * 
 * @author ��Ʒ��
 * @version v1.01
 * @since v1.01
 * @Date Aug 19, 2013 6:52:31 PM
 * @mail wupinlong@dhcc.com.cn
 */
public class H3CFireWallDataService extends NodeDataService {

	private static final SysLogger logger = SysLogger
			.getLogger(TopsecDataService.class.getName());

	private static CreateMetersPic cmp = new CreateMetersPic();

	private static final String pingImagePath = "resource/image/jfreechart/reportimg/";

	private static final String pingImageName = "pingdata.png";

	private static final String pingBgImagePath = ResourceCenter.getInstance()
			.getSysPath()
			+ "resource\\image\\dashBoardGray.png";

	private static final String pingImageTitle = "��ͨ��";

	private static final String pingImageType = "pingdata";

	private static final String memoryImagePath = "resource/image/jfreechart/reportimg/";

	private static final String memoryImageName = "avgpmemory.png";

	private static final String memoryBgImagePath = ResourceCenter
			.getInstance().getSysPath()
			+ "resource\\image\\dashBoard1.png";

	private static final String memoryImageTitle = "�ڴ�������";

	private static final String memoryImageType = "avgpmemory";

	private static final String CPUImagePath = "resource/image/jfreechart/reportimg/";

	private static final String CPUImageName = "cpu.png";

	private static final String CPUMaxImageName = "cpumax.png";

	private static final String CPUAvgImageName = "cpuavg.png";

	private static final String responseTimeImageName = "response.png";

	private static final String responseTimeImagePath = "/resource/image/jfreechart/reportimg/";

	/**
	 * 
	 */
	public H3CFireWallDataService() {
		super();
	}

	/**
	 * @param baseVo
	 */
	public H3CFireWallDataService(BaseVo baseVo) {
		super(baseVo);
	}

	/**
	 * @param nodeDTO
	 */
	public H3CFireWallDataService(NodeDTO nodeDTO) {
		super(nodeDTO);
	}

	/**
	 * @param nodeid
	 * @param type
	 * @param subtype
	 */
	public H3CFireWallDataService(String nodeid, String type, String subtype) {
		super(nodeid, type, subtype);
	}

	/**
	 * ��д����ķ��������������
	 * 
	 * @see com.afunms.data.service.NodeDataService#init(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public void init(String nodeid, String type, String subtype) {
		BaseVo baseVo = null;
		HostNodeDao hostNodeDao = new HostNodeDao();
		try {
			baseVo = (HostNode) hostNodeDao.findByID(nodeid);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			hostNodeDao.close();
		}
		super.init(baseVo);
	}

	public List<InterfaceInfo> getcurAllBandwidthUtilHdxInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		InterfaceInfoService interfaceInfoService = new InterfaceInfoService(
				nodeid, type, subtype);
		String[] subentities = new String[] { "AllInBandwidthUtilHdx",
				"AllOutBandwidthUtilHdx" };
		return interfaceInfoService.getCurrAllInterfaceInfos(subentities);
	}

	/**
	 * getMaxAlarmLevel:
	 * <p>
	 * ��ȡ���ĸ澯�ȼ�
	 * 
	 * @return {@link String} - �澯�ȼ�
	 * 
	 * @since v1.01
	 */
	public String getMaxAlarmLevel() {
		NodeAlarmService nodeAlarmService = new NodeAlarmService();
		int maxAlarmLevel = nodeAlarmService.getMaxAlarmLevel(nodeDTO);
		return String.valueOf(maxAlarmLevel);
	}

	/**
	 * getSupperName:
	 * <p>
	 * ��ȡ��Ӧ������
	 * 
	 * @return {@link String} - ��Ӧ������
	 * 
	 * @since v1.01
	 */
	public String getSupperName() {
		SupperDao supperdao = new SupperDao();
		Supper supper = null;
		String supperName = "";
		try {
			supper = (Supper) supperdao.findByID(((HostNode) getBaseVo())
					.getSupperid()
					+ "");
			if (supper != null)
				supperName = supper.getSu_name() + "(" + supper.getSu_dept()
						+ ")";
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			supperdao.close();
		}
		return supperName;
	}

	/**
	 * getSystemInfo:
	 * <p>
	 * ��ȡϵͳ��Ϣ
	 * 
	 * @return {@link String} - ϵͳ��Ϣ
	 * 
	 * @since v1.01
	 */
	public List<NodeTemp> getSystemInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		return new NetService(nodeid, type, subtype).getSystemInfo();
	}

	/**
	 * getPingInfo:
	 * <p>
	 * ��ȡ��ͨ�ʵ���Ϣ
	 * 
	 * @return {@link List<NodeTemp>} - ��ͨ��
	 * 
	 * @since v1.01
	 */
	public List<NodeTemp> getPingInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		PingInfoService pingInfoService = new PingInfoService(nodeid, type,
				subtype);
		return pingInfoService.getCurPingInfo();
	}

	/**
	 * getPingImageInfo:
	 * <p>
	 * ͨ����ͨ�ʵ�ֵ��ȡ��ͨ��ͼƬ
	 * 
	 * @param value -
	 *            ��ͨ��ֵ
	 * @return {@link String} - ��ͨ��ͼƬ
	 * 
	 * @since v1.01
	 */
	public String getPingImageInfo(String value) {
		if (value == null) {
			value = "0";
		}
		String ip = CommonUtil.doip(getIpaddress());
		String bgImagePath = pingBgImagePath;
		String title = pingImageTitle;
		String type = pingImageType;
		cmp.createChartByParam(ip, value, bgImagePath, title, type);
		String pingImageInfo = pingImagePath + ip + pingImageName;
		return pingImageInfo;
	}

	/**
	 * getMemoryInfo:
	 * <p>
	 * ��ȡ�ڴ���Ϣ
	 * 
	 * @return {@link List<NodeTemp>} - �ڴ���Ϣ
	 * 
	 * @since v1.01
	 */
	public List<NodeTemp> getMemoryInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		MemoryInfoService memoryInfoService = new MemoryInfoService(nodeid,
				type, subtype);
		return memoryInfoService.getCurrPerMemoryListInfo();
	}

	/**
	 * getMemoryImageInfo:
	 * <p>
	 * ͨ���ڴ��ֵ��ȡ�ڴ�ͼƬ��Ϣ
	 * 
	 * @param value -
	 *            �ڴ�
	 * @return {@link String} - �ڴ�ͼƬ
	 * 
	 * @since v1.01
	 */
	public String getMemoryImageInfo(Double value) {
		if (value == null) {
			return null;
		}
		String ip = CommonUtil.doip(getIpaddress());
		String bgImagePath = memoryBgImagePath;
		String title = memoryImageTitle;
		String type = memoryImageType;
		cmp.createPic(ip, value, bgImagePath, title, type);
		String memoryImageInfo = memoryImagePath + ip + memoryImageName;
		return memoryImageInfo;
	}

	/**
	 * getCPUInfo:
	 * <p>
	 * ��ȡ CPU ��Ϣ
	 * 
	 * @return {@link String} - CPU ��Ϣ
	 * 
	 * @since v1.01
	 */
	public String getCPUInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		CpuInfoService cpuInfoService = new CpuInfoService(nodeid, type,
				subtype);
		return cpuInfoService.getCurrCpuAvgInfo();
	}

	/**
	 * getCPUImageInfo:
	 * <p>
	 * ͨ�� CPU ֵ��ȡ CPU ͼƬ
	 * 
	 * @param value -
	 *            CPU ֵ
	 * @return {@link String} - CPU ͼƬ
	 * 
	 * @since v1.01
	 */
	public String getCPUImageInfo(int value) {
		String ip = CommonUtil.doip(getIpaddress());
		String CPUImageInfo = CPUImagePath + ip + CPUImageName;
		cmp.createCpuPic(ip, value);
		return CPUImageInfo;
	}

	// ------------------------------------ Interface Info
	// ------------------------------------------------
	/**
	 * getInterfaceInfo:
	 * <p>
	 * ��ȡ Interface ��Ϣ
	 * 
	 * @param orderFlag -
	 *            �����ֶ�
	 * @return {@link Vector} - Interface ��Ϣ
	 * 
	 * @since v1.01
	 */
	public Vector getInterfaceInfo(String orderFlag) {
		String[] time = { "", "" };
		DateE datemanager = new DateE();
		Calendar current = new GregorianCalendar();
		current.set(Calendar.MINUTE, 59);
		current.set(Calendar.SECOND, 59);
		time[1] = datemanager.getDateDetail(current);
		current.add(Calendar.HOUR_OF_DAY, -1);
		current.set(Calendar.MINUTE, 0);
		current.set(Calendar.SECOND, 0);
		time[0] = datemanager.getDateDetail(current);
		String starttime = time[0];
		String endtime = time[1];
		HostLastCollectDataManager hostlastmanager = new HostLastCollectDataManager();
		String[] netInterfaceItem = { "index", "ifDescr", "ifSpeed",
				"ifOperStatus", "OutBandwidthUtilHdxPerc",
				"InBandwidthUtilHdxPerc", "OutBandwidthUtilHdx",
				"InBandwidthUtilHdx" , "ifPhysAddress"};
		Vector vector = null;
		try {
			vector = hostlastmanager.getInterface(getIpaddress(),
					netInterfaceItem, orderFlag, starttime, endtime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vector;
	}

	// ------------------------------------ Performance Info
	// ------------------------------------------------
	/**
	 * getCurDayPingValueHashtableInfo:
	 * <p>
	 * ��ȡ������ͨ��ֵ�� {@link Hashtable<String, String>} ��Ϣ
	 * 
	 * @return {@link Hashtable<String, String>} - ��ͨ��ֵ��
	 *         {@link Hashtable<String, String>} ��Ϣ
	 * 
	 * @since v1.01
	 */
	public Hashtable<String, String> getCurDayPingValueHashtableInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		PingInfoService pingInfoService = new PingInfoService(nodeid, type,
				subtype);
		return pingInfoService.getCurDayPingValueHashtableInfo(getIpaddress());
	}

	/**
	 * getCurDayResponseTimeValueHashtableInfo:
	 * <p>
	 * ��ȡ������Ӧʱ��ֵ�� {@link Hashtable<String, String>} ��Ϣ
	 * 
	 * @return {@link Hashtable<String, String>} - ��Ӧʱ��ֵ��
	 *         {@link Hashtable<String, String>} ��Ϣ
	 * 
	 * @since v1.01
	 */
	public Hashtable<String, String> getCurDayResponseTimeValueHashtableInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		PingInfoService pingInfoService = new PingInfoService(nodeid, type,
				subtype);
		return pingInfoService
				.getCurDayResponseTimeValueHashtableInfo(getIpaddress());
	}

	/**
	 * getCurDayResponseTimeBarImageInfo:
	 * <p>
	 * ��ȡ������Ӧʱ����״ͼ����Ϣ
	 * 
	 * @param curResponseTimeValue -
	 *            ��ǰ��Ӧʱ��ֵ
	 * @param curDayResponseTimeMaxValue -
	 *            ������Ӧʱ�����ֵ
	 * @param curDayResponseTimeAvgValue -
	 *            ������Ӧʱ��ƽ��ֵ
	 * @return {@link String} - ������Ӧʱ����״ͼ����Ϣ
	 * 
	 * @since v1.01
	 */
	public String getCurDayResponseTimeBarImageInfo(
			String curResponseTimeValue, String curDayResponseTimeMaxValue,
			String curDayResponseTimeAvgValue) {
		String ipaddress = CommonUtil.doip(getIpaddress());
		double[] r_data1 = { new Double(curResponseTimeValue),
				new Double(curDayResponseTimeMaxValue),
				new Double(curDayResponseTimeAvgValue) };
		String[] r_labels = { "��ǰ��Ӧʱ��(ms)", "�����Ӧʱ��(ms)", "ƽ����Ӧʱ��(ms)" };
		TitleModel tm = new TitleModel();
		tm.setPicName(ipaddress + "response");//
		tm.setBgcolor(0xffffff);
		tm.setXpic(450);// ͼƬ����
		tm.setYpic(180);// ͼƬ�߶�
		tm.setX1(30);// �������
		tm.setX2(20);// �������
		tm.setX3(400);// ��ͼ���
		tm.setX4(130);// ��ͼ�߶�
		tm.setX5(10);
		tm.setX6(115);
		CreateBarPic cbp = new CreateBarPic();
		cbp.createTimeBarPic(r_data1, r_labels, tm, 40);
		String curDayResponseTimeBarImage = responseTimeImagePath + ipaddress
				+ responseTimeImageName;
		return curDayResponseTimeBarImage;
	}

	/**
	 * getCurDayCPUValueHashtableInfo:
	 * <p>
	 * ��ȡ���� CPU ֵ�� {@link Hashtable<String, String>} ��Ϣ
	 * 
	 * @return {@link Hashtable<String, String>} - CPU ֵ��
	 *         {@link Hashtable<String, String>} ��Ϣ
	 * 
	 * @since v1.01
	 */
	public Hashtable<String, String> getCurDayCPUValueHashtableInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		CpuInfoService cpuInfoService = new CpuInfoService(nodeid, type,
				subtype);
		return cpuInfoService.getCurDayCPUValueHashtableInfo(getIpaddress());
	}

	/**
	 * getCurDayCPUMaxImageInfo:
	 * <p>
	 * ��ȡ���� CPU ���ֵ��ͼƬ��Ϣ
	 * 
	 * @param value -
	 *            ���� CPU ���ֵ
	 * @return {@link String} - ���� CPU ���ֵ��ͼƬ��Ϣ
	 * 
	 * @since v1.01
	 */
	public String getCurDayCPUMaxImageInfo(String value) {
		String ip = CommonUtil.doip(getIpaddress());
		String CPUImageInfo = CPUImagePath + ip + CPUMaxImageName;
		cmp.createMaxCpuPic(ip, value);
		return CPUImageInfo;
	}

	/**
	 * getCurDayCPUAvgImageInfo:
	 * <p>
	 * ��ȡ���� CPU ƽ��ֵ��ͼƬ��Ϣ
	 * 
	 * @param value -
	 *            ���� CPU ƽ��ֵ
	 * @return {@link String} - ���� CPU ƽ��ֵ��ͼƬ��Ϣ
	 * 
	 * @since v1.01
	 */
	public String getCurDayCPUAvgImageInfo(String value) {
		String ip = CommonUtil.doip(getIpaddress());
		String CPUImageInfo = CPUImagePath + ip + CPUAvgImageName;
		cmp.createAvgCpuPic(ip, value);
		return CPUImageInfo;
	}

	/**
	 * getCurDeviceListInfo:
	 * <p>
	 * ��ȡ��ǰ�豸��Ϣ
	 * 
	 * @return {@link List<DeviceNodeTemp>} - ��ǰ�豸��Ϣ
	 * 
	 * @since v1.01
	 */
	public List<DeviceNodeTemp> getCurDeviceListInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		DeviceInfoService deviceInfoService = new DeviceInfoService(nodeid,
				type, subtype);
		return deviceInfoService.getCurrDeviceInfo();
	}

	/**
	 * getCurAllBandwidthUtilHdxInfo:
	 * <p>
	 * ��ȡ��ǰ�ۺ�������Ϣ
	 * 
	 * @return {@link List<InterfaceInfo>} - ��ǰ�ۺ�������Ϣ
	 * 
	 * @since v1.01
	 */
	public List<InterfaceInfo> getCurAllBandwidthUtilHdxInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		InterfaceInfoService interfaceInfoService = new InterfaceInfoService(
				nodeid, type, subtype);
		String[] subentities = new String[] { "AllInBandwidthUtilHdx",
				"AllOutBandwidthUtilHdx" };
		return interfaceInfoService.getCurrAllInterfaceInfos(subentities);
	}

	/**
	 * getCurDayAllBandwidthUtilHdxHashtable:
	 * <p>
	 * ��ȡ�����ۺ�����ֵ�� {@link Hashtable<String, String>} ��Ϣ
	 * 
	 * @return {@link Hashtable<String, String>} - �����ۺ�����ֵ��
	 *         {@link Hashtable<String, String>} ��Ϣ
	 * 
	 * @since v1.01
	 */
	public Hashtable<String, Integer> getCurDayAllBandwidthUtilHdxHashtable() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		InterfaceInfoService interfaceInfoService = new InterfaceInfoService(
				nodeid, type, subtype);
		return interfaceInfoService
				.getCurDayAllBandwidthUtilHdxHashtableInfo(getIpaddress());
	}

	// ------------------------------------ IPMAC Info
	// ------------------------------------------------
	/**
	 * getCurAllIpMacInfo:
	 * <p>
	 * ��ȡ��ǰ���� IPMAC ��Ϣ
	 * 
	 * @return {@link List<IpMac>} - ��ǰ���� IPMAC ��Ϣ
	 * 
	 * @since v1.01
	 */
	public List<IpMac> getCurAllIpMacInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		IpMacInfoService interfaceInfoService = new IpMacInfoService(nodeid,
				type, subtype);
		return interfaceInfoService.getCurrAllIpMacInfo(getIpaddress());
	}

	// ------------------------------------ Router Info
	// ------------------------------------------------
	/**
	 * getCurRouterInfo:
	 * <p>
	 * ��ȡ��ǰ·����Ϣ
	 * 
	 * @return {@link List<RouterNodeTemp>} - ·����Ϣ
	 * 
	 * @since v1.01
	 */
	public List<RouterNodeTemp> getCurRouterInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		RouterInfoService routerInfoService = new RouterInfoService(nodeid,
				type, subtype);
		return routerInfoService.getCurrAllRouterInfo();
	}

	// ------------------------------------ IpList Info
	// ------------------------------------------------
	/**
	 * getCurIpListInfo:
	 * <p>
	 * ��ȡ��ǰ ipList ��Ϣ
	 * 
	 * @return {@link List<IpAlias>} - ��ǰ ipList ��Ϣ
	 * 
	 * @since v1.01
	 */
	public List<IpAlias> getCurIpListInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		IpListInfoService ipListInfoService = new IpListInfoService(nodeid,
				type, subtype);
		return ipListInfoService.getCurrAllIpListInfo(getIpaddress());
	}
}
