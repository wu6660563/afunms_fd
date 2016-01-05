/*
 * @(#)URLConfigDao.java     v1.01, Mar 5, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.application.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.afunms.application.model.URLConfig;
import com.afunms.application.model.WebConfig;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;

/**
 * ClassName:   URLConfigDao.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        Mar 5, 2013 10:49:46 PM
 */
public class URLConfigDao extends BaseDao implements DaoInterface {

    public URLConfigDao() {
        super("nms_url_config");
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
        URLConfig config = new URLConfig();
        try {
            config.setId(rs.getInt("id"));
            config.setName(rs.getString("name"));
            config.setIpaddress(rs.getString("ipaddress"));
            config.setUrl(rs.getString("url"));
            config.setMonFlag(rs.getInt("mon_flag"));
            config.setPageSize(rs.getInt("page_size"));
            config.setSupperid(rs.getInt("supper_id"));
            config.setTimeout(rs.getInt("timeout"));
            config.setBid(rs.getString("bid"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return config;
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
        URLConfig config = (URLConfig) vo;
        StringBuffer sql=new StringBuffer();
        sql.append("insert into " + table + "(id,name,ipaddress,url,timeout,mon_flag,page_size,bid,supper_id) values(");
        sql.append(config.getId());
        sql.append(",'");
        sql.append(config.getName());
        sql.append("','");
        sql.append(config.getIpaddress());
        sql.append("','");
        sql.append(config.getUrl());
        sql.append("','");
        sql.append(config.getTimeout());
        sql.append("','");
        sql.append(config.getMonFlag());
        sql.append("','");
        sql.append(config.getPageSize());
        sql.append("','");
        sql.append(config.getBid());
        sql.append("','");
        sql.append(config.getSupperid());
        sql.append("')");
        return saveOrUpdate(sql.toString());
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
        URLConfig config = (URLConfig) vo;
        StringBuffer sql=new StringBuffer();
        sql.append("update " + table + " set name ='");
        sql.append(config.getName());
        sql.append("',url='");
        sql.append(config.getUrl());
        sql.append("',timeout='");
        sql.append(config.getTimeout());
        sql.append("',mon_flag='");
        sql.append(config.getMonFlag());
        sql.append("',page_size='");
        sql.append(config.getPageSize());
        sql.append("',bid='");
        sql.append(config.getBid());
        sql.append("',supper_id='");
        sql.append(config.getSupperid());
        sql.append("' where id="+config.getId());
        return saveOrUpdate(sql.toString());
    }

}

