/*
 * @(#)HpunixDataService.java     v1.01, Apr 2, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.data.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.alarm.service.NodeAlarmService;
import com.afunms.common.base.BaseVo;
import com.afunms.common.util.CommonUtil;
import com.afunms.common.util.CreateAmColumnPic;
import com.afunms.common.util.CreateBarPic;
import com.afunms.common.util.CreateMetersPic;
import com.afunms.common.util.DateE;
import com.afunms.common.util.MeterModel;
import com.afunms.common.util.StageColor;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.TitleModel;
import com.afunms.config.dao.NodeconfigDao;
import com.afunms.config.dao.NodecpuconfigDao;
import com.afunms.config.dao.SupperDao;
import com.afunms.config.model.IpAlias;
import com.afunms.config.model.Nodeconfig;
import com.afunms.config.model.Nodecpuconfig;
import com.afunms.config.model.Supper;
import com.afunms.detail.net.service.NetService;
import com.afunms.detail.reomte.model.InterfaceInfo;
import com.afunms.detail.service.OtherInfo.OtherInfoService;
import com.afunms.detail.service.configInfo.NetmediaConfigInfoService;
import com.afunms.detail.service.cpuInfo.CpuInfoService;
import com.afunms.detail.service.deviceInfo.DeviceInfoService;
import com.afunms.detail.service.diskInfo.DiskInfoService;
import com.afunms.detail.service.interfaceInfo.InterfaceInfoService;
import com.afunms.detail.service.ipListInfo.IpListInfoService;
import com.afunms.detail.service.ipMacInfo.IpMacInfoService;
import com.afunms.detail.service.memoryInfo.MemoryInfoService;
import com.afunms.detail.service.pingInfo.PingInfoService;
import com.afunms.detail.service.routerInfo.RouterInfoService;
import com.afunms.detail.service.serviceInfo.ServiceInfoService;
import com.afunms.detail.service.sofwareInfo.SoftwareInfoService;
import com.afunms.detail.service.storageInfo.StorageInfoService;
import com.afunms.detail.service.sysInfo.PageSpaceInfoService;
import com.afunms.detail.service.userInfo.UserInfoService;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.IpMac;
import com.afunms.polling.om.Usercollectdata;
import com.afunms.temp.dao.OthersTempDao;
import com.afunms.temp.model.DeviceNodeTemp;
import com.afunms.temp.model.NodeTemp;
import com.afunms.temp.model.RouterNodeTemp;
import com.afunms.temp.model.SoftwareNodeTemp;
import com.afunms.temp.model.StorageNodeTemp;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;

/**
 * 
 * ClassName: HpunixDataService.java
 * <p>
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Apr 2, 2013 11:40:01 AM
 */
public class HpunixDataService extends NodeDataService {

	private static final SysLogger logger = SysLogger
			.getLogger(HpunixDataService.class.getName());

	private static CreateMetersPic cmp = new CreateMetersPic();

	private static final String pingImagePath = "resource/image/jfreechart/reportimg/";

	private static final String pingImageName = "pingdata.png";

	private static final String pingBgImagePath = ResourceCenter.getInstance()
			.getSysPath()
			+ "resource\\image\\dashBoardGray.png";

	private static final String pingImageTitle = "连通率";

	private static final String pingImageType = "pingdata";

	private static final String memoryImagePath = "resource/image/jfreechart/reportimg/";

	private static final String memoryImageName = "avgpmemory.png";

	private static final String memoryBgImagePath = ResourceCenter
			.getInstance().getSysPath()
			+ "resource\\image\\dashBoard1.png";

	private static final String memoryImageTitle = "内存利用率";

	private static final String memoryImageType = "avgpmemory";

	private static final String CPUImagePath = "resource/image/jfreechart/reportimg/";

	private static final String CPUImageName = "cpu.png";

	private static final String CPUMaxImageName = "cpumax.png";

	private static final String CPUAvgImageName = "cpuavg.png";

	private static final String responseTimeImageName = "response.png";

	private static final String responseTimeImagePath = "/resource/image/jfreechart/reportimg/";

	private static final String curPageImageName = "pageout";

	private static final String curDayPageMaxImageName = "pageoutmax";

	private static final String curDayPageAvgImageName = "pageoutavg";

	private static final String imageFileSuffix = ".png";

	private static final String pageImagePath = "resource/image/jfreechart/reportimg/";

	/**
	 * 
	 */
	public HpunixDataService() {
		super();
	}

