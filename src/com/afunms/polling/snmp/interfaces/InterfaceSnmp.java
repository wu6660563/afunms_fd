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

    public static final String CHNAME_IFINDEX = "�˿�����";
    
    public static final String CHNAME_IFDESCR = "����";
    
    public static final String CHNAME_IFTYPE = "����";
    
    public static final String CHNAME_IFMTU = "������ݰ�";
    
    public static final String CHNAME_IFSPEED = "�˿��������(bit)";
    
    public static final String CHNAME_IFPHYSADDRESS = "�˿�Mac��ַ";

    public static final String CHNAME_IFADMINSTATUS = "����״̬";
    
    public static final String CHNAME_IFOPERSTATUS = "��ǰ״̬";
    
    public static final String CHNAME_IFLASTCHANGE = "ϵͳsysUpTime����";

    public static final String CHNAME_IFINOCTETS = "���յ��ֽ�";
    
    public static final String CHNAME_IFOUTOCTETS = "������ֽ�";

    public static final String CHNAME_IFINMULTICASTPKTS = "��ڶಥ���ݰ�";
    
    public static final String CHNAME_IFOUTMULTICASTPKTS = "���ڶಥ���ݰ�";

    public static final String CHNAME_IFINBROADCASTPKTS = "��ڹ㲥���ݰ�";
    
    public static final String CHNAME_IFOUTBROADCASTPKTS = "���ڹ㲥���ݰ�";

    public static final String CHNAME_IFINPKTS = "������ݰ�";

    public static final String CHNAME_IFOUTPKTS = "�������ݰ�";

    public static final String CHNAME_IFINPKTSPERS = "ÿ��������ݰ�";

    public static final String CHNAME_IFOUTPKTSPERS = "ÿ��������ݰ�";

    public static final String CHNAME_IFINDISCARDS = "��ڶ�����";
    
    public static final String CHNAME_IFOUTDISCARDS = "���ڶ�����";

    public static final String CHNAME_IFINERRORS = "��ڴ������";
    
    public static final String CHNAME_IFOUTERRORS = "���ڴ������";

    public static final String CHNAME_IFINBANDWIDTHUTILHDX = "�˿��������";

    public static final String CHNAME_IFOUTBANDWIDTHUTILHDX = "�˿ڳ�������";

    public static final String CHNAME_IFINBANDWIDTHUTILHDXPERC = "�˿���ڴ���������";

    public static final String CHNAME_IFOUTBANDWIDTHUTILHDXPERC = "�˿ڳ��ڴ���������";

    public static final String CHNAME_IFINDISCARDSPERC = "��ڶ�����";
    
    public static final String CHNAME_IFOUTDISCARDSPERC = "���ڶ�����";

    public static final String CHNAME_IFINERRORSPERC = "��ڴ����";
    
    public static final String CHNAME_IFOUTERRORSPERC = "���ڴ����";
