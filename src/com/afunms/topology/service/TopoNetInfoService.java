package com.afunms.topology.service;

import java.util.List;

import com.afunms.data.service.NetDataService;
import com.afunms.detail.reomte.model.InterfaceInfo;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.temp.model.NodeTemp;

public class TopoNetInfoService extends TopoNodeInfoService {

    private NetDataService netDataService;

    @Override
    public String getPerformanceInfo() {
        NodeDTO node = getNode();
        String name = node.getName();
        String ipaddress = node.getIpaddress();

        String curPingInfo = getPingInfo();     // 可用性
        String curCPUInfo = getCPUInfo();       // CPU
        String curMemroyInfo = getMemoryInfo(); // 内存
        String[] allInBandwidthUtilHdx = getAllBandwidthUtilHdx();

        StringBuffer msg = new StringBuffer(200);
        msg.append("设备名称:");
        msg.append(name);
        msg.append("<br>");
        msg.append("IP地址:");
        msg.append(ipaddress);
        msg.append("<br>");
        msg.append("可用性:");
        msg.append(curPingInfo);
        msg.append("<br>");
        msg.append("CPU利用率:");
        msg.append(curCPUInfo);
        msg.append("<br>");
        msg.append("内存利用率:");
        msg.append(curMemroyInfo);
        msg.append("<br>");
        msg.append("入口总流速:");
        msg.append(allInBandwidthUtilHdx[0]);
        msg.append("<br>");
        msg.append("出口总流速:");
        msg.append(allInBandwidthUtilHdx[1]);
        return msg.toString(); 
    }

    public NetDataService getNetDataService() {
        if (netDataService == null) {
            String nodeid = getNodeid();
            String type = getType();
            String subtype = getSubtype();
            netDataService = new NetDataService(nodeid, type, subtype);
        }
        return netDataService;
    }

    public String getPingInfo() {
        // 当前连通率和响应时间
        String curPingValue = "0";               // 可用性
        List<NodeTemp> pingList = getNetDataService().getPingInfo();
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
                }
            }
        }
        return curPingValue += "%";
    }

    public String getCPUInfo() {
        String curCPUValue = "0";       // CPU
        NetDataService netDataService = getNetDataService();
        String curCPUValueTemp = netDataService.getCPUInfo();
        if (curCPUValueTemp != null) {
            curCPUValue = curCPUValueTemp;
        }
        curCPUValue += "%";
        return curCPUValue;
    }

    // 内存
    public String getMemoryInfo() {
        String curMemoryValue = "0";        // 当前内存值
        Double curMemoryValueDouble = 0D;   // 当前内存值
        
        List<NodeTemp> curMemoryValueList = getNetDataService().getMemoryInfo();
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
        curMemoryValue += "%";
        return curMemoryValue;
    }

    /**
     * 包含入口和出口总流速
     * @return
     */
    public String[] getAllBandwidthUtilHdx() {
        String curAllInBandwidthUtilHdxValue = "0"; // 当前入口总流速
        String curAllOutBandwidthUtilHdxValue = "0";// 当前出口总流速
        List<InterfaceInfo> curAllBandwidthUtilHdxList = getNetDataService()
                .getcurAllBandwidthUtilHdxInfo();
        if (curAllBandwidthUtilHdxList != null) {
            for (InterfaceInfo interfaceInfo : curAllBandwidthUtilHdxList) {
                curAllInBandwidthUtilHdxValue = interfaceInfo
                        .getInBandwidthUtilHdx();
                curAllOutBandwidthUtilHdxValue = interfaceInfo
                        .getOutBandwidthUtilHdx();
            }
        }
        return new String[] {curAllInBandwidthUtilHdxValue += "KB/s", curAllOutBandwidthUtilHdxValue += "KB/s"} ;
    }
}
