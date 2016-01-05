package com.afunms.polling.snmp.interfaces;

/*
 * @author hukelei@dhcc.com.cn
 *
 */

import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtils;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.base.Node;
import com.afunms.polling.loader.HostLoader;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.IpMac;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.NetHostipmacRttosql;

public class ArpSnmp extends SnmpMonitor {

    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public ArpSnmp() {
    }

    public void collectData(Node node, MonitoredItem item) {

    }

    public void collectData(HostNode node) {

    }

    public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
        Hashtable returnHash = new Hashtable();
        Vector ipmacVector = new Vector();
        HostLoader hostLoader = new HostLoader();
        Host node = hostLoader.loadOneByID(nodeGatherIndicators.getNodeid());
        if (node == null) {
            return null;
        }
        if (!node.isManaged()) {
            return null;
        }
        try {
            // Calendar date = Calendar.getInstance();
            // try {
            // SimpleDateFormat sdf = new SimpleDateFormat(
            // "yyyy-MM-dd HH:mm:ss");
            // com.afunms.polling.base.Node snmpnode =
            // (com.afunms.polling.base.Node) PollingEngine
            // .getInstance().getNodeByIP(node.getIpAddress());
            // Date cc = date.getTime();
            // String time = sdf.format(cc);
            // snmpnode.setLastTime(time);
            // } catch (Exception e) {
            //
            // }
            // ---------------------------------------------------得到所有IpNetToMedia,即直接与该设备连接的ip
            try {
                String[] oids = new String[] { "1.3.6.1.2.1.4.22.1.1", // 1.ifIndex
                        "1.3.6.1.2.1.4.22.1.2", // 2.mac
                        "1.3.6.1.2.1.4.22.1.3", // 3.ip
                        "1.3.6.1.2.1.4.22.1.4" }; // 4.type
                String[][] valueArray = null;
                try {
                    valueArray = SnmpUtils.getTableData(node.getIpAddress(),
                            node.getCommunity(), oids, node.getSnmpversion(),
                            3, 1000 * 30);
                } catch (Exception e) {
                    valueArray = null;
                }
                for (int i = 0; i < valueArray.length; i++) {
                    IpMac ipmac = new IpMac();
                    for (int j = 0; j < 4; j++) {
                        String sValue = valueArray[i][j];
                        if (sValue == null)
                            continue;
                        if (j == 0) {
                            ipmac.setIfindex(sValue);
                        } else if (j == 1) {
                            if (sValue != null && !sValue.contains(":")) {
                                // MAC地址如：00:d0:83:04:d5:97
                                // SysLogger.info("ArpSnmp.java MAC地址为乱码：" +
                                // sValue);
                                sValue = "--";
                            }
                            ipmac.setMac(sValue);
                        } else if (j == 2) {
                            ipmac.setIpaddress(sValue);
                        }
                    }
                    ipmac.setIfband("0");
                    ipmac.setIfsms("0");
                    ipmac.setCollecttime(new GregorianCalendar());
                    ipmac.setRelateipaddr(node.getIpAddress());
                    ipmacVector.addElement(ipmac);
                }
                valueArray = null;
            } catch (Exception e) {
                e.printStackTrace();
            }

            // ---------------------------------------------------得到所有IpNetToMedia,即直接与该设备连接的ip
        } catch (Exception e) {
        }

        Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(
                node.getIpAddress());
        if (ipAllData == null)
            ipAllData = new Hashtable();
        ipAllData.put("ipmac", ipmacVector);
        ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
        returnHash.put("ipmac", ipmacVector);

        ipmacVector = null;
        ipAllData = null;
        ipmacVector = null;

        // 把采集结果生成sql
        NetHostipmacRttosql ipmactosql = new NetHostipmacRttosql();
        ipmactosql.CreateResultTosql(returnHash, node);
        return returnHash;
    }
}
