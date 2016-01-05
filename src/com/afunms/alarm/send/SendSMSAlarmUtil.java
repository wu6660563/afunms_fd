package com.afunms.alarm.send;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import montnets.SmsDao;

import com.afunms.alarm.sms.service.SendSMSAlarmByDBService;
import com.afunms.alarm.sms.service.SendSMSAlarmByFDDBService;
import com.afunms.alarm.sms.service.SendSMSAlarmByGSMModemService;
import com.afunms.alarm.sms.service.SendSMSAlarmByRMIService;
import com.afunms.common.util.SysLogger;
import com.afunms.event.model.SendSmsConfig;
import com.afunms.initialize.ResourceCenter;

public class SendSMSAlarmUtil {

    private final static String SMS_PROPERTIES_PATH = "WEB-INF/classes/SMS.properties";

    private static Properties properties = null;

    private static SysLogger logger = SysLogger
                    .getLogger(SendSMSAlarmUtil.class);
    /**
     * ��ȡ���Žӿ�������Ϣ
     */
    static {
        try {
            String proPath = ResourceCenter.getInstance().getSysPath()
                            + SMS_PROPERTIES_PATH;
            // String proPath =
            // "D:/Program Files/apache-tomcat-6.0.30/apache-tomcat-6.0.30/webapps/afunms/"+SMS_PROPERTIES_PATH;
            System.out.println(proPath);
            File file = new File(proPath);
            long l = file.lastModified();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                            new FileInputStream(file), "gb2312"));
            properties = new Properties();
            properties.load(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean sendSMSAlarm(String userName, String mobile,
                    String content) {
        boolean result = false;
        String method = properties.getProperty("method");
        String way = properties.getProperty("way");
        String driver = properties.getProperty("driver");
        String dbURL = properties.getProperty("dbURL");
        String rmiHost = properties.getProperty("rmi_host");
        String rmiPort = properties.getProperty("rmi_port");
        String rmiName = properties.getProperty("rmi_name");
        String rmiAction = properties.getProperty("rmi_action");
        if ("rmi".equalsIgnoreCase(method)) {
            // ���ʹ��Զ�̵��õķ�ʽ����ֱ�ӵ���Զ�̷������з��ͣ����ز����ж�
            SendSMSAlarmByRMIService.send(rmiHost, rmiPort, rmiName, rmiAction,
                            userName, mobile, content);
            return result;
        }
        // ���ʹ�ñ��ص��õķ�ʽ����Ҫ�жϷ��͵ķ�ʽ����������è�����ߵ���֮��Ķ��Žӿ�
        if ("modem".equalsIgnoreCase(way)) {
            logger.info("ʹ�ö���è���ͽӿ�");
            result = SendSMSAlarmByGSMModemService.send(mobile, content);
        } else if ("DB".equalsIgnoreCase(way)) {
            logger.info("ʹ�õ��ŵ����ݿ�ӿڷ�ʽ");
            result = SendSMSAlarmByDBService.sendSMS(driver, dbURL, mobile,
                            content);
        } else if ("FDDB".equalsIgnoreCase(way)) {
        	logger.info("ʹ�÷�ɽ�й���ֵĶ���ƽ̨��ʽ");
        	String db_driver = properties.getProperty("db_driver");
        	String db_url = properties.getProperty("db_url");
        	String db_username = properties.getProperty("db_username");
        	String db_password = properties.getProperty("db_password");
        	String resultBuff = SendSMSAlarmByFDDBService.sendSMS(db_driver, db_url, mobile,
                    content,db_username,db_password);
        	if(resultBuff != null && !"".equals(resultBuff)) {
        		if("0".equals(resultBuff)){
        			logger.info("û��鵽��ȷ���ֻ����룬��������");
        			result = false;
        		} else if (resultBuff.startsWith("$")){
        			logger.info("������鲻ͨ�������Ͷ���Ϊ������");
        			result = false;
        		} else if(resultBuff.matches("[0-9]+")){
        			logger.info("�ɹ����Ͷ���"+Integer.parseInt(resultBuff)+"��");
        			result = true;
        		}
        	}
        } else {
            logger.info("��ʱ��֧�ָö��ŷ��ͷ�ʽ");
        }
        return result;
    }

    // private static boolean localSendSMSAlarm() {
    // return false;
    // }

    public static void saveSendSmsConfig(String userName, String mobile,
                    String content) {
        SendSmsConfig ssc = new SendSmsConfig();
        ssc.setName(userName);
        ssc.setMobilenum(mobile);
        ssc.setEventlist(content);
        SmsDao dao = new SmsDao();
        try {
            dao.save(ssc);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
    }

    public static void main(String[] args) {

    }
}
