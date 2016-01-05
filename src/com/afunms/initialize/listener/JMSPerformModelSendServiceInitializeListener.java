/*
 * @(#)JMSModelSendServiceInitializeListener.java     v1.01, Jul 16, 2014
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.initialize.listener;

import com.afunms.middle.service.SendPerfModelService;

/**
 * ClassName:   JMSModelSendServiceInitializeListener.java
 * <p> ����ģ�Ͷ������ݴ���
 *
 * @author      ��Ʒ��
 * @version     v1.01
 * @since       v1.01
 * @Date        Jul 16, 2014 2:35:34 PM
 * @mail		wupinlong@dhcc.com.cn
 */
public class JMSPerformModelSendServiceInitializeListener  extends AbstractInitializeListener {

	public boolean init(String configFile) {
		SendPerfModelService.getInstance().start();
		return true;
	}

}

