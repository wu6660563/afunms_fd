/*
 * @(#)BufferSendRunnable.java     v1.01, 2013 12 23
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.synchronize.service;

import java.util.List;

import com.afunms.common.util.SysLogger;
import com.afunms.common.util.threadPool.ThreadPoolRunnableAbstract;
import com.afunms.common.util.threadPool.ThreadPoolRunnableAttributes;
import com.afunms.rmi.service.RMIParameter;

/**
 * ClassName:   BufferSendRunnable.java
 * <p>�����������߳�
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 23 17:49:06
 */
public class SendRunnable extends ThreadPoolRunnableAbstract {

    /**
     * logger:
     * <p>��־
     *
     * @since   v1.01
     */
    private final static SysLogger logger = SysLogger.getLogger(SendRunnable.class);

    /**
     * service:
     * <p>ͬ������
     *
     * @since   v1.01
     */
    private SynchronizeService service;

    /**
     * BufferSendRunnable.java:
     * ���췽��
     * @param   service
     *          - ͬ������
     *
     * @since   v1.01
     */
    public SendRunnable(SynchronizeService service) {
        this.service = service;
    }

    @Override
    public ThreadPoolRunnableAttributes getThreadPoolRunnableAttributes() {
        return this.threadPoolRunnableAttributes;
    }

    @Override
    public void runIt(ThreadPoolRunnableAttributes threadPoolRunnableAttributes) {
        try {
            RMIParameter parameter = (RMIParameter) threadPoolRunnableAttributes.getAttribute("parameter");
            List<SendService> list = getService().getSendServiceList();
            for (SendService sendService : list) {
                sendService.send(parameter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * getService:
     * <p>
     *
     * @return  SynchronizeService
     *          -
     * @since   v1.01
     */
    public SynchronizeService getService() {
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
    public void setService(SynchronizeService service) {
        this.service = service;
    }
   
}

