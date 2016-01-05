package com.gatherResulttosql;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.CommonUtil;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.AllUtilHdx;
import com.afunms.polling.om.DiscardsPerc;
import com.afunms.polling.om.ErrorsPerc;
import com.afunms.polling.om.InPkts;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.OutPkts;
import com.afunms.polling.om.Packs;
import com.afunms.polling.om.UtilHdx;
import com.afunms.polling.om.UtilHdxPerc;
import com.database.DBManager;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

public class NetInterfaceDataTemptosql implements ResultTosql {

    private static SysLogger logger = SysLogger.getLogger(NetInterfaceDataTemptosql.class.getName());

    private static SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public void CreateResultTosql(Hashtable interfacehash, Host node) {
        DBAttribute attribute = new DBAttribute();
        attribute.setAttribute("interfacehash", interfacehash);
        attribute.setAttribute("node", node);
        ResultToDB resultToDB = new ResultToDB();
        resultToDB.setResultTosql(this);
        resultToDB.setAttribute(attribute);
        GathersqlListManager.getInstance().addToQueue(resultToDB);
    }

    public void executeResultToDB(DBAttribute attribute) {
        Hashtable interfacehash = (Hashtable) attribute
                .getAttribute("interfacehash");
        Host node = (Host) attribute.getAttribute("node");

        Vector allutilhdxVector = (Vector) interfacehash.get("allutilhdx");
        Vector utilhdxpercVector = (Vector) interfacehash.get("utilhdxperc");
        Vector utilhdxVector = (Vector) interfacehash.get("utilhdx");
        Vector discardspercVector = (Vector) interfacehash.get("discardsperc");
        Vector errorspercVector = (Vector) interfacehash.get("errorsperc");
        Vector packsVector = (Vector) interfacehash.get("packs");
        Vector inpacksVector = (Vector) interfacehash.get("inpacks");
        Vector outpacksVector = (Vector) interfacehash.get("outpacks");
        Vector interfaceVector = (Vector) interfacehash.get("interface");
        
        NodeUtil nodeUtil = new NodeUtil();
        NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);
        
        String ip = CommonUtil.doip(nodeDTO.getIpaddress());

        boolean isDelete = true;
        String deleteSql = "truncate nms_interface_data_temp" + ip;
        if (interfacehash != null && interfacehash.size() > 0) {
            isDelete = true;
        } else  {
            return;
        }

        if (isDelete) {
            execute(deleteSql);
        }

