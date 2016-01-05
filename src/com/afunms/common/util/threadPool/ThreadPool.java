/*
 *  ���̳߳�Ϊ�ɼ����ṩ�ɼ��̵߳��̳߳أ�����������뷨���ɼ��Ĳɼ��̹߳���һ���̳߳ء�
 *  ���ɼ���������ʱ���ɼ��̼߳��뵽�̳߳��С�
 *  ������ӻ��޸ģ����ڴ�ע����
 */

package com.afunms.common.util.threadPool;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Pattern;

import com.afunms.common.util.logging.Level;
import com.afunms.common.util.logging.Log;
import com.afunms.common.util.logging.LogFactory;



/**
 *
 * ģ�� Tomcat ���̳߳�
 *
 * @author <a href=nielin@dhcc.com.cn>����</a>
 * @version 1.0
 * $Date: 2011-03-07 18:59:45 PM +0100 (Mon, May 4, 2011) $
 */
public class ThreadPool {

    /**
     *  ��־
     */
    private static Log log = LogFactory.getLog(ThreadPool.class);

    /**
     *  Ĭ�ϵ��Ƿ��¼�̳߳ص���־
     */
    public static final boolean IS_LOG = true;

    /**
     *  Ĭ�ϵ�����߳���  Ĭ��ֵ
     */
    public static final int MAX_THREADS = 200;

    /**
     *  Ĭ�ϵ���С�߳���  Ĭ��ֵ
     */
    public static final int MAX_THREADS_MIN = 10;

    /**
     *  Ĭ�ϵ����Ŀ����߳���  Ĭ��ֵ
     */
    public static final int MAX_SPARE_THREADS = 50;

    /**
     *  Ĭ�ϵ���С�Ŀ����߳���  Ĭ��ֵ
     */
    public static final int MIN_SPARE_THREADS = 5;

    /**
     *  Ĭ�ϵ�������ȴ�ʱ��(�����տ����̼߳��ʱ��)  Ĭ��ֵ
     */
    public static final long WORK_WAIT_TIMEOUT = 60 * 1000;

    /**
     *  Ĭ�ϵ��̳߳ص��߳����ȼ�  Ĭ��ֵ
     */
    public static final int THREAD_PRIORITIY = Thread.NORM_PRIORITY;

    /**
     *  Ĭ�ϵ��̳߳ص��߳����ȼ�  Ĭ��ֵ
     */
    public static final String THREAD_NAME = "TP";

    /**
     *  �̳߳ص�����߳���
     */
    protected int maxThreads;

    /**
     *  �̳߳���С�����߳���
     */
    protected int minSpareThreads;

    /**
     *  �̳߳��������߳���
     */
    protected int maxSpareThreads;

    /**
     *  ��ǰ�̳߳ص��е��߳���
     */
    protected int currentThreadCount;

    /**
     *  ��ǰ�������߳���
     */
    protected int currentThreadsBusy;

    /**
     *  �Ƿ���ͣ�̳߳�(����ͣ�̳߳ص��е������߳���)
     */
    protected boolean stopThePool;

    /**
     *  ���ڱ�����߳��Ƿ�Ϊ�ػ��߳� 'daemon'
     */
    protected boolean isDaemon = false;

    /**
     * �Ƿ��¼�̳߳ص���־
     */
    protected boolean isLog = true;

    /**
     *  �̳߳�����
     */
    protected String name;

    /**
     *  �߳�ֵ������
     */
    protected Properties properties = null;

    /**
     *  ����ִ���߳�����
     */
    protected ControlRunnable[] pool = null;

    /**
     * ���ڼ�غ�������߳�
     */
    protected MonitorRunnable monitorRunnable = null;

    /**
     * ���ڽ��������� <code>ThreadPoolRunnable</code>
     * ���뵽�̳߳�������
     */
    protected BufferRunnable bufferRunnable = null;

    /**
     * <code>ThreadPoolRunnable</code> ������
     */
    protected ThreadPoolBuffer threadPoolBuffer = new ThreadPoolBuffer(this);

