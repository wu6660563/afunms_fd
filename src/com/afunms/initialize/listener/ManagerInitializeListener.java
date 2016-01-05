/*
 * @(#)ManagerInitializeListener.java     v1.01, 2014 1 8
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.initialize.listener;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import com.afunms.common.util.SysLogger;

/**
 * ClassName:   ManagerInitializeListener.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 8 16:53:06
 */
public class ManagerInitializeListener extends AbstractInitializeListener {

    /**
     * logger:
     * <p>日志
     *
     * @since   v1.01
     */
    private static SysLogger logger = SysLogger.getLogger(ManagerInitializeListener.class.getName());

    /**
     * init:
     *
     * @param   configFile
     *          - 配置文件
     * @return  {@link Boolean}
     *          - 启动是否成功
     *
     * @since   v1.01
     * @see com.afunms.initialize.SysInitializeListener#init(java.lang.String)
     */
    @SuppressWarnings({ "unchecked", "static-access" })
    public boolean init(String configFile) {
        Hashtable<String, Object> managerMap = new Hashtable<String, Object>();
        try {
            Document doc = getDocument(configFile);
            List<Element> list = (List<Element>) doc.getRootElement().getChildren("manager");
            Iterator<Element> it = list.iterator();
            while(it.hasNext()) {
                Element element = (Element)it.next();
                managerMap.put(element.getChild("name").getText(), 
                                Class.forName(element.getChild("class").getText().trim().replaceAll("\r", "").replaceAll("\n", "")).newInstance()); 
                logger.info("初始化 Manager 信息..." + element.getChild("class").getText().trim().replaceAll("\r", "").replaceAll("\n", ""));
            }    
            getResourceCenter().setManagerMap(managerMap);
        } catch(Exception e) {
            logger.error("初始化 Manager 配置信息出错！！！", e);
            return false;
        }
        return true;
    }

}

