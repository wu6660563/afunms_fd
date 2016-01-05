/*
 * @(#)IndicatorValueActionRunnable.java     v1.01, 2014 1 6
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.service;

import java.util.List;

import com.afunms.common.util.SysLogger;
import com.afunms.common.util.threadPool.ThreadPoolRunnable;
import com.afunms.common.util.threadPool.ThreadPoolRunnableAttributes;
import com.afunms.gather.model.IndicatorValue;
import com.afunms.node.indicator.action.IndicatorValueAction;;

/**
 * ClassName:   IndicatorValueActionRunnable.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 6 16:55:46
 */
public class IndicatorValueActionRunnable implements ThreadPoolRunnable {

    /**
     * logger:
     * <p>日志
     *
     * @since   v1.01
     */
    private final static SysLogger logger = SysLogger.getLogger(IndicatorValueActionRunnable.class);

    private List<IndicatorValueAction> list;

    private IndicatorValue indicatorValue;

    /**
     * getThreadPoolRunnableAttributes:
     *
     * @return
     *
     * @since   v1.01
     * @see com.afunms.common.util.threadPool.ThreadPoolRunnable#getThreadPoolRunnableAttributes()
     */
    public ThreadPoolRunnableAttributes getThreadPoolRunnableAttributes() {
        return null;
    }

    /**
     * runIt:
     *
     * @param threadPoolRunnableAttributes
     *
     * @since   v1.01
     * @see com.afunms.common.util.threadPool.ThreadPoolRunnable#runIt(com.afunms.common.util.threadPool.ThreadPoolRunnableAttributes)
     */
    @SuppressWarnings("static-access")
    public void runIt(ThreadPoolRunnableAttributes threadPoolRunnableAttributes) {
        String type = getIndicatorValue().getIndicatorInfo().getNodeDTO().getType();
        String subType = getIndicatorValue().getIndicatorInfo().getNodeDTO().getSubtype();
        String nodeId = getIndicatorValue().getIndicatorInfo().getNodeDTO().getNodeid();
        String ipAddress = getIndicatorValue().getIndicatorInfo().getNodeDTO().getIpaddress();
        String name = getIndicatorValue().getIndicatorInfo().getGatherIndicators().getName();
        try {
            if (indicatorValue.getValue() == null) {
                logger.error("执行 " + type + ":" + subType + ":" + nodeId + ":" + ipAddress + ":" + name +"  采集值处理值为  null ！");
                return;
            }
            if (list != null) {
                for (IndicatorValueAction indicatorValueAction : list) {
                    indicatorValueAction.execute(indicatorValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("执行 " + type + ":" + subType + ":" + nodeId + ":" + ipAddress + ":" + name +"  采集值处理出错", e);
        }
    }

    /**
     * getList:
     * <p>
     *
     * @return  {@link List<IndicatorValueAction>}
     *          -
     * @since   v1.01
     */
    public List<IndicatorValueAction> getList() {
        return list;
    }

    /**
     * setList:
     * <p>
     *
     * @param   list
     *          -
     * @since   v1.01
     */
    public void setList(List<IndicatorValueAction> list) {
        this.list = list;
    }

    /**
     * getIndicatorValue:
     * <p>
     *
     * @return  IndicatorValue
     *          -
     * @since   v1.01
     */
    public IndicatorValue getIndicatorValue() {
        return indicatorValue;
    }

    /**
     * setIndicatorValue:
     * <p>
     *
     * @param   indicatorValue
     *          -
     * @since   v1.01
     */
    public void setIndicatorValue(IndicatorValue indicatorValue) {
        this.indicatorValue = indicatorValue;
    }

}

