/*
 * @(#)URLPerformanceInfo.java     v1.01, May 30, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.model;

import java.io.Serializable;

import com.afunms.common.base.BaseVo;

/**
 * ClassName:   URLPerformanceInfo.java
 * <p>{@link URLPerformanceInfo} <code>URL</code> 性能信息
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        May 30, 2013 10:27:28 AM
 */
public class URLPerformanceInfo extends BaseVo implements Serializable {

    /**
     * serialVersionUID:
     * <p>序列化 Id
     *
     * @since   v1.01
     */
    private static final long serialVersionUID = 7319812168850595004L;

    /**
     * id:
     * <p>id
     *
     * @since   v1.01
     */
    private int id;

    /**
     * connectUtilization:
     * <p>连通率
     *
     * @since   v1.01
     */
    private int connectUtilization;

    /**
     * responseTime:
     * <p>响应时间
     *
     * @since   v1.01
     */
    private long responseTime;

    /**
     * pageSize:
     * <p>页面大小
     *
     * @since   v1.01
     */
    private int pageSize;

    /**
     * changeRate:
     * <p>修改率
     *
     * @since   v1.01
     */
    private int changeRate;

    /**
     * collecttime:
     * <p>采集时间
     *
     * @since   v1.01
     */
    private String collecttime;

    /**
     * getId:
     * <p>获取 id
     *
     * @return  {@link Integer}
     *          - id
     * @since   v1.01
     */
    public int getId() {
        return id;
    }

    /**
     * setId:
     * <p>设置 id
     *
     * @param   id
     *          - id
     * @since   v1.01
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * getConnectUtilization:
     * <p>获取连通率
     *
     * @return  {@link Integer}
     *          - 连通率
     * @since   v1.01
     */
    public int getConnectUtilization() {
        return connectUtilization;
    }

    /**
     * setConnectUtilization:
     * <p>设置连通率
     *
     * @param   connectUtilization
     *          - 连通率
     * @since   v1.01
     */
    public void setConnectUtilization(int connectUtilization) {
        this.connectUtilization = connectUtilization;
    }

    /**
     * getResponseTime:
     * <p>获取响应时间
     *
     * @return  {@link Long}
     *          - 响应时间
     * @since   v1.01
     */
    public long getResponseTime() {
        return responseTime;
    }

    /**
     * setResponseTime:
     * <p>设置响应时间
     *
     * @param   responseTime
     *          - 响应时间
     * @since   v1.01
     */
    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    /**
     * getPageSize:
     * <p>获取页面大小
     *
     * @return  {@link Integer}
     *          - 页面大小
     * @since   v1.01
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * setPageSize:
     * <p>设置页面大小
     *
     * @param   pageSize
     *          - 页面大小
     * @since   v1.01
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * getChangeRate:
     * <p>获取修改率
     *
     * @return  {@link Integer}
     *          - 修改率
     * @since   v1.01
     */
    public int getChangeRate() {
        return changeRate;
    }

    /**
     * setChangeRate:
     * <p>设置修改率
     *
     * @param   changeRate
     *          - 修改率
     * @since   v1.01
     */
    public void setChangeRate(int changeRate) {
        this.changeRate = changeRate;
    }

    /**
     * getCollecttime:
     * <p>获取采集时间
     *
     * @return  {@link String}
     *          - 采集时间
     * @since   v1.01
     */
    public String getCollecttime() {
        return collecttime;
    }

    /**
     * setCollecttime:
     * <p>设置采集时间
     *
     * @param   collecttime
     *          - 采集时间
     * @since   v1.01
     */
    public void setCollecttime(String collecttime) {
        this.collecttime = collecttime;
    }

}

