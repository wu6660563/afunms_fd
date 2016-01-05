package com.afunms.polling.snmp.interfaces;

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

import net.sf.ezmorph.primitive.IntMorpher;

import montnets.SmsDao;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.send.SendMailAlarm;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.Arith;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.CommonUtil;
import com.afunms.common.util.NodeAlarmUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtils;
import com.afunms.common.util.SysLogger;
import com.afunms.config.model.Portconfig;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.EventList;
import com.afunms.event.model.Smscontent;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.base.Node;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.loader.HostLoader;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.AllUtilHdx;
import com.afunms.polling.om.DiscardsPerc;
import com.afunms.polling.om.ErrorsPerc;
import com.afunms.polling.om.InPkts;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.OutPkts;
import com.afunms.polling.om.Packs;
import com.afunms.polling.om.Task;
import com.afunms.polling.om.UtilHdx;
import com.afunms.polling.om.UtilHdxPerc;
import com.afunms.polling.snmp.SnmpMibConstants;
import com.afunms.polling.task.TaskXml;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.NetInterfaceDataTemptosql;
import com.gatherResulttosql.NetinterfaceResultTosql;

/**
 * @author Administrator
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class InterfaceSnmp extends SnmpMonitor {

    private static Hashtable ifEntity_ifStatus = null;
    static {
        ifEntity_ifStatus = new Hashtable();
        ifEntity_ifStatus.put("1", "up");
        ifEntity_ifStatus.put("2", "down");
        ifEntity_ifStatus.put("3", "testing");
        ifEntity_ifStatus.put("5", "unknow");
        ifEntity_ifStatus.put("7", "unknow");
    };
    
    public static Hashtable Interface_IfType = null;
    static {
        Interface_IfType = new Hashtable();
        Interface_IfType.put("1", "other(1)");
        Interface_IfType.put("6", "ethernetCsmacd(6)");
        Interface_IfType.put("23", "ppp(23)");
        Interface_IfType.put("28", "slip(28)");
        Interface_IfType.put("33", "Console port");
        Interface_IfType.put("53", "propVirtual(53)");
        Interface_IfType.put("117", "gigabitEthernet(117)");

        Interface_IfType.put("131", "tunnel(131)");

        Interface_IfType.put("135", "others(135)");
        Interface_IfType.put("136", "others(136)");
        Interface_IfType.put("142", "others(142)");
        Interface_IfType.put("54", "others(54)");
        Interface_IfType.put("5", "others(5)");

    }

    private static java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private static Long INT_MAX = 4294967296L;

    private static DecimalFormat df = new DecimalFormat("#.##");

    public static final String DESC_IFINDEX = "index";
    
    public static final String DESC_IFDESCR = "ifDescr";
    
    public static final String DESC_IFTYPE = "ifType";
    
    public static final String DESC_IFMTU = "ifMtu";
    
    public static final String DESC_IFSPEED = "ifSpeed";
    
    public static final String DESC_IFPHYSADDRESS = "ifPhysAddress";

    public static final String DESC_IFADMINSTATUS = "ifAdminStatus";
    
    public static final String DESC_IFOPERSTATUS = "ifOperStatus";
    
    public static final String DESC_IFLASTCHANGE = "ifLastChange";

    public static final String DESC_IFINOCTETS = "ifInOctets";

    public static final String DESC_IFOUTOCTETS = "ifOutOctets";

    public static final String DESC_IFINMULTICASTPKTS = "ifInMulticastPkts";

    public static final String DESC_IFOUTMULTICASTPKTS = "ifOutMulticastPkts";

    public static final String DESC_IFINBROADCASTPKTS = "ifInBroadcastPkts";

    public static final String DESC_IFOUTBROADCASTPKTS = "ifOutBroadcastPkts";

    public static final String DESC_IFINPKTS = "ifInPkts";

    public static final String DESC_IFOUTPKTS = "ifOutPkts";

    public static final String DESC_IFINPKTSPERS = "ifInPktsPers";

    public static final String DESC_IFOUTPKTSPERS = "ifOutPktsPers";

    public static final String DESC_IFINDISCARDS = "ifInDiscards";

    public static final String DESC_IFOUTDISCARDS = "ifOutDiscards";

    public static final String DESC_IFINERRORS = "ifInErrors";

    public static final String DESC_IFOUTERRORS = "ifOutErrors";
    
    public static final String DESC_IFINBANDWIDTHUTILHDX = "InBandwidthUtilHdx";

    public static final String DESC_IFOUTBANDWIDTHUTILHDX = "OutBandwidthUtilHdx";

    public static final String DESC_IFINBANDWIDTHUTILHDXPERC = "InBandwidthUtilHdxPerc";

    public static final String DESC_IFOUTBANDWIDTHUTILHDXPERC = "OutBandwidthUtilHdxPerc";

    public static final String DESC_IFINDISCARDSPERC = "InDiscardsPerc";

    public static final String DESC_IFOUTDISCARDSPERC = "OutDiscardsPerc";

    public static final String DESC_IFINERRORSPERC = "InErrorsPerc";

    public static final String DESC_IFOUTERRORSPERC = "OutErrorsPerc";

    public static final String CHNAME_IFINDEX = "端口索引";
    
    public static final String CHNAME_IFDESCR = "描述";
    
    public static final String CHNAME_IFTYPE = "类型";
    
    public static final String CHNAME_IFMTU = "最大数据包";
    
    public static final String CHNAME_IFSPEED = "端口最大速率(bit)";
    
    public static final String CHNAME_IFPHYSADDRESS = "端口Mac地址";

    public static final String CHNAME_IFADMINSTATUS = "管理状态";
    
    public static final String CHNAME_IFOPERSTATUS = "当前状态";
    
    public static final String CHNAME_IFLASTCHANGE = "系统sysUpTime评估";

    public static final String CHNAME_IFINOCTETS = "接收的字节";
    
    public static final String CHNAME_IFOUTOCTETS = "传输的字节";

    public static final String CHNAME_IFINMULTICASTPKTS = "入口多播数据包";
    
    public static final String CHNAME_IFOUTMULTICASTPKTS = "出口多播数据包";

    public static final String CHNAME_IFINBROADCASTPKTS = "入口广播数据包";
    
    public static final String CHNAME_IFOUTBROADCASTPKTS = "出口广播数据包";

    public static final String CHNAME_IFINPKTS = "入口数据包";

    public static final String CHNAME_IFOUTPKTS = "出口数据包";

    public static final String CHNAME_IFINPKTSPERS = "每秒入口数据包";

    public static final String CHNAME_IFOUTPKTSPERS = "每秒出口数据包";

    public static final String CHNAME_IFINDISCARDS = "入口丢包数";
    
    public static final String CHNAME_IFOUTDISCARDS = "出口丢包数";

    public static final String CHNAME_IFINERRORS = "入口错误包数";
    
    public static final String CHNAME_IFOUTERRORS = "出口错误包数";

    public static final String CHNAME_IFINBANDWIDTHUTILHDX = "端口入口流速";

    public static final String CHNAME_IFOUTBANDWIDTHUTILHDX = "端口出口流速";

    public static final String CHNAME_IFINBANDWIDTHUTILHDXPERC = "端口入口带宽利用率";

    public static final String CHNAME_IFOUTBANDWIDTHUTILHDXPERC = "端口出口带宽利用率";

    public static final String CHNAME_IFINDISCARDSPERC = "入口丢包率";
    
    public static final String CHNAME_IFOUTDISCARDSPERC = "出口丢包率";

    public static final String CHNAME_IFINERRORSPERC = "入口错包率";
    
    public static final String CHNAME_IFOUTERRORSPERC = "出口错包率";
//    public static final String[] NetWorkMibInterfaceDesc1={"index","ifInOctets","ifInMulticastPkts","ifInBroadcastPkts",
//        "ifInDiscards","ifInErrors","ifOutOctets",
//        "ifOutMulticastPkts","ifOutBroadcastPkts","ifOutDiscards","ifOutErrors",
//        "ifOutQLen","ifSpecific"
//};
//public static final String[] NetWorkMibInterfaceChname1={"端口索引","接收的字节","单向传输数据包","非单向传输数据包",
//        "被丢弃的数据包","入站错误数据包","入站不知名的数据包","传输的字节",
//        "单向传输数据包","非单向传输数据包","出站被丢弃的数据包","出站传输失败的数据包",
//        "输出信息包排列的长度","Mib对端口的说明"
//};
    /**
     * 
     */
    public InterfaceSnmp() {
    }

    public void collectData(Node node, MonitoredItem item) {

    }

    public void collectData(HostNode node) {

    }

    public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
        Hashtable returnHash=new Hashtable();

        Vector interfaceVector=new Vector();
        Vector utilhdxVector = new Vector();
        Vector inUtilhdxVector = new Vector();
        Vector outUtilhdxVector = new Vector();
        Vector allutilhdxVector = new Vector();
        Vector packsVector = new Vector();
        Vector inpacksVector = new Vector();
        Vector outpacksVector = new Vector();
        Vector inpksVector = new Vector();
        Vector outpksVector = new Vector();
        Vector discardspercVector = new Vector();
        Vector errorspercVector = new Vector();
        Vector allerrorspercVector = new Vector();
        Vector alldiscardspercVector = new Vector();
        Vector allutilhdxpercVector=new Vector();
        Vector utilhdxpercVector = new Vector();
        String ifAllUtilhdx = "0";                // 端口总流速
        String ifAllInUtilhdx = "0";              // 入口总流速
        String ifAllOutUtilhdx = "0";             // 出口总流速
        String ifAllDiscardPerc = "0";            // 所有端口总丢包率
        String ifAllInDiscardPerc = "0";          // 所有入口总丢包率
        String ifAllOutDiscardPerc = "0";         // 所有出口总丢包率
        String ifAllErrorPerc = "0";              // 所有端口总错包率
        String ifAllInErrorPerc = "0";            // 所有入口总错包率
        String ifAllOutErrorPerc = "0";           // 所有出口总错包率
        Long ifAllUtilhdxLong = 0L;               // 端口总流速
        Long ifAllInUtilhdxLong = 0L;             // 入口总流速
        Long ifAllOutUtilhdxLong = 0L;            // 出口总流速
        Long ifAllUtilhdxPercLong = 0L;           // 端口总带宽
        Long ifAllInUtilhdxPercLong = 0L;         // 入口总带宽
        Long ifAllOutUtilhdxPercLong = 0L;        // 出口总带宽
        Double ifAllDiscardPercDouble = 0D;            // 所有端口总丢包率
        Double ifAllInDiscardPercDouble = 0D;          // 所有入口总丢包率
        Double ifAllOutDiscardPercDouble = 0D;         // 所有出口总丢包率
        Double ifAllErrorPercDouble = 0D;              // 所有端口总错包率
        Double ifAllInErrorPercDouble = 0D;            // 所有入口总错包率
        Double ifAllOutErrorPercDouble = 0D;           // 所有出口总错包率

        // 用于存放第一次采集的入口字节数， key 为端口索引， 为计算端口入口流速
        Hashtable<String, Interfacecollectdata> ifInOctetsHashtable = new Hashtable<String, Interfacecollectdata>();
        // 用于存放第一次采集的出口字节数， key 为端口索引， 为计算端口出口流速
        Hashtable<String, Interfacecollectdata> ifOutOctetsHashtable = new Hashtable<String, Interfacecollectdata>();
        // 用于存放端口最大速率， key 为端口索引， 为计算端口带宽利用率
        Hashtable<String, Interfacecollectdata> ifSpeedHashtable = new Hashtable<String, Interfacecollectdata>();
        // 用于存放存放第一次采集的端口入口数据包数，包含单播和非单播（广播和多播）以及丢包数、错包数， key 为端口索引， 为计算端口入口错包率和丢包率
        Hashtable<String, Interfacecollectdata> ifInPktsHashtable = new Hashtable<String, Interfacecollectdata>();
        // 用于存放存放第一次采集的端口出口数据包数，包含单播和非单播（广播和多播）以及丢包数、错包数， key 为端口索引， 为计算端口出口错包率和丢包率
        Hashtable<String, Interfacecollectdata> ifOutPktsHashtable = new Hashtable<String, Interfacecollectdata>();
        // 用于存放存放第一次采集的端口入口丢包数， key 为端口索引， 为计算端口出口错包率和丢包率
        Hashtable<String, Interfacecollectdata> ifInDiscardsHashtable = new Hashtable<String, Interfacecollectdata>();
        // 用于存放存放第一次采集的端口入口错包数， key 为端口索引， 为计算端口出口错包率和丢包率
        Hashtable<String, Interfacecollectdata> ifInErrorsHashtable = new Hashtable<String, Interfacecollectdata>();
        // 用于存放存放第一次采集的端口出口丢包数， key 为端口索引， 为计算端口出口错包率和丢包率
        Hashtable<String, Interfacecollectdata> ifOutDiscardsHashtable = new Hashtable<String, Interfacecollectdata>();
        // 用于存放存放第一次采集的端口出口错包数， key 为端口索引， 为计算端口出口错包率和丢包率
        Hashtable<String, Interfacecollectdata> ifOutErrorsHashtable = new Hashtable<String, Interfacecollectdata>();
        
        HostLoader hostLoader = new HostLoader();
        Host host = hostLoader.loadOneByID(nodeGatherIndicators.getNodeid());
        if (host == null) {
            return null;
        }
        if (!host.isManaged()) {
            return null;
        }
        String ip = host.getIpAddress();
        Long calculateInterval = 30000L;
        Long calculateIntervalSecond = calculateInterval / 1000;
        Long interval = 5000L;
        String[] ifIndexOIDs =new String[] {               
              "1.3.6.1.2.1.2.2.1.1",        // ifIndex
              "1.3.6.1.2.1.2.2.1.2",        // ifDescr
              "1.3.6.1.2.1.2.2.1.3",        // ifType
              "1.3.6.1.2.1.2.2.1.4",        // ifMtu
              "1.3.6.1.2.1.2.2.1.5",        // ifPhysAddress
              "1.3.6.1.2.1.2.2.1.6",        // ifSpeed
              };
        String[] ifStatusOIDs =new String[] {
                "1.3.6.1.2.1.2.2.1.1",      // ifIndex
                "1.3.6.1.2.1.2.2.1.7",      // ifAdminStatus
                "1.3.6.1.2.1.2.2.1.8",      // ifOperStatus
                "1.3.6.1.2.1.2.2.1.9",      // ifLastChange
                };
        String[] ifOctetsOIDs =new String[] {
                "1.3.6.1.2.1.2.2.1.1",       // ifIndex
                "1.3.6.1.2.1.2.2.1.10",      // ifInOctets
                "1.3.6.1.2.1.2.2.1.16",      // ifOutOctets
                };
        String[] ifInPktsOIDs =new String[] {
                "1.3.6.1.2.1.2.2.1.1",       // ifIndex
                "1.3.6.1.2.1.2.2.1.11",      // ifInUcastPkts
                "1.3.6.1.2.1.2.2.1.12",      // ifInNUcastPkts
                "1.3.6.1.2.1.2.2.1.13",      // ifInDiscards
                "1.3.6.1.2.1.2.2.1.14",      // ifInErrors
                };
        String[] ifOutPktsOIDs =new String[] {
                "1.3.6.1.2.1.2.2.1.1",       // ifIndex
                "1.3.6.1.2.1.2.2.1.17",      // ifOutUcastPkts
                "1.3.6.1.2.1.2.2.1.18",      // ifOutNUcastPkts
                "1.3.6.1.2.1.2.2.1.19",      // ifOutDiscards
                "1.3.6.1.2.1.2.2.1.20"       // ifOutErrors
                };
        String[] ifMulticastPktsOIDs =new String[] {
                "1.3.6.1.2.1.2.2.1.1",       // ifIndex
                "1.3.6.1.2.1.31.1.1.1.2",    // ifInMulticastPkts
                "1.3.6.1.2.1.31.1.1.1.4",    // ifOutMulticastPkts
                };
        String[] ifBroadcastPktsOIDs =new String[] {
                "1.3.6.1.2.1.2.2.1.1",       // ifIndex
                "1.3.6.1.2.1.31.1.1.1.3",    // ifInBroadcastPkts
                "1.3.6.1.2.1.31.1.1.1.5",    // ifOutBroadcastPkts
                };
