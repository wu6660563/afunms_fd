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
 * <p>{@link Series} <code>Flex</code> 图表中基础的数据序列，
 * 子类可以构造实际的数据结构。
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        May 17, 2013 11:56:59 AM
 */
public abstract class Series<D extends SeriesDataItem> extends BaseVo implements Serializable {

    /**
     * serialVersionUID:
     * <p>序列化 ID
     *
     * @since   v1.01
     */
    private static final long serialVersionUID = 4761108464691238842L;

    /**
     * name:
     * <p>序列名称
     *
     * @since   v1.01
     */
    protected String name;

    /**
     * title:
     * <p>序列显示标题
     *
     * @since   v1.01
     */
    protected String title;

    /**
     * dataItemList:
     * <p>数据项列表
     *
     * @since   v1.01
     */
    protected List<D> dataItemList = new ArrayList<D>();

    /**
     * getName:
     * <p>获取序列名称
     *
     * @return  {@link String}
     *          - 序列名称
     * @since   v1.01
     */
    public String getName() {
        return name;
    }

    public abstract void addOrUpdate(D dataItem);

    /**
     * setName:
     * <p>设置序列名称
     *
     * @param   name
     *          - 序列名称
     * @since   v1.01
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getItemCount:
     * <p>获取序列中数据数量
     *
     * @return  {@link Integer}
     *          - 序列中数据数量
     * @since   v1.01
     */
    public abstract int getItemCount();

    /**
     * isEmpty:
     * <p>序列为空，则返回 <code>true</code> ，否则返回 <code>false</code>
     *
     * @return  {@link Boolean}
     *          - 序列为空，则返回 <code>true</code> ，否则返回 <code>false</code>
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

