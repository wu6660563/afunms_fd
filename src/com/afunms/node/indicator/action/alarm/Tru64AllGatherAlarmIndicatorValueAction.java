/*
 * @(#)Tru64AllGatherAlarmIndicatorValueAction.java     v1.01, 2014 1 16
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.alarm;

import java.util.Hashtable;
import java.util.Iterator;

import com.afunms.gather.model.IndicatorValue;
import com.afunms.node.indicator.action.AlarmIndicatorValueAction;

/**
 * ClassName:   Tru64AllGatherAlarmIndicatorValueAction.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 16 00:36:15
 */
public class Tru64AllGatherAlarmIndicatorValueAction extends
                AlarmIndicatorValueAction {

    private Hashtable<String, AlarmIndicatorValueAction> allAlarmIndicatorValueActionHashtable;
    /**
     * executeToAlarm:
     *
     *
     * @since   v1.01
     * @see com.afunms.node.indicator.action.AlarmIndicatorValueAction#executeToAlarm()
     */
    @SuppressWarnings("unchecked")
    @Override
    public void executeToAlarm() {
        Hashtable<String, AlarmIndicatorValueAction> allAlarmIndicatorValueActionHashtable = getAllAlarmIndicatorValueActionHashtable();
        Hashtable<String, Object> hashtable = (Hashtable<String, Object>) getIndicatorValue().getValue();
        Iterator<String> iterator = allAlarmIndicatorValueActionHashtable.keySet().iterator();
        while (iterator.hasNext()) {
            try {
                String key = iterator.next();
                AlarmIndicatorValueAction action = allAlarmIndicatorValueActionHashtable.get(key);
                Object value = hashtable.get(key);
                if (value == null) {
                    continue;
                }
                action.setIndicatorValue(createIndicatorValue(value));
                action.executeToAlarm();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    public Hashtable<String, AlarmIndicatorValueAction> createAllAlarmIndicatorValueActionHashtable() {
        Hashtable<String, AlarmIndicatorValueAction> hashtable = new Hashtable<String, AlarmIndicatorValueAction>();
        // 磁盘
        hashtable.put("disk", new DiskAlarmIndicatorValueAction());

        // CPU
        hashtable.put("cpu", new CpuAlarmIndicatorValueAction());

        // Memory
        hashtable.put("memory", new MemoryAlarmIndicatorValueAction());

        // Process
        hashtable.put("process", new ProcessAlarmIndicatorValueAction());

        return hashtable;
    }

    /**
     * getAllAlarmIndicatorValueActionHashtable:
     * <p>
     *
     * @return  Hashtable<String,AlarmIndicatorValueAction>
     *          -
     * @since   v1.01
     */
    public Hashtable<String, AlarmIndicatorValueAction> getAllAlarmIndicatorValueActionHashtable() {
        if (allAlarmIndicatorValueActionHashtable == null) {
            allAlarmIndicatorValueActionHashtable = createAllAlarmIndicatorValueActionHashtable();
        }
        return allAlarmIndicatorValueActionHashtable;
    }

    /**
     * setAllAlarmIndicatorValueActionHashtable:
     * <p>
     *
     * @param   allAlarmIndicatorValueActionHashtable
     *          -
     * @since   v1.01
     */
    public void setAllAlarmIndicatorValueActionHashtable(
                    Hashtable<String, AlarmIndicatorValueAction> allAlarmIndicatorValueActionHashtable) {
        this.allAlarmIndicatorValueActionHashtable = allAlarmIndicatorValueActionHashtable;
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

