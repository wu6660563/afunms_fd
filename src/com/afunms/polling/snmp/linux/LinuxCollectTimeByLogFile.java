package com.afunms.polling.snmp.linux;


import java.text.SimpleDateFormat;
import java.util.Date;

import com.afunms.common.util.SysLogger;
import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;

/**
 * Linux CollectTime ��־������
 * 
 * @author ����
 */
public class LinuxCollectTimeByLogFile extends LinuxByLogFile {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * ��־
     */
    private static SysLogger logger = SysLogger
            .getLogger(LinuxCollectTimeByLogFile.class.getName());

    private static final String LINUX_COLLECTTIME_BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_COLLECTTIME_BEGIN_KEYWORD;

    private static final String LINUX_COLLECTTIME_END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_COLLECTTIME_END_KEYWORD;

    @Override
    public ObjectValue getObjectValue() {
    	//�ű�����û��CollectTime
        String beginStr = LINUX_COLLECTTIME_BEGIN_KEYWORD;
        String endStr = LINUX_COLLECTTIME_END_KEYWORD;
//        String collecttimeContent = getLogFileContent(beginStr, endStr);

        ObjectValue objectValue = new ObjectValue();
        objectValue.setObjectValue(sdf.format(new Date()));
        objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
        return objectValue;
    }
}
