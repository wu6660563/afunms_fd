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
 * <p> {@link FlexChartParameter} 用于保存由 <code>Flex</code> 图表中前台的参数。
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2012-5-8 下午01:51:34
 */
public class FlexChartParameter implements Serializable {

    // Fields


    /**
     * serialVersionUID:
     * <p>序列化ID
     *
     * @since   v1.01
     */
    private static final long serialVersionUID = -839085260532464818L;

    /**
     * parameterHashtable:
     * <p> {@link Hashtable} 类型的 <code>parameterHashtable</code>
     * 对象，用于保存 <code>Flex</code> 图表中前台的参数
     *
     * @since   v1.01
     */
    private Hashtable<String, Object> parameterHashtable;


    // Constructs

    /**
     * FlexChartParameter:
     * <p>默认的构造方法，创建一个 {@link FlexChartParameter} 实例。
     *
     * @since   v1.01
     */
    public FlexChartParameter() {
        this.setParameterHashtable(new Hashtable<String, Object>());
    }


    // Getter And Setter

    /**
     * getParameterHashtable:
     * <p>获取 {@link #parameterHashtable} ，该方法为私有的只用于本类。
     *
     * @return  {@link Hashtable}
     *          - {@link Hashtable} 类型的 {@link #parameterHashtable} 对象
     * @since   v1.01
     */
    private Hashtable<String, Object> getParameterHashtable() {
        return parameterHashtable;
    }


    /**
     * setParameterHashtable:
     * <p>设置 {@link #parameterHashtable} ，该方法为私有的只用于本类。
     *
     * @param   parameterHashtable
     *          - {@link Hashtable} 类型的 {@link #parameterHashtable} 对象
     * @since   v1.01
     */
    private void setParameterHashtable(
                    Hashtable<String, Object> parameterHashtable) {
        this.parameterHashtable = parameterHashtable;
    }


    // Method

    /**
     * getParameter:
     * <p>获取指定名称为 <code>name</code> 的参数。
     *
     * @param   name
     *          - 指定的参数名称
     * @return  {@link Object}
     *          -   {@link Object} 类型的参数。
     * @since   v1.01
     */
    public Object getParameter(String name) {
        return getParameterHashtable().get(name);
    }

    /**
     * getParameterNames:
     * <p>获取参数名称的枚举 {@link Enumeration<String>}</p>
     *
     * @return  {@link Enumeration<String>}
     *          - 参数名称的枚举 {@link Enumeration<String>}
     * @since   v1.01
     */
    public Enumeration<String> getParameterNames() {
        return getParameterHashtable().keys();
    }

    /**
     * setParameter:
     * <p>设置名称为 <code>name</code> 的参数 <code>parameter</code> 。
     *
     * @param   name
     *          - {@link String} 类型的参数名称
     * @param   parameter
     *          - {@link Object} 类型的参数对象
     *
     * @since   v1.01
     */
    public void setParameter(String name, Object parameter) {
        getParameterHashtable().put(name, parameter);
    }
}

