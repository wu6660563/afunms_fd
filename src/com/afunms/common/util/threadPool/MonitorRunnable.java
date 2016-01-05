package com.afunms.common.util.threadPool;

import com.afunms.common.util.logging.Level;
import com.afunms.common.util.logging.Log;
import com.afunms.common.util.logging.LogFactory;


/**
 * 用于监控的线程，定期执行清理线程池的动作。
 *
 * @author <a href=nielin@dhcc.com.cn>聂林</a>
 * @version 1.0
 * $Date: 2011-03-07 18:59:45 PM +0100 (Mon, May 4, 2011) $
 */
public class MonitorRunnable implements Runnable {

    /**
     * 日志
     */
    private static Log log = LogFactory.getLog(MonitorRunnable.class);

    /**
     * 所属的线程组
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
     * 监控执行的间隔
     */
    protected long interval;

    /**
     * 名称
     */
    protected String name;

    /**
     * 所属的线程池是否开启日志
     */
    protected boolean isLog;

    /**
     * 构造方法
     * @param threadPool
     *          需要添加监控线程的线程池
     */
    public MonitorRunnable(ThreadPool threadPool) {
        this.setThreadPool(threadPool);
        isLog = getThreadPool().isLog();
        log(Level.DEBUG, "启动线程池监控线程");
        this.start();
    }

    /**
     * 开始运行监控线程
     */
    public void start() {
        this.isTerminate = false;
        this.thread = new Thread(this);
        this.thread.setDaemon(threadPool.isDaemon());
        setName(this.threadPool.getName() + "-Monitor");
        setInterval(threadPool.getWorkWaitTimeout());
        this.thread.start();
    }

    /**
     * 实现 {@link Runnable} 接口
     */
    public void run() {
        while (true) {
            try {
                // 等待时间间隔 进行睡眠
                synchronized (this) {
                    this.wait(interval);
                }

                if (isTerminate) {
                    break;
                }
                log(Level.INFO, "线程池：" + threadPool.getName()
                                + " 在检查和回收空闲线程之前，当前还有："
                                + threadPool.getThreadPoolBuffer().size()
                                + "，个任务还未执行;");
                log(Level.INFO, "线程池：" + threadPool.getName()
                        + " 在检查和回收空闲线程之前，当前线程数（CTC）是："
                        + threadPool.getCurrentThreadCount()
                        + "，当前繁忙线程数（BTC）是："
                        + threadPool.getCurrentThreadsBusy());
                // 检查和回收空闲线程
                this.threadPool.checkSpareControllers();
                log(Level.INFO, "线程池：" + threadPool.getName()
                        + " 在检查和回收空闲线程之后，当前线程数（CTC）是："
                        + threadPool.getCurrentThreadCount()
                        + "，当前繁忙线程数（BTC）是："
                        + threadPool.getCurrentThreadsBusy());
            } catch (InterruptedException e) {
                log(Level.ERROR, "线程池监控线程:" + getName()
                        +  " 出现异常！！！ Unexpected exception", e);
            }
        }
    }

    /**
     * 停止监控线程，与 <code>terminate()</code> 效果一样
     * @see #terminate()
     */
    public void stop() {
        this.terminate();
    }

    /**
     * 中断监控线程
     */
    public synchronized void terminate() {
        this.isTerminate = true;
        this.notify();
    }

    /**
     * 返回所属的线程池
     * @return the threadPool
     */
    public ThreadPool getThreadPool() {
        return threadPool;
    }

    /**
     * 设置所属线程池
     * @param threadPool the threadPool to set
     */
    public void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    /**
     * 获取监控线程轮询间隔
     * @return the interval 轮询间隔
     */
    public long getInterval() {
        return interval;
    }

    /**
     * 设置监控线程轮询间隔
     * @param interval the interval to set 需要设置的轮询间隔
     */
    public void setInterval(long interval) {
        this.interval = interval;
    }

    /**
     * 返回线程名称
     * @return the name 线程名称
     */
    public String getName() {
        name = thread.getName();
        return name;
    }

    /**
     * 设置线程名称
     * @param name the name to set 需要设置的线程名称
     */
    public void setName(String name) {
        this.name = name;
        this.thread.setName(name);
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
