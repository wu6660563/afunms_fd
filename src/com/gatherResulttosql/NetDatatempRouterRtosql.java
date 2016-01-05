package com.gatherResulttosql;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.IpRouter;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

public class NetDatatempRouterRtosql implements ResultTosql {

    private static String[] iproutertype = { "", "", "", "direct(3)",
            "indirect(4)" };
    private static String[] iprouterproto = { "", "other(1)", "local(2)",
            "netmgmt(3)", "icmp(4)", "egp(5)", "ggp(6)", "hello(7)", "rip(8)",
            "is-is(9)", "es-is(10)", "ciscoIgrp(11)", "bbnSpfIgp(12)",
            "ospf(13)", "bgp(14)" };

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
        if (dataresult != null && dataresult.size() > 0) {
            Vector routerVector = (Vector) dataresult.get("iprouter");
            if (routerVector != null && routerVector.size() > 0) {
                NodeUtil nodeUtil = new NodeUtil();
                NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);

                String deleteSql = "delete from nms_route_data_temp where nodeid='"
                        + node.getId() + "'";
                execute(deleteSql);

                String sql = "insert into nms_route_data_temp(nodeid,ip,type,subtype,ifindex,nexthop,proto,rtype,mask,collecttime,physaddress,dest)"
                        + " values(?,?,?,?,?,?,?,?,?,?,?,?)";
                DBManager manager = new DBManager();
                try {
                    PreparedStatement preparedStatement = manager
                            .prepareStatement(sql);
                    for (int i = 0; i < routerVector.size(); i++) {
                        IpRouter iprouter = (IpRouter) routerVector
                                .elementAt(i);
                        Calendar tempCal = (Calendar) iprouter.getCollecttime();
                        Date cc = tempCal.getTime();
                        String time = sdf.format(cc);
                        try {
                            preparedStatement.setString(1, String.valueOf(node.getId()));
                            preparedStatement.setString(2, node.getIpAddress());
                            preparedStatement.setString(3, nodeDTO.getType());
                            preparedStatement.setString(4, nodeDTO.getSubtype());
                            preparedStatement.setString(5, iprouter.getIfindex());
                            preparedStatement.setString(6, iprouter.getNexthop());
                            preparedStatement.setString(7, iprouterproto[Integer.parseInt(iprouter
                                    .getProto().longValue()
                                    + "")]);
                            preparedStatement.setString(8, iproutertype[Integer.parseInt(iprouter
                                    .getType().longValue()
                                    + "")]);
                            preparedStatement.setString(9, iprouter.getMask());
                            preparedStatement.setString(10, time);
                            preparedStatement.setString(11, iprouter.getPhysaddress());
                            preparedStatement.setString(12, iprouter.getDest());
                            preparedStatement.addBatch();
                        } catch (Exception e1) {
                            e1.printStackTrace();
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
