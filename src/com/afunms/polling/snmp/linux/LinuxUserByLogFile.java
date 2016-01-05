package com.afunms.polling.snmp.linux;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.SysLogger;
import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.Usercollectdata;

/**
 * Linux User 日志解析类
 * 
 * @author 聂林
 */
public class LinuxUserByLogFile extends LinuxByLogFile {

    /**
     * 日志
     */
    private static SysLogger logger = SysLogger
            .getLogger(LinuxUserByLogFile.class.getName());

    private static final String LINUX_USERGROUP_BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_USERGROUP_BEGIN_KEYWORD;

    private static final String LINUX_USERGROUP_END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_USERGROUP_END_KEYWORD;

    private static final String LINUX_USER_BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_USER_BEGIN_KEYWORD;

    private static final String LINUX_USER_END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_USER_END_KEYWORD;

    @Override
    public ObjectValue getObjectValue() {
        String beginStr = LINUX_USERGROUP_BEGIN_KEYWORD;
        String endStr = LINUX_USERGROUP_END_KEYWORD;
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
        beginStr = LINUX_USER_BEGIN_KEYWORD;
        endStr = LINUX_USER_END_KEYWORD;
        String userContent = getLogFileContent(beginStr, endStr);

        String[] userLineArr = userContent.split("\n");

        Vector<Usercollectdata> userVector = new Vector<Usercollectdata>();
        for (int i = 0; i < userLineArr.length; i++) {
            String[] tmpData = userLineArr[i].trim().split(":");
            if (tmpData.length > 4) {
                String userName = tmpData[0];
                String userid = tmpData[2];
                String usergroupid = tmpData[3];

                if (userid.length() < 6) {
                    try {
                        if (Integer.parseInt(userid) < 500) {
                            // 小于500的为系统级用户,过滤
                            continue;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                String groupname = "";
                if (usergroupHashtable.containsKey(usergroupid)) {
                    groupname = (String) usergroupHashtable.get(usergroupid);
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

//        Hashtable<String, Object> userHashtable = new Hashtable<String, Object>();
//        userHashtable.put("usergroupHashtable", usergroupHashtable);
//        userHashtable.put("userVector", userVector);
        ObjectValue objectValue = new ObjectValue();
        objectValue.setObjectValue(userVector);
        objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
        return objectValue;
    }
}
