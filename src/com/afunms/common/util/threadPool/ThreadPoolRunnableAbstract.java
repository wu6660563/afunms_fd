package com.afunms.common.util.threadPool;



/**
 * 用于实现 {@link ThreadPoolRunnable} 接口
 *
 * @author 聂林
 * @version 1.0
 */
public abstract class ThreadPoolRunnableAbstract implements ThreadPoolRunnable {
    // XXX use notes or a hashtable-like
    // Important: ThreadData in JDK1.2 is
    // implemented as a Hashtable( Thread -> object ),
    // expensive.

    /**
     * 线程属性
     */
    protected ThreadPoolRunnableAttributes threadPoolRunnableAttributes;

    /**
     * 获取线程的属性
     * @return ThreadPoolRunnableAttributes
     *          线程的属性
     */
    public abstract ThreadPoolRunnableAttributes
                    getThreadPoolRunnableAttributes();

    /**
     * 设置线程的属性
     * @param threadPoolRunnableAttributes the
     *          threadPoolRunnableAttributes to set
     *          设置线程属性
     */
    public void setThreadPoolRunnableAttributes(
            ThreadPoolRunnableAttributes threadPoolRunnableAttributes) {
        this.threadPoolRunnableAttributes = threadPoolRunnableAttributes;
    }

    /**
     * 线程池会分配一个线程来执行这个方法。
     * 如果执行完成后，则会将线程归还给线程池。
     * 通过获取线程的属性方法来获取线程属性后，
     * 线程池将该属性作为参数来运行
     * {@link #runIt(ThreadPoolRunnableAttributes)} 方法
     * @param threadPoolRunnableAttributes
     *          通过获取线程的属性方法来获取的线程属性
     */
    public abstract void runIt(
        ThreadPoolRunnableAttributes threadPoolRunnableAttributes);

}
