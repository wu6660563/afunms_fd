/**
 * 
 */
package com.afunms.alarm.sms.service;

import com.afunms.alarm.send.SendSMSAlarmUtil;
import com.afunms.common.util.SysLogger;
import com.afunms.rmi.service.RMIAttribute;
import com.afunms.rmi.service.RMIClientCallBackAction;
import com.afunms.rmi.service.RMIParameter;

/**
 * @author hkmw
 *
 */
public class SMSRMIClientCallBackAction implements RMIClientCallBackAction {

    private static SysLogger logger = SysLogger.getLogger(SMSRMIClientCallBackAction.class);
    /**
     * 
     */
    private static final long serialVersionUID = -1885543836447639319L;

    /* (non-Javadoc)
     * @see com.afunms.rmi.service.RMIClientCallBackAction#callBack(com.afunms.rmi.service.RMIParameter, com.afunms.rmi.service.RMIAttribute)
     */
    public void callBack(RMIParameter parameter, RMIAttribute attribute) {
        try {
            String mobile = (String) parameter.getParameter("mobile");
            String content = (String) parameter.getParameter("content");
            String userName = (String) parameter.getParameter("userName");
            String result = (String) attribute.getAttribute("result");
            logger.info("调用远程发送告警成功，返回发送结果：" + result);
            if (Boolean.valueOf(result)) {
                SendSMSAlarmUtil.saveSendSmsConfig(userName, mobile, content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
    }

}
