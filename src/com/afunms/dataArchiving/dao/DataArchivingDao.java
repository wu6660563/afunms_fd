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
import com.afunms.dataArchiving.model.DataArchiving;

public class DataArchivingDao extends BaseDao implements DaoInterface {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");;

    public DataArchivingDao() {
        super("nms_data_archiving");
    }

    public BaseVo loadFromRS(ResultSet rs) {
        DataArchiving vo = new DataArchiving();
        try {
            vo.setId(rs.getInt("id"));
            vo.setName(rs.getString("name"));
            vo.setType(rs.getString("type"));
            vo.setInterval(rs.getLong("poll_interval"));
//            System.out.println(simpleDateFormat.parse(rs.getString("start_time")));
//            System.out.println(simpleDateFormat.parse(rs.getString("start_time")).getTime());
            vo.setStartTime(simpleDateFormat.parse(rs.getString("start_time")));
//            System.out.println(simpleDateFormat.parse(rs.getString("last_time")));
//            System.out.println(simpleDateFormat.parse(rs.getString("last_time")).getTime());
            vo.setLastTime(simpleDateFormat.parse(rs.getString("last_time")));
            vo.setFatherId(rs.getString("father_id"));
            vo.setRetentionTime(rs.getLong("retention_time"));
        } catch (Exception e) {
            e.printStackTrace();
            vo = null;
        }
        return vo;
    }

    public boolean save(BaseVo vo) {
        DataArchiving dataArchiving = (DataArchiving) vo;
        StringBuffer sql = new StringBuffer(100);
        sql.append("insert into " + table + "(name,type,poll_interval,start_time,last_time,father_id,retention_time)values(");
        sql.append("'");
        sql.append(dataArchiving.getName());
        sql.append("','");
        sql.append(dataArchiving.getType());
        sql.append("','");
        sql.append(dataArchiving.getInterval());
        sql.append("','");
        sql.append(simpleDateFormat.format(dataArchiving.getStartTime()));
        sql.append("','");
        sql.append(simpleDateFormat.format(dataArchiving.getLastTime()));
        sql.append("','");
        sql.append(dataArchiving.getFatherId());
        sql.append("','");
        sql.append(dataArchiving.getRetentionTime());
        sql.append("')");
        return saveOrUpdate(sql.toString());
    }

    public boolean update(BaseVo vo) {
        DataArchiving dataArchiving = (DataArchiving) vo;
        StringBuffer sql = new StringBuffer(100);
        sql.append("update " + table + " set name='");
        sql.append(dataArchiving.getName());
        sql.append("',type='");
        sql.append(dataArchiving.getType());
        sql.append("',poll_interval='");
        sql.append(dataArchiving.getInterval());
        sql.append("',start_time='");
        sql.append(simpleDateFormat.format(dataArchiving.getStartTime()));
        sql.append("',last_time='");
        sql.append(simpleDateFormat.format(dataArchiving.getLastTime()));
        sql.append("',father_id='");
        sql.append(dataArchiving.getFatherId());
        sql.append("',retention_time='");
        sql.append(dataArchiving.getRetentionTime());
        sql.append("' where id=");
        sql.append(dataArchiving.getId());
        return saveOrUpdate(sql.toString());
    }

    public List<DataArchiving> getChildDataArchiving(String dataArchivingId) {
        String sql = " where father_id='" + dataArchivingId + "'";
        return findByCondition(sql);
    }
}
