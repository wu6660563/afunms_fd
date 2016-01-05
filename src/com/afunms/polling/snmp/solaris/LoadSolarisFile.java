package com.afunms.polling.snmp.solaris;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.config.model.Nodeconfig;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.loader.HostLoader;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.Diskcollectdata;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.Memorycollectdata;
import com.afunms.polling.om.Systemcollectdata;
import com.afunms.polling.om.UtilHdx;
import com.afunms.polling.snmp.linux.LinuxByLogFile;
import com.afunms.polling.snmp.linux.LinuxCPUConfigByLogFile;
import com.afunms.polling.snmp.linux.LinuxCollectTimeByLogFile;
import com.afunms.polling.snmp.linux.LinuxCpuByLogFile;
import com.afunms.polling.snmp.linux.LinuxDateByLogFile;
import com.afunms.polling.snmp.linux.LinuxDiskByLogFile;
import com.afunms.polling.snmp.linux.LinuxDiskperfByLogFile;
import com.afunms.polling.snmp.linux.LinuxInterfaceByLogFile;
import com.afunms.polling.snmp.linux.LinuxMacByLogFile;
import com.afunms.polling.snmp.linux.LinuxMemoryByLogFile;
import com.afunms.polling.snmp.linux.LinuxProcessByLogFile;
import com.afunms.polling.snmp.linux.LinuxServiceByLogFile;
import com.afunms.polling.snmp.linux.LinuxUNameByLogFile;
import com.afunms.polling.snmp.linux.LinuxUserByLogFile;
import com.afunms.polling.snmp.linux.LinuxVersionByLogFile;
import com.gatherResulttosql.HostDatatempCollecttimeRtosql;
import com.gatherResulttosql.HostDatatempCpuconfiRtosql;
import com.gatherResulttosql.HostDatatempCpuperRtosql;
import com.gatherResulttosql.HostDatatempDiskRttosql;
import com.gatherResulttosql.HostDatatempNodeconfRtosql;
import com.gatherResulttosql.HostDatatempProcessRtTosql;
import com.gatherResulttosql.HostDatatempUserRtosql;
import com.gatherResulttosql.HostDatatempiflistRtosql;
import com.gatherResulttosql.HostDatatempinterfaceRtosql;
import com.gatherResulttosql.HostDatatempnDiskperfRtosql;
import com.gatherResulttosql.HostDatatempserciceRttosql;
import com.gatherResulttosql.HostDatatemputilhdxRtosql;
import com.gatherResulttosql.HostPhysicalMemoryResulttosql;
import com.gatherResulttosql.HostcpuResultTosql;
import com.gatherResulttosql.HostdiskResultosql;
import com.gatherResulttosql.NetHostDatatempCpuRTosql;
import com.gatherResulttosql.NetHostDatatempSystemRttosql;
import com.gatherResulttosql.NetHostMemoryRtsql;

/**
 * @author nielin
 */

public class LoadSolarisFile {

    private static SysLogger logger = SysLogger
                    .getLogger(LoadSolarisFile.class);

    private String logFileContent = null;

    public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
        HostLoader hostLoader = new HostLoader();
        Host host = hostLoader.loadOneByID(nodeGatherIndicators.getNodeid());
        if (host == null) {
            return null;
        }
        if (!host.isManaged()) {
            return null;
        }

        NodeUtil nodeUtil = new NodeUtil();
        NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(host);

        // 返回所有的数据的Hash
        Hashtable<String, Object> returnHash = new Hashtable<String, Object>();
        Vector<Memorycollectdata> memoryVector = new Vector<Memorycollectdata>();
        Vector<Systemcollectdata> systemVector = new Vector<Systemcollectdata>();
        Nodeconfig nodeconfig = new Nodeconfig();
        nodeconfig.setNodeid(host.getId());
        nodeconfig.setHostname(host.getAlias());

        ObjectValue objectValue = null;

        // Version
        objectValue = getVersionInfo(nodeDTO);
        nodeconfig.setCSDVersion(objectValue.getObjectValue().toString());

        // CPUConfig
        objectValue = getCPUConfigInfo(nodeDTO);
        Hashtable<String, Object> CPUConfigHashtable = (Hashtable<String, Object>) objectValue
                        .getObjectValue();
        nodeconfig.setNumberOfProcessors((String) CPUConfigHashtable
                        .get("procesorsnum"));
        returnHash.put("cpuconfiglist", CPUConfigHashtable.get("cpuconfiglist"));

