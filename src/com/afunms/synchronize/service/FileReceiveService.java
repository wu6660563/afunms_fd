/*
 * @(#)FileReceiveService.java     v1.01, 2013 12 20
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.synchronize.service;


/**
 * ClassName:   FileReceiveService.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 20 11:29:15
 */
public class FileReceiveService extends BaseReceiveService {

    /**
     * runnable:
     * <p>文件接收执行线程
     *
     * @since   v1.01
     */
    private FileReceiveRunnable runnable = null;

    /**
     * start:
     * 启动服务
     *
     * @param properties
     *
     * @since   v1.01
     * @see com.afunms.transport.service.ReceiveService#start(java.util.Properties)
     */
    public void start() {
        runnable = new FileReceiveRunnable(this);
        runnable.setProperties(getProperties());
        runnable.start();
    }

}

