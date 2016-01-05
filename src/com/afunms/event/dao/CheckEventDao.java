/**
 * <p>Description:operate table NMS_MENU and NMS_ROLE_MENU</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-07
 */

package com.afunms.event.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.SysLogger;
import com.afunms.event.model.CheckEvent;
import com.afunms.indicators.model.NodeDTO;

public class CheckEventDao extends BaseDao implements DaoInterface {

    private static SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private static SysLogger logger = SysLogger.getLogger(CheckEventDao.class.getName());

    public CheckEventDao() {
        super("nms_checkevent");
    }

    // -------------load all --------------
    public List loadAll() {
        List list = new ArrayList(5);
        try {
            rs = conn
                    .executeQuery("select * from nms_checkevent order by name");
            while (rs.next())
                list.add(loadFromRS(rs));
        } catch (Exception e) {
            SysLogger.error("CheckEventDao:loadAll()", e);
            list = null;
        } finally {
            conn.close();
        }
        return list;
    }

    public List loadByWhere(String where) {
        List list = new ArrayList();
        try {
            rs = conn.executeQuery("select * from nms_checkevent " + where);
            while (rs.next()) {
                list.add(loadFromRS(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean save(BaseVo baseVo) {
        CheckEvent vo = (CheckEvent) baseVo;
        StringBuffer sql = new StringBuffer(100);
        sql
                .append("insert into nms_checkevent(alarm_id,name,node_id,type,subtype,sindex,level,content,collecttime)values(");
        sql.append("'");
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

    public boolean savecheckevent(BaseVo baseVo) {
        CheckEvent vo = (CheckEvent) baseVo;
        boolean flag = true;
        // 先删除,如果有该指标告警
        delete(vo.getName());
        StringBuffer sql = new StringBuffer(100);
        sql.append("insert into nms_checkevent(name,alarmlevel)values(");
        sql.append("'");
        sql.append(vo.getName());
        sql.append("',");
        sql.append(vo.getAlarmlevel());
        sql.append(")");
        try {
            conn.executeUpdate(sql.toString());
        } catch (Exception e) {
            logger.error("CheckEventDao.savecheckevent()", e);
            flag = false;
        }
        // SysLogger.info(sql.toString());
        return flag;
    }

    public boolean delete(String name) {
        boolean flag = true;
        String sql = "delete from nms_checkevent where name='" + name + "'";
        try {
            conn.executeUpdate(sql);
        } catch (Exception e) {
            logger.error("CheckEventDao.delete(String name)", e);
            flag = false;
        }
        return flag;
    }

    public boolean empty() {
        String sql = "truncate nms_checkevent";
        // SysLogger.info(sql);
        return saveOrUpdate(sql);
    }

    // ---------------update a business----------------
    public boolean update(BaseVo baseVo) {
        return true;
    }

    public boolean delete(String[] id) {
        return true;
    }

    public BaseVo findByID(String id) {
        BaseVo vo = null;
        try {
            rs = conn.executeQuery("select * from nms_checkevent where id="
                    + id);
            if (rs.next())
                vo = loadFromRS(rs);
        } catch (Exception e) {
            SysLogger.error("EventListDao.findByID()", e);
            vo = null;
        } finally {
            conn.close();
        }
        return vo;
    }

    public int findByName(String name) {
        int flag = 0;
        CheckEvent vo = null;
        try {
            rs = conn.executeQuery("select * from nms_checkevent where name='"
                    + name + "'");
            if (rs.next()) {
                vo = (CheckEvent) loadFromRS(rs);
                flag = vo.getAlarmlevel();
            }

        } catch (Exception e) {
            // SysLogger.error("EventListDao.findByID()",e);
            // vo = null;
        } finally {
            // conn.close();
        }
        return flag;
    }

    public int findMaxAlarmLevelByName(String name) {
        int flag = 0;
        try {
            rs = conn
                    .executeQuery("select max(alarmlevel) from nms_checkevent where name like '%"
                            + name + "%'");
            if (rs.next()) {
                flag = rs.getInt("max(alarmlevel)");
            }
        } catch (Exception e) {
        } finally {
        }
        return flag;
    }

    public BaseVo loadFromRS(ResultSet rs) {
        CheckEvent vo = new CheckEvent();
        try {
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

    public BaseVo findCheckEventByName(String name) {
        CheckEvent vo = null;
        try {
            rs = conn.executeQuery("select * from nms_checkevent where name='"
                    + name + "'");
            if (rs.next()) {
                vo = (CheckEvent) loadFromRS(rs);
            }
        } catch (Exception e) {
            SysLogger.error("CheckEventDao.findByID()", e);
        }
        return vo;
    }

    public BaseVo findCheckEventByAlarmId(String alarmId) {
        CheckEvent vo = null;
        try {
            rs = conn
                    .executeQuery("select * from nms_checkevent where alarm_id='"
                            + alarmId + "'");
            if (rs.next()) {
                vo = (CheckEvent) loadFromRS(rs);
            }
        } catch (Exception e) {
            SysLogger.error("CheckEventDao.findCheckEventByAlarmId()", e);
        }
        return vo;
    }

    public BaseVo findCheckEventByAlarmIdAndSindex(String alarmId, String sindex) {
        CheckEvent vo = null;
        try {
            rs = conn
                    .executeQuery("select * from nms_checkevent where alarm_id='"
                            + alarmId + "' and sindex='" + sindex + "'");
            if (rs.next()) {
                vo = (CheckEvent) loadFromRS(rs);
            }
        } catch (Exception e) {
            SysLogger.error("CheckEventDao.findCheckEventByAlarmIdAndSindex()",
                    e);
        }
        return vo;
    }

    public boolean deleteByAlarmId(String alarmId) {
        boolean flag = true;
        String sql = "delete from nms_checkevent where alarm_id='" + alarmId
                + "'";
        try {
            conn.executeUpdate(sql);
        } catch (Exception e) {
            logger.error("CheckEventDao.deleteByAlarmId(String alarmId)", e);
            flag = false;
        }
        return flag;
    }

    public boolean deleteByAlarmIdAndSindex(String alarmId, String sindex) {
        boolean flag = true;
        String sql = "delete from nms_checkevent where alarm_id='" + alarmId
                + "' and sindex='" + sindex + "'";
        try {
            conn.executeUpdate(sql);
        } catch (Exception e) {
            logger.error("CheckEventDao.deleteByAlarmIdAndSindex(String alarmId, String sindex)", e);
            flag = false;
        }
        return flag;
    }

    public int findMaxAlarmLevel(List<NodeDTO> list) {
        int maxAlarmLevel = 0;
        if (list == null || list.size() == 0) {
            return maxAlarmLevel;
        }
        String sql = "select max(level) from nms_checkevent where";
        StringBuffer sqlBuffer = new StringBuffer();
        for (NodeDTO nodeDTO : list) {
            sqlBuffer.append(" (node_id='" + nodeDTO.getId() + "'");
            sqlBuffer.append(" and type='" + nodeDTO.getType() + "'");
            sqlBuffer.append(" and subtype='" + nodeDTO.getSubtype() + "')");
            sqlBuffer.append(" or");
        }
        sqlBuffer.append(" node_id='-1'");
        try {
            rs = conn.executeQuery(sql + sqlBuffer.toString());
            if (rs != null && rs.next()) {
                maxAlarmLevel = rs.getInt("max(level)");
            }
        } catch (Exception e) {
            logger.error("CheckEventDao.findMaxAlarmLevel() " + sql + sqlBuffer.toString(), e);
        }
        return maxAlarmLevel;
    }

    public List<CheckEvent> find(List<NodeDTO> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        String sql = " where";
        StringBuffer sqlBuffer = new StringBuffer();
        for (NodeDTO nodeDTO : list) {
            sqlBuffer.append(" (node_id='" + nodeDTO.getId() + "'");
            sqlBuffer.append(" and type='" + nodeDTO.getType() + "'");
            sqlBuffer.append(" and subtype='" + nodeDTO.getSubtype() + "')");
            sqlBuffer.append(" or");
        }
        sqlBuffer.append(" node_id='-1'");
        return findByCondition(sql + sqlBuffer.toString());
    }

    public List<CheckEvent> findCheckEvent(NodeDTO node) {
        List<CheckEvent> list = null;
        if (node == null) {
            return list;
        }
        String sql = "select * from nms_checkevent where";
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(" node_id='" + node.getId() + "'");
        sqlBuffer.append(" and type='" + node.getType() + "'");
        sqlBuffer.append(" and subtype='" + node.getSubtype() + "'");
        try {
            list = findByCriteria(sql + sqlBuffer.toString());
        } catch (Exception e) {
            logger.error("CheckEventDao.findMaxAlarmLevel() " + sql + sqlBuffer.toString(), e);
        }
        return list;
    }
}
