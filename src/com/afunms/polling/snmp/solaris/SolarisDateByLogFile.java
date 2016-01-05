package com.afunms.polling.snmp.solaris;

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
import com.afunms.polling.om.Systemcollectdata;

/**
 * Solaris Date 日志解析类
 * 
 * @author 聂林
 */
public class SolarisDateByLogFile extends SolarisByLogFile {

    /**
     * 日志
     */
    private static SysLogger logger = SysLogger
            .getLogger(SolarisDateByLogFile.class.getName());

    private static final String SOLARIS_DATE_BEGIN_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_DATE_BEGIN_KEYWORD;

    private static final String SOLARIS_DATE_END_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_DATE_END_KEYWORD;

    @Override
    public ObjectValue getObjectValue() {
        String beginStr = SOLARIS_DATE_BEGIN_KEYWORD;
        String endStr = SOLARIS_DATE_END_KEYWORD;
        String dateContent = getLogFileContent(beginStr, endStr);
        String ipaddress = getNodeDTO().getIpaddress();
        Calendar date = getCalendarInstance();

        String dateString = "";
        if (dateContent != null && dateContent.length() > 0) {
            dateString = dateContent.trim();
        }

        Systemcollectdata systemdata = new Systemcollectdata();
        systemdata.setIpaddress(ipaddress);
        systemdata.setCollecttime(date);
        systemdata.setCategory("System");
        systemdata.setEntity("Systime");
        systemdata.setSubentity("Systime");
        systemdata.setRestype("static");
        systemdata.setUnit(" ");
        systemdata.setThevalue(dateString);

        ObjectValue objectValue = new ObjectValue();
        objectValue.setObjectValue(systemdata);
        objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
        return objectValue;
    }
}
