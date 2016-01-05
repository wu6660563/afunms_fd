/*
 * @(#)LoadHpunixFile.java     v1.01, Mar 26, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.hpunix;

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
import com.afunms.application.dao.ProcessGroupDao;
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

/**
 * 
 * ClassName: LoadHpunixFile.java
 * <p>
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Mar 26, 2013 4:12:16 PM
 */

public class LoadHpunixFile {

	private SysLogger logger = SysLogger.getLogger(LoadHpunixFile.class);

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
		Hashtable processCountHash = (Hashtable) processHash
				.get("processCountHash");
		returnHash.put("process", processVector);
		returnHash.put("importProcess", processCountHash);

		// memory
		objectValue = getMemoryInfo(nodeDTO);
		returnHash.put("memory", objectValue.getObjectValue());

		// disk
		objectValue = getDiskInfo(nodeDTO);
		returnHash.put("disk", objectValue.getObjectValue());

		// cpu
		objectValue = getCpuInfo(nodeDTO);
		returnHash.put("cpu", objectValue.getObjectValue());

		// systemVector
		returnHash.put("system", systemVector);

		try {
			logger.info("Hpunix 设备:" + nodeDTO.getIpaddress()
					+ "日志文件解析完成，开始判断告警！");
			checkevent(host, nodeDTO, returnHash);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			logger.info("Hpunix 设备:" + nodeDTO.getIpaddress()
					+ " 判断告警完成，开始执行入库");
			executeSQL(host, returnHash);
			logger.info("Hpunix 设备:" + nodeDTO.getIpaddress() + " 入库完成！结束！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private ObjectValue getObjectValue(HpunixByLogFile hpunixByLogFile,
			NodeDTO nodeDTO) {
		if (logFileContent == null) {
			logFileContent = loadFileContent(nodeDTO.getIpaddress());
		}
		Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(
				nodeDTO.getIpaddress());
		if (ipAllData == null) {
			ipAllData = new Hashtable();
		}
		hpunixByLogFile.setIpAllData(ipAllData);
		hpunixByLogFile.setLogFileContent(logFileContent);
		hpunixByLogFile.setNodeDTO(nodeDTO);
		return hpunixByLogFile.getObjectValue();
	}

	// uptime
	public ObjectValue getUptimeInfo(NodeDTO nodeDTO) {
		HpunixByLogFile hpunixByLogFile = new HpunixUptimeByLogFile();
		return getObjectValue(hpunixByLogFile, nodeDTO);
	}

	// date
	public ObjectValue getDateInfo(NodeDTO nodeDTO) {
		HpunixByLogFile hpunixByLogFile = new HpunixDateByLogFile();
		return getObjectValue(hpunixByLogFile, nodeDTO);
	}

	// netstat
	public ObjectValue getNetstatInfo(NodeDTO nodeDTO) {
		HpunixByLogFile hpunixByLogFile = new HpunixNetstatByLogFile();
		return getObjectValue(hpunixByLogFile, nodeDTO);
	}

	// user
	public ObjectValue getUserInfo(NodeDTO nodeDTO) {
		HpunixByLogFile hpunixByLogFile = new HpunixUserByLogFile();
		return getObjectValue(hpunixByLogFile, nodeDTO);
	}

	// uname
	public ObjectValue getUnameInfo(NodeDTO nodeDTO) {
		HpunixByLogFile hpunixByLogFile = new HpunixUnameByLogFile();
		return getObjectValue(hpunixByLogFile, nodeDTO);
	}

	// process
	public ObjectValue getProcInfo(NodeDTO nodeDTO) {
		HpunixByLogFile hpunixByLogFile = new HpunixProcByLogFile();
		return getObjectValue(hpunixByLogFile, nodeDTO);
	}

	// memory
	public ObjectValue getMemoryInfo(NodeDTO nodeDTO) {
		HpunixByLogFile hpunixByLogFile = new HpunixMemoryByLogFile();
		return getObjectValue(hpunixByLogFile, nodeDTO);
	}

	// disk
	public ObjectValue getDiskInfo(NodeDTO nodeDTO) {
		HpunixByLogFile hpunixByLogFile = new HpunixDiskByLogFile();
		return getObjectValue(hpunixByLogFile, nodeDTO);
	}

	// cpu
	public ObjectValue getCpuInfo(NodeDTO nodeDTO) {
		HpunixByLogFile hpunixByLogFile = new HpunixCpuByLogFile();
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
			SysLogger.info("开始解析Hpunix日志文件：" + filename);
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
