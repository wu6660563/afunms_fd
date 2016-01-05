/*
 * @(#)CustomReportVo.java     v1.01, Jun 18, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.capreport.model;

import com.afunms.common.base.BaseVo;

/**
 * ClassName: CustomReportVo.java
 * <p>
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Jun 18, 2013 10:58:00 PM
 */
public class CustomReportVo extends BaseVo {
	/**
	 * id:序号
	 * <p>
	 * 
	 * @since v1.01
	 */
	private int id;
	/**
	 * name:报表名称(选择)
	 * <p>
	 * 
	 * @since v1.01
	 */
	private String name;
	/**
	 * type:报表类型（日报、周报、月报）
	 * <p>
	 * 
	 * @since v1.01
	 */
	private String type;
	/**
	 * code:报表编号
	 * <p>
	 * 
	 * @since v1.01
	 */
	private String code;
	/**
	 * userId:报表接收人
	 * <p>
	 * 
	 * @since v1.01
	 */
	private String userId;
	/**
	 * isCreate:是否生成
	 * <p>
	 * 
	 * @since v1.01
	 */
	private String isCreate;
	/**
	 * isSend:是否发送
	 * <p>
	 * 
	 * @since v1.01
	 */
	private String isSend;
	/**
	 * mailTitle:邮件标题
	 * <p>
	 * 
	 * @since v1.01
	 */
	private String mailTitle;
	/**
	 * mailDesc:邮件描述
	 * <p>
	 * 
	 * @since v1.01
	 */
	private String mailDesc;
	/**
	 * bid:所属业务
	 * <p>
	 * 
	 * @since v1.01
	 */
	private String bid;
	/**
	 * fileType:导出类型（Excel、Word、Pdf）
	 * <p>
	 * 
	 * @since v1.01
	 */
	private String fileType;
	/**
	 * sendDate:发送日期
	 * <p>
	 * 
	 * @since v1.01
	 */
	private String sendDate;
	/**
	 * sendTime:发送时间
	 * <p>
	 * 
	 * @since v1.01
	 */
	private String sendTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIsCreate() {
		return isCreate;
	}

	public void setIsCreate(String isCreate) {
		this.isCreate = isCreate;
	}

	public String getIsSend() {
		return isSend;
	}

	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}

	public String getMailTitle() {
		return mailTitle;
	}

	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	public String getMailDesc() {
		return mailDesc;
	}

	public void setMailDesc(String mailDesc) {
		this.mailDesc = mailDesc;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

}
