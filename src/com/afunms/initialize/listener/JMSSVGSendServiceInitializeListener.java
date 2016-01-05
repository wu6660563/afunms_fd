/*
 * @(#)JMSSVGSendServiceInitializeListener.java     v1.01, Jan 20, 2014
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.initialize.listener;

import com.afunms.middle.service.SendSVGService;

public class JMSSVGSendServiceInitializeListener extends AbstractInitializeListener {

	public boolean init(String configFile) {
		SendSVGService.getInstance().start();
		return true;
	}

}

