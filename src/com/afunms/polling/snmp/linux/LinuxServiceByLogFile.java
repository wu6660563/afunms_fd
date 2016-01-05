package com.afunms.polling.snmp.linux;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.common.util.Arith;
import com.afunms.common.util.SysLogger;
import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.CPUcollectdata;

/**
 * Linux Service ��־������
 * 
 * @author ����
 */
public class LinuxServiceByLogFile extends LinuxByLogFile {

    /**
     * ��־
     */
    private static SysLogger logger = SysLogger
            .getLogger(LinuxServiceByLogFile.class.getName());

    private static final String LINUX_SERVICE_BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_SERVICE_BEGIN_KEYWORD;

    private static final String LINUX_SERVICE_END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_SERVICE_END_KEYWORD;

    @Override
    public ObjectValue getObjectValue() {
        String beginStr = LINUX_SERVICE_BEGIN_KEYWORD;
        String endStr = LINUX_SERVICE_END_KEYWORD;
        String serviceContent = getLogFileContent(beginStr, endStr);

        String[] serviceLineArr = serviceContent.split("\n");

        List<Hashtable<String, String>> servicelist = new ArrayList<Hashtable<String, String>>();
        for (int i = 0; i < serviceLineArr.length; i++) {
            String[] tmpData = serviceLineArr[i].trim().split("\\s++");
            if (tmpData.length == 8) {
                Hashtable<String, String> service = new Hashtable<String, String>();
                String name = tmpData[0];
                String servicestatus = tmpData[5];
                String status = "δ����";
                if (servicestatus != null
                        && (servicestatus.indexOf("on") >= 0 || servicestatus
                                .indexOf("����") >= 0)) {
                    status = "����";
                }
                service.put("name", name);
                service.put("status", status);
                servicelist.add(service);
            }
        }
        ObjectValue objectValue = new ObjectValue();
        objectValue.setObjectValue(servicelist);
        objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
        return objectValue;
    }
}
