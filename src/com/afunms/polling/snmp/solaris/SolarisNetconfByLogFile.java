package com.afunms.polling.snmp.solaris;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.afunms.common.util.SysLogger;
import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;

/**
 * Solaris Netconf 日志解析类
 * 
 * @author 聂林
 */
public class SolarisNetconfByLogFile extends SolarisByLogFile {

    /**
     * 日志
     */
    private static SysLogger logger = SysLogger
                    .getLogger(SolarisNetconfByLogFile.class.getName());

    private static final String SOLARIS_NETCONF_BEGIN_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_NETCONF_BEGIN_KEYWORD;

    private static final String SOLARIS_NETCONF_END_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_NETCONF_END_KEYWORD;

    @Override
    public ObjectValue getObjectValue() {
        String beginStr = SOLARIS_NETCONF_BEGIN_KEYWORD;
        String endStr = SOLARIS_NETCONF_END_KEYWORD;
        String netconfContent = getLogFileContent(beginStr, endStr);

        String[] netconfLineArr = netconfContent.split("\n");
        List<Hashtable<String, String>> netconfList = new ArrayList<Hashtable<String,String>>();
        for (int i = 0; i < netconfLineArr.length; i++) {
            String[] tmpData = netconfLineArr[i].trim().split("\\s++");
            if (tmpData != null && tmpData.length == 8) {
                if (!tmpData[2].equalsIgnoreCase("unknown")) {
                    Hashtable<String, String> netconfhash = new Hashtable<String, String>();
                    netconfhash.put("desc", tmpData[0]);   // 描述
                    netconfhash.put("speed", tmpData[4] + " "
                                    + tmpData[5]);          // 带宽
                    netconfhash.put("mac", "");                    
                    netconfhash.put("status", tmpData[2]); // 连接状态
                    netconfList.add(netconfhash);
                }
            }
        }

        ObjectValue objectValue = new ObjectValue();
        objectValue.setObjectValue(netconfList);
        objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
        return objectValue;
    }
}
