/*
 * @(#)BaseSendService.java     v1.01, 2013 12 23
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.synchronize.service;

import java.util.Properties;


/**
 * ClassName:   BaseSendService.java
 * <p>基础的发送服务，其他类型的发送应该继承自该类
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 23 14:35:43
 */
public abstract class BaseSendService implements SendService {

    /**
     * properties:
     * <p>配置
     *
     * @since   v1.01
     */
    private Properties properties;

    /**
     * name:
     * <p>名称
     *
     * @since   v1.01
     */
    protected String name;

    /**
     * isClosed:
     * <p>是否被关闭
     *
     * @since   v1.01
     */
    private boolean isClosed;

    /**
     * BaseSendService.java:
     * <p>无参数的构造方法
     *
     * @param   properties
     *          - 配置
     *
     * @since   v1.01
     */
    public BaseSendService() {
    }

    /**
     * BaseSendService.java:
     * <p>带有配置参数的构造方法
     *
     * @param   properties
     *          - 配置
     *
     * @since   v1.01
     */
    public BaseSendService(Properties properties) {
        setProperties(properties);
    }

    /**
     * getProperties:
     * <p>
     *
     * @return  Properties
     *          -
     * @since   v1.01
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * setProperties:
     * <p>
     *
     * @param   properties
     *          -
     * @since   v1.01
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * isClosed:
     * <p>
     *
     * @return  boolean
     *          -
     * @since   v1.01
     */
    public boolean isClosed() {
        return isClosed;
    }

    /**
     * setClosed:
     * <p>
     *
     * @param   isClosed
     *          -
     * @since   v1.01
     */
    public void setClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

    /**
     * getName:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getName() {
        if (this.name == null) {
            String name = null;
            if (getProperties() != null) {
                name = getProperties().getProperty("name");
            }
            if (name != null && name.trim().length() > 0) {
                setName(name);
            } else {
                setName(getClass().getName());
            }
        }
        return this.name;
    }

    /**
     * setName:
     * <p>
     *
     * @param   name
     *          -
     * @since   v1.01
     */
    public void setName(String name) {
        this.name = name;
    }

}

