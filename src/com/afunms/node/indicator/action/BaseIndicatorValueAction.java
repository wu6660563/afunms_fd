/*
 * @(#)BaseIndicatorValueAction.java     v1.01, 2014 1 1
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action;

import com.afunms.gather.model.IndicatorValue;
import com.afunms.indicators.model.NodeDTO;

/**
 * ClassName:   BaseIndicatorValueAction.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 1 16:05:45
 */
public abstract class BaseIndicatorValueAction implements IndicatorValueAction {

    /**
     * indicatorValue:
     * <p>
     *
     * @since   v1.01
     */
    private IndicatorValue indicatorValue;

    /**
     * execute:
     *
     * @param indicatorValue
     *
     * @since   v1.01
     * @see com.afunms.node.indicator.action.IndicatorValueAction#execute(com.afunms.gather.model.IndicatorValue)
     */
    public void execute(IndicatorValue indicatorValue) {
        setIndicatorValue(indicatorValue);
        execute();
    }

    public abstract void execute();
    /**
     * getIndicatorValue:
     * <p>
     *
     * @return  IndicatorValue
     *          -
     * @since   v1.01
     */
    public IndicatorValue getIndicatorValue() {
        return indicatorValue;
    }

    /**
     * setIndicatorValue:
     * <p>
     *
     * @param   indicatorValue
     *          -
     * @since   v1.01
     */
    public void setIndicatorValue(IndicatorValue indicatorValue) {
        this.indicatorValue = indicatorValue;
    }

    public NodeDTO getNode() {
        return this.getIndicatorValue().getIndicatorInfo().getNodeDTO();
    }

    public String getIpAddress() {
        return this.getIndicatorValue().getIndicatorInfo().getNodeDTO().getIpaddress();
    }

    public String getNodeType() {
        return this.getIndicatorValue().getIndicatorInfo().getNodeDTO().getType();
    }

    public String getNodeSubtype() {
        return this.getIndicatorValue().getIndicatorInfo().getNodeDTO().getSubtype();
    }

    public String getNodeSysOid() {
        return this.getIndicatorValue().getIndicatorInfo().getNodeDTO().getSysOid();
    }

    public String getNodeId() {
        return this.getIndicatorValue().getIndicatorInfo().getNodeDTO().getNodeid();
    }

    public String getIndicatorName() {
        return this.getIndicatorValue().getIndicatorInfo().getGatherIndicators().getName();
    }
}

