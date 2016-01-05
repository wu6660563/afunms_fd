/*
 * @(#)JMSModelSendServiceInitializeListener.java     v1.01, Jul 16, 2014
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.initialize.listener;

import com.afunms.middle.service.SendPerformRunningSerivce;

/**
 * ClassName:   JMSModelSendServiceInitializeListener.java
 * <p> 推送性能运行数据代码
 *
 * @author      吴品龙
 * @version     v1.01
 * @since       v1.01
 * @Date        Jul 16, 2014 2:35:34 PM
 * @mail		wupinlong@dhcc.com.cn
 */
public class JMSPerformRunningServiceInitializeListener  extends AbstractInitializeListener {

	public boolean init(String configFile) {
		SendPerformRunningSerivce.getInstance().start();
		return true;
	}

}

