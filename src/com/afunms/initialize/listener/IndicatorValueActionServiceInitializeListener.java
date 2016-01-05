/*
 * @(#)IndicatorValueActionServiceInitializeListener.java     v1.01, 2014 1 13
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.initialize.listener;

import java.util.List;
import java.util.Properties;

import org.jdom.Document;
import org.jdom.Element;

import com.afunms.common.util.SysLogger;
import com.afunms.node.indicator.service.IndicatorValueActionService;

/**
 * ClassName:   IndicatorValueActionServiceInitializeListener.java
 * <p>{@link IndicatorValueActionServiceInitializeListener} 采集数据处理服务监听
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 13 10:57:42
 */
public class IndicatorValueActionServiceInitializeListener extends
                AbstractInitializeListener {

    /**
     * logger:
     * <p>日志
     *
     * @since   v1.01
     */
    private static SysLogger logger = SysLogger.getLogger(MenuInitializeListener.class.getName());

    /**
     * init:
     *
     * @param configFile
     * @return
     *
     * @since   v1.01
     * @see com.afunms.initialize.SysInitializeListener#init(java.lang.String)
     */
    @SuppressWarnings({ "unchecked", "static-access" })
    public boolean init(String configFile) {
        try {
            Document doc = getDocument(configFile);
            Element threadPoolElement = doc.getRootElement().getChild("threadPool");
            List<Element> list = threadPoolElement.getChildren();
            Properties properties = new Properties();
            for (Element element : list) {
                properties.put(element.getName(), element.getTextTrim());
            }
            IndicatorValueActionService indicatorValueActionService = new IndicatorValueActionService();
            indicatorValueActionService.setThreadPoolProperties(properties);
            indicatorValueActionService.start();
            getResourceCenter().setIndicatorValueActionService(indicatorValueActionService);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("初始化 采集数据处理服务 配置信息出错！！！", e);
        }
        return true;
    }

}

