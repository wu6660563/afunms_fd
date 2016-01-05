/*
 * @(#)XmlNodeStandardDao.java     v1.01, Oct 6, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.topology.dao;

import java.sql.ResultSet;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.SysLogger;
import com.afunms.topology.model.XmlNodeStandardVo;

public class XmlNodeStandardDao extends BaseDao implements DaoInterface {

	public XmlNodeStandardDao() {
		super("nms_xml_node_standard");
	}

	public BaseVo loadFromRS(ResultSet rs) {
		XmlNodeStandardVo vo = new XmlNodeStandardVo();
		try {
			vo.setId(rs.getInt("id"));
			vo.setNodeId(rs.getString("nodeId"));
			vo.setNodeName(rs.getString("nodeName"));
			vo.setType(rs.getString("type"));
			vo.setSubtype(rs.getString("subtype"));
			vo.setIpaddress(rs.getString("ipaddress"));
			vo.setXmlName(rs.getString("xmlName"));
			vo.setTopoId(rs.getString("topoId"));
			vo.setTopoName(rs.getString("topoName"));
		} catch(Exception e) {
			SysLogger.error("XmlNodeStandardDao.loadFromRS()",e);
		} 
	    return vo;
	}

	public boolean save(BaseVo vo) {
		XmlNodeStandardVo nodeStandardVo = (XmlNodeStandardVo) vo;
		StringBuffer sql = new StringBuffer();
		sql.append("insert into nms_xml_node_standard(nodeId,nodeName,type,subtype,ipaddress,xmlName,topoId,topoName)values('");
		sql.append(nodeStandardVo.getNodeId());
		sql.append("','");
		sql.append(nodeStandardVo.getNodeName());
		sql.append("','");
		sql.append(nodeStandardVo.getType());
		sql.append("','");
		sql.append(nodeStandardVo.getSubtype());
		sql.append("','");
		sql.append(nodeStandardVo.getIpaddress());
		sql.append("','");
		sql.append(nodeStandardVo.getXmlName());
		sql.append("','");
		sql.append(nodeStandardVo.getTopoId());
		sql.append("','");
		sql.append(nodeStandardVo.getTopoName());
		sql.append("')");
		return saveOrUpdate(sql.toString());
	}

	public boolean update(BaseVo vo) {
		XmlNodeStandardVo nodeStandardVo = (XmlNodeStandardVo) vo;
		StringBuffer sql = new StringBuffer();
		sql.append("update nms_xml_node_standard set nodeId='");
		sql.append(nodeStandardVo.getNodeId());
		sql.append("',nodeName='");
		sql.append(nodeStandardVo.getNodeName());
		sql.append("',type='");
		sql.append(nodeStandardVo.getType());
		sql.append("',subtype='");
		sql.append(nodeStandardVo.getSubtype());
		sql.append("',ipaddress='");
		sql.append(nodeStandardVo.getIpaddress());
		sql.append("',xmlName='");
		sql.append(nodeStandardVo.getXmlName());
		sql.append("',topoId='");
		sql.append(nodeStandardVo.getTopoId());
		sql.append("',topoName='");
		sql.append(nodeStandardVo.getTopoName());
		sql.append("' where id=");
		sql.append(nodeStandardVo.getId());
		return saveOrUpdate(sql.toString());
	}
	
	public XmlNodeStandardVo getXmlNodeStandardVoByNodeId(String nodeId){
		XmlNodeStandardVo vo = null;
		String sql = " where nodeId='"+nodeId+"'";
		List list = findByCondition(sql);
		if(list != null && list.size() > 0){
			vo = (XmlNodeStandardVo)list.get(0);
		}
		return vo;
	}
	
	public XmlNodeStandardVo getXmlNodeStandardVo(String nodeId,String type,String subtype){
		XmlNodeStandardVo vo = null;
		String sql = " where nodeId='"+nodeId+"' and type='"+type+"' and subtype='"+subtype+"'";
		List list = findByCondition(sql);
		if(list != null && list.size() > 0){
			vo = (XmlNodeStandardVo)list.get(0);
		}
		return vo;
	}
	
}
