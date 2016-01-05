package com.afunms.common.util.logging;

import java.util.Properties;

/**
 * ��־����
 * ��ʹ����־֮ǰ���������ȵ���
 * {@link #config(String, Properties)} ������־��������
 * @author ����
 * @version 1.0 $Date 2011-05-29 12:16:35 +0100 (Tue, Match 24, 2011)$
 *
 */
public class LogConfigurator {

    /**
     * ��־��ȫ�޶���
     */
    private static String logClassName;

    /**
     * ��־��������
     */
    private static Properties logProperties;

    /**
     * ������־
     * @param logClassName
     *          ��־��ȫ�޶���
     * @param logProperties
     *          ��־��������
     */
    public static void config(String logClassName, Properties logProperties) {
        LogConfigurator.logClassName = logClassName;
        LogConfigurator.logProperties = logProperties;
    }

    /**
     * ������־��ȫ�޶���
     * @return the logClassName
     *          ��־��ȫ�޶���
     */
    public static String getLogClassName() {
        return logClassName;
    }

    /**
     * ������־��������
     * @return the logProperties
     *          ��־��������
     */
    public static Properties getLogProperties() {
        return logProperties;
    }

    /**
     * main ����
     * @param args
     *          ����
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
