package com.gatherResulttosql;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.CommonUtil;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Processcollectdata;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

public class HostDatatempProcessRtTosql implements ResultTosql {

    private static SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private static SysLogger logger = SysLogger.getLogger(HostDatatempProcessRtTosql.class);
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

        if (dataresult != null && dataresult.size() > 0) {
            Vector processVector = (Vector) dataresult.get("process");
            if (processVector != null && processVector.size() > 0) {

                NodeUtil nodeUtil = new NodeUtil();
                NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);

                String tableName = "nms_process_data_temp" + CommonUtil.doip(nodeDTO.getIpaddress());
                String deleteSql = "truncate " + tableName;
                execute(deleteSql);

                String sql = "insert into " + tableName + "(nodeid,ip,`type`,subtype,entity,subentity,sindex,thevalue,chname,restype,collecttime,unit,bak)"
                        + "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                DBManager manager = new DBManager();
                try {
                    PreparedStatement preparedStatement = manager
                            .prepareStatement(sql);
                    for (int i = 0; i < processVector.size(); i++) {
                        Processcollectdata vo = (Processcollectdata) processVector
                                .elementAt(i);
                        Calendar tempCal = (Calendar) vo.getCollecttime();
                        Date cc = tempCal.getTime();
                        String time = sdf.format(cc);
                        String thevalue = vo.getThevalue();
                        if (thevalue != null) {
                            thevalue = thevalue.replaceAll("\\\\", "/");
                            if (thevalue.length() > 200) {
                                thevalue = thevalue.substring(0, 200) + "...";
                            }
                        }
                        preparedStatement.setString(1, String.valueOf(node
                                .getId()));
                        preparedStatement.setString(2, node.getIpAddress());
                        preparedStatement.setString(3, nodeDTO.getType());
                        preparedStatement.setString(4, nodeDTO.getSubtype());
                        preparedStatement.setString(5, vo.getCategory());
                        preparedStatement.setString(6, vo.getEntity());
                        preparedStatement.setString(7, vo.getSubentity());
                        preparedStatement.setString(8, thevalue);
                        preparedStatement.setString(9, vo.getChname());
                        preparedStatement.setString(10, vo.getRestype());
                        preparedStatement.setString(11, time);
                        preparedStatement.setString(12, vo.getUnit());
                        preparedStatement.setString(13, vo.getBak());
                        preparedStatement.addBatch();
                    }
                    preparedStatement.executeBatch();
                } catch (Exception e) {
                    logger.error("IP地址：" + nodeDTO.getIpaddress() + " 临时进程入库错误" , e);
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
