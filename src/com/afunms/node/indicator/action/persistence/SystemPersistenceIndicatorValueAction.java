/*
 * @(#)SystemPersistenceIndicatorValueAction.java     v1.01, 2014 1 2
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.afunms.node.indicator.action.PersistenceIndicatorValueAction;
import com.afunms.polling.om.Systemcollectdata;

/**
 * ClassName:   SystemPersistenceIndicatorValueAction.java
 * <p>{@link SystemPersistenceIndicatorValueAction} System 指标的持久化处理
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 2 22:23:30
 */
public class SystemPersistenceIndicatorValueAction extends
                PersistenceIndicatorValueAction {

    private final static String TABLE_NAME_DATA_TEMP = "nms_system_data_temp";

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
        Vector<Systemcollectdata> vector = (Vector<Systemcollectdata>) getIndicatorValue().getValue();
        List<String[]> dataTempList = new ArrayList<String[]>();
        for (Systemcollectdata systemcollectdata : vector) {
            dataTempList.add(createDataTempArray(systemcollectdata));
        }

        // 删除临时数据
        String deleteDataTempSQL = "delete from " + TABLE_NAME_DATA_TEMP + " where nodeid='" + getNodeId() + "'";
        executeSQL(deleteDataTempSQL);

        // 保存临时数据
        String dataTempSQL = "insert into " + TABLE_NAME_DATA_TEMP + "(nodeid,ip,`type`,subtype,entity,subentity,sindex,thevalue,chname,restype,collecttime,unit,bak)"
            + "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        executePreparedSQL(dataTempSQL, dataTempList);

    }

    /**
     * createDataTempArray:
     * <p>创建 临时数据 的数组
     *
     * @param   systemcollectdata
     *          - {@link Systemcollectdata}
     * @return  {@link String[]}
     *          - 临时数据 的数组
     *
     * @since   v1.01
     */
    public String[] createDataTempArray(Systemcollectdata systemcollectdata) {
        String[] array = new String[13];
        array[0] = getNodeId();
        array[1] = getIpAddress();
        array[2] = getNodeType();
        array[3] = getNodeSubtype();
        array[4] = systemcollectdata.getCategory();
        array[5] = systemcollectdata.getEntity();
        array[6] = systemcollectdata.getSubentity();
        array[7] = systemcollectdata.getThevalue();
        array[8] = systemcollectdata.getChname();
        array[9] = systemcollectdata.getRestype();
        array[10] = format(systemcollectdata.getCollecttime());
        array[11] = systemcollectdata.getUnit();
        array[12] = systemcollectdata.getBak();
        return array;
    }
}

