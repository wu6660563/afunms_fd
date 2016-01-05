/*
 * @(#)TomcatNodeTemp.java     v1.01, Jan 10, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.temp.model;

import com.afunms.common.base.BaseVo;

/**
 * ClassName:   TomcatNodeTemp.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        Jan 10, 2013 11:37:33 AM
 */
public class TomcatNodeTemp extends BaseVo {

    /**
     * id:
     * <p>id
     *
     * @since   v1.01
     */
    private int id;

    /**
     * nodeid:
     * <p>设备id
     *
     * @since   v1.01
     */
    private String nodeid;

    /**
     * ping:
     * <p>连通率
     *
     * @since   v1.01
     */
    private String ping;

    /**
     * tomcatVersion:
     * <p>Tomcat 版本
     *
     * @since   v1.01
     */
    private String tomcatVersion;

    /**
     * JVMVersion:
     * <p>JVM 版本
     *
     * @since   v1.01
     */
    private String JVMVersion;

    /**
     * JVMVendor:
     * <p>JVM 供应商
     *
     * @since   v1.01
     */
    private String JVMVendor;

    /**
     * OSName:
     * <p>操作系统名称
     *
     * @since   v1.01
     */
    private String OSName;

    /**
     * OSVersion:
     * <p>操作系统版本
     *
     * @since   v1.01
     */
    private String OSVersion;
    
    /**
     * OSArchitecture:
     * <p>操作系统架构
     *
     * @since   v1.01
     */
    private String OSArchitecture;

    /**
     * freeMemory:
     * <p>空闲内存
     *
     * @since   v1.01
     */
    private String freeMemory;
    
    /**
     * totalMemory:
     * <p>总内存
     *
     * @since   v1.01
     */
    private String totalMemory;

    /**
     * memoryUtilization:
     * <p>内存利用率
     *
     * @since   v1.01
     */
    private String memoryUtilization;

    /**
     * maxMemory:
     * <p>最大内存
     *
     * @since   v1.01
     */
    private String maxMemory;

    /**
     * collectTime:
     * <p>采集时间
     *
     * @since   v1.01
     */
    private String collectTime;

    /**
     * getId:
     * <p>获取id
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
     * <p>设置id
     *
     * @param   id
     *          - id
     * @since   v1.01
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * getNodeid:
     * <p>获取设备id
     *
     * @return  {@link String}
     *          - id
     * @since   v1.01
     */
    public String getNodeid() {
        return nodeid;
    }

    /**
     * setNodeid:
     * <p>设置设备id
     *
     * @param   nodeid
     *          - 设备id
     * @since   v1.01
     */
    public void setNodeid(String nodeid) {
        this.nodeid = nodeid;
    }

    /**
     * getPing:
     * <p>获取连通率
     *
     * @return  {@link String}
     *          - 连通率
     * @since   v1.01
     */
    public String getPing() {
        return ping;
    }

    /**
     * setPing:
     * <p>设置连通率
     *
     * @param   ping
     *          - 连通率
     * @since   v1.01
     */
    public void setPing(String ping) {
        this.ping = ping;
    }

    /**
     * getTomcatVersion:
     * <p>获取 tomcat 版本
     *
     * @return  {@link String}
     *          - tomcat 版本
     * @since   v1.01
     */
    public String getTomcatVersion() {
        return tomcatVersion;
    }

    /**
     * setTomcatVersion:
     * <p>设置 tomcat 版本
     *
     * @param   tomcatVersion
     *          - tomcat 版本
     * @since   v1.01
     */
    public void setTomcatVersion(String tomcatVersion) {
        this.tomcatVersion = tomcatVersion;
    }

    /**
     * getJVMVersion:
     * <p>获取 JVM 版本
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getJVMVersion() {
        return JVMVersion;
    }

    /**
     * setJVMVersion:
     * <p>设置 JVM 版本
     *
     * @param   version
     *          - JVM 版本
     * @since   v1.01
     */
    public void setJVMVersion(String version) {
        JVMVersion = version;
    }

    /**
     * getJVMVendor:
     * <p>获取 JVM 供应商
     *
     * @return  {@link String}
     *          - JVM 供应商
     * @since   v1.01
     */
    public String getJVMVendor() {
        return JVMVendor;
    }

