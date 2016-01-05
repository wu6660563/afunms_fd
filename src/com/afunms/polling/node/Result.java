/*
 * @(#)Result.java     v1.01, Dec 28, 2012
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.node;

import java.util.Date;

import com.afunms.common.base.BaseVo;

/**
 * ClassName:   Result.java
 * <p> {@link Result} �ɼ����
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        Dec 28, 2012 11:34:30 AM
 */
public class Result extends BaseVo {

    /**
     * result:
     * <p>���
     *
     * @since   v1.01
     */
    private Object result;

    /**
     * errorCode:
     * <p>���룬1Ϊ�ɹ���δ�ɹ�С��0
     *
     * @since   v1.01
     */
    private int errorCode;

    /**
     * errorInfo:
     * <p>������Ϣ
     *
     * @since   v1.01
     */
    private String errorInfo;

    /**
     * collectTime:
     * <p>�ɼ�ʱ��
     *
     * @since   v1.01
     */
    private Date collectTime;

    /**
     * getResult:
     * <p>��ȡ���
     *
     * @return  {@link Object}
     *          - ���
     * @since   v1.01
     */
    public Object getResult() {
        return result;
    }

    /**
     * setResult:
     * <p>���ý��
     *
     * @param   result
     *          - ���
     * @since   v1.01
     */
    public void setResult(Object result) {
        this.result = result;
    }

    
    /**
     * getErrorCode:
     * <p>��ȡ����
     *
     * @return  {@link Integer}
     *          - ����
     * @since   v1.01
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * setErrorCode:
     * <p>���ñ���
     *
     * @param   code
     *          - ����
     * @since   v1.01
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * getErrorInfo:
     * <p>��ȡ������Ϣ
     *
     * @return  {@link String}
     *          - ������Ϣ
     * @since   v1.01
     */
    public String getErrorInfo() {
        return errorInfo;
    }

    /**
     * setErrorInfo:
     * <p>���ô�����Ϣ
     *
     * @param   reason
     *          - ������Ϣ
     * @since   v1.01
     */
    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    /**
     * getCollectTime:
     * <p>��ȡ�ɼ�ʱ��
     *
     * @return  {@link Date}
     *          - �ɼ�ʱ��
     * @since   v1.01
     */
    public Date getCollectTime() {
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
    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

}

