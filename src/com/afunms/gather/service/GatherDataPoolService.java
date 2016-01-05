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
 * <p>�ɼ����ݳط������вɼ���ɺ�����ݶ���洢������
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 3 16:30:39
 */
public class GatherDataPoolService {

    /**
     * currentIndicatorValueMap:
     * <p>��ǰ�ɼ�ֵ�� {@link Map}
     *
     * @since   v1.01
     */
    private Map<String, IndicatorValue> currentIndicatorValueMap = new Hashtable<String, IndicatorValue>();

    /**
     * lastIndicatorValueMap:
     * <p>�ϴβɼ�ֵ�� {@link Map}
     *
     * @since   v1.01
     */
    private Map<String, IndicatorValue> lastIndicatorValueMap = new Hashtable<String, IndicatorValue>();

    /**
     * gatherService:
     * <p>�����Ĳɼ�����
     *
     * @since   v1.01
     */
    private GatherService gatherService;

    /**
     * GatherDataPoolService.java:
     * �ɼ����ݳط����췽��
     *
     * @param   gatherService
     *          - �����Ĳɼ�������
     *
     * @since   v1.01
     */
    public GatherDataPoolService(GatherService gatherService) {
       this.gatherService = gatherService;
    }

    /**
     * updateIndicatorValue:
     * <p>���²ɼ�ָ��ֵ�������βɼ�����Ϊ��ǰ�ɼ�ֵ��
     * ��ԭ��ǰ�ɼ�ֵ����Ϊ�ϴβɼ�ֵ��ͬʱɾ��ԭ�ϴβɼ�ֵ��
     *
     * @param   indicatorId
     *          - ָ�� Id
     * @param   indicatorValue
     *          - ���βɼ�ָ��ֵ
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

