package com.afunms.polling.snmp.solaris;

import com.afunms.common.util.SysLogger;
import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;

/**
 * Linux Version 日志解析类
 * 
 * @author 聂林
 */
public class SolarisVersionByLogFile extends SolarisByLogFile {

    /**
     * 日志
     */
    private static SysLogger logger = SysLogger
            .getLogger(SolarisVersionByLogFile.class.getName());

    private static final String SOLARIS_VERSION_BEGIN_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_VERSION_BEGIN_KEYWORD;

    private static final String SOLARIS_VERSION_END_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_VERSION_END_KEYWORD;

    @Override
    public ObjectValue getObjectValue() {
        String beginStr = SOLARIS_VERSION_BEGIN_KEYWORD;
        String endStr = SOLARIS_VERSION_END_KEYWORD;
        String versionContent = getLogFileContent(beginStr, endStr);

        ObjectValue objectValue = new ObjectValue();
        objectValue.setObjectValue(versionContent);
        objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
        return objectValue;
    }
}
