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
 * <p>{@link SystemPersistenceIndicatorValueAction} System ָ��ĳ־û�����
 *
 * @author      ����
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

        // ɾ����ʱ����
        String deleteDataTempSQL = "delete from " + TABLE_NAME_DATA_TEMP + " where nodeid='" + getNodeId() + "'";
        executeSQL(deleteDataTempSQL);

        // ������ʱ����
        String dataTempSQL = "insert into " + TABLE_NAME_DATA_TEMP + "(nodeid,ip,`type`,subtype,entity,subentity,sindex,thevalue,chname,restype,collecttime,unit,bak)"
            + "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        executePreparedSQL(dataTempSQL, dataTempList);

    }

    /**
     * createDataTempArray:
     * <p>���� ��ʱ���� ������
     *
     * @param   systemcollectdata
     *          - {@link Systemcollectdata}
     * @return  {@link String[]}
     *          - ��ʱ���� ������
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

