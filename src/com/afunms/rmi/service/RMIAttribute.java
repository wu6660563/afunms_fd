package com.afunms.rmi.service;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

public class RMIAttribute implements Serializable {

    /**
     * 序列化 ID
     */
    private static final long serialVersionUID = -4971794582381903121L;

    private Map<String, Object> attributeMap = new Hashtable<String, Object>();

    /**
     * 获取属性
     * @param key
     * @return
     */
    public Object getAttribute(String key) {
        return attributeMap.get(key);
    }

    /**
     * 设置属性
     * @param key
     * @param value
     */
    public void setAttribute(String key, Object value) {
        attributeMap.put(key, value);
    }
}
