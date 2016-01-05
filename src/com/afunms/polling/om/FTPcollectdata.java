package com.afunms.polling.om;

import java.io.Serializable;

import java.util.Calendar;

/**
 * ClassName:   FTPcollectdata.java
 * <p>{@link FTPcollectdata} <code>FTP</code> �ɼ��������
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        Jan 4, 2013 10:04:53 AM
 */
public class FTPcollectdata implements Serializable {

    /**
     * serialVersionUID:
     * <p>���л� ID
     *
     * @since   v1.01
     */
    private static final long serialVersionUID = -7599859559902009025L;

    /**
     * id:
     * <p>id
     *
     * @since   v1.01
     */
    private int id;

    /**
     * category:
     * <p>���
     *
     * @since   v1.01
     */
    private String category;

    /**
     * thevalue:
     * <p>ֵ
     *
     * @since   v1.01
     */
    private String thevalue;

    /**
     * collecttime:
     * <p>�ɼ�ʱ��
     *
     * @since   v1.01
     */
    private Calendar collecttime;

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
     * getCategory:
     * <p>��ȡ���
     *
     * @return  {@link String}
     *          - ���
     * @since   v1.01
     */
    public String getCategory() {
        return category;
    }

    /**
     * setCategory:
     * <p>�������
     *
     * @param   category
     *          - ���
     * @since   v1.01
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * getThevalue:
     * <p>��ȡֵ
     *
     * @return  {@link String}
     *          - ֵ
     * @since   v1.01
     */
    public String getThevalue() {
        return thevalue;
    }

    /**
     * setThevalue:
     * <p>����ֵ
     *
     * @param   thevalue
     *          - ֵ
     * @since   v1.01
     */
    public void setThevalue(String thevalue) {
        this.thevalue = thevalue;
    }

    /**
     * getCollecttime:
     * <p>��ȡ�ɼ�ʱ��
     *
     * @return  {@link Calendar}
     *          - �ɼ�ʱ��
     * @since   v1.01
     */
    public Calendar getCollecttime() {
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
    public void setCollecttime(Calendar collecttime) {
        this.collecttime = collecttime;
    }

    


}