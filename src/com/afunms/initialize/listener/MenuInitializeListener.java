/*
 * @(#)MenuInitializeListener.java     v1.01, 2014 1 9
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
 * ClassName:   MenuInitializeListener.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 9 10:21:20
 */
public class MenuInitializeListener extends AbstractInitializeListener {

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
        Hashtable<String, String> menuMap = new Hashtable<String, String>();
        try {           
            Document doc = getDocument(configFile);
            List<Element> list = (List<Element>) doc.getRootElement().getChildren("menu");
            Iterator<Element> it = list.iterator();
            while(it.hasNext()) {
                Element element = (Element)it.next();
                menuMap.put(element.getChild("filename").getText(), 
                        element.getChild("menuId").getText()); 
            }
            getResourceCenter().setMenuMap(menuMap);
        } catch(Exception e) {
            logger.error("初始化 Menu 配置信息出错！！！", e);
        }
        return false;
    }

}

