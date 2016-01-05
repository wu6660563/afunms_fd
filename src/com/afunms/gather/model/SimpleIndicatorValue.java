/*
 * @(#)SimpleIndicatorValue.java     v1.01, 2013 12 27
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.model;

/**
 * ClassName:   SimpleIndicatorValue.java
 * <p>�򵥵�ָ��ֵ��ֻ���� {@link Object} ���͵Ĳɼ�ֵ�Լ� {@link Integer} ���͵Ĵ�����
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 27 00:05:02
 */
public class SimpleIndicatorValue {

    /**
     * value:
     * <p>�ɼ�ֵ
     *
     * @since   v1.01
     */
    private Object value;

    /**
     * errorCode:
     * <p>�����룬���ֶ������Ժ���չ�ɼ�����ԭ���
     *
     * @since   v1.01
     */
    private int errorCode;

    /**
     * SimpleIndicatorValue.java:
     * <p>Ĭ�ϵĹ��췽��
     *
     * @since   v1.01
     */
    public SimpleIndicatorValue() {
    }

    /**
     * SimpleIndicatorValue.java:
     * <p>���в����Ĺ��췽��
     *
     * @param   value
     *          - �ɼ�ֵ
     * @param   errorCode
     *          - ������
     *
     * @since   v1.01
     */
    public SimpleIndicatorValue(Object value, int errorCode) {
        this.value = value;
        this.errorCode = errorCode;
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

