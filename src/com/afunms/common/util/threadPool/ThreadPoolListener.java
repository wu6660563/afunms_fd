package com.afunms.common.util.threadPool;

/**
 * <p>线程池的监听器。</p>
 *
 * <p>用于监听线程池创建线程和停止线程。
 * @author 聂林
 * @version 1.0 $Date: 2011-03-13 00:41:40 +0100 (Sun, Mar 13, 2011) $
 *
 */
public interface ThreadPoolListener {
    /**
     * Interface to allow applications to be notified when a threads are created
     * and stopped.
     */
    /**
     * 需要实现的线程开始的方法
     * @param threadPool 所属线程池
     * @param thread 所属线程
     */
    void threadStart(ThreadPool threadPool, Thread thread);

    /**
     * 需要实现的线程完成的方法
     * @param threadPool 所属线程池
     * @param thread 所属线程
     */
    void threadEnd(ThreadPool threadPool, Thread thread);
}
