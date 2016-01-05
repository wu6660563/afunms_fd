/*
 * @(#)CreateReportRunnable.java     v1.01, Jun 20, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.capreport.service;

import java.util.List;

import com.afunms.capreport.model.CustomReportVo;

/**
 * ClassName: CreateReportRunnable.java
 * <p>
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Jun 20, 2013 9:31:26 PM
 */
public class CreateReportRunnable implements Runnable {

	/**
	 * isStart:是否
	 * <p>
	 * 
	 * @since v1.01
	 */
	private boolean isStart;

	private Thread thread = new Thread();

	private boolean isTerminate = false;

	private ReportService service;

	private List<CustomReportVo> buffer;

	private CustomReportVo vo = null;

	public CreateReportRunnable(ReportService service) {
		setService(service);
		this.start();
	}

	/**
	 * run:
	 * 
	 * 
	 * @since v1.01
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		while (true) {
			try {
				// 如果缓冲区内没有任何需要执行的 ThreadPoolRunnable.

				synchronized (this) {
					while (buffer == null || buffer.size() == 0) {
						this.wait();
						if (isTerminate) {
							buffer = null;
							break;
						}
					}
					if (isTerminate) {
						break;
					}
					// 取到第一个
					vo = getService().popBuffer();
				}
				// 执行生成报表
				getService().createReport(vo);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void start() {
		this.isTerminate = false;
		setBuffer(getService().getBuffer());
		this.thread = new Thread(this);
		this.thread.start();
	}

	/**
	 * 停止监控线程，与 <code>terminate()</code> 效果相同
	 * 
	 * @see #terminate()
	 */
	public void stop() {
		this.terminate();
	}

	/**
	 * 执行缓冲线程
	 */
	public synchronized void runIt() {
		this.isTerminate = false;
		this.notify();
	}

	/**
	 * 中断缓冲线程
	 */
	public synchronized void terminate() {
		this.isTerminate = true;
		this.notify();
	}

	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public ReportService getService() {
		return service;
	}

	public void setService(ReportService service) {
		this.service = service;
	}

	/**
	 * isTerminate:
	 * <p>
	 * 
	 * @return boolean -
	 * @since v1.01
	 */
	public boolean isTerminate() {
		return isTerminate;
	}

	/**
	 * setTerminate:
	 * <p>
	 * 
	 * @param isTerminate -
	 * @since v1.01
	 */
	public void setTerminate(boolean isTerminate) {
		this.isTerminate = isTerminate;
	}

	/**
	 * getBuffer:
	 * <p>
	 * 
	 * @return List<CustomReportVo> -
	 * @since v1.01
	 */
	public List<CustomReportVo> getBuffer() {
		return buffer;
	}

	/**
	 * setBuffer:
	 * <p>
	 * 
	 * @param buffer -
	 * @since v1.01
	 */
	public void setBuffer(List<CustomReportVo> buffer) {
		this.buffer = buffer;
	}

}
