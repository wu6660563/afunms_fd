package com.gatherResulttosql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysUtil;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.Servicecollectdata;
import com.afunms.polling.om.Storagecollectdata;
import com.afunms.temp.model.ServiceNodeTemp;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

public class HostDatatempserciceRttosql implements ResultTosql {

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
        attribute.setAttribute("method", "CreateResultTosql");
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
    public void CreateResultLinuxTosql(Hashtable dataresult, Host node) {
        DBAttribute attribute = new DBAttribute();
        attribute.setAttribute("dataresult", dataresult);
        attribute.setAttribute("node", node);
        attribute.setAttribute("method", "CreateLinuxResultTosql");
        ResultToDB resultToDB = new ResultToDB();
        resultToDB.setResultTosql(this);
        resultToDB.setAttribute(attribute);
        GathersqlListManager.getInstance().addToQueue(resultToDB);
    }

    /**
     * 根据service的Hashtable得到ServiceNodeTemp对象
     * 
     * @param serviceItemHash
     * @return
     */
    public ServiceNodeTemp getServiceNodeTempByHashtable(
            Hashtable serviceItemHash) {
        ServiceNodeTemp serviceNodeTemp = new ServiceNodeTemp();
        Iterator iterator = serviceItemHash.keySet().iterator();
        while (iterator.hasNext()) {
            String key = String.valueOf(iterator.next());
            String value = String.valueOf(serviceItemHash.get(key));
            if (value != null && value.indexOf("\\") != -1) {
                value = value.replaceAll("\\\\", "/");
            }
            if ("DisplayName".equalsIgnoreCase(key)
                    || "name".equalsIgnoreCase(key)) {
                serviceNodeTemp.setName(value);
            }
            if ("instate".equalsIgnoreCase(key)
                    || "State".equalsIgnoreCase(key)
                    || "status".equalsIgnoreCase(key)) {
                serviceNodeTemp.setInstate(value);
            }
            if ("opstate".equalsIgnoreCase(key)) {
                serviceNodeTemp.setOpstate(value);
            }
            if ("uninst".equalsIgnoreCase(key)) {
                serviceNodeTemp.setUninst(value);
            }
            if ("paused".equalsIgnoreCase(key)) {
                serviceNodeTemp.setPaused(value);
            }
            if ("StartMode".equalsIgnoreCase(key)) {
                serviceNodeTemp.setStartMode(value);
            }
            if ("PathName".equalsIgnoreCase(key)) {
                serviceNodeTemp.setPathName(value);
            }
            if ("Description".equalsIgnoreCase(key)) {
                serviceNodeTemp.setDescription(value);
            }
            if ("ServiceType".equalsIgnoreCase(key)) {
                serviceNodeTemp.setServiceType(value);
            }
            if ("pid".equalsIgnoreCase(key)) {
                serviceNodeTemp.setPid(value);
            }
            if ("groupstr".equalsIgnoreCase(key)
                    || "group".equalsIgnoreCase(key)) {
                serviceNodeTemp.setGroupstr(value);
            }
        }
        return serviceNodeTemp;
    }

