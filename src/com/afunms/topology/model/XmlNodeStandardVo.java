/*
 * @(#)XmlNodeStandardVo.java     v1.01, Oct 6, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.topology.model;

import com.afunms.common.base.BaseVo;

/**
 * ClassName: XmlNodeStandardVo.java
 * <p>
 * ��������Ĭ���豸��תXML
 * 
 * @author ��Ʒ��
 * @version v1.01
 * @since v1.01
 * @Date Oct 6, 2013 10:50:32 AM
 * @mail wupinlong@dhcc.com.cn
 */
public class XmlNodeStandardVo extends BaseVo {

	/**
	 * serialVersionUID:
	 * <p>
	 * 
	 * @since v1.01
	 */
	private static final long serialVersionUID = 4677363497312158484L;

	/**
	 * ����ID��������
	 */
	private int id;

	/**
	 * �豸ID
	 */
	private String nodeId;
	
	/**
	 * �豸����
	 */
	private String nodeName;

	/**
	 * ����
	 */
	private String type;

	/**
	 * ������
	 */
	private String subtype;

	/**
	 * IP��ַ
	 */
	private String ipaddress;

	/**
	 * XML����
	 */
	private String xmlName;
	
	/**
	 * ����ID
	 */
	private String topoId;
	
	/**
	 * ����ͼ����
	 */
	private String topoName;

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
	 * getXmlName:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getXmlName() {
		return xmlName;
	}

	/**
	 * setXmlName:
	 * <p>
	 * 
	 * @param xmlName -
	 * @since v1.01
	 */
	public void setXmlName(String xmlName) {
		this.xmlName = xmlName;
	}

	/**
	 * getNodeId:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * setNodeId:
	 * <p>
	 * 
	 * @param nodeId -
	 * @since v1.01
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * getNodeName:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * setNodeName:
	 * <p>
	 * 
	 * @param nodeName -
	 * @since v1.01
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * getType:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getType() {
		return type;
	}

	/**
	 * setType:
	 * <p>
	 * 
	 * @param type -
	 * @since v1.01
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * getSubtype:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getSubtype() {
		return subtype;
	}

	/**
	 * setSubtype:
	 * <p>
	 * 
	 * @param subtype -
	 * @since v1.01
	 */
	public void setSubtype(String subtype) {
		this.subtype = subtype;
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
	 * getTopoId:
	 * <p>
	 *
	 * @return  String
	 *          -
	 * @since   v1.01
	 */
	public String getTopoId() {
		return topoId;
	}

	/**
	 * setTopoId:
	 * <p>
	 *
	 * @param   topoId
	 *          -
	 * @since   v1.01
	 */
	public void setTopoId(String topoId) {
		this.topoId = topoId;
	}

	/**
	 * getTopoName:
	 * <p>
	 *
	 * @return  String
	 *          -
	 * @since   v1.01
	 */
	public String getTopoName() {
		return topoName;
	}

	/**
	 * setTopoName:
	 * <p>
	 *
	 * @param   topoName
	 *          -
	 * @since   v1.01
	 */
	public void setTopoName(String topoName) {
		this.topoName = topoName;
	}

}
