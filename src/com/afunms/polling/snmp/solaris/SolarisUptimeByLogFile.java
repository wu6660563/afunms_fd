package com.afunms.polling.snmp.solaris;

import java.util.Calendar;

import com.afunms.common.util.SysLogger;
import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.Systemcollectdata;

/**
 * Solaris CPU 日志解析类
 * 
 * @author 聂林
 */
public class SolarisUptimeByLogFile extends SolarisByLogFile {

    /**
     * 日志
     */
    private static SysLogger logger = SysLogger
            .getLogger(SolarisUptimeByLogFile.class.getName());

    private static final String SOLARIS_UPTIME_BEGIN_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_UPTIME_BEGIN_KEYWORD;

    private static final String SOLARIS_UPTIME_END_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_UPTIME_END_KEYWORD;

    @Override
    public ObjectValue getObjectValue() {
        String beginStr = SOLARIS_UPTIME_BEGIN_KEYWORD;
        String endStr = SOLARIS_UPTIME_END_KEYWORD;
        String uptimeContent = getLogFileContent(beginStr, endStr);
        String ipaddress = getNodeDTO().getIpaddress();
        Calendar date = getCalendarInstance();

        if (uptimeContent == null) {
            uptimeContent = "";
        }

        Systemcollectdata systemdata = new Systemcollectdata();
        systemdata.setIpaddress(ipaddress);
        systemdata.setCollecttime(date);
        systemdata.setCategory("System");
        systemdata.setEntity("SysUptime");
        systemdata.setSubentity("SysUptime");
        systemdata.setRestype("static");
        systemdata.setUnit(" ");
        systemdata.setThevalue(uptimeContent);

        ObjectValue objectValue = new ObjectValue();
        objectValue.setObjectValue(systemdata);
        objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
        return objectValue;
    }
}
