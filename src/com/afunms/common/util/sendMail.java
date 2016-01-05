/**
 * <p>Description:utility class,includes some methods which are often used</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-06
 */

package com.afunms.common.util;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

// This function can be used to send the mail
// with the parameters given to it
// U have to specify the smtp server through
// which u have to send the mail
// since i was trying with a homenetmail
// account i directly sent the mail its server
// For sending this mail u need a mail server
// which lets u to relay the messages
// Try this thing for sending to a
// www.homenetmail.com account because it lets
// u send
// mails to the accounts like example try
// sending it to a "abc@homenetmail.com"
// account.Create the mail account in homenet
// mail first. If u get any other server which
// supports relaying u can try this on that
// also.

// Use this function in ur Servlet to send
// mail by calling the function with the
// parameters

public class sendMail {
	private String mailaddress;
	private String sendmail;
	private String sendpasswd;
	private String toAddr;
	private String subject;
	private String body;
	private String fromAddr;
	private Address[] ccAddress;

	/**
	 * @return the mailaddress
	 */
	public String getMailaddress() {
		return mailaddress;
	}

	/**
	 * @param mailaddress
	 *            the mailaddress to set
	 */
	public void setMailaddress(String mailaddress) {
		this.mailaddress = mailaddress;
	}

	/**
	 * @return the sendmail
	 */
	public String getSendmail() {
		return sendmail;
	}

	/**
	 * @param sendmail
	 *            the sendmail to set
	 */
	public void setSendmail(String sendmail) {
		this.sendmail = sendmail;
	}

	/**
	 * @return the sendpasswd
	 */
	public String getSendpasswd() {
		return sendpasswd;
	}

	/**
	 * @param sendpasswd
	 *            the sendpasswd to set
	 */
	public void setSendpasswd(String sendpasswd) {
		this.sendpasswd = sendpasswd;
	}

	/**
	 * @return the toAddr
	 */
	public String getToAddr() {
		return toAddr;
	}

