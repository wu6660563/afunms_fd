/*
 * @(#)Tru64InterfaceLogFileIndicatorGather.java     v1.01, 2014 1 15
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.tru64;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.gather.indicator.base.IndicatorValueFactory;
import com.afunms.gather.indicator.base.LogFileIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.snmp.tru64.Tru64LogFileKeywordConstant;

/**
 * ClassName:   Tru64InterfaceLogFileIndicatorGather.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 15 16:59:35
 */
public class Tru64InterfaceLogFileIndicatorGather extends LogFileIndicatorGather {

    /**
     * BEGIN_KEYWORD:
     * <p>NETSTAT 开始字符串
     *
     * @since   v1.01
     */
    private static final String NETSTAT_BEGIN_KEYWORD = Tru64LogFileKeywordConstant.TRU64_NETSTAT_BEGIN_KEYWORD;

    /**
     * END_KEYWORD:
     * <p>NETSTAT 结束字符串
     *
     * @since   v1.01
     */
    private static final String NETSTAT_END_KEYWORD = Tru64LogFileKeywordConstant.TRU64_NETSTAT_END_KEYWORD;

    /**
     * BEGIN_KEYWORD:
     * <p>IPCONFIG 开始字符串
     *
     * @since   v1.01
     */
    private static final String IPCONFIG_START_KEYWORD = Tru64LogFileKeywordConstant.TRU64_IPCONFIG_START_KEYWORD;

    /**
     * END_KEYWORD:
     * <p>IPCONFIG 结束字符串
     *
     * @since   v1.01
     */
    private static final String IPCONFIG_END_KEYWORD = Tru64LogFileKeywordConstant.TRU64_IPCONFIG_END_KEYWORD;