        String table = "nms_interface_data_temp" + ip;
        String sql = "insert into " + table
                + "(nodeid,ip,`type`,subtype,entity,subentity,sindex,thevalue,chname,restype,collecttime,unit,bak)"
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        DBManager manager = new DBManager();
        Date date = new Date();
        try {
            PreparedStatement preparedStatement = manager.prepareStatement(sql);
            // 开始设置综合流速
            if (allutilhdxVector != null && allutilhdxVector.size() > 0) {
                try {
                    for (int i = 0; i < allutilhdxVector.size(); i++) {
                        AllUtilHdx allutilhdx = (AllUtilHdx) allutilhdxVector
                                .elementAt(i);
                        Calendar tempCal = (Calendar) allutilhdx
                                .getCollecttime();
                        Date cc = tempCal.getTime();
                        String time = sdf.format(cc);

                        preparedStatement.setString(1, String.valueOf(node
                                .getId()));
                        preparedStatement.setString(2, node.getIpAddress());
                        preparedStatement.setString(3, nodeDTO.getType());
                        preparedStatement.setString(4, nodeDTO.getSubtype());
                        preparedStatement
                                .setString(5, allutilhdx.getCategory());
                        preparedStatement.setString(6, allutilhdx.getEntity());
                        preparedStatement.setString(7, allutilhdx
                                .getSubentity());
                        preparedStatement
                                .setString(8, allutilhdx.getThevalue());
                        preparedStatement.setString(9, allutilhdx.getChname());
                        preparedStatement
                                .setString(10, allutilhdx.getRestype());
                        preparedStatement.setString(11, time);
                        preparedStatement.setString(12, allutilhdx.getUnit());
                        preparedStatement.setString(13, allutilhdx.getBak());
                        preparedStatement.addBatch();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 结束设置综合流速

            // 开始设置带宽利用率
            if (utilhdxpercVector != null && utilhdxpercVector.size() > 0) {
                try {
                    for (int i = 0; i < utilhdxpercVector.size(); i++) {
                        UtilHdxPerc utilhdxperc = (UtilHdxPerc) utilhdxpercVector
                                .elementAt(i);

                        Calendar tempCal = (Calendar) utilhdxperc
                                .getCollecttime();
                        Date cc = tempCal.getTime();
                        String time = sdf.format(cc);

                        preparedStatement.setString(1, String.valueOf(node
                                .getId()));
                        preparedStatement.setString(2, node.getIpAddress());
                        preparedStatement.setString(3, nodeDTO.getType());
                        preparedStatement.setString(4, nodeDTO.getSubtype());
                        preparedStatement.setString(5, utilhdxperc
                                .getCategory());
                        preparedStatement.setString(6, utilhdxperc.getEntity());
                        preparedStatement.setString(7, utilhdxperc
                                .getSubentity());
                        preparedStatement.setString(8, utilhdxperc
                                .getThevalue());
                        preparedStatement.setString(9, utilhdxperc.getChname());
                        preparedStatement.setString(10, utilhdxperc
                                .getRestype());
                        preparedStatement.setString(11, time);
                        preparedStatement.setString(12, utilhdxperc.getUnit());
                        preparedStatement.setString(13, utilhdxperc.getBak());
                        preparedStatement.addBatch();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 结束设置带宽利用率

            // 开始设置流速
            if (utilhdxVector != null && (utilhdxVector.size() > 0)) {
                try {
                    isDelete = true;
                    for (int i = 0; i < utilhdxVector.size(); i++) {
                        UtilHdx utilhdx = (UtilHdx) utilhdxVector.elementAt(i);
                        Calendar tempCal = (Calendar) utilhdx.getCollecttime();
                        Date cc = tempCal.getTime();
                        String time = sdf.format(cc);
                        preparedStatement.setString(1, String.valueOf(node
                                .getId()));
                        preparedStatement.setString(2, node.getIpAddress());
                        preparedStatement.setString(3, nodeDTO.getType());
                        preparedStatement.setString(4, nodeDTO.getSubtype());
                        preparedStatement.setString(5, utilhdx.getCategory());
                        preparedStatement.setString(6, utilhdx.getEntity());
                        preparedStatement.setString(7, utilhdx.getSubentity());
                        preparedStatement.setString(8, utilhdx.getThevalue());
                        preparedStatement.setString(9, utilhdx.getChname());
                        preparedStatement.setString(10, utilhdx.getRestype());
                        preparedStatement.setString(11, time);
                        preparedStatement.setString(12, utilhdx.getUnit());
                        preparedStatement.setString(13, utilhdx.getBak());
                        preparedStatement.addBatch();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 结束设置流速

            // 开始设置流速
            if (interfaceVector  != null
                    && interfaceVector.size() > 0) {
                try {
                    for (int i = 0; i < interfaceVector.size(); i++) {
                        Interfacecollectdata interfacedata = (Interfacecollectdata) interfaceVector.elementAt(i);

                        Calendar tempCal = (Calendar) interfacedata
                                .getCollecttime();
                        Date cc = tempCal.getTime();
                        String time = sdf.format(cc);
                        preparedStatement.setString(1, String.valueOf(node
                                .getId()));
                        preparedStatement.setString(2, node.getIpAddress());
                        preparedStatement.setString(3, nodeDTO.getType());
                        preparedStatement.setString(4, nodeDTO.getSubtype());
                        preparedStatement.setString(5, interfacedata
                                .getCategory());
                        preparedStatement.setString(6, interfacedata
                                .getEntity());
                        preparedStatement.setString(7, interfacedata
                                .getSubentity());
                        preparedStatement.setString(8, interfacedata
                                .getThevalue());
                        preparedStatement.setString(9, interfacedata
                                .getChname());
                        preparedStatement.setString(10, interfacedata
                                .getRestype());
                        preparedStatement.setString(11, time);
                        preparedStatement
                                .setString(12, interfacedata.getUnit());
                        preparedStatement.setString(13, interfacedata.getBak());
                        preparedStatement.addBatch();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 结束设置流速

            // 开始设置丢包率
            if (discardspercVector != null && discardspercVector.size() > 0) {
                try {
                    isDelete = true;
                    for (int i = 0; i < discardspercVector.size(); i++) {
                        DiscardsPerc discardsperc = (DiscardsPerc) discardspercVector
                                .elementAt(i);

                        Calendar tempCal = (Calendar) discardsperc
                                .getCollecttime();
                        Date cc = tempCal.getTime();
                        String time = sdf.format(cc);
                        preparedStatement.setString(1, String.valueOf(node
                                .getId()));
                        preparedStatement.setString(2, node.getIpAddress());
                        preparedStatement.setString(3, nodeDTO.getType());
                        preparedStatement.setString(4, nodeDTO.getSubtype());
                        preparedStatement.setString(5, discardsperc
                                .getCategory());
                        preparedStatement
                                .setString(6, discardsperc.getEntity());
                        preparedStatement.setString(7, discardsperc
                                .getSubentity());
                        preparedStatement.setString(8, discardsperc
                                .getThevalue());
                        preparedStatement
                                .setString(9, discardsperc.getChname());
                        preparedStatement.setString(10, discardsperc
                                .getRestype());
                        preparedStatement.setString(11, time);
                        preparedStatement.setString(12, discardsperc.getUnit());
                        preparedStatement.setString(13, discardsperc.getBak());
                        preparedStatement.addBatch();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 结束设置丢包率

            // 开始设置错误率
            if (errorspercVector != null && errorspercVector.size() > 0) {
                try {
                    isDelete = true;
                    for (int i = 0; i < errorspercVector.size(); i++) {
                        ErrorsPerc errorsperc = (ErrorsPerc) errorspercVector
                                .elementAt(i);
                        Calendar tempCal = (Calendar) errorsperc
                                .getCollecttime();
                        Date cc = tempCal.getTime();
                        String time = sdf.format(cc);
                        preparedStatement.setString(1, String.valueOf(node
                                .getId()));
                        preparedStatement.setString(2, node.getIpAddress());
                        preparedStatement.setString(3, nodeDTO.getType());
                        preparedStatement.setString(4, nodeDTO.getSubtype());
                        preparedStatement
                                .setString(5, errorsperc.getCategory());
                        preparedStatement.setString(6, errorsperc.getEntity());
                        preparedStatement.setString(7, errorsperc
                                .getSubentity());
                        preparedStatement
                                .setString(8, errorsperc.getThevalue());
                        preparedStatement.setString(9, errorsperc.getChname());
                        preparedStatement
                                .setString(10, errorsperc.getRestype());
                        preparedStatement.setString(11, time);
                        preparedStatement.setString(12, errorsperc.getUnit());
                        preparedStatement.setString(13, errorsperc.getBak());
                        preparedStatement.addBatch();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 结束设置错误率

            // 开始设置数据包
            if (packsVector != null && packsVector.size() > 0) {
                try {
                    for (int i = 0; i < packsVector.size(); i++) {
                        Packs packs = (Packs) packsVector.elementAt(i);
                        Calendar tempCal = (Calendar) packs.getCollecttime();
                        Date cc = tempCal.getTime();
                        String time = sdf.format(cc);
                        preparedStatement.setString(1, String.valueOf(node
                                .getId()));
                        preparedStatement.setString(2, node.getIpAddress());
                        preparedStatement.setString(3, nodeDTO.getType());
                        preparedStatement.setString(4, nodeDTO.getSubtype());
                        preparedStatement.setString(5, packs.getCategory());
                        preparedStatement.setString(6, packs.getEntity());
                        preparedStatement.setString(7, packs.getSubentity());
                        preparedStatement.setString(8, packs.getThevalue());
                        preparedStatement.setString(9, packs.getChname());
                        preparedStatement.setString(10, packs.getRestype());
                        preparedStatement.setString(11, time);
                        preparedStatement.setString(12, packs.getUnit());
                        preparedStatement.setString(13, packs.getBak());
                        preparedStatement.addBatch();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 结束设置数据包

            // 开始设置入口广播和多播数据包
            if (inpacksVector != null && inpacksVector.size() > 0) {
                try {
                    isDelete = true;
                    for (int i = 0; i < inpacksVector.size(); i++) {
                        InPkts packs = (InPkts) inpacksVector.elementAt(i);
                        Calendar tempCal = (Calendar) packs.getCollecttime();
                        Date cc = tempCal.getTime();
                        String time = sdf.format(cc);
                        preparedStatement.setString(1, String.valueOf(node
                                .getId()));
                        preparedStatement.setString(2, node.getIpAddress());
                        preparedStatement.setString(3, nodeDTO.getType());
                        preparedStatement.setString(4, nodeDTO.getSubtype());
                        preparedStatement.setString(5, packs.getCategory());
                        preparedStatement.setString(6, packs.getEntity());
                        preparedStatement.setString(7, packs.getSubentity());
                        preparedStatement.setString(8, packs.getThevalue());
                        preparedStatement.setString(9, packs.getChname());
                        preparedStatement.setString(10, packs.getRestype());
                        preparedStatement.setString(11, time);
                        preparedStatement.setString(12, packs.getUnit());
                        preparedStatement.setString(13, packs.getBak());
                        preparedStatement.addBatch();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 结束设置入口广播和多播数据包

            // 开始设置出口广播和多播数据包
            if (outpacksVector != null && outpacksVector.size() > 0) {
                try {
                    isDelete = true;
                    for (int i = 0; i < outpacksVector.size(); i++) {
                        OutPkts packs = (OutPkts) outpacksVector.elementAt(i);
                        Calendar tempCal = (Calendar) packs.getCollecttime();
                        Date cc = tempCal.getTime();
                        String time = sdf.format(cc);
                        preparedStatement.setString(1, String.valueOf(node
                                .getId()));
                        preparedStatement.setString(2, node.getIpAddress());
                        preparedStatement.setString(3, nodeDTO.getType());
                        preparedStatement.setString(4, nodeDTO.getSubtype());
                        preparedStatement.setString(5, packs.getCategory());
                        preparedStatement.setString(6, packs.getEntity());
                        preparedStatement.setString(7, packs.getSubentity());
                        preparedStatement.setString(8, packs.getThevalue());
                        preparedStatement.setString(9, packs.getChname());
                        preparedStatement.setString(10, packs.getRestype());
                        preparedStatement.setString(11, time);
                        preparedStatement.setString(12, packs.getUnit());
                        preparedStatement.setString(13, packs.getBak());
                        preparedStatement.addBatch();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            preparedStatement.executeBatch();
        } catch (Exception e) {
            logger.error("超时时间：" + (new Date().getTime() - date.getTime()), e);
        } finally {
            manager.close();
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
