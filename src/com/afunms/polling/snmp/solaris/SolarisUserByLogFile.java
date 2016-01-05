package com.afunms.polling.snmp.solaris;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.SysLogger;
import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.Usercollectdata;

/**
 * Solaris User 日志解析类
 * 
 * @author 聂林
 */
public class SolarisUserByLogFile extends SolarisByLogFile {

    /**
     * 日志
     */
    private static SysLogger logger = SysLogger
            .getLogger(SolarisUserByLogFile.class.getName());

    private static final String SOLARIS_USERGROUP_BEGIN_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_USERGROUP_BEGIN_KEYWORD;

    private static final String SOLARIS_USERGROUP_END_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_USERGROUP_END_KEYWORD;

    private static final String SOLARIS_USER_BEGIN_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_USER_BEGIN_KEYWORD;

    private static final String SOLARIS_USER_END_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_USER_END_KEYWORD;

    @Override
    public ObjectValue getObjectValue() {
        String beginStr = SOLARIS_USERGROUP_BEGIN_KEYWORD;
        String endStr = SOLARIS_USERGROUP_END_KEYWORD;
        String usergroupContent = getLogFileContent(beginStr, endStr);

        String[] usergroupLineArr = usergroupContent.split("\n");

        Hashtable<String, String> usergroupHashtable = new Hashtable<String, String>();
        for (int i = 0; i < usergroupLineArr.length; i++) {
            String[] usergroup_tmpData = usergroupLineArr[i].split(":");
            if (usergroup_tmpData.length >= 3) {
                usergroupHashtable.put((String) usergroup_tmpData[2],
                        usergroup_tmpData[0]);
            }
        }

        String ipaddress = getNodeDTO().getIpaddress();
        Calendar date = getCalendarInstance();
        beginStr = SOLARIS_USER_BEGIN_KEYWORD;
        endStr = SOLARIS_USER_END_KEYWORD;
        String userContent = getLogFileContent(beginStr, endStr);

        String[] userLineArr = userContent.split("\n");

        Vector<Usercollectdata> userVector = new Vector<Usercollectdata>();
        for (int i = 0; i < userLineArr.length; i++) {
            String[] tmpData = userLineArr[i].trim().split(":x:");
            if (tmpData.length>=2) {
                String userName = tmpData[0];
                String usergroupid = tmpData[1];

                String groupname = "";
                if (usergroupHashtable.containsKey(usergroupid)) {
                    groupname = (String) usergroupHashtable.get(usergroupid);
                }
                if (groupname == null || "".equals(groupname)) {
                    groupname = usergroupid;
                }

                Usercollectdata userdata = new Usercollectdata();
                userdata.setIpaddress(ipaddress);
                userdata.setCollecttime(date);
                userdata.setCategory("User");
                userdata.setEntity("Sysuser");
                userdata.setSubentity(groupname);
                userdata.setRestype("static");
                userdata.setUnit(" ");
                userdata.setThevalue(userName);
                userVector.addElement(userdata);
            }
        }

        ObjectValue objectValue = new ObjectValue();
        objectValue.setObjectValue(userVector);
        objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
        return objectValue;
    }
}
