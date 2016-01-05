/*
 * @(#)SendService.java     v1.01, 2013 12 23
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.synchronize.service;

import com.afunms.rmi.service.RMIParameter;

/**
 * ClassName:   SendService.java
 * <p> ���ݷ��ͽӿ�
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 23 10:22:03
 */
public interface SendService {

    /**
     * send:
     * <p>���� {@link RMIParameter} ����
     *
     * @param   parameter
     *          - ����
     *
     * @since   v1.01
     */
    void send(RMIParameter parameter);

    /**
     * init:
     * <p>��ʼ������
     *
     *
     * @since   v1.01
     */
    void init();

    /**
     * getName:
     * <p>��ȡ��������
     *
     *
     * @since   v1.01
     */
    String getName();
}

