package com.afunms.config.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.SysLogger;
import com.afunms.config.model.Portconfig;

/**
 * ClassName: PortconfigDao.java
 * <p>
 * ∂Àø⁄≈‰÷√ {@link DaoInterface}
 * 
 * @author ƒÙ¡÷
 * @version v1.01
 * @since v1.01
 * @Date Nov 19, 2012 2:52:18 PM
 */
public class PortconfigDao extends BaseDao implements DaoInterface {

    public PortconfigDao() {
        super("system_portconfig");
    }

    @Override
    public BaseVo loadFromRS(ResultSet rs) {
        Portconfig vo = new Portconfig();
        try {
            vo.setId(rs.getInt("id"));
            vo.setBak(rs.getString("bak"));
            vo.setIpaddress(rs.getString("ipaddress"));
            vo.setLinkuse(rs.getString("linkuse"));
            vo.setName(rs.getString("name"));
            vo.setPortindex(rs.getInt("portindex"));
            vo.setReportflag(rs.getInt("reportflag"));
            vo.setSms(rs.getInt("sms"));
            vo.setImportant(rs.getInt("important"));
            vo.setInportalarm(rs.getString("inportalarm"));
            vo.setOutportalarm(rs.getString("outportalarm"));
            vo.setSpeed(rs.getString("speed"));
        } catch (Exception e) {
            SysLogger.error("PortconfigDao.loadFromRS()", e);
        }
        return vo;
    }

    public boolean save(BaseVo vo) {
        Portconfig portconfig = (Portconfig) vo;
        StringBuffer sql = new StringBuffer(100);
        sql
                .append("insert into system_portconfig(ipaddress,name,portindex,linkuse,sms,important,bak,reportflag,inportalarm,outportalarm,speed)values(");
        sql.append("'");
        sql.append(portconfig.getIpaddress());
        sql.append("','");
        sql.append(portconfig.getName());
        sql.append("',");
        sql.append(portconfig.getPortindex());
        sql.append(",'");
        sql.append(portconfig.getLinkuse());
        sql.append("',");
        sql.append(portconfig.getSms());
        sql.append(",");
        sql.append(portconfig.getImportant());
        sql.append(",'");
        sql.append(portconfig.getBak());
        sql.append("',");
        sql.append(portconfig.getReportflag());
        sql.append(",'");
        sql.append(portconfig.getInportalarm());
        sql.append("','");
        sql.append(portconfig.getOutportalarm());
        sql.append("','");
        sql.append(portconfig.getSpeed());
        sql.append("')");
        return saveOrUpdate(sql.toString());
    }

