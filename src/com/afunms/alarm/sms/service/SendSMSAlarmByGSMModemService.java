package com.afunms.alarm.sms.service;

import com.afunms.common.util.SysLogger;

import montnets.SmsServer;

public class SendSMSAlarmByGSMModemService {

    private static SysLogger logger = SysLogger.getLogger(SendSMSAlarmByGSMModemService.class);

    public static boolean send(String mobile, String content) {
        // 开始发送短信接口
        // 公司短信猫开始发送短信
        logger.info("发送短信：" + mobile + "-" + content);
        boolean result = false;
        try {
            SmsServer ss = new SmsServer();
            int rc = ss.sendSMS(mobile, content);
            if (rc >= 0) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("发送短信告警结束..........结果为：" + result);
        return result;
        // 公司短信猫结束发送短信
    }
}
