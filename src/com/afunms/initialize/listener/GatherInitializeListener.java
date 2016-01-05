/*
 * @(#)GatherInitializeListener.java     v1.01, 2014 1 9
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.initialize.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jdom.Document;
import org.jdom.Element;

import com.afunms.common.util.SysLogger;
import com.afunms.gather.service.GatherDataSendService;
import com.afunms.gather.service.GatherService;
import com.afunms.node.service.NodeDomainService;

/**
 * ClassName:   GatherInitializeListener.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 9 11:12:55
 */
public class GatherInitializeListener extends AbstractInitializeListener {

    /**
     * logger:
     * <p>日志
     *
     * @since   v1.01
     */
    private static SysLogger logger = SysLogger.getLogger(GatherInitializeListener.class.getName());

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
            List<Element> elementList = threadPoolElement.getChildren();
            Properties properties = new Properties();
            for (Element element : elementList) {
                properties.put(element.getName(), element.getTextTrim());
            }
            elementList = doc.getRootElement().getChildren("gather-send-service");
            List<GatherDataSendService> gatherDataSendServiceList = new ArrayList<GatherDataSendService>();
            for (Element element : elementList) {
                boolean enable = Boolean.valueOf(element.getChildText("enable"));
                String className = element.getChildText("class");
                if (enable) {
                    gatherDataSendServiceList.add((GatherDataSendService) Class.forName(className).newInstance());
                }
                properties.put(element.getName(), element.getTextTrim());
            }
            GatherService gatherService = new GatherService();
            gatherService.setThreadPoolProperties(properties);
            gatherService.setDomain(String.valueOf(NodeDomainService.DEFAULT_DOMAIN.getId()));
            gatherService.setGatherDataSendServiceList(gatherDataSendServiceList);
            gatherService.start();
            getResourceCenter().getGatherServiceList().add(gatherService);
        } catch(Exception e) {
            e.printStackTrace();
            logger.error("初始化 采集服务 配置信息出错！！！", e);
            return false;
        }
        return false;
    }

}

