package com.afunms.common.util.logging;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import com.afunms.common.util.logging.impl.Log4jPorperties;

/**
 * 日志工厂类，通过日志工厂来获取一个日志实例
 * @author <a href=nielin@dhcc.com.cn>聂林</a>
 * @version 1.0 $Date: 2011-05-24 10:47:38 +0100 (Tue, Match 24, 2011)$
 */
@SuppressWarnings("unchecked")
public /* abstract */ final class LogFactory {

    /**
     * 用于单例模式
     */
    private static LogFactory logFactory;

    /**
     * 日志的接口
     */
    private static Logger logger;

    /**
     * 日志配置属性
     */
    private static Properties logProperties;

    /**
     * 日志完全限定名
     */
    private static String logClassName;

    static {
        logProperties = LogConfigurator.getLogProperties();
        logClassName = LogConfigurator.getLogClassName();
        Class clazz = null;
        try {
            if (logClassName == null) {
                logClassName = "com.afunms.common.util.logging.impl.Log4jLogger";
                logProperties = Log4jPorperties.getProperties();
                LogConfigurator.config(logClassName, logProperties);
            }
            clazz = Class.forName(logClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Constructor constructor = null;
        try {
            constructor = clazz.getConstructor(String.class);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            logger = (Logger) constructor.newInstance(
                    LogFactory.class.getName());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于单例模式的私有构造方法
     */
    private LogFactory() {
    }

    /**
     * 静态的获取日志工厂类的实例
     * @return
     *          返回日志工厂的实例
     */
    public static LogFactory getInstance() {
        if (logFactory == null) {
            logFactory = new LogFactory();
        }
        return logFactory;
    }

    /**
     * 通过使用日志的类的完全限定名获取一个日志
     * @param className
     *          需要使用日志的类的完全限定名
     * @return {@link Logger}
     *          返回日志的一个实例
     */
    public static Log getLog(String className) {
        return logger.getNewInstance(className);
    }

    /**
     * 通过使用日志的类获取一个日志
     * @param clazz
     *          需要使用日志的类
     * @return {@link Logger}
     *          返回日志的一个实例
     */
    public static Log getLog(Class clazz) {
        return logger.getNewInstance(clazz.getName());
    }

    /**
     * 返回日志配置属性
     * @return the logProperties
     *          日志配置属性
     */
    public static Properties getLoGProperties() {
        return logProperties;
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
     * 静态的 main 方法
     * @param args
     *          参数
     */
    public static void main(String[] args) {
    }

}
