package com.gatherResulttosql;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
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

public class HostDatatempCollecttimeRtosql implements ResultTosql {

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
            
        String collecttime = (String) dataresult.get("collecttime");

        if (null != collecttime) {
            String deleteSql = "delete from nms_other_data_temp where nodeid='"
                    + node.getId() + "' and entity = 'collecttime'";
            execute(deleteSql);
            try {
                Calendar tempCal = Calendar.getInstance();
                Date cc = tempCal.getTime();
                String time = sdf.format(cc);
                StringBuffer sql = new StringBuffer(500);
                sql.append("insert into nms_other_data_temp(nodeid,ip,type,subtype,entity,thevalue,collecttime)values('");
                sql.append(nodeDTO.getId());
                sql.append("','");
                sql.append(node.getIpAddress());
                sql.append("','");
                sql.append(nodeDTO.getType());      // type
                sql.append("','");
                sql.append(nodeDTO.getSubtype());   // subtype
                sql.append("','collecttime','");    // entity
                sql.append(collecttime);            // thevalue数据采集时间
                sql.append("','");
                sql.append(time);//                 collecttime数据保存时间
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            manager.close();
        }
    }
}
