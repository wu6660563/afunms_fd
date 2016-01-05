/*
 * @(#)DiskPersistenceIndicatorValueAction.java     v1.01, 2014 1 2
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.afunms.node.indicator.action.PersistenceIndicatorValueAction;
import com.afunms.polling.om.Diskcollectdata;

/**
 * ClassName:   DiskPersistenceIndicatorValueAction.java
 * <p>{@link DiskPersistenceIndicatorValueAction} Disk 指标的持久化处理
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 2 11:31:23
 */
public class DiskPersistenceIndicatorValueAction extends PersistenceIndicatorValueAction {

    private final static String TABLE_NAME_PERSISTENCE_DISK =  "disk";
    
    private final static String TABLE_NAME_PERSISTENCE_DISKINCRE =  "diskincre";

    private final static String TABLE_NAME_DATA_TEMP = "nms_disk_data_temp";

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
        Vector<Diskcollectdata> vector = (Vector<Diskcollectdata>) getIndicatorValue().getValue();
        List<String[]> dataTempList = new ArrayList<String[]>();
        List<String[]> diskPersistenceList = new ArrayList<String[]>();
        List<String[]> diskIncPersistenceList = new ArrayList<String[]>();
        for (Diskcollectdata diskcollectdata : vector) {
            dataTempList.add(createDataTempArray(diskcollectdata));
            if ("Utilization".equals(diskcollectdata.getEntity())) {
                diskPersistenceList.add(createPersistenceArray(diskcollectdata));
            } else if ("UtilizationInc".equals(diskcollectdata.getEntity())) {
                diskIncPersistenceList.add(createPersistenceArray(diskcollectdata));
            }
            
        }

        // 删除临时数据
        String deleteDataTempSQL = "delete from " + TABLE_NAME_DATA_TEMP + " where nodeid='" + getNodeId() + "'";
        executeSQL(deleteDataTempSQL);

        // 保存临时数据
        String dataTempSQL = "insert into " + TABLE_NAME_DATA_TEMP + "(nodeid,ip,`type`,subtype,entity,subentity,sindex,thevalue,chname,restype,collecttime,unit,bak)"
            + " values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        executePreparedSQL(dataTempSQL, dataTempList);

        // 保存持久数据
        String diskPersistenceSQL = "insert into " + TABLE_NAME_PERSISTENCE_DISK + getIPTableName(getIpAddress()) + "(ipaddress,category,subentity,restype,entity,thevalue,collecttime,unit)"
            + " values(?,?,?,?,?,?,?,?)";
        executePreparedSQL(diskPersistenceSQL, diskPersistenceList);
        // 保存持久数据
        String diskIncPersistenceSQL = "insert into " + TABLE_NAME_PERSISTENCE_DISKINCRE + getIPTableName(getIpAddress()) + "(ipaddress,category,subentity,restype,entity,thevalue,collecttime,unit)"
            + " values(?,?,?,?,?,?,?,?)";
        executePreparedSQL(diskIncPersistenceSQL, diskIncPersistenceList);
    
    }

    /**
     * createDataTempArray:
     * <p>创建 临时数据 的数组
     *
     * @param   diskcollectdata
     *          - {@link Diskcollectdata}
     * @return  {@link String[]}
     *          - 临时数据 的数组
     *
     * @since   v1.01
     */
    public String[] createDataTempArray(Diskcollectdata diskcollectdata) {
        String[] array = new String[13];
        array[0] = getNodeId();
        array[1] = getIpAddress();
        array[2] = getNodeType();
        array[3] = getNodeSubtype();
        array[4] = diskcollectdata.getCategory();
        array[5] = diskcollectdata.getEntity();
        array[6] = diskcollectdata.getSubentity();
        array[7] = diskcollectdata.getThevalue();
        array[8] = diskcollectdata.getChname();
        array[9] = diskcollectdata.getRestype();
        array[10] = format(diskcollectdata.getCollecttime());
        array[11] = diskcollectdata.getUnit();
        array[12] = diskcollectdata.getBak();
        return array;
    }

    /**
     * createPersistenceArray:
     * <p>创建 持久数据 的数组
     *
     * @param   diskcollectdata
     *          - {@link Diskcollectdata}
     * @return  {@link String[]}
     *          - 持久数据 的数组
     *
     * @since   v1.01
     */
    public String[] createPersistenceArray(Diskcollectdata diskcollectdata) {
        Long count = diskcollectdata.getCount();
        if (count == null) {
            count = 0L;
        }
        String[] array = new String[8];
        array[0] = getIpAddress();
        array[1] = diskcollectdata.getCategory();
        array[2] = diskcollectdata.getSubentity();
        array[3] = diskcollectdata.getRestype();
        array[4] = diskcollectdata.getEntity();
        array[5] = diskcollectdata.getThevalue();
        array[6] = format(diskcollectdata.getCollecttime());
        array[7] = diskcollectdata.getUnit();
        return array;
    }

}

