/*
 * @(#)DataAction.java     v1.01, 2013 12 20
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.synchronize.service;

import com.afunms.rmi.service.RMIParameter;

/**
 * ClassName:   DataAction.java
 * <p>���ݴ���ӿ�
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 20 10:44:21
 */
public interface DataAction {

    /**
     * action:
     * <p>����
     *
     * @param   parameter
     *          - ��Ҫ����� {@link RMIParameter}
     *
     * @since   v1.01
     */
    void action(RMIParameter parameter);

}

