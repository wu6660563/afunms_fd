package com.afunms.common.util.logging.impl;

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import com.afunms.common.util.logging.Level;
import com.afunms.common.util.logging.LogConfigurator;
import com.afunms.common.util.logging.Logger;

/**
 * ʹ�� Log4j ����־��ʵ����־�ӿ�
 * @author <a href=nielin@dhcc.com.cn>����</a>
 * @version 1.0
 *
 */
public class Log4jLogger implements Logger {

    /**
     * ��־��������
     */
    private static Properties properties = null;

    static {
        properties = LogConfigurator.getLogProperties();
        PropertyConfigurator.configure(properties);
    }

    /**
     * Log4j ����־
     */
    private org.apache.log4j.Logger logger;

    /**
     * ͨ���������������ʵ����
     * @param className
     *          ʹ�� Log4j ��־����
     */
    public Log4jLogger(String className) {
        logger = org.apache.log4j.Logger.getLogger(className);
    }

    /**
     * ͨ����Ҫʹ����־�������ȫ�޶���������ȡһ���µ���־ʵ����
     * @param className
     *          ��Ҫʹ����־�������ȫ�޶���
     * @return {@link Logger}
     *          �����µ���־ʵ��
     */
    public Logger getNewInstance(String className) {
        return new Log4jLogger(className);
    }

    /**
     * �Ը�������־����ת��Ϊ Log4j ��־ϵͳ�еļ���
     * @param level
     *          ��������־����
     * @return {@link org.apache.log4j.Level}
     *          -- Log4j ��־ϵͳ�еļ���
     */
    public static org.apache.log4j.Level toLog4jLevel(Level level) {
        org.apache.log4j.Level log4Level = null;
        if (level.getLevel() <= org.apache.log4j.Level.ALL_INT) {
            log4Level = org.apache.log4j.Level.ALL;
        } else if (level.getLevel() <= org.apache.log4j.Level.DEBUG_INT) {
            log4Level = org.apache.log4j.Level.DEBUG;
        } else if (level.getLevel() <= org.apache.log4j.Level.INFO_INT) {
            log4Level = org.apache.log4j.Level.INFO;
        } else if (level.getLevel() <= org.apache.log4j.Level.WARN_INT) {
            log4Level = org.apache.log4j.Level.WARN;
        } else if (level.getLevel() <= org.apache.log4j.Level.ERROR_INT) {
            log4Level = org.apache.log4j.Level.ERROR;
        } else if (level.getLevel() <= org.apache.log4j.Level.FATAL_INT) {
            log4Level = org.apache.log4j.Level.FATAL;
        } else if (level.getLevel() <= org.apache.log4j.Level.OFF_INT) {
            log4Level = org.apache.log4j.Level.OFF;
        }
        return log4Level;
    }

    /**
     * <p>����־�� DEBUG �ȼ�����¼��Ϣ��</p>
     *
     * <p>�÷��������ȵ��� {@link #isDebugEnabled()} ������
     * �жϺͱȽ���־�� DEBUG �ȼ��Ƿ����á�������� DEBUG �ȼ�
     * ����¼��־����Ὣ��Ϊ�����������Ϣ����ת��Ϊ�ַ�����Ȼ��
     * ���������</p>
     *
     * <p>ע�⣺�÷���ֻ���ӡ���� {@link Throwable} �����ƣ�
     * ��û�� {@link Throwable} ��ջ������Ϣ������Ҫ��ջ����
     * ��Ϣ��Ӧ��ʹ�� {@link #debug(Object, Throwable)} ����
     * �����档
     *
     * @param message
     *          ��־��Ϣ����
     */
    public void debug(Object message) {
        if (isDebugEnabled()) {
            logger.debug(message);
        }
    }

    /**
     * <p>����־�� DEBUG �ȼ�����¼��Ϣ��������Ϊ���������
     * {@link Throwable} �Ķ�ջ������Ϣ��</p>
     *
     * @see #debug(Object)
     * @param message
     *          ��־��Ϣ����
     * @param t
     *          ��־�쳣���������ջ����
     */
    public void debug(Object message, Throwable t) {
        if (isDebugEnabled()) {
            logger.debug(message, t);
        }
    }