	/**
	 * @param baseVo
	 */
	public HpunixDataService(BaseVo baseVo) {
		super(baseVo);
	}

	/**
	 * @param nodeDTO
	 */
	public HpunixDataService(NodeDTO nodeDTO) {
		super(nodeDTO);
	}

	/**
	 * @param nodeid
	 * @param type
	 * @param subtype
	 */
	public HpunixDataService(String nodeid, String type, String subtype) {
		super(nodeid, type, subtype);
	}

	/**
	 * 重写父类的方法用于提高性能
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

	public List<NodeTemp> getSystemInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		return new NetService(nodeid, type, subtype).getSystemInfo();

	}

	public String getMaxAlarmLevel() {
		NodeAlarmService nodeAlarmService = new NodeAlarmService();
		int maxAlarmLevel = nodeAlarmService.getMaxAlarmLevel(nodeDTO);
		return String.valueOf(maxAlarmLevel);
	}

	public String getPingCurDayAvgInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		PingInfoService pingInfoService = new PingInfoService(nodeid, type,
				subtype);
		return pingInfoService.getCurrDayPingAvgInfo(getIpaddress());
	}

	public Hashtable<String, String> getCurDayPingValueHashtableInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		PingInfoService pingInfoService = new PingInfoService(nodeid, type,
				subtype);
		return pingInfoService.getCurDayPingValueHashtableInfo(getIpaddress());
	}

	public Hashtable<String, String> getCurDayResponseTimeValueHashtableInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		PingInfoService pingInfoService = new PingInfoService(nodeid, type,
				subtype);
		return pingInfoService
				.getCurDayResponseTimeValueHashtableInfo(getIpaddress());
	}

	public String getResponseTimeCurDayAvgInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		PingInfoService pingInfoService = new PingInfoService(nodeid, type,
				subtype);
		return pingInfoService.getCurDayResponseTimeAvgInfo(getIpaddress());
	}

	public List<NodeTemp> getMemoryInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		MemoryInfoService memoryInfoService = new MemoryInfoService(nodeid,
				type, subtype);
		return memoryInfoService.getCurrPerMemoryListInfo();
	}

	public String getCPUInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		CpuInfoService cpuInfoService = new CpuInfoService(nodeid, type,
				subtype);
		return cpuInfoService.getCurrCpuAvgInfo();
	}

	public List<NodeTemp> getPingInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		PingInfoService pingInfoService = new PingInfoService(nodeid, type,
				subtype);
		return pingInfoService.getCurPingInfo();
	}

	public String getPingCurDayAvgImageInfo() {
		String value = getPingCurDayAvgInfo();
		return getPingImageInfo(value);
	}

	public String getPingImageInfo(String value) {
		if (value == null) {
			return null;
		}
		String ip = CommonUtil.doip(getIpaddress());
		String bgImagePath = pingBgImagePath;
		String title = pingImageTitle;
		String type = pingImageType;
		cmp.createChartByParam(ip, value, bgImagePath, title, type);
		String pingImageInfo = pingImagePath + ip + pingImageName;
		return pingImageInfo;
	}

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

	public String getCPUImageInfo(int value) {
		String ip = CommonUtil.doip(getIpaddress());
		String CPUImageInfo = CPUImagePath + ip + CPUImageName;
		cmp.createCpuPic(ip, value);
		return CPUImageInfo;
	}

	public String getCurDayCPUMaxImageInfo(String value) {
		String ip = CommonUtil.doip(getIpaddress());
		String CPUImageInfo = CPUImagePath + ip + CPUMaxImageName;
		cmp.createMaxCpuPic(ip, value);
		return CPUImageInfo;
	}

	public String getCurDayCPUAvgImageInfo(String value) {
		String ip = CommonUtil.doip(getIpaddress());
		String CPUImageInfo = CPUImagePath + ip + CPUAvgImageName;
		cmp.createAvgCpuPic(ip, value);
		return CPUImageInfo;
	}

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

	public Nodeconfig getNodeConfig() {
		Nodeconfig nodeconfig = null;
		NodeconfigDao nodeconfigDao = new NodeconfigDao();
		try {
			nodeconfig = nodeconfigDao.getByNodeID(getNodeid());
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			nodeconfigDao.close();
		}
		if (nodeconfig == null) {
			nodeconfig = new Nodeconfig();
		}
		return nodeconfig;
	}

	public Hashtable<String, String> getCurPageValueHashtable() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		OtherInfoService otherInfoService = new OtherInfoService(nodeid, type,
				subtype);
		return otherInfoService.getPaginghash();
	}

	public Vector getInterfaceInfo(String orderFlag, String sorttype) {
		String[] time = {"",""};
        DateE datemanager = new DateE();
        Calendar current = new GregorianCalendar();
        current.set(Calendar.MINUTE,59);
        current.set(Calendar.SECOND,59);
        time[1] = datemanager.getDateDetail(current);
        current.add(Calendar.HOUR_OF_DAY,-1);
        current.set(Calendar.MINUTE,0);
        current.set(Calendar.SECOND,0);
        time[0] = datemanager.getDateDetail(current);
        String starttime = time[0];
        String endtime = time[1];
        HostLastCollectDataManager hostlastmanager = new HostLastCollectDataManager();
        String[] netInterfaceItem = { "index", "ifDescr", "ifSpeed",
                "ifOperStatus", "OutBandwidthUtilHdxPerc",
                "InBandwidthUtilHdxPerc", "OutBandwidthUtilHdx",
                "InBandwidthUtilHdx" };
        Vector vector = null;
        try {
            vector = hostlastmanager.getInterface(getIpaddress(),
                    netInterfaceItem, orderFlag, starttime, endtime);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return vector;
	}

	public String getCurDayResponseTimeBarImageInfo(
			String curResponseTimeValue, String curDayResponseTimeMaxValue,
			String curDayResponseTimeAvgValue) {
		String ipaddress = CommonUtil.doip(getIpaddress());
		double[] r_data1 = { new Double(curResponseTimeValue),
				new Double(curDayResponseTimeMaxValue),
				new Double(curDayResponseTimeAvgValue) };
		String[] r_labels = { "当前响应时间(ms)", "最大响应时间(ms)", "平均响应时间(ms)" };
		TitleModel tm = new TitleModel();
		tm.setPicName(ipaddress + "response");//
		tm.setBgcolor(0xffffff);
		tm.setXpic(450);// 图片长度
		tm.setYpic(180);// 图片高度
		tm.setX1(30);// 左面距离
		tm.setX2(20);// 上面距离
		tm.setX3(400);// 内图宽度
		tm.setX4(130);// 内图高度
		tm.setX5(10);
		tm.setX6(115);
		CreateBarPic cbp = new CreateBarPic();
		cbp.createTimeBarPic(r_data1, r_labels, tm, 40);
		String curDayResponseTimeBarImage = responseTimeImagePath + ipaddress
				+ responseTimeImageName;
		return curDayResponseTimeBarImage;
	}

	public Hashtable<String, String> getCurDayCPUValueHashtableInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		CpuInfoService cpuInfoService = new CpuInfoService(nodeid, type,
				subtype);
		return cpuInfoService.getCurDayCPUValueHashtableInfo(getIpaddress());
	}

	public List<DeviceNodeTemp> getCurDeviceListInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		DeviceInfoService deviceInfoService = new DeviceInfoService(nodeid,
				type, subtype);
		return deviceInfoService.getCurrDeviceInfo();
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

	public Hashtable<String, Integer> getCurDayAllBandwidthUtilHdxHashtable() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		InterfaceInfoService interfaceInfoService = new InterfaceInfoService(
				nodeid, type, subtype);
		return interfaceInfoService
				.getCurDayAllBandwidthUtilHdxHashtableInfo(getIpaddress());
	}

	public List<IpMac> getCurAllIpMacInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		IpMacInfoService interfaceInfoService = new IpMacInfoService(nodeid,
				type, subtype);
		return interfaceInfoService.getCurrAllIpMacInfo(getIpaddress());
	}

	public List<RouterNodeTemp> getCurRouterInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		RouterInfoService routerInfoService = new RouterInfoService(nodeid,
				type, subtype);
		return routerInfoService.getCurrAllRouterInfo();
	}

	public List<IpAlias> getCurIpListInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		IpListInfoService ipListInfoService = new IpListInfoService(nodeid,
				type, subtype);
		return ipListInfoService.getCurrAllIpListInfo(getIpaddress());
	}

	public String getAmMemoryChartInfo(
			Hashtable<Integer, Hashtable<String, String>> memoryValueHashtable) {
		CreateAmColumnPic createAmColumnPic = new CreateAmColumnPic();
		String amMemoryChartInfo = createAmColumnPic.createAmMemoryChart(
				getIpaddress(), memoryValueHashtable);
		return amMemoryChartInfo;
	}

	public Hashtable<Integer, Hashtable<String, String>> getCurDayMemoryValueHashtable() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		MemoryInfoService memoryInfoService = new MemoryInfoService(nodeid,
				type, subtype);
		return memoryInfoService
				.getCurDayMemoryValueHashtableInfo(getIpaddress());
	}

	public Hashtable[] getCurDayAllMemoryValueHashtable() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		MemoryInfoService memoryInfoService = new MemoryInfoService(nodeid,
				type, subtype);
		return memoryInfoService
				.getCurDayAllMemoryValueHashtableInfo(getIpaddress());
	}

	public String getAmDiskChartInfo(
			Hashtable<Integer, Hashtable<String, String>> diskValueHashtable) {
		CreateAmColumnPic createAmColumnPic = new CreateAmColumnPic();
		String amDiskChartInfo = createAmColumnPic
				.createDiskChartTop5(diskValueHashtable);
		return amDiskChartInfo;
	}

	public Hashtable<Integer, Hashtable<String, String>> getCurDayDiskValueHashtable() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		DiskInfoService diskInfoService = new DiskInfoService(nodeid, type,
				subtype);
		return diskInfoService.getCurDayDiskValueHashtableInfo(getIpaddress());
	}

	public Hashtable<String, String> getCurCPUPerValueHashtable() {
		OthersTempDao tempdao = new OthersTempDao();
		List _cpuperflist = null;
		try {
			_cpuperflist = tempdao.getCpuPerfInfoList(getIpaddress());
		} catch (Exception e) {

		} finally {
			tempdao.close();
		}
		Hashtable<String, String> curCPUPerValueHashtable = new Hashtable<String, String>();
		if (_cpuperflist != null && _cpuperflist.size() > 0) {
			for (int si = 0; si < _cpuperflist.size(); si++) {
				CPUcollectdata cpudata = (CPUcollectdata) _cpuperflist.get(si);
				curCPUPerValueHashtable.put(cpudata.getSubentity(), cpudata
						.getThevalue());
			}
		}
		return curCPUPerValueHashtable;
	}

	public Hashtable<String, Hashtable<String, String>> getCurCPUDetailValueHashtable() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		CpuInfoService cpuInfoService = new CpuInfoService(nodeid, type,
				subtype);
		return cpuInfoService.getCPUDetailValueHashtableInfo(getIpaddress());
	}

	public String getCurCPUDetailAmChartInfo(
			Hashtable<String, String> curCPUPerValueHashtable,
			Hashtable<String, Hashtable<String, String>> curCPUDetailValueHashtable) {
		CreateAmColumnPic cpudetail = new CreateAmColumnPic();
		return cpudetail.createCpuDetailAmChart(curCPUPerValueHashtable,
				curCPUDetailValueHashtable);
	}

	public Hashtable<String, String> getCurPageDetailHashtable() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		PageSpaceInfoService pageSpaceInfoService = new PageSpaceInfoService(
				nodeid, type, subtype);
		return pageSpaceInfoService
				.getCurDayPageDetailValueHashtableInfo(getIpaddress());
	}

	public String getCurPageValueImageInfo(String thevalue) {
		if (thevalue == null) {
			thevalue = "0";
		}
		thevalue.replaceAll("%", "");
		String ip = CommonUtil.doip(getIpaddress());
		String imageName = ip + curPageImageName;
		createSimpleMeter(imageName, Double.valueOf(thevalue));
		return pageImagePath + imageName + imageFileSuffix;
	}

	public String getCurDayPageMaxValueImageInfo(String thevalue) {
		if (thevalue == null) {
			thevalue = "0";
		}
		thevalue.replaceAll("%", "");
		String ip = CommonUtil.doip(getIpaddress());
		String imageName = ip + curDayPageMaxImageName;
		createSimpleMeter(imageName, Double.valueOf(thevalue));
		return pageImagePath + imageName + imageFileSuffix;
	}

	public String getCurDayPageAvgValueImageInfo(String thevalue) {
		if (thevalue == null) {
			thevalue = "0";
		}
		thevalue.replaceAll("%", "");
		String ip = CommonUtil.doip(getIpaddress());
		String imageName = ip + curDayPageAvgImageName;
		createSimpleMeter(imageName, Double.valueOf(thevalue));
		return pageImagePath + imageName + imageFileSuffix;
	}

	public Hashtable<Integer, Hashtable<String, String>> getCurDiskValueHashtableInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		DiskInfoService diskInfoService = new DiskInfoService(nodeid, type,
				subtype);
		return diskInfoService.getCurrDiskListInfo();
	}

	public String getCurDiskAmChartInfo(
			Hashtable<Integer, Hashtable<String, String>> curDiskValueHashtableInfo) {
		CreateAmColumnPic aixColumnPic = new CreateAmColumnPic();
		return aixColumnPic.createDiskChart(curDiskValueHashtableInfo);
	}

	public List<Hashtable<String, String>> getCurDiskPerfInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		DiskInfoService diskInfoService = new DiskInfoService(nodeid, type,
				subtype);
		return diskInfoService.getDiskperflistInfo();
	}

	public List<Hashtable<String, String>> getCurDiskIoInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		DiskInfoService diskInfoService = new DiskInfoService(nodeid, type,
				subtype);
		return diskInfoService.getDiskperflistInfo();
	}

	public Hashtable<String, String> getCurPagePerfInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		OtherInfoService otherInfoService = new OtherInfoService(nodeid, type,
				subtype);
		return otherInfoService.getPagehash();
	}

	public List<Nodecpuconfig> getCurCPUConfigInfo() {
		List<Nodecpuconfig> curCPUConfigList = null;
		String nodeid = getNodeid();
		NodecpuconfigDao cpucoNodecpuconfigDao = new NodecpuconfigDao();
		try {
			curCPUConfigList = cpucoNodecpuconfigDao.getNodecpuconfig(nodeid);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cpucoNodecpuconfigDao.close();
		}
		return curCPUConfigList;
	}

	public List<Hashtable<String, String>> getCurNetmediaConfigInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		NetmediaConfigInfoService netmediaConfigInfoService = new NetmediaConfigInfoService(
				nodeid, type, subtype);
		return netmediaConfigInfoService.getNetmediaConfigInfo();
	}

	public Vector<Usercollectdata> getCurUserConfigInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		UserInfoService userInfoService = new UserInfoService(nodeid, type,
				subtype);
		return userInfoService.getUserInfo();
	}

	public List<SoftwareNodeTemp> getSoftwareInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		SoftwareInfoService softwareInfoService = new SoftwareInfoService(
				nodeid, type, subtype);
		return softwareInfoService.getCurrSoftwareInfo();
	}

	public List<Hashtable<String, String>> getServiceInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		ServiceInfoService serviceInfoService = new ServiceInfoService(nodeid,
				type, subtype);
		return serviceInfoService.getServicelistInfo();
	}

	public List<DeviceNodeTemp> getDeviceInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		DeviceInfoService deviceInfoService = new DeviceInfoService(nodeid,
				type, subtype);
		return deviceInfoService.getCurrDeviceInfo();
	}

	public List<StorageNodeTemp> getStorageInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		StorageInfoService storageInfoService = new StorageInfoService(nodeid,
				type, subtype);
		return storageInfoService.getCurrStorageInfo();
	}

	public void createSimpleMeter(String imageName, Double thevalue) {
		cmp = new CreateMetersPic();
		MeterModel mm = new MeterModel();
		mm.setBgColor(0xffffff);
		mm.setInnerRoundColor(0xececec);
		mm.setOutRingColor(0x80ff80);
		mm.setTitle("换页率");
		mm.setPicx(150);//
		mm.setPicy(150);//
		mm.setMeterX(80);//
		mm.setMeterY(80);//
		mm.setPicName(imageName);//
		mm.setValue(thevalue);//
		mm.setMeterSize(60);// 设置仪表盘大小
		mm.setTitleY(79);// 设置标题离左边距离
		mm.setTitleTop(122);// 设置标题离顶部距离
		mm.setValueY(78);// 设置值离左边距离
		mm.setValueTop(105);// 设置值离顶部距离
		mm.setOutPointerColor(0x80ff80);// 设置指针外部颜色
		mm.setInPointerColor(0x8080ff);// 设置指针内部颜色
		mm.setFontSize(10);// 设置字体大小
		List<StageColor> sm = new ArrayList<StageColor>();
		StageColor sc1 = new StageColor();
		sc1.setColor(0x99ff99);
		sc1.setStart(0);
		sc1.setEnd(60);
		StageColor sc2 = new StageColor();
		sc2.setColor(0xffff00);
		sc2.setStart(60);
		sc2.setEnd(80);
		StageColor sc3 = new StageColor();
		sc3.setColor(0xff3333);
		sc3.setStart(80);
		sc3.setEnd(100);
		sm.add(sc1);
		sm.add(sc2);
		sm.add(sc3);
		mm.setList(sm);
		cmp.createSimpleMeter(mm);
	}
}
