/*
 * @(#)IpManagerBasicVo.java     v1.01, Feb 14, 2014
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.ipresource.model;

import java.util.Calendar;

import com.afunms.common.base.BaseVo;

/**
 * ClassName: IpManageBasicVo.java
 * <p>
 * IP地址管理基础表，对应数据库ip_manager_basic表
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Feb 14, 2014 10:57:24 AM
 * @mail wupinlong@dhcc.com.cn
 */
public class IpManageBasicVo extends BaseVo {

	/**
	 * serialVersionUID:
	 * <p>
	 * 
	 * @since v1.01
	 */
	private static final long serialVersionUID = -4233607328734580932L;

	/**
	 * id:
	 * <p>
	 * 
	 * @since v1.01
	 */
	private Long id;

	/**
	 * relateipaddr:
	 * <p>
	 * 
	 * @since v1.01
	 */
	private String relateipaddr;

	/**
	 * ifindex:
	 * <p>
	 * 
	 * @since v1.01
	 */
	private String ifindex;

	/**
	 * ipaddress:IP地址
	 * <p>
	 * 
	 * @since v1.01
	 */
	private String ipaddress;

	/**
	 * mac:MAC地址
	 * <p>
	 * 
	 * @since v1.01
	 */
	private String mac;

	/**
	 * collecttime:采集时间，入库时间，不修改
	 * <p>
	 * 
	 * @since v1.01
	 */
	private Calendar collecttime;

	/**
	 * ifband:是否绑定
	 * <p>
	 * 
	 * @since v1.01
	 */
	private String ifband;

	/**
	 * ifmanage:是否管理
	 * <p>
	 * 
	 * @since v1.01
	 */
	private String ifmanage;

	/**
	 * bak:备注，用途
	 * <p>
	 * 
	 * @since v1.01
	 */
	private String bak;

	/**
	 * getId:
	 * <p>
	 * 
	 * @return Long -
	 * @since v1.01
	 */
	public Long getId() {
		return id;
	}

	/**
	 * setId:
	 * <p>
	 * 
	 * @param id -
	 * @since v1.01
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * getRelateipaddr:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getRelateipaddr() {
		return relateipaddr;
	}

	/**
	 * setRelateipaddr:
	 * <p>
	 * 
	 * @param relateipaddr -
	 * @since v1.01
	 */
	public void setRelateipaddr(String relateipaddr) {
		this.relateipaddr = relateipaddr;
	}

	/**
	 * getIfindex:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getIfindex() {
		return ifindex;
	}

	/**
	 * setIfindex:
	 * <p>
	 * 
	 * @param ifindex -
	 * @since v1.01
	 */
	public void setIfindex(String ifindex) {
		this.ifindex = ifindex;
	}

	/**
	 * getIpaddress:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getIpaddress() {
		return ipaddress;
	}

	/**
	 * setIpaddress:
	 * <p>
	 * 
	 * @param ipaddress -
	 * @since v1.01
	 */
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	/**
	 * getMac:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getMac() {
		return mac;
	}

	/**
	 * setMac:
	 * <p>
	 * 
	 * @param mac -
	 * @since v1.01
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}

	/**
	 * getCollecttime:
	 * <p>
	 * 
	 * @return Calendar -
	 * @since v1.01
	 */
	public Calendar getCollecttime() {
		return collecttime;
	}

	/**
	 * setCollecttime:
	 * <p>
	 * 
	 * @param collecttime -
	 * @since v1.01
	 */
	public void setCollecttime(Calendar collecttime) {
		this.collecttime = collecttime;
	}

	/**
	 * getIfband:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getIfband() {
		return ifband;
	}

	/**
	 * setIfband:
	 * <p>
	 * 
	 * @param ifband -
	 * @since v1.01
	 */
	public void setIfband(String ifband) {
		this.ifband = ifband;
	}

	/**
	 * getBak:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getBak() {
		return bak;
	}

	/**
	 * setBak:
	 * <p>
	 * 
	 * @param bak -
	 * @since v1.01
	 */
	public void setBak(String bak) {
		this.bak = bak;
	}

	/**
	 * getIfmanage:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getIfmanage() {
		return ifmanage;
	}

	/**
	 * setIfmanage:
	 * <p>
	 * 
	 * @param ifmanage -
	 * @since v1.01
	 */
	public void setIfmanage(String ifmanage) {
		this.ifmanage = ifmanage;
	}

}
