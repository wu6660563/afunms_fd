/*
 * @(#)GatherMaintainInitializeListener.java     v1.01, 2014 1 12
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.initialize.listener;

import com.afunms.gather.service.GatherMaintainService;

/**
 * ClassName:   GatherMaintainInitializeListener.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 12 16:55:14
 */
public class GatherMaintainInitializeListener extends
                AbstractInitializeListener {

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
        GatherMaintainService service = new GatherMaintainService();
        service.setGatherServiceList(getResourceCenter().getGatherServiceList());
        service.start();
        return false;
    }

}

