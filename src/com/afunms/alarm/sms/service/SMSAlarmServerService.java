package com.afunms.alarm.sms.service;


import com.afunms.alarm.send.SendSMSAlarmUtil;
import com.afunms.rmi.service.RMIAttribute;
import com.afunms.rmi.service.RMIParameter;
import com.afunms.rmi.service.RMIServerHandleAction;



public class SMSAlarmServerService implements RMIServerHandleAction {

    public RMIAttribute execute(RMIParameter parameter) {
        RMIAttribute rmiAttribute = new RMIAttribute();
        String mobile = (String) parameter.getParameter("mobile");
        String content = (String) parameter.getParameter("content");
        String userName = (String) parameter.getParameter("userName");
        boolean result = SendSMSAlarmUtil.sendSMSAlarm(userName, mobile, content);
        rmiAttribute.setAttribute("result", String.valueOf(result));
        return rmiAttribute;
    }

}
