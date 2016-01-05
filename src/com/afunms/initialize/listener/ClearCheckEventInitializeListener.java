/*
 * @(#)ClearCheckEventInitializeListener.java     v1.01, 2014 1 9
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.initialize.listener;

import com.afunms.event.dao.CheckEventCompressDao;
import com.afunms.event.dao.CheckEventDao;

/**
 * ClassName:   ClearCheckEventInitializeListener.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 9 11:08:16
 */
public class ClearCheckEventInitializeListener extends
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
        CheckEventDao checkeventdao = new CheckEventDao();
        try{
            checkeventdao.empty();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            checkeventdao.close();
        }
        CheckEventCompressDao compressDao = new CheckEventCompressDao();
        try{
            compressDao.empty();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            compressDao.close();
        }
        return false;
    }

}

