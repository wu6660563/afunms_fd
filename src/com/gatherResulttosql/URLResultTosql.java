/*
 * @(#)URLResultTosql.java     v1.01, May 31, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.gatherResulttosql;

import java.util.Hashtable;

import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.node.model.URLPerformanceInfo;
import com.afunms.node.service.URLPerformanceInfoService;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.Tomcat;
import com.afunms.polling.node.URLConfigNode;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

/**
 * ClassName:   URLResultTosql.java
 * <p>{@link URLResultTosql} URL 数据入库
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        May 31, 2013 11:30:44 AM
 */
public class URLResultTosql implements ResultTosql {

    /**
     * CreateResultTosql:
     * <p>把采集结果生成sql
     *
     * @param   dataresult
     *          - 数据结果列表
     * @param   node
     *          - 节点
     *
     * @since   v1.01
     */
    public void CreateResultTosql(Hashtable dataresult, Node node) {
        DBAttribute attribute = new DBAttribute();
        attribute.setAttribute("dataresult", dataresult);
        attribute.setAttribute("node", node);
        ResultToDB resultToDB = new ResultToDB();
        resultToDB.setResultTosql(this);
        resultToDB.setAttribute(attribute);
        GathersqlListManager.getInstance().addToQueue(resultToDB);
    }

    /**
     * executeResultToDB:
     * <p>执行结果至数据库中
     *
     * @param   attribute
     *          - 数据结果
     *
     * @since   v1.01
     * @see com.gatherdb.ResultTosql#executeResultToDB(com.gatherdb.DBAttribute)
     */
    public void executeResultToDB(DBAttribute attribute) {
        Hashtable dataresult = (Hashtable) attribute.getAttribute("dataresult");
        URLConfigNode node = (URLConfigNode) attribute.getAttribute("node");
        NodeUtil nodeUtil = new NodeUtil();
        NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);
        if (dataresult != null && dataresult.size() > 0) {
            URLPerformanceInfo info = (URLPerformanceInfo) dataresult.get("info");
            if (info != null) {
                URLPerformanceInfoService service = new URLPerformanceInfoService();
                service.save(nodeDTO.getNodeid(), info);
            }
        }
    }
}

