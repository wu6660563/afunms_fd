/*
 * @(#)SMSModel.java     v1.01, Nov 19, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.alarm.model;

import com.afunms.common.base.BaseVo;

/**
 * ClassName:   SMSModel.java
 * <p>��ɽ����֣�����Model
 *
 * @author      ��Ʒ��
 * @version     v1.01
 * @since       v1.01
 * @Date        Nov 19, 2013 3:35:34 PM
 * @mail		wupinlong@dhcc.com.cn
 */
public class SMSModel extends BaseVo{

	/**
	 * serialVersionUID:
	 * <p>
	 *
	 * @since   v1.01
	 */
	private static final long serialVersionUID = -325161043990528876L;
	
	/**
	 * �����ֻ����뼯�ϣ��ö��Ÿ���
	 */
	private String mobile;
	
	/**
	 * ��������
	 */
	private String content;
	
	/**
	 * ϵͳ���
	 */
	private String systemid;
	
	/**
	 * ���ܱ��
	 */
	private String functionid;
	
	/**
	 * �Ƿ��ӳٷ���
	 */
	private String ifdelaysend;
	
	/**
	 * ���Ϳ�ʼʱ��
	 */
	private String strarttime;
	
	/**
	 * ���ͳ�ʱʱ��
	 */
	private String endtime;
	
	/**
	 * �ͻ��˺�����,�ÿ�
	 */
	private String clientype;

	/**
	 * getMobile:
	 * <p>
	 *
	 * @return  String
	 *          -
	 * @since   v1.01
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * setMobile:
	 * <p>
	 *
	 * @param   mobile
	 *          -
	 * @since   v1.01
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * getContent:
	 * <p>
	 *
	 * @return  String
	 *          -
	 * @since   v1.01
	 */
	public String getContent() {
		return content;
	}

	/**
	 * setContent:
	 * <p>
	 *
	 * @param   content
	 *          -
	 * @since   v1.01
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * getSystemid:
	 * <p>
	 *
	 * @return  String
	 *          -
	 * @since   v1.01
	 */
	public String getSystemid() {
		return systemid;
	}

	/**
	 * setSystemid:
	 * <p>
	 *
	 * @param   systemid
	 *          -
	 * @since   v1.01
	 */
	public void setSystemid(String systemid) {
		this.systemid = systemid;
	}

	/**
	 * getFunctionid:
	 * <p>
	 *
	 * @return  String
	 *          -
	 * @since   v1.01
	 */
	public String getFunctionid() {
		return functionid;
	}

	/**
	 * setFunctionid:
	 * <p>
	 *
	 * @param   functionid
	 *          -
	 * @since   v1.01
	 */
	public void setFunctionid(String functionid) {
		this.functionid = functionid;
	}

	/**
	 * getIfdelaysend:
	 * <p>
	 *
	 * @return  String
	 *          -
	 * @since   v1.01
	 */
	public String getIfdelaysend() {
		return ifdelaysend;
	}

	/**
	 * setIfdelaysend:
	 * <p>
	 *
	 * @param   ifdelaysend
	 *          -
	 * @since   v1.01
	 */
	public void setIfdelaysend(String ifdelaysend) {
		this.ifdelaysend = ifdelaysend;
	}

	/**
	 * getStrarttime:
	 * <p>
	 *
	 * @return  String
	 *          -
	 * @since   v1.01
	 */
	public String getStrarttime() {
		return strarttime;
	}

	/**
	 * setStrarttime:
	 * <p>
	 *
	 * @param   strarttime
	 *          -
	 * @since   v1.01
	 */
	public void setStrarttime(String strarttime) {
		this.strarttime = strarttime;
	}

	/**
	 * getEndtime:
	 * <p>
	 *
	 * @return  String
	 *          -
	 * @since   v1.01
	 */
	public String getEndtime() {
		return endtime;
	}

	/**
	 * setEndtime:
	 * <p>
	 *
	 * @param   endtime
	 *          -
	 * @since   v1.01
	 */
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	/**
	 * getClientype:
	 * <p>
	 *
	 * @return  String
	 *          -
	 * @since   v1.01
	 */
	public String getClientype() {
		return clientype;
	}

	/**
	 * setClientype:
	 * <p>
	 *
	 * @param   clientype
	 *          -
	 * @since   v1.01
	 */
	public void setClientype(String clientype) {
		this.clientype = clientype;
	}
	

}

