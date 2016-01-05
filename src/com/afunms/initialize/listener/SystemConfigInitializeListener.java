/*
 * @(#)SystemConfigInitializeListener.java     v1.01, 2014 1 13
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.initialize.listener;

import org.jdom.Document;

import com.afunms.common.util.SysLogger;

/**
 * ClassName:   SystemConfigInitializeListener.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 13 10:10:16
 */
public class SystemConfigInitializeListener extends AbstractInitializeListener {

    /**
     * logger:
     * <p>日志
     *
     * @since   v1.01
     */
    private static SysLogger logger = SysLogger.getLogger(SystemConfigInitializeListener.class.getName());

    /**
     * init:
     *
     * @param configFile
     * @return
     *
     * @since   v1.01
     * @see com.afunms.initialize.SysInitializeListener#init(java.lang.String)
     */
    @SuppressWarnings("static-access")
    public boolean init(String configFile) {
        try {           
            Document doc = getDocument(configFile);
            // web 容器
            getResourceCenter().setAppServer(doc.getRootElement().getChildText("app-server")); 
            // snmp 版本
            getResourceCenter().setSnmpversion(doc.getRootElement().getChildText("snmpversion"));
            // 数据库连接池
            getResourceCenter().setJndi(doc.getRootElement().getChildText("jndi"));
            // 
            String temp1 = doc.getRootElement().getChildText("log-info");
            getResourceCenter().setLogInfo(Boolean.parseBoolean(temp1));
            String temp2 = doc.getRootElement().getChildText("log-error");
            getResourceCenter().setLogError(Boolean.parseBoolean(temp2));
           
            String temp3 = doc.getRootElement().getChildText("poll_per_thread_nodes");
            getResourceCenter().setPerThreadNodes(Integer.parseInt(temp3));
            String temp4 = doc.getRootElement().getChildText("poll_thread_interval");
            getResourceCenter().setPollingThreadInterval(Integer.parseInt(temp4) * 60 * 1000);                      
            String temp5 = doc.getRootElement().getChildText("max_threads");
            getResourceCenter().setMaxThreads(Integer.parseInt(temp5));
            String temp6 = doc.getRootElement().getChildText("start_polling");
            getResourceCenter().setStartPolling(Boolean.parseBoolean(temp6));
            String temp8 = doc.getRootElement().getChildText("has_discoverd");
            getResourceCenter().setHasDiscovered(Boolean.parseBoolean(temp8));           
        } catch(Exception e) {
            logger.error("初始化系统配置信息出错！！！",e);
        }
        return false;
    }

}