    /**
     * <p>����־�� ERROR �ȼ�����¼��Ϣ��</p>
     *
     * <p>�÷��������ȵ��� {@link #isErrorEnabled()} ������
     * �жϺͱȽ���־�� ERROR �ȼ��Ƿ����á�������� ERROR �ȼ�
     * ����¼��־����Ὣ��Ϊ�����������Ϣ����ת��Ϊ�ַ�����Ȼ��
     * ���������</p>
     *
     * <p>ע�⣺�÷���ֻ���ӡ���� {@link Throwable} �����ƣ�
     * ��û�� {@link Throwable} ��ջ������Ϣ������Ҫ��ջ����
     * ��Ϣ��Ӧ��ʹ�� {@link #error(Object, Throwable)} ����
     * �����档
     *
     * @param message
     *          ��־��Ϣ����
     */
    public void error(Object message) {
        if (isErrorEnabled()) {
            logger.error(message);
        }
    }

    /**
     * <p>����־�� ERROR �ȼ�����¼��Ϣ��������Ϊ���������
     * {@link Throwable} �Ķ�ջ������Ϣ��</p>
     *
     * @see #error(Object)
     * @param message
     *          ��־��Ϣ����
     * @param t
     *          ��־�쳣���������ջ����
     */
    public void error(Object message, Throwable t) {
        if (isErrorEnabled()) {
            logger.error(message, t);
        }
    }

    /**
     * <p>����־�� FATAL �ȼ�����¼��Ϣ��</p>
     *
     * <p>�÷��������ȵ��� {@link #isFatalEnabled()} ������
     * �жϺͱȽ���־�� FATAL �ȼ��Ƿ����á�������� FATAL �ȼ�
     * ����¼��־����Ὣ��Ϊ�����������Ϣ����ת��Ϊ�ַ�����Ȼ��
     * ���������</p>
     *
     * <p>ע�⣺�÷���ֻ���ӡ���� {@link Throwable} �����ƣ�
     * ��û�� {@link Throwable} ��ջ������Ϣ������Ҫ��ջ����
     * ��Ϣ��Ӧ��ʹ�� {@link #fatal(Object, Throwable)} ����
     * �����档
     *
     * @param message
     *          ��־��Ϣ����
     */
    public void fatal(Object message) {
        if (isFatalEnabled()) {
            logger.fatal(message);
        }
    }

    /**
     * <p>����־�� FATAL �ȼ�����¼��Ϣ��������Ϊ���������
     * {@link Throwable} �Ķ�ջ������Ϣ��</p>
     *
     * @see #fatal(Object)
     * @param message
     *          ��־��Ϣ����
     * @param t
     *          ��־�쳣���������ջ����
     */
    public void fatal(Object message, Throwable t) {
        if (isFatalEnabled()) {
            logger.fatal(message, t);
        }
    }

    /**
     * <p>����־�� INFO �ȼ�����¼��Ϣ��</p>
     *
     * <p>�÷��������ȵ��� {@link #isFatalEnabled()} ������
     * �жϺͱȽ���־�� INFO �ȼ��Ƿ����á�������� INFO �ȼ�
     * ����¼��־����Ὣ��Ϊ�����������Ϣ����ת��Ϊ�ַ�����Ȼ��
     * ���������</p>
     *
     * <p>ע�⣺�÷���ֻ���ӡ���� {@link Throwable} �����ƣ�
     * ��û�� {@link Throwable} ��ջ������Ϣ������Ҫ��ջ����
     * ��Ϣ��Ӧ��ʹ�� {@link #info(Object, Throwable)} ����
     * �����档
     *
     * @param message
     *          ��־��Ϣ����
     */
    public void info(Object message) {
        if (isInfoEnabled()) {
            logger.info(message);
        }
    }

