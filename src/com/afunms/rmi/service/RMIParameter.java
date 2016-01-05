package com.afunms.rmi.service;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

public class RMIParameter implements Serializable {

    /**
     * 序列化 ID
     */
    private static final long serialVersionUID = -4971794582381903121L;

    private Map<String, Object> parameterMap = new Hashtable<String, Object>();

    /**
     * 获取属性
     * @param key
     * @return
     */
    public Object getParameter(String key) {
        return parameterMap.get(key);
    }

    /**
     * 设置属性
     * @param key
     * @param value
     */
    public void setParameter(String key, Object value) {
        parameterMap.put(key, value);
    }
}
