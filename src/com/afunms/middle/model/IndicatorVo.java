/*
 * @(#)IndicatorVo.java     v1.01, Oct 24, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.model;

import com.afunms.common.base.BaseVo;

/**
 * ClassName:   IndicatorVo.java
 * <p>
 *
 * @author      Œ‚∆∑¡˙
 * @version     v1.01
 * @since       v1.01
 * @Date        Oct 24, 2013 4:25:38 PM
 * @mail		wupinlong@dhcc.com.cn
 */
public class IndicatorVo extends BaseVo {

	/**
	 * serialVersionUID:
	 * <p>
	 *
	 * @since   v1.01
	 */
	private static final long serialVersionUID = 2427147434581514069L;
	
	private int id;
	
	private String gid;
	
	private Long count_time;
	
	private int count_frequenc;
	
	private String kpi_name;
	
	private String kpi_value;

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
	 * @return  String
	 *          -
	 * @since   v1.01
	 */
	public String getKpi_value() {
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
	public void setKpi_value(String kpi_value) {
		this.kpi_value = kpi_value;
	}
	
	

}

