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
import com.afunms.polling.om.Interfacecollectdata;
import com.database.DBManager;

import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

public class NetDatatempfanRtosql implements ResultTosql {

    private static SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * �ѽ������sql
     * 
     * @param dataresult
     *            �ɼ����
     * @param node
     *            ��Ԫ�ڵ�
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
        // ���������Ϣ���
        if (dataresult != null && dataresult.size() > 0) {
            Vector fanVector = (Vector) dataresult.get("fan");
            if (fanVector != null && fanVector.size() > 0) {
                NodeUtil nodeUtil = new NodeUtil();
                NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);

                String deleteSql = "delete from nms_envir_data_temp where nodeid='"
                    + node.getId() + "' and entity='fan'";
                execute(deleteSql);

                Calendar tempCal = Calendar.getInstance();
                Date cc = tempCal.getTime();
                String time = sdf.format(cc);

                String sql = "insert into nms_envir_data_temp (nodeid,ip,`type`,subtype,entity,subentity,sindex,thevalue,chname,restype,collecttime,unit,bak)"
                        + " values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                DBManager manager = new DBManager();
                try {
                    PreparedStatement preparedStatement = manager
                            .prepareStatement(sql);
                    for (int i = 0; i < fanVector.size(); i++) {
                        Interfacecollectdata vo = (Interfacecollectdata) fanVector.elementAt(i);
                        preparedStatement.setString(1, String.valueOf(node.getId()));
                        preparedStatement.setString(2, node.getIpAddress());
                        preparedStatement.setString(3, nodeDTO.getType());
                        preparedStatement.setString(4, nodeDTO.getSubtype());
                        preparedStatement.setString(5, vo.getCategory());
                        preparedStatement.setString(6, vo.getEntity());
                        preparedStatement.setString(7, vo.getSubentity());
                        preparedStatement.setString(8, vo.getThevalue());
                        preparedStatement.setString(9, vo.getChname());
                        preparedStatement.setString(10, vo.getRestype());
                        preparedStatement.setString(11, time);
                        preparedStatement.setString(12, vo.getUnit());
                        preparedStatement.setString(13, vo.getBak());
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
        DBManager manager = new DBManager();// ���ݿ�������
        try {
            manager.executeUpdate(sql);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            manager.close();
        }
    }
}
