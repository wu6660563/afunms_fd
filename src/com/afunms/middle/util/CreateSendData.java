/*
 * @(#)CreateSendData.java     v1.01, Oct 11, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONObject;

import com.afunms.common.base.BaseVo;
import com.afunms.event.model.CheckEvent;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.middle.model.AlarmData;
import com.afunms.middle.model.HeaderData;
import com.afunms.middle.model.SendData;

/**
 * ClassName: CreateSendData.java
 * <p>
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Oct 11, 2013 10:31:30 AM
 * @mail wupinlong@dhcc.com.cn
 */
public class CreateSendData {

	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private String sysName = "afunms";

	public String getSendAlarmData(List<CheckEvent> checkEventList) {
		SendData<AlarmData> sendData = new SendData<AlarmData>();
		Set<AlarmData> set = new HashSet<AlarmData>();
		for (int i = 0; i < checkEventList.size(); i++) {
			AlarmData alarmData = getAlarmData(checkEventList.get(i));
			set.add(alarmData);
		}
		sendData.setBody(set);
		JSONObject returnData = JSONObject.fromObject(sendData);
		return returnData.toString();
	}

	public HeaderData getHeaderData(int msgtype) {
		HeaderData headerData = new HeaderData();
		headerData.setSystype(1); // 网管
		headerData.setSysname(sysName);
		headerData.setSysdomain(4); // 佛山地调
		headerData.setMsgtype(msgtype); // 告警数据
		return headerData;
	}

	public AlarmData getAlarmData(CheckEvent checkEvent) {
		AlarmData alarmData = new AlarmData();

		NodeUtil nodeUtil = new NodeUtil();
		// 得到所有该类型的设备
		List<BaseVo> list = nodeUtil.getNodeByTyeAndSubtype(checkEvent
				.getType(), checkEvent.getSubtype());
		NodeDTO nodeDto = null;
		for (int i = 0; i < list.size(); i++) {
			NodeDTO dto = nodeUtil.conversionToNodeDTO(list.get(i));
			if (dto.getNodeid() == checkEvent.getNodeId()) {
				nodeDto = dto;
				break;
			}
		}

		alarmData.setId(Long.parseLong(checkEvent.getAlarmId()));
		Date date = null;
		try {
			date = format.parse(checkEvent.getCollecttime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		alarmData.setTime(date.getTime());
		// 1：performance（性能）2：security（安全）3：business（业务）
		alarmData.setType("performance");
		alarmData.setLevel(checkEvent.getAlarmlevel());
		alarmData.setObject_gid("");
		alarmData.setObject_name(nodeDto.getName());
		int alarmType = getAlarmDataSubType(checkEvent.getName());
		alarmData.setSub_type("ping"); // ping
		alarmData.setDevice_gid(nodeDto.getNodeid());
		alarmData.setHandle_time(new Date().getTime());
		alarmData.setTitle(checkEvent.getName() + "预警");
		alarmData.setContent(checkEvent.getContent());
		return alarmData;
	}

	public int getAlarmDataSubType(String alarmName) {
		if ("ping".equals(alarmName)) {
			return 1;
		} else if ("cpu".equals(alarmName)) {
			return 2;
		}
		return 0;
	}

}
