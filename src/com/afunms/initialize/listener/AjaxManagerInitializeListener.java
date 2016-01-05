/*
 * @(#)AjaxManagerInitializeListener.java     v1.01, 2014 1 9
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
 * ClassName:   AjaxManagerInitializeListener.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 9 10:03:32
 */
public class AjaxManagerInitializeListener extends AbstractInitializeListener {

    /**
     * logger:
     * <p>日志
     *
     * @since   v1.01
     */
    private static SysLogger logger = SysLogger.getLogger(AjaxManagerInitializeListener.class.getName());

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
        Hashtable<String, Object> ajaxManagerMap = new Hashtable<String, Object>();
        try {           
            Document doc = getDocument(configFile);
            List<Element> list = (List<Element>) doc.getRootElement().getChildren("manager");
            Iterator<Element> it = list.iterator();
            while(it.hasNext()) {
                Element element = (Element)it.next();
                String name = element.getChild("name").getText();
                String theclass = element.getChild("class").getText();
                ajaxManagerMap.put(name, Class.forName(theclass).newInstance()); 
            }    
            getResourceCenter().setAjaxManagerMap(ajaxManagerMap);
        } catch(Exception e) {
            logger.error("初始化 AJAX Manager 配置信息出错！！！", e);
        }
        return false;
    }

}

