package com.afunms.topology.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.service.NodeAlarmService;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.base.BaseVo;
import com.afunms.data.service.AixDataService;
import com.afunms.detail.service.interfaceInfo.InterfaceInfoService;
import com.afunms.event.model.CheckEvent;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.temp.dao.InterfaceTempDao;
import com.afunms.temp.model.NodeTemp;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.dao.LinkDao;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.model.Link;
import com.ibm.ws.management.resources.nodeutils;

public class TopoLinkInfoService extends TopoNodeInfoService {

    private Link link;

    private NodeDTO startNode;

    private NodeDTO endNode;

    @Override
    public String getAlarmInfo() {
        String alarmInfo = "";
        NodeDTO startNode = getStartNode();
        NodeDTO endNode = getEndNode();
        NodeAlarmService nodeAlarmService = new NodeAlarmService();
        AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
        List<CheckEvent> list = nodeAlarmService.getAllAlarm(getStartNode());
        List<AlarmIndicatorsNode> alarmIndicatorsNodeList = alarmIndicatorsUtil.getAlarmIndicatorsForNode(startNode.getNodeid(), startNode.getType(), startNode.getSubtype());
        StringBuffer alarmInfoSB = new StringBuffer();
        if (list != null && list.size() > 0 ) {
            for (CheckEvent checkEvent : list) {
                String sindex = checkEvent.getSindex();
                String alarmId = checkEvent.getAlarmId();
                int alarmLevel = checkEvent.getAlarmlevel();
                if (sindex == null) {
                    continue;
                }
                sindex = sindex.trim();
                if (!getStartIndex().equalsIgnoreCase(sindex)) {
                    // 排除其他端口
                    continue;
                }
                for (AlarmIndicatorsNode alarmIndicatorsNode : alarmIndicatorsNodeList) {
                    if (alarmId.equals(String.valueOf(alarmIndicatorsNode.getId()))
                            && "interface".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
                        alarmInfoSB.append("<br>").append(checkEvent.getContent());
                        if (alarmLevel > getAlarmLevel()) {
                            setAlarmLevel(alarmLevel);
                        }
                    } else if (alarmId.equals(String.valueOf(alarmIndicatorsNode.getId()))
                            && "InBandwidthUtilHdx".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
                        if (alarmLevel > getAlarmLevel()) {
                            setAlarmLevel(alarmLevel);
                        }
                        alarmInfoSB.append("<br>").append(checkEvent.getContent());
                    } else if (alarmId.equals(String.valueOf(alarmIndicatorsNode.getId()))
                            && "OutBandwidthUtilHdx".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
                        if (alarmLevel > getAlarmLevel()) {
                            setAlarmLevel(alarmLevel);
                        }
                        alarmInfoSB.append("<br>").append(checkEvent.getContent());
                    }
                } 
            }
        }
        list = nodeAlarmService.getAllAlarm(endNode);
        alarmIndicatorsNodeList = alarmIndicatorsUtil.getAlarmIndicatorsForNode(endNode.getNodeid(), endNode.getType(), endNode.getSubtype());
        if (list != null && list.size() > 0 ) {
            for (CheckEvent checkEvent : list) {
                String sindex = checkEvent.getSindex();
                String alarmId = checkEvent.getAlarmId();
                int alarmLevel = checkEvent.getAlarmlevel();
                if (sindex == null) {
                    continue;
                }
                sindex = sindex.trim();
                if (!getEndIndex().equalsIgnoreCase(sindex)) {
                    // 排除其他端口
                    continue;
                }
                for (AlarmIndicatorsNode alarmIndicatorsNode : alarmIndicatorsNodeList) {
                    if (alarmId.equals(String.valueOf(alarmIndicatorsNode.getId()))
                            && "interface".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
                        if (alarmLevel > getAlarmLevel()) {
                            setAlarmLevel(alarmLevel);
                        }
                        alarmInfoSB.append("<br>").append(checkEvent.getContent());
                    } else if (alarmId.equals(String.valueOf(alarmIndicatorsNode.getId()))
                            && "InBandwidthUtilHdx".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
                        if (alarmLevel > getAlarmLevel()) {
                            setAlarmLevel(alarmLevel);
                        }
                        alarmInfoSB.append("<br>").append(checkEvent.getContent());
                    } else if (alarmId.equals(String.valueOf(alarmIndicatorsNode.getId()))
                            && "OutBandwidthUtilHdx".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
                        if (alarmLevel > getAlarmLevel()) {
                            setAlarmLevel(alarmLevel);
                        }
                        alarmInfoSB.append("<br>").append(checkEvent.getContent());
                    }
                } 
            }
        }
        System.out.println(getAlarmLevel() + "==getAlarmLevel()=");
        if (getAlarmLevel() > 0) {
            alarmInfo = "<br><font color='red'>--报警信息:--</font>";
            alarmInfo += alarmInfoSB.toString();
        }
        return alarmInfo;
    }

