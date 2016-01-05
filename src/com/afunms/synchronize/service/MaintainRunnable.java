/*
 * @(#)MaintainRunnable.java     v1.01, 2013 12 23
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.synchronize.service;

import java.util.List;
import java.util.Properties;

import com.afunms.common.util.SysLogger;

/**
 * ClassName:   MaintainRunnable.java
 * <p>ά���̣߳������ͷ����������ʱ���ã���������
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 23 17:12:10
 */
@SuppressWarnings("static-access")
public class MaintainRunnable implements Runnable {

    /**
     * logger:
     * <p>��־
     *
     * @since   v1.01
     */
    private final static SysLogger logger = SysLogger.getLogger(MaintainRunnable.class);

    /**
     * isTerminate:
     * <p>�Ƿ���ֹ
     *
     * @since   v1.01
     */
    private boolean isTerminate = true;

    /**
     * intervalTime:
     * <p>ÿ��ά�����ʱ��
     *
     * @since   v1.01
     */
    private long intervalTime = 60 * 1000;

    /**
     * name:
     * <p>����
     *
     * @since   v1.01
     */
    private String name = "ά���߳�";

    /**
     * properties:
     * <p>����
     *
     * @since   v1.01
     */
    private Properties properties;

    /**
     * thread:
     * <p>�߳�
     *
     * @since   v1.01
     */
    private Thread thread;

    /**
     * service:
     * <p>ͬ������
     *
     * @since   v1.01
     */
    private SynchronizeService service;

    /**
     * BufferSendRunnable.java:
     * ���췽��
     * @param   service
     *          - ͬ������
     *
     * @since   v1.01
     */
    public MaintainRunnable(SynchronizeService service) {
        this.service = service;
    }

    /**
     * terminate:
     * <p>��ֹ
     *
     *
     * @since   v1.01
     */
    public synchronized void terminate() {
        setTerminate(true);
        this.notify();
    }

    /**
     * start:
     * <p>����ά���߳�
     *
     *
     * @since   v1.01
     */
    public void start() {
        if (isTerminate()) {
            logger.info("����" + getName() + "��");
            try {
                long intervalTime = Long.valueOf((String) getProperties().get("intervalTime"));
                if (intervalTime >= 0) {
                    setIntervalTime(intervalTime);
                }
                String name = (String) getProperties().get("name");
                if (name.trim().length() > 0) {
                    setName(name);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            setTerminate(false);
            if (getThread() == null || !getThread().isAlive()) {
                setThread(new Thread(this));
                getThread().start();
                logger.info("����" + getName() + "�ɹ�");
            }
        }
    }

    /**
     * run:
     * <p>����ά���̣߳����������һ�����ͷ����������ɹ���
     * ��ȴ� {@link #intervalTime} ʱ��󣬼���ִ�У����ȫ���������ɹ����˳���
     *
     *
     * @since   v1.01
     * @see java.lang.Runnable#run()
     */
    public void run() {
        while(true) {
            try {
                if (isTerminate()) {
                    // �Ƿ���ֹ
                    logger.info("�˳�" + getName() + "��");
                    break;
                }
                logger.info("Ŀǰ��:" + getService().getSendTimes() + " ��������뷢��");
                logger.info("Ŀǰ��:" + getService().getSendThreadPool().getThreadPoolBuffer().size() + " ������δ��ɷ���");
                logger.info("Ŀǰ��:" + getService().getSendThreadPool().getCurrentThreadCount() + " �����������̳߳���ִ��");
                logger.info("Ŀǰ��:" + getService().getReceiveServiceList() + " ��������뷢��");
                List<ReceiveService> list = getService().getReceiveServiceList();
                for (ReceiveService receiveService : list) {
                    logger.info("Ŀǰ��:" + receiveService.getReceiveTimes() + " ��������ܵ�");
                }
                logger.info("Ŀǰ��:" + DataActionService.getInstance().getExecuteTimes() + " ��������봦��");

                synchronized (this) {
                    logger.info("�ȴ�:" + intervalTime + "������ִ��" + getName() + "��");
                    this.wait(intervalTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                setTerminate(false);
                logger.error("ά���߳�ִ�г�������ִ��" + getName() + "��", e);
            }
        }
    }

    /**
     * isTerminate:
     * <p>�Ƿ���ֹ
     *
     * @return  {@link Boolean}
     *          - �Ƿ���ֹ
     * @since   v1.01
     */
    public synchronized boolean isTerminate() {
        return isTerminate;
    }
    /**
     * setTerminate:
     * <p>������ֹ
     *
     * @param   isTerminate
     *          -
     * @since   v1.01
     */
    public synchronized void setTerminate(boolean isTerminate) {
        this.isTerminate = isTerminate;
    }

    /**
     * getThread:
     * <p>��ȡά���߳�
     *
     * @return  {@link Thread}
     *          - ά���߳�
     * @since   v1.01
     */
    public Thread getThread() {
        return thread;
    }
    /**
     * setThread:
     * <p>����ά���߳�
     *
     * @param   thread
     *          - ά���߳�
     * @since   v1.01
     */
    public void setThread(Thread thread) {
        this.thread = thread;
    }

    /**
     * getIntervalTime:
     * <p>
     *
     * @return  long
     *          -
     * @since   v1.01
     */
    public long getIntervalTime() {
        return intervalTime;
    }

    /**
     * setIntervalTime:
     * <p>
     *
     * @param   intervalTime
     *          -
     * @since   v1.01
     */
    public void setIntervalTime(long intervalTime) {
        this.intervalTime = intervalTime;
    }

    /**
     * getProperties:
     * <p>
     *
     * @return  Properties
     *          -
     * @since   v1.01
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * setProperties:
     * <p>
     *
     * @param   properties
     *          -
     * @since   v1.01
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * getName:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getName() {
        return name;
    }

    /**
     * setName:
     * <p>
     *
     * @param   name
     *          -
     * @since   v1.01
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getService:
     * <p>
     *
     * @return  SynchronizeService
     *          -
     * @since   v1.01
     */
    public SynchronizeService getService() {
        return service;
    }

    /**
     * setService:
     * <p>
     *
     * @param   service
     *          -
     * @since   v1.01
     */
    public void setService(SynchronizeService service) {
        this.service = service;
    }

}

