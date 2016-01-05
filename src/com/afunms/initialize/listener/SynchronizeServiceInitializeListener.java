/*
 * @(#)SynchronizeServiceInitializeListener.java     v1.01, 2014 1 17
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.initialize.listener;

import com.afunms.synchronize.service.SynchronizeService;

/**
 * ClassName:   SynchronizeServiceInitializeListener.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 17 11:32:13
 */
public class SynchronizeServiceInitializeListener extends AbstractInitializeListener {

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
        SynchronizeService.getInstance().start();
        return false;
    }

}

