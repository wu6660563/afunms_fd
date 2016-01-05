/*
 * @(#)DataBaseInitializeListener.java     v1.01, 2014 1 8
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.initialize.listener;

import java.sql.Connection;
import java.sql.SQLException;

import com.database.DBConnectionManager;

/**
 * ClassName:   DataBaseInitializeListener.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 8 16:37:38
 */
public class DataBaseInitializeListener extends AbstractInitializeListener {

    /**
     * init:
     *
     * @param   configFile
     *          - 配置文件
     * @return  {@link Boolean}
     *          - 启动是否成功
     *
     * @since   v1.01
     * @see com.afunms.initialize.SysInitializeListener#init(java.lang.String)
     */
    public boolean init(String configFile) {
        try {
            getResourceCenter().setConnectionPoolConfigFile(configFile);
            Connection connection = DBConnectionManager.getConnection();
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

}

