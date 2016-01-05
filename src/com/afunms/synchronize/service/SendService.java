/*
 * @(#)SendService.java     v1.01, 2013 12 23
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.synchronize.service;

import com.afunms.rmi.service.RMIParameter;

/**
 * ClassName:   SendService.java
 * <p> 数据发送接口
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 23 10:22:03
 */
public interface SendService {

    /**
     * send:
     * <p>发送 {@link RMIParameter} 对象
     *
     * @param   parameter
     *          - 参数
     *
     * @since   v1.01
     */
    void send(RMIParameter parameter);

    /**
     * init:
     * <p>初始化方法
     *
     *
     * @since   v1.01
     */
    void init();

    /**
     * getName:
     * <p>获取服务名称
     *
     *
     * @since   v1.01
     */
    String getName();
}

