package com.afunms.discovery;

import java.util.LinkedList;

/**
    �̳߳���һ���̣߳�����ִ��������߳���
*/
public class ThreadPool extends ThreadGroup {

    private boolean isAlive;
    private LinkedList taskQueue;
    private int threadID;
    private static int threadPoolID;

    /**
        �����µ��̳߳أ�numThreads�ǳ��е��߳���
    */
    public ThreadPool(int numThreads) {
        super("ThreadPool-" + (threadPoolID++));
        setDaemon(true);

        isAlive = true;

        taskQueue = new LinkedList();
        for (int i=0; i<numThreads; i++) {
            new PooledThread().start();
        }
    }
    /**
        ���������������ڳ�����һ�����߳������У������յ���˳��ִ��
    */
    public synchronized void runTask(Runnable task) {
        if (!isAlive) {
            throw new IllegalStateException();//�̱߳������׳�IllegalStateException�쳣
        }
        if (task != null) {
            taskQueue.add(task);
            notify();
        }

    }


    protected synchronized Runnable getTask()
        throws InterruptedException
    {
        while (taskQueue.size() == 0) {
            if (!isAlive) {
                return null;
            }
            wait();
        }
        return (Runnable)taskQueue.removeFirst();
    }


    /**
        �ر��̳߳أ������߳�ֹͣ������ִ������
    */
    public synchronized void close() {
        if (isAlive) {
            isAlive = false;
            taskQueue.clear();
            interrupt();
        }
    }


    /**
        �ر��̳߳ز��ȴ������߳���ɣ�ִ�еȴ�������
    */
    public void join() {
        //���ߵȴ��߳��̳߳��ѹ�
        synchronized (this) {
            isAlive = false;
            notifyAll();
        }

        // �ȴ������߳����
        Thread[] threads = new Thread[activeCount()];
        int count = enumerate(threads);
        for (int i=0; i<count; i++) {
            try {
                threads[i].join();
            }
            catch (InterruptedException ex) { }
        }
    }
    
    
private class PooledThread extends Thread {


        public PooledThread() {
            super(ThreadPool.this,
                "PooledThread-" + (threadID++));
        }


        public void run() {
            while (!isInterrupted()) {

                // �õ�����
                Runnable task = null;
                try {
                    task = getTask();
                }
                catch (InterruptedException ex) { }

                // ��getTask()����null���жϣ���رմ��̲߳�����
                if (task == null) {
                    return;
                }

                // �������������쳣
                try {
                    task.run();
                }
                catch (Throwable t) {
                    uncaughtException(this, t);
                }
            }
        }
    }
}
    

