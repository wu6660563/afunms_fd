package com.afunms.rmi.service;

import java.io.Serializable;

public interface RMIClientCallBackAction extends Serializable {

    /**
     * 服务器端处理完请求后回调的方法
     * @param parameter
     *          请求参数
     * @param attribute
     *          请求结果
     */
    public void callBack(RMIParameter parameter, RMIAttribute attribute);
}
