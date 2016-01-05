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
        // 由于使用了远程调用方式发送短信，故其发送后是否成功的处理在CallBackAction中处理而非直接返回 true
        return false;

    }
}
