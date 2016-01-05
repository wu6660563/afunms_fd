package com.gatherResulttosql;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.config.model.Errptlog;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Host;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

public class HostDatatempErrptRtosql implements ResultTosql {

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
        
        Vector errptVector = (Vector) dataresult.get("errptlog");
        
        if (errptVector != null && errptVector.size() > 0) {
            NodeUtil nodeUtil = new NodeUtil();
            NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);
            
            String deleteSql = "delete from nms_errptlog where nodeid='"
                + node.getId() + "'";
            execute(deleteSql);
            
            Calendar tempCal = Calendar.getInstance();
            Date cc = tempCal.getTime();
            String time = sdf.format(cc);
            
            String sql = "insert into nms_errptlog(labels,identifier,collettime,seqnumber,nodeid,machineid,errptclass,errpttype,resourcename,resourceclass,resourcetype,locations,vpd,descriptions,hostid)" +
            		"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            
            DBManager manager = new DBManager();
            try {
                PreparedStatement preparedStatement = manager.prepareStatement(sql);
                for (int i = 0; i < errptVector.size(); i++) {
                    Errptlog vo = (Errptlog) errptVector.get(i);
                    try {
                        preparedStatement.setString(1, vo.getLabels());
                        preparedStatement.setString(2, vo.getIdentifier());
                        preparedStatement.setString(3, time);
                        preparedStatement.setString(4, String.valueOf(vo.getSeqnumber()));
                        preparedStatement.setString(5, vo.getNodeid());
                        preparedStatement.setString(6, vo.getMachineid());
                        preparedStatement.setString(7, vo.getErrptclass());
                        preparedStatement.setString(8, vo.getErrpttype());
                        preparedStatement.setString(9, vo.getResourcename());
                        preparedStatement.setString(10, vo.getResourceclass());
                        preparedStatement.setString(11, vo.getRescourcetype());
                        preparedStatement.setString(12, vo.getLocations());
                        preparedStatement.setString(13, vo.getVpd());
                        preparedStatement.setString(14, vo.getDescriptions().replaceAll("'", ""));
                        preparedStatement.setString(15, vo.getHostid());
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
