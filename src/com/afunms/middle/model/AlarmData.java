/*
 * @(#)AlarmData.java     v1.01, Oct 8, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.model;

import com.afunms.common.base.BaseVo;

/**
 * 
 * ClassName: AlarmData.java
 * <p>
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Oct 8, 2013 5:47:33 PM
 * @mail wupinlong@dhcc.com.cn
 */
public class AlarmData extends BaseVo {

	/**
	 * serialVersionUID:
	 * <p>
	 * 
	 * @since v1.01
	 */
	private static final long serialVersionUID = -2027060590104559231L;

	private Long id;

	private Long time;

	/**
	 * 1：performance（性能） 2：security（安全） 3：business（业务）
	 */
	private String type;

	private int level;

	/**
	 * 如果告警对象不在健康模型范围内，则此项为空
	 */
	private String object_gid;

	/**
	 * GID所对应的健康模型中此对象名称；如果告警对象不在健康模型范围内则填写机器采集名称，比如系统进程名sox
	 */
	private String object_name;

	/**
	 * 如果告警对象在健康模型内则通过获取GID对应的对象可分析出其子类型，不需要在此填写，为空；如果告警对象不在健康模型范围内则此项不能为空，且遵循如下规则 ：
	 * 性能： 1：CPU 2：MEM 3:DISK
	 */
	private String sub_type;

	/**
	 * 告警设备的GID
	 */
	private String device_gid;

	private int status;

	/**
	 * 告警处理时间
	 */
	private Long handle_time;

	/**
	 * 告警名称
	 */
	private String title;
	/**
	 * 比如CPU越限告警，则告警内容为CPU的使用值：80%
	 * 如果告警对象不在健康模型内，则描述信息需更加详细，比如sox进程占用20G内存，描述为：sox 32117 mem 20GB
	 * 其中32117是进程号
	 * 
	 */
	private String content;

	/**
	 * getTime:
	 * <p>
	 * 
	 * @return Long -
	 * @since v1.01
	 */
	public Long getTime() {
		return time;
	}

	/**
	 * setTime:
	 * <p>
	 * 
	 * @param time -
	 * @since v1.01
	 */
	public void setTime(Long time) {
		this.time = time;
	}

	/**
	 * getType:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getType() {
		return type;
	}

	/**
	 * setType:
	 * <p>
	 * 
	 * @param type -
	 * @since v1.01
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * getLevel:
	 * <p>
	 * 
	 * @return int -
	 * @since v1.01
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * setLevel:
	 * <p>
	 * 
	 * @param level -
	 * @since v1.01
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * getObject_gid:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getObject_gid() {
		return object_gid;
	}

	/**
	 * setObject_gid:
	 * <p>
	 * 
	 * @param object_gid -
	 * @since v1.01
	 */
	public void setObject_gid(String object_gid) {
		this.object_gid = object_gid;
	}

	/**
	 * getObject_name:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getObject_name() {
		return object_name;
	}

	/**
	 * setObject_name:
	 * <p>
	 * 
	 * @param object_name -
	 * @since v1.01
	 */
	public void setObject_name(String object_name) {
		this.object_name = object_name;
	}

	/**
	 * getSub_type:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getSub_type() {
		return sub_type;
	}

	/**
	 * setSub_type:
	 * <p>
	 * 
	 * @param sub_type -
	 * @since v1.01
	 */
	public void setSub_type(String sub_type) {
		this.sub_type = sub_type;
	}

	/**
	 * getDevice_gid:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getDevice_gid() {
		return device_gid;
	}

	/**
	 * setDevice_gid:
	 * <p>
	 * 
	 * @param device_gid -
	 * @since v1.01
	 */
	public void setDevice_gid(String device_gid) {
		this.device_gid = device_gid;
	}

	/**
	 * getStatus:
	 * <p>
	 * 
	 * @return int -
	 * @since v1.01
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * setStatus:
	 * <p>
	 * 
	 * @param status -
	 * @since v1.01
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * getHandle_time:
	 * <p>
	 * 
	 * @return Long -
	 * @since v1.01
	 */
	public Long getHandle_time() {
		return handle_time;
	}

	/**
	 * setHandle_time:
	 * <p>
	 * 
	 * @param handle_time -
	 * @since v1.01
	 */
	public void setHandle_time(Long handle_time) {
		this.handle_time = handle_time;
	}

	/**
	 * getTitle:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * setTitle:
	 * <p>
	 * 
	 * @param title -
	 * @since v1.01
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * getContent:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getContent() {
		return content;
	}

	/**
	 * setContent:
	 * <p>
	 * 
	 * @param content -
	 * @since v1.01
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * getId:
	 * <p>
	 * 
	 * @return Long -
	 * @since v1.01
	 */
	public Long getId() {
		return id;
	}

	/**
	 * setId:
	 * <p>
	 * 
	 * @param id -
	 * @since v1.01
	 */
	public void setId(Long id) {
		this.id = id;
	}

}
