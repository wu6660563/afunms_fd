/*
 * @(#)CreateReportService.java     v1.01, Jun 20, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.capreport.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.afunms.capreport.model.CustomReportVo;

/**
 * ClassName: CreateReportService.java
 * <p>
 * 
 * @author ��Ʒ��
 * @version v1.01
 * @since v1.01
 * @Date Jun 27, 2013 9:31:57 AM
 */
public class CreateReportService {

	/**
	 * sdf:
	 * <p>
	 * 
	 * @since v1.01
	 */
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * create:���ɱ�����
	 * <p>
	 * 
	 * @param vo
	 * 
	 * @since v1.01
	 */
	public void create(CustomReportVo vo) {
		// ���� CustomReportVo �����ж� �������ֱ����ڴ˴�ִ�����ɱ���ִ�в�����
		String temp = getTime(vo.getType());
		String[] temps = temp.split(",");
		String startTime = temps[0];
		String endTime = temps[1];
		if ("node".equals(vo.getCode())) {
			TestReportService services = new TestReportService();
			services.create(vo, startTime, endTime);
		}
	}

	/**
	 * getFileName:���ݵ������ͣ��������ͣ��õ��������ļ�����
	 * <p>
	 * 
	 * @param vo
	 * @param exportType
	 * @return �ļ�����
	 * 
	 * @since v1.01
	 */
	public String getFileName(CustomReportVo vo, String exportType) {
		String type = vo.getType();
		String fileName = "";
		CreateReportService service = new CreateReportService();
		String temp = service.getTime(type);
		String[] temps = temp.split(",");
		String startTime = temps[0];
		String endTime = temps[1];
		if ("day".equals(type)) {
			fileName = "day_" + vo.getCode() + startTime;
		} else if ("week".equals(type)) {
			fileName = "week_" + vo.getCode() + startTime + "_" + endTime;
		} else if ("month".equals(type)) {
			fileName = "month_" + vo.getCode() + startTime + "_" + endTime;
		}

		if ("excel".equals(exportType)) {
			fileName = fileName + ".xls";
		} else if ("word".equals(exportType)) {
			fileName = fileName + ".doc";
		} else if ("pdf".equals(exportType)) {
			fileName = fileName + ".pdf";
		}
		return fileName;
	}

	/**
	 * getTime:�õ���ʼʱ��ͽ���ʱ�䣬����Сʱ
	 * <p>
	 * 
	 * @param type
	 * @return startTime,endTime
	 * 
	 * @since v1.01
	 */
	public String getTime(String type) {
		// ���ݲ�ͬ���ͣ���ȡ��ʼʱ��ͽ���ʱ��
		String startTime = "";
		String endTime = "";
		if ("day".equals(type)) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1); // ǰһ��
			String date = sdf.format(cal.getTime());

			startTime = date;
			endTime = date;
		} else if ("week".equals(type)) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // ��ǰ���ڵ�����һ
			cal.add(Calendar.DAY_OF_WEEK, -7); // �ϸ����ڵ�����һ
			startTime = sdf.format(cal.getTime());
			cal.add(Calendar.DATE, 6); // ��ǰ�����죬�����ϸ����ڵ�������(�й��㷨)
			endTime = sdf.format(cal.getTime());
		} else if ("month".equals(type)) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.DATE, -1); // �ϸ��µ����һ��
			endTime = sdf.format(cal.getTime());
			cal.set(Calendar.DAY_OF_MONTH, 1); // �ϸ��µĵ�һ��
			startTime = sdf.format(cal.getTime());
		}
		return startTime + "," + endTime;
	}
}
