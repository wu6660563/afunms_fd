package com.afunms.polling.snmp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
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
import com.afunms.polling.om.Memorycollectdata;
import com.afunms.polling.om.Processcollectdata;
import com.afunms.polling.om.Systemcollectdata;
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
import com.gatherResulttosql.HostDataimportProcessRtTosql;
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
import com.gatherResulttosql.NetinterfaceResultTosql;

/**
 * @author nielin
 */

public class LoadLinuxFile {

	private static SysLogger logger = SysLogger.getLogger(LoadLinuxFile.class);

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

	@SuppressWarnings("unchecked")
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

		Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(
				host.getIpAddress());
		if (ipAllData == null) {
			ipAllData = new Hashtable();
		}

		// 返回所有的数据的Hash
		Hashtable<String, Object> returnHash = new Hashtable<String, Object>();
		Vector<Systemcollectdata> systemVector = new Vector<Systemcollectdata>();

		try {
			String ipaddress = nodeDTO.getIpaddress();
			String fileContent = loadFileContent(ipaddress);

			LinuxByLogFile linuxByLogFile = null;
			ObjectValue objectValue = null;

			// 采集时间
			linuxByLogFile = new LinuxCollectTimeByLogFile();
			objectValue = getObjectValue(linuxByLogFile, nodeDTO, ipAllData,
					fileContent);
			returnHash.put("collecttime", objectValue.getObjectValue());

			// Version
			linuxByLogFile = new LinuxVersionByLogFile();
			objectValue = getObjectValue(linuxByLogFile, nodeDTO, ipAllData,
					fileContent);
			String version = (String) objectValue.getObjectValue();

			// CPUConfig
			linuxByLogFile = new LinuxCPUConfigByLogFile();
			objectValue = getObjectValue(linuxByLogFile, nodeDTO, ipAllData,
					fileContent);
			Hashtable<String, Object> CPUConfigHashtable = (Hashtable<String, Object>) objectValue
					.getObjectValue();
			String procesorsnum = (String) CPUConfigHashtable.get("procesorsnum");
			returnHash
					.put("cpuconfiglist", CPUConfigHashtable.get("cpuconfiglist"));

			// 磁盘
			linuxByLogFile = new LinuxDiskByLogFile();
			objectValue = getObjectValue(linuxByLogFile, nodeDTO, ipAllData,
					fileContent);
			returnHash.put("disk", objectValue.getObjectValue());

			// 磁盘性能
			linuxByLogFile = new LinuxDiskperfByLogFile();
			objectValue = getObjectValue(linuxByLogFile, nodeDTO, ipAllData,
					fileContent);
			returnHash.put("alldiskperf", objectValue.getObjectValue());

			// CPU
			linuxByLogFile = new LinuxCpuByLogFile();
			objectValue = getObjectValue(linuxByLogFile, nodeDTO, ipAllData,
					fileContent);
			Hashtable<String, Object> cpuHashtable = (Hashtable<String, Object>) objectValue
					.getObjectValue();
			returnHash.put("cpuperflist", cpuHashtable.get("cpuperflist"));
			returnHash.put("cpu", cpuHashtable.get("cpuVector"));

			// Memory
			linuxByLogFile = new LinuxMemoryByLogFile();
			objectValue = getObjectValue(linuxByLogFile, nodeDTO, ipAllData,
					fileContent);
			returnHash.put("memory", objectValue.getObjectValue());

			// Process
			linuxByLogFile = new LinuxProcessByLogFile();
			objectValue = getObjectValue(linuxByLogFile, nodeDTO, ipAllData,
					fileContent);
			Hashtable procssHash = (Hashtable) objectValue.getObjectValue();
			returnHash.put("process", procssHash.get("processVector"));
			returnHash.put("importProcess", procssHash.get("processCountHash"));

			// MAC
			linuxByLogFile = new LinuxMacByLogFile();
			objectValue = getObjectValue(linuxByLogFile, nodeDTO, ipAllData,
					fileContent);
			Systemcollectdata macSystemcollectdata = (Systemcollectdata) objectValue
					.getObjectValue();
			systemVector.add(macSystemcollectdata);

			// Interface
			linuxByLogFile = new LinuxInterfaceByLogFile();
			objectValue = getObjectValue(linuxByLogFile, nodeDTO, ipAllData,
					fileContent);
			Hashtable<String, Object> interfaceHashtable = (Hashtable<String, Object>) objectValue
					.getObjectValue();
			returnHash.put("iflist", interfaceHashtable.get("iflist"));
			returnHash.put("interface", interfaceHashtable.get("interfaceVector"));
			returnHash.put("utilhdx", interfaceHashtable.get("utilhdxVector"));
			returnHash.put("allutilhdx", interfaceHashtable.get("allutilhdxVector"));

			// UName
			linuxByLogFile = new LinuxUNameByLogFile();
			objectValue = getObjectValue(linuxByLogFile, nodeDTO, ipAllData,
					fileContent);
			Vector<Systemcollectdata> unameVector = (Vector<Systemcollectdata>) objectValue
					.getObjectValue();
			systemVector.addAll(unameVector);

			// User
			linuxByLogFile = new LinuxUserByLogFile();
			objectValue = getObjectValue(linuxByLogFile, nodeDTO, ipAllData,
					fileContent);
			returnHash.put("user", objectValue.getObjectValue());

			// Date
			linuxByLogFile = new LinuxDateByLogFile();
			objectValue = getObjectValue(linuxByLogFile, nodeDTO, ipAllData,
					fileContent);
			systemVector.add((Systemcollectdata) objectValue.getObjectValue());

			// Service
			linuxByLogFile = new LinuxServiceByLogFile();
			objectValue = getObjectValue(linuxByLogFile, nodeDTO, ipAllData,
					fileContent);
			returnHash.put("servicelist", objectValue.getObjectValue());

			returnHash.put("system", systemVector);

			Nodeconfig nodeconfig = new Nodeconfig();
			nodeconfig.setNodeid(nodeDTO.getId());
			nodeconfig.setHostname(nodeDTO.getName());
			nodeconfig.setCSDVersion(version);
			nodeconfig.setNumberOfProcessors(procesorsnum);
			nodeconfig.setMac(macSystemcollectdata.getThevalue());
			returnHash.put("nodeconfig", nodeconfig);

			ShareData.getSharedata().put(host.getIpAddress(), returnHash);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("IP:"+host.getIpAddress(), e);
		}

