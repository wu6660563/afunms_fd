/*
 * @(#)ReceiveRunable.java     v1.01, 2013 12 20
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.synchronize.service;

/**
 * ClassName:   ReceiveRunable.java
 * <p>接收服务的 {@link Runnable}
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 20 11:46:43
 */
public abstract class ReceiveRunable implements Runnable {

    /**
     * service:
     * <p>接收服务
     *
     * @since   v1.01
     */
    protected ReceiveService service;

    /**
     * ReceiveRunable.java:
     * @param   service
     *
     * @since   v1.01
     */
    public ReceiveRunable(ReceiveService service) {
        setService(service);
    }

    /**
     * getService:
     * <p>
     *
     * @return  ReceiveService
     *          -
     * @since   v1.01
     */
    public ReceiveService getService() {
        return service;
    }

    /**
     * setService:
     * <p>
     *
     * @param   service
     *          -
     * @since   v1.01
     */
    public void setService(ReceiveService service) {
        this.service = service;
    }

}

