/*
 * @(#)ManagerParamter.java     v1.01, 2012-5-8
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.flex.chart;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * ClassName:   FlexChartParameter.java
 * <p> {@link FlexChartParameter} ���ڱ����� <code>Flex</code> ͼ����ǰ̨�Ĳ�����
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2012-5-8 ����01:51:34
 */
public class FlexChartParameter implements Serializable {

    // Fields


    /**
     * serialVersionUID:
     * <p>���л�ID
     *
     * @since   v1.01
     */
    private static final long serialVersionUID = -839085260532464818L;

    /**
     * parameterHashtable:
     * <p> {@link Hashtable} ���͵� <code>parameterHashtable</code>
     * �������ڱ��� <code>Flex</code> ͼ����ǰ̨�Ĳ���
     *
     * @since   v1.01
     */
    private Hashtable<String, Object> parameterHashtable;


    // Constructs

    /**
     * FlexChartParameter:
     * <p>Ĭ�ϵĹ��췽��������һ�� {@link FlexChartParameter} ʵ����
     *
     * @since   v1.01
     */
    public FlexChartParameter() {
        this.setParameterHashtable(new Hashtable<String, Object>());
    }


    // Getter And Setter

    /**
     * getParameterHashtable:
     * <p>��ȡ {@link #parameterHashtable} ���÷���Ϊ˽�е�ֻ���ڱ��ࡣ
     *
     * @return  {@link Hashtable}
     *          - {@link Hashtable} ���͵� {@link #parameterHashtable} ����
     * @since   v1.01
     */
    private Hashtable<String, Object> getParameterHashtable() {
        return parameterHashtable;
    }


    /**
     * setParameterHashtable:
     * <p>���� {@link #parameterHashtable} ���÷���Ϊ˽�е�ֻ���ڱ��ࡣ
     *
     * @param   parameterHashtable
     *          - {@link Hashtable} ���͵� {@link #parameterHashtable} ����
     * @since   v1.01
     */
    private void setParameterHashtable(
                    Hashtable<String, Object> parameterHashtable) {
        this.parameterHashtable = parameterHashtable;
    }


    // Method

    /**
     * getParameter:
     * <p>��ȡָ������Ϊ <code>name</code> �Ĳ�����
     *
     * @param   name
     *          - ָ���Ĳ�������
     * @return  {@link Object}
     *          -   {@link Object} ���͵Ĳ�����
     * @since   v1.01
     */
    public Object getParameter(String name) {
        return getParameterHashtable().get(name);
    }

    /**
     * getParameterNames:
     * <p>��ȡ�������Ƶ�ö�� {@link Enumeration<String>}</p>
     *
     * @return  {@link Enumeration<String>}
     *          - �������Ƶ�ö�� {@link Enumeration<String>}
     * @since   v1.01
     */
    public Enumeration<String> getParameterNames() {
        return getParameterHashtable().keys();
    }

    /**
     * setParameter:
     * <p>��������Ϊ <code>name</code> �Ĳ��� <code>parameter</code> ��
     *
     * @param   name
     *          - {@link String} ���͵Ĳ�������
     * @param   parameter
     *          - {@link Object} ���͵Ĳ�������
     *
     * @since   v1.01
     */
    public void setParameter(String name, Object parameter) {
        getParameterHashtable().put(name, parameter);
    }
}

