/*
 * @(#)PTimeDBDatatempRttosql.java     v1.01, May 14, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.gatherResulttosql;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.application.model.ApplicationNode;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Pingcollectdata;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

/**
 * ClassName: PTimeDBDatatempRttosql.java
 * <p>
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date May 14, 2013 4:33:26 PM
 */
public class PTimeDBDatatempRttosql implements ResultTosql {

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
	public void CreateResultTosql(Hashtable dataresult, ApplicationNode node) {
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
		ApplicationNode node = (ApplicationNode) attribute.getAttribute("node");
		if (dataresult != null && dataresult.size() > 0) {
			Vector pingVector = (Vector) dataresult.get("ping");
			if (pingVector != null && pingVector.size() > 0) {

				NodeUtil nodeUtil = new NodeUtil();
				NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(node);
				String tablename = "ptimedbping" + nodeDTO.getNodeid();

				String sql = "insert into "+tablename+"(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime)"
                	+ "values(?,?,?,?,?,?,?,?,?,?,?)";
				DBManager manager = new DBManager();
				try {
					PreparedStatement preparedStatement = manager
							.prepareStatement(sql);
					for (int i = 0; i < pingVector.size(); i++) {
		                Pingcollectdata pingdata = (Pingcollectdata) pingVector
		                        .elementAt(i);
		                if (pingdata.getRestype().equals("dynamic")) {
		                    Calendar tempCal = (Calendar) pingdata
		                            .getCollecttime();
		                    Date cc = tempCal.getTime();
		                    String time = sdf.format(cc);
		                    Long count = pingdata.getCount();
		                    if (count == null) {
		                        count = 0L;
		                    }

		                    preparedStatement.setString(1, nodeDTO.getIpaddress());
		                    preparedStatement.setString(2, pingdata
		                            .getRestype());
		                    preparedStatement.setString(3, pingdata
		                            .getCategory());// type
		                    preparedStatement
		                            .setString(4, pingdata.getEntity());// subtype
		                    preparedStatement.setString(5, pingdata
		                            .getSubentity());// entity
		                    preparedStatement.setString(6, pingdata.getUnit());// subentity
		                    preparedStatement
		                            .setString(7, pingdata.getChname());// sindex
		                    preparedStatement.setString(8, pingdata.getBak());// thevalue
		                    preparedStatement.setString(9, String
		                            .valueOf(count));// collecttime
		                    preparedStatement.setString(10, pingdata
		                            .getThevalue());// collecttime
		                    preparedStatement.setString(11, time);// collecttime
		                    preparedStatement.addBatch();
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
