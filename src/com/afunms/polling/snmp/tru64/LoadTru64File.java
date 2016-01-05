/*
 * @(#)LoadHpunixFile.java     v1.01, Mar 26, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.tru64;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
import com.gatherResulttosql.HostDataimportProcessRtTosql;
import com.gatherResulttosql.HostDatatempDiskRttosql;
import com.gatherResulttosql.HostDatatempProcessRtTosql;
import com.gatherResulttosql.HostDatatempUserRtosql;
import com.gatherResulttosql.HostDatatempiflistRtosql;
import com.gatherResulttosql.HostDatatempinterfaceRtosql;
import com.gatherResulttosql.HostDatatemputilhdxRtosql;
import com.gatherResulttosql.HostPhysicalMemoryResulttosql;
import com.gatherResulttosql.HostcpuResultTosql;
import com.gatherResulttosql.HostdiskResultosql;
import com.gatherResulttosql.NetHostDatatempCpuRTosql;
import com.gatherResulttosql.NetHostDatatempSystemRttosql;
import com.gatherResulttosql.NetHostMemoryRtsql;
import com.gatherResulttosql.NetinterfaceResultTosql;

/**
 * 
 * ClassName: LoadTru64File.java
 * <p>
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Mar 26, 2013 4:12:16 PM
 */

public class LoadTru64File {

	private SysLogger logger = SysLogger.getLogger(LoadTru64File.class);

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

		Nodeconfig nodeconfig = new Nodeconfig();
		nodeconfig.setNodeid(host.getId());
		nodeconfig.setHostname(host.getAlias());

		Hashtable<String, Object> returnHash = new Hashtable<String, Object>();
		Vector<Systemcollectdata> systemVector = new Vector<Systemcollectdata>();
		ObjectValue objectValue = null;

		try {
			// uptime
			objectValue = getUptimeInfo(nodeDTO);
			systemVector.add((Systemcollectdata) objectValue.getObjectValue());

			// date
			objectValue = getDateInfo(nodeDTO);
			systemVector.add((Systemcollectdata) objectValue.getObjectValue());

			// netstat
			objectValue = getNetstatInfo(nodeDTO);
			Hashtable hash = (Hashtable) objectValue.getObjectValue();
			returnHash.put("iflist", hash.get("iflist"));
			returnHash.put("interface", hash.get("interface"));
			returnHash.put("utilhdx", hash.get("utilhdx"));
			returnHash.put("allutilhdx", hash.get("allutilhdxVector"));

			// user
			objectValue = getUserInfo(nodeDTO);
			returnHash.put("user", objectValue.getObjectValue());

			// uname
			objectValue = getUnameInfo(nodeDTO);
			systemVector.addAll((Vector<Systemcollectdata>) objectValue
					.getObjectValue());

			// process
			objectValue = getProcInfo(nodeDTO);
			Hashtable processHash = (Hashtable) objectValue.getObjectValue();
			Vector processVector = (Vector) processHash.get("processVector");
			// cpu
//			Vector cpuVector = (Vector) processHash.get("cpuVector");	//cpu改为vmstat取值2013/09/10
			
			Hashtable processCountHash = (Hashtable) processHash
					.get("processCountHash");
			returnHash.put("process", processVector);
			returnHash.put("importProcess", processCountHash);
//			returnHash.put("cpu", cpuVector);
			
			// memory
			objectValue = getMemoryInfo(nodeDTO);
			returnHash.put("memory", objectValue.getObjectValue());
			
			//cpu 2013/09/10
			objectValue = getCpuInfo(nodeDTO);
			returnHash.put("cpu", objectValue.getObjectValue());

			// disk
			objectValue = getDiskInfo(nodeDTO);
			returnHash.put("disk", objectValue.getObjectValue());

			// systemVector
			returnHash.put("system", systemVector);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("IP:"+host.getIpAddress(), e);
		}

