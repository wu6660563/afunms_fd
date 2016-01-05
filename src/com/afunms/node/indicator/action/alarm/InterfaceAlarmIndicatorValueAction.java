/*
 * @(#)InterfaceAlarmIndicatorValueAction.java     v1.01, 2014 1 3
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.alarm;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.node.indicator.action.AlarmIndicatorValueAction;
import com.afunms.polling.om.Interfacecollectdata;

/**
 * ClassName:   InterfaceAlarmIndicatorValueAction.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 3 10:49:04
 */
public class InterfaceAlarmIndicatorValueAction extends AlarmIndicatorValueAction {

    private final static String ALARM_INDICATOR_NAME_INTERFACE = "interface";

    private final static String ALARM_INDICATOR_NAME_INBANDWIDTHUTILHDX = "InBandwidthUtilHdx";

    private final static String ALARM_INDICATOR_NAME_OUTBANDWIDTHUTILHDX = "OutBandwidthUtilHdx";

    private final static String ALARM_INDICATOR_NAME_ALLINBANDWIDTHUTILHDX = "AllInBandwidthUtilHdx";

    private final static String ALARM_INDICATOR_NAME_ALLOUTBANDWIDTHUTILHDX = "AllOutBandwidthUtilHdx";

    @SuppressWarnings("unchecked")
    @Override
    public void executeToAlarm() {
        Hashtable<String, Vector<Interfacecollectdata>> interfaceHashtable = (Hashtable<String, Vector<Interfacecollectdata>>) getIndicatorValue().getValue();
        Vector<Interfacecollectdata> interfaceVector = (Vector<Interfacecollectdata>) interfaceHashtable.get("interface");
        Vector<Interfacecollectdata> inUtilhdxVector = (Vector<Interfacecollectdata>) interfaceHashtable.get("inUtilhdx");
        Vector<Interfacecollectdata> outUtilhdxVector = (Vector<Interfacecollectdata>) interfaceHashtable.get("outUtilhdx");

        Vector<Interfacecollectdata> allutilhdxVector = (Vector<Interfacecollectdata>) interfaceHashtable.get("allutilhdx");

        List<AlarmIndicatorsNode> list = getAlarmIndicatorsNodeList();
        CheckEventUtil checkEventUtil = new CheckEventUtil();
        for (AlarmIndicatorsNode alarmIndicatorsNode : list) {
            String name = alarmIndicatorsNode.getName();
            if (ALARM_INDICATOR_NAME_INTERFACE.equalsIgnoreCase(alarmIndicatorsNode.getName())) {
                checkEventUtil.createInterfaceEventList(getNode(), interfaceVector, alarmIndicatorsNode);
            } else if (ALARM_INDICATOR_NAME_INBANDWIDTHUTILHDX.equalsIgnoreCase(name)) {
                checkEventUtil.checkUtilhdxEvent(getNode(), inUtilhdxVector, alarmIndicatorsNode);
            } else if (ALARM_INDICATOR_NAME_OUTBANDWIDTHUTILHDX.equalsIgnoreCase(name)) {
                checkEventUtil.checkUtilhdxEvent(getNode(), outUtilhdxVector, alarmIndicatorsNode);
            }  else if (ALARM_INDICATOR_NAME_ALLINBANDWIDTHUTILHDX.equalsIgnoreCase(name)) {
                for (Interfacecollectdata interfacecollectdata : allutilhdxVector) {
                    if ("AllInBandwidthUtilHdx".equalsIgnoreCase(interfacecollectdata.getEntity())) {
                        checkEventUtil.checkEvent(getNode(), alarmIndicatorsNode, interfacecollectdata.getThevalue());
                    }
                }
            }  else if (ALARM_INDICATOR_NAME_ALLOUTBANDWIDTHUTILHDX.equalsIgnoreCase(name)) {
                for (Interfacecollectdata interfacecollectdata : allutilhdxVector) {
                    if ("AllOutBandwidthUtilHdx".equalsIgnoreCase(interfacecollectdata.getEntity())) {
                        checkEventUtil.checkEvent(getNode(), alarmIndicatorsNode, interfacecollectdata.getThevalue());
                    }
                }
            } 
        }
    }

}

