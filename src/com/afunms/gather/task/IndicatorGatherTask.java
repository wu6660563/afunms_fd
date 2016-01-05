/*
 * @(#)IndicatorGatherTask.java     v1.01, 2014 1 3
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.task;

import java.util.TimerTask;

import com.afunms.common.util.SysLogger;
import com.afunms.gather.indicator.IndicatorGather;
import com.afunms.gather.model.IndicatorInfo;
import com.afunms.gather.model.IndicatorValue;

/**
 * ClassName:   IndicatorGatherTask.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 3 15:13:34
 */
public class IndicatorGatherTask extends TimerTask {

    /**
     * logger:
     * <p>日志
     *
     * @since   v1.01
     */
    private final static SysLogger logger = SysLogger.getLogger(IndicatorGatherTask.class.getName());

    /**
     * indicatorGatherTaskManager:
     * <p>指标采集任务管理器
     *
     * @since   v1.01
     */
    private IndicatorGatherTaskManager indicatorGatherTaskManager;

    /**
     * id:
     * <p>采集 Id
     *
     * @since   v1.01
     */
    private String id;

    /**
     * indicator:
     * <p>采集指标
     *
     * @since   v1.01
     */
    private IndicatorInfo indicatorInfo;

    /**
     * intervalTime:
     * <p>采集间隔时间
     *
     * @since   v1.01
     */
    private Long intervalTime;

    /**
     * indicatorGather:
     * <p>指标采集类
     *
     * @since   v1.01
     */
    private IndicatorGather indicatorGather;

    /**
     * runnable:
     * <p>指标采集线程执行类
     *
     * @since   v1.01
     */
    private IndicatorGatherRunnable runnable = new IndicatorGatherRunnable(this);

    /**
     * run:
     *
     *
     * @since   v1.01
     * @see java.util.TimerTask#run()
     */
    @SuppressWarnings("static-access")
    @Override
    public void run() {
        try {
            getIndicatorGatherTaskManager().getThreadPool().add(getRunnable());
        } catch (RuntimeException e) {
            e.printStackTrace();
            logger.error("设备：" + getIndicatorInfo().getNodeDTO().getNodeid() + "=="
                            + getIndicatorInfo().getNodeDTO().getIpaddress() + "=="
                            + getIndicatorInfo().getNodeDTO().getType() + "=="
                            + getIndicatorInfo().getNodeDTO().getSubtype()
                            +  "，指标:" 
                            + getIndicatorInfo().getGatherIndicators().getName()
                            + "采集过程中出错", e);
        }
    }

    /**
     * update:
     * <p>更新采集指标值
     *
     * @param indicatorValue
     *
     * @since   v1.01
     */
    public void update(IndicatorValue indicatorValue) {
        getIndicatorGatherTaskManager().getGatherService().updateIndicatorValue(getId(), indicatorValue);
    }
    /**
     * getRunnable:
     * <p>
     *
     * @return  IndicatorGatherRunnable
     *          -
     * @since   v1.01
     */
    public IndicatorGatherRunnable getRunnable() {
        return runnable;
    }

    /**
     * setRunnable:
     * <p>
     *
     * @param   runnable
     *          -
     * @since   v1.01
     */
    public void setRunnable(IndicatorGatherRunnable runnable) {
        this.runnable = runnable;
    }

    /**
     * getIndicatorGatherTaskManager:
     * <p>
     *
     * @return  IndicatorGatherTaskManager
     *          -
     * @since   v1.01
     */
    public IndicatorGatherTaskManager getIndicatorGatherTaskManager() {
        return indicatorGatherTaskManager;
    }

    /**
     * setIndicatorGatherTaskManager:
     * <p>
     *
     * @param   indicatorGatherTaskManager
     *          -
     * @since   v1.01
     */
    public void setIndicatorGatherTaskManager(
                    IndicatorGatherTaskManager indicatorGatherTaskManager) {
        this.indicatorGatherTaskManager = indicatorGatherTaskManager;
    }

    /**
     * getId:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getId() {
        return id;
    }

    /**
     * setId:
     * <p>
     *
     * @param   id
     *          -
     * @since   v1.01
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * getIndicatorInfo:
     * <p>
     *
     * @return  IndicatorInfo
     *          -
     * @since   v1.01
     */
    public IndicatorInfo getIndicatorInfo() {
        return indicatorInfo;
    }

    /**
     * setIndicatorInfo:
     * <p>
     *
     * @param   indicatorInfo
     *          -
     * @since   v1.01
     */
    public void setIndicatorInfo(IndicatorInfo indicatorInfo) {
        this.indicatorInfo = indicatorInfo;
    }

    /**
     * getIntervalTime:
     * <p>
     *
     * @return  Long
     *          -
     * @since   v1.01
     */
    public Long getIntervalTime() {
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
    public void setIntervalTime(Long intervalTime) {
        this.intervalTime = intervalTime;
    }

    /**
     * getIndicatorGather:
     * <p>
     *
     * @return  IndicatorGather
     *          -
     * @since   v1.01
     */
    public IndicatorGather getIndicatorGather() {
        return indicatorGather;
    }

    /**
     * setIndicatorGather:
     * <p>
     *
     * @param   indicatorGather
     *          -
     * @since   v1.01
     */
    public void setIndicatorGather(IndicatorGather indicatorGather) {
        this.indicatorGather = indicatorGather;
    }

}