        // Disk
        objectValue = getDiskInfo(nodeDTO);
        returnHash.put("disk", objectValue.getObjectValue());

        // Diskperf
        objectValue = getDiskperfInfo(nodeDTO);
        returnHash.put("alldiskperf", objectValue.getObjectValue());

//        // Netconfig
//        objectValue = getn(nodeDTO);
//        returnHash.put("netmedialist", objectValue.getObjectValue());

        // CPU
        objectValue = getCPUInfo(nodeDTO);
        Hashtable<String, Object> cpuHashtable = (Hashtable<String, Object>) objectValue
                        .getObjectValue();
        returnHash.put("cpu", cpuHashtable.get("cpuVector"));
        returnHash.put("cpuperflist", cpuHashtable.get("cpuperflist"));

        // Memory
        objectValue = getMemoryInfo(nodeDTO);
        memoryVector.addAll((Vector<Memorycollectdata>) objectValue
                        .getObjectValue());

        objectValue = getSwapInfo(nodeDTO);
        memoryVector.addAll((Vector<Memorycollectdata>) objectValue
                        .getObjectValue());

        returnHash.put("memory", memoryVector);

        // Process
        objectValue = getProcessInfo(nodeDTO);
        returnHash.put("process", objectValue.getObjectValue());

        // UName
        objectValue = getUNameInfo(nodeDTO);
        systemVector.addAll((Vector<Systemcollectdata>) objectValue
                        .getObjectValue());

        // User
        objectValue = getUserInfo(nodeDTO);
        returnHash.put("user", objectValue.getObjectValue());
        
        // Date
        objectValue = getDateInfo(nodeDTO);
        systemVector.add((Systemcollectdata) objectValue
                        .getObjectValue());
        
        // Uptime
        objectValue = getUptimeInfo(nodeDTO);
        systemVector.add((Systemcollectdata) objectValue
                        .getObjectValue());

        // Interface
        objectValue = getInterfaceInfo(nodeDTO);
        Hashtable<String, Object> interfaceHashtable = (Hashtable<String, Object>) objectValue.getObjectValue();
        Vector<UtilHdx> utilhdxVector = (Vector<UtilHdx>) interfaceHashtable.get("utilhdxVector");
        Vector<Interfacecollectdata> interfaceVector = (Vector<Interfacecollectdata>) interfaceHashtable.get("interfaceVector");
        List<Hashtable<String, String>> netflowlist = (List<Hashtable<String, String>>) interfaceHashtable.get("netflowlist");
        List<Hashtable<String, String>> netconfList = (List<Hashtable<String, String>>) interfaceHashtable.get("netconfList");

        nodeconfig.setMac((String)interfaceHashtable.get("allMac"));
        returnHash.put("interface", interfaceVector);
        returnHash.put("utilhdx", utilhdxVector);
        returnHash.put("netflowlist", netflowlist);
        returnHash.put("netconflist", netconfList);

        returnHash.put("system", systemVector);
        returnHash.put("nodeconfig", nodeconfig);

