/*
 * @(#)HostDataimportProcessRtTosql.java     v1.01, Apr 18, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.gatherResulttosql;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import com.afunms.application.dao.ProcessGroupDao;
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

/**
 * ClassName: HostDataimportProcessRtTosql.java
 * <p>
 * 重要进程入库类
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Apr 18, 2013 5:16:44 PM
 */
public class HostDataimportProcessRtTosql implements ResultTosql {

	private SysLogger logger = SysLogger
			.getLogger(HostDataimportProcessRtTosql.class);

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public void CreateResultTosql(Hashtable dataresult, Host node) {
		DBAttribute attribute = new DBAttribute();
		attribute.setAttribute("dataresult", dataresult);
		attribute.setAttribute("node", node);
		ResultToDB resultToDB = new ResultToDB();
		resultToDB.setResultTosql(this);
		resultToDB.setAttribute(attribute);
		GathersqlListManager.getInstance().addToQueue(resultToDB);
	}

	/**
	 * 把结果生成sql
	 * 
	 * @param dataresult
	 *            采集结果
	 * @param node
	 *            网元节点
	 */
	public void executeResultToDB(DBAttribute attribute) {
		Hashtable dataresult = (Hashtable) attribute.getAttribute("dataresult");
		Host node = (Host) attribute.getAttribute("node");
		if (dataresult != null && dataresult.size() > 0) {

			Hashtable<String, Processcollectdata> importProcess = (Hashtable<String, Processcollectdata>) dataresult
					.get("importProcess");
			Vector<Processcollectdata> processVector = (Vector<Processcollectdata>) dataresult
					.get("process");

			Vector<Processcollectdata> importNumVector = new Vector<Processcollectdata>();
			Vector<Processcollectdata> importAllVector = new Vector<Processcollectdata>();

			NodeUtil nodeUtil = new NodeUtil();
			NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);
			// 取到数据库中IP和进程名称
			ProcessGroupDao groupDao = new ProcessGroupDao();
			Hashtable<String, String> procNameHash = null;
			try {
				procNameHash = groupDao.findByImport(nodeDTO.getNodeid());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				groupDao.close();
			}

			// 刷选重要进程********start**********
			Hashtable<String, Processcollectdata> sindexHash = new Hashtable<String, Processcollectdata>();
			if (importProcess != null && importProcess.size() > 0) {
				Iterator<String> iterator = importProcess.keySet().iterator();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					Iterator<String> iterator1 = procNameHash.keySet().iterator();
					while (iterator1.hasNext()) {
                        String name = (String) iterator1.next();
                        if (key.trim().toLowerCase().contains(name.trim().toLowerCase())) {
                            Processcollectdata datavo = importProcess.get(key);
                            importNumVector.add(datavo);
                            sindexHash.put(datavo.getSubentity(), datavo);
                        }
                    }
				}
			}

			if (processVector != null && processVector.size() > 0) {
				for (int i = 0; i < processVector.size(); i++) {
					// vo为所有的
					Processcollectdata vo = processVector.elementAt(i);
					String sIndex = vo.getSubentity();
					if (sindexHash.containsKey(sIndex.trim())) {
						importAllVector.add(vo);
					}
				}
				// 刷选重要进程********end**********
			}

			String tablename = "pro" + CommonUtil.doip(nodeDTO.getIpaddress());
			String sql = "insert into "
					+ tablename
					+ "(ipaddress,restype,category,entity,subentity,thevalue,collecttime,unit,count,bak,chname)"
					+ "values(?,?,?,?,?,?,?,?,?,?,?)";
			DBManager manager = new DBManager();
			Date date = new Date();
			try {
				PreparedStatement preparedStatement = manager
						.prepareStatement(sql);

				if (importNumVector != null && importNumVector.size() > 0) {
					for (int i = 0; i < importNumVector.size(); i++) {
						Processcollectdata collectdata = (Processcollectdata) importNumVector
								.elementAt(i);
						Calendar tempCal = (Calendar) collectdata
								.getCollecttime();
						Date cc = tempCal.getTime();
						String time = sdf.format(cc);

						preparedStatement.setString(1, collectdata
								.getIpaddress());
						preparedStatement
								.setString(2, collectdata.getRestype());
						preparedStatement.setString(3, collectdata
								.getCategory());
						preparedStatement.setString(4, collectdata.getEntity());
						preparedStatement.setString(5, collectdata
								.getSubentity());
						preparedStatement.setString(6, collectdata
								.getThevalue());
						preparedStatement.setString(7, time);
						preparedStatement.setString(8, collectdata.getUnit());
						preparedStatement.setInt(9, Integer
								.parseInt(collectdata.getThevalue()));
						preparedStatement.setString(10, collectdata.getBak());
						preparedStatement
								.setString(11, collectdata.getChname());
						preparedStatement.addBatch();
					}
				}

				if (importAllVector != null && importAllVector.size() > 0) {
					for (int i = 0; i < importAllVector.size(); i++) {
						Processcollectdata collectdata = (Processcollectdata) importAllVector
								.elementAt(i);
						Calendar tempCal = (Calendar) collectdata
								.getCollecttime();
						Date cc = tempCal.getTime();
						String time = sdf.format(cc);

						preparedStatement.setString(1, collectdata
								.getIpaddress());
						preparedStatement
								.setString(2, collectdata.getRestype());
						preparedStatement.setString(3, collectdata
								.getCategory());
						preparedStatement.setString(4, collectdata.getEntity());
						preparedStatement.setString(5, collectdata
								.getSubentity());
						preparedStatement.setString(6, collectdata
								.getThevalue());
						preparedStatement.setString(7, time);
						preparedStatement.setString(8, collectdata.getUnit());
						preparedStatement.setInt(9, 0);
						preparedStatement.setString(10, collectdata.getBak());
						preparedStatement
								.setString(11, collectdata.getChname());
						preparedStatement.addBatch();
					}
				}
				preparedStatement.executeBatch();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			    Date date2 = new Date();
			    if (date2.getTime() - date.getTime() > 60000) {
			        logger.info(nodeDTO.getIpaddress() + "执行进程 importAllVector.size :" +  importAllVector.size() + ",importNumVector.size:" + importNumVector.size());
			    }
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