//    public static final String[] NetWorkMibInterfaceDesc1={"index","ifInOctets","ifInMulticastPkts","ifInBroadcastPkts",
//        "ifInDiscards","ifInErrors","ifOutOctets",
//        "ifOutMulticastPkts","ifOutBroadcastPkts","ifOutDiscards","ifOutErrors",
//        "ifOutQLen","ifSpecific"
//};
//public static final String[] NetWorkMibInterfaceChname1={"�˿�����","���յ��ֽ�","���������ݰ�","�ǵ��������ݰ�",
//        "�����������ݰ�","��վ�������ݰ�","��վ��֪�������ݰ�","������ֽ�",
//        "���������ݰ�","�ǵ��������ݰ�","��վ�����������ݰ�","��վ����ʧ�ܵ����ݰ�",
//        "�����Ϣ�����еĳ���","Mib�Զ˿ڵ�˵��"
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
        String ifAllUtilhdx = "0";                // �˿�������
        String ifAllInUtilhdx = "0";              // ���������
        String ifAllOutUtilhdx = "0";             // ����������
        String ifAllDiscardPerc = "0";            // ���ж˿��ܶ�����
        String ifAllInDiscardPerc = "0";          // ��������ܶ�����
        String ifAllOutDiscardPerc = "0";         // ���г����ܶ�����
        String ifAllErrorPerc = "0";              // ���ж˿��ܴ����
        String ifAllInErrorPerc = "0";            // ��������ܴ����
        String ifAllOutErrorPerc = "0";           // ���г����ܴ����
        Long ifAllUtilhdxLong = 0L;               // �˿�������
        Long ifAllInUtilhdxLong = 0L;             // ���������
        Long ifAllOutUtilhdxLong = 0L;            // ����������
        Long ifAllUtilhdxPercLong = 0L;           // �˿��ܴ���
        Long ifAllInUtilhdxPercLong = 0L;         // ����ܴ���
        Long ifAllOutUtilhdxPercLong = 0L;        // �����ܴ���
        Double ifAllDiscardPercDouble = 0D;            // ���ж˿��ܶ�����
        Double ifAllInDiscardPercDouble = 0D;          // ��������ܶ�����
        Double ifAllOutDiscardPercDouble = 0D;         // ���г����ܶ�����
        Double ifAllErrorPercDouble = 0D;              // ���ж˿��ܴ����
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
        // ��ȡ�˿ھ�ֵ̬
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
        // ��ȡ�˿�״ֵ̬
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
        // ��ȡ��һ�ζ˿�����
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
        // ��ȡ�ڶ��ζ˿�����
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
        // ��ȡ�˿ڶಥ���ݰ�
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
        // ��ȡ�˿ڹ㲥���ݰ�
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
        // ��ȡ�˿ڵ�һ��������ݰ����������ǵ����������Լ������
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
        // ��ȡ�˿ڵڶ���������ݰ����������ǵ����������Լ������
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
        // ��ȡ�˿ڵ�һ�γ������ݰ����������ǵ����������Լ������
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
        // ��ȡ�˿ڵڶ��γ������ݰ����������ǵ����������Լ������
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

                // ����
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

                // ����
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
                
                // ������ݰ�
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
                
                // �˿��������(bit)
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
                
                // �˿�Mac��ַ
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

                // ��ǰ״̬
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
                
                // ϵͳsysUpTime����
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

                // ���յ��ֽ�
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

                // ������ֽ�
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

                // �˿��������
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

                // �˿ڳ�������
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

                // �˿���ڴ���������
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

                // �˿ڳ��ڴ���������
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

                // ��ڶಥ���ݰ�
                Interfacecollectdata interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFINMULTICASTPKTS);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("dynamic");
                interfacedata.setUnit("��");
                interfacedata.setThevalue(ifInMulticastPkts);
                interfacedata.setChname(CHNAME_IFINMULTICASTPKTS);
                interfaceVector.add(interfacedata);

                // ���ڶಥ���ݰ�
                interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFOUTMULTICASTPKTS);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("dynamic");
                interfacedata.setUnit("��");
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

                // ��ڹ㲥���ݰ�
                Interfacecollectdata interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFINBROADCASTPKTS);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("dynamic");
                interfacedata.setUnit("��");
                interfacedata.setThevalue(ifInBroadcastPkts);
                interfacedata.setChname(CHNAME_IFINBROADCASTPKTS);
                interfaceVector.add(interfacedata);

                // ���ڹ㲥���ݰ�
                interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFOUTBROADCASTPKTS);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("dynamic");
                interfacedata.setUnit("��");
                interfacedata.setThevalue(ifOutBroadcastPkts);
                interfacedata.setChname(CHNAME_IFOUTBROADCASTPKTS);
                interfaceVector.add(interfacedata);
                
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
                Interfacecollectdata interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFINPKTS);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("dynamic");
                interfacedata.setUnit("��");
                interfacedata.setThevalue(ifInPkts);
                interfacedata.setChname(CHNAME_IFINPKTS);
                ifInPktsHashtable.put(ifIndex, interfacedata);

                // ��ڶ�����
                interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFINDISCARDS);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("dynamic");
                interfacedata.setUnit("��");
                interfacedata.setThevalue(ifInDiscards);
                interfacedata.setChname(CHNAME_IFINDISCARDS);
                ifInDiscardsHashtable.put(ifIndex, interfacedata);
                
                // ��ڴ����
                interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFINERRORS);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("dynamic");
                interfacedata.setUnit("��");
                interfacedata.setThevalue(ifInErrors);
                interfacedata.setChname(CHNAME_IFINERRORS);
                ifInErrorsHashtable.put(ifIndex, interfacedata);

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
                    ifInPktsPers = df.format(ifInPktsPersLong);
                    ifInDiscardsPerc = df.format(ifInDiscardsPercDouble);
                    ifInErrorsPerc = df.format(ifInErrorsPercDouble);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // ÿ����ڰ���
                InPkts inPkts = new InPkts();
                inPkts.setIpaddress(ip);
                inPkts.setCollecttime(date);
                inPkts.setCategory("Interface");
                inPkts.setEntity(DESC_IFINPKTSPERS);
                inPkts.setSubentity(ifIndex);
                inPkts.setRestype("dynamic");
                inPkts.setUnit("��/s");
                inPkts.setThevalue(ifInPktsPers);
                inPkts.setChname(CHNAME_IFINPKTSPERS);
                inpacksVector.add(inPkts);

                // ��ڶ�����
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
                
                // ��ڴ����
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
                Interfacecollectdata interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFOUTPKTS);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("dynamic");
                interfacedata.setUnit("��");
                interfacedata.setThevalue(ifOutPkts);
                interfacedata.setChname(CHNAME_IFOUTPKTS);
                ifOutPktsHashtable.put(ifIndex, interfacedata);

                // ���ڶ�����
                interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFOUTDISCARDS);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("dynamic");
                interfacedata.setUnit("��");
                interfacedata.setThevalue(ifOutDiscards);
                interfacedata.setChname(CHNAME_IFOUTDISCARDS);
                ifOutDiscardsHashtable.put(ifIndex, interfacedata);
                
                // ���ڴ����
                interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ip);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity(DESC_IFOUTERRORS);
                interfacedata.setSubentity(ifIndex);
                interfacedata.setRestype("dynamic");
                interfacedata.setUnit("��");
                interfacedata.setThevalue(ifOutErrors);
                interfacedata.setChname(CHNAME_IFOUTERRORS);
                ifOutErrorsHashtable.put(ifIndex, interfacedata);

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
                    ifOutDiscardsPerc = df.format(ifOutDiscardsPercDouble);
                    ifOutErrorsPerc = df.format(ifOutErrorsPercDouble);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // ÿ����ڰ���
                OutPkts outPkts = new OutPkts();
                outPkts.setIpaddress(ip);
                outPkts.setCollecttime(date);
                outPkts.setCategory("Interface");
                outPkts.setEntity(DESC_IFOUTPKTSPERS);
                outPkts.setSubentity(ifIndex);
                outPkts.setRestype("dynamic");
                outPkts.setUnit("��/s");
                outPkts.setThevalue(ifOutPktsPers);
                outPkts.setChname(CHNAME_IFOUTPKTSPERS);
                outpacksVector.add(outPkts);

                // ���ڶ�����
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
                
                // ���ڴ����
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
        allInUtilhdx.setChname("���������");
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
        allOutUtilhdx.setChname("����������");    
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
        allUtilhdx.setChname("�ۺ�����");
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
