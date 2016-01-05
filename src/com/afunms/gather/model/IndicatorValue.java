/*
 * @(#)IndicatorValue.java     v1.01, 2013 12 26
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.model;

import com.afunms.common.base.BaseVo;

/**
 * ClassName:   IndicatorValue.java
 * <p>{@link IndicatorValue} ָ��ֵ���ɼ�����ָ��ɼ���ɺ󽫸�ֵ���ز���������Ͷ����С�
 * ��ֵ������{@link IndicatorInfo} �ɼ���Ϣ�࣬
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 26 23:15:22
 */
public class IndicatorValue extends BaseVo {

    /**
     * serialVersionUID:
     * <p>
     *
     * @since   v1.01
     */
    private static final long serialVersionUID = -5340473711792039263L;

    /**
     * indicatorInfo:
     * <p>�ɼ���Ϣ��
     *
     * @since   v1.01
     */
    private IndicatorInfo indicatorInfo;

    /**
     * value:
     * <p>�ɼ�ֵ
     *
     * @since   v1.01
     */
    private Object value;

    /**
     * time:
     * <p>�ɼ�ʱ��
     *
     * @since   v1.01
     */
    private long time;

    /**
     * errorCode:
     * <p>�����룬���ֶ������Ժ���չ�ɼ�����ԭ���
     *
     * @since   v1.01
     */
    private int errorCode;

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
     * getValue:
     * <p>
     *
     * @return  Object
     *          -
     * @since   v1.01
     */
    public Object getValue() {
        return value;
    }

    /**
     * setValue:
     * <p>
     *
     * @param   value
     *          -
     * @since   v1.01
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * getTime:
     * <p>
     *
     * @return  long
     *          -
     * @since   v1.01
     */
    public long getTime() {
        return time;
    }

    /**
     * setTime:
     * <p>
     *
     * @param   time
     *          -
     * @since   v1.01
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * getErrorCode:
     * <p>
     *
     * @return  int
     *          -
     * @since   v1.01
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * setErrorCode:
     * <p>
     *
     * @param   errorCode
     *          -
     * @since   v1.01
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}

