
package com.afunms.util;

import com.database.DBManager;

import java.sql.Connection;

public class DataGate {

    /**
     * @roseuid 3AD1A51103A6
     */
    public static Connection getCon() throws Exception {
        DBManager manager = new DBManager();
 		return manager.getConn();
    }

    /**
     * @roseuid 3AD1A5180270
     */
    public static void freeCon(Connection con) throws Exception {
        //DBConnectPoolManager.getInstance().freeConnection(con);
    }    
}
