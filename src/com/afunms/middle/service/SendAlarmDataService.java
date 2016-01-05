/*
 * @(#)SendAlarmDataService.java     v1.01, Dec 3, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.service;

import java.util.Timer;

import com.afunms.middle.task.SendAlarmDataTask;


/**
 * ClassName:   SendAlarmDataService.java
 * <p>
 *
 * @author      吴品龙
 * @version     v1.01
 * @since       v1.01
 * @Date        Dec 3, 2013 9:24:53 PM
 * @mail		wupinlong@dhcc.com.cn
 */
public class SendAlarmDataService {
	
	/**
	 * instance:
	 * <p>
	 *
	 * @since   v1.01
	 */
	private static SendAlarmDataService instance = null;
	
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
	private SendAlarmDataTask task = null;
	
	/**
	 * intervalTime:
	 * <p>
	 *
	 * @since   v1.01
	 */
	private Long intervalTime = 5000 * 60L;
	
	/**
	 * getInstance:单例模式，实例化
	 * <p>
	 * 
	 * @return
	 * 
	 * @since v1.01
	 */
	public static SendAlarmDataService getInstance() {
		if (instance == null) {
			instance = new SendAlarmDataService();
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
			task = new SendAlarmDataTask();
		}
		if (timer == null) {
			timer = new Timer();
		}
		timer.schedule(task, 100L, intervalTime);
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

