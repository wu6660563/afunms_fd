package com.afunms.polling.snmp.device;

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
import com.afunms.polling.om.Devicecollectdata;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.HostDatatempDeviceRttosql;

public class LinuxDeviceSnmp extends SnmpMonitor {

    private static java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public LinuxDeviceSnmp() {
    }

    private static Hashtable device_Status = null;
    static {
        device_Status = new Hashtable();
        device_Status.put("1", "未知");
        device_Status.put("2", "运行");
        device_Status.put("3", "告警");
        device_Status.put("4", "测试");
        device_Status.put("5", "停止");
    };
    private static Hashtable device_Type = null;
    static {
        device_Type = new Hashtable();
        device_Type.put("1.3.6.1.2.1.25.3.1.1", "其他");
        device_Type.put("1.3.6.1.2.1.25.3.1.2", "未知");
        device_Type.put("1.3.6.1.2.1.25.3.1.3", "CPU");
        device_Type.put("1.3.6.1.2.1.25.3.1.4", "网络");
        device_Type.put("1.3.6.1.2.1.25.3.1.5", "打印机");
        device_Type.put("1.3.6.1.2.1.25.3.1.6", "磁盘");
        device_Type.put("1.3.6.1.2.1.25.3.1.10", "显卡");
        device_Type.put("1.3.6.1.2.1.25.3.1.11", "声卡");
        device_Type.put("1.3.6.1.2.1.25.3.1.12", "协处理器");
        device_Type.put("1.3.6.1.2.1.25.3.1.13", "键盘");
        device_Type.put("1.3.6.1.2.1.25.3.1.14", "调制解调器");
        device_Type.put("1.3.6.1.2.1.25.3.1.15", "并口");
        device_Type.put("1.3.6.1.2.1.25.3.1.16", "打印口");
        device_Type.put("1.3.6.1.2.1.25.3.1.17", "串口");
        device_Type.put("1.3.6.1.2.1.25.3.1.18", "磁带");
        device_Type.put("1.3.6.1.2.1.25.3.1.19", "时钟");
        device_Type.put("1.3.6.1.2.1.25.3.1.20", "动态内存");
        device_Type.put("1.3.6.1.2.1.25.3.1.21", "固定内存");
    };

    public void collectData(Node node, MonitoredItem item) {

    }

    public void collectData(HostNode node) {

    }

    public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
        Hashtable returnHash = new Hashtable();
        Vector deviceVector = new Vector();
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
//            Devicecollectdata devicedata = null;
//            Calendar date = Calendar.getInstance();
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
                String[] oids = new String[] { "1.3.6.1.2.1.25.3.2.1.1", // hrDeviceIndex
                        "1.3.6.1.2.1.25.3.2.1.2", // hrDeviceType
                        "1.3.6.1.2.1.25.3.2.1.3", // hrDeviceDescr
                        "1.3.6.1.2.1.25.3.2.1.5" }; // hrDeviceStatus

                String[][] valueArray = null;
                try {
                    valueArray = SnmpUtils.getTableData(node.getIpAddress(),
                            node.getCommunity(), oids, node.getSnmpversion(),
                            3, 1000 * 30);
                } catch (Exception e) {
                    valueArray = null;
                    // SysLogger.error(node.getIpAddress() + "_WindowsSnmp");
                }
                for (int i = 0; i < valueArray.length; i++) {
                    Devicecollectdata devicedata = new Devicecollectdata();
                    String devindex = valueArray[i][0];
                    String type = valueArray[i][1];
                    String name = valueArray[i][2];
                    String status = valueArray[i][3];
                    if (status == null)
                        status = "";
                    if (device_Status.containsKey(status))
                        status = (String) device_Status.get(status);
                    devicedata.setDeviceindex(devindex);
                    devicedata.setIpaddress(node.getIpAddress());
                    devicedata.setName(name);
                    devicedata.setStatus(status);
                    devicedata.setType((String) device_Type.get(type));
                    deviceVector.addElement(devicedata);
                }
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }

        Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(
                node.getIpAddress());
        if (ipAllData == null)
            ipAllData = new Hashtable();
        ipAllData.put("device", deviceVector);
        ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
        returnHash.put("device", deviceVector);
        // 把采集结果生成sql
        HostDatatempDeviceRttosql totempsql = new HostDatatempDeviceRttosql();
        totempsql.CreateResultTosql(returnHash, node);

        return returnHash;
    }
}
