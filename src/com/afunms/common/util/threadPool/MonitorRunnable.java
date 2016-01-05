package com.afunms.common.util.threadPool;

import com.afunms.common.util.logging.Level;
import com.afunms.common.util.logging.Log;
import com.afunms.common.util.logging.LogFactory;


/**
 * ���ڼ�ص��̣߳�����ִ�������̳߳صĶ�����
 *
 * @author <a href=nielin@dhcc.com.cn>����</a>
 * @version 1.0
 * $Date: 2011-03-07 18:59:45 PM +0100 (Mon, May 4, 2011) $
 */
public class MonitorRunnable implements Runnable {

    /**
     * ��־
     */
    private static Log log = LogFactory.getLog(MonitorRunnable.class);

    /**
     * �������߳���
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
     * ���ִ�еļ��
     */
    protected long interval;

    /**
     * ����
     */
    protected String name;

    /**
     * �������̳߳��Ƿ�����־
     */
    protected boolean isLog;

    /**
     * ���췽��
     * @param threadPool
     *          ��Ҫ��Ӽ���̵߳��̳߳�
     */
    public MonitorRunnable(ThreadPool threadPool) {
        this.setThreadPool(threadPool);
        isLog = getThreadPool().isLog();
        log(Level.DEBUG, "�����̳߳ؼ���߳�");
        this.start();
    }

    /**
     * ��ʼ���м���߳�
     */
    public void start() {
        this.isTerminate = false;
        this.thread = new Thread(this);
        this.thread.setDaemon(threadPool.isDaemon());
        setName(this.threadPool.getName() + "-Monitor");
        setInterval(threadPool.getWorkWaitTimeout());
        this.thread.start();
    }

    /**
     * ʵ�� {@link Runnable} �ӿ�
     */
    public void run() {
        while (true) {
            try {
                // �ȴ�ʱ���� ����˯��
                synchronized (this) {
                    this.wait(interval);
                }

                if (isTerminate) {
                    break;
                }
                log(Level.INFO, "�̳߳أ�" + threadPool.getName()
                                + " �ڼ��ͻ��տ����߳�֮ǰ����ǰ���У�"
                                + threadPool.getThreadPoolBuffer().size()
                                + "��������δִ��;");
                log(Level.INFO, "�̳߳أ�" + threadPool.getName()
                        + " �ڼ��ͻ��տ����߳�֮ǰ����ǰ�߳�����CTC���ǣ�"
                        + threadPool.getCurrentThreadCount()
                        + "����ǰ��æ�߳�����BTC���ǣ�"
                        + threadPool.getCurrentThreadsBusy());
                // ���ͻ��տ����߳�
                this.threadPool.checkSpareControllers();
                log(Level.INFO, "�̳߳أ�" + threadPool.getName()
                        + " �ڼ��ͻ��տ����߳�֮�󣬵�ǰ�߳�����CTC���ǣ�"
                        + threadPool.getCurrentThreadCount()
                        + "����ǰ��æ�߳�����BTC���ǣ�"
                        + threadPool.getCurrentThreadsBusy());
            } catch (InterruptedException e) {
                log(Level.ERROR, "�̳߳ؼ���߳�:" + getName()
                        +  " �����쳣������ Unexpected exception", e);
            }
        }
    }

    /**
     * ֹͣ����̣߳��� <code>terminate()</code> Ч��һ��
     * @see #terminate()
     */
    public void stop() {
        this.terminate();
    }

    /**
     * �жϼ���߳�
     */
    public synchronized void terminate() {
        this.isTerminate = true;
        this.notify();
    }

    /**
     * �����������̳߳�
     * @return the threadPool
     */
    public ThreadPool getThreadPool() {
        return threadPool;
    }

    /**
     * ���������̳߳�
     * @param threadPool the threadPool to set
     */
    public void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    /**
     * ��ȡ����߳���ѯ���
     * @return the interval ��ѯ���
     */
    public long getInterval() {
        return interval;
    }

    /**
     * ���ü���߳���ѯ���
     * @param interval the interval to set ��Ҫ���õ���ѯ���
     */
    public void setInterval(long interval) {
        this.interval = interval;
    }

    /**
     * �����߳�����
     * @return the name �߳�����
     */
    public String getName() {
        name = thread.getName();
        return name;
    }

    /**
     * �����߳�����
     * @param name the name to set ��Ҫ���õ��߳�����
     */
    public void setName(String name) {
        this.name = name;
        this.thread.setName(name);
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
