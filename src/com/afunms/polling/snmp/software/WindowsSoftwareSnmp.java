package com.afunms.polling.snmp.software;

/*
 * @author hukelei@dhcc.com.cn
 *
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtils;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.loader.HostLoader;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Softwarecollectdata;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.HostDatatempsoftwareRttosql;

public class WindowsSoftwareSnmp extends SnmpMonitor {

    private static java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public WindowsSoftwareSnmp() {
    }

    public void collectData(Node node, MonitoredItem item) {
    }

    public void collectData(HostNode node) {
    }

    public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
        Hashtable returnHash = new Hashtable();
        Vector softwareVector = new Vector();
        HostLoader hostLoader = new HostLoader();
        Host node = hostLoader.loadOneByID(nodeGatherIndicators.getNodeid());
        if (node == null) {
            return null;
        }
        if (!node.isManaged()) {
            return null;
        }
        try {
//            Softwarecollectdata softwaredata = null;
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
                String[] oids = new String[] { "1.3.6.1.2.1.25.6.3.1.2", // 名称
                        "1.3.6.1.2.1.25.6.3.1.3", // id
                        "1.3.6.1.2.1.25.6.3.1.4", // 类别
                        "1.3.6.1.2.1.25.6.3.1.5" }; // 安装日期

                String[][] valueArray = null;
                try {
                    valueArray = SnmpUtils.getTableData(node.getIpAddress(),
                            node.getCommunity(), oids, node.getSnmpversion(),
                            3, 1000);
                } catch (Exception e) {
                    valueArray = null;
                }
                if (valueArray == null) {
                    valueArray = new String[0][0];
                }
                for (int i = 0; i < valueArray.length; i++) {
                    Softwarecollectdata softwaredata = new Softwarecollectdata();
                    String name = valueArray[i][0];
                    String swid = valueArray[i][1];
                    String type = valueArray[i][2];
                    if (type.equalsIgnoreCase("4")) {
                        type = "应用软件";
                    } else {
                        type = "系统软件";
                    }
                    String insdate = valueArray[i][3];
                    String swdate = getDate(insdate);
                    softwaredata.setIpaddress(node.getIpAddress());
                    softwaredata.setName(name);
                    softwaredata.setSwid(swid);
                    softwaredata.setType(type);
                    softwaredata.setInsdate(swdate);
                    softwareVector.addElement(softwaredata);
                }
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
        Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(
                node.getIpAddress());
        if (ipAllData == null)
            ipAllData = new Hashtable();
        ipAllData.put("software", softwareVector);
        ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
        returnHash.put("software", softwareVector);

        // 把结果生成sql
        HostDatatempsoftwareRttosql totempsql = new HostDatatempsoftwareRttosql();
        totempsql.CreateResultTosql(returnHash, node);

        return returnHash;
    }

    public String getDate(String swdate) {
        String[] num = swdate.split(":");
        String num1 = Integer.valueOf(num[0], 16).toString();
        String num2 = Integer.valueOf(num[1], 16).toString();
        String num3 = Integer.valueOf(num[2], 16).toString();
        String num4 = Integer.valueOf(num[3], 16).toString();
        String num5 = Integer.valueOf(num[4], 16).toString();
        String num6 = Integer.valueOf(num[5], 16).toString();
        String num7 = Integer.valueOf(num[6], 16).toString();
        String num8 = Integer.valueOf(num[7], 16).toString();
        String swyear = Integer.parseInt(num1) * 256 + Integer.parseInt(num2)
                + "";
        String swnewdate = swyear + "-" + num3 + "-" + num4 + " " + num5 + ":"
                + num6 + ":" + num7 + ":" + num8;
        return swnewdate;

    }
}
