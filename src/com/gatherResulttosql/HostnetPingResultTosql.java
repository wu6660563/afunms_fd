package com.gatherResulttosql;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.SysUtil;
import com.afunms.polling.om.Pingcollectdata;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

public class HostnetPingResultTosql implements ResultTosql {

    private static SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * 
     * 根据采集结果生成对应的sql放入到内存列表中
     */
    public void CreateResultTosql(Hashtable ipdata, String ip) {
        DBAttribute attribute = new DBAttribute();
        attribute.setAttribute("ipdata", ipdata);
        attribute.setAttribute("ip", ip);
        ResultToDB resultToDB = new ResultToDB();
        resultToDB.setResultTosql(this);
        resultToDB.setAttribute(attribute);
        GathersqlListManager.getInstance().addToQueue(resultToDB);
    }

    public void executeResultToDB(DBAttribute attribute) {
        Hashtable ipdata = (Hashtable) attribute.getAttribute("ipdata");
        String ip = (String) attribute.getAttribute("ip");
        if (ipdata.containsKey("ping")) {
            String allipstr = SysUtil.doip(ip);
            Vector pingVector = (Vector) ipdata.get("ping");
            if (pingVector != null && pingVector.size() > 0) {
                String tablename = "ping" + allipstr;
                String sql = "insert into "
                        + tablename
                        + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime)"
                        + "values(?,?,?,?,?,?,?,?,?,?,?)";
                DBManager manager = new DBManager();
                try {
                    PreparedStatement preparedStatement = manager
                            .prepareStatement(sql);
                    for (int i = 0; i < pingVector.size(); i++) {
                        Pingcollectdata pingdata = (Pingcollectdata) pingVector
                                .elementAt(i);
                        if (pingdata.getRestype().equals("dynamic")) {
                            Calendar tempCal = (Calendar) pingdata
                                    .getCollecttime();
                            Date cc = tempCal.getTime();
                            String time = sdf.format(cc);
                            Long count = pingdata.getCount();
                            if (count == null) {
                                count = 0L;
                            }

                            preparedStatement.setString(1, ip);
                            preparedStatement.setString(2, pingdata
                                    .getRestype());
                            preparedStatement.setString(3, pingdata
                                    .getCategory());// type
                            preparedStatement
                                    .setString(4, pingdata.getEntity());// subtype
                            preparedStatement.setString(5, pingdata
                                    .getSubentity());// entity
                            preparedStatement.setString(6, pingdata.getUnit());// subentity
                            preparedStatement
                                    .setString(7, pingdata.getChname());// sindex
                            preparedStatement.setString(8, pingdata.getBak());// thevalue
                            preparedStatement.setString(9, String
                                    .valueOf(count));// collecttime
                            preparedStatement.setString(10, pingdata
                                    .getThevalue());// collecttime
                            preparedStatement.setString(11, time);// collecttime
                            preparedStatement.addBatch();
                        }
                    }
                    preparedStatement.executeBatch();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    manager.close();
                }
            }
        }
    }

}
