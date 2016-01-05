/*
 * @(#)StoragePersistenceIndicatorValueAction.java     v1.01, 2014 1 2
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.afunms.node.indicator.action.PersistenceIndicatorValueAction;
import com.afunms.polling.om.Storagecollectdata;

/**
 * ClassName:   StoragePersistenceIndicatorValueAction.java
 * <p>{@link StoragePersistenceIndicatorValueAction} Storage 指标的持久化处理
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 2 15:10:27
 */
public class StoragePersistenceIndicatorValueAction extends
                PersistenceIndicatorValueAction {

    private final static String TABLE_NAME_DATA_TEMP = "nms_storage_data_temp";
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
        Vector<Storagecollectdata> vector = (Vector<Storagecollectdata>) getIndicatorValue().getValue();
        List<String[]> dataTempList = new ArrayList<String[]>();
        for (Storagecollectdata storagecollectdata : vector) {
            dataTempList.add(createDataTempArray(storagecollectdata));
        }

        // 删除临时数据
        String deleteDataTempSQL = "delete from " + TABLE_NAME_DATA_TEMP + " where nodeid='" + getNodeId() + "'";
        executeSQL(deleteDataTempSQL);

        // 保存临时数据
        String dataTempSQL = "insert into " + TABLE_NAME_DATA_TEMP + "(nodeid,ip,`type`,subtype,name,stype,cap,storageindex,collecttime)"
            + "values(?,?,?,?,?,?,?,?,?)";
        executePreparedSQL(dataTempSQL, dataTempList);
    
    }

    /**
     * createDataTempArray:
     * <p>创建 临时数据 的数组
     *
     * @param   storagecollectdata
     *          - {@link Storagecollectdata}
     * @return  {@link String[]}
     *          - 临时数据 的数组
     *
     * @since   v1.01
     */
    public String[] createDataTempArray(Storagecollectdata storagecollectdata) {
        String name = storagecollectdata.getName();
        if(name != null){
            name = name.replace("\\", "/");
        }
        
        String[] array = new String[9];
        array[0] = getNodeId();
        array[1] = getIpAddress();
        array[2] = getNodeType();
        array[3] = getNodeSubtype();
        array[4] = name;
        array[5] = storagecollectdata.getType();
        array[6] = storagecollectdata.getCap();
        array[7] = storagecollectdata.getStorageindex();;
        array[8] = format(getCalendar());
        return array;
    }

}

