/*
 * @(#)LoadPTimeDBFile.java     v1.01, May 13, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.ptimedb;

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
import com.afunms.application.dao.ApplicationNodeDao;
import com.afunms.application.model.ApplicationNode;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.Pingcollectdata;
import com.gatherResulttosql.PTimeDBDatatempRttosql;
import com.gatherResulttosql.PTimeDBPingdatatempRtosql;

/**
 * 
 * ClassName:   LoadPTimeDBFile.java
 * <p>
 *
 * @author      吴品龙
 * @version     v1.01
 * @since       v1.01
 * @Date        May 13, 2013 5:29:47 PM
 */
public class LoadPTimeDBLogFile {
	
	private SysLogger logger = SysLogger.getLogger(LoadPTimeDBLogFile.class);

	private String logFileContent = null;
	
	public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
		ApplicationNode node = null;
		ApplicationNodeDao dao = new ApplicationNodeDao();
		try {
			node = (ApplicationNode)dao.findByID(nodeGatherIndicators.getNodeid());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		if (node == null) {
			return null;
		}
		if (!node.isManaged()) {
			return null;
		}

		NodeUtil nodeUtil = new NodeUtil();
		NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(node);

		Hashtable<String, Object> returnHash = new Hashtable<String, Object>();
		ObjectValue objectValue = null;
		
		objectValue = getPing(nodeDTO);
		returnHash.put("ping", objectValue.getObjectValue());
		try {
			logger.info("PTimeDB:" + nodeDTO.getIpaddress()
					+ "日志文件解析完成，开始判断告警并入库！");
			checkevent(node, nodeDTO, returnHash);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			executeSQL(node, returnHash);
			logger.info("PTimeDB:" + nodeDTO.getIpaddress() + " 入库完成！结束！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// ping
	public ObjectValue getPing(NodeDTO nodeDTO) {
		PTimeDBByLogFileAbstract ptimedbByLogFile = new PTimeDBPingByLogFile();
		return getObjectValue(ptimedbByLogFile, nodeDTO);
	}
	
	private ObjectValue getObjectValue(PTimeDBByLogFileAbstract ptimedbByLogFile,
			NodeDTO nodeDTO) {
		if (logFileContent == null) {
			logFileContent = loadFileContent(nodeDTO.getIpaddress());
		}
		Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(
				nodeDTO.getIpaddress());
		if (ipAllData == null) {
			ipAllData = new Hashtable();
		}
		ptimedbByLogFile.setIpAllData(ipAllData);
		ptimedbByLogFile.setLogFileContent(logFileContent);
		ptimedbByLogFile.setNodeDTO(nodeDTO);
		return ptimedbByLogFile.getObjectValue();
	}
	
	private void checkevent(ApplicationNode node, NodeDTO nodeDTO,
			Hashtable<String, Object> returnHash) {
		String nodeid = nodeDTO.getNodeid();
		String type = nodeDTO.getType();
		String subtype = nodeDTO.getSubtype();
		
		//ping
		if (returnHash != null && returnHash.size() > 0) {
            Vector pingvector = (Vector) returnHash.get("ping");
            if (pingvector != null) {
                AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                for (int i = 0; i < pingvector.size(); i++) {
                    Pingcollectdata pingdata = (Pingcollectdata) pingvector
                            .elementAt(i);
                    if (pingdata.getSubentity().equalsIgnoreCase(
                            "ConnectUtilization")) {
                        // 连通率进行判断
                        List list = alarmIndicatorsUtil
                                .getAlarmInicatorsThresholdForNode(nodeid, type, subtype);
                        for (int m = 0; m < list.size(); m++) {
                            AlarmIndicatorsNode _alarmIndicatorsNode = (AlarmIndicatorsNode) list
                                    .get(m);
                            if ("1".equals(_alarmIndicatorsNode.getEnabled())) {
                                if (_alarmIndicatorsNode.getName()
                                        .equalsIgnoreCase("ping")) {
                                    CheckEventUtil checkeventutil = new CheckEventUtil();
                                    checkeventutil.checkEvent(node,
                                            _alarmIndicatorsNode, pingdata
                                                    .getThevalue());
                                }
                            }
                        }

                    }
                }
            }
        }
		
	}
	
	private String loadFileContent(String ipaddress) {
		StringBuffer fileContent = new StringBuffer();
		try {
			String filename = ResourceCenter.getInstance().getSysPath()
					+ "/linuxserver/" + ipaddress + ".ptimelog";
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
	
	private void executeSQL(ApplicationNode host, Hashtable<String, Object> returnHash) {
		//ping
		PTimeDBPingdatatempRtosql totempsql = new PTimeDBPingdatatempRtosql();
        totempsql.CreateResultTosql(returnHash, host);
        PTimeDBDatatempRttosql pingtosql = new PTimeDBDatatempRttosql();
        pingtosql.CreateResultTosql(returnHash, host);
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
					+ currenttime + ".ptimelog";
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

