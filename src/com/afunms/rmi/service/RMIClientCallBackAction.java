package com.afunms.rmi.service;

import java.io.Serializable;

public interface RMIClientCallBackAction extends Serializable {

    /**
     * �������˴����������ص��ķ���
     * @param parameter
     *          �������
     * @param attribute
     *          ������
     */
    public void callBack(RMIParameter parameter, RMIAttribute attribute);
}
