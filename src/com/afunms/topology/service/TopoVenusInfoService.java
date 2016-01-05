/*
 * @(#)TopoTopsecInfoService.java     v1.01, May 29, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.topology.service;

import java.util.List;

import com.afunms.data.service.TopsecDataService;
import com.afunms.data.service.VenusDataService;
import com.afunms.detail.reomte.model.InterfaceInfo;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.temp.model.NodeTemp;

/**
 * ClassName:   TopoVenusInfoService.java
 * <p>
 *
 * @author      吴品龙
 * @version     v1.01
 * @since       v1.01
 * @Date        May 29, 2013 11:18:10 AM
 */
public class TopoVenusInfoService extends TopoNodeInfoService  {

	private VenusDataService venusDataService;
	
	@Override
	public String getPerformanceInfo() {
        NodeDTO node = getNode();
        String name = node.getName();
        String ipaddress = node.getIpaddress();

        String curPingInfo = getPingInfo();     // 可用性
        String curCPUInfo = getCPUInfo();       // CPU
        String[] curMemroyInfo = getMemoryInfo(); // 内存
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
        msg.append("物理利用率:");
        msg.append(curMemroyInfo[0]);
        msg.append("<br>");
        msg.append("入口总流速:");
        msg.append(allInBandwidthUtilHdx[0]);
        msg.append("<br>");
        msg.append("出口总流速:");
        msg.append(allInBandwidthUtilHdx[1]);
        return msg.toString(); 
    }
	
	public VenusDataService getVenusDataService() {
        if (venusDataService == null) {
            String nodeid = getNodeid();
            String type = getType();
            String subtype = getSubtype();
            venusDataService = new VenusDataService(nodeid, type, subtype);
        }
        return venusDataService;
    }
	
	public String getPingInfo() {
        // 当前连通率和响应时间
        String curPingValue = "0";               // 可用性
        List<NodeTemp> pingList = getVenusDataService().getPingInfo();
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
        String curCPUValueTemp = getVenusDataService().getCPUInfo();
        if (curCPUValueTemp != null) {
            curCPUValue = curCPUValueTemp;
        }
        curCPUValue += "%";
        return curCPUValue;
    }
	
	/** 
     * 物理内存
     * @return
     */
    public String[] getMemoryInfo() {
        String curPhysicalMemoryValue = "0";    // 物理内存值
        
        List<NodeTemp> curMemoryValueList = getVenusDataService().getMemoryInfo();
        if (curMemoryValueList != null && curMemoryValueList.size() > 0) {
            for (int i = 0; i < curMemoryValueList.size(); i++) {
                NodeTemp nodeTemp = (NodeTemp) curMemoryValueList.get(i);
                if ("Avg".equalsIgnoreCase(nodeTemp
                        .getSindex())
                        && "Utilization".equalsIgnoreCase(nodeTemp
                                .getSubentity())) {
                    curPhysicalMemoryValue = Math.round(Float
                            .parseFloat(nodeTemp.getThevalue()))
                            + "";
                }
            }
        }
        return new String[] {curPhysicalMemoryValue += "%"};
    }
    
    /**
     * 包含入口和出口总流速
     * @return
     */
    public String[] getAllBandwidthUtilHdx() {
        String curAllInBandwidthUtilHdxValue = "0"; // 当前入口总流速
        String curAllOutBandwidthUtilHdxValue = "0";// 当前出口总流速
        List<InterfaceInfo> curAllBandwidthUtilHdxList = getVenusDataService()
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

