/*
 * @(#)DiskperfPersistenceIndicatorValueAction.java     v1.01, 2014 1 15
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.persistence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.afunms.node.indicator.action.PersistenceIndicatorValueAction;
import com.afunms.polling.om.Usercollectdata;

/**
 * ClassName:   DiskperfPersistenceIndicatorValueAction.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 15 11:39:24
 */
public class DiskperfPersistenceIndicatorValueAction extends
                PersistenceIndicatorValueAction {

    private final static String TABLE_NAME_DATA_TEMP = "nms_diskperf_data_temp";

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
        List<Hashtable<String,String>> alldiskperf = (List<Hashtable<String,String>>) getIndicatorValue().getValue();
        List<String[]> dataTempList = new ArrayList<String[]>();
        Calendar date = getCalendar();
        int i = 0;
        for (Hashtable<String, String> diskperfHashtable : alldiskperf) {
            Iterator<String> iterator = diskperfHashtable.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                dataTempList.add(createDataTempArray(date, key, diskperfHashtable.get(key), String.valueOf(i++)));
            }
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
     * @param   usercollectdata
     *          - {@link Usercollectdata}
     * @return  {@link String[]}
     *          - 临时数据 的数组
     *
     * @since   v1.01
     */
    public String[] createDataTempArray(Calendar date, String key, String value, String sindex) {
        String[] array = new String[13];
        array[0] = getNodeId();
        array[1] = getIpAddress();
        array[2] = getNodeType();
        array[3] = getNodeSubtype();
        array[4] = "alldiskperf";
        array[5] = key;
        array[6] = sindex;
        array[7] = value;
        array[8] = key;
        array[9] = "static";
        array[10] = format(date);
        array[11] = "";
        array[12] = "";
        return array;
    }
}

