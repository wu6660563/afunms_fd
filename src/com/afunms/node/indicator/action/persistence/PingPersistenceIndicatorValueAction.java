/*
 * @(#)PingPersistenceIndicatorValueAction.java     v1.01, 2014 1 2
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.afunms.node.indicator.action.PersistenceIndicatorValueAction;
import com.afunms.polling.om.Pingcollectdata;

/**
 * ClassName:   PingPersistenceIndicatorValueAction.java
 * <p>{@link PingPersistenceIndicatorValueAction} Ping 指标的持久化处理
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 2 22:55:48
 */
public class PingPersistenceIndicatorValueAction extends PersistenceIndicatorValueAction {

    private final static String TABLE_NAME_PERSISTENCE =  "ping";

    private final static String TABLE_NAME_DATA_TEMP = "nms_ping_data_temp";

    /**
     * executeToDB:
     *
     *
     * @since   v1.01
     * @see com.afunms.node.indicator.action.PersistenceIndicatorValueAction#executeToDB()
     */
    @SuppressWarnings("unchecked")
    @Override
    public void executeToDB() {
        Vector<Pingcollectdata> vector = (Vector<Pingcollectdata>) getIndicatorValue().getValue();
        List<String[]> dataTempList = new ArrayList<String[]>();
        List<String[]> persistenceList = new ArrayList<String[]>();
        for (Pingcollectdata pingcollectdata : vector) {
            dataTempList.add(createDataTempArray(pingcollectdata));
            persistenceList.add(createPersistenceArray(pingcollectdata));
        }

        // 删除临时数据
        String deleteDataTempSQL = "delete from " + TABLE_NAME_DATA_TEMP + " where nodeid='" + getNodeId() + "'";
        executeSQL(deleteDataTempSQL);

        // 保存临时数据
        String dataTempSQL = "insert into " + TABLE_NAME_DATA_TEMP + "(nodeid,ip,`type`,subtype,entity,subentity,sindex,thevalue,chname,restype,collecttime,unit,bak)"
            + "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        executePreparedSQL(dataTempSQL, dataTempList);

        // 保存持久数据
        String persistenceSQL = "insert into " + TABLE_NAME_PERSISTENCE + getIPTableName(getIpAddress()) + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime)"
            + "values(?,?,?,?,?,?,?,?,?,?,?)";
        executePreparedSQL(persistenceSQL, persistenceList);
    }

    /**
     * createDataTempArray:
     * <p>创建 临时数据 的数组
     *
     * @param   pingcollectdata
     *          - {@link Pingcollectdata}
     * @return  {@link String[]}
     *          - 临时数据 的数组
     *
     * @since   v1.01
     */
    public String[] createDataTempArray(Pingcollectdata pingcollectdata) {
        String[] array = new String[13];
        array[0] = getNodeId();
        array[1] = getIpAddress();
        array[2] = getNodeType();
        array[3] = getNodeSubtype();
        array[4] = pingcollectdata.getCategory();
        array[5] = pingcollectdata.getEntity();
        array[6] = pingcollectdata.getSubentity();
        array[7] = pingcollectdata.getThevalue();
        array[8] = pingcollectdata.getChname();
        array[9] = pingcollectdata.getRestype();
        array[10] = format(pingcollectdata.getCollecttime());
        array[11] = pingcollectdata.getUnit();
        array[12] = pingcollectdata.getBak();
        return array;
    }

    /**
     * createPersistenceArray:
     * <p>创建 持久数据 的数组
     *
     * @param   pingcollectdata
     *          - {@link Pingcollectdata}
     * @return  {@link String[]}
     *          - 持久数据 的数组
     *
     * @since   v1.01
     */
    public String[] createPersistenceArray(Pingcollectdata pingcollectdata) {
        Long count = pingcollectdata.getCount();
        if (count == null) {
            count = 0L;
        }
        String[] array = new String[11];
        array[0] = getIpAddress();
        array[1] = pingcollectdata.getRestype();
        array[2] = pingcollectdata.getCategory();
        array[3] = pingcollectdata.getEntity();
        array[4] = pingcollectdata.getSubentity();
        array[5] = pingcollectdata.getUnit();
        array[6] = pingcollectdata.getChname();
        array[7] = pingcollectdata.getBak();
        array[8] = String.valueOf(count);
        array[9] = pingcollectdata.getThevalue();
        array[10] = format(pingcollectdata.getCollecttime());
        return array;
    }
}

