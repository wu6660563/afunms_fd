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
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 14 16:13:24
 */
public class LinuxInterfaceLogFileIndicatorGather extends
                LogFileIndicatorGather {

    /**
     * BEGIN_KEYWORD:
     * <p>��ʼ�ַ���
     *
     * @since   v1.01
     */
    private static final String BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_INTERFACE_BEGIN_KEYWORD;

    /**
     * END_KEYWORD:
     * <p>�����ַ���
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
        
        // ��������� ������
        Double AllInBandwidthUtilHdxDouble = 0D;
        // ���������� ������
        Double AllOutBandwidthUtilHdxDouble = 0D;
        for (int i = 0; i < interfaceLineArr.length; i++) {
            String[] interface_tmpData = interfaceLineArr[i].trim().split(
                    "\\s++");
            if (interface_tmpData != null && interface_tmpData.length == 9) {
                if (interfaceLineArr[i].contains("Average:")
                        || interfaceLineArr[i].contains("ƽ��ʱ��:")) {
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
                    // ������� ������
                    Double ifInBandwidthUtilHdxDouble = Double.parseDouble(interface_tmpData[4]) / 1024;
                    // �������� ������
                    Double ifOutBandwidthUtilHdxDouble = Double.parseDouble(interface_tmpData[5]) / 1024;
                    // ������� �ַ�����
                    String ifInBandwidthUtilHdx = String.valueOf(format(ifInBandwidthUtilHdxDouble));
                    // �������� �ַ�����
                    String ifOutBandwidthUtilHdx = String.valueOf(format(ifOutBandwidthUtilHdxDouble));

                    AllInBandwidthUtilHdxDouble += ifInBandwidthUtilHdxDouble;
                    AllOutBandwidthUtilHdxDouble += ifOutBandwidthUtilHdxDouble;

                    String ifIndex = i + "";

                    // �˿�����
                    interfaceVector.add(createInterfacecollectdata(date, DESC_IFINDEX, ifIndex, ifIndex, RESTYPE_STATIC, "", CHNAME_IFINDEX));

                    // �˿�����
                    interfaceVector.add(createInterfacecollectdata(date, DESC_IFDESCR, ifIndex, ifDescr, RESTYPE_STATIC, "", CHNAME_IFDESCR));

                    // �˿ڴ��� (Ϊ��)
                    interfaceVector.add(createInterfacecollectdata(date, DESC_IFSPEED, ifIndex, "", RESTYPE_STATIC, "bit", CHNAME_IFSPEED));

                    // ��ǰ״̬ (Ϊ��)
                    interfaceVector.add(createInterfacecollectdata(date, DESC_IFOPERSTATUS, ifIndex, "up", RESTYPE_DYNAMIC, "", CHNAME_IFOPERSTATUS));

                    // �˿��������
                    utilhdxVector.add(createInterfacecollectdata(date, DESC_IFINBANDWIDTHUTILHDX, ifIndex, ifInBandwidthUtilHdx, RESTYPE_DYNAMIC, "KB/s", ifIndex + CHNAME_IFINBANDWIDTHUTILHDX));

                    // �˿ڳ�������
                    utilhdxVector.add(createInterfacecollectdata(date, DESC_IFOUTBANDWIDTHUTILHDX, ifIndex, ifOutBandwidthUtilHdx, RESTYPE_DYNAMIC, "KB/s", ifIndex + CHNAME_IFOUTBANDWIDTHUTILHDX));
                }

            }
        }

        String AllInBandwidthUtilHdx = String.valueOf(format(AllInBandwidthUtilHdxDouble));
        String AllOutBandwidthUtilHdx = String.valueOf(format(AllInBandwidthUtilHdxDouble));
        String AllBandwidthUtilHdx = String.valueOf(format(AllInBandwidthUtilHdxDouble) + format(AllInBandwidthUtilHdxDouble));

        // �˿����������
        allutilhdxVector.add(createInterfacecollectdata(date, "AllInBandwidthUtilHdx", "AllInBandwidthUtilHdx", AllInBandwidthUtilHdx, RESTYPE_DYNAMIC, "KB/s", "���������"));

        // �˿ڳ���������
        allutilhdxVector.add(createInterfacecollectdata(date, "AllOutBandwidthUtilHdx", "AllOutBandwidthUtilHdx", AllOutBandwidthUtilHdx, RESTYPE_DYNAMIC, "KB/s", "����������"));

        // �˿�������
        allutilhdxVector.add(createInterfacecollectdata(date, "AllBandwidthUtilHdx", "AllBandwidthUtilHdx", AllBandwidthUtilHdx, RESTYPE_DYNAMIC, "KB/s", "�ۺ�����"));

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

