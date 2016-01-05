/*
 * @(#)SendModelService.java     v1.01, Jul 16, 2014
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.service;

import java.util.Timer;

import com.afunms.middle.task.SendModelObjectTask;

/**
 * ClassName:   SendModelService.java
 * <p>
 *
 * @author      吴品龙
 * @version     v1.01
 * @since       v1.01
 * @Date        Jul 16, 2014 2:37:08 PM
 * @mail		wupinlong@dhcc.com.cn
 */
public class SendModelObjectService {
	
	/**
	 * instance:
	 * <p>
	 *
	 * @since   v1.01
	 */
	private static SendModelObjectService instance = null;
	
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
	private SendModelObjectTask task = null;
	
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
	public static SendModelObjectService getInstance() {
		if (instance == null) {
			instance = new SendModelObjectService();
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
			task = new SendModelObjectTask();
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

