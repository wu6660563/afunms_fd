package com.afunms.polling.snmp.linux;

import com.afunms.common.util.SysLogger;
import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;

/**
 * Linux Version ��־������
 * 
 * @author ����
 */
public class LinuxVersionByLogFile extends LinuxByLogFile {

    /**
     * ��־
     */
    private static SysLogger logger = SysLogger
            .getLogger(LinuxVersionByLogFile.class.getName());

    private static final String LINUX_VERSION_BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_VERSION_BEGIN_KEYWORD;

    private static final String LINUX_VERSION_END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_VERSION_END_KEYWORD;

    @Override
    public ObjectValue getObjectValue() {
        String beginStr = LINUX_VERSION_BEGIN_KEYWORD;
        String endStr = LINUX_VERSION_END_KEYWORD;
        String versionContent = getLogFileContent(beginStr, endStr);

        ObjectValue objectValue = new ObjectValue();
        objectValue.setObjectValue(versionContent);
        objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
        return objectValue;
    }
}
