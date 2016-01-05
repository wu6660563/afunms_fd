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
 * Solaris UName 日志解析类
 * 
 * @author 聂林
 */
public class SolarisUNameByLogFile extends SolarisByLogFile {

    /**
     * 日志
     */
    private static SysLogger logger = SysLogger
                    .getLogger(SolarisUNameByLogFile.class.getName());

    private static final String SOLARIS_UNAME_BEGIN_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_UNAME_BEGIN_KEYWORD;

    private static final String SOLARIS_UNAME_END_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_UNAME_END_KEYWORD;

    @Override
    public ObjectValue getObjectValue() {
        String beginStr = SOLARIS_UNAME_BEGIN_KEYWORD;
        String endStr = SOLARIS_UNAME_END_KEYWORD;
        String unameContent = getLogFileContent(beginStr, endStr);
        String ipaddress = getNodeDTO().getIpaddress();
        Calendar date = getCalendarInstance();

        Vector<Systemcollectdata> systemVector = new Vector<Systemcollectdata>();
        String[] unameLineArr = unameContent.split("\n");
        for (int i = 0; i < unameLineArr.length; i++) {
            String[] tmpData = unameLineArr[i].split("\\s++");
            if (tmpData.length == 2) {
                String operatSystem = tmpData[0];
                String sysName = tmpData[1];

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
