/*
 * @(#)NodeDomainManager.java     v1.01, 2013 10 23
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.manage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.ManagerInterface;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.Constant;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.location.model.FvsdReportLocation;
import com.afunms.location.model.FvsdReportLocationNode;
import com.afunms.location.service.FvsdReportLocationService;
import com.afunms.node.model.Domain;
import com.afunms.node.model.NodeDomain;
import com.afunms.node.service.NodeDomainService;

/**
 * ClassName:   NodeDomainManager.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 10 23 10:27:10
 */
public class NodeDomainManager extends BaseManager implements ManagerInterface {

    /**
     * execute:
     *
     * @param action
     * @return
     *
     * @since   v1.01
     * @see com.afunms.common.base.ManagerInterface#execute(java.lang.String)
     */
    public String execute(String action) {
        if ("list".equals(action)) {
            return list();
        } else if ("choose".equals(action)) {
            return choose();
        }
        return null;
    }

    public String list() {
        String type = Constant.ALL_TYPE;
        String subtype = Constant.ALL_SUBTYPE;
        NodeUtil nodeUtil = new NodeUtil();
        nodeUtil.setBid(getBid());
        List<BaseVo> baseVolist =  nodeUtil.getNodeByTyeAndSubtype(type, subtype);
        List<NodeDTO> list = nodeUtil.conversionToNodeDTO(baseVolist);
        NodeDomainService service = new NodeDomainService();
        List<NodeDomain> nodeDomainlist = service.getAllNodeDomain();
        Hashtable<String, NodeDomain> nodeDomainHashtable = new Hashtable<String, NodeDomain>();
        for (NodeDomain nodeDomain : nodeDomainlist) {
            nodeDomainHashtable.put(nodeDomain.getNodeId() + ":" + nodeDomain.getNodeType() + ":" + nodeDomain.getNodeSubtype(), nodeDomain);
        }
        request.setAttribute("list", list);
        request.setAttribute("nodeDomainHashtable", nodeDomainHashtable);
        return "/topology/domain/list.jsp";
    }

    public String choose() {
        request.setAttribute("list", NodeDomainService.getDomain());
        return "/topology/domain/choose.jsp";
    }

    public List<NodeDomain> update(String domainId, NodeDomain[] nodeDomains) {
        List<NodeDomain> addList = new ArrayList<NodeDomain>();
        List<NodeDomain> updateList = new ArrayList<NodeDomain>();
        List<NodeDomain> allList = new ArrayList<NodeDomain>();
        if (nodeDomains != null) {
            for (NodeDomain nodeDomain : nodeDomains) {
                nodeDomain.setDomain(domainId);
                Domain domain = NodeDomainService.getDomain(domainId);
                nodeDomain.setDomainDescr(domain.getDescr());
                allList.add(nodeDomain);
                if (-1 == nodeDomain.getId()) {
                    addList.add(nodeDomain);
                } else {
                    updateList.add(nodeDomain);
                }
            }
        }
        NodeDomainService service = new NodeDomainService();
        for (NodeDomain nodeDomain : updateList) {
            service.delete(nodeDomain.getNodeId(), nodeDomain.getNodeType(), nodeDomain.getNodeSubtype());
        }
        for (NodeDomain nodeDomain : allList) {
            service.add(nodeDomain);
        }
        return allList;
    }
}

