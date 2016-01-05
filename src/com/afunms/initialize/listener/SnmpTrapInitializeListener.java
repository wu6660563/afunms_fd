/*
 * @(#)SnmpTrapInitializeListener.java     v1.01, 2014 1 9
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.initialize.listener;

import com.afunms.initialize.SnmpTrapsListener;

/**
 * ClassName:   SnmpTrapInitializeListener.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 9 11:11:47
 */
public class SnmpTrapInitializeListener extends AbstractInitializeListener {

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
        SnmpTrapsListener traplistener = SnmpTrapsListener.getInstance();
        traplistener.listen();
        return false;
    }

}

