package com.gatherResulttosql;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.node.Host;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

public class HostDatatempiflistRtosql implements ResultTosql {

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

        List ifList = (ArrayList) dataresult.get("iflist");

        if (ifList != null && ifList.size() > 0) {

            String ip = node.getIpAddress();
            NodeUtil nodeUtil = new NodeUtil();
            NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);

            String deleteSql = "delete from nms_other_data_temp where nodeid='"
                    + node.getId() + "' and entity = 'iflist'";
            execute(deleteSql);

            Calendar tempCal = Calendar.getInstance();
            Date cc = tempCal.getTime();
            String time = sdf.format(cc);

            String sql = "insert into nms_other_data_temp(nodeid,ip,type,subtype,entity,subentity,sindex,thevalue,collecttime)"
                    + "values(?,?,?,?,?,?,?,?,?)";

            DBManager manager = new DBManager();
            try {
                PreparedStatement preparedStatement = manager.prepareStatement(sql);
                for (int i = 0; i < ifList.size(); i++) {
                    Hashtable ifItemHash = (Hashtable) ifList.get(i);
                    Enumeration tempEnumeration2 = ifItemHash.keys();
                    while (tempEnumeration2.hasMoreElements()) {
                        String key = (String) tempEnumeration2.nextElement();
                        String value = (String) ifItemHash.get(key);
                        try {
                            preparedStatement.setString(1, String.valueOf(nodeDTO.getId()));
                            preparedStatement.setString(2, ip);
                            preparedStatement.setString(3, nodeDTO.getType());// type
                            preparedStatement.setString(4, nodeDTO.getSubtype());// subtype
                            preparedStatement.setString(5, "iflist");// entity
                            preparedStatement.setString(6, key);// subentity
                            preparedStatement.setString(7, String.valueOf(i + 1));// sindex
                            preparedStatement.setString(8, value);// thevalue
                            preparedStatement.setString(9, time);// collecttime
                            preparedStatement.addBatch();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
