/*
 * @(#)TopoHpunixInfoService.java     v1.01, May 29, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.topology.service;

import java.util.Hashtable;
import java.util.List;

import com.afunms.data.service.HpunixDataService;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.temp.model.NodeTemp;

/**
 * 
 * ClassName:   TopoHpunixInfoService.java
 * <p>
 *
 * @author      ��Ʒ��
 * @version     v1.01
 * @since       v1.01
 * @Date        May 29, 2013 3:12:33 PM
 */
public class TopoHpunixInfoService  extends TopoNodeInfoService {

	private HpunixDataService hpunixDataService;
	
	@Override
	public String getPerformanceInfo() {
        NodeDTO node = getNode();
        String name = node.getName();
        String ipaddress = node.getIpaddress();

        String curPingInfo = getPingInfo();     // ������
        String curCPUInfo = getCPUInfo();       // CPU
        String[] curMemroyInfo = getMemoryInfo(); // �ڴ�
        String[][] curDiskInfo = getDiskInfo();

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
        msg.append("�����ڴ�������:");
        msg.append(curMemroyInfo[1]);
        if (curDiskInfo != null && curMemroyInfo.length > 0) {
            for (String[] perCurDiskInfo : curDiskInfo) {
                msg.append("<br>");
                msg.append(perCurDiskInfo[0]);
                msg.append(" ������:");
                msg.append(perCurDiskInfo[1]);
            }
        }
        return msg.toString(); 
    }
	
	public HpunixDataService getHpunixDataService() {
        if (hpunixDataService == null) {
            String nodeid = getNodeid();
            String type = getType();
            String subtype = getSubtype();
            hpunixDataService = new HpunixDataService(nodeid, type, subtype);
        }
        return hpunixDataService;
    }
	
	public String getPingInfo() {
        // ��ǰ��ͨ�ʺ���Ӧʱ��
        String curPingValue = "0";               // ������
        List<NodeTemp> pingList = getHpunixDataService().getPingInfo();
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
        String curCPUValueTemp = getHpunixDataService().getCPUInfo();
        if (curCPUValueTemp != null) {
            curCPUValue = curCPUValueTemp;
        }
        curCPUValue += "%";
        return curCPUValue;
    }
	
	/** 
     * �����ڴ�������ڴ�
     * @return
     */
    public String[] getMemoryInfo() {
        String curSwapMemoryValue = "0";        // �����ڴ�ֵ
        String curPhysicalMemoryValue = "0";    // �����ڴ�ֵ
        
        List<NodeTemp> curMemoryValueList = getHpunixDataService().getMemoryInfo();
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
     * �������еĴ��̵����ƺ�������
     * @return
     */
    public String[][] getDiskInfo() {
        // ����ֵ
        String[][] curDiskInfo = null;
        Hashtable<Integer, Hashtable<String, String>> curDayDiskValueHashtable = getHpunixDataService()
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

