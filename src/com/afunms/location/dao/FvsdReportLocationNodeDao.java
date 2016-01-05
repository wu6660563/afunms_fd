package com.afunms.location.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.location.model.FvsdReportLocationNode;

public class FvsdReportLocationNodeDao extends BaseDao implements DaoInterface {

    public FvsdReportLocationNodeDao() {
        super("nms_node_fvsd_report_location");
    }

    @Override
    public BaseVo loadFromRS(ResultSet rs) {
        FvsdReportLocationNode fvsdReportLocationNode = new FvsdReportLocationNode();
        try {
            fvsdReportLocationNode.setId(rs.getString("id"));
            fvsdReportLocationNode.setNodeid(rs.getString("nodeid"));
            fvsdReportLocationNode.setType(rs.getString("type"));
            fvsdReportLocationNode.setSubtype(rs.getString("subtype"));
            fvsdReportLocationNode.setFvsdReportLocationId(rs
                    .getString("fvsd_report_location_id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fvsdReportLocationNode;
    }

    public boolean save(BaseVo vo) {
        FvsdReportLocationNode fvsdReportLocationNode = (FvsdReportLocationNode) vo;
        StringBuffer sql = new StringBuffer(100);
        sql.append("insert into " + table
                + "(nodeid,type,subtype,fvsd_report_location_id)values(");
        sql.append("'");
        sql.append(fvsdReportLocationNode.getNodeid());
        sql.append("','");
        sql.append(fvsdReportLocationNode.getType());
        sql.append("','");
        sql.append(fvsdReportLocationNode.getSubtype());
        sql.append("','");
        sql.append(fvsdReportLocationNode.getFvsdReportLocationId());
        sql.append("')");
        return saveOrUpdate(sql.toString());
    }

    public boolean update(BaseVo vo) {
        return false;
    }

    @SuppressWarnings("unchecked")
    public List<FvsdReportLocationNode> find(String nodeid, String type, String subtype) {
        StringBuffer sql = new StringBuffer(200);
        sql.append("select * from " + table + " where nodeid='");
        sql.append(nodeid);
        sql.append("' and type='");
        sql.append(type);
        sql.append("' and subtype='");
        sql.append(subtype);
        sql.append("'");
        return findByCriteria(sql.toString());
    }

    public boolean delete(String nodeid, String type, String subtype) {
        StringBuffer sql = new StringBuffer(200);
        sql.append("delete from " + table + " where nodeid='");
        sql.append(nodeid);
        sql.append("' and type='");
        sql.append(type);
        sql.append("' and subtype='");
        sql.append(subtype);
        sql.append("'");
        return saveOrUpdate(sql.toString());
    }

    public boolean delete(String fvsdReportLocationId) {
        StringBuffer sql = new StringBuffer(200);
        sql.append("delete from " + table + " where fvsd_report_location_id='");
        sql.append(fvsdReportLocationId);
        sql.append("'");
        return saveOrUpdate(sql.toString());
    }

    public boolean save(List<FvsdReportLocationNode> list) {
        for (FvsdReportLocationNode fvsdReportLocationNode : list) {
            StringBuffer sql = new StringBuffer(100);
            sql.append("insert into " + table
                    + "(nodeid,type,subtype,fvsd_report_location_id)values(");
            sql.append("'");
            sql.append(fvsdReportLocationNode.getNodeid());
            sql.append("','");
            sql.append(fvsdReportLocationNode.getType());
            sql.append("','");
            sql.append(fvsdReportLocationNode.getSubtype());
            sql.append("','");
            sql.append(fvsdReportLocationNode.getFvsdReportLocationId());
            sql.append("')");
            conn.addBatch(sql.toString());
        }
        conn.executeBatch();
        return false;
    }

    public boolean update(List<FvsdReportLocationNode> list) {
        for (FvsdReportLocationNode fvsdReportLocationNode : list) {
            StringBuffer sql = new StringBuffer(100);
            sql.append("update " + table + " set ");
            sql.append("nodeid='");
            sql.append(fvsdReportLocationNode.getNodeid());
            sql.append("',type='");
            sql.append(fvsdReportLocationNode.getType());
            sql.append("',subtype='");
            sql.append(fvsdReportLocationNode.getSubtype());
            sql.append("',fvsd_report_location_id='");
            sql.append(fvsdReportLocationNode.getFvsdReportLocationId());
            sql.append("' where id='");
            sql.append(fvsdReportLocationNode.getId());
            sql.append("'");
            conn.addBatch(sql.toString());
        }
        conn.executeBatch();
        return false;
    }
}