/*
 * @(#)ReceiveService.java     v1.01, 2013 12 19
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.synchronize.service;

/**
 * ClassName:  ReceiveService.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 19 15:32:40
 */
public interface ReceiveService {

    /**
     * start:
     * <p>启动服务
     *
     *
     * @since   v1.01
     */
    void start();

    /**
     * getReceiveTimes:
     * <p>
     *
     * @return  long
     *          -
     * @since   v1.01
     */
    long getReceiveTimes();
}

