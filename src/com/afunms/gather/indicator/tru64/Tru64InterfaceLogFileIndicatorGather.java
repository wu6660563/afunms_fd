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
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 15 16:59:35
 */
public class Tru64InterfaceLogFileIndicatorGather extends LogFileIndicatorGather {

    /**
     * BEGIN_KEYWORD:
     * <p>NETSTAT ��ʼ�ַ���
     *
     * @since   v1.01
     */
    private static final String NETSTAT_BEGIN_KEYWORD = Tru64LogFileKeywordConstant.TRU64_NETSTAT_BEGIN_KEYWORD;

    /**
     * END_KEYWORD:
     * <p>NETSTAT �����ַ���
     *
     * @since   v1.01
     */
    private static final String NETSTAT_END_KEYWORD = Tru64LogFileKeywordConstant.TRU64_NETSTAT_END_KEYWORD;

    /**
     * BEGIN_KEYWORD:
     * <p>IPCONFIG ��ʼ�ַ���
     *
     * @since   v1.01
     */
    private static final String IPCONFIG_START_KEYWORD = Tru64LogFileKeywordConstant.TRU64_IPCONFIG_START_KEYWORD;

    /**
     * END_KEYWORD:
     * <p>IPCONFIG �����ַ���
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

            // ��������� ������
            Double AllInBandwidthUtilHdxDouble = 0D;
            // ���������� ������
            Double AllOutBandwidthUtilHdxDouble = 0D;
            String[] netstatLineArr = netstatContent.trim().split("\n");
            if (netstatLineArr != null && netstatLineArr.length > 0) {
                // ��ʼѭ������ӿ�
                Hashtable<String, String> portDescHashtable = new Hashtable<String, String>();
                for (int k = 1; k < netstatLineArr.length; k++) {
                    String[] netstat_tmpData = netstatLineArr[k].trim().split("\\s++");
                    if(netstat_tmpData != null && netstat_tmpData.length>= 9){
                        
                        String portDesc = netstat_tmpData[0].trim();            // Name
                        String mtu = netstat_tmpData[1].trim();                 // Mtu          
                        //String network = netstat_tmpData[2].trim();             // Network      δʹ��
                        //String address = netstat_tmpData[3].trim();             // Address      δʹ��
                        String inPackets = netstat_tmpData[4].trim();// Ipkts        ���������   
                        //String ierrs = netstat_tmpData[5].trim();// Ierrs        ��ڴ����   δʹ��
                        String outPackets = netstat_tmpData[6].trim();// Opkts        ����������
                        //String oerrs = netstat_tmpData[7].trim();// Oerrs        ���ڴ����   δʹ��
                        //String coll = netstat_tmpData[8].trim();// Coll         δʹ��
                        
                        if (portDescHashtable.containsKey(portDesc)) {
                            // �����ظ�
                            continue;
                        } else {
                            portDescHashtable.put(portDesc, portDesc);
                        }
                        perIpconfigHashtable = ipconfigHashtable.get(portDesc);
                        String ifPhysAddress = "";                  // IP ���� MAC ��ַ
                        if(perIpconfigHashtable != null && perIpconfigHashtable.get("ipaddress") != null){
                            ifPhysAddress = (String) perIpconfigHashtable.get("ipaddress");
                        }
                        String ifOperStatus = "up";                 // �˿�״̬
                        if(perIpconfigHashtable != null && perIpconfigHashtable.get("status") != null){
                            ifOperStatus = (String) perIpconfigHashtable.get("status");
                        }

                        String ifIndex = k + "";                    // �˿�����
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
                        
                        // �˿�����
                        interfaceVector.add(createInterfacecollectdata(date, DESC_IFINDEX, ifIndex, ifIndex, RESTYPE_STATIC, "", CHNAME_IFINDEX));

                        // �˿�����
                        interfaceVector.add(createInterfacecollectdata(date, DESC_IFDESCR, ifIndex, portDesc, RESTYPE_STATIC, "", CHNAME_IFDESCR));

                        // �˿�MAC����IP
                        interfaceVector.add(createInterfacecollectdata(date, DESC_IFPHYSADDRESS, ifIndex, ifPhysAddress, RESTYPE_STATIC, "", CHNAME_IFPHYSADDRESS));
                        
                        // �˿ڴ���
                        interfaceVector.add(createInterfacecollectdata(date, DESC_IFSPEED, ifIndex, mtu, RESTYPE_STATIC, "bit", CHNAME_IFSPEED));
                        
                        // ��ǰ״̬
                        interfaceVector.add(createInterfacecollectdata(date, DESC_IFOPERSTATUS, ifIndex, ifOperStatus, RESTYPE_DYNAMIC, "", CHNAME_IFOPERSTATUS));
                        
                        // �˿��������
                        String ifInBandwidthUtilHdx = String.valueOf(format(ifInBandwidthUtilHdxDouble));
                        utilhdxVector.add(createInterfacecollectdata(date, DESC_IFINBANDWIDTHUTILHDX, ifIndex, ifInBandwidthUtilHdx, RESTYPE_DYNAMIC, "KB/s", ifIndex + CHNAME_IFINBANDWIDTHUTILHDX));
                        
                        // �˿ڳ�������
                        String ifOutBandwidthUtilHdx = String.valueOf(format(ifOutBandwidthUtilHdxDouble));
                        utilhdxVector.add(createInterfacecollectdata(date, DESC_IFOUTBANDWIDTHUTILHDX, ifIndex, ifOutBandwidthUtilHdx, RESTYPE_DYNAMIC, "KB/s", ifIndex + CHNAME_IFOUTBANDWIDTHUTILHDX));
                        
                        //������ݰ�
                        interfaceVector.add(createInterfacecollectdata(date, DESC_IFINPKTS, ifIndex, ifInPackets, RESTYPE_DYNAMIC, "", ifIndex + "�˿�" + CHNAME_IFINPKTS));

                        
                        //�������ݰ�
                        interfaceVector.add(createInterfacecollectdata(date, DESC_IFOUTPKTS, ifIndex, ifOutPackets, RESTYPE_DYNAMIC, "", ifIndex + "�˿�" + CHNAME_IFOUTPKTS));
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

