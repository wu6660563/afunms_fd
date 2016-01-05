package com.afunms.common.util.threadPool;



/**
 * ����ʵ�� {@link ThreadPoolRunnable} �ӿ�
 *
 * @author ����
 * @version 1.0
 */
public abstract class ThreadPoolRunnableAbstract implements ThreadPoolRunnable {
    // XXX use notes or a hashtable-like
    // Important: ThreadData in JDK1.2 is
    // implemented as a Hashtable( Thread -> object ),
    // expensive.

    /**
     * �߳�����
     */
    protected ThreadPoolRunnableAttributes threadPoolRunnableAttributes;

    /**
     * ��ȡ�̵߳�����
     * @return ThreadPoolRunnableAttributes
     *          �̵߳�����
     */
    public abstract ThreadPoolRunnableAttributes
                    getThreadPoolRunnableAttributes();

    /**
     * �����̵߳�����
     * @param threadPoolRunnableAttributes the
     *          threadPoolRunnableAttributes to set
     *          �����߳�����
     */
    public void setThreadPoolRunnableAttributes(
            ThreadPoolRunnableAttributes threadPoolRunnableAttributes) {
        this.threadPoolRunnableAttributes = threadPoolRunnableAttributes;
    }

    /**
     * �̳߳ػ����һ���߳���ִ�����������
     * ���ִ����ɺ���Ὣ�̹߳黹���̳߳ء�
     * ͨ����ȡ�̵߳����Է�������ȡ�߳����Ժ�
     * �̳߳ؽ���������Ϊ����������
     * {@link #runIt(ThreadPoolRunnableAttributes)} ����
     * @param threadPoolRunnableAttributes
     *          ͨ����ȡ�̵߳����Է�������ȡ���߳�����
     */
    public abstract void runIt(
        ThreadPoolRunnableAttributes threadPoolRunnableAttributes);

}
