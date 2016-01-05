package com.gatherResulttosql;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Host;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

public class HostDatatempDiskPeriofRtosql implements ResultTosql {

    private static SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * 把结果生成sql
     * 
     * @param dataresult
     *            采集结果
     * @param node
     *            网元节点
     */
    public void CreateResultTosql(Hashtable dataresult, Host node) {
        DBAttribute attribute = new DBAttribute();
        attribute.setAttribute("dataresult", dataresult);
        attribute.setAttribute("node", node);
        ResultToDB resultToDB = new ResultToDB();
        resultToDB.setResultTosql(this);
        resultToDB.setAttribute(attribute);
        GathersqlListManager.getInstance().addToQueue(resultToDB);
    }

    public void executeResultToDB(DBAttribute attribute) {
        Hashtable dataresult = (Hashtable) attribute.getAttribute("dataresult");
        Host node = (Host) attribute.getAttribute("node");
        String ip = (String) node.getIpAddress();
        NodeUtil nodeUtil = new NodeUtil();
        NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);

        List diskperfList = (ArrayList) dataresult.get("alldiskio");
        if (diskperfList != null && diskperfList.size() > 0) {
            String deleteSql = "delete from nms_diskio_data_temp where nodeid='"
                    + node.getId() + "'";
            execute(deleteSql);

            Calendar tempCal = Calendar.getInstance();
            Date cc = tempCal.getTime();
            String time = sdf.format(cc);
            DBManager manager = new DBManager();
            try {
                String sql = "insert into nms_diskio_data_temp(nodeid,ip,type,subtype,entity,subentity,sindex,thevalue,chname,restype,collecttime,unit,bak)values"
                    + "(?,?,?,?,?,?,?,?,?,?,?,?,?);";
                PreparedStatement preparedStatement = manager.prepareStatement(sql);
                for (int i = 0; i < diskperfList.size(); i++) {
                    Hashtable diskprefHash = (Hashtable) diskperfList.get(i);
                    try {
                        Enumeration en = diskprefHash.keys();
                        while (en.hasMoreElements()) {
                            String key = (String) en.nextElement();
                            String value = String.valueOf(diskprefHash.get(key));
                            preparedStatement.setString(1, String.valueOf(nodeDTO.getId()));
                            preparedStatement.setString(2, ip);
                            preparedStatement.setString(3, nodeDTO.getType());
                            preparedStatement.setString(4, nodeDTO.getSubtype());
                            preparedStatement.setString(5, "alldiskperf");// entity
                            preparedStatement.setString(6, key);// subentity
                            preparedStatement.setString(7, String.valueOf(i + 1));// sindex
                            preparedStatement.setString(8, value);// thevalue
                            preparedStatement.setString(9, key);// chname
                            preparedStatement.setString(10, "dynamic");// restype
                            preparedStatement.setString(11, time);// collecttime
                            preparedStatement.setString(12,"");// unit
                            preparedStatement.setString(13,"");// unit
                            preparedStatement.addBatch();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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

    public void execute(String sql) {
        DBManager manager = new DBManager();// 数据库管理对象
        try {
            manager.executeUpdate(sql);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            manager.close();
        }
    }
}
