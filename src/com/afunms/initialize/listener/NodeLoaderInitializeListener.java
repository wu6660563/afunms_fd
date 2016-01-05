/*
 * @(#)NodeLoderInitializeListener.java     v1.01, 2014 4 16
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.initialize.listener;

import java.util.List;

import com.afunms.config.dao.IpaddressPanelDao;
import com.afunms.initialize.SysInitializeListener;
import com.afunms.polling.PollingEngine;
import com.afunms.topology.dao.ManageXmlDao;

/**
 * ClassName:   NodeLoderInitializeListener.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 4 16 10:19:10
 */
public class NodeLoaderInitializeListener implements SysInitializeListener {

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
        PollingEngine.getInstance().doPolling();
        IpaddressPanelDao paneldao = new IpaddressPanelDao();
        List list = null;
        try{
            list = paneldao.loadAll();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            paneldao.close();
        }
        PollingEngine.getInstance().setPanelList(list);
        
        
        ManageXmlDao subMapDao = new ManageXmlDao();
        List subfileList = null;
        try{
            subfileList = subMapDao.loadAll();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            subMapDao.close();
        }
        PollingEngine.getInstance().setXmlList(subfileList);
        PollingEngine.getInstance().setCollectwebflag("1");
        return false;
    }

}

