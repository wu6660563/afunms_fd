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

public class NetDataService extends NodeDataService {

    private static final SysLogger logger = SysLogger
            .getLogger(NetDataService.class.getName());

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
    /**
     * 
     */
    public NetDataService() {
        super();
    }

    /**
     * @param baseVo
     */
    public NetDataService(BaseVo baseVo) {
        super(baseVo);
    }

    /**
     * @param nodeDTO
     */
    public NetDataService(NodeDTO nodeDTO) {
        super(nodeDTO);
    }

    /**
     * @param nodeid
     * @param type
     * @param subtype
     */
    public NetDataService(String nodeid, String type, String subtype) {
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
        return pingInfoService.getCurDayResponseTimeValueHashtableInfo(getIpaddress());
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

    public Vector getInterfaceInfo(String orderFlag) {
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
                "InBandwidthUtilHdx" , "ifPhysAddress" };
        Vector vector = null;
        try {
            vector = hostlastmanager.getInterface(getIpaddress(),
                    netInterfaceItem, orderFlag, starttime, endtime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vector;
    }

    public String getCurDayResponseTimeBarImageInfo(String curResponseTimeValue,
            String curDayResponseTimeMaxValue,
            String curDayResponseTimeAvgValue) {
        String ipaddress = CommonUtil.doip(getIpaddress());
        double[] r_data1 = { new Double(curResponseTimeValue),
                new Double(curDayResponseTimeMaxValue), new Double(curDayResponseTimeAvgValue) };
        String[] r_labels = { "当前响应时间(ms)", "最大响应时间(ms)", "平均响应时间(ms)" };
        TitleModel tm = new TitleModel();
        tm.setPicName(ipaddress + "response");//
        tm.setBgcolor(0xffffff);
        tm.setXpic(450);//图片长度
        tm.setYpic(180);//图片高度
        tm.setX1(30);//左面距离
        tm.setX2(20);//上面距离
        tm.setX3(400);//内图宽度
        tm.setX4(130);//内图高度
        tm.setX5(10);
        tm.setX6(115);
        CreateBarPic cbp = new CreateBarPic();
        cbp.createTimeBarPic(r_data1, r_labels, tm, 40);
        String curDayResponseTimeBarImage = responseTimeImagePath + ipaddress + responseTimeImageName;
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
        DeviceInfoService deviceInfoService = new DeviceInfoService(nodeid, type,
                subtype);
        return deviceInfoService.getCurrDeviceInfo();
    }

    public List<InterfaceInfo> getcurAllBandwidthUtilHdxInfo() {
        String nodeid = getNodeid();
        String type = getType();
        String subtype = getSubtype();
        InterfaceInfoService interfaceInfoService = new InterfaceInfoService(nodeid, type,
                subtype);
        String[] subentities = new String[]{
            "AllInBandwidthUtilHdx", "AllOutBandwidthUtilHdx"
        };
        return interfaceInfoService.getCurrAllInterfaceInfos(subentities);
    }

    public Hashtable<String, Integer> getCurDayAllBandwidthUtilHdxHashtable() {
        String nodeid = getNodeid();
        String type = getType();
        String subtype = getSubtype();
        InterfaceInfoService interfaceInfoService = new InterfaceInfoService(nodeid, type,
                subtype);
        return interfaceInfoService.getCurDayAllBandwidthUtilHdxHashtableInfo(getIpaddress());
    }

    public List<IpMac> getCurAllIpMacInfo() {
        String nodeid = getNodeid();
        String type = getType();
        String subtype = getSubtype();
        IpMacInfoService interfaceInfoService = new IpMacInfoService(nodeid, type,
                subtype);
        return interfaceInfoService.getCurrAllIpMacInfo(getIpaddress());
    }

    public List<RouterNodeTemp> getCurRouterInfo() {
        String nodeid = getNodeid();
        String type = getType();
        String subtype = getSubtype();
        RouterInfoService routerInfoService = new RouterInfoService(nodeid, type,
                subtype);
        return routerInfoService.getCurrAllRouterInfo();
    }

    public List<IpAlias> getCurIpListInfo() {
        String nodeid = getNodeid();
        String type = getType();
        String subtype = getSubtype();
        IpListInfoService ipListInfoService = new IpListInfoService(nodeid, type,
                subtype);
        return ipListInfoService.getCurrAllIpListInfo(getIpaddress());
    }
}
