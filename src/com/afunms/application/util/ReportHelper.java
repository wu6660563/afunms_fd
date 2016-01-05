package com.afunms.application.util;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;


import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.DBTypeDao;
import com.afunms.application.dao.OraclePartsDao;
import com.afunms.application.model.DBTypeVo;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.OracleEntity;
import com.afunms.capreport.model.ReportValue;
import com.afunms.capreport.model.StatisNumer;
import com.afunms.common.util.CEIString;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.event.dao.EventListDao;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostCollectData;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;


public class ReportHelper {

    private SysLogger logger = SysLogger.getLogger(ReportHelper.class);

    public HashMap getAllValue(String ids, String startTime, String toTime) {
        HashMap allValueMap = new HashMap();
        List<StatisNumer> gridList = new ArrayList<StatisNumer>();
        List<ReportValue> valueList = new ArrayList<ReportValue>();
        String[] idValue = this.getIdValue(ids);

        String runmodel = PollingEngine.getCollectwebflag();// �ɼ������ģʽ
        I_HostCollectData hostmanager = new HostCollectDataManager();
        I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();
        if (startTime == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            startTime = sdf.format(new Date()) + " 00:00:00";
        } else {
            startTime = startTime + " 00:00:00";
        }

        if (toTime == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            toTime = sdf.format(new Date()) + " 23:59:59";
        } else {
            toTime = toTime + " 23:59:59";
        }
        List<List> pingList = new ArrayList<List>();
        List<List> list = new ArrayList<List>();
        List<List> memList = new ArrayList<List>();
        List<List> utilInList = new ArrayList<List>();
        List<List> utilOutList = new ArrayList<List>();
        List<List> diskList = new ArrayList<List>();

        List<String> pingipList = new ArrayList<String>();
        List<String> ipList = new ArrayList<String>();
        List<String> memipList = new ArrayList<String>();
        List<String> portipList = new ArrayList<String>();
        List<String> diskipList = new ArrayList<String>();
        if (idValue != null && idValue.length > 0) {

            for (int i = 0; i < idValue.length; i++) {
                if (idValue[i].indexOf("ping") >= 0) {

                    String pingvalue = "0";
                    String ip = idValue[i].replace("ping", "");

                    try {
                        Hashtable pinghash = hostmanager.getCategory(ip,
                                        "Ping", "ConnectUtilization",
                                        startTime, toTime);
                        if ("0".equals(runmodel)) {

                            Vector pingData = (Vector) ShareData.getPingdata()
                                            .get(ip);
                            if (pingData != null && pingData.size() > 0) {
                                Pingcollectdata ping = (Pingcollectdata) pingData
                                                .get(0);

                                pingvalue = ping.getThevalue();

                            }
                        } else {

                            Hashtable curPinghash = hostmanager
                                            .getCurByCategory(ip, "Ping",
                                                            "ConnectUtilization");
                            pingvalue = (String) curPinghash.get("pingCur");

                        }
                        if (pinghash != null && pinghash.size() > 0) {
                            List pingDataList = (List) pinghash.get("list");
                            String pingAvg = (String) pinghash
                                            .get("avgpingcon");

                            String pingMin = (String) pinghash.get("pingmax");
                            StatisNumer voNumer = new StatisNumer();
                            voNumer.setIp(ip);
                            voNumer.setType("gridPing");
                            voNumer.setCurrent(pingvalue);
                            voNumer.setMininum(pingMin);
                            voNumer.setAverage(pingAvg);
                            gridList.add(voNumer);
                            if (pingDataList != null && pingDataList.size() > 0)
                                pingList.add(pingDataList);
                            pingipList.add(ip);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (idValue[i].indexOf("cpu") >= 0) {

                    String ip = idValue[i].replace("cpu", "");

                    String cpuvalue = "0";
                    if ("0".equals(runmodel)) {
                        Hashtable ipAllData = (Hashtable) ShareData
                                        .getSharedata().get(ip);

                        if (ipAllData != null) {

                            Vector cpuV = (Vector) ipAllData.get("cpu");
                            if (cpuV != null && cpuV.size() > 0) {

                                CPUcollectdata cpu = (CPUcollectdata) cpuV
                                                .get(0);
                                cpuvalue = cpu.getThevalue();

                            }
                        }
                    } else {
                        Hashtable curCpuhash = null;
                        try {
                            curCpuhash = hostmanager.getCurByCategory(ip,
                                            "CPU", "Utilization");
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                        cpuvalue = (String) curCpuhash.get("pingCur");

                    }
                    try {
                        Hashtable cpuhash = hostmanager.getCategory(ip, "CPU",
                                        "Utilization", startTime, toTime);

                        List cpuList = (List) cpuhash.get("list");
                        String cpumax = "0";
                        String cpuavg = "0";
                        if (cpuhash.get("max") != null) {
                            cpumax = (String) cpuhash.get("max");
                        }
                        if (cpuhash.get("avgcpucon") != null) {
                            cpuavg = (String) cpuhash.get("avgcpucon");
                        }
                        StatisNumer voNumer = new StatisNumer();
                        voNumer.setIp(ip);
                        voNumer.setType("gridCpu");
                        voNumer.setCurrent(cpuvalue);
                        voNumer.setMaximum(cpumax);
                        voNumer.setAverage(cpuavg);
                        gridList.add(voNumer);
                        if (cpuList != null && cpuList.size() > 0)
                            list.add(cpuList);
                        ipList.add(ip);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (idValue[i].indexOf("mem") >= 0) {

                    String ip = idValue[i].replace("mem", "");

                    try {
                        Hashtable memhash = hostmanager.getNetMemeory(ip,
                                        "Memory", startTime, toTime);
                        if (memhash == null || memhash.size() == 0)
                            continue;
                        List memDataList = (List) memhash.get("absList");
                        String memMax = (String) memhash.get("max");
                        String memAvg = (String) memhash.get("avg");
                        String memCur = (String) memhash.get("cur");
                        StatisNumer voNumer = new StatisNumer();
                        voNumer.setIp(ip);
                        voNumer.setType("gridMem");
                        voNumer.setCurrent(memCur);
                        voNumer.setMaximum(memMax);
                        voNumer.setAverage(memAvg);
                        gridList.add(voNumer);
                        if (memDataList != null && memDataList.size() > 0)
                            memList.add(memDataList);
                        memipList.add(ip);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (idValue[i].indexOf("port") >= 0) {
                    String realValue = idValue[i];
                    String[] idRelValue = new String[realValue.split("\\*").length];
                    idRelValue = realValue.split("\\*");
                    if (idRelValue.length < 4) {
                        continue;
                    }
                    String ip = idRelValue[1].trim();
                    String sindex = idRelValue[2].trim();
                    String sname = "";
                    try {
                        sname = new String(idRelValue[3].trim().getBytes(
                                        "ISO8859-1"), "gb2312");
                    } catch (UnsupportedEncodingException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    try {
                        Hashtable utilhash = hostmanager.getUtilhdx(ip, sindex,
                                        startTime, toTime);

                        List utilInDataList = (List) utilhash.get("inList");
                        List utilOutDataList = (List) utilhash.get("outList");
                        String curin = "0";
                        String curout = "0";
                        String maxin = "0";
                        String maxout = "0";
                        String avgin = "0";
                        String avgout = "0";
                        if (utilhash.get("curin") != null)
                            curin = (String) utilhash.get("curin");
                        if (utilhash.get("curout") != null)
                            curout = (String) utilhash.get("curout");
                        if (utilhash.get("maxin") != null)
                            maxin = (String) utilhash.get("maxin");
                        if (utilhash.get("maxout") != null)
                            maxout = (String) utilhash.get("maxout");
                        if (utilhash.get("avgin") != null)
                            avgin = (String) utilhash.get("avgin");
                        if (utilhash.get("avgout") != null)
                            avgout = (String) utilhash.get("avgout");
                        // ����
                        StatisNumer voNumer = new StatisNumer();
                        voNumer.setIp(ip);
                        voNumer.setType("gridPortIn");
                        voNumer.setName(sname);
                        voNumer.setCurrent(curin);
                        voNumer.setMaximum(maxin);
                        voNumer.setAverage(avgin);
                        gridList.add(voNumer);
                        // ����
                        StatisNumer voNumerout = new StatisNumer();
                        voNumerout.setIp(ip);
                        voNumerout.setType("gridPortOut");
                        voNumerout.setName(sname);
                        voNumerout.setCurrent(curin);
                        voNumerout.setMaximum(maxin);
                        voNumerout.setAverage(avgin);
                        gridList.add(voNumerout);
                        if (utilInDataList != null && utilInDataList.size() > 0)
                            utilInList.add(utilInDataList);
                        if (utilOutDataList != null
                                        && utilOutDataList.size() > 0)
                            utilOutList.add(utilOutDataList);
                        portipList.add(ip);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (idValue[i].indexOf("disk") >= 0) {// ����

                    String ip = idValue[i].replace("disk", "");

                    Hashtable hostdiskhash = new Hashtable();
                    // Host host =
                    // (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(id));

                    if ("0".equals(runmodel)) {
                        try {
                            hostdiskhash = hostlastmanager.getDisk_share(ip,
                                            "Disk", "", "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            hostdiskhash = hostlastmanager.getDisk(ip, "Disk",
                                            "", "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Hashtable diskHash = hostmanager.getDiskHistroy(ip,
                                        "Disk", startTime, toTime);
                        String countStr = (String) diskHash.get("count");
                        double[] disk_data1 = new double[hostdiskhash.size()];

                        int count = Integer.parseInt(countStr);
                        List list2 = new ArrayList();
                        String diskType = "";
                        for (int j = 1; j <= count; j++) {
                            List diskList1 = (List) diskHash.get("list" + j);
                            if (diskList1 == null || diskList1.size() == 0)
                                break;
                            String cur = "0";
                            String avg = "0";
                            String max = "0";
                            if (hostdiskhash.get(new Integer(j - 1)) != null) {
                                Hashtable dhash = (Hashtable) (hostdiskhash
                                                .get(new Integer(j - 1)));

                                cur = (String) dhash.get("Utilization");

                            }

                            if (diskHash.get("avg" + j) != null)
                                avg = (String) diskHash.get("avg" + j);
                            if (diskHash.get("max" + j) != null)
                                max = (String) diskHash.get("max" + j);

                            Vector vector = (Vector) diskList1.get(0);
                            diskType = ip + "(" + vector.get(1) + ")";
                            // ����
                            StatisNumer voNumerout = new StatisNumer();
                            voNumerout.setIp(ip);
                            voNumerout.setType("gridDisk");
                            voNumerout.setName((String) vector.get(1));
                            voNumerout.setCurrent(cur);
                            voNumerout.setMaximum(max);
                            voNumerout.setAverage(avg);
                            gridList.add(voNumerout);
                            if (diskList1 != null && diskList1.size() > 0)
                                list2.add(diskList1);
                            diskipList.add(diskType);
                        }

                        diskList.add(list2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
            // ��ͨ��
            ReportValue pingValue = new ReportValue();
            pingValue.setIpList(pingipList);
            pingValue.setListValue(pingList);

            // CPU
            ReportValue cpuValue = new ReportValue();

            cpuValue.setIpList(ipList);
            cpuValue.setListValue(list);
            // �ڴ�
            ReportValue memValue = new ReportValue();
            memValue.setIpList(memipList);
            memValue.setListValue(memList);
            // �˿�
            ReportValue portValue = new ReportValue();
            portValue.setIpList(portipList);
            portValue.setListValue(utilInList);// ���
            portValue.setListTemp(utilOutList);// ����

            // ����
            ReportValue diskValue = new ReportValue();
            diskValue.setIpList(diskipList);
            diskValue.setListValue(diskList);

            allValueMap.put("ping", pingValue);
            allValueMap.put("cpu", cpuValue);
            allValueMap.put("mem", memValue);
            allValueMap.put("port", portValue);
            allValueMap.put("disk", diskValue);

            allValueMap.put("gridVlue", gridList);
        }
        return allValueMap;
    }

    public HashMap getDbValue(String ids, String startTime, String toTime) {
        HashMap allValueMap = new HashMap();
        List<StatisNumer> gridList = new ArrayList<StatisNumer>();
        List<ReportValue> valueList = new ArrayList<ReportValue>();
        String[] idValue = this.getIdValue(ids);

        String runmodel = PollingEngine.getCollectwebflag();// �ɼ������ģʽ
        I_HostCollectData hostmanager = new HostCollectDataManager();
        I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();
        if (startTime == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            startTime = sdf.format(new Date()) + " 00:00:00";
        } else {
            startTime = startTime + " 00:00:00";
        }

        if (toTime == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            toTime = sdf.format(new Date()) + " 23:59:59";
        } else {
            toTime = toTime + " 23:59:59";
        }
        Hashtable pinghash = new Hashtable();
        Hashtable curhash = new Hashtable();
        List<List> pingList = new ArrayList<List>();
        List<String> pingipList = new ArrayList<String>();
        List<List> tableList = new ArrayList<List>();
        List<String> tableipList = new ArrayList<String>();
        List<StatisNumer> valList = new ArrayList<StatisNumer>();
        boolean valFlag = true;
        boolean tableFlag = true;
        String tabledata = "";
        StringBuffer val = new StringBuffer();
        StringBuffer tableHtml = new StringBuffer();
        if (idValue != null && idValue.length > 0) {

            for (int i = 0; i < idValue.length; i++) {

                String realValue = idValue[i];
                String[] idRelValue = new String[realValue.split("\\*").length];
                idRelValue = realValue.split("\\*");
                if (idRelValue.length < 4)
                    continue;
                String itemId = idRelValue[0].trim();
                String typeId = idRelValue[1].trim();
                String id = idRelValue[2].trim();
                String ip = idRelValue[3].trim();
                DBTypeVo typevo = null;
                if (itemId.equals("ping")) {
                    DBTypeDao typedao = new DBTypeDao();
                    try {
                        typevo = (DBTypeVo) typedao.findByID(typeId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        typedao.close();
                    }
                    try {
                        if (typevo.getDbtype().equalsIgnoreCase("oracle")) {
                            String sid = "";
                            OraclePartsDao oracledao = new OraclePartsDao();
                            List sidlist = new ArrayList();
                            try {
                                sidlist = oracledao.findOracleParts(Integer
                                                .parseInt(id));
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                oracledao.close();
                            }
                            if (sidlist != null) {
                                for (int j = 0; j < sidlist.size(); j++) {
                                    OracleEntity ora = (OracleEntity) sidlist
                                                    .get(j);
                                    sid = ora.getId() + "";
                                    break;
                                }
                            }

                            pinghash = hostmanager.getCategory(ip + ":" + sid,
                                            "ORAPing", "ConnectUtilization",
                                            startTime, toTime);
                            curhash = hostmanager.getCurByCategory(ip + ":"
                                            + sid, "ORAPing",
                                            "ConnectUtilization");
                        } else if (typevo.getDbtype().equalsIgnoreCase(
                                        "sqlserver")) {
                            pinghash = hostmanager.getCategory(ip, "SQLPing",
                                            "ConnectUtilization", startTime,
                                            toTime);
                            curhash = hostmanager.getCurByCategory(ip,
                                            "SQLPing", "ConnectUtilization");
                        } else if (typevo.getDbtype().equalsIgnoreCase("db2")) {
                            pinghash = hostmanager.getCategory(ip, "DB2Ping",
                                            "ConnectUtilization", startTime,
                                            toTime);
                            curhash = hostmanager.getCurByCategory(ip,
                                            "DB2Ping", "ConnectUtilization");
                        } else if (typevo.getDbtype()
                                        .equalsIgnoreCase("sybase")) {
                            pinghash = hostmanager.getCategory(ip, "SYSPing",
                                            "ConnectUtilization", startTime,
                                            toTime);
                            curhash = hostmanager.getCurByCategory(ip,
                                            "SYSPing", "ConnectUtilization");
                        } else if (typevo.getDbtype().equalsIgnoreCase(
                                        "informix")) {
                            pinghash = hostmanager.getCategory(ip,
                                            "INFORMIXPing",
                                            "ConnectUtilization", startTime,
                                            toTime);
                            curhash = hostmanager.getCurByCategory(ip,
                                            "INFORMIXPing",
                                            "ConnectUtilization");
                        } else if (typevo.getDbtype().equalsIgnoreCase("mysql")) {// HONGLI
                            pinghash = hostmanager.getCategory(ip, "MYPing",
                                            "ConnectUtilization", startTime,
                                            toTime);
                            curhash = hostmanager.getCurByCategory(ip,
                                            "MYPing", "ConnectUtilization");
                        }
                        if (pinghash != null && pinghash.size() > 0) {
                            List pingDataList = (List) pinghash.get("list");
                            String pingCur = (String) curhash.get("pingCur");
                            String pingAvg = (String) pinghash
                                            .get("avgpingcon");

                            String pingMin = (String) pinghash.get("pingmax");
                            StatisNumer voNumer = new StatisNumer();
                            voNumer.setIp(ip);
                            voNumer.setType("gridPing");
                            voNumer.setCurrent(pingCur);
                            voNumer.setMininum(pingMin);
                            voNumer.setAverage(pingAvg);
                            gridList.add(voNumer);
                            if (pingDataList != null && pingDataList.size() > 0)
                                pingList.add(pingDataList);
                        }
                        pingipList.add(ip);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (itemId.equals("val")) {
                    getVaList(valList, ip, id);

                } else if (itemId.equals("tablespace")) {

                    try {
                        Hashtable table = hostmanager.getOraSpaceHistroy(ip,
                                        "tablespace", startTime, toTime);
                        String countStr = (String) table.get("count");

                        int count = Integer.parseInt(countStr);
                        List list2 = new ArrayList();
                        String diskType = "";
                        for (int j = 1; j <= count; j++) {
                            List tableList1 = (List) table.get("list" + j);
                            if (tableList1 == null || tableList1.size() == 0)
                                continue;
                            String cur = "0";
                            String avg = "0";
                            String max = "0";

                            if (table.get("avg" + j) != null)
                                cur = (String) table.get("avg" + j);
                            if (table.get("avg" + j) != null)
                                avg = (String) table.get("avg" + j);
                            if (table.get("max" + j) != null)
                                max = (String) table.get("max" + j);

                            Vector vector = (Vector) tableList1.get(0);
                            diskType = ip + "(" + vector.get(1) + ")";

                            StatisNumer voNumer = new StatisNumer();
                            voNumer.setIp(ip);
                            voNumer.setType("gridTableSpace");
                            voNumer.setName((String) vector.get(1));// ��ռ�����
                            voNumer.setCurrent(cur);
                            voNumer.setMaximum(max);
                            voNumer.setAverage(avg);
                            gridList.add(voNumer);
                            tableipList.add(diskType);
                            if (tableList1 != null && tableList1.size() > 0)
                                list2.add(tableList1);
                        }
                        if (list2 != null && list2.size() > 0)
                            tableList.add(list2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
            // ��ͨ��
            ReportValue pingValue = new ReportValue();
            pingValue.setIpList(pingipList);
            pingValue.setListValue(pingList);

            // ��ռ�
            ReportValue tableValue = new ReportValue();
            tableValue.setIpList(tableipList);
            tableValue.setListValue(tableList);
            allValueMap.put("ping", pingValue);
            allValueMap.put("tablespace", tableValue);// oracle��ռ�
            allValueMap.put("val", valList);// mysql ������Ϣ
            allValueMap.put("gridVlue", gridList);
        }

        return allValueMap;
    }

    public HashMap getMidwareValue(String ids, String startTime, String toTime) {
        HashMap allValueMap = new HashMap();
        List<StatisNumer> gridList = new ArrayList<StatisNumer>();
        List<ReportValue> valueList = new ArrayList<ReportValue>();
        String[] idValue = this.getIdValue(ids);

        String runmodel = PollingEngine.getCollectwebflag();// �ɼ������ģʽ
        I_HostCollectData hostmanager = new HostCollectDataManager();
        I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();
        if (startTime == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            startTime = sdf.format(new Date()) + " 00:00:00";
        } else {
            startTime = startTime + " 00:00:00";
        }

        if (toTime == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            toTime = sdf.format(new Date()) + " 23:59:59";
        } else {
            toTime = toTime + " 23:59:59";
        }

        if (ids != null && !ids.equals("null") && !ids.equals("")) {
            idValue = new String[ids.split(",").length];
            idValue = ids.split(",");
        }

        Hashtable pinghash = new Hashtable();
        Hashtable jvmhash = new Hashtable();
        Hashtable curhash = new Hashtable();
        List<List> pingList = new ArrayList<List>();
        List<List> jvmList = new ArrayList<List>();
        List<String> pingipList = new ArrayList<String>();
        List<String> jvmipList = new ArrayList<String>();

        if (idValue != null && idValue.length > 0) {

            for (int i = 0; i < idValue.length; i++) {

                String realValue = idValue[i];
                String[] idRelValue = new String[realValue.split("\\*").length];
                idRelValue = realValue.split("\\*");
                if (idRelValue.length < 4)
                    continue;
                String type = idRelValue[0].trim();
                String item = idRelValue[1].trim();
                String ip = idRelValue[3].trim();
                try {
                    if (type.equalsIgnoreCase("tomcat")) {
                        if (item.equals("ping")) {
                            pinghash = getCategory(ip, "TomcatPing",
                                            "ConnectUtilization", startTime,
                                            toTime, "");
                            curhash = hostmanager.getCurByCategory(ip,
                                            "TomcatPing", "ConnectUtilization");

                            if (pinghash != null && pinghash.size() > 0) {
                                List pingDataList = (List) pinghash.get("list");
                                String pingCur = (String) curhash
                                                .get("pingCur");
                                String pingAvg = (String) pinghash
                                                .get("avgpingcon");

                                String pingMin = (String) pinghash
                                                .get("pingmax");
                                StatisNumer voNumer = new StatisNumer();
                                voNumer.setIp(ip);
                                voNumer.setType("gridPing");
                                voNumer.setName("Tomcat");
                                voNumer.setCurrent(pingCur);
                                voNumer.setMininum(pingMin);
                                voNumer.setAverage(pingAvg);
                                gridList.add(voNumer);

                                if (pingDataList != null
                                                && pingDataList.size() > 0)
                                    pingList.add(pingDataList);
                            }
                            pingipList.add(ip);
                        } else if (item.equals("jvm")) {
                            jvmhash = getCategory(ip, "tomcat_jvm",
                                            "jvm_utilization", startTime,
                                            toTime, "");
                            Hashtable curJvm = hostmanager.getCurByCategory(ip,
                                            "tomcat_jvm", "jvm_utilization");
                            if (jvmhash != null && jvmhash.size() > 0) {
                                List jvmDataList = (List) jvmhash.get("list");
                                String jvmCur = (String) curJvm.get("pingCur");// ��ǰ����������
                                String jvmAvg = (String) jvmhash
                                                .get("avg_tomcat_jvm");

                                String jvmMin = (String) jvmhash.get("max");
                                StatisNumer voNumer = new StatisNumer();
                                voNumer.setIp(ip);
                                voNumer.setType("tomcat_jvm");
                                voNumer.setName("Tomcat");
                                voNumer.setCurrent(jvmCur);
                                voNumer.setMaximum(jvmMin);
                                voNumer.setAverage(jvmAvg);
                                gridList.add(voNumer);
                                if (jvmDataList != null
                                                && jvmDataList.size() > 0)
                                    jvmList.add(jvmDataList);

                            }
                            jvmipList.add(ip);
                        }

                    } else if (type.equalsIgnoreCase("iis")) {
                        if (item.equals("ping")) {
                            pinghash = getCategory(ip, "IISPing",
                                            "ConnectUtilization", startTime,
                                            toTime, "");
                            curhash = hostmanager.getCurByCategory(ip,
                                            "IISPing", "ConnectUtilization");

                            if (pinghash != null && pinghash.size() > 0) {
                                List pingDataList = (List) pinghash.get("list");
                                String pingCur = (String) curhash
                                                .get("pingCur");
                                String pingAvg = (String) pinghash
                                                .get("avgpingcon");

                                String pingMin = (String) pinghash
                                                .get("pingmax");
                                StatisNumer voNumer = new StatisNumer();
                                voNumer.setIp(ip);
                                voNumer.setType("gridPing");
                                voNumer.setName("IIS");
                                voNumer.setCurrent(pingCur);
                                voNumer.setMininum(pingMin);
                                voNumer.setAverage(pingAvg);
                                gridList.add(voNumer);
                                if (pingDataList != null
                                                && pingDataList.size() > 0)
                                    pingList.add(pingDataList);
                            }
                            pingipList.add(ip);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            // ��ͨ��
            ReportValue pingValue = new ReportValue();
            pingValue.setIpList(pingipList);
            pingValue.setListValue(pingList);
            // jvm
            ReportValue jvmValue = new ReportValue();
            jvmValue.setIpList(jvmipList);
            jvmValue.setListValue(jvmList);

            allValueMap.put("ping", pingValue);
            allValueMap.put("jvm", jvmValue);// Tomcat ����������
            allValueMap.put("gridVlue", gridList);
        }

        return allValueMap;
    }

    public Hashtable getCategory(String ip, String category, String subentity,
                    String starttime, String endtime, String time)
                    throws Exception {
        Hashtable hash = new Hashtable();

        DBManager dbmanager = new DBManager();
        ResultSet rs = null;
        try {
            if (!starttime.equals("") && !endtime.equals("")) {

                String allipstr = SysUtil.doip(ip);
                String sql = "";
                StringBuffer sb = new StringBuffer();
                if (category.equals("TomcatPing")) {
                    sb.append(" select h.thevalue,DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as collecttime,h.unit from tomcatping"
                                    + time + allipstr + " h where ");
                }
                if (category.equals("tomcat_jvm")) {
                    sb.append(" select h.thevalue,DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as collecttime,h.unit from tomcat_jvm"
                                    + allipstr + " h where ");
                }
                if (category.equals("IISPing")) {
                    sb.append(" select h.thevalue,DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as collecttime,h.unit from iisping"
                                    + allipstr + " h where ");
                }
                sb.append(" h.category='");
                sb.append(category);
                sb.append("' and h.subentity='");
                sb.append(subentity);
                sb.append("' and h.collecttime >= '");
                sb.append(starttime);
                sb.append("' and h.collecttime <= '");
                sb.append(endtime);
                sb.append("' order by h.collecttime");
                sql = sb.toString();
                rs = dbmanager.executeQuery(sql);
                List list1 = new ArrayList();
                String unit = "";
                double tempfloat = 0;
                double pingcon = 0;
                double tomcat_jvm_con = 0;
                int downnum = 0;
                int i = 0;
                while (rs.next()) {
                    i = i + 1;
                    Vector v = new Vector();
                    String thevalue = rs.getString("thevalue");
                    String collecttime = rs.getString("collecttime");
                    v.add(0, emitStr(thevalue));
                    v.add(1, collecttime);
                    v.add(2, rs.getString("unit"));
                    if ((category.equals("TomcatPing") || category
                                    .equals("IISPing"))
                                    && subentity.equalsIgnoreCase("ConnectUtilization")) {
                        pingcon = pingcon + getfloat(thevalue);
                        if (thevalue.equals("0")) {
                            downnum = downnum + 1;
                        }
                    }

                    if (subentity.equalsIgnoreCase("ConnectUtilization")) {
                        if (i == 1)
                            tempfloat = getfloat(thevalue);
                        if (tempfloat > getfloat(thevalue))
                            tempfloat = getfloat(thevalue);
                    } else if (category.equalsIgnoreCase("tomcat_jvm")) {
                        tomcat_jvm_con = tomcat_jvm_con + getfloat(thevalue);
                        if (tempfloat < getfloat(thevalue))
                            tempfloat = getfloat(thevalue);
                    } else {
                        if (tempfloat < getfloat(thevalue))
                            tempfloat = getfloat(thevalue);
                    }
                    list1.add(v);
                }
                rs.close();
                // stmt.close();

                Integer size = new Integer(0);
                hash.put("list", list1);
                if (list1.size() != 0) {
                    size = new Integer(list1.size());
                    if (list1.get(0) != null) {
                        Vector tempV = (Vector) list1.get(0);
                        unit = (String) tempV.get(2);
                    }
                }
                if ((category.equals("TomcatPing") || category
                                .equals("IISPing"))
                                && subentity.equalsIgnoreCase("ConnectUtilization")) {
                    if (list1 != null && list1.size() > 0) {
                        hash.put("avgpingcon",
                                        CEIString.round(pingcon / list1.size(),
                                                        2) + unit);
                        hash.put("pingmax", tempfloat + "");
                        hash.put("downnum", downnum + "");
                    } else {
                        hash.put("avgpingcon", "0.0");
                        hash.put("pingmax", "0.0");
                        hash.put("downnum", "0");
                    }
                }
                if (category.equals("tomcat_jvm")) {
                    if (list1 != null && list1.size() > 0) {
                        hash.put("avg_tomcat_jvm",
                                        CEIString.round(tomcat_jvm_con
                                                        / list1.size(), 2)
                                                        + unit);
                    } else {
                        hash.put("avg_tomcat_jvm", "0.0%");
                    }
                }
                hash.put("size", size);
                hash.put("max", CEIString.round(tempfloat, 2) + unit);
                hash.put("unit", unit);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null)
                rs.close();
            dbmanager.close();
        }

        return hash;
    }

    private String emitStr(String num) {
        if (num != null) {
            if (num.indexOf(".") >= 0) {
                if (num.substring(num.indexOf(".") + 1).length() > 7) {
                    String tempStr = num.substring(num.indexOf(".") + 1);
                    num = num.substring(0, num.indexOf(".") + 1)
                                    + tempStr.substring(0, 7);
                }
            }
        }
        return num;
    }

    private double getfloat(String num) {
        double snum = 0.0;
        if (num != null) {
            if (num.indexOf(".") >= 0) {
                if (num.substring(num.indexOf(".") + 1).length() > 7) {
                    String tempStr = num.substring(num.indexOf(".") + 1);
                    num = num.substring(0, num.indexOf(".") + 1)
                                    + tempStr.substring(0, 7);
                }
            }
            int inum = (int) (Float.parseFloat(num) * 100);
            snum = new Double(inum / 100.0).doubleValue();
        }
        return snum;
    }

    private List<StatisNumer> getVaList(List<StatisNumer> list, String ip,
                    String id) {

        DBVo vo = new DBVo();

        int doneFlag = 0;
        Vector val = new Vector();

        try {
            DBDao dao = new DBDao();
            try {
                vo = (DBVo) dao.findByID(id);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dao.close();
            }

            IpTranslation tranfer = new IpTranslation();
            String hex = tranfer.formIpToHex(vo.getIpAddress());
            String serverip = hex + ":" + vo.getId();
            DBDao dbDao = null;
            Hashtable ipData = null;
            try {
                dbDao = new DBDao();
                ipData = dbDao.getMysqlDataByServerip(serverip);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (dbDao != null) {
                    dbDao.close();
                }
            }
            if (ipData != null && ipData.size() > 0) {
                String dbnames = vo.getDbName();
                String[] dbs = dbnames.split(",");
                for (int k = 0; k < dbs.length; k++) {
                    // �ж��Ƿ��Ѿ���ȡ�˵�ǰ��������Ϣ
                    // if(doneFlag == 1)break;
                    String dbStr = dbs[k];
                    if (ipData.containsKey(dbStr)) {
                        Hashtable returnValue = new Hashtable();
                        returnValue = (Hashtable) ipData.get(dbStr);
                        if (returnValue != null && returnValue.size() > 0) {
                            if (doneFlag == 0) {

                                if (returnValue.containsKey("Val")) {
                                    val = (Vector) returnValue.get("Val");
                                }
                            }
                        }
                    }
                }
            }
            // }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (val != null && val.size() > 0) {

            for (int ii = 0; ii < val.size(); ii++) {
                Hashtable return_value = (Hashtable) val.get(ii);
                if (return_value != null && return_value.size() > 0) {
                    String name = return_value.get("variable_name").toString();
                    String value = return_value.get("value").toString();
                    if (name.equalsIgnoreCase("Max_used_connections")) {
                        name = "��������Ӧ�����������";
                    }
                    if (name.equalsIgnoreCase("Handler_read_first")) {
                        name = "�����е�һ�������Ĵ���";
                    }
                    if (name.equalsIgnoreCase("Handler_read_key")) {
                        name = "���ݼ���һ�е�������";
                    }
                    if (name.equalsIgnoreCase("Handler_read_next")) {
                        name = "���ռ�˳�����һ�е�������";
                    }
                    if (name.equalsIgnoreCase("Handler_read_prev")) {
                        name = "���ռ�˳���ǰһ�е�������";
                    }
                    if (name.equalsIgnoreCase("Handler_read_rnd")) {
                        name = "H���ݹ̶�λ�ö�һ�е�������";
                    }
                    if (name.equalsIgnoreCase("Handler_read_rnd_next")) {
                        name = "�������ļ��ж���һ�е�������";
                    }
                    if (name.equalsIgnoreCase("Open_tables")) {
                        name = "��ǰ�򿪵ı������";
                    }
                    if (name.equalsIgnoreCase("Opened_tables")) {
                        name = "�Ѿ��򿪵ı������";
                    }
                    if (name.equalsIgnoreCase("Threads_cached")) {
                        name = "�̻߳����ڵ��̵߳�����";
                    }
                    if (name.equalsIgnoreCase("Threads_connected")) {
                        name = "��ǰ�򿪵����ӵ�����";
                    }
                    if (name.equalsIgnoreCase("Threads_created")) {
                        name = "���������������ӵ��߳���";
                    }
                    if (name.equalsIgnoreCase("Threads_running")) {
                        name = "����ķ�˯��״̬���߳���";
                    }
                    if (name.equalsIgnoreCase("Table_locks_immediate")) {
                        name = "������õı�����Ĵ���";
                    }
                    if (name.equalsIgnoreCase("Table_locks_waited")) {
                        name = "����������õı�����Ĵ���";
                    }
                    if (name.equalsIgnoreCase("Key_read_requests")) {
                        name = "�ӻ�����������ݿ��������";
                    }
                    if (name.equalsIgnoreCase("Key_reads")) {
                        name = "��Ӳ�̶�ȡ�������ݿ�Ĵ���";
                    }
                    if (name.equalsIgnoreCase("log_slow_queries")) {
                        name = "�Ƿ��¼����ѯ";
                    }
                    if (name.equalsIgnoreCase("slow_launch_time")) {
                        name = "�����̵߳�ʱ�䳬��������������������Slow_launch_threads״̬����";
                    }

                    StatisNumer numer = new StatisNumer();
                    numer.setIp(ip);
                    numer.setName(name);
                    numer.setCurrent(value);
                    numer.setType("valInfo");
                    list.add(numer);
                }
            }

        }
        return list;

    }

    private String[] getIdValue(String ids) {
        String[] idValue = null;
        if (ids != null && !ids.equals("null") && !ids.equals("")) {
            idValue = new String[ids.split(",").length];
            idValue = ids.split(",");
        }
        return idValue;
    }

    public List<String[]> getAllValueForSubscribe(String ids, String startTime, String toTime) {
        List<String[]> data = new ArrayList<String[]>();
        try {
            if (startTime == null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                startTime = sdf.format(new Date()) + " 00:00:00";
            } else {
                startTime = startTime + " 00:00:00";
            }

            if (toTime == null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                toTime = sdf.format(new Date()) + " 23:59:59";
            } else {
                toTime = toTime + " 23:59:59";
            }
            List<String> ipList = getIPListByIds(ids);
            if (ipList == null) {
                return data;
            } else {
                logger.info("�����ģ������" + ipList.size() + "���豸����Ϣ");
            }
            int i = 0;
            for (String ip : ipList) {
                try {
                    HostNode hostNode = null;
                    HostNodeDao hostNodeDao = new HostNodeDao();
                    try {
                        hostNode = (HostNode) hostNodeDao.findByIpaddress(ip);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        hostNodeDao.close();
                    }
                    if (hostNode == null) {
                        continue;
                    }
                    String nodeid = String.valueOf(hostNode.getId());
                    String nodeName = hostNode.getAlias();
                    String pingValue = getCategoryValue(ip, "Ping", "ConnectUtilization", startTime, toTime);
                    String cpuValue = getCategoryValue(ip, "CPU", "Utilization", startTime, toTime);
                    String memoryValue = getCategoryValue(ip,  "Memory", "avg", startTime, toTime);
                    String eventListNumLevel1 = getEventListNumByLevel(nodeid, startTime, toTime, "1");
                    String eventListNumLevel2 = getEventListNumByLevel(nodeid, startTime, toTime, "2");
                    String eventListNumLevel3 = getEventListNumByLevel(nodeid, startTime, toTime, "3");

                    String[] value = new String[8]; 
                    value[0] = ip;
                    value[1] = nodeName;
                    value[2] = pingValue;
                    value[3] = cpuValue;
                    value[4] = memoryValue;
                    value[5] = eventListNumLevel1;
                    value[6] = eventListNumLevel2;
                    value[7] = eventListNumLevel3;
                    data.add(value);
                    i++;
                    logger.info("�����ģ�����ɵ�" + i + "���豸��Ϣ�Ĳ�ѯ��ip=" + ip);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public String getCategoryValue(String ip, String category, String subentity, String startTime, String toTime) {
        String value = null;
        try {
            I_HostCollectData hostmanager = new HostCollectDataManager();
            Hashtable valuehash = null;
            if ("Memory".equalsIgnoreCase(category)) {
                valuehash = hostmanager.getNetMemeory(ip,
                                "Memory", startTime, toTime);
            } else {
                valuehash = hostmanager.getCategory(ip, category,
                                subentity, startTime, toTime);
            }
            value = getCategoryValue(category, valuehash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (value == null) {
            value = "0";
        }
        return value;
    }

    public String getCategoryValue(String category, Hashtable valuehash) {
        String key = "";
        String value = "0";
        if (valuehash == null) {
            return value;
        }
        if ("Ping".equalsIgnoreCase(category)) {
            key = "avgpingcon";
        } else if ("CPU".equalsIgnoreCase(category)) {
            key = "avgcpucon";
        } else if ("Memory".equalsIgnoreCase(category)) {
            key = "avg";
        }
        value = (String) valuehash.get(key);
        return value;
    }

    public List<String> getIPListByIds(String ids) {
        List<String> ipList = new ArrayList<String>();
        Set<String> ipSet = new HashSet<String>();
        String[] idValue = this.getIdValue(ids);
        if (idValue == null || idValue.length == 0) {
            return ipList;
        }
        for (int i = 0; i < idValue.length; i++) {
            String ip = "";
            if (idValue[i].indexOf("ping") >= 0) {
                String pingvalue = "0";
                ip = idValue[i].replace("ping", "");
            } else if (idValue[i].indexOf("cpu") >= 0) {
                ip = idValue[i].replace("cpu", "");
            }  else if (idValue[i].indexOf("mem") >= 0) {
                ip = idValue[i].replace("mem", "");
            } else if (idValue[i].indexOf("port") >= 0) {
                String realValue = idValue[i];
                String[] idRelValue = new String[realValue.split("\\*").length];
                idRelValue = realValue.split("\\*");
                if (idRelValue.length < 4) {
                    continue;
                }
                ip = idRelValue[1].trim();
            } else if (idValue[i].indexOf("port") >= 0) {
                String realValue = idValue[i];
                String[] idRelValue = new String[realValue.split("\\*").length];
                idRelValue = realValue.split("\\*");
                if (idRelValue.length < 4) {
                    continue;
                }
                ip = idRelValue[1].trim();
            } else if (idValue[i].indexOf("disk") >= 0) {// ����
                ip = idValue[i].replace("disk", "");
            } else {
                continue;
            }
            ipSet.add(ip);
        }
        if (ipSet.size() > 0) {
            Iterator<String> iterator = ipSet.iterator();
            while (iterator.hasNext()) {
                String ip = iterator.next();
                ipList.add(ip);
            }
        }
        return ipList;
    }

    public String getEventListNumByLevel(String nodeid, String startTime, String toTime, String level) {
        String eventListNum = "0";
        EventListDao eventListDao = new EventListDao();
        try {
            List<Object> list = eventListDao.getSummary(startTime, toTime, nodeid, null, level, null, null);
            if (list != null && list.size() > 0) {
                eventListNum = String.valueOf(list.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventListDao.close();
        }
        return eventListNum;
    }
}
