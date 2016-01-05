package com.afunms.system.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.afunms.alarm.service.NodeAlarmService;
import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.DBTypeDao;
import com.afunms.application.dao.OraclePartsDao;
import com.afunms.application.model.DBTypeVo;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.OracleEntity;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.util.NodeHelper;

public class DeviceSnapshotService {

    public String[][] getAllInfo(String bids) {
        // 设备快照
        List routeList = new ArrayList(); // 路由器列表
        List switchList = new ArrayList(); // 交换机列表
        List hostList = new ArrayList(); // 服务器列表
        List DBList = new ArrayList(); // 数据库列表
        List secureList = new ArrayList(); // 安全列表

        int routeSize = 0; // 路由器数量
        int switchSize = 0; // 交换机数量
        int hostSize = 0; // 服务器数量
        int DBSize = 0; // 数据库数量
        int middleSize = 0; // 中间件数量
        int serviceSize = 0; // 服务数量
        int secureSize = 0; // 安全数量

        int routeMaxAlarmLevel = 0; // 路由器最高告警等级
        int switchMaxAlarmLevel = 0; // 交换机最高告警等级
        int hostMaxAlarmLevel = 0; // 服务器最高告警等级
        int DBMaxAlarmLevel = 0; // 数据库最高告警等级
        int middleMaxAlarmLevel = 0; // 中间件最高告警等级
        int serviceMaxAlarmLevel = 0; // 服务最高告警等级
        int secureMaxAlarmLevel = 0; // 安全最高告警等级

        String routeSnapStatusImage = ""; // 路由器系统快照状态图片
        String switchSnapStatusImage = ""; // 交换机系统快照状态图片
        String hostSnapStatusImage = ""; // 服务器系统快照状态图片
        String DBSnapStatusImage = ""; // 数据库系统快照状态图片
        String middleSnapStatusImage = ""; // 中间件系统快照状态图片
        String serviceSnapStatusImage = ""; // 服务系统快照状态图片
        String secureSnapStatusImage = ""; // 安全系统快照状态图片

        Date date = new Date();
        HostNodeDao hostNodeDao = new HostNodeDao();
        try {
            routeList = hostNodeDao.loadNetworkByBidAndCategory(1, bids);
            switchList = hostNodeDao.loadNetworkByBidAndCategory(2, bids);
            // hostList = hostNodeDao.loadNetworkByBid(4, bids);
            secureList = hostNodeDao.loadNetworkByBid(8, bids);
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            hostNodeDao.close();
        }

        try {
            DBTypeVo oracleType = null;
            DBTypeDao DBTypeDao = new DBTypeDao();
            try {
                oracleType = (DBTypeVo) DBTypeDao.findByDbtype("Oracle");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                DBTypeDao.close();
            }
            DBDao DBDao = new DBDao();
            try {
                DBList = DBDao.getDbByBID(bids);
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                DBDao.close();
            }
            List oracleList = new ArrayList();
            List otherDBList = new ArrayList();
            for (Object object : DBList) {
                DBVo dbvo = (DBVo) object;
                if (dbvo.getDbtype() == oracleType.getId()) {
                    oracleList.add(dbvo);
                } else {
                    otherDBList.add(dbvo);
                }
            }
            for (int i = 0; i < oracleList.size(); i++) {
                DBVo dbvo = (DBVo) oracleList.get(i);
                List oracles = new ArrayList();
                OraclePartsDao odao = new OraclePartsDao();
                try {
                    oracles = odao.findOracleParts(dbvo.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    odao.close();
                }
                for (int j = 0; j < oracles.size(); j++) {
                    OracleEntity ora = (OracleEntity) oracles.get(j);
                    DBVo nvo = new DBVo();
                    nvo.setAlias(ora.getAlias());
                    nvo.setBid(ora.getBid());
                    nvo.setCategory(dbvo.getCategory());
                    nvo.setCollecttype(ora.getCollectType());
                    nvo.setDbName(ora.getSid());
                    nvo.setDbtype(dbvo.getDbtype());
                    nvo.setId(ora.getId());
                    nvo.setIpAddress(dbvo.getIpAddress());
                    nvo.setManaged(dbvo.getManaged());
                    nvo.setPassword(ora.getPassword());
                    nvo.setPort(dbvo.getPort());
                    nvo.setSendemail(dbvo.getSendemail());
                    nvo.setUser(ora.getUser());
                    otherDBList.add(nvo);
                }
            }
            DBList = otherDBList;
        } catch (Exception e) {

        }

        NodeUtil nodeUtil = new NodeUtil();
        nodeUtil.setBid(bids);

        routeList = nodeUtil.conversionToNodeDTO(routeList);
        switchList = nodeUtil.conversionToNodeDTO(switchList);
        hostList = nodeUtil.conversionToNodeDTO(nodeUtil.getHostList());
        secureList = nodeUtil.conversionToNodeDTO(secureList);
        DBList = nodeUtil.conversionToNodeDTO(DBList);
        List<NodeDTO> middlewareList = nodeUtil.conversionToNodeDTO(nodeUtil
                        .getMiddlewareList());
        List<NodeDTO> serviceList = nodeUtil.conversionToNodeDTO(nodeUtil
                        .getServiceList());

        NodeAlarmService nodeAlarmService = new NodeAlarmService();
        if (routeList != null) {
            routeSize = routeList.size();
            routeMaxAlarmLevel = nodeAlarmService.getMaxAlarmLevel(routeList);
            routeSnapStatusImage = NodeHelper.getSnapStatusImage(
                            routeMaxAlarmLevel, 1);
        }
        if (switchList != null) {
            switchSize = switchList.size();
            switchMaxAlarmLevel = nodeAlarmService.getMaxAlarmLevel(switchList);
            switchSnapStatusImage = NodeHelper.getSnapStatusImage(
                            switchMaxAlarmLevel, 2);
        }
        if (hostList != null) {
            hostSize = hostList.size();
            hostMaxAlarmLevel = nodeAlarmService.getMaxAlarmLevel(hostList);
            hostSnapStatusImage = NodeHelper.getSnapStatusImage(
                            hostMaxAlarmLevel, 3);
        }
        if (DBList != null) {
            DBSize = DBList.size();
            DBMaxAlarmLevel = nodeAlarmService.getMaxAlarmLevel(DBList);
            DBSnapStatusImage = NodeHelper.getSnapStatusImage(DBMaxAlarmLevel,
                            4);
        }
        if (middlewareList != null) {
            middleSize = middlewareList.size();
            middleMaxAlarmLevel = nodeAlarmService
                            .getMaxAlarmLevel(middlewareList);
            middleSnapStatusImage = NodeHelper.getSnapStatusImage(
                            middleMaxAlarmLevel, 5);
        }
        if (serviceList != null) {
            serviceSize = serviceList.size();
            serviceMaxAlarmLevel = nodeAlarmService
                            .getMaxAlarmLevel(serviceList);
            serviceSnapStatusImage = NodeHelper.getSnapStatusImage(
                            serviceMaxAlarmLevel, 6);
        }
        if (secureList != null) {
            secureSize = secureList.size();
            secureMaxAlarmLevel = nodeAlarmService.getMaxAlarmLevel(secureList);
            secureSnapStatusImage = NodeHelper.getSnapStatusImage(
                            secureMaxAlarmLevel, 7);
        }

        String[] routeInfo = { String.valueOf(routeSize), routeSnapStatusImage };
        String[] switchInfo = { String.valueOf(switchSize),
                switchSnapStatusImage };
        String[] hostInfo = { String.valueOf(hostSize), hostSnapStatusImage };
        String[] DBInfo = { String.valueOf(DBSize), DBSnapStatusImage };
        String[] middleInfo = { String.valueOf(middleSize),
                middleSnapStatusImage };
        String[] serviceInfo = { String.valueOf(serviceSize),
                serviceSnapStatusImage };
        String[] secureInfo = { String.valueOf(secureSize),
                secureSnapStatusImage };
        String[][] deviceSnaphotInfo = { routeInfo, switchInfo, hostInfo,
                DBInfo, middleInfo, serviceInfo, secureInfo };
        return deviceSnaphotInfo;
    }
    // public String getRouterInfo() {
    // List routeList = new ArrayList(); // 路由器列表
    // int routeSize = 0; // 路由器数量
    // int routeMaxAlarmLevel = 0; // 路由器最高告警等级
    // String routeSnapStatusImage = ""; // 路由器系统快照状态图片
    // HostNodeDao hostNodeDao = new HostNodeDao();
    // try {
    // routeList = hostNodeDao.loadNetworkByBidAndCategory(1, bids);
    // } catch (Exception e2) {
    // e2.printStackTrace();
    // } finally {
    // hostNodeDao.close();
    // }
    // if (routeList != null) {
    //
    // }
    // }
}
