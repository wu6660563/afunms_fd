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
 * @author      吴品龙
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
     * 重写父类的方法用于提高性能
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
     * <p>获取最大告警等级
     *
     * @return  {@link String}
     *          - 最大告警等级
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
     * <p>获取当天连通率值的 {@link Hashtable<String, String>} 信息
     *
     * @return  {@link Hashtable<String, String>}
     *          - 连通率值的 {@link Hashtable<String, String>} 信息
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

