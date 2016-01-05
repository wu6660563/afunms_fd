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

/**
 * @author Administrator
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class H3CCpuSnmp extends SnmpMonitor {
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * 
     */
    public H3CCpuSnmp() {
    }

    public void collectData(Node node, MonitoredItem item) {

    }

    public void collectData(HostNode node) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.dhcc.webnms.host.snmp.AbstractSnmp#collectData()
     */
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
            CPUcollectdata cpudata = null;
            Calendar date = Calendar.getInstance();

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
                    // temp =
                    // snmp.getMibValue(host.getIpAddress(),host.getCommunity(),"1.3.6.1.4.1.2011.6.1.1.1.4.0");
                    String[][] valueArray = null;
                    String[] oids = new String[] { "1.3.6.1.4.1.2011.6.1.1.1.4" };
                    String[] oids2 = new String[] { "1.3.6.1.4.1.2011.10.2.6.1.1.1.1.6" };

                    // -----1.3.6.1.4.1.2011.2.26. heiweine08
                    // if(node.getSysOid().trim().equals("1.3.6.1.4.1.2011.2.26.2")
                    // && !node.getIpAddress().toString().equals("10.15.225.2"))
                    // {
                    //		   		    	
                    // //System.out.println("======heiweine08-----"+node.getIpAddress());
                    // oids =
                    // new String[] {
                    // "1.3.6.1.4.1.2011.5.1.1.1.4"};
                    // oids2 = new String[]
                    // {"1.3.6.1.4.1.2011.5.25.31.1.1.1.1.6"};
                    // //System.out.println("======heiweine08-----"+node.getIpAddress());
                    // }

                    // -----1.3.6.1.4.1.2011.2.31 NE40
                    if (node.getSysOid().trim().equals("1.3.6.1.4.1.2011.2.31")) {
                        oids = new String[] { "1.3.6.1.4.1.2011.2.17.4.4.1.7" };
                        oids2 = new String[] { "1.3.6.1.4.1.2011.2.17.4.4.1.7" };
                    }

                    if (node.getSysOid().trim().equals(
                            "1.3.6.1.4.1.2011.2.62.2.5")
                            || node.getSysOid().trim().equals(
                                    "1.3.6.1.4.1.2011.2.88.2")) {

                        oids = new String[] { "1.3.6.1.4.1.2011.5.25.31.1.1.1.1.5" };
                        oids2 = new String[] { "1.3.6.1.4.1.2011.5.25.31.1.1.1.1.5" };
                    }

                    if (node.getSysOid().trim().equals(
                            "1.3.6.1.4.1.2011.2.62.2.3")) {

                        oids = new String[] { "1.3.6.1.4.1.2011.5.25.31.1.1.1.1.5" };
                        oids2 = new String[] { "1.3.6.1.4.1.2011.5.25.31.1.1.1.1.5" };
                    }

                    if (node.getSysOid().trim().equals(
                            "1.3.6.1.4.1.2011.2.62.2.9")) {

                        oids = new String[] { "1.3.6.1.4.1.2011.5.25.31.1.1.1.1.5" };
                        oids2 = new String[] { "1.3.6.1.4.1.2011.5.25.31.1.1.1.1.5" };
                    }

                    if (node.getSysOid().trim().equals(
                            "1.3.6.1.4.1.2011.2.23.97")) {

                        oids = new String[] { "1.3.6.1.4.1.2011.5.25.31.1.1.1.1.5" };
                        oids2 = new String[] { "1.3.6.1.4.1.2011.5.25.31.1.1.1.1.5" };
                    }

                    if (node.getSysOid().trim().equals(
                            "1.3.6.1.4.1.2011.10.1.88")) {

                        oids = new String[] { "1.3.6.1.4.1.2011.5.12.2.1.1.1.1.5" };
                        oids2 = new String[] { "1.3.6.1.4.1.2011.5.12.2.1.1.1.1.5" };
                    }

                    if (node.getSysOid().trim().equals(
                            "1.3.6.1.4.1.2011.2.170.2")) {

                        oids = new String[] { "1.3.6.1.4.1.2011.6.3.4.1.2" };
                        oids2 = new String[] { "1.3.6.1.4.1.2011.6.3.4.1.2" };
                    }

                    if (node.getSysOid().trim().equals(
                            "1.3.6.1.4.1.2011.2.170.3")) {

                        oids = new String[] { "1.3.6.1.4.1.2011.6.3.4.1.2" };
                        oids2 = new String[] { "1.3.6.1.4.1.2011.6.3.4.1.2" };
                    }

                    valueArray = snmp.getCpuTableData(node.getIpAddress(), node
                            .getCommunity(), oids);
                    if (valueArray == null || valueArray.length == 0) {// yangjun
                        valueArray = snmp.getCpuTableData(node.getIpAddress(),
                                node.getCommunity(), oids2);
                    }
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
                            if (value > 0) {
                                List alist = new ArrayList();
                                alist.add(index);
                                alist.add(value + "");
                                cpuList.add(alist);
                            }
                        }
                    }

                    if (flag > 0) {
                        int intvalue = (allvalue / flag);
                        temp = intvalue + "";
                    }
                } else if (node.getSysOid().startsWith("1.3.6.1.4.1.25506.")) {
                    String[][] valueArray = null;
                    String[] oids = new String[] { "1.3.6.1.4.1.2011.10.2.6.1.1.1.1.6"// CPU������
                    };
                    String[] oids2 = new String[] { "1.3.6.1.4.1.25506.2.6.1.1.1.1.6" };
                    valueArray = SnmpUtils.getCpuTableData(node.getIpAddress(),
                            node.getCommunity(), oids, node.getSnmpversion(),
                            3, 30000);
                    if (valueArray == null || valueArray.length == 0) {// hukelei
                        valueArray = SnmpUtils.getCpuTableData(node
                                .getIpAddress(), node.getCommunity(), oids2,
                                node.getSnmpversion(), 3, 30000);

                    }
                    int allvalue = 0;
                    int flag = 0;
                    if (valueArray != null) {
                        for (int i = 0; i < valueArray.length; i++) {

                            String _value = valueArray[i][0];
                            String index = valueArray[i][1];

                            int value = 0;
                            value = Integer.parseInt(_value);
                            allvalue = allvalue + Integer.parseInt(_value);
                            if (value > 0) {
                                flag = flag + 1;
                                List alist = new ArrayList();
                                alist.add(index);
                                alist.add(_value);
                                cpuList.add(alist);
                            }
                        }
                    }

                    if (flag > 0) {
                        int intvalue = (allvalue / flag);
                        temp = intvalue + "";
                    }
                }

                if (temp == null) {
                    result = 0;
                } else {
                    try {
                        if (temp.equalsIgnoreCase("noSuchObject")) {
                            result = 0;
                        } else
                            result = Integer.parseInt(temp);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        result = 0;
                    }
                }
                cpudata = new CPUcollectdata();
                cpudata.setIpaddress(node.getIpAddress());
                cpudata.setCollecttime(date);
                cpudata.setCategory("CPU");
                cpudata.setEntity("Utilization");
                cpudata.setSubentity("Utilization");
                cpudata.setRestype("dynamic");
                cpudata.setUnit("%");
                cpudata.setThevalue(result + "");

                cpuVector.add(0, cpudata);
                cpuVector.add(1, cpuList);
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

        // ��CPUֵ���и澯���
        Hashtable collectHash = new Hashtable();
        collectHash.put("cpu", cpuVector);

        try {
            AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
            List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(
                    String.valueOf(node.getId()), AlarmConstant.TYPE_NET,
                    "h3c", "cpu");

            for (int i = 0; i < list.size(); i++) {

                AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode) list
                        .get(i);
                CheckEventUtil checkutil = new CheckEventUtil();
                checkutil.checkEvent(node, alarmIndicatorsnode, result+"");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // �ѽ��ת����sql
        NetcpuResultTosql tosql = new NetcpuResultTosql();
        tosql.CreateResultTosql(returnHash, node.getIpAddress());
        NetHostDatatempCpuRTosql totempsql = new NetHostDatatempCpuRTosql();
        totempsql.CreateResultTosql(returnHash, node);
        return returnHash;
    }
}
