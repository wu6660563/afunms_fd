package com.afunms.location.model;

import com.afunms.common.base.BaseVo;

public class FvsdReportLocationNode extends BaseVo {

    private String id;

    private String nodeid;
    
    private String type;
    
    private String subtype;
    
    private String fvsdReportLocationId;

    private String descr;
    /**
     * @return the descr
     */
    public String getDescr() {
        return descr;
    }

    /**
     * @param descr the descr to set
     */
    public void setDescr(String descr) {
        this.descr = descr;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the nodeid
     */
    public String getNodeid() {
        return nodeid;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the subtype
     */
    public String getSubtype() {
        return subtype;
    }

    /**
     * @return the fvsdReportLocationId
     */
    public String getFvsdReportLocationId() {
        return fvsdReportLocationId;
    }

    /**
     * @param nodeid the nodeid to set
     */
    public void setNodeid(String nodeid) {
        this.nodeid = nodeid;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @param subtype the subtype to set
     */
    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    /**
     * @param fvsdReportLocationId the fvsdReportLocationId to set
     */
    public void setFvsdReportLocationId(String fvsdReportLocationId) {
        this.fvsdReportLocationId = fvsdReportLocationId;
    }

    
}
