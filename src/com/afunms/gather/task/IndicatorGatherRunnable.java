/*
 * @(#)IndicatorGatherRunnable.java     v1.01, 2014 1 3
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.task;

import com.afunms.common.util.threadPool.ThreadPoolRunnableAbstract;
import com.afunms.common.util.threadPool.ThreadPoolRunnableAttributes;

/**
 * ClassName:   IndicatorGatherRunnable.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 3 15:22:33
 */
public class IndicatorGatherRunnable extends ThreadPoolRunnableAbstract {

    /**
     * indicatorGather:
     * <p>指标采集类
     *
     * @since   v1.01
     */
    private IndicatorGatherTask indicatorGatherTask;

    /**
     * IndicatorGatherRunnable.java:
     * <p>构造方法
     * @param   indicatorGatherTask
     *          - 所属的采集任务
     *
     * @since   v1.01
     */
    public IndicatorGatherRunnable(IndicatorGatherTask indicatorGatherTask) {
        setIndicatorGatherTask(indicatorGatherTask);
    }

    /**
     * getThreadPoolRunnableAttributes:
     *
     * @return
     *
     * @since   v1.01
     * @see com.afunms.common.util.threadPool.ThreadPoolRunnableAbstract#getThreadPoolRunnableAttributes()
     */
    @Override
    public ThreadPoolRunnableAttributes getThreadPoolRunnableAttributes() {
        return this.threadPoolRunnableAttributes;
    }

    /**
     * runIt:
     *
     * @param threadPoolRunnableAttributes
     *
     * @since   v1.01
     * @see com.afunms.common.util.threadPool.ThreadPoolRunnableAbstract#runIt(com.afunms.common.util.threadPool.ThreadPoolRunnableAttributes)
     */
    @Override
    public void runIt(ThreadPoolRunnableAttributes threadPoolRunnableAttributes) {
        getIndicatorGatherTask().update(getIndicatorGatherTask().getIndicatorGather().getIndicatorValue());
    }
    /**
     * getIndicatorGatherTask:
     * <p>
     *
     * @return  IndicatorGatherTask
     *          -
     * @since   v1.01
     */
    public IndicatorGatherTask getIndicatorGatherTask() {
        return indicatorGatherTask;
    }
    /**
     * setIndicatorGatherTask:
     * <p>
     *
     * @param   indicatorGatherTask
     *          -
     * @since   v1.01
     */
    public void setIndicatorGatherTask(IndicatorGatherTask indicatorGatherTask) {
        this.indicatorGatherTask = indicatorGatherTask;
    }

}

