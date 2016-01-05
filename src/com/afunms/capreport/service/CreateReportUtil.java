/*
 * @(#)CreateReportUtil.java     v1.01, Jun 27, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.capreport.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import javax.mail.Address;

import org.apache.log4j.Logger;

import com.afunms.capreport.dao.CustomReportHistoryDao;
import com.afunms.capreport.model.CustomReportHistory;
import com.afunms.common.base.BaseVo;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.sendMail;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.system.dao.AlertEmailDao;
import com.afunms.system.model.AlertEmail;

/**
 * ClassName: CreateReportUtil.java
 * <p>
 * 创建报表的工具类，包括Data，发送邮件等等
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Jun 27, 2013 9:55:00 AM
 */
public class CreateReportUtil {

	private static Logger log = Logger.getLogger(CreateReportUtil.class);

	public boolean saveHistory(int reportId, String fileName) {
		boolean bln = false;
		CustomReportHistory history = new CustomReportHistory();
		history.setReportId(reportId);
		history.setFileName(fileName);
		history.setIsSuccess("1");
		Calendar cal = Calendar.getInstance();
		history.setCreateDate(cal);
		CustomReportHistoryDao historyDao = new CustomReportHistoryDao();
		try {
			bln = historyDao.save(history);
		} catch (Exception e) {
			bln = false;
			e.printStackTrace();
		} finally {
			historyDao.close();
		}
		return bln;
	}

	public String[][] getNodeAlarmReportData(String startTime, String endTime,
			List<NodeDTO> nodeList) {
		if (nodeList == null && nodeList.size() <= 0) {
			return null;
		}
		String nodeId = "";
		String nodeType = "";
		String nodeSubtype = "";

		for (int i = 0; i < nodeList.size(); i++) {
			nodeId += nodeList.get(i).getNodeid() + ",";
			nodeType += nodeList.get(i).getType() + ",";
			nodeSubtype += nodeList.get(i).getSubtype() + ",";
		}

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
			sb.append(startTime);
			sb.append("' AND recordtime<'");
			sb.append(endTime);
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
		return dataStr;
	}

	public String[][] getNodeAlarmReportData(String startTime, String endTime,
			String type, String subtype) {
		/*
		 * 获取设备信息
		 */
		NodeUtil nodeUtil = new NodeUtil();
		List<BaseVo> list = nodeUtil.getNodeByTyeAndSubtype(type, subtype);
		List<NodeDTO> nodeList = null;
		if (list != null && list.size() > 0) {
			nodeList = nodeUtil.conversionToNodeDTO(list);
		}
		if (nodeList == null && nodeList.size() <= 0) {
			return null;
		}
		return getNodeAlarmReportData(startTime, endTime, nodeList);
	}

	public boolean sendEmail(String receivemailaddr, String title, String body,
			String[] path, String[] fileName) {
		boolean bln = false;
		AlertEmail vo = null;
		AlertEmailDao emaildao = new AlertEmailDao();
		List list = null;
		try {
			list = emaildao.getByFlage(1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emaildao.close();
		}
		if (list != null && list.size() > 0) {
			vo = (AlertEmail) list.get(0);
		}
		if (vo == null) {
			log.info("报表订阅：未设置发送邮箱");
			return false;
		}
		String mailaddr = vo.getMailAddress();
		String mailpassword = vo.getPassword();
		String mailsmtp = vo.getSmtp();
		String fromAddr = vo.getMailAddress();

		try {
			Address[] ccAddress = { null, null };
			sendMail sendmail = new sendMail(mailsmtp, mailaddr, mailpassword,
					receivemailaddr, title, body, fromAddr, ccAddress);
			try {
				bln = sendmail.sendmailHaveFile(path, fileName);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bln;
	}
}
