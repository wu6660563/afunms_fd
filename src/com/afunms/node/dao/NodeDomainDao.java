/*
 * @(#)NodeDomainDao.java     v1.01, 2013 9 28
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
import com.afunms.indicators.model.NodeDTO;
import com.afunms.node.model.NodeDomain;

/**
 * ClassName:   NodeDomainDao.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 9 28 22:06:17
 */
public class NodeDomainDao extends BaseDao implements DaoInterface {

    public NodeDomainDao() {
        super("nms_node_domain");
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
        NodeDomain domain = new NodeDomain();
        try {
            domain.setId(rs.getInt("id"));
            domain.setNodeId(rs.getString("node_id"));
            domain.setNodeType(rs.getString("node_type"));
            domain.setNodeSubtype(rs.getString("node_subtype"));
            domain.setDomain(rs.getString("domain"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return domain;
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
        NodeDomain domain = (NodeDomain) vo;
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ");
        sql.append(table);
        sql.append("(node_id, node_type, node_subtype, domain)values('");
        sql.append(domain.getNodeId());
        sql.append("','");
        sql.append(domain.getNodeType());
        sql.append("','");
        sql.append(domain.getNodeSubtype());
        sql.append("','");
        sql.append(domain.getDomain());
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
        // TODO Auto-generated method stub
        return false;
    }

    public List<NodeDomain> find(NodeDTO nodeDTO) {
        String sql = " where node_id='" + nodeDTO.getNodeid() + "' and node_type='"+ nodeDTO.getType() + "' and node_subtype='"+ nodeDTO.getSubtype() + "'";
        return findByCondition(sql);
    }

    public boolean delete(NodeDTO nodeDTO) {
        return delete(nodeDTO.getNodeid(), nodeDTO.getType(), nodeDTO.getSubtype());
    }
    
    public boolean delete(String nodeId, String nodeType, String nodeSubtype) {
        String sql = "delete from " + table + " where node_id='" +nodeId + "' and node_type='"+ nodeType + "' and node_subtype='"+ nodeSubtype + "'";
        return saveOrUpdate(sql);
    }
}

