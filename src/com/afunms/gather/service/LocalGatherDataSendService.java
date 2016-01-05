/*
 * @(#)LocalGatherDataSendService.java     v1.01, 2014 1 17
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.service;

import com.afunms.gather.model.IndicatorValue;
import com.afunms.initialize.ResourceCenter;
import com.afunms.node.indicator.service.IndicatorValueActionService;

/**
 * ClassName:   LocalGatherDataSendService.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 17 10:37:05
 */
public class LocalGatherDataSendService implements GatherDataSendService {

    /**
     * send:
     *
     * @param indicatorValue
     *
     * @since   v1.01
     * @see com.afunms.gather.service.GatherDataSendService#send(com.afunms.gather.model.IndicatorValue)
     */
    public void send(IndicatorValue indicatorValue) {
        IndicatorValueActionService indicatorValueActionService = ResourceCenter.getInstance().getIndicatorValueActionService();
        indicatorValueActionService.add(indicatorValue);
    }

}

