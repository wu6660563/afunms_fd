package com.afunms.cleaning.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.afunms.common.util.SysLogger;
import com.afunms.event.dao.EventListDao;

public class AlarmDataCleaningService {

    private static SysLogger logger = SysLogger
                    .getLogger(AlarmDataCleaningService.class.getName());

    private static SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

    private static SimpleDateFormat sdfTime = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");

    private static String timeTemp = " 00:00:00";

    private static String executTime = " 02:00:00";

    private static Long timeForDay = 1000 * 60 * 60 * 24L;

    /**
     * ����ʱ�� 60 ��
     */
    private static Long retentionTime = 1000 * 60 * 60 * 24 * 60L;

    private static AlarmDataCleaningService instance;

    private AlarmDataCleaningRunnable alarmDataCleaningRunnable;

    private static Date lastDeleteDate = null;

    private boolean isRunning = false;

    private AlarmDataCleaningService() {
    }

    public static AlarmDataCleaningService getInstance() {
        if (instance == null) {
            instance = new AlarmDataCleaningService();
        }
        return instance;
    }

    public void start() {
        logger.info("�����澯������� ......");
        alarmDataCleaningRunnable = new AlarmDataCleaningRunnable(this);
        alarmDataCleaningRunnable.start();
        setRunning(false);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                try {
                    AlarmDataCleaningService.getInstance()
                                    .getAlarmDataCleaningRunnable().runIt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }, 1000 * 60, 1000 * 60 * 60);
    }

    public void cleaningAlarmData() {
        try {
            Date currDate = new Date();
            if (!vilidate(currDate)) {
                logger.info("�ϴ�ִ��ʱ��Ϊ:" + sdfTime.format(lastDeleteDate) + "����ǰִ��ʱ������ϴ�ִ��ʱ��δ����һ�죬������ɾ������");
                return;
            }
            setRunning(true);
            Date recordTime = new Date(currDate.getTime() - retentionTime);
            String recordTimeStr = sdfDate.format(recordTime) + timeTemp;
            logger.info("��ʼɾ��ʱ����" + recordTimeStr + "֮ǰ�ĸ澯����");
            EventListDao eventListDao = new EventListDao();
            try {
                eventListDao.deleteByRecordtime(recordTimeStr);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                eventListDao.close();
            }
            Date currDeleteDate = sdfTime.parse(sdfDate.format(recordTime) + executTime);
            lastDeleteDate = currDeleteDate;
            setRunning(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean vilidate(Date currDeleteDate) {
        boolean result = true;
        // С���賿6���ִ��
        try {
            Date sixDate = sdfTime.parse(sdfDate.format(currDeleteDate) + " 06:00:00");
            
            if (lastDeleteDate != null && currDeleteDate.getTime() - lastDeleteDate.getTime() < timeForDay) {
                result = false;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    /**
     * @return the alarmDataCleaningRunnable
     */
    public AlarmDataCleaningRunnable getAlarmDataCleaningRunnable() {
        return alarmDataCleaningRunnable;
    }

    /**
     * @param alarmDataCleaningRunnable
     *            the alarmDataCleaningRunnable to set
     */
    public void setAlarmDataCleaningRunnable(
                    AlarmDataCleaningRunnable alarmDataCleaningRunnable) {
        this.alarmDataCleaningRunnable = alarmDataCleaningRunnable;
    }

    /**
     * @return the isRunning
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * @param isRunning the isRunning to set
     */
    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

}

class AlarmDataCleaningRunnable implements Runnable {

    /**
     * ��־
     */
    private static SysLogger logger = SysLogger
                    .getLogger(AlarmDataCleaningRunnable.class);

    /**
     * �����߳�
     */
    private Thread thread;

    /**
     * �Ƿ��ж�
     */
    private boolean isTerminate;

    /**
     * �澯����ɾ������
     */
    private AlarmDataCleaningService service;

    /**
     * ���췽��
     * 
     * @param service
     *            �澯����ɾ������
     */
    public AlarmDataCleaningRunnable(AlarmDataCleaningService service) {
        setService(service);
        this.start();
    }

    /**
     * ��ʼɾ���߳�
     */
    public void start() {
        this.isTerminate = false;
        this.thread = new Thread(this);
        this.thread.start();
    }

    public void run() {
        while (true) {
            try {
                synchronized (this) {
                    while (true) {
                        this.wait();
                        if (!this.service.isRunning()) {
                            logger.info("�澯����ɾ�������ڿ���״̬���˳��ȴ�...");
                            break;
                        }
                        logger.info("�澯����ɾ�������������У������ȴ�...");
                    }
                }
                if (isTerminate) {
                    break;
                }
                this.service.cleaningAlarmData();
            } catch (InterruptedException e) {
                logger.error("", e);
            }
        }
    }

    /**
     * ֪ͨ�߳�����
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
     * @return the service
     */
    public AlarmDataCleaningService getService() {
        return service;
    }

    /**
     * @param service
     *            the service to set
     */
    public void setService(AlarmDataCleaningService service) {
        this.service = service;
    }

}