    /**
     * <p>���ڴ�Ŵ򿪵��̡߳�</p>
     * <p>���ǣ�<code>Thread</code> ��ֵ�ǣ�<code>ControlRunnable</code></p>
     */
    protected Hashtable<Thread, ControlRunnable> threads
        = new Hashtable<Thread, ControlRunnable>();

    /**
     * �̳߳ؼ�������
     */
    protected Vector<ThreadPoolListener> listeners
        = new Vector<ThreadPoolListener>();

    /**
     * ����
     */
    protected int sequence = 1;

    /**
     * �߳����ȼ�
     */
    protected int threadPriority;

    /**
     * �����ȴ��ĳ�ʱʱ�䣬���������̻߳��յĵȴ�ʱ��
     */
    protected long workWaitTimeout;

    /**
     * ʹ��Ĭ�ϵĲ���������һ���̳߳�
     */
    public ThreadPool() {
        initialize();
    }

    /**
     * ʹ��ָ���ĳ�ʼ������������һ���̳߳�
     * @param properties ��Ҫָ��������
     */
    public ThreadPool(Properties properties) {
        setProperties(properties);
        initialize();
    }

    /**
     * <p>����һ���̳߳�ʵ����</p>
     * <p>����� <code>jmx</code> ��δʹ�ã��÷�����ͬ�ڷ���
     * һ��Ĭ�����Ե��̳߳ء�</p>
     * @param jmx
     *            ��δʹ��(UNUSED)
     * @return �̳߳�<code>ThreadPool</code> ��ʵ��.
     *         If JMX support is requested, you need to
     *         call register() in order to set a name.
     */
    public static ThreadPool createThreadPool(boolean jmx) {
        return new ThreadPool();
    }