    /**
     * <p>����־�� INFO �ȼ�����¼��Ϣ��������Ϊ���������
     * {@link Throwable} �Ķ�ջ������Ϣ��</p>
     *
     * @see #info(Object)
     * @param message
     *          ��־��Ϣ����
     * @param t
     *          ��־�쳣���������ջ����
     */
    public void info(Object message, Throwable t) {
        if (isInfoEnabled()) {
            logger.info(message, t);
        }
    }

    /**
     * <p>��鵱ǰ��־�����Ƿ����� DEBUG ����</p>
     *
     * <p>���������Ϊ�˼�����־δ���� DEBUG �������俪�����磺</p>
     * <p>
     * if({@link #isDebugEnabled()}) {
     *      {@link #debug(Object)}
     * }
     * </p>
     * <p>���������Ƿ��¼����־�������һ���жϵĿ�����΢������ģ�����
     * ��������м�¼��ȴ����ʡ����࿪����</p>
     *
     * @return {@link Boolean}
     *          -- ��ǰ��־�������� DEBUG ���𷵻� true �����򷵻� false
     */
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    /**
     * <p>��鵱ǰ��־�����Ƿ����� ERROR ����</p>
     *
     * <p>���������Ϊ�˼�����־δ���� ERROR �������俪�����磺</p>
     * <p>
     * if({@link #isErrorEnabled()}) {
     *      {@link #error(Object)}
     * }
     * </p>
     * <p>���������Ƿ��¼����־�������һ���жϵĿ�����΢������ģ�����
     * ��������м�¼��ȴ����ʡ����࿪����</p>
     *
     * @return {@link Boolean}
     *          -- ��ǰ��־�������� ERROR ���𷵻� true �����򷵻� false
     */
    public boolean isErrorEnabled() {
        return logger.isEnabledFor(org.apache.log4j.Level.ERROR);
    }

    /**
     * <p>��鵱ǰ��־�����Ƿ����� FATAL ����</p>
     *
     * <p>���������Ϊ�˼�����־δ���� FATAL �������俪�����磺</p>
     * <p>
     * if({@link #isFatalEnabled()}) {
     *      {@link #fatal(Object)}
     * }
     * </p>
     * <p>���������Ƿ��¼����־�������һ���жϵĿ�����΢������ģ�����
     * ��������м�¼��ȴ����ʡ����࿪����</p>
     *
     * @return {@link Boolean}
     *          -- ��ǰ��־�������� FATAL ���𷵻� true �����򷵻� false
     */
    public boolean isFatalEnabled() {
        return logger.isEnabledFor(org.apache.log4j.Level.FATAL);
    }

    /**
     * <p>��鵱ǰ��־�����Ƿ����� INFO ����</p>
     *
     * <p>���������Ϊ�˼�����־δ���� INFO �������俪�����磺</p>
     * <p>
     * if({@link #isInfoEnabled()}) {
     *      {@link #info(Object)}
     * }
     * </p>
     * <p>���������Ƿ��¼����־�������һ���жϵĿ�����΢������ģ�����
     * ��������м�¼��ȴ����ʡ����࿪����</p>
     *
     * @return {@link Boolean}
     *          -- ��ǰ��־�������� INFO ���𷵻� true �����򷵻� false
     */
    public boolean isInfoEnabled() {
        return logger.isEnabledFor(org.apache.log4j.Level.INFO);
    }

    /**
     * <p>��鵱ǰ��־�����Ƿ����� WARN ����</p>
     *
     * <p>���������Ϊ�˼�����־δ���� WARN �������俪�����磺</p>
     * <p>
     * if({@link #isWarnEnabled()}) {
     *      {@link #warn(Object)}
     * }
     * </p>
     * <p>���������Ƿ��¼����־�������һ���жϵĿ�����΢������ģ�����
     * ��������м�¼��ȴ����ʡ����࿪����</p>
     *
     * @return {@link Boolean}
     *          -- ��ǰ��־�������� WARN ���𷵻� true �����򷵻� false
     */
    public boolean isWarnEnabled() {
        return logger.isEnabledFor(org.apache.log4j.Level.WARN);
    }

