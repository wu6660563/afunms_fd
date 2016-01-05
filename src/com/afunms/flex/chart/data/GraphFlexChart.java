/*
 * @(#)GraphFlexChart.java     v1.01, May 16, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.flex.chart.data;

import java.util.ArrayList;
import java.util.List;

import com.afunms.flex.chart.FlexChart;
import com.afunms.flex.chart.base.BaseFlexChart;

/**
 * ClassName:   GraphFlexChart.java
 * <p>{@link GraphFlexChart} <code>Flex</code>图表 {@link FlexChart} 曲线图的顶级类，
 * 包含一些曲线图的基本字段，同时适用于多曲线图。
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        May 16, 2013 11:03:08 PM
 */
public class GraphFlexChart<D extends SeriesDataItem,
                            S extends Series<D>>
                extends BaseFlexChart {

    /**
     * serialVersionUID:
     * <p>序列化 ID
     *
     * @since   v1.01
     */
    private static final long serialVersionUID = 8678585758263244821L;

    /**
     * ordinateUnit:
     * <p><code>Flex</code> 曲线图的纵坐标单位
     *
     * @since   v1.01
     */
    protected String ordinateUnit;

    /**
     * list:
     * <p>曲线图的序列列表，可以添加多个序列
     *
     * @since   v1.01
     */
    protected List<S> seriesList;

    
    /**
     * GraphFlexChart:
     * 构造一个 {@link GraphFlexChart} 实例
     *
     * @since   v1.01
     */
    public GraphFlexChart() {
        super();
        seriesList = new ArrayList<S>();
    }

    /**
     * getOrdinateUnit:
     * <p>获取 <code>Flex</code> 曲线图的纵坐标单位
     *
     * @return  {@link String}
     *          - <code>Flex</code> 曲线图的纵坐标单位
     * @since   v1.01
     */
    public String getOrdinateUnit() {
        return ordinateUnit;
    }

    /**
     * setOrdinateUnit:
     * <p>设置 <code>Flex</code> 曲线图的纵坐标单位
     *
     * @param   ordinateUnit
     *          - <code>Flex</code> 曲线图的纵坐标单位
     * @since   v1.01
     */
    public void setOrdinateUnit(String ordinateUnit) {
        this.ordinateUnit = ordinateUnit;
    }

    /**
     * addSeries:
     * <p>添加序列
     *
     * @since   v1.01
     */
    public void addSeries(S series) {
        seriesList.add(series);
    }

    /**
     * getSeriesList:
     * <p>
     *
     * @return  List<S>
     *          -
     * @since   v1.01
     */
    public List<S> getSeriesList() {
        return seriesList;
    }

    /**
     * setSeriesList:
     * <p>
     *
     * @param   seriesList
     *          -
     * @since   v1.01
     */
    public void setSeriesList(List<S> seriesList) {
        this.seriesList = seriesList;
    }

}

