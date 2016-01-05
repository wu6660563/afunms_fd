/*
 * @(#)CollectTimePersistenceIndicatorValueAction.java     v1.01, 2014 1 15
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.persistence;

import com.afunms.node.indicator.action.PersistenceIndicatorValueAction;

/**
 * ClassName:   CollectTimePersistenceIndicatorValueAction.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 15 14:59:37
 */
public class CollectTimePersistenceIndicatorValueAction extends
                PersistenceIndicatorValueAction {

    private final static String TABLE_NAME_DATA_TEMP = "nms_other_data_temp";

    /**
     * executeToDB:
     *
     *
     * @since   v1.01
     * @see com.afunms.node.indicator.action.PersistenceIndicatorValueAction#executeToDB()
     */
    @Override
    public void executeToDB() {
        String collecttime = (String) getIndicatorValue().getValue();

        // 删除临时数据
        String deleteDataTempSQL = "delete from " + TABLE_NAME_DATA_TEMP + " where nodeid='" + getNodeId() + "' and entity = 'collecttime'";
        executeSQL(deleteDataTempSQL);

        // 保存临时数据
        StringBuffer dataTempSQL = new StringBuffer(200);
        dataTempSQL.append("insert into nms_other_data_temp(nodeid,ip,type,subtype,entity,thevalue,collecttime)values('");
        dataTempSQL.append(getNodeId());
        dataTempSQL.append("','");
        dataTempSQL.append(getIpAddress());
        dataTempSQL.append("','");
        dataTempSQL.append(getNodeType());                  // type
        dataTempSQL.append("','");
        dataTempSQL.append(getNodeSubtype());               // subtype
        dataTempSQL.append("','collecttime','");            // entity
        dataTempSQL.append(collecttime);                    // thevalue数据采集时间
        dataTempSQL.append("','");
        dataTempSQL.append(format(getCalendar()));          // collecttime数据保存时间
        dataTempSQL.append("')");
        executeSQL(dataTempSQL.toString());
    }

}

