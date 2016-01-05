/*
 * @(#)CpuPersistenceIndicatorValueAction.java     v1.01, 2014 1 1
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.afunms.node.indicator.action.PersistenceIndicatorValueAction;
import com.afunms.polling.om.CPUcollectdata;

/**
 * ClassName:   CpuPersistenceIndicatorValueAction.java
 * <p>{@link CpuPersistenceIndicatorValueAction} Cpu 指标的持久化处理
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 1 18:25:51
 */
public class CpuPersistenceIndicatorValueAction extends PersistenceIndicatorValueAction {

    private final static String TABLE_NAME_PERSISTENCE =  "cpu";

    private final static String TABLE_NAME_DATA_TEMP = "nms_cpu_data_temp";
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
        Vector<CPUcollectdata> vector = (Vector<CPUcollectdata>) getIndicatorValue().getValue();

        List<String[]> dataTempList = new ArrayList<String[]>();
        List<String[]> persistenceList = new ArrayList<String[]>();
        for (CPUcollectdata cpucollectdata : vector) {
            dataTempList.add(createDataTempArray(cpucollectdata));
            persistenceList.add(createPersistenceArray(cpucollectdata));
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
     * @param   cpucollectdata
     *          - {@link CPUcollectdata}
     * @return  {@link String[]}
     *          - 临时数据 的数组
     *
     * @since   v1.01
     */
    public String[] createDataTempArray(CPUcollectdata cpucollectdata) {
        String[] array = new String[13];
        array[0] = getNodeId();
        array[1] = getIpAddress();
        array[2] = getNodeType();
        array[3] = getNodeSubtype();
        array[4] = cpucollectdata.getCategory();
        array[5] = cpucollectdata.getEntity();
        array[6] = cpucollectdata.getSubentity();
        array[7] = cpucollectdata.getThevalue();
        array[8] = cpucollectdata.getChname();
        array[9] = cpucollectdata.getRestype();
        array[10] = format(cpucollectdata.getCollecttime());
        array[11] = cpucollectdata.getUnit();
        array[12] = cpucollectdata.getBak();
        return array;
    }

    /**
     * createPersistenceArray:
     * <p>创建 持久数据 的数组
     *
     * @param   cpucollectdata
     *          - {@link CPUcollectdata}
     * @return  {@link String[]}
     *          - 持久数据 的数组
     *
     * @since   v1.01
     */
    public String[] createPersistenceArray(CPUcollectdata cpucollectdata) {
        Long count = cpucollectdata.getCount();
        if (count == null) {
            count = 0L;
        }
        String[] array = new String[11];
        array[0] = getIpAddress();
        array[1] = cpucollectdata.getRestype();
        array[2] = cpucollectdata.getCategory();
        array[3] = cpucollectdata.getEntity();
        array[4] = cpucollectdata.getSubentity();
        array[5] = cpucollectdata.getUnit();
        array[6] = cpucollectdata.getChname();
        array[7] = cpucollectdata.getBak();
        array[8] = String.valueOf(count);
        array[9] = cpucollectdata.getThevalue();
        array[10] = format(cpucollectdata.getCollecttime());
        return array;
    }
}

