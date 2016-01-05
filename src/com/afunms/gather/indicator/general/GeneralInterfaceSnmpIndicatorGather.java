/*
 * @(#)GeneralInterfaceSnmpIndicatorGather.java     v1.01, 2013 12 29
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.general;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.common.util.CommonUtil;
import com.afunms.gather.indicator.base.SnmpIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
//import com.afunms.polling.om.AllUtilHdx;
//import com.afunms.polling.om.DiscardsPerc;
//import com.afunms.polling.om.ErrorsPerc;
//import com.afunms.polling.om.InPkts;
import com.afunms.polling.om.Interfacecollectdata;
//import com.afunms.polling.om.OutPkts;
//import com.afunms.polling.om.UtilHdx;
//import com.afunms.polling.om.UtilHdxPerc;

/**
 * ClassName:   GeneralInterfaceSnmpIndicatorGather.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 29 15:50:52
 */
public class GeneralInterfaceSnmpIndicatorGather extends SnmpIndicatorGather {

    
    private final static String[] ifIndexOIDs = new String[] {               
        "1.3.6.1.2.1.2.2.1.1",        // ifIndex
        "1.3.6.1.2.1.2.2.1.2",        // ifDescr
        "1.3.6.1.2.1.2.2.1.3",        // ifType
        "1.3.6.1.2.1.2.2.1.4",        // ifMtu
        "1.3.6.1.2.1.2.2.1.5",        // ifPhysAddress
        "1.3.6.1.2.1.2.2.1.6",        // ifSpeed
    };

    private final static String[] ifStatusOIDs =new String[] {
        "1.3.6.1.2.1.2.2.1.1",      // ifIndex
        "1.3.6.1.2.1.2.2.1.7",      // ifAdminStatus
        "1.3.6.1.2.1.2.2.1.8",      // ifOperStatus
        "1.3.6.1.2.1.2.2.1.9",      // ifLastChange
    };

    private final static String[] ifOctetsOIDs = new String[] {
        "1.3.6.1.2.1.2.2.1.1",       // ifIndex
        "1.3.6.1.2.1.2.2.1.10",      // ifInOctets
        "1.3.6.1.2.1.2.2.1.16",      // ifOutOctets
    };

    private final static String[] ifInPktsOIDs = new String[] {
        "1.3.6.1.2.1.2.2.1.1",       // ifIndex
        "1.3.6.1.2.1.2.2.1.11",      // ifInUcastPkts
        "1.3.6.1.2.1.2.2.1.12",      // ifInNUcastPkts
        "1.3.6.1.2.1.2.2.1.13",      // ifInDiscards
        "1.3.6.1.2.1.2.2.1.14",      // ifInErrors
    };

    private final static String[] ifOutPktsOIDs = new String[] {
        "1.3.6.1.2.1.2.2.1.1",       // ifIndex
        "1.3.6.1.2.1.2.2.1.17",      // ifOutUcastPkts
        "1.3.6.1.2.1.2.2.1.18",      // ifOutNUcastPkts
        "1.3.6.1.2.1.2.2.1.19",      // ifOutDiscards
        "1.3.6.1.2.1.2.2.1.20"       // ifOutErrors
    };

    private final static String[] ifMulticastPktsOIDs = new String[] {
        "1.3.6.1.2.1.2.2.1.1",       // ifIndex
        "1.3.6.1.2.1.31.1.1.1.2",    // ifInMulticastPkts
        "1.3.6.1.2.1.31.1.1.1.4",    // ifOutMulticastPkts
    };

    private final static String[] ifBroadcastPktsOIDs = new String[] {
        "1.3.6.1.2.1.2.2.1.1",       // ifIndex
        "1.3.6.1.2.1.31.1.1.1.3",    // ifInBroadcastPkts
        "1.3.6.1.2.1.31.1.1.1.5",    // ifOutBroadcastPkts
    };

