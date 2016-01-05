/*
 *  该线程池为采集器提供采集线程的线程池，按照最初的想法，采集的采集线程共用一个线程池。
 *  当采集器启动成时将采集线程加入到线程池中。
 *  如有添加或修改，请在此注明。
 */

package com.afunms.common.util.threadPool;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Pattern;

import com.afunms.common.util.logging.Level;
import com.afunms.common.util.logging.Log;
import com.afunms.common.util.logging.LogFactory;



/**
 *
 * 模仿 Tomcat 的线程池
 *
 * @author <a href=nielin@dhcc.com.cn>聂林</a>
 * @version 1.0
 * $Date: 2011-03-07 18:59:45 PM +0100 (Mon, May 4, 2011) $
 */
public class ThreadPool {

    /**
     *  日志
     */
    private static Log log = LogFactory.getLog(ThreadPool.class);

    /**
     *  默认的是否记录线程池的日志
     */
    public static final boolean IS_LOG = true;

    /**
     *  默认的最大线程数  默认值
     */
    public static final int MAX_THREADS = 200;

    /**
     *  默认的最小线程数  默认值
     */
    public static final int MAX_THREADS_MIN = 10;

    /**
     *  默认的最大的空闲线程数  默认值
     */
    public static final int MAX_SPARE_THREADS = 50;

    /**
     *  默认的最小的空闲线程数  默认值
     */
    public static final int MIN_SPARE_THREADS = 5;

    /**
     *  默认的最大工作等待时间(即回收空闲线程间隔时间)  默认值
     */
    public static final long WORK_WAIT_TIMEOUT = 60 * 1000;

    /**
     *  默认的线程池的线程优先级  默认值
     */
    public static final int THREAD_PRIORITIY = Thread.NORM_PRIORITY;

    /**
     *  默认的线程池的线程优先级  默认值
     */
    public static final String THREAD_NAME = "TP";

    /**
     *  线程池的最大线程数
     */
    protected int maxThreads;

    /**
     *  线程池最小空闲线程数
     */
    protected int minSpareThreads;

    /**
     *  线程池最大空闲线程数
     */
    protected int maxSpareThreads;

    /**
     *  当前线程池当中的线程数
     */
    protected int currentThreadCount;

    /**
     *  当前工作的线程数
     */
    protected int currentThreadsBusy;

    /**
     *  是否暂停线程池(即暂停线程池当中的所有线程数)
     */
    protected boolean stopThePool;

    /**
     *  用于标记主线程是否为守护线程 'daemon'
     */
    protected boolean isDaemon = false;

    /**
     * 是否记录线程池的日志
     */
    protected boolean isLog = true;

    /**
     *  线程池名称
     */
    protected String name;

    /**
     *  线程值的属性
     */
    protected Properties properties = null;

    /**
     *  所有执行线程数组
     */
    protected ControlRunnable[] pool = null;

    /**
     * 用于监控和清理的线程
     */
    protected MonitorRunnable monitorRunnable = null;

    /**
     * 用于将缓冲区的 <code>ThreadPoolRunnable</code>
     * 加入到线程池中运行
     */
    protected BufferRunnable bufferRunnable = null;

    /**
     * <code>ThreadPoolRunnable</code> 缓冲区
     */
    protected ThreadPoolBuffer threadPoolBuffer = new ThreadPoolBuffer(this);

    /**
     * <p>用于存放打开的线程。</p>
     * <p>键是：<code>Thread</code> ，值是：<code>ControlRunnable</code></p>
     */
    protected Hashtable<Thread, ControlRunnable> threads
        = new Hashtable<Thread, ControlRunnable>();

    /**
     * 线程池监听集合
     */
    protected Vector<ThreadPoolListener> listeners
        = new Vector<ThreadPoolListener>();

    /**
     * 序列
     */
    protected int sequence = 1;

    /**
     * 线程优先级
     */
    protected int threadPriority;

    /**
     * 工作等待的超时时间，即：空闲线程回收的等待时间
     */
    protected long workWaitTimeout;

    /**
     * 使用默认的参数来构造一个线程池
     */
    public ThreadPool() {
        initialize();
    }

    /**
     * 使用指定的初始化属性来构造一个线程池
     * @param properties 需要指定的属性
     */
    public ThreadPool(Properties properties) {
        setProperties(properties);
        initialize();
    }

