/*
 * @(#)GatherDataPoolService.java     v1.01, 2014 1 3
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.service;

import java.util.Hashtable;
import java.util.Map;

import com.afunms.gather.model.IndicatorValue;

/**
 * ClassName:   GatherDataPoolService.java
 * <p>采集数据池服务，所有采集完成后的数据都会存储在其中
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 3 16:30:39
 */
public class GatherDataPoolService {

    /**
     * currentIndicatorValueMap:
     * <p>当前采集值的 {@link Map}
     *
     * @since   v1.01
     */
    private Map<String, IndicatorValue> currentIndicatorValueMap = new Hashtable<String, IndicatorValue>();

    /**
     * lastIndicatorValueMap:
     * <p>上次采集值的 {@link Map}
     *
     * @since   v1.01
     */
    private Map<String, IndicatorValue> lastIndicatorValueMap = new Hashtable<String, IndicatorValue>();

    /**
     * gatherService:
     * <p>所属的采集服务
     *
     * @since   v1.01
     */
    private GatherService gatherService;

    /**
     * GatherDataPoolService.java:
     * 采集数据池服务构造方法
     *
     * @param   gatherService
     *          - 所属的采集机服务
     *
     * @since   v1.01
     */
    public GatherDataPoolService(GatherService gatherService) {
       this.gatherService = gatherService;
    }

    /**
     * updateIndicatorValue:
     * <p>更新采集指标值，将本次采集设置为当前采集值，
     * 将原当前采集值设置为上次采集值，同时删除原上次采集值。
     *
     * @param   indicatorId
     *          - 指标 Id
     * @param   indicatorValue
     *          - 本次采集指标值
     *
     * @since   v1.01
     */
    public void updateIndicatorValue(String indicatorId, IndicatorValue indicatorValue) {
        IndicatorValue lastIndicatorValue = currentIndicatorValueMap.get(indicatorId);
        lastIndicatorValueMap.remove(indicatorId);
        if (lastIndicatorValue != null) {
            lastIndicatorValueMap.put(indicatorId, lastIndicatorValue);
        }
        currentIndicatorValueMap.put(indicatorId, indicatorValue);
    }

    /**
     * getCurrentIndicatorValueMap:
     * <p>
     *
     * @return  Map<String,IndicatorValue>
     *          -
     * @since   v1.01
     */
    public Map<String, IndicatorValue> getCurrentIndicatorValueMap() {
        return currentIndicatorValueMap;
    }

    /**
     * setCurrentIndicatorValueMap:
     * <p>
     *
     * @param   currentIndicatorValueMap
     *          -
     * @since   v1.01
     */
    public void setCurrentIndicatorValueMap(
                    Map<String, IndicatorValue> currentIndicatorValueMap) {
        this.currentIndicatorValueMap = currentIndicatorValueMap;
    }

    /**
     * getLastIndicatorValueMap:
     * <p>
     *
     * @return  Map<String,IndicatorValue>
     *          -
     * @since   v1.01
     */
    public Map<String, IndicatorValue> getLastIndicatorValueMap() {
        return lastIndicatorValueMap;
    }

    /**
     * setLastIndicatorValueMap:
     * <p>
     *
     * @param   lastIndicatorValueMap
     *          -
     * @since   v1.01
     */
    public void setLastIndicatorValueMap(
                    Map<String, IndicatorValue> lastIndicatorValueMap) {
        this.lastIndicatorValueMap = lastIndicatorValueMap;
    }

    /**
     * getGatherService:
     * <p>
     *
     * @return  GatherService
     *          -
     * @since   v1.01
     */
    public GatherService getGatherService() {
        return gatherService;
    }

    /**
     * setGatherService:
     * <p>
     *
     * @param   gatherService
     *          -
     * @since   v1.01
     */
    public void setGatherService(GatherService gatherService) {
        this.gatherService = gatherService;
    }

}

