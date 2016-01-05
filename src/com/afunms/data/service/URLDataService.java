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
 * <p> {@link URLDataService} �����ṩ {@link URLConfig} ��ϸ��Ϣҳ���е���Ϣ
 *
 * @author      ����
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
     * ��д����ķ��������������
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
     * <p>��ȡ���澯�ȼ�
     *
     * @return  {@link String}
     *          - ���澯�ȼ�
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

