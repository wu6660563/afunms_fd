package com.afunms.common.util.logging.impl;

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import com.afunms.common.util.logging.Level;
import com.afunms.common.util.logging.LogConfigurator;
import com.afunms.common.util.logging.Logger;

/**
 * 使用 Log4j 的日志来实现日志接口
 * @author <a href=nielin@dhcc.com.cn>聂林</a>
 * @version 1.0
 *
 */
public class Log4jLogger implements Logger {

    /**
     * 日志配置属性
     */
    private static Properties properties = null;

    static {
        properties = LogConfigurator.getLogProperties();
        PropertyConfigurator.configure(properties);
    }

    /**
     * Log4j 的日志
     */
    private org.apache.log4j.Logger logger;

    /**
     * 通过类的名称来进行实例化
     * @param className
     *          使用 Log4j 日志的类
     */
    public Log4jLogger(String className) {
        logger = org.apache.log4j.Logger.getLogger(className);
    }

    /**
     * 通过需要使用日志的类的完全限定名，来获取一个新的日志实例。
     * @param className
     *          需要使用日志的类的完全限定名
     * @return {@link Logger}
     *          返回新的日志实例
     */
    public Logger getNewInstance(String className) {
        return new Log4jLogger(className);
    }

    /**
     * 对给定的日志级别转化为 Log4j 日志系统中的级别
     * @param level
     *          给定的日志级别
     * @return {@link org.apache.log4j.Level}
     *          -- Log4j 日志系统中的级别
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
            logger.debug(message);
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
            logger.debug(message, t);
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
            logger.error(message);
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
            logger.error(message, t);
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
            logger.fatal(message);
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
            logger.fatal(message, t);
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
            logger.info(message);
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
            logger.info(message, t);
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
     * @return {@link Boolean}
     *          -- 当前日志配置启用 DEBUG 级别返回 true ，否则返回 false
     */
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
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
     * @return {@link Boolean}
     *          -- 当前日志配置启用 ERROR 级别返回 true ，否则返回 false
     */
    public boolean isErrorEnabled() {
        return logger.isEnabledFor(org.apache.log4j.Level.ERROR);
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
     * @return {@link Boolean}
     *          -- 当前日志配置启用 FATAL 级别返回 true ，否则返回 false
     */
    public boolean isFatalEnabled() {
        return logger.isEnabledFor(org.apache.log4j.Level.FATAL);
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
     * @return {@link Boolean}
     *          -- 当前日志配置启用 INFO 级别返回 true ，否则返回 false
     */
    public boolean isInfoEnabled() {
        return logger.isEnabledFor(org.apache.log4j.Level.INFO);
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
     * @return {@link Boolean}
     *          -- 当前日志配置启用 WARN 级别返回 true ，否则返回 false
     */
    public boolean isWarnEnabled() {
        return logger.isEnabledFor(org.apache.log4j.Level.WARN);
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
    * @param level
    *          给定的级别
    * @return {@link Boolean}
    *          -- 当前日志配置启用给定的 level 级别返回 true ，否则返回 false
    */
    public boolean isEnabledFor(Level level) {
        return logger.isEnabledFor(toLog4jLevel(level));
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
        org.apache.log4j.Level log4jLevel = toLog4jLevel(level);
        if (logger.isEnabledFor(log4jLevel)) {
            logger.log(log4jLevel, message);
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
        org.apache.log4j.Level log4jLevel = toLog4jLevel(level);
        if (logger.isEnabledFor(log4jLevel)) {
            logger.log(log4jLevel, message, t);
        }
    }

    /**
     * <p>用日志的 WARN 等级来记录信息。</p>
     *
     * <p>该方法会首先调用 {@link #isFatalEnabled()} 方法来
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
            logger.warn(message);
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
            logger.warn(message, t);
        }
    }

}
