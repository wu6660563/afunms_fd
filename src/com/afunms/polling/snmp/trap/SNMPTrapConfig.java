/*
 * @(#)SNMPTrapConfig.java     v1.01, Dec 3, 2012
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.trap;

import java.io.Serializable;

import com.afunms.common.base.BaseVo;

/**
 * ClassName:   SNMPTrapConfig.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        Dec 3, 2012 3:08:10 PM
 */
public class SNMPTrapConfig extends BaseVo implements Serializable {

    /**
     * serialVersionUID:
     * <p>序列化 Id
     *
     * @since   v1.01
     */
    private static final long serialVersionUID = 9113779966557686712L;

    /**
     * id:
     * <p>
     *
     * @since   v1.01
     */
    private int id;

    /**
     * enable:
     * <p>是否启用
     *
     * @since   v1.01
     */
    private boolean enable;

    /**
     * port:
     * <p>端口号
     *
     * @since   v1.01
     */
    private int port;

    /**
     * handler:
     * <p>处理类
     *
     * @since   v1.01
     */
    private SNMPTrapHandler handler;

    /**
     * description:
     * <p>描述
     *
     * @since   v1.01
     */
    private String description;

    /**
     * getId:
     * <p>获取Id
     *
     * @return  {@link Integer}
     *          - Id
     * @since   v1.01
     */
    public int getId() {
        return id;
    }

    /**
     * setId:
     * <p>设置Id
     *
     * @param   id
     *          - id
     * @since   v1.01
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * isEnable:
     * <p>是否启动
     *
     * @return  {@link Boolean}
     *          - 如果启用则返回 <code>true</code> ，否者返回 <code>false</code>
     * @since   v1.01
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * setEnable:
     * <p>设置启动
     *
     * @param   enable
     *          - 如果启用则设置为 <code>true</code>
     * @since   v1.01
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * getPort:
     * <p>获取端口
     *
     * @return  {@link Integer}
     *          - 端口
     * @since   v1.01
     */
    public int getPort() {
        return port;
    }

    /**
     * setPort:
     * <p>设置端口
     *
     * @param   port
     *          - 端口
     * @since   v1.01
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * getHandler:
     * <p>获取处理类
     *
     * @return  {@link SNMPTrapHandler}
     *          - SNMP Trap 处理类
     * @since   v1.01
     */
    public SNMPTrapHandler getHandler() {
        return handler;
    }

    /**
     * setHandler:
     * <p>设置处理类
     *
     * @param   handler
     *          - SNMP Trap 处理类
     * @since   v1.01
     */
    public void setHandler(SNMPTrapHandler handler) {
        this.handler = handler;
    }

    /**
     * getDescription:
     * <p>获取描述
     *
     * @return  {@link String}
     *          - 描述
     * @since   v1.01
     */
    public String getDescription() {
        return description;
    }

    /**
     * setDescription:
     * <p>设置描述
     *
     * @param   description
     *          - 描述
     * @since   v1.01
     */
    public void setDescription(String description) {
        this.description = description;
    }

}

