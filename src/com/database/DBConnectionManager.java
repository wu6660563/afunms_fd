package com.database;

import java.sql.*;

import javax.sql.DataSource;

import com.afunms.common.util.SysLogger;
import com.afunms.initialize.ResourceCenter;
import com.mchange.v2.c3p0.*;

/**
 * 
 * 
 * ���ݿ⽨������ʵ����
 * 
 * @author itims
 * 
 */

public class DBConnectionManager {

    /**
     * ��־
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
            logger.error("��ʼ�����ݿ����ӳس���", e);
        }
    }

    public static Connection getConnection () {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            logger.error("��ȡ���ݿ����ӳ���", e);
        }
        return connection;
    }
    
}