//        String[] ifDiscardsOIDs =new String[] {
//                "1.3.6.1.2.1.2.2.1.1",       // ifIndex
//                "1.3.6.1.2.1.2.2.1.13",      // ifInDiscards
//                "1.3.6.1.2.1.2.2.1.19",      // ifOutDiscards
//                };
//        String[] ifErrorsOIDs =new String[] {
//                "1.3.6.1.2.1.2.2.1.1",       // ifIndex
//                "1.3.6.1.2.1.2.2.1.14",      // ifInErrors
//                "1.3.6.1.2.1.2.2.1.20"       // ifOutErrors
//                };
        // 获取端口静态值
        String[][] ifIndexArrays = null;   
        try {
            ifIndexArrays = SnmpUtils.getTableData(host.getIpAddress(), host.getCommunity(), ifIndexOIDs, host.getSnmpversion(), 3, 1000 * 20);
        } catch(Exception e){
            e.printStackTrace();
        }
        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 获取端口状态值
        String[][] ifStatusArrays = null;   
        try {
            ifStatusArrays = SnmpUtils.getTableData(host.getIpAddress(), host.getCommunity(), ifStatusOIDs, host.getSnmpversion(), 3, 1000 * 20);
        } catch(Exception e){
        }
        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 获取第一次端口流量
        String[][] ifOctetsArrays = null;   
        try {
            ifOctetsArrays = SnmpUtils.getTableData(host.getIpAddress(), host.getCommunity(), ifOctetsOIDs, host.getSnmpversion(), 3, 1000 * 20);
        } catch(Exception e){
            e.printStackTrace();
        }
        try {
            Thread.sleep(calculateInterval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 获取第二次端口流量
        String[][] ifOctetsArrays2 = null;   
        try {
            ifOctetsArrays2 = SnmpUtils.getTableData(host.getIpAddress(), host.getCommunity(), ifOctetsOIDs, host.getSnmpversion(), 3, 1000 * 20);
        } catch(Exception e){
        }
        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 获取端口多播数据包
        String[][] ifMulticastPktsArrays = null;   
        try {
            ifMulticastPktsArrays = SnmpUtils.getTableData(host.getIpAddress(), host.getCommunity(), ifMulticastPktsOIDs, host.getSnmpversion(), 3, 1000 * 20);
        } catch(Exception e){
        }
        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 获取端口广播数据包
        String[][] ifBroadcastPktsArrays = null;   
        try {
            ifBroadcastPktsArrays = SnmpUtils.getTableData(host.getIpAddress(), host.getCommunity(), ifBroadcastPktsOIDs, host.getSnmpversion(), 3, 1000 * 20);
        } catch(Exception e){
        }
        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 获取端口第一次入口数据包（单播、非单播、丢包以及错包）
        String[][] ifInPktsArrays = null;   
        try {
            ifInPktsArrays = SnmpUtils.getTableData(host.getIpAddress(), host.getCommunity(), ifInPktsOIDs, host.getSnmpversion(), 3, 1000 * 20);
        } catch(Exception e){
        }
        try {
            Thread.sleep(calculateInterval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 获取端口第二次入口数据包（单播、非单播、丢包以及错包）
        String[][] ifInPktsArrays2 = null;   
        try {
            ifInPktsArrays2 = SnmpUtils.getTableData(host.getIpAddress(), host.getCommunity(), ifInPktsOIDs, host.getSnmpversion(), 3, 1000 * 20);
        } catch(Exception e){
        }
        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 获取端口第一次出口数据包（单播、非单播、丢包以及错包）
        String[][] ifOutPktsArrays = null;   
        try {
            ifOutPktsArrays = SnmpUtils.getTableData(host.getIpAddress(), host.getCommunity(), ifOutPktsOIDs, host.getSnmpversion(), 3, 1000 * 20);
        } catch(Exception e){
        }
        try {
            Thread.sleep(calculateInterval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 获取端口第二次出口数据包（单播、非单播、丢包以及错包）
        String[][] ifOutPktsArrays2 = null;   
        try {
            ifOutPktsArrays2 = SnmpUtils.getTableData(host.getIpAddress(), host.getCommunity(), ifOutPktsOIDs, host.getSnmpversion(), 3, 1000 * 20);
        } catch(Exception e){
        }
        Calendar date = Calendar.getInstance();
        if (ifIndexArrays != null && ifIndexArrays.length > 0) { 
            for (String[] ifIndexArray : ifIndexArrays) {
                if (ifIndexArray == null || ifIndexArray.length < 6) {
                    continue;
                }
                String ifIndex = ifIndexArray[0];
                String ifDescr = ifIndexArray[1];
                String ifType = ifIndexArray[2];
                String ifMtu = ifIndexArray[3];
                String ifSpeed = ifIndexArray[4];
                String ifPhysAddress = ifIndexArray[5];
                if (ifIndex == null || ifIndex.trim().length() == 0) {
                    continue;
                }
                if (ifDescr == null) {
                    ifDescr = "";
                }
                if (ifType == null) {
                    ifType = "";
                }
                if (ifSpeed == null) {
                    ifSpeed = "0";
                }
                if (ifPhysAddress == null || (!ifPhysAddress.contains("-")
                        && !ifPhysAddress.contains(":"))) {
                    // 排除乱码
                    ifPhysAddress = "";
                }
                if (ifType == null || (!Interface_IfType.contains(ifType.trim()) && 
                        !Interface_IfType.containsKey(ifType.trim()))) {
                    ifType = "1";
                }
                ifType = (String) Interface_IfType.get(ifType.trim());
                ifPhysAddress = CommonUtil.removeIllegalStr(ifPhysAddress);

                // 端口索引
                Interfacecollectdata interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFINDEX);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("static");
                interfacedata.setUnit("");
                interfacedata.setThevalue(ifIndex);
                interfacedata.setChname(CHNAME_IFINDEX);
                interfaceVector.add(interfacedata);

                // 描述
                interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFDESCR);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("static");
                interfacedata.setUnit("");
                interfacedata.setThevalue(ifDescr);
                interfacedata.setChname(CHNAME_IFDESCR);
                interfaceVector.add(interfacedata);

                // 类型
                interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFTYPE);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("static");
                interfacedata.setUnit("");
                interfacedata.setThevalue(ifType);
                interfacedata.setChname(CHNAME_IFTYPE);
                interfaceVector.add(interfacedata);
                
                // 最大数据包
                interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFMTU);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("static");
                interfacedata.setUnit("bit");
                interfacedata.setThevalue(ifMtu);
                interfacedata.setChname(CHNAME_IFMTU);
                interfaceVector.add(interfacedata);
                
                // 端口最大速率(bit)
                interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFSPEED);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("static");
                interfacedata.setUnit("bit");
                interfacedata.setThevalue(ifSpeed);
                interfacedata.setChname(CHNAME_IFSPEED);
                interfaceVector.add(interfacedata);
                ifSpeedHashtable.put(ifIndex, interfacedata);
                
                // 端口Mac地址
                interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFPHYSADDRESS);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("static");
                interfacedata.setUnit("");
                interfacedata.setThevalue(ifPhysAddress);
                interfacedata.setChname(CHNAME_IFPHYSADDRESS);
                interfaceVector.add(interfacedata);
            }
        }
        if (ifStatusArrays != null && ifStatusArrays.length > 0) {
            for (String[] ifStatusArray : ifStatusArrays) {
                if (ifStatusArray == null || ifStatusArray.length < 4) {
                    continue;
                }
                String ifIndex = ifStatusArray[0];
                String ifAdminStatus = ifStatusArray[1];
                String ifOperStatus = ifStatusArray[2];
                String ifLastChange = ifStatusArray[3];
                // 判断端口管理状态
                if (ifAdminStatus == null || (!ifEntity_ifStatus.contains(ifAdminStatus.trim())
                        && !ifEntity_ifStatus.containsKey(ifAdminStatus.trim()))) {
                    // 如果为 null 或者既不是真正状态，又不是状态的代号 默认代号为 1 即打开状态
                    ifAdminStatus = "1";
                }
                if (!ifEntity_ifStatus.contains(ifAdminStatus.trim())) {
                    // 如果为不是真正状态
                    ifAdminStatus = (String)ifEntity_ifStatus.get(ifAdminStatus.trim());
                }
                // 判断端口当前状态
                if (ifOperStatus == null || (!ifEntity_ifStatus.contains(ifOperStatus.trim())
                        && !ifEntity_ifStatus.containsKey(ifOperStatus.trim()))) {
                    // 如果为 null 或者既不是真正状态，又不是状态的代号 默认代号为 1 即打开状态
                    ifOperStatus = "1";
                }
                if (!ifEntity_ifStatus.contains(ifOperStatus.trim())) {
                    // 如果为不是真正状态
                    ifOperStatus = (String)ifEntity_ifStatus.get(ifOperStatus.trim());
                }
                if (ifLastChange == null) {
                    ifLastChange = "";
                }
                // 端口索引无需入库

                // 管理状态
                Interfacecollectdata interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFADMINSTATUS);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("dynamic");
                interfacedata.setUnit("");
                interfacedata.setThevalue(ifAdminStatus);
                interfacedata.setChname(CHNAME_IFADMINSTATUS);
                interfaceVector.add(interfacedata);

                // 当前状态
                interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFOPERSTATUS);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("dynamic");
                interfacedata.setUnit("");
                interfacedata.setThevalue(ifOperStatus);
                interfacedata.setChname(CHNAME_IFOPERSTATUS);
                interfaceVector.add(interfacedata);
                
                // 系统sysUpTime评估
                interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFLASTCHANGE);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("dynamic");
                interfacedata.setUnit("bit");
                interfacedata.setThevalue(ifLastChange);
                interfacedata.setChname(CHNAME_IFLASTCHANGE);
                interfaceVector.add(interfacedata);
                
            }
        }
        if (ifOctetsArrays != null && ifOctetsArrays.length > 0 ) {
            for (String[] ifOctetsArray : ifOctetsArrays) {
                if (ifOctetsArray == null || ifOctetsArray.length < 3) {
                    continue;
                }
                String ifIndex = ifOctetsArray[0];
                String ifInOctets = ifOctetsArray[1];
                String ifOutOctets = ifOctetsArray[2];
                if (ifIndex == null) {
                    continue;
                }
                if (ifInOctets == null) {
                    ifInOctets = "0";
                }
                if (ifOutOctets == null) {
                    ifOutOctets = "0";
                }

                // 接收的字节
                Interfacecollectdata interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFINOCTETS);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("dynamic");
                interfacedata.setUnit("");
                interfacedata.setThevalue(ifInOctets);
                interfacedata.setChname(CHNAME_IFINOCTETS);
                ifInOctetsHashtable.put(ifIndex, interfacedata);

                // 传输的字节
                interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFOUTOCTETS);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("dynamic");
                interfacedata.setUnit("");
                interfacedata.setThevalue(ifOutOctets);
                interfacedata.setChname(CHNAME_IFOUTOCTETS);
                ifOutOctetsHashtable.put(ifIndex, interfacedata);
                
            }
        }

        if (ifOctetsArrays2 != null && ifOctetsArrays2.length > 0 ) {
            for (String[] ifOctetsArray : ifOctetsArrays2) {
                if (ifOctetsArray == null || ifOctetsArray.length < 3) {
                    continue;
                }
                String ifIndex = ifOctetsArray[0];
                String ifInOctets = ifOctetsArray[1];
                String ifOutOctets = ifOctetsArray[2];
                if (ifIndex == null) {
                    continue;
                }
                if (ifInOctets == null) {
                    ifInOctets = "0";
                }
                if (ifOutOctets == null) {
                    ifOutOctets = "0";
                }
                
                Long ifInOctetsLong = 0L;
                try {
                    ifInOctetsLong = Long.valueOf(ifInOctets);
                } catch (NumberFormatException e) {
                }
                Long ifOutOctetsLong = 0L;
                try {
                    ifOutOctetsLong = Long.valueOf(ifOutOctets);
                } catch (NumberFormatException e) {
                }
                
                String ifInBandwidthUtilHdx = "0";         // 端口入口流速
                String ifOutBandwidthUtilHdx = "0";        // 端口出口流速
                String ifInBandwidthUtilHdxPerc = "0";     // 端口入口带宽利用率
                String ifOutBandwidthUtilHdxPerc = "0";    // 端口出口带宽利用率
                Long ifInBandwidthUtilHdxLong = 0L;
                Long ifOutBandwidthUtilHdxLong = 0L;
                Double ifInBandwidthUtilHdxPercDouble = 0.0D;
                Double ifOutBandwidthUtilHdxPercDouble = 0.0D;
                String ifSpeed = "0";
                Long ifSpeedLong = 0L;
                // 端口最大速率
                Interfacecollectdata ifSpeedInterfacecollectdata = ifSpeedHashtable.get(ifIndex);
                if (ifSpeedInterfacecollectdata != null) {
                    ifSpeed = ifSpeedInterfacecollectdata.getThevalue();
                    try {
                        ifSpeedLong = Long.valueOf(ifSpeed);
                    } catch (Exception e) {
                    }
                }

                // 计算端口入口流速 (本次采集 - 上次采集) / utilhdxInterval (时间)
                // 上次端口入口字节数
                Interfacecollectdata lastInInterfacecollectdata = ifInOctetsHashtable.get(ifIndex);
                if (lastInInterfacecollectdata != null) {
                    try {
                        String lastIfInOctets = lastInInterfacecollectdata.getThevalue();
                        Long lastIfInOctetsLong = Long.valueOf(lastIfInOctets);
                        if (ifInOctetsLong < lastIfInOctetsLong) {
                            ifInOctetsLong += INT_MAX;
                        }
                        // 流量之差 / 时间间隔
                        Long inOctetsBetween = ifInOctetsLong - lastIfInOctetsLong;
                        ifInBandwidthUtilHdxLong = inOctetsBetween / (calculateIntervalSecond * 1024);
                        ifInBandwidthUtilHdx = String.valueOf(ifInBandwidthUtilHdxLong);
                        ifAllInUtilhdxLong +=  ifInBandwidthUtilHdxLong;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                
                // 计算端口出口流速 (本次采集 - 上次采集 / (utilhdxInterval(时间) * 1024) 转换成多少 每秒K字节数KB/s
                // 上次端口出口字节数
                Interfacecollectdata lastOutInterfacecollectdata = ifOutOctetsHashtable.get(ifIndex);
                if (lastOutInterfacecollectdata != null) {
                    try {
                        String lastIfOutOctets = lastOutInterfacecollectdata.getThevalue();
                        Long lastIfOutOctetsLong = Long.valueOf(lastIfOutOctets);
                        if (ifOutOctetsLong < lastIfOutOctetsLong) {
                            ifOutOctetsLong += INT_MAX;
                        }
                        // 流量之差 / 时间间隔
                        Long outOctetsBetween = ifOutOctetsLong - lastIfOutOctetsLong;
                        ifOutBandwidthUtilHdxLong = outOctetsBetween / (calculateIntervalSecond * 1024);
                        ifOutBandwidthUtilHdx = String.valueOf(ifOutBandwidthUtilHdxLong);
                        ifAllOutUtilhdxLong += ifOutBandwidthUtilHdxLong;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                
                // 计算带宽利用率 ( 流速 * 8 * 1024 ) / ifSpeedLong 先将 流速 转换成 多少 每秒位数 即 b/s 然后除以最大速率
                if (ifSpeedLong > 0) {
                    try {
                        ifInBandwidthUtilHdxPercDouble  = (ifInBandwidthUtilHdxLong * 8.0D * 1024.0D * 100.0D) / ifSpeedLong;
                        ifInBandwidthUtilHdxPerc = df.format(ifInBandwidthUtilHdxPercDouble);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        ifOutBandwidthUtilHdxPercDouble = (ifOutBandwidthUtilHdxLong * 8.0D * 1024.0D * 100.0D) / ifSpeedLong;
                        ifOutBandwidthUtilHdxPerc = df.format(ifOutBandwidthUtilHdxPercDouble);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // 端口入口流速
                UtilHdx inUtilHdx = new UtilHdx();
                inUtilHdx.setIpaddress(ip);
                inUtilHdx.setCollecttime(date);
                inUtilHdx.setCategory("Interface");
                inUtilHdx.setEntity(DESC_IFINBANDWIDTHUTILHDX);
                inUtilHdx.setSubentity(ifIndex);
                inUtilHdx.setRestype("dynamic");
                inUtilHdx.setUnit("KB/s");
                inUtilHdx.setChname(ifIndex + CHNAME_IFINBANDWIDTHUTILHDX);
                inUtilHdx.setThevalue(ifInBandwidthUtilHdx);
                inUtilhdxVector.add(inUtilHdx);
                utilhdxVector.add(inUtilHdx);

                // 端口出口流速
                UtilHdx outUtilHdx = new UtilHdx();
                outUtilHdx.setIpaddress(ip);
                outUtilHdx.setCollecttime(date);
                outUtilHdx.setCategory("Interface");
                outUtilHdx.setEntity(DESC_IFOUTBANDWIDTHUTILHDX);
                outUtilHdx.setSubentity(ifIndex);
                outUtilHdx.setRestype("dynamic");
                outUtilHdx.setUnit("KB/s");
                outUtilHdx.setChname(ifIndex + CHNAME_IFOUTBANDWIDTHUTILHDX);
                outUtilHdx.setThevalue(ifOutBandwidthUtilHdx);
                outUtilhdxVector.add(outUtilHdx);
                utilhdxVector.add(outUtilHdx);

                // 端口入口带宽利用率
                UtilHdxPerc inUtilHdxPerc = new UtilHdxPerc();
                inUtilHdxPerc.setIpaddress(ip);
                inUtilHdxPerc.setCollecttime(date);
                inUtilHdxPerc.setCategory("Interface");
                inUtilHdxPerc.setEntity(DESC_IFINBANDWIDTHUTILHDXPERC);
                inUtilHdxPerc.setSubentity(ifIndex);
                inUtilHdxPerc.setRestype("dynamic");
                inUtilHdxPerc.setUnit("%");
                inUtilHdxPerc.setChname(ifIndex + CHNAME_IFINBANDWIDTHUTILHDXPERC);
                inUtilHdxPerc.setThevalue(ifInBandwidthUtilHdxPerc);
                utilhdxpercVector.add(inUtilHdxPerc);

                // 端口出口带宽利用率
                UtilHdxPerc outUtilHdxPerc = new UtilHdxPerc();
                outUtilHdxPerc.setIpaddress(ip);
                outUtilHdxPerc.setCollecttime(date);
                outUtilHdxPerc.setCategory("Interface");
                outUtilHdxPerc.setEntity(DESC_IFOUTBANDWIDTHUTILHDXPERC);
                outUtilHdxPerc.setSubentity(ifIndex);
                outUtilHdxPerc.setRestype("dynamic");
                outUtilHdxPerc.setUnit("%");
                outUtilHdxPerc.setChname(ifIndex + CHNAME_IFOUTBANDWIDTHUTILHDXPERC);
                outUtilHdxPerc.setThevalue(ifOutBandwidthUtilHdxPerc);
                utilhdxpercVector.add(outUtilHdxPerc);

            }
        }

        if (ifMulticastPktsArrays != null && ifMulticastPktsArrays.length > 0 ) {
            for (String[] ifMulticastPktsArray : ifMulticastPktsArrays) {
                if (ifMulticastPktsArray == null || ifMulticastPktsArray.length < 3) {
                    continue;
                }
                String ifIndex = ifMulticastPktsArray[0];
                String ifInMulticastPkts = ifMulticastPktsArray[1];
                String ifOutMulticastPkts = ifMulticastPktsArray[2];
                if (ifIndex == null) {
                    continue;
                }
                if (ifInMulticastPkts == null) {
                    ifInMulticastPkts = "0";
                }
                if (ifOutMulticastPkts == null) {
                    ifOutMulticastPkts = "0";
                }

                // 入口多播数据包
                Interfacecollectdata interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFINMULTICASTPKTS);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("dynamic");
                interfacedata.setUnit("个");
                interfacedata.setThevalue(ifInMulticastPkts);
                interfacedata.setChname(CHNAME_IFINMULTICASTPKTS);
                interfaceVector.add(interfacedata);

                // 出口多播数据包
                interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFOUTMULTICASTPKTS);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("dynamic");
                interfacedata.setUnit("个");
                interfacedata.setThevalue(ifOutMulticastPkts);
                interfacedata.setChname(CHNAME_IFOUTMULTICASTPKTS);
                interfaceVector.add(interfacedata);
                
            }
        }

        if (ifBroadcastPktsArrays != null && ifBroadcastPktsArrays.length > 0 ) {
            for (String[] ifBroadcastPktsArray : ifBroadcastPktsArrays) {
                if (ifBroadcastPktsArray == null || ifBroadcastPktsArray.length < 3) {
                    continue;
                }
                String ifIndex = ifBroadcastPktsArray[0];
                String ifInBroadcastPkts = ifBroadcastPktsArray[1];
                String ifOutBroadcastPkts = ifBroadcastPktsArray[2];
                if (ifIndex == null || ifIndex.trim().length() == 0) {
                    continue;
                }
                if (ifInBroadcastPkts == null) {
                    ifInBroadcastPkts = "0";
                }
                if (ifOutBroadcastPkts == null) {
                    ifOutBroadcastPkts = "0";
                }

                // 入口广播数据包
                Interfacecollectdata interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFINBROADCASTPKTS);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("dynamic");
                interfacedata.setUnit("个");
                interfacedata.setThevalue(ifInBroadcastPkts);
                interfacedata.setChname(CHNAME_IFINBROADCASTPKTS);
                interfaceVector.add(interfacedata);

                // 出口广播数据包
                interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFOUTBROADCASTPKTS);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("dynamic");
                interfacedata.setUnit("个");
                interfacedata.setThevalue(ifOutBroadcastPkts);
                interfacedata.setChname(CHNAME_IFOUTBROADCASTPKTS);
                interfaceVector.add(interfacedata);
                
            }
        }

        // 第一次次端口入口包
        if (ifInPktsArrays != null && ifInPktsArrays.length > 0 ) {
            for (String[] ifInPktsArray : ifInPktsArrays) {
                if (ifInPktsArray == null || ifInPktsArray.length < 5) {
                    continue;
                }
                String ifIndex = ifInPktsArray[0];
                String ifInUcastPkts = ifInPktsArray[1];        // 入口单播
                String ifInNUcastPkts = ifInPktsArray[2];       // 入口非单播
                String ifInDiscards = ifInPktsArray[3];         // 入口丢包
                String ifInErrors = ifInPktsArray[4];           // 入口错包
                if (ifIndex == null) {
                    continue;
                }
                if (ifInUcastPkts == null) {
                    ifInUcastPkts = "0";
                }
                if (ifInNUcastPkts == null) {
                    ifInNUcastPkts = "0";
                }
                if (ifInDiscards == null) {
                    ifInDiscards = "0";
                }
                if (ifInErrors == null) {
                    ifInErrors = "0";
                }

                Long ifInUcastPktsLong = 0L;
                try {
                    ifInUcastPktsLong = Long.valueOf(ifInUcastPkts);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Long ifInNUcastPktsLong = 0L;
                try {
                    ifInNUcastPktsLong = Long.valueOf(ifInNUcastPkts);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String ifInPkts = "0";
                try {
                    ifInPkts = String.valueOf(ifInUcastPktsLong + ifInNUcastPktsLong);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                // 入口总包数
                Interfacecollectdata interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFINPKTS);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("dynamic");
                interfacedata.setUnit("个");
                interfacedata.setThevalue(ifInPkts);
                interfacedata.setChname(CHNAME_IFINPKTS);
                ifInPktsHashtable.put(ifIndex, interfacedata);

                // 入口丢包数
                interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFINDISCARDS);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("dynamic");
                interfacedata.setUnit("个");
                interfacedata.setThevalue(ifInDiscards);
                interfacedata.setChname(CHNAME_IFINDISCARDS);
                ifInDiscardsHashtable.put(ifIndex, interfacedata);
                
                // 入口错包数
                interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFINERRORS);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("dynamic");
                interfacedata.setUnit("个");
                interfacedata.setThevalue(ifInErrors);
                interfacedata.setChname(CHNAME_IFINERRORS);
                ifInErrorsHashtable.put(ifIndex, interfacedata);

            }
        }

        // 第二次端口入口包
        if (ifInPktsArrays2 != null && ifInPktsArrays2.length > 0 ) {
            for (String[] ifInPktsArray : ifInPktsArrays2) {
                if (ifInPktsArray == null || ifInPktsArray.length < 5) {
                    continue;
                }
                String ifIndex = ifInPktsArray[0];
                String ifInUcastPkts = ifInPktsArray[1];        // 入口单播
                String ifInNUcastPkts = ifInPktsArray[2];       // 入口非单播
                String ifInDiscards = ifInPktsArray[3];         // 入口丢包
                String ifInErrors = ifInPktsArray[4];           // 入口错包
                if (ifIndex == null) {
                    continue;
                }
                if (ifInUcastPkts == null) {
                    ifInUcastPkts = "0";
                }
                if (ifInNUcastPkts == null) {
                    ifInNUcastPkts = "0";
                }
                if (ifInDiscards == null) {
                    ifInDiscards = "0";
                }
                if (ifInErrors == null) {
                    ifInErrors = "0";
                }

                String ifInDiscardsPerc = "0";                  // 入口丢包率
                String ifInErrorsPerc = "0";                    // 入口错包率

                String ifInPktsPers = "0";                      // 每秒入口包数
                Long ifInPktsPersLong = 0L;                     // 每秒入口包数(个/s) 用Long
                
                Long ifInUcastPktsLong = 0L;                    // 入口单播
                Long ifInNUcastPktsLong = 0L;                   // 入口非单播
                Long ifInPktsLong = 0L;                         // 入口总包数
                Long ifInDiscardsLong = 0L;                     // 入口丢包
                Long ifInErrorsLong = 0L;                       // 入口错包
                Long ifInPktsBetweenLong = 0L;                  // 入口总包数之差
                Long ifInDiscardsBetweenLong = 0L;              // 入口丢包数之差
                Long ifInErrorsBetweenLong = 0L;                // 入口错包数之差
                Long lastIfInPktsLong = 0L;                     // 上次入口总包数
                Long lastIfInDiscardsLong = 0L;                 // 上次入口丢包
                Long lastIfInErrorsLong = 0L;                   // 上次入口错包
                Double ifInDiscardsPercDouble = 0D;             // 入口丢包率
                Double ifInErrorsPercDouble = 0D;               // 入口错包率
                try {
                    ifInUcastPktsLong = Long.valueOf(ifInUcastPkts);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    ifInNUcastPktsLong = Long.valueOf(ifInNUcastPkts);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    ifInDiscardsLong = Long.valueOf(ifInDiscards);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    ifInErrorsLong = Long.valueOf(ifInErrors);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ifInPktsLong = ifInUcastPktsLong + ifInNUcastPktsLong;
                
                Interfacecollectdata lastIfInPktsInterfacecollectdata = ifInPktsHashtable.get(ifIndex);
                if (lastIfInPktsInterfacecollectdata != null) {
                    String lastIfInPkts = lastIfInPktsInterfacecollectdata.getThevalue();
                    try {
                        lastIfInPktsLong = Long.valueOf(lastIfInPkts);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Interfacecollectdata lastIfInDiscardsInterfacecollectdata = ifInDiscardsHashtable.get(ifIndex);
                if (lastIfInDiscardsInterfacecollectdata != null) {
                    String lastIfInDiscards = lastIfInDiscardsInterfacecollectdata.getThevalue();
                    try {
                        lastIfInDiscardsLong = Long.valueOf(lastIfInDiscards);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Interfacecollectdata lastIfInErrorsInterfacecollectdata = ifInErrorsHashtable.get(ifIndex);
                if (lastIfInErrorsInterfacecollectdata != null) {
                    String lastIfInErrors = lastIfInErrorsInterfacecollectdata.getThevalue();
                    try {
                        lastIfInErrorsLong = Long.valueOf(lastIfInErrors);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                
                // 计算两次入口包数之差
                ifInPktsBetweenLong = ifInPktsLong - lastIfInPktsLong;
                // 计算两次入口丢包数之差
                ifInDiscardsBetweenLong = ifInDiscardsLong - lastIfInDiscardsLong;
                // 计算两次入口错包数之差
                ifInErrorsBetweenLong = ifInErrorsLong - lastIfInErrorsLong;
                if (ifInPktsBetweenLong < 0) {
                    // 如果小于0 则说明被重置了
                    ifInPktsBetweenLong += INT_MAX; 
                }
                if (ifInDiscardsBetweenLong < 0) {
                    // 如果小于0 则说明被重置了
                    ifInDiscardsBetweenLong += INT_MAX;
                }
                if (ifInErrorsBetweenLong < 0) {
                    // 如果小于0 则说明被重置了
                    ifInErrorsBetweenLong += INT_MAX;
                }
                if (ifInPktsBetweenLong > 0) {
                    // 如果现在大于 0 进行计算， 如果等于 0 则不计算
                    // 计算每秒入口包数 入口总包数之差 除以 时间间隔
                    ifInPktsPersLong = ifInPktsBetweenLong / calculateIntervalSecond;
                    // 将丢包数之差 除以 总包数之差
                    ifInDiscardsPercDouble = (ifInDiscardsBetweenLong * 1.0D) / ifInPktsBetweenLong;
                    // 将错包数之差 除以 总包数之差
                    ifInErrorsPercDouble = (ifInErrorsBetweenLong * 1.0D) / ifInPktsBetweenLong;
                    // 入口总丢包率
                    ifAllInDiscardPercDouble += ifInDiscardsPercDouble;
                    // 入口总错包率
                    ifAllInErrorPercDouble += ifInErrorsPercDouble;
                }

                try {
                    ifInPktsPers = df.format(ifInPktsPersLong);
                    ifInDiscardsPerc = df.format(ifInDiscardsPercDouble);
                    ifInErrorsPerc = df.format(ifInErrorsPercDouble);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 每秒入口包数
                InPkts inPkts = new InPkts();
                inPkts.setIpaddress(ip);
                inPkts.setCollecttime(date);
                inPkts.setCategory("Interface");
                inPkts.setEntity(DESC_IFINPKTSPERS);
                inPkts.setSubentity(ifIndex);
                inPkts.setRestype("dynamic");
                inPkts.setUnit("个/s");
                inPkts.setThevalue(ifInPktsPers);
                inPkts.setChname(CHNAME_IFINPKTSPERS);
                inpacksVector.add(inPkts);

                // 入口丢包率
                DiscardsPerc inDiscardsPerc = new DiscardsPerc();
                inDiscardsPerc.setIpaddress(ip);
                inDiscardsPerc.setCollecttime(date);
                inDiscardsPerc.setCategory("Interface");
                inDiscardsPerc.setEntity(DESC_IFINDISCARDSPERC);
                inDiscardsPerc.setSubentity(ifIndex);
                inDiscardsPerc.setRestype("dynamic");
                inDiscardsPerc.setUnit("%");
                inDiscardsPerc.setThevalue(ifInDiscardsPerc);
                inDiscardsPerc.setChname(CHNAME_IFINDISCARDSPERC);
                discardspercVector.add(inDiscardsPerc);
                
                // 入口错包率
                ErrorsPerc inErrorsPerc = new  ErrorsPerc();
                inErrorsPerc.setIpaddress(ip);
                inErrorsPerc.setCollecttime(date);
                inErrorsPerc.setCategory("Interface");
                inErrorsPerc.setEntity(DESC_IFINERRORSPERC);
                inErrorsPerc.setSubentity(ifIndex);
                inErrorsPerc.setRestype("dynamic");
                inErrorsPerc.setUnit("%");
                inErrorsPerc.setThevalue(ifInErrorsPerc);
                inErrorsPerc.setChname(CHNAME_IFINERRORSPERC);
                errorspercVector.add(inErrorsPerc);
            }
        }

        // 第一次端口出口包
        if (ifOutPktsArrays != null && ifOutPktsArrays.length > 0 ) {
            for (String[] ifOutPktsArray : ifOutPktsArrays) {
                if (ifOutPktsArray == null || ifOutPktsArray.length < 5) {
                    continue;
                }
                String ifIndex = ifOutPktsArray[0];
                String ifOutUcastPkts = ifOutPktsArray[1];        // 出口单播
                String ifOutNUcastPkts = ifOutPktsArray[2];       // 出口非单播
                String ifOutDiscards = ifOutPktsArray[3];         // 出口丢包
                String ifOutErrors = ifOutPktsArray[4];           // 出口错包
                if (ifIndex == null) {
                    continue;
                }
                if (ifOutUcastPkts == null) {
                    ifOutUcastPkts = "0";
                }
                if (ifOutNUcastPkts == null) {
                    ifOutNUcastPkts = "0";
                }
                if (ifOutDiscards == null) {
                    ifOutDiscards = "0";
                }
                if (ifOutErrors == null) {
                    ifOutErrors = "0";
                }

                Long ifOutUcastPktsLong = 0L;
                try {
                    ifOutUcastPktsLong = Long.valueOf(ifOutUcastPkts);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Long ifOutNUcastPktsLong = 0L;
                try {
                    ifOutNUcastPktsLong = Long.valueOf(ifOutNUcastPkts);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String ifOutPkts = "0";
                try {
                    ifOutPkts = String.valueOf(ifOutUcastPktsLong + ifOutNUcastPktsLong);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                // 出口总包数
                Interfacecollectdata interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFOUTPKTS);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("dynamic");
                interfacedata.setUnit("个");
                interfacedata.setThevalue(ifOutPkts);
                interfacedata.setChname(CHNAME_IFOUTPKTS);
                ifOutPktsHashtable.put(ifIndex, interfacedata);

                // 出口丢包数
                interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFOUTDISCARDS);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("dynamic");
                interfacedata.setUnit("个");
                interfacedata.setThevalue(ifOutDiscards);
                interfacedata.setChname(CHNAME_IFOUTDISCARDS);
                ifOutDiscardsHashtable.put(ifIndex, interfacedata);
                
                // 出口错包数
                interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFOUTERRORS);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("dynamic");
                interfacedata.setUnit("个");
                interfacedata.setThevalue(ifOutErrors);
                interfacedata.setChname(CHNAME_IFOUTERRORS);
                ifOutErrorsHashtable.put(ifIndex, interfacedata);

            }
        }

        // 第二次端口出口包
        if (ifOutPktsArrays2 != null && ifOutPktsArrays2.length > 0 ) {
            for (String[] ifOutPktsArray : ifOutPktsArrays2) {
                if (ifOutPktsArray == null || ifOutPktsArray.length < 5) {
                    continue;
                }
                String ifIndex = ifOutPktsArray[0];
                String ifOutUcastPkts = ifOutPktsArray[1];        // 出口单播
                String ifOutNUcastPkts = ifOutPktsArray[2];       // 出口非单播
                String ifOutDiscards = ifOutPktsArray[3];         // 出口丢包
                String ifOutErrors = ifOutPktsArray[4];           // 出口错包
                if (ifIndex == null) {
                    continue;
                }
                if (ifOutUcastPkts == null) {
                    ifOutUcastPkts = "0";
                }
                if (ifOutNUcastPkts == null) {
                    ifOutNUcastPkts = "0";
                }
                if (ifOutDiscards == null) {
                    ifOutDiscards = "0";
                }
                if (ifOutErrors == null) {
                    ifOutErrors = "0";
                }
                String ifOutDiscardsPerc = "0";                   // 出口丢包率
                String ifOutErrorsPerc = "0";                     // 出口错包率

                String ifOutPktsPers = "0";                       // 每秒出口包数
                Long ifOutPktsPersLong = 0L;                      // 每秒出口总包数（个/s） 用Long
                
                Long ifOutUcastPktsLong = 0L;                    // 出口单播
                Long ifOutNUcastPktsLong = 0L;                   // 出口非单播
                Long ifOutPktsLong = 0L;                         // 出口总包数
                Long ifOutDiscardsLong = 0L;                     // 出口丢包
                Long ifOutErrorsLong = 0L;                       // 出口错包
                Long ifOutPktsBetweenLong = 0L;                  // 出口总包数之差
                Long ifOutDiscardsBetweenLong = 0L;              // 出口丢包数之差
                Long ifOutErrorsBetweenLong = 0L;                // 出口错包数之差
                Long lastIfOutPktsLong = 0L;                     // 上次出口总包数
                Long lastIfOutDiscardsLong = 0L;                 // 上次出口丢包
                Long lastIfOutErrorsLong = 0L;                   // 上次出口错包
                Double ifOutDiscardsPercDouble = 0D;             // 出口丢包率
                Double ifOutErrorsPercDouble = 0D;               // 出口错包率
                try {
                    ifOutUcastPktsLong = Long.valueOf(ifOutUcastPkts);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    ifOutNUcastPktsLong = Long.valueOf(ifOutNUcastPkts);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    ifOutDiscardsLong = Long.valueOf(ifOutDiscards);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    ifOutErrorsLong = Long.valueOf(ifOutErrors);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ifOutPktsLong = ifOutUcastPktsLong + ifOutNUcastPktsLong;
                
                Interfacecollectdata lastIfOutPktsInterfacecollectdata = ifOutPktsHashtable.get(ifIndex);
                if (lastIfOutPktsInterfacecollectdata != null) {
                    String lastIfOutPkts = lastIfOutPktsInterfacecollectdata.getThevalue();
                    try {
                        lastIfOutPktsLong = Long.valueOf(lastIfOutPkts);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Interfacecollectdata lastIfOutDiscardsInterfacecollectdata = ifOutDiscardsHashtable.get(ifIndex);
                if (lastIfOutDiscardsInterfacecollectdata != null) {
                    String lastIfOutDiscards = lastIfOutDiscardsInterfacecollectdata.getThevalue();
                    try {
                        lastIfOutDiscardsLong = Long.valueOf(lastIfOutDiscards);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Interfacecollectdata lastIfOutErrorsInterfacecollectdata = ifOutErrorsHashtable.get(ifIndex);
                if (lastIfOutErrorsInterfacecollectdata != null) {
                    String lastIfOutErrors = lastIfOutErrorsInterfacecollectdata.getThevalue();
                    try {
                        lastIfOutErrorsLong = Long.valueOf(lastIfOutErrors);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                
                // 计算两次出口包数之差
                ifOutPktsBetweenLong = ifOutPktsLong - lastIfOutPktsLong;
                // 计算两次出口丢包数之差
                ifOutDiscardsBetweenLong = ifOutDiscardsLong - lastIfOutDiscardsLong;
                // 计算两次出口错包数之差
                ifOutErrorsBetweenLong = ifOutErrorsLong - lastIfOutErrorsLong;
                if (ifOutPktsBetweenLong < 0) {
                    // 如果小于0 则说明被重置了
                    ifOutPktsBetweenLong += INT_MAX; 
                }
                if (ifOutDiscardsBetweenLong < 0) {
                    // 如果小于0 则说明被重置了
                    ifOutDiscardsBetweenLong += INT_MAX;
                }
                if (ifOutErrorsBetweenLong < 0) {
                    // 如果小于0 则说明被重置了
                    ifOutErrorsBetweenLong += INT_MAX;
                }
                if (ifOutPktsBetweenLong > 0) {
                    // 如果现在大于 0 进行计算， 如果等于 0 则不计算
                    // 出口每秒的包数计算 总包数之差 除以 时间间隔
                    ifOutPktsPersLong = ifOutPktsBetweenLong / calculateIntervalSecond; 
                    // 将丢包数之差 除以 总包数之差
                    ifOutDiscardsPercDouble = (ifOutDiscardsBetweenLong * 1.0D) / ifOutPktsBetweenLong;
                    // 将错包数之差 除以 总包数之差
                    ifOutErrorsPercDouble = (ifOutErrorsBetweenLong * 1.0D) / ifOutPktsBetweenLong;
                    // 出口总丢包率
                    ifAllOutDiscardPercDouble += ifOutDiscardsPercDouble;
                    // 出口总错包率
                    ifAllOutErrorPercDouble += ifOutErrorsPercDouble;
                }

                try {
                    ifOutPktsPers = String.valueOf(ifOutPktsPersLong);
                    ifOutDiscardsPerc = df.format(ifOutDiscardsPercDouble);
                    ifOutErrorsPerc = df.format(ifOutErrorsPercDouble);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 每秒出口包数
                OutPkts outPkts = new OutPkts();
                outPkts.setIpaddress(ip);
                outPkts.setCollecttime(date);
                outPkts.setCategory("Interface");
                outPkts.setEntity(DESC_IFOUTPKTSPERS);
                outPkts.setSubentity(ifIndex);
                outPkts.setRestype("dynamic");
                outPkts.setUnit("个/s");
                outPkts.setThevalue(ifOutPktsPers);
                outPkts.setChname(CHNAME_IFOUTPKTSPERS);
                outpacksVector.add(outPkts);

                // 出口丢包率
                DiscardsPerc outDiscardsPerc = new DiscardsPerc();
                outDiscardsPerc.setIpaddress(ip);
                outDiscardsPerc.setCollecttime(date);
                outDiscardsPerc.setCategory("Interface");
                outDiscardsPerc.setEntity(DESC_IFOUTDISCARDSPERC);
                outDiscardsPerc.setSubentity(ifIndex);
                outDiscardsPerc.setRestype("dynamic");
                outDiscardsPerc.setUnit("%");
                outDiscardsPerc.setThevalue(ifOutDiscardsPerc);
                outDiscardsPerc.setChname(CHNAME_IFINDISCARDSPERC);
                discardspercVector.add(outDiscardsPerc);
                
                // 出口错包率
                ErrorsPerc outErrorsPerc = new  ErrorsPerc();
                outErrorsPerc.setIpaddress(ip);
                outErrorsPerc.setCollecttime(date);
                outErrorsPerc.setCategory("Interface");
                outErrorsPerc.setEntity(DESC_IFOUTERRORSPERC);
                outErrorsPerc.setSubentity(ifIndex);
                outErrorsPerc.setRestype("dynamic");
                outErrorsPerc.setUnit("%");
                outErrorsPerc.setThevalue(ifOutErrorsPerc);
                outErrorsPerc.setChname(CHNAME_IFOUTERRORSPERC);
                errorspercVector.add(outErrorsPerc);
            }
        }
        try {
            ifAllInUtilhdx = String.valueOf(ifAllInUtilhdxLong);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ifAllOutUtilhdx = String.valueOf(ifAllOutUtilhdxLong);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ifAllUtilhdxLong = ifAllInUtilhdxLong + ifAllOutUtilhdxLong;
            ifAllUtilhdx = String.valueOf(ifAllUtilhdxLong);
        } catch (Exception e) {
            e.printStackTrace();
        }

        AllUtilHdx allInUtilhdx = new AllUtilHdx();
        allInUtilhdx.setIpaddress(host.getIpAddress());
        allInUtilhdx.setCollecttime(date);
        allInUtilhdx.setCategory("Interface");
        allInUtilhdx.setEntity("AllInBandwidthUtilHdx");
        allInUtilhdx.setSubentity("AllInBandwidthUtilHdx");
        allInUtilhdx.setRestype("dynamic");
        allInUtilhdx.setUnit("KB/s");    
        allInUtilhdx.setChname("入口总流速");
        allInUtilhdx.setThevalue(ifAllInUtilhdx);    
        allutilhdxVector.add(allInUtilhdx);

        AllUtilHdx allOutUtilhdx = new AllUtilHdx();
        allOutUtilhdx = new AllUtilHdx();
        allOutUtilhdx.setIpaddress(host.getIpAddress());
        allOutUtilhdx.setCollecttime(date);
        allOutUtilhdx.setCategory("Interface");
        allOutUtilhdx.setEntity("AllOutBandwidthUtilHdx");
        allOutUtilhdx.setSubentity("AllOutBandwidthUtilHdx");
        allOutUtilhdx.setRestype("dynamic");
        allOutUtilhdx.setUnit("KB/s");
        allOutUtilhdx.setChname("出口总流速");    
        allOutUtilhdx.setThevalue(ifAllOutUtilhdx);  
        allutilhdxVector.add(allOutUtilhdx);
        
        AllUtilHdx allUtilhdx = new AllUtilHdx();
        allUtilhdx.setIpaddress(host.getIpAddress());
        allUtilhdx.setCollecttime(date);
        allUtilhdx.setCategory("Interface");
        allUtilhdx.setEntity("AllBandwidthUtilHdx");
        allUtilhdx.setSubentity("AllBandwidthUtilHdx");
        allUtilhdx.setRestype("dynamic");
        allUtilhdx.setUnit("KB/s");
        allUtilhdx.setChname("综合流速");
        allUtilhdx.setThevalue(ifAllUtilhdx);    
        allutilhdxVector.add(allUtilhdx);

        Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
        if(ipAllData == null) ipAllData = new Hashtable();
        if (interfaceVector != null && interfaceVector.size()>0)ipAllData.put("interface",interfaceVector);     
        if (allutilhdxpercVector != null && allutilhdxpercVector.size()>0)ipAllData.put("allutilhdxperc",allutilhdxpercVector);
        if (allutilhdxVector != null && allutilhdxVector.size()>0)ipAllData.put("allutilhdx",allutilhdxVector);
        if (utilhdxpercVector != null && utilhdxpercVector.size()>0)ipAllData.put("utilhdxperc",utilhdxpercVector);
        if (utilhdxVector != null && utilhdxVector.size()>0)ipAllData.put("utilhdx",utilhdxVector);     
        if (discardspercVector != null && discardspercVector.size()>0)ipAllData.put("discardsperc",discardspercVector);
        if (errorspercVector != null && errorspercVector.size()>0)ipAllData.put("errorsperc",errorspercVector);
        if (allerrorspercVector != null && allerrorspercVector.size()>0)ipAllData.put("allerrorsperc",allerrorspercVector);
        if (alldiscardspercVector != null && alldiscardspercVector.size()>0)ipAllData.put("alldiscardsperc",alldiscardspercVector);
        if (packsVector != null && packsVector.size()>0)ipAllData.put("packs",packsVector);
        if (inpacksVector != null && inpacksVector.size()>0)ipAllData.put("inpacks",inpacksVector);
        if (outpacksVector != null && outpacksVector.size()>0)ipAllData.put("outpacks",outpacksVector);
        ShareData.getSharedata().put(host.getIpAddress(), ipAllData);
        try {
            NodeUtil nodeUtil = new NodeUtil();
            NodeDTO node =nodeUtil.conversionToNodeDTO(host);
            AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
            List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(
                    String.valueOf(node.getId()), node.getType(),
                    node.getSubtype());
            CheckEventUtil checkutil = new CheckEventUtil();
            for (int i = 0; i < list.size(); i++) {
                AlarmIndicatorsNode alarmIndicatorsNode = (AlarmIndicatorsNode) list.get(i);
                String name = alarmIndicatorsNode.getName();
                if ("interface".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
                    checkutil.createInterfaceEventList(node, interfaceVector, alarmIndicatorsNode);
                } else if ("InBandwidthUtilHdx".equalsIgnoreCase(name)) {
                    checkutil.checkUtilhdxEvent(node, inUtilhdxVector, alarmIndicatorsNode);
                } else if ("OutBandwidthUtilHdx".equalsIgnoreCase(name)) {
                    checkutil.checkUtilhdxEvent(node, outUtilhdxVector, alarmIndicatorsNode);
                }  else if ("AllInBandwidthUtilHdx".equalsIgnoreCase(name)) {
                    checkutil.checkEvent(node, alarmIndicatorsNode, allInUtilhdx.getThevalue());
                }  else if ("AllOutBandwidthUtilHdx".equalsIgnoreCase(name)) {
                    checkutil.checkEvent(node, alarmIndicatorsNode, allOutUtilhdx.getThevalue());
                } 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        returnHash.put("interface",interfaceVector);        
        returnHash.put("allutilhdxperc",allutilhdxpercVector);
        returnHash.put("allutilhdx",allutilhdxVector);
        returnHash.put("utilhdxperc",utilhdxpercVector);
        returnHash.put("utilhdx",utilhdxVector);        
        returnHash.put("discardsperc",discardspercVector);
        returnHash.put("errorsperc",errorspercVector);
        returnHash.put("allerrorsperc",allerrorspercVector);
        returnHash.put("alldiscardsperc",alldiscardspercVector);
        returnHash.put("packs",packsVector);
        returnHash.put("inpacks",inpacksVector);
        returnHash.put("outpacks",outpacksVector);
        NetinterfaceResultTosql tosql = new NetinterfaceResultTosql();
        tosql.CreateResultTosql(returnHash, host);
        NetInterfaceDataTemptosql datatemp = new NetInterfaceDataTemptosql();
        datatemp.CreateResultTosql(returnHash, host);
        return returnHash;
    }

    public int getInterval(float d, String t) {
        return 0;
    }

}
