package com.gatherResulttosql;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.SysUtil;
import com.afunms.polling.om.Interfacecollectdata;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

public class NetvoltageResultTosql implements ResultTosql {

    private static SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * 
     * 把采集数据生成sql放入的内存列表中
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
        String allipstr = SysUtil.doip(ip);
        if (ipdata.containsKey("voltage")) {
            // 电源
            Vector voltageVector = (Vector) ipdata.get("voltage");
            if (voltageVector != null && voltageVector.size() > 0) {
                String tablename = "vol" + allipstr;
                String sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?)";
                DBManager manager = new DBManager();
                try {
                    PreparedStatement preparedStatement = manager
                            .prepareStatement(sql);
                    for (int i = 0; i < voltageVector.size(); i++) {
                        Interfacecollectdata voltagedata = (Interfacecollectdata) voltageVector.elementAt(i);
                        if (voltagedata.getRestype().equals("dynamic")) {
                            Calendar tempCal = (Calendar) voltagedata
                                    .getCollecttime();
                            Date cc = tempCal.getTime();
                            String time = sdf.format(cc);
                            
                            long count = 0;
                            if (voltagedata.getCount() != null) {
                                count = voltagedata.getCount();
                            }
                            
                            preparedStatement.setString(1, ip);
                            preparedStatement.setString(2, voltagedata.getRestype());
                            preparedStatement.setString(3, voltagedata.getCategory());
                            preparedStatement
                                    .setString(4, voltagedata.getEntity());
                            preparedStatement.setString(5, voltagedata.getSubentity());
                            preparedStatement.setString(6, voltagedata.getUnit());
                            preparedStatement.setString(7, voltagedata.getChname());
                            preparedStatement.setString(8, voltagedata.getBak());
                            preparedStatement.setString(9, String.valueOf(count));
                            preparedStatement.setString(10, voltagedata.getThevalue());
                            preparedStatement.setString(11, time);
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


