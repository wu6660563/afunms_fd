package com.afunms.common.util.threadPool;

/**
 * <p>�̳߳صļ�������</p>
 *
 * <p>���ڼ����̳߳ش����̺߳�ֹͣ�̡߳�
 * @author ����
 * @version 1.0 $Date: 2011-03-13 00:41:40 +0100 (Sun, Mar 13, 2011) $
 *
 */
public interface ThreadPoolListener {
    /**
     * Interface to allow applications to be notified when a threads are created
     * and stopped.
     */
    /**
     * ��Ҫʵ�ֵ��߳̿�ʼ�ķ���
     * @param threadPool �����̳߳�
     * @param thread �����߳�
     */
    void threadStart(ThreadPool threadPool, Thread thread);

    /**
     * ��Ҫʵ�ֵ��߳���ɵķ���
     * @param threadPool �����̳߳�
     * @param thread �����߳�
     */
    void threadEnd(ThreadPool threadPool, Thread thread);
}
