package com.database;

import java.sql.*;

import javax.sql.DataSource;

import com.afunms.common.util.SysLogger;
import com.afunms.initialize.ResourceCenter;
import com.mchange.v2.c3p0.*;

/**
 * 
 * 
 * 数据库建立管理实现类
 * 
 * @author itims
 * 
 */

public class DBConnectionManager {

    /**
     * 日志
     */
    private static final SysLogger logger = SysLogger.getLogger(DBConnectionManager.class.getName());

    private static DataSource dataSource;
    
    private DBConnectionManager() {
       
    }

    static {
        String connectionPoolConfigFile = ResourceCenter.getInstance().getConnectionPoolConfigFile();
        try {
            dataSource = new ComboPooledDataSource("afunms");
        } catch (Exception e) {
            logger.error("初始化数据库连接池出错", e);
        }
    }

    public static Connection getConnection () {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            logger.error("获取数据库连接出错", e);
        }
        return connection;
    }
    
}