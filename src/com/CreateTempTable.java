package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.afunms.common.util.CommonUtil;

public class CreateTempTable {

    public static void main(String[] args) {
        CreateTempTable.createTempTable();
    }

    public static void createTempTable() {
        String ip = "127.0.0.1";
        String username = "root";
        String password = "root";
        String db = "newafunms";
        String dburl = "jdbc:mysql://" + ip + "/" + db + "?user=" + username + "&password=" + password
                + "&useUnicode=true&&characterEncoding=gbk&autoReconnect = true";
        String sql = "select * from topo_host_node";
        ResultSet rs = null;
        String strDriver = "com.mysql.jdbc.Driver";
        
        Connection conn = null;
        Connection conn2 = null;
        try {
            Class.forName(strDriver).newInstance();
            conn = DriverManager.getConnection(dburl, username, password);
            conn2 = DriverManager.getConnection(dburl, username, password);
            Statement statement = conn2.createStatement();
            rs = conn.createStatement().executeQuery(sql);
            int i = 0;
            while (rs.next()) {
                sql = "CREATE TABLE if not exists nms_interface_data_temp" + CommonUtil.doip(rs.getString("ip_address"))
                        + "(`nodeid` varchar(10) DEFAULT NULL,`ip` varchar(20) DEFAULT NULL,`type` varchar(30) DEFAULT NULL,`subtype` varchar(30) DEFAULT NULL,`entity` varchar(30) DEFAULT NULL,`subentity` varchar(30) DEFAULT NULL,`sindex` varchar(30) DEFAULT NULL,`thevalue` varchar(300) DEFAULT NULL,`chname` varchar(50) DEFAULT NULL,`restype` varchar(20) DEFAULT NULL,`collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,`unit` varchar(10) DEFAULT NULL,`bak` varchar(20) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                System.out.println(sql);
                statement.executeUpdate(sql);
                sql = "CREATE TABLE if not exists nms_process_data_temp" + CommonUtil.doip(rs.getString("ip_address"))
                    + "(`nodeid` varchar(10) DEFAULT NULL,`ip` varchar(20) DEFAULT NULL,`type` varchar(30) DEFAULT NULL,`subtype` varchar(30) DEFAULT NULL,`entity` varchar(30) DEFAULT NULL,`subentity` varchar(30) DEFAULT NULL,`sindex` varchar(30) DEFAULT NULL,`thevalue` varchar(300) DEFAULT NULL,`chname` varchar(50) DEFAULT NULL,`restype` varchar(20) DEFAULT NULL,`collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,`unit` varchar(10) DEFAULT NULL,`bak` varchar(20) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                System.out.println(sql);
                statement.executeUpdate(sql);
                i ++;
            }
            System.out.println("node count is:" + i);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (conn2 != null) {
                    conn2.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
