package com.afunms.topology.service;

import java.util.Hashtable;
import java.util.List;

import com.afunms.data.service.SolarisDataService;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.temp.model.NodeTemp;

public class TopoSolarisInfoService extends TopoNodeInfoService {

    private SolarisDataService solarisDataService;

    @Override
    public String getPerformanceInfo() {
        NodeDTO node = getNode();
        String name = node.getName();
        String ipaddress = node.getIpaddress();

        String curPingInfo = getPingInfo();     // 可用性
        String curCPUInfo = getCPUInfo();       // CPU
        String[] curMemroyInfo = getMemoryInfo(); // 内存
        String[][] curDiskInfo = getDiskInfo();

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
        msg.append("物理利用率:");
        msg.append(curMemroyInfo[0]);
        msg.append("<br>");
        msg.append("交换内存利用率:");
        msg.append(curMemroyInfo[1]);
        if (curDiskInfo != null && curMemroyInfo.length > 0) {
            for (String[] perCurDiskInfo : curDiskInfo) {
                msg.append("<br>");
                msg.append(perCurDiskInfo[0]);
                msg.append(" 利用率:");
                msg.append(perCurDiskInfo[1]);
            }
        }
        return msg.toString(); 
    }

    public SolarisDataService getSolarisDataService() {
        if (solarisDataService == null) {
            String nodeid = getNodeid();
            String type = getType();
            String subtype = getSubtype();
            solarisDataService = new SolarisDataService(nodeid, type, subtype);
        }
        return solarisDataService;
    }

    public String getPingInfo() {
        // 当前连通率和响应时间
        String curPingValue = "0";               // 可用性
        List<NodeTemp> pingList = getSolarisDataService().getPingInfo();
        if (pingList != null && pingList.size() > 0) {
            for (int i = 0; i < pingList.size(); i++) {
                NodeTemp nodetemp = (NodeTemp) pingList.get(i);
                if ("ConnectUtilization".equals(nodetemp.getSindex())) {
                    curPingValue = nodetemp.getThevalue();
                    if (curPingValue == null
                            || curPingValue.trim().length() == 0) {
                        curPingValue = "0";
                    }
                    curPingValue = curPingValue.replaceAll("%", "");
                }
            }
        }
        return curPingValue += "%";
    }

    public String getCPUInfo() {
        String curCPUValue = "0";       // CPU
        String curCPUValueTemp = getSolarisDataService().getCPUInfo();
        if (curCPUValueTemp != null) {
            curCPUValue = curCPUValueTemp;
        }
        curCPUValue += "%";
        return curCPUValue;
    }

    /** 
     * 物理内存和虚拟内存
     * @return
     */
    public String[] getMemoryInfo() {
        String curSwapMemoryValue = "0";        // 交换内存值
        String curPhysicalMemoryValue = "0";    // 物理内存值
        
        List<NodeTemp> curMemoryValueList = getSolarisDataService().getMemoryInfo();
        if (curMemoryValueList != null && curMemoryValueList.size() > 0) {
            for (int i = 0; i < curMemoryValueList.size(); i++) {
                NodeTemp nodeTemp = (NodeTemp) curMemoryValueList.get(i);
                if ("SwapMemory".equalsIgnoreCase(nodeTemp.getSindex())
                        && "Utilization".equalsIgnoreCase(nodeTemp
                                .getSubentity())) {
                    curSwapMemoryValue = Math.round(Float
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
        return new String[] {curPhysicalMemoryValue += "%", curSwapMemoryValue += "%"};
    }

    /**
     * 包含所有的磁盘的名称和利用率
     * @return
     */
    public String[][] getDiskInfo() {
        // 磁盘值
        String[][] curDiskInfo = null;
        Hashtable<Integer, Hashtable<String, String>> curDayDiskValueHashtable = getSolarisDataService()
                .getCurDayDiskValueHashtable();
        if (curDayDiskValueHashtable != null && curDayDiskValueHashtable.size() > 0) {
            curDiskInfo = new String[curDayDiskValueHashtable.size()][2];
        }
        for (int i = 0; i < curDayDiskValueHashtable.size(); i++) {
            Hashtable<String, String> curDayDiskValueHashtablePer = curDayDiskValueHashtable.get(new Integer(i));
            String name = (String) curDayDiskValueHashtablePer.get("name");
            String Utilization = (String) curDayDiskValueHashtablePer.get("Utilization");
            if (Utilization == null || Utilization.trim().length() == 0) {
                Utilization = "0";
            }
            Utilization = Utilization.replaceAll("%", "");
            curDiskInfo[i][0] =  name;
            curDiskInfo[i][1] = Utilization + "%";
        }
        return curDiskInfo;
    }
}