    public void executeResultToDB(DBAttribute attribute) {
        String method = (String) attribute.getAttribute("method");
        if (method.equals("CreateLinuxResultTosql")) {
            excuteLinuxResultToDB(attribute);
            return;
        }
        Hashtable dataresult = (Hashtable) attribute.getAttribute("dataresult");
        Host node = (Host) attribute.getAttribute("node");
        if (dataresult != null && dataresult.size() > 0) {

            NodeUtil nodeUtil = new NodeUtil();
            NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);

            Vector serviceVector = (Vector) dataresult.get("winservice");
            if (serviceVector != null && serviceVector.size() > 0) {
                Calendar tempCal = Calendar.getInstance();
                Date cc = tempCal.getTime();
                String time = sdf.format(cc);

                String deleteSql = "delete from nms_sercice_data_temp where nodeid='"
                        + node.getId() + "'";
                execute(deleteSql);

                String sql = "insert into nms_sercice_data_temp(nodeid,ip,`type`,subtype,name,instate,opstate,paused,uninst,collecttime)"
                        + "values(?,?,?,?,?,?,?,?,?,?)";
                DBManager manager = new DBManager();
                try {
                    PreparedStatement preparedStatement = manager
                            .prepareStatement(sql);
                    for (int i = 0; i < serviceVector.size(); i++) {
                        Servicecollectdata vo = (Servicecollectdata) serviceVector
                                .elementAt(i);
                        try {
                            preparedStatement.setString(1, String.valueOf(node
                                    .getId()));
                            preparedStatement.setString(2, node.getIpAddress());
                            preparedStatement.setString(3, nodeDTO.getType());
                            preparedStatement
                                    .setString(4, nodeDTO.getSubtype());
                            preparedStatement.setString(5, new String(vo
                                    .getName().getBytes(), "UTF-8"));
                            preparedStatement.setString(6, vo.getInstate());
                            preparedStatement.setString(7, vo.getOpstate());
                            preparedStatement.setString(8, vo.getPaused());
                            preparedStatement.setString(9, vo.getUninst());
                            preparedStatement.setString(10, time);
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
    }

    public void excuteLinuxResultToDB(DBAttribute attribute) {
        Hashtable dataresult = (Hashtable) attribute.getAttribute("dataresult");
        Host node = (Host) attribute.getAttribute("node");

        List serviceList = (ArrayList) dataresult.get("servicelist");
        

        if (serviceList != null && serviceList.size() > 0) {
            NodeUtil nodeUtil = new NodeUtil();
            NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);

            String deleteSql = "delete from nms_sercice_data_temp where nodeid='"
                + node.getId() + "'";
            execute(deleteSql);

            Calendar tempCal = Calendar.getInstance();
            Date cc = tempCal.getTime();
            String time = sdf.format(cc);

            String sql = "insert into nms_sercice_data_temp(nodeid,ip,type,subtype,name,instate,opstate,paused,uninst,collecttime,startMode,pathName,description,serviceType,pid,groupstr)"
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            DBManager manager = new DBManager();
            try {
                PreparedStatement preparedStatement = manager
                        .prepareStatement(sql);
                for (int i = 0; i < serviceList.size(); i++) {
                    Hashtable serviceItemHash = (Hashtable) serviceList.get(i);
                    Enumeration tempEnumeration2 = serviceItemHash.keys();
                    ServiceNodeTemp serviceNodeTemp = null;
                    serviceNodeTemp = getServiceNodeTempByHashtable(serviceItemHash);
                    if (serviceNodeTemp.getPathName() != null
                            && serviceNodeTemp.getPathName().trim().length() > 0) {
                        serviceNodeTemp.setPathName(serviceNodeTemp
                                .getPathName().replaceAll("\"", ""));
                    }
                    try {
                        preparedStatement.setString(1, String.valueOf(nodeDTO.getId()));
                        preparedStatement.setString(2, node.getIpAddress());
                        preparedStatement.setString(3, nodeDTO.getType());// type
                        preparedStatement.setString(4, nodeDTO.getSubtype());// subtype
                        preparedStatement.setString(5, serviceNodeTemp.getName());// name
                        preparedStatement.setString(6, serviceNodeTemp.getInstate());// instate
                        preparedStatement.setString(7, serviceNodeTemp.getOpstate());// opstate
                        preparedStatement.setString(8, serviceNodeTemp.getPaused());// paused
                        preparedStatement.setString(9, serviceNodeTemp.getUninst());// uninst
                        preparedStatement.setString(10, time);// collecttime
                        preparedStatement.setString(11, serviceNodeTemp.getStartMode());// startMode
                        preparedStatement.setString(12, serviceNodeTemp.getPathName());// pathName
                        preparedStatement.setString(13, serviceNodeTemp.getDescription());// description
                        preparedStatement.setString(14, serviceNodeTemp.getServiceType());// serviceType
                        preparedStatement.setString(15, serviceNodeTemp.getPid());// pid
                        preparedStatement.setString(16, serviceNodeTemp.getGroupstr());// groupstr
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
