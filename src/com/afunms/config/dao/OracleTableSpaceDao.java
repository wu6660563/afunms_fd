/*
 * @(#)OracleTableSpaceDao.java     v1.01, Jan 23, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.config.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.config.model.OracleTableSpaceConfig;

/**
 * ClassName:   OracleTableSpaceDao.java
 * <p>{@link OracleTableSpaceDao} Oracle 表空间阀值
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        Jan 23, 2013 4:45:16 PM
 */
public class OracleTableSpaceDao extends BaseDao implements DaoInterface {

    public OracleTableSpaceDao() {
        super("nms_oratablespace_config");
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
        OracleTableSpaceConfig vo = new OracleTableSpaceConfig();
        try {
            vo.setId(rs.getInt("id"));
            vo.setNodeid(rs.getString("nodeid"));
            vo.setBak(rs.getString("bak"));
            vo.setIpaddress(rs.getString("ipaddress")); 
            vo.setDbType(rs.getString("db_type"));
            vo.setDbName(rs.getString("db_name"));
            vo.setLinkuse(rs.getString("linkuse"));
            vo.setTableSpace(rs.getString("table_space"));
            vo.setFileName(rs.getString("file_name"));
            vo.setTableIndex(rs.getInt("table_index"));
            vo.setMonflag(rs.getInt("monflag"));
            vo.setReportflag(rs.getInt("reportflag"));
            vo.setSms(rs.getInt("sms"));
            vo.setSms1(rs.getInt("sms1"));
            vo.setSms2(rs.getInt("sms2"));
            vo.setSms3(rs.getInt("sms3"));
            vo.setLimenvalue(rs.getInt("limenvalue"));
            vo.setLimenvalue1(rs.getInt("limenvalue1"));
            vo.setLimenvalue2(rs.getInt("limenvalue2"));
        } catch(Exception e) {
            e.printStackTrace();
            vo = null;
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
    public boolean save(BaseVo baseVo) {
        OracleTableSpaceConfig vo = (OracleTableSpaceConfig) baseVo;
        StringBuffer sql = new StringBuffer(100);
        sql.append("insert into nms_oratablespace_config(ipaddress,nodeid,db_type,db_name,table_index,table_space,file_name,linkuse,sms,bak,monflag,reportflag,sms1,sms2,sms3,limenvalue,limenvalue1,limenvalue2)values(");
        sql.append("'");
        sql.append(vo.getIpaddress());
        sql.append("','");
        sql.append(vo.getNodeid());
        sql.append("','");
        sql.append(vo.getDbType());
        sql.append("','");
        sql.append(vo.getDbName());
        sql.append("','");
        sql.append(vo.getTableIndex());
        sql.append("','");
        sql.append(vo.getTableSpace());
        sql.append("','");
        sql.append(vo.getFileName());
        sql.append("','");
        sql.append(vo.getLinkuse());
        sql.append("',");
        sql.append(vo.getSms());
        sql.append(",'");
        sql.append(vo.getBak());
        sql.append("',");
        sql.append(vo.getMonflag());
        sql.append(",");
        sql.append(vo.getReportflag());
        sql.append(",");
        sql.append(vo.getSms1());
        sql.append(",");
        sql.append(vo.getSms2());
        sql.append(",");
        sql.append(vo.getSms3());
        sql.append(",");
        sql.append(vo.getLimenvalue());
        sql.append(",");
        sql.append(vo.getLimenvalue1());
        sql.append(",");
        sql.append(vo.getLimenvalue2());
        sql.append(")");
        return saveOrUpdate(sql.toString());
    }

    public boolean save(List<OracleTableSpaceConfig> list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        for (OracleTableSpaceConfig oracleTableSpaceConfig : list) {
            StringBuffer sql = new StringBuffer(100);
            sql.append("insert into nms_oratablespace_config(ipaddress,nodeid,db_type,db_name,table_index,table_space,file_name,linkuse,sms,bak,monflag,reportflag,sms1,sms2,sms3,limenvalue,limenvalue1,limenvalue2)values(");
            sql.append("'");
            sql.append(oracleTableSpaceConfig.getIpaddress());
            sql.append("','");
            sql.append(oracleTableSpaceConfig.getNodeid());
            sql.append("','");
            sql.append(oracleTableSpaceConfig.getDbType());
            sql.append("','");
            sql.append(oracleTableSpaceConfig.getDbName());
            sql.append("','");
            sql.append(oracleTableSpaceConfig.getTableIndex());
            sql.append("','");
            sql.append(oracleTableSpaceConfig.getTableSpace());
            sql.append("','");
            sql.append(oracleTableSpaceConfig.getFileName());
            sql.append("','");
            sql.append(oracleTableSpaceConfig.getLinkuse());
            sql.append("',");
            sql.append(oracleTableSpaceConfig.getSms());
            sql.append(",'");
            sql.append(oracleTableSpaceConfig.getBak());
            sql.append("',");
            sql.append(oracleTableSpaceConfig.getMonflag());
            sql.append(",");
            sql.append(oracleTableSpaceConfig.getReportflag());
            sql.append(",");
            sql.append(oracleTableSpaceConfig.getSms1());
            sql.append(",");
            sql.append(oracleTableSpaceConfig.getSms2());
            sql.append(",");
            sql.append(oracleTableSpaceConfig.getSms3());
            sql.append(",");
            sql.append(oracleTableSpaceConfig.getLimenvalue());
            sql.append(",");
            sql.append(oracleTableSpaceConfig.getLimenvalue1());
            sql.append(",");
            sql.append(oracleTableSpaceConfig.getLimenvalue2());
            sql.append(")");
            conn.addBatch(sql.toString());
        }
        return conn.executeBatch();
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
    public boolean update(BaseVo baseVo) {
        OracleTableSpaceConfig vo = (OracleTableSpaceConfig) baseVo;
        StringBuffer sql = new StringBuffer();
        sql.append("update nms_oratablespace_config set ipaddress='");
        sql.append(vo.getIpaddress());
        sql.append("',nodeid='");
        sql.append(vo.getNodeid());   
        sql.append("',file_name='");
        sql.append(vo.getFileName());   
        sql.append("',table_index=");
        sql.append(vo.getTableIndex()); 
        sql.append(",db_type='");
        sql.append(vo.getDbType());
        sql.append("',linkuse='");
        if (vo.getLinkuse() != null) {
            sql.append(vo.getLinkuse());
        } else {
            sql.append("");
        }
        sql.append("',sms=");
        sql.append(vo.getSms());
        sql.append(",bak='");
        sql.append(vo.getBak());
        sql.append("',monflag=");
        sql.append(vo.getMonflag());
        sql.append(",reportflag=");
        sql.append(vo.getReportflag());
        sql.append(",sms1=");
        sql.append(vo.getSms1());
        sql.append(",sms2=");
        sql.append(vo.getSms2());
        sql.append(",sms3=");
        sql.append(vo.getSms3());
        sql.append(",limenvalue=");
        sql.append(vo.getLimenvalue());
        sql.append(",limenvalue1=");
        sql.append(vo.getLimenvalue1());
        sql.append(",limenvalue2=");
        sql.append(vo.getLimenvalue2());    
        sql.append(" where id=");
        sql.append(vo.getId());
        return saveOrUpdate(sql.toString());
    }

    public List<OracleTableSpaceConfig> findByNodeId(String nodid) {
        String sql = "select * from " + table + " where nodeid='" + nodid + "'";
        return findByCriteria(sql);
    }

    public OracleTableSpaceConfig findByNodeIdAndTableSpaceName(String nodid, String name) {
        String sql = "select * from nms_oratablespace_config where nodeid='" + nodid
        +"' and table_space ='"+ name + "' ";
        List<OracleTableSpaceConfig> list = findByCriteria(sql);
        OracleTableSpaceConfig config = null;
        if (list != null && list.size() > 0) {
            config = list.get(0);
        }
        return config;
    }

    /**
     * getIps:
     * <p>返回所有IP地址的列表
     *
     * @return  {@link List<String>}
     *          - 所有IP地址的列表
     *
     * @since   v1.01
     */
    public List<String> getIps() {
        List<String> list = new ArrayList<String>();
        try{
            String sql="select distinct h.ipaddress from " + table + " h order by h.ipaddress";
            rs = conn.executeQuery(sql);
            while(rs.next()){
                list.add(rs.getString("ipaddress"));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    /**
     * empty:
     * <p>清空所有数据
     *
     * @return  {@link Boolean}
     *          - 返回清空的结果，成功则返回 <code>true</code>, 否则返回 <code>false</code>
     *
     * @since   v1.01
     */
    public boolean empty() {
        return saveOrUpdate("delete from " + table);
    }

    /**
     * delete:
     * <p>删除指定的设备的数据
     *
     * @param   nodeid
     *          - 指定的设备的 nodeid
      * @return  {@link Boolean}
     *          - 返回删除的结果，成功则返回 <code>true</code>, 否则返回 <code>false</code>
     *
     * @since   v1.01
     */
    public boolean delete(String nodeid) {
        return saveOrUpdate("delete from " + table + " where nodeid='" + nodeid + "'");
    }
}

