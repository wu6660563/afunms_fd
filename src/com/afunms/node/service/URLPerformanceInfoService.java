/*
 * @(#)URLPerformanceInfoService.java     v1.01, May 30, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.service;

import java.util.List;

import com.afunms.node.dao.UrlPerformanceInfoTabaleDao;
import com.afunms.node.model.URLPerformanceInfo;

/**
 * ClassName:   URLPerformanceInfoService.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        May 30, 2013 11:27:35 AM
 */
public class URLPerformanceInfoService {

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
    public List<URLPerformanceInfo> getPerformance(String nodeid, String startTime, String endTime) {
        List<URLPerformanceInfo> list = null;
        UrlPerformanceInfoTabaleDao dao = new UrlPerformanceInfoTabaleDao("url" + nodeid);
        try {
            list = dao.findByCollectTime(startTime, endTime);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return list;
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
    public boolean save(String nodeid, URLPerformanceInfo info) {
        boolean result = false;
        UrlPerformanceInfoTabaleDao dao = new UrlPerformanceInfoTabaleDao("url" + nodeid);
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