    /**
     * <p>创建一个线程池实例。</p>
     * <p>因参数 <code>jmx</code> 暂未使用，该方法等同于返回
     * 一个默认属性的线程池。</p>
     * @param jmx
     *            暂未使用(UNUSED)
     * @return 线程池<code>ThreadPool</code> 的实例.
     *         If JMX support is requested, you need to
     *         call register() in order to set a name.
     */
    public static ThreadPool createThreadPool(boolean jmx) {
        return new ThreadPool();
    }

    /**
     * 线程池初始化
     */
    protected void initialize() {
        isLog = true;
        name = THREAD_NAME;
        maxThreads = MAX_THREADS;
        maxSpareThreads = MAX_SPARE_THREADS;
        minSpareThreads = MIN_SPARE_THREADS;
        threadPriority = THREAD_PRIORITIY;
        workWaitTimeout = WORK_WAIT_TIMEOUT;
        currentThreadCount = 0;
        currentThreadsBusy = 0;
        stopThePool = false;

        if (properties != null) {
            String nameProperty =
                properties.getProperty(ThreadPoolProperitesConstant.NAME);
            if (nameProperty != null && nameProperty.trim().length() > 0) {
                name = nameProperty.trim();
            } else {
                log(Level.DEBUG , "线程池名称未设置或者是设置出错，使用默认设置：" + name);
            }

            Pattern pattern = Pattern.compile("[0-9]+");

            String maxThreadsProperty = properties.getProperty(
                ThreadPoolProperitesConstant.MAX_THREADS);
            if (maxThreadsProperty != null
                && pattern.matcher(maxThreadsProperty).matches()) {
                if (Integer.valueOf(maxThreadsProperty) >= MAX_THREADS_MIN) {
                    maxThreads = Integer.valueOf(maxThreadsProperty);
                } else {
                    log(Level.DEBUG, "线程池最大线程数不能小于:"
                            + maxThreads + "，使用默认设置：" + maxThreads);
                }
            } else {
                log(Level.DEBUG, "线程池最大线程数没有设置"
                        + "或者是设置出错，使用默认设置：" + maxThreads);
            }

            String maxSpareThreadsProperty = properties.getProperty(
                    ThreadPoolProperitesConstant.MAX_SPARE_THREADS);
            if (maxSpareThreadsProperty != null
                && pattern.matcher(maxSpareThreadsProperty).matches()) {
                maxSpareThreads = Integer.valueOf(maxSpareThreadsProperty);
            } else {
                log(Level.DEBUG, "线程池最大空闲数没有设置"
                        + "或者是设置出错，使用默认设置：" + maxSpareThreads);
            }

            String minSpareThreadsProperty = properties.getProperty(
                ThreadPoolProperitesConstant.MIN_SPARE_THREADS);
            if (minSpareThreadsProperty != null
                && pattern.matcher(minSpareThreadsProperty).matches()) {
                minSpareThreads = Integer.valueOf(minSpareThreadsProperty);
            } else {
                log(Level.DEBUG, "线程池最小空闲数没有设置"
                        + "或者是设置出错，使用默认设置：" + minSpareThreads);
            }

            String threadPriorityProperty = properties.getProperty(
                ThreadPoolProperitesConstant.THREAD_PRIORITIY);
            if (threadPriorityProperty != null
                && pattern.matcher(threadPriorityProperty).matches()) {
                if (Integer.valueOf(threadPriorityProperty)
                        >= Thread.MIN_PRIORITY
                    && Integer.valueOf(threadPriorityProperty)
                    <= Thread.MAX_PRIORITY) {
                    threadPriority = Integer.valueOf(threadPriorityProperty);
                } else {
                    log(Level.DEBUG, "线程池优先级设定不在1~10范围内，"
                            + "使用默认设置：" + threadPriority);
                }
            }

            String workWaitTimeoutProperty = properties.getProperty(
                ThreadPoolProperitesConstant.WORK_WAIT_TIMEOUT);
            if (workWaitTimeoutProperty != null
                && pattern.matcher(workWaitTimeoutProperty).matches()) {
                workWaitTimeout = Long.valueOf(workWaitTimeoutProperty);
            } else {
                log(Level.DEBUG, "线程池中线程的工作等待时间"
                        + "（即：空闲线程被回收的时间间隔）没有设置或者是设置出错，"
                        + "使用默认设置（单位毫秒）：" + workWaitTimeout);
            }

            String isLogProperty =
                properties.getProperty(ThreadPoolProperitesConstant.IS_LOG);
            if (isLogProperty != null && isLogProperty.trim().length() > 0) {
                try {
                    isLog = Boolean.valueOf(isLogProperty.trim());
                } catch (Exception e) {
                    isLog = IS_LOG;
                    log(Level.DEBUG, "线程池中线程的工作等待时间"
                            + "（即：空闲线程被回收的时间间隔）没有设置或者是设置出错，"
                            + "使用默认设置（单位毫秒）：" + isLog, e);
                }
            } else {
                isLog = IS_LOG;
                log(Level.DEBUG, "线程池中线程的工作等待时间"
                        + "（即：空闲线程被回收的时间间隔）没有设置或者是设置出错，"
                        + "使用默认设置（单位毫秒）：" + isLog);
            }
        } else {
            log(Level.DEBUG, "线程池属性集为空，使用默认设置初始化");
            properties = new Properties();
            isLog = IS_LOG;
            properties.setProperty(ThreadPoolProperitesConstant.NAME, name);
            properties.setProperty(
                    ThreadPoolProperitesConstant.MAX_THREADS,
                    String.valueOf(maxThreads));
            properties.setProperty(
                    ThreadPoolProperitesConstant.MAX_SPARE_THREADS,
                    String.valueOf(maxSpareThreads));
            properties.setProperty(
                    ThreadPoolProperitesConstant.MIN_SPARE_THREADS,
                    String.valueOf(minSpareThreads));
            properties.setProperty(
                    ThreadPoolProperitesConstant.THREAD_PRIORITIY,
                    String.valueOf(threadPriority));
            properties.setProperty(
                    ThreadPoolProperitesConstant.WORK_WAIT_TIMEOUT,
                    String.valueOf(workWaitTimeout));
            properties.setProperty(
                    ThreadPoolProperitesConstant.IS_LOG,
                    String.valueOf(isLog));
            properties.setProperty(
                    ThreadPoolProperitesConstant.WORK_WAIT_TIMEOUT,
                    String.valueOf(workWaitTimeout));

        }

        log.info("当前线程池名称配置为：" + name);
        log.info("当前线程池最大线程数配置为：" + maxThreads);
        log.info("当前线程池最大空闲线程数配置为：" + maxSpareThreads);
        log.info("当前线程池最小空闲线程数配置为：" + minSpareThreads);
        log.info("当前线程池线程优先级配置为：" + threadPriority);
        log.info("当前线程池线程的工作等待时间（即：空闲线程被回收的时间间隔）配置为：" + workWaitTimeout);
        log.info("当前线程池的日志配置为：" + isLog);
        if (!isLog) {
            log.info("不再打印线程池：" + name + " 日志!!!");
        }
    }

