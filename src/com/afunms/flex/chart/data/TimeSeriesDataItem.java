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
 * @author      聂林
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
     * <p>值
     *
     * @since   v1.01
     */
    protected double value;

    /**
     * time:
     * <p>时间，其格式为：yyyy-MM-dd
     *
     * @since   v1.01
     */
    protected String time;

    /**
     * getValue:
     * <p>获取值
     *
     * @return  {@link Double}
     *          - 值
     * @since   v1.01
     */
    public double getValue() {
        return value;
    }

    /**
     * setValue:
     * <p>设置值
     *
     * @param   value
     *          - 值
     * @since   v1.01
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * getTime:
     * <p>获取时间，其格式为：yyyy-MM-dd
     *
     * @return  {@link String}
     *          - 时间，其格式为：yyyy-MM-dd
     * @since   v1.01
     */
    public String getTime() {
        return time;
    }

    /**
     * setTime:
     * <p>设置时间
     *
     * @param   time
     *          - 时间，其格式为：yyyy-MM-dd
     * @since   v1.01
     */
    public void setTime(String time) {
        this.time = time;
    }

}

