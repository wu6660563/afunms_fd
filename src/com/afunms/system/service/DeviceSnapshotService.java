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
        // �豸����
        List routeList = new ArrayList(); // ·�����б�
        List switchList = new ArrayList(); // �������б�
        List hostList = new ArrayList(); // �������б�
        List DBList = new ArrayList(); // ���ݿ��б�
        List secureList = new ArrayList(); // ��ȫ�б�

        int routeSize = 0; // ·��������
        int switchSize = 0; // ����������
        int hostSize = 0; // ����������
        int DBSize = 0; // ���ݿ�����
        int middleSize = 0; // �м������
        int serviceSize = 0; // ��������
        int secureSize = 0; // ��ȫ����

        int routeMaxAlarmLevel = 0; // ·������߸澯�ȼ�
        int switchMaxAlarmLevel = 0; // ��������߸澯�ȼ�
        int hostMaxAlarmLevel = 0; // ��������߸澯�ȼ�
        int DBMaxAlarmLevel = 0; // ���ݿ���߸澯�ȼ�
        int middleMaxAlarmLevel = 0; // �м����߸澯�ȼ�
        int serviceMaxAlarmLevel = 0; // ������߸澯�ȼ�
        int secureMaxAlarmLevel = 0; // ��ȫ��߸澯�ȼ�

        String routeSnapStatusImage = ""; // ·����ϵͳ����״̬ͼƬ
        String switchSnapStatusImage = ""; // ������ϵͳ����״̬ͼƬ
        String hostSnapStatusImage = ""; // ������ϵͳ����״̬ͼƬ
        String DBSnapStatusImage = ""; // ���ݿ�ϵͳ����״̬ͼƬ
        String middleSnapStatusImage = ""; // �м��ϵͳ����״̬ͼƬ
        String serviceSnapStatusImage = ""; // ����ϵͳ����״̬ͼƬ
        String secureSnapStatusImage = ""; // ��ȫϵͳ����״̬ͼƬ

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
    // List routeList = new ArrayList(); // ·�����б�
    // int routeSize = 0; // ·��������
    // int routeMaxAlarmLevel = 0; // ·������߸澯�ȼ�
    // String routeSnapStatusImage = ""; // ·����ϵͳ����״̬ͼƬ
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
