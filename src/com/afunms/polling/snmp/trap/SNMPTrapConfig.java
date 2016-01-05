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
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        Dec 3, 2012 3:08:10 PM
 */
public class SNMPTrapConfig extends BaseVo implements Serializable {

    /**
     * serialVersionUID:
     * <p>���л� Id
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
     * <p>�Ƿ�����
     *
     * @since   v1.01
     */
    private boolean enable;

    /**
     * port:
     * <p>�˿ں�
     *
     * @since   v1.01
     */
    private int port;

    /**
     * handler:
     * <p>������
     *
     * @since   v1.01
     */
    private SNMPTrapHandler handler;

    /**
     * description:
     * <p>����
     *
     * @since   v1.01
     */
    private String description;

    /**
     * getId:
     * <p>��ȡId
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
     * <p>����Id
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
     * <p>�Ƿ�����
     *
     * @return  {@link Boolean}
     *          - ��������򷵻� <code>true</code> �����߷��� <code>false</code>
     * @since   v1.01
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * setEnable:
     * <p>��������
     *
     * @param   enable
     *          - �������������Ϊ <code>true</code>
     * @since   v1.01
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * getPort:
     * <p>��ȡ�˿�
     *
     * @return  {@link Integer}
     *          - �˿�
     * @since   v1.01
     */
    public int getPort() {
        return port;
    }

    /**
     * setPort:
     * <p>���ö˿�
     *
     * @param   port
     *          - �˿�
     * @since   v1.01
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * getHandler:
     * <p>��ȡ������
     *
     * @return  {@link SNMPTrapHandler}
     *          - SNMP Trap ������
     * @since   v1.01
     */
    public SNMPTrapHandler getHandler() {
        return handler;
    }

    /**
     * setHandler:
     * <p>���ô�����
     *
     * @param   handler
     *          - SNMP Trap ������
     * @since   v1.01
     */
    public void setHandler(SNMPTrapHandler handler) {
        this.handler = handler;
    }

    /**
     * getDescription:
     * <p>��ȡ����
     *
     * @return  {@link String}
     *          - ����
     * @since   v1.01
     */
    public String getDescription() {
        return description;
    }

    /**
     * setDescription:
     * <p>��������
     *
     * @param   description
     *          - ����
     * @since   v1.01
     */
    public void setDescription(String description) {
        this.description = description;
    }

}

