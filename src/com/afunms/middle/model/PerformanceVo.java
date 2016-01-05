/*
 * @(#)PerformanceVo.java     v1.01, Oct 24, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.model;

import java.util.Set;

import com.afunms.common.base.BaseVo;

/**
 * ClassName: PerformanceVo.java
 * <p>
 * 
 * @author Œ‚∆∑¡˙
 * @version v1.01
 * @since v1.01
 * @Date Oct 24, 2013 4:01:43 PM
 * @mail wupinlong@dhcc.com.cn
 */
public class PerformanceVo extends BaseVo {

	/**
	 * serialVersionUID:
	 * <p>
	 * 
	 * @since v1.01
	 */
	private static final long serialVersionUID = 1765375076859757380L;

	private String group;

	private Set<Counter> counters;

	/**
	 * getGroup:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * setGroup:
	 * <p>
	 * 
	 * @param group -
	 * @since v1.01
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	public Set<Counter> getCounters() {
		return counters;
	}

	public void setCounters(Set<Counter> counters) {
		this.counters = counters;
	}

}

