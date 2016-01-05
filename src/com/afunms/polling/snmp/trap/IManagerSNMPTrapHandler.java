/*
 * @(#)IManagerSNMPTrapHandler.java     v1.01, Dec 3, 2012
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.trap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.snmp4j.CommandResponderEvent;
import org.snmp4j.PDU;
import org.snmp4j.smi.VariableBinding;

import com.afunms.alarm.dao.AlarmIndicatorsNodeDao;
import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.application.dao.ApplicationNodeDao;
import com.afunms.application.model.ApplicationNode;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.SysLogger;
import com.afunms.event.dao.IManagerTrapDao;
import com.afunms.event.model.IManagerTrap;
import com.afunms.indicators.model.NodeDTO;

/**
 * ClassName: IManagerSNMPTrapHandler.java
 * <p>{@link IManagerSNMPTrapHandler} 华为 SDH 管理软件 IManager 2000 的 Trap 处理
 * 
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        Dec 3, 2012 5:26:17 PM
 */
public class IManagerSNMPTrapHandler implements SNMPTrapHandler {

    /**
     * serialVersionUID:
     * <p>序列化 Id
     * 
     * @since   v1.01
     */
    private static final long serialVersionUID = 4803204067971581659L;

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private static SysLogger logger = SysLogger.getLogger(IManagerSNMPTrapHandler.class);
    
    // 华为SDH接口trap正常时发送时的oid信息
    private static final String imanagerAliveTrapKeywords = "1.3.6.1.4.1.2011.2.15.1";
    
    /**
     * processPdu:
     * 
     * @param   arg0
     * 
     * @since   v1.01
     * @see     org.snmp4j.CommandResponder#processPdu(org.snmp4j.CommandResponderEvent)
     */
    public void processPdu(CommandResponderEvent event) {

    	String trapType = PDU.getTypeString(event.getPDU().getType());
        Vector<VariableBinding> vector = event.getPDU().getVariableBindings();
    
        IManagerTrap trap = new IManagerTrap();
        String deviceIP = "";
        for (VariableBinding variableBinding : vector) {
        	String variableData = variableBinding.getVariable().toString();
        	String variableOid = variableBinding.getOid().toString();
        	
        	//logger.info(variableBinding.getOid() + "====" + variableBinding.getVariable().toString() + "====" +  simpleDateFormat.format(new Date()));
        	logger.info(variableOid + "====" + variableData + "====" +  simpleDateFormat.format(new Date()));
        	//logger.info(variableOid);
        	
        	if(imanagerAliveTrapKeywords.equals(variableOid)){
        		return;
        	}
        	//logger.info(variableOid);
        	if ("1.3.6.1.4.1.2011.2.15.1.7.1.1.0".equals(variableOid)) {
        		// 名称
        		trap.setName(variableData);
        	} else if ("1.3.6.1.4.1.2011.2.15.1.7.1.2.0".equals(variableOid)) {
        		// 设备类型
        		trap.setDeviceType(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.3.0".equals(variableOid)) {
        		// 元素实例
        		trap.setElementInstance(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.4.0".equals(variableOid)) {
        		// 网络管理事件类型
        		trap.setNetworkManagementType(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.5.0".equals(variableOid)) {
        		// 告警发生时间
        		trap.setAlarmCreateTime(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.6.0".equals(variableOid)) {
        		// 引起告警的原因
        		trap.setReasonOfCausingAlarm(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.7.0".equals(variableOid)) {
        		// 告警等级
        		trap.setAlarmLevel(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.8.0".equals(variableOid)) {
        		// 告警信息
        		trap.setAlarmInfo(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.9.0".equals(variableOid)) {
        		// 额外的告警信息
        		trap.setAlarmAdditionalInfo(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.10.0".equals(variableOid)) {
        		// 告警标识
        		trap.setAlarmFlag(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.11.0".equals(variableOid)) {
        		// 告警源类型
        		trap.setAlarmFunctionType(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.12.0".equals(variableOid)) {
        		// 设备IP地址
        		trap.setDeviceIP(variableData);
        		deviceIP = variableData;
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.13.0".equals(variableOid)) {
        		// 告警序列号
        		trap.setAlarmNumber(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.14.0".equals(variableOid)) {
        		// 处理告警时的可能建议
        		trap.setAdviceOfReparingAlarm(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.15.0".equals(variableOid)) {
        		// 资源号
        		trap.setResourceID(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.24.0".equals(variableOid)) {
        		//  时间名称
        		trap.setEventName(variableData);
        	}
            //logger.info(trap.toString());
        }//end For
        boolean flag = false;
        IManagerTrapDao trapDao = new IManagerTrapDao();
        try {
        	trap.setCollecttime(simpleDateFormat.format(new Date()));
        	logger.info("开始保存 imanager trap信息为：" + trap);
			trapDao.save(trap);
			flag = true;
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			trapDao.close();
		}
		
		if(!flag) {
			return;
		}
		
		// 告警处理
		String trapName = trap.getName();
		String uniqueKey = "";
		// 根据trap名称提取关键码
		if(null != trapName && trapName.trim().length()!=0) {
			int pos = trapName.indexOf("-");
			uniqueKey = trapName.substring(0, pos);
		}
		
		// 根据关键码获取应用节点
		ApplicationNode applicationNode = new ApplicationNode();
		ApplicationNodeDao nodeDao = new ApplicationNodeDao();
		try {
			applicationNode = (ApplicationNode) nodeDao.findByUniquekey(uniqueKey);
		} catch (RuntimeException e) {
			logger.error(this.getClass().getName()+" 根据关键码查询应用节点失败！",e);
			return;
		} finally {
			nodeDao.close();
		}
		
		// 根据节点信息获取告警指标信息
		AlarmIndicatorsNode nm = new AlarmIndicatorsNode();
		AlarmIndicatorsNodeDao alarmIndicatorsNodeDao = new AlarmIndicatorsNodeDao();
		try {
			String condition = "where nodeid=" + applicationNode.getId() + " and subtype='" + "sdh";
			alarmIndicatorsNodeDao.findByCondition(condition);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+ "查询告警指标节点失败！", e);
			return;
		} finally {
			alarmIndicatorsNodeDao.close();
		}
		
		// 开始进行告警处理
		CheckEventUtil checkEventUtil = new CheckEventUtil();
		checkEventUtil.checkImanagerTrapEvent(applicationNode, trap, nm);
    }
    
    public static void main(String[] args) {
    	String trapName = "12-银川核心局";
		String uniqueKey = "";
		if(null != trapName && trapName.trim().length()!=0) {
			int pos = trapName.indexOf("-");
			uniqueKey = trapName.substring(0, pos);
		}
		logger.info(uniqueKey);
		System.out.println(uniqueKey);
    }

    
}
