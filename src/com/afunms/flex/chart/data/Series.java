/*
 * @(#)Series.java     v1.01, May 17, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.flex.chart.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseVo;

/**
 * ClassName:   Series.java
 * <p>{@link Series} <code>Flex</code> ͼ���л������������У�
 * ������Թ���ʵ�ʵ����ݽṹ��
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        May 17, 2013 11:56:59 AM
 */
public abstract class Series<D extends SeriesDataItem> extends BaseVo implements Serializable {

    /**
     * serialVersionUID:
     * <p>���л� ID
     *
     * @since   v1.01
     */
    private static final long serialVersionUID = 4761108464691238842L;

    /**
     * name:
     * <p>��������
     *
     * @since   v1.01
     */
    protected String name;

    /**
     * title:
     * <p>������ʾ����
     *
     * @since   v1.01
     */
    protected String title;

    /**
     * dataItemList:
     * <p>�������б�
     *
     * @since   v1.01
     */
    protected List<D> dataItemList = new ArrayList<D>();

    /**
     * getName:
     * <p>��ȡ��������
     *
     * @return  {@link String}
     *          - ��������
     * @since   v1.01
     */
    public String getName() {
        return name;
    }

    public abstract void addOrUpdate(D dataItem);

    /**
     * setName:
     * <p>������������
     *
     * @param   name
     *          - ��������
     * @since   v1.01
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getItemCount:
     * <p>��ȡ��������������
     *
     * @return  {@link Integer}
     *          - ��������������
     * @since   v1.01
     */
    public abstract int getItemCount();

    /**
     * isEmpty:
     * <p>����Ϊ�գ��򷵻� <code>true</code> �����򷵻� <code>false</code>
     *
     * @return  {@link Boolean}
     *          - ����Ϊ�գ��򷵻� <code>true</code> �����򷵻� <code>false</code>
     * @since   v1.01
     */
    public abstract boolean isEmpty();

    /**
     * getDataItemList:
     * <p>
     *
     * @return  List<D>
     *          -
     * @since   v1.01
     */
    public List<D> getDataItemList() {
        return dataItemList;
    }

    /**
     * setDataItemList:
     * <p>
     *
     * @param   dataItemList
     *          -
     * @since   v1.01
     */
    public void setDataItemList(List<D> dataItemList) {
        this.dataItemList = dataItemList;
    }

    /**
     * getTitle:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getTitle() {
        return title;
    }

    /**
     * setTitle:
     * <p>
     *
     * @param   title
     *          -
     * @since   v1.01
     */
    public void setTitle(String title) {
        this.title = title;
    }

}

