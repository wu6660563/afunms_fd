package com.afunms.common.util.threadPool;

import java.util.Hashtable;

/**
 * ���ڸ�ָ�����̴߳������Ժ�ע�͡�
 *
 * @author ����
 * @version 1.0 $Date: 2011-03-12 22:34:30 +0100 (Sat, Mar 12, 2011) $
 *
 */
public class ThreadPoolRunnableAttributes {

    /**
     * �̵߳�����
     */
    private String threadName = null;

    /**
     * �̵߳����ȼ���Ĭ��Ϊ Thread.NORM_PRIORITY
     */
    private int priority = Thread.NORM_PRIORITY;


    /**
     * ���ڴ洢�߳����߳����Ե� {@link Hashtable}
     */
    private Hashtable<Object, Object> attributes
        = new Hashtable<Object, Object>();


    /**
     * ����һ������
     * @param name ��������
     * @param object ����ֵ
     */
    public void setAttribute(String name, Object object) {
        if (name != null) {
            attributes.put(name, object);
        }
    }

    /**
     * ��ȡ����ֵ
     * @param name ��������
     * @return ����ֵ
     */
    public Object getAttribute(String name) {
        if (name == null) {
            return null;
        }
        return attributes.get(name);
    }

    /**
     * �����߳����ƣ������Ҫ���������̳߳��е��߳�����������
     * ����ʹ�ø����ԡ�
     *
     * @see #setThreadName(String)
     * @return the threadName
     *          �߳�����
     */
    public String getThreadName() {
        return threadName;
    }

    /**
     * ���������̳߳��е��߳������߳�����
     * @param threadName
     *          ��Ҫ���õ��߳�����
     */
    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    /**
     * ��ȡ�߳����ȼ�
     * @return the priority ���ȼ�
     */
    public int getPriority() {
        return priority;
    }


    /**
     * �����߳����ȼ�
     * @param priority the priority to set
     *          ��Ҫ���õ��߳����ȼ�
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }
}
