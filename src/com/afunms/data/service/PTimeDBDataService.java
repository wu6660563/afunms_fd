/*
 * @(#)PTimeDBDataService.java     v1.01, May 15, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.data.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import com.afunms.alarm.service.NodeAlarmService;
import com.afunms.application.dao.ApplicationNodeDao;
import com.afunms.common.base.BaseVo;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.node.dao.PerformaceInfoTableDao;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.temp.dao.PingTempDao;

/**
 * ClassName:   PTimeDBDataService.java
 * <p>
 *
 * @author      吴品龙
 * @version     v1.01
 * @since       v1.01
 * @Date        May 15, 2013 9:28:31 AM
 */
public class PTimeDBDataService extends NodeDataService {
	/**
     * 
     */
    public PTimeDBDataService() {
        super();
    }

    /**
     * @param baseVo
     */
    public PTimeDBDataService(BaseVo baseVo) {
        super(baseVo);
    }

    /**
     * @param nodeDTO
     */
    public PTimeDBDataService(NodeDTO nodeDTO) {
        super(nodeDTO);
    }

    /**
     * @param nodeid
     * @param type
     * @param subtype
     */
    public PTimeDBDataService(String nodeid, String type, String subtype) {
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
        ApplicationNodeDao nodeDao = new ApplicationNodeDao();
        try {
            baseVo = nodeDao.findByID(nodeid);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
        	nodeDao.close();
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
     * 
     * getCurPingValue:
     * <p>
     *
     * @return pingValue
     *
     * @since   v1.01
     */
    public String getCurPingValue(){
    	String curPingValue = "0";	//当前PING值
    	
    	PingTempDao dao = new PingTempDao();
    	Vector nodeVector = null;
    	try {
    		nodeVector = dao.getPingInfo(nodeDTO.getNodeid(), nodeDTO.getType(), nodeDTO.getSubtype());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
    	if(nodeVector != null && nodeVector.size() > 0) {
    		for (int i = 0; i < nodeVector.size(); i++) {
        		Pingcollectdata data = (Pingcollectdata) nodeVector.elementAt(i);
        		if("ConnectUtilization".equals(data.getSubentity())) {
        			curPingValue = data.getThevalue();
        		}
    		}
    	}
		return curPingValue;
    }
    
    public String getDayAvgPingValue(){
    	String dayAvgPingValue = "0"; //avg
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = new Date();
    	String datestring = sdf.format(date);
    	String startdate = datestring + " 00:00:00";
    	String enddate = datestring + " 23:59:59";
    	PerformaceInfoTableDao dao = new PerformaceInfoTableDao("ptimedbping"+nodeDTO.getNodeid());
    	String[] subentitys = {"ConnectUtilization"};
    	try {
    		dayAvgPingValue = dao.getAvgThevalue(startdate, enddate, subentitys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		return dayAvgPingValue;
    }
    
    public String getDayMaxPingValue(){
    	String dayMaxPingValue = "0"; //max
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = new Date();
    	String datestring = sdf.format(date);
    	String startdate = datestring + " 00:00:00";
    	String enddate = datestring + " 23:59:59";
    	PerformaceInfoTableDao dao = new PerformaceInfoTableDao("ptimedbping"+nodeDTO.getNodeid());
    	String[] subentitys = {"ConnectUtilization"};
    	try {
    		dayMaxPingValue = dao.getMaxThevalue(startdate, enddate, subentitys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		return dayMaxPingValue;
    }
    
}