    /**
    *
    * <p>��鵱ǰ��־���öԸ����� level �����Ƿ����á�</p>
    *
    * <p>���������Ϊ�˼�����־�Ը����� level ������δ����ʱ����俪�����磺</p>
    * <p>
    * if({@link #isEnabledFor(Level)}) {
    *      {@link #warn(Object)}
    * }
    * </p>
    * <p>���������Ƿ��¼����־�������һ���жϵĿ�����΢������ģ�����
    * ��������м�¼��ȴ����ʡ����࿪����</p>
    *
    * @param level
    *          �����ļ���
    * @return {@link Boolean}
    *          -- ��ǰ��־�������ø����� level ���𷵻� true �����򷵻� false
    */
    public boolean isEnabledFor(Level level) {
        return logger.isEnabledFor(toLog4jLevel(level));
    }

    /**
     * <p>�ø��� level �ȼ�����¼��־��Ϣ��</p>
     *
     * <p>�÷��������ȵ��� {@link #isEnabledFor(Level)} ������
     * �жϺͱȽϸ�������־ level �ȼ��Ƿ����á�������ø����� level
     * �ȼ�����¼��־����Ὣ��Ϊ�����������Ϣ����ת��Ϊ�ַ�����Ȼ��
     * ���������</p>
     *
     * <p>ע�⣺�÷���ֻ���ӡ���� {@link Throwable} �����ƣ�
     * ��û�� {@link Throwable} ��ջ������Ϣ������Ҫ��ջ����
     * ��Ϣ��Ӧ��ʹ�� {@link #log(Level, Object, Throwable)}
     * ���������档
     *
     * @param level
     *          ��������־����
     * @param message
     *          ��־��Ϣ����
     */
    public void log(Level level, Object message) {
        org.apache.log4j.Level log4jLevel = toLog4jLevel(level);
        if (logger.isEnabledFor(log4jLevel)) {
            logger.log(log4jLevel, message);
        }
    }

    /**
     * <p>�ø�������־ level �ȼ�����¼��Ϣ��������Ϊ���������
     * {@link Throwable} �Ķ�ջ������Ϣ��</p>
     *
     * @see #log(Level, Object)
     * @param level
     *          ��������־����
     * @param message
     *          ��־��Ϣ����
     * @param t
     *          ��־�쳣���������ջ����
     */
    public void log(Level level, Object message, Throwable t) {
        org.apache.log4j.Level log4jLevel = toLog4jLevel(level);
        if (logger.isEnabledFor(log4jLevel)) {
            logger.log(log4jLevel, message, t);
        }
    }

    /**
     * <p>����־�� WARN �ȼ�����¼��Ϣ��</p>
     *
     * <p>�÷��������ȵ��� {@link #isFatalEnabled()} ������
     * �жϺͱȽ���־�� WARN �ȼ��Ƿ����á�������� WARN �ȼ�
     * ����¼��־����Ὣ��Ϊ�����������Ϣ����ת��Ϊ�ַ�����Ȼ��
     * ���������</p>
     *
     * <p>ע�⣺�÷���ֻ���ӡ���� {@link Throwable} �����ƣ�
     * ��û�� {@link Throwable} ��ջ������Ϣ������Ҫ��ջ����
     * ��Ϣ��Ӧ��ʹ�� {@link #warn(Object, Throwable)} ����
     * �����档
     *
     * @param message
     *          ��־��Ϣ����
     */
    public void warn(Object message) {
        if (isWarnEnabled()) {
            logger.warn(message);
        }
    }

    /**
     * <p>����־�� WARN �ȼ�����¼��Ϣ��������Ϊ���������
     * {@link Throwable} �Ķ�ջ������Ϣ��</p>
     *
     * @see #warn(Object)
     * @param message
     *          ��־��Ϣ����
     * @param t
     *          ��־�쳣���������ջ����
     */
    public void warn(Object message, Throwable t) {
        if (isWarnEnabled()) {
            logger.warn(message, t);
        }
    }

}
