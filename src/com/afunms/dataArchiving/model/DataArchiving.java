package com.afunms.dataArchiving.model;

import java.io.Serializable;
import java.util.Date;

import com.afunms.common.base.BaseVo;

public class DataArchiving extends BaseVo implements Serializable {

    private static final long serialVersionUID = 627630555457163098L;

    private int id;

    private String name;

    private String type;

    private long interval;

    private Date startTime;

    private Date lastTime;

    private String fatherId;

    private long retentionTime;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the interval
     */
    public long getInterval() {
        return interval;
    }

    /**
     * @return the startTime
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @return the lastTime
     */
    public Date getLastTime() {
        return lastTime;
    }

    /**
     * @return the fatherId
     */
    public String getFatherId() {
        return fatherId;
    }

    /**
     * @return the retentionTime
     */
    public long getRetentionTime() {
        return retentionTime;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @param interval the interval to set
     */
    public void setInterval(long interval) {
        this.interval = interval;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * @param lastTime the lastTime to set
     */
    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    /**
     * @param fatherId the fatherId to set
     */
    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    /**
     * @param retentionTime the retentionTime to set
     */
    public void setRetentionTime(long retentionTime) {
        this.retentionTime = retentionTime;
    }

}
