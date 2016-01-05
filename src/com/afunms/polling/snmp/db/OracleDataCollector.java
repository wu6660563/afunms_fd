/*
 * @(#)OracleDataCollector.java     v1.01, Jan 21, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.db;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.OraclePartsDao;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.OracleEntity;
import com.afunms.application.model.OracleLockInfo;
import com.afunms.application.model.OracleTopSqlReadWrite;
import com.afunms.application.model.OracleTopSqlSort;
import com.afunms.application.model.Oracle_sessiondata;
import com.afunms.application.util.IpTranslation;
import com.afunms.common.base.BaseVo;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.EncryptUtil;
import com.afunms.config.dao.OracleTableSpaceDao;
import com.afunms.config.model.OracleTableSpaceConfig;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.polling.api.IndicatorGather;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.DBNode;
import com.afunms.polling.node.Result;
import com.afunms.polling.om.Pingcollectdata;
import com.gatherResulttosql.DBDataTosql;

/**
 * ClassName:   OracleDataCollector.java
 * <p>{@link OracleDataCollector} Oracle 数据库采集控制类
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        Jan 21, 2013 4:14:58 PM
 */
public class OracleDataCollector extends SnmpMonitor implements IndicatorGather {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * collect_Data:
     * <p>用于反射调用
     *
     * @param   nodeGatherIndicators
     *          - 采集指标
     * @return  {@link Hashtable}
     *          - 设备信息, 不在使用
     *
     * @since   v1.01
     */
    public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
        OracleEntity oracleEntity = null;
        OraclePartsDao partdao = new OraclePartsDao();
        try {
            oracleEntity = (OracleEntity) partdao.findByID(nodeGatherIndicators.getNodeid());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            partdao.close();
        }
        if (oracleEntity == null) {
            return null;
        }
        DBVo oracleVo = null;
        DBDao dbdao = new DBDao();
        try{
            oracleVo = (DBVo)dbdao.findByID(oracleEntity.getDbid()+"");
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            dbdao.close();
        }
        if (oracleVo == null) {
            return null;
        }
        
