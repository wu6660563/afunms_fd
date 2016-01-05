package com.afunms.topology.service;


import com.afunms.indicators.model.NodeDTO;

public class TopoDBInfoService extends TopoNodeInfoService {

    @Override
    public String getPerformanceInfo() {
        NodeDTO node = getNode();
        String name = node.getName();
        String ipaddress = node.getIpaddress();

        StringBuffer msg = new StringBuffer(200);
        msg.append("设备名称:");
        msg.append(name);
        msg.append("<br>");
        msg.append("IP地址:");
        msg.append(ipaddress);
        return msg.toString(); 
    }


}
