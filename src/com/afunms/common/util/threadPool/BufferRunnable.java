package com.afunms.common.util.threadPool;

import com.afunms.common.util.logging.Level;
import com.afunms.common.util.logging.Log;
import com.afunms.common.util.logging.LogFactory;


/**
 * <p>
 * 用于处理线程池缓冲区中需要执行的操作，所有需要处理的操作先加入到
 * 缓冲区中，然后由该线程每次将缓冲区的操作加入到线程池中运行。
 * </p>
 * @author 聂林
 */
public class BufferRunnable implements Runnable {

    /**
     * 日志
     */
    private static Log log = LogFactory.getLog(BufferRunnable.class);

    /**
     * 所属的线程池
     */
    protected ThreadPool threadPool;

    /**
     * 所属线程
     */
    protected Thread thread;

    /**
     * 是否中断
     */
    protected boolean isTerminate;

    /**
     * 名称
     */
    protected String name;

    /**
     * 所属的线程池是否开启日志
     */
    protected boolean isLog;

    /**
     * <code>ThreadPoolBuffer</code> 缓冲区
     */
    protected ThreadPoolBuffer threadPoolBuffer = null;

    /**
     * 当前执行的线程 <code>ThreadPoolRunnable</code>
     */
    protected ThreadPoolRunnable threadPoolRunnable = null;

    /**
     * 构造方法
     * @param threadPool
     *          需要添加缓冲线程的线程池
     */
    public BufferRunnable(ThreadPool threadPool) {
        this.setThreadPool(threadPool);
        this.start();
    }

    /**
     * 开始运行缓冲区线程
     */
    public void start() {
        this.isTerminate = false;
        this.thread = new Thread(this);
        this.thread.setDaemon(threadPool.isDaemon());
        setName(this.threadPool.getName() + "-Buffer");
        isLog = this.threadPool.isLog();
        setThreadPoolBuffer(threadPool.getThreadPoolBuffer());
        log(Level.DEBUG, "启动线程池缓冲区线程");
        this.thread.start();
    }

    /**
     * 该方法为实现 Runnable 接口 run() 方法
     */
    public void run() {
        while (true) {
            try {
                // 如果缓冲区内没有任何需要执行的 ThreadPoolRunnable.
                synchronized (this) {
                    while (threadPoolBuffer == null
                        || threadPoolBuffer.size() == 0) {
                        this.wait();
                        if (isTerminate) {
                            threadPoolBuffer = null;
                            break;
                        }
                    }
                    if (isTerminate) {
                        break;
                    }
                    threadPoolRunnable =
                        threadPoolBuffer.popThreadPoolRunnable();
                }
//                if (log.isDebugEnabled()) {
//                    log.debug("从缓冲区中获取一个需要执行的 ThreadPoolRunnable");
//                }
                // 执行
                this.threadPool.runIt(threadPoolRunnable);
            } catch (InterruptedException e) {
                log(Level.ERROR, "线程池缓冲线程:" + getName()
                    +  " 出现异常！！ Unexpected exception", e);
            }
        }
    }

    /**
     * 停止监控线程，与 <code>terminate()</code> 效果相同
     * @see #terminate()
     */
    public void stop() {
        this.terminate();
    }

    /**
     * 执行缓冲线程
     */
    public synchronized void runIt() {
        this.isTerminate = false;
        this.notify();
    }

    /**
     * 中断缓冲线程
     */
    public synchronized void terminate() {
        this.isTerminate = true;
        this.notify();
    }

    /**
     * <p>获取线程池
     * @return the threadPool
     */
    public ThreadPool getThreadPool() {
        return threadPool;
    }

    /**
     * 指定线程池
     * @param threadPool the threadPool to set
     */
    public void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    /**
     * 返回线程名称
     * @return the name
     */
    public String getName() {
        name = thread.getName();
        return name;
    }

    /**
     * 指定线程名称
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
        this.thread.setName(name);
    }

    /**
     * 返回线程池的缓冲区
     * @return the threadPoolBuffer
     *          返回线程池中的缓冲区
     */
    protected ThreadPoolBuffer getThreadPoolBuffer() {
        return threadPoolBuffer;
    }

    /**
     * 设置线程池的缓冲区
     * @param threadPoolBuffer the threadPoolBuffer to set
     *          需要设置的线程池缓冲区
     */
    protected void setThreadPoolBuffer(ThreadPoolBuffer threadPoolBuffer) {
        this.threadPoolBuffer = threadPoolBuffer;
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

}
