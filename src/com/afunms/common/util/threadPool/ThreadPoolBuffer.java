package com.afunms.common.util.threadPool;

import java.util.Vector;

import com.afunms.common.util.logging.Log;
import com.afunms.common.util.logging.LogFactory;


/**
 * <p>
 * �̳߳ػ����������̳߳ؼ�����Ҫִ�е��߳�����Ҳ���ǽ�������뵽�������У�
 * Ȼ���̳߳��п����߳�ʱ���ʹӻ������л�ȡһ����Ҫִ�е��߳�����
 * </p>
 * @author ����
 * @version 1.0 $Date: 2011-03-07 17:23:28 +0100 (Mon, May 4, 2011) $
 */
public class ThreadPoolBuffer {

	/**
     * ��־
     */
    private static Log log = LogFactory.getLog(ThreadPoolBuffer.class);

    /**
     * ���ڴ洢���̻߳�����
     */
    private Vector<ThreadPoolRunnable> threadPoolRunnableBuffer = null;

    /**
     * �̳߳ػ����������̳߳�
     */
    private ThreadPool threadPool = null;

    /**
     * ���췽��
     * @param threadPool �̳߳ػ����������̳߳�
     */
    public ThreadPoolBuffer(ThreadPool threadPool) {
        this.threadPool = threadPool;
        threadPoolRunnableBuffer = new Vector<ThreadPoolRunnable>();
    }

    /**
     * �򻺳����м���һ����Ҫִ�е��߳�����
     * @param threadPoolRunnable ��Ҫִ�е��߳�����
     */
    public void addThreadPoolRunnable(ThreadPoolRunnable threadPoolRunnable) {
        this.threadPoolRunnableBuffer.add(threadPoolRunnable);
    }

    /**
     * �ӻ������е���һ����һ����Ҫִ�е��߳�����
     * @return �������е�һ�����߳�����
     */
    public synchronized ThreadPoolRunnable popThreadPoolRunnable() {
        ThreadPoolRunnable threadPoolRunnable = null;
        if (!isEmpty()) {
            threadPoolRunnable = this.threadPoolRunnableBuffer.remove(0);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("��ǰ������Ϊ��");
            }
        }
        return threadPoolRunnable;
    }

    /**
     * �жϵ�ǰ�������Ƿ�Ϊ��
     * @return ��ǰ������Ϊ����Ϊ true ; ����Ϊ false
     */
    public synchronized boolean isEmpty() {
       return this.threadPoolRunnableBuffer.isEmpty();
    }

    /**
     * ���������̳߳�
     * @return the threadPool �����̳߳�
     */
    public ThreadPool getThreadPool() {
        return threadPool;
    }

    /**
     * ���������̳߳�
     * @param threadPool ��Ҫ���õ��̳߳�
     */
    public void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    /**
     * ��ȡ��������ǰ��Ҫִ�е�������
     * @return ����������Ҫִ�е�������
     */
    public int size() {
        return this.threadPoolRunnableBuffer.size();
    }

}
