/*
 * @(#)ReSerialServices.java     v1.01, Jun 28, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.gatherdb;

import java.util.Timer;

/**
 * 
 * ClassName:   ReSerialServices.java
 * <p>
 *
 * @author      ��Ʒ��
 * @version     v1.01
 * @since       v1.01
 * @Date        Jul 1, 2013 9:49:44 AM
 */
public class ReSerialServices {

	/**
	 * instance:���õ���ģʽ
	 * <p>
	 * 
	 * @since v1.01
	 */
	private static ReSerialServices instance = null;

	/**
	 * timer:��ʱ��Timer
	 * <p>
	 * 
	 * @since v1.01
	 */
	private Timer timer;

	/**
	 * task:Taskʵ��
	 * <p>
	 * 
	 * @since v1.01
	 */
	private ReserialObjectTask task = null;

	/**
	 * intervalTime:
	 * <p>
	 * 
	 * @since v1.01
	 */
	private Long intervalTime = 3000 * 60L;

	private ReSerialServices() {

	}

	/**
	 * getInstance:����ģʽ��ʵ����
	 * <p>
	 * 
	 * @return
	 * 
	 * @since v1.01
	 */
	public static ReSerialServices getInstance() {
		if (instance == null) {
			instance = new ReSerialServices();
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
			task = new ReserialObjectTask();
		}
		if (timer == null) {
			timer = new Timer();
		}
		timer.schedule(task, 0L, intervalTime);
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
