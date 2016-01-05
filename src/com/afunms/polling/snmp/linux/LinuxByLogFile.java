package com.afunms.polling.snmp.linux;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.afunms.common.util.SysLogger;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.polling.base.ObjectValue;

public abstract class LinuxByLogFile {

    /**
     * ��־
     */
    private static final SysLogger sysLogger = SysLogger
            .getLogger(LinuxByLogFile.class.getName());

    /**
     * ���ڸ�ʽ��
     */
    protected static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * ��־�ļ�����
     */
    protected String logFileContent;

    /**
     * �豸 nodeDTO
     */
    protected NodeDTO nodeDTO;

    /**
     * IP �豸����������
     */
    protected Hashtable ipAllData;

    /**
     * ����ָ������־�ļ�����
     * 
     * @param logFileContent
     *            ָ������־�ļ�����
     */
    public void setLogFileContent(String logFileContent) {
        this.logFileContent = logFileContent;
    }

    /**
     * ��ȡ��־�ļ��е�����
     * 
     * @return {@link String} ��־�ļ�����
     */
    protected String getLogFileContent() {
        return logFileContent;
    }

    /**
     * @return the nodeDTO
     */
    public NodeDTO getNodeDTO() {
        return nodeDTO;
    }

    /**
     * @param nodeDTO
     *            the nodeDTO to set
     */
    public void setNodeDTO(NodeDTO nodeDTO) {
        this.nodeDTO = nodeDTO;
    }

    /**
     * @return the ipAllData
     */
    public Hashtable getIpAllData() {
        return ipAllData;
    }

    /**
     * @param ipAllData
     *            the ipAllData to set
     */
    public void setIpAllData(Hashtable ipAllData) {
        this.ipAllData = ipAllData;
    }

    /**
     * ���ݿ�ʼ�ַ���������ַ������ݻ�ȡ��־�ļ��е�����
     * 
     * @param beginStr
     *            ��ʼ�ַ���
     * @param endStr
     *            �����ַ���
     * @return {@link String} ��־�ļ��е�����
     */
    protected String getLogFileContent(String beginStr, String endStr) {
        String fileContent = getLogFileContent();
        String content = "";
        Pattern pattern = Pattern.compile("(" + beginStr + ")(.*)(" + endStr
                + ")", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(fileContent.toString());
        if (matcher.find()) {
            content = matcher.group(2);
        }
        if (content != null && content.length() > 0) {
            content = content.trim();
        }
        return content;
    }

    protected Calendar getCalendarInstance() {
        return Calendar.getInstance();
    }

    public abstract ObjectValue getObjectValue();
}
