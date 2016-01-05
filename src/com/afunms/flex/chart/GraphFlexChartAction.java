/*
 * @(#)GraphFlexChartAction.java     v1.01, May 16, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.flex.chart;

import com.afunms.flex.chart.data.GraphFlexChart;
import com.afunms.flex.chart.data.Series;
import com.afunms.flex.chart.data.SeriesDataItem;

/**
 * ClassName:   GraphFlexChartAction.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        May 16, 2013 11:32:51 PM
 */
public interface GraphFlexChartAction<D extends SeriesDataItem,
                                    S extends Series<D>,
                                    G extends GraphFlexChart<D, S>> extends FlexChartAction {

    /**
     * getGraphFlexChart:
     * <p>获取 <code>Flex</code> 曲线图
     *
     * @param   method
     *          - 处理方法名
     * @param   parameter
     *          - 参数
     * @return  {@link GraphFlexChart}
     *          - <code>Flex</code> 曲线图
     *
     * @since   v1.01
     */
    public G getGraphFlexChart(String method, FlexChartParameter parameter);
}

