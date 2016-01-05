/*
 * @(#)PerformanceInfoService.java     v1.01, Jan 11, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.afunms.node.dao.PerformaceInfoTableDao;
import com.afunms.node.model.PerformanceInfo;

/**
 * ClassName:   PerformanceInfoService.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        Jan 11, 2013 5:42:45 PM
 */
public class PerformanceInfoService {

    public final static String TABLE_NAME_MEMORY = "memory";

    public final static String TABLE_NAME_ALLUTILHDX = "allutilhdx";

    public final static String TABLE_NAME_UTILHDX = "utilhdx";

    /**
     * getPerformance:
     * <p>获取性能信息
     *
     * @param table
     * @param startTime
     * @param endTime
     * @return
     *
     * @since   v1.01
     */
    public List<PerformanceInfo> getPerformance(String table, String startTime, String endTime) {
        List<PerformanceInfo> list = null;
        PerformaceInfoTableDao dao = new PerformaceInfoTableDao(table);
        try {
            list = dao.find(startTime, endTime);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return list;
    }

    /**
     * getPerformance:
     * <p>获取性能信息
     *
     * @param table
     * @param startTime
     * @param endTime
     * @return
     *
     * @since   v1.01
     */
    public Hashtable<String, List<PerformanceInfo>> getPerformanceGroupBySubentity(String table, String subentity, String startTime, String endTime) {
        List<PerformanceInfo> list = null;
        PerformaceInfoTableDao dao = new PerformaceInfoTableDao(table);
        try {
            list = dao.find(subentity, startTime, endTime);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        Hashtable<String, List<PerformanceInfo>> hashtable = new Hashtable<String, List<PerformanceInfo>>();
        for (PerformanceInfo performanceInfo : list) {
            List<PerformanceInfo> performanceInfoList = hashtable.get(performanceInfo.getEntity());
            if (performanceInfoList == null) {
                performanceInfoList = new ArrayList<PerformanceInfo>();
                hashtable.put(performanceInfo.getEntity(), performanceInfoList);
            }
            performanceInfoList.add(performanceInfo);
        }
        return hashtable;
    }

    public Hashtable<String, List<PerformanceInfo>> getPerformanceGroupBySubentity(String table, String startTime, String endTime) {
        List<PerformanceInfo> list = getPerformance(table, startTime, endTime);
        Hashtable<String, List<PerformanceInfo>> hashtable = new Hashtable<String, List<PerformanceInfo>>();
        for (PerformanceInfo performanceInfo : list) {
            List<PerformanceInfo> performanceInfoList = hashtable.get(performanceInfo.getSubentity());
            if (performanceInfoList == null) {
                performanceInfoList = new ArrayList<PerformanceInfo>();
                hashtable.put(performanceInfo.getSubentity(), performanceInfoList);
            }
            performanceInfoList.add(performanceInfo);
        }
        return hashtable;
    }
    /**
     * save:
     * <p>保存性能信息
     *
     * @param   table
     *          - 性能信息表
     * @param   info
     *          - 性能信息
     * @return  {@link Boolean}
     *          - 如果保存成功，则返回 <code>true</code> , 否则返回 <code>false</code>
     *
     * @since   v1.01
     */
    public boolean save(String table, PerformanceInfo info) {
        boolean result = false;
        PerformaceInfoTableDao dao = new PerformaceInfoTableDao(table);
        try {
            result = dao.save(info);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return result;
    }
}

