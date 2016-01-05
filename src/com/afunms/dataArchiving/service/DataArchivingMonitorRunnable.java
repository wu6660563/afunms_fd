package com.afunms.dataArchiving.service;

import com.afunms.common.util.SysLogger;

public class DataArchivingMonitorRunnable implements Runnable {

	private static SysLogger logger = SysLogger.getLogger(DataArchivingMonitorRunnable.class);

	private DataArchivingService archivingService;

    private Thread thread;

    private boolean isTerminate = true;

    private static long interval = 60 * 1000L;

    public DataArchivingMonitorRunnable(DataArchivingService archivingService) {
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
                    this.wait(interval);
                }
                if (isTerminate) {
                    break;
                }
                if (archivingService.getDataArchivingCalculate().isExecuting() && archivingService.getDataArchivingCalculate().getIsExecutingDataArchiving() != null) {
                    logger.info("当前数据归档服务正在执行的 " + archivingService.getDataArchivingCalculate().getIsExecutingDataArchiving().getName() + " 数据归档在" + archivingService.getDataArchivingCalculate().getIsExecutingDataArchiving().getLastTime()
                                    +"时间的第 " + archivingService.getDataArchivingCalculate().getNodeNumber() + "个的统计服务");
                } else {
                    logger.info("当前数据归档服务统计处于空闲状态");
                }
                if (archivingService.getDataArchivingDelete().isExecuting() && archivingService.getDataArchivingDelete().getIsExecutingDataArchiving() != null) {
                    logger.info("当前数据归档服务正在执行的 " + archivingService.getDataArchivingDelete().getIsExecutingDataArchiving().getName() + " 数据归档在" + archivingService.getDataArchivingDelete().getIsExecutingDataArchiving().getStartTime()
                                    +"时间的第 " + archivingService.getDataArchivingDelete().getNodeNumber() + "个的删除服务");
                } else {
                    logger.info("当前数据归档服务删除处于空闲状态");
                }
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
 