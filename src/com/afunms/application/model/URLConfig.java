/*
 * @(#)URLConfig.java     v1.01, Mar 5, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.application.model;

import java.io.Serializable;

import com.afunms.common.base.BaseVo;

/**
 * ClassName:   URLConfig.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        Mar 5, 2013 11:08:44 PM
 */
public class URLConfig extends BaseVo implements Serializable {

    /**
     * serialVersionUID:
     * <p>序列化ID
     *
     * @since   v1.01
     */
    private static final long serialVersionUID = 593898959391796710L;

    /**
     * id:
     * <p>Id
     *
     * @since   v1.01
     */
    private int id;

    /**
     * name:
     * <p>名称
     *
     * @since   v1.01
     */
    private String name;

    /**
     * ipaddress:
     * <p>IP 地址
     *
     * @since   v1.01
     */
    private String ipaddress;

    /**
     * url:
     * <p>地址
     *
     * @since   v1.01
     */
    private String url;

    /**
     * timeout:
     * <p>
     *
     * @since   v1.01
     */
    private int timeout;

    /**
     * monFlag:
     * <p>是否监控
     *
     * @since   v1.01
     */
    private int monFlag;

    /**
     * pageSize:
     * <p>页面数据大小
     *
     * @since   v1.01
     */
    private int pageSize;

    /**
     * supperid:
     * <p>供应商 ID
     *
     * @since   v1.01
     */
    private int supperid;

    /**
     * bid:
     * <p>权限ID
     *
     * @since   v1.01
     */
    private String bid;

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
     * getName:
     * <p>获取名称
     *
     * @return  {@link String}
     *          - 名称
     * @since   v1.01
     */
    public String getName() {
        return name;
    }

    /**
     * setName:
     * <p>设置名称
     *
     * @param   name
     *          - 名称
     * @since   v1.01
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getIpaddress:
     * <p>获取IP地址
     *
     * @return  {@link String}
     *          - IP地址
     * @since   v1.01
     */
    public String getIpaddress() {
        return ipaddress;
    }

    /**
     * setIpaddress:
     * <p>设置IP地址
     *
     * @param   ipaddress
     *          - IP地址
     * @since   v1.01
     */
    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    /**
     * getUrl:
     * <p>获取地址
     *
     * @return  {@link String}
     *          - 地址
     * @since   v1.01
     */
    public String getUrl() {
        return url;
    }

    /**
     * setUrl:
     * <p>设置地址
     *
     * @param   url
     *          - 地址
     * @since   v1.01
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * getTimeout:
     * <p>获取超时
     *
     * @return  {@link Integer}
     *          - 超时
     * @since   v1.01
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * setTimeout:
     * <p>设置超时
     *
     * @param   timeout
     *          - 超时
     * @since   v1.01
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * getMonFlag:
     * <p>是否监控
     *
     * @return  {@link Integer}
     *          - 如果监控则返回 1，否则返回 0
     * @since   v1.01
     */
    public int getMonFlag() {
        return monFlag;
    }

    /**
     * setMonFlag:
     * <p>设置监控
     *
     * @param   monFlag
     *          - 如果监控则设置 1，否则设置 0
     * @since   v1.01
     */
    public void setMonFlag(int monFlag) {
        this.monFlag = monFlag;
    }

    /**
     * getPageSize:
     * <p>获取页面数据大小
     *
     * @return  {@link Integer}
     *          - 页面数据大小
     * @since   v1.01
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * setPageSize:
     * <p>设置页面数据大小
     *
     * @param   pageSize
     *          - 页面数据大小
     * @since   v1.01
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * getSupperid:
     * <p>获取供应商
     *
     * @return  {@link Integer}
     *          - 供应商 id
     * @since   v1.01
     */
    public int getSupperid() {
        return supperid;
    }

    /**
     * setSupperid:
     * <p>设置供应商
     *
     * @param   supperid
     *          - 供应商 id
     * @since   v1.01
     */
    public void setSupperid(int supperid) {
        this.supperid = supperid;
    }

    /**
     * getBid:
     * <p>获取权限
     *
     * @return  {@link String}
     *          - 权限
     * @since   v1.01
     */
    public String getBid() {
        return bid;
    }

    /**
     * setBid:
     * <p>设置权限
     *
     * @param   bid
     *          - 权限
     * @since   v1.01
     */
    public void setBid(String bid) {
        this.bid = bid;
    }

}