    private static Long INT_MAX = 4294967296L;

    private static Hashtable<String, String> ifEntity_ifStatus = new Hashtable<String, String>();
    static {
        ifEntity_ifStatus.put("1", "up");
        ifEntity_ifStatus.put("2", "down");
        ifEntity_ifStatus.put("3", "testing");
        ifEntity_ifStatus.put("5", "unknow");
        ifEntity_ifStatus.put("7", "unknow");
    };
    
    public static Hashtable<String, String> Interface_IfType = new Hashtable<String, String>();
    static {
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

    /**
     * OIDSList:
     * <p>所有的 OIDS 列表
     *
     * @since   v1.01
     */
    private static List<String[]> OIDSList = new ArrayList<String[]>();

    static {
        // 获取端口静态值
        OIDSList.add(ifIndexOIDs);
        // 获取端口状态值
        OIDSList.add(ifStatusOIDs);
        // 获取第一次端口流量
        OIDSList.add(ifOctetsOIDs);
        // 获取第二次端口流量
        OIDSList.add(ifOctetsOIDs);
        // 获取端口多播数据包
        OIDSList.add(ifMulticastPktsOIDs);
        // 获取端口广播数据包
        OIDSList.add(ifBroadcastPktsOIDs);
        // 获取端口第一次入口数据包（单播、非单播、丢包以及错包）
        OIDSList.add(ifInPktsOIDs);
        // 获取端口第二次入口数据包（单播、非单播、丢包以及错包）
        OIDSList.add(ifInPktsOIDs);
        // 获取端口第一次出口数据包（单播、非单播、丢包以及错包）
        OIDSList.add(ifOutPktsOIDs);
        // 获取端口第二次出口数据包（单播、非单播、丢包以及错包）
        OIDSList.add(ifOutPktsOIDs);
    }
    /**
     * getSimpleIndicatorValue:
     *
     * @return
     *
     * @since   v1.01
     * @see com.afunms.gather.indicator.base.BaseIndicatorGather#getSimpleIndicatorValue()
     */
    @Override
    public SimpleIndicatorValue getSimpleIndicatorValue() {
        Long calculateInterval = 10000L;
        Long calculateIntervalSecond = calculateInterval / 1000;
        Long interval = 10000L;
        List<String[][]> valueArrayList = new ArrayList<String[][]>();
        for (String[] oids : OIDSList) {
            String[][] valueArray = getTableValuesByOids(oids);
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            valueArrayList.add(valueArray);
        }
        // 获取端口静态值
        String[][] ifIndexArrays = valueArrayList.get(0);
        // 获取端口状态值
        String[][] ifStatusArrays = valueArrayList.get(1);
        // 获取第一次端口流量
        String[][] ifOctetsArrays = valueArrayList.get(2);
        // 获取第二次端口流量
        String[][] ifOctetsArrays2 = valueArrayList.get(3);
        // 获取端口多播数据包
        String[][] ifMulticastPktsArrays = valueArrayList.get(4);
        // 获取端口广播数据包
        String[][] ifBroadcastPktsArrays = valueArrayList.get(5);
        // 获取端口第一次入口数据包（单播、非单播、丢包以及错包）
        String[][] ifInPktsArrays = valueArrayList.get(6);
        // 获取端口第二次入口数据包（单播、非单播、丢包以及错包）
        String[][] ifInPktsArrays2= valueArrayList.get(7);
        // 获取端口第一次出口数据包（单播、非单播、丢包以及错包）
        String[][] ifOutPktsArrays = valueArrayList.get(8);
        // 获取端口第二次出口数据包（单播、非单播、丢包以及错包）
        String[][] ifOutPktsArrays2 = valueArrayList.get(9);

        Vector<Interfacecollectdata> interfaceVector=new Vector<Interfacecollectdata>();
        Vector<Interfacecollectdata> utilhdxVector = new Vector<Interfacecollectdata>();
        Vector<Interfacecollectdata> inUtilhdxVector = new Vector<Interfacecollectdata>();
        Vector<Interfacecollectdata> outUtilhdxVector = new Vector<Interfacecollectdata>();
        Vector<Interfacecollectdata> allutilhdxVector = new Vector<Interfacecollectdata>();
//        Vector packsVector = new Vector();
        Vector<Interfacecollectdata> inpacksVector = new Vector<Interfacecollectdata>();
        Vector<Interfacecollectdata> outpacksVector = new Vector<Interfacecollectdata>();
        Vector<Interfacecollectdata> discardspercVector = new Vector<Interfacecollectdata>();
        Vector<Interfacecollectdata> errorspercVector = new Vector<Interfacecollectdata>();
//        Vector allerrorspercVector = new Vector();
//        Vector alldiscardspercVector = new Vector();
//        Vector allutilhdxpercVector= new Vector();
        Vector<Interfacecollectdata> utilhdxpercVector = new Vector<Interfacecollectdata>();
        String ifAllUtilhdx = "0";                // 端口总流速
        String ifAllInUtilhdx = "0";              // 入口总流速
        String ifAllOutUtilhdx = "0";             // 出口总流速
//        String ifAllDiscardPerc = "0";            // 所有端口总丢包率
//        String ifAllInDiscardPerc = "0";          // 所有入口总丢包率
//        String ifAllOutDiscardPerc = "0";         // 所有出口总丢包率
//        String ifAllErrorPerc = "0";              // 所有端口总错包率
//        String ifAllInErrorPerc = "0";            // 所有入口总错包率
//        String ifAllOutErrorPerc = "0";           // 所有出口总错包率
        Long ifAllUtilhdxLong = 0L;               // 端口总流速
        Long ifAllInUtilhdxLong = 0L;             // 入口总流速
        Long ifAllOutUtilhdxLong = 0L;            // 出口总流速
//        Long ifAllUtilhdxPercLong = 0L;           // 端口总带宽
//        Long ifAllInUtilhdxPercLong = 0L;         // 入口总带宽
//        Long ifAllOutUtilhdxPercLong = 0L;        // 出口总带宽
//        Double ifAllDiscardPercDouble = 0D;            // 所有端口总丢包率
        Double ifAllInDiscardPercDouble = 0D;          // 所有入口总丢包率
        Double ifAllOutDiscardPercDouble = 0D;         // 所有出口总丢包率
//        Double ifAllErrorPercDouble = 0D;              // 所有端口总错包率
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
                interfaceVector.add(createInterfacecollectdata(date, DESC_IFINDEX, ifIndex, ifIndex, RESTYPE_STATIC, "", CHNAME_IFINDEX));

                // 描述
                interfaceVector.add(createInterfacecollectdata(date, DESC_IFDESCR, ifIndex, ifDescr, RESTYPE_STATIC, "", CHNAME_IFDESCR));

                // 类型
                interfaceVector.add(createInterfacecollectdata(date, DESC_IFTYPE, ifIndex, ifType, RESTYPE_STATIC, "", CHNAME_IFTYPE));

                // 最大数据包
                interfaceVector.add(createInterfacecollectdata(date, DESC_IFMTU, ifIndex, ifMtu, RESTYPE_STATIC, "bit", CHNAME_IFMTU));

                // 端口最大速率(bit)
                Interfacecollectdata interfacedata = createInterfacecollectdata(date, DESC_IFSPEED, ifIndex, ifSpeed, RESTYPE_STATIC, "bit", CHNAME_IFSPEED);
                interfaceVector.add(interfacedata);
                ifSpeedHashtable.put(ifIndex, interfacedata);

                // 端口Mac地址
                interfaceVector.add(createInterfacecollectdata(date, DESC_IFPHYSADDRESS, ifIndex, ifPhysAddress, RESTYPE_STATIC, "", CHNAME_IFPHYSADDRESS));
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
                interfaceVector.add(createInterfacecollectdata(date, DESC_IFADMINSTATUS, ifIndex, ifAdminStatus, RESTYPE_DYNAMIC, "", CHNAME_IFADMINSTATUS));

                // 当前状态
                interfaceVector.add(createInterfacecollectdata(date, DESC_IFOPERSTATUS, ifIndex, ifOperStatus, RESTYPE_DYNAMIC, "", CHNAME_IFOPERSTATUS));
                
                // 系统sysUpTime评估
                interfaceVector.add(createInterfacecollectdata(date, DESC_IFLASTCHANGE, ifIndex, ifLastChange, RESTYPE_DYNAMIC, "bit", CHNAME_IFLASTCHANGE));
                
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
                ifInOctetsHashtable.put(ifIndex, createInterfacecollectdata(date, DESC_IFINOCTETS, ifIndex, ifInOctets, RESTYPE_DYNAMIC, "", CHNAME_IFINOCTETS));

                // 传输的字节
                ifOutOctetsHashtable.put(ifIndex, createInterfacecollectdata(date, DESC_IFOUTOCTETS, ifIndex, ifOutOctets, RESTYPE_DYNAMIC, "", CHNAME_IFOUTOCTETS));
                
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
                        ifInBandwidthUtilHdxPerc = String.valueOf(format(ifInBandwidthUtilHdxPercDouble));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        ifOutBandwidthUtilHdxPercDouble = (ifOutBandwidthUtilHdxLong * 8.0D * 1024.0D * 100.0D) / ifSpeedLong;
                        ifOutBandwidthUtilHdxPerc = String.valueOf(format(ifOutBandwidthUtilHdxPercDouble));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // 端口入口流速
                Interfacecollectdata inUtilHdx = createInterfacecollectdata(date, DESC_IFINBANDWIDTHUTILHDX, ifIndex, ifInBandwidthUtilHdx, RESTYPE_DYNAMIC, "KB/s", ifIndex + CHNAME_IFINBANDWIDTHUTILHDX);
                inUtilhdxVector.add(inUtilHdx);
                utilhdxVector.add(inUtilHdx);

                // 端口出口流速
                Interfacecollectdata outUtilHdx = createInterfacecollectdata(date, DESC_IFOUTBANDWIDTHUTILHDX, ifIndex, ifOutBandwidthUtilHdx, RESTYPE_DYNAMIC, "KB/s", ifIndex + CHNAME_IFOUTBANDWIDTHUTILHDX);
                outUtilhdxVector.add(outUtilHdx);
                utilhdxVector.add(outUtilHdx);

                // 端口入口带宽利用率
                utilhdxpercVector.add(createInterfacecollectdata(date, DESC_IFINBANDWIDTHUTILHDXPERC, ifIndex, ifInBandwidthUtilHdxPerc, RESTYPE_DYNAMIC, "%", ifIndex + CHNAME_IFINBANDWIDTHUTILHDXPERC));

                // 端口出口带宽利用率
                utilhdxpercVector.add(createInterfacecollectdata(date, DESC_IFOUTBANDWIDTHUTILHDXPERC, ifIndex, ifOutBandwidthUtilHdxPerc, RESTYPE_DYNAMIC, "%", ifIndex + CHNAME_IFOUTBANDWIDTHUTILHDXPERC));

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
                interfaceVector.add(createInterfacecollectdata(date, DESC_IFINMULTICASTPKTS, ifIndex, ifInMulticastPkts, RESTYPE_DYNAMIC, "个", CHNAME_IFINMULTICASTPKTS));

                // 出口多播数据包
                interfaceVector.add(createInterfacecollectdata(date, DESC_IFOUTMULTICASTPKTS, ifIndex, ifOutMulticastPkts, RESTYPE_DYNAMIC, "个", CHNAME_IFOUTMULTICASTPKTS));
                
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
                interfaceVector.add(createInterfacecollectdata(date, DESC_IFINBROADCASTPKTS, ifIndex, ifInBroadcastPkts, RESTYPE_DYNAMIC, "个", CHNAME_IFINBROADCASTPKTS));

                // 出口广播数据包
                interfaceVector.add(createInterfacecollectdata(date, DESC_IFOUTBROADCASTPKTS, ifIndex, ifOutBroadcastPkts, RESTYPE_DYNAMIC, "个", CHNAME_IFOUTBROADCASTPKTS));
                
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
                ifInPktsHashtable.put(ifIndex, createInterfacecollectdata(date, DESC_IFINPKTS, ifIndex, ifInPkts, RESTYPE_DYNAMIC, "个", CHNAME_IFINPKTS));

                // 入口丢包数
                ifInDiscardsHashtable.put(ifIndex, createInterfacecollectdata(date, DESC_IFINDISCARDS, ifIndex, ifInDiscards, RESTYPE_DYNAMIC, "个", CHNAME_IFINDISCARDS));
                
                // 入口错包数
                ifInErrorsHashtable.put(ifIndex, createInterfacecollectdata(date, DESC_IFINERRORS, ifIndex, ifInErrors, RESTYPE_DYNAMIC, "个", CHNAME_IFINERRORS));

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
                    ifInPktsPers = String.valueOf(ifInPktsPersLong);
                    ifInDiscardsPerc = String.valueOf(format(ifInDiscardsPercDouble));
                    ifInErrorsPerc = String.valueOf(format(ifInErrorsPercDouble));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 每秒入口包数
                inpacksVector.add(createInterfacecollectdata(date, DESC_IFINPKTSPERS, ifIndex, ifInPktsPers, RESTYPE_DYNAMIC, "个/s", CHNAME_IFINPKTSPERS));

                // 入口丢包率
                discardspercVector.add(createInterfacecollectdata(date, DESC_IFINDISCARDSPERC, ifIndex, ifInDiscardsPerc, RESTYPE_DYNAMIC, "%", CHNAME_IFINDISCARDSPERC));
                
                // 入口错包率
                errorspercVector.add(createInterfacecollectdata(date, DESC_IFINERRORSPERC, ifIndex, ifInErrorsPerc, RESTYPE_DYNAMIC, "%", CHNAME_IFINERRORSPERC));
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
                ifOutPktsHashtable.put(ifIndex, createInterfacecollectdata(date, DESC_IFOUTPKTS, ifIndex, ifOutPkts, RESTYPE_DYNAMIC, "个", CHNAME_IFOUTPKTS));

                // 出口丢包数
                ifOutDiscardsHashtable.put(ifIndex, createInterfacecollectdata(date, DESC_IFOUTDISCARDS, ifIndex, ifOutDiscards, RESTYPE_DYNAMIC, "个", CHNAME_IFOUTDISCARDS));
                
                // 出口错包数
                ifOutErrorsHashtable.put(ifIndex, createInterfacecollectdata(date, DESC_IFOUTERRORS, ifIndex, ifOutErrors, RESTYPE_DYNAMIC, "个", CHNAME_IFOUTERRORS));

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
                    ifOutDiscardsPerc = String.valueOf(format(ifOutDiscardsPercDouble));
                    ifOutErrorsPerc = String.valueOf(format(ifOutErrorsPercDouble));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 每秒出口包数
                outpacksVector.add(createInterfacecollectdata(date, DESC_IFOUTPKTSPERS, ifIndex, ifOutPktsPers, RESTYPE_DYNAMIC, "个/s", CHNAME_IFOUTPKTSPERS));

                // 出口丢包率
                discardspercVector.add(createInterfacecollectdata(date, DESC_IFOUTDISCARDSPERC, ifIndex, ifOutDiscardsPerc, RESTYPE_DYNAMIC, "%", CHNAME_IFOUTDISCARDSPERC));
                
                // 出口错包率
                errorspercVector.add(createInterfacecollectdata(date, DESC_IFOUTERRORSPERC, ifIndex, ifOutErrorsPerc, RESTYPE_DYNAMIC, "%", CHNAME_IFOUTERRORSPERC));
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

        allutilhdxVector.add(createInterfacecollectdata(date, "AllInBandwidthUtilHdx", "AllInBandwidthUtilHdx", ifAllInUtilhdx, RESTYPE_DYNAMIC, "KB/s", "入口总流速"));

        allutilhdxVector.add(createInterfacecollectdata(date, "AllOutBandwidthUtilHdx", "AllOutBandwidthUtilHdx", ifAllOutUtilhdx, RESTYPE_DYNAMIC, "KB/s", "出口总流速"));
        
        allutilhdxVector.add(createInterfacecollectdata(date, "AllBandwidthUtilHdx", "AllBandwidthUtilHdx", ifAllUtilhdx, RESTYPE_DYNAMIC, "KB/s", "综合流速"));

        Hashtable<String, Vector<Interfacecollectdata>> interfaceHashtable = new Hashtable<String, Vector<Interfacecollectdata>>();
        interfaceHashtable.put("interface",interfaceVector);        
//        interfaceHashtable.put("allutilhdxperc",allutilhdxpercVector);
        interfaceHashtable.put("allutilhdx",allutilhdxVector);
        interfaceHashtable.put("utilhdxperc",utilhdxpercVector);
        interfaceHashtable.put("utilhdx",utilhdxVector);   
        interfaceHashtable.put("inUtilhdx",inUtilhdxVector);        
        interfaceHashtable.put("outUtilhdx",outUtilhdxVector);        
        interfaceHashtable.put("discardsperc",discardspercVector);
        interfaceHashtable.put("errorsperc",errorspercVector);
//        interfaceHashtable.put("allerrorsperc",allerrorspercVector);
//        interfaceHashtable.put("alldiscardsperc",alldiscardspercVector);
//        interfaceHashtable.put("packs",packsVector);
        interfaceHashtable.put("inpacks",inpacksVector);
        interfaceHashtable.put("outpacks",outpacksVector);
        return createSimpleIndicatorValue(interfaceHashtable);
    }

    public Interfacecollectdata createInterfacecollectdata(Calendar date, String entity, String subentity, String thevalue, String restype, String unit, String chname) {
        Interfacecollectdata interfacedata = new Interfacecollectdata();
        interfacedata.setIpaddress(getIpAddress());
        interfacedata.setCollecttime(date);
        interfacedata.setCategory("Interface");
        interfacedata.setEntity(entity);
        interfacedata.setSubentity(subentity);
        interfacedata.setThevalue(thevalue);
        interfacedata.setRestype(restype);
        interfacedata.setUnit(unit);
        interfacedata.setChname(chname);
        return interfacedata;
    }
//
//    public UtilHdx createUtilHdx(Calendar date, String entity, String subentity, String thevalue, String restype, String unit, String chname) {
//        UtilHdx utilHdx = new UtilHdx();
//        utilHdx.setIpaddress(getIpAddress());
//        utilHdx.setCollecttime(date);
//        utilHdx.setCategory("Interface");
//        utilHdx.setEntity(entity);
//        utilHdx.setSubentity(subentity);
//        utilHdx.setThevalue(thevalue);
//        utilHdx.setRestype(restype);
//        utilHdx.setUnit(unit);
//        utilHdx.setChname(chname);
//        return utilHdx;
//    }
//
//    public UtilHdxPerc createUtilHdxPerc(Calendar date, String entity, String subentity, String thevalue, String restype, String unit, String chname) {
//        // 端口带宽利用率
//        UtilHdxPerc utilHdxPerc = new UtilHdxPerc();
//        utilHdxPerc.setIpaddress(getIpAddress());
//        utilHdxPerc.setCollecttime(date);
//        utilHdxPerc.setCategory("Interface");
//        utilHdxPerc.setEntity(entity);
//        utilHdxPerc.setSubentity(subentity);
//        utilHdxPerc.setThevalue(thevalue);
//        utilHdxPerc.setRestype(restype);
//        utilHdxPerc.setUnit(unit);
//        utilHdxPerc.setChname(chname);
//        return utilHdxPerc;
//    }
//    
//    public InPkts createInPkts(Calendar date, String entity, String subentity, String thevalue, String restype, String unit, String chname) {
//        // 每秒入口包数
//        InPkts inPkts = new InPkts();
//        inPkts.setIpaddress(getIpAddress());
//        inPkts.setCollecttime(date);
//        inPkts.setCategory("Interface");
//        inPkts.setEntity(entity);
//        inPkts.setSubentity(subentity);
//        inPkts.setThevalue(thevalue);
//        inPkts.setRestype(restype);
//        inPkts.setUnit(unit);
//        inPkts.setChname(chname);
//        return inPkts;
//    }
//
//    public DiscardsPerc createDiscardsPerc(Calendar date, String entity, String subentity, String thevalue, String restype, String unit, String chname) {
//        // 丢包率
//        DiscardsPerc discardsPerc = new DiscardsPerc();
//        discardsPerc.setIpaddress(getIpAddress());
//        discardsPerc.setCollecttime(date);
//        discardsPerc.setCategory("Interface");
//        discardsPerc.setEntity(entity);
//        discardsPerc.setSubentity(subentity);
//        discardsPerc.setThevalue(thevalue);
//        discardsPerc.setRestype(restype);
//        discardsPerc.setUnit(unit);
//        discardsPerc.setChname(chname);
//        return discardsPerc;
//    }
//
//    public ErrorsPerc createErrorsPerc(Calendar date, String entity, String subentity, String thevalue, String restype, String unit, String chname) {
//        // 错包率
//        ErrorsPerc errorsPerc = new ErrorsPerc();
//        errorsPerc.setIpaddress(getIpAddress());
//        errorsPerc.setCollecttime(date);
//        errorsPerc.setCategory("Interface");
//        errorsPerc.setEntity(entity);
//        errorsPerc.setSubentity(subentity);
//        errorsPerc.setThevalue(thevalue);
//        errorsPerc.setRestype(restype);
//        errorsPerc.setUnit(unit);
//        errorsPerc.setChname(chname);
//        return errorsPerc;
//    }
//
//    public OutPkts createOutPkts(Calendar date, String entity, String subentity, String thevalue, String restype, String unit, String chname) {
//        // 每秒出口包数
//        OutPkts outPkts = new OutPkts();
//        outPkts.setIpaddress(getIpAddress());
//        outPkts.setCollecttime(date);
//        outPkts.setCategory("Interface");
//        outPkts.setEntity(entity);
//        outPkts.setSubentity(subentity);
//        outPkts.setThevalue(thevalue);
//        outPkts.setRestype(restype);
//        outPkts.setUnit(unit);
//        outPkts.setChname(chname);
//        return outPkts;
//    }
//    
//    public AllUtilHdx createAllUtilHdx(Calendar date, String entity, String subentity, String thevalue, String restype, String unit, String chname) {
//        AllUtilHdx allInUtilhdx = new AllUtilHdx();
//        allInUtilhdx.setIpaddress(getIpAddress());
//        allInUtilhdx.setCollecttime(date);
//        allInUtilhdx.setCategory("Interface");
//        allInUtilhdx.setEntity(entity);
//        allInUtilhdx.setSubentity(subentity);
//        allInUtilhdx.setThevalue(thevalue);    
//        allInUtilhdx.setRestype(restype);
//        allInUtilhdx.setUnit(unit);    
//        allInUtilhdx.setChname(chname);
//        return allInUtilhdx;
//    }
}

