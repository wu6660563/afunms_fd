/*
 * @(#)JMSReceiveServiceInitializeListener.java     v1.01, Jan 20, 2014
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.initialize.listener;

import com.afunms.middle.util.JMSReceiveFactory;

public class JMSReceiveServiceInitializeListener extends AbstractInitializeListener {

	public boolean init(String configFile) {
		JMSReceiveFactory.getInstance().startReceive();
		return true;
	}

}

