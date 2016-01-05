/*
 * @(#)NodeDomain.java     v1.01, 2013 9 28
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.model;

import com.afunms.common.base.BaseVo;

/**
 * ClassName:   NodeDomain.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 9 28 22:07:26
 */
public class NodeDomain extends BaseVo {

    /**
     * serialVersionUID:
     * <p>序列化 id
     *
     * @since   v1.01
     */
    private static final long serialVersionUID = -6395120857589012552L;

    private int id;

    /**
     * nodeId:
     * <p>设备 id
     *
     * @since   v1.01
     */
    private String nodeId;

    /**
     * nodeType:
     * <p>设备类型
     *
     * @since   v1.01
     */
    private String nodeType;

    /**
     * nodeSubtype:
     * <p>设备子类型
     *
     * @since   v1.01
     */
    private String nodeSubtype;

    /**
     * domain:
     * <p>区域
     *
     * @since   v1.01
     */
    private String domain;

    /**
     * dominDescr:
     * <p>区域描述
     *
     * @since   v1.01
     */
    private String domainDescr;

    /**
     * getId:
     * <p>
     *
     * @return  int
     *          -
     * @since   v1.01
     */
    public int getId() {
        return id;
    }

    /**
     * setId:
     * <p>
     *
     * @param   id
     *          -
     * @since   v1.01
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * getNodeId:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getNodeId() {
        return nodeId;
    }

    /**
     * setNodeId:
     * <p>
     *
     * @param   nodeId
     *          -
     * @since   v1.01
     */
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * getNodeType:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     * setNodeType:
     * <p>
     *
     * @param   nodeType
     *          -
     * @since   v1.01
     */
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * getNodeSubtype:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getNodeSubtype() {
        return nodeSubtype;
    }

    /**
     * setNodeSubtype:
     * <p>
     *
     * @param   nodeSubtype
     *          -
     * @since   v1.01
     */
    public void setNodeSubtype(String nodeSubtype) {
        this.nodeSubtype = nodeSubtype;
    }

    /**
     * getDomain:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getDomain() {
        return domain;
    }

    /**
     * setDomain:
     * <p>
     *
     * @param   domain
     *          -
     * @since   v1.01
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * getDomainDescr:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getDomainDescr() {
        return domainDescr;
    }

    /**
     * setDomainDescr:
     * <p>
     *
     * @param   domainDescr
     *          -
     * @since   v1.01
     */
    public void setDomainDescr(String domainDescr) {
        this.domainDescr = domainDescr;
    }

}

