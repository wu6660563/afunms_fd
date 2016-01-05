package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.afunms.common.util.CommonUtil;
import com.afunms.indicators.util.Constant;
import com.afunms.indicators.util.NodeUtil;

public class UpdateNodeName {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String ip = "10.199.62.124";
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
                i++;
                int id = rs.getInt("id");
                String alias = rs.getString("alias");
                if (alias.contains("\"")) {
                    System.out.println("id==" + id + "====alias===" + alias);
                    System.out.println("id==" + id + "====alias2===" + alias.replaceAll("\"", ""));
                    System.out.println("update topo_host_node set alias='" + alias.replaceAll("\"", "") + "' where id=" + id +"");
                    statement.executeUpdate("update topo_host_node set alias='" + alias.replaceAll("\"", "") + "' where id=" + id +"");
                    //System.out.println("node count is:" + i + "node  subtype = " + subtype + " node type=" + type);
                }
            }
//            statement.executeBatch();
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
