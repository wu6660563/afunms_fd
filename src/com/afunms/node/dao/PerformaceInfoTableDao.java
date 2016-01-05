/*
 * @(#)TableDao.java     v1.01, Jan 11, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.node.model.PerformanceInfo;

/**
 * ClassName:   PerformaceInfoTableDao.java
 * <p>���ڲ�ѯ ���ܴ�����
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        Jan 11, 2013 2:57:20 PM
 */
public class PerformaceInfoTableDao extends BaseDao implements DaoInterface {

    public PerformaceInfoTableDao(String tableName) {
        super(tableName);
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
        PerformanceInfo info = new PerformanceInfo();
        try {
            info.setId(rs.getInt("id"));
            info.setIpAddress(rs.getString("ipaddress"));
            info.setRestype(rs.getString("restype"));
            info.setCategory(rs.getString("category"));
            info.setEntity(rs.getString("entity"));
            info.setSubentity(rs.getString("subentity"));
            info.setThevalue(rs.getString("thevalue"));
            info.setCollecttime(rs.getString("collecttime"));
            info.setUnit(rs.getString("unit"));
            info.setCount(rs.getString("count"));
            info.setBak(rs.getString("bak"));
            info.setChname(rs.getString("chname"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return info;
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
        PerformanceInfo info = (PerformanceInfo) vo;
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ");
        sql.append(table);
        sql.append("(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime)values('");
        sql.append(info.getIpAddress());
        sql.append("','");
        sql.append(info.getRestype());
        sql.append("','");
        sql.append(info.getCategory());
        sql.append("','");
        sql.append(info.getEntity());
        sql.append("','");
        sql.append(info.getSubentity());
        sql.append("','");
        sql.append(info.getUnit());
        sql.append("','");
        sql.append(info.getChname());
        sql.append("','");
        sql.append(info.getBak());
        sql.append("','");
        sql.append(info.getCount());
        sql.append("','");
        sql.append(info.getThevalue());
        sql.append("','");
        sql.append(info.getCollecttime());
        sql.append("')");
        return saveOrUpdate(sql.toString());
    }

    public boolean save(List<PerformanceInfo> list) {
        return false;
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
        // TODO Auto-generated method stub
        return false;
    }

    public List<PerformanceInfo> find(String startTime, String endTime) {
        String sql = "select * from " + table + " where collecttime>='" + startTime + "' and collecttime<='" + endTime + "'";
        System.out.println(sql);
        return findByCriteria(sql);
    }

    public List<PerformanceInfo> find(String subentity, String startTime, String endTime) {
        String sql = "select * from " + table + " where subentity='" + subentity + "' and collecttime>='" + startTime + "' and collecttime<='" + endTime + "'";
        return findByCriteria(sql);
    }

    public String getAvgThevalue(String startTime, String endTime, String[] subentities) {
    	String avgValue = "0";
    	try {
			StringBuffer sql = new StringBuffer();
			sql.append("select avg(thevalue) avgping from ");
			sql.append(table);
			sql.append(" where collecttime>='" + startTime + "'");
			sql.append(" and collecttime<='" + endTime + "'");
			if (subentities != null && subentities.length > 0) {
				for (int i = 0; i < subentities.length; i++) {
					if (i == 0) {
						sql.append(" and (");
					} else {
						sql.append(" or");
					} 
					sql.append(" subentity='" + subentities[i] + "'");
				}
				sql.append(" )");
			}
			rs = conn.executeQuery(sql.toString());
			if (rs.next()) {
				avgValue = rs.getString("avgping");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return avgValue;
    }
    
    public String getMaxThevalue(String startTime, String endTime, String[] subentities) {
    	String maxValue = "0";
    	try {
			StringBuffer sql = new StringBuffer();
			sql.append("select max(thevalue) maxping from ");
			sql.append(table);
			sql.append(" where collecttime>='" + startTime + "'");
			sql.append(" and collecttime<='" + endTime + "'");
			if (subentities != null && subentities.length > 0) {
				for (int i = 0; i < subentities.length; i++) {
					if (i == 0) {
						sql.append(" and (");
					} else {
						sql.append(" or");
					} 
					sql.append(" subentity='" + subentities[i] + "'");
				}
				sql.append(" )");
			}
			rs = conn.executeQuery(sql.toString());
			if (rs.next()) {
				maxValue = rs.getString("maxping");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return maxValue;
    }
}

