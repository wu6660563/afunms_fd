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
 * @author      ����
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
     * <p>�豸id
     *
     * @since   v1.01
     */
    private String nodeid;

    /**
     * ping:
     * <p>��ͨ��
     *
     * @since   v1.01
     */
    private String ping;

    /**
     * tomcatVersion:
     * <p>Tomcat �汾
     *
     * @since   v1.01
     */
    private String tomcatVersion;

    /**
     * JVMVersion:
     * <p>JVM �汾
     *
     * @since   v1.01
     */
    private String JVMVersion;

    /**
     * JVMVendor:
     * <p>JVM ��Ӧ��
     *
     * @since   v1.01
     */
    private String JVMVendor;

    /**
     * OSName:
     * <p>����ϵͳ����
     *
     * @since   v1.01
     */
    private String OSName;

    /**
     * OSVersion:
     * <p>����ϵͳ�汾
     *
     * @since   v1.01
     */
    private String OSVersion;
    
    /**
     * OSArchitecture:
     * <p>����ϵͳ�ܹ�
     *
     * @since   v1.01
     */
    private String OSArchitecture;

    /**
     * freeMemory:
     * <p>�����ڴ�
     *
     * @since   v1.01
     */
    private String freeMemory;
    
    /**
     * totalMemory:
     * <p>���ڴ�
     *
     * @since   v1.01
     */
    private String totalMemory;

    /**
     * memoryUtilization:
     * <p>�ڴ�������
     *
     * @since   v1.01
     */
    private String memoryUtilization;

    /**
     * maxMemory:
     * <p>����ڴ�
     *
     * @since   v1.01
     */
    private String maxMemory;

    /**
     * collectTime:
     * <p>�ɼ�ʱ��
     *
     * @since   v1.01
     */
    private String collectTime;

    /**
     * getId:
     * <p>��ȡid
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
     * <p>����id
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
     * <p>��ȡ�豸id
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
     * <p>�����豸id
     *
     * @param   nodeid
     *          - �豸id
     * @since   v1.01
     */
    public void setNodeid(String nodeid) {
        this.nodeid = nodeid;
    }

    /**
     * getPing:
     * <p>��ȡ��ͨ��
     *
     * @return  {@link String}
     *          - ��ͨ��
     * @since   v1.01
     */
    public String getPing() {
        return ping;
    }

    /**
     * setPing:
     * <p>������ͨ��
     *
     * @param   ping
     *          - ��ͨ��
     * @since   v1.01
     */
    public void setPing(String ping) {
        this.ping = ping;
    }

    /**
     * getTomcatVersion:
     * <p>��ȡ tomcat �汾
     *
     * @return  {@link String}
     *          - tomcat �汾
     * @since   v1.01
     */
    public String getTomcatVersion() {
        return tomcatVersion;
    }

    /**
     * setTomcatVersion:
     * <p>���� tomcat �汾
     *
     * @param   tomcatVersion
     *          - tomcat �汾
     * @since   v1.01
     */
    public void setTomcatVersion(String tomcatVersion) {
        this.tomcatVersion = tomcatVersion;
    }

    /**
     * getJVMVersion:
     * <p>��ȡ JVM �汾
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
     * <p>���� JVM �汾
     *
     * @param   version
     *          - JVM �汾
     * @since   v1.01
     */
    public void setJVMVersion(String version) {
        JVMVersion = version;
    }

    /**
     * getJVMVendor:
     * <p>��ȡ JVM ��Ӧ��
     *
     * @return  {@link String}
     *          - JVM ��Ӧ��
     * @since   v1.01
     */
    public String getJVMVendor() {
        return JVMVendor;
    }

    /**
     * setJVMVendor:
     * <p>���� JVM ��Ӧ��
     *
     * @param   vendor
     *          - JVM ��Ӧ��
     * @since   v1.01
     */
    public void setJVMVendor(String vendor) {
        JVMVendor = vendor;
    }

    /**
     * getOSName:
     * <p>��ȡ����ϵͳ����
     *
     * @return  {@link String}
     *          - ����ϵͳ����
     * @since   v1.01
     */
    public String getOSName() {
        return OSName;
    }

    /**
     * setOSName:
     * <p>���ò���ϵͳ����
     *
     * @param   name
     *          - ����ϵͳ����
     * @since   v1.01
     */
    public void setOSName(String name) {
        OSName = name;
    }

    /**
     * getOSVersion:
     * <p>��ȡ����ϵͳ�汾
     *
     * @return  {@link String}
     *          - ����ϵͳ�汾
     * @since   v1.01
     */
    public String getOSVersion() {
        return OSVersion;
    }

    /**
     * setOSVersion:
     * <p>���ò���ϵͳ�汾
     *
     * @param   version
     *          - ����ϵͳ�汾
     * @since   v1.01
     */
    public void setOSVersion(String version) {
        OSVersion = version;
    }

    /**
     * getOSArchitecture:
     * <p>��ȡ����ϵͳ�ܹ�
     *
     * @return  {@link String}
     *          - ����ϵͳ�ܹ�
     * @since   v1.01
     */
    public String getOSArchitecture() {
        return OSArchitecture;
    }

    /**
     * setOSArchitecture:
     * <p>���ò���ϵͳ�ܹ�
     *
     * @param   architecture
     *          - ����ϵͳ�ܹ�
     * @since   v1.01
     */
    public void setOSArchitecture(String architecture) {
        OSArchitecture = architecture;
    }

    /**
     * getFreeMemory:
     * <p>��ȡ�����ڴ�
     *
     * @return  {@link String}
     *          - �����ڴ�
     * @since   v1.01
     */
    public String getFreeMemory() {
        return freeMemory;
    }

    /**
     * setFreeMemory:
     * <p>���ÿ����ڴ�
     *
     * @param   freeMemory
     *          - �����ڴ�
     * @since   v1.01
     */
    public void setFreeMemory(String freeMemory) {
        this.freeMemory = freeMemory;
    }

    /**
     * getTotalMemory:
     * <p>��ȡ���ڴ�
     *
     * @return  {@link String}
     *          - ���ڴ�
     * @since   v1.01
     */
    public String getTotalMemory() {
        return totalMemory;
    }

    /**
     * setTotalMemory:
     * <p>�������ڴ�
     *
     * @param   totalMemory
     *          - ���ڴ�
     * @since   v1.01
     */
    public void setTotalMemory(String totalMemory) {
        this.totalMemory = totalMemory;
    }

    /**
     * getMaxMemory:
     * <p>��ȡ����ڴ�
     *
     * @return  {@link String}
     *          - ����ڴ�
     * @since   v1.01
     */
    public String getMaxMemory() {
        return maxMemory;
    }

    /**
     * setMaxMemory:
     * <p>��������ڴ�
     *
     * @param   maxMemory
     *          - ����ڴ�
     * @since   v1.01
     */
    public void setMaxMemory(String maxMemory) {
        this.maxMemory = maxMemory;
    }

    /**
     * getMemoryUtilization:
     * <p>��ȡ�ڴ�������
     *
     * @return  {@link String}
     *          - �ڴ�������
     * @since   v1.01
     */
    public String getMemoryUtilization() {
        return memoryUtilization;
    }

    /**
     * setMemoryUtilization:
     * <p>�����ڴ�������
     *
     * @param   memoryUtilization
     *          - �ڴ�������
     * @since   v1.01
     */
    public void setMemoryUtilization(String memoryUtilization) {
        this.memoryUtilization = memoryUtilization;
    }

    /**
     * getCollectTime:
     * <p>��ȡ�ɼ�ʱ��
     *
     * @return  {@link String}
     *          - �ɼ�ʱ��
     * @since   v1.01
     */
    public String getCollectTime() {
        return collectTime;
    }

    /**
     * setCollectTime:
     * <p>���òɼ�ʱ��
     *
     * @param   collectTime
     *          - �ɼ�ʱ��
     * @since   v1.01
     */
    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }

}

