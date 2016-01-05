package com.afunms.polling.snmp.disk;

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
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.loader.HostLoader;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Diskcollectdata;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.HostDatatempDiskRttosql;

public class LinuxDiskSnmp extends SnmpMonitor {

    private static java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public LinuxDiskSnmp() {
    }

    public void collectData(Node node, MonitoredItem item) {

    }

    public void collectData(HostNode node) {

    }

    public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
        Hashtable returnHash = new Hashtable();
        Vector diskVector = new Vector();
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
//            Diskcollectdata diskdata = null;
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
                String[] oids = new String[] { "1.3.6.1.2.1.25.2.3.1.1",
                        "1.3.6.1.2.1.25.2.3.1.2", "1.3.6.1.2.1.25.2.3.1.3",
                        "1.3.6.1.2.1.25.2.3.1.4", "1.3.6.1.2.1.25.2.3.1.5",
                        "1.3.6.1.2.1.25.2.3.1.6", "1.3.6.1.2.1.25.2.3.1.7" };

                String[][] valueArray = null;
                try {
                    valueArray = SnmpUtils.getTableData(node.getIpAddress(),
                            node.getCommunity(), oids, node.getSnmpversion(),
                            3, 1000 * 30);
                } catch (Exception e) {
                    valueArray = null;
                    SysLogger.error(node.getIpAddress() + "_LinuxSnmp");
                }
                for (int i = 0; i < valueArray.length; i++) {
                    Diskcollectdata diskdata = new Diskcollectdata();
                    diskdata.setIpaddress(node.getIpAddress());
                    diskdata.setCollecttime(date);
                    diskdata.setCategory("Disk");
                    diskdata.setEntity("Utilization");
                    diskdata.setRestype("static");
                    diskdata.setUnit("%");
                    String descriptions = valueArray[i][2];
                    String byteunit = valueArray[i][3];
                    String desc = "";
                    if (descriptions == null)
                        descriptions = "";
                    if (descriptions.indexOf("\\") >= 0) {
                        desc = descriptions.substring(0, descriptions
                                .indexOf("\\"))
                                + "/"
                                + descriptions.substring(descriptions
                                        .indexOf("\\") + 1, descriptions
                                        .length());
                    } else {
                        desc = descriptions;
                    }
                    diskdata.setSubentity(desc);
                    float value = 0.0f;
                    String svb4 = valueArray[i][4];
                    String svb5 = valueArray[i][5];
                    if (svb4 == null || svb5 == null)
                        continue;
                    int allsize = Integer.parseInt(svb4.trim());
                    int used = Integer.parseInt(svb5.trim());
                    if (allsize != 0) {
                        value = used * 100.0f / allsize;
                    } else {
                        value = 0.0f;
                    }
                    diskdata.setThevalue(Float.toString(value));
                    // SysLogger.info(((OctetString.fromHexString(desc))).toHexString());

                    if (diskdata.getSubentity().equals("Memory Buffers")) {
                    } else if (diskdata.getSubentity().equals("Real Memory")) {
                    } else if (diskdata.getSubentity().equals("Swap Space")) {
                        // memorydata=new Memorycollectdata();
                        // memorydata.setIpaddress(diskdata.getIpaddress());
                        // memorydata.setCollecttime(diskdata.getCollecttime());
                        // memorydata.setCategory("Memory");
                        // memorydata.setEntity("Utilization");
                        // memorydata.setSubentity("VirtualMemory");
                        // memorydata.setRestype("dynamic");
                        // memorydata.setUnit("%");
                        // memorydata.setThevalue(diskdata.getThevalue());
                        // memoryVector.addElement(memorydata);
                    } else
                        diskVector.addElement(diskdata);

                    diskdata = new Diskcollectdata();
                    diskdata.setIpaddress(node.getIpAddress());
                    diskdata.setCollecttime(date);
                    diskdata.setCategory("Disk");
                    diskdata.setEntity("AllSize");
                    diskdata.setRestype("static");
                    diskdata.setSubentity(desc);
                    float size = 0.0f;
                    size = allsize * Long.parseLong(byteunit) * 1.0f / 1024
                            / 1024;
                    String unit = "";
                    if (size >= 1024.0f) {
                        size = size / 1024;
                        diskdata.setUnit("G");
                        unit = "G";
                    } else {
                        diskdata.setUnit("M");
                        unit = "M";
                    }
                    diskdata.setThevalue(Float.toString(size));
                    // if (diskdata.getSubentity().equals("Swap Space")){
                    // memorydata=new Memorycollectdata();
                    // memorydata.setIpaddress(diskdata.getIpaddress());
                    // memorydata.setCollecttime(diskdata.getCollecttime());
                    // memorydata.setCategory("Memory");
                    // memorydata.setEntity("Capability");
                    // memorydata.setRestype("static");
                    // memorydata.setSubentity("VirtualMemory");
                    // memorydata.setUnit(diskdata.getUnit());
                    // memorydata.setThevalue(Float.toString(size));
                    // memoryVector.addElement(memorydata);
                    // }
                    if (!diskdata.getSubentity().equals("Memory Buffers")
                            && !diskdata.getSubentity().equals("Real Memory")
                            && !diskdata.getSubentity().equals("Swap Space"))
                        diskVector.addElement(diskdata);
                    diskdata = new Diskcollectdata();
                    diskdata.setIpaddress(node.getIpAddress());
                    diskdata.setCollecttime(date);
                    diskdata.setCategory("Disk");
                    diskdata.setEntity("UsedSize");
                    diskdata.setRestype("static");
                    diskdata.setSubentity(desc);
                    size = used * Long.parseLong(byteunit) * 1.0f / 1024 / 1024;
                    if ("G".equals(unit)) {
                        size = size / 1024;
                        diskdata.setUnit("G");
                    } else {
                        diskdata.setUnit("M");
                    }
                    diskdata.setThevalue(Float.toString(size));
                    if (!diskdata.getSubentity().equals("Memory Buffers")
                            && !diskdata.getSubentity().equals("Real Memory")
                            && !diskdata.getSubentity().equals("Swap Space"))
                        diskVector.addElement(diskdata);
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
        ipAllData.put("disk", diskVector);
        ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
        returnHash.put("disk", diskVector);
        // 把采集结果生成sql
        HostDatatempDiskRttosql temptosql = new HostDatatempDiskRttosql();
        temptosql.CreateResultTosql(returnHash, node);
        return returnHash;
    }
}
