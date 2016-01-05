/*
 * @(#)IndicatorValueBuffer.java     v1.01, 2014 1 6
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.service;

import java.util.Vector;

import com.afunms.common.util.SysLogger;
import com.afunms.gather.model.IndicatorValue;

/**
 * ClassName:   IndicatorValueBuffer.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 6 16:24:22
 */
public class IndicatorValueBuffer {

    /**
     * logger:
     * <p>日志
     *
     * @since   v1.01
     */
    private static SysLogger logger = SysLogger.getLogger(IndicatorValueBuffer.class);

    /**
     * indicatorValueActionService:
     * <p>采集值处理服务
     *
     * @since   v1.01
     */
    private IndicatorValueActionService indicatorValueActionService;

    /**
     * indicatorValueBuffer:
     * <p>采集值缓冲区
     *
     * @since   v1.01
     */
    private Vector<IndicatorValue> indicatorValueBuffer = null;

    /**
     * IndicatorValueBuffer.java:
     * 构造方法
     * @param   indicatorValueActionService
     *          - 处理服务
     *
     * @since   v1.01
     */
    public IndicatorValueBuffer(IndicatorValueActionService indicatorValueActionService) {
        this.indicatorValueActionService = indicatorValueActionService;
        indicatorValueBuffer = new Vector<IndicatorValue>();
    }
    
    /**
     * addIndicatorValue:
     * <p>向缓冲区中加入一个需要被执行的采集指标值
     *
     * @param   indicatorValue
     *          - 采集指标值
     *
     * @since   v1.01
     */
    public void addIndicatorValue(IndicatorValue indicatorValue) {
        this.indicatorValueBuffer.add(indicatorValue);
    }

    /**
     * popIndicatorValue:
     * <p>从缓冲区中弹出一个第一个需要执行的采集值
     *
     * @return
     *
     * @since   v1.01
     */
    public synchronized IndicatorValue popIndicatorValue() {
        IndicatorValue indicatorValue = null;
        if (!isEmpty()) {
            indicatorValue = this.indicatorValueBuffer.remove(0);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("当前缓冲区为空");
            }
        }
        return indicatorValue;
    }

    /**
     * isEmpty:
     * <p>判断当前缓冲区是否为空
     *
     * @return  {@link Boolean}
     *          - 当前缓冲区为空则为 true ; 否则为 false
     *
     * @since   v1.01
     */
    public synchronized boolean isEmpty() {
       return this.indicatorValueBuffer.isEmpty();
    }

    /**
     * size:
     * <p>获取缓冲区当前需要执行的任务数
     *
     * @return  {@link Integer}
     *          - 缓冲区中需要执行的任务数
     *
     * @since   v1.01
     */
    public int size() {
        return this.indicatorValueBuffer.size();
    }

    /**
     * getIndicatorValueActionService:
     * <p>
     *
     * @return  IndicatorValueActionService
     *          -
     * @since   v1.01
     */
    public IndicatorValueActionService getIndicatorValueActionService() {
        return indicatorValueActionService;
    }

    /**
     * setIndicatorValueActionService:
     * <p>
     *
     * @param   indicatorValueActionService
     *          -
     * @since   v1.01
     */
    public void setIndicatorValueActionService(
                    IndicatorValueActionService indicatorValueActionService) {
        this.indicatorValueActionService = indicatorValueActionService;
    }

    /**
     * getIndicatorValueBuffer:
     * <p>
     *
     * @return  Vector<IndicatorValue>
     *          -
     * @since   v1.01
     */
    public Vector<IndicatorValue> getIndicatorValueBuffer() {
        return indicatorValueBuffer;
    }

    /**
     * setIndicatorValueBuffer:
     * <p>
     *
     * @param   indicatorValueBuffer
     *          -
     * @since   v1.01
     */
    public void setIndicatorValueBuffer(Vector<IndicatorValue> indicatorValueBuffer) {
        this.indicatorValueBuffer = indicatorValueBuffer;
    }

}

