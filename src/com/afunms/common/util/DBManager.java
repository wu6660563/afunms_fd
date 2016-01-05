/**
 * <p>Description:DBManager,used to connect database</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-06
 */

package com.afunms.common.util;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


import com.afunms.initialize.ResourceCenter;

public class DBManager {
    protected Connection conn;
    protected Statement stmt;
    protected PreparedStatement pstmt;
    protected ResultSet rs;
    protected String preparesql;
    protected com.database.DBManager manager;
    /**
     * 我们默认系统数据库是可以正常连接的，所以不抛出错误
     */

    public Connection getConn() {
        return conn;
    }

    public DBManager() {
        try {
            init(ResourceCenter.getInstance().getJndi());
        } catch (Exception e) {
            e.printStackTrace();
            SysLogger.error("Can not connect system DB!", e);
        }
    }

    public DBManager(String jndi) throws Exception {
        init(jndi);
    }

    /**
     * 初始化
     */
    public void init(String jndi) {
        try {
            manager = new com.database.DBManager();
            conn = manager.getConn();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 查询
     */
    public ResultSet executeQuery(String sql) {
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException se) {
            SysLogger.error("Error in DBManager.executeQuery(),SQL=\n" + sql);
        }
        return rs;
    }

    /**
     * 更新,并提交
     */
    public void executeUpdate(String sql) {
        // SysLogger.info("executeUpdate(sql)==="+sql);
        executeUpdate(sql, true);
    }

    /**
     * 更新
     */
    public void executeUpdate(String sql, boolean bCommit) {

        // SysLogger.info("executeUpdate(sql,boolean)==="+sql);
        try {
            // if(stmt == null){
            // try
            // {
            // init(ResourceCenter.getInstance().getJndi());
            // }
            // catch(Exception e)
            // {
            // e.printStackTrace();
            // SysLogger.error("Can not connect system DB!",e);
            // }
            // }
            try {
                stmt.executeUpdate(sql);
            } catch (Exception e) {
                e.printStackTrace();
                // SysLogger.error("Can not connect system DB!",e);
            }
            if (bCommit)
                conn.commit();
        } catch (SQLException se) {
            se.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException sqle) {
            }
            SysLogger.error("DBManager.executeUpdate():更新数据出错:\n" + sql, se);
        }
    }

    /**
     * 提交
     */
    public void commit() {
        try {
            conn.commit();
        } catch (SQLException se) {
            SysLogger.error("DBManager.commit():", se);
        }
    }

    /**
     * 回滚
     */
    public void rollback() {
        try {
            conn.rollback();
        } catch (SQLException se) {
            SysLogger.error("DBManager.rollback():", se);
        }
    }

    /**
     * 加入批处理
     */
    public void addBatch(String sql) {
        try {
            stmt.addBatch(sql);
        } catch (SQLException se) {
            SysLogger.error("Error in DBManager.addBatch()!" + sql, se);
        }
    }

    /**
     * 执行批处理
     */
    public boolean executeBatch() {
        boolean result = false;
        try {
            stmt.executeBatch();
            conn.commit();
            result = true;
        } catch (BatchUpdateException bse) {
            rollback();
            bse.printStackTrace();
            SysLogger.error("-------至少有一条SQL语句错误-------", bse);
        } catch (SQLException se) {
            rollback();
            se.printStackTrace();
            SysLogger.error("Error in DBManager.executeBatch()!", se);
        } catch (Exception se) {
            rollback();
            se.printStackTrace();
            SysLogger.error("Error in DBManager.executeBatch()!", se);
        } finally {
            try {
                stmt.clearBatch();
            } catch (SQLException xe) {
            }
        }
        return result;
    }

    /**
     * 设置批处理SQL
     */
    public void setPrepareSql(String sql) {
        preparesql = sql;
        try {
            pstmt = conn.prepareStatement(preparesql);
        } catch (SQLException xe) {
            xe.printStackTrace();
        }
    }

    /**
     * 加入批处理
     */
    public void addPrepareBatch(List list) {
        try {
            pstmt.setString(1, (String) list.get(0));
            pstmt.setString(2, (String) list.get(1));
            pstmt.setString(3, (String) list.get(2));
            pstmt.setString(4, (String) list.get(3));
            pstmt.setString(5, (String) list.get(4));
            pstmt.setString(6, (String) list.get(5));
            pstmt.setString(7, (String) list.get(6));
            pstmt.setString(8, (String) list.get(7));
            pstmt.setString(9, (String) list.get(8));
            pstmt.setString(10, (String) list.get(9));
            pstmt.setString(11, (String) list.get(10));
            pstmt.setString(12, (String) list.get(11));
            pstmt.setString(13, (String) list.get(12));
            pstmt.addBatch();
            // SysLogger.info((String)list.get(4)+"====="+(String)list.get(5)+"==="+(String)list.get(6));
        } catch (SQLException se) {
            SysLogger.error("Error in DBManager.addBatch()!", se);
        }
    }

