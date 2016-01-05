/*
 * @(#)IPMacPersistenceIndicatorValueAction.java     v1.01, 2014 1 2
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.afunms.node.indicator.action.PersistenceIndicatorValueAction;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.IpMac;

/**
 * ClassName:   IPMacPersistenceIndicatorValueAction.java
 * <p>{@link IPMacPersistenceIndicatorValueAction} IPMac 指标的持久化处理
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 2 21:51:47
 */
public class IPMacPersistenceIndicatorValueAction extends PersistenceIndicatorValueAction {

    private final static String TABLE_NAME_DATA_TEMP = "ipmac";

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
        Vector<IpMac> vector = (Vector<IpMac>) getIndicatorValue().getValue();
        List<String[]> dataTempList = new ArrayList<String[]>();
        for (IpMac ipmac : vector) {
            dataTempList.add(createDataTempArray(ipmac));
        }

        // 删除临时数据
        String deleteDataTempSQL = "delete from " + TABLE_NAME_DATA_TEMP + " where relateipaddr='" + getIpAddress() + "'";
        executeSQL(deleteDataTempSQL);

        // 保存临时数据
        String dataTempSQL = "insert into " + TABLE_NAME_DATA_TEMP + "(relateipaddr,ifindex,ipaddress,mac,collecttime,ifband,ifsms)values(?,?,?,?,?,?,?)";
        executePreparedSQL(dataTempSQL, dataTempList);
    
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
    public String[] createDataTempArray(IpMac ipmac) {
        String[] array = new String[7];
        try {
            String mac = ipmac.getMac();
            if(mac == null){
                mac = "";
            }
            array[0] = ipmac.getRelateipaddr() ;
            array[1] = ipmac.getIfindex() ;
            array[2] = ipmac.getIpaddress();
            array[3] = new String(mac.getBytes(), "UTF-8");
            array[4] = format(ipmac.getCollecttime());
            array[5] = ipmac.getIfband();
            array[6] = ipmac.getIfsms();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }
}