		try {
//			logger.info("Linux 设备:" + ipaddress + "日志文件解析完成，开始判断告警！");
			checkevent(host, nodeDTO, returnHash);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("IP:"+host.getIpAddress(), e);
		}
		try {
//			logger.info("Linux 设备:" + ipaddress + " 判断告警完成，开始执行入库");
			executeSQL(host, returnHash);
//			logger.info("Linux 设备:" + ipaddress + " 入库完成！结束！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("IP:"+host.getIpAddress(), e);
		}
		return returnHash;
	}

	private ObjectValue getObjectValue(LinuxByLogFile linuxByLogFile,
			NodeDTO nodeDTO, Hashtable ipAllData, String logFileContent) {
		linuxByLogFile.setIpAllData(ipAllData);
		linuxByLogFile.setLogFileContent(logFileContent);
		linuxByLogFile.setNodeDTO(nodeDTO);
		return linuxByLogFile.getObjectValue();
	}

	@SuppressWarnings("unchecked")
	private void checkevent(Host host, NodeDTO nodeDTO,
			Hashtable<String, Object> returnHash) {
		String nodeid = nodeDTO.getNodeid();
		String type = nodeDTO.getType();
		String subtype = nodeDTO.getSubtype();
		AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
		List<AlarmIndicatorsNode> list = alarmIndicatorsUtil
				.getAlarmInicatorsThresholdForNode(nodeid, type, subtype);
		CheckEventUtil checkutil = new CheckEventUtil();
		for (AlarmIndicatorsNode alarmIndicatorsNode : list) {
			if ("diskperc".equalsIgnoreCase(alarmIndicatorsNode.getName())
					|| "diskinc"
							.equalsIgnoreCase(alarmIndicatorsNode.getName())) {
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
			}else if("process".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
				Vector<Processcollectdata> processVector = (Vector<Processcollectdata>) returnHash
						.get("process");
				
				checkutil.createProcessGroupEventList(nodeDTO, processVector,
						alarmIndicatorsNode);
			}
		}
	}

	private void executeSQL(Host host, Hashtable<String, Object> returnHash) {
		// 把采集结果生成sql

		// 磁盘
		HostdiskResultosql hostdiskResultosql = new HostdiskResultosql();
		hostdiskResultosql.CreateResultTosql(returnHash, host.getIpAddress());

		HostDatatempDiskRttosql hostDatatempDiskRttosql = new HostDatatempDiskRttosql();
		hostDatatempDiskRttosql.CreateResultTosql(returnHash, host);

		// CPU
		NetHostDatatempCpuRTosql netHostDatatempCpuRTosql = new NetHostDatatempCpuRTosql();
		netHostDatatempCpuRTosql.CreateResultTosql(returnHash, host);

		HostcpuResultTosql hostcpuResultTosql = new HostcpuResultTosql();
		hostcpuResultTosql.CreateLinuxResultTosql(returnHash, host
				.getIpAddress());

		HostDatatempCpuperRtosql hostDatatempCpuperRtosql = new HostDatatempCpuperRtosql();
		hostDatatempCpuperRtosql.CreateResultTosql(returnHash, host);

		// CPUConfig
		HostDatatempCpuconfiRtosql hostDatatempCpuconfiRtosql = new HostDatatempCpuconfiRtosql();
		hostDatatempCpuconfiRtosql.CreateResultTosql(returnHash, host);

		// Memory
		HostPhysicalMemoryResulttosql hostPhysicalMemoryResulttosql = new HostPhysicalMemoryResulttosql();
		hostPhysicalMemoryResulttosql.CreateResultTosql(returnHash, host
				.getIpAddress());

		NetHostMemoryRtsql netHostMemoryRtsql = new NetHostMemoryRtsql();
		netHostMemoryRtsql.CreateResultTosql(returnHash, host);

		// USER
		HostDatatempUserRtosql hostDatatempUserRtosql = new HostDatatempUserRtosql();
		hostDatatempUserRtosql.CreateResultTosql(returnHash, host);

		// Process
		HostDatatempProcessRtTosql hostDatatempProcessRtTosql = new HostDatatempProcessRtTosql();
		hostDatatempProcessRtTosql.CreateResultTosql(returnHash, host);
		// 关键进程入库
		HostDataimportProcessRtTosql importtosql = new HostDataimportProcessRtTosql();
		importtosql.CreateResultTosql(returnHash, host);

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

		// Interface历史库
		NetinterfaceResultTosql tosql = new NetinterfaceResultTosql();
        tosql.CreateResultTosql(returnHash, host);
        // Interface
		HostDatatempinterfaceRtosql hostDatatempinterfaceRtosql = new HostDatatempinterfaceRtosql();
		hostDatatempinterfaceRtosql.CreateResultTosql(returnHash, host);

		// Diskperf
		HostDatatempnDiskperfRtosql hostDatatempnDiskperfRtosql = new HostDatatempnDiskperfRtosql();
		hostDatatempnDiskperfRtosql.CreateResultTosql(returnHash, host);

		// Service
		HostDatatempserciceRttosql hostDatatempserciceRttosql = new HostDatatempserciceRttosql();
		hostDatatempserciceRttosql.CreateResultLinuxTosql(returnHash, host);

		// CollectTime
		HostDatatempCollecttimeRtosql hostDatatempCollecttimeRtosql = new HostDatatempCollecttimeRtosql();
		hostDatatempCollecttimeRtosql.CreateResultTosql(returnHash, host);
	}

	private String loadFileContent(String ipaddress) {
		StringBuffer fileContent = new StringBuffer();
		String filename = null;
		try {
			filename = ResourceCenter.getInstance().getSysPath()
					+ "/linuxserver/" + ipaddress + ".log";
//			logger.info("开始解析日志文件：" + filename);
			File file = new File(filename);
			if (!file.exists()) {
				// 文件不存在, 返回 null
				return fileContent.toString();
			}
			FileInputStream fis = new FileInputStream(filename);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String strLine = null;
			// 读入文件内容
			while ((strLine = br.readLine()) != null) {
				fileContent.append(strLine + "\n");
			}
			fis.close();
			isr.close();
			br.close();
		} catch (FileNotFoundException e1) {
			logger.info("没有这个日志文件！"+filename);
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
					+ "linuxserver_bak\\" + ipdir + "\\" + ipaddress + "-"
					+ currenttime + ".log";
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
