/*
 * @(#)NodeAction.java     v1.01, 2013 12 20
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.synchronize.service;

import com.afunms.common.base.BaseVo;
import com.afunms.node.model.NodeDomain;
import com.afunms.node.service.NodeDomainService;
import com.afunms.rmi.service.RMIParameter;
import com.afunms.system.model.User;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.util.TopoHelper;

/**
 * ClassName:   NodeAction.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 20 10:58:03
 */
public class NodeAction implements DataAction {

    /**
     * action:
     *
     * @param parameter
     *
     * @since   v1.01
     * @see com.afunms.synchronize.service.DataAction#action(com.afunms.rmi.service.RMIParameter)
     */
    public void action(RMIParameter parameter) {
        String operation = (String) parameter.getParameter("operation");
        if ("add".equals(operation)) {
            BaseVo baseVo = (BaseVo) parameter.getParameter("baseVo");
            NodeDomain domain = (NodeDomain) parameter.getParameter("nodeDomain");
            add(baseVo, domain);
        } else if ("delete".equals(operation)) {
            BaseVo baseVo = (BaseVo) parameter.getParameter("baseVo");
            User user = (User) parameter.getParameter("user");
            delete(baseVo, user);
        }
    }

    private void add(BaseVo baseVo, NodeDomain domain) {
        NodeDomainService.setNodeDomain(domain);
        if (baseVo instanceof HostNode) {
            HostNode hostNode = (HostNode) baseVo;
            TopoHelper helper = new TopoHelper();
            helper.addHost(hostNode);
        }
    }

    private void delete(BaseVo baseVo, User user) {
        if (baseVo instanceof HostNode) {
            HostNode hostNode = (HostNode) baseVo;
            TopoHelper helper = new TopoHelper();
            helper.delete(hostNode, user);
        }
    }
}