    public boolean save(List<Portconfig> list) {
        boolean result = false;
        try {
            for (Portconfig portconfig : list) {
                StringBuffer sql = new StringBuffer(100);
                sql
                        .append("insert into system_portconfig(ipaddress,name,portindex,linkuse,sms,important,bak,reportflag,inportalarm,outportalarm,speed)values(");
                sql.append("'");
                sql.append(portconfig.getIpaddress());
                sql.append("','");
                sql.append(portconfig.getName());
                sql.append("',");
                sql.append(portconfig.getPortindex());
                sql.append(",'");
                sql.append(portconfig.getLinkuse());
                sql.append("',");
                sql.append(portconfig.getSms());
                sql.append(",");
                sql.append(portconfig.getImportant());
                sql.append(",'");
                sql.append(portconfig.getBak());
                sql.append("',");
                sql.append(portconfig.getReportflag());
                sql.append(",'");
                sql.append(portconfig.getInportalarm());
                sql.append("','");
                sql.append(portconfig.getOutportalarm());
                sql.append("','");
                sql.append(portconfig.getSpeed());
                sql.append("')");
                conn.addBatch(sql.toString());
            }
            result = conn.executeBatch();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean update(BaseVo vo) {
        Portconfig portconfig = (Portconfig) vo;
        StringBuffer sql = new StringBuffer();
        sql.append("update system_portconfig set ipaddress='");
        sql.append(portconfig.getIpaddress());
        sql.append("',name='");
        sql.append(portconfig.getName());
        sql.append("',portindex=");
        sql.append(portconfig.getPortindex());
        sql.append(",linkuse='");
        if (portconfig.getLinkuse() != null) {
            sql.append(portconfig.getLinkuse());
        } else {
            sql.append("");
        }
        sql.append("',sms=");
        sql.append(portconfig.getSms());
        sql.append(",important=");
        sql.append(portconfig.getImportant());
        sql.append(",bak='");
        sql.append(portconfig.getBak());
        sql.append("',reportflag=");
        sql.append(portconfig.getReportflag());
        sql.append(",inportalarm='");
        sql.append(portconfig.getInportalarm());
        sql.append("',outportalarm='");
        sql.append(portconfig.getOutportalarm());
        sql.append("',speed='");
        sql.append(portconfig.getSpeed());
        sql.append("' where id=");
        sql.append(portconfig.getId());
        return saveOrUpdate(sql.toString());
    }

    public Portconfig getByip(String ip) {
        try {
            rs = conn
                    .executeQuery("select * from system_portconfig where ipaddress='"
                            + ip + "' order by id");
            if (rs.next())
                return (Portconfig) loadFromRS(rs);
        } catch (Exception e) {
            SysLogger.error("PortconfigDao:getByip()", e);
        } finally {
            conn.close();
        }
        return null;
    }

    public List getIps() {
        List list = new ArrayList();
        try {
            String sql = "select distinct h.ipaddress from system_portconfig h order by h.ipaddress";
            rs = conn.executeQuery(sql);
            while (rs.next()) {
                list.add(rs.getString("ipaddress"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void empty() {
        String sql = "delete from system_portconfig";
        saveOrUpdate(sql);
    }

    public void emptyNode(String ipaddress) {
        String sql = "delete from system_portconfig where ipaddress='"
                + ipaddress + "'";
        saveOrUpdate(sql);
    }

    public List<Portconfig> loadByIpaddress(String ip) {
        String sql = "select * from system_portconfig where ipaddress='" + ip
                + "' order by portindex";
        return findByCriteria(sql);
    }

    public boolean deleteByIpaddress(String ip) {
        String sql = "delete from system_portconfig where ipaddress='" + ip
                + "' ";
        return saveOrUpdate(sql);
    }

    public List<Portconfig> findByImportant(String ipaddress, String important) {
        String  sql = "select * from system_portconfig where ipaddress='" + ipaddress + "' and important='" + important
        + "' order by portindex";
        return findByCriteria(sql);
    }

    public List<Portconfig> getAllBySms() {
        String  sql = "select * from system_portconfig where sms='1' order by portindex";
        return findByCriteria(sql);
    }

    public List<Portconfig> getBySms(String ipaddress) {
        String  sql = "select * from system_portconfig where ipaddress='" + ipaddress + "' and sms='1' order by portindex";
        return findByCriteria(sql);
    }

    public List<Portconfig> findBySms(String sms) {
        String  sql = "select * from system_portconfig where sms='" + sms
        + "' order by portindex";
        return findByCriteria(sql);
    }

    public List<Portconfig> findBySms(String ipaddress, String sms) {
        String  sql = "select * from system_portconfig where ipaddress='" + ipaddress + "' and sms='" + sms
        + "' order by portindex";
        return findByCriteria(sql);
    }
    
    public Hashtable getIpsHash(String ipaddress) {
        Hashtable hash = new Hashtable();
        List list = loadByIpaddress(ipaddress);
        if (list != null) {
            for (Object object : list) {
                Portconfig portconfig = (Portconfig) object;
                if (portconfig.getLinkuse()!= null && portconfig.getLinkuse().trim().length()>0){                       
                    hash.put(portconfig.getIpaddress()+":"+portconfig.getPortindex(),portconfig.getLinkuse());
                }else{
                    hash.put(portconfig.getIpaddress()+":"+portconfig.getPortindex(),"");
                }
            }
        }
        return hash;
    }

    public List getByIpAndReportflag(String ip,Integer reportflag) {
        String sql="select * from system_portconfig h where h.ipaddress = '"+ip+"' and h.reportflag="+reportflag+" order by h.portindex";
        return findByCriteria(sql);
    }

    public Portconfig loadPortconfig(int id) {
        Portconfig portconfig = null;
        try {
            portconfig = (Portconfig) findByID(String.valueOf(id));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return portconfig;
    }

    public Portconfig getByipandindex(String ipaddress, String sIndex) {
        String sql="select * from system_portconfig h where h.ipaddress = '"+ipaddress+"' and h.portindex="+sIndex+" order by h.portindex";
        List list = findByCriteria(sql);
        Portconfig portconfig = null;
        if (list != null || list.size() > 0) {
            portconfig = (Portconfig) list.get(0);
        }
        return portconfig;
    }
    
    public Portconfig getPanelByipandindex(String ipaddress, String sIndex) {
        return getByipandindex(ipaddress, sIndex);
    }
}
