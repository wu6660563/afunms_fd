package com.afunms.application.ajaxManager;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sf.json.JSONObject;

import com.afunms.capreport.common.DateTime;
import com.afunms.capreport.dao.SubscribeResourcesDao;
import com.afunms.capreport.model.SubscribeResources;
import com.afunms.common.base.AjaxBaseManager;
import com.afunms.common.base.AjaxManagerInterface;
import com.afunms.common.util.CommonUtil;
import com.afunms.common.util.CreateMetersPic;
import com.afunms.common.util.CreatePiePicture;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.GeneratorKey;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.config.dao.IpAliasDao;
import com.afunms.config.dao.PortconfigDao;
import com.afunms.config.model.Portconfig;
import com.afunms.detail.service.cpuInfo.CpuInfoService;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostCollectData;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.system.dao.UserDao;
import com.afunms.system.model.User;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.util.KeyGenerator;

public class NetworkDeviceAjaxManager extends AjaxBaseManager implements AjaxManagerInterface {

	// 更改端口配置是否监视
	private void editnodeport() {
		String flagStr = "";
		Portconfig vo = new Portconfig();
		int id = getParaIntValue("id");
		int sms = getParaIntValue("sms");
		int reportflag = getParaIntValue("reportflag");
		if (sms != -1) {
			flagStr = sms + "";
		}
		if (reportflag != -1) {
			flagStr = reportflag + "";
		}
		PortconfigDao dao = null;

		dao = new PortconfigDao();
		try {
			vo = dao.loadPortconfig(id);
		} catch (Exception e) {
			e.printStackTrace();
			flagStr = "3";
		} finally {
			dao.close();
		}

		if (sms > -1)
			vo.setSms(sms);

		if (reportflag > -1)
			vo.setReportflag(reportflag);

		dao = new PortconfigDao();
		try {
			dao.update(vo);
		} catch (Exception e) {
			e.printStackTrace();
			flagStr = "3";
		} finally {
			dao.close();
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("flagStr", flagStr);
		JSONObject json = JSONObject.fromObject(map);
		out.print(json);
		out.flush();

	}

	private void savePerforIndex() {

		String ids = request.getParameter("ids");

		String type = "";
		type = request.getParameter("type");
		String startTime = request.getParameter("startdate");
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (startTime == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			startTime = sdf.format(new Date()) + " 00:00:00";
		} else {
			startTime = startTime + " 00:00:00";
		}

		String toTime = request.getParameter("todate");
		if (toTime == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			toTime = sdf.format(new Date()) + " 23:59:59";
		} else {
			toTime = toTime + " 23:59:59";
		}

		
		String[] idValue = null;
		String ip = "";
		String category = "";
		String entity = "";
		String subentity = "";
		String dataStr = "保存成功！";
		// ////////////////////////////////////////////////////
		DBManager dbManager = new DBManager();
		String reportname = this.getParaValue("report_name");
		String transmitfrequency = request.getParameter("transmitfrequency");
		String bidtext = this.getParaValue("bidtext");
		String reporttype = this.getParaValue("reporttype");
		String exporttype = request.getParameter("exporttype");
		String username = this.getParaValue("recievers_name");
		String tile = this.getParaValue("tile");
		String desc = this.getParaValue("desc");
		try {
			reportname = new String(reportname.getBytes("iso8859-1"), "UTF-8");
			bidtext = new String(bidtext.getBytes("iso8859-1"), "UTF-8");
			reporttype = new String(reporttype.getBytes("iso8859-1"), "UTF-8");
			username = new String(username.getBytes("iso8859-1"), "UTF-8");
			tile = new String(tile.getBytes("iso8859-1"), "UTF-8");
			desc = new String(desc.getBytes("iso8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String sendtimemonth = null;
		String sendtimeweek = null;
		String sendtimeday = null;
		String sendtimehou = null;
		if ("1".equals(transmitfrequency)) {
			sendtimehou = request.getParameter("sendtimehou");
		} else if ("2".equals(transmitfrequency)) {
			sendtimehou = request.getParameter("sendtimehou");
			sendtimeweek = request.getParameter("sendtimeweek");
		} else if ("3".equals(transmitfrequency)) {
			sendtimehou = request.getParameter("sendtimehou");
			sendtimeday = request.getParameter("sendtimeday");
		} else if ("4".equals(transmitfrequency)) {
			sendtimehou = request.getParameter("sendtimehou");
			sendtimeday = request.getParameter("sendtimeday");
			sendtimemonth = request.getParameter("sendtimemonth");
		} else if ("5".equals(transmitfrequency)) {
			sendtimehou = request.getParameter("sendtimehou");
			sendtimeday = request.getParameter("sendtimeday");
			sendtimemonth = request.getParameter("sendtimemonth");
		}
		String recieversId = this.getParaValue("recievers_id");
		String bid = this.getParaValue("bid");
		int key = KeyGenerator.getInstance().getNextKey();
		Calendar tempCal = Calendar.getInstance();
		Date cc = tempCal.getTime();
		String time = sdFormat.format(cc);
		StringBuffer sql = new StringBuffer();
		sql.append("insert into nms_userReport(id,name,type,ids,collecttime,begintime,endtime) values('");
		sql.append(key);
        sql.append("','");
		sql.append(reportname);
		sql.append("','");

		sql.append(type);
		sql.append("','");
		sql.append(ids);
		sql.append("','");
		sql.append(time);

		sql.append("','");
		sql.append(startTime);
		sql.append("','");
		sql.append(toTime);

		sql.append("')");
		try {
			dbManager.executeUpdate(sql.toString());
		} catch (Exception e) {
			e.printStackTrace();
			dataStr = "保存失败！！！";
		} finally {
			dbManager.close();
		}
		UserDao userDao = new UserDao();
		List userList = new ArrayList();
		try {
			userList = userDao.findbyIDs(recieversId.substring(1));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			userDao.close();
		}
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < userList.size(); i++) {
			User vo = (User) userList.get(i);
			buf.append(vo.getEmail());
			buf.append(",");
		}
		DateTime dt = new DateTime();
		SubscribeResources sr = new SubscribeResources();
		sr.setSubscribe_id(key);
		sr.setBidtext(bidtext);
		sr.setBID(bid);
		sr.setUsername(username);
		sr.setEmail(buf.toString());
		sr.setEmailtitle(tile);
		sr.setEmailcontent(desc);
		sr.setAttachmentformat(exporttype);
		sr.setReport_type(reporttype);
		sr.setReport_senddate(Integer.parseInt(dt.getMyDateTime(DateTime.Datetime_Format_14)));
		sr.setReport_sendfrequency(Integer.parseInt(transmitfrequency));
		sr.setReport_time_month(arrayToString(sendtimemonth));
		sr.setReport_time_week(arrayToString(sendtimeweek));
		sr.setReport_time_day(arrayToString(sendtimeday));
		sr.setReport_time_hou(arrayToString(sendtimehou));
		SubscribeResourcesDao srd = new SubscribeResourcesDao();
		boolean b = srd.save(sr);
		if (!b) {
			dataStr = "保存失败！！！";
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("dataStr", dataStr);
		JSONObject json = JSONObject.fromObject(map);
		out.print(json);
		out.flush();
	}

	public String arrayToString(String array) {
		StringBuilder sb = new StringBuilder();
		if (array != null) {
			array = array.replace(",", "/");
			sb.append("/").append(array).append("/");
		}
		return sb.toString();
	}

	// 加载模板列表（网络设备、服务器）
	private void loadPeforIndex() {
		DBManager dbManager = new DBManager();
		ResultSet rs = null;
		StringBuffer html = new StringBuffer();

		String id = this.getParaValue("id");
		String type = this.getParaValue("type");

		String sql = "select n.id,n.name,n.ids,s.EMAIL,s.REPORT_SENDDATE from nms_userreport n,sys_subscribe_resources s where n.id=s.SUBSCRIBE_ID and n.type='"
				+ type + "'";
		List<SubscribeResources> list = new ArrayList<SubscribeResources>();
		try {
			if (id != null && !id.equals("")) {

				String sql1 = "delete from nms_userreport where id=" + id;
				String sql2 = "delete from sys_subscribe_resources where SUBSCRIBE_ID=" + id;

				try {
					dbManager.executeUpdate(sql1);
					dbManager.executeUpdate(sql2);

				} catch (Exception e) {
					e.printStackTrace();

				}
			}
			rs = dbManager.executeQuery(sql);
			while (rs.next()) {
				SubscribeResources sr = new SubscribeResources();
				sr.setSubscribe_id(rs.getInt("id"));
				sr.setUsername(rs.getString("name"));// 为方便暂用字段充当模板名称
				sr.setEmail(rs.getString("EMAIL"));
				sr.setReport_senddate(rs.getInt("REPORT_SENDDATE"));
				sr.setEmailcontent(rs.getString("ids"));// 为方便暂用字段充当性能指标
				list.add(sr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbManager.close();

		}
		if (list != null && list.size() > 0) {

			html
					.append("<table   border=1 bordercolor='#C0C0C0'><tr><td align='center' class='body-data-title' height=21>序号</td><td align='center' class='body-data-title' height=21>模板名称</td><td align='center' class='body-data-title' height=21>接收邮箱</td><td align='center' class='body-data-title' height=21>发送时间</td><td align='center' class='body-data-title' height=21>详情</td>");
			for (int i = 0; i < list.size(); i++) {
				SubscribeResources sr = new SubscribeResources();
				sr = list.get(i);
				html.append("<tr><td  align='center' height=19>");
				html.append(i + 1);
				html.append("</td><td align='center' height=19>");
				html.append(sr.getUsername());
				html.append("</td><td align='center' height=19>");
				html.append(sr.getEmail());
				html.append("</td><td align='center' height=19>");
				html.append(sr.getReport_senddate());
				html.append("</td><td align='center' height=19>");

				html.append("<img src='resource/image/vcf.gif' border='0' onClick='createWin(" + sr.getSubscribe_id()
						+ ")' title='查看模板详细信息'/>&nbsp;&nbsp;<img src='resource/image/viewreport.gif' border='0' onClick='preview("
						+ sr.getSubscribe_id()
						+ ")' title='预览模板报表'/>&nbsp;&nbsp;<img src='resource/image/delete.gif' border='0' onClick='deleteItem("
						+ sr.getSubscribe_id() + ")' title='删除模板'/></td></tr>");
			}
			html.append("</table>");

		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("dataStr", html.toString());
		JSONObject json = JSONObject.fromObject(map);
		out.print(json);
		out.flush();
	}

	private void deletePerforIndex() {

		DBManager dbManager = new DBManager();
		String id = this.getParaValue("id");
		String flagStr = "删除成功！";
		String sql1 = "delete from nms_userreport where id=" + id;
		String sql2 = "delete from sys_subscribe_resources where SUBSCRIBE_ID=" + id;
		List<SubscribeResources> list = new ArrayList<SubscribeResources>();
		try {
			dbManager.executeUpdate(sql1);
			dbManager.executeUpdate(sql2);

		} catch (Exception e) {
			e.printStackTrace();
			flagStr = "删除失败！";
		} finally {
			dbManager.close();

		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("flagStr", flagStr);
		JSONObject json = JSONObject.fromObject(map);
		out.print(json);
		out.flush();

	}

	public void execute(String action) {
		// 端口配置
		if (action.equals("editnodeport")) {
			editnodeport();
		}
		if (action.equals("savePerforIndex")) {
			savePerforIndex();
		}
		if (action.equals("loadPeforIndex")) {
			loadPeforIndex();
		}

		if (action.equals("deletePerforIndex")) {
			deletePerforIndex();
		}
		if (action.equals("netAjaxUpdate")) {
			SysLogger.info("###########开始刷新数据 ######################");
			String runmodel = PollingEngine.getCollectwebflag();
			String pingconavg = "";
			double cpuvalue = 0;
			String tmp = request.getParameter("tmp");
			Host host = (Host) PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());

			if (host == null) {
				return;
			}

			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";

			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager = new HostCollectDataManager();
			try {
				ConnectUtilizationhash = hostmanager.getCategory(host.getIpAddress(), "Ping", "ConnectUtilization",
					starttime1, totime1);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			if (ConnectUtilizationhash.get("avgpingcon") != null) {
				pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
				pingconavg = pingconavg.replace("%", "");
			}
			int percent1 = Double.valueOf(pingconavg).intValue();
			int percent2 = 100 - percent1;
			Map<String, Integer> map = new HashMap<String, Integer>();

			map.put("percent1", percent1);
			map.put("percent2", percent2);

			// }
			Vector cpuV = null;
			if ("0".equals(runmodel)) {
				// 采集与访问是集成模式
				Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(host.getIpAddress());
				cpuV = (Vector) ipAllData.get("cpu");
			} else {
				// 采集与访问是分离模式
				NodeUtil nodeUtil = new NodeUtil();
				NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host);
				CpuInfoService cpuInfoService = new CpuInfoService(nodedto.getId() + "", nodedto.getType(), nodedto
						.getSubtype());
				cpuV = cpuInfoService.getCpuInfo();
			}
			if (cpuV != null && cpuV.size() > 0) {
				CPUcollectdata cpu = (CPUcollectdata) cpuV.get(0);
				if (cpu != null && cpu.getThevalue() != null) {
					cpuvalue = new Double(cpu.getThevalue());
				}
			}
			int cpuper = Double.valueOf(cpuvalue).intValue();
			String picip = CommonUtil.doip(host.getIpAddress());
			CreatePiePicture _cpp = new CreatePiePicture();
			_cpp.createAvgPingPic(picip, Double.valueOf(pingconavg));

			// 生成CPU仪表盘
			CreateMetersPic cmp = new CreateMetersPic();
			cmp.createCpuPic(picip, cpuper);

			map.put("cpuper", cpuper);
			JSONObject json = JSONObject.fromObject(map);
			out.print(json);
			out.flush();
			// System.out.println(json);
			out.close();
		} else if (action.equals("modifyIpAlias")) {
			String ipalias = getParaValue("ipalias");
			String ipaddress = getParaValue("ipaddress");
			// 更新数据库
			IpAliasDao ipAliasDao = new IpAliasDao();
			try {
				ipAliasDao.updateIpAlias(ipaddress, ipalias);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ipAliasDao.close();
			}
		} else if (action.equals("updatemac")) {
			String mac = getParaValue("mac");
			String id = getParaValue("id");
			int tmp = Integer.parseInt(id);

			HostNodeDao dao = new HostNodeDao();
			try {
				dao.UpdateAixMac(tmp, mac);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}
		}
	}

}
