package com.afunms.common.util.logging;

/**
 * <p>一个简单的日志记录 API 的抽象接口.  为了能够让日志工厂 {@ LogFactory }
 * 来成功实例化日志, 所有日志类必须实现该接口，并且必须有带一个字符串 {@ String}
 * 作为参数的构造方法，该字符串用于给这个日志的实例命名。</p>
 *
 * <p> 以下为 <code>Log</code> 的五个等级:
 * <ol>
 * <li>调试 DEBUG (the least serious)</li>
 * <li>信息 INFO</li>
 * <li>警告 WARN</li>
 * <li>错误 ERROR</li>
 * <li>致命 FATAL (the most serious)</li>
 * </ol>
 *
 * <ol>
 * <li>DEBUG Level指出细粒度信息事件对调试应用程序是非常有帮助的。</li>
 * <li>INFO level表明 消息在粗粒度级别上突出强调应用程序的运行过程。</li>
 * <li>WARN level表明会出现潜在错误的情形。</li>
 * <li>ERROR level指出虽然发生错误事件，但仍然不影响系统的继续运行。</li>
 * <li>FATAL level指出每个严重的错误事件将会导致应用程序的退出。</li>
 * </ol>
 *
 * 这些日志等级的概念是否对应，需要依赖于底层日志系统的实现。
 * 虽然预期是这样排序的，但是还是需要实现来保证.</p>
 *
 * <p>性能往往是记录比较关注的.
 * 通过检查适当的属性, 一个组件往往能避免能省下许多开销(如果记录生产信息的话).</p>
 *
 * <p> 例如,
 * <code><pre>
 *    if (log.isDebugEnabled()) {
 *        ... do something expensive ...
 *        log.debug(theResult);
 *    }
 * </pre></code>
 * </p>
 *
 * <p>底层的日志系统一般都会做外部的日志 API 来进行配置, 然后通过某种机制来支持该系统</p>
 *
 * @author <a href="mailto:nielin@dhcc.com.cn">聂林</a>
 * @version $Revision: 1.0 $
 * $Date: 2011-03-01 15:48:25 +0100 (Tue, 01 Mar 2011) $
 */
public interface Log {


    // ----------------------------------------------------- Logging Properties


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
    boolean isDebugEnabled();


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
    boolean isErrorEnabled();


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
    boolean isFatalEnabled();


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
    boolean isInfoEnabled();


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
    boolean isWarnEnabled();

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
    boolean isEnabledFor(Level level);


    // -------------------------------------------------------- Logging Methods


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
    void debug(Object message);


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
    void debug(Object message, Throwable t);


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
    void info(Object message);


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
    void info(Object message, Throwable t);


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
    void warn(Object message);


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
    void warn(Object message, Throwable t);


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
    void error(Object message);


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
    void error(Object message, Throwable t);


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
    void fatal(Object message);


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
    void fatal(Object message, Throwable t);

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
    void log(Level level, Object message);

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
    void log(Level level, Object message, Throwable t);
}
