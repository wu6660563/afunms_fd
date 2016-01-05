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
 * @author 吴品龙
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
	 * create:生成报表方法
	 * <p>
	 * 
	 * @param vo
	 * 
	 * @since v1.01
	 */
	public void create(CustomReportVo vo) {
		// 根据 CustomReportVo 这里判断 创建何种报表，在此处执行生成报表（执行操作）
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
	 * getFileName:根据导出类型，报表类型，得到导出的文件名称
	 * <p>
	 * 
	 * @param vo
	 * @param exportType
	 * @return 文件名称
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
	 * getTime:得到开始时间和结束时间，不含小时
	 * <p>
	 * 
	 * @param type
	 * @return startTime,endTime
	 * 
	 * @since v1.01
	 */
	public String getTime(String type) {
		// 根据不同类型，获取开始时间和结束时间
		String startTime = "";
		String endTime = "";
		if ("day".equals(type)) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1); // 前一天
			String date = sdf.format(cal.getTime());

			startTime = date;
			endTime = date;
		} else if ("week".equals(type)) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 当前星期的星期一
			cal.add(Calendar.DAY_OF_WEEK, -7); // 上个星期的星期一
			startTime = sdf.format(cal.getTime());
			cal.add(Calendar.DATE, 6); // 往前翻六天，就是上个星期的星期天(中国算法)
			endTime = sdf.format(cal.getTime());
		} else if ("month".equals(type)) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.DATE, -1); // 上个月的最后一天
			endTime = sdf.format(cal.getTime());
			cal.set(Calendar.DAY_OF_MONTH, 1); // 上个月的第一天
			startTime = sdf.format(cal.getTime());
		}
		return startTime + "," + endTime;
	}
}
