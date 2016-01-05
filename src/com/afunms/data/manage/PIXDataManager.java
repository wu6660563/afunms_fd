/*
 * @(#)PIXDataManager.java     v1.01, May 7, 2014
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.data.manage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import com.afunms.common.util.SessionConstant;
import com.afunms.data.service.PIXDataService;
import com.afunms.event.dao.EventListDao;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.system.model.User;

/**
 * ClassName: PIXDataManager.java
 * <p>
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date May 7, 2014 8:49:04 PM
 * @mail wupinlong@dhcc.com.cn
 */
public class PIXDataManager extends NodeDataManager {

	public String execute(String action) {
		if ("getPerformanceInfo".equals(action)) {
			return getPerformanceInfo();
		} else if ("getEventInfo".equals(action)) {
			return getEventInfo();
		}
		return null;
	}

	public String getPerformanceInfo() {
		getBaseInfo();
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();

		PIXDataService pixDataService = new PIXDataService(nodeid, type,
				subtype);
		Hashtable<String, String> curDayPingValuHashtable = pixDataService.getCurDayPingValueHashtableInfo();

		// 开始获取连通率值
		String curDayPingMaxValue = curDayPingValuHashtable.get("pingmax");
		if (curDayPingMaxValue == null
				|| curDayPingMaxValue.trim().length() == 0) {
			curDayPingMaxValue = "0";
		}
		curDayPingMaxValue = curDayPingMaxValue.replaceAll("%", "");
		curDayPingMaxValue = String.valueOf(Math.round(Double
				.valueOf(curDayPingMaxValue)));

		String curDayPingAvgValue = curDayPingValuHashtable.get("avgpingcon");
		if (curDayPingAvgValue == null
				|| curDayPingAvgValue.trim().length() == 0) {
			curDayPingAvgValue = "0";
		}
		curDayPingAvgValue = curDayPingAvgValue.replaceAll("%", "");
		curDayPingAvgValue = String.valueOf(Math.round(Double
				.valueOf(curDayPingAvgValue)));

		request.setAttribute("curDayPingMaxValue", curDayPingMaxValue);
		request.setAttribute("curDayPingAvgValue", curDayPingAvgValue);
		return "/detail/performance.jsp";
	}

	public void getBaseInfo() {
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();

		PIXDataService pixDataService = new PIXDataService(nodeid, type,
				subtype);
		NodeDTO node = pixDataService.getNodeDTO();
		String alarmLevel = pixDataService.getMaxAlarmLevel(); // 告警等级
		
		request.setAttribute("node", node);
		request.setAttribute("alarmLevel", alarmLevel);
	}

	public String getEventInfo() {
		getBaseInfo();
		String nodeid = getNodeid();
		String type = getType();
		String subtype = getSubtype();
		int status = getParaIntValue("status");
		int level1 = getParaIntValue("level1");
		if (status == -1) {
			status = 99;
		}
		if (level1 == -1) {
			level1 = 99;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startdate = request.getParameter("startdate");
		if (startdate == null) {
			startdate = sdf.format(new Date());
		}
		String todate = request.getParameter("todate");
		if (todate == null) {
			todate = sdf.format(new Date());
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String bid = ((User) session.getAttribute(SessionConstant.CURRENT_USER))
				.getBusinessids();
		List list = null;
		EventListDao dao = new EventListDao();
		try {
			list = dao.getQuery(starttime, totime, status + "", level1 + "",
					bid, Integer.valueOf(nodeid));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		request.setAttribute("status", status);
		request.setAttribute("level1", level1);
		request.setAttribute("list", list);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		return "/detail/event.jsp";
	}

}
