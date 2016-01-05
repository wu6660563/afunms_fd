package com.afunms.location.model;

import com.afunms.common.base.BaseVo;

public class FvsdReportLocation extends BaseVo {

    private String id;
    
    private String descr;

    private boolean isDefault;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the descr
     */
    public String getDescr() {
        return descr;
    }

    /**
     * @return the isDefault
     */
    public boolean isDefault() {
        return isDefault;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @param descr the descr to set
     */
    public void setDescr(String descr) {
        this.descr = descr;
    }

    /**
     * @param isDefault the isDefault to set
     */
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
    
}
