/*
 * @(#)JMSKpiSendServiceInitializeListener.java     v1.01, Jan 20, 2014
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.initialize.listener;

import com.afunms.middle.service.SendKpiDataService;

public class JMSKpiSendServiceInitializeListener extends AbstractInitializeListener {

	public boolean init(String configFile) {
		SendKpiDataService.getInstance().start();
		return true;
	}

}

