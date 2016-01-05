package com.afunms.polling.snmp.interfaces;

/*
 * @author hukelei@dhcc.com.cn
 *
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtils;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.base.Node;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.loader.HostLoader;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.AllUtilHdx;
import com.afunms.polling.om.InPkts;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.OutPkts;
import com.afunms.polling.om.Task;
import com.afunms.polling.om.UtilHdx;
import com.afunms.polling.om.UtilHdxPerc;
import com.afunms.polling.snmp.SnmpMibConstants;
import com.afunms.polling.task.TaskXml;
import com.afunms.topology.model.HostNode;

public class PackageSnmp extends SnmpMonitor {

    private static Hashtable ifEntity_ifStatus = null;
    static {
        ifEntity_ifStatus = new Hashtable();
        ifEntity_ifStatus.put("1", "up");
        ifEntity_ifStatus.put("2", "down");
        ifEntity_ifStatus.put("3", "testing");
        ifEntity_ifStatus.put("5", "unknow");
        ifEntity_ifStatus.put("7", "unknow");
    };

    private static java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public PackageSnmp() {
    }

    public void collectData(Node node, MonitoredItem item) {

    }

    public void collectData(HostNode node) {

    }

    public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {

        Hashtable returnHash = new Hashtable();
        Vector interfaceVector = new Vector();
        Vector utilhdxVector = new Vector();
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
        Vector allutilhdxpercVector = new Vector();
        Vector utilhdxpercVector = new Vector();
        HostLoader hostLoader = new HostLoader();
        Host host = hostLoader.loadOneByID(nodeGatherIndicators.getNodeid());
        if (host == null) {
            return null;
        }
        if (!host.isManaged()) {
            return null;
        }
        try {
            Interfacecollectdata interfacedata = null;
            UtilHdx utilhdx = new UtilHdx();
            InPkts inpacks = new InPkts();
            OutPkts outpacks = new OutPkts();
            UtilHdxPerc utilhdxperc = new UtilHdxPerc();
            AllUtilHdx allutilhdx = new AllUtilHdx();
            Calendar date = Calendar.getInstance();

            try {
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                com.afunms.polling.base.Node snmpnode = (com.afunms.polling.base.Node) PollingEngine
                        .getInstance().getNodeByIP(host.getIpAddress());
                Date cc = date.getTime();
                String time = sdf.format(cc);
                snmpnode.setLastTime(time);
            } catch (Exception e) {

            }
            // -------------------------------------------------------------------------------------------interface
            // pkts start
            try {
                I_HostLastCollectData lastCollectDataManager = new HostLastCollectDataManager();
                Hashtable hash = ShareData.getPksdata(host.getIpAddress());
                // ȡ����ѯ���ʱ��
                TaskXml taskxml = new TaskXml();
                Task task = taskxml.GetXml("netcollecttask");
                int interval = getInterval(task.getPolltime().floatValue(),
                        task.getPolltimeunit());
                Hashtable hashSpeed = new Hashtable();
                Hashtable pksHash = new Hashtable();
                if (hash == null)
                    hash = new Hashtable();
                String[] oids1 = new String[] { "1.3.6.1.2.1.2.2.1.1",
                        "1.3.6.1.2.1.31.1.1.1.2",// ifInMulticastPkts
                                                    // ���յĶಥ���ݰ���Ŀ
                        "1.3.6.1.2.1.31.1.1.1.3",// ifInBroadcastPkts
                                                    // ���յĹ㲥���ݰ���Ŀ
                        "1.3.6.1.2.1.31.1.1.1.4",// ifOutMulticastPkts
                                                    // ���͵Ķಥ���ݰ���Ŀ
                        "1.3.6.1.2.1.31.1.1.1.5" // ifOutBroadcastPkts
                                                    // ���͵Ĺ㲥���ݰ���Ŀ
                };

                final String[] desc = SnmpMibConstants.NetWorkMibInterfaceDesc3;

                String[][] valueArray1 = null;
                try {
                    valueArray1 = SnmpUtils.getTableData(host.getIpAddress(),
                            host.getCommunity(), oids1, host.getSnmpversion(),
                            3, 1000 * 30);
                } catch (Exception e) {
                    valueArray1 = null;
                    e.printStackTrace();
                    SysLogger.error(host.getIpAddress() + "_H3CSnmp");
                }

                long allinpacks = 0;
                long InMultiPks = 0;
                long InBroadPks = 0;
                long OutMultiPks = 0;
                long OutBroadPks = 0;
                long alloutpacks = 0;
                Vector tempV = new Vector();
                Hashtable tempHash = new Hashtable();
                if (valueArray1 != null) {
                    for (int i = 0; i < valueArray1.length; i++) {

                        allinpacks = 0;
                        InMultiPks = 0;// ��ڵ����
                        InBroadPks = 0;// �ǵ���
                        OutMultiPks = 0;
                        OutBroadPks = 0;
                        alloutpacks = 0;
                        if (valueArray1[i][0] == null)
                            continue;
                        String sIndex = valueArray1[i][0];
                        // if (tempV.contains(sIndex)){
                        if (sIndex != null && sIndex.trim().length() > 0) {
                            for (int j = 0; j < 5; j++) {
                                if (valueArray1[i][j] != null) {
                                    String sValue = valueArray1[i][j];
                                    interfacedata = new Interfacecollectdata();
                                    interfacedata.setThevalue(sValue);
                                    if (j == 1 || j == 2) {
                                        // ���յĶಥ���ݰ���Ŀ,���յĹ㲥���ݰ���Ŀ
                                        if (sValue != null) {
                                            allinpacks = allinpacks
                                                    + Long.parseLong(sValue);

                                            Calendar cal = (Calendar) hash
                                                    .get("collecttime");
                                            long timeInMillis = 0;
                                            if (cal != null)
                                                timeInMillis = cal
                                                        .getTimeInMillis();
                                            long longinterval = (date
                                                    .getTimeInMillis() - timeInMillis) / 1000;

                                            inpacks = new InPkts();
                                            inpacks.setIpaddress(host
                                                    .getIpAddress());
                                            inpacks.setCollecttime(date);
                                            inpacks.setCategory("Interface");
                                            String chnameBand = "";
                                            if (j == 1) {
                                                inpacks
                                                        .setEntity("ifInMulticastPkts");
                                                chnameBand = "�ಥ";
                                            }
                                            if (j == 2) {
                                                inpacks
                                                        .setEntity("ifInBroadcastPkts");
                                                chnameBand = "�㲥";
                                            }
                                            inpacks.setSubentity(sIndex);
                                            inpacks.setRestype("dynamic");
                                            inpacks.setUnit("");
                                            inpacks.setChname(chnameBand);
                                            long currentPacks = Long
                                                    .parseLong(sValue);
                                            long lastPacks = 0;
                                            long l = 0;

                                            // �����ǰ�ɼ�ʱ�����ϴβɼ�ʱ��Ĳ�С�ڲɼ�����������������������ʣ��������������Ϊ0��
                                            if (longinterval < 2 * interval) {
                                                String lastvalue = "";

                                                if (hash.get(desc[j] + ":"
                                                        + sIndex) != null)
                                                    lastvalue = hash.get(
                                                            desc[j] + ":"
                                                                    + sIndex)
                                                            .toString();
                                                // ȡ���ϴλ�õ�Octets
                                                if (lastvalue != null
                                                        && !lastvalue
                                                                .equals(""))
                                                    lastPacks = Long
                                                            .parseLong(lastvalue);
                                            }

                                            if (longinterval != 0) {
                                                if (currentPacks < lastPacks) {
                                                    currentPacks = currentPacks + 4294967296L;
                                                }
                                                // ������-ǰ����
                                                // SysLogger.info(host.getIpAddress()+"==="+sIndex+"�˿�==="+currentPacks+"===="+lastPacks);
                                                long octetsBetween = currentPacks
                                                        - lastPacks;
                                                l = octetsBetween;
                                                if (lastPacks == 0)
                                                    l = 0;
                                            }
                                            inpacks.setThevalue(Long
                                                    .toString(l));
                                            // SysLogger.info(host.getIpAddress()+"
                                            // ��"+inpacks.getSubentity()+"�˿�
                                            // "+"inpacks "+Long.toString(l));
                                            if (cal != null)
                                                inpksVector.addElement(inpacks);
                                        }
                                        // continue;
                                    }
                                    if (j == 3 || j == 4) {
                                        // ���͵Ķಥ���ݰ���Ŀ,���͵Ĺ㲥���ݰ���Ŀ
                                        if (sValue != null) {
                                            alloutpacks = alloutpacks
                                                    + Long.parseLong(sValue);

                                            Calendar cal = (Calendar) hash
                                                    .get("collecttime");
                                            long timeInMillis = 0;
                                            if (cal != null)
                                                timeInMillis = cal
                                                        .getTimeInMillis();
                                            long longinterval = (date
                                                    .getTimeInMillis() - timeInMillis) / 1000;

                                            outpacks = new OutPkts();
                                            outpacks.setIpaddress(host
                                                    .getIpAddress());
                                            outpacks.setCollecttime(date);
                                            outpacks.setCategory("Interface");
                                            String chnameBand = "";

                                            if (j == 3) {
                                                outpacks
                                                        .setEntity("ifOutMulticastPkts");
                                                chnameBand = "�ಥ";
                                            }
                                            if (j == 4) {
                                                outpacks
                                                        .setEntity("ifOutBroadcastPkts");
                                                chnameBand = "�㲥";
                                            }
                                            outpacks.setSubentity(sIndex);
                                            outpacks.setRestype("dynamic");
                                            outpacks.setUnit("");
                                            outpacks.setChname(chnameBand);
                                            long currentPacks = Long
                                                    .parseLong(sValue);
                                            long lastPacks = 0;
                                            long l = 0;

                                            // �����ǰ�ɼ�ʱ�����ϴβɼ�ʱ��Ĳ�С�ڲɼ�����������������������ʣ��������������Ϊ0��
                                            if (longinterval < 2 * interval) {
                                                String lastvalue = "";

                                                if (hash.get(desc[j] + ":"
                                                        + sIndex) != null)
                                                    lastvalue = hash.get(
                                                            desc[j] + ":"
                                                                    + sIndex)
                                                            .toString();
                                                // ȡ���ϴλ�õ�Octets
                                                if (lastvalue != null
                                                        && !lastvalue
                                                                .equals(""))
                                                    lastPacks = Long
                                                            .parseLong(lastvalue);
                                            }

                                            if (longinterval != 0) {
                                                if (currentPacks < lastPacks) {
                                                    currentPacks = currentPacks + 4294967296L;
                                                }
                                                // ������-ǰ����
                                                long octetsBetween = currentPacks
                                                        - lastPacks;
                                                l = octetsBetween;
                                                if (lastPacks == 0)
                                                    l = 0;
                                            }
                                            outpacks.setThevalue(Long
                                                    .toString(l));
                                            // SysLogger.info(host.getIpAddress()+"
                                            // ��"+outpacks.getSubentity()+"�˿�
                                            // "+"Speed "+Long.toString(l));
                                            if (cal != null)
                                                outpksVector
                                                        .addElement(outpacks);

                                        }
                                        // continue;
                                    }
                                    // SysLogger.info(host.getIpAddress()+"==="+desc1[j]+":"+sIndex+"===="+sValue);
                                    pksHash.put(desc[j] + ":" + sIndex,
                                            interfacedata.getThevalue());
                                } // valueArray1[i][j]==null
                            } // end for j
                        } // end for contains

                    }
                }

                String flag = "0";
                // hash=null;
                hashSpeed = null;
                pksHash.put("collecttime", date);
                if (hash != null) {
                    flag = (String) hash.get("flag");
                    if (flag == null) {
                        flag = "0";
                    } else {
                        if (flag.equals("0")) {
                            flag = "1";
                        } else {
                            flag = "0";
                        }
                    }
                }
                pksHash.put("flag", flag);
                ShareData.setPksdata(host.getIpAddress(), pksHash);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // -------------------------------------------------------------------------------------------interface
            // end
        } catch (Exception e) {
            // returnHash=null;
            // e.printStackTrace();
            // return null;
        }

        Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(
                host.getIpAddress());
        if (ipAllData == null)
            ipAllData = new Hashtable();

        if (inpksVector != null && inpksVector.size() > 0)
            ipAllData.put("inpacks", inpksVector);
        if (outpksVector != null && outpksVector.size() > 0)
            ipAllData.put("outpacks", outpksVector);

        ShareData.getSharedata().put(host.getIpAddress(), ipAllData);

        returnHash.put("inpacks", inpksVector);
        returnHash.put("outpacks", outpksVector);
        return returnHash;
    }

    public int getInterval(float d, String t) {
        int interval = 0;
        if (t.equals("d"))
            interval = (int) d * 24 * 60 * 60; // ����
        else if (t.equals("h"))
            interval = (int) d * 60 * 60; // Сʱ
        else if (t.equals("m"))
            interval = (int) d * 60; // ����
        else if (t.equals("s"))
            interval = (int) d; // ��
        return interval;
    }
}