    /**
     * �̳߳س�ʼ��
     */
    protected void initialize() {
        isLog = true;
        name = THREAD_NAME;
        maxThreads = MAX_THREADS;
        maxSpareThreads = MAX_SPARE_THREADS;
        minSpareThreads = MIN_SPARE_THREADS;
        threadPriority = THREAD_PRIORITIY;
        workWaitTimeout = WORK_WAIT_TIMEOUT;
        currentThreadCount = 0;
        currentThreadsBusy = 0;
        stopThePool = false;

        if (properties != null) {
            String nameProperty =
                properties.getProperty(ThreadPoolProperitesConstant.NAME);
            if (nameProperty != null && nameProperty.trim().length() > 0) {
                name = nameProperty.trim();
            } else {
                log(Level.DEBUG , "�̳߳�����δ���û��������ó���ʹ��Ĭ�����ã�" + name);
            }

            Pattern pattern = Pattern.compile("[0-9]+");

            String maxThreadsProperty = properties.getProperty(
                ThreadPoolProperitesConstant.MAX_THREADS);
            if (maxThreadsProperty != null
                && pattern.matcher(maxThreadsProperty).matches()) {
                if (Integer.valueOf(maxThreadsProperty) >= MAX_THREADS_MIN) {
                    maxThreads = Integer.valueOf(maxThreadsProperty);
                } else {
                    log(Level.DEBUG, "�̳߳�����߳�������С��:"
                            + maxThreads + "��ʹ��Ĭ�����ã�" + maxThreads);
                }
            } else {
                log(Level.DEBUG, "�̳߳�����߳���û������"
                        + "���������ó���ʹ��Ĭ�����ã�" + maxThreads);
            }

            String maxSpareThreadsProperty = properties.getProperty(
                    ThreadPoolProperitesConstant.MAX_SPARE_THREADS);
            if (maxSpareThreadsProperty != null
                && pattern.matcher(maxSpareThreadsProperty).matches()) {
                maxSpareThreads = Integer.valueOf(maxSpareThreadsProperty);
            } else {
                log(Level.DEBUG, "�̳߳���������û������"
                        + "���������ó���ʹ��Ĭ�����ã�" + maxSpareThreads);
            }

            String minSpareThreadsProperty = properties.getProperty(
                ThreadPoolProperitesConstant.MIN_SPARE_THREADS);
            if (minSpareThreadsProperty != null
                && pattern.matcher(minSpareThreadsProperty).matches()) {
                minSpareThreads = Integer.valueOf(minSpareThreadsProperty);
            } else {
                log(Level.DEBUG, "�̳߳���С������û������"
                        + "���������ó���ʹ��Ĭ�����ã�" + minSpareThreads);
            }

            String threadPriorityProperty = properties.getProperty(
                ThreadPoolProperitesConstant.THREAD_PRIORITIY);
            if (threadPriorityProperty != null
                && pattern.matcher(threadPriorityProperty).matches()) {
                if (Integer.valueOf(threadPriorityProperty)
                        >= Thread.MIN_PRIORITY
                    && Integer.valueOf(threadPriorityProperty)
                    <= Thread.MAX_PRIORITY) {
                    threadPriority = Integer.valueOf(threadPriorityProperty);
                } else {
                    log(Level.DEBUG, "�̳߳����ȼ��趨����1~10��Χ�ڣ�"
                            + "ʹ��Ĭ�����ã�" + threadPriority);
                }
            }

            String workWaitTimeoutProperty = properties.getProperty(
                ThreadPoolProperitesConstant.WORK_WAIT_TIMEOUT);
            if (workWaitTimeoutProperty != null
                && pattern.matcher(workWaitTimeoutProperty).matches()) {
                workWaitTimeout = Long.valueOf(workWaitTimeoutProperty);
            } else {
                log(Level.DEBUG, "�̳߳����̵߳Ĺ����ȴ�ʱ��"
                        + "�����������̱߳����յ�ʱ������û�����û��������ó���"
                        + "ʹ��Ĭ�����ã���λ���룩��" + workWaitTimeout);
            }

            String isLogProperty =
                properties.getProperty(ThreadPoolProperitesConstant.IS_LOG);
            if (isLogProperty != null && isLogProperty.trim().length() > 0) {
                try {
                    isLog = Boolean.valueOf(isLogProperty.trim());
                } catch (Exception e) {
                    isLog = IS_LOG;
                    log(Level.DEBUG, "�̳߳����̵߳Ĺ����ȴ�ʱ��"
                            + "�����������̱߳����յ�ʱ������û�����û��������ó���"
                            + "ʹ��Ĭ�����ã���λ���룩��" + isLog, e);
                }
            } else {
                isLog = IS_LOG;
                log(Level.DEBUG, "�̳߳����̵߳Ĺ����ȴ�ʱ��"
                        + "�����������̱߳����յ�ʱ������û�����û��������ó���"
                        + "ʹ��Ĭ�����ã���λ���룩��" + isLog);
            }
        } else {
            log(Level.DEBUG, "�̳߳����Լ�Ϊ�գ�ʹ��Ĭ�����ó�ʼ��");
            properties = new Properties();
            isLog = IS_LOG;
            properties.setProperty(ThreadPoolProperitesConstant.NAME, name);
            properties.setProperty(
                    ThreadPoolProperitesConstant.MAX_THREADS,
                    String.valueOf(maxThreads));
            properties.setProperty(
                    ThreadPoolProperitesConstant.MAX_SPARE_THREADS,
                    String.valueOf(maxSpareThreads));
            properties.setProperty(
                    ThreadPoolProperitesConstant.MIN_SPARE_THREADS,
                    String.valueOf(minSpareThreads));
            properties.setProperty(
                    ThreadPoolProperitesConstant.THREAD_PRIORITIY,
                    String.valueOf(threadPriority));
            properties.setProperty(
                    ThreadPoolProperitesConstant.WORK_WAIT_TIMEOUT,
                    String.valueOf(workWaitTimeout));
            properties.setProperty(
                    ThreadPoolProperitesConstant.IS_LOG,
                    String.valueOf(isLog));
            properties.setProperty(
                    ThreadPoolProperitesConstant.WORK_WAIT_TIMEOUT,
                    String.valueOf(workWaitTimeout));

        }

        log.info("��ǰ�̳߳���������Ϊ��" + name);
        log.info("��ǰ�̳߳�����߳�������Ϊ��" + maxThreads);
        log.info("��ǰ�̳߳��������߳�������Ϊ��" + maxSpareThreads);
        log.info("��ǰ�̳߳���С�����߳�������Ϊ��" + minSpareThreads);
        log.info("��ǰ�̳߳��߳����ȼ�����Ϊ��" + threadPriority);
        log.info("��ǰ�̳߳��̵߳Ĺ����ȴ�ʱ�䣨���������̱߳����յ�ʱ����������Ϊ��" + workWaitTimeout);
        log.info("��ǰ�̳߳ص���־����Ϊ��" + isLog);
        if (!isLog) {
            log.info("���ٴ�ӡ�̳߳أ�" + name + " ��־!!!");
        }
    }

