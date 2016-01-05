package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.afunms.common.util.CommonUtil;
import com.afunms.indicators.util.Constant;
import com.afunms.indicators.util.NodeUtil;

public class UpdateNodeType {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String ip = "10.199.122.124";
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
                int category = rs.getInt("category");
                String sysoid = rs.getString("sys_oid");
                String type = rs.getString("type");
                String subtype = "";
                if(category == 4){
                    if(sysoid != null && sysoid.trim().length()>0){
                        if(sysoid.startsWith("1.3.6.1.4.1.311."))subtype = Constant.TYPE_HOST_SUBTYPE_WINDOWS;
                        if(sysoid.startsWith("1.3.6.1.4.1.2021."))subtype = Constant.TYPE_HOST_SUBTYPE_LINUX;
                        if(sysoid.startsWith("1.3.6.1.4.1.2."))subtype = Constant.TYPE_HOST_SUBTYPE_AIX;
                        if(sysoid.startsWith("1.3.6.1.4.1.11."))subtype = Constant.TYPE_HOST_SUBTYPE_HPUNIX;
                        if(sysoid.startsWith("1.3.6.1.4.1.42."))subtype = Constant.TYPE_HOST_SUBTYPE_SOLARIS;
                        if(sysoid.startsWith("as400"))subtype = Constant.TYPE_HOST_SUBTYPE_AS400;
                    }
                } else{
                    //网络设备,需要判断操作系统
                    if(sysoid != null && sysoid.trim().length()>0){
                        if(sysoid.startsWith("1.3.6.1.4.1.9."))subtype = Constant.TYPE_NET_SUBTYPE_CISCO;
                        if(sysoid.startsWith("1.3.6.1.4.1.2011."))subtype = Constant.TYPE_NET_SUBTYPE_H3C;
                        if(sysoid.startsWith("1.3.6.1.4.1.25506."))subtype = Constant.TYPE_NET_SUBTYPE_H3C;
                        if(sysoid.startsWith("1.3.6.1.4.1.9.2.1.57."))subtype = Constant.TYPE_NET_SUBTYPE_ENTRASYS;
                        if(sysoid.startsWith("1.3.6.1.4.1.89."))subtype = Constant.TYPE_NET_SUBTYPE_RADWARE;
                        if(sysoid.startsWith("1.3.6.1.4.1.5651."))subtype = Constant.TYPE_NET_SUBTYPE_MAIPU;
                        if(sysoid.startsWith("1.3.6.1.4.1.4881."))subtype = Constant.TYPE_NET_SUBTYPE_REDGIANT;
                        if(sysoid.startsWith("1.3.6.1.4.1.45."))subtype = Constant.TYPE_NET_SUBTYPE_NORTHTEL;
                        if(sysoid.startsWith("1.3.6.1.4.1.171."))subtype = Constant.TYPE_NET_SUBTYPE_DLINK;
                        if(sysoid.startsWith("1.3.6.1.4.1.3320."))subtype = Constant.TYPE_NET_SUBTYPE_BDCOM;
                        if(sysoid.startsWith("1.3.6.1.4.1.3902."))subtype = Constant.TYPE_NET_SUBTYPE_ZTE;
                    }
                }
                System.out.println("update topo_host_node set type='" + subtype + "' where id=" + id +"");
                statement.addBatch("update topo_host_node set type='" + subtype + "' where id=" + id +"");
                System.out.println("node count is:" + i + "node  subtype = " + subtype + " node type=" + type);
            }
            statement.executeBatch();
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
