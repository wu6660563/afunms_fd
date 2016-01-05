/*
 * @(#)GatherDataSendService.java     v1.01, 2014 1 3
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.service;

import com.afunms.gather.model.IndicatorValue;

/**
 * ClassName:   GatherDataSendService.java
 * <p>{@link GatherDataSendService} 采集指标数据发送服务，用于发送采集数据。
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 3 14:55:08
 */
public interface GatherDataSendService {

    /**
     * send:
     * <p>发送采集指标数据
     *
     * @param indicatorValue
     *
     * @since   v1.01
     */
    public void send(IndicatorValue indicatorValue);
}

