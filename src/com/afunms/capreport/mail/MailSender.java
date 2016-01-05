/**
 * @author sunqichang/������
 * Created on 2011-5-12 ����02:44:37
 */
package com.afunms.capreport.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.afunms.initialize.ResourceCenter;

/**
 * ����Mail���͹��ܣ�֧�ֶ฽���������֤
 * 
 * @author sunqichang/������
 * 
 */
public class MailSender {
	private final static Log log = LogFactory.getLog(MailSender.class);

	/**
	 * �ʼ������ļ�·��
	 */
	final private static String mailPath = ResourceCenter.getInstance().getSysPath() + File.separator
			+ "mail.properties";

	/**
	 * ʹ��smtp�����ʼ�
	 * 
	 * @param mail
	 * @throws MessagingException
	 *             mail����ʧ��
	 */
	public static boolean send(MailInfo mail) {
		try {
			Properties properties = MailProperties.getMailproperties(mailPath);
			String auth = properties.getProperty("mail.smtp.auth");
			Session session = null;

			// ����ʼ��Ự����
			if ("true".equalsIgnoreCase(auth)) {
				session = Session.getInstance(properties, new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(MailProperties.getUser(), MailProperties.getPassword());
					}
				});
			} else {
				session = Session.getInstance(properties, null);
			}

			MimeMessage mimeMsg = new MimeMessage(session);// ����MIME�ʼ�����

			if (MailProperties.getSender() != null)// ���÷����˵�ַ
				mimeMsg.setFrom(new InternetAddress(MailProperties.getSender()));

			if (mail.getReceiver() != null)// �����ռ��˵�ַ
				mimeMsg.setRecipients(Message.RecipientType.TO, parse(mail.getReceiver()));

			if (mail.getCopyReceiver() != null)// ���ó����˵�ַ
				mimeMsg.setRecipients(Message.RecipientType.CC, parse(mail.getCopyReceiver()));

			if (mail.getHiddenReceiver() != null)// ���ð����˵�ַ
				mimeMsg.setRecipients(Message.RecipientType.BCC, parse(mail.getHiddenReceiver()));

			if (mail.getSubject() != null)// �����ʼ�����
				mimeMsg.setSubject(mail.getSubject(), "UTF-8");

			MimeBodyPart part = new MimeBodyPart();// mail���ݲ���
			part.setText(mail.getContent() == null ? "" : mail.getContent(), "UTF-8");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(part);// �� Multipart ������mail���ݲ���

			if (!isNull(mail.getAffixPath()))// mail��������
			{
				ArrayList<MimeBodyPart> parts = parsePath(mail.getAffixPath()
				// + mail.getSubject()
				// +
				// mail.getAffixPath().substring(mail.getAffixPath().lastIndexOf("."),
				// mail.getAffixPath().length())
				);
				for (int i = 0; i < parts.size(); i++) {
					multipart.addBodyPart((MimeBodyPart) parts.get(i));
				}
			}

