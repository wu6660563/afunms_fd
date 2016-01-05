package com.afunms.alarm.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.afunms.alarm.model.FvsdAlarm;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.SysLogger;
import com.afunms.event.dao.CheckEventDao;
import com.afunms.event.model.CheckEvent;
import com.afunms.indicators.model.NodeDTO;

public class SendFvsdAlarmDao extends BaseDao implements DaoInterface {

    private static SysLogger logger = SysLogger.getLogger(SendFvsdAlarmDao.class.getName());

    public SendFvsdAlarmDao() {
        super("nms_fvsd_alarm");
    }

    public boolean save(BaseVo baseVo) {
        FvsdAlarm vo = (FvsdAlarm) baseVo;
        StringBuffer sql = new StringBuffer(100);
        sql.append("insert into nms_fvsd_alarm(fvsd_code,alarm_id,name,node_id,type,subtype,sindex,level,content,collecttime)values(");
        sql.append("'");
        sql.append(vo.getFvsdCode());
        sql.append("','");
        sql.append(vo.getAlarmId());
        sql.append("','");
        sql.append(vo.getName());
        sql.append("','");
        sql.append(vo.getNodeId());
        sql.append("','");
        sql.append(vo.getType());
        sql.append("','");
        sql.append(vo.getSubtype());
        sql.append("','");
        sql.append(vo.getSindex());
        sql.append("','");
        sql.append(vo.getAlarmlevel());
        sql.append("','");
        sql.append(vo.getContent());
        sql.append("','");
        sql.append(vo.getCollecttime());
        sql.append("')");
        return saveOrUpdate(sql.toString());
    }

    public boolean delete(String fvsdCode) {
        boolean flag = true;
        String sql = "delete from nms_fvsd_alarm where fvsd_code='" + fvsdCode + "'";
        try {
            conn.executeUpdate(sql);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public boolean empty() {
        String sql = "delete from nms_fvsd_alarm";
        return saveOrUpdate(sql);
    }

    public BaseVo loadFromRS(ResultSet rs) {
        FvsdAlarm vo = new FvsdAlarm();
        try {
            vo.setFvsdCode(rs.getString("fvsd_code"));
            vo.setAlarmId(rs.getString("alarm_id"));
            vo.setName(rs.getString("name"));
            vo.setNodeId(rs.getString("node_id"));
            vo.setType(rs.getString("type"));
            vo.setSubtype(rs.getString("subtype"));
            vo.setSindex(rs.getString("sindex"));
            vo.setAlarmlevel(rs.getInt("level"));
            vo.setContent(rs.getString("content"));
            vo.setCollecttime(rs.getString("collecttime"));
        } catch (Exception e) {
            SysLogger.error("EventListDao.loadFromRS()", e);
            vo = null;
        }
        return vo;
    }

    public BaseVo findFvsdAlarmByAlarmIdAndSindex(String alarmId, String sindex) {
        FvsdAlarm vo = null;
        try {
            rs = conn.executeQuery("select * from nms_fvsd_alarm where alarm_id='"
                            + alarmId + "' and sindex='" + sindex + "'");
            if (rs.next()) {
                vo = (FvsdAlarm) loadFromRS(rs);
            }
        } catch (Exception e) {
            SysLogger.error("SendFvsdAlarmDao.findCheckEventByAlarmIdAndSindex()",
                    e);
        }
        return vo;
    }

    public boolean update(BaseVo vo) {
        // TODO Auto-generated method stub
        return false;
    }

}