package com.afunms.dataArchiving.model;

import java.io.Serializable;
import java.util.Date;

import com.afunms.common.base.BaseVo;

public class DataArchivingNode extends BaseVo implements Serializable {

    private static final long serialVersionUID = -3438971621258453845L;

    private int id;

    private String dataArchivingId;

    private String dataArchivingType;

    private Date executeStartTime;

    private Date executeEndTime;

    private Date recordTime;

    private String tableName;
    
    private String nodeId;

    private String nodeType;

    private String nodeSubtype;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the dataArchivingId
     */
    public String getDataArchivingId() {
        return dataArchivingId;
    }

    /**
     * @return the dataArchivingType
     */
    public String getDataArchivingType() {
        return dataArchivingType;
    }

    /**
     * @return the executeStartTime
     */
    public Date getExecuteStartTime() {
        return executeStartTime;
    }

    /**
     * @return the executeEndTime
     */
    public Date getExecuteEndTime() {
        return executeEndTime;
    }

    /**
     * @return the recordTime
     */
    public Date getRecordTime() {
        return recordTime;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @return the nodeId
     */
    public String getNodeId() {
        return nodeId;
    }

    /**
     * @return the nodeType
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     * @return the nodeSubtype
     */
    public String getNodeSubtype() {
        return nodeSubtype;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param dataArchivingId the dataArchivingId to set
     */
    public void setDataArchivingId(String dataArchivingId) {
        this.dataArchivingId = dataArchivingId;
    }

    /**
     * @param dataArchivingType the dataArchivingType to set
     */
    public void setDataArchivingType(String dataArchivingType) {
        this.dataArchivingType = dataArchivingType;
    }

    /**
     * @param executeStartTime the executeStartTime to set
     */
    public void setExecuteStartTime(Date executeStartTime) {
        this.executeStartTime = executeStartTime;
    }

    /**
     * @param executeEndTime the executeEndTime to set
     */
    public void setExecuteEndTime(Date executeEndTime) {
        this.executeEndTime = executeEndTime;
    }

    /**
     * @param recordTime the recordTime to set
     */
    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @param nodeId the nodeId to set
     */
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * @param nodeType the nodeType to set
     */
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * @param nodeSubtype the nodeSubtype to set
     */
    public void setNodeSubtype(String nodeSubtype) {
        this.nodeSubtype = nodeSubtype;
    }

}
