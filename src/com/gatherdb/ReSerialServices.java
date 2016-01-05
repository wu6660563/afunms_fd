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
 * @author      吴品龙
 * @version     v1.01
 * @since       v1.01
 * @Date        Jul 1, 2013 9:49:44 AM
 */
public class ReSerialServices {

	/**
	 * instance:采用单例模式
	 * <p>
	 * 
	 * @since v1.01
	 */
	private static ReSerialServices instance = null;

	/**
	 * timer:定时器Timer
	 * <p>
	 * 
	 * @since v1.01
	 */
	private Timer timer;

	/**
	 * task:Task实例
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
	 * getInstance:单例模式，实例化
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
	 * start: 初始化，启动
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
	 * stop:停止
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
