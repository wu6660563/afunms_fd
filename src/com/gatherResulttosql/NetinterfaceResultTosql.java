package com.gatherResulttosql;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.config.dao.PortconfigDao;
import com.afunms.config.model.Portconfig;
import com.afunms.polling.node.Host;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.Constant;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.base.Node;
import com.afunms.polling.om.Interfacecollectdata;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;
import com.afunms.polling.om.*;
import com.database.DBManager;

public class NetinterfaceResultTosql implements ResultTosql {

    private static SysLogger logger = SysLogger.getLogger(NetinterfaceResultTosql.class.getName());
    
    private static SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * 
     * 把采集数据生成sql放入的内存列表中
     */
    public void CreateResultTosql(Hashtable ipdata, Host node) {
        DBAttribute attribute = new DBAttribute();
        attribute.setAttribute("ipdata", ipdata);
        attribute.setAttribute("node", node);
        ResultToDB resultToDB = new ResultToDB();
        resultToDB.setResultTosql(this);
        resultToDB.setAttribute(attribute);
        GathersqlListManager.getInstance().addToQueue(resultToDB);
    }

    public void executeResultToDB(DBAttribute attribute) {
        Hashtable ipdata = (Hashtable) attribute.getAttribute("ipdata");
        Host node = (Host) attribute.getAttribute("node");
        NodeUtil nodeUtil = new NodeUtil();
        NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);
        String ip = nodeDTO.getIpaddress();
        String type = nodeDTO.getType();
        if (ipdata.containsKey("interface")) {
            String allipstr = SysUtil.doip(ip);
            
            // 端口状态信息
            Vector interfaceVector = (Vector) ipdata.get("interface");
            // 综合流速信息入库
            Vector allutilhdxVector = (Vector) ipdata.get("allutilhdx");
            // 带宽利用率信息入库
            Vector utilhdxpercVector = (Vector) ipdata.get("utilhdxperc");
            // 各个端口的流速信息入库
            Vector utilhdxVector = (Vector) ipdata.get("utilhdx");
            // 广播包信息入库
            Vector discardspercVector = (Vector) ipdata.get("discardsperc");
            // 错误率信息入库
            Vector errorspercVector = (Vector) ipdata.get("errorsperc");
            // 数据包信息入库
            Vector packsVector = (Vector) ipdata.get("packs");
            // 入口数据包
            Vector inpacksVector = (Vector) ipdata.get("inpacks");
            // 出口数据包信息入库
            Vector outpacksVector = (Vector) ipdata.get("outpacks");
            if (Constant.TYPE_NET.equals(type)) {
                PortconfigDao portconfigDao = new PortconfigDao();
                List<Portconfig> list = null;
                try {
                    list = portconfigDao.findByImportant(ip, "1");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    portconfigDao.close();
                }
                Hashtable<String, Portconfig> hashtable = new Hashtable<String, Portconfig>();
                for (Portconfig portconfig : list) {
                    hashtable.put(String.valueOf(portconfig.getPortindex()), portconfig);
                }
                Vector newIterfaceVector = new Vector();
                if (interfaceVector != null && interfaceVector.size() > 0) {
                    for (int i = 0; i < interfaceVector.size(); i++) {
                        Interfacecollectdata interfacedata = (Interfacecollectdata) interfaceVector
                                .elementAt(i);
                        if (interfacedata.getSubentity() != null && hashtable.containsKey(interfacedata.getSubentity())) {
                            newIterfaceVector.add(interfacedata);
                        }
                    }
                    interfaceVector = newIterfaceVector;
                }
                Vector newUtilhdxpercVector = new Vector();
                if (utilhdxpercVector != null && utilhdxpercVector.size() > 0) {
                    for (int i = 0; i < utilhdxpercVector.size(); i++) {
                        UtilHdxPerc utilhdxperc = (UtilHdxPerc) utilhdxpercVector.elementAt(i);
                        if (hashtable.containsKey(utilhdxperc.getSubentity())) {
                            newUtilhdxpercVector.add(utilhdxperc);
                        }
                    }
                    utilhdxpercVector = newUtilhdxpercVector;
                }
                Vector newUtilhdxVector = new Vector();
                if (utilhdxVector != null && utilhdxVector.size() > 0) {
                    for (int i = 0; i < utilhdxVector.size(); i++) {
                        UtilHdx utilhdx = (UtilHdx) utilhdxVector.elementAt(i);
                        if (hashtable.containsKey(utilhdx.getSubentity())) {
                            newUtilhdxVector.add(utilhdx);
                        }
                    }
                    utilhdxVector = newUtilhdxVector;
                }
                Vector newDiscardspercVector = new Vector();
                if (discardspercVector != null && discardspercVector.size() > 0) {
                    for (int i = 0; i < discardspercVector.size(); i++) {
                        DiscardsPerc discardsperc = (DiscardsPerc) discardspercVector.elementAt(i);
                        if (hashtable.containsKey(discardsperc.getSubentity())) {
                            newDiscardspercVector.add(discardsperc);
                        }
                    }
                    discardspercVector = newDiscardspercVector;
                }
                
                Vector newErrorspercVector = new Vector();
                if (errorspercVector != null && errorspercVector.size() > 0) {
                    for (int i = 0; i < errorspercVector.size(); i++) {
                        ErrorsPerc errorsperc = (ErrorsPerc) errorspercVector.elementAt(i);
                        if (hashtable.containsKey(errorsperc.getSubentity())) {
                            newErrorspercVector.add(errorsperc);
                        }
                    }
                    errorspercVector = newErrorspercVector;
                }
                
                Vector newPacksVector = new Vector();
                if (packsVector != null && packsVector.size() > 0) {
                    for (int i = 0; i < packsVector.size(); i++) {
                        Packs packs = (Packs) packsVector.elementAt(i);
                        if (hashtable.containsKey(packs.getSubentity())) {
                            newPacksVector.add(packs);
                        }
                    }
                    packsVector = newPacksVector;
                }
                
                Vector newInpacksVector = new Vector();
                if (inpacksVector != null && inpacksVector.size() > 0) {
                    for (int i = 0; i < inpacksVector.size(); i++) {
                        InPkts packs = (InPkts) inpacksVector.elementAt(i);
                        if (hashtable.containsKey(packs.getSubentity())) {
                            newInpacksVector.add(packs);
                        }
                    }
                    inpacksVector = newInpacksVector;
                }
                
                Vector newOutpacksVector = new Vector();
                if (outpacksVector != null && outpacksVector.size() > 0) {
                    for (int i = 0; i < outpacksVector.size(); i++) {
                        OutPkts packs = (OutPkts) outpacksVector.elementAt(i);
                        if (hashtable.containsKey(packs.getSubentity())) {
                            newOutpacksVector.add(packs);
                        }
                    }
                    outpacksVector = newOutpacksVector;
                }
            }
            
            // 端口状态信息入库
            if (Constant.TYPE_NET.equals(type) && interfaceVector != null && interfaceVector.size() > 0) {
                String tablename = "portstatus" + allipstr;
                String sql = "insert into "
                    + tablename
                    + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?)";
                DBManager manager = new DBManager();
                Date date = new Date();
                try {
                    PreparedStatement preparedStatement = manager
                            .prepareStatement(sql);
                    for (int i = 0; i < interfaceVector.size(); i++) {
                        Interfacecollectdata interfacedata = (Interfacecollectdata) interfaceVector
                                .elementAt(i);
                        if (interfacedata.getEntity().equals("ifOperStatus")) {
                            Calendar tempCal = (Calendar) interfacedata.getCollecttime();
                            Date cc = tempCal.getTime();
                            String time = sdf.format(cc);
                            long count = 0;
                            if (interfacedata.getCount() != null) {
                                count = interfacedata.getCount();
                            }
    
                            preparedStatement.setString(1, ip);
                            preparedStatement.setString(2, interfacedata.getRestype());
                            preparedStatement.setString(3, interfacedata.getCategory());
                            preparedStatement
                                    .setString(4, interfacedata.getEntity());
                            preparedStatement.setString(5, interfacedata.getSubentity());
                            preparedStatement.setString(6, interfacedata.getUnit());
                            preparedStatement.setString(7, interfacedata.getChname());
                            preparedStatement.setString(8, interfacedata.getBak());
                            preparedStatement.setString(9, String.valueOf(count));
                            preparedStatement.setString(10, interfacedata.getThevalue());
                            preparedStatement.setString(11, time);
                            preparedStatement.addBatch();
                        }
                    }
                    preparedStatement.executeBatch();
                } catch (Exception e) {
                    logger.error(node.getIpAddress() + " 超时时间：" + (new Date().getTime() - date.getTime()), e);
                } finally {
                    manager.close();
                }
            }
            Date date1 = new Date();
            // 综合流速信息入库
            if (allutilhdxVector != null && allutilhdxVector.size() > 0) {
                String tablename = "allutilhdx" + allipstr;
                String sql = "insert into "
                    + tablename
                    + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?)";
                DBManager manager = new DBManager();
                try {
                    PreparedStatement preparedStatement = manager
                            .prepareStatement(sql);
                    for (int si = 0; si < allutilhdxVector.size(); si++) {
                        AllUtilHdx allutilhdx = (AllUtilHdx) allutilhdxVector.elementAt(si);
                        if (allutilhdx.getRestype().equals("dynamic")) {
                            Calendar tempCal = (Calendar) allutilhdx.getCollecttime();
                            Date cc = tempCal.getTime();
                            String time = sdf.format(cc);
    
                            long count = 0;
                            if (allutilhdx.getCount() != null) {
                                count = allutilhdx.getCount();
                            }
                            preparedStatement.setString(1, ip);
                            preparedStatement.setString(2, allutilhdx.getRestype());
                            preparedStatement.setString(3, allutilhdx.getCategory());
                            preparedStatement
                                    .setString(4, allutilhdx.getEntity());
                            preparedStatement.setString(5, allutilhdx.getSubentity());
                            preparedStatement.setString(6, allutilhdx.getUnit());
                            preparedStatement.setString(7, allutilhdx.getChname());
                            preparedStatement.setString(8, allutilhdx.getBak());
                            preparedStatement.setString(9, String.valueOf(count));
                            preparedStatement.setString(10, allutilhdx.getThevalue());
                            preparedStatement.setString(11, time);
                            preparedStatement.addBatch();
                        }
                    }
                    preparedStatement.executeBatch();
                } catch (Exception e) {
                    logger.error(node.getIpAddress() + " 超时时间：" + (new Date().getTime() - date1.getTime()), e);
                } finally {
                    manager.close();
                }
            }
            Date date2 = new Date();
            // 带宽利用率信息入库
            if (utilhdxpercVector != null && utilhdxpercVector.size() > 0) {
                String tablename = "utilhdxperc" + allipstr;
                String sql = "insert into "
                    + tablename
                    + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?)";
                DBManager manager = new DBManager();
                try {
                    PreparedStatement preparedStatement = manager
                            .prepareStatement(sql);
                    for (int si = 0; si < utilhdxpercVector.size(); si++) {
                        UtilHdxPerc utilhdxperc = (UtilHdxPerc) utilhdxpercVector.elementAt(si);
                        if (utilhdxperc.getRestype().equals("dynamic")) {
                            if (utilhdxperc.getThevalue().equals("0")
                                    || utilhdxperc.getThevalue().equals("0.0")) {
                                utilhdxperc.setThevalue("0");
                            }
                            Calendar tempCal = (Calendar) utilhdxperc.getCollecttime();
                            Date cc = tempCal.getTime();
                            String time = sdf.format(cc);
    
                            long count = 0;
                            if (utilhdxperc.getCount() != null) {
                                count = utilhdxperc.getCount();
                            }
                            preparedStatement.setString(1, ip);
                            preparedStatement.setString(2, utilhdxperc.getRestype());
                            preparedStatement.setString(3, utilhdxperc.getCategory());
                            preparedStatement
                                    .setString(4, utilhdxperc.getEntity());
                            preparedStatement.setString(5, utilhdxperc.getSubentity());
                            preparedStatement.setString(6, utilhdxperc.getUnit());
                            preparedStatement.setString(7, utilhdxperc.getChname());
                            preparedStatement.setString(8, utilhdxperc.getBak());
                            preparedStatement.setString(9, String.valueOf(count));
                            preparedStatement.setString(10, utilhdxperc.getThevalue());
                            preparedStatement.setString(11, time);
                            preparedStatement.addBatch();
                        }
                    }
                    preparedStatement.executeBatch();
                } catch (Exception e) {
                    logger.error(node.getIpAddress() + " 超时时间：" + (new Date().getTime() - date2.getTime()), e);
                } finally {
                    manager.close();
                }
            }
            Date date3 = new Date();
            // 各个端口的流速信息入库
            if (utilhdxVector != null && utilhdxVector.size() > 0) {
                String tablename = "utilhdx" + allipstr;
                String sql = "insert into "
                    + tablename
                    + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?)";
                DBManager manager = new DBManager();
                try {
                    PreparedStatement preparedStatement = manager
                            .prepareStatement(sql);
                    for (int si = 0; si < utilhdxVector.size(); si++) {
    
                        UtilHdx utilhdx = (UtilHdx) utilhdxVector.elementAt(si);
                        if (utilhdx.getRestype().equals("dynamic")) {
                            Calendar tempCal = (Calendar) utilhdx.getCollecttime();
                            Date cc = tempCal.getTime();
                            String time = sdf.format(cc);
                            long count = 0;
                            if (utilhdx.getCount() != null) {
                                count = utilhdx.getCount();
                            }
                            preparedStatement.setString(1, ip);
                            preparedStatement.setString(2, utilhdx.getRestype());
                            preparedStatement.setString(3, utilhdx.getCategory());
                            preparedStatement
                                    .setString(4, utilhdx.getEntity());
                            preparedStatement.setString(5, utilhdx.getSubentity());
                            preparedStatement.setString(6, utilhdx.getUnit());
                            preparedStatement.setString(7, utilhdx.getChname());
                            preparedStatement.setString(8, utilhdx.getBak());
                            preparedStatement.setString(9, String.valueOf(count));
                            preparedStatement.setString(10, utilhdx.getThevalue());
                            preparedStatement.setString(11, time);
                            preparedStatement.addBatch();
                        }
                    }
                    preparedStatement.executeBatch();
                } catch (Exception e) {
                    logger.error(node.getIpAddress() + " 超时时间：" + (new Date().getTime() - date3.getTime()), e);
                } finally {
                    manager.close();
                }
            }
            Date date4 = new Date();
            // 广播包信息入库
            if (discardspercVector != null && discardspercVector.size() > 0) {
                String tablename = "discardsperc" + allipstr;
                String sql = "insert into "
                    + tablename
                    + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?)";
                DBManager manager = new DBManager();
                try {
                    PreparedStatement preparedStatement = manager
                            .prepareStatement(sql);
                    for (int si = 0; si < discardspercVector.size(); si++) {
                        DiscardsPerc discardsperc = (DiscardsPerc) discardspercVector
                                .elementAt(si);
                        if (discardsperc.getRestype().equals("dynamic")) {
                            if (discardsperc.getThevalue().equals("0.0")) {
                                discardsperc.setThevalue("0");
                            }
                            Calendar tempCal = (Calendar) discardsperc.getCollecttime();
                            Date cc = tempCal.getTime();
                            String time = sdf.format(cc);
                            long count = 0;
                            if (discardsperc.getCount() != null) {
                                count = discardsperc.getCount();
                            }
                            preparedStatement.setString(1, ip);
                            preparedStatement.setString(2, discardsperc.getRestype());
                            preparedStatement.setString(3, discardsperc.getCategory());
                            preparedStatement
                                    .setString(4, discardsperc.getEntity());
                            preparedStatement.setString(5, discardsperc.getSubentity());
                            preparedStatement.setString(6, discardsperc.getUnit());
                            preparedStatement.setString(7, discardsperc.getChname());
                            preparedStatement.setString(8, discardsperc.getBak());
                            preparedStatement.setString(9, String.valueOf(count));
                            preparedStatement.setString(10, discardsperc.getThevalue());
                            preparedStatement.setString(11, time);
                            preparedStatement.addBatch();
                        }
                    }
                    preparedStatement.executeBatch();
                } catch (Exception e) {
                    logger.error(node.getIpAddress() + " 超时时间：" + (new Date().getTime() - date4.getTime()), e);
                } finally {
                    manager.close();
                }
            }
            Date date5 = new Date();
            // 错误率信息入库
            if (errorspercVector != null && errorspercVector.size() > 0) {
                String tablename = "errorsperc" + allipstr;
                String sql = "insert into "
                    + tablename
                    + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?)";
                DBManager manager = new DBManager();
                try {
                    PreparedStatement preparedStatement = manager
                            .prepareStatement(sql);
                    for (int si = 0; si < errorspercVector.size(); si++) {
                        ErrorsPerc errorsperc = (ErrorsPerc) errorspercVector.elementAt(si);
                        if (errorsperc.getRestype().equals("dynamic")) {
                            if (errorsperc.getThevalue().equals("0.0")) {
                                errorsperc.setThevalue("0");
                            }
                            Calendar tempCal = (Calendar) errorsperc.getCollecttime();
                            Date cc = tempCal.getTime();
                            String time = sdf.format(cc);
                            long count = 0;
                            if (errorsperc.getCount() != null) {
                                count = errorsperc.getCount();
                            }
                            preparedStatement.setString(1, ip);
                            preparedStatement.setString(2, errorsperc.getRestype());
                            preparedStatement.setString(3, errorsperc.getCategory());
                            preparedStatement
                                    .setString(4, errorsperc.getEntity());
                            preparedStatement.setString(5, errorsperc.getSubentity());
                            preparedStatement.setString(6, errorsperc.getUnit());
                            preparedStatement.setString(7, errorsperc.getChname());
                            preparedStatement.setString(8, errorsperc.getBak());
                            preparedStatement.setString(9, String.valueOf(count));
                            preparedStatement.setString(10, errorsperc.getThevalue());
                            preparedStatement.setString(11, time);
                            preparedStatement.addBatch();
                        }
                    }
                    preparedStatement.executeBatch();
                } catch (Exception e) {
                    logger.error(node.getIpAddress() + " 超时时间：" + (new Date().getTime() - date5.getTime()), e);
                } finally {
                    manager.close();
                }
            }
            Date date6 = new Date();
            // 数据包信息入库
            if (packsVector != null && packsVector.size() > 0) {
                String tablename = "packs" + allipstr;
                String sql = "insert into "
                    + tablename
                    + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?)";
                DBManager manager = new DBManager();
                try {
                    PreparedStatement preparedStatement = manager
                            .prepareStatement(sql);
                    for (int si = 0; si < packsVector.size(); si++) {
                        Packs packs = (Packs) packsVector.elementAt(si);
                        if (packs.getRestype().equals("dynamic")) {
                            Calendar tempCal = (Calendar) packs.getCollecttime();
                            Date cc = tempCal.getTime();
                            String time = sdf.format(cc);
                            long count = 0;
                            if (packs.getCount() != null) {
                                count = packs.getCount();
                            }
                            preparedStatement.setString(1, ip);
                            preparedStatement.setString(2, packs.getRestype());
                            preparedStatement.setString(3, packs.getCategory());
                            preparedStatement
                                    .setString(4, packs.getEntity());
                            preparedStatement.setString(5, packs.getSubentity());
                            preparedStatement.setString(6, packs.getUnit());
                            preparedStatement.setString(7, packs.getChname());
                            preparedStatement.setString(8, packs.getBak());
                            preparedStatement.setString(9, String.valueOf(count));
                            preparedStatement.setString(10, packs.getThevalue());
                            preparedStatement.setString(11, time);
                            preparedStatement.addBatch();
                        }
                    }
                    preparedStatement.executeBatch();
                } catch (Exception e) {
                    logger.error(node.getIpAddress() + " 超时时间：" + (new Date().getTime() - date6.getTime()), e);
                } finally {
                    manager.close();
                }
            }
            Date date7 = new Date();
            // 入口数据包
            if (inpacksVector != null && inpacksVector.size() > 0) {
                String tablename = "inpacks" + allipstr;
                String sql = "insert into "
                    + tablename
                    + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?)";
                DBManager manager = new DBManager();
                try {
                    PreparedStatement preparedStatement = manager
                            .prepareStatement(sql);
                    for (int si = 0; si < inpacksVector.size(); si++) {
                        InPkts packs = (InPkts) inpacksVector.elementAt(si);
                        if (packs.getRestype().equals("dynamic")) {
                            Calendar tempCal = (Calendar) packs.getCollecttime();
                            Date cc = tempCal.getTime();
                            String time = sdf.format(cc);
    
                            long count = 0;
                            if (packs.getCount() != null) {
                                count = packs.getCount();
                            }
                            preparedStatement.setString(1, ip);
                            preparedStatement.setString(2, packs.getRestype());
                            preparedStatement.setString(3, packs.getCategory());
                            preparedStatement
                                    .setString(4, packs.getEntity());
                            preparedStatement.setString(5, packs.getSubentity());
                            preparedStatement.setString(6, packs.getUnit());
                            preparedStatement.setString(7, packs.getChname());
                            preparedStatement.setString(8, packs.getBak());
                            preparedStatement.setString(9, String.valueOf(count));
                            preparedStatement.setString(10, packs.getThevalue());
                            preparedStatement.setString(11, time);
                            preparedStatement.addBatch();
                        }
                    }
                    preparedStatement.executeBatch();
                } catch (Exception e) {
                    logger.error(node.getIpAddress() + " 超时时间：" + (new Date().getTime() - date7.getTime()), e);
                } finally {
                    manager.close();
                }
            }
            Date date8 = new Date();
            // 出口数据包信息入库
            if (outpacksVector != null && outpacksVector.size() > 0) {
                String tablename = "outpacks" + allipstr;
                String sql = "insert into "
                    + tablename
                    + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?)";
                DBManager manager = new DBManager();
                try {
                    PreparedStatement preparedStatement = manager
                            .prepareStatement(sql);
                    for (int si = 0; si < outpacksVector.size(); si++) {
    
                        OutPkts packs = (OutPkts) outpacksVector.elementAt(si);
                        if (packs.getRestype().equals("dynamic")) {
                            Calendar tempCal = (Calendar) packs.getCollecttime();
                            Date cc = tempCal.getTime();
                            String time = sdf.format(cc);
                            long count = 0;
                            if (packs.getCount() != null) {
                                count = packs.getCount();
                            }
                            preparedStatement.setString(1, ip);
                            preparedStatement.setString(2, packs.getRestype());
                            preparedStatement.setString(3, packs.getCategory());
                            preparedStatement
                                    .setString(4, packs.getEntity());
                            preparedStatement.setString(5, packs.getSubentity());
                            preparedStatement.setString(6, packs.getUnit());
                            preparedStatement.setString(7, packs.getChname());
                            preparedStatement.setString(8, packs.getBak());
                            preparedStatement.setString(9, String.valueOf(count));
                            preparedStatement.setString(10, packs.getThevalue());
                            preparedStatement.setString(11, time);
                            preparedStatement.addBatch();
                        }
                    }
                    preparedStatement.executeBatch();
                } catch (Exception e) {
                    logger.error(node.getIpAddress() + " 超时时间：" + (new Date().getTime() - date8.getTime()), e);
                } finally {
                    manager.close();
                }
            }
        }
    }
}
