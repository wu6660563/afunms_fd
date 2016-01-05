package com.afunms.polling.snmp.service;

/*
 * @author hukelei@dhcc.com.cn
 *
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.send.SendMailAlarm;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtils;
import com.afunms.common.util.SysLogger;
import com.afunms.event.model.EventList;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.loader.HostLoader;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Servicecollectdata;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.HostDatatempserciceRttosql;

public class WindowsServiceSnmp extends SnmpMonitor {

    private static java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public WindowsServiceSnmp() {
    }

    public void collectData(Node node, MonitoredItem item) {
    }

    public void collectData(HostNode node) {
    }

    public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
        Hashtable returnHash = new Hashtable();
        Vector serviceVector = new Vector();
        HostLoader hostLoader = new HostLoader();
        Host node = hostLoader.loadOneByID(nodeGatherIndicators.getNodeid());
        if (node == null) {
            return null;
        }
        if (!node.isManaged()) {
            return null;
        }
        try {
//            Servicecollectdata servicedata = null;
//            Calendar date = Calendar.getInstance();
//            Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(
//                    node.getIpAddress());
//            if (ipAllData == null)
//                ipAllData = new Hashtable();
//
//            try {
//                SimpleDateFormat sdf = new SimpleDateFormat(
//                        "yyyy-MM-dd HH:mm:ss");
//                com.afunms.polling.base.Node snmpnode = (com.afunms.polling.base.Node) PollingEngine
//                        .getInstance().getNodeByIP(node.getIpAddress());
//                Date cc = date.getTime();
//                String time = sdf.format(cc);
//                snmpnode.setLastTime(time);
//            } catch (Exception e) {
//
//            }
            try {
                String[] oids = new String[] {
                        "1.3.6.1.4.1.77.1.2.3.1.1", // 名称
                        "1.3.6.1.4.1.77.1.2.3.1.2", "1.3.6.1.4.1.77.1.2.3.1.3",
                        "1.3.6.1.4.1.77.1.2.3.1.4", "1.3.6.1.4.1.77.1.2.3.1.5" };
                String[][] valueArray = null;
                try {
                    valueArray = SnmpUtils.getTableData(node.getIpAddress(),
                            node.getCommunity(), oids, node.getSnmpversion(),
                            3, 1000 * 30);
                } catch (Exception e) {
                    valueArray = null;
                }
                if (valueArray == null) {
                    valueArray = new String[0][0];
                }
                for (int i = 0; i < valueArray.length; i++) {
                    Servicecollectdata servicedata = new Servicecollectdata();
                    String vbstring0 = valueArray[i][0];
                    String vbstring1 = valueArray[i][1];
                    if (vbstring1.equalsIgnoreCase("1")) {
                        vbstring1 = "已卸载";
                    } else if (vbstring1.equalsIgnoreCase("2")) {
                        vbstring1 = "安装待批";
                    } else if (vbstring1.equalsIgnoreCase("3")) {
                        vbstring1 = "卸载待批";
                    } else {
                        vbstring1 = "已安装";
                    }
                    String vbstring2 = valueArray[i][2];
                    if (vbstring2.equalsIgnoreCase("1")) {
                        vbstring2 = "活动的";
                    } else if (vbstring2.equalsIgnoreCase("2")) {
                        vbstring2 = "活动待批";
                    } else if (vbstring2.equalsIgnoreCase("3")) {
                        vbstring2 = "暂停待批";
                    } else {
                        vbstring2 = "暂停的";
                    }
                    String vbstring3 = valueArray[i][3];
                    if (vbstring3.equalsIgnoreCase("1")) {
                        vbstring3 = "不能卸载";
                    } else {
                        vbstring3 = "允许卸载";
                    }
                    String vbstring4 = valueArray[i][4];
                    if (vbstring4.equalsIgnoreCase("1")) {
                        vbstring4 = "不能暂停";
                    } else {
                        vbstring4 = "允许暂停";
                    }
                    servicedata.setIpaddress(node.getIpAddress());
                    servicedata.setName(vbstring0);
                    servicedata.setInstate(vbstring1);
                    servicedata.setOpstate(vbstring2);
                    servicedata.setUninst(vbstring3);
                    servicedata.setPaused(vbstring4);
                    serviceVector.addElement(servicedata);
                }
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }

        Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(
                node.getIpAddress());
        if (ipAllData == null)
            ipAllData = new Hashtable();
        ipAllData.put("winservice", serviceVector);
        ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
        returnHash.put("winservice", serviceVector);

        try {
            if (serviceVector != null && serviceVector.size() > 0) {
                AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                List list = alarmIndicatorsUtil
                        .getAlarmInicatorsThresholdForNode(node.getId() + "",
                                "host", "windows");
                AlarmIndicatorsNode alarmIndicatorsNode2 = null;
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        AlarmIndicatorsNode alarmIndicatorsNode2_per = (AlarmIndicatorsNode) list
                                .get(i);
                        if (alarmIndicatorsNode2_per != null
                                && "service".equals(alarmIndicatorsNode2_per
                                        .getName())) {
                            alarmIndicatorsNode2 = alarmIndicatorsNode2_per;
                            break;
                        }

                    }
                    NodeUtil nodeUtil = new NodeUtil();
                    NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(node);
                    CheckEventUtil checkutil = new CheckEventUtil();
                    checkutil.createHostServiceGroupEventList(nodeDTO, serviceVector,
                                    alarmIndicatorsNode2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 把sql生成sql
        HostDatatempserciceRttosql totempsql = new HostDatatempserciceRttosql();
        totempsql.CreateResultTosql(returnHash, node);
        return returnHash;
    }
}
