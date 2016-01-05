package com.afunms.common.util.logging;

import java.util.Properties;

/**
 * 日志配置
 * 在使用日志之前，必须首先调用
 * {@link #config(String, Properties)} 来对日志进行配置
 * @author 聂林
 * @version 1.0 $Date 2011-05-29 12:16:35 +0100 (Tue, Match 24, 2011)$
 *
 */
public class LogConfigurator {

    /**
     * 日志完全限定名
     */
    private static String logClassName;

    /**
     * 日志配置属性
     */
    private static Properties logProperties;

    /**
     * 配置日志
     * @param logClassName
     *          日志完全限定名
     * @param logProperties
     *          日志配置属性
     */
    public static void config(String logClassName, Properties logProperties) {
        LogConfigurator.logClassName = logClassName;
        LogConfigurator.logProperties = logProperties;
    }

    /**
     * 返回日志完全限定名
     * @return the logClassName
     *          日志完全限定名
     */
    public static String getLogClassName() {
        return logClassName;
    }

    /**
     * 返回日志配置属性
     * @return the logProperties
     *          日志配置属性
     */
    public static Properties getLogProperties() {
        return logProperties;
    }

    /**
     * main 方法
     * @param args
     *          参数
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
