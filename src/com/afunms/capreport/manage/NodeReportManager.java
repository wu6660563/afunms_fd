/*
 * @(#)NodeReportManager.java     v1.01, May 22, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.capreport.manage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import com.afunms.alarm.dao.AlarmDeviceDependenceDao;
import com.afunms.alarm.util.DeviceDependenceUtil;
import com.afunms.application.util.ReportExport;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.Constant;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.initialize.ResourceCenter;
import com.afunms.system.model.User;

/**
 * 
 * ClassName: NodeReportManager.java
 * <p>
 * 综合报表展示和导出
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date May 22, 2013 10:38:08 AM
 */
public class NodeReportManager extends BaseManager implements ManagerInterface {

	private static SysLogger logger = SysLogger
			.getLogger(NodeReportManager.class.getName());

	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");

	public String execute(String action) {
		if ("showReport".equals(action))
			return showReport();
		if ("reportList".equals(action))
			return reportList();
		if ("downloadReport".equals(action)) {
			return downloadReport();
		}
		if ("relatedNode".equals(action)) {
			return relatedNode();
		}
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
	}

	// 关联成功，关联关系存在nms_alarm_device_dependence表里，需要自己设定基准设备
	// 隐藏该功能，目的防止失误修改基准设备
	// 拓扑图创建完毕，关联之后，该功能则无需使用
	public String relatedNode() {
		// 先清空nms_alarm_device_dependence表
		Properties properties = new Properties();
		String filePath = ResourceCenter.getInstance().getSysPath() + "task"
				+ "/relatedNode.properties";
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(new File(filePath));
			properties.load(fin);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fin != null)
					fin.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				fin = null;
			}
		}
		String nodeId = properties.getProperty("initNodeId");

		AlarmDeviceDependenceDao alarmDeviceDependenceDao = new AlarmDeviceDependenceDao();
		if (alarmDeviceDependenceDao.empty()) {
			DeviceDependenceUtil util = new DeviceDependenceUtil(nodeId);
			boolean flag = util.excute(0);
			request.setAttribute("flag", flag);
		} else {
			logger.info("清空失败！请查找原因！");
		}
		return "/capreport/relatedResult.jsp";
	}

	public String showReport() {
		User user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
		String bid = user.getBusinessids();
		NodeUtil nodeUtil = new NodeUtil();
		/**
		 * 初始查询日期
		 */
		String startdate = simpleDateFormat.format(new Date());
		String todate = simpleDateFormat.format(new Date());

		/**
		 * 判断是超级用户还是正常用户
		 */
		boolean result = false;
		if (user.getRole() == 0) {
			result = true;
		} else if (bid == null || bid.trim().length() == 0) {
			result = false;
		} else {
			/*
			 * 设定正常用户在权限范围内可访问的设备
			 */
			nodeUtil.setBid(bid);
			result = true;
		}
		/*
		 * 获取设备信息
		 */
		List<NodeDTO> nodeList = null;
		if (result) {
			List<BaseVo> list = nodeUtil.getNodeByTyeAndSubtype(
					Constant.ALL_TYPE, Constant.ALL_SUBTYPE);
			nodeList = nodeUtil.conversionToNodeDTO(list);
		}
		if (nodeList == null) {
			nodeList = new ArrayList<NodeDTO>();
		}
		/*
		 * 将查询到的设备信息传至jsp页面
		 */
		// System.out.println("设备个数是：" + nodeList.size());
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("nodeList", nodeList);
		return "/capreport/reportList.jsp";
	}

	public String reportList() {
		String starttime = request.getParameter("starttime");
		String endtime = request.getParameter("endtime");
		String nodeId = request.getParameter("nodeId");
		String nodeType = request.getParameter("nodeType");
		String nodeSubtype = request.getParameter("nodeSubtype");

		String[] nodeIds = nodeId.split(",");
		String[] nodeTypes = nodeType.split(",");
		String[] nodeSubtypes = nodeSubtype.split(",");

		String[][] dataStr = new String[nodeIds.length + 1][6];
		dataStr[0][0] = "设备名称";
		dataStr[0][1] = "IP地址";
		dataStr[0][2] = "提示";
		dataStr[0][3] = "普通";
		dataStr[0][4] = "严重";
		dataStr[0][5] = "紧急";

		try {
			StringBuilder sb = new StringBuilder();
			sb
					.append("SELECT nodeid,subtype,SUM(CASE  WHEN level1='0' THEN 1 ELSE 0 END) A,SUM(CASE  WHEN level1='1' THEN 1 ELSE 0 END) B,");
			sb
					.append("SUM(CASE  WHEN level1='2' THEN 1 ELSE 0 END) C,SUM(CASE  WHEN level1='3' THEN 1 ELSE 0 END) D,");
			sb.append("COUNT(*) AS cnt FROM system_eventlist  WHERE ");
			sb.append("recordtime>'");
			sb.append(starttime);
			sb.append("' AND recordtime<'");
			sb.append(endtime);
			sb.append("' and (");
			for (int i = 0; i < nodeIds.length; i++) {
				if (i + 1 == nodeIds.length) {
					sb.append("(nodeid='" + nodeIds[i] + "' and subtype='"
							+ nodeTypes[i] + "')");
				} else {
					sb.append("(nodeid='" + nodeIds[i] + "' and subtype='"
							+ nodeTypes[i] + "') or ");
				}
			}
			sb.append(") GROUP BY subtype,nodeid ");
			DBManager conn = new DBManager();

			Hashtable<String, Hashtable<String, String>> resultHash = new Hashtable<String, Hashtable<String, String>>();

			NodeUtil util = new NodeUtil();
			Hashtable<String, NodeDTO> nodeHash = new Hashtable<String, NodeDTO>();
			for (int i = 0; i < nodeIds.length; i++) {
				List<BaseVo> list1 = util.getNodeByTyeAndSubtype(nodeTypes[i],
						nodeSubtypes[i]);
				List<NodeDTO> list2 = util.conversionToNodeDTO(list1);
				for (int j = 0; j < list2.size(); j++) {
					if (nodeIds[i].equals(list2.get(j).getNodeid())) {
						nodeHash.put(list2.get(j).getNodeid() + ","
								+ list2.get(j).getType(), list2.get(j));
						break;
					}
				}
			}

			ResultSet rs = conn.executeQuery(sb.toString());
			try {
				while (rs.next()) {
					Hashtable<String, String> alarmhash = new Hashtable<String, String>();

					String nodeid = rs.getString("nodeid");
					String subtype = rs.getString("subtype");
					alarmhash.put("nodeid", nodeid);
					alarmhash.put("subtype", subtype);
					alarmhash.put("A", rs.getString("A"));
					alarmhash.put("B", rs.getString("B"));
					alarmhash.put("C", rs.getString("C"));
					alarmhash.put("D", rs.getString("D"));
					alarmhash.put("cnt", rs.getString("cnt"));
					if (nodeHash.containsKey(nodeid + "," + subtype)) {
						NodeDTO nodedto = nodeHash.get(nodeid + "," + subtype);
						alarmhash.put("name", nodedto.getName());
						alarmhash.put("ipaddress", nodedto.getIpaddress());
					}
					resultHash.put(nodeid + "," + subtype, alarmhash);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				rs.close();
				conn.close();
			}

			for (int i = 0; i < dataStr.length - 1; i++) {
				if (resultHash.containsKey(nodeIds[i] + "," + nodeTypes[i])) {
					Hashtable<String, String> alarmhash = resultHash
							.get(nodeIds[i] + "," + nodeTypes[i]);
					dataStr[i + 1][0] = alarmhash.get("name");
					dataStr[i + 1][1] = alarmhash.get("ipaddress");
					dataStr[i + 1][2] = alarmhash.get("A");
					dataStr[i + 1][3] = alarmhash.get("B");
					dataStr[i + 1][4] = alarmhash.get("C");
					dataStr[i + 1][5] = alarmhash.get("D");
				} else {
					NodeDTO nodevo = nodeHash.get(nodeIds[i] + ","
							+ nodeTypes[i]);
					dataStr[i + 1][0] = nodevo.getName();
					dataStr[i + 1][1] = nodevo.getIpaddress();
					dataStr[i + 1][2] = "0";
					dataStr[i + 1][3] = "0";
					dataStr[i + 1][4] = "0";
					dataStr[i + 1][5] = "0";
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("dataStr", dataStr);
		Hashtable<String, Object> hash = new Hashtable<String, Object>();
		hash.put("dataStr", dataStr);
		hash.put("starttime", starttime);
		hash.put("endtime", endtime);
		ShareData.getNodereportData().put("nodereportHash", hash);
		return "/capreport/alarmList.jsp";
	}

	private String downloadReport() {
		Hashtable hash = (Hashtable) ShareData.getNodereportData().get(
				"nodereportHash");
		String startTime = (String) hash.get("starttime");
		String toTime = (String) hash.get("endtime");

		String ids = "";
		String exportType = getParaValue("exportType");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String filename = "";
		if (exportType.equals("xls")) {
			filename = "/temp/node_report.xls";
		} else if (exportType.equals("doc")) {
			filename = "/temp/node_report.doc";
		} else if (exportType.equals("pdf")) {
			filename = "/temp/node_report.pdf";
		}
		String filePath = ResourceCenter.getInstance().getSysPath() + filename;

		if (startTime == null) {
			startTime = sdf.format(new Date()) + " 00:00:00";
		} else {
			startTime = startTime + " 00:00:00";
		}
		if (toTime == null) {
			toTime = sdf.format(new Date()) + " 23:59:59";
		} else {
			toTime = toTime + " 23:59:59";
		}
		ReportExport export = new ReportExport();
		String type = "node";
		export.exportReport(ids, type, filePath, startTime, toTime, exportType);
		request.setAttribute("filename", filePath);
		return "/capreport/net/download.jsp";
	}

}
