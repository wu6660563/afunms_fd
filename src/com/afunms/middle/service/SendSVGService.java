/*
 * @(#)SendSVGService.java     v1.01, Dec 4, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.service;

import java.util.Timer;

import com.afunms.middle.task.SendSVGTask;

/**
 * ClassName:   SendSVGService.java
 * <p>
 *
 * @author      ��Ʒ��
 * @version     v1.01
 * @since       v1.01
 * @Date        Dec 4, 2013 9:30:28 AM
 * @mail		wupinlong@dhcc.com.cn
 */
public class SendSVGService {
	/**
	 * instance:
	 * <p>
	 *
	 * @since   v1.01
	 */
	private static SendSVGService instance = null;
	
	/**
	 * timer:
	 * <p>
	 *
	 * @since   v1.01
	 */
	private Timer timer;
	
	/**
	 * task:
	 * <p>
	 *
	 * @since   v1.01
	 */
	private SendSVGTask task = null;
	
	/**
	 * intervalTime:
	 * <p>
	 *
	 * @since   v1.01
	 */
	private Long intervalTime = 5000 * 60L;
	
	/**
	 * getInstance:����ģʽ��ʵ����
	 * <p>
	 * 
	 * @return
	 * 
	 * @since v1.01
	 */
	public static SendSVGService getInstance() {
		if (instance == null) {
			instance = new SendSVGService();
		}
		return instance;
	}
	
	/**
	 * start: ��ʼ��������
	 * <p>
	 * 
	 * 
	 * @since v1.01
	 */
	public void start() {
		if (task == null) {
			task = new SendSVGTask();
		}
		if (timer == null) {
			timer = new Timer();
		}
		timer.schedule(task, 100L, intervalTime);
	}
	
	/**
	 * stop:ֹͣ
	 * <p>
	 * 
	 * 
	 * @since v1.01
	 */
	public void stop() {
		task.cancel();
		timer.purge();
		timer.cancel();
	}
}

