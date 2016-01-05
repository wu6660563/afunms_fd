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
 * @author      ����
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
     * <p>���е� OIDS �б�
     *
     * @since   v1.01
     */
    private static List<String[]> OIDSList = new ArrayList<String[]>();

    static {
        // ��ȡ�˿ھ�ֵ̬
        OIDSList.add(ifIndexOIDs);
        // ��ȡ�˿�״ֵ̬
        OIDSList.add(ifStatusOIDs);
        // ��ȡ��һ�ζ˿�����
        OIDSList.add(ifOctetsOIDs);
        // ��ȡ�ڶ��ζ˿�����
        OIDSList.add(ifOctetsOIDs);
        // ��ȡ�˿ڶಥ���ݰ�
        OIDSList.add(ifMulticastPktsOIDs);
        // ��ȡ�˿ڹ㲥���ݰ�
        OIDSList.add(ifBroadcastPktsOIDs);
        // ��ȡ�˿ڵ�һ��������ݰ����������ǵ����������Լ������
        OIDSList.add(ifInPktsOIDs);
        // ��ȡ�˿ڵڶ���������ݰ����������ǵ����������Լ������
        OIDSList.add(ifInPktsOIDs);
        // ��ȡ�˿ڵ�һ�γ������ݰ����������ǵ����������Լ������
        OIDSList.add(ifOutPktsOIDs);
        // ��ȡ�˿ڵڶ��γ������ݰ����������ǵ����������Լ������
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
        // ��ȡ�˿ھ�ֵ̬
        String[][] ifIndexArrays = valueArrayList.get(0);
        // ��ȡ�˿�״ֵ̬
        String[][] ifStatusArrays = valueArrayList.get(1);
        // ��ȡ��һ�ζ˿�����
        String[][] ifOctetsArrays = valueArrayList.get(2);
        // ��ȡ�ڶ��ζ˿�����
        String[][] ifOctetsArrays2 = valueArrayList.get(3);
        // ��ȡ�˿ڶಥ���ݰ�
        String[][] ifMulticastPktsArrays = valueArrayList.get(4);
        // ��ȡ�˿ڹ㲥���ݰ�
        String[][] ifBroadcastPktsArrays = valueArrayList.get(5);
        // ��ȡ�˿ڵ�һ��������ݰ����������ǵ����������Լ������
        String[][] ifInPktsArrays = valueArrayList.get(6);
        // ��ȡ�˿ڵڶ���������ݰ����������ǵ����������Լ������
        String[][] ifInPktsArrays2= valueArrayList.get(7);
        // ��ȡ�˿ڵ�һ�γ������ݰ����������ǵ����������Լ������
        String[][] ifOutPktsArrays = valueArrayList.get(8);
        // ��ȡ�˿ڵڶ��γ������ݰ����������ǵ����������Լ������
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
        String ifAllUtilhdx = "0";                // �˿�������
        String ifAllInUtilhdx = "0";              // ���������
        String ifAllOutUtilhdx = "0";             // ����������
//        String ifAllDiscardPerc = "0";            // ���ж˿��ܶ�����
//        String ifAllInDiscardPerc = "0";          // ��������ܶ�����
//        String ifAllOutDiscardPerc = "0";         // ���г����ܶ�����
//        String ifAllErrorPerc = "0";              // ���ж˿��ܴ����
//        String ifAllInErrorPerc = "0";            // ��������ܴ����
//        String ifAllOutErrorPerc = "0";           // ���г����ܴ����
        Long ifAllUtilhdxLong = 0L;               // �˿�������
        Long ifAllInUtilhdxLong = 0L;             // ���������
        Long ifAllOutUtilhdxLong = 0L;            // ����������
//        Long ifAllUtilhdxPercLong = 0L;           // �˿��ܴ���
//        Long ifAllInUtilhdxPercLong = 0L;         // ����ܴ���
//        Long ifAllOutUtilhdxPercLong = 0L;        // �����ܴ���
//        Double ifAllDiscardPercDouble = 0D;            // ���ж˿��ܶ�����
        Double ifAllInDiscardPercDouble = 0D;          // ��������ܶ�����
        Double ifAllOutDiscardPercDouble = 0D;         // ���г����ܶ�����
//        Double ifAllErrorPercDouble = 0D;              // ���ж˿��ܴ����
        Double ifAllInErrorPercDouble = 0D;            // ��������ܴ����
        Double ifAllOutErrorPercDouble = 0D;           // ���г����ܴ����

        // ���ڴ�ŵ�һ�βɼ�������ֽ����� key Ϊ�˿������� Ϊ����˿��������
        Hashtable<String, Interfacecollectdata> ifInOctetsHashtable = new Hashtable<String, Interfacecollectdata>();
        // ���ڴ�ŵ�һ�βɼ��ĳ����ֽ����� key Ϊ�˿������� Ϊ����˿ڳ�������
        Hashtable<String, Interfacecollectdata> ifOutOctetsHashtable = new Hashtable<String, Interfacecollectdata>();
        // ���ڴ�Ŷ˿�������ʣ� key Ϊ�˿������� Ϊ����˿ڴ���������
        Hashtable<String, Interfacecollectdata> ifSpeedHashtable = new Hashtable<String, Interfacecollectdata>();
        // ���ڴ�Ŵ�ŵ�һ�βɼ��Ķ˿�������ݰ��������������ͷǵ������㲥�Ͷಥ���Լ���������������� key Ϊ�˿������� Ϊ����˿���ڴ���ʺͶ�����
        Hashtable<String, Interfacecollectdata> ifInPktsHashtable = new Hashtable<String, Interfacecollectdata>();
        // ���ڴ�Ŵ�ŵ�һ�βɼ��Ķ˿ڳ������ݰ��������������ͷǵ������㲥�Ͷಥ���Լ���������������� key Ϊ�˿������� Ϊ����˿ڳ��ڴ���ʺͶ�����
        Hashtable<String, Interfacecollectdata> ifOutPktsHashtable = new Hashtable<String, Interfacecollectdata>();
        // ���ڴ�Ŵ�ŵ�һ�βɼ��Ķ˿���ڶ������� key Ϊ�˿������� Ϊ����˿ڳ��ڴ���ʺͶ�����
        Hashtable<String, Interfacecollectdata> ifInDiscardsHashtable = new Hashtable<String, Interfacecollectdata>();
        // ���ڴ�Ŵ�ŵ�һ�βɼ��Ķ˿���ڴ������ key Ϊ�˿������� Ϊ����˿ڳ��ڴ���ʺͶ�����
        Hashtable<String, Interfacecollectdata> ifInErrorsHashtable = new Hashtable<String, Interfacecollectdata>();
        // ���ڴ�Ŵ�ŵ�һ�βɼ��Ķ˿ڳ��ڶ������� key Ϊ�˿������� Ϊ����˿ڳ��ڴ���ʺͶ�����
        Hashtable<String, Interfacecollectdata> ifOutDiscardsHashtable = new Hashtable<String, Interfacecollectdata>();
        // ���ڴ�Ŵ�ŵ�һ�βɼ��Ķ˿ڳ��ڴ������ key Ϊ�˿������� Ϊ����˿ڳ��ڴ���ʺͶ�����
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
                    // �ų�����
                    ifPhysAddress = "";
                }
                if (ifType == null || (!Interface_IfType.contains(ifType.trim()) && 
                        !Interface_IfType.containsKey(ifType.trim()))) {
                    ifType = "1";
                }
                ifType = (String) Interface_IfType.get(ifType.trim());
                ifPhysAddress = CommonUtil.removeIllegalStr(ifPhysAddress);

                // �˿�����
                interfaceVector.add(createInterfacecollectdata(date, DESC_IFINDEX, ifIndex, ifIndex, RESTYPE_STATIC, "", CHNAME_IFINDEX));

                // ����
                interfaceVector.add(createInterfacecollectdata(date, DESC_IFDESCR, ifIndex, ifDescr, RESTYPE_STATIC, "", CHNAME_IFDESCR));

                // ����
                interfaceVector.add(createInterfacecollectdata(date, DESC_IFTYPE, ifIndex, ifType, RESTYPE_STATIC, "", CHNAME_IFTYPE));

                // ������ݰ�
                interfaceVector.add(createInterfacecollectdata(date, DESC_IFMTU, ifIndex, ifMtu, RESTYPE_STATIC, "bit", CHNAME_IFMTU));

                // �˿��������(bit)
                Interfacecollectdata interfacedata = createInterfacecollectdata(date, DESC_IFSPEED, ifIndex, ifSpeed, RESTYPE_STATIC, "bit", CHNAME_IFSPEED);
                interfaceVector.add(interfacedata);
                ifSpeedHashtable.put(ifIndex, interfacedata);

                // �˿�Mac��ַ
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
                // �ж϶˿ڹ���״̬
                if (ifAdminStatus == null || (!ifEntity_ifStatus.contains(ifAdminStatus.trim())
                        && !ifEntity_ifStatus.containsKey(ifAdminStatus.trim()))) {
                    // ���Ϊ null ���߼Ȳ�������״̬���ֲ���״̬�Ĵ��� Ĭ�ϴ���Ϊ 1 ����״̬
                    ifAdminStatus = "1";
                }
                if (!ifEntity_ifStatus.contains(ifAdminStatus.trim())) {
                    // ���Ϊ��������״̬
                    ifAdminStatus = (String)ifEntity_ifStatus.get(ifAdminStatus.trim());
                }
                // �ж϶˿ڵ�ǰ״̬
                if (ifOperStatus == null || (!ifEntity_ifStatus.contains(ifOperStatus.trim())
                        && !ifEntity_ifStatus.containsKey(ifOperStatus.trim()))) {
                    // ���Ϊ null ���߼Ȳ�������״̬���ֲ���״̬�Ĵ��� Ĭ�ϴ���Ϊ 1 ����״̬
                    ifOperStatus = "1";
                }
                if (!ifEntity_ifStatus.contains(ifOperStatus.trim())) {
                    // ���Ϊ��������״̬
                    ifOperStatus = (String)ifEntity_ifStatus.get(ifOperStatus.trim());
                }
                if (ifLastChange == null) {
                    ifLastChange = "";
                }
                // �˿������������

                // ����״̬
                interfaceVector.add(createInterfacecollectdata(date, DESC_IFADMINSTATUS, ifIndex, ifAdminStatus, RESTYPE_DYNAMIC, "", CHNAME_IFADMINSTATUS));

                // ��ǰ״̬
                interfaceVector.add(createInterfacecollectdata(date, DESC_IFOPERSTATUS, ifIndex, ifOperStatus, RESTYPE_DYNAMIC, "", CHNAME_IFOPERSTATUS));
                
                // ϵͳsysUpTime����
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

                // ���յ��ֽ�
                ifInOctetsHashtable.put(ifIndex, createInterfacecollectdata(date, DESC_IFINOCTETS, ifIndex, ifInOctets, RESTYPE_DYNAMIC, "", CHNAME_IFINOCTETS));

                // ������ֽ�
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
                
                String ifInBandwidthUtilHdx = "0";         // �˿��������
                String ifOutBandwidthUtilHdx = "0";        // �˿ڳ�������
                String ifInBandwidthUtilHdxPerc = "0";     // �˿���ڴ���������
                String ifOutBandwidthUtilHdxPerc = "0";    // �˿ڳ��ڴ���������
                Long ifInBandwidthUtilHdxLong = 0L;
                Long ifOutBandwidthUtilHdxLong = 0L;
                Double ifInBandwidthUtilHdxPercDouble = 0.0D;
                Double ifOutBandwidthUtilHdxPercDouble = 0.0D;
                String ifSpeed = "0";
                Long ifSpeedLong = 0L;
                // �˿��������
                Interfacecollectdata ifSpeedInterfacecollectdata = ifSpeedHashtable.get(ifIndex);
                if (ifSpeedInterfacecollectdata != null) {
                    ifSpeed = ifSpeedInterfacecollectdata.getThevalue();
                    try {
                        ifSpeedLong = Long.valueOf(ifSpeed);
                    } catch (Exception e) {
                    }
                }

                // ����˿�������� (���βɼ� - �ϴβɼ�) / utilhdxInterval (ʱ��)
                // �ϴζ˿�����ֽ���
                Interfacecollectdata lastInInterfacecollectdata = ifInOctetsHashtable.get(ifIndex);
                if (lastInInterfacecollectdata != null) {
                    try {
                        String lastIfInOctets = lastInInterfacecollectdata.getThevalue();
                        Long lastIfInOctetsLong = Long.valueOf(lastIfInOctets);
                        if (ifInOctetsLong < lastIfInOctetsLong) {
                            ifInOctetsLong += INT_MAX;
                        }
                        // ����֮�� / ʱ����
                        Long inOctetsBetween = ifInOctetsLong - lastIfInOctetsLong;
                        ifInBandwidthUtilHdxLong = inOctetsBetween / (calculateIntervalSecond * 1024);
                        ifInBandwidthUtilHdx = String.valueOf(ifInBandwidthUtilHdxLong);
                        ifAllInUtilhdxLong +=  ifInBandwidthUtilHdxLong;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                
                // ����˿ڳ������� (���βɼ� - �ϴβɼ� / (utilhdxInterval(ʱ��) * 1024) ת���ɶ��� ÿ��K�ֽ���KB/s
                // �ϴζ˿ڳ����ֽ���
                Interfacecollectdata lastOutInterfacecollectdata = ifOutOctetsHashtable.get(ifIndex);
                if (lastOutInterfacecollectdata != null) {
                    try {
                        String lastIfOutOctets = lastOutInterfacecollectdata.getThevalue();
                        Long lastIfOutOctetsLong = Long.valueOf(lastIfOutOctets);
                        if (ifOutOctetsLong < lastIfOutOctetsLong) {
                            ifOutOctetsLong += INT_MAX;
                        }
                        // ����֮�� / ʱ����
                        Long outOctetsBetween = ifOutOctetsLong - lastIfOutOctetsLong;
                        ifOutBandwidthUtilHdxLong = outOctetsBetween / (calculateIntervalSecond * 1024);
                        ifOutBandwidthUtilHdx = String.valueOf(ifOutBandwidthUtilHdxLong);
                        ifAllOutUtilhdxLong += ifOutBandwidthUtilHdxLong;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                
                // ������������� ( ���� * 8 * 1024 ) / ifSpeedLong �Ƚ� ���� ת���� ���� ÿ��λ�� �� b/s Ȼ������������
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

                // �˿��������
                Interfacecollectdata inUtilHdx = createInterfacecollectdata(date, DESC_IFINBANDWIDTHUTILHDX, ifIndex, ifInBandwidthUtilHdx, RESTYPE_DYNAMIC, "KB/s", ifIndex + CHNAME_IFINBANDWIDTHUTILHDX);
                inUtilhdxVector.add(inUtilHdx);
                utilhdxVector.add(inUtilHdx);

                // �˿ڳ�������
                Interfacecollectdata outUtilHdx = createInterfacecollectdata(date, DESC_IFOUTBANDWIDTHUTILHDX, ifIndex, ifOutBandwidthUtilHdx, RESTYPE_DYNAMIC, "KB/s", ifIndex + CHNAME_IFOUTBANDWIDTHUTILHDX);
                outUtilhdxVector.add(outUtilHdx);
                utilhdxVector.add(outUtilHdx);

                // �˿���ڴ���������
                utilhdxpercVector.add(createInterfacecollectdata(date, DESC_IFINBANDWIDTHUTILHDXPERC, ifIndex, ifInBandwidthUtilHdxPerc, RESTYPE_DYNAMIC, "%", ifIndex + CHNAME_IFINBANDWIDTHUTILHDXPERC));

                // �˿ڳ��ڴ���������
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

                // ��ڶಥ���ݰ�
                interfaceVector.add(createInterfacecollectdata(date, DESC_IFINMULTICASTPKTS, ifIndex, ifInMulticastPkts, RESTYPE_DYNAMIC, "��", CHNAME_IFINMULTICASTPKTS));

                // ���ڶಥ���ݰ�
                interfaceVector.add(createInterfacecollectdata(date, DESC_IFOUTMULTICASTPKTS, ifIndex, ifOutMulticastPkts, RESTYPE_DYNAMIC, "��", CHNAME_IFOUTMULTICASTPKTS));
                
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

                // ��ڹ㲥���ݰ�
                interfaceVector.add(createInterfacecollectdata(date, DESC_IFINBROADCASTPKTS, ifIndex, ifInBroadcastPkts, RESTYPE_DYNAMIC, "��", CHNAME_IFINBROADCASTPKTS));

                // ���ڹ㲥���ݰ�
                interfaceVector.add(createInterfacecollectdata(date, DESC_IFOUTBROADCASTPKTS, ifIndex, ifOutBroadcastPkts, RESTYPE_DYNAMIC, "��", CHNAME_IFOUTBROADCASTPKTS));
                
            }
        }

        // ��һ�δζ˿���ڰ�
        if (ifInPktsArrays != null && ifInPktsArrays.length > 0 ) {
            for (String[] ifInPktsArray : ifInPktsArrays) {
                if (ifInPktsArray == null || ifInPktsArray.length < 5) {
                    continue;
                }
                String ifIndex = ifInPktsArray[0];
                String ifInUcastPkts = ifInPktsArray[1];        // ��ڵ���
                String ifInNUcastPkts = ifInPktsArray[2];       // ��ڷǵ���
                String ifInDiscards = ifInPktsArray[3];         // ��ڶ���
                String ifInErrors = ifInPktsArray[4];           // ��ڴ��
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
                
                // ����ܰ���
                ifInPktsHashtable.put(ifIndex, createInterfacecollectdata(date, DESC_IFINPKTS, ifIndex, ifInPkts, RESTYPE_DYNAMIC, "��", CHNAME_IFINPKTS));

                // ��ڶ�����
                ifInDiscardsHashtable.put(ifIndex, createInterfacecollectdata(date, DESC_IFINDISCARDS, ifIndex, ifInDiscards, RESTYPE_DYNAMIC, "��", CHNAME_IFINDISCARDS));
                
                // ��ڴ����
                ifInErrorsHashtable.put(ifIndex, createInterfacecollectdata(date, DESC_IFINERRORS, ifIndex, ifInErrors, RESTYPE_DYNAMIC, "��", CHNAME_IFINERRORS));

            }
        }

        // �ڶ��ζ˿���ڰ�
        if (ifInPktsArrays2 != null && ifInPktsArrays2.length > 0 ) {
            for (String[] ifInPktsArray : ifInPktsArrays2) {
                if (ifInPktsArray == null || ifInPktsArray.length < 5) {
                    continue;
                }
                String ifIndex = ifInPktsArray[0];
                String ifInUcastPkts = ifInPktsArray[1];        // ��ڵ���
                String ifInNUcastPkts = ifInPktsArray[2];       // ��ڷǵ���
                String ifInDiscards = ifInPktsArray[3];         // ��ڶ���
                String ifInErrors = ifInPktsArray[4];           // ��ڴ��
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

                String ifInDiscardsPerc = "0";                  // ��ڶ�����
                String ifInErrorsPerc = "0";                    // ��ڴ����

                String ifInPktsPers = "0";                      // ÿ����ڰ���
                Long ifInPktsPersLong = 0L;                     // ÿ����ڰ���(��/s) ��Long
                
                Long ifInUcastPktsLong = 0L;                    // ��ڵ���
                Long ifInNUcastPktsLong = 0L;                   // ��ڷǵ���
                Long ifInPktsLong = 0L;                         // ����ܰ���
                Long ifInDiscardsLong = 0L;                     // ��ڶ���
                Long ifInErrorsLong = 0L;                       // ��ڴ��
                Long ifInPktsBetweenLong = 0L;                  // ����ܰ���֮��
                Long ifInDiscardsBetweenLong = 0L;              // ��ڶ�����֮��
                Long ifInErrorsBetweenLong = 0L;                // ��ڴ����֮��
                Long lastIfInPktsLong = 0L;                     // �ϴ�����ܰ���
                Long lastIfInDiscardsLong = 0L;                 // �ϴ���ڶ���
                Long lastIfInErrorsLong = 0L;                   // �ϴ���ڴ��
                Double ifInDiscardsPercDouble = 0D;             // ��ڶ�����
                Double ifInErrorsPercDouble = 0D;               // ��ڴ����
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
                
                // ����������ڰ���֮��
                ifInPktsBetweenLong = ifInPktsLong - lastIfInPktsLong;
                // ����������ڶ�����֮��
                ifInDiscardsBetweenLong = ifInDiscardsLong - lastIfInDiscardsLong;
                // ����������ڴ����֮��
                ifInErrorsBetweenLong = ifInErrorsLong - lastIfInErrorsLong;
                if (ifInPktsBetweenLong < 0) {
                    // ���С��0 ��˵����������
                    ifInPktsBetweenLong += INT_MAX; 
                }
                if (ifInDiscardsBetweenLong < 0) {
                    // ���С��0 ��˵����������
                    ifInDiscardsBetweenLong += INT_MAX;
                }
                if (ifInErrorsBetweenLong < 0) {
                    // ���С��0 ��˵����������
                    ifInErrorsBetweenLong += INT_MAX;
                }
                if (ifInPktsBetweenLong > 0) {
                    // ������ڴ��� 0 ���м��㣬 ������� 0 �򲻼���
                    // ����ÿ����ڰ��� ����ܰ���֮�� ���� ʱ����
                    ifInPktsPersLong = ifInPktsBetweenLong / calculateIntervalSecond;
                    // ��������֮�� ���� �ܰ���֮��
                    ifInDiscardsPercDouble = (ifInDiscardsBetweenLong * 1.0D) / ifInPktsBetweenLong;
                    // �������֮�� ���� �ܰ���֮��
                    ifInErrorsPercDouble = (ifInErrorsBetweenLong * 1.0D) / ifInPktsBetweenLong;
                    // ����ܶ�����
                    ifAllInDiscardPercDouble += ifInDiscardsPercDouble;
                    // ����ܴ����
                    ifAllInErrorPercDouble += ifInErrorsPercDouble;
                }

                try {
                    ifInPktsPers = String.valueOf(ifInPktsPersLong);
                    ifInDiscardsPerc = String.valueOf(format(ifInDiscardsPercDouble));
                    ifInErrorsPerc = String.valueOf(format(ifInErrorsPercDouble));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // ÿ����ڰ���
                inpacksVector.add(createInterfacecollectdata(date, DESC_IFINPKTSPERS, ifIndex, ifInPktsPers, RESTYPE_DYNAMIC, "��/s", CHNAME_IFINPKTSPERS));

                // ��ڶ�����
                discardspercVector.add(createInterfacecollectdata(date, DESC_IFINDISCARDSPERC, ifIndex, ifInDiscardsPerc, RESTYPE_DYNAMIC, "%", CHNAME_IFINDISCARDSPERC));
                
                // ��ڴ����
                errorspercVector.add(createInterfacecollectdata(date, DESC_IFINERRORSPERC, ifIndex, ifInErrorsPerc, RESTYPE_DYNAMIC, "%", CHNAME_IFINERRORSPERC));
            }
        }

        // ��һ�ζ˿ڳ��ڰ�
        if (ifOutPktsArrays != null && ifOutPktsArrays.length > 0 ) {
            for (String[] ifOutPktsArray : ifOutPktsArrays) {
                if (ifOutPktsArray == null || ifOutPktsArray.length < 5) {
                    continue;
                }
                String ifIndex = ifOutPktsArray[0];
                String ifOutUcastPkts = ifOutPktsArray[1];        // ���ڵ���
                String ifOutNUcastPkts = ifOutPktsArray[2];       // ���ڷǵ���
                String ifOutDiscards = ifOutPktsArray[3];         // ���ڶ���
                String ifOutErrors = ifOutPktsArray[4];           // ���ڴ��
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
                
                // �����ܰ���
                ifOutPktsHashtable.put(ifIndex, createInterfacecollectdata(date, DESC_IFOUTPKTS, ifIndex, ifOutPkts, RESTYPE_DYNAMIC, "��", CHNAME_IFOUTPKTS));

                // ���ڶ�����
                ifOutDiscardsHashtable.put(ifIndex, createInterfacecollectdata(date, DESC_IFOUTDISCARDS, ifIndex, ifOutDiscards, RESTYPE_DYNAMIC, "��", CHNAME_IFOUTDISCARDS));
                
                // ���ڴ����
                ifOutErrorsHashtable.put(ifIndex, createInterfacecollectdata(date, DESC_IFOUTERRORS, ifIndex, ifOutErrors, RESTYPE_DYNAMIC, "��", CHNAME_IFOUTERRORS));

            }
        }

        // �ڶ��ζ˿ڳ��ڰ�
        if (ifOutPktsArrays2 != null && ifOutPktsArrays2.length > 0 ) {
            for (String[] ifOutPktsArray : ifOutPktsArrays2) {
                if (ifOutPktsArray == null || ifOutPktsArray.length < 5) {
                    continue;
                }
                String ifIndex = ifOutPktsArray[0];
                String ifOutUcastPkts = ifOutPktsArray[1];        // ���ڵ���
                String ifOutNUcastPkts = ifOutPktsArray[2];       // ���ڷǵ���
                String ifOutDiscards = ifOutPktsArray[3];         // ���ڶ���
                String ifOutErrors = ifOutPktsArray[4];           // ���ڴ��
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
                String ifOutDiscardsPerc = "0";                   // ���ڶ�����
                String ifOutErrorsPerc = "0";                     // ���ڴ����

                String ifOutPktsPers = "0";                       // ÿ����ڰ���
                Long ifOutPktsPersLong = 0L;                      // ÿ������ܰ�������/s�� ��Long
                
                Long ifOutUcastPktsLong = 0L;                    // ���ڵ���
                Long ifOutNUcastPktsLong = 0L;                   // ���ڷǵ���
                Long ifOutPktsLong = 0L;                         // �����ܰ���
                Long ifOutDiscardsLong = 0L;                     // ���ڶ���
                Long ifOutErrorsLong = 0L;                       // ���ڴ��
                Long ifOutPktsBetweenLong = 0L;                  // �����ܰ���֮��
                Long ifOutDiscardsBetweenLong = 0L;              // ���ڶ�����֮��
                Long ifOutErrorsBetweenLong = 0L;                // ���ڴ����֮��
                Long lastIfOutPktsLong = 0L;                     // �ϴγ����ܰ���
                Long lastIfOutDiscardsLong = 0L;                 // �ϴγ��ڶ���
                Long lastIfOutErrorsLong = 0L;                   // �ϴγ��ڴ��
                Double ifOutDiscardsPercDouble = 0D;             // ���ڶ�����
                Double ifOutErrorsPercDouble = 0D;               // ���ڴ����
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
                
                // �������γ��ڰ���֮��
                ifOutPktsBetweenLong = ifOutPktsLong - lastIfOutPktsLong;
                // �������γ��ڶ�����֮��
                ifOutDiscardsBetweenLong = ifOutDiscardsLong - lastIfOutDiscardsLong;
                // �������γ��ڴ����֮��
                ifOutErrorsBetweenLong = ifOutErrorsLong - lastIfOutErrorsLong;
                if (ifOutPktsBetweenLong < 0) {
                    // ���С��0 ��˵����������
                    ifOutPktsBetweenLong += INT_MAX; 
                }
                if (ifOutDiscardsBetweenLong < 0) {
                    // ���С��0 ��˵����������
                    ifOutDiscardsBetweenLong += INT_MAX;
                }
                if (ifOutErrorsBetweenLong < 0) {
                    // ���С��0 ��˵����������
                    ifOutErrorsBetweenLong += INT_MAX;
                }
                if (ifOutPktsBetweenLong > 0) {
                    // ������ڴ��� 0 ���м��㣬 ������� 0 �򲻼���
                    // ����ÿ��İ������� �ܰ���֮�� ���� ʱ����
                    ifOutPktsPersLong = ifOutPktsBetweenLong / calculateIntervalSecond; 
                    // ��������֮�� ���� �ܰ���֮��
                    ifOutDiscardsPercDouble = (ifOutDiscardsBetweenLong * 1.0D) / ifOutPktsBetweenLong;
                    // �������֮�� ���� �ܰ���֮��
                    ifOutErrorsPercDouble = (ifOutErrorsBetweenLong * 1.0D) / ifOutPktsBetweenLong;
                    // �����ܶ�����
                    ifAllOutDiscardPercDouble += ifOutDiscardsPercDouble;
                    // �����ܴ����
                    ifAllOutErrorPercDouble += ifOutErrorsPercDouble;
                }

                try {
                    ifOutPktsPers = String.valueOf(ifOutPktsPersLong);
                    ifOutDiscardsPerc = String.valueOf(format(ifOutDiscardsPercDouble));
                    ifOutErrorsPerc = String.valueOf(format(ifOutErrorsPercDouble));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // ÿ����ڰ���
                outpacksVector.add(createInterfacecollectdata(date, DESC_IFOUTPKTSPERS, ifIndex, ifOutPktsPers, RESTYPE_DYNAMIC, "��/s", CHNAME_IFOUTPKTSPERS));

                // ���ڶ�����
                discardspercVector.add(createInterfacecollectdata(date, DESC_IFOUTDISCARDSPERC, ifIndex, ifOutDiscardsPerc, RESTYPE_DYNAMIC, "%", CHNAME_IFOUTDISCARDSPERC));
                
                // ���ڴ����
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

        allutilhdxVector.add(createInterfacecollectdata(date, "AllInBandwidthUtilHdx", "AllInBandwidthUtilHdx", ifAllInUtilhdx, RESTYPE_DYNAMIC, "KB/s", "���������"));

        allutilhdxVector.add(createInterfacecollectdata(date, "AllOutBandwidthUtilHdx", "AllOutBandwidthUtilHdx", ifAllOutUtilhdx, RESTYPE_DYNAMIC, "KB/s", "����������"));
        
        allutilhdxVector.add(createInterfacecollectdata(date, "AllBandwidthUtilHdx", "AllBandwidthUtilHdx", ifAllUtilhdx, RESTYPE_DYNAMIC, "KB/s", "�ۺ�����"));

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
//        // �˿ڴ���������
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
//        // ÿ����ڰ���
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
//        // ������
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
//        // �����
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
//        // ÿ����ڰ���
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

