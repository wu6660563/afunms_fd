/**
 * <p>Description:DiscoverManager</p>
 * <p>Company: dhcc.com</p>
 * @author nielin
 * @project afunms
 * @date 2010-12-08
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
import com.afunms.config.model.Portconfig;
import com.afunms.data.service.NetDataService;
import com.afunms.data.service.WindowsDataService;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.dao.SyslogDao;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.polling.api.I_HostCollectData;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.om.IpMac;
import com.afunms.polling.om.ProcessInfo;
import com.afunms.system.model.User;
import com.afunms.temp.model.DeviceNodeTemp;
import com.afunms.temp.model.NodeTemp;
import com.afunms.temp.model.RouterNodeTemp;
import com.afunms.temp.model.ServiceNodeTemp;
import com.afunms.temp.model.SoftwareNodeTemp;
import com.afunms.temp.model.StorageNodeTemp;
import com.afunms.topology.model.HostNode;

/**
 * �� manager ���ڽ����豸��ϸ��Ϣҳ����ת
 */
public class WindowsDataManager extends NodeDataManager {

    private static final SysLogger sysLogger = SysLogger
            .getLogger(WindowsDataManager.class.getName());

    public String execute(String action) {
        if ("getInterfaceInfo".equals(action)) {
            return getInterfaceInfo();
        } else if ("getPerformanceInfo".equals(action)) {
            return getPerformanceInfo();
        } else if ("getProcessInfo".equals(action)) {
            return getProcessInfo();
        }  else if ("getArpInfo".equals(action)) {
            return getArpInfo();
        } else if ("getSoftwareInfo".equals(action)) {
            return getSoftwareInfo();
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
        String nodeid = getNodeid();
        String type = getType();
        String subtype = getSubtype();
        WindowsDataService windowsDataService = new WindowsDataService(nodeid, type,
                subtype);
        String orderFlag = getParaValue("orderflag");
        if (orderFlag == null || orderFlag.trim().length() == 0) {
            orderFlag = "index";
        }
        Vector interfaceInfo = windowsDataService.getInterfaceInfo(orderFlag);
        
        //wupinlong add 2013/7/9 �˿ڹ���Ӧ��
        PortconfigDao portconfigDao = new PortconfigDao();
        List<Portconfig> list = null;
        try {
        	list = portconfigDao.loadByIpaddress(windowsDataService.getNodeDTO().getIpaddress());
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
        request.setAttribute("interfaceInfo", interfaceInfo);
        return "/detail/host/windows/interface.jsp";
    }
    

    public String getPerformanceInfo() {
        getBaseInfo();
        String nodeid = getNodeid();
        String type = getType();
        String subtype = getSubtype();
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

        WindowsDataService windowsDataService = new WindowsDataService(nodeid,
                type, subtype);

        curDayDiskValueHashtable = (Hashtable<Integer, Hashtable<String, String>>) request
                .getAttribute("curDayDiskValueHashtable");
        curDayAmWindowsDiskChartInfo = windowsDataService
                .getAmWindowsDiskChartInfo(curDayDiskValueHashtable);
        Hashtable[] curDayAllMemoryValuHashtables = windowsDataService
                .getCurDayAllMemoryValueHashtable();
        curDayMaxMemoryValueHashtable = curDayAllMemoryValuHashtables[1];
        curDayAvgMemoryValueHashtable = curDayAllMemoryValuHashtables[2];

        Hashtable<String, String> curDayPingValuHashtable = windowsDataService
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
        Hashtable<String, String> curDayResponseTimeValueHashtable = windowsDataService
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
        curDayResponseTimeBarImage = windowsDataService
                .getCurDayResponseTimeBarImageInfo(curResponseTimeValue,
                        curDayResponseTimeMaxValue, curDayResponseTimeAvgValue);

        // ��ʼ��ȡCPU��ͼƬ
        Hashtable<String, String> curDayCPUValueHashtable = windowsDataService
                .getCurDayCPUValueHashtableInfo();

        curDayCPUMaxValue = curDayCPUValueHashtable.get("max");
        if (curDayCPUMaxValue == null || curDayCPUMaxValue.trim().length() == 0) {
            curDayCPUMaxValue = "0";
        }
        curDayCPUMaxValue = curDayCPUMaxValue.replaceAll("%", "").replaceAll("����", "");
        curDayCPUMaxValue = String.valueOf(Math.round(Double
                .valueOf(curDayCPUMaxValue)));
        curDayCPUMaxImageInfo = windowsDataService
                .getCurDayCPUMaxImageInfo(curDayCPUMaxValue);

        curDayCPUAvgValue = curDayCPUValueHashtable.get("avgcpucon");
        if (curDayCPUAvgValue == null || curDayCPUAvgValue.trim().length() == 0) {
            curDayCPUAvgValue = "0";
        }
        curDayCPUAvgValue = curDayCPUAvgValue.replaceAll("%", "");
        curDayCPUAvgValue = String.valueOf(Math.round(Double
                .valueOf(curDayCPUAvgValue)));
        curDayCPUAvgImageInfo = windowsDataService
                .getCurDayCPUAvgImageInfo(curDayCPUAvgValue);
        
        // ��ȡ��ǰ�豸��Ϣ�б�
        currDeviceNodeTempList = windowsDataService.getCurDeviceListInfo();

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

        request.setAttribute("curDayAmWindowsDiskChartInfo",
                curDayAmWindowsDiskChartInfo);

        request.setAttribute("curDayMaxMemoryValueHashtable",
                curDayMaxMemoryValueHashtable);
        request.setAttribute("curDayAvgMemoryValueHashtable",
                curDayAvgMemoryValueHashtable);
        return "/detail/host/windows/performance.jsp";
    }

    public String getArpInfo() {
        getBaseInfo();
        String nodeid = getNodeid();
        String type = getType();
        String subtype = getSubtype();
        NetDataService netDataService = new NetDataService(nodeid, type,
                subtype);
        List<IpMac> ipMacList = netDataService.getCurAllIpMacInfo();
        request.setAttribute("ipMacList", ipMacList);
        return "/detail/host/windows/arp.jsp";
    }

    public String getSoftwareInfo() {
        getBaseInfo();
        String nodeid = getNodeid();
        String type = getType();
        String subtype = getSubtype();
        WindowsDataService windowsDataService = new WindowsDataService(nodeid, type,
                subtype);
        List<SoftwareNodeTemp> softwareNodeTempList = windowsDataService.getSoftwareInfo();
        request.setAttribute("softwareNodeTempList", softwareNodeTempList);
        return "/detail/host/windows/software.jsp";
    }

    public String getServiceInfo() {
        getBaseInfo();
        String nodeid = getNodeid();
        String type = getType();
        String subtype = getSubtype();
        WindowsDataService windowsDataService = new WindowsDataService(nodeid, type,
                subtype);
        List<ServiceNodeTemp> serviceNodeTempList = windowsDataService.getServiceInfo();
        request.setAttribute("serviceNodeTempList", serviceNodeTempList);
        return "/detail/host/windows/service.jsp";
    }

    public String getDeviceInfo() {
        getBaseInfo();
        String nodeid = getNodeid();
        String type = getType();
        String subtype = getSubtype();
        WindowsDataService windowsDataService = new WindowsDataService(nodeid, type,
                subtype);
        List<DeviceNodeTemp> deviceNodeTempList = windowsDataService.getDeviceInfo();
        request.setAttribute("deviceNodeTempList", deviceNodeTempList);
        return "/detail/host/windows/device.jsp";
    }

    public String getStorageInfo() {
        getBaseInfo();
        String nodeid = getNodeid();
        String type = getType();
        String subtype = getSubtype();
        WindowsDataService windowsDataService = new WindowsDataService(nodeid, type,
                subtype);
        List<StorageNodeTemp> storageNodeTempList = windowsDataService.getStorageInfo();
        request.setAttribute("storageNodeTempList", storageNodeTempList);
        return "/detail/host/windows/storage.jsp";
    }

    public String getRouterInfo() {
        getBaseInfo();
        String nodeid = getNodeid();
        String type = getType();
        String subtype = getSubtype();
        NetDataService netDataService = new NetDataService(nodeid, type,
                subtype);
        List<RouterNodeTemp> routerNodeTempList = netDataService
                .getCurRouterInfo();
        request.setAttribute("routerNodeTempList", routerNodeTempList);
        return "/detail/net/router.jsp";
    }

    public String getIpListInfo() {
        getBaseInfo();
        String nodeid = getNodeid();
        String type = getType();
        String subtype = getSubtype();
        NetDataService netDataService = new NetDataService(nodeid, type,
                subtype);
        List<IpAlias> ipAliasList = netDataService.getCurIpListInfo();
        request.setAttribute("ipAliasList", ipAliasList);
        return "/detail/net/iplist.jsp";
    }

    public String getSyslogInfo() {
        getBaseInfo();
        String nodeid = getNodeid();
        String type = getType();
        String subtype = getSubtype();
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
        if(priorityname == null) {
            priorityname = "all";
        }
        NodeDTO node = (NodeDTO) request.getAttribute("node");
        SyslogDao syslogdao = new SyslogDao();
        try{
            list = syslogdao.getQuery(node.getIpaddress(), starttime, totime, priorityname);
        }catch(Exception e){
            e.printStackTrace();
        }
        request.setAttribute("list", list);
        request.setAttribute("priorityname", priorityname);
        request.setAttribute("startdate", startdate);
        request.setAttribute("todate", todate);
        return "/detail/host/windows/syslog.jsp";
    }

    public String getEventInfo() {
        getBaseInfo();
        String nodeid = getNodeid();
        String type = getType();
        String subtype = getSubtype();
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
        return "/detail/host/windows/event.jsp";
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
        String curVirtualMemoryValue = "0"; // ��ǰ�����ڴ�ֵ
        String curCPUValue = "0"; // ��ǰCPUֵ
        String curPingImage = ""; // ��ǰ��ͨ��ͼƬ
        String curPhysicalMemoryImage = ""; // ��ǰ�ڴ�ͼƬ
        String curCPUImage = ""; // ��ǰCPUͼƬ

        Double curMemoryValueDouble = 0D; // ��ǰ�ڴ�ֵ
        Double curCPUValueDouble = 0D; // ��ǰCPUֵ

        Hashtable<Integer, Hashtable<String, String>> curDayMemoryValueHashtable = null; // �ڴ�
        Hashtable<Integer, Hashtable<String, String>> curDayDiskValueHashtable = null; // ����
        String curDayAmMemoryChartInfo = ""; // �ڴ���״ͼʹ�õ�AMCHART��XML
        String curDayAmDiskChartInfo = ""; // ������״ͼʹ�õ�AMCHART��XML

        String nodeid = getNodeid();
        String type = getType();
        String subtype = getSubtype();
        WindowsDataService windowsDataService = new WindowsDataService(nodeid,
                type, subtype);
        NodeDTO node = windowsDataService.getNodeDTO();
        HostNode hostNode = (HostNode) windowsDataService.getBaseVo();
        category = String.valueOf(hostNode.getCategory());
        maxAlarmLevel = windowsDataService.getMaxAlarmLevel();
        mac = hostNode.getBridgeAddress();
        String supperName = windowsDataService.getSupperName();

        List<NodeTemp> systemList = windowsDataService.getSystemInfo();
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

        List<NodeTemp> pingList = windowsDataService.getPingInfo();
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
        curResponseTimeValue = curResponseTimeValue.replaceAll(
                "����", "").replaceAll("%", "");
        curPingImage = windowsDataService.getPingImageInfo(curPingValue);

        List<NodeTemp> curMemoryValueList = windowsDataService.getMemoryInfo();
        if (curMemoryValueList != null && curMemoryValueList.size() > 0) {
            for (int i = 0; i < curMemoryValueList.size(); i++) {
                NodeTemp nodeTemp = (NodeTemp) curMemoryValueList.get(i);
                if ("VirtualMemory".equalsIgnoreCase(nodeTemp.getSindex())
                        && "Utilization".equalsIgnoreCase(nodeTemp
                                .getSubentity())) {
                    curVirtualMemoryValue = Math.round(Float
                            .parseFloat(nodeTemp.getThevalue()))
                            + "";
                } else if ("PhysicalMemory".equalsIgnoreCase(nodeTemp
                        .getSindex())
                        && "Utilization".equalsIgnoreCase(nodeTemp
                                .getSubentity())) {
                    curPhysicalMemoryValue = Math.round(Float
                            .parseFloat(nodeTemp.getThevalue()))
                            + "";
                }
            }
        }
        curPhysicalMemoryImage = windowsDataService
            .getMemoryImageInfo(Double.valueOf(curPhysicalMemoryValue));

        curCPUValue = windowsDataService.getCPUInfo();
        if (curCPUValue != null && curCPUValue.length() > 0) {
            curCPUValueDouble = Double.valueOf(curCPUValue);
        }
        curCPUImage = windowsDataService.getCPUImageInfo(curCPUValueDouble
                        .intValue());

        // �ڴ�ֵ
        curDayMemoryValueHashtable = windowsDataService
                .getCurDayMemoryValueHashtable();
        // ����ֵ
        curDayDiskValueHashtable = windowsDataService
                .getCurDayDiskValueHashtable();
        // �ڴ�ͼ
        curDayAmMemoryChartInfo = windowsDataService
                .getAmMemoryChartInfo(curDayMemoryValueHashtable);
        // ����ͼ
        curDayAmDiskChartInfo = windowsDataService
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
        request.setAttribute("curVirtualMemoryValue", curVirtualMemoryValue);
        request.setAttribute("curMemoryValueList", curMemoryValueList);
        request.setAttribute("curCPUValue", curCPUValue);
        request.setAttribute("curPingImage", curPingImage);
        request.setAttribute("curMemoryImage", curPhysicalMemoryImage);
        request.setAttribute("curCPUImage", curCPUImage);

        request.setAttribute("curDayMemoryValueHashtable",
                curDayMemoryValueHashtable);
        request.setAttribute("curDayDiskValueHashtable",
                curDayDiskValueHashtable);
        request.setAttribute("curDayAmMemoryChartInfo",
                        curDayAmMemoryChartInfo);
        request.setAttribute("curDayAmDiskChartInfo", curDayAmDiskChartInfo);
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
        String orderflag1 = this.getParaValue("orderflag1");
        boolean orderflagBoolean = true;
        System.out.println(orderflag1);
        if ("0".equals(orderflag1)) {
            orderflagBoolean = false;
            orderflag1 = "1";
        } else {
            orderflag1 = "0";
        }
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
        sort(list, orderflag, orderflagBoolean);
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
        request.setAttribute("orderflag1", orderflag1);
        //wupinlong add
        request.setAttribute("procNameHash", procNameHash);
        
        return "/detail/host/windows/process.jsp";
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

    private List sort(List list, String type, final boolean order) {
        if (type.equals("CpuTime")) {
            Collections.sort(list, new Comparator() {
                public int compare(Object o1, Object o2) {
                    ProcessInfo p1 = (ProcessInfo) o1;
                    ProcessInfo p2 = (ProcessInfo) o2;
                    if ((Float) p1.getCpuTime() <= (Float) p2.getCpuTime() && order)
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
                            .getCpuUtilization() && order)
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
                    if ((Float) p1.getMemory() <= (Float) p2.getMemory() && order)
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
                            .getMemoryUtilization() && order)
                        return 1;
                    else
                        return -1;
                }
            });
        }
        return list;
    }
}
