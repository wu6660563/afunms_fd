package com.afunms.polling.snmp.memory;

/*
 * @author hukelei@dhcc.com.cn
 *
 */

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.loader.HostLoader;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.Memorycollectdata;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.NetHostMemoryRtsql;
import com.gatherResulttosql.NetmemoryResultTosql;

public class CiscoMemorySnmp extends SnmpMonitor {

    private static java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private static DecimalFormat df = new DecimalFormat("#.##");

    public CiscoMemorySnmp() {
    }

    public void collectData(Node node, MonitoredItem item) {

    }

    public void collectData(HostNode node) {

    }

    public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
        // yangjun
        Hashtable returnHash = new Hashtable();
        Vector memoryVector = new Vector();
        List memoryList = new ArrayList();
        HostLoader hostLoader = new HostLoader();
        Host node = hostLoader.loadOneByID(nodeGatherIndicators.getNodeid());
        if (node == null) {
            return null;
        }
        if (!node.isManaged()) {
            return null;
        }
        String allthevalue = "0";
        try {
//            CPUcollectdata cpudata = null;
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
                if (node.getSysOid().startsWith("1.3.6.1.4.1.9.")) {
                    String[][] valueArray = null;
                    String[] oids = new String[] {
                            "1.3.6.1.4.1.9.9.48.1.1.1.5", // ciscoMemoryPoolUsed
                            "1.3.6.1.4.1.9.9.48.1.1.1.6"  // ciscoMemoryPoolFree
                    };
                    valueArray = SnmpUtils.getTemperatureTableData(node
                            .getIpAddress(), node.getCommunity(), oids, node
                            .getSnmpversion(), 3, 1000 * 30);
                    int flag = 0;
                    double allvalue = 0;
                    if (valueArray != null) {
                        for (int i = 0; i < valueArray.length; i++) {
                            String usedvalue = valueArray[i][0];
                            String freevalue = valueArray[i][1];
                            String index = valueArray[i][2];
                            float value = 0.0f;
                            String usedperc = "0";
                            try {
                                if (Long.parseLong(freevalue)
                                        + Long.parseLong(usedvalue) > 0)
                                    value = Long.parseLong(usedvalue)
                                            * 100
                                            / (Long.parseLong(freevalue) + Long
                                                    .parseLong(usedvalue));
                            } catch (Exception e) {

                            }
                            String thevalue = df.format(value);
                            allvalue += value;
                            flag = flag + 1;
                            List alist = new ArrayList();
                            alist.add("");
                            alist.add(usedperc);
                            // 内存
                            memoryList.add(alist);
                            Memorycollectdata memorycollectdata = new Memorycollectdata();
                            memorycollectdata.setIpaddress(node
                                    .getIpAddress());
                            memorycollectdata.setCollecttime(date);
                            memorycollectdata.setCategory("Memory");
                            memorycollectdata.setEntity("Utilization");
                            memorycollectdata.setSubentity(index);
                            memorycollectdata.setRestype("dynamic");
                            memorycollectdata.setUnit("%");
                            memorycollectdata.setThevalue(thevalue + "");
                            memoryVector.addElement(memorycollectdata);

                        }
                        if (flag > 0) {
                            try {
                                allthevalue = df.format(allvalue / flag);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Memorycollectdata memorycollectdata = new Memorycollectdata();
                        memorycollectdata.setIpaddress(node
                                .getIpAddress());
                        memorycollectdata.setCollecttime(date);
                        memorycollectdata.setCategory("Memory");
                        memorycollectdata.setEntity("Utilization");
                        memorycollectdata.setSubentity("avg");
                        memorycollectdata.setRestype("dynamic");
                        memorycollectdata.setUnit("%");
                        memorycollectdata.setThevalue(allthevalue);
                        memoryVector.addElement(memorycollectdata);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
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
        if (memoryVector != null && memoryVector.size() > 0)
            ipAllData.put("memory", memoryVector);
        ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
        returnHash.put("memory", memoryVector);

        // 对平均内存值进行告警检测
        try {
            AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
            List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(
                    String.valueOf(node.getId()), AlarmConstant.TYPE_NET,
                    "cisco", "memory");
            for (int i = 0; i < list.size(); i++) {
                AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode) list
                        .get(i);
                // 对平均内存值进行告警检测
                CheckEventUtil checkutil = new CheckEventUtil();
                checkutil.checkEvent(node, alarmIndicatorsnode, allthevalue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 把采集结果生成sql
        NetmemoryResultTosql tosql = new NetmemoryResultTosql();
        tosql.CreateResultTosql(returnHash, node.getIpAddress());
        NetHostMemoryRtsql totempsql = new NetHostMemoryRtsql();
        totempsql.CreateResultTosql(returnHash, node);

        return returnHash;
    }
}