        try {
            logger.info("Solaris 设备:" + nodeDTO.getIpaddress() + "日志文件解析完成，开始判断告警！");
            checkevent(host, nodeDTO, returnHash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            logger.info("Solaris 设备:" + nodeDTO.getIpaddress() + " 判断告警完成，开始执行入库");
            executeSQL(host, returnHash);
            logger.info("Solaris 设备:" + nodeDTO.getIpaddress() + " 入库完成！结束！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnHash;
    }

    private ObjectValue getObjectValue(SolarisByLogFile solarisByLogFile,
                    NodeDTO nodeDTO) {
        if (logFileContent == null) {
            logFileContent = loadFileContent(nodeDTO.getIpaddress());
        }
        Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(
                        nodeDTO.getIpaddress());
        if (ipAllData == null) {
            ipAllData = new Hashtable();
        }
        solarisByLogFile.setIpAllData(ipAllData);
        solarisByLogFile.setLogFileContent(logFileContent);
        solarisByLogFile.setNodeDTO(nodeDTO);
        return solarisByLogFile.getObjectValue();
    }

    public ObjectValue getVersionInfo(NodeDTO nodeDTO) {
        SolarisByLogFile solarisByLogFile = new SolarisVersionByLogFile();
        return getObjectValue(solarisByLogFile, nodeDTO);
    }

    public ObjectValue getMemoryInfo(NodeDTO nodeDTO) {
        SolarisByLogFile solarisByLogFile = new SolarisMemoryByLogFile();
        return getObjectValue(solarisByLogFile, nodeDTO);
    }

    public ObjectValue getSwapInfo(NodeDTO nodeDTO) {
        SolarisByLogFile solarisByLogFile = new SolarisSwapByLogFile();
        return getObjectValue(solarisByLogFile, nodeDTO);
    }

    public ObjectValue getProcessInfo(NodeDTO nodeDTO) {
        SolarisByLogFile solarisByLogFile = new SolarisProcessByLogFile();
        return getObjectValue(solarisByLogFile, nodeDTO);
    }

    public ObjectValue getCPUInfo(NodeDTO nodeDTO) {
        SolarisByLogFile solarisByLogFile = new SolarisCpuByLogFile();
        return getObjectValue(solarisByLogFile, nodeDTO);
    }

    public ObjectValue getCPUConfigInfo(NodeDTO nodeDTO) {
        SolarisByLogFile solarisByLogFile = new SolarisCPUConfigByLogFile();
        return getObjectValue(solarisByLogFile, nodeDTO);
    }

    public ObjectValue getDiskInfo(NodeDTO nodeDTO) {
        SolarisByLogFile solarisByLogFile = new SolarisDiskByLogFile();
        return getObjectValue(solarisByLogFile, nodeDTO);
    }

    public ObjectValue getDiskperfInfo(NodeDTO nodeDTO) {
        SolarisByLogFile solarisByLogFile = new SolarisDiskperfByLogFile();
        return getObjectValue(solarisByLogFile, nodeDTO);
    }

//    public ObjectValue getNetConfigInfo(NodeDTO nodeDTO) {
//        SolarisByLogFile solarisByLogFile = new SolarisNetConfigByLogFile();
//        return getObjectValue(solarisByLogFile, nodeDTO);
//    }
//
//    public ObjectValue getNetperfInfo(NodeDTO nodeDTO) {
//        SolarisByLogFile solarisByLogFile = new SolarisNetperfByLogFile();
//        return getObjectValue(solarisByLogFile, nodeDTO);
//    }
//
//    public ObjectValue getNetaddrInfo(NodeDTO nodeDTO) {
//        SolarisByLogFile solarisByLogFile = new SolarisNetaddrByLogFile();
//        return getObjectValue(solarisByLogFile, nodeDTO);
//    }
//
//    public ObjectValue getServiceInfo(NodeDTO nodeDTO) {
//        SolarisByLogFile solarisByLogFile = new SolarisServiceByLogFile();
//        return getObjectValue(solarisByLogFile, nodeDTO);
//    }

    public ObjectValue getUNameInfo(NodeDTO nodeDTO) {
        SolarisByLogFile solarisByLogFile = new SolarisUNameByLogFile();
        return getObjectValue(solarisByLogFile, nodeDTO);
    }

    public ObjectValue getUserInfo(NodeDTO nodeDTO) {
        SolarisByLogFile solarisByLogFile = new SolarisUserByLogFile();
        return getObjectValue(solarisByLogFile, nodeDTO);
    }

    public ObjectValue getDateInfo(NodeDTO nodeDTO) {
        SolarisByLogFile solarisByLogFile = new SolarisDateByLogFile();
        return getObjectValue(solarisByLogFile, nodeDTO);
    }

    public ObjectValue getUptimeInfo(NodeDTO nodeDTO) {
        SolarisByLogFile solarisByLogFile = new SolarisUptimeByLogFile();
        return getObjectValue(solarisByLogFile, nodeDTO);
    }

    public ObjectValue getInterfaceInfo(NodeDTO nodeDTO) {
        SolarisByLogFile solarisByLogFile = new SolarisInterfaceByLogFile();
        return getObjectValue(solarisByLogFile, nodeDTO);
    }

    @SuppressWarnings("unchecked")
    private void checkevent(Host host, NodeDTO nodeDTO,
                    Hashtable<String, Object> returnHash) {
        String nodeid = nodeDTO.getNodeid();
        String type = nodeDTO.getType();
        String subtype = nodeDTO.getSubtype();
        AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
        List<AlarmIndicatorsNode> list = alarmIndicatorsUtil
                        .getAlarmInicatorsThresholdForNode(nodeid, type,
                                        subtype);
        CheckEventUtil checkutil = new CheckEventUtil();
        for (AlarmIndicatorsNode alarmIndicatorsNode : list) {
            if ("diskperc".equalsIgnoreCase(alarmIndicatorsNode.getName())
                            || "diskinc".equalsIgnoreCase(alarmIndicatorsNode
                                            .getName())) {
                Vector diskVector = (Vector) returnHash.get("disk");
                checkutil.checkDisk(host, diskVector, alarmIndicatorsNode);
                continue;
            } else if ("cpu".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
                Vector<CPUcollectdata> cpuVector = (Vector<CPUcollectdata>) returnHash
                                .get("cpu");
                String value = "";
                for (CPUcollectdata CPUcollectdata : cpuVector) {
                    if ("Utilization".equalsIgnoreCase(CPUcollectdata
                                    .getEntity())) {
                        value = CPUcollectdata.getThevalue();
                    }
                }
                checkutil.checkEvent(host, alarmIndicatorsNode, value);
            } else if ("physicalmemory".equalsIgnoreCase(alarmIndicatorsNode
                            .getName())) {
                Vector<Memorycollectdata> cpuVector = (Vector<Memorycollectdata>) returnHash
                                .get("memory");
                String value = "";
                for (Memorycollectdata memorycollectdata : cpuVector) {
                    if ("Utilization".equalsIgnoreCase(memorycollectdata
                                    .getEntity())
                                    && "PhysicalMemory"
                                                    .equalsIgnoreCase(memorycollectdata
                                                                    .getSubentity())) {
                        value = memorycollectdata.getThevalue();
                    }
                }
                checkutil.checkEvent(host, alarmIndicatorsNode, value);
            } else if ("swapmemory".equalsIgnoreCase(alarmIndicatorsNode
                            .getName())) {
                Vector<Memorycollectdata> cpuVector = (Vector<Memorycollectdata>) returnHash
                                .get("memory");
                String value = "";
                for (Memorycollectdata memorycollectdata : cpuVector) {
                    if ("Utilization".equalsIgnoreCase(memorycollectdata
                                    .getEntity())
                                    && "SwapMemory".equalsIgnoreCase(memorycollectdata
                                                    .getSubentity())) {
                        value = memorycollectdata.getThevalue();
                    }
                }
                checkutil.checkEvent(host, alarmIndicatorsNode, value);
            }
        }
    }

    private void executeSQL(Host host, Hashtable<String, Object> returnHash) {
        // 把采集结果生成sql

        // CPUConfig
        HostDatatempCpuconfiRtosql hostDatatempCpuconfiRtosql = new HostDatatempCpuconfiRtosql();
        hostDatatempCpuconfiRtosql.CreateResultTosql(returnHash, host);

        // 磁盘
        HostDatatempDiskRttosql temptosql = new HostDatatempDiskRttosql();
        temptosql.CreateResultTosql(returnHash, host);

        HostdiskResultosql tosql = new HostdiskResultosql();
        tosql.CreateResultTosql(returnHash, host.getIpAddress());

        // Diskperf
        HostDatatempnDiskperfRtosql hostDatatempnDiskperfRtosql = new HostDatatempnDiskperfRtosql();
        hostDatatempnDiskperfRtosql.CreateResultTosql(returnHash, host);

        // CPU
        NetHostDatatempCpuRTosql totempsql = new NetHostDatatempCpuRTosql();
        totempsql.CreateResultTosql(returnHash, host);

        HostcpuResultTosql hostcpuResultTosql = new HostcpuResultTosql();
        hostcpuResultTosql.CreateLinuxResultTosql(returnHash,
                        host.getIpAddress());

        HostDatatempCpuperRtosql hostDatatempCpuperRtosql = new HostDatatempCpuperRtosql();
        hostDatatempCpuperRtosql.CreateResultTosql(returnHash, host);

        // Memory
        HostPhysicalMemoryResulttosql hostPhysicalMemoryResulttosql = new HostPhysicalMemoryResulttosql();
        hostPhysicalMemoryResulttosql.CreateResultTosql(returnHash,
                        host.getIpAddress());

        NetHostMemoryRtsql netHostMemoryRtsql = new NetHostMemoryRtsql();
        netHostMemoryRtsql.CreateResultTosql(returnHash, host);
        
        // Process
        HostDatatempProcessRtTosql hostDatatempProcessRtTosql = new HostDatatempProcessRtTosql();
        hostDatatempProcessRtTosql.CreateResultTosql(returnHash, host);

        // USER
        HostDatatempUserRtosql hostDatatempUserRtosql = new HostDatatempUserRtosql();
        hostDatatempUserRtosql.CreateResultTosql(returnHash, host);

        

        // System
        NetHostDatatempSystemRttosql netHostDatatempSystemRttosql = new NetHostDatatempSystemRttosql();
        netHostDatatempSystemRttosql.CreateResultTosql(returnHash, host);

        // NodeConfig
        HostDatatempNodeconfRtosql hostDatatempNodeconfRtosql = new HostDatatempNodeconfRtosql();
        hostDatatempNodeconfRtosql.CreateResultTosql(returnHash, host);

        // Iflist
        HostDatatempiflistRtosql hostDatatempiflistRtosql = new HostDatatempiflistRtosql();
        hostDatatempiflistRtosql.CreateResultTosql(returnHash, host);

        // Utilhdx
        HostDatatemputilhdxRtosql hostDatatemputilhdxRtosql = new HostDatatemputilhdxRtosql();
        hostDatatemputilhdxRtosql.CreateResultTosql(returnHash, host);

        // Interface
        HostDatatempinterfaceRtosql hostDatatempinterfaceRtosql = new HostDatatempinterfaceRtosql();
        hostDatatempinterfaceRtosql.CreateResultTosql(returnHash, host);

        // Service
        HostDatatempserciceRttosql hostDatatempserciceRttosql = new HostDatatempserciceRttosql();
        hostDatatempserciceRttosql.CreateResultLinuxTosql(returnHash, host);

        // CollectTime
        HostDatatempCollecttimeRtosql hostDatatempCollecttimeRtosql = new HostDatatempCollecttimeRtosql();
        hostDatatempCollecttimeRtosql.CreateResultTosql(returnHash, host);
    }

    private String loadFileContent(String ipaddress) {
        StringBuffer fileContent = new StringBuffer();
        try {
            String filename = ResourceCenter.getInstance().getSysPath()
                            + "/linuxserver/" + ipaddress + ".log";
            logger.info("开始解析日志文件：" + filename);
            File file = new File(filename);
            if (!file.exists()) {
                // 文件不存在, 返回 null
                return null;
            }
            file = null;
            FileInputStream fis = new FileInputStream(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String strLine = null;
            // 读入文件内容
            while ((strLine = br.readLine()) != null) {
                fileContent.append(strLine + "\n");
            }
            isr.close();
            fis.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileContent.toString();
    }

    private String getMaxNum(String ipaddress) {
        String maxStr = null;
        File logFolder = new File(ResourceCenter.getInstance().getSysPath()
                        + "linuxserver/");
        String[] fileList = logFolder.list();
        for (int i = 0; i < fileList.length; i++) // 找一个最新的文件
        {
            if (!fileList[i].startsWith(ipaddress)) {
                continue;
            }
            return ipaddress;
        }
        return maxStr;
    }

    private void copyFile(String ipaddress, String max) {
        try {
            String currenttime = SysUtil.getCurrentTime();
            currenttime = currenttime.replaceAll("-", "");
            currenttime = currenttime.replaceAll(" ", "");
            currenttime = currenttime.replaceAll(":", "");
            String ipdir = ipaddress.replaceAll("\\.", "-");
            String filename = ResourceCenter.getInstance().getSysPath()
                            + "/linuxserver_bak/" + ipdir;
            File file = new File(filename);
            if (!file.exists())
                file.mkdir();
            String cmd = "cmd   /c   copy   "
                            + ResourceCenter.getInstance().getSysPath()
                            + "linuxserver\\" + ipaddress + ".log" + " "
                            + ResourceCenter.getInstance().getSysPath()
                            + "linuxserver_bak\\" + ipdir + "\\" + ipaddress
                            + "-" + currenttime + ".log";
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
