package com.gatherResulttosql;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Usercollectdata;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

/**
 * 
 * linux 用户信息转换成sql
 * 
 * @author konglq
 * 
 */
public class HostDatatempUserRtosql implements ResultTosql {

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

        Vector userVector = (Vector) dataresult.get("user");

        if (userVector != null && userVector.size() > 0) {

            String ip = (String) node.getIpAddress();

            NodeUtil nodeUtil = new NodeUtil();
            NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);

            String deleteSql = "delete from nms_user_data_temp where nodeid='"
                    + node.getId() + "'";
            execute(deleteSql);

            Calendar tempCal = Calendar.getInstance();
            Date cc = tempCal.getTime();
            String time = sdf.format(cc);

            String sql = "insert into nms_user_data_temp(nodeid,ip,type,subtype,entity,subentity,sindex,thevalue,chname,restype,collecttime,unit,bak)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
            DBManager manager = new DBManager();
            try {
                PreparedStatement preparedStatement = manager
                        .prepareStatement(sql);
                for (int i = 0; i < userVector.size(); i++) {
                    Usercollectdata vo = (Usercollectdata) userVector
                            .elementAt(i);
                    try {
                        preparedStatement.setString(1, String.valueOf(nodeDTO
                                .getId()));
                        preparedStatement.setString(2, ip);
                        preparedStatement.setString(3, nodeDTO.getType());
                        preparedStatement.setString(4, nodeDTO.getSubtype());
                        preparedStatement.setString(5, vo.getCategory());// entity
                        preparedStatement.setString(6, vo.getEntity());// subentity
                        preparedStatement
                                .setString(7, vo.getSubentity().trim());// sindex
                        preparedStatement.setString(8, vo.getThevalue());// thevalue
                        preparedStatement.setString(9, vo.getChname());
                        preparedStatement.setString(10, vo.getRestype());
                        preparedStatement.setString(11, time);
                        preparedStatement.setString(12, vo.getUnit());
                        preparedStatement.setString(13, vo.getBak());
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
