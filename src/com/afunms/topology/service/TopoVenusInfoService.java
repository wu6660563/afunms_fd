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
 * @author      ��Ʒ��
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

        String curPingInfo = getPingInfo();     // ������
        String curCPUInfo = getCPUInfo();       // CPU
        String[] curMemroyInfo = getMemoryInfo(); // �ڴ�
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
        msg.append("����������:");
        msg.append(curMemroyInfo[0]);
        msg.append("<br>");
        msg.append("���������:");
        msg.append(allInBandwidthUtilHdx[0]);
        msg.append("<br>");
        msg.append("����������:");
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
        // ��ǰ��ͨ�ʺ���Ӧʱ��
        String curPingValue = "0";               // ������
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
     * �����ڴ�
     * @return
     */
    public String[] getMemoryInfo() {
        String curPhysicalMemoryValue = "0";    // �����ڴ�ֵ
        
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
     * ������ںͳ���������
     * @return
     */
    public String[] getAllBandwidthUtilHdx() {
        String curAllInBandwidthUtilHdxValue = "0"; // ��ǰ���������
        String curAllOutBandwidthUtilHdxValue = "0";// ��ǰ����������
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