    /**
     * <p>�����̳߳أ������������̳߳��Ƿ�ɹ�</p>
     * @return ���������̳߳��Ƿ�ɹ�
     */
    public synchronized boolean start() {
        stopThePool = false;
        currentThreadCount = 0;
        currentThreadsBusy = 0;

        adjustLimits();

        pool = new ControlRunnable[maxThreads];

        openThreads(minSpareThreads);
        if (maxSpareThreads < maxThreads) {
            monitorRunnable = new MonitorRunnable(this);
            bufferRunnable = new BufferRunnable(this);
        }

        return false;
    }

    /**
     * ����δ�������߳�
     *
     * @param toOpen
     *            ��Ҫ�򿪵��߳���
     */
    protected void openThreads(int toOpen) {

        if (toOpen > maxThreads) {
            toOpen = maxThreads;
        }

        for (int i = currentThreadCount; i < toOpen; i++) {
            pool[i - currentThreadsBusy] = new ControlRunnable(this);
        }

        currentThreadCount = toOpen;
    }

    /**
     * �ɼ���߳������ͻ��տ����߳�
     */
    protected synchronized void checkSpareControllers() {

        if (stopThePool) {
            return;
        }

        if ((currentThreadCount - currentThreadsBusy) > maxSpareThreads) {
            int toFree = currentThreadCount - currentThreadsBusy
                - maxSpareThreads;

            for (int i = 0; i < toFree; i++) {
                ControlRunnable controlRunnable = pool[currentThreadCount
                    - currentThreadsBusy - 1];
                controlRunnable.terminate();
                pool[currentThreadCount - currentThreadsBusy - 1] = null;
                currentThreadCount--;
            }
        }
    }

    /**
     * ����������⣬����������޸������ṩ��Ĭ�ϵ�����
     */
    protected void adjustLimits() {
        if (maxThreads <= 0) {
            maxThreads = MAX_THREADS;
        } else if (maxThreads < MAX_THREADS_MIN) {
            maxThreads = MAX_THREADS_MIN;
            log.warn("�̳߳�����߳�������С��:"
                + maxThreads + "����ǰ���ã�" + maxThreads);
        }

        if (maxSpareThreads >= maxThreads) {
            maxSpareThreads = maxThreads;
            log.warn("�̳߳��������߳���"
                + "���ܴ�������߳�������ǰ���ã�" + maxSpareThreads);
        }

        if (maxSpareThreads <= 0) {
            if (1 == maxThreads) {
                maxSpareThreads = 1;
            } else {
                maxSpareThreads = maxThreads / 2;
            }
            log.warn("�̳߳��������߳�������С��0����ǰ���ã�" + maxSpareThreads);
        }

        if (minSpareThreads > maxSpareThreads) {
            minSpareThreads = maxSpareThreads;
            log.warn("�̳߳���С�����߳���"
                + "���ܴ����������߳�������ǰ���ã�" + minSpareThreads);
        }

        if (minSpareThreads <= 0) {
            if (1 == maxSpareThreads) {
                minSpareThreads = 1;
            } else {
                minSpareThreads = maxSpareThreads / 2;
            }
            log.warn("�̳߳���С�����߳�������С��0����ǰ���ã�" + maxSpareThreads);
        }
    }

