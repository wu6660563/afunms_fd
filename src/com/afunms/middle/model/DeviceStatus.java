/*
 * @(#)DeviceStatus.java     v1.01, Oct 24, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.model;

import com.afunms.common.base.BaseVo;

/**
 * ClassName: DeviceStatus.java
 * <p>
 * 设备信息与状态数据报文
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Oct 24, 2013 4:38:04 PM
 * @mail wupinlong@dhcc.com.cn
 */
public class DeviceStatus extends BaseVo {

	/**
	 * serialVersionUID:
	 * <p>
	 * 
	 * @since v1.01
	 */
	private static final long serialVersionUID = 3748553554599983471L;

	private String gid;
	
	private String name;
	
	private String model;
	
	private String type;
	
	private int status;
	
	private int net_a_status;
	
	private int net_b_status;
	
	private Long report_time;

	/**
	 * getGid:
	 * <p>
	 *
	 * @return  String
	 *          -
	 * @since   v1.01
	 */
	public String getGid() {
		return gid;
	}

	/**
	 * setGid:
	 * <p>
	 *
	 * @param   gid
	 *          -
	 * @since   v1.01
	 */
	public void setGid(String gid) {
		this.gid = gid;
	}

	/**
	 * getName:
	 * <p>
	 *
	 * @return  String
	 *          -
	 * @since   v1.01
	 */
	public String getName() {
		return name;
	}

	/**
	 * setName:
	 * <p>
	 *
	 * @param   name
	 *          -
	 * @since   v1.01
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * getModel:
	 * <p>
	 *
	 * @return  String
	 *          -
	 * @since   v1.01
	 */
	public String getModel() {
		return model;
	}

	/**
	 * setModel:
	 * <p>
	 *
	 * @param   model
	 *          -
	 * @since   v1.01
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * getType:
	 * <p>
	 *
	 * @return  String
	 *          -
	 * @since   v1.01
	 */
	public String getType() {
		return type;
	}

	/**
	 * setType:
	 * <p>
	 *
	 * @param   type
	 *          -
	 * @since   v1.01
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * getStatus:
	 * <p>
	 *
	 * @return  int
	 *          -
	 * @since   v1.01
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * setStatus:
	 * <p>
	 *
	 * @param   status
	 *          -
	 * @since   v1.01
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * getNet_a_status:
	 * <p>
	 *
	 * @return  int
	 *          -
	 * @since   v1.01
	 */
	public int getNet_a_status() {
		return net_a_status;
	}

	/**
	 * setNet_a_status:
	 * <p>
	 *
	 * @param   net_a_status
	 *          -
	 * @since   v1.01
	 */
	public void setNet_a_status(int net_a_status) {
		this.net_a_status = net_a_status;
	}

	/**
	 * getNet_b_status:
	 * <p>
	 *
	 * @return  int
	 *          -
	 * @since   v1.01
	 */
	public int getNet_b_status() {
		return net_b_status;
	}

	/**
	 * setNet_b_status:
	 * <p>
	 *
	 * @param   net_b_status
	 *          -
	 * @since   v1.01
	 */
	public void setNet_b_status(int net_b_status) {
		this.net_b_status = net_b_status;
	}

	/**
	 * getReport_time:
	 * <p>
	 *
	 * @return  Long
	 *          -
	 * @since   v1.01
	 */
	public Long getReport_time() {
		return report_time;
	}

	/**
	 * setReport_time:
	 * <p>
	 *
	 * @param   report_time
	 *          -
	 * @since   v1.01
	 */
	public void setReport_time(Long report_time) {
		this.report_time = report_time;
	}
	
}
