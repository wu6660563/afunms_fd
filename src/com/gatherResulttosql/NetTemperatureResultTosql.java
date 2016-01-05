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

public class NetTemperatureResultTosql implements ResultTosql {

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
        if (ipdata.containsKey("temperature")) {
            // 电源
            Vector temperatureVector = (Vector) ipdata.get("temperature");
            if (temperatureVector != null && temperatureVector.size() > 0) {
                String tablename = "temper" + allipstr;
                String sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?)";
                DBManager manager = new DBManager();
                try {
                    PreparedStatement preparedStatement = manager
                            .prepareStatement(sql);
                    for (int i = 0; i < temperatureVector.size(); i++) {
                        Interfacecollectdata temperdata = (Interfacecollectdata) temperatureVector.elementAt(i);
                        if (temperdata.getRestype().equals("dynamic")) {
                            Calendar tempCal = (Calendar) temperdata
                                    .getCollecttime();
                            Date cc = tempCal.getTime();
                            String time = sdf.format(cc);
                            
                            long count = 0;
                            if (temperdata.getCount() != null) {
                                count = temperdata.getCount();
                            }
                            
                            preparedStatement.setString(1, ip);
                            preparedStatement.setString(2, temperdata.getRestype());
                            preparedStatement.setString(3, temperdata.getCategory());
                            preparedStatement
                                    .setString(4, temperdata.getEntity());
                            preparedStatement.setString(5, temperdata.getSubentity());
                            preparedStatement.setString(6, temperdata.getUnit());
                            preparedStatement.setString(7, temperdata.getChname());
                            preparedStatement.setString(8, temperdata.getBak());
                            preparedStatement.setString(9, String.valueOf(count));
                            preparedStatement.setString(10, temperdata.getThevalue());
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

