package com.afunms.topology.service;

import java.util.List;

import com.afunms.alarm.service.NodeAlarmService;
import com.afunms.event.model.CheckEvent;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.Constant;

public abstract class TopoNodeInfoService {

    private NodeDTO node;

    private String nodeid;
    
    private String type;
    
    private String subtype;
    
    private String ipaddress;

    private int alarmLevel = 0;

    public static TopoNodeInfoService getInstance(NodeDTO node) {
        TopoNodeInfoService topoNodeInfoService = null; 
        if (Constant.TYPE_NET.equals(node.getType())) {
            topoNodeInfoService = new TopoNetInfoService();
        } else if (Constant.TYPE_HOST_SUBTYPE_WINDOWS.equals(node.getSubtype())) {
            topoNodeInfoService = new TopoWindowsInfoService();
        } else if (Constant.TYPE_HOST_SUBTYPE_AIX.equals(node.getSubtype())) {
            topoNodeInfoService = new TopoAixInfoService();
        } else if (Constant.TYPE_HOST_SUBTYPE_LINUX.equals(node.getSubtype())) {
            topoNodeInfoService = new TopoLinuxInfoService();
        } else if (Constant.TYPE_HOST_SUBTYPE_HPUNIX.equals(node.getSubtype())) {
            topoNodeInfoService = new TopoHpunixInfoService();
        } else if (Constant.TYPE_HOST_SUBTYPE_TRU64.equals(node.getSubtype())) {
            topoNodeInfoService = new TopoTru64InfoService();
        } else if (Constant.TYPE_HOST_SUBTYPE_SOLARIS.equals(node.getSubtype())) {
            topoNodeInfoService = new TopoSolarisInfoService();
        } else if (Constant.TYPE_FIREWALL_SUBTYPE_TOPSEC.equals(node.getSubtype())) {
            topoNodeInfoService = new TopoTopsecInfoService();
        } else if (Constant.TYPE_FIREWALL_SUBTYPE_VENUS.equals(node.getSubtype())) {
            topoNodeInfoService = new TopoVenusInfoService();
        } else if (Constant.TYPE_LINK.equals(node.getType())) {
            topoNodeInfoService = new TopoLinkInfoService();
        } else if (Constant.TYPE_LINK_SUBTYPE_HIN.equals(node.getSubtype())) {
            topoNodeInfoService = new TopoAixInfoService();
        } else if (Constant.TYPE_DB.equals(node.getType())) {
            topoNodeInfoService = new TopoDBInfoService();
        } else if (Constant.TYPE_MIDDLEWARE.equals(node.getType())) {
            topoNodeInfoService = new TopoMiddlewareInfoService();
        }
        topoNodeInfoService.setNode(node);
        topoNodeInfoService.setAlarmLevel(0);
        return topoNodeInfoService;
    }

    public String getNodeInfo() {
        return getPerformanceInfo() + getAlarmInfo();
    }

    public abstract String getPerformanceInfo();

    public String getAlarmInfo() {
        String alarmInfo = "";
        NodeAlarmService nodeAlarmService = new NodeAlarmService();
        List<CheckEvent> list = nodeAlarmService.getAllAlarm(getNode());
        StringBuffer alarmInfoSB = new StringBuffer();
        if (list != null && list.size() > 0 ) {
            for (CheckEvent checkEvent : list) {
                int alarmLevel = checkEvent.getAlarmlevel();
                if (alarmLevel > getAlarmLevel()) {
                    setAlarmLevel(alarmLevel);
                }
                alarmInfoSB.append("<br>").append(checkEvent.getContent());
            }
        }
        if (getAlarmLevel() > 0) {
            alarmInfo = "<br><font color='red'>--±¨¾¯ÐÅÏ¢:--</font>";
            alarmInfo += alarmInfoSB.toString();
        }
        return alarmInfo;
    }

    public int getAlarmLevel() {
        return alarmLevel;
    }

    public NodeDTO getNode() {
        return node;
    }

    public String getNodeid() {
        return getNode().getNodeid();
    }
    /**
     * @return the type
     */
    public String getType() {
        return getNode().getType();
    }
    /**
     * @return the subtype
     */
    public String getSubtype() {
        return getNode().getSubtype();
    }
    /**
     * @return the ipaddress
     */
    public String getIpaddress() {
        return getNode().getIpaddress();
    }
    /**
     * @param node the node to set
     */
    private void setNode(NodeDTO node) {
        this.node = node;
    }

    protected void setAlarmLevel(int alarmLevel) {
        this.alarmLevel = alarmLevel;
    }
}
