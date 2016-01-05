package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.afunms.common.util.CommonUtil;

public class CleanPortStatusTable {

    public static void main(String[] args) {
        String ip = "10.199.92.124";
        String username = "root";
        String password = "root";
        String db = "afunms";
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
                sql = "truncate protstatus" + CommonUtil.doip(rs.getString("ip_address")) + " if exists protstatus" + CommonUtil.doip(rs.getString("ip_address"));
                System.out.println(sql);
                statement.executeUpdate(sql);
                sql = "optimize TABLE if exists protstatus" + CommonUtil.doip(rs.getString("ip_address"));
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
