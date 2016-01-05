package com.gatherResulttosql;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.CommonUtil;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Softwarecollectdata;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

public class HostDatatempsoftwareRttosql implements ResultTosql {

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
        if (dataresult != null && dataresult.size() > 0) {
            Vector softwareVector = (Vector) dataresult.get("software");
            if (softwareVector != null && softwareVector.size() > 0) {
                NodeUtil nodeUtil = new NodeUtil();
                NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);

                String deleteSql = "delete from nms_software_data_temp where nodeid='"
                        + node.getId() + "'";
                execute(deleteSql);

                Calendar tempCal = Calendar.getInstance();
                Date cc = tempCal.getTime();
                String time = sdf.format(cc);

                String sql = "insert into nms_software_data_temp(nodeid,ip,`type`,subtype,insdate,name,stype,swid,collecttime)"
                        + "values(?,?,?,?,?,?,?,?,?)";
                DBManager manager = new DBManager();
                try {
                    PreparedStatement preparedStatement = manager
                            .prepareStatement(sql);
                    for (int i = 0; i < softwareVector.size(); i++) {
                        Softwarecollectdata vo = (Softwarecollectdata) softwareVector
                                .elementAt(i);
                        String name = vo.getName();
                        if (name != null) {
                            name = name.replaceAll("'", "");
                        } else {
                            name = "";
                        }
                        name = CommonUtil.removeIllegalStr("GB2312", name);

                        preparedStatement.setString(1, String.valueOf(node
                                .getId()));
                        preparedStatement.setString(2, node.getIpAddress());
                        preparedStatement.setString(3, nodeDTO.getType());
                        preparedStatement.setString(4, nodeDTO.getSubtype());
                        preparedStatement.setString(5, vo.getInsdate());
                        preparedStatement.setString(6, name);
                        preparedStatement.setString(7, vo.getType());
                        preparedStatement.setString(8, vo.getSwid());
                        preparedStatement.setString(9, time);
                        preparedStatement.addBatch();
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
