/*
 * @(#)AlarmIndicatorValueAction.java     v1.01, 2014 1 3
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action;

import java.util.List;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.CheckEventUtil;

/**
 * ClassName:   AlarmIndicatorValueAction.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 3 09:14:52
 */
public abstract class AlarmIndicatorValueAction extends BaseIndicatorValueAction {

    /**
     * execute:
     *
     *
     * @since   v1.01
     * @see com.afunms.node.indicator.action.BaseIndicatorValueAction#execute()
     */
    @Override
    public void execute() {
        executeToAlarm();
    }

    /**
     * executeToDB:
     * <p>执行告警操作
     *
     *
     * @since   v1.01
     */
    public abstract void executeToAlarm();

    public void executeToAlarm(String alarmIndicatorName, String value) {
        AlarmIndicatorsNode alarmIndicatorsnode = getAlarmIndicatorsNode(alarmIndicatorName);
        executeToAlarm(alarmIndicatorsnode, value);
    }

    public void executeToAlarm(AlarmIndicatorsNode alarmIndicatorsnode, String value) {
        if (alarmIndicatorsnode == null) {
            return;
        }
        CheckEventUtil checkutil = new CheckEventUtil();
        checkutil.checkEvent(getNode(), alarmIndicatorsnode, value);
    }

    @SuppressWarnings("unchecked")
    public AlarmIndicatorsNode getAlarmIndicatorsNode(String name) {
        AlarmIndicatorsNode alarmIndicatorsNode = null;
        List<AlarmIndicatorsNode> list = getAlarmIndicatorsNodeList();
        if (list != null) {
            for (AlarmIndicatorsNode alarmIndicatorsNodePer : list) {
                if (alarmIndicatorsNodePer.getName().equalsIgnoreCase(name)) {
                    alarmIndicatorsNode = alarmIndicatorsNodePer;
                }
            }
        }
        return alarmIndicatorsNode;
    }

    @SuppressWarnings("unchecked")
    public List<AlarmIndicatorsNode> getAlarmIndicatorsNodeList() {
        AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
        List<AlarmIndicatorsNode> list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(getNodeId(), getNodeType(), getNodeSubtype());
        return list;
    }
}

