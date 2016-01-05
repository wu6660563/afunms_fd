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
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        Mar 5, 2013 11:08:44 PM
 */
public class URLConfig extends BaseVo implements Serializable {

    /**
     * serialVersionUID:
     * <p>���л�ID
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
     * <p>����
     *
     * @since   v1.01
     */
    private String name;

    /**
     * ipaddress:
     * <p>IP ��ַ
     *
     * @since   v1.01
     */
    private String ipaddress;

    /**
     * url:
     * <p>��ַ
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
     * <p>�Ƿ���
     *
     * @since   v1.01
     */
    private int monFlag;

    /**
     * pageSize:
     * <p>ҳ�����ݴ�С
     *
     * @since   v1.01
     */
    private int pageSize;

    /**
     * supperid:
     * <p>��Ӧ�� ID
     *
     * @since   v1.01
     */
    private int supperid;

    /**
     * bid:
     * <p>Ȩ��ID
     *
     * @since   v1.01
     */
    private String bid;

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
     * getName:
     * <p>��ȡ����
     *
     * @return  {@link String}
     *          - ����
     * @since   v1.01
     */
    public String getName() {
        return name;
    }

    /**
     * setName:
     * <p>��������
     *
     * @param   name
     *          - ����
     * @since   v1.01
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getIpaddress:
     * <p>��ȡIP��ַ
     *
     * @return  {@link String}
     *          - IP��ַ
     * @since   v1.01
     */
    public String getIpaddress() {
        return ipaddress;
    }

    /**
     * setIpaddress:
     * <p>����IP��ַ
     *
     * @param   ipaddress
     *          - IP��ַ
     * @since   v1.01
     */
    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    /**
     * getUrl:
     * <p>��ȡ��ַ
     *
     * @return  {@link String}
     *          - ��ַ
     * @since   v1.01
     */
    public String getUrl() {
        return url;
    }

    /**
     * setUrl:
     * <p>���õ�ַ
     *
     * @param   url
     *          - ��ַ
     * @since   v1.01
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * getTimeout:
     * <p>��ȡ��ʱ
     *
     * @return  {@link Integer}
     *          - ��ʱ
     * @since   v1.01
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * setTimeout:
     * <p>���ó�ʱ
     *
     * @param   timeout
     *          - ��ʱ
     * @since   v1.01
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * getMonFlag:
     * <p>�Ƿ���
     *
     * @return  {@link Integer}
     *          - �������򷵻� 1�����򷵻� 0
     * @since   v1.01
     */
    public int getMonFlag() {
        return monFlag;
    }

    /**
     * setMonFlag:
     * <p>���ü��
     *
     * @param   monFlag
     *          - ������������ 1���������� 0
     * @since   v1.01
     */
    public void setMonFlag(int monFlag) {
        this.monFlag = monFlag;
    }

    /**
     * getPageSize:
     * <p>��ȡҳ�����ݴ�С
     *
     * @return  {@link Integer}
     *          - ҳ�����ݴ�С
     * @since   v1.01
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * setPageSize:
     * <p>����ҳ�����ݴ�С
     *
     * @param   pageSize
     *          - ҳ�����ݴ�С
     * @since   v1.01
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * getSupperid:
     * <p>��ȡ��Ӧ��
     *
     * @return  {@link Integer}
     *          - ��Ӧ�� id
     * @since   v1.01
     */
    public int getSupperid() {
        return supperid;
    }

    /**
     * setSupperid:
     * <p>���ù�Ӧ��
     *
     * @param   supperid
     *          - ��Ӧ�� id
     * @since   v1.01
     */
    public void setSupperid(int supperid) {
        this.supperid = supperid;
    }

    /**
     * getBid:
     * <p>��ȡȨ��
     *
     * @return  {@link String}
     *          - Ȩ��
     * @since   v1.01
     */
    public String getBid() {
        return bid;
    }

    /**
     * setBid:
     * <p>����Ȩ��
     *
     * @param   bid
     *          - Ȩ��
     * @since   v1.01
     */
    public void setBid(String bid) {
        this.bid = bid;
    }

}

