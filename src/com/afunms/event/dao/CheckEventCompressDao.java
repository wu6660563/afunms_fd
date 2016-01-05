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
import com.afunms.event.model.CheckEventCompress;
import com.afunms.indicators.model.NodeDTO;

public class CheckEventCompressDao extends BaseDao implements DaoInterface {

    private static SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private static SysLogger logger = SysLogger.getLogger(CheckEventCompressDao.class.getName());

    public CheckEventCompressDao() {
        super("nms_checkevent_compress");
    }

    // -------------load all --------------
    public List loadAll() {
        List list = new ArrayList(5);
        try {
            rs = conn
                    .executeQuery("select * from nms_checkevent_compress order by name");
            while (rs.next())
                list.add(loadFromRS(rs));
        } catch (Exception e) {
            SysLogger.error("CheckEventCompressDao:loadAll()", e);
            list = null;
        } finally {
            conn.close();
        }
        return list;
    }

    public List loadByWhere(String where) {
        List list = new ArrayList();
        try {
            rs = conn.executeQuery("select * from nms_checkevent_compress " + where);
            while (rs.next()) {
                list.add(loadFromRS(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean save(BaseVo baseVo) {
        CheckEventCompress vo = (CheckEventCompress) baseVo;
        StringBuffer sql = new StringBuffer(100);
        sql.append("insert into nms_checkevent_compress(alarm_id,name,node_id,type,subtype," +
        		"sindex,level,content,firsttime,collecttime)values(");
        sql.append(vo.getAlarmId());
        sql.append(",'");
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
        sql.append(vo.getFirsttime());
        sql.append("','");
        sql.append(vo.getCollecttime());
        sql.append("')");
        return saveOrUpdate(sql.toString());
    }

    public boolean savecheckevent(BaseVo baseVo) {
        CheckEventCompress vo = (CheckEventCompress) baseVo;
        boolean flag = true;
        // 先删除,如果有该指标告警
        delete(vo.getType(),vo.getSubtype(), vo.getName());
        StringBuffer sql = new StringBuffer(100);
        sql.append("insert into nms_checkevent_compress(name,alarmlevel)values(");
        sql.append("'");
        sql.append(vo.getName());
        sql.append("',");
        sql.append(vo.getAlarmlevel());
        sql.append(")");
        try {
            conn.executeUpdate(sql.toString());
        } catch (Exception e) {
            logger.error("CheckEventCompressDao.savecheckevent()", e);
            flag = false;
        }
        // SysLogger.info(sql.toString());
        return flag;
    }

    public boolean delete(String type, String subtype, String name) {
        boolean flag = true;
        String sql = "delete from nms_checkevent_compress where type='"
        		+ type +"' and subtype='" + subtype +"' and name='" + name + "'";
        try {
            conn.executeUpdate(sql);
        } catch (Exception e) {
            logger.error("CheckEventCompressDao.delete(String name)", e);
            flag = false;
        }
        return flag;
    }

    public boolean empty() {
        String sql = "truncate nms_checkevent_compress";
        // SysLogger.info(sql);
        return saveOrUpdate(sql);
    }

    public boolean update(BaseVo baseVo) {
    	return false;
    }
    
    /**
     * 更新压缩告警信息，不更新firsttime字段
     */

    public boolean updateExceptFirsttime(BaseVo baseVo) {
    	 CheckEventCompress vo = (CheckEventCompress) baseVo;
         boolean flag = true;   
         StringBuffer sql = new StringBuffer(100);
         sql.append("update nms_checkevent_compress set level=");
         sql.append(vo.getAlarmlevel());
         sql.append(",content='");
         sql.append(vo.getContent());
         sql.append("',collecttime='");
         sql.append(vo.getCollecttime());
         sql.append("' where node_id=" + vo.getNodeId() 
        		 +" and type='" + vo.getType() +"' and subtype='" + vo.getSubtype() +"'" 
        		 +" and name='" + vo.getName() +"'");
         try {
        	 //System.out.println(sql.toString());
             conn.executeUpdate(sql.toString());
         } catch (Exception e) {
             logger.error("CheckEventCompressDao.savecheckevent()", e);
             flag = false;
         }
         // SysLogger.info(sql.toString());
         return flag;
    }

    public boolean delete(String[] id) {
        return true;
    }

    public BaseVo findByID(String id) {
        BaseVo vo = null;
        try {
            rs = conn.executeQuery("select * from nms_checkevent_compress where id="
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
        CheckEventCompress vo = null;
        try {
            rs = conn.executeQuery("select * from nms_checkevent_compress where name='"
                    + name + "'");
            if (rs.next()) {
                vo = (CheckEventCompress) loadFromRS(rs);
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
                    .executeQuery("select max(alarmlevel) from nms_checkevent_compress where name like '%"
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
        CheckEventCompress vo = new CheckEventCompress();
        try {
            vo.setAlarmId(rs.getString("alarm_id"));
            vo.setName(rs.getString("name"));
            vo.setNodeId(rs.getString("node_id"));
            vo.setType(rs.getString("type"));
            vo.setSubtype(rs.getString("subtype"));
            vo.setSindex(rs.getString("sindex"));
            vo.setAlarmlevel(rs.getInt("level"));
            vo.setContent(rs.getString("content"));
            vo.setFirsttime(rs.getString("firsttime"));
            vo.setCollecttime(rs.getString("collecttime"));
        } catch (Exception e) {
            SysLogger.error("EventListDao.loadFromRS()", e);
            vo = null;
        }
        return vo;
    }

    public BaseVo findCheckEventCompressByName(String name) {
        CheckEventCompress vo = null;
        try {
            rs = conn.executeQuery("select * from nms_checkevent_compress where name='"
                    + name + "'");
            if (rs.next()) {
                vo = (CheckEventCompress) loadFromRS(rs);
            }
        } catch (Exception e) {
            SysLogger.error("CheckEventCompressDao.findByID()", e);
        }
        return vo;
    }

    public BaseVo findCheckEventCompressByAlarmId(String alarmId) {
        CheckEventCompress vo = null;
        try {
            rs = conn
                    .executeQuery("select * from nms_checkevent_compress where alarm_id='"
                            + alarmId + "'");
            if (rs.next()) {
                vo = (CheckEventCompress) loadFromRS(rs);
            }
        } catch (Exception e) {
            SysLogger.error("CheckEventCompressDao.findCheckEventCompressByAlarmId()", e);
        }
        return vo;
    }

    public BaseVo findCheckEventCompressByAlarmIdAndSindex(String alarmId, String sindex) {
        CheckEventCompress vo = null;
        try {
            rs = conn
                    .executeQuery("select * from nms_checkevent_compress where alarm_id='"
                            + alarmId + "' and sindex='" + sindex + "'");
            if (rs.next()) {
                vo = (CheckEventCompress) loadFromRS(rs);
            }
        } catch (Exception e) {
            SysLogger.error("CheckEventCompressDao.findCheckEventCompressByAlarmIdAndSindex()",
                    e);
        }
        return vo;
    }

    public boolean deleteByAlarmId(String alarmId) {
        boolean flag = true;
        String sql = "delete from nms_checkevent_compress where alarm_id='" + alarmId
                + "'";
        try {
            conn.executeUpdate(sql);
        } catch (Exception e) {
            logger.error("CheckEventCompressDao.deleteByAlarmId(String alarmId)", e);
            flag = false;
        }
        return flag;
    }

    public boolean deleteByAlarmIdAndSindex(String alarmId, String sindex) {
        boolean flag = true;
        String sql = "delete from nms_checkevent_compress where alarm_id='" + alarmId
                + "' and sindex='" + sindex + "'";
        try {
            conn.executeUpdate(sql);
        } catch (Exception e) {
            logger.error("CheckEventCompressDao.deleteByAlarmIdAndSindex(String alarmId, String sindex)", e);
            flag = false;
        }
        return flag;
    }

    public int findMaxAlarmLevel(List<NodeDTO> list) {
        int maxAlarmLevel = 0;
        if (list == null || list.size() == 0) {
            return maxAlarmLevel;
        }
        String sql = "select max(level) from nms_checkevent_compress where";
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
            logger.error("CheckEventCompressDao.findMaxAlarmLevel() " + sql + sqlBuffer.toString(), e);
        }
        return maxAlarmLevel;
    }

    public List<CheckEventCompress> find(List<NodeDTO> list) {
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

    public List<CheckEventCompress> findCheckEventCompress(NodeDTO node) {
        List<CheckEventCompress> list = null;
        if (node == null) {
            return list;
        }
        String sql = "select * from nms_checkevent_compress where";
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(" node_id='" + node.getId() + "'");
        sqlBuffer.append(" and type='" + node.getType() + "'");
        sqlBuffer.append(" and subtype='" + node.getSubtype() + "'");
        try {
            list = findByCriteria(sql + sqlBuffer.toString());
        } catch (Exception e) {
            logger.error("CheckEventCompressDao.findMaxAlarmLevel() " + sql + sqlBuffer.toString(), e);
        }
        return list;
    }
}
