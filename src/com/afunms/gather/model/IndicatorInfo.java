/*
 * @(#)IndicatorInfo.java     v1.01, 2013 12 26
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.model;

import com.afunms.common.base.BaseVo;
import com.afunms.gather.indicator.IndicatorGather;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;

/**
 * ClassName:   IndicatorInfo.java
 * <p>{@link IndicatorInfo} ָ����Ϣ�࣬
 * ���� {@link NodeDTO}, {@link BaseVo}, {@link NodeGatherIndicators} �Ȳɼ�ʱ����Ĳ�����
 * �ڶ�ָ����вɼ�ǰ���ֲɼ����ĵ��ȳ��򽫸���Ϣ������ {@link IndicatorGather} ���С� 
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 26 22:18:36
 */
public class IndicatorInfo extends BaseVo {

    /**
     * serialVersionUID:
     * <p>
     *
     * @since   v1.01
     */
    private static final long serialVersionUID = 7806203184780371949L;

    /**
     * nodeDTO:
     * <p>�豸 {@link NodeDTO}
     *
     * @since   v1.01
     */
    private NodeDTO nodeDTO;

    /**
     * baseVo:
     * <p>�豸
     *
     * @since   v1.01
     */
    private BaseVo baseVo;

    /**
     * gatherIndicators:
     * <p>�ɼ�ָ��
     *
     * @since   v1.01
     */
    private NodeGatherIndicators gatherIndicators;

    /**
     * getNodeDTO:
     * <p>
     *
     * @return  NodeDTO
     *          -
     * @since   v1.01
     */
    public NodeDTO getNodeDTO() {
        return nodeDTO;
    }

    /**
     * setNodeDTO:
     * <p>
     *
     * @param   nodeDTO
     *          -
     * @since   v1.01
     */
    public void setNodeDTO(NodeDTO nodeDTO) {
        this.nodeDTO = nodeDTO;
    }

    /**
     * getBaseVo:
     * <p>
     *
     * @return  BaseVo
     *          -
     * @since   v1.01
     */
    public BaseVo getBaseVo() {
        return baseVo;
    }

    /**
     * setBaseVo:
     * <p>
     *
     * @param   baseVo
     *          -
     * @since   v1.01
     */
    public void setBaseVo(BaseVo baseVo) {
        this.baseVo = baseVo;
    }

    /**
     * getGatherIndicators:
     * <p>
     *
     * @return  NodeGatherIndicators
     *          -
     * @since   v1.01
     */
    public NodeGatherIndicators getGatherIndicators() {
        return gatherIndicators;
    }

    /**
     * setGatherIndicators:
     * <p>
     *
     * @param   gatherIndicators
     *          -
     * @since   v1.01
     */
    public void setGatherIndicators(NodeGatherIndicators gatherIndicators) {
        this.gatherIndicators = gatherIndicators;
    }

}

