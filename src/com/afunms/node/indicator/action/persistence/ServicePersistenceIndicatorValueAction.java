/*
 * @(#)ServicePersistenceIndicatorValueAction.java     v1.01, 2014 1 2
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.afunms.common.util.CommonUtil;
import com.afunms.node.indicator.action.PersistenceIndicatorValueAction;
import com.afunms.polling.om.Servicecollectdata;
import com.afunms.temp.model.ServiceNodeTemp;

/**
 * ClassName:   ServicePersistenceIndicatorValueAction.java
 * <p>{@link ServicePersistenceIndicatorValueAction} Service ָ��ĳ־û�����
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 2 14:47:19
 */
public class ServicePersistenceIndicatorValueAction extends
                PersistenceIndicatorValueAction {

    private final static String TABLE_NAME_DATA_TEMP = "nms_sercice_data_temp";

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
        Vector<Servicecollectdata> vector = (Vector<Servicecollectdata>) getIndicatorValue().getValue();
        List<String[]> dataTempList = new ArrayList<String[]>();
        for (Servicecollectdata servicecollectdata : vector) {
            dataTempList.add(createDataTempArray(servicecollectdata));
        }

        // ɾ����ʱ����
        String deleteDataTempSQL = "delete from " + TABLE_NAME_DATA_TEMP + " where nodeid='" + getNodeId() + "'";
        executeSQL(deleteDataTempSQL);
     
        // ������ʱ����
        String dataTempSQL = "insert into " + TABLE_NAME_DATA_TEMP + "(nodeid,ip,type,subtype,name,instate,opstate,paused,uninst,collecttime,startMode,pathName,description,serviceType,pid,groupstr)"
        + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        executePreparedSQL(dataTempSQL, dataTempList);
    
    }

    /**
     * createDataTempArray:
     * <p>���� ��ʱ���� ������
     *
     * @param   serviceNodeTemp
     *          - {@link ServiceNodeTemp}
     * @return  {@link String[]}
     *          - ��ʱ���� ������
     *
     * @since   v1.01
     */
    public String[] createDataTempArray(Servicecollectdata servicecollectdata) {
        String[] array = new String[16];
        try {
            String name = new String(servicecollectdata.getName().getBytes(), "UTF-8");
            if (name != null) {
                name = name.replaceAll("'", "");
            } else {
                name = "";
            }
            name = CommonUtil.removeIllegalStr("GB2312", name);
            
            if (servicecollectdata.getPathName() != null
                    && servicecollectdata.getPathName().trim().length() > 0) {
                servicecollectdata.setPathName(servicecollectdata
                        .getPathName().replaceAll("\"", ""));
            }

            array[0] = getNodeId();
            array[1] = getIpAddress();
            array[2] = getNodeType();// type
            array[3] = getNodeSubtype();// subtype
            array[4] = servicecollectdata.getName();// name
            array[5] = servicecollectdata.getInstate();// instate
            array[6] = servicecollectdata.getOpstate();// opstate
            array[7] = servicecollectdata.getPaused();// paused
            array[8] = servicecollectdata.getUninst();// uninst
            array[9] = format(getCalendar());// collecttime
            array[10] = servicecollectdata.getStartMode();// startMode
            array[11] = servicecollectdata.getPathName();// pathName
            array[12] = servicecollectdata.getDescription();// description
            array[13] = servicecollectdata.getServiceType();// serviceType
            array[14] = servicecollectdata.getPid();// pid
            array[15] = servicecollectdata.getGroupstr();// groupstr
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }
}


