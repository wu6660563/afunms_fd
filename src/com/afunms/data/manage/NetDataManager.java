/**
 * <p>Description:DiscoverManager</p>
 * <p>Company: dhcc.com</p>
 * @author nielin
 * @project afunms
 * @date 2010-12-08
 */

package com.afunms.data.manage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.common.base.ErrorMessage;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.SysLogger;
import com.afunms.config.dao.PortconfigDao;
import com.afunms.config.model.IpAlias;
import com.afunms.config.model.Portconfig;
import com.afunms.data.service.NetDataService;
import com.afunms.detail.reomte.model.InterfaceInfo;
import com.afunms.event.dao.EventListDao;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.polling.om.IpMac;
import com.afunms.system.model.User;
import com.afunms.temp.model.DeviceNodeTemp;
import com.afunms.temp.model.NodeTemp;
import com.afunms.temp.model.RouterNodeTemp;
import com.afunms.topology.model.HostNode;
import com.sybase.jdbc2.tds.CurInfoToken;

/**
 * �� manager ���ڽ����豸��ϸ��Ϣҳ����ת
 */
public class NetDataManager extends NodeDataManager {

    private static final SysLogger sysLogger = SysLogger
            .getLogger(NetDataManager.class.getName());

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
     * ��ȡ Interface ��Ϣ
     * 
     * @return {@link String} ���� Interface ��Ϣ��ҳ��
     */
    public String getInterfaceInfo() {
        getBaseInfo();
        String nodeid = getNodeid();
        String type = getType();
        String subtype = getSubtype();
        NetDataService netDataService = new NetDataService(nodeid, type,
                subtype);
        String orderFlag = getParaValue("orderflag");
        String important = getParaValue("important");
        if (orderFlag == null || orderFlag.trim().length() == 0) {
            orderFlag = "index";
        }
        
        Vector interfaceInfo = null;
        interfaceInfo = netDataService.getInterfaceInfo(orderFlag);
        PortconfigDao portconfigDao = new PortconfigDao();
        List<Portconfig> list = null;
        try {
            if ("0".equals(important)) {
                list = portconfigDao.loadByIpaddress(netDataService.getNodeDTO().getIpaddress());
            } else {
                important = "1";
                list = portconfigDao.findByImportant(netDataService.getNodeDTO().getIpaddress(), important);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            portconfigDao.close();
        }
        Hashtable<String, Portconfig> hashtable = new Hashtable<String, Portconfig>();
        for (Portconfig portconfig : list) {
            hashtable.put(String.valueOf(portconfig.getPortindex()), portconfig);
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
        return "/detail/net/interface.jsp";
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
        String curAllInBandwidthUtilHdxValue = "0"; // ��ǰ���������
        String curDayAllInBandwidthUtilHdxMaxValue = "0"; // ����������������ֵ
        String curDayAllInBandwidthUtilHdxAvgValue = "0"; // �������������ƽ��ֵ
        String curAllOutBandwidthUtilHdxValue = "0";// ��ǰ����������
        String curDayAllOutBandwidthUtilHdxMaxValue = "0"; // ����������������ֵ
        String curDayAllOutBandwidthUtilHdxAvgValue = "0"; // �������������ƽ��ֵ

        NetDataService netDataService = new NetDataService(nodeid, type,
                subtype);
        Hashtable<String, String> curDayPingValuHashtable = netDataService
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
        Hashtable<String, String> curDayResponseTimeValueHashtable = netDataService
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
            curDayResponseTimeAvgValue = "0";
        }
        curDayResponseTimeAvgValue = curDayResponseTimeAvgValue.replaceAll(
                "����", "").replaceAll("%", "");
        curDayResponseTimeAvgValue = String.valueOf(Math.round(Double
                .valueOf(curDayResponseTimeAvgValue)));

        // ��ʼ��ȡ��Ӧʱ����״ͼ
        curDayResponseTimeBarImage = netDataService
                .getCurDayResponseTimeBarImageInfo(curResponseTimeValue,
                        curDayResponseTimeMaxValue, curDayResponseTimeAvgValue);

        // ��ʼ��ȡCPU��ͼƬ
        Hashtable<String, String> curDayCPUValueHashtable = netDataService
                .getCurDayCPUValueHashtableInfo();

        curDayCPUMaxValue = curDayCPUValueHashtable.get("max");
        if (curDayCPUMaxValue == null || curDayCPUMaxValue.trim().length() == 0) {
            curDayCPUMaxValue = "0";
        }
        curDayCPUMaxValue = curDayCPUMaxValue.replaceAll("%", "");
        curDayCPUMaxValue = String.valueOf(Math.round(Double
                .valueOf(curDayCPUMaxValue)));
        curDayCPUMaxImageInfo = netDataService
                .getCurDayCPUMaxImageInfo(curDayCPUMaxValue);

        curDayCPUAvgValue = curDayCPUValueHashtable.get("avgcpucon");
        if (curDayCPUAvgValue == null || curDayCPUAvgValue.trim().length() == 0) {
            curDayCPUAvgValue = "0";
        }
        curDayCPUAvgValue = curDayCPUAvgValue.replaceAll("%", "");
        curDayCPUAvgValue = String.valueOf(Math.round(Double
                .valueOf(curDayCPUAvgValue)));
        curDayCPUAvgImageInfo = netDataService
                .getCurDayCPUAvgImageInfo(curDayCPUAvgValue);

        // ��ȡ��ǰ�豸��Ϣ�б�
        currDeviceNodeTempList = netDataService.getCurDeviceListInfo();

        // ��ȡ�ۺ�������Ϣ
        List<InterfaceInfo> curAllBandwidthUtilHdxList = netDataService
                .getcurAllBandwidthUtilHdxInfo();
        if (curAllBandwidthUtilHdxList != null) {
            for (InterfaceInfo interfaceInfo : curAllBandwidthUtilHdxList) {
                curAllInBandwidthUtilHdxValue = interfaceInfo
                        .getInBandwidthUtilHdx();
                curAllOutBandwidthUtilHdxValue = interfaceInfo
                        .getInBandwidthUtilHdx();
            }
        }
        Hashtable<String, Integer> curDayAllBandwidthUtilHdxHashtable = netDataService
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
        return "/detail/net/performance.jsp";
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
        return "/detail/net/arp.jsp";
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
        return "/detail/net/event.jsp";
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
        String curMemoryValue = "0"; // ��ǰ�ڴ�ֵ
        String curCPUValue = "0"; // ��ǰCPUֵ
        String curPingImage = ""; // ��ǰ��ͨ��ͼƬ
        String curMemoryImage = ""; // ��ǰ�ڴ�ͼƬ
        String curCPUImage = ""; // ��ǰCPUͼƬ

        Double curMemoryValueDouble = 0D; // ��ǰ�ڴ�ֵ
        Double curCPUValueDouble = 0D; // ��ǰCPUֵ

        String nodeid = getNodeid();
        String type = getType();
        String subtype = getSubtype();
        NetDataService netDataService = new NetDataService(nodeid, type,
                subtype);
        NodeDTO node = netDataService.getNodeDTO();
        HostNode hostNode = (HostNode) netDataService.getBaseVo();
        category = String.valueOf(hostNode.getCategory());
        maxAlarmLevel = netDataService.getMaxAlarmLevel();
        mac = hostNode.getBridgeAddress();
        String supperName = netDataService.getSupperName();

        List<NodeTemp> systemList = netDataService.getSystemInfo();
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

        List<NodeTemp> pingList = netDataService.getPingInfo();
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
        curResponseTimeValue = curResponseTimeValue.replaceAll(
                "����", "").replaceAll("%", "");
        curPingImage = netDataService.getPingImageInfo(curPingValue);

        List<NodeTemp> curMemoryValueList = netDataService.getMemoryInfo();
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
        curMemoryImage = netDataService.getMemoryImageInfo(Double
                        .valueOf(curMemoryValue));

        curCPUValue = netDataService.getCPUInfo();
        if (curCPUValue != null && curCPUValue.length() > 0) {
            curCPUValueDouble = Double.valueOf(curCPUValue);
        }
        curCPUImage = netDataService.getCPUImageInfo(curCPUValueDouble
                        .intValue());

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

}
