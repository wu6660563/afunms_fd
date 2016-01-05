package com.afunms.common.util.threadPool;

import java.util.Hashtable;

/**
 * 用于给指定的线程带上属性和注释。
 *
 * @author 聂林
 * @version 1.0 $Date: 2011-03-12 22:34:30 +0100 (Sat, Mar 12, 2011) $
 *
 */
public class ThreadPoolRunnableAttributes {

    /**
     * 线程的名称
     */
    private String threadName = null;

    /**
     * 线程的优先级，默认为 Thread.NORM_PRIORITY
     */
    private int priority = Thread.NORM_PRIORITY;


    /**
     * 用于存储线程与线程属性的 {@link Hashtable}
     */
    private Hashtable<Object, Object> attributes
        = new Hashtable<Object, Object>();


    /**
     * 设置一个属性
     * @param name 属性名称
     * @param object 属性值
     */
    public void setAttribute(String name, Object object) {
        if (name != null) {
            attributes.put(name, object);
        }
    }

    /**
     * 获取属性值
     * @param name 属性名称
     * @return 属性值
     */
    public Object getAttribute(String name) {
        if (name == null) {
            return null;
        }
        return attributes.get(name);
    }

    /**
     * 返回线程名称，如果需要对运行在线程池中的线程名称命名，
     * 可以使用该属性。
     *
     * @see #setThreadName(String)
     * @return the threadName
     *          线程名称
     */
    public String getThreadName() {
        return threadName;
    }

    /**
     * 对运行在线程池中的线程设置线程名称
     * @param threadName
     *          需要设置的线程名称
     */
    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    /**
     * 获取线程优先级
     * @return the priority 优先级
     */
    public int getPriority() {
        return priority;
    }


    /**
     * 设置线程优先级
     * @param priority the priority to set
     *          需要设置的线程优先级
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }
}
