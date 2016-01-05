/*
 * @(#)LinuxInterfaceLogFileIndicatorGather.java     v1.01, 2014 1 14
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.linux;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.gather.indicator.base.IndicatorValueFactory;
import com.afunms.gather.indicator.base.LogFileIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.snmp.linux.LinuxLogFileKeywordConstant;

/**
 * ClassName:   LinuxInterfaceLogFileIndicatorGather.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 14 16:13:24
 */
public class LinuxInterfaceLogFileIndicatorGather extends
                LogFileIndicatorGather {

    /**
     * BEGIN_KEYWORD:
     * <p>开始字符串
     *
     * @since   v1.01
     */
    private static final String BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_INTERFACE_BEGIN_KEYWORD;

    /**
     * END_KEYWORD:
     * <p>结束字符串
     *
     * @since   v1.01
     */
    private static final String END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_INTERFACE_END_KEYWORD;

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
        String beginStr = BEGIN_KEYWORD;
        String endStr = END_KEYWORD;
        String interfaceContent = getLogFileContent(beginStr, endStr);
        Calendar date = getCalendar();

        String[] interfaceLineArr = interfaceContent.split("\n");
        
        ArrayList<Hashtable<String, String>> iflist = new ArrayList<Hashtable<String, String>>();
        Vector<Interfacecollectdata> interfaceVector = new Vector<Interfacecollectdata>();
        Vector<Interfacecollectdata> utilhdxVector = new Vector<Interfacecollectdata>();
        Vector<Interfacecollectdata> allutilhdxVector = new Vector<Interfacecollectdata>();
        
        // 入口总流速 浮点型
        Double AllInBandwidthUtilHdxDouble = 0D;
        // 出口总流速 浮点型
        Double AllOutBandwidthUtilHdxDouble = 0D;
        for (int i = 0; i < interfaceLineArr.length; i++) {
            String[] interface_tmpData = interfaceLineArr[i].trim().split(
                    "\\s++");
            if (interface_tmpData != null && interface_tmpData.length == 9) {
                if (interfaceLineArr[i].contains("Average:")
                        || interfaceLineArr[i].contains("平均时间:")) {
                    if (interface_tmpData[1].trim().equalsIgnoreCase("IFACE")) {
                        continue;
                    }
                    Hashtable<String, String> ifhash = new Hashtable<String, String>();
                    ifhash.put("IFACE", interface_tmpData[1]);
                    ifhash.put("rxpck/s", interface_tmpData[2]);
                    ifhash.put("txpck/s", interface_tmpData[3]);
                    ifhash.put("rxbyt/s", interface_tmpData[4]);
                    ifhash.put("txbyt/s", interface_tmpData[5]);
                    ifhash.put("rxcmp/s", interface_tmpData[6]);
                    ifhash.put("txcmp/s", interface_tmpData[7]);
                    ifhash.put("rxmcst/s", interface_tmpData[8]);
                    iflist.add(ifhash);
                    
                    String ifDescr = interface_tmpData[1];
                    // 入口流速 浮点型
                    Double ifInBandwidthUtilHdxDouble = Double.parseDouble(interface_tmpData[4]) / 1024;
                    // 出口流速 浮点型
                    Double ifOutBandwidthUtilHdxDouble = Double.parseDouble(interface_tmpData[5]) / 1024;
                    // 入口流速 字符串型
                    String ifInBandwidthUtilHdx = String.valueOf(format(ifInBandwidthUtilHdxDouble));
                    // 出口流速 字符串型
                    String ifOutBandwidthUtilHdx = String.valueOf(format(ifOutBandwidthUtilHdxDouble));

                    AllInBandwidthUtilHdxDouble += ifInBandwidthUtilHdxDouble;
                    AllOutBandwidthUtilHdxDouble += ifOutBandwidthUtilHdxDouble;

                    String ifIndex = i + "";

                    // 端口索引
                    interfaceVector.add(createInterfacecollectdata(date, DESC_IFINDEX, ifIndex, ifIndex, RESTYPE_STATIC, "", CHNAME_IFINDEX));

                    // 端口描述
                    interfaceVector.add(createInterfacecollectdata(date, DESC_IFDESCR, ifIndex, ifDescr, RESTYPE_STATIC, "", CHNAME_IFDESCR));

                    // 端口带宽 (为空)
                    interfaceVector.add(createInterfacecollectdata(date, DESC_IFSPEED, ifIndex, "", RESTYPE_STATIC, "bit", CHNAME_IFSPEED));

                    // 当前状态 (为空)
                    interfaceVector.add(createInterfacecollectdata(date, DESC_IFOPERSTATUS, ifIndex, "up", RESTYPE_DYNAMIC, "", CHNAME_IFOPERSTATUS));

                    // 端口入口流速
                    utilhdxVector.add(createInterfacecollectdata(date, DESC_IFINBANDWIDTHUTILHDX, ifIndex, ifInBandwidthUtilHdx, RESTYPE_DYNAMIC, "KB/s", ifIndex + CHNAME_IFINBANDWIDTHUTILHDX));

                    // 端口出口流速
                    utilhdxVector.add(createInterfacecollectdata(date, DESC_IFOUTBANDWIDTHUTILHDX, ifIndex, ifOutBandwidthUtilHdx, RESTYPE_DYNAMIC, "KB/s", ifIndex + CHNAME_IFOUTBANDWIDTHUTILHDX));
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

