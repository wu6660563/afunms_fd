/*
 * @(#)Domain.java     v1.01, 2013 10 16
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.model;

import com.afunms.common.base.BaseVo;

/**
 * ClassName:   Domain.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 10 16 08:53:45
 */
public class Domain extends BaseVo {

    /**
     * serialVersionUID:
     * <p>–Ú¡–ªØ id
     *
     * @since   v1.01
     */
    private static final long serialVersionUID = 4382899371170359955L;

    private int id;

    private String descr;

    private boolean isDefault;

    /**
     * getId:
     * <p>
     *
     * @return  int
     *          -
     * @since   v1.01
     */
    public int getId() {
        return id;
    }

    /**
     * setId:
     * <p>
     *
     * @param   id
     *          -
     * @since   v1.01
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * getDescr:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getDescr() {
        return descr;
    }

    /**
     * setDescr:
     * <p>
     *
     * @param   descr
     *          -
     * @since   v1.01
     */
    public void setDescr(String descr) {
        this.descr = descr;
    }

    /**
     * isDefault:
     * <p>
     *
     * @return  boolean
     *          -
     * @since   v1.01
     */
    public boolean isDefault() {
        return isDefault;
    }

    /**
     * setDefault:
     * <p>
     *
     * @param   isDefault
     *          -
     * @since   v1.01
     */
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

}

