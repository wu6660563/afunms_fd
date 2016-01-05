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

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.BaseVo;
import com.afunms.dataArchiving.model.DataArchiving;
import com.afunms.dataArchiving.model.DataArchivingHistory;

public class DataArchivingHistoryDao extends BaseDao implements DaoInterface {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");;

    public DataArchivingHistoryDao() {
        super("nms_data_archiving_history");
    }

    public BaseVo loadFromRS(ResultSet rs) {
        DataArchivingHistory vo = new DataArchivingHistory();
        try {
            vo.setId(rs.getInt("id"));
            vo.setDataArchivingId(rs.getString("data_archiving_id"));
            vo.setDataArchivingType(rs.getString("data_archiving_type"));
            vo.setExecuteStartTime(simpleDateFormat.parse(rs.getString("execute_start_time")));
            vo.setExecuteEndTime(simpleDateFormat.parse(rs.getString("execute_end_time")));
            vo.setRecordTime(simpleDateFormat.parse(rs.getString("record_time")));
        } catch (Exception e) {
            e.printStackTrace();
            vo = null;
        }
        return vo;
    }

    public boolean save(BaseVo vo) {
        DataArchivingHistory dataArchivingHistory = (DataArchivingHistory) vo;
        StringBuffer sql = new StringBuffer(100);
        sql.append("insert into " + table + "(data_archiving_id,data_archiving_type,execute_start_time,execute_end_time,record_time)values(");
        sql.append("'");
        sql.append(dataArchivingHistory.getDataArchivingId());
        sql.append("','");
        sql.append(dataArchivingHistory.getDataArchivingType());
        sql.append("','");
        sql.append(simpleDateFormat.format(dataArchivingHistory.getExecuteStartTime()));
        sql.append("','");
        sql.append(simpleDateFormat.format(dataArchivingHistory.getExecuteEndTime()));
        sql.append("','");
        sql.append(simpleDateFormat.format(dataArchivingHistory.getRecordTime()) );
        sql.append("')");
        return saveOrUpdate(sql.toString());
    }

    public boolean update(BaseVo vo) {
        DataArchivingHistory dataArchivingHistory = (DataArchivingHistory) vo;
        StringBuffer sql = new StringBuffer(100);
        sql.append("update " + table + " set data_archiving_id='");
        sql.append(dataArchivingHistory.getDataArchivingId());
        sql.append("',data_archiving_type='");
        sql.append(dataArchivingHistory.getDataArchivingType());
        sql.append("',execute_start_time='");
        sql.append(simpleDateFormat.format(dataArchivingHistory.getExecuteStartTime()));
        sql.append("',execute_end_time='");
        sql.append(simpleDateFormat.format(dataArchivingHistory.getExecuteEndTime()));
        sql.append("',record_time='");
        sql.append(simpleDateFormat.format(dataArchivingHistory.getRecordTime()));
        sql.append("'where id=");
        sql.append(dataArchivingHistory.getId());
        return saveOrUpdate(sql.toString());
    }

}
