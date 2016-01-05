/*
 * @(#)CustomReportDao.java     v1.01, Jun 19, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.capreport.dao;

import java.sql.ResultSet;

import com.afunms.capreport.model.CustomReportVo;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
/**
 * 
 * ClassName:   CustomReportDao.java
 * <p>
 *
 * @author      Œ‚∆∑¡˙
 * @version     v1.01
 * @since       v1.01
 * @Date        Jun 19, 2013 10:37:31 AM
 */
public class CustomReportDao extends BaseDao implements DaoInterface{
	
	public CustomReportDao() {
		super("nms_customreport");
	}

	/**
	 * loadFromRS:
	 *
	 * @param rs
	 * @return
	 *
	 * @since   v1.01
	 * @see com.afunms.common.base.BaseDao#loadFromRS(java.sql.ResultSet)
	 */
	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		CustomReportVo vo = new CustomReportVo();
		try{
			vo.setId(rs.getInt("id"));
			vo.setName(rs.getString("name"));
			vo.setType(rs.getString("type"));
			vo.setCode(rs.getString("code"));
			vo.setUserId(rs.getString("userId"));
			vo.setIsCreate(rs.getString("isCreate"));
			vo.setIsSend(rs.getString("isSend"));
			vo.setMailTitle(rs.getString("mailTitle"));
			vo.setMailDesc(rs.getString("mailDesc"));
			vo.setFileType(rs.getString("fileType"));
			vo.setSendDate(rs.getString("sendDate"));
			vo.setSendTime(rs.getString("sendTime"));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return vo;
	}

	/**
	 * save:
	 *
	 * @param vo
	 * @return
	 *
	 * @since   v1.01
	 * @see com.afunms.common.base.DaoInterface#save(com.afunms.common.base.BaseVo)
	 */
	public boolean save(BaseVo vo) {
		CustomReportVo report = (CustomReportVo)vo;
		StringBuffer sql = new StringBuffer("insert into nms_customreport(name,type,code,userId,isCreate,isSend,mailTitle,mailDesc,fileType,sendDate,sendTime) values('");
		sql.append(report.getName());
		sql.append("','");
		sql.append(report.getType());
		sql.append("','");
		sql.append(report.getCode());
		sql.append("','");
		sql.append(report.getUserId());
		sql.append("','");
		sql.append(report.getIsCreate());
		sql.append("','");
		sql.append(report.getIsSend());
		sql.append("','");
		sql.append(report.getMailTitle());
		sql.append("','");
		sql.append(report.getMailDesc());
		sql.append("','");
		sql.append(report.getFileType());
		sql.append("','");
		sql.append(report.getSendDate());
		sql.append("','");
		sql.append(report.getSendTime());
		sql.append("')");
		return this.saveOrUpdate(sql.toString());
	}

	/**
	 * update:
	 *
	 * @param vo
	 * @return
	 *
	 * @since   v1.01
	 * @see com.afunms.common.base.DaoInterface#update(com.afunms.common.base.BaseVo)
	 */
	public boolean update(BaseVo vo) {
		CustomReportVo report = (CustomReportVo)vo;
		StringBuffer sql = new StringBuffer("update nms_customreport set name='");
		sql.append(report.getName());
		sql.append("',type='");
		sql.append(report.getType());
		sql.append("',code='");
		sql.append(report.getCode());
		sql.append("',userId='");
		sql.append(report.getUserId());
		sql.append("',isCreate='");
		sql.append(report.getIsCreate());
		sql.append("',isSend='");
		sql.append(report.getIsSend());
		sql.append("',mailTitle='");
		sql.append(report.getMailTitle());
		sql.append("',mailDesc='");
		sql.append(report.getMailDesc());
		sql.append("',fileType='");
		sql.append(report.getFileType());
		sql.append("',sendDate='");
		sql.append(report.getSendDate());
		sql.append("',sendTime='");
		sql.append(report.getSendTime());
		sql.append("' where id=");
		sql.append(report.getId());
		return this.saveOrUpdate(sql.toString());
	}

}

