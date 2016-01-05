package com.afunms.common.util.logging;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import com.afunms.common.util.logging.impl.Log4jPorperties;

/**
 * ��־�����࣬ͨ����־��������ȡһ����־ʵ��
 * @author <a href=nielin@dhcc.com.cn>����</a>
 * @version 1.0 $Date: 2011-05-24 10:47:38 +0100 (Tue, Match 24, 2011)$
 */
@SuppressWarnings("unchecked")
public /* abstract */ final class LogFactory {

    /**
     * ���ڵ���ģʽ
     */
    private static LogFactory logFactory;

    /**
     * ��־�Ľӿ�
     */
    private static Logger logger;

    /**
     * ��־��������
     */
    private static Properties logProperties;

    /**
     * ��־��ȫ�޶���
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
     * ���ڵ���ģʽ��˽�й��췽��
     */
    private LogFactory() {
    }

    /**
     * ��̬�Ļ�ȡ��־�������ʵ��
     * @return
     *          ������־������ʵ��
     */
    public static LogFactory getInstance() {
        if (logFactory == null) {
            logFactory = new LogFactory();
        }
        return logFactory;
    }

    /**
     * ͨ��ʹ����־�������ȫ�޶�����ȡһ����־
     * @param className
     *          ��Ҫʹ����־�������ȫ�޶���
     * @return {@link Logger}
     *          ������־��һ��ʵ��
     */
    public static Log getLog(String className) {
        return logger.getNewInstance(className);
    }

    /**
     * ͨ��ʹ����־�����ȡһ����־
     * @param clazz
     *          ��Ҫʹ����־����
     * @return {@link Logger}
     *          ������־��һ��ʵ��
     */
    public static Log getLog(Class clazz) {
        return logger.getNewInstance(clazz.getName());
    }

    /**
     * ������־��������
     * @return the logProperties
     *          ��־��������
     */
    public static Properties getLoGProperties() {
        return logProperties;
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
     * ��̬�� main ����
     * @param args
     *          ����
     */
    public static void main(String[] args) {
    }

}
