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
 * <p>{@link BaseFlexChart} <code>Flex</code>ͼ�� {@link FlexChart} �����࣬
 * ����һЩ�����ֶΣ�����⡢���͵ȡ�
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        May 16, 2013 10:44:56 PM
 */
public class BaseFlexChart extends BaseVo implements FlexChart {

    /**
     * serialVersionUID:
     * <p>���л� ID
     *
     * @since   v1.01
     */
    private static final long serialVersionUID = 6354402028081782437L;

    /**
     * title:
     * <p><code>Flex</code> ͼ��ı���
     *
     * @since   v1.01
     */
    protected String title;

    /**
     * type:
     * <p><code>Flex</code> ͼ������ͣ���������ͼ����ͼ����״ͼ��...
     *
     * @since   v1.01
     */
    protected String type;

    /**
     * getTitle:
     * <p>��ȡ <code>Flex</code> ͼ��ı���
     *
     * @return  String
     *          - <code>Flex</code> ͼ��ı���
     * @since   v1.01
     */
    public String getTitle() {
        return title;
    }

    /**
     * setTitle:
     * <p>���� <code>Flex</code> ͼ��ı���
     *
     * @param   title
     *          - <code>Flex</code> ͼ��ı���
     * @since   v1.01
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * getType:
     * <p>��ȡ <code>Flex</code> ͼ������ͣ���������ͼ����ͼ����״ͼ��...
     *
     * @return  String
     *          - <code>Flex</code> ͼ������ͣ���������ͼ����ͼ����״ͼ��...
     * @since   v1.01
     */
    public String getType() {
        return type;
    }

    /**
     * setType:
     * <p>���� <code>Flex</code> ͼ������ͣ���������ͼ����ͼ����״ͼ��...
     *
     * @param   type
     *          - <code>Flex</code> ͼ������ͣ���������ͼ����ͼ����״ͼ��...
     * @since   v1.01
     */
    public void setType(String type) {
        this.type = type;
    }

}

