/*
 * @(#)DataSendService.java     v1.01, 2013 12 25
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.service;

import com.afunms.alarm.model.AlarmWayDetail;
import com.afunms.common.base.BaseVo;
import com.afunms.event.model.CheckEvent;
import com.afunms.event.model.EventList;
import com.afunms.gather.model.IndicatorValue;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.node.model.NodeDomain;
import com.afunms.rmi.service.RMIParameter;
import com.afunms.synchronize.service.SynchronizeService;
import com.afunms.system.model.User;
import com.gatherdb.DBAttribute;

/**
 * ClassName:   DataSendService.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 25 15:05:32
 */
public class DataSendService {

    public static void sendAddNode(BaseVo baseVo) {
        NodeUtil util = new NodeUtil();
        NodeDTO nodeDTO = util.conversionToNodeDTO(baseVo);
        NodeDomainService service = new NodeDomainService();
        NodeDomain nodeDomain = service.getNodeDomain(nodeDTO);
        sendAddNode(baseVo, nodeDomain);
    }

    public static void sendAddNode(BaseVo baseVo, NodeDomain nodeDomain) {
        RMIParameter parameter = new RMIParameter();
        parameter.setParameter("operation", "add");
        parameter.setParameter("action", "NodeAction");
        parameter.setParameter("baseVo", baseVo);
        parameter.setParameter("nodeDomain", nodeDomain);
        send(parameter);
    }

    public static void sendDeleteNode(BaseVo baseVo, User user) {
        NodeUtil util = new NodeUtil();
        NodeDTO nodeDTO = util.conversionToNodeDTO(baseVo);
        NodeDomainService service = new NodeDomainService();
        NodeDomain nodeDomain = service.getNodeDomain(nodeDTO);
        sendDeleteNode(baseVo, user, nodeDomain);
    }

    public static void sendDeleteNode(BaseVo baseVo, User user, NodeDomain nodeDomain) {
        RMIParameter parameter = new RMIParameter();
        parameter.setParameter("operation", "delete");
        parameter.setParameter("action", "NodeAction");
        parameter.setParameter("baseVo", baseVo);
        parameter.setParameter("user", user);
        send(parameter);
    }

    public static void sendIndicatorValue(IndicatorValue indicatorValue) {
        RMIParameter parameter = new RMIParameter();
        parameter.setParameter("action", "IndicatorValueAction");
        parameter.setParameter("indicatorValue", indicatorValue);
        send(parameter);
    }

    public static void sendAlarm(CheckEvent checkEvent, EventList eventList, AlarmWayDetail alarmWayDetail) {
        RMIParameter parameter = new RMIParameter();
        parameter.setParameter("action", "AlarmAction");
        parameter.setParameter("checkEvent", checkEvent);
        parameter.setParameter("eventList", eventList);
        parameter.setParameter("alarmWayDetail", alarmWayDetail);
        send(parameter);
    }

    public static void sendDBAttribute(DBAttribute attribute) {
        RMIParameter parameter = new RMIParameter();
        parameter.setParameter("action", "DBAttributeAction");
        parameter.setParameter("DBAttribute", attribute);
        send(parameter);
    }

    public static void send(RMIParameter parameter) {
        SynchronizeService.getInstance().send(parameter);
    } 
}

