/*
 * @(#)IndicatorValueAction.java     v1.01, 2014 1 17
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.synchronize.service;

import com.afunms.gather.model.IndicatorValue;
import com.afunms.initialize.ResourceCenter;
import com.afunms.node.indicator.service.IndicatorValueActionService;
import com.afunms.rmi.service.RMIParameter;

/**
 * ClassName:   IndicatorValueAction.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 17 10:50:49
 */
public class IndicatorValueAction implements DataAction {

    /**
     * action:
     *
     * @param parameter
     *
     * @since   v1.01
     * @see com.afunms.synchronize.service.DataAction#action(com.afunms.rmi.service.RMIParameter)
     */
    public void action(RMIParameter parameter) {
        IndicatorValue indicatorValue = (IndicatorValue) parameter.getParameter("indicatorValue");
        if (indicatorValue != null) {
            IndicatorValueActionService indicatorValueActionService = ResourceCenter.getInstance().getIndicatorValueActionService();
            indicatorValueActionService.add(indicatorValue);
        }
    }

}