		try {
			checkevent(host, nodeDTO, returnHash);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("IP:"+host.getIpAddress(), e);
		}
		try {
			executeSQL(host, returnHash);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("IP:"+host.getIpAddress(), e);
		}
		return null;
	}

	private ObjectValue getObjectValue(Tru64ByLogFile tru64ByLogFile,
			NodeDTO nodeDTO) {
		if (logFileContent == null) {
			logFileContent = loadFileContent(nodeDTO.getIpaddress());
		}
		Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(
				nodeDTO.getIpaddress());
		if (ipAllData == null) {
			ipAllData = new Hashtable();
		}
		tru64ByLogFile.setIpAllData(ipAllData);
		tru64ByLogFile.setLogFileContent(logFileContent);
		tru64ByLogFile.setNodeDTO(nodeDTO);
		return tru64ByLogFile.getObjectValue();
	}

	// uptime
	public ObjectValue getUptimeInfo(NodeDTO nodeDTO) {
		Tru64ByLogFile hpunixByLogFile = new Tru64UptimeByLogFile();
		return getObjectValue(hpunixByLogFile, nodeDTO);
	}

	// date
	public ObjectValue getDateInfo(NodeDTO nodeDTO) {
		Tru64ByLogFile hpunixByLogFile = new Tru64DateByLogFile();
		return getObjectValue(hpunixByLogFile, nodeDTO);
	}

	// netstat
	public ObjectValue getNetstatInfo(NodeDTO nodeDTO) {
		Tru64ByLogFile hpunixByLogFile = new Tru64NetstatByLogFile();
		return getObjectValue(hpunixByLogFile, nodeDTO);
	}

	// user
	public ObjectValue getUserInfo(NodeDTO nodeDTO) {
		Tru64ByLogFile hpunixByLogFile = new Tur64UserByLogFile();
		return getObjectValue(hpunixByLogFile, nodeDTO);
	}

	// uname
	public ObjectValue getUnameInfo(NodeDTO nodeDTO) {
		Tru64ByLogFile hpunixByLogFile = new Tru64UnameByLogFile();
		return getObjectValue(hpunixByLogFile, nodeDTO);
	}

	// process
	public ObjectValue getProcInfo(NodeDTO nodeDTO) {
		Tru64ByLogFile hpunixByLogFile = new Tru64ProcByLogFile();
		return getObjectValue(hpunixByLogFile, nodeDTO);
	}

	// memory
	 public ObjectValue getMemoryInfo(NodeDTO nodeDTO) {
		 Tru64ByLogFile hpunixByLogFile = new Tru64MemoryByLogFile();
		 return getObjectValue(hpunixByLogFile, nodeDTO);
	 }

	// disk
	public ObjectValue getDiskInfo(NodeDTO nodeDTO) {
		Tru64ByLogFile hpunixByLogFile = new Tru64DiskByLogFile();
		return getObjectValue(hpunixByLogFile, nodeDTO);
	}

	 // cpu
	 public ObjectValue getCpuInfo(NodeDTO nodeDTO) {
		 Tru64ByLogFile hpunixByLogFile = new Tru64CpuByLogFile();
		 return getObjectValue(hpunixByLogFile, nodeDTO);
	 }

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
			} else if("process".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
				Vector<Processcollectdata> processVector = (Vector<Processcollectdata>) returnHash
						.get("process");
				
				checkutil.createProcessGroupEventList(nodeDTO, processVector,
						alarmIndicatorsNode);
			}
		}

	}

	private String loadFileContent(String ipaddress) {
		StringBuffer fileContent = new StringBuffer();
		try {
			String filename = ResourceCenter.getInstance().getSysPath()
					+ "/linuxserver/" + ipaddress + ".log";
//			SysLogger.info("开始解析Tru64日志文件：" + filename);
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

	// 把采集结果生成sql，入库
	private void executeSQL(Host host, Hashtable<String, Object> returnHash) {
		// iflist
		HostDatatempiflistRtosql iflisttosql = new HostDatatempiflistRtosql();
		iflisttosql.CreateResultTosql(returnHash, host);

		// interface
		HostDatatempinterfaceRtosql interfacetosql = new HostDatatempinterfaceRtosql();
		interfacetosql.CreateResultTosql(returnHash, host);
		// Interface历史库
		NetinterfaceResultTosql tosql = new NetinterfaceResultTosql();
        tosql.CreateResultTosql(returnHash, host);
		// utilhdx
		HostDatatemputilhdxRtosql utilhdxtosql = new HostDatatemputilhdxRtosql();
		utilhdxtosql.CreateResultTosql(returnHash, host);

		// user
		HostDatatempUserRtosql usertosql = new HostDatatempUserRtosql();
		usertosql.CreateResultTosql(returnHash, host);

		// process
		HostDatatempProcessRtTosql processtosql = new HostDatatempProcessRtTosql();
		processtosql.CreateResultTosql(returnHash, host);
		// 关键进程入库
		HostDataimportProcessRtTosql importtosql = new HostDataimportProcessRtTosql();
		importtosql.CreateResultTosql(returnHash, host);

		// memory
		HostPhysicalMemoryResulttosql memorytosql = new HostPhysicalMemoryResulttosql();
		memorytosql.CreateResultTosql(returnHash, host.getIpAddress());
		NetHostMemoryRtsql totempsql = new NetHostMemoryRtsql();
		totempsql.CreateResultTosql(returnHash, host);

		// disk
		HostdiskResultosql disktosql = new HostdiskResultosql();
		disktosql.CreateResultTosql(returnHash, host.getIpAddress());
		HostDatatempDiskRttosql tempdisktosql = new HostDatatempDiskRttosql();
		tempdisktosql.CreateResultTosql(returnHash, host);

		// cpu
		HostcpuResultTosql cputosql = new HostcpuResultTosql();
		cputosql.CreateLinuxResultTosql(returnHash, host.getIpAddress());
		NetHostDatatempCpuRTosql tempcputosql = new NetHostDatatempCpuRTosql();
		tempcputosql.CreateResultTosql(returnHash, host);

		// system
		NetHostDatatempSystemRttosql netHostDatatempSystemRttosql = new NetHostDatatempSystemRttosql();
		netHostDatatempSystemRttosql.CreateResultTosql(returnHash, host);
	}

	public String getMaxNum(String ipaddress) {
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

	public void copyFile(String ipaddress, String max) {
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
