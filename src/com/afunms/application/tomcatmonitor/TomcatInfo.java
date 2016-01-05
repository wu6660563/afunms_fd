/*
 * @(#)TomcatInfo.java     v1.01, Jan 8, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.application.tomcatmonitor;

/**
 * ClassName:   TomcatInfo.java
 * <p>{@link TomcatInfo} ��Ϣ
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        Jan 8, 2013 1:50:28 PM
 */
public class TomcatInfo {

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
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getTomcatVersion() {
        return tomcatVersion;
    }

    /**
     * setTomcatVersion:
     * <p>
     *
     * @param   tomcatVersion
     *          -
     * @since   v1.01
     */
    public void setTomcatVersion(String tomcatVersion) {
        this.tomcatVersion = tomcatVersion;
    }

    /**
     * getJVMVersion:
     * <p>
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
     * <p>
     *
     * @param   version
     *          -
     * @since   v1.01
     */
    public void setJVMVersion(String version) {
        JVMVersion = version;
    }

    /**
     * getJVMVendor:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getJVMVendor() {
        return JVMVendor;
    }

    /**
     * setJVMVendor:
     * <p>
     *
     * @param   vendor
     *          -
     * @since   v1.01
     */
    public void setJVMVendor(String vendor) {
        JVMVendor = vendor;
    }

    /**
     * getOSName:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getOSName() {
        return OSName;
    }

    /**
     * setOSName:
     * <p>
     *
     * @param   name
     *          -
     * @since   v1.01
     */
    public void setOSName(String name) {
        OSName = name;
    }

    /**
     * getOSVersion:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getOSVersion() {
        return OSVersion;
    }

    /**
     * setOSVersion:
     * <p>
     *
     * @param   version
     *          -
     * @since   v1.01
     */
    public void setOSVersion(String version) {
        OSVersion = version;
    }

    /**
     * getOSArchitecture:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getOSArchitecture() {
        return OSArchitecture;
    }

    /**
     * setOSArchitecture:
     * <p>
     *
     * @param   architecture
     *          -
     * @since   v1.01
     */
    public void setOSArchitecture(String architecture) {
        OSArchitecture = architecture;
    }

    /**
     * getFreeMemory:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getFreeMemory() {
        return freeMemory;
    }

    /**
     * setFreeMemory:
     * <p>
     *
     * @param   freeMemory
     *          -
     * @since   v1.01
     */
    public void setFreeMemory(String freeMemory) {
        this.freeMemory = freeMemory;
    }

    /**
     * getTotalMemory:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getTotalMemory() {
        return totalMemory;
    }

    /**
     * setTotalMemory:
     * <p>
     *
     * @param   totalMemory
     *          -
     * @since   v1.01
     */
    public void setTotalMemory(String totalMemory) {
        this.totalMemory = totalMemory;
    }

    /**
     * getMaxMemory:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getMaxMemory() {
        return maxMemory;
    }

    /**
     * setMaxMemory:
     * <p>
     *
     * @param   maxMemory
     *          -
     * @since   v1.01
     */
    public void setMaxMemory(String maxMemory) {
        this.maxMemory = maxMemory;
    }

    /**
     * getMemoryUtilization:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getMemoryUtilization() {
        return memoryUtilization;
    }

    /**
     * setMemoryUtilization:
     * <p>
     *
     * @param   memoryUtilization
     *          -
     * @since   v1.01
     */
    public void setMemoryUtilization(String memoryUtilization) {
        this.memoryUtilization = memoryUtilization;
    }

}

