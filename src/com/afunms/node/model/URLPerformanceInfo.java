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
 * <p>{@link URLPerformanceInfo} <code>URL</code> ������Ϣ
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        May 30, 2013 10:27:28 AM
 */
public class URLPerformanceInfo extends BaseVo implements Serializable {

    /**
     * serialVersionUID:
     * <p>���л� Id
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
     * <p>��ͨ��
     *
     * @since   v1.01
     */
    private int connectUtilization;

    /**
     * responseTime:
     * <p>��Ӧʱ��
     *
     * @since   v1.01
     */
    private long responseTime;

    /**
     * pageSize:
     * <p>ҳ���С
     *
     * @since   v1.01
     */
    private int pageSize;

    /**
     * changeRate:
     * <p>�޸���
     *
     * @since   v1.01
     */
    private int changeRate;

    /**
     * collecttime:
     * <p>�ɼ�ʱ��
     *
     * @since   v1.01
     */
    private String collecttime;

    /**
     * getId:
     * <p>��ȡ id
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
     * <p>���� id
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
     * <p>��ȡ��ͨ��
     *
     * @return  {@link Integer}
     *          - ��ͨ��
     * @since   v1.01
     */
    public int getConnectUtilization() {
        return connectUtilization;
    }

    /**
     * setConnectUtilization:
     * <p>������ͨ��
     *
     * @param   connectUtilization
     *          - ��ͨ��
     * @since   v1.01
     */
    public void setConnectUtilization(int connectUtilization) {
        this.connectUtilization = connectUtilization;
    }

    /**
     * getResponseTime:
     * <p>��ȡ��Ӧʱ��
     *
     * @return  {@link Long}
     *          - ��Ӧʱ��
     * @since   v1.01
     */
    public long getResponseTime() {
        return responseTime;
    }

    /**
     * setResponseTime:
     * <p>������Ӧʱ��
     *
     * @param   responseTime
     *          - ��Ӧʱ��
     * @since   v1.01
     */
    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    /**
     * getPageSize:
     * <p>��ȡҳ���С
     *
     * @return  {@link Integer}
     *          - ҳ���С
     * @since   v1.01
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * setPageSize:
     * <p>����ҳ���С
     *
     * @param   pageSize
     *          - ҳ���С
     * @since   v1.01
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * getChangeRate:
     * <p>��ȡ�޸���
     *
     * @return  {@link Integer}
     *          - �޸���
     * @since   v1.01
     */
    public int getChangeRate() {
        return changeRate;
    }

    /**
     * setChangeRate:
     * <p>�����޸���
     *
     * @param   changeRate
     *          - �޸���
     * @since   v1.01
     */
    public void setChangeRate(int changeRate) {
        this.changeRate = changeRate;
    }

    /**
     * getCollecttime:
     * <p>��ȡ�ɼ�ʱ��
     *
     * @return  {@link String}
     *          - �ɼ�ʱ��
     * @since   v1.01
     */
    public String getCollecttime() {
        return collecttime;
    }

    /**
     * setCollecttime:
     * <p>���òɼ�ʱ��
     *
     * @param   collecttime
     *          - �ɼ�ʱ��
     * @since   v1.01
     */
    public void setCollecttime(String collecttime) {
        this.collecttime = collecttime;
    }

}

