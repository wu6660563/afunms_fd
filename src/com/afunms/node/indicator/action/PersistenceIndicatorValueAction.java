/*
 * @(#)PersistenceIndicatorValueAction.java     v1.01, 2014 1 1
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.afunms.common.util.SysUtil;
import com.database.DBManager;


/**
 * ClassName:   PersistenceIndicatorValueAction.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 1 17:47:41
 */
public abstract class PersistenceIndicatorValueAction extends BaseIndicatorValueAction {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * execute:
     *
     * @param indicatorValue
     *
     * @since   v1.01
     * @see com.afunms.node.indicator.action.BaseIndicatorValueAction#execute()
     */
    @Override
    public void execute() {
        executeToDB();
    }

    /**
     * executeToDB:
     * <p>执行入库操作
     *
     *
     * @since   v1.01
     */
    public abstract void executeToDB();

    /**
     * executeSQL:
     * <p>执行 SQL 语句
     *
     * @param   sql
     *          - SQL 语句
     *
     * @since   v1.01
     */
    public void executeSQL(String sql) {
        DBManager manager = new DBManager();// 数据库管理对象
        try {
            manager.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            manager.close();
        }
    }

    /**
     * executePreparedSQL:
     * <p>执行 SQL 语句
     *
     * @param   sql
     *          - sql 语句
     * @param list
     *
     * @since   v1.01
     *
     * @since   v1.01
     */
    public void executePreparedSQL(String sql, List<String[]> list) {
        DBManager manager = new DBManager();// 数据库管理对象
        try {
            PreparedStatement preparedStatement = manager.prepareStatement(sql);
            for (String[] array : list) {
                for (int i = 0; i < array.length; i++) {
                    preparedStatement.setString(i + 1, array[i]);
                }
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            manager.close();
        }
    }

    public void truncateDataTemp(String dataTempTableName) {
        String sql = "truncate " + dataTempTableName;
        executeSQL(sql);
    }

    public void deleteDataTemp(String dataTempTableName) {
        deleteDataTemp(dataTempTableName, null);
    }

    public void deleteDataTemp(String dataTempTableName, String sindex) {
        String sql = "delete from " + dataTempTableName + " where nodeid='" + getNodeId() + "'";
        if (sindex != null && sindex.trim().length() > 0) {
            sql += " and sindex='" + sindex + "'";
        }
        executeSQL(sql);
    }

    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }
    /**
     * format:
     * <p>格式化时间
     *
     * @param   date
     *          - 时间
     * @return  {@link String}
     *          - 字符串类型时间
     *
     * @since   v1.01
     */
    public static String format(Calendar date) {
        return sdf.format(date.getTime());
    }

    public static String getIPTableName(String ipAddress) {
        return SysUtil.doip(ipAddress);
    }
}

