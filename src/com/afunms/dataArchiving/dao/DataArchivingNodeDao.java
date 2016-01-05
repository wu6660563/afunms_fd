/**
 * <p>Description:operate table NMS_MENU and NMS_ROLE_MENU</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-07
 */

package com.afunms.dataArchiving.dao;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.BaseVo;
import com.afunms.common.util.SysLogger;
import com.afunms.dataArchiving.model.DataArchivingNode;

public class DataArchivingNodeDao extends BaseDao implements DaoInterface {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");;

    public DataArchivingNodeDao() {
        super("nms_data_archiving_node");
    }

    public BaseVo loadFromRS(ResultSet rs) {
        DataArchivingNode vo = new DataArchivingNode();
        try {
            vo.setId(rs.getInt("id"));
            vo.setDataArchivingId(rs.getString("data_archiving_id"));
            vo.setDataArchivingType(rs.getString("data_archiving_type"));
            vo.setExecuteStartTime(simpleDateFormat.parse(rs
                    .getString("execute_start_time")));
            vo.setExecuteEndTime(simpleDateFormat.parse(rs
                    .getString("execute_end_time")));
            vo.setRecordTime(simpleDateFormat
                    .parse(rs.getString("record_time")));
            vo.setTableName(rs.getString("table_name"));
            vo.setNodeId(rs.getString("node_id"));
            vo.setNodeType(rs.getString("node_type"));
            vo.setNodeSubtype(rs.getString("node_subtype"));
        } catch (Exception e) {
            e.printStackTrace();
            vo = null;
        }
        return vo;
    }

    public boolean save(BaseVo vo) {
        DataArchivingNode dataArchivingNode = (DataArchivingNode) vo;
        StringBuffer sql = new StringBuffer(100);
        sql
                .append("insert into "
                        + table
                        + "(data_archiving_id,data_archiving_type,execute_start_time,execute_end_time,record_time,table_name,node_id,node_type,node_subtype)values(");
        sql.append("'");
        sql.append(dataArchivingNode.getDataArchivingId());
        sql.append("','");
        sql.append(dataArchivingNode.getDataArchivingType());
        sql.append("','");
        sql.append(simpleDateFormat.format(dataArchivingNode
                .getExecuteStartTime()));
        sql.append("','");
        sql.append(simpleDateFormat.format(dataArchivingNode
                .getExecuteEndTime()));
        sql.append("','");
        sql.append(simpleDateFormat.format(dataArchivingNode.getRecordTime()));
        sql.append("','");
        sql.append(dataArchivingNode.getTableName());
        sql.append("','");
        sql.append(dataArchivingNode.getNodeId());
        sql.append("','");
        sql.append(dataArchivingNode.getNodeType());
        sql.append("','");
        sql.append(dataArchivingNode.getNodeSubtype());
        sql.append("')");
        return saveOrUpdate(sql.toString());
    }

    public boolean update(BaseVo vo) {
        DataArchivingNode dataArchivingNode = (DataArchivingNode) vo;
        StringBuffer sql = new StringBuffer(100);
        sql.append("update " + table + " set data_archiving_id='");
        sql.append(dataArchivingNode.getDataArchivingId());
        sql.append("',data_archiving_type='");
        sql.append(dataArchivingNode.getDataArchivingType());
        sql.append("',execute_start_time='");
        sql.append(simpleDateFormat.format(dataArchivingNode
                .getExecuteStartTime()));
        sql.append("',execute_end_time='");
        sql.append(simpleDateFormat.format(dataArchivingNode
                .getExecuteEndTime()));
        sql.append("',record_time='");
        sql.append(simpleDateFormat.format(dataArchivingNode.getRecordTime()));
        sql.append("',table_name='");
        sql.append(dataArchivingNode.getTableName());
        sql.append("',node_id='");
        sql.append(dataArchivingNode.getNodeId());
        sql.append("',node_type='");
        sql.append(dataArchivingNode.getNodeType());
        sql.append("',node_subtype='");
        sql.append(dataArchivingNode.getNodeSubtype());
        sql.append("'where id=");
        sql.append(dataArchivingNode.getId());
        return saveOrUpdate(sql.toString());
    }

    /**
     * 设备查找到最新的归档记录
     */
    public List<DataArchivingNode> findByNode(String dataArchivingId, String nodeId, String nodeType,
            String nodeSubtype) {
        return findByCriteria("select * from " + table +" where data_archiving_id='" + dataArchivingId + "' and node_id='"
                    + nodeId + "' and node_type='" + nodeType + "' and node_subtype='" + nodeSubtype + "'");
            
    }
}
