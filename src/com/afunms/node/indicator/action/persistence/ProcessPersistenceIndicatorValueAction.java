/*
 * @(#)ProcessPersistenceIndicatorValueAction.java     v1.01, 2014 1 2
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.persistence;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.afunms.application.dao.ProcessGroupDao;
import com.afunms.node.indicator.action.PersistenceIndicatorValueAction;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.polling.om.Processcollectdata;

/**
 * ClassName:   ProcessPersistenceIndicatorValueAction.java
 * <p>{@link ProcessPersistenceIndicatorValueAction} Process ָ��ĳ־û�����
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 2 11:43:35
 */
public class ProcessPersistenceIndicatorValueAction extends
                PersistenceIndicatorValueAction {

    private final static String TABLE_NAME_DATA_TEMP = "nms_process_data_temp";
    
    private final static String TABLE_NAME_PERSISTENCE =  "pro";

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
        Hashtable<String, Object> processHashtable = (Hashtable<String, Object>) getIndicatorValue().getValue();
        Vector<Processcollectdata> vector = (Vector<Processcollectdata>) processHashtable.get("process");
        Hashtable<String, Processcollectdata> processNumberHashtable = (Hashtable<String, Processcollectdata>) processHashtable.get("processNum");

        List<String[]> dataTempList = new ArrayList<String[]>();
        List<String[]> persistenceList = new ArrayList<String[]>();

        // ˢѡ��Ҫ����
        // ����ȡ�����ݿ�����Ҫ���̵�����
        ProcessGroupDao groupDao = new ProcessGroupDao();
        Hashtable<String, String> procNameHash = null;
        try {
            procNameHash = groupDao.findByImport(getNodeId());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            groupDao.close();
        }

        // ���˽���������Hashtable�����н���������Hashtabl �ļ�Ϊ�������ƣ�
        // ���������Ҫ���̵�������Ϊ��Ҫ���̣�
        // ����Ҫ���̵������������־û����飬������Ҫ���̵Ľ���ID ������ Hashtable ��
        Hashtable<String, Processcollectdata> sindexHashtable = new Hashtable<String, Processcollectdata>();
        if (processNumberHashtable != null && processNumberHashtable.size() > 0) {
            Iterator<String> iterator = processNumberHashtable.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                Iterator<String> iterator1 = procNameHash.keySet().iterator();
                while (iterator1.hasNext()) {
                    String name = (String) iterator1.next();
                    if (key.trim().toLowerCase().contains(name.trim().toLowerCase())) {
                        Processcollectdata processcollectdata = processNumberHashtable.get(key);
                        persistenceList.add(createPersistenceArray(processcollectdata));
                        sindexHashtable.put(processcollectdata.getSubentity(), processcollectdata);
                    }
                }
            }
        }
        
        for (Processcollectdata processcollectdata : vector) {
            dataTempList.add(createDataTempArray(processcollectdata));
            if (sindexHashtable.containsKey(processcollectdata.getSubentity())) {
                // ���Ϊ��Ҫ��������г־û�
                persistenceList.add(createPersistenceArray(processcollectdata));
            }
        }

        // ɾ����ʱ����
        truncateDataTemp(TABLE_NAME_DATA_TEMP + getIPTableName(getIpAddress()));

        // ������ʱ����
        String dataTempSQL = "insert into " + TABLE_NAME_DATA_TEMP + getIPTableName(getIpAddress()) + "(nodeid,ip,`type`,subtype,entity,subentity,sindex,thevalue,chname,restype,collecttime,unit,bak) "
        + "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        executePreparedSQL(dataTempSQL, dataTempList);
    
        // ����־�����
        String persistenceSQL = "insert into " + TABLE_NAME_PERSISTENCE + getIPTableName(getIpAddress()) + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime)"
            + "values(?,?,?,?,?,?,?,?,?,?,?)";
        executePreparedSQL(persistenceSQL, persistenceList);
    }

    /**
     * createDataTempArray:
     * <p>���� ��ʱ���� ������
     *
     * @param   processcollectdata
     *          - {@link Processcollectdata}
     * @return  {@link String[]}
     *          - ��ʱ���� ������
     *
     * @since   v1.01
     */
    public String[] createDataTempArray(Processcollectdata processcollectdata) {
        String thevalue = processcollectdata.getThevalue();
        if (thevalue != null) {
            thevalue = thevalue.replaceAll("\\\\", "/");
            if (thevalue.length() > 200) {
                thevalue = thevalue.substring(0, 200) + "...";
            }
        }
        String[] array = new String[13];
        array[0] = getNodeId();
        array[1] = getIpAddress();
        array[2] = getNodeType();
        array[3] = getNodeSubtype();
        array[4] = processcollectdata.getCategory();
        array[5] = processcollectdata.getEntity();
        array[6] = processcollectdata.getSubentity();
        array[7] = thevalue;
        array[8] = processcollectdata.getChname();
        array[9] = processcollectdata.getRestype();
        array[10] = format(processcollectdata.getCollecttime());
        array[11] = processcollectdata.getUnit();
        array[12] = processcollectdata.getBak();
        return array;
    }

    /**
     * createPersistenceArray:
     * <p>���� �־����� ������
     *
     * @param   pingcollectdata
     *          - {@link Pingcollectdata}
     * @return  {@link String[]}
     *          - �־����� ������
     *
     * @since   v1.01
     */
    public String[] createPersistenceArray(Processcollectdata processcollectdata) {
        Long count = processcollectdata.getCount();
        if (count == null) {
            count = 0L;
        }
        String[] array = new String[11];
        array[0] = getIpAddress();
        array[1] = processcollectdata.getRestype();
        array[2] = processcollectdata.getCategory();
        array[3] = processcollectdata.getEntity();
        array[4] = processcollectdata.getSubentity();
        array[5] = processcollectdata.getUnit();
        array[6] = processcollectdata.getChname();
        array[7] = processcollectdata.getBak();
        array[8] = String.valueOf(count);
        array[9] = processcollectdata.getThevalue();
        array[10] = format(processcollectdata.getCollecttime());
        return array;
    }
}

