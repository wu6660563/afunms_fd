package com.afunms.polling.snmp.linux;

import java.util.Calendar;

import com.afunms.common.util.SysLogger;
import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.Systemcollectdata;

/**
 * Linux CPU 日志解析类
 * 
 * @author 聂林
 */
public class LinuxUptimeByLogFile extends LinuxByLogFile {

    /**
     * 日志
     */
    private static SysLogger logger = SysLogger
            .getLogger(LinuxUptimeByLogFile.class.getName());

    private static final String LINUX_UPTIME_BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_UPTIME_BEGIN_KEYWORD;

    private static final String LINUX_UPTIME_END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_UPTIME_END_KEYWORD;

    @Override
    public ObjectValue getObjectValue() {
        String beginStr = LINUX_UPTIME_BEGIN_KEYWORD;
        String endStr = LINUX_UPTIME_END_KEYWORD;
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
