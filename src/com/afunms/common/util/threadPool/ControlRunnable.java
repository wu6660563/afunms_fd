package com.afunms.common.util.threadPool;

import com.afunms.common.util.logging.Level;
import com.afunms.common.util.logging.Log;
import com.afunms.common.util.logging.LogFactory;



/**
 * 由线程池控制和用于执行各种操作(即 {@link ThreadPoolRunnable} )
 * 的一个线程对象。
 *
 * @author 聂林
 * @version 1.0 $Date: 2011-03-07 16:34:30 +0100 (Mon, Mar 7, 2011) $
 */
public class ControlRunnable implements Runnable {

    /**
     * 日志
     */
    private Log log = LogFactory.getLog(ControlRunnable.class);

    /**
     * 所属的线程组
     */
    protected ThreadPool threadPool;

    /**
     * 是否中断
     */
    protected boolean shouldTerminate;

    /**
     * 用于激活执行线程
     */
    protected boolean shouldRun;

    /**
     * 该线程执行的方法：
     */

    /**
     * 1. Runnable 形式
     */
    protected Runnable toRunRunnable;

    /**
     * 2. ThreadPoolRunnable 的形式
     */
    protected ThreadPoolRunnable toRun;

    /**
     * 用于执行的线程
     */
    protected Thread thread;

    /**
     * 该线程的属性
     */
    protected ThreadPoolRunnableAttributes runnableAttributes;

    /**
     * 所属的线程池是否开启日志
     */
    protected boolean isLog;

    /**
     * 序列
     */
    protected int sequence;

    /**
     * 在所属的线程池中创建一个新线程
     * @param threadPool 所属线程池
     */
    protected ControlRunnable(ThreadPool threadPool) {
        this.threadPool = threadPool;
        toRun = null;
        shouldTerminate = false;
        shouldRun = false;
        isLog = this.threadPool.isLog();
        thread = new Thread(this);
        thread.setDaemon(this.threadPool.isDaemon());
        sequence = this.threadPool.incSequence();
        thread.setName(this.threadPool.getName()
            + "-Processor-" + sequence);
        thread.setPriority(threadPool.getThreadPriority());
        this.threadPool.addThread(thread, this);
        thread.start();
    }

    /**
     * 实现 {@link Runnable} 方法
     */
    public void run() {
        boolean shouldRunTemp = false;
        boolean shouldTerminateTemp = false;
        ThreadPoolRunnable toRunTemp = null;
        try {
            while (true) {
                try {
                    /* Wait for work. */
                    synchronized (this) {
                        while (!shouldRun && !shouldTerminate) {
                            this.wait();
                        }
                        shouldRunTemp = shouldRun;
                        shouldTerminateTemp = shouldTerminate;
                        toRunTemp = toRun;
                    }

                    if (shouldTerminateTemp) {
                        log(Level.DEBUG, "Terminate");
                        break;
                    }

                    /* 检查是否可以执行一个线程 */
                    try {
                        if (toRunTemp != null) {
                            runnableAttributes =
                                toRunTemp.getThreadPoolRunnableAttributes();
                            if (runnableAttributes != null) {
                                try {
                                    thread.setPriority(
                                            runnableAttributes.getPriority());
                                } catch (Exception e) {
                                    log(Level.ERROR, "设置的线程池优先级不在：0~9之间", e);
                                }
                                if (runnableAttributes.getThreadName()
                                        != null) {
                                    thread.setName(this.threadPool.getName()
                                            + "-Processor-"
                                            + runnableAttributes
                                            .getThreadName());
                                } else {
                                    thread.setName(this.threadPool.getName()
                                            + "-Processor-" + sequence);
                                }
                            } else {
                                thread.setPriority(
                                        threadPool.getThreadPriority());
                                thread.setName(this.threadPool.getName()
                                        + "-Processor-" + sequence);
                            }
                             // log(Level.DEBUG, "获取执行线程属性");
                        }
                        if (shouldRunTemp) {
                            if (toRunTemp != null) {
                                toRunTemp.runIt(runnableAttributes);
                            } else if (toRunRunnable != null) {
                                toRunRunnable.run();
                            } else {
                                log(Level.WARN, "获取到的执行线程为 null ???");
                            }
                        }
                    } catch (Throwable t) {
                        log(Level.ERROR, "执行线程执行过程中出现异常，将线程进行释放！！！", t);
                        /*
                         *
                         * 如果这个 runnable 抛出异常 (甚至可能是一个死线程)，这说明
                         * 这个线程已经死亡。
                         *
                         * 也就意味着我们需要从线程池中释放这个线程
                         *
                         */
                        shouldTerminateTemp = true;
                        shouldRunTemp = false;
                        threadPool.notifyThreadEnd(this);
                    } finally {
                        if (shouldRunTemp) {
                            shouldRun = false;
                            /*
                             * 通知线程池，该线程现在处于空闲状态
                             */
                            threadPool.returnController(this);
                        }
                    }

                    /*
                     * 检查是否终止
                     */
                    if (shouldTerminateTemp) {
                        break;
                    }
                } catch (InterruptedException ie) {
                    /*
                     * 为了等待一直能够持续下去，我们不会调用中断，
                     * 所以这个不会发生
                     */
                    log(Level.ERROR, "未知的错误", ie);
                }
            }
        } finally {
            threadPool.removeThread(Thread.currentThread());
        }
    }

    /**
     * Run a task
     *
     * @param toRun 需要执行的任务
     */
    public synchronized void runIt(Runnable toRun) {
        this.toRunRunnable = toRun;
        // Do not re-init, the whole idea is to run init only once per
        // thread - the pool is supposed to run a single task, that is
        // initialized once.
        // noThData = true;
        shouldRun = true;
        this.notify();
    }

    /**
     * Run a task
     *
     * @param toRun 需要执行的任务
     */
    public synchronized void runIt(ThreadPoolRunnable toRun) {
        this.toRun = toRun;
        // Do not re-init, the whole idea is to run init only once per
        // thread - the pool is supposed to run a single task, that is
        // initialized once.
        // noThData = true;
        shouldRun = true;
        this.notify();
    }

    /**
     * 终止该线程
     */
    public synchronized void terminate() {
        shouldTerminate = true;
        this.notify();
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
