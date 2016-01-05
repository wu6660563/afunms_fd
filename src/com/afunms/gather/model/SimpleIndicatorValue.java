/*
 * @(#)SimpleIndicatorValue.java     v1.01, 2013 12 27
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.model;

/**
 * ClassName:   SimpleIndicatorValue.java
 * <p>简单的指标值，只包含 {@link Object} 类型的采集值以及 {@link Integer} 类型的错误码
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 27 00:05:02
 */
public class SimpleIndicatorValue {

    /**
     * value:
     * <p>采集值
     *
     * @since   v1.01
     */
    private Object value;

    /**
     * errorCode:
     * <p>错误码，该字段用于以后扩展采集错误原因等
     *
     * @since   v1.01
     */
    private int errorCode;

    /**
     * SimpleIndicatorValue.java:
     * <p>默认的构造方法
     *
     * @since   v1.01
     */
    public SimpleIndicatorValue() {
    }

    /**
     * SimpleIndicatorValue.java:
     * <p>带有参数的构造方法
     *
     * @param   value
     *          - 采集值
     * @param   errorCode
     *          - 错误码
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

