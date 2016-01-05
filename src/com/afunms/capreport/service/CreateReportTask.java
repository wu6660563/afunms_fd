/*
 * @(#)CreateReportTask.java     v1.01, Jun 20, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.capreport.service;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.afunms.capreport.common.DateTime;
import com.afunms.capreport.dao.CustomReportDao;
import com.afunms.capreport.dao.CustomReportHistoryDao;
import com.afunms.capreport.model.CustomReportVo;

/**
 * ClassName: CreateReportTask.java
 * <p>
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Jun 20, 2013 10:14:42 PM
 */
public class CreateReportTask extends TimerTask {

	private static Logger log = Logger.getLogger(CreateReportTask.class);
	
	/**
	 * run:
	 * 
	 * 
	 * @since v1.01
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		// 取出所有报表列表，然后判断哪些需要执行；加入到buff里面（只需要判断是否需要生成报表）
		List<CustomReportVo> list = getNeedCreateReportList();
		if(list!=null&&list.size()>0){
			log.info("需要执行报表定制服务的个数："+list.size());
			ReportService.getInstance().addBufferAll(list);
		}
	}

	public List<CustomReportVo> getNeedCreateReportList() {
		List<CustomReportVo> list = new ArrayList<CustomReportVo>();
		CustomReportDao dao = new CustomReportDao();
		List<CustomReportVo> reportList = null;
		try {
			reportList = dao.loadAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		for (int i = 0; i < reportList.size(); i++) {
			CustomReportVo vo = reportList.get(i);
			if (needCreate(vo)) {
				list.add(vo);
			}
		}
		return list;
	}

	/**
	 * needCreate:是否需要生成报表，根据时间和是否已经生成来判断
	 * <p>
	 *
	 * @param vo
	 * @return
	 *
	 * @since   v1.01
	 */
	public boolean needCreate(CustomReportVo vo) {
		boolean needCreate = false;

		DateTime dt = new DateTime();
		String time = dt.getMyDateTime(DateTime.Datetime_Format_14);
		if ("day".equals(vo.getType())) {
			// 日报
			if (inTime(dt, vo) && !created(vo, time)&& "1".equals(vo.getIsCreate())) {
				// 可被创建
				needCreate = true;
			}
		} else if ("week".equals(vo.getType())) {
			// 周报
			if (inTime(dt, vo) && !created(vo, time) && "1".equals(vo.getIsCreate())) {
				// 可被创建
				needCreate = true;
			}
		} else if ("month".equals(vo.getType())) {
			// 月报
			if (inTime(dt, vo) && !created(vo, time)&& "1".equals(vo.getIsCreate())) {
				// 可被创建
				needCreate = true;
			}
		}
		return needCreate;
	}

	/**
	 * created:是否已经生成过报表
	 * <p>
	 * 
	 * @return boolean
	 * 
	 * @since v1.01
	 */
	public boolean created(CustomReportVo vo, String time) {
		boolean created = false;
		CreateReportService services = new CreateReportService();
		String fileName = services.getFileName(vo, "excel");

		boolean iscreate = false;
		CustomReportHistoryDao dao = new CustomReportHistoryDao();
		try {
			iscreate = dao.inHistory(fileName, vo.getId());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}

		if (iscreate) {
			created = true;
		}
		return created;
	}

	/**
	 * inTime:日期在当前日期里，时间在当前小时内
	 * <p>
	 *
	 * @param dtime
	 * @param vo
	 * @return boolean
	 *
	 * @since   v1.01
	 */
	public boolean inTime(DateTime dtime, CustomReportVo vo) {
		boolean inTime = false;
		int hour = Integer.parseInt(vo.getSendTime());
		if ("day".equals(vo.getType())) {
			if (dtime.getHours() == hour) {
				inTime = true;
			}
		} else if ("week".equals(vo.getType())) {
			int date = Integer.parseInt(vo.getSendDate());
			if (dtime.getHours() == hour && (dtime.getDay() == date + 1)) {
				inTime = true;
			}
		} else if ("month".equals(vo.getType())) {
			int date = Integer.parseInt(vo.getSendDate());
			if (dtime.getHours() == hour && (dtime.getDate() == date)) {
				inTime = true;
			}
		}
		return inTime;
	}

}
