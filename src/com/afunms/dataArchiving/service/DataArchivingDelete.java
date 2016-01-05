package com.afunms.dataArchiving.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.afunms.common.util.CommonUtil;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.SysLogger;
import com.afunms.dataArchiving.model.DataArchiving;
import com.afunms.dataArchiving.model.DataArchivingNode;
import com.afunms.indicators.model.NodeDTO;

public class DataArchivingDelete {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");

    private static SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat(
                    "yyyy-MM-dd");

    private static String DateTemp = " 00:00:00";

    private static SysLogger logger = SysLogger
                    .getLogger(DataArchivingService.class.getName());

    private boolean isExecuting;

    private DataArchiving isExecutingDataArchiving;

    private DataArchivingService dataArchivingService;

    private int nodeNumber;

    public DataArchivingDelete(DataArchivingService dataArchivingService) {
        setDataArchivingService(dataArchivingService);
        setExecuting(false);
    }

    public boolean execute() {
        if (isExecuting()) {
            return isExecuting();
        }
        setExecuting(true);
        delete();
        return isExecuting();
    }

    public void delete() {
        List<DataArchiving> list = getDataArchivingService()
                        .getAllDataArchiving();
        for (DataArchiving dataArchiving : list) {
            if (validate(dataArchiving)) {
                delete(String.valueOf(dataArchiving.getId()));
            }
        }
        setExecuting(false);
    }

    public void delete(String dataArchivingId) {
        List<NodeDTO> nodeDTOList = getDataArchivingService().getNodeDTOList();
        DataArchiving dataArchiving = getDataArchivingService()
                        .getDataArchivingById(dataArchivingId);
        setIsExecutingDataArchiving(dataArchiving);
        int i = 0;
        for (NodeDTO nodeDTO : nodeDTOList) {
            delete(dataArchiving, nodeDTO);
            i++;
            setNodeNumber(i);
        }
        Date startTime = new Date(dataArchiving.getLastTime().getTime()
                        - dataArchiving.getRetentionTime());
        String startTimeStr = simpleDateFormatDate.format(startTime) + DateTemp;
        try {
            dataArchiving.setStartTime(simpleDateFormat.parse(startTimeStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        getDataArchivingService().updateDataArchiving(dataArchiving);
    }

    public void delete(DataArchiving dataArchiving, NodeDTO nodeDTO) {
        String dataArchivingId = String.valueOf(dataArchiving.getId());
        String dataArchivingType = dataArchiving.getType();
        String nodeId = nodeDTO.getNodeid();
        String nodeType = nodeDTO.getType();
        String nodeSubtype = nodeDTO.getSubtype();
        String ipaddress = nodeDTO.getIpaddress();
        String tableNameIpaddress = CommonUtil.doip(ipaddress);
        Date lastTime = dataArchiving.getLastTime();
        List<DataArchivingNode> dataArchivingNodeList = getDataArchivingService()
                        .getDataArchivingNodeList(dataArchivingId, nodeId,
                                        nodeType, nodeSubtype);
        if (dataArchivingNodeList != null) {
            for (DataArchivingNode dataArchivingNode : dataArchivingNodeList) {
                if (lastTime.after(dataArchivingNode.getRecordTime())) {
                    lastTime = dataArchivingNode.getRecordTime();
                }
            }
        }
        List<DataArchiving> childDataArchivingList = getDataArchivingService()
                        .getChildDataArchiving(
                                        String.valueOf(dataArchiving.getId()));
        if (childDataArchivingList != null) {
            for (DataArchiving childDataArchiving : childDataArchivingList) {
                if (lastTime.after(childDataArchiving.getLastTime())) {
                    lastTime = childDataArchiving.getLastTime();
                }
            }
        }
        lastTime = new Date(lastTime.getTime()
                        - dataArchiving.getRetentionTime());
        DBManager manager = new DBManager();
        try {
            for (String tableNamePer : DataArchivingService.TABLE_NAME_LIST) {
                String tableName = tableNamePer + dataArchivingType
                                + tableNameIpaddress;
                String deleteSQL = "delete from " + tableName
                                + " where COLLECTTIME<='"
                                + simpleDateFormat.format(lastTime) + "'";
                manager.addBatch(deleteSQL);
            }
            manager.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            manager.close();
        }
    }

    public boolean validate(DataArchiving dataArchiving) {
        boolean result = false;
        if (dataArchiving == null || "-1".equals(dataArchiving.getFatherId())) {
            result = false;
        }
        if (dataArchiving.getStartTime().getTime() + dataArchiving.getRetentionTime()
                        < dataArchiving.getLastTime().getTime()) {
            result = true;
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
    
    /**
     * @param args
     */
    public static void main(String[] args) {
    }


}
