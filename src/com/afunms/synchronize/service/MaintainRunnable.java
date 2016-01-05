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
 * <p>维护线程，当发送服务出现问题时启用，用于重连
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 23 17:12:10
 */
@SuppressWarnings("static-access")
public class MaintainRunnable implements Runnable {

    /**
     * logger:
     * <p>日志
     *
     * @since   v1.01
     */
    private final static SysLogger logger = SysLogger.getLogger(MaintainRunnable.class);

    /**
     * isTerminate:
     * <p>是否终止
     *
     * @since   v1.01
     */
    private boolean isTerminate = true;

    /**
     * intervalTime:
     * <p>每次维护间隔时间
     *
     * @since   v1.01
     */
    private long intervalTime = 60 * 1000;

    /**
     * name:
     * <p>名称
     *
     * @since   v1.01
     */
    private String name = "维护线程";

    /**
     * properties:
     * <p>配置
     *
     * @since   v1.01
     */
    private Properties properties;

    /**
     * thread:
     * <p>线程
     *
     * @since   v1.01
     */
    private Thread thread;

    /**
     * service:
     * <p>同步服务
     *
     * @since   v1.01
     */
    private SynchronizeService service;

    /**
     * BufferSendRunnable.java:
     * 构造方法
     * @param   service
     *          - 同步服务
     *
     * @since   v1.01
     */
    public MaintainRunnable(SynchronizeService service) {
        this.service = service;
    }

    /**
     * terminate:
     * <p>终止
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
     * <p>启动维护线程
     *
     *
     * @since   v1.01
     */
    public void start() {
        if (isTerminate()) {
            logger.info("启动" + getName() + "！");
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
                logger.info("启动" + getName() + "成功");
            }
        }
    }

    /**
     * run:
     * <p>运行维护线程，如果其中有一个发送服务启动不成功，
     * 则等待 {@link #intervalTime} 时间后，继续执行，如果全部都启动成功后，退出。
     *
     *
     * @since   v1.01
     * @see java.lang.Runnable#run()
     */
    public void run() {
        while(true) {
            try {
                if (isTerminate()) {
                    // 是否终止
                    logger.info("退出" + getName() + "！");
                    break;
                }
                logger.info("目前有:" + getService().getSendTimes() + " 个任务加入发送");
                logger.info("目前有:" + getService().getSendThreadPool().getThreadPoolBuffer().size() + " 个任务未完成发送");
                logger.info("目前有:" + getService().getSendThreadPool().getCurrentThreadCount() + " 个任务正在线程池中执行");
                logger.info("目前有:" + getService().getReceiveServiceList() + " 个任务加入发送");
                List<ReceiveService> list = getService().getReceiveServiceList();
                for (ReceiveService receiveService : list) {
                    logger.info("目前有:" + receiveService.getReceiveTimes() + " 个任务接受到");
                }
                logger.info("目前有:" + DataActionService.getInstance().getExecuteTimes() + " 个任务加入处理");

                synchronized (this) {
                    logger.info("等待:" + intervalTime + "秒后继续执行" + getName() + "！");
                    this.wait(intervalTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                setTerminate(false);
                logger.error("维护线程执行出错！继续执行" + getName() + "！", e);
            }
        }
    }

    /**
     * isTerminate:
     * <p>是否终止
     *
     * @return  {@link Boolean}
     *          - 是否终止
     * @since   v1.01
     */
    public synchronized boolean isTerminate() {
        return isTerminate;
    }
    /**
     * setTerminate:
     * <p>设置终止
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
     * <p>获取维护线程
     *
     * @return  {@link Thread}
     *          - 维护线程
     * @since   v1.01
     */
    public Thread getThread() {
        return thread;
    }
    /**
     * setThread:
     * <p>设置维护线程
     *
     * @param   thread
     *          - 维护线程
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

