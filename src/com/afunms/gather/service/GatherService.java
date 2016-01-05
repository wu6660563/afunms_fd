/*
 * @(#)GatherService.java     v1.01, 2014 1 3
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.afunms.gather.model.IndicatorInfo;
import com.afunms.gather.model.IndicatorValue;
import com.afunms.gather.task.IndicatorGatherTask;
import com.afunms.gather.task.IndicatorGatherTaskManager;

/**
 * ClassName:   GatherService.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 3 16:31:42
 */
public class GatherService {

    /**
     * gatherDataPoolService:
     * <p>采集机数据池服务
     *
     * @since   v1.01
     */
    private GatherDataPoolService gatherDataPoolService;

    /**
     * gatherDataSendService:
     * <p>采集指标数据发送服务
     *
     * @since   v1.01
     */
    private List<GatherDataSendService> gatherDataSendServiceList = new ArrayList<GatherDataSendService>();

    /**
     * gatherDataSendService:
     * <p>采集指标数据发送服务
     *
     * @since   v1.01
     */
    private IndicatorGatherTaskManager indicatorGatherTaskManager;

    /**
     * threadPoolProperties:
     * <p>线程池配置
     *
     * @since   v1.01
     */
    private Properties threadPoolProperties = null;

    /**
     * domain:
     * <p>域
     *
     * @since   v1.01
     */
    private String domain;

    /**
     * start:
     * <p>启动
     *
     * @return
     *
     * @since   v1.01
     */
    public boolean start() {
        setGatherDataPoolService(new GatherDataPoolService(this));
        //setGatherDataSendService(new GatherDataSendService());
        setIndicatorGatherTaskManager(new IndicatorGatherTaskManager(this));
        getIndicatorGatherTaskManager().setThreadPoolProperties(getThreadPoolProperties());
        getIndicatorGatherTaskManager().start();
        return false;
    }

    /**
     * addIndicatorInfo:
     * <p>添加一个采集指标信息
     *
     * @param   indicatorInfo
     *          - 指标信息
     * @return  {@link Boolean}
     *          - 是否成功
     *
     * @since   v1.01
     */
    public boolean addIndicatorInfo(IndicatorInfo indicatorInfo) {
        return getIndicatorGatherTaskManager().addIndicatorInfo(indicatorInfo);
    }


    /**
     * addIndicatorInfo:
     * <p>添加一个采集指标信息列表
     *
     * @param   list
     *          - 指标信息列表
     * @return  {@link Integer}
     *          - 成功个数
     *
     * @since   v1.01
     */
    public int addIndicatorInfo(List<IndicatorInfo> list) {
        return getIndicatorGatherTaskManager().addIndicatorInfo(list);
    }

    /**
     * deleteIndicatorInfo:
     * <p>删除一个采集指标信息
     *
     * @param   indicatorInfo
     *          - 指标信息
     * @return  {@link Boolean}
     *          - 是否成功
     *
     * @since   v1.01
     */
    public boolean deleteIndicatorInfo(IndicatorInfo indicatorInfo) {
        return getIndicatorGatherTaskManager().deleteIndicatorInfo(indicatorInfo);
    }

    /**
     * deleteIndicatorInfo:
     * <p>批量通过指标信息删除采集指标
     *
     * @param   list
     *          - 指标信息
     * @return  {@link Integer}
     *          - 删除成功数量
     *
     * @since   v1.01
     */
    public int deleteIndicatorInfo(List<IndicatorInfo> list) {
        return getIndicatorGatherTaskManager().deleteIndicatorInfo(list);
    }
    /**
     * updateIndicatorValue:
     * <p>更新采集指标值，更新完成后并将该采集值进行发送
     *
     * @param   indicatorId
     *          - 指标 Id
     * @param   indicatorValue
     *          - 本次采集指标值
     *
     * @since   v1.01
     */
    public void updateIndicatorValue(String indicatorId, IndicatorValue indicatorValue) {
        getGatherDataPoolService().updateIndicatorValue(indicatorId, indicatorValue);
        sendIndicatorValue(indicatorId, indicatorValue);
    }

    public void sendIndicatorValue(String indicatorId, IndicatorValue indicatorValue) {
        List<GatherDataSendService> list = getGatherDataSendServiceList();
        for (GatherDataSendService gatherDataSendService : list) {
            gatherDataSendService.send(indicatorValue);
        }
    }

