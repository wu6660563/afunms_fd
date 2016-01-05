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
import com.afunms.common.util.SysLogger;
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

public class H3CMemorySnmp extends SnmpMonitor {

    private static java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private static DecimalFormat df = new DecimalFormat("#.##");

    private static final SysLogger logger = SysLogger.getLogger(H3CMemorySnmp.class);
    public H3CMemorySnmp() {
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
                Double usedvalueperc = 0D;
                Double allvalue = 0D;
                int flag = 0;
                String memtype = "";// 有两中类型 memp 代表百分比，memsize 代表大小

                if (node.getSysOid().startsWith("1.3.6.1.4.1.2011.")) {
                    String[][] valueArray = null;
                    String[] oids = new String[] {
                            "1.3.6.1.4.1.2011.6.1.2.1.1.2",// hwMemSize
                            "1.3.6.1.4.1.2011.6.1.2.1.1.3"// hwMemFree
                    };
                    memtype = "memsize";

                    if (node.getSysOid().equals("1.3.6.1.4.1.2011.1.1.1.12811")
                            || node.getSysOid().equals(
                                    "1.3.6.1.4.1.2011.10.1.89")) {

                        oids = new String[] { "1.3.6.1.4.1.2011.2.2.5.1",// 已用内存
                                "1.3.6.1.4.1.2011.2.2.5.2"// 空闲内存
                        };
                        memtype = "memsize";
                    }

                    if (node.getSysOid().equals("1.3.6.1.4.1.2011.2.170.2")) {

                        oids = new String[] { "1.3.6.1.4.1.2011.6.3.5.1.1.2",// 已用内存
                                "1.3.6.1.4.1.2011.6.3.5.1.1.3"// 空闲内存
                        };
                        memtype = "memsize";
                    }

                    if (node.getSysOid().equals("1.3.6.1.4.1.2011.2.170.3")) {

                        oids = new String[] { "1.3.6.1.4.1.2011.6.3.5.1.1.2",// 已用内存
                                "1.3.6.1.4.1.2011.6.3.5.1.1.3"// 空闲内存
                        };
                        memtype = "memsize";
                    }

                    if (node.getSysOid().equals("1.3.6.1.4.1.2011.2.45")) {

                        oids = new String[] { "1.3.6.1.4.1.2011.10.2.6.1.1.1.1.8"// 内存利用率
                        };
                        memtype = "memp";
                    }

                    if (node.getSysOid().equals("1.3.6.1.4.1.2011.2.31")) {
                        oids = new String[] { "1.3.6.1.4.1.2011.2.17.6.9.1.2"// 内存利用率
                        };
                        memtype = "memp";

                    }

                    if (node.getSysOid().equals("1.3.6.1.4.1.2011.2.62.2.5")
                            || node.getSysOid().equals(
                                    "1.3.6.1.4.1.2011.2.88.2")) {
                        oids = new String[] { "1.3.6.1.4.1.2011.5.25.31.1.1.1.1.7"// 内存利用率
                        };
                        memtype = "memp";

                    }

                    if (node.getSysOid().equals("1.3.6.1.4.1.2011.2.62.2.9")) {
                        oids = new String[] { "1.3.6.1.4.1.2011.5.25.31.1.1.1.1.7"// 内存利用率
                        };
                        memtype = "memp";

                    }

                    if (node.getSysOid().equals("1.3.6.1.4.1.2011.2.62.2.3")) {
                        oids = new String[] { "1.3.6.1.4.1.2011.5.25.31.1.1.1.1.7"// 内存利用率
                        };
                        memtype = "memp";

                    }

                    if (node.getSysOid().equals("1.3.6.1.4.1.2011.2.23.97")) {
                        oids = new String[] { "1.3.6.1.4.1.2011.5.25.31.1.1.1.1.7"// 内存利用率
                        };
                        memtype = "memp";

                    }

                    valueArray = snmp.getTemperatureTableData(node
                            .getIpAddress(), node.getCommunity(), oids);
                    
                    if (valueArray != null && valueArray.length > 0) {

                        // 根据不同的类型来判断
                        if (memtype.equals("memp")) {
                            for (int i = 0; i < valueArray.length; i++) {
                                String _value = valueArray[i][0];
                                String index = valueArray[i][1];
                                Double value = 0D;
                                try {
                                    value = Double.valueOf(_value);
                                } catch (Exception e) {
                                }
                                allvalue = allvalue + value;
                                String thevalue = "0";
                                try {
                                    thevalue = df.format(value);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                
                                flag = flag + 1;
                                List alist = new ArrayList();
                                alist.add(index);
                                alist.add(_value);
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
                                memorycollectdata.setThevalue(thevalue);
                                memoryVector.addElement(memorycollectdata);
                            }

                        }

                        if (memtype.equals("memsize")) {
                            logger.info(node.getIpAddress() + "=======================" + node.getSysOid());
                            for (int i = 0; i < valueArray.length; i++) {
                                String sizevalue = valueArray[i][0];
                                String freevalue = valueArray[i][1];
                                String index = valueArray[i][2];
                                Double value = 0.0D;
                                String usedperc = "0";
                                
                                try {
                                    if (Long.parseLong(sizevalue) > 0)
                                        value = (Long.parseLong(sizevalue) 
                                                * 100D / ((Long.parseLong(sizevalue)) + Long
                                                .parseLong(freevalue)));
                                } catch (Exception e) {
                                }
                                allvalue = allvalue + value;
                                String thevalue = "0";
                                try {
                                    thevalue = df.format(value);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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
                                memorycollectdata
                                        .setThevalue(thevalue);
                                memoryVector.addElement(memorycollectdata);
                            }
                        }

                        if (node.getSysOid().equals(
                                "1.3.6.1.4.1.2011.1.1.1.12811")
                                || node.getSysOid().equals(
                                        "1.3.6.1.4.1.2011.10.1.89")) {
                            for (int i = 0; i < valueArray.length; i++) {
                                String sizevalue = valueArray[i][0];
                                String freevalue = valueArray[i][1];
                                String index = valueArray[i][2];
                                Double value = 0.0D;
                                String usedperc = "0";
                                
                                try {
                                    if (Long.parseLong(sizevalue) > 0)
                                        value = (Long.parseLong(sizevalue) 
                                                * 100D / ((Long.parseLong(sizevalue)) + Long
                                                .parseLong(freevalue)));
                                } catch (Exception e) {
                                }
                                allvalue = allvalue + value;
                                String thevalue = "0";
                                try {
                                    thevalue = df.format(value);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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
                                memorycollectdata
                                        .setThevalue(thevalue);
                                memoryVector.addElement(memorycollectdata);
                            }
                        }

                        if (flag > 0) {
                            usedvalueperc = allvalue / flag;
                        }
                    }
                } else if (node.getSysOid().startsWith("1.3.6.1.4.1.25506.")) {
                    String[][] valueArray = null;
                    String[] oids = new String[] { "1.3.6.1.4.1.2011.10.2.6.1.1.1.1.8"// 内存利用率
                    };

                    if (node.getSysOid().equals("1.3.6.1.4.1.25506.1.194")
                            || node.getSysOid()
                                    .equals("1.3.6.1.4.1.25506.1.76")
                            || node.getSysOid()
                                    .equals("1.3.6.1.4.1.25506.1.73")) {
                        oids = new String[] { "1.3.6.1.4.1.25506.2.6.1.1.1.1.8"// 内存利用率
                        };

                    }

                    if (node.getSysOid().equals("1.3.6.1.4.1.25506.1.2")) {
                        oids = new String[] { "1.3.6.1.4.1.25506.2.6.1.1.1.1.8"// 内存利用率
                        };

                    }

                    if (node.getSysOid().equals("1.3.6.1.4.1.25506.1.515")) {
                        oids = new String[] { "1.3.6.1.4.1.25506.2.6.1.1.1.1.8"// 内存利用率
                        };

                    }

                    valueArray = snmp.getCpuTableData(node.getIpAddress(), node
                            .getCommunity(), oids);
                    if (valueArray != null && valueArray.length > 0) {
                        for (int i = 0; i < valueArray.length; i++) {
                            String _value = valueArray[i][0];
                            String index = valueArray[i][1];
                            Double value = 0D;
                            try {
                                value = Double.valueOf(_value);
                            } catch (Exception e) {
                            }
                            allvalue = allvalue + value;
                            String thevalue = "0";
                            try {
                                thevalue = df.format(value);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            
                            flag = flag + 1;
                            List alist = new ArrayList();
                            alist.add(index);
                            alist.add(_value);
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
                            memorycollectdata.setThevalue(thevalue);
                            memoryVector.addElement(memorycollectdata);
                        }
                    }
                }
                if (flag > 0) {
                    usedvalueperc = allvalue / flag;
                }
                try {
                    allthevalue = df.format(usedvalueperc);
                } catch (Exception e) {
                    e.printStackTrace();
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
        if (memoryVector != null && memoryVector.size() > 0)
            ipAllData.put("memory", memoryVector);
        ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
        returnHash.put("memory", memoryVector);

        Hashtable collectHash = new Hashtable();
        collectHash.put("memory", memoryVector);

        ipAllData = null;
        memoryVector = null;

        // 对平均内存值进行告警检测
        try {
            AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
            List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(
                    String.valueOf(node.getId()), AlarmConstant.TYPE_NET,
                    "h3c", "memory");
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