    /**
     * getSimpleIndicatorValue:
     *
     * @return
     *
     * @since   v1.01
     * @see com.afunms.gather.indicator.base.BaseIndicatorGather#getSimpleIndicatorValue()
     */
    @SuppressWarnings("unchecked")
    @Override
    public SimpleIndicatorValue getSimpleIndicatorValue() {
        String beginStr = NETSTAT_BEGIN_KEYWORD;
        String endStr = NETSTAT_END_KEYWORD;
        
        String beginStr2 = IPCONFIG_START_KEYWORD;
        String endStr2 = IPCONFIG_END_KEYWORD;
        String ipconfigContent = getLogFileContent(beginStr2, endStr2);
        String netstatContent = getLogFileContent(beginStr, endStr);
        ArrayList<Hashtable<String, String>> iflist = new ArrayList<Hashtable<String, String>>();
        Vector<Interfacecollectdata> interfaceVector = new Vector<Interfacecollectdata>();
        Vector<Interfacecollectdata> utilhdxVector = new Vector<Interfacecollectdata>();
        Vector<Interfacecollectdata> allutilhdxVector = new Vector<Interfacecollectdata>();
        Calendar date = getCalendar();
        try {
            Hashtable<String, Hashtable<String, String>> ipconfigHashtable = new Hashtable<String, Hashtable<String, String>>();
            Hashtable<String, String> perIpconfigHashtable = null;
            String[] ipconfigLineArr = ipconfigContent.trim().split("\n");
            for (int i = 0; i < ipconfigLineArr.length; i++) {
                String lineArr = ipconfigLineArr[i].trim();
                if (lineArr.indexOf("flags") >= 0) {
                    //ee0: flags=4000c63<UP,BROADCAST,NOTRAILERS,RUNNING,MULTICAST,SIMPLEX,RESERVED>
                    perIpconfigHashtable = new Hashtable<String, String>();
                    String[] tmpData = lineArr.split(":");
                    String name = tmpData[0].trim();
                    perIpconfigHashtable.put("name", name);
                    if(tmpData[1].indexOf("<")>0&&tmpData[1].indexOf(",")>0){
                        String status = tmpData[1].substring(tmpData[1].indexOf("<") +1 , tmpData[1].indexOf(",")).trim();
                        perIpconfigHashtable.put("status", status);
                    }
                    ipconfigHashtable.put(name, perIpconfigHashtable);
                }
                if(lineArr.indexOf("inet") >= 0){
                    //inet 127.0.0.1 netmask ff000000 ipmtu 4096 
                    String ipaddress = lineArr.substring(lineArr.indexOf("inet")+4, lineArr.indexOf("netmask")).trim();
                    perIpconfigHashtable.put("ipaddress", ipaddress);
                }
            }

            // 入口总流速 浮点型
            Double AllInBandwidthUtilHdxDouble = 0D;
            // 出口总流速 浮点型
            Double AllOutBandwidthUtilHdxDouble = 0D;
            String[] netstatLineArr = netstatContent.trim().split("\n");
            if (netstatLineArr != null && netstatLineArr.length > 0) {
                // 开始循环网络接口
                Hashtable<String, String> portDescHashtable = new Hashtable<String, String>();
                for (int k = 1; k < netstatLineArr.length; k++) {
                    String[] netstat_tmpData = netstatLineArr[k].trim().split("\\s++");
                    if(netstat_tmpData != null && netstat_tmpData.length>= 9){
                        
                        String portDesc = netstat_tmpData[0].trim();            // Name
                        String mtu = netstat_tmpData[1].trim();                 // Mtu          
                        //String network = netstat_tmpData[2].trim();             // Network      未使用
                        //String address = netstat_tmpData[3].trim();             // Address      未使用
                        String inPackets = netstat_tmpData[4].trim();// Ipkts        入口流量包   
                        //String ierrs = netstat_tmpData[5].trim();// Ierrs        入口错误包   未使用
                        String outPackets = netstat_tmpData[6].trim();// Opkts        出口流量包
                        //String oerrs = netstat_tmpData[7].trim();// Oerrs        出口错误包   未使用
                        //String coll = netstat_tmpData[8].trim();// Coll         未使用
                        
                        if (portDescHashtable.containsKey(portDesc)) {
                            // 过滤重复
                            continue;
                        } else {
                            portDescHashtable.put(portDesc, portDesc);
                        }
                        perIpconfigHashtable = ipconfigHashtable.get(portDesc);
                        String ifPhysAddress = "";                  // IP 或者 MAC 地址
                        if(perIpconfigHashtable != null && perIpconfigHashtable.get("ipaddress") != null){
                            ifPhysAddress = (String) perIpconfigHashtable.get("ipaddress");
                        }
                        String ifOperStatus = "up";                 // 端口状态
                        if(perIpconfigHashtable != null && perIpconfigHashtable.get("status") != null){
                            ifOperStatus = (String) perIpconfigHashtable.get("status");
                        }

                        String ifIndex = k + "";                    // 端口索引
                        String ifInPackets = inPackets;
                        String ifOutPackets = outPackets;
                        Double ifInBandwidthUtilHdxDouble = 0D;
                        Double ifOutBandwidthUtilHdxDouble = 0D;
                        if (getLastSimpleIndicatorValue() != null) {
                            Hashtable<String, Object> lastInterfaceHashtable = (Hashtable<String, Object>) getLastSimpleIndicatorValue().getValue();
                            Vector<Interfacecollectdata> lastVector = (Vector<Interfacecollectdata>) lastInterfaceHashtable.get("interface");
                            for (Interfacecollectdata interfacecollectdata : lastVector) {
                                if (DESC_IFINPKTS.equalsIgnoreCase(interfacecollectdata.getEntity())
                                                && ifIndex.equalsIgnoreCase(interfacecollectdata.getSubentity())) {
                                    Long intervalTime = date.getTime().getTime() - interfacecollectdata.getCollecttime().getTime().getTime();
                                    ifInBandwidthUtilHdxDouble = (Double.valueOf(ifInPackets) - Double.valueOf(interfacecollectdata.getThevalue())) * 1000 / intervalTime;
                                } else if (DESC_IFOUTPKTS.equalsIgnoreCase(interfacecollectdata.getEntity())
                                                && ifIndex.equalsIgnoreCase(interfacecollectdata.getSubentity())) {
                                    Long intervalTime = date.getTime().getTime() - interfacecollectdata.getCollecttime().getTime().getTime();
                                    ifOutBandwidthUtilHdxDouble = (Double.valueOf(ifOutPackets) - Double.valueOf(interfacecollectdata.getThevalue()) * 1000) / intervalTime;
                                }
                            }
                        }
                        
                        AllInBandwidthUtilHdxDouble += ifInBandwidthUtilHdxDouble;
                        AllOutBandwidthUtilHdxDouble += ifOutBandwidthUtilHdxDouble;
                        
                        // 端口索引
                        interfaceVector.add(createInterfacecollectdata(date, DESC_IFINDEX, ifIndex, ifIndex, RESTYPE_STATIC, "", CHNAME_IFINDEX));

                        // 端口描述
                        interfaceVector.add(createInterfacecollectdata(date, DESC_IFDESCR, ifIndex, portDesc, RESTYPE_STATIC, "", CHNAME_IFDESCR));

                        // 端口MAC或者IP
                        interfaceVector.add(createInterfacecollectdata(date, DESC_IFPHYSADDRESS, ifIndex, ifPhysAddress, RESTYPE_STATIC, "", CHNAME_IFPHYSADDRESS));
                        
                        // 端口带宽
                        interfaceVector.add(createInterfacecollectdata(date, DESC_IFSPEED, ifIndex, mtu, RESTYPE_STATIC, "bit", CHNAME_IFSPEED));
                        
                        // 当前状态
                        interfaceVector.add(createInterfacecollectdata(date, DESC_IFOPERSTATUS, ifIndex, ifOperStatus, RESTYPE_DYNAMIC, "", CHNAME_IFOPERSTATUS));
                        
                        // 端口入口流速
                        String ifInBandwidthUtilHdx = String.valueOf(format(ifInBandwidthUtilHdxDouble));
                        utilhdxVector.add(createInterfacecollectdata(date, DESC_IFINBANDWIDTHUTILHDX, ifIndex, ifInBandwidthUtilHdx, RESTYPE_DYNAMIC, "KB/s", ifIndex + CHNAME_IFINBANDWIDTHUTILHDX));
                        
                        // 端口出口流速
                        String ifOutBandwidthUtilHdx = String.valueOf(format(ifOutBandwidthUtilHdxDouble));
                        utilhdxVector.add(createInterfacecollectdata(date, DESC_IFOUTBANDWIDTHUTILHDX, ifIndex, ifOutBandwidthUtilHdx, RESTYPE_DYNAMIC, "KB/s", ifIndex + CHNAME_IFOUTBANDWIDTHUTILHDX));
                        
                        //入口数据包
                        interfaceVector.add(createInterfacecollectdata(date, DESC_IFINPKTS, ifIndex, ifInPackets, RESTYPE_DYNAMIC, "", ifIndex + "端口" + CHNAME_IFINPKTS));

                        
                        //出口数据包
                        interfaceVector.add(createInterfacecollectdata(date, DESC_IFOUTPKTS, ifIndex, ifOutPackets, RESTYPE_DYNAMIC, "", ifIndex + "端口" + CHNAME_IFOUTPKTS));
                    }
                }
            }

            String AllInBandwidthUtilHdx = String.valueOf(format(AllInBandwidthUtilHdxDouble));
            String AllOutBandwidthUtilHdx = String.valueOf(format(AllInBandwidthUtilHdxDouble));
            String AllBandwidthUtilHdx = String.valueOf(format(AllInBandwidthUtilHdxDouble) + format(AllInBandwidthUtilHdxDouble));

            // 端口入口总流速
            allutilhdxVector.add(createInterfacecollectdata(date, "AllInBandwidthUtilHdx", "AllInBandwidthUtilHdx", AllInBandwidthUtilHdx, RESTYPE_DYNAMIC, "KB/s", "入口总流速"));

            // 端口出口总流速
            allutilhdxVector.add(createInterfacecollectdata(date, "AllOutBandwidthUtilHdx", "AllOutBandwidthUtilHdx", AllOutBandwidthUtilHdx, RESTYPE_DYNAMIC, "KB/s", "出口总流速"));

            // 端口总流速
            allutilhdxVector.add(createInterfacecollectdata(date, "AllBandwidthUtilHdx", "AllBandwidthUtilHdx", AllBandwidthUtilHdx, RESTYPE_DYNAMIC, "KB/s", "综合流速"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Hashtable<String, Object> interfaceHashtable = new Hashtable<String, Object>();
        interfaceHashtable.put("iflist", iflist);
        interfaceHashtable.put("interface", interfaceVector);
        interfaceHashtable.put("utilhdx", utilhdxVector);
        interfaceHashtable.put("allutilhdx", allutilhdxVector);
        return createSimpleIndicatorValue(interfaceHashtable);
    }

    public Interfacecollectdata createInterfacecollectdata(Calendar date, String entity, String subentity, String thevalue, String restype, String unit, String chname) {
        return IndicatorValueFactory.createInterfacecollectdata(ipAddress, date, entity, subentity, thevalue, restype, unit, chname);
    }
}

