package com.afunms.polling.snmp.system;

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
import com.afunms.common.util.CommonUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtils;
import com.afunms.common.util.SysLogger;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.loader.HostLoader;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Softwarecollectdata;
import com.afunms.polling.om.Systemcollectdata;
import com.afunms.polling.snmp.SnmpMibConstants;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.NetHostDatatempSystemRttosql;

public class WindowsSystemSnmp extends SnmpMonitor {

    private static java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public WindowsSystemSnmp() {
    }

    public void collectData(Node node, MonitoredItem item) {
    }

    public void collectData(HostNode node) {
    }

    public Hashtable collect_Data(AlarmIndicatorsNode nodeGatherIndicators) {
        Hashtable returnHash = new Hashtable();
        Vector systemVector = new Vector();
        HostLoader hostLoader = new HostLoader();
        Host node = hostLoader.loadOneByID(nodeGatherIndicators.getNodeid());
        if (node == null) {
            return null;
        }
        if (!node.isManaged()) {
            return null;
        }
        try {
//            Systemcollectdata systemdata = null;
            Calendar date = Calendar.getInstance();
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
            // -------------------------------------------------------------------------------------------system
            // start
            try {

                final String[] desc = SnmpMibConstants.NetWorkMibSystemDesc;
                final String[] chname = SnmpMibConstants.NetWorkMibSystemChname;
                String[] oids = new String[] { "1.3.6.1.2.1.1.1",
                        "1.3.6.1.2.1.1.3", "1.3.6.1.2.1.1.4",
                        "1.3.6.1.2.1.1.5", "1.3.6.1.2.1.1.6", "1.3.6.1.2.1.1.7"

                };

                String[][] valueArray = null;
                try {
                    valueArray = SnmpUtils.getTableData(node.getIpAddress(),
                            node.getCommunity(), oids, node.getSnmpversion(),
                            3, 1000 * 30);
                } catch (Exception e) {
                    valueArray = null;
                }
                if (valueArray != null) {
                    for (int i = 0; i < valueArray.length; i++) {
                        for (int j = 0; j < 6; j++) {
                            Systemcollectdata systemdata = new Systemcollectdata();
                            systemdata.setIpaddress(node.getIpAddress());
                            systemdata.setCollecttime(date);
                            systemdata.setCategory("System");
                            systemdata.setEntity(desc[i]);
                            systemdata.setSubentity(desc[j]);
                            systemdata.setChname(chname[j]);
                            systemdata.setRestype("static");
                            systemdata.setUnit("");
                            String value = valueArray[i][j];
                            if (value != null) {
                                value = value.replaceAll("'", " ");
                            }
                            systemdata.setThevalue(value);
                            systemVector.addElement(systemdata);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // -------------------------------------------------------------------------------------------system
            // end

            // -------------------------------------------------------------------------------------------ip
            // mac start
            try {

                String[] oids = new String[] { "1.3.6.1.2.1.2.2.1.6" };
                String[][] valueArray = null;
                try {
                    valueArray = snmp.getTableData(node.getIpAddress(), node
                            .getCommunity(), oids);
                } catch (Exception e) {
                    valueArray = null;
                }
                Systemcollectdata systemdata = new Systemcollectdata();
                systemdata.setIpaddress(node.getIpAddress());
                systemdata.setCollecttime(date);
                systemdata.setCategory("System");
                systemdata.setEntity("MacAddr");
                systemdata.setSubentity("MacAddr");
                systemdata.setRestype("static");
                systemdata.setUnit(" ");
                if (valueArray != null) {
                    for (int i = 0; i < valueArray.length; i++) {
                        String value = valueArray[i][0];
                        if (value == null || value.length() == 0)
                            continue;
                        systemdata.setThevalue(value);
                        break;
                    }
                }
                systemVector.addElement(systemdata);

            } catch (Exception e) {
            }
            // -------------------------------------------------------------------------------------------ip
            // mac end

        } catch (Exception e) {
        }

        Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(
                node.getIpAddress());
        if (ipAllData == null)
            ipAllData = new Hashtable();
        ipAllData.put("system", systemVector);
        ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
        returnHash.put("system", systemVector);

        // 把采集结果生成sql
        NetHostDatatempSystemRttosql tosql = new NetHostDatatempSystemRttosql();
        tosql.CreateResultTosql(returnHash, node);
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
