package com.afunms.dataArchiving.service;

public class DataArchivingCalculateRunnable implements Runnable {

    private DataArchivingService archivingService;

    private Thread thread;

    private boolean isTerminate = true;

    public DataArchivingCalculateRunnable(DataArchivingService archivingService) {
        this.archivingService = archivingService;
    }

    public boolean start () {
        isTerminate = false;
        thread = new Thread(this);
        thread.start();
        return true;
    }
    
    public void run() {
        while (true) {
            try {
                synchronized (this) {
                    this.wait();
                }
                if (isTerminate) {
                    break;
                }
                archivingService.calculate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 中断归档线程
     */
    public synchronized void terminate() {
        this.isTerminate = true;
        this.notify();
    }

    /**
     * 停止归档线程
     */
    public synchronized void stop() {
        this.terminate();
    }

    /**
     * 通知线程执行
     */
    public synchronized void runIt() {
        this.isTerminate = false;
        this.notify();
    }
    
}
 