/**
 * <p>Description:utility class,includes some methods which are often used</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-06
 */

package com.afunms.common.util;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import com.afunms.system.dao.*;
import com.afunms.system.model.*;

public class SendMailManager 
{
	private String test;
    private MimeMessage mimeMessage = null;
    private String saveAttachPath = "";//�������غ�Ĵ��Ŀ¼
    private StringBuffer bodytext = new StringBuffer(); //����ʼ����ݵ�StringBuffer����
	private String dateformat = "yy-MM-ddHH:mm"; //Ĭ�ϵ���ǰ��ʾ��ʽ
	
	public boolean SendMail(String receivemailaddr,String body)
	{
		String reporttime = ProjectProperties.getDayReporTime();
		AlertEmail vo = null;
		AlertEmailDao emaildao = new AlertEmailDao();
		List list = null;
		try{
			list = emaildao.getByFlage(1);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			emaildao.close();
		}
		if(list != null && list.size()>0)
		{
			vo = (AlertEmail)list.get(0);
		}
		if(vo == null)return false;
		String mailaddr = vo.getUsername();
		String mailpassword = vo.getPassword();
		String mailsmtp = vo.getSmtp();
		SysLogger.info(mailaddr+"==="+mailpassword+"==="+mailsmtp);
		boolean flag = false;
		try{
			Address[] ccAddress = {new InternetAddress("hukelei@dhcc.com.cn"),new InternetAddress("rhythm333@163.com")};
			//String fromAddr = "supergzm@sina.com";
			String fromAddr=vo.getMailAddress();
			sendMail sendmail = new sendMail(mailsmtp,mailaddr,
					mailpassword,receivemailaddr, "���ܷ���澯�ʼ�", body,fromAddr,ccAddress);
			
			try{
				flag = sendmail.sendmail();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return flag;
	}
	public boolean SendMailWithFile(String fromAddress,String receivemailaddr,String body,String fileName)
	{
		return SendMailWithFile(fromAddress, receivemailaddr, "���ܷ���澯�ʼ�", body, fileName);
	}
	
	public boolean SendMailWithFile(String fromAddress,String receivemailaddr,String title,String body,String fileName){
		String reporttime = ProjectProperties.getDayReporTime();
		AlertEmail vo = null;
		AlertEmailDao emaildao = new AlertEmailDao();
		List list = null;
		try{
			list = emaildao.getByFlage(1);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			emaildao.close();
		}
		if(list != null && list.size()>0)
		{
			vo = (AlertEmail)list.get(0);
		}
		if(vo == null)return false;
		String mailaddr = vo.getUsername();
		String mailpassword = vo.getPassword();
		String mailsmtp = vo.getSmtp();
		SysLogger.info(mailaddr+"==="+mailpassword+"==="+mailsmtp);
		boolean flag = false;
		try{
			Address[] ccAddress = {new InternetAddress("hukelei@dhcc.com.cn"),new InternetAddress("rhythm333@163.com")};
			String fromAddr = fromAddress;
			if (fromAddr == null) {
				fromAddr = vo.getMailAddress();
			}
			//String fromAddr="";
			sendMail sendmail = new sendMail(mailsmtp,mailaddr,
					mailpassword,receivemailaddr, title, body,fromAddr,ccAddress);
			
			try{
				flag = sendmail.sendmailWithFile(fileName);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return flag;
	}
	
	public boolean SendMailNoFile(String fromAddress,String receivemailaddr,String body)
	{
		String reporttime = ProjectProperties.getDayReporTime();
		AlertEmail vo = null;
		AlertEmailDao emaildao = new AlertEmailDao();
		List list = null;
		try{
			list = emaildao.getByFlage(1);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			emaildao.close();
		}
		if(list != null && list.size()>0)
		{
			vo = (AlertEmail)list.get(0);
		}
		if(vo == null)return false;
		String mailaddr = vo.getUsername();
		String mailpassword = vo.getPassword();
		String mailsmtp = vo.getSmtp();
		SysLogger.info(mailaddr+"==="+mailpassword+"==="+mailsmtp);
		boolean flag = false;
		try{
			Address[] ccAddress = {new InternetAddress("hukelei@dhcc.com.cn"),new InternetAddress("rhythm333@163.com")};
			String fromAddr = fromAddress;
			//String fromAddr="";
			sendMail sendmail = new sendMail(mailsmtp,mailaddr,
					mailpassword,receivemailaddr, "���ܷ���澯�ʼ�", body,fromAddr,ccAddress);
			
			try{
				flag = sendmail.sendmailNoFile();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return flag;
	}
	
	public boolean SendMyMail(String receivemailaddr,String body)
	{
		String reporttime = ProjectProperties.getDayReporTime();
		AlertEmail vo = null;
		AlertEmailDao emaildao = new AlertEmailDao();
		List list = null;
		try{
			list = emaildao.getByFlage(1);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			emaildao.close();
		}
		if(list != null && list.size()>0)
		{
			vo = (AlertEmail)list.get(0);
		}
		if(vo == null)return false;
		String mailaddr = vo.getUsername();
		String mailpassword = vo.getPassword();
		String mailsmtp = vo.getSmtp();
		SysLogger.info(mailaddr+"==="+mailpassword+"==="+mailsmtp);
		boolean flag = false;
		try{
			Address[] ccAddress = {new InternetAddress("hukelei@dhcc.com.cn"),new InternetAddress("rhythm333@163.com")};
			String fromAddr = "";
			sendMail sendmail = new sendMail(mailsmtp,mailaddr,
					mailpassword,receivemailaddr, "���ܷ���澯�ʼ�", body,fromAddr,ccAddress);
			
			try{
				flag = sendmail.sendmail();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return flag;
	}
	
	
	
	/**
	*���캯��
	*/
	public SendMailManager(){}

}