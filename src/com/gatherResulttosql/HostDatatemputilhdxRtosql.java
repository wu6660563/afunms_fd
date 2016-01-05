package com.gatherResulttosql;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.common.util.CommonUtil;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.UtilHdx;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

import flex.messaging.io.ArrayList;

public class HostDatatemputilhdxRtosql implements ResultTosql {

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
        attribute.setAttribute("method", "CreateResultTosql");
        ResultToDB resultToDB = new ResultToDB();
        resultToDB.setResultTosql(this);
        resultToDB.setAttribute(attribute);
        GathersqlListManager.getInstance().addToQueue(resultToDB);
    }

    public void executeResultToDB(DBAttribute attribute) {
        Hashtable dataresult = (Hashtable) attribute.getAttribute("dataresult");
        Host node = (Host) attribute.getAttribute("node");

        Vector utilhdxVector = (Vector) dataresult.get("utilhdx");
        if (utilhdxVector != null && utilhdxVector.size() > 0) {

            String ip = (String) node.getIpAddress();
            NodeUtil nodeUtil = new NodeUtil();
            NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);

            String tableName = "nms_interface_data_temp" + CommonUtil.doip(nodeDTO.getIpaddress());
            String deleteSql = "delete from " + tableName + " where nodeid='"
                    + node.getId()
                    + "' and subentity in ('InBandwidthUtilHdx','OutBandwidthUtilHdx')";
            execute(deleteSql);

            Calendar tempCal = Calendar.getInstance();
            Date cc = tempCal.getTime();
            String time = sdf.format(cc);

            String sql = "insert into " + tableName + "(nodeid,ip,type,subtype,entity,subentity,sindex,thevalue,chname,restype,collecttime,unit,bak)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
            DBManager manager = new DBManager();
            try {
                PreparedStatement preparedStatement = manager
                        .prepareStatement(sql);
                for (int i = 0; i < utilhdxVector.size(); i++) {
                    UtilHdx vo = (UtilHdx) utilhdxVector.elementAt(i);
                    try {
                        preparedStatement.setString(1, String.valueOf(nodeDTO
                                .getId()));
                        preparedStatement.setString(2, ip);
                        preparedStatement.setString(3, nodeDTO.getType());// type
                        preparedStatement.setString(4, nodeDTO.getSubtype());// subtype
                        preparedStatement.setString(5, vo.getCategory());// entity
                        preparedStatement.setString(6, vo.getEntity());// subentity
                        preparedStatement.setString(7, vo.getSubentity());// sindex
                        preparedStatement.setString(8, vo.getThevalue());// thevalue
                        preparedStatement.setString(9, vo.getChname());// chname
                        preparedStatement.setString(10, vo.getRestype());// restype
                        preparedStatement.setString(11, time);// collecttime
                        preparedStatement.setString(12, vo.getUnit());// unit
                        preparedStatement.setString(13, vo.getBak());// bak
                        preparedStatement.addBatch();
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
