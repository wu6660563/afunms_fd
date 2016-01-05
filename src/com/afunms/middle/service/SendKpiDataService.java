/*
 * @(#)SendKpiDataService.java     v1.01, Dec 4, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.service;

import java.util.Timer;

import com.afunms.middle.task.SendKpiDataTask;

/**
 * ClassName:   SendKpiDataService.java
 * <p>
 *
 * @author      吴品龙
 * @version     v1.01
 * @since       v1.01
 * @Date        Dec 4, 2013 10:17:01 AM
 * @mail		wupinlong@dhcc.com.cn
 */
public class SendKpiDataService {
	/**
	 * instance:
	 * <p>
	 *
	 * @since   v1.01
	 */
	private static SendKpiDataService instance = null;
	
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
	private SendKpiDataTask task = null;
	
	/**
	 * getInstance:单例模式，实例化
	 * <p>
	 * 
	 * @return
	 * 
	 * @since v1.01
	 */
	public static SendKpiDataService getInstance() {
		if (instance == null) {
			instance = new SendKpiDataService();
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
			task = new SendKpiDataTask();
		}
		if (timer == null) {
			timer = new Timer();
		}
		//启动后只执行一次
		timer.schedule(task, 100L);
	}
	
}

