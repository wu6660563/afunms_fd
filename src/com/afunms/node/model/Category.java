/*
 * @(#)Category.java     v1.01, Dec 21, 2012
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseVo;

/**
 * ClassName:   Category.java
 * <p> {@link Category} �豸����
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        Dec 21, 2012 5:43:11 PM
 */
public class Category extends BaseVo implements Serializable {

    /**
     * serialVersionUID:
     * <p>���л� Id
     *
     * @since   v1.01
     */
    private static final long serialVersionUID = 5904029678714154986L;

    /**
     * id:
     * <p>id
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
     * isLeaf:
     * <p>�Ƿ�ΪҶ�ڵ�
     *
     * @since   v1.01
     */
    private boolean isLeaf;

    /**
     * tableList:
     * <p>��
     *
     * @since   v1.01
     */
    private List<Table> tableList;

    /**
     * tableList:
     * <p>��
     *
     * @since   v1.01
     */
    private List<Indicator> indicatorList;

    /**
     * father:
     * <p>���ڵ�
     *
     * @since   v1.01
     */
    private Category father;

    /**
     * children:
     * <p>�ӽڵ�
     *
     * @since   v1.01
     */
    private List<Category> children;

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
     * isLeaf:
     * <p>�Ƿ�ΪҶ�ڵ�
     *
     * @return  boolean
     *          - ���ΪҶ�ڵ��򷵻� <code>true</code> �����߷��� <code>false</code>
     * @since   v1.01
     */
    public boolean isLeaf() {
        return isLeaf;
    }

    /**
     * setLeaf:
     * <p>����ΪҶ�ڵ�
     *
     * @param   isLeaf
     *          - �Ƿ�ΪҶ�ڵ�
     * @since   v1.01
     */
    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    /**
     * getTableList:
     * <p>��ȡ��
     *
     * @return  {@link List<Table>}
     *          - ��
     * @since   v1.01
     */
    public List<Table> getTableList() {
        return tableList;
    }

    /**
     * setTableList:
     * <p>���ñ�
     *
     * @param   tableList
     *          - ��
     * @since   v1.01
     */
    public void setTableList(List<Table> tableList) {
        this.tableList = tableList;
    }

    /**
     * getIndicatorList:
     * <p>
     *
     * @return  List<Indicator>
     *          -
     * @since   v1.01
     */
    public List<Indicator> getIndicatorList() {
        return indicatorList;
    }

    /**
     * setIndicatorList:
     * <p>
     *
     * @param   indicatorList
     *          -
     * @since   v1.01
     */
    public void setIndicatorList(List<Indicator> indicatorList) {
        this.indicatorList = indicatorList;
    }

    /**
     * getFather:
     * <p>��ȡ���ڵ�
     *
     * @return  {@link Category}
     *          - ���ڵ�
     * @since   v1.01
     */
    public Category getFather() {
        return father;
    }

    /**
     * setFather:
     * <p>���ø��ڵ�
     *
     * @param   father
     *          - ���ڵ�
     * @since   v1.01
     */
    public void setFather(Category father) {
        this.father = father;
    }

    /**
     * getChildren:
     * <p>��ȡ�ӽڵ�
     *
     * @return  {@link List<Category>}
     *          - �ӽڵ�
     * @since   v1.01
     */
    public List<Category> getChildren() {
        return children;
    }

    /**
     * setChildren:
     * <p>�����ӽڵ�
     *
     * @param   children
     *          - �ӽڵ�
     * @since   v1.01
     */
    public void setChildren(List<Category> children) {
        this.children = children;
    }

    /**
     * addChild:
     * <p>����ӽڵ�
     *
     * @param   child
     *          - �ӽڵ�
     *
     * @since   v1.01
     */
    public void addChild(Category child) {
        if (this.children == null) {
            this.children = new ArrayList<Category>();
        }
        this.children.add(child);
    }

    /**
     * removeChild:
     * <p>ɾ��һ���ӽڵ�
     *
     * @param   index
     *          - ��Ҫɾ���ӽڵ������
     * @return  {@link Category}
     *          - �ӽڵ�
     *
     * @since   v1.01
     */
    public Category removeChild(int index) {
        List<Category> children = getChildren();
        if (children != null && children.size() > index) {
            return children.remove(index);
        }
        return null;
    }

    /**
     * getChild:
     * <p>��ȡ�ӽڵ�
     *
     * @param   index
     *          - �ӽڵ������
     * @return  {@link Category}
     *          - �ӽڵ�
     *
     * @since   v1.01
     */
    public Category getChild(int index) {
        List<Category> children = getChildren();
        if (children != null && children.size() > index) {
            return children.get(index);
        }
        return null;
    }

    /**
     * getChild:
     * <p>�������ƻ�ȡ�ӽڵ�
     *
     * @param   name
     *          - �ӽڵ�����
     * @return  {@link Category}
     *          - �ӽڵ�
     *
     * @since   v1.01
     */
    public Category getChild(String name) {
        List<Category> children = getChildren();
        if (children != null) {
            for (Category child : children) {
                if (name.equals(child.getName())) {
                    return child;
                }
            }
        }
        return null;
    }
}
