package com.afunms.polling.snmp.power;

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

import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtils;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.loader.HostLoader;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.NetDatatemppowerRtosql;
import com.gatherResulttosql.NetpowerResultTosql;

public class H3CPowerSnmp extends SnmpMonitor {

    private static java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public H3CPowerSnmp() {
    }

    public void collectData(Node node, MonitoredItem item) {
    }

    public void collectData(HostNode node) {
    }

    public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
        Hashtable returnHash = new Hashtable();
        Vector powerVector = new Vector();
        HostLoader hostLoader = new HostLoader();
        Host node = hostLoader.loadOneByID(nodeGatherIndicators.getNodeid());
        if (node == null) {
            return null;
        }
        if (!node.isManaged()) {
            return null;
        }
        try {
//            Interfacecollectdata interfacedata = new Interfacecollectdata();
            Calendar date = Calendar.getInstance();
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
                String temp = "0";
                if (node.getSysOid().startsWith("1.3.6.1.4.1.2011.")) {
                    String[][] valueArray = null;
                    String[] oids = new String[] {
                            "1.3.6.1.4.1.2011.2.23.1.9.1.2.1.1",// hwDevMPowerNum
                            "1.3.6.1.4.1.2011.2.23.1.9.1.2.1.2"// hwDevMPowerStatus
                    };
                    valueArray = SnmpUtils.getTemperatureTableData(node
                            .getIpAddress(), node.getCommunity(), oids, node
                            .getSnmpversion(), 2, 1000);
                    int flag = 0;
                    if (valueArray != null) {
                        for (int i = 0; i < valueArray.length; i++) {
                            String _value = valueArray[i][1];
                            String index = valueArray[i][2];
                            String desc = valueArray[i][0];
                            int value = 0;
                            value = Integer.parseInt(_value);
                            flag = flag + 1;
                            List alist = new ArrayList();
                            alist.add(index);
                            alist.add(_value);
                            alist.add(desc);
                            Interfacecollectdata interfacedata = new Interfacecollectdata();
                            interfacedata.setIpaddress(node.getIpAddress());
                            interfacedata.setCollecttime(date);
                            interfacedata.setCategory("Power");
                            interfacedata.setEntity(index);
                            interfacedata.setSubentity(desc);
                            interfacedata.setRestype("dynamic");
                            interfacedata.setUnit("");
                            interfacedata.setThevalue(_value);
                            powerVector.addElement(interfacedata);
                        }
                    }
                } else if (node.getSysOid().startsWith("1.3.6.1.4.1.25506.")) {
                    String[][] valueArray = null;
                    String[] oids = new String[] {
                            "1.3.6.1.4.1.2011.2.23.1.9.1.2.1.1",// hwDevMPowerNum
                            "1.3.6.1.4.1.2011.2.23.1.9.1.2.1.2"// hwDevMPowerStatus
                    };
                    valueArray = SnmpUtils.getTemperatureTableData(node
                            .getIpAddress(), node.getCommunity(), oids, node
                            .getSnmpversion(), 2, 1000);
                    int flag = 0;
                    if (valueArray != null) {
                        for (int i = 0; i < valueArray.length; i++) {
                            String _value = valueArray[i][1];
                            String index = valueArray[i][2];
                            String desc = valueArray[i][0];
                            int value = 0;
                            value = Integer.parseInt(_value);
                            flag = flag + 1;
                            List alist = new ArrayList();
                            alist.add(index);
                            alist.add(_value);
                            alist.add(desc);
                            // powerList.add(alist);
                            Interfacecollectdata interfacedata = new Interfacecollectdata();
                            interfacedata.setIpaddress(node.getIpAddress());
                            interfacedata.setCollecttime(date);
                            interfacedata.setCategory("Power");
                            interfacedata.setEntity(index);
                            interfacedata.setSubentity(desc);
                            interfacedata.setRestype("dynamic");
                            interfacedata.setUnit("");
                            interfacedata.setThevalue(_value);
                            powerVector.addElement(interfacedata);
                        }
                    }
                }
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }

        Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(
                node.getIpAddress());
        if (ipAllData == null)
            ipAllData = new Hashtable();
        ipAllData.put("power", powerVector);
        ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
        returnHash.put("power", powerVector);

        powerVector = null;
        ipAllData = null;

        // 把采集结果生成sql
        NetpowerResultTosql tosql = new NetpowerResultTosql();
        tosql.CreateResultTosql(returnHash, node.getIpAddress());
        NetDatatemppowerRtosql totempsql = new NetDatatemppowerRtosql();
        totempsql.CreateResultTosql(returnHash, node);
        return returnHash;
    }
}
