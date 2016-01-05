/*
 * @(#)LogFileIndicatorGather.java     v1.01, 2014 1 14
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.afunms.common.util.SysLogger;
import com.afunms.initialize.ResourceCenter;

/**
 * ClassName:   LogFileIndicatorGather.java
 * <p>
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 14 11:43:50
 */
public abstract class LogFileIndicatorGather extends BaseIndicatorGather {

    private static SysLogger logger = SysLogger.getLogger(LogFileIndicatorGather.class);

    /**
     * ��־�ļ�����
     */
    protected String logFileContent;

    /**
     * afterGetSimpleIndicatorValue:
     *
     *
     * @since   v1.01
     * @see com.afunms.gather.indicator.base.BaseIndicatorGather#afterGetSimpleIndicatorValue()
     */
    @Override
    public void afterGetSimpleIndicatorValue() {

    }

    /**
     * beforeGetSimpleIndicatorValue:
     *
     *
     * @since   v1.01
     * @see com.afunms.gather.indicator.base.BaseIndicatorGather#beforeGetSimpleIndicatorValue()
     */
    @Override
    public void beforeGetSimpleIndicatorValue() {

    }

    /**
     * loadFileContent:
     * <p>������־�ļ�����
     *
     * @param   ipaddress
     *          - 
     * @return
     *
     * @since   v1.01
     */
    @SuppressWarnings("static-access")
    protected String loadLogFileContent() {
        StringBuffer fileContent = new StringBuffer();
        String filename = null;
        try {
            filename = ResourceCenter.getInstance().getSysPath()
                    + "/linuxserver/" + getIpAddress() + ".log";
//          logger.info("��ʼ������־�ļ���" + filename);
            File file = new File(filename);
            if (!file.exists()) {
                // �ļ�������, ���� null
                return fileContent.toString();
            }
            FileInputStream fis = new FileInputStream(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String strLine = null;
            // �����ļ�����
            while ((strLine = br.readLine()) != null) {
                fileContent.append(strLine + "\n");
            }
            fis.close();
            isr.close();
            br.close();
        } catch (FileNotFoundException e1) {
            logger.info("û�������־�ļ���"+filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileContent.toString();
    }

    /**
     * getLogFileContent:
     * <p>���ݿ�ʼ�ַ���������ַ������ݻ�ȡ��־�ļ��е�����
     *
     * @param   beginStr
     *          - ��ʼ�ַ���
     * @param   endStr
     *          - �����ַ���
     * @return  {@link String}
     *          - ��־�ļ��е�����
     *
     * @since   v1.01
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

    /**
     * getLogFileContent:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getLogFileContent() {
        return logFileContent;
    }

    /**
     * setLogFileContent:
     * <p>
     *
     * @param   logFileContent
     *          -
     * @since   v1.01
     */
    public void setLogFileContent(String logFileContent) {
        this.logFileContent = logFileContent;
    }

    
}

