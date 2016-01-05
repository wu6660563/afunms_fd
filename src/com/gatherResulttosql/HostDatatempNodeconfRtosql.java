package com.gatherResulttosql;

import java.util.Hashtable;

import com.afunms.config.model.Nodeconfig;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.node.Host;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

public class HostDatatempNodeconfRtosql implements ResultTosql {

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

        Nodeconfig nodeconfig = (Nodeconfig) dataresult.get("nodeconfig");

        if (nodeconfig != null) {
            String deleteSql = "delete from nms_nodeconfig where nodeid='"
                    + node.getId() + "'";
            execute(deleteSql);

            NodeUtil nodeUtil = new NodeUtil();       
            NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);
            try {
                StringBuffer sql = new StringBuffer(200);
                sql.append("insert into nms_nodeconfig(nodeid,hostname,sysname,serialNumber,cSDVersion,numberOfProcessors,mac)"
                        + "values('");
                sql.append(nodeDTO.getId());
                sql.append("','");
                sql.append(nodeconfig.getHostname());// hostname
                sql.append("','");
                sql.append(nodeconfig.getSysname());// sysname
                sql.append("','");
                sql.append(nodeconfig.getSerialNumber());// serialNumber
                sql.append("','");
                sql.append(nodeconfig.getCSDVersion());// cSDVersion
                sql.append("','");
                sql.append(nodeconfig.getNumberOfProcessors());// numberOfProcessors
                sql.append("','");
                sql.append(nodeconfig.getMac());// mac
                sql.append("')");
                execute(sql.toString());
            } catch (Exception e) {
                e.printStackTrace();
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
