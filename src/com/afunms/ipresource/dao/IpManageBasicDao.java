/*
 * @(#)IpManagerBasicDao.java     v1.01, Feb 14, 2014
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.ipresource.dao;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.SysLogger;
import com.afunms.ipresource.model.IpManageBasicVo;

/**
 * ClassName: IpManageBasicDao.java
 * <p>
 * IpManageBasicVo 的Dao方法
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Feb 14, 2014 11:24:16 AM
 * @mail wupinlong@dhcc.com.cn
 */
public class IpManageBasicDao extends BaseDao implements DaoInterface {

	public IpManageBasicDao() {
		super("ip_manage_basic");
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
		IpManageBasicVo vo = new IpManageBasicVo();
		try {
			Calendar tempCal = Calendar.getInstance();
			Date cc = new Date();
			cc.setTime(rs.getTimestamp("collecttime").getTime());
			tempCal.setTime(cc);
			vo.setId(rs.getLong("id"));
			vo.setRelateipaddr(rs.getString("relateipaddr"));
			vo.setBak(rs.getString("bak"));
			vo.setCollecttime(tempCal);
			vo.setIfband(rs.getString("ifband"));
			vo.setIfindex(rs.getString("ifindex"));
			vo.setIfmanage(rs.getString("ifmanage"));
			vo.setIpaddress(rs.getString("ipaddress"));
			vo.setMac(rs.getString("mac"));
		} catch (Exception e) {
			SysLogger.error("IpManagerBasicDao.loadFromRS()", e);
		}
		return vo;
	}

	/**
	 * save:
	 *
	 * @param baseVo
	 * @return
	 *
	 * @since   v1.01
	 * @see com.afunms.common.base.DaoInterface#save(com.afunms.common.base.BaseVo)
	 */
	public boolean save(BaseVo baseVo) {
		IpManageBasicVo vo = (IpManageBasicVo) baseVo;
		StringBuffer sql = new StringBuffer();
		sql
				.append("insert into "
						+ table
						+ "(relateipaddr,ifindex,ipaddress,mac,ifband,ifmanage,collecttime,bak) values('");
		sql.append(vo.getRelateipaddr());
		sql.append("','");
		sql.append(vo.getIfindex());
		sql.append("','");
		sql.append(vo.getIpaddress());
		sql.append("','");
		sql.append(vo.getMac());
		sql.append("','");
		sql.append(vo.getIfband());
		sql.append("','");
		sql.append(vo.getIfmanage());
		sql.append("','");
		Calendar cal = Calendar.getInstance();
		cal.setTime(vo.getCollecttime().getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(cal.getTime());
		sql.append(date);
		sql.append("','");
		sql.append(vo.getBak());
		sql.append("')");
		return saveOrUpdate(sql.toString());
	}

	/**
	 * update:
	 *
	 * @param baseVo
	 * @return
	 *
	 * @since   v1.01
	 * @see com.afunms.common.base.DaoInterface#update(com.afunms.common.base.BaseVo)
	 */
	public boolean update(BaseVo baseVo) {
		IpManageBasicVo vo = (IpManageBasicVo) baseVo;
		StringBuffer sql = new StringBuffer();
		sql.append("update " + table + " set relateipaddr='");
		sql.append(vo.getRelateipaddr());
		sql.append("',ifindex='");
		sql.append(vo.getIfindex());
		sql.append("',ipaddress='");
		sql.append(vo.getIpaddress());
		sql.append("',mac='");
		sql.append(vo.getMac());
		sql.append("',ifband='");
		sql.append(vo.getIfband());
		sql.append("',ifmanage='");
		sql.append(vo.getIfmanage());
		sql.append("',bak='");
		sql.append(vo.getBak());
		sql.append("' where id=");
		sql.append(vo.getId());
		return saveOrUpdate(sql.toString());
	}

	/**
	 * findByIpaddress:
	 * <p>
	 *
	 * @param ipaddress
	 * @return
	 *
	 * @since   v1.01
	 */
	public IpManageBasicVo findByIpaddress(String ipaddress) {
		List list = findByCriteria("select * from " + table
				+ " where ipaddress=" + ipaddress);
		if (list != null && list.size() > 0) {
			IpManageBasicVo vo = (IpManageBasicVo) list.get(0);
			return vo;
		}
		return null;
	}
	
	public boolean truncateTable() {
		conn.addBatch("truncate table ip_manage_basic");
		boolean bln = conn.executeBatch();
		return bln;
	}
	
	public boolean setManage(String id,String ifmanage) {
		StringBuffer sql = new StringBuffer();
		sql.append("update " + table + " set ifmanage='"+ifmanage+"' where id=");
		sql.append(id);
		return saveOrUpdate(sql.toString());
	}

}
