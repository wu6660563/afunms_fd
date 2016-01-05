/*
 * @(#)SendModelService.java     v1.01, Jul 16, 2014
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.service;

import java.util.Timer;

import com.afunms.middle.task.SendPerformModelTask;

/**
 * ClassName:   SendModelService.java
 * <p>
 *
 * @author      ��Ʒ��
 * @version     v1.01
 * @since       v1.01
 * @Date        Jul 16, 2014 2:37:08 PM
 * @mail		wupinlong@dhcc.com.cn
 */
public class SendPerfModelService {
	
	/**
	 * instance:
	 * <p>
	 *
	 * @since   v1.01
	 */
	private static SendPerfModelService instance = null;
	
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
	private SendPerformModelTask task = null;
	
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
	public static SendPerfModelService getInstance() {
		if (instance == null) {
			instance = new SendPerfModelService();
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
			task = new SendPerformModelTask();
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

