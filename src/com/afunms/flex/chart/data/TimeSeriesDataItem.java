/*
 * @(#)TimeSeriesDataItem.java     v1.01, May 17, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.flex.chart.data;

/**
 * ClassName:   TimeSeriesDataItem.java
 * <p>
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        May 17, 2013 1:02:43 PM
 */
public class TimeSeriesDataItem extends SeriesDataItem {

    /**
     * serialVersionUID:
     * <p>
     *
     * @since   v1.01
     */
    private static final long serialVersionUID = -5309046452019860347L;

    /**
     * value:
     * <p>ֵ
     *
     * @since   v1.01
     */
    protected double value;

    /**
     * time:
     * <p>ʱ�䣬���ʽΪ��yyyy-MM-dd
     *
     * @since   v1.01
     */
    protected String time;

    /**
     * getValue:
     * <p>��ȡֵ
     *
     * @return  {@link Double}
     *          - ֵ
     * @since   v1.01
     */
    public double getValue() {
        return value;
    }

    /**
     * setValue:
     * <p>����ֵ
     *
     * @param   value
     *          - ֵ
     * @since   v1.01
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * getTime:
     * <p>��ȡʱ�䣬���ʽΪ��yyyy-MM-dd
     *
     * @return  {@link String}
     *          - ʱ�䣬���ʽΪ��yyyy-MM-dd
     * @since   v1.01
     */
    public String getTime() {
        return time;
    }

    /**
     * setTime:
     * <p>����ʱ��
     *
     * @param   time
     *          - ʱ�䣬���ʽΪ��yyyy-MM-dd
     * @since   v1.01
     */
    public void setTime(String time) {
        this.time = time;
    }

}

