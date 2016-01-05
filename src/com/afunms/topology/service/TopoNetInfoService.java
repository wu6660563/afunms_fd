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

        String curPingInfo = getPingInfo();     // ������
        String curCPUInfo = getCPUInfo();       // CPU
        String curMemroyInfo = getMemoryInfo(); // �ڴ�
        String[] allInBandwidthUtilHdx = getAllBandwidthUtilHdx();

        StringBuffer msg = new StringBuffer(200);
        msg.append("�豸����:");
        msg.append(name);
        msg.append("<br>");
        msg.append("IP��ַ:");
        msg.append(ipaddress);
        msg.append("<br>");
        msg.append("������:");
        msg.append(curPingInfo);
        msg.append("<br>");
        msg.append("CPU������:");
        msg.append(curCPUInfo);
        msg.append("<br>");
        msg.append("�ڴ�������:");
        msg.append(curMemroyInfo);
        msg.append("<br>");
        msg.append("���������:");
        msg.append(allInBandwidthUtilHdx[0]);
        msg.append("<br>");
        msg.append("����������:");
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
        // ��ǰ��ͨ�ʺ���Ӧʱ��
        String curPingValue = "0";               // ������
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

    // �ڴ�
    public String getMemoryInfo() {
        String curMemoryValue = "0";        // ��ǰ�ڴ�ֵ
        Double curMemoryValueDouble = 0D;   // ��ǰ�ڴ�ֵ
        
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
     * ������ںͳ���������
     * @return
     */
    public String[] getAllBandwidthUtilHdx() {
        String curAllInBandwidthUtilHdxValue = "0"; // ��ǰ���������
        String curAllOutBandwidthUtilHdxValue = "0";// ��ǰ����������
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
