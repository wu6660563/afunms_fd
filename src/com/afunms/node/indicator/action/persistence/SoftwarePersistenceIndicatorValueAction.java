/*
 * @(#)SoftwarePersistenceIndicatorValueAction.java     v1.01, 2014 1 2
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.afunms.common.util.CommonUtil;
import com.afunms.node.indicator.action.PersistenceIndicatorValueAction;
import com.afunms.polling.om.Softwarecollectdata;

/**
 * ClassName:   SoftwarePersistenceIndicatorValueAction.java
 * <p>{@link SoftwarePersistenceIndicatorValueAction} Software 指标的持久化处理
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 2 11:51:42
 */
public class SoftwarePersistenceIndicatorValueAction extends
                PersistenceIndicatorValueAction {

    private final static String TABLE_NAME_DATA_TEMP = "nms_software_data_temp";

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
        Vector<Softwarecollectdata> vector = (Vector<Softwarecollectdata>) getIndicatorValue().getValue();
        List<String[]> dataTempList = new ArrayList<String[]>();
        for (Softwarecollectdata softwarecollectdata : vector) {
            dataTempList.add(createDataTempArray(softwarecollectdata));
        }

        // 删除临时数据
        String deleteDataTempSQL = "delete from " + TABLE_NAME_DATA_TEMP + " where nodeid='" + getNodeId() + "'";
        executeSQL(deleteDataTempSQL);

        // 保存临时数据
        String dataTempSQL = "insert into " + TABLE_NAME_DATA_TEMP + "(nodeid,ip,`type`,subtype,insdate,name,stype,swid,collecttime)"
        + "values(?,?,?,?,?,?,?,?,?)";
        executePreparedSQL(dataTempSQL, dataTempList);

    
    }


    /**
     * createDataTempArray:
     * <p>创建 临时数据 的数组
     *
     * @param   softwarecollectdata
     *          - {@link Softwarecollectdata}
     * @return  {@link String[]}
     *          - 临时数据 的数组
     *
     * @since   v1.01
     */
    public String[] createDataTempArray(Softwarecollectdata softwarecollectdata) {
        String name = softwarecollectdata.getName();
        if (name != null) {
            name = name.replaceAll("'", "");
        } else {
            name = "";
        }
        name = CommonUtil.removeIllegalStr("GB2312", name);
        
        String[] array = new String[9];
        array[0] = getNodeId();
        array[1] = getIpAddress();
        array[2] = getNodeType();
        array[3] = getNodeSubtype();
        array[4] = softwarecollectdata.getInsdate();
        array[5] = name;
        array[6] = softwarecollectdata.getType();
        array[7] = softwarecollectdata.getSwid();;
        array[8] = format(getCalendar());
        return array;
    }
}