    @Override
    public String getPerformanceInfo() {
        String linkName = getLink().getLinkName();
        String startIP = getStartIP();
        String endIP = getEndIP();
        String startIndex = getStartIndex();
        String endIndex = getEndIndex();
        String startDescr = getStartDescr();
        String endDescr = getEndDescr();
        String cable_type = getLink().getCableType();
        String cable_capacity = getLink().getCableCapacity();
        String start_oututilhdx = "0";
        String start_oututilhdxperc = "0";
        String end_oututilhdx = "0";
        String end_oututilhdxperc = "0";
        String startOperStatus = "";
        String endOperStatus = "";
        List<NodeTemp> startNodeInterfaceInfo = new ArrayList<NodeTemp>();
        List<NodeTemp> endNodeInterfaceInfo = new ArrayList<NodeTemp>();
        String[] netInterfaceItem = { 
                "ifOperStatus",                 // 端口状态
                "OutBandwidthUtilHdx",          // 端口出口流速
                "OutBandwidthUtilHdxPerc" };    // 端口出口带宽
        NodeDTO startNode = getStartNode();
        NodeDTO endNode = getEndNode();
        InterfaceTempDao interfacedao = new InterfaceTempDao(startIP);
        try {
            startNodeInterfaceInfo = interfacedao.getNodeTempList(startNode.getNodeid(), startNode.getType(), startNode.getSubtype(), netInterfaceItem);
        } catch (RuntimeException e1) {
            e1.printStackTrace();
        } finally {
            interfacedao.close();
        }
        try {
            interfacedao = new InterfaceTempDao(endIP);
            endNodeInterfaceInfo = interfacedao.getNodeTempList(endNode.getNodeid(), endNode.getType(), endNode.getSubtype(), netInterfaceItem);
        } catch (RuntimeException e1) {
            e1.printStackTrace();
        } finally {
            interfacedao.close();
        }
        
        if (startNodeInterfaceInfo != null && startNodeInterfaceInfo.size() > 0) {
            try {
                for (NodeTemp nodeTemp : startNodeInterfaceInfo) {
                    if (startIndex.equalsIgnoreCase(nodeTemp.getSindex())
                            && "ifOperStatus".equalsIgnoreCase(nodeTemp.getSubentity())) {
                        startOperStatus = nodeTemp.getThevalue();
                    } else if (startIndex.equalsIgnoreCase(nodeTemp.getSindex())
                            && "OutBandwidthUtilHdx".equalsIgnoreCase(nodeTemp.getSubentity())) {
                        start_oututilhdx = nodeTemp.getThevalue().replaceAll("KB/秒", "").replaceAll("kb/s", "").replaceAll("kb/秒", "").replaceAll("KB/S", "").replaceAll("KB/s", "");
                    } else if (startIndex.equalsIgnoreCase(nodeTemp.getSindex())
                            && "OutBandwidthUtilHdxPerc".equalsIgnoreCase(nodeTemp.getSubentity())) {
                        start_oututilhdxperc = nodeTemp.getThevalue().replaceAll("KB/秒", "").replaceAll("kb/s", "").replaceAll("kb/秒", "").replaceAll("KB/S", "").replaceAll("KB/s", "");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (endNodeInterfaceInfo != null && endNodeInterfaceInfo.size() > 0) {
            try {
                for (NodeTemp nodeTemp : endNodeInterfaceInfo) {
                    if (endIndex.equalsIgnoreCase(nodeTemp.getSindex())
                            && "ifOperStatus".equalsIgnoreCase(nodeTemp.getSubentity())) {
                        endOperStatus = nodeTemp.getThevalue();
                    } else if (endIndex.equalsIgnoreCase(nodeTemp.getSindex())
                            && "OutBandwidthUtilHdx".equalsIgnoreCase(nodeTemp.getSubentity())) {
                        end_oututilhdx = nodeTemp.getThevalue().replaceAll("KB/秒", "").replaceAll("kb/s", "").replaceAll("kb/秒", "").replaceAll("KB/S", "").replaceAll("KB/s", "");
                    } else if (endIndex.equalsIgnoreCase(nodeTemp.getSindex())
                            && "OutBandwidthUtilHdxPerc".equalsIgnoreCase(nodeTemp.getSubentity())) {
                        end_oututilhdxperc = nodeTemp.getThevalue().replaceAll("KB/秒", "").replaceAll("kb/s", "").replaceAll("kb/秒", "").replaceAll("KB/S", "").replaceAll("KB/s", "");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (start_oututilhdx == null) {
            start_oututilhdx = "0";
        }
        if (start_oututilhdxperc == null) {
            start_oututilhdxperc = "0";
        }
        if (end_oututilhdx == null) {
            end_oututilhdx = "0";
        }
        if (end_oututilhdxperc == null) {
            end_oututilhdxperc = "0";
        }
        start_oututilhdx += "KB/s";
        start_oututilhdxperc += "%";
        end_oututilhdx += "KB/s";
        end_oututilhdxperc += "%";
        if(startOperStatus != null){
            startOperStatus = "up".equalsIgnoreCase(startOperStatus) ? "(<font color='green'>up</font>)" : "(<font color='red'>down</font>)";
        } else {
            startOperStatus = "(正在获取请稍后查看...)";
        }
        if(endOperStatus != null){
            endOperStatus = "up".equalsIgnoreCase(endOperStatus) ? "(<font color='green'>up</font>)" : "(<font color='red'>down</font>)";
        } else {
            endOperStatus = "(正在获取请稍后查看...)";
        }
        StringBuffer msg = new StringBuffer(200);
        msg.append("链路名称:");
        msg.append(linkName);
        msg.append("<br>");
        msg.append("<br>");
        msg.append("起点IP:");
        msg.append(startIP);
        msg.append("<br>");
        msg.append("<font color='green'>起点端口:");
        msg.append(startDescr + "</font>" + startOperStatus);
        msg.append("<br>");
        msg.append("终点IP:");
        msg.append(endIP);
        msg.append("<br>");
        msg.append("<font color='green'>终点端口:");
        msg.append(endDescr + "</font>" + endOperStatus);
        msg.append("<br>");
        msg.append("<font color='green'>下行流速:");
        msg.append(start_oututilhdx);
        msg.append("</font><br>");
        msg.append("<font color='green'>上行流速:");
        msg.append(end_oututilhdx);
        msg.append("</font><br>");
        msg.append("<font color='green'>下行带宽利用率:");
        msg.append(start_oututilhdxperc);
        msg.append("</font><br>");
        msg.append("<font color='green'>上行带宽利用率:");
        msg.append(end_oututilhdxperc);
        msg.append("</font>");
        msg.append("<br>");
        msg.append("线缆类型:");
        msg.append(cable_type);
        msg.append("<br>");
        msg.append("线缆容量:");
        msg.append(cable_capacity);
        return msg.toString(); 
    }

    public Link getLink() {
        if (link == null) {
            LinkDao linkDao = new LinkDao();
            try {
                link = (Link) linkDao.findByID(getNodeid());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                linkDao.close();
            }
        }
        return link;
    }

    public NodeDTO getStartNode() {
        if (startNode == null) {
            int id = getStartId();
            HostNodeDao hostNodeDao = new HostNodeDao();
            BaseVo vo = null;
            try {
                vo = hostNodeDao.findByID(String.valueOf(id));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                hostNodeDao.close();
            }
            NodeUtil nodeUtil = new NodeUtil();
            startNode = nodeUtil.conversionToNodeDTO(vo);
        }
        return startNode;
    }

    public NodeDTO getEndNode() {
        if (endNode == null) {
            int id = getEndId();
            HostNodeDao hostNodeDao = new HostNodeDao();
            BaseVo vo = null;
            try {
                vo = hostNodeDao.findByID(String.valueOf(id));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                hostNodeDao.close();
            }
            NodeUtil nodeUtil = new NodeUtil();
            endNode = nodeUtil.conversionToNodeDTO(vo);
        }
        return endNode;
    }

    public int getStartId() {
        return getLink().getStartId();
    }

    public int getEndId() {
        return getLink().getEndId();
    }

    public String getStartIndex() {
        return getLink().getStartIndex();
    }

    public String getEndIndex() {
        return getLink().getEndIndex();
    }

    public String getStartDescr() {
        return getLink().getStartDescr();
    }

    public String getEndDescr() {
        return getLink().getEndDescr();
    }

    public String getStartIP() {
        String startIP = getLink().getStartIp();
        if (startIP == null || startIP.trim().length() == 0) {
            startIP = getStartNode().getIpaddress();
        }
        return startIP;
    }

    public String getEndIP() {
        String endIP = getLink().getEndIp();
        if (endIP == null || endIP.trim().length() == 0) {
            endIP = getEndNode().getIpaddress();
        }
        return endIP;
    }
}
