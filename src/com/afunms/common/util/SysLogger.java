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
     * 当前系統中配置日志等级
     */
    private static Level logLevel = Level.DEBUG;

    /**
     * 从日志工厂获取的日志实例
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
     * 私有的构造方法，
     * 构造一个带有指定类的完全限定名的 {@link DBCPLog}
     * @param className
     *          需要使用日志的类的完全限定名
     */
    private SysLogger(String className) {
        log = LogFactory.getLog(className);
    }

    /**
     * 通过使用日志的类的完全限定名获取 @link DBCPLog} 的一个实例
     * @param className
     *          需要使用日志的类的完全限定名
     * @return {@link DBCPLog}
     *          返回 {@link DBCPLog} 的一个实例
     */
    public static SysLogger getLogger(String className) {
        return new SysLogger(className);
    }

    /**
     * 通过使用日志 {@link Class} 实例获取 @link DBCPLog} 的一个实例
     * @param clazz
     *          需要使用日志的类
     * @return {@link DBCPLog}
     *          返回日志的一个实例
     */
    @SuppressWarnings("unchecked")
    public static SysLogger getLogger(Class clazz) {
        return new SysLogger(clazz.getName());
    }

    /**
     * <p>用日志的 DEBUG 等级来记录信息。</p>
     *
     * <p>该方法会首先调用 {@link #isDebugEnabled()} 方法来
     * 判断和比较日志的 DEBUG 等级是否启用。如果启用 DEBUG 等级
     * 来记录日志，则会将作为参数传入的消息对象转化为字符串，然后
     * 进行输出。</p>
     *
     * <p>注意：该方法只会打印发生 {@link Throwable} 的名称，
     * 但没有 {@link Throwable} 堆栈跟踪信息，如需要堆栈跟踪
     * 信息，应该使用 {@link #debug(Object, Throwable)} 方法
     * 来代替。
     *
     * @param message
     *          日志信息对象
     */
    public void debug(Object message) {
        if (isDebugEnabled()) {
            log.log(Level.DEBUG, message);
        }
    }

    /**
     * <p>用日志的 DEBUG 等级来记录信息并包括作为参数传入的
     * {@link Throwable} 的堆栈跟踪信息。</p>
     *
     * @see #debug(Object)
     * @param message
     *          日志信息对象
     * @param t
     *          日志异常，包括其堆栈跟踪
     */
    public void debug(Object message, Throwable t) {
        if (isDebugEnabled()) {
            log.debug(message, t);
        }
    }

    /**
     * <p>用日志的 ERROR 等级来记录信息。</p>
     *
     * <p>该方法会首先调用 {@link #isErrorEnabled()} 方法来
     * 判断和比较日志的 ERROR 等级是否启用。如果启用 ERROR 等级
     * 来记录日志，则会将作为参数传入的消息对象转化为字符串，然后
     * 进行输出。</p>
     *
     * <p>注意：该方法只会打印发生 {@link Throwable} 的名称，
     * 但没有 {@link Throwable} 堆栈跟踪信息，如需要堆栈跟踪
     * 信息，应该使用 {@link #error(Object, Throwable)} 方法
     * 来代替。
     *
     * @param message
     *          日志信息对象
     */
    public void error(Object message) {
        if (isErrorEnabled()) {
            log.error(message);
        }
    }

    /**
     * <p>用日志的 ERROR 等级来记录信息并包括作为参数传入的
     * {@link Throwable} 的堆栈跟踪信息。</p>
     *
     * @see #error(Object)
     * @param message
     *          日志信息对象
     * @param t
     *          日志异常，包括其堆栈跟踪
     */
    public void error(Object message, Throwable t) {
        if (isErrorEnabled()) {
            log.error(message, t);
        }
    }

    /**
     * <p>用日志的 FATAL 等级来记录信息。</p>
     *
     * <p>该方法会首先调用 {@link #isFatalEnabled()} 方法来
     * 判断和比较日志的 FATAL 等级是否启用。如果启用 FATAL 等级
     * 来记录日志，则会将作为参数传入的消息对象转化为字符串，然后
     * 进行输出。</p>
     *
     * <p>注意：该方法只会打印发生 {@link Throwable} 的名称，
     * 但没有 {@link Throwable} 堆栈跟踪信息，如需要堆栈跟踪
     * 信息，应该使用 {@link #fatal(Object, Throwable)} 方法
     * 来代替。
     *
     * @param message
     *          日志信息对象
     */
    public void fatal(Object message) {
        if (isFatalEnabled()) {
            log.fatal(message);
        }
    }

    /**
     * <p>用日志的 FATAL 等级来记录信息并包括作为参数传入的
     * {@link Throwable} 的堆栈跟踪信息。</p>
     *
     * @see #fatal(Object)
     * @param message
     *          日志信息对象
     * @param t
     *          日志异常，包括其堆栈跟踪
     */
    public void fatal(Object message, Throwable t) {
        if (isFatalEnabled()) {
            log.fatal(message, t);
        }
    }

    /**
     * <p>用日志的 INFO 等级来记录信息。</p>
     *
     * <p>该方法会首先调用 {@link #isFatalEnabled()} 方法来
     * 判断和比较日志的 INFO 等级是否启用。如果启用 INFO 等级
     * 来记录日志，则会将作为参数传入的消息对象转化为字符串，然后
     * 进行输出。</p>
     *
     * <p>注意：该方法只会打印发生 {@link Throwable} 的名称，
     * 但没有 {@link Throwable} 堆栈跟踪信息，如需要堆栈跟踪
     * 信息，应该使用 {@link #info(Object, Throwable)} 方法
     * 来代替。
     *
     * @param message
     *          日志信息对象
     */
    public void info(Object message) {
        if (isInfoEnabled()) {
            log.info(message);
        }
    }

    /**
     * <p>用日志的 INFO 等级来记录信息并包括作为参数传入的
     * {@link Throwable} 的堆栈跟踪信息。</p>
     *
     * @see #info(Object)
     * @param message
     *          日志信息对象
     * @param t
     *          日志异常，包括其堆栈跟踪
     */
    public void info(Object message, Throwable t) {
        if (isInfoEnabled()) {
            log.info(message, t);
        }
    }

    /**
     * <p>检查当前日志配置是否启用 DEBUG 级别。</p>
     *
     * <p>这个方法是为了减轻日志未启用 DEBUG 级别的语句开销。如：</p>
     * <p>
     * if({@link #isDebugEnabled()}) {
     *      {@link #debug(Object)}
     * }
     * </p>
     * <p>对于无论是否记录该日志，其进行一次判断的开销是微不足道的，但是
     * 如果不进行记录，却可以省下许多开销。</p>
     *
     * <p>该方法会调用 {@link #isEnabledFor(Level)} 方法
     * 返回是否启用 DEBUG 级别。</p>
     *
     * @see #isEnabledFor(Level)
     * @return {@link Boolean}
     *          -- 当前日志配置启用 DEBUG 级别返回 true ，否则返回 false
     */
    public boolean isDebugEnabled() {
        return isEnabledFor(Level.DEBUG);
    }

    /**
    *
    * <p>检查当前日志配置对给定的 level 级别是否启用。</p>
    *
    * <p>这个方法是为了减轻日志对给定的 level 级别在未启用时的语句开销。如：</p>
    * <p>
    * if({@link #isEnabledFor(Level)}) {
    *      {@link #warn(Object)}
    * }
    * </p>
    * <p>对于无论是否记录该日志，其进行一次判断的开销是微不足道的，但是
    * 如果不进行记录，却可以省下许多开销。</p>
    *
    * <p>首先该方法回判断当前 DBCP 组件配置的日志等级是否启用给定的级别，
    * 而后在判断使用 DBCP 组件的系统配置的日志等级是否启动给定的级别，如果
    * 都启用，则返回 true ，否则返回 false 。</p>
    *
    * @param level
    *          给定的级别
    * @return {@link Boolean}
    *          -- 当前日志配置启用给定的 level 级别返回 true ，否则返回 false
    */
    public boolean isEnabledFor(Level level) {
        if (logLevel.compareTo(level) <= 0) {
            return log.isEnabledFor(level);
        } else {
            return false;
        }
    }

    /**
     * <p>检查当前日志配置是否启用 ERROR 级别。</p>
     *
     * <p>这个方法是为了减轻日志未启用 ERROR 级别的语句开销。如：</p>
     * <p>
     * if({@link #isErrorEnabled()}) {
     *      {@link #error(Object)}
     * }
     * </p>
     * <p>对于无论是否记录该日志，其进行一次判断的开销是微不足道的，但是
     * 如果不进行记录，却可以省下许多开销。</p>
     *
     * * <p>该方法会调用 {@link #isEnabledFor(Level)} 方法
     * 返回是否启用 DEBUG 级别。</p>
     *
     * @see #isEnabledFor(Level)
     * @return {@link Boolean}
     *          -- 当前日志配置启用 ERROR 级别返回 true ，否则返回 false
     */
    public boolean isErrorEnabled() {
        return isEnabledFor(Level.ERROR);
    }

    /**
     * <p>检查当前日志配置是否启用 FATAL 级别。</p>
     *
     * <p>这个方法是为了减轻日志未启用 FATAL 级别的语句开销。如：</p>
     * <p>
     * if({@link #isFatalEnabled()}) {
     *      {@link #fatal(Object)}
     * }
     * </p>
     * <p>对于无论是否记录该日志，其进行一次判断的开销是微不足道的，但是
     * 如果不进行记录，却可以省下许多开销。</p>
     *
     * <p>该方法会调用 {@link #isEnabledFor(Level)} 方法
     * 返回是否启用 DEBUG 级别。</p>
     *
     * @see #isEnabledFor(Level)
     * @return {@link Boolean}
     *          -- 当前日志配置启用 FATAL 级别返回 true ，否则返回 false
     */
    public boolean isFatalEnabled() {
        return isEnabledFor(Level.FATAL);
    }

    /**
     * <p>检查当前日志配置是否启用 INFO 级别。</p>
     *
     * <p>这个方法是为了减轻日志未启用 INFO 级别的语句开销。如：</p>
     * <p>
     * if({@link #isInfoEnabled()}) {
     *      {@link #info(Object)}
     * }
     * </p>
     * <p>对于无论是否记录该日志，其进行一次判断的开销是微不足道的，但是
     * 如果不进行记录，却可以省下许多开销。</p>
     *
     * <p>该方法会调用 {@link #isEnabledFor(Level)} 方法
     * 返回是否启用 DEBUG 级别。</p>
     *
     * @see #isEnabledFor(Level)
     * @return {@link Boolean}
     *          -- 当前日志配置启用 INFO 级别返回 true ，否则返回 false
     */
    public boolean isInfoEnabled() {
        return isEnabledFor(Level.INFO);
    }

    /**
     * <p>检查当前日志配置是否启用 WARN 级别。</p>
     *
     * <p>这个方法是为了减轻日志未启用 WARN 级别的语句开销。如：</p>
     * <p>
     * if({@link #isWarnEnabled()}) {
     *      {@link #warn(Object)}
     * }
     * </p>
     * <p>对于无论是否记录该日志，其进行一次判断的开销是微不足道的，但是
     * 如果不进行记录，却可以省下许多开销。</p>
     *
     * <p>该方法会调用 {@link #isEnabledFor(Level)} 方法
     * 返回是否启用 DEBUG 级别。</p>
     *
     * @see #isEnabledFor(Level)
     * @return {@link Boolean}
     *          -- 当前日志配置启用 WARN 级别返回 true ，否则返回 false
     */
    public boolean isWarnEnabled() {
        return isEnabledFor(Level.WARN);
    }

    /**
     * <p>用给定 level 等级来记录日志信息。</p>
     *
     * <p>该方法会首先调用 {@link #isEnabledFor(Level)} 方法来
     * 判断和比较给定的日志 level 等级是否启用。如果启用给定的 level
     * 等级来记录日志，则会将作为参数传入的消息对象转化为字符串，然后
     * 进行输出。</p>
     *
     * <p>注意：该方法只会打印发生 {@link Throwable} 的名称，
     * 但没有 {@link Throwable} 堆栈跟踪信息，如需要堆栈跟踪
     * 信息，应该使用 {@link #log(Level, Object, Throwable)}
     * 方法来代替。
     *
     * @param level
     *          给定的日志级别
     * @param message
     *          日志信息对象
     */
    public void log(Level level, Object message) {
        if (isEnabledFor(level)) {
            log.log(level, message);
        }
    }

    /**
     * <p>用给定的日志 level 等级来记录信息并包括作为参数传入的
     * {@link Throwable} 的堆栈跟踪信息。</p>
     *
     * @see #log(Level, Object)
     * @param level
     *          给定的日志级别
     * @param message
     *          日志信息对象
     * @param t
     *          日志异常，包括其堆栈跟踪
     */
    public void log(Level level, Object message, Throwable t) {
        log.log(level, message, t);
    }

    /**
     * <p>用日志的 WARN 等级来记录信息。</p>
     *
     * <p>该方法会首先调用 {@link #isWarnEnabled()} 方法来
     * 判断和比较日志的 WARN 等级是否启用。如果启用 WARN 等级
     * 来记录日志，则会将作为参数传入的消息对象转化为字符串，然后
     * 进行输出。</p>
     *
     * <p>注意：该方法只会打印发生 {@link Throwable} 的名称，
     * 但没有 {@link Throwable} 堆栈跟踪信息，如需要堆栈跟踪
     * 信息，应该使用 {@link #warn(Object, Throwable)} 方法
     * 来代替。
     *
     * @param message
     *          日志信息对象
     */
    public void warn(Object message) {
        if (isWarnEnabled()) {
            log.warn(message);
        }
    }

    /**
     * <p>用日志的 WARN 等级来记录信息并包括作为参数传入的
     * {@link Throwable} 的堆栈跟踪信息。</p>
     *
     * @see #warn(Object)
     * @param message
     *          日志信息对象
     * @param t
     *          日志异常，包括其堆栈跟踪
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
