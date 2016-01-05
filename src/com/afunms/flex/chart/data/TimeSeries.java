/*
 * @(#)TimeSeries.java     v1.01, May 17, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.flex.chart.data;

/**
 * ClassName:   TimeSeries.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        May 17, 2013 12:01:24 PM
 */
public class TimeSeries extends Series<TimeSeriesDataItem> {

    /**
     * serialVersionUID:
     * <p>序列化 ID
     *
     * @since   v1.01
     */
    private static final long serialVersionUID = 2024417723300944339L;

    /**
     * maxValue:
     * <p>最大值
     *
     * @since   v1.01
     */
    private double maxValue;

    /**
     * maxValueTime:
     * <p>最大值时间
     *
     * @since   v1.01
     */
    private String maxValueTime;

    /**
     * minValue:
     * <p>最小值
     *
     * @since   v1.01
     */
    private double minValue;

    /**
     * minValueTime:
     * <p>最小值时间
     *
     * @since   v1.01
     */
    private String minValueTime;

    /**
     * curValue:
     * <p>最新值
     *
     * @since   v1.01
     */
    private double curValue;

    /**
     * curValueTime:
     * <p>当前值的时间
     *
     * @since   v1.01
     */
    private String curValueTime;

    /**
     * avgValue:
     * <p>平均值
     *
     * @since   v1.01
     */
    private double avgValue;

    /**
     * addOrUpdate:
     *
     * @param dataItem
     *
     * @since   v1.01
     * @see com.afunms.flex.chart.data.Series#addOrUpdate(com.afunms.flex.chart.data.SeriesDataItem)
     */
    @Override
    public void addOrUpdate(TimeSeriesDataItem dataItem) {
        dataItemList.add(dataItem);
    }

    /**
     * getItemCount:
     *
     * @return
     *
     * @since   v1.01
     * @see com.afunms.flex.chart.data.Series#getItemCount()
     */
    @Override
    public int getItemCount() {
        return dataItemList.size();
    }

    /**
     * isEmpty:
     *
     * @return
     *
     * @since   v1.01
     * @see com.afunms.flex.chart.data.Series#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return dataItemList.isEmpty();
    }

    /**
     * getMaxValue:
     * <p>
     *
     * @return  double
     *          -
     * @since   v1.01
     */
    public double getMaxValue() {
        return maxValue;
    }

    /**
     * setMaxValue:
     * <p>
     *
     * @param   maxValue
     *          -
     * @since   v1.01
     */
    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * getMaxValueTime:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getMaxValueTime() {
        return maxValueTime;
    }

    /**
     * setMaxValueTime:
     * <p>
     *
     * @param   maxValueTime
     *          -
     * @since   v1.01
     */
    public void setMaxValueTime(String maxValueTime) {
        this.maxValueTime = maxValueTime;
    }

    /**
     * getMinValue:
     * <p>
     *
     * @return  double
     *          -
     * @since   v1.01
     */
    public double getMinValue() {
        return minValue;
    }

    /**
     * setMinValue:
     * <p>
     *
     * @param   minValue
     *          -
     * @since   v1.01
     */
    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    /**
     * getMinValueTime:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getMinValueTime() {
        return minValueTime;
    }

    /**
     * setMinValueTime:
     * <p>
     *
     * @param   minValueTime
     *          -
     * @since   v1.01
     */
    public void setMinValueTime(String minValueTime) {
        this.minValueTime = minValueTime;
    }

    /**
     * getCurValue:
     * <p>
     *
     * @return  double
     *          -
     * @since   v1.01
     */
    public double getCurValue() {
        return curValue;
    }

    /**
     * setCurValue:
     * <p>
     *
     * @param   curValue
     *          -
     * @since   v1.01
     */
    public void setCurValue(double curValue) {
        this.curValue = curValue;
    }

    /**
     * getCurValueTime:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getCurValueTime() {
        return curValueTime;
    }

    /**
     * setCurValueTime:
     * <p>
     *
     * @param   curValueTime
     *          -
     * @since   v1.01
     */
    public void setCurValueTime(String curValueTime) {
        this.curValueTime = curValueTime;
    }

    /**
     * getAvgValue:
     * <p>
     *
     * @return  double
     *          -
     * @since   v1.01
     */
    public double getAvgValue() {
        return avgValue;
    }

    /**
     * setAvgValue:
     * <p>
     *
     * @param   avgValue
     *          -
     * @since   v1.01
     */
    public void setAvgValue(double avgValue) {
        this.avgValue = avgValue;
    }

}

