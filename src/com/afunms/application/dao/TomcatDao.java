/**
 * <p>Description: topo_tomcat_node</p>
 * <p>Company:dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-12-13
 */

package com.afunms.application.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.application.model.Tomcat;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.CreateTableManager;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.NetworkUtil;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.polling.om.Pingcollectdata;

/**
 * ClassName:   TomcatDao.java
 * <p>{@link TomcatDao} 为 {@link Tomcat} 实例的 {@link DaoInterface} 类
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        Jan 6, 2013 3:49:09 PM
 */
public class TomcatDao extends BaseDao implements DaoInterface {

    /**
     * logger:
     * <p>日志
     * 
     * @since v1.01
     */
    private static final SysLogger logger = SysLogger.getLogger(Tomcat.class);

    /**
     * TomcatDao:
     * <p>默认的构造方法，构造一个 {@link TomcatDao} 实例
     *
     * @since   v1.01
     */
    public TomcatDao() {
        super("app_tomcat_node");
    }

    /**
     * loadFromRS:
     * <p>
     * 从结果集加载数据
     * 
     * @param rs -
     *            结果集
     * @return {@link BaseVo} - 实例
     * 
     * @since v1.01
     * @see com.afunms.common.base.BaseDao#loadFromRS(java.sql.ResultSet)
     */
    @Override
    public BaseVo loadFromRS(ResultSet rs) {
        Tomcat vo = new Tomcat();
        try {
            vo.setId(rs.getInt("id"));
            vo.setName(rs.getString("name"));
            vo.setIpAddress(rs.getString("ip_address"));
            vo.setPort(rs.getString("port"));
            vo.setUser(rs.getString("user"));
            vo.setPassword(rs.getString("password"));
            vo.setBid(rs.getString("bid"));
            vo.setMonflag(rs.getInt("monflag"));
            vo.setVersion(rs.getString("version"));
            vo.setJvmversion(rs.getString("jvmversion"));
            vo.setJvmvender(rs.getString("jvmvender"));
            vo.setOs(rs.getString("os"));
            vo.setOsversion(rs.getString("osversion"));
            vo.setSupperid(rs.getInt("supperid"));
        } catch (Exception e) {
            logger.error("TomcatDao.loadFromRS()", e);
        }
        return vo;
    }

    /**
     * save:
     * <p>保存 {@link Tomcat} 实例至数据库
     *
     * @param   vo
     *          - {@link Tomcat} 实例
     * @return  {@link Boolean}
     *          - 如果成功则返回 <code>true</code> ，否则返回 <code>false</code>
     *
     * @since   v1.01
     * @see com.afunms.common.base.DaoInterface#save(com.afunms.common.base.BaseVo)
     */
    public boolean save(BaseVo vo) {
        Tomcat tomcat = (Tomcat) vo;
        StringBuffer sql = new StringBuffer();
        sql.append("insert into app_tomcat_node(id,name,ip_address,ip_long,port,user,password,bid,");
        sql.append("monflag,version,jvmversion,jvmvender,os,osversion)values(");
        sql.append(tomcat.getId());
        sql.append(",'");
        sql.append(tomcat.getName());
        sql.append("','");
        sql.append(tomcat.getIpAddress());
        sql.append("',ip_long=");
        sql.append(NetworkUtil.ip2long(tomcat.getIpAddress()));
        sql.append(",'");
        sql.append(tomcat.getPort());
        sql.append("','");
        sql.append(tomcat.getUser());
        sql.append("','");
        sql.append(tomcat.getPassword());
        sql.append("','");
        sql.append(tomcat.getBid());
        sql.append("',");
        sql.append(tomcat.getMonflag());
        sql.append(",'");
        sql.append(tomcat.getVersion());
        sql.append("','");
        sql.append(tomcat.getJvmversion());
        sql.append("','");
        sql.append(tomcat.getJvmvender());
        sql.append("','");
        sql.append(tomcat.getOs());
        sql.append("','");
        sql.append(tomcat.getOsversion());
        sql.append("')");
        return saveOrUpdate(sql.toString());
    }

    /**
     * update:
     * <p>更新数据库
     *
     * @param   vo
     *          - {@link Tomcat} 实例
     * @return  {@link Boolean}
     *          - 如果成功则返回 <code>true</code> ，否者返回 <code>false</code>
     *
     * @since   v1.01
     * @see com.afunms.common.base.DaoInterface#update(com.afunms.common.base.BaseVo)
     */
    public boolean update(BaseVo vo) {
        Tomcat tomcat = (Tomcat) vo;
        StringBuffer sql = new StringBuffer();
        sql.append("update app_tomcat_node set name='");
        sql.append(tomcat.getName());
        sql.append("',ip_address='");
        sql.append(tomcat.getIpAddress());
        sql.append("',ip_long=");
        sql.append(NetworkUtil.ip2long(tomcat.getIpAddress()));
        sql.append(",port='");
        sql.append(tomcat.getPort());
        sql.append("',user='");
        sql.append(tomcat.getUser());
        sql.append("',password='");
        sql.append(tomcat.getPassword());
        sql.append("',bid='");
        sql.append(tomcat.getBid());
        sql.append("',monflag=");
        sql.append(tomcat.getMonflag());
        sql.append(",version='");
        sql.append(tomcat.getVersion());
        sql.append("',jvmversion='");
        sql.append(tomcat.getJvmversion());
        sql.append("',jvmvender='");
        sql.append(tomcat.getJvmvender());
        sql.append("',os='");
        sql.append(tomcat.getOs());
        sql.append("',osversion='");
        sql.append(tomcat.getVersion());
        sql.append("',supperid='");
        sql.append(tomcat.getSupperid());
        sql.append("' where id=");
        sql.append(tomcat.getId());
        return saveOrUpdate(sql.toString());
    }

    public Tomcat findByIp(String ip) {
        String sql = " where ip_address='" + ip + "'";
        List<Tomcat> list = findByCondition(sql);
        Tomcat tomcat = null;
        if (list != null && list.size() > 0) {
            tomcat = list.get(0);
        }
        return tomcat;
    }

    public List getTomcatByBID(Vector bids){
        String businessId = ",";
        for (Object object : bids) {
            String bid = (String) object;
            businessId += bid + ",";
        }
        return loadByPerAll(businessId, "bid");
    }

    public int getidByIp(String ip) {
        String string = "select id from app_tomcat_node where ip_address ="+"'"+ip+"'";
        int id = 0;
        ResultSet rSet = null;
        rSet = conn.executeQuery(string);
         try {
            while(rSet.next())
             {
                 id = rSet.getInt(1);
             }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            conn.close();
        }
        return id;
    }
    /**
     * createHostData:
     * <p>该方法 不在使用
     *
     * @param pingdata
     * @return
     *
     * @since   v1.01
     */
    @Deprecated
    public synchronized boolean createHostData(Pingcollectdata pingdata) {
        return false;
    }
}