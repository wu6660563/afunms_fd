/*
 * @(#)FileReceiveRunnable.java     v1.01, 2013 12 20
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.synchronize.service;

import java.io.File;
import java.util.Properties;

import com.afunms.common.util.SerializeUtil;
import com.afunms.common.util.SysLogger;
import com.afunms.initialize.ResourceCenter;
import com.afunms.rmi.service.RMIParameter;

/**
 * ClassName:   FileReceiveRunnable.java
 * <p> File ���� {@link Runnable}
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 20 11:30:22
 */
@SuppressWarnings("static-access")
public class FileReceiveRunnable extends ReceiveRunable {

    /**
     * SYS_PATH:
     * <p>�����ļ���·��
     *
     * @since   v1.01
     */
    private final static String SYS_PATH = ResourceCenter.getInstance().getSysPath() +  "/";

    /**
     * filePath:
     * <p>�����ļ���·��
     *
     * @since   v1.01
     */
    private String filePath = "receive_file/";

    /**
     * logger:
     * <p>��־
     *
     * @since   v1.01
     */
    private final static SysLogger logger = SysLogger.getLogger(FileReceiveRunnable.class);

    /**
     * isTerminate:
     * <p>�Ƿ���ֹ
     *
     * @since   v1.01
     */
    private boolean isTerminate = false;

    /**
     * intervalTime:
     * <p>ÿ��ִ�м��ʱ��
     *
     * @since   v1.01
     */
    private long intervalTime = 60 * 1000;

    /**
     * properties:
     * <p>����
     *
     * @since   v1.01
     */
    private Properties properties;

    /**
     * name:
     * <p>
     *
     * @since   v1.01
     */
    private String name = "�ļ������߳�";

    /**
     * thread:
     * <p>�߳�
     *
     * @since   v1.01
     */
    private Thread thread;

    /**
     * FileReceiveRunnable.java:
     * <p>���췽��
     * @param   service
     *          - ���շ���
     *
     * @since   v1.01
     */
    public FileReceiveRunnable(ReceiveService service) {
        super(service);
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
     * init:
     * <p>��ʼ��
     *
     *
     * @since   v1.01
     */
    public void init() {
        try {
            long intervalTime = Long.valueOf((String)getProperties().get("intervalTime"));
            if (intervalTime >= 0) {
                setIntervalTime(intervalTime);
            }
            String filePath = (String) getProperties().get("filePath");
            if (filePath != null || filePath.trim().length() == 0) {
                setFilePath(filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * start:
     * <p>����ά���߳�
     *
     *
     * @since   v1.01
     */
    public void start() {
        if (!isTerminate()) {
            init();
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
     *
     *
     * @since   v1.01
     * @see java.lang.Runnable#run()
     */
    public void run() {
        while (true) {
            try {
                String filePath = SYS_PATH + getFilePath();
                File dir = new File(filePath);
                if (!dir.exists()) {
                    dir.createNewFile();
                }
                if (dir.isDirectory()) {
                    File[] files = dir.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        try {
                            RMIParameter parameter = (RMIParameter) SerializeUtil.deserialize(filePath + files[i].getName(), true);
                            DataActionService.getInstance().execute(parameter);
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                        }
                    }
                }
                synchronized (this) {
                    this.wait(getIntervalTime());
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("ִ��" + getName() + "����", e);
            }
        }
    }

    /**
     * isTerminate:
     * <p>
     *
     * @return  boolean
     *          -
     * @since   v1.01
     */
    public boolean isTerminate() {
        return isTerminate;
    }

    /**
     * setTerminate:
     * <p>
     *
     * @param   isTerminate
     *          -
     * @since   v1.01
     */
    public void setTerminate(boolean isTerminate) {
        this.isTerminate = isTerminate;
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
     * getThread:
     * <p>
     *
     * @return  Thread
     *          -
     * @since   v1.01
     */
    public Thread getThread() {
        return thread;
    }

    /**
     * setThread:
     * <p>
     *
     * @param   thread
     *          -
     * @since   v1.01
     */
    public void setThread(Thread thread) {
        this.thread = thread;
    }

    /**
     * getFilePath:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * setFilePath:
     * <p>
     *
     * @param   filePath
     *          -
     * @since   v1.01
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

}

