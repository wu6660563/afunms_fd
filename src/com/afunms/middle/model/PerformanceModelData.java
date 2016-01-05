/*
 * @(#)PerformanceData.java     v1.01, Oct 24, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.model;

import com.afunms.common.base.BaseVo;

/**
 * ClassName: PerformanceData.java
 * <p> 性能模型数据
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Oct 24, 2013 3:57:48 PM
 * @mail wupinlong@dhcc.com.cn
 */
public class PerformanceModelData extends BaseVo {

	/**
	 * serialVersionUID:
	 * <p>
	 *
	 * @since   v1.01
	 */
	private static final long serialVersionUID = 737476745980076720L;
	
	private String type;
	
	private PerformanceVo perf;

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
	 * getPerf:
	 * <p>
	 *
	 * @return  PerformanceVo
	 *          -
	 * @since   v1.01
	 */
	public PerformanceVo getPerf() {
		return perf;
	}

	/**
	 * setPerf:
	 * <p>
	 *
	 * @param   perf
	 *          -
	 * @since   v1.01
	 */
	public void setPerf(PerformanceVo perf) {
		this.perf = perf;
	}

}
