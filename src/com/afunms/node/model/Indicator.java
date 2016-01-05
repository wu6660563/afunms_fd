/*
 * @(#)Indicator.java     v1.01, 2014 1 7
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.model;

import java.util.List;


/**
 * ClassName:   Indicator.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 7 10:18:35
 */
public class Indicator {

    private String name;

    private String indicatorGather;

    private List<String> indicatorValueActionList;

    /**
     * getName:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getName() {
        return name;
    }

    /**
     * setName:
     * <p>
     *
     * @param   name
     *          -
     * @since   v1.01
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getIndicatorGather:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getIndicatorGather() {
        return indicatorGather;
    }

    /**
     * setIndicatorGather:
     * <p>
     *
     * @param   indicatorGather
     *          -
     * @since   v1.01
     */
    public void setIndicatorGather(String indicatorGather) {
        this.indicatorGather = indicatorGather;
    }

    /**
     * getIndicatorValueActionList:
     * <p>
     *
     * @return  List<String>
     *          -
     * @since   v1.01
     */
    public List<String> getIndicatorValueActionList() {
        return indicatorValueActionList;
    }

    /**
     * setIndicatorValueActionList:
     * <p>
     *
     * @param   indicatorValueActionList
     *          -
     * @since   v1.01
     */
    public void setIndicatorValueActionList(List<String> indicatorValueActionList) {
        this.indicatorValueActionList = indicatorValueActionList;
    }

}

