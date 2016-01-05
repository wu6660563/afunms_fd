/*
 * @(#)URLDataService.java     v1.01, May 22, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.data.service;

import java.util.List;

import com.afunms.alarm.service.NodeAlarmService;
import com.afunms.application.dao.URLConfigDao;
import com.afunms.application.model.URLConfig;
import com.afunms.common.base.BaseVo;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.node.model.URLPerformanceInfo;
import com.afunms.node.service.URLPerformanceInfoService;


/**
 * ClassName:   URLDataService.java
 * <p> {@link URLDataService} 用于提供 {@link URLConfig} 详细信息页面中的信息
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        May 22, 2013 11:09:15 AM
 */
public class URLDataService extends NodeDataService {

    /**
     * 
     */
    public URLDataService() {
        super();
    }

    /**
     * @param baseVo
     */
    public URLDataService(BaseVo baseVo) {
        super(baseVo);
    }

    /**
     * @param nodeDTO
     */
    public URLDataService(NodeDTO nodeDTO) {
        super(nodeDTO);
    }

    /**
     * @param nodeid
     * @param type
     * @param subtype
     */
    public URLDataService(String nodeid, String type, String subtype) {
        super(nodeid, type, subtype);
    }

    /**
     * 重写父类的方法用于提高性能
     * 
     * @see com.afunms.data.service.NodeDataService#init(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public void init(String nodeid, String type, String subtype) {
        BaseVo baseVo = null;
        URLConfigDao dao = new URLConfigDao();
        try {
            System.out.println(nodeid);
            baseVo = dao.findByID(nodeid);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        super.init(baseVo);
    }

    /**
     * getMaxAlarmLevel:
     * <p>获取最大告警等级
     *
     * @return  {@link String}
     *          - 最大告警等级
     *
     * @since   v1.01
     */
    public String getMaxAlarmLevel() {
        NodeAlarmService nodeAlarmService = new NodeAlarmService();
        System.out.println(nodeDTO);
        int maxAlarmLevel = nodeAlarmService.getMaxAlarmLevel(nodeDTO);
        return String.valueOf(maxAlarmLevel);
    }

    public List<URLPerformanceInfo> getList(String startTime, String endTime) {
        URLPerformanceInfoService service = new URLPerformanceInfoService();
        return service.getPerformance(getNodeid(), startTime, endTime);
    }
}

