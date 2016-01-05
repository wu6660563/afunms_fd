package com.afunms.polling.snmp.temperature;

/*
 * @author hukelei@dhcc.com.cn
 *
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtils;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.base.Node;
import com.afunms.polling.loader.HostLoader;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.NetTemperatureResultTosql;

public class H3CTemperatureSnmp extends SnmpMonitor {

    private static java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * 
     */
    public H3CTemperatureSnmp() {
    }

    public void collectData(Node node, MonitoredItem item) {

    }

    public void collectData(HostNode node) {

    }

    public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
        Hashtable returnHash = new Hashtable();
        Vector temperatureVector = new Vector();
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
            // -------------------------------------------------------------------------------------------�¶�
            // start
            try {
                if (node.getSysOid().startsWith("1.3.6.1.4.1.25506.")
                        || node.getSysOid().startsWith("1.3.6.1.4.1.2011.")) {
                    String[][] valueArray = null;
                    String[] oids = new String[] {
                    // "1.3.6.1.4.1.43.45.1.10.2.6.1.1.1.1.12"
                    "1.3.6.1.4.1.2011.10.2.6.1.1.1.1.12.14"// �¶�
                    };
                    valueArray = SnmpUtils.getCpuTableData(node.getIpAddress(),
                            node.getCommunity(), oids, node.getSnmpversion(),
                            3, 1000 * 30);
                    int allvalue = 0;
                    int flag = 0;
                    if (valueArray != null) {
                        for (int i = 0; i < valueArray.length; i++) {
                            String _value = valueArray[i][0];
                            String index = valueArray[i][1];
                            int value = 0;
                            try {
                                value = Integer.parseInt(_value);
                                allvalue = allvalue + Integer.parseInt(_value);
                            } catch (Exception e) {

                            }
                            if (value > 0) {
                                flag = flag + 1;
                                List alist = new ArrayList();
                                alist.add(index);
                                alist.add(_value);
                                Interfacecollectdata interfacedata = new Interfacecollectdata();
                                interfacedata.setIpaddress(node.getIpAddress());
                                interfacedata.setCollecttime(date);
                                interfacedata.setCategory("Temperature");
                                interfacedata.setEntity("");
                                interfacedata.setSubentity(index);
                                interfacedata.setRestype("dynamic");
                                interfacedata.setUnit("��");
                                interfacedata.setThevalue(value + "");
                                temperatureVector.addElement(interfacedata);
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
            // -------------------------------------------------------------------------------------------�¶�
            // end
        } catch (Exception e) {
        }
        Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(
                node.getIpAddress());
        if (ipAllData == null)
            ipAllData = new Hashtable();
        ipAllData.put("temperature", temperatureVector);
        ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
        returnHash.put("temperature", temperatureVector);

        // �Ѳɼ��������sql
        NetTemperatureResultTosql tosql = new NetTemperatureResultTosql();
        tosql.CreateResultTosql(returnHash, node.getIpAddress());

        return returnHash;
    }
}
