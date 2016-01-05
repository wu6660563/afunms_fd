package com.afunms.dataArchiving.model;

import java.io.Serializable;
import java.util.Date;

import com.afunms.common.base.BaseVo;

public class DataArchivingHistory extends BaseVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3438971621258453845L;

    private int id;

    private String dataArchivingId;

    private String dataArchivingType;

    private Date executeStartTime;

    private Date executeEndTime;

    private Date recordTime;

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

}
