/*
 * @(#)SendPerformTask.java     v1.01, Jul 16, 2014
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import org.json.simple.JSONObject;


import com.afunms.common.base.BaseVo;
import com.afunms.data.service.NetDataService;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.Constant;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.initialize.ResourceCenter;
import com.afunms.middle.model.HeaderData;
import com.afunms.middle.util.SendDataConstant;
import com.afunms.temp.model.NodeTemp;

/**
 * ClassName:   SendPerformModelTask.java
 * <p> 性能运行数据定时task
 *
 * @author      吴品龙
 * @version     v1.01
 * @since       v1.01
 * @Date        Aug 5, 2014 11:13:22 AM
 * @mail		wupinlong@dhcc.com.cn
 */
public class SendPerformRunningTask extends TimerTask {
	
	private DecimalFormat df = new DecimalFormat("#.0");

	@Override
	public void run() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<HashMap<String, Object>> runningData = new LinkedList<HashMap<String, Object>>();
		
		//得到所有的主机、网络设备、防火墙
		NodeUtil nodeUtil = new NodeUtil();
		List<BaseVo> basevolist = new ArrayList<BaseVo>();
		// 暂时只推送网络设备、服务器、防火墙
		List<BaseVo> netList = nodeUtil.getNodeByTyeAndSubtype(
				Constant.TYPE_NET, Constant.ALL_SUBTYPE);
		basevolist.addAll(netList);
		List<BaseVo> hostList = nodeUtil.getNodeByTyeAndSubtype(
				Constant.TYPE_HOST, Constant.ALL_SUBTYPE);
		basevolist.addAll(hostList);
		List<BaseVo> firewallList = nodeUtil.getNodeByTyeAndSubtype(
				Constant.TYPE_FIREWALL, Constant.ALL_SUBTYPE);
		basevolist.addAll(firewallList);
		List<NodeDTO> dtoList = nodeUtil.conversionToNodeDTO(basevolist);
		
		try {
			if(dtoList != null && dtoList.size() > 0) {
				for (NodeDTO nodeDTO : dtoList) {
					HashMap<String, Object> modelHash = new HashMap<String, Object>();
					
					NetDataService dataService = new NetDataService(nodeDTO.getNodeid(), nodeDTO.getType(), nodeDTO.getSubtype());
					//Ping_Utilization Ping_ResponseTime
					List<NodeTemp> pingList = dataService.getPingInfo();
					modelHash.put("Ping_Utilization", 0.0);
					modelHash.put("Ping_ResponseTime", 0.0);
					if(pingList != null && pingList.size() > 0) {
						for (NodeTemp nodeTemp : pingList) {
							if("ConnectUtilization".equals(nodeTemp.getSindex())) {
								modelHash.put("Ping_Utilization", Double.parseDouble(nodeTemp.getThevalue()));
							}
							if("ResponseTime".equals(nodeTemp.getSindex())) {
								modelHash.put("Ping_ResponseTime", Double.parseDouble(nodeTemp.getThevalue()));
							}
						}
					}
					
					//CPU_Utilization
					String cpuValue = dataService.getCPUInfo();
					modelHash.put("CPU_Utilization", Double.parseDouble(df.format(Double.parseDouble(cpuValue))));
					
					//Memory_Utilization
					List<NodeTemp> memList = dataService.getMemoryInfo();
					modelHash.put("Memory_Utilization", 0.0);
					if(memList != null && memList.size() > 0) {
						for (NodeTemp nodeTemp2 : memList) {
							if("PhysicalMemory".equals(nodeTemp2.getSindex())) {
								modelHash.put("Memory_Utilization", Double.parseDouble(df.format(Double.parseDouble(nodeTemp2.getThevalue()))));
							}
						}
					}
					modelHash.put("gid", "");
					modelHash.put("group", "publicIndicators");
					Date date = new Date();
					modelHash.put("netgain_time", date.getTime());
					runningData.add(modelHash);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("body", runningData);
		
		HashMap<String, Object> headerMap = new HashMap<String, Object>();
		headerMap.put("systype", Integer.parseInt(SendDataConstant.HEADER_SYSTYPE));
		headerMap.put("sysname", SendDataConstant.HEADER_SYSNAME);
		headerMap.put("sysdomain", Integer.parseInt(SendDataConstant.HEADER_SYSDOMAIN));
		headerMap.put("msgtype", Integer.parseInt(SendDataConstant.HEADER_MSGTYPE_5));
		map.put("header", headerMap);
		
		String jsonString = JSONObject.toString("", map);
		saveFileByString(jsonString.substring(3));
	}
	
	public void saveFileByString(String jsonString) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(ResourceCenter.getInstance().getSysPath() + 
					File.separator + "middle_file" + File.separator + "perform_running.log");
			fileWriter.write(jsonString);
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(fileWriter != null)fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				fileWriter = null;
			}
		}
	}

}

