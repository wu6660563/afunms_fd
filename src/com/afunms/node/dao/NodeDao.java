/*
 * @(#)NodeDao.java     v1.01, Dec 22, 2012
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.dao;

import java.sql.ResultSet;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.CommonUtil;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.node.model.Column;
import com.afunms.node.model.Table;

/**
 * ClassName:   NodeDao.java
 * <p>
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        Dec 22, 2012 6:53:12 PM
 */
public class NodeDao extends BaseDao implements DaoInterface {

    /**
     * logger:
     * <p>��־
     *
     * @since   v1.01
     */
    private static final SysLogger logger = SysLogger.getLogger(NodeDao.class);

    /**
     * loadFromRS:
     * <p>ͨ����������� {@link BaseVo}
     *
     * @param   rs
     *          - �����
     * @return  {@link BaseVo}
     *          - baseVo
     *
     * @since   v1.01
     * @see     com.afunms.common.base.BaseDao#loadFromRS(java.sql.ResultSet)
     */
    @Override
    public BaseVo loadFromRS(ResultSet rs) {
        return null;
    }

    /**
     * save:
     * <p>���� {@link BaseVo} �����ݿ�(δʵ��)
     *
     * @param   vo
     *          - {@link BaseVo}
     * @return  {@link Boolean}
     *          - �������ɹ����򷵻� <code>true</code> �����򷵻� <code>false</code>
     *
     * @since   v1.01
     * @see     com.afunms.common.base.DaoInterface#save(com.afunms.common.base.BaseVo)
     */
    public boolean save(BaseVo vo) {
        return false;
    }

    /**
     * update:
     * <p>�޸� {@link BaseVo} �����ݿ�(δʵ��)
     *
     * @param   vo
     *          - {@link BaseVo}
     * @return  {@link Boolean}
     *          - ����޸ĳɹ����򷵻� <code>true</code> �����򷵻� <code>false</code>
     *
     * @since   v1.01
     * @see com.afunms.common.base.DaoInterface#update(com.afunms.common.base.BaseVo)
     */
    public boolean update(BaseVo vo) {
        return false;
    }

    /**
     * createTable:
     * <p>������
     *
     * @param   node
     *          - �豸
     * @param   list
     *          - ��
     * @return  {@link Boolean}
     *          - ��������ɹ��򷵻� <code>true</code> �����򷵻� <code>false</code>
     *
     * @since   v1.01
     */
    @SuppressWarnings("static-access")
    public boolean createTable(NodeDTO node, List<Table> list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        for (Table table : list) {
            List<Column> columnList = table.getColumnList();
            StringBuffer tableStringBuffer = new StringBuffer(200);
            tableStringBuffer.append("create table if not exists " + table.getName() + node.getNodeid());
            tableStringBuffer.append("(");
            for (Column column : columnList) {
                tableStringBuffer.append(column.getName());
                tableStringBuffer.append(" ");
                tableStringBuffer.append(column.getType());
                if (column.getLength() > 0) {
                    tableStringBuffer.append(" ");
                    tableStringBuffer.append("(" + column.getLength() + ")");
                }
                if (column.isNotNull()) {
                    tableStringBuffer.append(" ");
                    tableStringBuffer.append("not null");
                }
                if (column.isAutoIncrement()) {
                    tableStringBuffer.append(" ");
                    tableStringBuffer.append("auto_increment");
                }
                tableStringBuffer.append(",");
            }
            tableStringBuffer.append(" PRIMARY KEY (" + table.getPrimaryKey() + ")");
            tableStringBuffer.append(")");
            tableStringBuffer.append(" ENGINE=" + table.getEngine());
            tableStringBuffer.append(" DEFAULT CHARSET=" + table.getCharset());
            String tableSql = tableStringBuffer.toString();
            logger.info(tableSql);
            conn.addBatch(tableSql);
        }
        return conn.executeBatch();
    }

    /**
     * dropTable:
     * <p>ɾ����
     *
     * @param   node
     *          - �豸
     * @param   list
     *          - ��
     * @return  {@link Boolean}
     *          - ��������ɹ��򷵻� <code>true</code> �����򷵻� <code>false</code>
     * @since   v1.01
     */
    public boolean dropTable(NodeDTO node, List<Table> list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        for (Table table : list) {
            String sql = "drop table if exists " + table.getName() + node.getNodeid();
            conn.addBatch(sql);
        }
        return conn.executeBatch();
    }
}

