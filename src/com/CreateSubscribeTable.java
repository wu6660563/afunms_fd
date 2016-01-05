package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.afunms.common.util.CommonUtil;

public class CreateSubscribeTable {

    public static void main(String[] args) {
        String ip = "10.199.132.124";
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
            String subscribeSQLMode = "insert into `sys_subscribe_resources` (`SUBSCRIBE_ID`, `BIDTEXT`, `BID`, `USERNAME`, `EMAIL`, `EMAILTITLE`, `EMAILCONTENT`, `ATTACHMENTFORMAT`, `REPORT_TYPE`, `REPORT_SENDDATE`, `REPORT_SENDFREQUENCY`, `REPORT_TIME_MONTH`, `REPORT_TIME_WEEK`, `REPORT_TIME_DAY`, `REPORT_TIME_HOU`, `REPORT_DAY_STOP`, `REPORT_WEEK_STOP`, `REPORT_MONTH_STOP`, `REPORT_SEASON_STOP`, `REPORT_YEAR_STOP`) values(";
            String subscribeSQL = "";
            rs = conn.createStatement().executeQuery("SELECT * FROM system_business WHERE pid='1'");
            String businessId = "',";
            String businessName = "',";
            while (rs.next()) {
                businessId += rs.getInt("id") + ",";
                businessName +=  rs.getString("name") + ",";
            }
            businessName += "'";
            businessId += "'";
            subscribeSQL = subscribeSQL + businessName + "," + businessId + ",";
            rs = conn.createStatement().executeQuery("SELECT * FROM SYSTEM_USER where position_id='2'");
            String userName = "',";
            String useremail = "',";
            while (rs.next()) {
                userName +=  rs.getString("name") + ",";
                useremail += rs.getString("email") + ",";
            }
            userName += "'";
            useremail += "'";
            subscribeSQL = subscribeSQL + userName + "," + useremail + ",";
            String subscribeSQL2 = subscribeSQL + "";
            
            subscribeSQL =  subscribeSQLMode + "'1'," + subscribeSQL + "'呼市网络设备周报','呼市网络设备周报','pdf','week','2012050920','2','','/1/','','/04/','null','null','null','null','null');";
            
            subscribeSQL2 = subscribeSQLMode + "'2'," + subscribeSQL2 + "'呼市服务器周报','呼市服务器周报','pdf','week','2012050920','2','','/1/','','/04/','null','null','null','null','null');";
            
            System.out.println(subscribeSQL);
            System.out.println(subscribeSQL2);
            
            
            

            rs = conn.createStatement().executeQuery(sql);
            String allIP1 = "";
            String allIP2 = "";
            while (rs.next()) {
                String ip_address = rs.getString("ip_address");
                int category = rs.getInt("category");
                if (category == 4) {
                    allIP1 = allIP1 + "ping" + ip_address + "," + "cpu" + ip_address + "," + "mem" + ip_address + "," + "port" + ip_address + ",";
                } else {
                    allIP2 = allIP2 + "ping" + ip_address + "," + "cpu" + ip_address + "," + "mem" + ip_address + "," + "port" + ip_address + "," + "disk" + ip_address + ",";
                }
                
            }
            allIP1 += "'";
            allIP2 += "'";
            
            String userreportMode = "insert into `nms_userreport` (`id`, `name`, `type`, `ids`, `collecttime`, `begintime`, `endtime`) values(";
            
            String userreport1 = userreportMode + "'1','呼市网络设备周报','net','root,83," + allIP1 + ",'2012-05-09 20:28:47','2012-05-09 00:00:00','2012-05-09 23:59:59');";
            
            String userreport2 = userreportMode + "'2','呼市服务器周报','host','root,83," + allIP2 + ",'2012-05-09 20:28:47','2012-05-09 00:00:00','2012-05-09 23:59:59');";
            
            System.out.println(userreport1);
            System.out.println(userreport2);
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
