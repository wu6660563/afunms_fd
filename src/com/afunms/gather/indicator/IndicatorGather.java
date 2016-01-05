/*
 * @(#)IndicatorGather.java     v1.01, 2013 12 26
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator;

import com.afunms.gather.model.IndicatorValue;

/**
 * ClassName:   IndicatorGather.java
 * <p>指标采集类接口，提供获取采集指标值方法
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 26 22:55:46
 */
public interface IndicatorGather {

    /**
     * getIndicatorValue:
     * <p>获取采集指标值
     *
     * @return
     *
     * @since   v1.01
     */
    IndicatorValue getIndicatorValue();
}