    /**
     * <p>启动线程池，并返回启动线程池是否成功</p>
     * @return 返回启动线程池是否成功
     */
    public synchronized boolean start() {
        stopThePool = false;
        currentThreadCount = 0;
        currentThreadsBusy = 0;

        adjustLimits();

        pool = new ControlRunnable[maxThreads];

        openThreads(minSpareThreads);
        if (maxSpareThreads < maxThreads) {
            monitorRunnable = new MonitorRunnable(this);
            bufferRunnable = new BufferRunnable(this);
        }

        return false;
    }

    /**
     * 创建未启动的线程
     *
     * @param toOpen
     *            需要打开的线程数
     */
    protected void openThreads(int toOpen) {

        if (toOpen > maxThreads) {
            toOpen = maxThreads;
        }

        for (int i = currentThreadCount; i < toOpen; i++) {
            pool[i - currentThreadsBusy] = new ControlRunnable(this);
        }

        currentThreadCount = toOpen;
    }

    /**
     * 由监控线程来检查和回收空闲线程
     */
    protected synchronized void checkSpareControllers() {

        if (stopThePool) {
            return;
        }

        if ((currentThreadCount - currentThreadsBusy) > maxSpareThreads) {
            int toFree = currentThreadCount - currentThreadsBusy
                - maxSpareThreads;

            for (int i = 0; i < toFree; i++) {
                ControlRunnable controlRunnable = pool[currentThreadCount
                    - currentThreadsBusy - 1];
                controlRunnable.terminate();
                pool[currentThreadCount - currentThreadsBusy - 1] = null;
                currentThreadCount--;
            }
        }
    }

