/*
 * @(#)GatherIndicatorInfoService.java     v1.01, 2014 1 12
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.afunms.common.base.BaseVo;
import com.afunms.common.util.SysLogger;
import com.afunms.gather.model.IndicatorInfo;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.Constant;
import com.afunms.indicators.util.NodeGatherIndicatorsUtil;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.node.model.Category;
import com.afunms.node.model.Indicator;
import com.afunms.node.model.NodeDomain;
import com.afunms.node.service.CategoryService;
import com.afunms.node.service.NodeDomainService;

/**
 * ClassName:   GatherIndicatorInfoService.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 12 13:47:28
 */
public class GatherIndicatorInfoService {

    /**
     * logger:
     * <p>日志
     *
     * @since   v1.01
     */
    private static SysLogger logger = SysLogger.getLogger(GatherIndicatorInfoService.class.getName());

    /**
     * ALL_DOMAIN:
     * <p>所有域
     *
     * @since   v1.01
     */
    private static final String ALL_DOMAIN = "-1";
    /**
     * nodeDomainHashtable:
     * <p>所有设备所在与的 {@link Hashtable}
     *
     * @since   v1.01
     */
    private Hashtable<String, NodeDomain> nodeDomainHashtable = new Hashtable<String, NodeDomain>();

    /**
     * getAllIndicatorInfo:
     * <p>获取所有采集信息类
     *
     * @return  {@link List<IndicatorInfo>}
     *          - 所有采集信息类
     *
     * @since   v1.01
     */
    public List<IndicatorInfo> getAllIndicatorInfo() {
        return getAllIndicatorInfo(ALL_DOMAIN);
    }

    /**
     * getAllIndicatorInfo:
     * <p>根据所在域获取所有的采集信息类，如果域为 null 或者 -1，则返回所有采集信息类
     *
     * @param   domain
     *          - 所在域
     * @return  {@link List<IndicatorInfo>}
     *          - 所有采集信息类
     *
     * @since   v1.01
     */
    @SuppressWarnings("static-access")
    public List<IndicatorInfo> getAllIndicatorInfo(String domain) {
        List<IndicatorInfo> list = new ArrayList<IndicatorInfo>();
        try {
            // 刷新设备所在域的 {@link Hashtable}
            refreshNodeDomainHashtable();
            NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
            List<NodeGatherIndicators> nodeGatherIndicatorsList = nodeGatherIndicatorsUtil.loadAll();

            NodeUtil nodeUtil = new NodeUtil();
            List<BaseVo> baseVoList = nodeUtil.getNodeByTyeAndSubtype(Constant.ALL_TYPE, Constant.ALL_SUBTYPE);
            Hashtable<String, NodeDTO> nodeHashtable = new Hashtable<String, NodeDTO>();
            Hashtable<String, BaseVo> baseVoHashtable = new Hashtable<String, BaseVo>();
            for (BaseVo baseVo : baseVoList) {
                NodeDTO node = nodeUtil.conversionToNodeDTO(baseVo);
                String key = node.getNodeid() + ":" + node.getType() + ":" + node.getSubtype();
                nodeHashtable.put(key, node);
                baseVoHashtable.put(key, baseVo);
            }

            
            for (NodeGatherIndicators nodeGatherIndicators : nodeGatherIndicatorsList) {
                String key = nodeGatherIndicators.getNodeid() + ":" + nodeGatherIndicators.getType() + ":" + nodeGatherIndicators.getSubtype();
                if (!checkDomain(nodeGatherIndicators, domain)) {
                    continue;
                }
                if (baseVoHashtable.containsKey(key) && nodeHashtable.containsKey(key)) {
                    IndicatorInfo indicatorInfo = new IndicatorInfo();
                    indicatorInfo.setBaseVo(baseVoHashtable.get(key));
                    indicatorInfo.setGatherIndicators(nodeGatherIndicators);
                    indicatorInfo.setNodeDTO(nodeHashtable.get(key));
                    if (!setInicatorGather(indicatorInfo)) {
                        continue;
                    }
                    list.add(indicatorInfo);
                } else {
                    logger.info("发现无效的指标,设备：" + key + ", 指标名称:" + nodeGatherIndicators.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * setInicatorGather:
     * <p>设置指标采集类
     *
     * @param   indicatorInfo
     *          - 指标信息
     *
     * @since   v1.01
     */
    public boolean setInicatorGather(IndicatorInfo indicatorInfo) {
        CategoryService categoryService = new CategoryService();
        Category category = categoryService.getCategory(indicatorInfo.getNodeDTO());
        List<Indicator> list = category.getIndicatorList();
        String name = indicatorInfo.getGatherIndicators().getName();
        boolean result = false;
        for (Indicator indicator : list) {
            if (name.equals(indicator.getName())) {
                String indicatorGather = indicator.getIndicatorGather();
                indicatorInfo.getGatherIndicators().setClasspath(indicatorGather);
                result = true;
            }
        }
        return result;
    }
    /**
     * checkDomain:
     * <p>检查指标所在的域
     *
     * @param   indicatorInfo
     *          - 指标信息
     * @param   domain
     *          - 域
     * @return
     *
     * @since   v1.01
     */
    public boolean checkDomain(NodeGatherIndicators nodeGatherIndicators, String domain) {
        if (domain == null || ALL_DOMAIN.equals(domain)) {
            return true;
        }
        boolean result = false;
        String key = nodeGatherIndicators.getNodeid() + ":" + nodeGatherIndicators.getType() + ":" + nodeGatherIndicators.getSubtype();
        NodeDomain nodeDomain = getNodeDomainHashtable().get(key);
        if (nodeDomain != null) {
            if (domain.equals(String.valueOf(nodeDomain.getDomain()))) {
                result = true;
            }
        }
        return result;
    }

    /**
     * refreshNodeDomainHashtable:
     * <p>刷新设备所在域的 {@link Hashtable}
     *
     *
     * @since   v1.01
     */
    public void refreshNodeDomainHashtable() {
        // 先查出所有设备所属的域
        Hashtable<String, NodeDomain> nodeDomainHashtable = getNodeDomainHashtable();
        if (nodeDomainHashtable == null) {
            nodeDomainHashtable = new Hashtable<String, NodeDomain>();
            setNodeDomainHashtable(nodeDomainHashtable);
        }
        // 先清空
        nodeDomainHashtable.clear();
        // 再设置
        NodeDomainService nodeDomainService = new NodeDomainService();
        List<NodeDomain> nodeDomainList = nodeDomainService.getAllNodeDomain();
        if (nodeDomainList != null) {
            for (NodeDomain nodeDomain : nodeDomainList) {
                String key = nodeDomain.getNodeId() + ":" + nodeDomain.getNodeType() + ":" + nodeDomain.getNodeSubtype();
                nodeDomainHashtable.put(key, nodeDomain);
            }
        }
    }
    /**
     * getNodeDomainHashtable:
     * <p>
     *
     * @return  Hashtable<String,NodeDomain>
     *          -
     * @since   v1.01
     */
    public Hashtable<String, NodeDomain> getNodeDomainHashtable() {
        return nodeDomainHashtable;
    }

    /**
     * setNodeDomainHashtable:
     * <p>
     *
     * @param   nodeDomainHashtable
     *          -
     * @since   v1.01
     */
    public void setNodeDomainHashtable(
                    Hashtable<String, NodeDomain> nodeDomainHashtable) {
        this.nodeDomainHashtable = nodeDomainHashtable;
    }
    
}

