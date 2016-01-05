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
                    logger.info("��ǰ���ݹ鵵��������ִ�е� " + archivingService.getDataArchivingCalculate().getIsExecutingDataArchiving().getName() + " ���ݹ鵵��" + archivingService.getDataArchivingCalculate().getIsExecutingDataArchiving().getLastTime()
                                    +"ʱ��ĵ� " + archivingService.getDataArchivingCalculate().getNodeNumber() + "����ͳ�Ʒ���");
                } else {
                    logger.info("��ǰ���ݹ鵵����ͳ�ƴ��ڿ���״̬");
                }
                if (archivingService.getDataArchivingDelete().isExecuting() && archivingService.getDataArchivingDelete().getIsExecutingDataArchiving() != null) {
                    logger.info("��ǰ���ݹ鵵��������ִ�е� " + archivingService.getDataArchivingDelete().getIsExecutingDataArchiving().getName() + " ���ݹ鵵��" + archivingService.getDataArchivingDelete().getIsExecutingDataArchiving().getStartTime()
                                    +"ʱ��ĵ� " + archivingService.getDataArchivingDelete().getNodeNumber() + "����ɾ������");
                } else {
                    logger.info("��ǰ���ݹ鵵����ɾ�����ڿ���״̬");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * �жϹ鵵�߳�
     */
    public synchronized void terminate() {
        this.isTerminate = true;
        this.notify();
    }

    /**
     * ֹͣ�鵵�߳�
     */
    public synchronized void stop() {
        this.terminate();
    }

    /**
     * ֪ͨ�߳�ִ��
     */
    public synchronized void runIt() {
        this.isTerminate = false;
        this.notify();
    }
    
}
 