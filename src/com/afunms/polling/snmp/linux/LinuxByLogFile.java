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
     * 日志
     */
    private static final SysLogger sysLogger = SysLogger
            .getLogger(LinuxByLogFile.class.getName());

    /**
     * 日期格式化
     */
    protected static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * 日志文件内容
     */
    protected String logFileContent;

    /**
     * 设备 nodeDTO
     */
    protected NodeDTO nodeDTO;

    /**
     * IP 设备的所有数据
     */
    protected Hashtable ipAllData;

    /**
     * 设置指定的日志文件内容
     * 
     * @param logFileContent
     *            指定的日志文件内容
     */
    public void setLogFileContent(String logFileContent) {
        this.logFileContent = logFileContent;
    }

    /**
     * 获取日志文件中的内容
     * 
     * @return {@link String} 日志文件内容
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
     * 根据开始字符串与结束字符串内容获取日志文件中的内容
     * 
     * @param beginStr
     *            开始字符串
     * @param endStr
     *            结束字符串
     * @return {@link String} 日志文件中的内容
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
