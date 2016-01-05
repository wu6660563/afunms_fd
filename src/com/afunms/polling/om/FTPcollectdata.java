package com.afunms.polling.om;

import java.io.Serializable;

import java.util.Calendar;

/**
 * ClassName:   FTPcollectdata.java
 * <p>{@link FTPcollectdata} <code>FTP</code> 采集后的数据
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        Jan 4, 2013 10:04:53 AM
 */
public class FTPcollectdata implements Serializable {

    /**
     * serialVersionUID:
     * <p>序列化 ID
     *
     * @since   v1.01
     */
    private static final long serialVersionUID = -7599859559902009025L;

    /**
     * id:
     * <p>id
     *
     * @since   v1.01
     */
    private int id;

    /**
     * category:
     * <p>类别
     *
     * @since   v1.01
     */
    private String category;

    /**
     * thevalue:
     * <p>值
     *
     * @since   v1.01
     */
    private String thevalue;

    /**
     * collecttime:
     * <p>采集时间
     *
     * @since   v1.01
     */
    private Calendar collecttime;

    /**
     * getId:
     * <p>获取 id
     *
     * @return  {@link Integer}
     *          - id
     * @since   v1.01
     */
    public int getId() {
        return id;
    }

    /**
     * setId:
     * <p>设置 id
     *
     * @param   id
     *          - id
     * @since   v1.01
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * getCategory:
     * <p>获取类别
     *
     * @return  {@link String}
     *          - 类别
     * @since   v1.01
     */
    public String getCategory() {
        return category;
    }

    /**
     * setCategory:
     * <p>设置类别
     *
     * @param   category
     *          - 类别
     * @since   v1.01
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * getThevalue:
     * <p>获取值
     *
     * @return  {@link String}
     *          - 值
     * @since   v1.01
     */
    public String getThevalue() {
        return thevalue;
    }

    /**
     * setThevalue:
     * <p>设置值
     *
     * @param   thevalue
     *          - 值
     * @since   v1.01
     */
    public void setThevalue(String thevalue) {
        this.thevalue = thevalue;
    }

    /**
     * getCollecttime:
     * <p>获取采集时间
     *
     * @return  {@link Calendar}
     *          - 采集时间
     * @since   v1.01
     */
    public Calendar getCollecttime() {
        return collecttime;
    }

    /**
     * setCollecttime:
     * <p>设置采集时间
     *
     * @param   collecttime
     *          - 采集时间
     * @since   v1.01
     */
    public void setCollecttime(Calendar collecttime) {
        this.collecttime = collecttime;
    }

    


}