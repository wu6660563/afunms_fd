/*
 * @(#)JMSModelSendServiceInitializeListener.java     v1.01, Jul 16, 2014
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.initialize.listener;

import com.afunms.middle.service.SendModelObjectService;

/**
 * ClassName:   JMSModelObjectSendServiceInitializeListener.java
 * <p> 推送模型对象数据代码
 *
 * @author      吴品龙
 * @version     v1.01
 * @since       v1.01
 * @Date        Jul 16, 2014 2:35:34 PM
 * @mail		wupinlong@dhcc.com.cn
 */
public class JMSModelObjectSendServiceInitializeListener  extends AbstractInitializeListener {

	public boolean init(String configFile) {
		SendModelObjectService.getInstance().start();
		return true;
	}

}

