/*
 * @(#)PIXDataService.java     v1.01, May 7, 2014
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.data.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.alarm.service.NodeAlarmService;
import com.afunms.common.base.BaseVo;
import com.afunms.detail.service.pingInfo.PingInfoService;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.node.dao.PerformaceInfoTableDao;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.temp.dao.PingTempDao;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;

/**
 * ClassName:   PIXDataService.java
 * <p>
 *
 * @author      ��Ʒ��
 * @version     v1.01
 * @since       v1.01
 * @Date        May 7, 2014 8:49:22 PM
 * @mail		wupinlong@dhcc.com.cn
 */
public class PIXDataService extends NodeDataService{
	/**
     * 
     */
    public PIXDataService() {
        super();
    }

    /**
     * @param baseVo
     */
    public PIXDataService(BaseVo baseVo) {
        super(baseVo);
    }

    /**
     * @param nodeDTO
     */
    public PIXDataService(NodeDTO nodeDTO) {
        super(nodeDTO);
    }

    /**
     * @param nodeid
     * @param type
     * @param subtype
     */
    public PIXDataService(String nodeid, String type, String subtype) {
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
		HostNodeDao hostNodeDao = new HostNodeDao();
		try {
			baseVo = (HostNode) hostNodeDao.findByID(nodeid);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			hostNodeDao.close();
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
        int maxAlarmLevel = nodeAlarmService.getMaxAlarmLevel(nodeDTO);
        return String.valueOf(maxAlarmLevel);
    }
    
    /**
     * getCurDayPingValueHashtableInfo:
     * <p>��ȡ������ͨ��ֵ�� {@link Hashtable<String, String>} ��Ϣ
     *
     * @return  {@link Hashtable<String, String>}
     *          - ��ͨ��ֵ�� {@link Hashtable<String, String>} ��Ϣ
     *
     * @since   v1.01
     */
    public Hashtable<String, String> getCurDayPingValueHashtableInfo() {
        String nodeid = getNodeid();
        String type = getType();
        String subtype = getSubtype();
        PingInfoService pingInfoService = new PingInfoService(nodeid, type,
                subtype);
        return pingInfoService.getCurDayPingValueHashtableInfo(getIpaddress());
    }
    
}