    /**
     * ֹͣ�̳߳�
     */
    public synchronized void shutdown() {
        if (!stopThePool) {
            stopThePool = true;
            if (monitorRunnable != null) {
                // ֹͣ����߳�
                monitorRunnable.terminate();
                monitorRunnable = null;
            }
            if (bufferRunnable != null) {
                // ֹͣ�������߳�
                bufferRunnable.terminate();
                bufferRunnable = null;
            }
            for (int i = 0; i < currentThreadCount - currentThreadsBusy; i++) {
                try {
                    pool[i].terminate();
                } catch (Throwable t) {
                    // �����κ���...�������У�ֹͣ�̳߳أ�������ֹͣ����������
                    log.error("Ignored exception while "
                        + "shutting down thread pool",
                                t);
                }
            }
            currentThreadsBusy = 0;
            currentThreadCount = 0;
            pool = null;
            notifyAll();
        }
    }

    /**
     * ���̳߳صĻ����������һ����Ҫ���е� <code>ThreadPoolRunnable</code> ,
     * һ�����뵽�������У�����̳߳��Ѿ������������뵽�̳߳����Զ����С�
     * ����̳߳ص������̶߳���æ�����һֱ�ȴ�һ�������߳������С�
     * @param r
     *          ��Ҫ���е� ThreadPoolRunnable
     */
    public void add(ThreadPoolRunnable r) {
        if (null == r) {
            throw new NullPointerException();
        }

        threadPoolBuffer.addThreadPoolRunnable(r);

        bufferRunnable.runIt();
    }

    /**
     * ���и����� ThreadPoolRunnable
     * @param r
     *          ������ ThreadPoolRunnable
     */
    public void runIt(ThreadPoolRunnable r) {
        if (null == r) {
            throw new NullPointerException();
        }

        ControlRunnable c = findControlRunnable();
        c.runIt(r);
    }

