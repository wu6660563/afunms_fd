/*
 * @(#)FlexChartService.java     v1.01, May 16, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.flex.chart;

import com.afunms.flex.chart.data.TimeGraphFlexChart;
import com.afunms.flex.chart.service.TimeGraphFlexChartService;

/**
 * ClassName:   FlexChartService.java
 * <p>
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        May 16, 2013 7:08:46 PM
 */
public class FlexChartService {

    /**
     * getTimeGraphFlexChart:
     * <p>��ȡʱ�����ߵ� Flex ͼ��
     *
     * @param   action
     *          - ִ�е� action
     * @return  {@link TimeGraphFlexChart}
     *          - ʱ�����ߵ� Flex ͼ��
     *
     * @since   v1.01
     */
    public TimeGraphFlexChart getTimeGraphFlexChart(String[] parameterNames, String[] parameteValues) {
        TimeGraphFlexChartService service = new TimeGraphFlexChartService();
        TimeGraphFlexChart chart = service.execute(createFlexChartParameter(parameterNames, parameteValues));
        return chart;
    }

    public FlexChartParameter createFlexChartParameter(String[] parameterNames, String[] parameteValues) {
        FlexChartParameter parameter = new FlexChartParameter();
        for (int i = 0; i < parameterNames.length; i++) {
            parameter.setParameter(parameterNames[i], parameteValues[i]);
        }
        return parameter;
    }
}

