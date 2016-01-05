/*
 * @(#)SendSVGTask.java     v1.01, Dec 4, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.task;

import java.util.TimerTask;

import com.afunms.middle.util.MQSend;
import com.afunms.middle.util.SVGCreate;

/**
 * ClassName: SendSVGTask.java
 * <p>
 * 
 * @author ÎâÆ·Áú
 * @version v1.01
 * @since v1.01
 * @Date Dec 4, 2013 9:28:14 AM
 * @mail wupinlong@dhcc.com.cn
 */
public class SendSVGTask extends TimerTask {

	@Override
	public void run() {
		// Éú³ÉSVG
		String topoXmlName = "network.jsp";

		MQSend mqSend = new MQSend("topic_fs_donghua_all_nr");
		try {
			String content = SVGCreate.createSVG(topoXmlName);
			mqSend.sendMessage(content.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mqSend.close();
		}
	}

}
