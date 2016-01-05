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
 * ��ɽ�����ר�ö��ŷ����Service
 * 
 * @author ��Ʒ��
 * @version v1.01
 * @since v1.01
 * @Date Nov 19, 2013 3:23:35 PM
 * @mail wupinlong@dhcc.com.cn
 */
public class SendSMSAlarmByFDDBService {

	/**
	 * ���ŷ���
	 * 
	 * @param phone
	 *            �ֻ���
	 * @param userName
	 *            �û���
	 * @param content
	 *            ��������
	 * @return
	 */
	public static String sendSMS(String driver, String dbURL, String mobiles,
			String content, String user, String password) {
		String systemid = "14"; // ϵͳ���(����)
		String functionid = "99"; // ���ܱ��(����)
		String ifdelaysend = "0"; // �Ƿ��ӳٷ���,��ѡֵ:��,0,1; 1Ϊ�ӳ�,����ֵΪ���ӳ�
		content = content + "(����������ϵͳ)";

		String sendResult = null;
		Connection conn = null;
		CallableStatement callStamement = null;

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(dbURL, user, password);
			callStamement = conn
					.prepareCall("{? = call smsusern.message.send_sms(?,?,?,?,?,?,?,?,?)}");
			// ���ͳɹ����ֻ��������,������null��"$"��ͷ���ַ���
			callStamement.registerOutParameter(1, Types.VARCHAR);
			callStamement.setString(2, mobiles); // �����ֻ����뼯�ϣ��ö��Ÿ���
			callStamement.setString(3, content); // ��������
			callStamement.setString(4, systemid); // ϵͳ���
			callStamement.setString(5, functionid); // ���ܱ��
			callStamement.setString(6, ifdelaysend); // �Ƿ��ӳٷ���,��ѡֵ:��,0,1;
														// 1Ϊ�ӳ�,����ֵΪ���ӳ�
			callStamement.setString(7, ""); // ��ʼ����ʱ�䣬����û����ô�ʱ�䣬����������ڴ�ʱ��֮��ŻῪʼ���Ͳ�������ʽΪYYYY-MM-DD
											// HH24:MI:SS(�ɿ�)
			callStamement.setString(8, ""); // ��������ʱ�䣬����û����ô�ʱ�䣬���Ҵ��������ڴ�ʱ��֮����Ȼû�з��ͳɹ�����������ŷ���ʧ�ܣ���ʽΪYYYY-MM-DD
											// HH24:MI:SS���ɿգ�
			callStamement.setString(9, ""); // �ʺ�����,�ÿ�ֵ
			callStamement.setString(10, "1"); // ѡ����;��,1Ϊ���Ż�,����ֵΪ95598ƽ̨
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
