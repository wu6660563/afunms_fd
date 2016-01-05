/*
 * @(#)HeaderData.java     v1.01, Oct 8, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.model;

import com.afunms.common.base.BaseVo;

/**
 * 
 * ClassName: HeaderData.java
 * <p>
 * 
 * @author Œ‚∆∑¡˙
 * @version v1.01
 * @since v1.01
 * @Date Oct 8, 2013 5:47:38 PM
 * @mail wupinlong@dhcc.com.cn
 */
public class HeaderData extends BaseVo {

	/**
	 * serialVersionUID:
	 * <p>
	 * 
	 * @since v1.01
	 */
	private static final long serialVersionUID = -7324952667863928353L;

	private int systype;

	private String sysname;

	private int sysdomain;

	private int msgtype;

	/**
	 * getSystype:
	 * <p>
	 *
	 * @return  int
	 *          -
	 * @since   v1.01
	 */
	public int getSystype() {
		return systype;
	}

	/**
	 * setSystype:
	 * <p>
	 *
	 * @param   systype
	 *          -
	 * @since   v1.01
	 */
	public void setSystype(int systype) {
		this.systype = systype;
	}

	/**
	 * getSysname:
	 * <p>
	 *
	 * @return  String
	 *          -
	 * @since   v1.01
	 */
	public String getSysname() {
		return sysname;
	}

	/**
	 * setSysname:
	 * <p>
	 *
	 * @param   sysname
	 *          -
	 * @since   v1.01
	 */
	public void setSysname(String sysname) {
		this.sysname = sysname;
	}

	/**
	 * getSysdomain:
	 * <p>
	 *
	 * @return  int
	 *          -
	 * @since   v1.01
	 */
	public int getSysdomain() {
		return sysdomain;
	}

	/**
	 * setSysdomain:
	 * <p>
	 *
	 * @param   sysdomain
	 *          -
	 * @since   v1.01
	 */
	public void setSysdomain(int sysdomain) {
		this.sysdomain = sysdomain;
	}

	/**
	 * getMsgtype:
	 * <p>
	 *
	 * @return  int
	 *          -
	 * @since   v1.01
	 */
	public int getMsgtype() {
		return msgtype;
	}

	/**
	 * setMsgtype:
	 * <p>
	 *
	 * @param   msgtype
	 *          -
	 * @since   v1.01
	 */
	public void setMsgtype(int msgtype) {
		this.msgtype = msgtype;
	}

	

}
