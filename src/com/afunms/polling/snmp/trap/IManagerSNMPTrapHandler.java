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
 * <p>{@link IManagerSNMPTrapHandler} ��Ϊ SDH ������� IManager 2000 �� Trap ����
 * 
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        Dec 3, 2012 5:26:17 PM
 */
public class IManagerSNMPTrapHandler implements SNMPTrapHandler {

    /**
     * serialVersionUID:
     * <p>���л� Id
     * 
     * @since   v1.01
     */
    private static final long serialVersionUID = 4803204067971581659L;

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private static SysLogger logger = SysLogger.getLogger(IManagerSNMPTrapHandler.class);
    
    // ��ΪSDH�ӿ�trap����ʱ����ʱ��oid��Ϣ
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
        		// ����
        		trap.setName(variableData);
        	} else if ("1.3.6.1.4.1.2011.2.15.1.7.1.2.0".equals(variableOid)) {
        		// �豸����
        		trap.setDeviceType(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.3.0".equals(variableOid)) {
        		// Ԫ��ʵ��
        		trap.setElementInstance(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.4.0".equals(variableOid)) {
        		// ��������¼�����
        		trap.setNetworkManagementType(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.5.0".equals(variableOid)) {
        		// �澯����ʱ��
        		trap.setAlarmCreateTime(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.6.0".equals(variableOid)) {
        		// ����澯��ԭ��
        		trap.setReasonOfCausingAlarm(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.7.0".equals(variableOid)) {
        		// �澯�ȼ�
        		trap.setAlarmLevel(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.8.0".equals(variableOid)) {
        		// �澯��Ϣ
        		trap.setAlarmInfo(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.9.0".equals(variableOid)) {
        		// ����ĸ澯��Ϣ
        		trap.setAlarmAdditionalInfo(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.10.0".equals(variableOid)) {
        		// �澯��ʶ
        		trap.setAlarmFlag(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.11.0".equals(variableOid)) {
        		// �澯Դ����
        		trap.setAlarmFunctionType(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.12.0".equals(variableOid)) {
        		// �豸IP��ַ
        		trap.setDeviceIP(variableData);
        		deviceIP = variableData;
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.13.0".equals(variableOid)) {
        		// �澯���к�
        		trap.setAlarmNumber(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.14.0".equals(variableOid)) {
        		// ����澯ʱ�Ŀ��ܽ���
        		trap.setAdviceOfReparingAlarm(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.15.0".equals(variableOid)) {
        		// ��Դ��
        		trap.setResourceID(variableData);
        	}else if ("1.3.6.1.4.1.2011.2.15.1.7.1.24.0".equals(variableOid)) {
        		//  ʱ������
        		trap.setEventName(variableData);
        	}
            //logger.info(trap.toString());
        }//end For
        boolean flag = false;
        IManagerTrapDao trapDao = new IManagerTrapDao();
        try {
        	trap.setCollecttime(simpleDateFormat.format(new Date()));
        	logger.info("��ʼ���� imanager trap��ϢΪ��" + trap);
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
		
		// �澯����
		String trapName = trap.getName();
		String uniqueKey = "";
		// ����trap������ȡ�ؼ���
		if(null != trapName && trapName.trim().length()!=0) {
			int pos = trapName.indexOf("-");
			uniqueKey = trapName.substring(0, pos);
		}
		
		// ���ݹؼ����ȡӦ�ýڵ�
		ApplicationNode applicationNode = new ApplicationNode();
		ApplicationNodeDao nodeDao = new ApplicationNodeDao();
		try {
			applicationNode = (ApplicationNode) nodeDao.findByUniquekey(uniqueKey);
		} catch (RuntimeException e) {
			logger.error(this.getClass().getName()+" ���ݹؼ����ѯӦ�ýڵ�ʧ�ܣ�",e);
			return;
		} finally {
			nodeDao.close();
		}
		
		// ���ݽڵ���Ϣ��ȡ�澯ָ����Ϣ
		AlarmIndicatorsNode nm = new AlarmIndicatorsNode();
		AlarmIndicatorsNodeDao alarmIndicatorsNodeDao = new AlarmIndicatorsNodeDao();
		try {
			String condition = "where nodeid=" + applicationNode.getId() + " and subtype='" + "sdh";
			alarmIndicatorsNodeDao.findByCondition(condition);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+ "��ѯ�澯ָ��ڵ�ʧ�ܣ�", e);
			return;
		} finally {
			alarmIndicatorsNodeDao.close();
		}
		
		// ��ʼ���и澯����
		CheckEventUtil checkEventUtil = new CheckEventUtil();
		checkEventUtil.checkImanagerTrapEvent(applicationNode, trap, nm);
    }
    
    public static void main(String[] args) {
    	String trapName = "12-�������ľ�";
		String uniqueKey = "";
		if(null != trapName && trapName.trim().length()!=0) {
			int pos = trapName.indexOf("-");
			uniqueKey = trapName.substring(0, pos);
		}
		logger.info(uniqueKey);
		System.out.println(uniqueKey);
    }

    
}
