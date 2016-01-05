package com.afunms.common.util.threadPool;

import com.afunms.common.util.logging.Level;
import com.afunms.common.util.logging.Log;
import com.afunms.common.util.logging.LogFactory;


/**
 * <p>
 * ���ڴ����̳߳ػ���������Ҫִ�еĲ�����������Ҫ����Ĳ����ȼ��뵽
 * �������У�Ȼ���ɸ��߳�ÿ�ν��������Ĳ������뵽�̳߳������С�
 * </p>
 * @author ����
 */
public class BufferRunnable implements Runnable {

    /**
     * ��־
     */
    private static Log log = LogFactory.getLog(BufferRunnable.class);

    /**
     * �������̳߳�
     */
    protected ThreadPool threadPool;

    /**
     * �����߳�
     */
    protected Thread thread;

    /**
     * �Ƿ��ж�
     */
    protected boolean isTerminate;

    /**
     * ����
     */
    protected String name;

    /**
     * �������̳߳��Ƿ�����־
     */
    protected boolean isLog;

    /**
     * <code>ThreadPoolBuffer</code> ������
     */
    protected ThreadPoolBuffer threadPoolBuffer = null;

    /**
     * ��ǰִ�е��߳� <code>ThreadPoolRunnable</code>
     */
    protected ThreadPoolRunnable threadPoolRunnable = null;

    /**
     * ���췽��
     * @param threadPool
     *          ��Ҫ��ӻ����̵߳��̳߳�
     */
    public BufferRunnable(ThreadPool threadPool) {
        this.setThreadPool(threadPool);
        this.start();
    }

    /**
     * ��ʼ���л������߳�
     */
    public void start() {
        this.isTerminate = false;
        this.thread = new Thread(this);
        this.thread.setDaemon(threadPool.isDaemon());
        setName(this.threadPool.getName() + "-Buffer");
        isLog = this.threadPool.isLog();
        setThreadPoolBuffer(threadPool.getThreadPoolBuffer());
        log(Level.DEBUG, "�����̳߳ػ������߳�");
        this.thread.start();
    }

    /**
     * �÷���Ϊʵ�� Runnable �ӿ� run() ����
     */
    public void run() {
        while (true) {
            try {
                // �����������û���κ���Ҫִ�е� ThreadPoolRunnable.
                synchronized (this) {
                    while (threadPoolBuffer == null
                        || threadPoolBuffer.size() == 0) {
                        this.wait();
                        if (isTerminate) {
                            threadPoolBuffer = null;
                            break;
                        }
                    }
                    if (isTerminate) {
                        break;
                    }
                    threadPoolRunnable =
                        threadPoolBuffer.popThreadPoolRunnable();
                }
//                if (log.isDebugEnabled()) {
//                    log.debug("�ӻ������л�ȡһ����Ҫִ�е� ThreadPoolRunnable");
//                }
                // ִ��
                this.threadPool.runIt(threadPoolRunnable);
            } catch (InterruptedException e) {
                log(Level.ERROR, "�̳߳ػ����߳�:" + getName()
                    +  " �����쳣���� Unexpected exception", e);
            }
        }
    }

    /**
     * ֹͣ����̣߳��� <code>terminate()</code> Ч����ͬ
     * @see #terminate()
     */
    public void stop() {
        this.terminate();
    }

    /**
     * ִ�л����߳�
     */
    public synchronized void runIt() {
        this.isTerminate = false;
        this.notify();
    }

    /**
     * �жϻ����߳�
     */
    public synchronized void terminate() {
        this.isTerminate = true;
        this.notify();
    }

    /**
     * <p>��ȡ�̳߳�
     * @return the threadPool
     */
    public ThreadPool getThreadPool() {
        return threadPool;
    }

    /**
     * ָ���̳߳�
     * @param threadPool the threadPool to set
     */
    public void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    /**
     * �����߳�����
     * @return the name
     */
    public String getName() {
        name = thread.getName();
        return name;
    }

    /**
     * ָ���߳�����
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
        this.thread.setName(name);
    }

    /**
     * �����̳߳صĻ�����
     * @return the threadPoolBuffer
     *          �����̳߳��еĻ�����
     */
    protected ThreadPoolBuffer getThreadPoolBuffer() {
        return threadPoolBuffer;
    }

    /**
     * �����̳߳صĻ�����
     * @param threadPoolBuffer the threadPoolBuffer to set
     *          ��Ҫ���õ��̳߳ػ�����
     */
    protected void setThreadPoolBuffer(ThreadPoolBuffer threadPoolBuffer) {
        this.threadPoolBuffer = threadPoolBuffer;
    }

    /**
     * ��¼��־
     * @param level
     *          ��־�ȼ�
     * @param message
     *          ��־��Ϣ����
     */
    private void log(Level level, Object message) {
        if (isLog && log.isEnabledFor(level)) {
            log.log(level, message);
        }
    }

    /**
     * ��¼��־
     * @param level
     *          ��־�ȼ�
     * @param message
     *          ��־��Ϣ����
     * @param t
     *          ��־�쳣���������ջ����
     */
    private void log(Level level, Object message, Throwable t) {
        if (isLog && log.isEnabledFor(level)) {
            log.log(level, message, t);
        }
    }

}
