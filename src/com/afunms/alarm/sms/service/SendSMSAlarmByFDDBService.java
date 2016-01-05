/*
 * @(#)SendSMSAlarmByFDDBService.java     v1.01, Nov 19, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.alarm.sms.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;

/**
 * ClassName: SendSMSAlarmByFDDBService.java
 * <p>
 * 佛山供电局专用短信服务的Service
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Nov 19, 2013 3:23:35 PM
 * @mail wupinlong@dhcc.com.cn
 */
public class SendSMSAlarmByFDDBService {

	/**
	 * 短信发送
	 * 
	 * @param phone
	 *            手机号
	 * @param userName
	 *            用户名
	 * @param content
	 *            发送内容
	 * @return
	 */
	public static String sendSMS(String driver, String dbURL, String mobiles,
			String content, String user, String password) {
		String systemid = "14"; // 系统编号(必填)
		String functionid = "99"; // 功能编号(必填)
		String ifdelaysend = "0"; // 是否延迟发送,可选值:空,0,1; 1为延迟,其他值为不延迟
		content = content + "(发送至网管系统)";

		String sendResult = null;
		Connection conn = null;
		CallableStatement callStamement = null;

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(dbURL, user, password);
			callStamement = conn
					.prepareCall("{? = call smsusern.message.send_sms(?,?,?,?,?,?,?,?,?)}");
			// 发送成功的手机号码个数,出错返回null或"$"开头的字符串
			callStamement.registerOutParameter(1, Types.VARCHAR);
			callStamement.setString(2, mobiles); // 接收手机号码集合，用逗号隔开
			callStamement.setString(3, content); // 发送内容
			callStamement.setString(4, systemid); // 系统编号
			callStamement.setString(5, functionid); // 功能编号
			callStamement.setString(6, ifdelaysend); // 是否延迟发送,可选值:空,0,1;
														// 1为延迟,其他值为不延迟
			callStamement.setString(7, ""); // 开始发送时间，如果用户设置此时间，则此条短信在此时间之后才会开始发送操作，格式为YYYY-MM-DD
											// HH24:MI:SS(可空)
			callStamement.setString(8, ""); // 结束发送时间，如果用户设置此时间，并且此条短信在此时间之后仍然没有发送成功，则此条短信发送失败，格式为YYYY-MM-DD
											// HH24:MI:SS（可空）
			callStamement.setString(9, ""); // 帐号类型,置空值
			callStamement.setString(10, "1"); // 选择发送途径,1为短信机,其他值为95598平台
			callStamement.execute();
			sendResult = callStamement.getString(1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (callStamement != null)
					callStamement.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sendResult;
	}

}