    /**
     * setJVMVendor:
     * <p>设置 JVM 供应商
     *
     * @param   vendor
     *          - JVM 供应商
     * @since   v1.01
     */
    public void setJVMVendor(String vendor) {
        JVMVendor = vendor;
    }

    /**
     * getOSName:
     * <p>获取操作系统名称
     *
     * @return  {@link String}
     *          - 操作系统名称
     * @since   v1.01
     */
    public String getOSName() {
        return OSName;
    }

    /**
     * setOSName:
     * <p>设置操作系统名称
     *
     * @param   name
     *          - 操作系统名称
     * @since   v1.01
     */
    public void setOSName(String name) {
        OSName = name;
    }

    /**
     * getOSVersion:
     * <p>获取操作系统版本
     *
     * @return  {@link String}
     *          - 操作系统版本
     * @since   v1.01
     */
    public String getOSVersion() {
        return OSVersion;
    }

    /**
     * setOSVersion:
     * <p>设置操作系统版本
     *
     * @param   version
     *          - 操作系统版本
     * @since   v1.01
     */
    public void setOSVersion(String version) {
        OSVersion = version;
    }

    /**
     * getOSArchitecture:
     * <p>获取操作系统架构
     *
     * @return  {@link String}
     *          - 操作系统架构
     * @since   v1.01
     */
    public String getOSArchitecture() {
        return OSArchitecture;
    }

    /**
     * setOSArchitecture:
     * <p>设置操作系统架构
     *
     * @param   architecture
     *          - 操作系统架构
     * @since   v1.01
     */
    public void setOSArchitecture(String architecture) {
        OSArchitecture = architecture;
    }

    /**
     * getFreeMemory:
     * <p>获取空闲内存
     *
     * @return  {@link String}
     *          - 空闲内存
     * @since   v1.01
     */
    public String getFreeMemory() {
        return freeMemory;
    }

    /**
     * setFreeMemory:
     * <p>设置空闲内存
     *
     * @param   freeMemory
     *          - 空闲内存
     * @since   v1.01
     */
    public void setFreeMemory(String freeMemory) {
        this.freeMemory = freeMemory;
    }

    /**
     * getTotalMemory:
     * <p>获取总内存
     *
     * @return  {@link String}
     *          - 总内存
     * @since   v1.01
     */
    public String getTotalMemory() {
        return totalMemory;
    }

    /**
     * setTotalMemory:
     * <p>设置总内存
     *
     * @param   totalMemory
     *          - 总内存
     * @since   v1.01
     */
    public void setTotalMemory(String totalMemory) {
        this.totalMemory = totalMemory;
    }

    /**
     * getMaxMemory:
     * <p>获取最大内存
     *
     * @return  {@link String}
     *          - 最大内存
     * @since   v1.01
     */
    public String getMaxMemory() {
        return maxMemory;
    }

    /**
     * setMaxMemory:
     * <p>设置最大内存
     *
     * @param   maxMemory
     *          - 最大内存
     * @since   v1.01
     */
    public void setMaxMemory(String maxMemory) {
        this.maxMemory = maxMemory;
    }

    /**
     * getMemoryUtilization:
     * <p>获取内存利用率
     *
     * @return  {@link String}
     *          - 内存利用率
     * @since   v1.01
     */
    public String getMemoryUtilization() {
        return memoryUtilization;
    }

    /**
     * setMemoryUtilization:
     * <p>设置内存利用率
     *
     * @param   memoryUtilization
     *          - 内存利用率
     * @since   v1.01
     */
    public void setMemoryUtilization(String memoryUtilization) {
        this.memoryUtilization = memoryUtilization;
    }

    /**
     * getCollectTime:
     * <p>获取采集时间
     *
     * @return  {@link String}
     *          - 采集时间
     * @since   v1.01
     */
    public String getCollectTime() {
        return collectTime;
    }

    /**
     * setCollectTime:
     * <p>设置采集时间
     *
     * @param   collectTime
     *          - 采集时间
     * @since   v1.01
     */
    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }

}

