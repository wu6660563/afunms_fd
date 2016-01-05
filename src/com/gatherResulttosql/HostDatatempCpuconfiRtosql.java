package com.gatherResulttosql;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.config.model.Nodecpuconfig;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Host;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

public class HostDatatempCpuconfiRtosql implements ResultTosql {

	/**
	 * 把结果生成sql
	 * @param dataresult 采集结果
	 * @param node 网元节点
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

        List cpuconfiglist = (ArrayList)dataresult.get("cpuconfiglist");
        String deleteSql = "delete from nms_nodecpuconfig where nodeid='" +node.getId() + "'";
        execute(deleteSql);
        //得到CPU平均
        if (cpuconfiglist != null && cpuconfiglist.size() > 0) {
            String sql = "insert into nms_nodecpuconfig(nodeid,dataWidth,processorId,name,l2CacheSize,l2CacheSpeed,descrOfProcessors,processorType,processorSpeed)" +
            		"values(?,?,?,?,?,?,?,?,?)";
            DBManager manager = new DBManager();
            PreparedStatement preparedStatement = manager.prepareStatement(sql);
            try {
                for (int i = 0; i < cpuconfiglist.size(); i++) {
                    Nodecpuconfig cpuconfig = (Nodecpuconfig) cpuconfiglist.get(i);
                    if(cpuconfig.getProcessorSpeed() == null){
                        cpuconfig.setProcessorSpeed("");
                    }
                    if(cpuconfig.getProcessorType() == null){
                        cpuconfig.setProcessorType("");
                    }
                    try {
                        preparedStatement.setString(1,String.valueOf(nodeDTO.getId()));
                        preparedStatement.setString(2,cpuconfig.getDataWidth());//dataWidth
                        preparedStatement.setString(3,cpuconfig.getProcessorId());//processorId
                        preparedStatement.setString(4,cpuconfig.getName());//name
                        preparedStatement.setString(5,cpuconfig.getL2CacheSize());//l2CacheSize
                        preparedStatement.setString(6,cpuconfig.getL2CacheSpeed());//l2CacheSpeed
                        preparedStatement.setString(7,cpuconfig.getDescrOfProcessors());//descrOfProcessors
                        preparedStatement.setString(8,cpuconfig.getProcessorType());//processorType
                        preparedStatement.setString(9,cpuconfig.getProcessorSpeed());//processorSpeed
                        preparedStatement.addBatch();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                manager.executePreparedBatch();
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            manager.close();
        }
    }	
	
}
