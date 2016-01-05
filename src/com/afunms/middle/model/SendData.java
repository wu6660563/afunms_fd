/*
 * @(#)SendData.java     v1.01, Oct 8, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.model;

import java.util.Set;

import com.afunms.common.base.BaseVo;

/**
 * ClassName: SendData.java
 * <p>
 * 
 * @author Œ‚∆∑¡˙
 * @version v1.01
 * @since v1.01
 * @Date Oct 8, 2013 5:47:25 PM
 * @mail wupinlong@dhcc.com.cn
 */
public class SendData<T> extends BaseVo {

	/**
	 * serialVersionUID:
	 * <p>
	 * 
	 * @since v1.01
	 */
	private static final long serialVersionUID = 990281968456255735L;

	private HeaderData header;

	private Set<T> body;

	/**
	 * getHeader:
	 * <p>
	 * 
	 * @return HeaderData -
	 * @since v1.01
	 */
	public HeaderData getHeader() {
		return header;
	}

	/**
	 * setHeader:
	 * <p>
	 * 
	 * @param header -
	 * @since v1.01
	 */
	public void setHeader(HeaderData header) {
		this.header = header;
	}

	/**
	 * getBody:
	 * <p>
	 *
	 * @return  Set<T>
	 *          -
	 * @since   v1.01
	 */
	public Set<T> getBody() {
		return body;
	}

	/**
	 * setBody:
	 * <p>
	 *
	 * @param   body
	 *          -
	 * @since   v1.01
	 */
	public void setBody(Set<T> body) {
		this.body = body;
	}


}
