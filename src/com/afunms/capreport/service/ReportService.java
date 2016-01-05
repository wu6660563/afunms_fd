/*
 * @(#)CreateReportService.java     v1.01, Jun 20, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.capreport.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import javax.mail.Address;

import com.afunms.capreport.model.CustomReportVo;
import com.afunms.common.util.sendMail;

/**
 * ClassName: CreateReportService.java
 * <p>
 * 
 * @author ��Ʒ��
 * @version v1.01
 * @since v1.01
 * @Date Jun 20, 2013 9:26:34 PM
 */
public class ReportService {

	/**
	 * instance:���õ���ģʽ
	 * <p>
	 * 
	 * @since v1.01
	 */
	private static ReportService instance = null;

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
	private CreateReportTask task = null;

	/**
	 * createReportRunnable:�߳�
	 * <p>
	 * 
	 * @since v1.01
	 */
	private CreateReportRunnable createReportRunnable;

	/**
	 * buffer:�������List�������洢��Ҫ����
	 * <p>
	 * 
	 * @since v1.01
	 */
	private List<CustomReportVo> buffer = new ArrayList<CustomReportVo>();

	/**
	 * intervalTime:
	 * <p>
	 * 
	 * @since v1.01
	 */
	private Long intervalTime = 3000 * 60L;

	private ReportService() {

	}

	/**
	 * getInstance:����ģʽ��ʵ����
	 * <p>
	 * 
	 * @return
	 * 
	 * @since v1.01
	 */
	public static ReportService getInstance() {
		if (instance == null) {
			instance = new ReportService();
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
		if (createReportRunnable == null) {
			createReportRunnable = new CreateReportRunnable(this);
		}
		if (task == null) {
			task = new CreateReportTask();
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

	/**
	 * addBuffer:������󣬻����߳�
	 * <p>
	 * 
	 * @param vo
	 * 
	 * @since v1.01
	 */
	public void addBuffer(CustomReportVo vo) {
		buffer.add(vo);
		createReportRunnable.runIt();
	}

	public void addBufferAll(List<CustomReportVo> list) {
		buffer.addAll(list);
		createReportRunnable.runIt();
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

	/**
	 * createReport:������
	 * <p>
	 * 
	 * @param vo
	 * 
	 * @since v1.01
	 */
	public void createReport(CustomReportVo vo) {
		CreateReportService service = new CreateReportService();
		service.create(vo);
	}

	/**
	 * �ӻ������е���һ����һ����Ҫִ�е��߳�����
	 * 
	 * @return �������е�һ�����߳�����
	 */
	public synchronized CustomReportVo popBuffer() {
		CustomReportVo vo = null;
		if (buffer != null && buffer.size() > 0) {
			vo = this.buffer.remove(0);
		}
		return vo;
	}

	/**
	 * getIntervalTime:
	 * <p>
	 * 
	 * @return Long -
	 * @since v1.01
	 */
	public Long getIntervalTime() {
		return intervalTime;
	}
	

	public static void main(String[] args) {
		boolean flag = false;
		String receivemailaddr = "867965446@qq.com";
		String title = "�����ʼ�����";
		String body = "��������һ���ʼ��������ԣ���ע����գ�";
		String[] path ={"E:\\WorkSpace\\apache-tomcat-6.0.24\\apache-tomcat-6.0.24\\webapps\\afunms\\reportFile\\","E:\\WorkSpace\\apache-tomcat-6.0.24\\apache-tomcat-6.0.24\\webapps\\afunms\\reportFile\\"};
		String[] fileName = {"day_node2013-06-25_2013-06-25.doc","day_node2013-06-25_2013-06-25.xls"};
		
		String mailsmtp = "smtp.126.com";
		String mailaddr = "wu6660563@126.com";
		String mailpassword = "****";
		String fromAddr = "wu6660563@126.com";
		try {
			Address[] ccAddress = {null,null};
			sendMail sendmail = new sendMail(mailsmtp,mailaddr,
					mailpassword,receivemailaddr, title, body,fromAddr,ccAddress);
			
			try{
				flag = sendmail.sendmailHaveFile(path, fileName);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(">>>>>>>>>>>>"+flag);

	}

}
