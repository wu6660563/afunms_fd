/*
 * @(#)IndicatorGatherTimer.java     v1.01, 2014 1 3
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.task;

import java.util.Timer;

/**
 * ClassName:   IndicatorGatherTimer.java
 * <p>
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 3 17:35:16
 */
public class IndicatorGatherTimer extends Timer {

    /**
     * addTask:
     * <p>�������
     *
     * @param task
     *
     * @since   v1.01
     */
    public void addTask(IndicatorGatherTask task) {
        this.schedule(task, 0, task.getIntervalTime());
    }

    /**
     * deleteTask:
     * <p>ɾ������
     *
     * @param task
     * @return
     *
     * @since   v1.01
     */
    public boolean deleteTask(IndicatorGatherTask task) {
        return task.cancel();
    }
}

