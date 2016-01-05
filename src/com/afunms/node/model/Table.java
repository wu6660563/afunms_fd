/*
 * @(#)Table.java     v1.01, Dec 21, 2012
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.model;

import java.io.Serializable;
import java.util.List;

import com.afunms.common.base.BaseVo;

/**
 * ClassName:   Table.java
 * <p> {@link Table} �豸�����еı�
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        Dec 21, 2012 6:15:34 PM
 */
public class Table extends BaseVo implements Serializable {

    /**
     * serialVersionUID:
     * <p>���л� ID
     *
     * @since   v1.01
     */
    private static final long serialVersionUID = -3746972947336858996L;

    /**
     * name:
     * <p>����
     *
     * @since   v1.01
     */
    private String name;

    /**
     * columnList:
     * <p>��
     *
     * @since   v1.01
     */
    private List<Column> columnList;

    /**
     * primaryKey:
     * <p>����
     *
     * @since   v1.01
     */
    private String primaryKey;

    /**
     * engine:
     * <p>��������
     *
     * @since   v1.01
     */
    private String engine;
    
    /**
     * charset:
     * <p>����
     *
     * @since   v1.01
     */
    private String charset;

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
     * getColumnList:
     * <p>��ȡ��
     *
     * @return  {@link List<Column>}
     *          - ��
     * @since   v1.01
     */
    public List<Column> getColumnList() {
        return columnList;
    }

    /**
     * setColumnList:
     * <p>������
     *
     * @param   columnList
     *          - ��
     * @since   v1.01
     */
    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }

    /**
     * getPrimaryKey:
     * <p>��ȡ����
     *
     * @return  {@link String}
     *          - ����
     * @since   v1.01
     */
    public String getPrimaryKey() {
        return primaryKey;
    }

    /**
     * setPrimaryKey:
     * <p>��������
     *
     * @param   primaryKey
     *          - ����
     * @since   v1.01
     */
    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * getEngine:
     * <p>��ȡ��������
     *
     * @return  {@link String}
     *          - ��������
     * @since   v1.01
     */
    public String getEngine() {
        return engine;
    }

    /**
     * setEngine:
     * <p>������������
     *
     * @param   engine
     *          - ��������
     * @since   v1.01
     */
    public void setEngine(String engine) {
        this.engine = engine;
    }

    /**
     * getCharset:
     * <p>��ȡ����
     *
     * @return  {@link String}
     *          - ����
     * @since   v1.01
     */
    public String getCharset() {
        return charset;
    }

    /**
     * setCharset:
     * <p>���ñ���
     *
     * @param   charset
     *          - ����
     * @since   v1.01
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }
    
}

