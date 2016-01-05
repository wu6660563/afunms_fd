/*
 * @(#)UrlPerformanceInfoTabaleDao.java     v1.01, May 30, 2013
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
import com.afunms.node.model.URLPerformanceInfo;
import com.afunms.portscan.model.PortConfig;

/**
 * ClassName:   UrlPerformanceInfoTabaleDao.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        May 30, 2013 10:25:10 AM
 */
public class UrlPerformanceInfoTabaleDao extends BaseDao implements DaoInterface {

    public UrlPerformanceInfoTabaleDao(String tableName) {
        super(tableName);
    }

    /**
     * loadFromRS:
     *
     * @param rs
     * @return
     *
     * @since   v1.01
     * @see com.afunms.node.dao.PerformaceInfoTableDao#loadFromRS(java.sql.ResultSet)
     */
    @Override
    public BaseVo loadFromRS(ResultSet rs) {
        URLPerformanceInfo info = new URLPerformanceInfo();
        try {
            info.setId(rs.getInt("id"));
            info.setConnectUtilization(rs.getInt("connect_utilization"));
            info.setResponseTime(rs.getInt("response_time"));
            info.setPageSize(rs.getInt("page_size"));
            info.setChangeRate(rs.getInt("change_rate"));
            info.setCollecttime(rs.getString("collecttime"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return info;
    }

    public List<URLPerformanceInfo> findByCollectTime(String startTime, String endTime) {
        String sql = "select * from " + table + " where collecttime>='" + startTime + "' and collecttime<='" + endTime + "'";
        return findByCriteria(sql);
    }

    public boolean save(BaseVo vo) {
        // 应该保证端口不能重复
        URLPerformanceInfo info = (URLPerformanceInfo) vo;
        StringBuffer sb = new StringBuffer(200);
        sb.append("insert into ");
        sb.append(table);
        sb.append("(connect_utilization,response_time,page_size,change_rate,collecttime)values('");
        sb.append(info.getConnectUtilization());
        sb.append("','");
        sb.append(info.getResponseTime());
        sb.append("','");
        sb.append(info.getPageSize());
        sb.append("','");
        sb.append(info.getChangeRate());
        sb.append("','");
        sb.append(info.getCollecttime());
        sb.append("')");
        return saveOrUpdate(sb.toString());
    }

    public boolean update(BaseVo vo) {
        // TODO Auto-generated method stub
        return false;
    }
}