    /**
     * ����һ�����õĿ����߳�
     * @return ����һ�����õĿ����߳�
     */
    private ControlRunnable findControlRunnable() {
        ControlRunnable c = null;

        if (stopThePool) {
            throw new IllegalStateException();
        }

        // ���̳߳��л�ȡһ�����߳�
        synchronized (this) {

            while (currentThreadsBusy == currentThreadCount) {
                // �����̶߳���æ
                if (currentThreadCount < maxThreads) {
                    // ������е��̶߳��Ǵ򿪵ģ����һ���µĿ��߳��̣߳����߳���Ϊ��С������
                    int toOpen = currentThreadCount + minSpareThreads;
                    openThreads(toOpen);
                } else {
                    log(Level.DEBUG, "�ȴ��̳߳ؿ����̣߳���ǰ�̣߳�" + currentThreadCount
                            + " ����߳�����" + maxThreads);
                    // �ȴ��̵߳������
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        // ������������ڲ��� Throwable;
                        // ��Ϊ�����ܻ��� wait() �׳������쳣;
                        // ���������ֻ�����ڲ���һ���쳣; ��Ϊû�����κεط�ʹ���߳��жϣ�����
                        // ����쳣��Զ���ᷢ����
                        log(Level.ERROR,
                                "�ȴ������̳߳����쳣������Unexpected exception",
                                e);
                    }
                    log(Level.DEBUG, "��ɵȴ������߳�: CTC=" + currentThreadCount
                            + ", CTB=" + currentThreadsBusy);
                    // ���ֹͣ�̳߳أ����˳�
                    if (stopThePool) {
                        break;
                    }
                }
            }
            // �̳߳��Ѿ�ֹͣ���׳��쳣
            if (0 == currentThreadCount || stopThePool) {
                throw new IllegalStateException();
            }

            // ������������˵����һ�������̣߳�����ȥʹ�á�
            int pos = currentThreadCount - currentThreadsBusy - 1;
            c = pool[pos];
            pool[pos] = null;
            currentThreadsBusy++;
        }
        return c;
    }

    /**
     *
     * ֪ͨ�̳߳أ�ָ�����߳��Ѿ���ɡ�
     *
     * �� {@link ControlRunnable}  ִ�����׳��쳣ʱ�����á�
     *
     * @param c �߳�
     *
     */
    protected synchronized void notifyThreadEnd(ControlRunnable c) {
        currentThreadsBusy--;
        currentThreadCount--;
        notify();
    }

    /**
     * ���̷߳��ص��̳߳��С�
     * ��������̷߳���ɿ����̣߳������̳߳���������á�
     *
     * @param c �߳�
     */
    protected synchronized void returnController(ControlRunnable c) {

        if (0 == currentThreadCount || stopThePool) {
            c.terminate();
            return;
        }

        // atomic
        currentThreadsBusy--;

        pool[currentThreadCount - currentThreadsBusy - 1] = c;
        notify();
    }

    /**
     * ���߳������һ���߳�
     * @param thread ��Ҫ��ӵ��߳�
     * @param cr     �߳������Ŀ����߳�
     */
    public void addThread(Thread thread, ControlRunnable cr) {
//      threads.put(t, cr);
        for (int i = 0; i < listeners.size(); i++) {
            ThreadPoolListener threadPoolListener =
                (ThreadPoolListener) listeners.elementAt(i);
            threadPoolListener.threadEnd(this, thread);
        }
    }

    /**
     * �Ƴ�ָ�����߳�
     * @param thread
     *          ��Ҫ�Ƴ����߳�
     */
    public void removeThread(Thread thread) {
//        threads.remove(thread);
        for (int i = 0; i < listeners.size(); i++) {
            ThreadPoolListener threadPoolListener =
                (ThreadPoolListener) listeners.elementAt(i);
            threadPoolListener.threadEnd(this, thread);
        }
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


    // --------------------------------------------------------- ��ȡ���������Է���


    /**
     * �����̳߳ص����Լ�
     * @return {@link Properties}
     *          -- �̳߳ص����Լ�
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * �����̳߳ص����Լ�
     * @param properties
     *          �̳߳ص����Լ�
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }


    /**
     * ��ȡ�̳߳����ơ�
     * @return {@link String}
     *          -- �����̳߳ص�����
     */
    public String getName() {
        return name;
    }

    /**
     * �����̳߳����ơ�
     * @param name
     *          �̳߳�����
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * ��ȡ�̳߳����߳�����Ĭ�ϵ����ȼ���
     * @return {@link String}
     *          -- �̳߳ظ��߳�Ĭ�ϵ����ȼ�
     */
    public int getThreadPriority() {
        return threadPriority;
    }

    /**
     * �����̳߳���־�Ƿ�����
     * @return {@link Boolean}
     *          -- ��������̳߳ص���־���򷵻� true , ���򷵻� false
     */
    public boolean isLog() {
        return isLog;
    }

    /**
     * �����̳߳���־�Ƿ�����
     * @param isLog
     *          �������Ƿ������̳߳���־
     */
    public void setLog(boolean isLog) {
        this.isLog = isLog;
    }

    /**
     * �����̳߳ظ��߳�����Ĭ�ϵ����ȼ���
     * @param threadPriority
     *          �̳߳ظ��߳�Ĭ�����õ����ȼ�
     */
    public void setThreadPriority(int threadPriority) {
        if (log.isDebugEnabled()) {
            log.debug(getClass().getName() + ": setPriority(" + threadPriority
                + "): here.");
        }

        if (threadPriority < Thread.MIN_PRIORITY) {
            throw new IllegalArgumentException("new priority < MIN_PRIORITY");
        } else if (threadPriority > Thread.MAX_PRIORITY) {
            throw new IllegalArgumentException("new priority > MAX_PRIORITY");
        }

        this.threadPriority = threadPriority;

        Enumeration<Thread> currentThreads = getThreads();
        Thread t = null;
        while (currentThreads.hasMoreElements()) {
            // ���õ�ǰ�����̵߳����ȼ�
            t = (Thread) currentThreads.nextElement();
            t.setPriority(threadPriority);
        }
    }

    /**
     * ��������߳�����
     * @param maxThreads
     *          ����߳���
     */
    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    /**
     * ��������߳�����
     * @return {@link Integer}
     *          ����߳���
     */
    public int getMaxThreads() {
        return maxThreads;
    }

    /**
     * ������С�����߳���
     * @param minSpareThreads
     *          ��С�����߳���
     */
    public void setMinSpareThreads(int minSpareThreads) {
        this.minSpareThreads = minSpareThreads;
    }

    /**
     * ������С�����߳�����
     * @return {@link Integer}
     *          ��С�����߳���
     */
    public int getMinSpareThreads() {
        return minSpareThreads;
    }

    /**
     * �����������߳�����
     * @param maxSpareThreads
     *          �������߳���
     */
    public void setMaxSpareThreads(int maxSpareThreads) {
        this.maxSpareThreads = maxSpareThreads;
    }

    /**
     * �����������߳�����
     * @return {@link Integer}
     *          -- �������߳���
     */
    public int getMaxSpareThreads() {
        return maxSpareThreads;
    }

    /**
     * �����̳߳��еĻ�����
     * @return {@link ThreadPoolBuffer}
     *          -- ������
     */
    public ThreadPoolBuffer getThreadPoolBuffer() {
        return threadPoolBuffer;
    }

    /**
     * �����̳߳ػ�����
     * @param threadPoolBuffer the threadPoolBuffer to set
     *          ��Ҫ���õĻ�����
     */
    public void setThreadPoolBuffer(
        ThreadPoolBuffer threadPoolBuffer) {
        this.threadPoolBuffer = threadPoolBuffer;
    }

    /**
     * ���ع����ȴ��ĳ�ʱʱ�䣬���������̻߳��յĵȴ�ʱ�䡣��λ������
     * @return {@link Long}
     *          -- �����ȴ��ĳ�ʱʱ�䣬���������̻߳��յĵȴ�ʱ�䡣��λ������
     */
    public long getWorkWaitTimeout() {
        return workWaitTimeout;
    }

    /**
     * ���ù����ȴ��ĳ�ʱʱ�䣬���������̻߳��յĵȴ�ʱ�䡣��λ������
     * @param workWaitTimeout the workWaitTimeout to set
     *          ��Ҫ���õĹ����ȴ��ĳ�ʱʱ�䣬���������̻߳��յĵȴ�ʱ�䡣��λ������
     */
    public void setWorkWaitTimeout(long workWaitTimeout) {
        this.workWaitTimeout = workWaitTimeout;
    }


    // --------------------------------------------------------- ��ȡֻ�����Եķ���

    /**
     * ��ȡ�̳߳����̵߳�ö��
     * @return �����̵߳�ö��
     */
    public Enumeration<Thread> getThreads() {
        return threads.keys();
    }

    /**
     * ��ȡ��ǰ�߳�����
     * @return ��ǰ�߳���
     */
    public int getCurrentThreadCount() {
        return currentThreadCount;
    }

    /**
     * ��ȡ��ǰ��æ(����)���߳���
     * @return ��ǰ��æ(����)���߳���
     */
    public int getCurrentThreadsBusy() {
        return currentThreadsBusy;
    }

    /**
     * ��ȡ�Ƿ�Ϊ�ػ��̡߳�
     * @return �Ƿ�Ϊ�ػ��߳�
     */
    public boolean isDaemon() {
        return isDaemon;
    }

    /**
     * �������Զ����������ڱ�����̳߳���ʵ������ִ���߳�
     * @return {@link Integer}
     *          �Զ�����������к�
     */
    protected int incSequence() {
        return sequence++;
    }

    /**
     * Ĭ�ϵ� main() ���������������̳߳�
     * @param args
     *          Ĭ�ϵĲ���
     */
    public static void main(String[] args) {
        final ThreadPool pool = new ThreadPool();
        pool.start();
        final int num = 100;
        final int times = 3;
        for (int i = 0; i < num; i++) {
            final int j = i;
            pool.add(new ThreadPoolRunnable() {

                public ThreadPoolRunnableAttributes
                    getThreadPoolRunnableAttributes() {
                    return new ThreadPoolRunnableAttributes();
                }

                public void runIt(
                        ThreadPoolRunnableAttributes
                        threadPoolRunnableAttributes) {
                    int k = 0;
                    while (true) {
                        try {
                            final int time = 1000;
                            Thread.sleep(time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(
                                "��j:" + j + "���߳�========��k:" + k + "������");
                        k++;
                        if (k == times) {
                            break;
                        }
                    }
                }

            });
        }
    }

}
