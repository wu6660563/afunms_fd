package com.afunms.dataArchiving.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.afunms.common.util.CommonUtil;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.SysLogger;
import com.afunms.dataArchiving.dao.DataArchivingHistoryDao;
import com.afunms.dataArchiving.dao.DataArchivingNodeDao;
import com.afunms.dataArchiving.model.DataArchiving;
import com.afunms.dataArchiving.model.DataArchivingHistory;
import com.afunms.dataArchiving.model.DataArchivingNode;
import com.afunms.indicators.model.NodeDTO;

public class DataArchivingCalculate {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");

    private static SysLogger logger = SysLogger
                    .getLogger(DataArchivingService.class.getName());

    private boolean isExecuting;

    private DataArchiving isExecutingDataArchiving;

    private DataArchivingService dataArchivingService;

    private int nodeNumber;

    public DataArchivingCalculate(DataArchivingService dataArchivingService) {
        setDataArchivingService(dataArchivingService);
    }

    public boolean execute() {
        if (isExecuting()) {
            return isExecuting();
        }
        setExecuting(true);
        calculate();
        return isExecuting();
    }

    public void calculate() {
        List<DataArchiving> list = getDataArchivingService()
                        .getAllDataArchiving();
        for (DataArchiving dataArchiving : list) {
            calculate(String.valueOf(dataArchiving.getId()));
        }
        setExecuting(false);
    }

    public void calculate(String dataArchivingId) {
        boolean result = false;
        while (true) {
            DataArchiving dataArchiving = getDataArchivingService()
                            .getDataArchivingById(dataArchivingId);
            isExecutingDataArchiving = dataArchiving;
            if (dataArchiving == null) {
                break;
            }
            String fatherId = dataArchiving.getFatherId();
            DataArchiving fatherDataArchiving = getDataArchivingService()
                            .getDataArchivingById(fatherId);
            Date currTime = new Date();
            Date startTime = dataArchiving.getStartTime();
            long interval = dataArchiving.getInterval();
            if (startTime == null) {
                startTime = new Date();
            }
            Date lastTime = dataArchiving.getLastTime();
            if (lastTime == null) {
                lastTime = startTime;
            }
            logger.info("��ʼִ�� " + dataArchiving.getName() + " �ϴθ���ʱ�䣺"
                            + simpleDateFormat.format(lastTime));
            if (currTime.getTime() - lastTime.getTime() < interval) {
                // �������һ�ι鵵ʱ����뵱ǰʱ��С�ڹ鵵����򲻽��й鵵
                logger.info(dataArchiving.getName() + " �ϴθ���ʱ�䣺"
                                + simpleDateFormat.format(lastTime)
                                + " С�ڵ�ǰʱ�������ʱ���ʱ�������˳��鵵");
                break;
            }
            if (fatherDataArchiving == null) {
                // ����������Ĺ鵵Ϊ�� �򲻱ؽ��й鵵
                logger.info(dataArchiving.getName() + " �������Ĺ鵵Ϊ�� �����й鵵");
                dataArchiving.setLastTime(new Date());
                getDataArchivingService().updateDataArchiving(dataArchiving);
                break;
            }
            Date fatherLastTime = fatherDataArchiving.getLastTime();
            if (fatherLastTime.getTime() - lastTime.getTime() < interval) {
                // �������һ�ι鵵ʱ�����������������һ�ι鵵ʱ��С�ڹ鵵���Ҳ�����й鵵
                logger.info(dataArchiving.getName() + " �����������Ĺ鵵"
                                + fatherDataArchiving.getLastTime()
                                + "ʱ��С�ڼ��ʱ��" + interval);
                break;
            }
            List<NodeDTO> nodeDTOList = getDataArchivingService()
                            .getNodeDTOList();
            Date executeStartTime = new Date();
            int i = 0;
            for (NodeDTO nodeDTO : nodeDTOList) {
                // ѭ����Ҫ�鵵��ÿ���豸
                if (nodeDTO == null) {
                    continue;
                }
                result = archiveData(dataArchiving, nodeDTO,
                                fatherDataArchiving);
                i ++;
                setNodeNumber(i);
                if (!result) {
                    break;
                }
            }
            if (result) {
                Date nextTime = new Date(lastTime.getTime()
                                + dataArchiving.getInterval());
                DataArchivingHistory dataArchivingHistory = new DataArchivingHistory();
                dataArchivingHistory.setExecuteStartTime(executeStartTime);
                dataArchivingHistory.setDataArchivingType(dataArchiving
                                .getType());
                dataArchivingHistory.setExecuteEndTime(new Date());
                dataArchivingHistory.setRecordTime(lastTime);
                dataArchivingHistory.setDataArchivingId(String
                                .valueOf(dataArchiving.getId()));
                DataArchivingHistoryDao dataArchivingHistoryDao = new DataArchivingHistoryDao();
                try {
                    dataArchivingHistoryDao.save(dataArchivingHistory);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    dataArchivingHistoryDao.close();
                }
                dataArchiving.setLastTime(nextTime);
                getDataArchivingService().updateDataArchiving(
                                dataArchiving);
                logger.info("��ǰ����ִ�е� " + dataArchiving.getName() + " �����ݹ鵵��"
                                + dataArchiving.getLastTime()
                                + "ʱ����ִ����ɣ���ʼִ����һ���鵵");
            }
        }
    }

