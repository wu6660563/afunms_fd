/*
 * @(#)MaintainTask.java     v1.01, 2014 1 12
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.service;

import java.util.TimerTask;

/**
 * ClassName:   MaintainTask.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 12 13:40:44
 */
public class MaintainTask extends TimerTask {

    /**
     * gatherMaintainService:
     * <p>所属的采集维护服务
     *
     * @since   v1.01
     */
    private GatherMaintainService gatherMaintainService;

    /**
     * MaintainTask.java:
     * 构造方法
     * @param   gatherMaintainService
     *          - 所属的采集维护服务
     *
     * @since   v1.01
     */
    public MaintainTask(GatherMaintainService gatherMaintainService) {
        setGatherMaintainService(gatherMaintainService);
    }

    /**
     * run:
     *
     *
     * @since   v1.01
     * @see java.util.TimerTask#run()
     */
    @Override
    public void run() {
        getGatherMaintainService().checkIndicatorInfo();
    }

    /**
     * getGatherMaintainService:
     * <p>
     *
     * @return  GatherMaintainService
     *          -
     * @since   v1.01
     */
    public GatherMaintainService getGatherMaintainService() {
        return gatherMaintainService;
    }
    /**
     * setGatherMaintainService:
     * <p>
     *
     * @param   gatherMaintainService
     *          -
     * @since   v1.01
     */
    public void setGatherMaintainService(GatherMaintainService gatherMaintainService) {
        this.gatherMaintainService = gatherMaintainService;
    }

    
}

