package com.afunms.alarm.sms.service;

import com.afunms.common.util.SysLogger;

import montnets.SmsServer;

public class SendSMSAlarmByGSMModemService {

    private static SysLogger logger = SysLogger.getLogger(SendSMSAlarmByGSMModemService.class);

    public static boolean send(String mobile, String content) {
        // ��ʼ���Ͷ��Žӿ�
        // ��˾����è��ʼ���Ͷ���
        logger.info("���Ͷ��ţ�" + mobile + "-" + content);
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
        logger.info("���Ͷ��Ÿ澯����..........���Ϊ��" + result);
        return result;
        // ��˾����è�������Ͷ���
    }
}