        String ipAddress = oracleVo.getIpAddress();
        String userName = oracleEntity.getUser();
        String password = "";
        try {
            password = EncryptUtil.decode(oracleEntity.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        int port = Integer.parseInt(oracleVo.getPort());
        String DBName = oracleEntity.getSid();
        DBNode node = new DBNode();
        node.setId(oracleEntity.getId());
        node.setIpAddress(ipAddress);
        node.setPort(String.valueOf(port));
        node.setDbName(DBName);
        node.setUser(userName);
        node.setPassword(password);
        Result result = getValue(node, nodeGatherIndicators);
        
        Hashtable oracledata = (Hashtable) result.getResult();
        // 用于告警
        updateData(oracleVo, oracleEntity, oracledata, nodeGatherIndicators);

        Hashtable dataresult = new  Hashtable();
        dataresult.put(ipAddress + ":" + oracleEntity.getId(), oracledata);
        DBDataTosql dataTosql = new DBDataTosql();
        dataTosql.CreateResultTosql(dataresult, null);
        return dataresult;
    }

    /**
     * getValue:
     *
     * @param   node
     *          - 
     * @param nodeGatherIndicators
     * @return
     *
     * @since   v1.01
     * @see com.afunms.polling.api.IndicatorGather#getValue(com.afunms.polling.base.Node, com.afunms.indicators.model.NodeGatherIndicators)
     */
    public Result getValue(Node node, NodeGatherIndicators nodeGatherIndicators) {
        DBNode dbNode = (DBNode) node;
        int id = dbNode.getId();
        String ipAddress = dbNode.getIpAddress();
        int port = Integer.parseInt(dbNode.getPort());
        String DBName = dbNode.getDbName();
        String userName = dbNode.getUser();
        String password = dbNode.getPassword();
        boolean oracleIsOK = false;
        DBDao dbdao = new DBDao();
        try {
            oracleIsOK = dbdao.getOracleIsOK(ipAddress, port, DBName, userName, password);
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            dbdao.close();
        }
        if (!oracleIsOK) {
            try {
                Thread.sleep(15000);
                oracleIsOK = dbdao.getOracleIsOK(ipAddress, port, DBName, userName, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Hashtable oracledata = new  Hashtable();
        if (oracleIsOK) {
            try {
                Hashtable<String, NodeGatherIndicators> gatherHash = new Hashtable<String, NodeGatherIndicators>();
                gatherHash.put("session", nodeGatherIndicators);
                gatherHash.put("tablespace", nodeGatherIndicators);
                gatherHash.put("rollback", nodeGatherIndicators);
                gatherHash.put("sysinfo", nodeGatherIndicators);
                gatherHash.put("memory", nodeGatherIndicators);
                gatherHash.put("lock", nodeGatherIndicators);
                gatherHash.put("HitRatioAndMemorysort", nodeGatherIndicators);
                gatherHash.put("opencur", nodeGatherIndicators);
                //gatherHash.put("table", nodeGatherIndicators);
                gatherHash.put("topsql", nodeGatherIndicators);
                gatherHash.put("controlfile", nodeGatherIndicators);
                gatherHash.put("sysinfo", nodeGatherIndicators);
                gatherHash.put("log", nodeGatherIndicators);
                gatherHash.put("keepobj", nodeGatherIndicators);
                gatherHash.put("openmode", nodeGatherIndicators);
                gatherHash.put("extent", nodeGatherIndicators);
                gatherHash.put("wait", nodeGatherIndicators);
                gatherHash.put("user", nodeGatherIndicators);
                gatherHash.put("baseInfo", nodeGatherIndicators);
                gatherHash.put("topSqlReadWrite", nodeGatherIndicators);
                gatherHash.put("topSqlSort", nodeGatherIndicators);
                gatherHash.put("asm", nodeGatherIndicators);
                
                Hashtable allOraData =  dbdao.getAllOracleData(ipAddress, port, DBName, userName, password, gatherHash);
                Vector info = (Vector) allOraData.get("session");
                Vector tableinfo = (Vector) allOraData.get("tablespace");
                Vector rollbackinfo_v = (Vector) allOraData.get("rollback");
                Hashtable sysValue = (Hashtable) allOraData.get("sysinfo");
                Hashtable memValue = (Hashtable) allOraData.get("ga_hash");
                Vector lockinfo_v = (Vector) allOraData.get("lock");
                Hashtable memPerfValue = (Hashtable) allOraData.get("memoryPerf");
                Vector table_v = (Vector) allOraData.get("table");
                Vector sql_v = (Vector) allOraData.get("topsql");
                Vector contrFile_v = (Vector) allOraData.get("controlfile");
                Hashtable isArchive_h = (Hashtable) allOraData.get("sy_hash");
                Vector logFile_v = (Vector) allOraData.get("log");
                Vector keepObj_v = (Vector) allOraData.get("keepobj");  
                String lstrnStatu = (String) allOraData.get("open_mode");
                Vector extent_v = (Vector) allOraData.get("extent");
                Hashtable userinfo_h = (Hashtable) allOraData.get("user");
                Hashtable cursors = (Hashtable) allOraData.get("cursors");
                Vector wait = (Vector) allOraData.get("wait");
                Hashtable dbio = (Hashtable) allOraData.get("dbio");
                OracleLockInfo oracleLockInfo = (OracleLockInfo) allOraData.get("oracleLockInfo");
                Vector<OracleTopSqlReadWrite> oracleTopSqlReadWriteVector = (Vector<OracleTopSqlReadWrite>) allOraData.get("topSqlReadWriteVector");
                Vector<OracleTopSqlSort> oracleTopSqlSortVector = (Vector<OracleTopSqlSort>) allOraData.get("topSqlSortVector");
                Hashtable<String, String> baseInfoHash = (Hashtable<String, String>) allOraData.get("baseInfoHash");

                if (info == null)
                    info = new Vector();
                if (sysValue == null)
                    sysValue = new Hashtable();
                if (memValue == null)
                    memValue = new Hashtable();
                if (memPerfValue == null)
                    memPerfValue = new Hashtable();
                if (tableinfo == null)
                    tableinfo = new Vector();
                if (rollbackinfo_v == null)
                    rollbackinfo_v = new Vector();
                if (lockinfo_v == null)
                    lockinfo_v = new Vector();
                if (table_v == null)
                    table_v = new Vector();
                if (sql_v == null)
                    sql_v = new Vector();
                if (contrFile_v == null)
                    contrFile_v = new Vector();
                if (logFile_v == null)
                    logFile_v = new Vector();
                if (keepObj_v == null)
                    keepObj_v = new Vector();
                if (isArchive_h == null)
                    isArchive_h = new Hashtable();
                if(lstrnStatu == null)
                    lstrnStatu = "READ WRITE";
                if(extent_v == null)
                    extent_v = new Vector();
                if(userinfo_h == null)
                    userinfo_h = new Hashtable();
                if(cursors == null)
                    cursors = new Hashtable();
                if(wait == null)
                    wait = new Vector();
                if(dbio == null)
                    dbio = new Hashtable();
                if(baseInfoHash == null){
                    baseInfoHash = new Hashtable<String, String>();
                }
                if(oracleLockInfo == null){
                    oracleLockInfo = new OracleLockInfo();
                }
                if(oracleTopSqlReadWriteVector == null){
                    oracleTopSqlReadWriteVector = new Vector<OracleTopSqlReadWrite>();
                }
                if(oracleTopSqlSortVector == null){
                    oracleTopSqlSortVector = new Vector<OracleTopSqlSort>();
                }
                oracledata.put("sysValue", sysValue);
                oracledata.put("memValue", memValue);//   nms_oramemvalue
                oracledata.put("memPerfValue", memPerfValue);// nms_oramemperfvalue
                oracledata.put("tableinfo_v", tableinfo);//表空间
                oracledata.put("rollbackinfo_v", rollbackinfo_v);//回滚段信息
                oracledata.put("lockinfo_v", lockinfo_v);//锁信息
                oracledata.put("info_v", info);//session信息
                oracledata.put("table_v", table_v);//表信息
                oracledata.put("sql_v", sql_v);//TOPN的SQL语句信息
                oracledata.put("contrFile_v", contrFile_v);//控制文件 nms_oracontrfile
                oracledata.put("isArchive_h", isArchive_h); // nms_oraisarchive
                oracledata.put("keepObj_v", keepObj_v);// nms_orakeepobj
                oracledata.put("lstrnStatu",  "READ WRITE");//监听状态     nms_orastatus
                oracledata.put("extent_v", extent_v);//扩展信息 //nms_oraextent
                oracledata.put("logFile_v", logFile_v);//日志文件  //nms_oralogfile
                oracledata.put("userinfo_h", userinfo_h);//用户信息  //nms_orauserinfo
                oracledata.put("cursors", cursors);//指针信息   //nms_oracursors
                oracledata.put("wait", wait);//等待信息   //nms_orawait
                oracledata.put("dbio", dbio);//IO信息   //nms_oradbio
                oracledata.put("status", "1");//状态信息     nms_orastatus
                oracledata.put("baseInfoHash", baseInfoHash);//基本信息
                oracledata.put("oracleLockInfo", oracleLockInfo);//oracle锁指标等//重庆商行
                oracledata.put("oracleTopSqlReadWriteVector", oracleTopSqlReadWriteVector);
                oracledata.put("oracleTopSqlSortVector", oracleTopSqlSortVector);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            oracledata.put("lstrnStatu", "0");//监听状态     nms_orastatus
        }
        
        Pingcollectdata hostdata = null;
        hostdata = new Pingcollectdata();
        hostdata.setIpaddress(ipAddress + ":" + id);
        Calendar date = Calendar.getInstance();
        hostdata.setCollecttime(date);
        hostdata.setCategory("ORAPing");
        hostdata.setEntity("Utilization");
        hostdata.setSubentity("ConnectUtilization");
        hostdata.setRestype("dynamic");
        hostdata.setUnit("%");
        hostdata.setThevalue("0");
        if (oracleIsOK) {
            hostdata.setThevalue("100");
        }
        oracledata.put("ping", hostdata);
        Result result = new Result();
        result.setCollectTime(getCalendar().getTime());
        result.setErrorCode(0);
        result.setResult("");
        result.setResult(oracledata);
        return result;
    }

    public void updateData(DBVo dbVo, OracleEntity vo , Object collectingData, NodeGatherIndicators nodeGatherIndicators){
        if(vo == null || collectingData == null){
            return ;
        }
        try {
            OracleEntity oracle = (OracleEntity)vo;
            Hashtable datahashtable = (Hashtable)collectingData;
            Hashtable memeryHashtable = (Hashtable)datahashtable.get("memPerfValue");
            if(memeryHashtable == null)memeryHashtable = new Hashtable();
            
            Hashtable cursorsHashtable = (Hashtable)datahashtable.get("cursors");
            if(cursorsHashtable == null)cursorsHashtable = new Hashtable();
            
            Vector tableinfo_v = null;
            try {
                tableinfo_v = (Vector) datahashtable.get("tableinfo_v");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Pingcollectdata pingcollectdata = null;
            try {
                pingcollectdata = (Pingcollectdata) datahashtable.get("ping");
            } catch (Exception e) {
                e.printStackTrace();
            }
            AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
            
            List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(oracle.getId()), AlarmConstant.TYPE_DB, "oracle");
            
            CheckEventUtil checkEvent = new CheckEventUtil();
            for(int i = 0 ; i < list.size() ; i ++){
                AlarmIndicatorsNode alarmIndicatorsNode = (AlarmIndicatorsNode)list.get(i);
                if("1".equals(alarmIndicatorsNode.getEnabled())){
                    String indicators = alarmIndicatorsNode.getName();
                    
                    String sindex = "";
                    String value = "0";
                    
                    if("buffercache".equals(indicators) && "HitRatioAndMemorysort".equalsIgnoreCase(nodeGatherIndicators.getName()) ){
                        value = (String)memeryHashtable.get(indicators);
                    }else if("dictionarycache".equals(indicators) && "HitRatioAndMemorysort".equalsIgnoreCase(nodeGatherIndicators.getName()) ){
                        value = (String)memeryHashtable.get(indicators);
                    }else if("pctmemorysorts".equals(indicators) && "HitRatioAndMemorysort".equalsIgnoreCase(nodeGatherIndicators.getName()) ){
                        value = (String)memeryHashtable.get(indicators);
                    }else if("pctbufgets".equals(indicators) && "HitRatioAndMemorysort".equalsIgnoreCase(nodeGatherIndicators.getName()) ){
                        value = (String)memeryHashtable.get(indicators);
                    }else if("opencur".equals(indicators) && "opencur".equalsIgnoreCase(nodeGatherIndicators.getName()) ){
                        value = (String)cursorsHashtable.get(indicators);
                    }else if("ping".equals(indicators)){
                        if (pingcollectdata == null) {
                            continue;
                        }
                        value = pingcollectdata.getThevalue();
                    }else if("tablespace".equals(indicators)){
                        value = (String)cursorsHashtable.get(indicators);
                        if (tableinfo_v == null) {
                            continue;
                        }
                        for (int k = 0; k < tableinfo_v.size(); k++) {
                            Hashtable ht = (Hashtable) tableinfo_v.get(k);
                            sindex = ht.get("tablespace").toString();
                            String percent_free = ht.get("percent_free").toString();
                            if (percent_free == null || percent_free.trim().length() == 0) {
                                percent_free = "0";
                            }
                            
                            // 获取表空间中的文件名信息
                            String file_name = ht.get("file_name").toString();
                            file_name = file_name.replaceAll("\\\\", "/");
                             
                            
                            try {
                                value = String.valueOf(100 - Double.valueOf(percent_free.trim()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                            }
                            
                            //----------------------------------------begin-----add by yaoanguo----2012-09-17--
                            // 对oracle数据库文件单独设定阀值告警处理。
                            
                            NodeUtil nodeUtil = new NodeUtil();
                            NodeDTO node = nodeUtil.conversionToNodeDTO(vo);
                         
                            OracleTableSpaceConfig config = getOracleTableSpaceConfig(node.getNodeid(), sindex);
                            if (config == null) {
                                continue;
                            }
                            alarmIndicatorsNode.setEnabled(config.getMonflag()+"");
                            alarmIndicatorsNode.setLimenvalue0(config.getLimenvalue() + "");
                            alarmIndicatorsNode.setLimenvalue1(config.getLimenvalue1() + "");
                            alarmIndicatorsNode.setLimenvalue2(config.getLimenvalue2() + "");
                            alarmIndicatorsNode.setSms0(config.getSms() + "");
                            alarmIndicatorsNode.setSms1(config.getSms1() + "");
                            alarmIndicatorsNode.setSms2(config.getSms2() + "");
                            checkEvent.checkEvent(oracle, alarmIndicatorsNode, value, sindex);
                        }
                        continue;
                    }else {
                        continue;
                    }
                    checkEvent.checkEvent(oracle, alarmIndicatorsNode, value, sindex);
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Oracle数据库表空间文件利用率信息
     * @param ip
     * @param dbName
     * @param fileName
     * @return
     */
    private OracleTableSpaceConfig getOracleTableSpaceConfig(String nodid, String name){
        OracleTableSpaceConfig config = null;
        OracleTableSpaceDao dao = new OracleTableSpaceDao();
        try {
            config = dao.findByNodeIdAndTableSpaceName(nodid, name);
        } catch (Exception e) {       
            e.printStackTrace();                            
        } finally {                                         
            dao.close();                                    
        }
        return config;
    }
}

