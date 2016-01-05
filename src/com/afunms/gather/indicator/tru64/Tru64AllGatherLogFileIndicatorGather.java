/*
 * @(#)Tru64AllGatherLogFileIndicatorGather.java     v1.01, 2014 1 15
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.tru64;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import com.afunms.common.util.SysLogger;
import com.afunms.gather.indicator.base.LogFileIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Systemcollectdata;

/**
 * ClassName:   Tru64AllGatherLogFileIndicatorGather.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 15 16:18:20
 */
public class Tru64AllGatherLogFileIndicatorGather extends
                LogFileIndicatorGather {

    /**
     * logger:
     * <p>日志
     *
     * @since   v1.01
     */
    private final static SysLogger logger = SysLogger.getLogger(Tru64AllGatherLogFileIndicatorGather.class);


    protected Hashtable<String, LogFileIndicatorGather> allIndicatorGatherHashtable = null;

    /**
     * getSimpleIndicatorValue:
     *
     * @return
     *
     * @since   v1.01
     * @see com.afunms.gather.indicator.base.BaseIndicatorGather#getSimpleIndicatorValue()
     */
    @SuppressWarnings({ "unchecked", "static-access" })
    @Override
    public SimpleIndicatorValue getSimpleIndicatorValue() {
        String logFileContent = loadLogFileContent();
        setLogFileContent(logFileContent);
        Hashtable<String, Object> hashtable = new Hashtable<String, Object>();

        if (logFileContent != null && logFileContent.trim().length() > 0) {
            Hashtable<String, LogFileIndicatorGather> allIndicatorGatherHashtable = getAllIndicatorGatherHashtable();
            Iterator<String> iterator = allIndicatorGatherHashtable.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                try {
                    LogFileIndicatorGather logFileIndicatorGather = allIndicatorGatherHashtable.get(key);
                    logFileIndicatorGather.setIndicatorInfo(getIndicatorInfo());
                    logFileIndicatorGather.setLogFileContent(logFileContent);
                    SimpleIndicatorValue simpleIndicatorValue = logFileIndicatorGather.getSimpleIndicatorValue();
                    logFileIndicatorGather.setLastSimpleIndicatorValue(simpleIndicatorValue);
   
                    hashtable.put(key, simpleIndicatorValue.getValue());
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    logger.error("Linux 设备 " + getIpAddress() + " 告警 key 为：" + key + " 出错", e);
                }
            }
            Vector<Systemcollectdata> vector = new Vector<Systemcollectdata>();
            vector.addAll((Vector<Systemcollectdata>) hashtable.get("uptime"));
            vector.addAll((Vector<Systemcollectdata>) hashtable.get("uname"));
            vector.addAll((Vector<Systemcollectdata>) hashtable.get("date"));
            hashtable.put("system", vector);
        }
        return createSimpleIndicatorValue(hashtable);
    }

    public Hashtable<String, LogFileIndicatorGather> createAllIndicatorGatherHashtable() {
        Hashtable<String, LogFileIndicatorGather> hashtable = new Hashtable<String, LogFileIndicatorGather>();
        // 磁盘
        hashtable.put("disk", new Tru64DiskLogFileIndicatorGather());

        // CPU
        hashtable.put("cpu", new Tru64CpuLogFileIndicatorGather());

        // Process
        hashtable.put("process", new Tru64ProcessLogFileIndicatorGather());

        // Memory
        hashtable.put("memory", new Tru64MemoryLogFileIndicatorGather());

        // UName
        hashtable.put("uname", new Tru64UNameLogFileIndicatorGather());

        // Date
        hashtable.put("date", new Tru64DateLogFileIndicatorGather());

        // uptime
        hashtable.put("uptime", new Tru64UpTimeLogFileIndicatorGather());

        // Interface
        hashtable.put("interface", new Tru64InterfaceLogFileIndicatorGather());

        // User
        hashtable.put("user", new Tru64UserLogFileIndicatorGather());
        return hashtable;
    }
    
    /**
     * getAllIndicatorGatherHashtable:
     * <p>
     *
     * @return  Hashtable<String,LogFileIndicatorGather>
     *          -
     * @since   v1.01
     */
    public Hashtable<String, LogFileIndicatorGather> getAllIndicatorGatherHashtable() {
        if (allIndicatorGatherHashtable == null) {
            allIndicatorGatherHashtable = createAllIndicatorGatherHashtable();
        }
        return allIndicatorGatherHashtable;
    }


    /**
     * setAllIndicatorGatherHashtable:
     * <p>
     *
     * @param   allIndicatorGatherHashtable
     *          -
     * @since   v1.01
     */
    public void setAllIndicatorGatherHashtable(
                    Hashtable<String, LogFileIndicatorGather> allIndicatorGatherHashtable) {
        this.allIndicatorGatherHashtable = allIndicatorGatherHashtable;
    }
}

