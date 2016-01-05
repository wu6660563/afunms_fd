/*
 * @(#)AlarmData.java     v1.01, Oct 8, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.model;

import com.afunms.common.base.BaseVo;

/**
 * 
 * ClassName: BugMsgData.java
 * <p>
 * 
 * @author Œ‚∆∑¡˙
 * @version v1.01
 * @since v1.01
 * @Date Oct 8, 2013 5:47:44 PM
 * @mail wupinlong@dhcc.com.cn
 */
public class BugMsgData extends BaseVo {

	/**
	 * serialVersionUID:
	 * <p>
	 * 
	 * @since v1.01
	 */
	private static final long serialVersionUID = -2027060590104559231L;

	private int id;

	private String name;

	private String sn;

	private String vender_code;

	private String vendor;

	private int severity;

	private int ori_severity;

	private String CVE;

	private String remedy;

	private String description;

	private int source;

	/**
	 * getId:
	 * <p>
	 * 
	 * @return int -
	 * @since v1.01
	 */
	public int getId() {
		return id;
	}

	/**
	 * setId:
	 * <p>
	 * 
	 * @param id -
	 * @since v1.01
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * getName:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getName() {
		return name;
	}

	/**
	 * setName:
	 * <p>
	 * 
	 * @param name -
	 * @since v1.01
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * getSn:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getSn() {
		return sn;
	}

	/**
	 * setSn:
	 * <p>
	 * 
	 * @param sn -
	 * @since v1.01
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}

	/**
	 * getVender_code:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getVender_code() {
		return vender_code;
	}

	/**
	 * setVender_code:
	 * <p>
	 * 
	 * @param vender_code -
	 * @since v1.01
	 */
	public void setVender_code(String vender_code) {
		this.vender_code = vender_code;
	}

	/**
	 * getVendor:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * setVendor:
	 * <p>
	 * 
	 * @param vendor -
	 * @since v1.01
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	/**
	 * getSeverity:
	 * <p>
	 * 
	 * @return int -
	 * @since v1.01
	 */
	public int getSeverity() {
		return severity;
	}

	/**
	 * setSeverity:
	 * <p>
	 * 
	 * @param severity -
	 * @since v1.01
	 */
	public void setSeverity(int severity) {
		this.severity = severity;
	}

	/**
	 * getOri_severity:
	 * <p>
	 * 
	 * @return int -
	 * @since v1.01
	 */
	public int getOri_severity() {
		return ori_severity;
	}

	/**
	 * setOri_severity:
	 * <p>
	 * 
	 * @param ori_severity -
	 * @since v1.01
	 */
	public void setOri_severity(int ori_severity) {
		this.ori_severity = ori_severity;
	}

	/**
	 * getCVE:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getCVE() {
		return CVE;
	}

	/**
	 * setCVE:
	 * <p>
	 * 
	 * @param cve -
	 * @since v1.01
	 */
	public void setCVE(String cve) {
		CVE = cve;
	}

	/**
	 * getRemedy:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getRemedy() {
		return remedy;
	}

	/**
	 * setRemedy:
	 * <p>
	 * 
	 * @param remedy -
	 * @since v1.01
	 */
	public void setRemedy(String remedy) {
		this.remedy = remedy;
	}

	/**
	 * getDescription:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * setDescription:
	 * <p>
	 * 
	 * @param description -
	 * @since v1.01
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * getSource:
	 * <p>
	 * 
	 * @return int -
	 * @since v1.01
	 */
	public int getSource() {
		return source;
	}

	/**
	 * setSource:
	 * <p>
	 * 
	 * @param source -
	 * @since v1.01
	 */
	public void setSource(int source) {
		this.source = source;
	}

}
