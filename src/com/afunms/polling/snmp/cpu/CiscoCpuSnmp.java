package com.afunms.polling.snmp.cpu;

/*
 * @author hukelei@dhcc.com.cn
 *
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtils;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.base.Node;
import com.afunms.polling.loader.HostLoader;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.NetHostDatatempCpuRTosql;
import com.gatherResulttosql.NetcpuResultTosql;

public class CiscoCpuSnmp extends SnmpMonitor {

    private static java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * 
     */
    public CiscoCpuSnmp() {
    }

    public void collectData(Node node, MonitoredItem item) {

    }

    public void collectData(HostNode node) {

    }

    public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
        Hashtable returnHash = new Hashtable();
        Vector cpuVector = new Vector();
        List cpuList = new ArrayList();
        HostLoader hostLoader = new HostLoader();
        Host node = hostLoader.loadOneByID(nodeGatherIndicators.getNodeid());
        if (node == null) {
            return null;
        }
        if (!node.isManaged()) {
            return null;
        }
        int result = 0;
        try {
            Calendar date = Calendar.getInstance();
//            try {
//                com.afunms.polling.base.Node snmpnode = (com.afunms.polling.base.Node) PollingEngine
//                        .getInstance().getNodeByIP(node.getIpAddress());
//                Date cc = date.getTime();
//                String time = sdf.format(cc);
//                snmpnode.setLastTime(time);
//            } catch (Exception e) {
//
//            }
            // -------------------------------------------------------------------------------------------cpu
            // start
            String temp = "0";
            try {
                String[] oids = new String[] { "1.3.6.1.4.1.9.2.1.57" };
                String[][] valueArray = null;
                valueArray = SnmpUtils.getCpuTableData(node.getIpAddress(),
                        node.getCommunity(), oids, node.getSnmpversion(), 3,
                        1000 * 30);
                int allvalue = 0;
                int flag = 0;

                if (valueArray != null) {
                    for (int i = 0; i < valueArray.length; i++) {
                        String _value = valueArray[i][0];
                        String index = valueArray[i][1];

                        int value = 0;
                        try {
                            if (_value != null) {
                                value = Integer.parseInt(_value);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        allvalue = allvalue + value;
                        flag = flag + 1;
                        List alist = new ArrayList();
                        alist.add(index);
                        alist.add(_value);
                        cpuList.add(alist);
                    }
                }

                if (flag > 0) {
                    int intvalue = (allvalue / flag);
                    temp = intvalue + "";
                }
                if (temp == null) {
                    result = 0;
                } else {
                    try {
                        if (temp.equalsIgnoreCase("noSuchObject")) {
                            result = 0;
                        } else {
                            result = Integer.parseInt(temp);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        result = 0;
                    }
                }
                CPUcollectdata cpudata = new CPUcollectdata();
                cpudata.setIpaddress(node.getIpAddress());
                cpudata.setCollecttime(date);
                cpudata.setCategory("CPU");
                cpudata.setEntity("Utilization");
                cpudata.setSubentity("Utilization");
                cpudata.setRestype("dynamic");
                cpudata.setUnit("%");
                cpudata.setThevalue(result + "");
                cpuVector.addElement(cpudata);
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }

        Hashtable ipAllData = new Hashtable();
        try {
            ipAllData = (Hashtable) ShareData.getSharedata().get(
                    node.getIpAddress());
        } catch (Exception e) {

        }
        if (ipAllData == null)
            ipAllData = new Hashtable();
        if (cpuVector != null && cpuVector.size() > 0)
            ipAllData.put("cpu", cpuVector);
        if (cpuList != null && cpuList.size() > 0)
            ipAllData.put("cpulist", cpuList);
        ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
        returnHash.put("cpu", cpuVector);

        // 对CPU值进行告警检测
        Hashtable collectHash = new Hashtable();
        collectHash.put("cpu", cpuVector);
        try {
            AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
            List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(
                    String.valueOf(node.getId()), AlarmConstant.TYPE_NET,
                    "cisco", "cpu");
            for (int i = 0; i < list.size(); i++) {
                AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode) list
                        .get(i);
                // 对CPU值进行告警检测
                CheckEventUtil checkutil = new CheckEventUtil();
                checkutil.checkEvent(node, alarmIndicatorsnode, result+"");
                // }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 把结果转换成sql
        NetcpuResultTosql tosql = new NetcpuResultTosql();
        tosql.CreateResultTosql(returnHash, node.getIpAddress());
        NetHostDatatempCpuRTosql totempsql = new NetHostDatatempCpuRTosql();
        totempsql.CreateResultTosql(returnHash, node);
        return returnHash;
    }
}
