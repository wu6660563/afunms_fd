/*
 * @(#)TomcatResulttosql.java     v1.01, Jan 9, 2013
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

import com.afunms.detail.service.tomcatInfo.TomcatInfoService;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.node.model.PerformanceInfo;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.Tomcat;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.temp.dao.TomcatTempDao;
import com.afunms.temp.model.TomcatNodeTemp;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

/**
 * ClassName:   TomcatResulttosql.java
 * <p>{@link TomcatResulttosql} Tomcat 数据入库
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        Jan 9, 2013 4:17:55 PM
 */
public class TomcatResulttosql implements ResultTosql {

    private static SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * CreateResultTosql:
     * <p>把采集结果生成sql
     *
     * @param   dataresult
     *          - 数据结果列表
     * @param   node
     *          - 节点
     *
     * @since   v1.01
     */
    public void CreateResultTosql(Hashtable dataresult, Node node) {
        DBAttribute attribute = new DBAttribute();
        attribute.setAttribute("dataresult", dataresult);
        attribute.setAttribute("node", node);
        ResultToDB resultToDB = new ResultToDB();
        resultToDB.setResultTosql(this);
        resultToDB.setAttribute(attribute);
        GathersqlListManager.getInstance().addToQueue(resultToDB);
    }

    /**
     * executeResultToDB:
     * <p>执行结果至数据库中
     *
     * @param   attribute
     *          - 数据结果
     *
     * @since   v1.01
     * @see com.gatherdb.ResultTosql#executeResultToDB(com.gatherdb.DBAttribute)
     */
    public void executeResultToDB(DBAttribute attribute) {
        Hashtable dataresult = (Hashtable) attribute.getAttribute("dataresult");
        Tomcat node = (Tomcat) attribute.getAttribute("node");
        NodeUtil nodeUtil = new NodeUtil();
        NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);
        if (dataresult != null && dataresult.size() > 0) {
            Vector<Pingcollectdata> pingVector = (Vector<Pingcollectdata>) dataresult.get("ping");
            if (pingVector != null && pingVector.size() > 0) {
                executePing(pingVector, nodeDTO, node);
            }
            TomcatNodeTemp nodeTemp = (TomcatNodeTemp) dataresult.get("nodeTemp");
            if (nodeTemp != null) {
                executeNodeTemp(nodeTemp, nodeDTO, node);
                executeJVM(nodeTemp, nodeDTO, node);
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

    private void executeNodeTemp(TomcatNodeTemp nodeTemp, NodeDTO nodeDTO, Node node) {
        TomcatTempDao tomcatTempDao = new TomcatTempDao();
        try {
            tomcatTempDao.deleteByNodeId(nodeDTO.getNodeid());
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            tomcatTempDao.close();
        }
        tomcatTempDao = new TomcatTempDao();
        try {
            tomcatTempDao.save(nodeTemp);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            tomcatTempDao.close();
        }
    }

    private void executePing(Vector<Pingcollectdata> pingVector, NodeDTO nodeDTO, Tomcat node) {
        String nodeid = nodeDTO.getNodeid();
        String ip = nodeDTO.getIpaddress();
        String tablename = "tomcatping" + nodeDTO.getNodeid();
        String sql = "insert into "
                + tablename
                + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime)"
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

                    preparedStatement.setString(1, ip);
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

    private void executeJVM(TomcatNodeTemp nodeTemp, NodeDTO nodeDTO, Node node) {
        String nodeid = nodeDTO.getNodeid();
        String ipAddress = nodeDTO.getIpaddress();
        PerformanceInfo info = new PerformanceInfo();
        info.setIpAddress(ipAddress);
        info.setRestype("");
        info.setCategory("");
        info.setEntity("");
        info.setSubentity("");
        info.setThevalue(nodeTemp.getMemoryUtilization());
        info.setCollecttime(nodeTemp.getCollectTime());
        info.setUnit("");
        info.setCount("");
        info.setBak("");
        info.setChname("");
        TomcatInfoService service = new TomcatInfoService();
        service.saveJVMInfo(nodeid, info);
    }
}

