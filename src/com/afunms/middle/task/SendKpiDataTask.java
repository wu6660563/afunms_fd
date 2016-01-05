/*
 * @(#)SendKpiService.java     v1.01, Dec 4, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.task;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimerTask;

import net.sf.json.JSONObject;

import com.afunms.middle.model.HeaderData;
import com.afunms.middle.model.KpiVo;
import com.afunms.middle.model.SendData;
import com.afunms.middle.util.MQSend;

/**
 * ClassName: SendKpiService.java
 * <p>
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Dec 4, 2013 10:21:24 AM
 * @mail wupinlong@dhcc.com.cn
 */
public class SendKpiDataTask extends TimerTask {

	@Override
	public void run() {
		SendData<KpiVo> sendData = new SendData<KpiVo>();
		Set<KpiVo> set = new HashSet<KpiVo>();

		for (int i = 0; i < 2; i++) {
			KpiVo vo = new KpiVo();
			vo.setId(1);
			vo.setGid("");
			vo.setCount_time(new Date().getTime());
			vo.setCount_frequenc(5);
			vo.setKpi_name("");
			vo.setKpi_value(100);

			set.add(vo);
		}
		sendData.setBody(set);
		HeaderData header = new HeaderData();
		header.setSystype(4); // 地调系统
		header.setSysname("ForceView");
		header.setSysdomain(4);
		header.setMsgtype(6);
		sendData.setHeader(header);

		JSONObject jsonObject = JSONObject.fromObject(sendData);
		String objToString = jsonObject.toString();

		MQSend mqSend = new MQSend("topic_fs_donghua_all_nr");
		try {
			mqSend.sendMessage(objToString.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mqSend.close();
		}
	}

}