    /**
     * getIndicatorGatherTaskSize:
     * <p>获取指标采集任务的数量
     *
     * @return  {@link Integer}
     *          - 指标采集任务的数量
     *
     * @since   v1.01
     */
    public int getIndicatorGatherTaskSize() {
        return getIndicatorGatherTaskManager().getIndicatorGatherTaskMap().size();
    }

    /**
     * getIndicatorGatherTaskSize:
     * <p>获取正在执行的指标采集任务数量
     *
     * @return  {@link Integer}
     *          - 正在执行的指标采集任务的数量
     *
     * @since   v1.01
     */
    public int getIndicatorGatherTaskRunningSize() {
        return getIndicatorGatherTaskManager().getThreadPool().getCurrentThreadsBusy();
    }

    /**
     * getIndicatorGatherTaskMap:
     * <p>获取采集服务中所有的采集任务
     *
     * @return  {@link Map<String, IndicatorGatherTask>}
     *          - 采集服务中所有的采集任务
     *
     * @since   v1.01
     */
    public Map<String, IndicatorGatherTask> getIndicatorGatherTaskMap() {
        return getIndicatorGatherTaskManager().getIndicatorGatherTaskMap();
    }

    /**
     * getGatherDataPoolService:
     * <p>
     *
     * @return  GatherDataPoolService
     *          -
     * @since   v1.01
     */
    public GatherDataPoolService getGatherDataPoolService() {
        return gatherDataPoolService;
    }

    /**
     * setGatherDataPoolService:
     * <p>
     *
     * @param   gatherDataPoolService
     *          -
     * @since   v1.01
     */
    public void setGatherDataPoolService(GatherDataPoolService gatherDataPoolService) {
        this.gatherDataPoolService = gatherDataPoolService;
    }

    /**
     * addGatherDataSendService:
     * <p>添加采集发送服务
     *
     * @param   gatherDataSendService
     *          - 采集发送服务
     * @since   v1.01
     */
    public void addGatherDataSendService(GatherDataSendService gatherDataSendService) {
        getGatherDataSendServiceList().add(gatherDataSendService);
    }

    /**
     * getGatherDataSendServiceList:
     * <p>
     *
     * @return  List<GatherDataSendService>
     *          -
     * @since   v1.01
     */
    public List<GatherDataSendService> getGatherDataSendServiceList() {
        return gatherDataSendServiceList;
    }

    /**
     * setGatherDataSendServiceList:
     * <p>
     *
     * @param   gatherDataSendServiceList
     *          -
     * @since   v1.01
     */
    public void setGatherDataSendServiceList(
                    List<GatherDataSendService> gatherDataSendServiceList) {
        this.gatherDataSendServiceList = gatherDataSendServiceList;
    }

    /**
     * getIndicatorGatherTaskManager:
     * <p>
     *
     * @return  IndicatorGatherTaskManager
     *          -
     * @since   v1.01
     */
    public IndicatorGatherTaskManager getIndicatorGatherTaskManager() {
        return indicatorGatherTaskManager;
    }
    /**
     * setIndicatorGatherTaskManager:
     * <p>
     *
     * @param   indicatorGatherTaskManager
     *          -
     * @since   v1.01
     */
    public void setIndicatorGatherTaskManager(
                    IndicatorGatherTaskManager indicatorGatherTaskManager) {
        this.indicatorGatherTaskManager = indicatorGatherTaskManager;
    }

    /**
     * getThreadPoolProperties:
     * <p>
     *
     * @return  Properties
     *          -
     * @since   v1.01
     */
    public Properties getThreadPoolProperties() {
        return threadPoolProperties;
    }
    /**
     * setThreadPoolProperties:
     * <p>
     *
     * @param   threadPoolProperties
     *          -
     * @since   v1.01
     */
    public void setThreadPoolProperties(Properties threadPoolProperties) {
        this.threadPoolProperties = threadPoolProperties;
    }

    /**
     * getDomain:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getDomain() {
        return domain;
    }

    /**
     * setDomain:
     * <p>
     *
     * @param   domain
     *          -
     * @since   v1.01
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }
    
}

