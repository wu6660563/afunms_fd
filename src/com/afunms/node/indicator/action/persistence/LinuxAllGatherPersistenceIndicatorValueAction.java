/*
 * @(#)LinuxAllGatherPersistenceIndicatorValueAction.java     v1.01, 2014 1 14
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.persistence;

import java.util.Hashtable;
import java.util.Iterator;

import com.afunms.common.util.SysLogger;
import com.afunms.gather.model.IndicatorValue;
import com.afunms.node.indicator.action.PersistenceIndicatorValueAction;

/**
 * ClassName:   LinuxAllGatherPersistenceIndicatorValueAction.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 14 15:13:20
 */
public class LinuxAllGatherPersistenceIndicatorValueAction extends
                PersistenceIndicatorValueAction {

    /**
     * logger:
     * <p>日志
     *
     * @since   v1.01
     */
    private final static SysLogger logger = SysLogger.getLogger(LinuxAllGatherPersistenceIndicatorValueAction.class);

    private Hashtable<String, PersistenceIndicatorValueAction> allPersistenceIndicatorValueActionHashtable ;

    /**
     * executeToDB:
     *
     *
     * @since   v1.01
     * @see com.afunms.node.indicator.action.PersistenceIndicatorValueAction#executeToDB()
     */
    @SuppressWarnings({ "unchecked", "static-access" })
    @Override
    public void executeToDB() {
        Hashtable<String, PersistenceIndicatorValueAction> allPersistenceIndicatorValueActionHashtable = getAllPersistenceIndicatorValueActionHashtable();
        Hashtable<String, Object> hashtable = (Hashtable<String, Object>) getIndicatorValue().getValue();
        Iterator<String> iterator = allPersistenceIndicatorValueActionHashtable.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            try {
                PersistenceIndicatorValueAction action = allPersistenceIndicatorValueActionHashtable.get(key);
                Object value = hashtable.get(key);
                if (value == null) {
                    continue;
                }
                action.setIndicatorValue(createIndicatorValue(value));
                action.executeToDB();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("设备 " + getIpAddress() + " 入库 key 为：" + key + " 出错", e);
            }
        }
    }

    public Hashtable<String, PersistenceIndicatorValueAction> createAllPersistenceIndicatorValueActionHashtable() {
        Hashtable<String, PersistenceIndicatorValueAction> hashtable = new Hashtable<String, PersistenceIndicatorValueAction>();
        // 磁盘
        hashtable.put("disk", new DiskPersistenceIndicatorValueAction());

        // CPU
        hashtable.put("cpu", new CpuPersistenceIndicatorValueAction());

        // CPU
        hashtable.put("CPUConfig", new CpuConfigPersistenceIndicatorValueAction());

        // Memory
        hashtable.put("memory", new MemoryPersistenceIndicatorValueAction());
        
        // User
        hashtable.put("user", new UserPersistenceIndicatorValueAction());

        // Process
        hashtable.put("process", new ProcessPersistenceIndicatorValueAction());

        // system
        hashtable.put("system", new SystemPersistenceIndicatorValueAction());
        
        // nodeconfig
        hashtable.put("nodeconfig", new NodeConfigPersistenceIndicatorValueAction());
        
        // Interface
        hashtable.put("interface", new InterfacePersistenceIndicatorValueAction());
        
        // diskperf
        hashtable.put("diskperf", new DiskperfPersistenceIndicatorValueAction());
        
        // Service
        hashtable.put("service", new ServicePersistenceIndicatorValueAction());
        
        // 采集时间
        hashtable.put("collecttime", new CollectTimePersistenceIndicatorValueAction());
        return hashtable;
    }

    /**
     * getAllPersistenceIndicatorValueActionHashtable:
     * <p>
     *
     * @return  Hashtable<String,PersistenceIndicatorValueAction>
     *          -
     * @since   v1.01
     */
    public Hashtable<String, PersistenceIndicatorValueAction> getAllPersistenceIndicatorValueActionHashtable() {
        if (allPersistenceIndicatorValueActionHashtable == null) {
            allPersistenceIndicatorValueActionHashtable = createAllPersistenceIndicatorValueActionHashtable();
        }
        return allPersistenceIndicatorValueActionHashtable;
    }

    /**
     * setAllPersistenceIndicatorValueActionHashtable:
     * <p>
     *
     * @param   allPersistenceIndicatorValueActionHashtable
     *          -
     * @since   v1.01
     */
    public void setAllPersistenceIndicatorValueActionHashtable(
                    Hashtable<String, PersistenceIndicatorValueAction> allPersistenceIndicatorValueActionHashtable) {
        this.allPersistenceIndicatorValueActionHashtable = allPersistenceIndicatorValueActionHashtable;
    }

    /**
     * createIndicatorValue:
     * <p>重新创建一个新的 {@link IndicatorValue}
     *
     * @param   object
     *          - 值
     * @return  {@link IndicatorValue}
     *          - 指标值
     *
     * @since   v1.01
     */
    public IndicatorValue createIndicatorValue(Object object) {
        IndicatorValue indicatorValue = new IndicatorValue();
        indicatorValue.setIndicatorInfo(getIndicatorValue().getIndicatorInfo());
        indicatorValue.setTime(getIndicatorValue().getTime());
        indicatorValue.setErrorCode(getIndicatorValue().getErrorCode());
        indicatorValue.setValue(object);
        return indicatorValue;
    }
    
}

