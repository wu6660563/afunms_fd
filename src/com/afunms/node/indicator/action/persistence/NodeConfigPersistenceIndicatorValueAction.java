/*
 * @(#)NodeConfigPersistenceIndicatorValueAction.java     v1.01, 2014 1 15
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.persistence;

import com.afunms.config.model.Nodeconfig;
import com.afunms.node.indicator.action.PersistenceIndicatorValueAction;

/**
 * ClassName:   NodeConfigPersistenceIndicatorValueAction.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 15 11:14:33
 */
public class NodeConfigPersistenceIndicatorValueAction extends
                PersistenceIndicatorValueAction {

    private final static String TABLE_NAME_DATA_TEMP = "nms_nodeconfig";

    /**
     * executeToDB:
     *
     *
     * @since   v1.01
     * @see com.afunms.node.indicator.action.PersistenceIndicatorValueAction#executeToDB()
     */
    @Override
    public void executeToDB() {
        Nodeconfig nodeconfig = (Nodeconfig) getIndicatorValue().getValue();

        // 删除临时数据
        String deleteDataTempSQL = "delete from " + TABLE_NAME_DATA_TEMP + " where nodeid='" + getNodeId() + "'";
        executeSQL(deleteDataTempSQL);

        // 保存临时数据
        StringBuffer dataTempSQL = new StringBuffer(200);
        dataTempSQL.append("insert into " + TABLE_NAME_DATA_TEMP + "(nodeid,hostname,sysname,serialNumber,cSDVersion,numberOfProcessors,mac)"
                + "values('");
        dataTempSQL.append(getNodeId());
        dataTempSQL.append("','");
        dataTempSQL.append(nodeconfig.getHostname());// hostname
        dataTempSQL.append("','");
        dataTempSQL.append(nodeconfig.getSysname());// sysname
        dataTempSQL.append("','");
        dataTempSQL.append(nodeconfig.getSerialNumber());// serialNumber
        dataTempSQL.append("','");
        dataTempSQL.append(nodeconfig.getCSDVersion());// cSDVersion
        dataTempSQL.append("','");
        dataTempSQL.append(nodeconfig.getNumberOfProcessors());// numberOfProcessors
        dataTempSQL.append("','");
        dataTempSQL.append(nodeconfig.getMac());// mac
        dataTempSQL.append("')");
        
        executeSQL(dataTempSQL.toString());
    }

}

