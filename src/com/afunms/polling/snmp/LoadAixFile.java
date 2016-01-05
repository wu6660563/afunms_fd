package com.afunms.polling.snmp;

/*
 * @author hukelei@dhcc.com.cn
 *
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.send.SendAlarmUtil;
import com.afunms.alarm.send.SendMailAlarm;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.Arith;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.ErrptlogUtil;
import com.afunms.common.util.ReadErrptlog;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.config.dao.ErrptconfigDao;
import com.afunms.config.dao.NodeToBusinessDao;
import com.afunms.config.dao.ProcsDao;
import com.afunms.config.model.Errptconfig;
import com.afunms.config.model.Errptlog;
import com.afunms.config.model.NodeToBusiness;
import com.afunms.config.model.Nodeconfig;
import com.afunms.config.model.Nodecpuconfig;
import com.afunms.config.model.Procs;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.EventList;
import com.afunms.event.model.Smscontent;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.loader.HostLoader;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.Diskcollectdata;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.Memorycollectdata;
import com.afunms.polling.om.Processcollectdata;
import com.afunms.polling.om.Systemcollectdata;
import com.afunms.polling.om.Usercollectdata;
import com.afunms.polling.om.UtilHdx;
import com.gatherResulttosql.HostDatatempCollecttimeRtosql;
import com.gatherResulttosql.HostDatatempCpuconfiRtosql;
import com.gatherResulttosql.HostDatatempCpuperRtosql;
import com.gatherResulttosql.HostDatatempDiskPeriofRtosql;
import com.gatherResulttosql.HostDatatempDiskRttosql;
import com.gatherResulttosql.HostDatatempErrptRtosql;
import com.gatherResulttosql.HostDatatempNodeconfRtosql;
import com.gatherResulttosql.HostDatatempPageRtosql;
import com.gatherResulttosql.HostDatatempPagingRtosql;
import com.gatherResulttosql.HostDatatempProcessRtTosql;
import com.gatherResulttosql.HostDatatempRuteRtosql;
import com.gatherResulttosql.HostDatatempUserRtosql;
import com.gatherResulttosql.HostDatatempVolumeRtosql;
import com.gatherResulttosql.HostDatatempiflistRtosql;
import com.gatherResulttosql.HostDatatempinterfaceRtosql;
import com.gatherResulttosql.HostDatatempnDiskperfRtosql;
import com.gatherResulttosql.HostDatatempserciceRttosql;
import com.gatherResulttosql.HostDatatemputilhdxRtosql;
import com.gatherResulttosql.HostPagingResultTosql;
import com.gatherResulttosql.HostPhysicalMemoryResulttosql;
import com.gatherResulttosql.HostcpuResultTosql;
import com.gatherResulttosql.HostdiskResultosql;
import com.gatherResulttosql.NetHostDatatempCpuRTosql;
import com.gatherResulttosql.NetHostDatatempSystemRttosql;
import com.gatherResulttosql.NetHostMemoryRtsql;

/**
 * @author Administrator
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LoadAixFile {

    private String ipaddress;

    private java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {

        HostLoader hostLoader = new HostLoader();
        Host host = hostLoader.loadOneByID(nodeGatherIndicators.getNodeid());
        if (host == null) {
            return null;
        }
        if (!host.isManaged()) {
            return null;
        }

        // yangjun
        Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(
                host.getIpAddress());
        if (ipAllData == null) {
            ipAllData = new Hashtable();
        }

        // ���ڴ�����е�����
        Hashtable returnHash = new Hashtable();
        StringBuffer fileContent = new StringBuffer();
        Vector cpuVector = new Vector();
        Vector systemVector = new Vector();
        Vector userVector = new Vector();
        Vector diskVector = new Vector();
        Vector processVector = new Vector();
        Nodeconfig nodeconfig = new Nodeconfig();
        Vector interfaceVector = new Vector();
        Vector utilhdxVector = new Vector();
        Vector errptlogVector = new Vector();
        String collecttime = "";
        Vector volumeVector = new Vector();
        List routeList = new ArrayList();

        CPUcollectdata cpudata = null;
        Systemcollectdata systemdata = null;
        Usercollectdata userdata = null;
        Processcollectdata processdata = null;
        nodeconfig.setNodeid(host.getId());
        nodeconfig.setHostname(host.getAlias());
        float PhysicalMemCap = 0;
        float freePhysicalMemory = 0;
        float allPhyPagesSize = 0;
        float usedPhyPagesSize = 0;
        float SwapMemCap = 0;
        float freeSwapMemory = 0;
        float usedSwapMemory = 0;
        Hashtable pagehash = new Hashtable();
        Hashtable paginghash = new Hashtable();

        Hashtable networkconfig = new Hashtable();

        try {

            String filename = ResourceCenter.getInstance().getSysPath()
                    + "/linuxserver/" + host.getIpAddress() + ".log";

            File file = new File(filename);
            if (!file.exists()) {
                // �ļ�������,������澯
                try {
                    createFileNotExistAlarm(ipaddress);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            file = null;
            FileInputStream fis = new FileInputStream(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String strLine = null;
            // �����ļ�����
            while ((strLine = br.readLine()) != null) {
                fileContent.append(strLine + "\n");
                // SysLogger.info(strLine);
            }
            isr.close();
            fis.close();
            br.close();
            try {
                //copyFile(host.getIpAddress(), getMaxNum(host.getIpAddress()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Pattern tmpPt = null;
        Matcher mr = null;
        Calendar date = Calendar.getInstance();

        // ----------------�������ݲɼ�ʱ������--���������---------------------
        tmpPt = Pattern.compile("(cmdbegin:collecttime)(.*)(cmdbegin:version)",
                Pattern.DOTALL);
        mr = tmpPt.matcher(fileContent.toString());
        if (mr.find()) {
            collecttime = mr.group(2);
        }
        if (collecttime != null && collecttime.length() > 0) {
            collecttime = collecttime.trim();
        }

        // ----------------����version����--���������---------------------
        String versionContent = "";
        tmpPt = Pattern.compile("(cmdbegin:version)(.*)(cmdbegin:vmstat)",
                Pattern.DOTALL);
        mr = tmpPt.matcher(fileContent.toString());
        if (mr.find()) {
            versionContent = mr.group(2);
        }
        if (versionContent != null && versionContent.length() > 0
                && versionContent.length() < 50) {
            nodeconfig.setCSDVersion(versionContent.trim());
        } else {
            nodeconfig.setCSDVersion("");
        }

        // ----------------����vmstat����--���������---------------------
        String vmstat_Content = "";
        tmpPt = Pattern.compile("(cmdbegin:vmstat)(.*)(cmdbegin:lsps)",
                Pattern.DOTALL);
        mr = tmpPt.matcher(fileContent.toString());
        if (mr.find()) {
            try {
                vmstat_Content = mr.group(2);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        String[] vmstat_LineArr = null;
        String[] vmstat_tmpData = null;
        try {
            vmstat_LineArr = vmstat_Content.split("\n");
            for (int i = 1; i < vmstat_LineArr.length; i++) {
                if (vmstat_LineArr[i].trim().indexOf("System configuration:") >= 0) {
                    String mem = (vmstat_LineArr[i].trim().substring(
                            vmstat_LineArr[i].trim().indexOf("mem="),
                            vmstat_LineArr[i].trim().length() - 2));
                    if (mem.indexOf("MB") > 0) {
                        mem = mem.substring(0, mem.indexOf("MB"));
                    }
                    PhysicalMemCap = Float.parseFloat(mem
                            .replaceAll("mem=", "").trim().replaceAll("MB", "")
                            .trim());

                }

                vmstat_tmpData = vmstat_LineArr[i].trim().split("\\s++");
                if ((vmstat_tmpData != null && vmstat_tmpData.length == 17 || vmstat_tmpData.length == 19)) {
                    if (vmstat_tmpData[0] != null
                            && !vmstat_tmpData[0].equalsIgnoreCase("r")) {
                        freePhysicalMemory = Integer
                                .parseInt(vmstat_tmpData[3]) * 4 / 1024;

                        String re = vmstat_tmpData[4];
                        String pi = vmstat_tmpData[5];
                        String po = vmstat_tmpData[6];
                        String fr = vmstat_tmpData[7];
                        String sr = vmstat_tmpData[8];
                        String cy = vmstat_tmpData[9];
                        String iw = vmstat_tmpData[16];
                        pagehash.put("re", re);
                        pagehash.put("pi", pi);
                        pagehash.put("po", po);
                        pagehash.put("fr", fr);
                        pagehash.put("sr", sr);
                        pagehash.put("cy", cy);
                        pagehash.put("cy", cy);
                        pagehash.put("iw", iw);
                    }
                    // ��iowaitֵ���и澯���
                    Hashtable collectHash = new Hashtable();
                    collectHash.put("vmstat", pagehash);
                    try {
                        AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                        List list = alarmIndicatorsUtil
                                .getAlarmInicatorsThresholdForNode(String
                                        .valueOf(host.getId()),
                                        AlarmConstant.TYPE_HOST, "aix",
                                        "iowait");
                        String value = "0";
                        if (pagehash != null && pagehash.size() > 0) {
                            value = (String) pagehash.get("iw");
                        }
                        for (int k = 0; k < list.size(); k++) {
                            AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode) list
                                    .get(k);
                            // ��CPUֵ���и澯���
                            CheckEventUtil checkutil = new CheckEventUtil();
                            checkutil.checkEvent(host, alarmIndicatorsnode, value);
                            // }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ----------------����Paging Space����--���������---------------------
        String Paging_Content = "";
        tmpPt = Pattern.compile("(cmdbegin:lsps)(.*)(cmdbegin:swap)",
                Pattern.DOTALL);
        mr = tmpPt.matcher(fileContent.toString());
        if (mr.find()) {
            try {
                Paging_Content = mr.group(2);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        String[] Paging_LineArr = null;
        String[] Paging_tmpData = null;

        try {
            Paging_LineArr = Paging_Content.split("\n");
            if (Paging_LineArr != null && Paging_LineArr.length > 1) {
                Paging_tmpData = Paging_LineArr[2].trim().split("\\s++");
                if (Paging_tmpData != null) {
                    String Total_Paging_Space = Paging_tmpData[0];
                    String Percent_Used = Paging_tmpData[1];
                    paginghash.put("Total_Paging_Space", Total_Paging_Space);
                    paginghash.put("Percent_Used", Percent_Used);

                    Hashtable collectHash = new Hashtable();
                    collectHash.put("pagingusage", paginghash);
                    // �Ի�ҳֵ���и澯���
                    try {
                        AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                        List list = alarmIndicatorsUtil
                                .getAlarmInicatorsThresholdForNode(String
                                        .valueOf(host.getId()),
                                        AlarmConstant.TYPE_HOST, "aix",
                                        "pagingusage");
                        String value = "0";
                        if (paginghash != null && paginghash.size() > 0) {
                            if (paginghash.get("Percent_Used") != null) {
                                value = ((String) paginghash.get("Percent_Used"))
                                        .replaceAll("%", "");
                            }
                        }
                        for (int i = 0; i < list.size(); i++) {
                            AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode) list
                                    .get(i);
                            // �Ի�ҳ�ʽ��и澯���
                            CheckEventUtil checkutil = new CheckEventUtil();
                            checkutil.checkEvent(host, alarmIndicatorsnode, value);
                            // }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // �ܻ�ҳ
                    try {
                        Total_Paging_Space = Total_Paging_Space.replaceAll(
                                "MB", "");
                        allPhyPagesSize = Float.parseFloat(Total_Paging_Space);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // ��ʹ����
                    try {
                        Percent_Used = Percent_Used.replaceAll("%", "");
                        usedPhyPagesSize = Float.parseFloat(Percent_Used);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ----------------����swap����--���������---------------------
        String swap_Content = "";
        tmpPt = Pattern.compile("(cmdbegin:swap)(.*)(cmdbegin:process\n)",
                Pattern.DOTALL);
        mr = tmpPt.matcher(fileContent.toString());
        if (mr.find()) {
            try {
                swap_Content = mr.group(2);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        String[] swap_LineArr = null;
        String[] swap_tmpData = null;
        try {
            swap_LineArr = swap_Content.trim().split("\n");
            if (swap_LineArr != null && swap_LineArr.length > 0) {
                swap_tmpData = swap_LineArr[0].trim().split("\\s++");
                if (swap_tmpData != null && swap_tmpData.length == 12) {

                    try {

                        SwapMemCap = Float.parseFloat(swap_tmpData[2].trim());
                        freeSwapMemory = Float.parseFloat(swap_tmpData[10]
                                .trim());
                        usedSwapMemory = Float.parseFloat(swap_tmpData[6]
                                .trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ----------------����processorconfig����--���������------û�����---------------
        String processorconfigContent = "";
        tmpPt = Pattern.compile(
                "(cmdbegin:processor\n)(.*)(cmdbegin:cpuconfig\n)",
                Pattern.DOTALL);
        mr = tmpPt.matcher(fileContent.toString());
        if (mr.find()) {
            processorconfigContent = mr.group(2);
        }
        String[] processorconfigLineArr = processorconfigContent.trim().split(
                "\n");
        if (processorconfigLineArr != null && processorconfigLineArr.length > 0) {
            nodeconfig.setNumberOfProcessors(
                    processorconfigLineArr.length + "");
        }
        // cpu��������澯
//        CheckEventUtil cEventUtil = new CheckEventUtil();
//        cEventUtil
//                .hardwareInfo(host, "cpu", processorconfigLineArr.length + "");


        // ----------------����processorconfig����--���������---------------------
        String cpuconfigContent = "";
        tmpPt = Pattern.compile(
                "(cmdbegin:cpuconfig\n)(.*)(cmdbegin:allconfig\n)",
                Pattern.DOTALL);
        mr = tmpPt.matcher(fileContent.toString());
        if (mr.find()) {
            processorconfigContent = mr.group(2);
        }
        String[] cpuconfigLineArr = processorconfigContent.split("\n");
        String[] cpuconfig_tmpData = null;
        List<Nodecpuconfig> cpuconfiglist = new ArrayList<Nodecpuconfig>();
        Nodecpuconfig nodecpuconfig = new Nodecpuconfig();
        String procesors = "";
        String processorType = "";
        String processorSpeed = "";
        String cputype = "";
        for (int i = 0; i < cpuconfigLineArr.length; i++) {

            String[] result = null;
            if (cpuconfigLineArr[i].trim().contains("��")) {
                result = cpuconfigLineArr[i].trim().split("��");// ���İ������
            } else {
                result = cpuconfigLineArr[i].trim().split("��");// Ӣ�İ�aix������
            }
            if (result.length > 0) {
                if (result[0].trim().equalsIgnoreCase("Number Of Processors")) {
                    // ����������
                    // ���ýڵ��CPU���ø���
                } else if (result[0].trim().equalsIgnoreCase("CPU Type")
                        || "CPU ����".equals(result[0].trim())) {
                    // CPU����λ
                    cputype = result[1].trim() + "";
                } else if (result[0].trim().equalsIgnoreCase("Processor Type")
                        || "�ں�����".equals(result[0].trim())) {
                    // //CPU����
                    // processorType = result[1].trim()+"";
                } else if (result[0].trim().equalsIgnoreCase(
                        "Processor Clock Speed")
                        || "������ʱ���ٶ�".equals(result[0].trim())) {
                    // CPU�ں���Ƶ
                    processorSpeed = result[1].trim() + "";
                    if (nodeconfig.getNumberOfProcessors() != null
                            && nodeconfig.getNumberOfProcessors().trim()
                                    .length() > 0) {
                        int pnum = Integer.parseInt(nodeconfig
                                .getNumberOfProcessors());
                        for (int k = 0; k < pnum; k++) {
                            nodecpuconfig.setDataWidth(cputype);
                            nodecpuconfig.setProcessorId(k + "");
                            nodecpuconfig.setName("");
                            nodecpuconfig.setNodeid(host.getId());
                            nodecpuconfig.setL2CacheSize("");
                            nodecpuconfig.setL2CacheSpeed("");
                            nodecpuconfig.setProcessorType(processorType);
                            nodecpuconfig.setProcessorSpeed(processorSpeed);
                            cpuconfiglist.add(nodecpuconfig);
                            nodecpuconfig = new Nodecpuconfig();
                        }
                    }
                } else if (result[0].trim().equalsIgnoreCase("Memory Size")
                        || "�ڴ��С".equals(result[0].trim())) {
                    String allphy = result[1].trim() + "";
                    try {
                        allphy = allphy.replaceAll("MB", "");
                        if (PhysicalMemCap == 0) {
                            PhysicalMemCap = Float.parseFloat(allphy);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        nodecpuconfig = null;
        // //----------------����cpuconfig����--���������---------------------
        // String cpuconfigContent = "";
        // tmpPt =
        // Pattern.compile("(cmdbegin:allconfig)(.*)(cmdbegin:disk\n)",Pattern.DOTALL);
        // mr = tmpPt.matcher(fileContent.toString());
        // if(mr.find())
        // {
        // cpuconfigContent = mr.group(2);
        // //System.out.println("================cpuconfig======================");
        // ///System.out.println(cpuconfigContent);
        // //System.out.println("=================cpuconfig=====================");
        //			
        // }
        // String[] cpuconfigLineArr = cpuconfigContent.split("\n");
        // String[] cpuconfig_tmpData = null;
        // List<Nodecpuconfig> cpuconfiglist = new ArrayList<Nodecpuconfig>();
        // Nodecpuconfig nodecpuconfig = new Nodecpuconfig();
        // String procesors = "";
        // String processorType = "";
        // String processorSpeed = "";
        // for(int i=0; i<cpuconfigLineArr.length;i++){
        // String[] result = cpuconfigLineArr[i].trim().split(":");
        // if (result.length>0){
        // if(result[0].trim().equalsIgnoreCase("Number Of Processors")){
        // //����������
        // //���ýڵ��CPU���ø���
        // nodeconfig.setNumberOfProcessors(result[1].trim()+"");
        // //System.out.println("======CPU����===="+result[1].trim()+"");
        // }else if(result[0].trim().equalsIgnoreCase("CPU Type")){
        // //CPU����λ
        // if(nodeconfig.getNumberOfProcessors()!= null &&
        // nodeconfig.getNumberOfProcessors().trim().length()>0){
        // int pnum = Integer.parseInt(nodeconfig.getNumberOfProcessors());
        // for(int k=0;k<pnum;k++){
        // nodecpuconfig.setDataWidth(result[1].trim()+"");
        // nodecpuconfig.setProcessorId(k+"");
        // nodecpuconfig.setName("");
        // nodecpuconfig.setNodeid(host.getId());
        // nodecpuconfig.setL2CacheSize("");
        // nodecpuconfig.setL2CacheSpeed("");
        // nodecpuconfig.setProcessorType(processorType);
        // nodecpuconfig.setProcessorSpeed(processorSpeed);
        // cpuconfiglist.add(nodecpuconfig);
        // nodecpuconfig = new Nodecpuconfig();
        // }
        // }
        // }else if(result[0].trim().equalsIgnoreCase("Processor Type")){
        // //CPU����
        // processorType = result[1].trim()+"";
        // // if(nodeconfig.getNumberOfProcessors()!= null &&
        // nodeconfig.getNumberOfProcessors().trim().length()>0){
        // // int pnum = Integer.parseInt(nodeconfig.getNumberOfProcessors());
        // // for(int k=0;k<pnum;k++){
        // // Nodecpuconfig _nodecpuconfig =
        // (Nodecpuconfig)cpuconfiglist.get(k);
        // // _nodecpuconfig.setProcessorType(result[1].trim()+"");
        // // cpuconfiglist.add(k, _nodecpuconfig);
        // // }
        // // }
        // }else if(result[0].trim().equalsIgnoreCase("Processor Clock Speed")){
        // //CPU�ں���Ƶ
        // processorSpeed = result[1].trim()+"";
        // // if(nodeconfig.getNumberOfProcessors()!= null &&
        // nodeconfig.getNumberOfProcessors().trim().length()>0){
        // // int pnum = Integer.parseInt(nodeconfig.getNumberOfProcessors());
        // // SysLogger.info("cpuconfiglist========size:"+cpuconfiglist.size());
        // // for(int k=0;k<pnum;k++){
        // // Nodecpuconfig _nodecpuconfig =
        // (Nodecpuconfig)cpuconfiglist.get(k);
        // // _nodecpuconfig.setProcessorSpeed(result[1].trim()+"");
        // // cpuconfiglist.add(k, _nodecpuconfig);
        // // }
        // // }
        // }else if(result[0].trim().equalsIgnoreCase("Good Memory Size")){
        // String allphy = result[1].trim().trim();
        // try{
        // allphy = allphy.replaceAll("MB", "");
        // PhysicalMemCap = Float.parseFloat(allphy);
        // }catch(Exception e){
        // e.printStackTrace();
        // }
        // //nodecpuconfig.setDataWidth(result[1].trim()+"");
        // }else if(result[0].trim().equalsIgnoreCase("IP Address") &&
        // result.length==2){
        // //IP��ַ
        // networkconfig.put("IP",result[1].trim()+"");
        // }else if(result[0].trim().equalsIgnoreCase("Sub Netmask") &&
        // result.length==2 ){
        // //��������
        // networkconfig.put("NETMASK",result[1].trim()+"");
        // }else if(result[0].trim().equalsIgnoreCase("Gateway") &&
        // result.length==2){
        // //����
        // networkconfig.put("GATEWAY",result[1].trim()+"");
        // // }else if(result[0].trim().equalsIgnoreCase("Total Paging Space")){
        // // //��ҳ�ռ���Ϣ
        // // String allphy = result[1].trim().trim();
        // // try{
        // // allphy = allphy.replaceAll("MB", "");
        // // allPhyPagesSize = Float.parseFloat(allphy);
        // // }catch(Exception e){
        // // e.printStackTrace();
        // // }
        // // }else if(result[0].trim().equalsIgnoreCase("Percent Used")){
        // // //��ʹ�õĵ�ҳ�ռ���Ϣ
        // // String allphy = result[1].trim().trim();
        // // try{
        // // allphy = allphy.replaceAll("%", "");
        // // usedPhyPagesSize = Float.parseFloat(allphy);
        // // }catch(Exception e){
        // // e.printStackTrace();
        // // }
        // }
        //				
        //											
        // }
        //			
        // }
        // nodecpuconfig = null;

        // System.out.println("=========================6==============================");
        // ----------------����disk����--���������---------------------
        // disk���ݼ��ϣ��仯ʱ���и澯���
        Hashtable<String, Object> diskInfoHash = new Hashtable<String, Object>();
        // ���̴�С
        float diskSize = 0;
        // �������Ƽ���
        List<String> diskNameList = new ArrayList<String>();
        String diskContent = "";
        String diskLabel;
        List disklist = new ArrayList();
        tmpPt = Pattern.compile("(cmdbegin:disk\n)(.*)(cmdbegin:diskperf)",
                Pattern.DOTALL);
        mr = tmpPt.matcher(fileContent.toString());
        if (mr.find()) {
            diskContent = mr.group(2);

        }
        String[] diskLineArr = diskContent.split("\n");
        String[] tmpData = null;
        Diskcollectdata diskdata = null;
        int diskflag = 0;
        for (int i = 1; i < diskLineArr.length; i++) {

            tmpData = diskLineArr[i].split("\\s++");
            if ((tmpData != null) && (tmpData.length == 7)) {
                diskLabel = tmpData[6];

                diskdata = new Diskcollectdata();
                diskdata.setIpaddress(ipaddress);
                diskdata.setCollecttime(date);
                diskdata.setCategory("Disk");
                diskdata.setEntity("Utilization");// ���ðٷֱ�
                diskdata.setSubentity(tmpData[6]);
                diskdata.setRestype("static");
                diskdata.setUnit("%");
                try {
                    diskdata.setThevalue(Float.toString(Float
                            .parseFloat(tmpData[3].substring(0, tmpData[3]
                                    .indexOf("%")))));
                } catch (Exception ex) {
                    continue;
                }
                diskVector.addElement(diskdata);

                // yangjun
                try {
                    String diskinc = "0.0";
                    float pastutil = 0.0f;
                    Vector disk_v = (Vector) ipAllData.get("disk");
                    if (disk_v != null && disk_v.size() > 0) {
                        for (int si = 0; si < disk_v.size(); si++) {
                            Diskcollectdata disk_data = (Diskcollectdata) disk_v
                                    .elementAt(si);
                            if ((tmpData[6]).equals(disk_data.getSubentity())
                                    && "Utilization".equals(disk_data
                                            .getEntity())) {
                                pastutil = Float.parseFloat(disk_data
                                        .getThevalue());
                            }
                        }
                    } else {
                        pastutil = Float.parseFloat(tmpData[3].substring(0,
                                tmpData[3].indexOf("%")));
                    }
                    if (pastutil == 0) {
                        pastutil = Float.parseFloat(tmpData[3].substring(0,
                                tmpData[3].indexOf("%")));
                    }
                    if (Float.parseFloat(tmpData[3].substring(0, tmpData[3]
                            .indexOf("%")))
                            - pastutil > 0) {
                        diskinc = (Float.parseFloat(tmpData[3].substring(0,
                                tmpData[3].indexOf("%"))) - pastutil)
                                + "";
                    }
                    diskdata = new Diskcollectdata();
                    diskdata.setIpaddress(host.getIpAddress());
                    diskdata.setCollecttime(date);
                    diskdata.setCategory("Disk");
                    diskdata.setEntity("UtilizationInc");// ���������ʰٷֱ�
                    diskdata.setSubentity(tmpData[6]);
                    diskdata.setRestype("dynamic");
                    diskdata.setUnit("%");
                    diskdata.setThevalue(diskinc);
                    diskVector.addElement(diskdata);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //

                diskdata = new Diskcollectdata();
                diskdata.setIpaddress(ipaddress);
                diskdata.setCollecttime(date);
                diskdata.setCategory("Disk");
                diskdata.setEntity("AllSize");// �ܿռ�
                diskdata.setSubentity(tmpData[6]);
                diskdata.setRestype("static");

                float allblocksize = 0;
                allblocksize = Float.parseFloat(tmpData[1]);
                float allsize = 0.0f;
                allsize = allblocksize;
                // �����ܴ�С ��λΪM
                diskSize = diskSize + allsize;
                // �������Ʒ��뼯��
                if (!diskdata.getSubentity().equals("")) {
                    diskNameList.add(diskdata.getSubentity());
                }
                if (allsize >= 1024.0f) {
                    allsize = allsize / 1024;
                    diskdata.setUnit("G");
                } else {
                    diskdata.setUnit("M");
                }

                diskdata.setThevalue(Float.toString(allsize));
                diskVector.addElement(diskdata);

                diskdata = new Diskcollectdata();
                diskdata.setIpaddress(ipaddress);
                diskdata.setCollecttime(date);
                diskdata.setCategory("Disk");
                diskdata.setEntity("UsedSize");// ʹ�ô�С
                diskdata.setSubentity(tmpData[6]);
                diskdata.setRestype("static");

                float FreeintSize = 0;
                FreeintSize = Float.parseFloat(tmpData[2]);

                float usedfloatsize = 0.0f;
                usedfloatsize = allblocksize - FreeintSize;
                if (usedfloatsize >= 1024.0f) {
                    usedfloatsize = usedfloatsize / 1024;
                    diskdata.setUnit("G");
                } else {
                    diskdata.setUnit("M");
                }
                diskdata.setThevalue(Float.toString(usedfloatsize));
                diskVector.addElement(diskdata);
                disklist.add(diskflag, diskLabel);
                diskflag = diskflag + 1;
            }
        }

        // ���д��̸澯���
        try {
            AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
            List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(
                    String.valueOf(host.getId()), AlarmConstant.TYPE_HOST,
                    "aix");
            for (int i = 0; i < list.size(); i++) {
                AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode) list
                        .get(i);
                if (alarmIndicatorsnode.getName().equalsIgnoreCase("diskperc")) {
                    CheckEventUtil checkutil = new CheckEventUtil();
                    checkutil.checkDisk(host, diskVector, alarmIndicatorsnode);
                    break;
                }
            }
            // ##########�ܴ�С�Լ��̷���Ϣ�仯�����и澯�ж�
            diskSize = diskSize / 1024;
            diskInfoHash.put("diskSize", diskSize + "G");
            diskInfoHash.put("diskNameList", diskNameList);
//            CheckEventUtil checkutil = new CheckEventUtil();
//            checkutil.hardwareInfo(host, "disk", diskInfoHash);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ----------------����diskperf����--���������---------------------
        String diskperfContent = "";
        String average = "";
        tmpPt = Pattern.compile("(cmdbegin:diskperf)(.*)(cmdbegin:diskiostat)",
                Pattern.DOTALL);
        mr = tmpPt.matcher(fileContent.toString());
        if (mr.find()) {
            diskperfContent = mr.group(2);

        }
        String[] diskperfLineArr = diskperfContent.split("\n");
        String[] diskperf_tmpData = null;
        List alldiskperf = new ArrayList();
        Hashtable<String, String> diskperfhash = new Hashtable<String, String>();
        int flag = 0;
        for (int i = 0; i < diskperfLineArr.length; i++) {
            diskperf_tmpData = diskperfLineArr[i].trim().split("\\s++");
            if (diskperf_tmpData != null
                    && (diskperf_tmpData.length == 7 || diskperf_tmpData.length == 8)) {
                if (diskperf_tmpData[0].trim().equalsIgnoreCase("Average")) {
                    flag = 1;
                    diskperfhash.put("%busy", diskperf_tmpData[2].trim());
                    diskperfhash.put("avque", diskperf_tmpData[3].trim());
                    diskperfhash.put("r+w/s", diskperf_tmpData[4].trim());
                    diskperfhash.put("Kbs/s", diskperf_tmpData[5].trim());
                    diskperfhash.put("avwait", diskperf_tmpData[6].trim());
                    diskperfhash.put("avserv", diskperf_tmpData[7].trim());
                    diskperfhash.put("disklebel", diskperf_tmpData[1].trim());
                    alldiskperf.add(diskperfhash);
                } else if (flag == 1) {
                    diskperfhash.put("%busy", diskperf_tmpData[1].trim());
                    diskperfhash.put("avque", diskperf_tmpData[2].trim());
                    diskperfhash.put("r+w/s", diskperf_tmpData[3].trim());
                    diskperfhash.put("Kbs/s", diskperf_tmpData[4].trim());
                    diskperfhash.put("avwait", diskperf_tmpData[5].trim());
                    diskperfhash.put("avserv", diskperf_tmpData[6].trim());
                    diskperfhash.put("disklebel", diskperf_tmpData[0].trim());
                    alldiskperf.add(diskperfhash);
                }

                diskperfhash = new Hashtable();
            }
        }
        // ----------------����diskiostat����--���������----cmdbegin:diskperf-----------------
        String diskioContent = "";
        tmpPt = Pattern.compile("(cmdbegin:diskiostat)(.*)(cmdbegin:netperf)",
                Pattern.DOTALL);
        mr = tmpPt.matcher(fileContent.toString());
        if (mr.find()) {
            diskioContent = mr.group(2);

        }
        String[] diskioLineArr = diskioContent.split("\n");
        String[] diskio_tmpData = null;
        List alldiskio = new ArrayList();
        Hashtable<String, String> diskpiohash = new Hashtable<String, String>();
        int flags = 0;
        for (int i = 0; i < diskioLineArr.length; i++) {
            diskio_tmpData = diskioLineArr[i].trim().split("\\s++");
            if (diskio_tmpData != null) {
                if (diskio_tmpData[0].trim().equalsIgnoreCase("Disks:")
                        || "���̣�".equals(diskio_tmpData[0].trim())) {
                    flags = 1;
                    continue;
                }

                if (flags == 1) {
                    // SysLogger.info(diskio_tmpData[0].trim());
                    diskpiohash.put("Disks", diskio_tmpData[0].trim());
                    diskpiohash.put("%tm_act", diskio_tmpData[1].trim());
                    diskpiohash.put("Kbps", diskio_tmpData[2].trim());
                    diskpiohash.put("tps", diskio_tmpData[3].trim());
                    diskpiohash.put("kb_read", diskio_tmpData[4].trim());
                    diskpiohash.put("kb_wrtn", diskio_tmpData[5].trim());
                    alldiskio.add(diskpiohash);
                }
                diskpiohash = new Hashtable();
            }
        }
        // ----------------����netperf����--���������---------------------
        String netperfContent = "";
        tmpPt = Pattern.compile("(cmdbegin:netperf)(.*)(cmdbegin:netallperf)",
                Pattern.DOTALL);
        mr = tmpPt.matcher(fileContent.toString());
        if (mr.find()) {
            netperfContent = mr.group(2);

        }
        String[] netperfLineArr = netperfContent.split("\n");
        String[] netperf_tmpData = null;
        List netperf = new ArrayList();
        for (int i = 0; i < netperfLineArr.length; i++) {
            netperf_tmpData = netperfLineArr[i].trim().split("\\s++");
            if (netperf_tmpData != null && netperf_tmpData.length == 9) {
                if (netperf_tmpData[0].trim().indexOf("en") >= 0
                        && netperf_tmpData[2].trim().indexOf("link") >= 0) {
                    netperf.add(netperf_tmpData[0].trim());
                }
            } else if (netperf_tmpData != null && netperf_tmpData.length == 10) {
                if (netperf_tmpData[0].trim().indexOf("en") >= 0
                        && netperf_tmpData[2].trim().indexOf("link") >= 0) {
                    netperf.add(netperf_tmpData[0].trim());
                }
            }

        }

        // ----------------����netallperf����--���������---------------------
        List iflist = new ArrayList();
        List oldiflist = new ArrayList();
        List netmedialist = new ArrayList();
        Hashtable netmediahash = new Hashtable();
        String netallperfContent = "";
        tmpPt = Pattern.compile("(cmdbegin:netallperf)(.*)(cmdbegin:uname)",
                Pattern.DOTALL);
        mr = tmpPt.matcher(fileContent.toString());
        if (mr.find()) {
            netallperfContent = mr.group(2);
        }
        String[] netallperfLineArr = netallperfContent.trim().split("\n");
        String[] netallperf_tmpData = null;
        List netalldiskperf = new ArrayList();
        Hashtable<String, String> netallperfhash = new Hashtable<String, String>();
        int macflag = 0;
        String MAC = "";
        if (ipAllData != null) {
            oldiflist = (List) ipAllData.get("iflist");
        }

        if (netperf != null && netperf.size() > 0) {
            Interfacecollectdata interfacedata = null;
            // ��ʼѭ������ӿ�
            for (int k = 0; k < netperf.size(); k++) {
                Hashtable ifhash = new Hashtable();
                Hashtable oldifhash = new Hashtable();// ���������ϴβɼ����
                if (oldiflist != null && oldiflist.size() > 0) {
                    oldifhash = (Hashtable) oldiflist.get(k);
                }
                String portDesc = (String) netperf.get(k);// en1
                // ͨ��
                /**
                 * ����aix ͬһ������ɼ������ݳ��ֲ�ͬ�����ݸ�ʽ��Ҫ����������ж� ��һ�������������ģʽ ģʽһ ETHERNET
                 * STATISTICS (en1 netflg="neten" ģʽ���� Hardware Address:
                 * netflg="netmac" ʹ��һ�� netflg �ж���ʲôģʽ
                 * 
                 */

                String netflg = "";

                if (netallperfContent.indexOf("ETHERNET STATISTICS ("
                        + portDesc) > 0) {
                    netflg = "neten";

                }

                for (int i = 0; i < netallperfLineArr.length; i++) {
                    if (i == 1 && netallperfLineArr[i].indexOf(
                            "Hardware Address:") >= 0) {
                        netflg = "netmac";
                        break;
                    }

                }

                // IDC�汾��ȥmac��ַ
                String mideaspeed = "";// ��������
                String status = "";// ����״̬
                String Bytes = "";// ������������ֽ���
                String Packets = "";// ����������ݰ�
                String LinkStatus = "";// ״̬
                if (netflg.equals("neten")) {
                    tmpPt = Pattern.compile("(start-" + portDesc + ")(.*)(end-"
                            + portDesc + ")", Pattern.DOTALL);
                    mr = tmpPt.matcher(fileContent.toString());
                    String netenContent = "";
                    if (mr.find()) {
                        netenContent = mr.group(2);
                    }

                    String[] netLineArr = null;
                    netLineArr = netenContent.trim().split("\n");
                    MAC = netLineArr[3].trim().substring(
                            netLineArr[3].trim().indexOf("Hardware Address:"));
                    MAC = MAC.replaceAll("Hardware Address:", "").trim();
                    try {

                        mideaspeed = netLineArr[45].trim().substring(
                                netLineArr[45].trim().indexOf(
                                        "Media Speed Running:"));
                        status = netLineArr[43].trim().substring(
                                netLineArr[43].trim().indexOf("Link Status :"));
                        Packets = netLineArr[8].trim();
                        LinkStatus = status;
                        Bytes = netLineArr[9].trim();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // Hardware Address ��ʼ�ĸ�ʽ
                if (netflg.equals("netmac")) {

                    tmpPt = Pattern.compile("(start-" + portDesc + ")(.*)(end-"
                            + portDesc + ")", Pattern.DOTALL);
                    mr = tmpPt.matcher(fileContent.toString());
                    String netenContent = "";
                    if (mr.find()) {
                        netenContent = mr.group(2);
                    }
                    String[] netLineArr = null;
                    netLineArr = netenContent.trim().split("\n");

                    if (netLineArr.length == 13) {
                        MAC = netLineArr[0].trim().substring(
                                netLineArr[0].trim().indexOf(
                                        "Hardware Address:"));
                        MAC = MAC.replaceAll("Hardware Address:", "").trim();
                        try {
                            mideaspeed = netLineArr[11].trim().substring(
                                    netLineArr[11].trim().indexOf(
                                            "Media Speed Running:"));
                            if (netLineArr[10].indexOf("Link Status :") >= 0) {

                                status = netLineArr[10].trim().substring(
                                        netLineArr[10].trim().indexOf(
                                                "Link Status :"));
                                status = netLineArr[10].replaceAll(
                                        "Link Status :", "").trim();
                            }

                            LinkStatus = status;
                            Packets = netLineArr[1].trim();
                            Bytes = netLineArr[2].trim();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                String mac_ = nodeconfig.getMac();
                if (mac_ != null && mac_.trim().length() > 0) {
                    if (k < 3) {
                        mac_ = mac_ + "," + MAC;
                    }

                    nodeconfig.setMac(mac_);
                } else {
                    nodeconfig.setMac(MAC);
                }

                status = status.replaceAll("Link Status :", "").trim();
                netmediahash.put("desc", portDesc);// ����
                netmediahash.put("speed", mideaspeed);// ����
                netmediahash.put("mac", MAC);
                netmediahash.put("status", status);// ����״��
                netmedialist.add(netmediahash);
                netmediahash = new Hashtable();

                // =================�������ݰ�==================
                String outPackets = "0";
                String inPackets = "0";
                if (Packets.indexOf("Packets:") >= 0) {
                    String[] packsperf_tmpData = Packets.split("\\s++");

                    outPackets = packsperf_tmpData[1];// ���͵����ݰ�
                    inPackets = packsperf_tmpData[3];// ���ܵ����ݰ�
                }
                String oldOutPackets = "0";
                String oldInPackets = "0";
                String endOutPackets = "0";
                String endInPackets = "0";

                if (oldifhash != null && oldifhash.size() > 0) {
                    if (oldifhash.containsKey("outPackets")) {
                        oldOutPackets = (String) oldifhash.get("outPackets");
                    }
                    try {
                        endOutPackets = (Long.parseLong(outPackets) - Long
                                .parseLong(oldOutPackets))
                                + "";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (oldifhash.containsKey("inPackets")) {
                        oldInPackets = (String) oldifhash.get("inPackets");
                    }
                    try {
                        endInPackets = (Long.parseLong(inPackets) - Long
                                .parseLong(oldInPackets))
                                + "";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                String outBytes = "0";
                String inBytes = "0";
                if (Bytes.indexOf("Bytes:") >= 0) {
                    String[] bytes_tmpData = Bytes.split("\\s++");
                    outBytes = bytes_tmpData[1];// ���͵��ֽ�
                    inBytes = bytes_tmpData[3];// ���ܵ��ֽ�
                }

                // ===�����ֽ���=====================
                String oldOutBytes = "0";
                String oldInBytes = "0";
                String endOutBytes = "0";
                String endInBytes = "0";

                if (oldifhash != null && oldifhash.size() > 0) {
                    if (oldifhash.containsKey("outBytes")) {
                        oldOutBytes = (String) oldifhash.get("outBytes");
                    }
                    try {
                        endOutBytes = (Long.parseLong(outBytes) - Long
                                .parseLong(oldOutBytes))
                                * 8 / 1024 / 300 + "";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (oldifhash.containsKey("inBytes")) {
                        oldInBytes = (String) oldifhash.get("inBytes");
                    }
                    try {
                        endInBytes = (Long.parseLong(inBytes) - Long
                                .parseLong(oldInBytes))
                                * 8 / 1024 / 300 + "";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // ע�͵ļ�����ֵĿǰ��û������
                // String Interrupts = netallperfLineArr[k*13+3].trim();
                // String PacketsDropped = netallperfLineArr[k*13+4].trim();
                // String BroadcastPackets = netallperfLineArr[k*13+7].trim();
                // String MulticastPackets = netallperfLineArr[k*13+8].trim();

                // System.out.println("&&&&&&&&&&&&&&&&&&&77==="+LinkStatus);

                // String[] link_tmpData = LinkStatus.split(":");
                // String linkstatus ="";
                // if(link_tmpData.length>1)
                // {
                // linkstatus = link_tmpData[1].toLowerCase();
                // }
                String linkstatus = "";
                // System.out.println("&&&&&&&&&&**************************"+LinkStatus);
                linkstatus = LinkStatus.replaceAll("Link Status :", "").trim();
                if (linkstatus.equals("Up")) {
                    linkstatus = "1";
                } else if (linkstatus.equals("Down")) {
                    linkstatus = "2";
                }
                // ============����===============
                String MediaSpeedRunning = mideaspeed;
                String speedunit = "";
                String speedstr = "";
                String mspeed = "0";
                if (MediaSpeedRunning.indexOf(":") >= 0) {
                    String[] speed_tmpData = MediaSpeedRunning.split(":");

                    mspeed = speed_tmpData[1].trim();
                    String[] speed = mspeed.split("\\s++");

                    if (speed.length > 0) {
                        speedstr = speed[0];
                    } else {
                        speedstr = "0";
                    }

                    if (speed.length > 1) {
                        speedunit = speed[1];
                    } else {
                        speedunit = "Mbps";
                    }
                }

                ifhash.put("outPackets", outPackets);
                ifhash.put("inPackets", inPackets);
                ifhash.put("outBytes", outBytes);
                ifhash.put("inBytes", inBytes);

                // �˿�����
                interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ipaddress);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity("index");
                interfacedata.setSubentity(k + 1 + "");
                // �˿�״̬�����棬ֻ��Ϊ��̬���ݷŵ���ʱ����
                interfacedata.setRestype("static");
                interfacedata.setUnit("");
                interfacedata.setThevalue(k + 1 + "");
                interfacedata.setChname("�˿�����");
                interfaceVector.addElement(interfacedata);
                // �˿�����
                interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ipaddress);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity("ifDescr");
                interfacedata.setSubentity(k + 1 + "");
                // �˿�״̬�����棬ֻ��Ϊ��̬���ݷŵ���ʱ����
                interfacedata.setRestype("static");
                interfacedata.setUnit("");
                interfacedata.setThevalue(portDesc);
                interfacedata.setChname("�˿�����2");
                interfaceVector.addElement(interfacedata);
                // �˿ڴ���
                interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ipaddress);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity("ifSpeed");
                interfacedata.setSubentity(k + 1 + "");
                interfacedata.setRestype("static");
                interfacedata.setUnit(speedunit);
                interfacedata.setThevalue(speedstr);
                interfacedata.setChname("");
                interfaceVector.addElement(interfacedata);
                // ��ǰ״̬
                interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ipaddress);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity("ifOperStatus");
                interfacedata.setSubentity(k + 1 + "");
                interfacedata.setRestype("static");
                interfacedata.setUnit("");
                interfacedata.setThevalue(linkstatus);
                interfacedata.setChname("��ǰ״̬");
                interfaceVector.addElement(interfacedata);
                // ��ǰ״̬
                interfacedata = new Interfacecollectdata();
                interfacedata.setIpaddress(ipaddress);
                interfacedata.setCollecttime(date);
                interfacedata.setCategory("Interface");
                interfacedata.setEntity("ifOperStatus");
                interfacedata.setSubentity(k + 1 + "");
                interfacedata.setRestype("static");
                interfacedata.setUnit("");
                interfacedata.setThevalue(1 + "");
                interfacedata.setChname("��ǰ״̬");
                interfaceVector.addElement(interfacedata);
                // �˿��������
                UtilHdx utilhdx = new UtilHdx();
                utilhdx.setIpaddress(ipaddress);
                utilhdx.setCollecttime(date);
                utilhdx.setCategory("Interface");
                String chnameBand = "";
                utilhdx.setEntity("InBandwidthUtilHdx");
                utilhdx.setThevalue(endInBytes);
                utilhdx.setSubentity(k + 1 + "");
                utilhdx.setRestype("dynamic");
                utilhdx.setUnit("Kb/��");
                utilhdx.setChname(k + 1 + "�˿����" + "����");
                utilhdxVector.addElement(utilhdx);
                // �˿ڳ�������
                utilhdx = new UtilHdx();
                utilhdx.setIpaddress(ipaddress);
                utilhdx.setCollecttime(date);
                utilhdx.setCategory("Interface");
                utilhdx.setEntity("OutBandwidthUtilHdx");
                utilhdx.setThevalue(endOutBytes);
                utilhdx.setSubentity(k + 1 + "");
                utilhdx.setRestype("dynamic");
                utilhdx.setUnit("Kb/��");
                utilhdx.setChname(k + 1 + "�˿ڳ���" + "����");
                utilhdxVector.addElement(utilhdx);
                /*
                 * //���������ݰ� utilhdx=new UtilHdx();
                 * utilhdx.setIpaddress(ipaddress);
                 * utilhdx.setCollecttime(date);
                 * utilhdx.setCategory("Interface");
                 * utilhdx.setChname("��վ�����������ݰ�");
                 * utilhdx.setEntity("ifInDiscards");
                 * utilhdx.setThevalue((String)rValue.get("PacketsReceivedDiscarded"));
                 * utilhdx.setSubentity(i+""); utilhdx.setRestype("dynamic");
                 * utilhdx.setUnit("��"); utilhdxVector.addElement(utilhdx);
                 * //��վ�������ݰ� utilhdx=new UtilHdx();
                 * utilhdx.setIpaddress(ipaddress);
                 * utilhdx.setCollecttime(date);
                 * utilhdx.setCategory("Interface");
                 * utilhdx.setChname("��վ�������ݰ�");
                 * utilhdx.setEntity("ifInErrors");
                 * utilhdx.setThevalue((String)rValue.get("PacketsReceivedErrors"));
                 * utilhdx.setSubentity(k+""); utilhdx.setRestype("dynamic");
                 * utilhdx.setUnit("��"); utilhdxVector.addElement(utilhdx);
                 * //��ڷǵ��������ݰ� utilhdx=new UtilHdx();
                 * utilhdx.setIpaddress(ipaddress);
                 * utilhdx.setCollecttime(date);
                 * utilhdx.setCategory("Interface");
                 * utilhdx.setChname("�ǵ��������ݰ�");
                 * utilhdx.setEntity("ifInNUcastPkts");
                 * utilhdx.setThevalue((String)rValue.get("PacketsReceivedNonUnicastPersec"));
                 * utilhdx.setSubentity(k+""); utilhdx.setRestype("dynamic");
                 * utilhdx.setUnit("��"); utilhdxVector.addElement(utilhdx);
                 * //��ڵ��������ݰ� utilhdx=new UtilHdx();
                 * utilhdx.setIpaddress(ipaddress);
                 * utilhdx.setCollecttime(date);
                 * utilhdx.setCategory("Interface");
                 * utilhdx.setChname("���������ݰ�");
                 * utilhdx.setEntity("ifInUcastPkts");
                 * utilhdx.setThevalue((String)rValue.get("PacketsReceivedUnicastPersec"));
                 * utilhdx.setSubentity(k+""); utilhdx.setRestype("dynamic");
                 * utilhdx.setUnit("��"); utilhdxVector.addElement(utilhdx);
                 * //���ڷǵ��������ݰ� utilhdx=new UtilHdx();
                 * utilhdx.setIpaddress(ipaddress);
                 * utilhdx.setCollecttime(date);
                 * utilhdx.setCategory("Interface");
                 * utilhdx.setChname("�ǵ��������ݰ�");
                 * utilhdx.setEntity("ifOutNUcastPkts");
                 * utilhdx.setThevalue((String)rValue.get("PacketsSentNonUnicastPersec"));
                 * utilhdx.setSubentity(k+""); utilhdx.setRestype("dynamic");
                 * utilhdx.setUnit("��"); utilhdxVector.addElement(utilhdx);
                 * //���ڵ��������ݰ� utilhdx=new UtilHdx();
                 * utilhdx.setIpaddress(ipaddress);
                 * utilhdx.setCollecttime(date);
                 * utilhdx.setCategory("Interface");
                 * utilhdx.setChname("���������ݰ�");
                 * utilhdx.setEntity("ifOutUcastPkts");
                 * utilhdx.setThevalue((String)rValue.get("PacketsSentUnicastPersec"));
                 * utilhdx.setSubentity(k+""); utilhdx.setRestype("dynamic");
                 * utilhdx.setUnit("��"); utilhdxVector.addElement(utilhdx);
                 */
                iflist.add(ifhash);
                ifhash = new Hashtable();

            }
        }
        systemdata = new Systemcollectdata();
        systemdata.setIpaddress(ipaddress);
        systemdata.setCollecttime(date);
        systemdata.setCategory("System");
        systemdata.setEntity("MacAddr");
        systemdata.setSubentity("MacAddr");
        systemdata.setRestype("static");
        systemdata.setUnit(" ");
        systemdata.setThevalue(MAC);
        systemVector.addElement(systemdata);

        // ----------------����cpu����--���������---------------------
        String cpuperfContent = "";
        // String average = "";
        tmpPt = Pattern.compile("(cmdbegin:cpu)(.*)(cmdbegin:allconfig)",
                Pattern.DOTALL);
        mr = tmpPt.matcher(fileContent.toString());
        if (mr.find()) {
            cpuperfContent = mr.group(2);
        }
        String[] cpuperfLineArr = cpuperfContent.split("\n");
        List cpuperflist = new ArrayList();
        Hashtable<String, String> cpuperfhash = new Hashtable<String, String>();
        for (int i = 0; i < cpuperfLineArr.length; i++) {
            diskperf_tmpData = cpuperfLineArr[i].trim().split("\\s++");
            if (diskperf_tmpData != null && diskperf_tmpData.length == 5
                    || diskperf_tmpData.length == 6
                    || diskperf_tmpData.length == 7) {

                if (diskperf_tmpData[0].trim().equalsIgnoreCase("Average")
                        || "ƽ��ֵ".equals(diskperf_tmpData[0].trim())) {
                    cpuperfhash.put("%usr", diskperf_tmpData[1].trim());
                    cpuperfhash.put("%sys", diskperf_tmpData[2].trim());
                    cpuperfhash.put("%wio", diskperf_tmpData[3].trim());
                    cpuperfhash.put("%idle", diskperf_tmpData[4].trim());
                    if (diskperf_tmpData.length == 6
                            || diskperf_tmpData.length == 7) {
                        cpuperfhash.put("physc", diskperf_tmpData[5].trim());
                    }
                    cpuperflist.add(cpuperfhash);

                    cpudata = new CPUcollectdata();
                    cpudata.setIpaddress(ipaddress);
                    cpudata.setCollecttime(date);
                    cpudata.setCategory("CPU");
                    cpudata.setEntity("Utilization");
                    cpudata.setSubentity("Utilization");
                    cpudata.setRestype("dynamic");
                    cpudata.setUnit("%");
                    cpudata.setThevalue(Arith.round((100.0 - Double
                            .parseDouble(diskperf_tmpData[4].trim())), 0)
                            + "");
                    cpuVector.addElement(cpudata);

                    // ��CPUֵ���и澯���
                    Hashtable collectHash = new Hashtable();
                    collectHash.put("cpu", cpuVector);
                    try {
                        AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                        List list = alarmIndicatorsUtil
                                .getAlarmInicatorsThresholdForNode(String
                                        .valueOf(host.getId()),
                                        AlarmConstant.TYPE_HOST, "aix", "cpu");
                        String value = "0";
                        if (cpuVector != null && cpuVector.size() > 0) {
                            CPUcollectdata cpudata1 = (CPUcollectdata) cpuVector
                                    .get(0);
                            value = cpudata1.getThevalue();
                        }
                        for (int k = 0; k < list.size(); k++) {
                            AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode) list
                                    .get(k);
                            // ��CPUֵ���и澯���
                            CheckEventUtil checkutil = new CheckEventUtil();
                            checkutil.checkEvent(host, alarmIndicatorsnode, value);
                            // }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // ��memory����д��ȥ
        // �����ڴ�������һ������
        Vector memoryVector = new Vector();
        Memorycollectdata memorydata = null;
        if (PhysicalMemCap > 0) {
            float PhysicalMemUtilization = (PhysicalMemCap - freePhysicalMemory)
                    * 100 / PhysicalMemCap;
            // �������ڴ��С
            memorydata = new Memorycollectdata();
            memorydata.setIpaddress(ipaddress);
            memorydata.setCollecttime(date);
            memorydata.setCategory("Memory");
            memorydata.setEntity("Capability");
            memorydata.setSubentity("PhysicalMemory");
            memorydata.setRestype("static");
            memorydata.setUnit("M");
            memorydata.setThevalue(Float.toString(PhysicalMemCap));
            memoryVector.addElement(memorydata);
            // �Ѿ��õ������ڴ�
            memorydata = new Memorycollectdata();
            memorydata.setIpaddress(ipaddress);
            memorydata.setCollecttime(date);
            memorydata.setCategory("Memory");
            memorydata.setEntity("UsedSize");
            memorydata.setSubentity("PhysicalMemory");
            memorydata.setRestype("static");
            memorydata.setUnit("M");
            memorydata.setThevalue(
            // Float.toString(PhysicalMemCap*(1-usedPhyPagesSize/100)));
                    Float.toString(PhysicalMemCap - freePhysicalMemory));
            memoryVector.addElement(memorydata);
            // �ڴ�ʹ����
            memorydata = new Memorycollectdata();
            memorydata.setIpaddress(ipaddress);
            memorydata.setCollecttime(date);
            memorydata.setCategory("Memory");
            memorydata.setEntity("Utilization");
            memorydata.setSubentity("PhysicalMemory");
            memorydata.setRestype("dynamic");
            memorydata.setUnit("%");
            memorydata.setThevalue(Math.round(PhysicalMemUtilization) + "");
            // memorydata.setThevalue(Math.round(usedPhyPagesSize)+"");
            memoryVector.addElement(memorydata);

            Vector phymemV = new Vector();
            phymemV.add(memorydata);
            Hashtable collectHash = new Hashtable();
            collectHash.put("physicalmem", phymemV);
            // �������ڴ�ֵ���и澯���
            try {
                AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                List list = alarmIndicatorsUtil
                        .getAlarmInicatorsThresholdForNode(String.valueOf(host
                                .getId()), AlarmConstant.TYPE_HOST, "aix",
                                "physicalmemory");
                String value = "0";
                if (memoryVector != null && memoryVector.size() > 0) {
                    for (int i = 0; i < memoryVector.size(); i++) {
                        Memorycollectdata memorydata1 = (Memorycollectdata) memoryVector
                                .get(i);
                        // SysLogger.info("windows=========="+memorydata.getSubentity()+"==="+memorydata.getEntity()+"==="+memorydata.getThevalue());
                        if ("PhysicalMemory".equalsIgnoreCase(memorydata1
                                .getSubentity())
                                && memorydata1.getEntity().equalsIgnoreCase(
                                        "Utilization")) {
                            value = memorydata1.getThevalue();
                            break;
                        }
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode) list
                            .get(i);
                    // �������ڴ�ֵ���и澯���
                    CheckEventUtil checkutil = new CheckEventUtil();
                    checkutil.checkEvent(host, alarmIndicatorsnode, value);
                    // }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // �����ڴ��ܴ�С�仯�澯���
            CheckEventUtil checkutil = new CheckEventUtil();
            checkutil.hardwareInfo(host, "PhysicalMemory", Float
                    .toString(PhysicalMemCap)
                    + "M");
        }
        if (SwapMemCap > 0) {
            // Swap
            memorydata = new Memorycollectdata();
            memorydata.setIpaddress(ipaddress);
            memorydata.setCollecttime(date);
            memorydata.setCategory("Memory");
            memorydata.setEntity("Capability");
            memorydata.setSubentity("SwapMemory");
            memorydata.setRestype("static");
            memorydata.setUnit("M");
            // һ��BLOCK��512byte
            // ��������ʹ�ô�С
            memorydata.setThevalue(Math.round(SwapMemCap / 1024) + "");
            memoryVector.addElement(memorydata);
            memorydata = new Memorycollectdata();
            memorydata.setIpaddress(ipaddress);
            memorydata.setCollecttime(date);
            memorydata.setCategory("Memory");
            memorydata.setEntity("UsedSize");
            memorydata.setSubentity("SwapMemory");
            memorydata.setRestype("static");
            memorydata.setUnit("M");
            memorydata.setThevalue(Math.round(usedSwapMemory / 1024) + "");
            memoryVector.addElement(memorydata);
            // ��������ʹ����
            memorydata = new Memorycollectdata();
            memorydata.setIpaddress(ipaddress);
            memorydata.setCollecttime(date);
            memorydata.setCategory("Memory");
            memorydata.setEntity("Utilization");
            memorydata.setSubentity("SwapMemory");
            memorydata.setRestype("dynamic");
            memorydata.setUnit("%");
            memorydata.setThevalue(Math
                    .round(usedSwapMemory * 100 / SwapMemCap)
                    + "");
            memoryVector.addElement(memorydata);

            Vector swapmemV = new Vector();
            swapmemV.add(memorydata);
            Hashtable collectHash = new Hashtable();
            collectHash.put("swapmem", swapmemV);
            // �Խ����ڴ�ֵ���и澯���
            try {
                AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                List list = alarmIndicatorsUtil
                        .getAlarmInicatorsThresholdForNode(String.valueOf(host
                                .getId()), AlarmConstant.TYPE_HOST, "aix",
                                "swapmemory");
                String value = "0";
                if (memoryVector != null && memoryVector.size() > 0) {
                    for (int i = 0; i < memoryVector.size(); i++) {
                        Memorycollectdata memorydata1 = (Memorycollectdata) memoryVector
                                .get(i);
                        if ("SwapMemory".equalsIgnoreCase(memorydata1
                                .getSubentity()) && memorydata1.getEntity().equalsIgnoreCase(
                                "Utilization")) {
                            value = memorydata1.getThevalue();
                        }
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode) list
                            .get(i);
                    // �Խ����ڴ�ֵ���и澯���
                    CheckEventUtil checkutil = new CheckEventUtil();
                    checkutil.checkEvent(host, alarmIndicatorsnode, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //		
        // //----------------����memory����--���������---------------------
        // String memperfContent = "";
        // tmpPt =
        // Pattern.compile("(cmdbegin:memory)(.*)(cmdbegin:process)",Pattern.DOTALL);
        // mr = tmpPt.matcher(fileContent.toString());
        // if(mr.find())
        // {
        // memperfContent = mr.group(2);
        // }
        // String[] memperfLineArr = memperfContent.split("\n");
        // List memperflist = new ArrayList();
        // //Vector memoryVector=new Vector();
        // //Memorycollectdata memorydata=null;
        // Hashtable<String,String> memperfhash = new
        // Hashtable<String,String>();
        // for(int i=0; i<memperfLineArr.length;i++){
        // diskperf_tmpData = memperfLineArr[i].trim().split("\\s++");
        // if(diskperf_tmpData != null && diskperf_tmpData.length>=4){
        // if(diskperf_tmpData[0].trim().equalsIgnoreCase("Mem:")){
        // memperfhash.put("total", diskperf_tmpData[1].trim());
        // memperfhash.put("used", diskperf_tmpData[2].trim());
        // memperfhash.put("free", diskperf_tmpData[3].trim());
        // memperfhash.put("shared", diskperf_tmpData[4].trim());
        // memperfhash.put("buffers", diskperf_tmpData[5].trim());
        // memperfhash.put("cached", diskperf_tmpData[6].trim());
        // memperflist.add(memperfhash);
        // memperfhash = new Hashtable();
        // //Memory
        // float PhysicalMemUtilization =100-
        // Float.parseFloat(diskperf_tmpData[3])* 100/
        // Float.parseFloat(diskperf_tmpData[1]);
        // memorydata=new Memorycollectdata();
        // memorydata.setIpaddress(ipaddress);
        // memorydata.setCollecttime(date);
        // memorydata.setCategory("Memory");
        // memorydata.setEntity("Capability");
        // memorydata.setSubentity("PhysicalMemory");
        // memorydata.setRestype("static");
        // memorydata.setUnit("M");
        // memorydata.setThevalue(
        // Integer.toString(Integer.parseInt(diskperf_tmpData[1]) / 1024));
        // memoryVector.addElement(memorydata);
        //		  			
        // memorydata=new Memorycollectdata();
        // memorydata.setIpaddress(ipaddress);
        // memorydata.setCollecttime(date);
        // memorydata.setCategory("Memory");
        // memorydata.setEntity("UsedSize");
        // memorydata.setSubentity("PhysicalMemory");
        // memorydata.setRestype("static");
        // memorydata.setUnit("M");
        // memorydata.setThevalue(
        // Integer.toString(Integer.parseInt(diskperf_tmpData[2]) / 1024));
        // memoryVector.addElement(memorydata);
        //		  			
        // memorydata=new Memorycollectdata();
        // memorydata.setIpaddress(ipaddress);
        // memorydata.setCollecttime(date);
        // memorydata.setCategory("Memory");
        // memorydata.setEntity("Utilization");
        // memorydata.setSubentity("PhysicalMemory");
        // memorydata.setRestype("dynamic");
        // memorydata.setUnit("%");
        // memorydata.setThevalue(Math.round(PhysicalMemUtilization)+"");
        // memoryVector.addElement(memorydata);
        // }else if(diskperf_tmpData[0].trim().equalsIgnoreCase("Swap:")){
        // memperfhash.put("total", diskperf_tmpData[1].trim());
        // memperfhash.put("used", diskperf_tmpData[2].trim());
        // memperfhash.put("free", diskperf_tmpData[3].trim());
        // memperflist.add(memperfhash);
        // memperfhash = new Hashtable();
        // //Swap
        // memorydata=new Memorycollectdata();
        // memorydata.setIpaddress(ipaddress);
        // memorydata.setCollecttime(date);
        // memorydata.setCategory("Memory");
        // memorydata.setEntity("Capability");
        // memorydata.setSubentity("SwapMemory");
        // memorydata.setRestype("static");
        // memorydata.setUnit("M");
        // memorydata.setThevalue(Integer.toString(Integer.parseInt(diskperf_tmpData[1])
        // / 1024));
        // memoryVector.addElement(memorydata);
        // memorydata=new Memorycollectdata();
        // memorydata.setIpaddress(ipaddress);
        // memorydata.setCollecttime(date);
        // memorydata.setCategory("Memory");
        // memorydata.setEntity("UsedSize");
        // memorydata.setSubentity("SwapMemory");
        // memorydata.setRestype("static");
        // memorydata.setUnit("M");
        // memorydata.setThevalue(
        // Integer.toString(Integer.parseInt(diskperf_tmpData[2]) / 1024));
        // memoryVector.addElement(memorydata);
        // float SwapMemUtilization =(Integer.parseInt(diskperf_tmpData[2]))*
        // 100/Integer.parseInt(diskperf_tmpData[1]);
        //					
        // memorydata=new Memorycollectdata();
        // memorydata.setIpaddress(ipaddress);
        // memorydata.setCollecttime(date);
        // memorydata.setCategory("Memory");
        // memorydata.setEntity("Utilization");
        // memorydata.setSubentity("SwapMemory");
        // memorydata.setRestype("dynamic");
        // memorydata.setUnit("%");
        // memorydata.setThevalue(Math.round(SwapMemUtilization)+"");
        // memoryVector.addElement(memorydata);
        // }
        // }
        // }

        // ----------------����process����--���������---------------------
        String processContent = "";
        tmpPt = Pattern.compile("(cmdbegin:process)(.*)(cmdbegin:cpu)",
                Pattern.DOTALL);
        mr = tmpPt.matcher(fileContent.toString());
        if (mr.find()) {
            processContent = mr.group(2);
        }
        List procslist = new ArrayList();
        ProcsDao procsdaor = new ProcsDao();
        try {
            procslist = procsdaor.loadByIp(ipaddress);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            procsdaor.close();
        }
        List procs_list = new ArrayList();
        Hashtable procshash = new Hashtable();
        Vector procsV = new Vector();
        if (procslist != null && procslist.size() > 0) {
            for (int i = 0; i < procslist.size(); i++) {
                Procs procs = (Procs) procslist.get(i);
                procshash.put(procs.getProcname(), procs);
                procsV.add(procs.getProcname());
            }
        }
        String[] process_LineArr = processContent.split("\n");
        String[] processtmpData = null;
        float cpuusage = 0.0f;
        for (int i = 1; i < process_LineArr.length; i++) {
            processtmpData = process_LineArr[i].trim().split("\\s++");
            if ((processtmpData != null) && (processtmpData.length >= 11)) {

                String USER = processtmpData[0];// USER
                String pid = processtmpData[1];// pid
                if ("USER".equalsIgnoreCase(USER))
                    continue;
                String cmd = processtmpData[10];
                String vbstring8 = processtmpData[8];
                String vbstring5 = processtmpData[9];// cputime
                if (processtmpData.length > 11) {
                    cmd = processtmpData[11];
                    vbstring8 = processtmpData[8] + processtmpData[9];// STIME
                    vbstring5 = processtmpData[10];// cputime
                }
                String vbstring2 = "Ӧ�ó���";
                String vbstring3 = "";
                String vbstring4 = processtmpData[4];// memsize
                if (vbstring4 == null)
                    vbstring4 = "0";
                String vbstring6 = processtmpData[3];// %mem
                String vbstring7 = processtmpData[2];// %CPU
                String vbstring9 = processtmpData[7];// STAT
                if ("Z".equals(vbstring9)) {
                    vbstring3 = "��ʬ����";
                } else {
                    vbstring3 = "��������";
                }
                processdata = new Processcollectdata();
                processdata.setIpaddress(ipaddress);
                processdata.setCollecttime(date);
                processdata.setCategory("Process");
                processdata.setEntity("process_id");
                processdata.setSubentity(pid);
                processdata.setRestype("dynamic");
                processdata.setUnit(" ");
                processdata.setThevalue(pid);
                processVector.addElement(processdata);

                processdata = new Processcollectdata();
                processdata.setIpaddress(ipaddress);
                processdata.setCollecttime(date);
                processdata.setCategory("Process");
                processdata.setEntity("USER");
                processdata.setSubentity(pid);
                processdata.setRestype("dynamic");
                processdata.setUnit(" ");
                processdata.setThevalue(USER);
                processVector.addElement(processdata);

                processdata = new Processcollectdata();
                processdata.setIpaddress(ipaddress);
                processdata.setCollecttime(date);
                processdata.setCategory("Process");
                processdata.setEntity("MemoryUtilization");
                processdata.setSubentity(pid);
                processdata.setRestype("dynamic");
                processdata.setUnit("%");
                processdata.setThevalue(vbstring6);
                processVector.addElement(processdata);

                processdata = new Processcollectdata();
                processdata.setIpaddress(ipaddress);
                processdata.setCollecttime(date);
                processdata.setCategory("Process");
                processdata.setEntity("Memory");
                processdata.setSubentity(pid);
                processdata.setRestype("static");
                processdata.setUnit("K");
                processdata.setThevalue(vbstring4);
                processVector.addElement(processdata);

                processdata = new Processcollectdata();
                processdata.setIpaddress(ipaddress);
                processdata.setCollecttime(date);
                processdata.setCategory("Process");
                processdata.setEntity("Type");
                processdata.setSubentity(pid);
                processdata.setRestype("static");
                processdata.setUnit(" ");
                processdata.setThevalue(vbstring2);
                processVector.addElement(processdata);

                processdata = new Processcollectdata();
                processdata.setIpaddress(ipaddress);
                processdata.setCollecttime(date);
                processdata.setCategory("Process");
                processdata.setEntity("Status");
                processdata.setSubentity(pid);
                processdata.setRestype("static");
                processdata.setUnit(" ");
                processdata.setThevalue(vbstring3);
                processVector.addElement(processdata);

                processdata = new Processcollectdata();
                processdata.setIpaddress(ipaddress);
                processdata.setCollecttime(date);
                processdata.setCategory("Process");
                processdata.setEntity("Name");
                processdata.setSubentity(pid);
                processdata.setRestype("static");
                processdata.setUnit(" ");
                processdata.setThevalue(cmd);
                processVector.addElement(processdata);

                processdata = new Processcollectdata();
                processdata.setIpaddress(ipaddress);
                processdata.setCollecttime(date);
                processdata.setCategory("Process");
                processdata.setEntity("CpuTime");
                processdata.setSubentity(pid);
                processdata.setRestype("static");
                processdata.setUnit("��");
                processdata.setThevalue(vbstring5);
                processVector.addElement(processdata);

                processdata = new Processcollectdata();
                processdata.setIpaddress(ipaddress);
                processdata.setCollecttime(date);
                processdata.setCategory("Process");
                processdata.setEntity("StartTime");
                processdata.setSubentity(pid);
                processdata.setRestype("static");
                processdata.setUnit(" ");
                processdata.setThevalue(vbstring8);
                processVector.addElement(processdata);

                processdata = new Processcollectdata();
                processdata.setIpaddress(ipaddress);
                processdata.setCollecttime(date);
                processdata.setCategory("Process");
                processdata.setEntity("CpuUtilization");
                processdata.setSubentity(pid);
                processdata.setRestype("dynamic");
                processdata.setUnit("%");
                processdata.setThevalue(vbstring7);
                processVector.addElement(processdata);
                /*
                 * //�ж��Ƿ�����Ҫ���ӵĽ��̣���ȡ�õ��б���������ӽ��̣����Vector��ȥ�� if (procshash !=null &&
                 * procshash.size()>0){ if (procshash.containsKey(vbstring1)){
                 * procshash.remove(vbstring1); procsV.remove(vbstring1); } }
                 */

            }
        }

        systemdata = new Systemcollectdata();
        systemdata.setIpaddress(ipaddress);
        systemdata.setCollecttime(date);
        systemdata.setCategory("System");
        systemdata.setEntity("ProcessCount");
        systemdata.setSubentity("ProcessCount");
        systemdata.setRestype("static");
        systemdata.setUnit(" ");
        systemdata.setThevalue(process_LineArr.length + "");
        systemVector.addElement(systemdata);

        // ----------------����uname����--���������---------------------
        String unameContent = "";
        tmpPt = Pattern.compile("(cmdbegin:uname)(.*)(cmdbegin:service)",
                Pattern.DOTALL);
        mr = tmpPt.matcher(fileContent.toString());
        if (mr.find()) {
            unameContent = mr.group(2);
        }
        String[] unameLineArr = unameContent.split("\n");
        String[] uname_tmpData = null;
        for (int i = 0; i < unameLineArr.length; i++) {
            uname_tmpData = unameLineArr[i].split("\\s++");
            if (uname_tmpData.length == 2) {
                systemdata = new Systemcollectdata();
                systemdata.setIpaddress(ipaddress);
                systemdata.setCollecttime(date);
                systemdata.setCategory("System");
                systemdata.setEntity("operatSystem");
                systemdata.setSubentity("operatSystem");
                systemdata.setRestype("static");
                systemdata.setUnit(" ");
                systemdata.setThevalue(uname_tmpData[0]);
                systemVector.addElement(systemdata);

                systemdata = new Systemcollectdata();
                systemdata.setIpaddress(ipaddress);
                systemdata.setCollecttime(date);
                systemdata.setCategory("System");
                systemdata.setEntity("SysName");
                systemdata.setSubentity("SysName");
                systemdata.setRestype("static");
                systemdata.setUnit(" ");
                systemdata.setThevalue(uname_tmpData[1]);
                systemVector.addElement(systemdata);

            }
        }

        // ----------------����service����--���������---------------------
        List servicelist = new ArrayList();
        Hashtable service = new Hashtable();
        String serviceContent = "";
        tmpPt = Pattern.compile("(cmdbegin:service)(.*)(cmdbegin:usergroup)",
                Pattern.DOTALL);
        mr = tmpPt.matcher(fileContent.toString());
        if (mr.find()) {
            serviceContent = mr.group(2);
        }
        String[] serviceLineArr = serviceContent.split("\n");
        String[] service_tmpData = null;
        for (int i = 0; i < serviceLineArr.length; i++) {
            service_tmpData = serviceLineArr[i].trim().split("\\s++");
            if (service_tmpData != null && service_tmpData.length >= 3) {
                if ("Subsystem".equalsIgnoreCase(service_tmpData[0]))
                    continue;
                if (service_tmpData.length == 4) {
                    // �����������,��PID
                    try {
                        service.put("name", service_tmpData[0]);
                        service.put("group", service_tmpData[1]);
                        service.put("pid", service_tmpData[2]);
                        service.put("status", service_tmpData[3]);
                        servicelist.add(service);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    service = new Hashtable();
                } else {
                    // δ���������û��PID
                    try {
                        service.put("name", service_tmpData[0]);
                        service.put("group", service_tmpData[1]);
                        service.put("status", service_tmpData[2]);
                        service.put("pid", "");
                        servicelist.add(service);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    service = new Hashtable();
                }

            } else if (service_tmpData != null && service_tmpData.length == 2) {
                // �����������,��PID
                try {
                    service.put("name", service_tmpData[0]);
                    service.put("group", "");
                    service.put("pid", "");
                    service.put("status", service_tmpData[1]);
                    servicelist.add(service);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                service = new Hashtable();
            }
            // if (service_tmpData.length==2){
            // systemdata=new Systemcollectdata();
            // systemdata.setIpaddress(ipaddress);
            // systemdata.setCollecttime(date);
            // systemdata.setCategory("System");
            // systemdata.setEntity("operatSystem");
            // systemdata.setSubentity("operatSystem");
            // systemdata.setRestype("static");
            // systemdata.setUnit(" ");
            // systemdata.setThevalue(service_tmpData[0]);
            // systemVector.addElement(systemdata);
            //				  
            // systemdata=new Systemcollectdata();
            // systemdata.setIpaddress(ipaddress);
            // systemdata.setCollecttime(date);
            // systemdata.setCategory("System");
            // systemdata.setEntity("SysName");
            // systemdata.setSubentity("SysName");
            // systemdata.setRestype("static");
            // systemdata.setUnit(" ");
            // systemdata.setThevalue(service_tmpData[1]);
            // systemVector.addElement(systemdata);
            //				
            // }
        }

        // ----------------����usergroup����--���������---------------------
        Hashtable usergrouphash = new Hashtable();
        String usergroupContent = "";
        tmpPt = Pattern.compile("(cmdbegin:usergroup)(.*)(cmdbegin:user\n)",
                Pattern.DOTALL);
        mr = tmpPt.matcher(fileContent.toString());
        if (mr.find()) {
            usergroupContent = mr.group(2);
        }
        String[] usergroupLineArr = usergroupContent.split("\n");
        String[] usergroup_tmpData = null;
        for (int i = 0; i < usergroupLineArr.length; i++) {
            usergroup_tmpData = usergroupLineArr[i].split(":");
            if (usergroup_tmpData.length >= 3) {
                usergrouphash.put((String) usergroup_tmpData[2],
                        usergroup_tmpData[0]);
            }
        }

        // ----------------����user����--���������---------------------
        String userContent = "";
        tmpPt = Pattern.compile("(cmdbegin:user\n)(.*)(cmdbegin:date)",
                Pattern.DOTALL);
        mr = tmpPt.matcher(fileContent.toString());
        if (mr.find()) {
            userContent = mr.group(2);
        }
        String[] userLineArr = userContent.split("\n");
        String[] user_tmpData = null;
        for (int i = 0; i < userLineArr.length; i++) {
            String[] result = userLineArr[i].trim().split("\\s++");
            if (result.length >= 4) {
                String userName = result[0];
                String groupStr = result[3];
                String[] groups = groupStr.split("=");
                String group = "";
                if (groups != null && groups.length == 2) {
                    group = groups[1];
                }
                userdata = new Usercollectdata();
                userdata.setIpaddress(ipaddress);
                userdata.setCollecttime(date);
                userdata.setCategory("User");
                userdata.setEntity("Sysuser");
                userdata.setSubentity(group);
                userdata.setRestype("static");
                userdata.setUnit(" ");
                userdata.setThevalue(userName);
                userVector.addElement(userdata);
            }

        }

        // ----------------����date����--���������---------------------
        String dateContent = "";
        tmpPt = Pattern.compile("(cmdbegin:date)(.*)(cmdbegin:uptime)",
                Pattern.DOTALL);
        mr = tmpPt.matcher(fileContent.toString());
        if (mr.find()) {
            dateContent = mr.group(2);
        }
        if (dateContent != null && dateContent.length() > 0) {
            systemdata = new Systemcollectdata();
            systemdata.setIpaddress(ipaddress);
            systemdata.setCollecttime(date);
            systemdata.setCategory("System");
            systemdata.setEntity("Systime");
            systemdata.setSubentity("Systime");
            systemdata.setRestype("static");
            systemdata.setUnit(" ");
            systemdata.setThevalue(dateContent.trim());
            systemVector.addElement(systemdata);

        }

        // ----------------����uptime����--���������---------------------
        String uptimeContent = "";
        tmpPt = Pattern.compile("(cmdbegin:uptime)(.*)(cmdbegin:errpt)",
                Pattern.DOTALL);
        mr = tmpPt.matcher(fileContent.toString());
        if (mr.find()) {
            uptimeContent = mr.group(2);
        }
        if (uptimeContent != null && uptimeContent.length() > 0) {
            systemdata = new Systemcollectdata();
            systemdata.setIpaddress(ipaddress);
            systemdata.setCollecttime(date);
            systemdata.setCategory("System");
            systemdata.setEntity("SysUptime");
            systemdata.setSubentity("SysUptime");
            systemdata.setRestype("static");
            systemdata.setUnit(" ");
            systemdata.setThevalue(uptimeContent.trim());
            systemVector.addElement(systemdata);
        }

        String errptlogContent = "";
        tmpPt = Pattern.compile("(cmdbegin:errpt)(.*)(cmdbegin:volume)",
                Pattern.DOTALL);
        mr = tmpPt.matcher(fileContent.toString());
        if (mr.find()) {
            errptlogContent = mr.group(2);
            ReadErrptlog readErrptlog = new ReadErrptlog();
            List list = null;
            try {
                list = readErrptlog.praseErrptlog(errptlogContent);
                if (list == null) {
                    list = new ArrayList();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // ----------------����volume����--���������---------------------
        String volumeContent = "";
        String volumeLabel;
        List volumelist = new ArrayList();
        tmpPt = Pattern.compile("(cmdbegin:volume)(.*)(cmdbegin:route)",
                Pattern.DOTALL);
        mr = tmpPt.matcher(fileContent.toString());
        if (mr.find()) {
            volumeContent = mr.group(2);
        }
        String[] volumeLineArr = volumeContent.split("\n");
        String[] volumetmpData = null;
        for (int i = 1; i < volumeLineArr.length; i++) {

            volumetmpData = volumeLineArr[i].split("\\s++");
            if ((volumetmpData != null)
                    && (volumetmpData.length == 4 || volumetmpData.length == 3)) {
                Hashtable volumeHash = new Hashtable();
                volumeHash.put("disk", volumetmpData[0]);
                volumeHash.put("pvid", volumetmpData[1]);
                volumeHash.put("vg", volumetmpData[2]);
                if (volumetmpData.length == 4) {
                    volumeHash.put("status", volumetmpData[3]);
                } else {
                    volumeHash.put("status", "-");
                }

                volumeVector.addElement(volumeHash);
            }
        }

        // ----------------����·������--���������---------------------
        String routeContent = "";
        String routeLabel;
        List routelist = new ArrayList();
        tmpPt = Pattern.compile("(cmdbegin:route)(.*)(cmdbegin:end)",
                Pattern.DOTALL);
        mr = tmpPt.matcher(fileContent.toString());
        if (mr.find()) {
            routeContent = mr.group(2);
        }
        String[] routeLineArr = routeContent.split("\n");
        String[] routetmpData = null;
        for (int i = 1; i < routeLineArr.length; i++) {
            routeList.add(routeLineArr[i]);
            routetmpData = routeLineArr[i].split("\\s++");
            if ((volumetmpData != null)
                    && (volumetmpData.length == 4 || volumetmpData.length == 3)) {
                Hashtable volumeHash = new Hashtable();
                volumeHash.put("disk", volumetmpData[0]);
                volumeHash.put("pvid", volumetmpData[1]);
                volumeHash.put("vg", volumetmpData[2]);
                if (volumetmpData.length == 4) {
                    volumeHash.put("status", volumetmpData[3]);
                } else {
                    volumeHash.put("status", "-");
                }

                volumeVector.addElement(volumeHash);
            }
        }

        try {
            // deleteFile(ipaddress);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (diskVector != null && diskVector.size() > 0) {// ����ʹ����
            returnHash.put("disk", diskVector);

            // �Ѳɼ��������sql
            HostdiskResultosql tosql = new HostdiskResultosql();
            tosql.CreateResultTosql(returnHash, host.getIpAddress());

            HostDatatempDiskRttosql temptosql = new HostDatatempDiskRttosql();
            temptosql.CreateResultTosql(returnHash, host);

        }
        if (cpuVector != null && cpuVector.size() > 0) {// cpu
            returnHash.put("cpu", cpuVector);

            // HostcpuResultTosql restosql=new HostcpuResultTosql();
            // restosql.CreateResultTosql(returnHash, host.getIpAddress());
            // �ѽ��ת����sql

            NetHostDatatempCpuRTosql totempsql = new NetHostDatatempCpuRTosql();
            totempsql.CreateResultTosql(returnHash, host);
            totempsql = null;

        }
        if (memoryVector != null && memoryVector.size() > 0) {
            returnHash.put("memory", memoryVector);
            // �Ѳɼ��������sql
            HostPhysicalMemoryResulttosql tosql = new HostPhysicalMemoryResulttosql();
            tosql.CreateResultTosql(returnHash, host.getIpAddress());
            NetHostMemoryRtsql totempsql = new NetHostMemoryRtsql();
            totempsql.CreateResultTosql(returnHash, host);

        }
        if (userVector != null && userVector.size() > 0) {
            returnHash.put("user", userVector);

            HostDatatempUserRtosql tosql = new HostDatatempUserRtosql();
            tosql.CreateResultTosql(returnHash, host);
        }
        if (processVector != null && processVector.size() > 0) {
            returnHash.put("process", processVector);

            // �ѽ������sql
            HostDatatempProcessRtTosql temptosql = new HostDatatempProcessRtTosql();
            temptosql.CreateResultTosql(returnHash, host);
        }
        if (systemVector != null && systemVector.size() > 0) {// ϵͳ��Ϣ
            returnHash.put("system", systemVector);
            NetHostDatatempSystemRttosql tosql = new NetHostDatatempSystemRttosql();
            tosql.CreateResultTosql(returnHash, host);

        }
        if (nodeconfig != null) {
            returnHash.put("nodeconfig", nodeconfig);

            HostDatatempNodeconfRtosql tosql = new HostDatatempNodeconfRtosql();
            tosql.CreateResultTosql(returnHash, host);

        }
        if (iflist != null && iflist.size() > 0) {
            returnHash.put("iflist", iflist);
            HostDatatempiflistRtosql tosql = new HostDatatempiflistRtosql();
            tosql.CreateResultTosql(returnHash, host);

        }
        if (utilhdxVector != null && utilhdxVector.size() > 0) {
            returnHash.put("utilhdx", utilhdxVector);
            HostDatatemputilhdxRtosql tosql = new HostDatatemputilhdxRtosql();
            tosql.CreateResultTosql(returnHash, host);
        }

        if (interfaceVector != null && interfaceVector.size() > 0) {
            returnHash.put("interface", interfaceVector);
            HostDatatempinterfaceRtosql tosql = new HostDatatempinterfaceRtosql();
            tosql.CreateResultTosql(returnHash, host);

        }
        if (alldiskperf != null && alldiskperf.size() > 0) {
            returnHash.put("alldiskperf", alldiskperf);
            HostDatatempnDiskperfRtosql tosql = new HostDatatempnDiskperfRtosql();
            tosql.CreateResultTosql(returnHash, host);

        }
        if (alldiskio != null && alldiskio.size() > 0) {
            returnHash.put("alldiskio", alldiskio);
            HostDatatempDiskPeriofRtosql tosql = new HostDatatempDiskPeriofRtosql();
            tosql.CreateResultTosql(returnHash, host);
        }

        if (cpuconfiglist != null && cpuconfiglist.size() > 0) {
            returnHash.put("cpuconfiglist", cpuconfiglist);
            HostDatatempCpuconfiRtosql tosql = new HostDatatempCpuconfiRtosql();
            tosql.CreateResultTosql(returnHash, host);
        }
        if (netmedialist != null && netmedialist.size() > 0) {
            returnHash.put("netmedialist", netmedialist);
        }
        if (servicelist != null && servicelist.size() > 0) {
            returnHash.put("servicelist", servicelist);
            // ��sql����sql
            HostDatatempserciceRttosql totempsql = new HostDatatempserciceRttosql();
            totempsql.CreateResultLinuxTosql(returnHash, host);

        }
        if (cpuperflist != null && cpuperflist.size() > 0) {
            returnHash.put("cpuperflist", cpuperflist);

            HostcpuResultTosql rtosql = new HostcpuResultTosql();
            rtosql.CreateLinuxResultTosql(returnHash, host.getIpAddress());

            HostDatatempCpuperRtosql tmptosql = new HostDatatempCpuperRtosql();
            tmptosql.CreateResultTosql(returnHash, host);

        }
        if (pagehash != null && pagehash.size() > 0) {
            returnHash.put("pagehash", pagehash);

            HostDatatempPageRtosql tosql = new HostDatatempPageRtosql();
            tosql.CreateResultTosql(returnHash, host);
        }
        if (paginghash != null && paginghash.size() > 0) {

            returnHash.put("paginghash", paginghash);

            HostDatatempPagingRtosql tosql = new HostDatatempPagingRtosql();
            tosql.CreateResultTosql(returnHash, host);

            HostPagingResultTosql Rtosql = new HostPagingResultTosql();
            Rtosql.CreateResultTosql(returnHash, host.getIpAddress());

        }
        if (errptlogVector != null && errptlogVector.size() > 0) {

            returnHash.put("errptlog", errptlogVector);
            HostDatatempErrptRtosql tosql = new HostDatatempErrptRtosql();
            tosql.CreateResultTosql(returnHash, host);

        }
        if (volumeVector != null && volumeVector.size() > 0) {
            returnHash.put("volume", volumeVector);

            HostDatatempVolumeRtosql tosql = new HostDatatempVolumeRtosql();
            tosql.CreateResultTosql(returnHash, host);

        }
        if (routeList != null && routeList.size() > 0) {
            returnHash.put("routelist", routeList);

            HostDatatempRuteRtosql tosql = new HostDatatempRuteRtosql();
            tosql.CreateResultTosql(returnHash, host);

        }
        returnHash.put("collecttime", collecttime);

        HostDatatempCollecttimeRtosql tosql = new HostDatatempCollecttimeRtosql();
        tosql.CreateResultTosql(returnHash, host);

        // if (! "1".equals(PollingEngine.getCollectwebflag())) {
        ShareData.getSharedata().put(host.getIpAddress(), returnHash);
        // System.out.println(returnHash.toString());
        // }
        return returnHash;
    }

    public String getMaxNum(String ipAddress) {
        String maxStr = null;
        File logFolder = new File(ResourceCenter.getInstance().getSysPath()
                + "linuxserver/");
        String[] fileList = logFolder.list();

        for (int i = 0; i < fileList.length; i++) // ��һ�����µ��ļ�
        {
            if (!fileList[i].startsWith(ipAddress))
                continue;

            return ipAddress;
        }
        return maxStr;
    }

    public void deleteFile(String ipAddress) {

        try {
            File delFile = new File(ResourceCenter.getInstance().getSysPath()
                    + "linuxserver/" + ipAddress + ".log");
            System.out.println("###��ʼɾ���ļ���" + delFile);
            // delFile.delete();
            System.out.println("###�ɹ�ɾ���ļ���" + delFile);
        } catch (Exception e) {
        }
    }

    public void copyFile(String ipAddress, String max) {
        try {
            String currenttime = SysUtil.getCurrentTime();
            currenttime = currenttime.replaceAll("-", "");
            currenttime = currenttime.replaceAll(" ", "");
            currenttime = currenttime.replaceAll(":", "");
            String ipdir = ipAddress.replaceAll("\\.", "-");
            String filename = ResourceCenter.getInstance().getSysPath()
                    + "/linuxserver_bak/" + ipdir;
            File file = new File(filename);
            if (!file.exists())
                file.mkdir();
            String cmd = "cmd   /c   copy   "
                    + ResourceCenter.getInstance().getSysPath()
                    + "linuxserver\\" + ipAddress + ".log" + " "
                    + ResourceCenter.getInstance().getSysPath()
                    + "linuxserver_bak\\" + ipdir + "\\" + ipAddress + "-"
                    + currenttime + ".log";
            // SysLogger.info(cmd);
            Process child = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void createSMS(Procs procs) {
    }

    public void createFileNotExistAlarm(String ipaddress) {
    }
}
