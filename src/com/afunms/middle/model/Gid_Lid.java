/*
 * @(#)Gid_Lid.java     v1.01, Oct 24, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.model;

import com.afunms.common.base.BaseVo;

/**
 * 
 * ClassName:   Gid_Lid.java
 * <p>
 *
 * @author      Œ‚∆∑¡˙
 * @version     v1.01
 * @since       v1.01
 * @Date        Oct 24, 2013 3:57:54 PM
 * @mail		wupinlong@dhcc.com.cn
 */
public class Gid_Lid extends BaseVo{

	/**
	 * serialVersionUID:
	 * <p>
	 *
	 * @since   v1.01
	 */
	private static final long serialVersionUID = 7522702752402224334L;
	
	private String gid;
	
	private String lid;

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
	 * getLid:
	 * <p>
	 *
	 * @return  String
	 *          -
	 * @since   v1.01
	 */
	public String getLid() {
		return lid;
	}

	/**
	 * setLid:
	 * <p>
	 *
	 * @param   lid
	 *          -
	 * @since   v1.01
	 */
	public void setLid(String lid) {
		this.lid = lid;
	}
	
	

}

