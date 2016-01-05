package com.gatherdb;

import java.io.Serializable;
import java.util.HashMap;

public class DBAttribute implements Serializable{

    /**
	 * serialVersionUID:
	 * <p>
	 *
	 * @since   v1.01
	 */
	private static final long serialVersionUID = 6064289821510828146L;
	private HashMap<String, Object> hashMap = new HashMap<String, Object>();

    public Object getAttribute(String key) {
        return hashMap.get(key);
    }
    public void setAttribute(String key, Object value) {
        hashMap.put(key, value);
    }
	/**
	 * getHashMap:
	 * <p>
	 *
	 * @return  HashMap<String,Object>
	 *          -
	 * @since   v1.01
	 */
	public HashMap<String, Object> getHashMap() {
		return hashMap;
	}

    
}
