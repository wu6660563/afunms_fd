/*
 * @(#)CustomReportDao.java     v1.01, Jun 19, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.capreport.dao;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.afunms.capreport.model.CustomReportHistory;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;

/**
 * 
 * ClassName: CustomReportDao.java
 * <p>
 * 
 * @author Œ‚∆∑¡˙
 * @version v1.01
 * @since v1.01
 * @Date Jun 19, 2013 10:37:31 AM
 */
public class CustomReportHistoryDao extends BaseDao implements DaoInterface {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public CustomReportHistoryDao() {
		super("customreport_history");
	}

	/**
	 * loadFromRS:
	 * 
	 * @param rs
	 * @return
	 * 
	 * @since v1.01
	 * @see com.afunms.common.base.BaseDao#loadFromRS(java.sql.ResultSet)
	 */
	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		CustomReportHistory vo = new CustomReportHistory();
		try {
			vo.setId(rs.getInt("id"));
			vo.setReportId(rs.getInt("reportId"));
			vo.setFileName(rs.getString("fileName"));
			Calendar cal = Calendar.getInstance();
			Date newdate = new Date();
			newdate.setTime(rs.getTimestamp("createDate").getTime());
			cal.setTime(newdate);
			vo.setCreateDate(cal);
			vo.setIsSuccess(rs.getString("isSuccess"));
		} catch (Exception e) {
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
	 * @since v1.01
	 * @see com.afunms.common.base.DaoInterface#save(com.afunms.common.base.BaseVo)
	 */
	public boolean save(BaseVo vo) {
		CustomReportHistory history = (CustomReportHistory) vo;
		Calendar tempCal = (Calendar) history.getCreateDate();
		Date cc = tempCal.getTime();
		String createDate = sdf.format(cc);
		StringBuffer sql = new StringBuffer(
				"insert into customreport_history(reportId,fileName,createDate,isSuccess) values(");
		sql.append(history.getReportId());
		sql.append(",'");
		sql.append(history.getFileName());
		sql.append("','");
		sql.append(createDate);
		sql.append("','");
		sql.append(history.getIsSuccess());
		sql.append("')");
		return this.saveOrUpdate(sql.toString());
	}

	/**
	 * update:
	 * 
	 * @param vo
	 * @return
	 * 
	 * @since v1.01
	 * @see com.afunms.common.base.DaoInterface#update(com.afunms.common.base.BaseVo)
	 */
	public boolean update(BaseVo vo) {
		CustomReportHistory history = (CustomReportHistory) vo;
		Calendar tempCal = (Calendar) history.getCreateDate();
		Date cc = tempCal.getTime();
		String createDate = sdf.format(cc);
		StringBuffer sql = new StringBuffer(
				"update customreport_history set reportId=");
		sql.append(history.getReportId());
		sql.append(",fileName='");
		sql.append(history.getFileName());
		sql.append("',createDate='");
		sql.append(createDate);
		sql.append("',isSuccess='");
		sql.append(history.getIsSuccess());
		sql.append("' where id=");
		sql.append(history.getId());
		return this.saveOrUpdate(sql.toString());
	}
	
	public boolean inHistory(String fileName,int reportId) {
		StringBuffer sql = new StringBuffer("select * from customreport_history where reportId="+reportId+" and fileName='"+fileName+"'");
		boolean bln = false;
		try {
			rs = conn.executeQuery(sql.toString());
			if(rs.next()){
				bln = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bln;
	}

}
