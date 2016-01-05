/*
 * @(#)IndicatorGather.java     v1.01, Jan 4, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.api;

import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.Result;

/**
 * ClassName:   IndicatorGather.java
 * <p>{@link IndicatorGather} ָ��ɼ��ӿ�
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        Jan 4, 2013 9:01:02 AM
 */
public interface IndicatorGather {

    /**
     * getValue:
     * <p>��ȡ���
     *
     * @param   node
     *          - �豸
     * @param   nodeGatherIndicators
     *          - �ɼ�ָ��
     * @return  {@link Result}
     *          - ���زɼ����
     *
     * @since   v1.01
     */
    Result getValue(Node node, NodeGatherIndicators nodeGatherIndicators);
}

