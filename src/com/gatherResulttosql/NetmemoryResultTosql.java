package com.gatherResulttosql;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.SysUtil;
import com.afunms.polling.om.Memorycollectdata;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

public class NetmemoryResultTosql implements ResultTosql {

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
        if (ipdata.containsKey("memory")) {
            String allipstr = SysUtil.doip(ip);
            Vector memoryVector = (Vector) ipdata.get("memory");
            if (memoryVector != null && memoryVector.size() > 0) {
                String tablename = "memory" + allipstr;
                String sql = "insert into "
                        + tablename
                        + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime)"
                        + "values(?,?,?,?,?,?,?,?,?,?,?)";
                DBManager manager = new DBManager();
                try {
                    PreparedStatement preparedStatement = manager
                            .prepareStatement(sql);
                    for (int si = 0; si < memoryVector.size(); si++) {
                        Memorycollectdata memorydata = (Memorycollectdata) memoryVector
                                .elementAt(si);
                        if (memorydata.getRestype().equals("dynamic")) {
                            Calendar tempCal = (Calendar) memorydata
                                    .getCollecttime();
                            Date cc = tempCal.getTime();
                            String time = sdf.format(cc);

                            long count = 0;
                            if (memorydata.getCount() != null) {
                                count = memorydata.getCount();
                            }

                            preparedStatement.setString(1, ip);
                            preparedStatement.setString(2, memorydata
                                    .getRestype());
                            preparedStatement.setString(3, memorydata
                                    .getCategory());
                            preparedStatement.setString(4, memorydata
                                    .getEntity());
                            preparedStatement.setString(5, memorydata
                                    .getSubentity());
                            preparedStatement
                                    .setString(6, memorydata.getUnit());
                            preparedStatement.setString(7, memorydata
                                    .getChname());
                            preparedStatement.setString(8, memorydata.getBak());
                            preparedStatement.setString(9, String
                                    .valueOf(count));
                            preparedStatement.setString(10, memorydata
                                    .getThevalue());
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
