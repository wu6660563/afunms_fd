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
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.base.Node;
import com.afunms.polling.loader.HostLoader;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.HostcpuResultTosql;
import com.gatherResulttosql.NetHostDatatempCpuRTosql;

/**
 * @author Administrator
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class WindowsCpuSnmp extends SnmpMonitor {

    private static java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * 
     */
    public WindowsCpuSnmp() {
    }

    public void collectData(Node node, MonitoredItem item) {

    }

    public void collectData(HostNode node) {

    }

    public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
        // yangjun
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
        try {
            Calendar date = Calendar.getInstance();
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
            // -------------------------------------------------------------------------------------------cpu
            // start
            int result = 0;
            try {
                String temp = "0";
                String[] oids = new String[] { "1.3.6.1.2.1.25.3.3.1.2" };
                String[][] valueArray = null;
                valueArray = snmp.getCpuTableData(node.getIpAddress(), node
                        .getCommunity(), oids);
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

                Hashtable collectHash = new Hashtable();
                collectHash.put("cpu", cpuVector);

                // 对CPU值进行告警检测
                try {
                    CheckEventUtil checkutil = new CheckEventUtil();
                    AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                    List list = alarmIndicatorsUtil
                            .getAlarmInicatorsThresholdForNode(String
                                    .valueOf(node.getId()),
                                    AlarmConstant.TYPE_HOST, "windows", "cpu");
                    for (int i = 0; i < list.size(); i++) {
                        AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode) list
                                .get(i);
                        checkutil.checkEvent(node, alarmIndicatorsnode, result
                                + "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

        ipAllData = null;
        cpuVector = null;
        cpuList = null;

        HostcpuResultTosql restosql = new HostcpuResultTosql();
        restosql.CreateResultTosql(returnHash, node.getIpAddress());

        // 把结果转换成sql
        NetHostDatatempCpuRTosql totempsql = new NetHostDatatempCpuRTosql();
        totempsql.CreateResultTosql(returnHash, node);

        return returnHash;
    }

}
