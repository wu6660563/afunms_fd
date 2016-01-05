/*
 * @(#)CustomReportHistory.java     v1.01, Jun 24, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.capreport.model;

import java.util.Calendar;

import com.afunms.common.base.BaseVo;

/**
 * 
 * ClassName: CustomReportHistory.java
 * <p>
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Jun 24, 2013 9:46:50 AM
 */
public class CustomReportHistory extends BaseVo {

	/**
	 * id:主键
	 * <p>
	 * 
	 * @since v1.01
	 */
	private int id;
	/**
	 * reportId:报表ID
	 * <p>
	 * 
	 * @since v1.01
	 */
	private int reportId;
	/**
	 * fileName:文件名称
	 * <p>
	 * 
	 * @since v1.01
	 */
	private String fileName;
	/**
	 * createDate:发送日期
	 * <p>
	 * 
	 * @since v1.01
	 */
	private Calendar createDate;
	/**
	 * isSuccess:是否成功
	 * <p>
	 * 
	 * @since v1.01
	 */
	private String isSuccess;

	/**
	 * getId:
	 * <p>
	 * 
	 * @return int -
	 * @since v1.01
	 */
	public int getId() {
		return id;
	}

	/**
	 * setId:
	 * <p>
	 * 
	 * @param id -
	 * @since v1.01
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * getReportId:
	 * <p>
	 * 
	 * @return int -
	 * @since v1.01
	 */
	public int getReportId() {
		return reportId;
	}

	/**
	 * setReportId:
	 * <p>
	 * 
	 * @param reportId -
	 * @since v1.01
	 */
	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

	/**
	 * getFileName:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * setFileName:
	 * <p>
	 * 
	 * @param fileName -
	 * @since v1.01
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * getCreateDate:
	 * <p>
	 * 
	 * @return Calendar -
	 * @since v1.01
	 */
	public Calendar getCreateDate() {
		return createDate;
	}

	/**
	 * setCreateDate:
	 * <p>
	 * 
	 * @param createDate -
	 * @since v1.01
	 */
	public void setCreateDate(Calendar createDate) {
		this.createDate = createDate;
	}

	/**
	 * getIsSuccess:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getIsSuccess() {
		return isSuccess;
	}

	/**
	 * setIsSuccess:
	 * <p>
	 * 
	 * @param isSuccess -
	 * @since v1.01
	 */
	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}

}
