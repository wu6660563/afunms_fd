/*
 * @(#)JMSAlarmSendServiceInitializeListener.java     v1.01, Jan 20, 2014
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.initialize.listener;

import com.afunms.middle.service.SendAlarmDataService;

public class JMSAlarmSendServiceInitializeListener extends AbstractInitializeListener {

	public boolean init(String configFile) {
		SendAlarmDataService.getInstance().start();
		return true;
	}

}

