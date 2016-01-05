package com.afunms.polling.snmp.voltage;

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
import com.gatherResulttosql.NetDatatempvoltageRtosql;
import com.gatherResulttosql.NetvoltageResultTosql;

public class CiscoVoltageSnmp extends SnmpMonitor {

    private static java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public CiscoVoltageSnmp() {
    }

    public void collectData(Node node, MonitoredItem item) {
    }

    public void collectData(HostNode node) {
    }

    public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
        Hashtable returnHash = new Hashtable();
        Vector voltageVector = new Vector();
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
                // -------------------------------------------------------------------------------------------电压
                // start
                if (node.getSysOid().startsWith("1.3.6.1.4.1.9.")) {
                    String[][] valueArray = null;
                    String[] oids = new String[] {
                            "1.3.6.1.4.1.9.9.13.1.2.1.3",// ciscoEnvMonVoltageStatusValue
                            "1.3.6.1.4.1.9.9.13.1.2.1.7"// ciscoEnvMonVoltageState
                    };
                    valueArray = SnmpUtils.getTemperatureTableData(node
                            .getIpAddress(), node.getCommunity(), oids, node
                            .getSnmpversion(), 3, 1000 * 30);
                    int flag = 0;
                    if (valueArray != null) {
                        for (int i = 0; i < valueArray.length; i++) {
                            String _value = valueArray[i][0];
                            String index = valueArray[i][1];
                            String state = valueArray[i][2];
                            int value = 0;
                            try {
                                value = Integer.parseInt(_value);
                            } catch (Exception e) {

                            }
                            flag = flag + 1;
                            List alist = new ArrayList();
                            alist.add(index);
                            alist.add(_value);
                            alist.add(state);
                            Interfacecollectdata interfacedata = new Interfacecollectdata();
                            interfacedata.setIpaddress(node.getIpAddress());
                            interfacedata.setCollecttime(date);
                            interfacedata.setCategory("Voltage");
                            interfacedata.setEntity(index);
                            interfacedata.setSubentity(state);
                            interfacedata.setRestype("dynamic");
                            interfacedata.setUnit("");
                            interfacedata.setThevalue(_value);
                            voltageVector.addElement(interfacedata);
                        }
                    }
                }
            } catch (Exception e) {
            }
            // -------------------------------------------------------------------------------------------电压
            // end
        } catch (Exception e) {
        }

        Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(
                node.getIpAddress());
        if (ipAllData == null)
            ipAllData = new Hashtable();
        ipAllData.put("voltage", voltageVector);
        ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
        returnHash.put("voltage", voltageVector);

        // 把采集结果生成sql
        NetvoltageResultTosql tosql = new NetvoltageResultTosql();
        tosql.CreateResultTosql(returnHash, node.getIpAddress());
        NetDatatempvoltageRtosql totempsql = new NetDatatempvoltageRtosql();
        totempsql.CreateResultTosql(returnHash, node);

        return returnHash;
    }
}
