package com.afunms.config.model;

import java.io.Serializable;
import java.sql.Clob;

import com.afunms.common.base.BaseVo;

public class Portconfig extends BaseVo implements Serializable {

    private Integer id;

    private String ipaddress;

    private Integer portindex;

    private String name;

    private String linkuse;

    private Integer sms;

    private String bak;

    private Integer reportflag;

    private String inportalarm;

    private String outportalarm;

    private String speed;

    private int important;

    public Portconfig(Integer id, String ipaddress, Integer portindex,
            String name, String linkuse, Integer sms, String bak,
            Integer reportflag, String inportalarm, String outportalarm) {
        this.id = id;
        this.ipaddress = ipaddress;
        this.portindex = portindex;
        this.name = name;
        this.linkuse = linkuse;
        this.sms = sms;
        this.bak = bak;
        this.reportflag = reportflag;
        this.inportalarm = inportalarm;
        this.outportalarm = outportalarm;
    }

    public Portconfig(Integer id, String ipaddress, Integer portindex,
            String name, String linkuse, Integer sms, String bak,
            Integer reportflag, String inportalarm, String outportalarm,
            String speed) {
        this.id = id;
        this.ipaddress = ipaddress;
        this.portindex = portindex;
        this.name = name;
        this.linkuse = linkuse;
        this.sms = sms;
        this.bak = bak;
        this.reportflag = reportflag;
        this.inportalarm = inportalarm;
        this.outportalarm = outportalarm;
        this.speed = speed;
    }

    public Portconfig() {
    }

    public Integer getReportflag() {
        return reportflag;
    }

    public void setReportflag(Integer reportflag) {
        this.reportflag = reportflag;
    }

    public Integer getId() {
        return id;
    }

    public Integer getPortindex() {
        return portindex;
    }

    public String getBak() {
        return bak;
    }

    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public String getLinkuse() {
        return linkuse;
    }

    /**
     * @return
     */
    public Integer getSms() {
        return sms;
    }

    /**
     * @return
     */
    public String getIpaddress() {
        return ipaddress;
    }

    /**
     * @param string
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @param serializable
     */
    public void setBak(String serializable) {
        bak = serializable;
    }

    /**
     * @param calendar
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * @param string
     */
    public void setPortindex(Integer string) {
        portindex = string;
    }

    /**
     * @param string
     */
    public void setLinkuse(String string) {
        linkuse = string;
    }

    /**
     * @param string
     */
    public void setSms(Integer string) {
        sms = string;
    }

    /**
     * @param string
     */
    public void setIpaddress(String string) {
        ipaddress = string;
    }

    public String getInportalarm() {
        return inportalarm;
    }

    public void setInportalarm(String inportalarm) {
        this.inportalarm = inportalarm;
    }

    public String getOutportalarm() {
        return outportalarm;
    }

    public void setOutportalarm(String outportalarm) {
        this.outportalarm = outportalarm;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public int getImportant() {
        return important;
    }

    public void setImportant(int important) {
        this.important = important;
    }
}
