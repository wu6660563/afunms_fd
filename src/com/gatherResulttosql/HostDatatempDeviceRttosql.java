package com.gatherResulttosql;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.SysLogger;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Devicecollectdata;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

public class HostDatatempDeviceRttosql implements ResultTosql {

    private static SysLogger logger = SysLogger.getLogger(HostDatatempDeviceRttosql.class.getName());
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
        
        NodeUtil nodeUtil = new NodeUtil();
        NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);

        // 处理硬件信息入库
        if (dataresult != null && dataresult.size() > 0) {
            String deleteSql = "delete from nms_device_data_temp where nodeid='"
                + node.getId() + "'";
            execute(deleteSql);
            String sql = "insert into nms_device_data_temp(nodeid,ip,`type`,subtype,name,deviceindex,dtype,status,collecttime)" +
            		"values(?,?,?,?,?,?,?,?,?)";
            Vector hardwareVector = (Vector) dataresult.get("device");
            if (hardwareVector != null && hardwareVector.size() > 0) {
                Calendar tempCal = Calendar.getInstance();
                Date cc = tempCal.getTime();
                String time = sdf.format(cc);
                DBManager manager = new DBManager();
                try {
                    PreparedStatement preparedStatement = manager.prepareStatement(sql);
                    for (int i = 0; i < hardwareVector.size(); i++) {
                        Devicecollectdata vo = (Devicecollectdata) hardwareVector.elementAt(i);
                        String name = vo.getName();
                        if (name != null) {
                            name = name.replaceAll("\\\\", "/").replaceAll("'","");
                        }
                        preparedStatement.setString(1,String.valueOf(node.getId()));
                        preparedStatement.setString(2,node.getIpAddress());
                        preparedStatement.setString(3,nodeDTO.getType());
                        preparedStatement.setString(4,nodeDTO.getSubtype());
                        preparedStatement.setString(5,name);
                        preparedStatement.setString(6,vo.getDeviceindex());
                        preparedStatement.setString(7,vo.getType());
                        preparedStatement.setString(8,vo.getStatus());
                        preparedStatement.setString(9,time);
                        preparedStatement.addBatch();
                    }
                    preparedStatement.executeBatch();
                } catch (Exception e) {
                    logger.error(node.getIpAddress(), e);
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
