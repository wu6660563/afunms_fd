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
 * <p>{@link GraphFlexChart} <code>Flex</code>ͼ�� {@link FlexChart} ����ͼ�Ķ����࣬
 * ����һЩ����ͼ�Ļ����ֶΣ�ͬʱ�����ڶ�����ͼ��
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        May 16, 2013 11:03:08 PM
 */
public class GraphFlexChart<D extends SeriesDataItem,
                            S extends Series<D>>
                extends BaseFlexChart {

    /**
     * serialVersionUID:
     * <p>���л� ID
     *
     * @since   v1.01
     */
    private static final long serialVersionUID = 8678585758263244821L;

    /**
     * ordinateUnit:
     * <p><code>Flex</code> ����ͼ�������굥λ
     *
     * @since   v1.01
     */
    protected String ordinateUnit;

    /**
     * list:
     * <p>����ͼ�������б�������Ӷ������
     *
     * @since   v1.01
     */
    protected List<S> seriesList;

    
    /**
     * GraphFlexChart:
     * ����һ�� {@link GraphFlexChart} ʵ��
     *
     * @since   v1.01
     */
    public GraphFlexChart() {
        super();
        seriesList = new ArrayList<S>();
    }

    /**
     * getOrdinateUnit:
     * <p>��ȡ <code>Flex</code> ����ͼ�������굥λ
     *
     * @return  {@link String}
     *          - <code>Flex</code> ����ͼ�������굥λ
     * @since   v1.01
     */
    public String getOrdinateUnit() {
        return ordinateUnit;
    }

    /**
     * setOrdinateUnit:
     * <p>���� <code>Flex</code> ����ͼ�������굥λ
     *
     * @param   ordinateUnit
     *          - <code>Flex</code> ����ͼ�������굥λ
     * @since   v1.01
     */
    public void setOrdinateUnit(String ordinateUnit) {
        this.ordinateUnit = ordinateUnit;
    }

    /**
     * addSeries:
     * <p>�������
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

