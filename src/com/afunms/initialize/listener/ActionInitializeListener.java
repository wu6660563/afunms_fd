/*
 * @(#)ActionInitializeListener.java     v1.01, 2014 1 9
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.initialize.listener;

/**
 * ClassName:   ActionInitializeListener.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 9 10:09:01
 */
public class ActionInitializeListener extends AbstractInitializeListener {

    /**
     * init:
     *
     * @param configFile
     * @return
     *
     * @since   v1.01
     * @see com.afunms.initialize.SysInitializeListener#init(java.lang.String)
     */
    public boolean init(String configFile) {
//        Hashtable actionMap = new Hashtable();
//        try
//        {           
//            Document doc = builder.build(new File(res.getSysPath() + actionFile));          
//            List list = doc.getRootElement().getChildren("action");
//            Iterator it = list.iterator();
//            while(it.hasNext()) {
//                Element element = (Element)it.next();
//                actionMap.put(element.getAttributeValue("tag"),
//                    new Integer(element.getAttributeValue("operate"))); 
//            }    
//            res.setActionMap(actionMap);
//        } catch(Exception e) {
//            logger.error("初始化 Action 配置信息出错！！！", e);
//        }
        return false;
    }

}

