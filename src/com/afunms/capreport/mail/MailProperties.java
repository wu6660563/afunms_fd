/**
 * @author sunqichang/������
 * Created on 2011-5-12 ����02:05:32
 */
package com.afunms.capreport.mail;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import com.afunms.system.dao.AlertEmailDao;
import com.afunms.system.model.AlertEmail;

/**
 * mail���Զ�ȡ
 * 
 * @author sunqichang/������
 * 
 */
public final class MailProperties {
	private static String sender; // �����˵�ַ

	private static String smtpHost; // �ʼ����ͷ�������smtp��

	private static String user; // ��¼�û���

	private static String password; // ��¼����

	private static String authentication; // �Ƿ���Ҫ�����֤

	private static String port; // smtp�˿�

	private static Properties mailproperties;

	/**
	 * �ʼ�������Ϣ��ȡ
	 * 
	 * @param mail
	 */
	public static void MailConfig(String mailPath) {
		try {
			mailproperties = new Properties();
			List list = null;
			AlertEmailDao alertEmailDao = new AlertEmailDao();
			try {
			    list = alertEmailDao.loadAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                alertEmailDao.close();
            }
            boolean alertEmailFlag = false;
            if (list != null && list.size() > 0) {
                for (Object object : list) {
                    AlertEmail alertEmail = (AlertEmail) object;
                    if (alertEmail.getUsedflag() >= 0) {
                        smtpHost = alertEmail.getSmtp();
                        sender = alertEmail.getMailAddress();
                        authentication = "true";
                        user = alertEmail.getUsername();
                        password = alertEmail.getPassword();
                        port = "25";
                        alertEmailFlag = true;
                    }
                }
            }
			if (!alertEmailFlag) {
			    Properties properties = new Properties();
	            FileInputStream stream = new FileInputStream(mailPath);
	            if (stream != null) {
	                properties.load(stream);
	                smtpHost = properties.getProperty("mail.smtp.host");
	                sender = properties.getProperty("mail.from");
	                authentication = properties.getProperty("mail.smtp.auth");
	                user = properties.getProperty("mail.smtp.user");
	                password = properties.getProperty("mail.smtp.password");
	                port = properties.getProperty("mail.smtp.port");
	            }
			}
			mailproperties.put("mail.smtp.host", smtpHost); // ����smtp����
            mailproperties.put("mail.from", sender); // ���÷�������
            mailproperties.put("mail.smtp.auth", authentication); // ����smtp�����֤
            mailproperties.put("mail.smtp.user", user); // �����û�
            mailproperties.put("mail.smtp.password", password); // ���ÿ���
            mailproperties.put("mail.smtp.port", port); // ����smtp�˿�
		} catch (Exception e) {
		    System.out.println(mailPath + "is null!");
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ�ʼ�������Ϣ
	 * 
	 * @param mailPath
	 *            �ʼ������ļ�·��
	 * @return
	 */
	public static Properties getMailproperties(String mailPath) {
		if (mailproperties == null) {
			MailConfig(mailPath);
		}
		return mailproperties;
	}

	public static String getAuthentication() {
		return authentication;
	}

	public static void setAuthentication(String authentication) {
		MailProperties.authentication = authentication;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		MailProperties.password = password;
	}

	public static String getPort() {
		return port;
	}

	public static void setPort(String port) {
		MailProperties.port = port;
	}

	public static String getSender() {
		return sender;
	}

	public static void setSender(String sender) {
		MailProperties.sender = sender;
	}

	public static String getSmtpHost() {
		return smtpHost;
	}

	public static void setSmtpHost(String smtpHost) {
		MailProperties.smtpHost = smtpHost;
	}

	public static String getUser() {
		return user;
	}

	public static void setUser(String user) {
		MailProperties.user = user;
	}
}
