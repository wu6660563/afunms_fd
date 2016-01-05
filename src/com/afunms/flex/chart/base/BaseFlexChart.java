/*
 * @(#)BaseFlexChart.java     v1.01, May 16, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.flex.chart.base;

import com.afunms.common.base.BaseVo;
import com.afunms.flex.chart.FlexChart;

/**
 * ClassName:   BaseFlexChart.java
 * <p>{@link BaseFlexChart} <code>Flex</code>图表 {@link FlexChart} 基础类，
 * 包含一些基本字段，如标题、类型等。
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        May 16, 2013 10:44:56 PM
 */
public class BaseFlexChart extends BaseVo implements FlexChart {

    /**
     * serialVersionUID:
     * <p>序列化 ID
     *
     * @since   v1.01
     */
    private static final long serialVersionUID = 6354402028081782437L;

    /**
     * title:
     * <p><code>Flex</code> 图表的标题
     *
     * @since   v1.01
     */
    protected String title;

    /**
     * type:
     * <p><code>Flex</code> 图表的类型，包含曲线图，饼图，柱状图等...
     *
     * @since   v1.01
     */
    protected String type;

    /**
     * getTitle:
     * <p>获取 <code>Flex</code> 图表的标题
     *
     * @return  String
     *          - <code>Flex</code> 图表的标题
     * @since   v1.01
     */
    public String getTitle() {
        return title;
    }

    /**
     * setTitle:
     * <p>设置 <code>Flex</code> 图表的标题
     *
     * @param   title
     *          - <code>Flex</code> 图表的标题
     * @since   v1.01
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * getType:
     * <p>获取 <code>Flex</code> 图表的类型，包含曲线图，饼图，柱状图等...
     *
     * @return  String
     *          - <code>Flex</code> 图表的类型，包含曲线图，饼图，柱状图等...
     * @since   v1.01
     */
    public String getType() {
        return type;
    }

    /**
     * setType:
     * <p>设置 <code>Flex</code> 图表的类型，包含曲线图，饼图，柱状图等...
     *
     * @param   type
     *          - <code>Flex</code> 图表的类型，包含曲线图，饼图，柱状图等...
     * @since   v1.01
     */
    public void setType(String type) {
        this.type = type;
    }

}

