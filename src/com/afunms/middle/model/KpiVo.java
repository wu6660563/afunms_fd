/*
 * @(#)KpiVo.java     v1.01, Dec 3, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.model;

import com.afunms.common.base.BaseVo;

/**
 * ClassName: KpiVo.java
 * <p>
 * 
 * @author Œ‚∆∑¡˙
 * @version v1.01
 * @since v1.01
 * @Date Dec 3, 2013 4:07:55 PM
 * @mail wupinlong@dhcc.com.cn
 */
public class KpiVo extends BaseVo {

	/**
	 * serialVersionUID:
	 * <p>
	 * 
	 * @since v1.01
	 */
	private static final long serialVersionUID = -2243577152539579332L;
	
	private int id;
	
	private String gid;
	
	private Long count_time;
	
	private int count_frequenc;
	
	private String kpi_name;
	
	private int kpi_value;

	/**
	 * getId:
	 * <p>
	 *
	 * @return  int
	 *          -
	 * @since   v1.01
	 */
	public int getId() {
		return id;
	}

	/**
	 * setId:
	 * <p>
	 *
	 * @param   id
	 *          -
	 * @since   v1.01
	 */
	public void setId(int id) {
		this.id = id;
	}

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
	 * getCount_time:
	 * <p>
	 *
	 * @return  Long
	 *          -
	 * @since   v1.01
	 */
	public Long getCount_time() {
		return count_time;
	}

	/**
	 * setCount_time:
	 * <p>
	 *
	 * @param   count_time
	 *          -
	 * @since   v1.01
	 */
	public void setCount_time(Long count_time) {
		this.count_time = count_time;
	}

	/**
	 * getCount_frequenc:
	 * <p>
	 *
	 * @return  int
	 *          -
	 * @since   v1.01
	 */
	public int getCount_frequenc() {
		return count_frequenc;
	}

	/**
	 * setCount_frequenc:
	 * <p>
	 *
	 * @param   count_frequenc
	 *          -
	 * @since   v1.01
	 */
	public void setCount_frequenc(int count_frequenc) {
		this.count_frequenc = count_frequenc;
	}

	/**
	 * getKpi_name:
	 * <p>
	 *
	 * @return  String
	 *          -
	 * @since   v1.01
	 */
	public String getKpi_name() {
		return kpi_name;
	}

	/**
	 * setKpi_name:
	 * <p>
	 *
	 * @param   kpi_name
	 *          -
	 * @since   v1.01
	 */
	public void setKpi_name(String kpi_name) {
		this.kpi_name = kpi_name;
	}

	/**
	 * getKpi_value:
	 * <p>
	 *
	 * @return  int
	 *          -
	 * @since   v1.01
	 */
	public int getKpi_value() {
		return kpi_value;
	}

	/**
	 * setKpi_value:
	 * <p>
	 *
	 * @param   kpi_value
	 *          -
	 * @since   v1.01
	 */
	public void setKpi_value(int kpi_value) {
		this.kpi_value = kpi_value;
	}
	
	

}
