package com.afunms.common.util.threadPool;


/**
 * ����뽫������뵽�̳߳������У������ʵ�ָýӿڡ�
 *
 * @author ����
 * @version 1.0
 */
public interface ThreadPoolRunnable {
    // XXX use notes or a hashtable-like
    // Important: ThreadData in JDK1.2
    // is implemented as a Hashtable( Thread -> object ),
    // expensive.

    /**
     * ��ȡ�̵߳�����
     * @return ThreadPoolRunnableAttributes
     *          �̵߳�����
     */
    ThreadPoolRunnableAttributes getThreadPoolRunnableAttributes();

    /**
     * �̳߳ػ����һ���߳���ִ�����������
     * ���ִ����ɺ���Ὣ�̹߳黹���̳߳ء�
     * ͨ����ȡ�̵߳����Է�������ȡ�߳����Ժ�
     * �̳߳ؽ���������Ϊ����������
     * {@link #runIt(ThreadPoolRunnableAttributes)} ����
     * @param threadPoolRunnableAttributes
     *          ͨ����ȡ�̵߳����Է�������ȡ���߳�����
     */
    void runIt(ThreadPoolRunnableAttributes threadPoolRunnableAttributes);

}
