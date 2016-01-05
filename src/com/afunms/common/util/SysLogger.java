/**
 * <p>Description:logger,writes error and debug information within system running</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-06
 */

package com.afunms.common.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import com.afunms.common.util.logging.Level;
import com.afunms.common.util.logging.Log;
import com.afunms.common.util.logging.LogConfigurator;
import com.afunms.common.util.logging.LogFactory;
import com.afunms.initialize.ResourceCenter;

public class SysLogger implements Serializable{
	
    /**
     * ��ǰϵ�y��������־�ȼ�
     */
    private static Level logLevel = Level.DEBUG;

    /**
     * ����־������ȡ����־ʵ��
     */
    private Log log;
	
	static {
		Properties logProperties = new Properties();
		   try {
			logProperties.load(new FileInputStream(ResourceCenter.getInstance().getSysPath() + "WEB-INF/logs/log4j.properties"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   LogConfigurator.config("com.afunms.common.util.logging.impl.Log4jLogger", logProperties);
	}

	public static void info(String infoMessage) {
	    if(ResourceCenter.getInstance().isLogInfo()) {
    	   //init();
    	   new SysLogger(SysLogger.class.getName()).getLog().info(infoMessage);
	    }         
	}
	
	public static void error(String errorMessage, Exception ex) {
	    if(ResourceCenter.getInstance().isLogError()) {	   
	        //init();
	        if(ex.getMessage()==null)
	            new SysLogger(SysLogger.class.getName()).getLog().error(errorMessage);
	        else
	            new SysLogger(SysLogger.class.getName()).getLog().error(errorMessage, ex);
	        ex.printStackTrace();
	    }   
	}

	public static void error(String errorMessage) {
	    if(ResourceCenter.getInstance().isLogError()) {	   
	        //init();
	        new SysLogger(SysLogger.class.getName()).getLog().error(errorMessage);
	    }   
	}  
   
//   public static Logger getLogger(String className) {
//	   return Logger.getLogger(className);
//   }

	/**
     * ˽�еĹ��췽����
     * ����һ������ָ�������ȫ�޶����� {@link DBCPLog}
     * @param className
     *          ��Ҫʹ����־�������ȫ�޶���
     */
    private SysLogger(String className) {
        log = LogFactory.getLog(className);
    }

    /**
     * ͨ��ʹ����־�������ȫ�޶�����ȡ @link DBCPLog} ��һ��ʵ��
     * @param className
     *          ��Ҫʹ����־�������ȫ�޶���
     * @return {@link DBCPLog}
     *          ���� {@link DBCPLog} ��һ��ʵ��
     */
    public static SysLogger getLogger(String className) {
        return new SysLogger(className);
    }

    /**
     * ͨ��ʹ����־ {@link Class} ʵ����ȡ @link DBCPLog} ��һ��ʵ��
     * @param clazz
     *          ��Ҫʹ����־����
     * @return {@link DBCPLog}
     *          ������־��һ��ʵ��
     */
    @SuppressWarnings("unchecked")
    public static SysLogger getLogger(Class clazz) {
        return new SysLogger(clazz.getName());
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
            log.log(Level.DEBUG, message);
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
            log.debug(message, t);
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
            log.error(message);
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
            log.error(message, t);
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
            log.fatal(message);
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
            log.fatal(message, t);
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
            log.info(message);
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
            log.info(message, t);
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
     * <p>�÷�������� {@link #isEnabledFor(Level)} ����
     * �����Ƿ����� DEBUG ����</p>
     *
     * @see #isEnabledFor(Level)
     * @return {@link Boolean}
     *          -- ��ǰ��־�������� DEBUG ���𷵻� true �����򷵻� false
     */
    public boolean isDebugEnabled() {
        return isEnabledFor(Level.DEBUG);
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
    * <p>���ȸ÷������жϵ�ǰ DBCP ������õ���־�ȼ��Ƿ����ø����ļ���
    * �������ж�ʹ�� DBCP �����ϵͳ���õ���־�ȼ��Ƿ����������ļ������
    * �����ã��򷵻� true �����򷵻� false ��</p>
    *
    * @param level
    *          �����ļ���
    * @return {@link Boolean}
    *          -- ��ǰ��־�������ø����� level ���𷵻� true �����򷵻� false
    */
    public boolean isEnabledFor(Level level) {
        if (logLevel.compareTo(level) <= 0) {
            return log.isEnabledFor(level);
        } else {
            return false;
        }
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
     * * <p>�÷�������� {@link #isEnabledFor(Level)} ����
     * �����Ƿ����� DEBUG ����</p>
     *
     * @see #isEnabledFor(Level)
     * @return {@link Boolean}
     *          -- ��ǰ��־�������� ERROR ���𷵻� true �����򷵻� false
     */
    public boolean isErrorEnabled() {
        return isEnabledFor(Level.ERROR);
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
     * <p>�÷�������� {@link #isEnabledFor(Level)} ����
     * �����Ƿ����� DEBUG ����</p>
     *
     * @see #isEnabledFor(Level)
     * @return {@link Boolean}
     *          -- ��ǰ��־�������� FATAL ���𷵻� true �����򷵻� false
     */
    public boolean isFatalEnabled() {
        return isEnabledFor(Level.FATAL);
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
     * <p>�÷�������� {@link #isEnabledFor(Level)} ����
     * �����Ƿ����� DEBUG ����</p>
     *
     * @see #isEnabledFor(Level)
     * @return {@link Boolean}
     *          -- ��ǰ��־�������� INFO ���𷵻� true �����򷵻� false
     */
    public boolean isInfoEnabled() {
        return isEnabledFor(Level.INFO);
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
     * <p>�÷�������� {@link #isEnabledFor(Level)} ����
     * �����Ƿ����� DEBUG ����</p>
     *
     * @see #isEnabledFor(Level)
     * @return {@link Boolean}
     *          -- ��ǰ��־�������� WARN ���𷵻� true �����򷵻� false
     */
    public boolean isWarnEnabled() {
        return isEnabledFor(Level.WARN);
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
        if (isEnabledFor(level)) {
            log.log(level, message);
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
        log.log(level, message, t);
    }

    /**
     * <p>����־�� WARN �ȼ�����¼��Ϣ��</p>
     *
     * <p>�÷��������ȵ��� {@link #isWarnEnabled()} ������
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
            log.warn(message);
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
            log.warn(message, t);
        }
    }

    private Log getLog() {
        return log;
    }
}