    /**
     * 检查配置问题，并解决。此修复程序提供了默认的设置
     */
    protected void adjustLimits() {
        if (maxThreads <= 0) {
            maxThreads = MAX_THREADS;
        } else if (maxThreads < MAX_THREADS_MIN) {
            maxThreads = MAX_THREADS_MIN;
            log.warn("线程池最大线程数不能小于:"
                + maxThreads + "，当前设置：" + maxThreads);
        }

        if (maxSpareThreads >= maxThreads) {
            maxSpareThreads = maxThreads;
            log.warn("线程池最大空闲线程数"
                + "不能大于最大线程数，当前设置：" + maxSpareThreads);
        }

        if (maxSpareThreads <= 0) {
            if (1 == maxThreads) {
                maxSpareThreads = 1;
            } else {
                maxSpareThreads = maxThreads / 2;
            }
            log.warn("线程池最大空闲线程数不能小于0，当前设置：" + maxSpareThreads);
        }

        if (minSpareThreads > maxSpareThreads) {
            minSpareThreads = maxSpareThreads;
            log.warn("线程池最小空闲线程数"
                + "不能大于最大空闲线程数，当前设置：" + minSpareThreads);
        }

        if (minSpareThreads <= 0) {
            if (1 == maxSpareThreads) {
                minSpareThreads = 1;
            } else {
                minSpareThreads = maxSpareThreads / 2;
            }
            log.warn("线程池最小空闲线程数不能小于0，当前设置：" + maxSpareThreads);
        }
    }

    /**
     * 停止线程池
     */
    public synchronized void shutdown() {
        if (!stopThePool) {
            stopThePool = true;
            if (monitorRunnable != null) {
                // 停止监控线程
                monitorRunnable.terminate();
                monitorRunnable = null;
            }
            if (bufferRunnable != null) {
                // 停止缓冲区线程
                bufferRunnable.terminate();
                bufferRunnable = null;
            }
            for (int i = 0; i < currentThreadCount - currentThreadsBusy; i++) {
                try {
                    pool[i].terminate();
                } catch (Throwable t) {
                    // 不做任何事...继续运行，停止线程池，并不会停止其他的运行
                    log.error("Ignored exception while "
                        + "shutting down thread pool",
                                t);
                }
            }
            currentThreadsBusy = 0;
            currentThreadCount = 0;
            pool = null;
            notifyAll();
        }
    }

    /**
     * 向线程池的缓冲区中添加一个需要运行的 <code>ThreadPoolRunnable</code> ,
     * 一旦加入到缓存区中，如果线程池已经启动，则会加入到线程池中自动运行。
     * 如果线程池的所有线程都在忙，则会一直等待一个空闲线程来运行。
     * @param r
     *          需要运行的 ThreadPoolRunnable
     */
    public void add(ThreadPoolRunnable r) {
        if (null == r) {
            throw new NullPointerException();
        }

        threadPoolBuffer.addThreadPoolRunnable(r);

        bufferRunnable.runIt();
    }

    /**
     * 运行给定的 ThreadPoolRunnable
     * @param r
     *          给定的 ThreadPoolRunnable
     */
    public void runIt(ThreadPoolRunnable r) {
        if (null == r) {
            throw new NullPointerException();
        }

        ControlRunnable c = findControlRunnable();
        c.runIt(r);
    }