    /**
     * 加入批处理
     */
    public void addPrepareProcBatch(List list) {
        try {
            pstmt.setString(1, (String) list.get(0));
            pstmt.setString(2, (String) list.get(1));
            pstmt.setString(3, (String) list.get(2));
            pstmt.setString(4, (String) list.get(3));
            pstmt.setString(5, (String) list.get(4));
            pstmt.setString(6, (String) list.get(5));
            pstmt.setString(7, (String) list.get(6));
            pstmt.setString(8, (String) list.get(7));
            pstmt.setString(9, (String) list.get(8));
            pstmt.setString(10, (String) list.get(9));
            pstmt.setString(11, (String) list.get(10));
            pstmt.addBatch();
        } catch (SQLException se) {
            SysLogger.error("Error in DBManager.addBatch()!", se);
        }
    }

    /**
     * 加入批处理
     */
    public void addPrepareProcLongBatch(List list) {
        try {
            pstmt.setString(1, (String) list.get(0));
            pstmt.setString(2, (String) list.get(1));
            pstmt.setString(3, (String) list.get(2));
            pstmt.setString(4, (String) list.get(3));
            pstmt.setString(5, (String) list.get(4));
            pstmt.setString(6, (String) list.get(5));
            pstmt.setString(7, (String) list.get(6));
            pstmt.setString(8, (String) list.get(7));
            pstmt.setLong(9, (Long) list.get(8));
            pstmt.setString(10, (String) list.get(9));
            pstmt.setString(11, (String) list.get(10));
            pstmt.addBatch();
        } catch (SQLException se) {
            SysLogger.error("Error in DBManager.addBatch()!", se);
        }
    }

    /**
     * 加入批处理
     */
    public void addPrepareSoftwareBatch(List list) {
        try {
            pstmt.setString(1, (String) list.get(0));
            pstmt.setString(2, (String) list.get(1));
            pstmt.setString(3, (String) list.get(2));
            pstmt.setString(4, (String) list.get(3));
            pstmt.setString(5, (String) list.get(4));
            pstmt.setString(6, (String) list.get(5));
            pstmt.setString(7, (String) list.get(6));
            pstmt.setString(8, (String) list.get(7));
            pstmt.setString(9, (String) list.get(8));
            pstmt.addBatch();
        } catch (SQLException se) {
            SysLogger.error("Error in DBManager.addBatch()!", se);
        }
    }

    /**
     * 加入批处理
     */
    public void addPrepareServiceBatch(List list) {
        try {
            pstmt.setString(1, (String) list.get(0));
            pstmt.setString(2, (String) list.get(1));
            pstmt.setString(3, (String) list.get(2));
            pstmt.setString(4, (String) list.get(3));
            pstmt.setString(5, (String) list.get(4));
            pstmt.setString(6, (String) list.get(5));
            pstmt.setString(7, (String) list.get(6));
            pstmt.setString(8, (String) list.get(7));
            pstmt.setString(9, (String) list.get(8));
            pstmt.setString(10, (String) list.get(9));
            pstmt.addBatch();
        } catch (SQLException se) {
            SysLogger.error("Error in DBManager.addBatch()!", se);
        }
    }

    /**
     * 加入批处理
     */
    public void addPrepareErrptBatch(List list) {
        try {
            pstmt.setString(1, (String) list.get(0));
            pstmt.setString(2, (String) list.get(1));
            pstmt.setString(3, (String) list.get(2));
            pstmt.setInt(4, (Integer) list.get(3));
            pstmt.setString(5, (String) list.get(4));
            pstmt.setString(6, (String) list.get(5));
            pstmt.setString(7, (String) list.get(6));
            pstmt.setString(8, (String) list.get(7));
            pstmt.setString(9, (String) list.get(8));
            pstmt.setString(10, (String) list.get(9));
            pstmt.setString(11, (String) list.get(10));
            pstmt.setString(12, (String) list.get(11));
            pstmt.setString(13, (String) list.get(12));
            pstmt.setString(14, (String) list.get(13));
            pstmt.setString(15, (String) list.get(14));
            pstmt.addBatch();
        } catch (SQLException se) {
            SysLogger.error("Error in DBManager.addBatch()!", se);
        }
    }

    /**
     * 执行批处理
     */
    public void executePreparedBatch() {
        try {
            pstmt.executeBatch();
            conn.commit();
        } catch (BatchUpdateException bse) {
            bse.printStackTrace();
            SysLogger.error("-------至少有一条SQL语句错误-------", bse);
        } catch (SQLException se) {
            se.printStackTrace();
            SysLogger.error("Error in DBManager.executeBatch()!", se);
        } catch (Exception se) {
            se.printStackTrace();
            SysLogger.error("Error in DBManager.executeBatch()!", se);
        } finally {
            try {
                pstmt.clearBatch();
            } catch (SQLException xe) {
            }
        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null && !conn.isClosed()) {
                manager.close();
            }
        } catch (SQLException se) {
            SysLogger.error("Error in DBManager.close()!", se);
        }
    }
}
