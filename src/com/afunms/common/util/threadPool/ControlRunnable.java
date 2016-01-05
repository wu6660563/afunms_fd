package com.afunms.common.util.threadPool;

import com.afunms.common.util.logging.Level;
import com.afunms.common.util.logging.Log;
import com.afunms.common.util.logging.LogFactory;



/**
 * ���̳߳ؿ��ƺ�����ִ�и��ֲ���(�� {@link ThreadPoolRunnable} )
 * ��һ���̶߳���
 *
 * @author ����
 * @version 1.0 $Date: 2011-03-07 16:34:30 +0100 (Mon, Mar 7, 2011) $
 */
public class ControlRunnable implements Runnable {

    /**
     * ��־
     */
    private Log log = LogFactory.getLog(ControlRunnable.class);

    /**
     * �������߳���
     */
    protected ThreadPool threadPool;

    /**
     * �Ƿ��ж�
     */
    protected boolean shouldTerminate;

    /**
     * ���ڼ���ִ���߳�
     */
    protected boolean shouldRun;

    /**
     * ���߳�ִ�еķ�����
     */

    /**
     * 1. Runnable ��ʽ
     */
    protected Runnable toRunRunnable;

    /**
     * 2. ThreadPoolRunnable ����ʽ
     */
    protected ThreadPoolRunnable toRun;

    /**
     * ����ִ�е��߳�
     */
    protected Thread thread;

    /**
     * ���̵߳�����
     */
    protected ThreadPoolRunnableAttributes runnableAttributes;

    /**
     * �������̳߳��Ƿ�����־
     */
    protected boolean isLog;

    /**
     * ����
     */
    protected int sequence;

    /**
     * ���������̳߳��д���һ�����߳�
     * @param threadPool �����̳߳�
     */
    protected ControlRunnable(ThreadPool threadPool) {
        this.threadPool = threadPool;
        toRun = null;
        shouldTerminate = false;
        shouldRun = false;
        isLog = this.threadPool.isLog();
        thread = new Thread(this);
        thread.setDaemon(this.threadPool.isDaemon());
        sequence = this.threadPool.incSequence();
        thread.setName(this.threadPool.getName()
            + "-Processor-" + sequence);
        thread.setPriority(threadPool.getThreadPriority());
        this.threadPool.addThread(thread, this);
        thread.start();
    }

    /**
     * ʵ�� {@link Runnable} ����
     */
    public void run() {
        boolean shouldRunTemp = false;
        boolean shouldTerminateTemp = false;
        ThreadPoolRunnable toRunTemp = null;
        try {
            while (true) {
                try {
                    /* Wait for work. */
                    synchronized (this) {
                        while (!shouldRun && !shouldTerminate) {
                            this.wait();
                        }
                        shouldRunTemp = shouldRun;
                        shouldTerminateTemp = shouldTerminate;
                        toRunTemp = toRun;
                    }

                    if (shouldTerminateTemp) {
                        log(Level.DEBUG, "Terminate");
                        break;
                    }

                    /* ����Ƿ����ִ��һ���߳� */
                    try {
                        if (toRunTemp != null) {
                            runnableAttributes =
                                toRunTemp.getThreadPoolRunnableAttributes();
                            if (runnableAttributes != null) {
                                try {
                                    thread.setPriority(
                                            runnableAttributes.getPriority());
                                } catch (Exception e) {
                                    log(Level.ERROR, "���õ��̳߳����ȼ����ڣ�0~9֮��", e);
                                }
                                if (runnableAttributes.getThreadName()
                                        != null) {
                                    thread.setName(this.threadPool.getName()
                                            + "-Processor-"
                                            + runnableAttributes
                                            .getThreadName());
                                } else {
                                    thread.setName(this.threadPool.getName()
                                            + "-Processor-" + sequence);
                                }
                            } else {
                                thread.setPriority(
                                        threadPool.getThreadPriority());
                                thread.setName(this.threadPool.getName()
                                        + "-Processor-" + sequence);
                            }
                             // log(Level.DEBUG, "��ȡִ���߳�����");
                        }
                        if (shouldRunTemp) {
                            if (toRunTemp != null) {
                                toRunTemp.runIt(runnableAttributes);
                            } else if (toRunRunnable != null) {
                                toRunRunnable.run();
                            } else {
                                log(Level.WARN, "��ȡ����ִ���߳�Ϊ null ???");
                            }
                        }
                    } catch (Throwable t) {
                        log(Level.ERROR, "ִ���߳�ִ�й����г����쳣�����߳̽����ͷţ�����", t);
                        /*
                         *
                         * ������ runnable �׳��쳣 (����������һ�����߳�)����˵��
                         * ����߳��Ѿ�������
                         *
                         * Ҳ����ζ��������Ҫ���̳߳����ͷ�����߳�
                         *
                         */
                        shouldTerminateTemp = true;
                        shouldRunTemp = false;
                        threadPool.notifyThreadEnd(this);
                    } finally {
                        if (shouldRunTemp) {
                            shouldRun = false;
                            /*
                             * ֪ͨ�̳߳أ����߳����ڴ��ڿ���״̬
                             */
                            threadPool.returnController(this);
                        }
                    }

                    /*
                     * ����Ƿ���ֹ
                     */
                    if (shouldTerminateTemp) {
                        break;
                    }
                } catch (InterruptedException ie) {
                    /*
                     * Ϊ�˵ȴ�һֱ�ܹ�������ȥ�����ǲ�������жϣ�
                     * ����������ᷢ��
                     */
                    log(Level.ERROR, "δ֪�Ĵ���", ie);
                }
            }
        } finally {
            threadPool.removeThread(Thread.currentThread());
        }
    }

    /**
     * Run a task
     *
     * @param toRun ��Ҫִ�е�����
     */
    public synchronized void runIt(Runnable toRun) {
        this.toRunRunnable = toRun;
        // Do not re-init, the whole idea is to run init only once per
        // thread - the pool is supposed to run a single task, that is
        // initialized once.
        // noThData = true;
        shouldRun = true;
        this.notify();
    }

    /**
     * Run a task
     *
     * @param toRun ��Ҫִ�е�����
     */
    public synchronized void runIt(ThreadPoolRunnable toRun) {
        this.toRun = toRun;
        // Do not re-init, the whole idea is to run init only once per
        // thread - the pool is supposed to run a single task, that is
        // initialized once.
        // noThData = true;
        shouldRun = true;
        this.notify();
    }

    /**
     * ��ֹ���߳�
     */
    public synchronized void terminate() {
        shouldTerminate = true;
        this.notify();
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