			mimeMsg.setContent(multipart);// ���� Multipart ����Ϣ��
			mimeMsg.setSentDate(new Date());// ���÷�������
			try {
				Transport.send(mimeMsg, mimeMsg.getAllRecipients());// �����ʼ�
			} catch (SendFailedException e) {
				log.error("�ʼ������쳣:", e);
				if (mail != null) {
					log
							.error("������:" + MailProperties.getSender() + "\n" + "�ռ���:" + mail.getReceiver() + "\n"
									+ "����:" + mail.getSubject() + "\n" + "����:" + mail.getContent() + "\n" + "����:"
									+ mail.getAffixPath());
				}
				if (mail.getReceiver().length() > 1 && e.toString().indexOf("<") != -1) {
					resend(e.toString(), mail);
				} else if (mail.getReceiver().length() == 0) {
					return false;
				} else if (e.toString().indexOf("javax.mail.AuthenticationFailedException") != -1) {
					log.error("�����ʼ�ʧ�ܣ������ʼ��������ò���ȷ����˶ԡ�");
					return false;
				}
				return false;
			}
			return true;
		} catch (Exception e) {
			log.error("�ʼ�����ʧ�ܣ�", e);
			return false;
		} finally {
			// if (!isNull(mail.getAffixPath())) {
			// File f = new File(XML_SUBSCRIBE_FILE
			// + mail.getSubject()
			// +
			// mail.getAffixPath().substring(mail.getAffixPath().lastIndexOf("."),
			// mail.getAffixPath().length()));
			// if (f.exists()) {
			// if (f.delete()) {
			// log.info("�ɹ�ɾ��Copy�������ļ���"
			// + mail.getSubject()
			// +
			// mail.getAffixPath().substring(mail.getAffixPath().lastIndexOf("."),
			// mail.getAffixPath().length()));
			// } else {
			// log.info("ɾ��Copy�������ļ���"
			// + mail.getSubject()
			// +
			// mail.getAffixPath().substring(mail.getAffixPath().lastIndexOf("."),
			// mail.getAffixPath().length()) + "���ɹ�");
			// }
			// }
			// }
		}

	}

	/**
	 * ���½�����ַ�����ַ�����ȥ�����Ϸ�email��ַ��
	 * 
	 * @param address
	 * @param mail
	 * @throws AddressException
	 */
	public static void resend(String address, MailInfo mail) throws AddressException {
		String email[] = address.split(">");
		String receiver = mail.getReceiver(); // ԭ����
		String remail[] = receiver.replaceAll(",", ";").split(";"); // ԭ����
		StringBuffer receivers = new StringBuffer();
		int index = 0;// ȡ��<�� ��λ��
		for (int j = 0; j < email.length; j++) {
			if (email[j].indexOf("<") > -1) {
				index = email[j].indexOf("<");
				String errorEmail = email[j].substring(index + 1);
				for (int i = 0; i < remail.length; i++) {
					if (errorEmail.equals(remail[i])) {
						log.error(errorEmail + " �ʼ���ַ������;");
						remail[i] = "error";
					}
				}
			}
		}

		for (int p = 0; p < remail.length; p++) {
			if (!"error".equals(remail[p])) {
				if (p > 0) {
					receivers.append(";" + remail[p]);
				} else {
					receivers.append(remail[p]);
				}
			}
		}
		mail.setReceiver(receivers.toString());
		send(mail);
	}

	/**
	 * ������ַ�����ַ���
	 * 
	 * @param addressSet
	 * @return
	 * @throws AddressException
	 */
	private static InternetAddress[] parse(String addressSet) throws AddressException {
		ArrayList<InternetAddress> list = new ArrayList<InternetAddress>();
		addressSet = addressSet.replaceAll(",", ";");
		StringTokenizer tokens = new StringTokenizer(addressSet, ";");
		while (tokens.hasMoreTokens()) {
			list.add(new InternetAddress(tokens.nextToken().trim()));
		}

		InternetAddress[] addressArray = new InternetAddress[list.size()];
		list.toArray(addressArray);
		return addressArray;
	}

	/**
	 * ��������·���ַ���
	 * 
	 * @param affixPath
	 * @param copyPath
	 * @return
	 * @throws MessagingException
	 */
	private static ArrayList<MimeBodyPart> parsePath(String affixPath) throws MessagingException {
		// fileCopy(affixPath, copyPath);// �����ļ�
		ArrayList<MimeBodyPart> list = new ArrayList<MimeBodyPart>();
		StringTokenizer tokens = new StringTokenizer(affixPath, ";");
		while (tokens.hasMoreTokens()) {
			MimeBodyPart part = new MimeBodyPart();
			FileDataSource source = new FileDataSource(tokens.nextToken().trim());
			part.setDataHandler(new DataHandler(source));
			try {
				part.setFileName(MimeUtility.encodeText(source.getName(), "GB2312", "B"));
			} catch (java.io.UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			list.add(part);
		}
		return list;
	}

	/**
	 * �����ʼ�ʱ�����ļ����Ѹ�������ĳ��������ַ����ʼ�
	 * 
	 * @param affixPath
	 * @param copyPath
	 */
	public static void fileCopy(String affixPath, String copyPath) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			String nullpath = affixPath.substring(0, affixPath.length() - 11); // ���ڶ����ļ�������7λ�Ļ���ID���ɣ����Կ���ȡ���ļ�·��
			String nullname = affixPath.substring(affixPath.length() - 11); // ȡ�ö������ɵ��ļ���
			File file = new File(affixPath);
			File nullfile = new File(nullpath + "nullltable" + nullname);
			int i = 0;
			while (!file.exists()) {
				log.error("�ļ�" + affixPath + "��δ���ɣ��ȴ�" + i + "��");
				if (nullfile.exists()) {
					i = 7;
				}
				if (i > 6) {
					break;
				}
				Thread.sleep(3000);
				i++;
			}
			byte[] iobuff = new byte[1024];
			int bytes;
			fis = new FileInputStream(affixPath);
			fos = new FileOutputStream(copyPath);
			while ((bytes = fis.read(iobuff)) != -1) {
				fos.write(iobuff, 0, bytes);
			}
			if (affixPath != null
					&& copyPath != null
					&& affixPath.substring(0, affixPath.lastIndexOf("\\") + 1).equals(
						copyPath.substring(0, copyPath.lastIndexOf("\\") + 1))) {
				log.info("�ɹ�copy�ļ�" + affixPath.substring(affixPath.lastIndexOf("\\") + 1, affixPath.length())
						+ "������Ϊ��" + copyPath.substring(copyPath.lastIndexOf("\\") + 1, copyPath.length()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * �ж��ַ����Ƿ�Ϊ��
	 * 
	 * @param str
	 * @return true:�ַ���Ϊ�� false:��Ϊ��
	 */
	public static boolean isNull(String str) {
		boolean result = false;
		if (str != null && !"".equals(str) && !"null".equalsIgnoreCase(str) && !"NaN".equalsIgnoreCase(str)) {
			result = false;
		} else {
			result = true;
		}
		return result;
	}
}
