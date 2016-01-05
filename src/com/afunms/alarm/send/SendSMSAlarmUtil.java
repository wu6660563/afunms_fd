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
     * 读取短信接口配置信息
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
            // 如果使用远程调用的方式，则直接调用远程方法进行发送，本地不再判断
            SendSMSAlarmByRMIService.send(rmiHost, rmiPort, rmiName, rmiAction,
                            userName, mobile, content);
            return result;
        }
        // 如果使用本地调用的方式，需要判断发送的方式，包括短信猫、或者电信之类的短信接口
        if ("modem".equalsIgnoreCase(way)) {
            logger.info("使用短信猫发送接口");
            result = SendSMSAlarmByGSMModemService.send(mobile, content);
        } else if ("DB".equalsIgnoreCase(way)) {
            logger.info("使用电信的数据库接口方式");
            result = SendSMSAlarmByDBService.sendSMS(driver, dbURL, mobile,
                            content);
        } else if ("FDDB".equalsIgnoreCase(way)) {
        	logger.info("使用佛山市供电局的短信平台方式");
        	String db_driver = properties.getProperty("db_driver");
        	String db_url = properties.getProperty("db_url");
        	String db_username = properties.getProperty("db_username");
        	String db_password = properties.getProperty("db_password");
        	String resultBuff = SendSMSAlarmByFDDBService.sendSMS(db_driver, db_url, mobile,
                    content,db_username,db_password);
        	if(resultBuff != null && !"".equals(resultBuff)) {
        		if("0".equals(resultBuff)){
        			logger.info("没检查到正确的手机号码，发送零条");
        			result = false;
        		} else if (resultBuff.startsWith("$")){
        			logger.info("参数检查不通过，发送短信为零条。");
        			result = false;
        		} else if(resultBuff.matches("[0-9]+")){
        			logger.info("成功发送短信"+Integer.parseInt(resultBuff)+"条");
        			result = true;
        		}
        	}
        } else {
            logger.info("暂时不支持该短信发送方式");
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