	/**
	 * @param toAddr
	 *            the toAddr to set
	 */
	public void setToAddr(String toAddr) {
		this.toAddr = toAddr;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body
	 *            the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the fromAddr
	 */
	public String getFromAddr() {
		return fromAddr;
	}

	/**
	 * @param fromAddr
	 *            the fromAddr to set
	 */
	public void setFromAddr(String fromAddr) {
		this.fromAddr = fromAddr;
	}

	/**
	 * @return the ccAddress
	 */
	public Address[] getCcAddress() {
		return ccAddress;
	}

	/**
	 * @param ccAddress
	 *            the ccAddress to set
	 */
	public void setCcAddress(Address[] ccAddress) {
		this.ccAddress = ccAddress;
	}

	public sendMail() {
	}

	public sendMail(String mailaddress, String sendmail, String sendpasswd,
			String toAddr, String subject, String body, String fromAddr,
			Address[] ccAddress) {
		super();
		this.mailaddress = mailaddress;
		this.sendmail = sendmail;
		this.sendpasswd = sendpasswd;
		this.toAddr = toAddr;
		this.subject = subject;
		this.body = body;
		this.fromAddr = fromAddr;
		this.ccAddress = ccAddress;

	}
	public boolean sendmailWithFile(String fileName) 
	{
		try{
			System.out.println("###################toAddr#######################################"+toAddr);
			System.out.println("###################subject#######################################"+subject);
			System.out.println("###################body#######################################"+body);
			System.out.println("###################fromAddr#######################################"+fromAddr);
			System.out.println("###################sendmail#######################################"+sendmail);
			System.out.println("###################sendpasswd#######################################"+sendpasswd);
			System.out.println("###################mailaddress#######################################"+mailaddress);
			this.postMail(new String[]{toAddr}, subject, body, fromAddr, sendmail, sendpasswd, mailaddress,fileName);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 
	 * @param recipients �ʼ������ˣ���"konglingqi@dhcc.com.cn"
	 * @param subject �ʼ�����
	 * @param message �ʼ�����
	 * @param from �ʼ������˵�ַ����"guzhiming@dhcc.com.cn"
	 * @param emailUserName ���ʼ��� ��¼���� ��ʹ�õ��û�������"guzhiming@dhcc.com.cn"
	 * @param emailPwd ���ʼ��� ��¼���� ��ʹ�õ��û��� ��Ӧ������
	 * @param smtpHost ����smtp��ַ�����ʹ��dhcc���䷢���ʼ�����ò�����"mail.dhcc.com.cn"
	 * @throws MessagingException
	 */
	public void postMail( String recipients[ ], String subject,String message , String from,String emailUserName,String emailPwd,String smtpHost,String fileName) throws MessagingException
	{
		try
		{
			boolean debug = false;

			//Set the host smtp address
			Properties props = new Properties();
			props.put("mail.smtp.host", smtpHost);
			props.put("mail.smtp.auth", "true");

			Authenticator auth = new SMTPAuthenticator(emailUserName,emailPwd);
			Session session = Session.getDefaultInstance(props, auth);

			session.setDebug(debug);

			// create a message
			MimeMessage msg = new MimeMessage(session);
			msg.setSubject(subject,"GB2312");
			// set the from and to address
			InternetAddress addressFrom = new InternetAddress(from);
			msg.setFrom(addressFrom);

			InternetAddress[] addressTo = new InternetAddress[recipients.length];
			for (int i = 0; i < recipients.length; i++)
			{
				addressTo[i] = new InternetAddress(recipients[i]);
			}
			msg.setRecipients(Message.RecipientType.TO, addressTo);


			// Setting the Subject and Content Type
			//msg.setSubject(subject);
			//msg.setContent(message, "text/plain;charset=gb2312");
			//Transport.send(msg);
			MimeMultipart multi = new MimeMultipart();
			// ����
			// BodyPart����Ҫ�����ǽ��Ժ󴴽���n�����ݼ���MimeMultipart.Ҳ���ǿ��Է�n����������������2��BodyPart.
			MimeBodyPart textBodyPart = new MimeBodyPart(); // ��һ��BodyPart.��ҪдһЩһ����ż����ݡ�
			textBodyPart.setContent(message, "text/plain;charset=GB2312");
			// ѹ���һ��BodyPart��MimeMultipart�����С�
			multi.addBodyPart(textBodyPart);


			if(fileName!=null)
			{
				FileDataSource fds = new FileDataSource(fileName); // ������ڵ��ĵ�������throw�쳣��
				BodyPart fileBodyPart = new MimeBodyPart(); // �ڶ���BodyPart
				fileBodyPart.setDataHandler(new DataHandler(fds)); // �ַ�����ʽװ���ļ�
				fileBodyPart.setFileName("report.xls"); // �����ļ��������Բ���ԭ�����ļ�����
				multi.addBodyPart(fileBodyPart);
			}

			// MimeMultPart��ΪContent����message
			msg.setContent(multi, "text/plain;charset=gb2312");
			msg.saveChanges();
			Transport.send(msg);
			System.out.println("#########################�ʼ��������######################################");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public boolean sendmailNoFile()
	{
		try{
			this.postMail(new String[]{toAddr}, subject, body, fromAddr, sendmail, sendpasswd, mailaddress,null);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	/**
	 * sendmailHaveFile:wupinlong ���฽�����ʼ����ͣ�path��fileNameһһ��Ӧ����
	 * <p>
	 *
	 * @param path·������
	 * @param fileName�ļ������飬��path·��һһ��Ӧ
	 * @return
	 *
	 * @since   v1.01
	 */
	public boolean sendmailHaveFile(String[] path,String[] fileName) 
	{
		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", mailaddress); // set host
			props.put("mail.smtp.auth", "true"); // set auth
			MyAuthenticator auth = new MyAuthenticator(sendmail, sendpasswd);
			Session sendMailSession = Session.getInstance(props, auth);
			// sendMailSession.setDebug(true);

			// ���� �ʼ���message��message����������ʼ��ڶ��еĲ��������Ƿ�װ����set����ȥ���õ�
			MimeMessage message = new MimeMessage(sendMailSession);
			// ���÷�����
			// message.setFrom(new InternetAddress("chqn@cmmail.com"));
			// message.setFrom( new
			// InternetAddress(sendmail+"@"+mailaddress.substring(5,
			// mailaddress.length())));
			message.setFrom(new InternetAddress(fromAddr));
			////message.setFrom(new InternetAddress(fromAddr));/////maybe there is a bug here   GZM
			System.out.println(fromAddr + "\t" + toAddr);
			// ������
			// message.setRecipient(Message.RecipientType.TO,new
			// InternetAddress("chqn@cmmail.com"));
			
			//Message.RecipientType.TO�ʼ�������	Message.RecipientType.BCC������	Message.RecipientType.CC����
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddr));
//			message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(toAddr));
			// �ʼ�����
			// message.setSubject("I love you"); //haha���Ż���
			message.setSubject(subject);
			message.setSentDate(new Date());
			// ����
			// Mimemultipart�����ǰ�����������Ǳ��봴���ġ����ֻ��һ�����ݣ�û�и���������ֱ����message.setText(String
			// str)
			// ȥд�ŵ����ݣ��ȽϷ��㡣����������Ҫ����������ݣ����¿���������
			MimeMultipart multi = new MimeMultipart();
			// ����
			// BodyPart����Ҫ�����ǽ��Ժ󴴽���n�����ݼ���MimeMultipart.Ҳ���ǿ��Է�n����������������2��BodyPart.
			BodyPart textBodyPart = new MimeBodyPart(); // ��һ��BodyPart.��ҪдһЩһ����ż����ݡ�
			textBodyPart.setText(body);
			// ѹ���һ��BodyPart��MimeMultipart�����С�
			multi.addBodyPart(textBodyPart);
			// �����ڶ���BodyPart,��һ��FileDataSource
			// File tempFile = new File("temp.htm");

			for (int i = 0; i < fileName.length; i++) {
				// tempFile.
				FileDataSource fds = new FileDataSource(path[i] + fileName[i]); // ������ڵ��ĵ�������throw�쳣��
				BodyPart fileBodyPart = new MimeBodyPart(); // �ڶ���BodyPart
				fileBodyPart.setDataHandler(new DataHandler(fds)); // �ַ�����ʽװ���ļ�
				fileBodyPart.setFileName(fileName[i]); // �����ļ��������Բ���ԭ�����ļ�����
				// �����ˣ�ͬ��һ��BodyPart.
				multi.addBodyPart(fileBodyPart);
			}

			// MimeMultPart��ΪContent����message
			message.setContent(multi);
			// �������ϵĹ������뱣�档
			message.saveChanges();
			// ���ͣ�����Transport�࣬����SMTP���ʼ�����Э�飬
			Transport transport = sendMailSession.getTransport("smtp");
			transport.connect(mailaddress, sendmail, sendpasswd);
			transport.sendMessage(message, message.getAllRecipients());
			// transport.send(message);
			transport.close();
		} catch (Exception exc) {
			exc.printStackTrace();
			return false;
		}

		return true;
	}
	
	public boolean sendmail() 
	{
		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", mailaddress); // set host
			props.put("mail.smtp.auth", "true"); // set auth
			MyAuthenticator auth = new MyAuthenticator(sendmail, sendpasswd);
			Session sendMailSession = Session.getInstance(props, auth);
			// sendMailSession.setDebug(true);

			// ���� �ʼ���message��message����������ʼ��ڶ��еĲ��������Ƿ�װ����set����ȥ���õ�
			MimeMessage message = new MimeMessage(sendMailSession);
			// ���÷�����
			// message.setFrom(new InternetAddress("chqn@cmmail.com"));
			// message.setFrom( new
			// InternetAddress(sendmail+"@"+mailaddress.substring(5,
			// mailaddress.length())));
			message.setFrom(new InternetAddress(fromAddr));
			////message.setFrom(new InternetAddress(fromAddr));/////maybe there is a bug here   GZM
			SysLogger.info(fromAddr + "\t" + toAddr);
			// ������
			// message.setRecipient(Message.RecipientType.TO,new
			// InternetAddress("chqn@cmmail.com"));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(
					toAddr));
			// �ʼ�����
			// message.setSubject("I love you"); //haha���Ż���
			message.setSubject(subject);
			message.setSentDate(new Date());
			// ����
			// Mimemultipart�����ǰ�����������Ǳ��봴���ġ����ֻ��һ�����ݣ�û�и���������ֱ����message.setText(String
			// str)
			// ȥд�ŵ����ݣ��ȽϷ��㡣����������Ҫ����������ݣ����¿���������
			MimeMultipart multi = new MimeMultipart();
			// ����
			// BodyPart����Ҫ�����ǽ��Ժ󴴽���n�����ݼ���MimeMultipart.Ҳ���ǿ��Է�n����������������2��BodyPart.
			BodyPart textBodyPart = new MimeBodyPart(); // ��һ��BodyPart.��ҪдһЩһ����ż����ݡ�
			textBodyPart.setText(body);
			// ѹ���һ��BodyPart��MimeMultipart�����С�
			multi.addBodyPart(textBodyPart);
			// �����ڶ���BodyPart,��һ��FileDataSource
			// File tempFile = new File("temp.htm");

			FileWriter fw = new FileWriter("aaa.doc");
			PrintWriter pw = new PrintWriter(fw);
			pw.println(body);
			pw.close();
			fw.close();

			// tempFile.

			FileDataSource fds = new FileDataSource("aaa.doc"); // ������ڵ��ĵ�������throw�쳣��
			BodyPart fileBodyPart = new MimeBodyPart(); // �ڶ���BodyPart
			fileBodyPart.setDataHandler(new DataHandler(fds)); // �ַ�����ʽװ���ļ�
			fileBodyPart.setFileName("alarm.doc"); // �����ļ��������Բ���ԭ�����ļ�����
			// �����ˣ�ͬ��һ��BodyPart.
			multi.addBodyPart(fileBodyPart);

			// MimeMultPart��ΪContent����message
			message.setContent(multi);
			// �������ϵĹ������뱣�档
			message.saveChanges();
			// ���ͣ�����Transport�࣬����SMTP���ʼ�����Э�飬
			Transport transport = sendMailSession.getTransport("smtp");
			transport.connect(mailaddress, sendmail, sendpasswd);
			transport.sendMessage(message, message.getAllRecipients());
			// transport.send(message);
			transport.close();
		} catch (Exception exc) {
		    SysLogger.error("���͸��ʼ�" + toAddr + "ʧ��", exc);
			return false;
		}

		return true;
	}
	
	public static void sendMail(String toAddr, String subject, String body,String fromAddr, Address[] ccAddress) throws RemoteException 
	{
		try {
			try {
				Properties props = new Properties();
				props.put("mail.smtp.host", "smtp.sohu.com"); // set host
				props.put("mail.smtp.auth", "true"); // set auth
				MyAuthenticator auth = new MyAuthenticator("donhukelei",
						"hukelei");
				Session sendMailSession = Session.getInstance(props, auth);
				sendMailSession.setDebug(true);

				// ���� �ʼ���message��message����������ʼ��ڶ��еĲ��������Ƿ�װ����set����ȥ���õ�
				MimeMessage message = new MimeMessage(sendMailSession);
				// ���÷�����
				// message.setFrom(new InternetAddress("chqn@cmmail.com"));
				message.setFrom(new InternetAddress(fromAddr));
				// ������
				// message.setRecipient(Message.RecipientType.TO,new
				// InternetAddress("chqn@cmmail.com"));
				message.setRecipient(Message.RecipientType.TO,new InternetAddress(toAddr));
				// �ʼ�����
				// message.setSubject("I love you"); //haha���Ż���
				message.setSubject(subject);
				message.setSentDate(new Date());
				// ����
				// Mimemultipart�����ǰ�����������Ǳ��봴���ġ����ֻ��һ�����ݣ�û�и���������ֱ����message.setText(String
				// str)
				// ȥд�ŵ����ݣ��ȽϷ��㡣����������Ҫ����������ݣ����¿���������
				MimeMultipart multi = new MimeMultipart();
				// ����
				// BodyPart����Ҫ�����ǽ��Ժ󴴽���n�����ݼ���MimeMultipart.Ҳ���ǿ��Է�n����������������2��BodyPart.
				BodyPart textBodyPart = new MimeBodyPart(); // ��һ��BodyPart.��ҪдһЩһ����ż����ݡ�
				textBodyPart.setText("���������");
				// ѹ���һ��BodyPart��MimeMultipart�����С�
				multi.addBodyPart(textBodyPart);
				// �����ڶ���BodyPart,��һ��FileDataSource
				// File tempFile = new File("temp.htm");
				FileWriter fw = new FileWriter("aaa.html");
				PrintWriter pw = new PrintWriter(fw);
				pw.println(body);
				pw.close();
				fw.close();

				// tempFile.

				FileDataSource fds = new FileDataSource("aaa.html"); // ������ڵ��ĵ�������throw�쳣��
				BodyPart fileBodyPart = new MimeBodyPart(); // �ڶ���BodyPart
				fileBodyPart.setDataHandler(new DataHandler(fds)); // �ַ�����ʽװ���ļ�
				fileBodyPart.setFileName("fujian.html"); // �����ļ��������Բ���ԭ�����ļ�����
				// �����ˣ�ͬ��һ��BodyPart.
				multi.addBodyPart(fileBodyPart);
				// MimeMultPart��ΪContent����message
				message.setContent(multi);
				// �������ϵĹ������뱣�档
				message.saveChanges();
				// ���ͣ�����Transport�࣬����SMTP���ʼ�����Э�飬
				Transport transport = sendMailSession.getTransport("smtp");
				transport.connect("smtp.163.com", "rhythm333", "hukelei");
				transport.sendMessage(message, message.getAllRecipients());
				// transport.send(message);
				transport.close();

			} catch (Exception exc) {
				exc.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void sendMyMail(String toAddr, String subject, String body,String fromAddr, Address[] ccAddress)
	{
		try {
			try {
				Properties props = new Properties();
				props.put("mail.smtp.host", "smtp.sina.com.cn"); // set host
				props.put("mail.smtp.auth", "true"); // set auth
				MyAuthenticator auth = new MyAuthenticator("supergzm",
						"6400891gzm");
				Session sendMailSession = Session.getInstance(props, auth);
				sendMailSession.setDebug(true);

				// ���� �ʼ���message��message����������ʼ��ڶ��еĲ��������Ƿ�װ����set����ȥ���õ�
				MimeMessage message = new MimeMessage(sendMailSession);
				// ���÷�����
				// message.setFrom(new InternetAddress("chqn@cmmail.com"));
				message.setFrom(new InternetAddress(fromAddr));
				// ������
				// message.setRecipient(Message.RecipientType.TO,new
				// InternetAddress("chqn@cmmail.com"));
				message.setRecipient(Message.RecipientType.TO,new InternetAddress(toAddr));
				// �ʼ�����
				// message.setSubject("I love you"); //haha���Ż���
				message.setSubject(subject);
				message.setSentDate(new Date());
				// ����
				// Mimemultipart�����ǰ�����������Ǳ��봴���ġ����ֻ��һ�����ݣ�û�и���������ֱ����message.setText(String
				// str)
				// ȥд�ŵ����ݣ��ȽϷ��㡣����������Ҫ����������ݣ����¿���������
				MimeMultipart multi = new MimeMultipart();
				// ����
				// BodyPart����Ҫ�����ǽ��Ժ󴴽���n�����ݼ���MimeMultipart.Ҳ���ǿ��Է�n����������������2��BodyPart.
				BodyPart textBodyPart = new MimeBodyPart(); // ��һ��BodyPart.��ҪдһЩһ����ż����ݡ�
				textBodyPart.setText("���������");
				// ѹ���һ��BodyPart��MimeMultipart�����С�
				multi.addBodyPart(textBodyPart);
				// �����ڶ���BodyPart,��һ��FileDataSource
				// File tempFile = new File("temp.htm");
				FileWriter fw = new FileWriter("aaa.html");
				PrintWriter pw = new PrintWriter(fw);
				pw.println(body);
				pw.close();
				fw.close();

				// tempFile.

				FileDataSource fds = new FileDataSource("aaa.html"); // ������ڵ��ĵ�������throw�쳣��
				BodyPart fileBodyPart = new MimeBodyPart(); // �ڶ���BodyPart
				fileBodyPart.setDataHandler(new DataHandler(fds)); // �ַ�����ʽװ���ļ�
				fileBodyPart.setFileName("fujian.html"); // �����ļ��������Բ���ԭ�����ļ�����
				// �����ˣ�ͬ��һ��BodyPart.
				multi.addBodyPart(fileBodyPart);
				// MimeMultPart��ΪContent����message
				message.setContent(multi);
				// �������ϵĹ������뱣�档
				message.saveChanges();
				// ���ͣ�����Transport�࣬����SMTP���ʼ�����Э�飬
				Transport transport = sendMailSession.getTransport("smtp");
				transport.connect("smtp.163.com", "rhythm333", "hukelei");
				transport.sendMessage(message, message.getAllRecipients());
				// transport.send(message);
				transport.close();

			} catch (Exception exc) {
				exc.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		try{
			new sendMail().postMail( new String[]{"wu6660563@126.com"}, "�������","hello,world!" , "wu6660563@163.com","wu6660563@163.com","mfscn6660563", "smtp.163.com",null);
			//postMail( String recipients[ ], String subject,String message , String from,String emailUserName,String emailPwd,String smtpHost,String fileName)
			//postMail( new String[]{"supergzm@sina.com"}, "test mail", "hello java" , "supergzm@sina.com");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
class SMTPAuthenticator extends javax.mail.Authenticator
{
	String auth_user = null;
	String auth_pwd = null;
	public SMTPAuthenticator(String auth_user,String auth_pwd)
	{
		this.auth_user = auth_user;
		this.auth_pwd = auth_pwd;
	}
    public PasswordAuthentication getPasswordAuthentication()
    {
        return new PasswordAuthentication(auth_user, auth_pwd);
    }
}
