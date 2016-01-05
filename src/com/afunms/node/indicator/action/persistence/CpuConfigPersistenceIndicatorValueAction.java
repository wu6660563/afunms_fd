/*
 * @(#)CpuConfigPersistenceIndicatorValueAction.java     v1.01, 2014 1 15
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.persistence;

import java.util.ArrayList;
import java.util.List;

import com.afunms.config.model.Nodecpuconfig;
import com.afunms.node.indicator.action.PersistenceIndicatorValueAction;
import com.afunms.polling.om.CPUcollectdata;

/**
 * ClassName:   CpuConfigPersistenceIndicatorValueAction.java
 * <p>
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 15 15:54:46
 */
public class CpuConfigPersistenceIndicatorValueAction extends
                PersistenceIndicatorValueAction {

    private final static String TABLE_NAME_DATA_TEMP = "nms_nodecpuconfig";


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
        List<String[]> dataTempList = new ArrayList<String[]>();
        List<Nodecpuconfig> list = (List<Nodecpuconfig>) getIndicatorValue().getValue();
        for (Nodecpuconfig cpuconfig : list) {
            dataTempList.add(createDataTempArray(cpuconfig));
        }

        // ɾ����ʱ����
        String deleteDataTempSQL = "delete from " + TABLE_NAME_DATA_TEMP + " where nodeid='" + getNodeId() + "'";
        executeSQL(deleteDataTempSQL);

        // ������ʱ����
        String dataTempSQL = "insert into " + TABLE_NAME_DATA_TEMP + "(nodeid,dataWidth,processorId,name,l2CacheSize,l2CacheSpeed,descrOfProcessors,processorType,processorSpeed)"
            + "values(?,?,?,?,?,?,?,?,?)";
        executePreparedSQL(dataTempSQL, dataTempList);
    }

    /**
     * createDataTempArray:
     * <p>���� ��ʱ���� ������
     *
     * @param   cpucollectdata
     *          - {@link CPUcollectdata}
     * @return  {@link String[]}
     *          - ��ʱ���� ������
     *
     * @since   v1.01
     */
    public String[] createDataTempArray(Nodecpuconfig cpuconfig) {
        if(cpuconfig.getProcessorSpeed() == null){
            cpuconfig.setProcessorSpeed("");
        }
        if(cpuconfig.getProcessorType() == null){
            cpuconfig.setProcessorType("");
        }
        String[] array = new String[9];
        array[0] = getNodeId();
        array[1] = cpuconfig.getDataWidth();
        array[2] = cpuconfig.getProcessorId();
        array[3] = cpuconfig.getName();
        array[4] = cpuconfig.getL2CacheSize();
        array[5] = cpuconfig.getL2CacheSpeed();
        array[6] = cpuconfig.getDescrOfProcessors();
        array[7] = cpuconfig.getProcessorType();
        array[8] = cpuconfig.getProcessorSpeed();
        return array;
    }
}

