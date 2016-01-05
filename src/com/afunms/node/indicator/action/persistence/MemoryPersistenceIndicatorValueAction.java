/*
 * @(#)MemoryPersistenceIndicatorValueAction.java     v1.01, 2014 1 2
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.afunms.node.indicator.action.PersistenceIndicatorValueAction;
import com.afunms.polling.om.Memorycollectdata;

/**
 * ClassName:   MemoryPersistenceIndicatorValueAction.java
 * <p>{@link MemoryPersistenceIndicatorValueAction} Memory 指标的持久化处理
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 2 10:59:30
 */
public class MemoryPersistenceIndicatorValueAction extends
                PersistenceIndicatorValueAction {

    private final static String TABLE_NAME_PERSISTENCE =  "memory";

    private final static String TABLE_NAME_DATA_TEMP = "nms_memory_data_temp";

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
        Vector<Memorycollectdata> vector = (Vector<Memorycollectdata>) getIndicatorValue().getValue();
        List<String[]> dataTempList = new ArrayList<String[]>();
        List<String[]> persistenceList = new ArrayList<String[]>();
        for (Memorycollectdata memorycollectdata : vector) {
            dataTempList.add(createDataTempArray(memorycollectdata));
            if ("Utilization".equalsIgnoreCase(memorycollectdata.getEntity())) {
                persistenceList.add(createPersistenceArray(memorycollectdata));
            }
        }

        // 删除临时数据
        String deleteDataTempSQL = getDeleteDataTempSQL();
        executeSQL(deleteDataTempSQL);

        // 保存临时数据
        String dataTempSQL = "insert into " + TABLE_NAME_DATA_TEMP + "(nodeid,ip,`type`,subtype,entity,subentity,sindex,thevalue,chname,restype,collecttime,unit,bak)"
            + "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        executePreparedSQL(dataTempSQL, dataTempList);

        // 保存持久数据
        String persistenceSQL = "insert into " + TABLE_NAME_PERSISTENCE + getIPTableName(getIpAddress()) + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) "
            + "values(?,?,?,?,?,?,?,?,?,?,?)";
        executePreparedSQL(persistenceSQL, persistenceList);
    }

    public String getDeleteDataTempSQL() {
        String sindex = null;
        if ("PhysicalMemory".equalsIgnoreCase(getIndicatorName())) {
            sindex = "PhysicalMemory";
        } else if ("VirtualMemory".equalsIgnoreCase(getIndicatorName())) {
            sindex = "VirtualMemory";
        }
        String sql = "delete from " + TABLE_NAME_DATA_TEMP + " where nodeid='" + getNodeId() + "'";
        if (sindex != null && sindex.trim().length() > 0) {
            sql += " and sindex='" + sindex + "'";
        }
        return sql;
    }


    /**
     * createDataTempArray:
     * <p>创建 临时数据 的数组
     *
     * @param   memorycollectdata
     *          - {@link Memorycollectdata}
     * @return  {@link String[]}
     *          - 临时数据 的数组
     *
     * @since   v1.01
     */
    public String[] createDataTempArray(Memorycollectdata memorycollectdata) {
        String[] array = new String[13];
        array[0] = getNodeId();
        array[1] = getIpAddress();
        array[2] = getNodeType();
        array[3] = getNodeSubtype();
        array[4] = memorycollectdata.getCategory();
        array[5] = memorycollectdata.getEntity();
        array[6] = memorycollectdata.getSubentity();
        array[7] = memorycollectdata.getThevalue();
        array[8] = memorycollectdata.getChname();
        array[9] = memorycollectdata.getRestype();
        array[10] = format(memorycollectdata.getCollecttime());
        array[11] = memorycollectdata.getUnit();
        array[12] = memorycollectdata.getBak();
        return array;
    }

    /**
     * createPersistenceArray:
     * <p>创建 持久数据 的数组
     *
     * @param   memorycollectdata
     *          - {@link Memorycollectdata}
     * @return  {@link String[]}
     *          - 持久数据 的数组
     *
     * @since   v1.01
     */
    public String[] createPersistenceArray(Memorycollectdata memorycollectdata) {
        Long count = memorycollectdata.getCount();
        if (count == null) {
            count = 0L;
        }
        String[] array = new String[11];
        array[0] = getIpAddress();
        array[1] = memorycollectdata.getRestype();
        array[2] = memorycollectdata.getCategory();
        array[3] = memorycollectdata.getEntity();
        array[4] = memorycollectdata.getSubentity();
        array[5] = memorycollectdata.getUnit();
        array[6] = memorycollectdata.getChname();
        array[7] = memorycollectdata.getBak();
        array[8] = String.valueOf(count);
        array[9] = memorycollectdata.getThevalue();
        array[10] = format(memorycollectdata.getCollecttime());
        return array;
    }
}

