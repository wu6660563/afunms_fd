/*
 * @(#)IpMacAlarmIndicatorValueAction.java     v1.01, Feb 17, 2014
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.alarm;

import java.util.Vector;

import com.afunms.common.util.CheckEventUtil;
import com.afunms.node.indicator.action.AlarmIndicatorValueAction;
import com.afunms.polling.om.IpMac;

public class IpMacAlarmIndicatorValueAction extends AlarmIndicatorValueAction{
	
	private final static String ALARM_INDICATOR_NAME = "ipmac";

	@Override
	public void executeToAlarm() {
		Vector<IpMac> vector = (Vector<IpMac>) getIndicatorValue().getValue();
		if(vector == null || vector.size() == 0) {
			return;
		}
		CheckEventUtil checkutil = new CheckEventUtil();
		checkutil.createArpEventList(getNode(), vector, getAlarmIndicatorsNode(ALARM_INDICATOR_NAME));
	}

}

