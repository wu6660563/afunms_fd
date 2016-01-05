/*
 * @(#)IndicatorValueActionService.java     v1.01, 2014 1 6
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.afunms.common.util.threadPool.ThreadPool;
import com.afunms.gather.model.IndicatorValue;
import com.afunms.node.indicator.action.IndicatorValueAction;
import com.afunms.node.model.Category;
import com.afunms.node.model.Indicator;
import com.afunms.node.service.CategoryService;

/**
 * ClassName:   IndicatorValueActionService.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 6 11:04:07
 */
public class IndicatorValueActionService {

    /**
     * threadPool:
     * <p>处理采集指标值的线程池
     *
     * @since   v1.01
     */
    public ThreadPool threadPool;

    /**
     * threadPoolProperties:
     * <p>线程池配置
     *
     * @since   v1.01
     */
    private Properties threadPoolProperties = null;

    /**
     * buffer:
     * <p>缓冲区
     *
     * @since   v1.01
     */
    private IndicatorValueBuffer buffer;

    /**
     * bufferRunnable:
     * <p>缓冲区线程
     *
     * @since   v1.01
     */
    private IndicatorValueActionBufferRunnable bufferRunnable;

    /**
     * IndicatorValueActionService.java:
     * 构造方法
     *
     * @since   v1.01
     */
    public IndicatorValueActionService() {
        setBuffer(new IndicatorValueBuffer(this));
        setBufferRunnable(new IndicatorValueActionBufferRunnable(this));
    }
    
    /**
     * start:
     * <p>启动
     *
     *
     * @since   v1.01
     */
    public void start() {
        getBufferRunnable().start();
        setThreadPool(new ThreadPool(getThreadPoolProperties()));
        getThreadPool().start();
    }

    /**
     * add:
     * <p>添加一个需要处理的采集指标值
     *
     * @param indicatorValue
     *
     * @since   v1.01
     */
    public synchronized void add(IndicatorValue indicatorValue) {
        getBuffer().addIndicatorValue(indicatorValue);
        getBufferRunnable().runIt();
    }

    /**
     * execute:
     * <p>执行处理采集指标值
     *
     * @param indicatorValue
     *
     * @since   v1.01
     */
    public void execute(IndicatorValue indicatorValue) {
        IndicatorValueActionRunnable indicatorValueActionRunnable = new IndicatorValueActionRunnable();
        indicatorValueActionRunnable.setIndicatorValue(indicatorValue);
        indicatorValueActionRunnable.setList(getIndicatorValueActionList(indicatorValue));
        getThreadPool().add(indicatorValueActionRunnable);
    }

    /**
     * getIndicatorValueActionList:
     * <p>获取采集指标值处理列表
     *
     * @param   indicatorValue
     *          - 采集指标值
     * @return  {@link List<IndicatorValueAction>}
     *          - 采集指标处理列表
     * @since   v1.01
     */
    public List<IndicatorValueAction> getIndicatorValueActionList(IndicatorValue indicatorValue) {
        CategoryService categoryService = new CategoryService();
        Category category = categoryService.getCategory(indicatorValue.getIndicatorInfo().getNodeDTO());
        List<Indicator> indicatorList = category.getIndicatorList();
        String indicatorName = indicatorValue.getIndicatorInfo().getGatherIndicators().getName();
        Indicator indicator = null;
        for (Indicator indicatorPer : indicatorList) {
            if (indicatorName.equals(indicatorPer.getName())) {
                indicator = indicatorPer;
                break;
            }
        }
        List<IndicatorValueAction> indicatorValueActionList = new ArrayList<IndicatorValueAction>();

        List<String> list = indicator.getIndicatorValueActionList();
        for (String indicatorValueActionName : list) {
            try {
                IndicatorValueAction indicatorValueAction = (IndicatorValueAction) Class.forName(indicatorValueActionName).newInstance();
                if (indicatorValueAction != null) {
                    indicatorValueActionList.add(indicatorValueAction);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            
        }
        return indicatorValueActionList;
    }

    /**
     * getThreadPool:
     * <p>
     *
     * @return  ThreadPool
     *          -
     * @since   v1.01
     */
    public ThreadPool getThreadPool() {
        return threadPool;
    }
    /**
     * setThreadPool:
     * <p>
     *
     * @param   threadPool
     *          -
     * @since   v1.01
     */
    public void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    /**
     * getThreadPoolProperties:
     * <p>
     *
     * @return  Properties
     *          -
     * @since   v1.01
     */
    public Properties getThreadPoolProperties() {
        return threadPoolProperties;
    }

    /**
     * setThreadPoolProperties:
     * <p>
     *
     * @param   threadPoolProperties
     *          -
     * @since   v1.01
     */
    public void setThreadPoolProperties(Properties threadPoolProperties) {
        this.threadPoolProperties = threadPoolProperties;
    }

    /**
     * getBuffer:
     * <p>
     *
     * @return  IndicatorValueBuffer
     *          -
     * @since   v1.01
     */
    public IndicatorValueBuffer getBuffer() {
        return buffer;
    }

    /**
     * setBuffer:
     * <p>
     *
     * @param   buffer
     *          -
     * @since   v1.01
     */
    public void setBuffer(IndicatorValueBuffer buffer) {
        this.buffer = buffer;
    }

    /**
     * getBufferRunnable:
     * <p>
     *
     * @return  IndicatorValueActionBufferRunnable
     *          -
     * @since   v1.01
     */
    public IndicatorValueActionBufferRunnable getBufferRunnable() {
        return bufferRunnable;
    }

    /**
     * setBufferRunnable:
     * <p>
     *
     * @param   bufferRunnable
     *          -
     * @since   v1.01
     */
    public void setBufferRunnable(IndicatorValueActionBufferRunnable bufferRunnable) {
        this.bufferRunnable = bufferRunnable;
    }

}

