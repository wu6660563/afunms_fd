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
import com.afunms.polling.om.Systemcollectdata;

/**
 * Linux UName 日志解析类
 * 
 * @author 聂林
 */
public class LinuxUNameByLogFile extends LinuxByLogFile {

    /**
     * 日志
     */
    private static SysLogger logger = SysLogger
            .getLogger(LinuxUNameByLogFile.class.getName());

    private static final String LINUX_UNAME_BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_UNAME_BEGIN_KEYWORD;

    private static final String LINUX_UNAME_END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_UNAME_END_KEYWORD;

    @Override
    public ObjectValue getObjectValue() {
        String beginStr = LINUX_UNAME_BEGIN_KEYWORD;
        String endStr = LINUX_UNAME_END_KEYWORD;
        String unameContent = getLogFileContent(beginStr, endStr);
        String ipaddress = getNodeDTO().getIpaddress();
        Calendar date = getCalendarInstance();

        Vector<Systemcollectdata> systemVector = new Vector<Systemcollectdata>();
        String[] unameLineArr = unameContent.split("\n");
        for (int i = 0; i < unameLineArr.length; i++) {
            String[] uname_tmpData = unameLineArr[i].split("\\s++");
            if (uname_tmpData.length == 2) {
                String operatSystem = uname_tmpData[0];
                String sysName = uname_tmpData[1];

                Systemcollectdata systemdata = new Systemcollectdata();
                systemdata.setIpaddress(ipaddress);
                systemdata.setCollecttime(date);
                systemdata.setCategory("System");
                systemdata.setEntity("operatSystem");
                systemdata.setSubentity("operatSystem");
                systemdata.setRestype("static");
                systemdata.setUnit(" ");
                systemdata.setThevalue(operatSystem);
                systemVector.addElement(systemdata);

                systemdata = new Systemcollectdata();
                systemdata.setIpaddress(ipaddress);
                systemdata.setCollecttime(date);
                systemdata.setCategory("System");
                systemdata.setEntity("SysName");
                systemdata.setSubentity("SysName");
                systemdata.setRestype("static");
                systemdata.setUnit(" ");
                systemdata.setThevalue(sysName);
                systemVector.addElement(systemdata);

            }
        }
        ObjectValue objectValue = new ObjectValue();
        objectValue.setObjectValue(systemVector);
        objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
        return objectValue;
    }
}
