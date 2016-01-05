package com.afunms.polling.snmp.linux;

import java.util.Calendar;
import java.util.Hashtable;

import com.afunms.common.util.SysLogger;
import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.Systemcollectdata;

/**
 * Linux MAC 日志解析类
 * 
 * @author 聂林
 */
public class LinuxMacByLogFile extends LinuxByLogFile {

    /**
     * 日志
     */
    private static SysLogger logger = SysLogger
            .getLogger(LinuxMacByLogFile.class.getName());

    private static final String LINUX_MAC_BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_MAC_BEGIN_KEYWORD;

    private static final String LINUX_MAC_END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_MAC_END_KEYWORD;

    @Override
    public ObjectValue getObjectValue() {
        String beginStr = LINUX_MAC_BEGIN_KEYWORD;
        String endStr = LINUX_MAC_END_KEYWORD;
        String macContent = getLogFileContent(beginStr, endStr);
        String ipaddress = getNodeDTO().getIpaddress();
        Calendar date = getCalendarInstance();

        String[] macLineArr = macContent.split("\n");
        String MAC = "";
        Hashtable<String, String> machash = new Hashtable<String, String>();
        boolean isFirst = true;
        for (int i = 0; i < macLineArr.length; i++) {
            String[] mac_tmpData = macLineArr[i].trim().split("\\s++");
            if (mac_tmpData.length == 4) {
                if (mac_tmpData[0].equalsIgnoreCase("link/ether")
                        && mac_tmpData[2].equalsIgnoreCase("brd")) {
                    String macPer = mac_tmpData[1].toLowerCase();
                    if (macPer.equalsIgnoreCase("00:00:00:00:00:00")) {
                        continue;
                    }
                    if (machash.containsKey(macPer)) {
                        continue;
                    }
                    machash.put(macPer, macPer);
                    if (!isFirst) {
                        MAC += ",";
                    }
                    MAC += macPer;
                    isFirst = false;
                }
            }
        }
        Systemcollectdata systemdata = new Systemcollectdata();
        systemdata.setIpaddress(ipaddress);
        systemdata.setCollecttime(date);
        systemdata.setCategory("System");
        systemdata.setEntity("MacAddr");
        systemdata.setSubentity("MacAddr");
        systemdata.setRestype("static");
        systemdata.setUnit(" ");
        systemdata.setThevalue(MAC);

        ObjectValue objectValue = new ObjectValue();
        objectValue.setObjectValue(systemdata);
        objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
        return objectValue;
    }
}
