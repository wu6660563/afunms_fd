package com.afunms.common.util.threadPool;

import java.util.Vector;

import com.afunms.common.util.logging.Log;
import com.afunms.common.util.logging.LogFactory;


/**
 * <p>
 * 线程池缓冲区，向线程池加入需要执行的线程任务，也就是将任务加入到缓冲区中，
 * 然后当线程池有空闲线程时，就从缓冲区中获取一条需要执行的线程任务。
 * </p>
 * @author 聂林
 * @version 1.0 $Date: 2011-03-07 17:23:28 +0100 (Mon, May 4, 2011) $
 */
public class ThreadPoolBuffer {

	/**
     * 日志
     */
    private static Log log = LogFactory.getLog(ThreadPoolBuffer.class);

    /**
     * 用于存储的线程缓冲区
     */
    private Vector<ThreadPoolRunnable> threadPoolRunnableBuffer = null;

    /**
     * 线程池缓冲区所属线程池
     */
    private ThreadPool threadPool = null;

    /**
     * 构造方法
     * @param threadPool 线程池缓冲区所属线程池
     */
    public ThreadPoolBuffer(ThreadPool threadPool) {
        this.threadPool = threadPool;
        threadPoolRunnableBuffer = new Vector<ThreadPoolRunnable>();
    }

    /**
     * 向缓冲区中加入一个需要执行的线程任务
     * @param threadPoolRunnable 需要执行的线程任务
     */
    public void addThreadPoolRunnable(ThreadPoolRunnable threadPoolRunnable) {
        this.threadPoolRunnableBuffer.add(threadPoolRunnable);
    }

    /**
     * 从缓冲区中弹出一个第一个需要执行的线程任务
     * @return 缓冲区中第一个的线程任务
     */
    public synchronized ThreadPoolRunnable popThreadPoolRunnable() {
        ThreadPoolRunnable threadPoolRunnable = null;
        if (!isEmpty()) {
            threadPoolRunnable = this.threadPoolRunnableBuffer.remove(0);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("当前缓冲区为空");
            }
        }
        return threadPoolRunnable;
    }

    /**
     * 判断当前缓冲区是否为空
     * @return 当前缓冲区为空则为 true ; 否则为 false
     */
    public synchronized boolean isEmpty() {
       return this.threadPoolRunnableBuffer.isEmpty();
    }

    /**
     * 返回所属线程池
     * @return the threadPool 所属线程池
     */
    public ThreadPool getThreadPool() {
        return threadPool;
    }

    /**
     * 设置所属线程池
     * @param threadPool 需要设置的线程池
     */
    public void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    /**
     * 获取缓冲区当前需要执行的任务数
     * @return 缓冲区中需要执行的任务数
     */
    public int size() {
        return this.threadPoolRunnableBuffer.size();
    }

}
