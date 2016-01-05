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
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 6 16:24:22
 */
public class IndicatorValueBuffer {

    /**
     * logger:
     * <p>��־
     *
     * @since   v1.01
     */
    private static SysLogger logger = SysLogger.getLogger(IndicatorValueBuffer.class);

    /**
     * indicatorValueActionService:
     * <p>�ɼ�ֵ�������
     *
     * @since   v1.01
     */
    private IndicatorValueActionService indicatorValueActionService;

    /**
     * indicatorValueBuffer:
     * <p>�ɼ�ֵ������
     *
     * @since   v1.01
     */
    private Vector<IndicatorValue> indicatorValueBuffer = null;

    /**
     * IndicatorValueBuffer.java:
     * ���췽��
     * @param   indicatorValueActionService
     *          - �������
     *
     * @since   v1.01
     */
    public IndicatorValueBuffer(IndicatorValueActionService indicatorValueActionService) {
        this.indicatorValueActionService = indicatorValueActionService;
        indicatorValueBuffer = new Vector<IndicatorValue>();
    }
    
    /**
     * addIndicatorValue:
     * <p>�򻺳����м���һ����Ҫ��ִ�еĲɼ�ָ��ֵ
     *
     * @param   indicatorValue
     *          - �ɼ�ָ��ֵ
     *
     * @since   v1.01
     */
    public void addIndicatorValue(IndicatorValue indicatorValue) {
        this.indicatorValueBuffer.add(indicatorValue);
    }

    /**
     * popIndicatorValue:
     * <p>�ӻ������е���һ����һ����Ҫִ�еĲɼ�ֵ
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
                logger.debug("��ǰ������Ϊ��");
            }
        }
        return indicatorValue;
    }

    /**
     * isEmpty:
     * <p>�жϵ�ǰ�������Ƿ�Ϊ��
     *
     * @return  {@link Boolean}
     *          - ��ǰ������Ϊ����Ϊ true ; ����Ϊ false
     *
     * @since   v1.01
     */
    public synchronized boolean isEmpty() {
       return this.indicatorValueBuffer.isEmpty();
    }

    /**
     * size:
     * <p>��ȡ��������ǰ��Ҫִ�е�������
     *
     * @return  {@link Integer}
     *          - ����������Ҫִ�е�������
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

