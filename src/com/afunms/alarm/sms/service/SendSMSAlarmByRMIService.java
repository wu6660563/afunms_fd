package com.afunms.alarm.sms.service;

import com.afunms.rmi.service.RMIClientCallBackAction;
import com.afunms.rmi.service.RMIClientService;
import com.afunms.rmi.service.RMIParameter;

public class SendSMSAlarmByRMIService {

    public static boolean send(String rmiHost, String rmiPort, String rmiName,
                    String rmiAction, String userName, String mobile,
                    String content) {
        try {
            RMIParameter parameter = new RMIParameter();
            parameter.setParameter("userName", userName);
            parameter.setParameter("mobile", mobile);
            parameter.setParameter("content", content);
            RMIClientCallBackAction clientCallBackAction = new SMSRMIClientCallBackAction();
            RMIClientService rmiClientService = new RMIClientService();
            if (rmiClientService.connect(rmiHost, rmiPort, rmiName)) {
                rmiClientService.addMessage("sms", parameter,
                                clientCallBackAction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ����ʹ����Զ�̵��÷�ʽ���Ͷ��ţ����䷢�ͺ��Ƿ�ɹ��Ĵ�����CallBackAction�д������ֱ�ӷ��� true
        return false;

    }
}