    /**
     * 查找一个可用的空闲线程
     * @return 返回一个可用的空闲线程
     */
    private ControlRunnable findControlRunnable() {
        ControlRunnable c = null;

        if (stopThePool) {
            throw new IllegalStateException();
        }

        // 从线程池中获取一个空线程
        synchronized (this) {

            while (currentThreadsBusy == currentThreadCount) {
                // 所有线程都在忙
                if (currentThreadCount < maxThreads) {
                    // 如果所有的线程都是打开的，则打开一组新的空线程线程，该线程数为最小空闲数
                    int toOpen = currentThreadCount + minSpareThreads;
                    openThreads(toOpen);
                } else {
                    log(Level.DEBUG, "等待线程池空闲线程，当前线程：" + currentThreadCount
                            + " 最大线程数：" + maxThreads);
                    // 等待线程到达空闲
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        // 这里仅仅是用于捕获 Throwable;
                        // 因为不可能会由 wait() 抛出其他异常;
                        // 因此我这里只是用于捕获一下异常; 因为没有在任何地方使用线程中断，所以
                        // 这个异常永远不会发生。
                        log(Level.ERROR,
                                "等待空闲线程出现异常！！！Unexpected exception",
                                e);
                    }
                    log(Level.DEBUG, "完成等待空闲线程: CTC=" + currentThreadCount
                            + ", CTB=" + currentThreadsBusy);
                    // 如果停止线程池，则退出
                    if (stopThePool) {
                        break;
                    }
                }
            }
            // 线程池已经停止，抛出异常
            if (0 == currentThreadCount || stopThePool) {
                throw new IllegalStateException();
            }

            // 如果到达这里，则说明有一个空闲线程，则拿去使用。
            int pos = currentThreadCount - currentThreadsBusy - 1;
            c = pool[pos];
            pool[pos] = null;
            currentThreadsBusy++;
        }
        return c;
    }

    /**
     *
     * 通知线程池，指定的线程已经完成。
     *
     * 当 {@link ControlRunnable}  执行中抛出异常时来调用。
     *
     * @param c 线程
     *
     */
    protected synchronized void notifyThreadEnd(ControlRunnable c) {
        currentThreadsBusy--;
        currentThreadCount--;
        notify();
    }

    /**
     * 将线程返回到线程池中。
     * 由于这个线程返变成空闲线程，则由线程池来分配调用。
     *
     * @param c 线程
     */
    protected synchronized void returnController(ControlRunnable c) {

        if (0 == currentThreadCount || stopThePool) {
            c.terminate();
            return;
        }

        // atomic
        currentThreadsBusy--;

        pool[currentThreadCount - currentThreadsBusy - 1] = c;
        notify();
    }

    /**
     * 向线程中添加一个线程
     * @param thread 需要添加的线程
     * @param cr     线程所属的控制线程
     */
    public void addThread(Thread thread, ControlRunnable cr) {
//      threads.put(t, cr);
        for (int i = 0; i < listeners.size(); i++) {
            ThreadPoolListener threadPoolListener =
                (ThreadPoolListener) listeners.elementAt(i);
            threadPoolListener.threadEnd(this, thread);
        }
    }

    /**
     * 移除指定的线程
     * @param thread
     *          需要移除的线程
     */
    public void removeThread(Thread thread) {
//        threads.remove(thread);
        for (int i = 0; i < listeners.size(); i++) {
            ThreadPoolListener threadPoolListener =
                (ThreadPoolListener) listeners.elementAt(i);
            threadPoolListener.threadEnd(this, thread);
        }
    }

    /**
     * 记录日志
     * @param level
     *          日志等级
     * @param message
     *          日志消息对象
     */
    private void log(Level level, Object message) {
        if (isLog && log.isEnabledFor(level)) {
            log.log(level, message);
        }
    }

    /**
     * 记录日志
     * @param level
     *          日志等级
     * @param message
     *          日志消息对象
     * @param t
     *          日志异常，包括其堆栈跟踪
     */
    private void log(Level level, Object message, Throwable t) {
        if (isLog && log.isEnabledFor(level)) {
            log.log(level, message, t);
        }
    }


    // --------------------------------------------------------- 获取和设置属性方法


    /**
     * 返回线程池的属性集
     * @return {@link Properties}
     *          -- 线程池的属性集
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * 设置线程池的属性集
     * @param properties
     *          线程池的属性集
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }


    /**
     * 获取线程池名称。
     * @return {@link String}
     *          -- 返回线程池的名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置线程池名称。
     * @param name
     *          线程池名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取线程池里线程设置默认的优先级。
     * @return {@link String}
     *          -- 线程池给线程默认的优先级
     */
    public int getThreadPriority() {
        return threadPriority;
    }

    /**
     * 返回线程池日志是否启用
     * @return {@link Boolean}
     *          -- 如果启用线程池的日志，则返回 true , 否则返回 false
     */
    public boolean isLog() {
        return isLog;
    }

    /**
     * 设置线程池日志是否启用
     * @param isLog
     *          给定的是否启用线程池日志
     */
    public void setLog(boolean isLog) {
        this.isLog = isLog;
    }

    /**
     * 设置线程池给线程设置默认的优先级。
     * @param threadPriority
     *          线程池给线程默认设置的优先级
     */
    public void setThreadPriority(int threadPriority) {
        if (log.isDebugEnabled()) {
            log.debug(getClass().getName() + ": setPriority(" + threadPriority
                + "): here.");
        }

        if (threadPriority < Thread.MIN_PRIORITY) {
            throw new IllegalArgumentException("new priority < MIN_PRIORITY");
        } else if (threadPriority > Thread.MAX_PRIORITY) {
            throw new IllegalArgumentException("new priority > MAX_PRIORITY");
        }

        this.threadPriority = threadPriority;

        Enumeration<Thread> currentThreads = getThreads();
        Thread t = null;
        while (currentThreads.hasMoreElements()) {
            // 设置当前所有线程的优先级
            t = (Thread) currentThreads.nextElement();
            t.setPriority(threadPriority);
        }
    }

    /**
     * 设置最大线程数。
     * @param maxThreads
     *          最大线程数
     */
    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    /**
     * 返回最大线程数。
     * @return {@link Integer}
     *          最大线程数
     */
    public int getMaxThreads() {
        return maxThreads;
    }

    /**
     * 设置最小空闲线程数
     * @param minSpareThreads
     *          最小空闲线程数
     */
    public void setMinSpareThreads(int minSpareThreads) {
        this.minSpareThreads = minSpareThreads;
    }

    /**
     * 返回最小空闲线程数。
     * @return {@link Integer}
     *          最小空闲线程数
     */
    public int getMinSpareThreads() {
        return minSpareThreads;
    }

    /**
     * 设置最大空闲线程数。
     * @param maxSpareThreads
     *          最大空闲线程数
     */
    public void setMaxSpareThreads(int maxSpareThreads) {
        this.maxSpareThreads = maxSpareThreads;
    }

    /**
     * 返回最大空闲线程数。
     * @return {@link Integer}
     *          -- 最大空闲线程数
     */
    public int getMaxSpareThreads() {
        return maxSpareThreads;
    }

    /**
     * 返回线程池中的缓冲区
     * @return {@link ThreadPoolBuffer}
     *          -- 缓冲区
     */
    public ThreadPoolBuffer getThreadPoolBuffer() {
        return threadPoolBuffer;
    }

    /**
     * 设置线程池缓冲区
     * @param threadPoolBuffer the threadPoolBuffer to set
     *          需要设置的缓冲区
     */
    public void setThreadPoolBuffer(
        ThreadPoolBuffer threadPoolBuffer) {
        this.threadPoolBuffer = threadPoolBuffer;
    }

    /**
     * 返回工作等待的超时时间，即：空闲线程回收的等待时间。单位：毫秒
     * @return {@link Long}
     *          -- 工作等待的超时时间，即：空闲线程回收的等待时间。单位：毫秒
     */
    public long getWorkWaitTimeout() {
        return workWaitTimeout;
    }

    /**
     * 设置工作等待的超时时间，即：空闲线程回收的等待时间。单位：毫秒
     * @param workWaitTimeout the workWaitTimeout to set
     *          需要设置的工作等待的超时时间，即：空闲线程回收的等待时间。单位：毫秒
     */
    public void setWorkWaitTimeout(long workWaitTimeout) {
        this.workWaitTimeout = workWaitTimeout;
    }


    // --------------------------------------------------------- 获取只读属性的方法

    /**
     * 获取线程池中线程的枚举
     * @return 返回线程的枚举
     */
    public Enumeration<Thread> getThreads() {
        return threads.keys();
    }

    /**
     * 获取当前线程数。
     * @return 当前线程数
     */
    public int getCurrentThreadCount() {
        return currentThreadCount;
    }

    /**
     * 获取当前繁忙(工作)的线程数
     * @return 当前繁忙(工作)的线程数
     */
    public int getCurrentThreadsBusy() {
        return currentThreadsBusy;
    }

    /**
     * 获取是否为守护线程。
     * @return 是否为守护线程
     */
    public boolean isDaemon() {
        return isDaemon;
    }

    /**
     * 对序列自动增长，用于标记在线程池中实例化的执行线程
     * @return {@link Integer}
     *          自动增长后的序列号
     */
    protected int incSequence() {
        return sequence++;
    }

    /**
     * 默认的 main() 方法，用来测试线程池
     * @param args
     *          默认的参数
     */
    public static void main(String[] args) {
        final ThreadPool pool = new ThreadPool();
        pool.start();
        final int num = 100;
        final int times = 3;
        for (int i = 0; i < num; i++) {
            final int j = i;
            pool.add(new ThreadPoolRunnable() {

                public ThreadPoolRunnableAttributes
                    getThreadPoolRunnableAttributes() {
                    return new ThreadPoolRunnableAttributes();
                }

                public void runIt(
                        ThreadPoolRunnableAttributes
                        threadPoolRunnableAttributes) {
                    int k = 0;
                    while (true) {
                        try {
                            final int time = 1000;
                            Thread.sleep(time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(
                                "第j:" + j + "个线程========第k:" + k + "次运行");
                        k++;
                        if (k == times) {
                            break;
                        }
                    }
                }

            });
        }
    }

}
