// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   SendSMS.java

package com.sxmcc.sms;

import java.io.PrintStream;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.huilin.tinysoap.client.AlertClient;
import com.huilin.tinysoap.client.SendAlert;

// Referenced classes of package com.sxmcc.sms:
//            Config

public class SendSMS
{
	    static int do_flag = 0;
	    static Logger logger = Logger.getLogger("SMS");
	    static String soap_uri = "";
	    static String sp_id = "";
	  

	    public SendSMS()
	    {
	    	
	        do_flag = 1;
	        soap_uri = Config.getProp("soap_uri");
	        logger.debug("soap_uri:" + soap_uri);
	        sp_id = Config.getProp("sp_id");
	        logger.debug("sp_id:" + sp_id);
	      
	    }
	    
	    
	    public static void sendAlert(String misId[],String title,String content,String flowId[],String nodeId[]) 
	    {

           for(int i=0;i<misId.length;i++){
        	   sendAlert(misId[i], "", title, content, flowId[i], nodeId[i]);
           }


		}
	    
	    public static void sendSms(String content,String[] des) 
	    {

           for(int i=0;i<des.length;i++){
        	   sendSms(content,des[i]);
           }


		}

	public static void sendSms(String content,String phoneNo) {

		String appId =sp_id; // �ɶ�������ƽ̨ͳһ�����Ӧ�ñ�ʶ���൱����ҵid��
	    // content = "���������ݲ��Թ������ݹ������ݲ��Թ������ݲ��Թ�������"; // ��������
		String soapUri =soap_uri;   //"http://10.204.4.38:8080/alert/services/HuilinAlertService";
		String out = null;
		// ���Ͷ��Žӿڵ���
		out = "";
		//String phoneNo = "13834220402";
  	   try{
  		    //logger.debug("׼�����Ͷ���");
System.out.println("================׼�����Ͷ���");  	

	    	out = AlertClient.sendSms(soapUri, appId, content, phoneNo);
System.out.println(out);	    	
	    	//logger.debug("��ɷ��Ͷ���");
	      }catch(Exception e){
	    	  e.printStackTrace();
		     //logger.error("���û��ֶ���ƽ̨������ʧ��ԭ��" + e.toString());
		    
          }
		//logger.debug("�ֻ���:" + phoneNo +"  ���ݣ� "+content+" \n���Žӿڷ���ֵ��"+out);
	    //System.out.println("�ֻ���:" + phoneNo +"  ���ݣ� "+content+" \n���Žӿڷ���ֵ��"+out);

	}
	
	/**
	 * ����soap���ÿͻ���,����wap�Ͷ���
	 * 
	 * @param args
	 */
	public static void sendAlert(String misId,String uid,String title,String content,String flowId,String nodeId) {

		String appId =Config.getProp("sp_id");  // �ɶ�������ƽ̨ͳһ�����Ӧ�ñ�ʶ���൱����ҵid��
		
		// title = "aslkdfalskdf"; // wap push ����
		// content = "�������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ��������Թ������ݲ��Թ������ݲ��Թ�������"; // ��������
		String wapUri = "211.138.109.68/wapoa/rds.jsp?flowId="+flowId.trim()+"&nodeId="+nodeId.trim(); // wap
		// push����
		// String uid = "scwangli"; // �û�id,����¼poral���û���
	    //	String misId = ""; // Ա�����, uid �� misId �ṩһ������ 

		String soapUri = Config.getProp("soap_uri");//"http://10.204.4.38:8080/alert/services/HuilinAlertService";
	
		String out = null;
		// �������ѽӿڵ���
		try{
			logger.debug("׼������WAP");
		   out = AlertClient.sendAlert(soapUri, appId, content, content, wapUri,uid, misId);
		   logger.debug("��ɷ���WAP");
		}catch(Exception e){
			logger.error("���û��ֶ���ƽ̨������ʧ��ԭ��" + e.toString() );	
		}
		logger.debug("����:" + misId+"  ���⣺"+title +"  ���ݣ� "+content+"  \n push����:"+wapUri+"  \n���Žӿڷ���ֵ��"+out);
		// ����ֵ��һ��xml��ʽ���ַ�������ʽ�����ӿڹ淶��
		//System.out.println("����:" + misId+"  ���⣺"+title +"  ���ݣ� "+content+"  \n push����:"+wapUri+"  \n���Žӿڷ���ֵ��"+out);
		System.out
				.println("-----------------------���ѽӿڷ���ֵ����--------------------\n");
		System.out.println(out);

	}

	/**
	 * ����soap���ÿͻ���,����ʾ������
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

	    String appId = "101012"; // �ɶ�������ƽ̨ͳһ�����Ӧ�ñ�ʶ���൱����ҵid��
	    SendSMS ss=new SendSMS();
		String title = "aslkdfalskdf"; // wap push ����
		String content = "�������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ������ݲ��Թ��������Թ������ݲ��Թ������ݲ��Թ�������"; // ��������
		String wapUri = "211.138.109.68/wapoa/rds.jsp?flowId=flow6018601892605466511570_12006444414117812&nodeId=node2042245782870_12006444965172465"; // wap
		// push����
		String uid = "gaojie"; // �û�id,����¼poral���û���
		String misId = ""; // Ա�����, uid �� misId �ṩһ������

		String soapUri = "http://10.204.4.38:8080/alert/services/HuilinAlertService";

		String phoneNo = "13453112535";
		String flowId="232323";
		String nodeId="23232323";
		ss.sendSms("�����ֻ�����",phoneNo);
		//ss.sendAlert(misId, uid, title, content, flowId, nodeId);

	}
	
}
