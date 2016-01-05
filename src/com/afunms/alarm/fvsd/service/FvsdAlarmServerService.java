package com.afunms.alarm.fvsd.service;


import com.afunms.rmi.service.RMIAttribute;
import com.afunms.rmi.service.RMIParameter;
import com.afunms.rmi.service.RMIServerHandleAction;



public class FvsdAlarmServerService implements RMIServerHandleAction {

    public RMIAttribute execute(RMIParameter parameter) {
        String fvsdCode = (String) parameter.getParameter("fvsdCode");
        RMIAttribute attribute = new RMIAttribute();
        boolean result = new FvsdAlarmService().deleteByFvsdCode(fvsdCode);
        attribute.setAttribute("result", String.valueOf(result));
        attribute.setAttribute("fvsdCode", String.valueOf(fvsdCode));
        System.out.println(fvsdCode);
        return attribute;
    }

}