    public boolean archiveData(DataArchiving dataArchiving, NodeDTO nodeDTO,
                    DataArchiving fatherDataArchiving) {
        boolean result = false;
        if (dataArchiving == null || nodeDTO == null) {
            return false;
        }
        String nodeId = nodeDTO.getNodeid();
        String nodeType = nodeDTO.getType();
        String nodeSubtype = nodeDTO.getSubtype();
        String dataArchivingId = String.valueOf(dataArchiving.getId());
        String dataArchivingType = dataArchiving.getType();
        String ipaddress = nodeDTO.getIpaddress();
        String tableNameIpaddress = CommonUtil.doip(ipaddress);
        Date lastTime = dataArchiving.getLastTime();
        Date nextTime = new Date(lastTime.getTime()
                        + dataArchiving.getInterval());
        List<DataArchivingNode> dataArchivingNodeList = getDataArchivingService()
                        .getDataArchivingNodeList(dataArchivingId, nodeId,
                                        nodeType, nodeSubtype);
        if (dataArchivingNodeList != null) {
            for (DataArchivingNode dataArchivingNode : dataArchivingNodeList) {
                if (dataArchivingNode.getRecordTime().getTime() >= dataArchiving
                                .getLastTime().getTime()) {
                    logger.info("�豸 "
                                    + nodeDTO.getIpaddress()
                                    + " �Ѿ����� "
                                    + simpleDateFormat.format(dataArchivingNode
                                                    .getRecordTime())
                                    + " ʱ��Ĺ鵵");
                    return true;
                }
            }
        }
        List<String> tableNameList = DataArchivingService.TABLE_NAME_LIST;
        Date executeStartTime = new Date();
        DBManager manager = new DBManager();
        try {
            for (String tableNamePer : tableNameList) {
                String tableName = tableNamePer + dataArchivingType
                                + tableNameIpaddress;
                String fatherTableName = tableNamePer
                                + fatherDataArchiving.getType()
                                + tableNameIpaddress;
                String insertSql = "insert into "
                                + tableName
                                + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) "
                                + "SELECT hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count,FORMAT(avg(hostcollectdata.thevalue),2),min(hostcollectdata.collecttime) "
                                + "FROM "
                                + fatherTableName
                                + " hostcollectdata where COLLECTTIME>='"
                                + simpleDateFormat.format(lastTime)
                                + "' and COLLECTTIME<='"
                                + simpleDateFormat.format(nextTime)
                                + "' group by hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count";
                manager.addBatch(insertSql);
            }
            result = manager.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            manager.close();
        }
        Date executeEndTime = new Date();
        if (result) {
            if (dataArchivingNodeList == null
                            || dataArchivingNodeList.size() == 0) {
                DataArchivingNode dataArchivingNode = new DataArchivingNode();
                dataArchivingNode.setExecuteStartTime(executeStartTime);
                dataArchivingNode.setDataArchivingType(dataArchiving.getType());
                dataArchivingNode.setExecuteEndTime(executeEndTime);
                dataArchivingNode.setRecordTime(lastTime);
                dataArchivingNode.setDataArchivingId(String
                                .valueOf(dataArchiving.getId()));
                dataArchivingNode.setNodeId(nodeDTO.getNodeid());
                dataArchivingNode.setNodeType(nodeDTO.getType());
                dataArchivingNode.setNodeSubtype(nodeDTO.getSubtype());
                DataArchivingNodeDao dataArchivingNodeDao = new DataArchivingNodeDao();
                try {
                    dataArchivingNodeDao.save(dataArchivingNode);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    dataArchivingNodeDao.close();
                }
            } else {
                for (DataArchivingNode dataArchivingNode : dataArchivingNodeList) {
                    dataArchivingNode.setExecuteStartTime(executeStartTime);
                    dataArchivingNode.setDataArchivingType(dataArchiving
                                    .getType());
                    dataArchivingNode.setExecuteEndTime(executeEndTime);
                    dataArchivingNode.setRecordTime(lastTime);
                    dataArchivingNode.setDataArchivingId(String
                                    .valueOf(dataArchiving.getId()));
                    dataArchivingNode.setNodeId(nodeDTO.getNodeid());
                    dataArchivingNode.setNodeType(nodeDTO.getType());
                    dataArchivingNode.setNodeSubtype(nodeDTO.getSubtype());
                    DataArchivingNodeDao dataArchivingNodeDao = new DataArchivingNodeDao();
                    try {
                        dataArchivingNodeDao.update(dataArchivingNode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        dataArchivingNodeDao.close();
                    }
                }
            }
        }
        return result;
    }

    /**
     * @return the isExecuting
     */
    public boolean isExecuting() {
        return isExecuting;
    }

    /**
     * @param isExecuting
     *            the isExecuting to set
     */
    public void setExecuting(boolean isExecuting) {
        this.isExecuting = isExecuting;
    }

    /**
     * @return the isExecutingDataArchiving
     */
    public DataArchiving getIsExecutingDataArchiving() {
        return isExecutingDataArchiving;
    }

    /**
     * @param isExecutingDataArchiving the isExecutingDataArchiving to set
     */
    public void setIsExecutingDataArchiving(DataArchiving isExecutingDataArchiving) {
        this.isExecutingDataArchiving = isExecutingDataArchiving;
    }

    /**
     * @return the dataArchivingService
     */
    public DataArchivingService getDataArchivingService() {
        return dataArchivingService;
    }

    /**
     * @param dataArchivingService
     *            the dataArchivingService to set
     */
    public void setDataArchivingService(
                    DataArchivingService dataArchivingService) {
        this.dataArchivingService = dataArchivingService;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
    }

    /**
     * @return the nodeNumber
     */
    public int getNodeNumber() {
        return nodeNumber;
    }

    /**
     * @param nodeNumber the nodeNumber to set
     */
    public void setNodeNumber(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

}
