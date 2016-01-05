package com.afunms.polling.snmp.solaris;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.afunms.common.util.SysLogger;
import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;

/**
 * Solaris Diskperf 日志解析类
 * 
 * @author 聂林
 */
public class SolarisDiskperfByLogFile extends SolarisByLogFile {

    /**
     * 日志
     */
    private static SysLogger logger = SysLogger
                    .getLogger(SolarisDiskperfByLogFile.class.getName());

    private static final String SOLARIS_DISKPERF_BEGIN_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_DISKPERF_BEGIN_KEYWORD;

    private static final String SOLARIS_DISKPERF_END_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_DISKPERF_END_KEYWORD;

    @Override
    public ObjectValue getObjectValue() {
        String beginStr = SOLARIS_DISKPERF_BEGIN_KEYWORD;
        String endStr = SOLARIS_DISKPERF_END_KEYWORD;
        String diskperfContent = getLogFileContent(beginStr, endStr);

        String[] diskperfLineArr = diskperfContent.split("\n");
        String[] diskperf_tmpData = null;
        List<Hashtable<String, String>> alldiskperf = new ArrayList<Hashtable<String, String>>();
        int flag = 0;
        for (int i = 0; i < diskperfLineArr.length; i++) {
            diskperf_tmpData = diskperfLineArr[i].trim().split("\\s++");
            if (diskperf_tmpData != null
                            && (diskperf_tmpData.length == 7 || diskperf_tmpData.length == 8)) {
                Hashtable<String, String> diskperfhash = new Hashtable<String, String>();
                if (diskperf_tmpData[0].trim().equalsIgnoreCase("Average")) {
                    flag = 1;
                    diskperfhash.put("%busy", diskperf_tmpData[2].trim());
                    diskperfhash.put("avque", diskperf_tmpData[3].trim());
                    diskperfhash.put("r+w/s", diskperf_tmpData[4].trim());
                    diskperfhash.put("Kbs/s", diskperf_tmpData[5].trim());
                    diskperfhash.put("avwait", diskperf_tmpData[6].trim());
                    diskperfhash.put("avserv", diskperf_tmpData[7].trim());
                    diskperfhash.put("disklebel", diskperf_tmpData[1].trim());
                    alldiskperf.add(diskperfhash);
                } else if (flag == 1) {
                    diskperfhash.put("%busy", diskperf_tmpData[1].trim());
                    diskperfhash.put("avque", diskperf_tmpData[2].trim());
                    diskperfhash.put("r+w/s", diskperf_tmpData[3].trim());
                    diskperfhash.put("Kbs/s", diskperf_tmpData[4].trim());
                    diskperfhash.put("avwait", diskperf_tmpData[5].trim());
                    diskperfhash.put("avserv", diskperf_tmpData[6].trim());
                    diskperfhash.put("disklebel", diskperf_tmpData[0].trim());
                    alldiskperf.add(diskperfhash);
                }
            }
        }

        ObjectValue objectValue = new ObjectValue();
        objectValue.setObjectValue(alldiskperf);
        objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
        return objectValue;
    }
}
