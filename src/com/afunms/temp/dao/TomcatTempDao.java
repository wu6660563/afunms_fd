/*
 * @(#)TomcatTempDao.java     v1.01, Jan 10, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.temp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.temp.model.TomcatNodeTemp;

/**
 * ClassName:   TomcatTempDao.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        Jan 10, 2013 11:11:36 AM
 */
public class TomcatTempDao extends BaseDao implements DaoInterface {

    public TomcatTempDao() {
        super("nms_tomcat_temp");
    }

    @Override
    public BaseVo loadFromRS(ResultSet rs) {
        TomcatNodeTemp nodeTemp = new TomcatNodeTemp();
        try {
            nodeTemp.setId(rs.getInt("id"));
            nodeTemp.setNodeid(rs.getString("nodeid"));
            nodeTemp.setPing(rs.getString("ping"));
            nodeTemp.setTomcatVersion(rs.getString("tomcat_version"));
            nodeTemp.setJVMVersion(rs.getString("JVM_version"));
            nodeTemp.setJVMVendor(rs.getString("JVM_vendor"));
            nodeTemp.setOSName(rs.getString("OS_name"));
            nodeTemp.setOSVersion(rs.getString("OS_version"));
            nodeTemp.setOSArchitecture(rs.getString("OS_architecture"));
            nodeTemp.setFreeMemory(rs.getString("free_memory"));
            nodeTemp.setTotalMemory(rs.getString("total_memory"));
            nodeTemp.setMaxMemory(rs.getString("max_memory"));
            nodeTemp.setMemoryUtilization(rs.getString("memory_utilization"));
            nodeTemp.setCollectTime(rs.getString("memory_utilization"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nodeTemp;
    }

    public boolean save(BaseVo vo) {
        TomcatNodeTemp nodeTemp = (TomcatNodeTemp) vo;   
        StringBuffer sql = new StringBuffer(500);
        sql.append("insert into " + table + "(nodeid,ping,tomcat_version,JVM_version,JVM_vendor,");
        sql.append("OS_name,OS_version,OS_architecture,free_memory,total_memory,max_memory,memory_utilization,collect_time)values('");
        sql.append(nodeTemp.getNodeid());
        sql.append("','");
        sql.append(nodeTemp.getPing());
        sql.append("','");
        sql.append(nodeTemp.getTomcatVersion());
        sql.append("','");
        sql.append(nodeTemp.getJVMVersion());
        sql.append("','");
        sql.append(nodeTemp.getJVMVendor());
        sql.append("','");
        sql.append(nodeTemp.getOSName());
        sql.append("','");
        sql.append(nodeTemp.getOSVersion());
        sql.append("','");
        sql.append(nodeTemp.getOSArchitecture());
        sql.append("','");
        sql.append(nodeTemp.getFreeMemory());
        sql.append("','");
        sql.append(nodeTemp.getTotalMemory());
        sql.append("','");
        sql.append(nodeTemp.getMaxMemory());
        sql.append("','");
        sql.append(nodeTemp.getMemoryUtilization());
        sql.append("','");
        sql.append(nodeTemp.getCollectTime());
        sql.append("')");
        return saveOrUpdate(sql.toString());
    }

    public boolean update(BaseVo vo) {
        return false;
    }

    public boolean deleteByNodeId(String nodeid) {
        String sql = "delete from " + table + " where nodeid='" + nodeid + "'"; 
        return saveOrUpdate(sql);
    }

    public TomcatNodeTemp findByNodeid(String nodeid) {
        String sql = " where nodeid='" + nodeid + "'"; 
        List<TomcatNodeTemp> list = (List<TomcatNodeTemp>) findByCondition(sql);
        TomcatNodeTemp nodeTemp = null;
        if (list != null && list.size() > 0) {
            nodeTemp = list.get(0);
        }
        return nodeTemp;
    }
}

