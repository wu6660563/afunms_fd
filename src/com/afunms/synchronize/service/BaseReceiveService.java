/*
 * @(#)BaseReceiveService.java     v1.01, 2013 12 24
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.synchronize.service;

import java.util.Properties;

/**
 * ClassName:   BaseReceiveService.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 24 11:01:43
 */
public abstract class BaseReceiveService implements ReceiveService {

    /**
     * properties:
     * <p>属性配置
     *
     * @since   v1.01
     */
    private Properties properties;

    /**
     * receiveTimes:
     * <p>接受次数
     *
     * @since   v1.01
     */
    protected long receiveTimes;

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
     * getReceiveTimes:
     * <p>
     *
     * @return  long
     *          -
     * @since   v1.01
     */
    public long getReceiveTimes() {
        return receiveTimes;
    }

    /**
     * setReceiveTimes:
     * <p>
     *
     * @param   receiveTimes
     *          -
     * @since   v1.01
     */
    public void addReceiveTimes() {
        if (receiveTimes <= Long.MAX_VALUE) {
            receiveTimes = 0;
        }
        receiveTimes ++;
    }

}

