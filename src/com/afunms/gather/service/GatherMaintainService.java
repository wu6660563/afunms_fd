/*
 * @(#)GatherMaintainService.java     v1.01, 2014 1 12
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import com.afunms.common.util.SysLogger;
import com.afunms.gather.model.IndicatorInfo;
import com.afunms.gather.task.IndicatorGatherTask;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;

/**
 * ClassName:   GatherMaintainService.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 12 13:12:17
 */
public class GatherMaintainService {

    /**
     * logger:
     * <p>日志
     *
     * @since   v1.01
     */
    private static SysLogger logger = SysLogger.getLogger(GatherMaintainService.class.getName());

    /**
     * maintainTimer:
     * <p>维护计时器
     *
     * @since   v1.01
     */
    private Timer maintainTimer;

    /**
     * maintainTask:
     * <p>维护任务
     *
     * @since   v1.01
     */
    private MaintainTask maintainTask;

    /**
     * gatherServiceList:
     * <p>采集服务列表
     *
     * @since   v1.01
     */
    private List<GatherService> gatherServiceList;

    /**
     * GatherMaintainService.java:
     * 构造方法
     * @param   gatherService
     *          - 所属的采集服务
     *
     * @since   v1.01
     */
    public GatherMaintainService() {
        setMaintainTimer(new Timer());
        setMaintainTask(new MaintainTask(this));
    }

    /**
     * start:
     * <p>启动
     *
     *
     * @since   v1.01
     */
    public void start() {
        getMaintainTimer().schedule(getMaintainTask(), 0, 60 * 1000);
    }

    /**
     * checkIndicatorInfo:
     * <p>检查采集服务中指标信息是否有变化
     *
     *
     * @since   v1.01
     */
    @SuppressWarnings("static-access")
    public void checkIndicatorInfo() {
        try {
            List<GatherService> gatherServiceList = getGatherServiceList();
            logger.info("系统共有" + gatherServiceList.size() + "个采集机");
            for (GatherService gatherService : gatherServiceList) {
                GatherIndicatorInfoService gatherIndicatorInfoService = new GatherIndicatorInfoService();
                List<IndicatorInfo> list = gatherIndicatorInfoService.getAllIndicatorInfo(gatherService.getDomain());
                logger.info("采集机本次维护的任务数：" + list.size() + "个");
                Map<String, IndicatorInfo> indicatorInfoMap = new Hashtable<String, IndicatorInfo>();
                Map<String, IndicatorGatherTask> indicatorGatherTaskMap = gatherService.getIndicatorGatherTaskManager().getIndicatorGatherTaskMap();
                List<IndicatorInfo> addOrUpdateList = new ArrayList<IndicatorInfo>();
                List<IndicatorInfo> deleteList = new ArrayList<IndicatorInfo>();

                // 先查找出需要对采集服务中采集任务进行修改和添加的采集信息加入至 #addOrUpdateList 中
                for (IndicatorInfo indicatorInfo : list) {
                    String key = String.valueOf(indicatorInfo.getGatherIndicators().getId());
                    indicatorInfoMap.put(key, indicatorInfo);
                    if ("0".equalsIgnoreCase(indicatorInfo.getGatherIndicators().getIsCollection())) {
                        // 如果无需采集，则过滤
                        continue;
                    }
                    IndicatorGatherTask indicatorGatherTask = indicatorGatherTaskMap.get(key);
                    if (indicatorGatherTask == null) {
                        // 如果采集服务中不包含该采集任务，则将采集信息至 #addOrUpdateList 中
                        addOrUpdateList.add(indicatorInfo);
                    } else if (checkUpdateIndicatorInfo(indicatorGatherTask.getIndicatorInfo(), indicatorInfo)){
                        // 如果采集服务中该采集任务的采集信息与现在的不相同，则也添加采集信息至 #addOrUpdateList 中
                        addOrUpdateList.add(indicatorInfo);
                    }
                }

                // 然后查找出需要删除的采集信息
                Iterator<String> iterator = indicatorGatherTaskMap.keySet().iterator();
                while (iterator.hasNext()) {
                    String taskId = iterator.next();
                    IndicatorInfo indicatorInfo = indicatorInfoMap.get(taskId);
                    if (indicatorInfo == null) {
                        deleteList.add(indicatorInfo);
                    } else if ("0".equals(indicatorInfo.getGatherIndicators().getIsCollection())) {
                        deleteList.add(indicatorInfo);
                    }
                }

                if (logger.isInfoEnabled()) {
                    logger.info("更新内存中的采集任务...");
                    logger.info("添加或者更新的采集任务个数为：" + addOrUpdateList.size());
                    logger.info("删除的采集任务个数为：" + deleteList.size());
                    logger.info("当前的采集任务个数为：" + gatherService.getIndicatorGatherTaskSize());
                    logger.info("当前的采集线程池中，正在采集任务数为：" + gatherService.getIndicatorGatherTaskRunningSize());
                }
                gatherService.addIndicatorInfo(addOrUpdateList);
                gatherService.deleteIndicatorInfo(deleteList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("检查采集机中采集任务出错！！！", e);
        }
    }

    /**
     * checkUpdateIndicatorInfo:
     * <p>检查是否需要修改采集信息
     *
     * @return
     *
     * @since   v1.01
     */
    public boolean checkUpdateIndicatorInfo(IndicatorInfo oldIndicatorInfo, IndicatorInfo newIndicatorInfo) {
        NodeGatherIndicators oldIndicators = oldIndicatorInfo.getGatherIndicators();
        NodeGatherIndicators newIndicators = newIndicatorInfo.getGatherIndicators();
        NodeDTO oldNode = oldIndicatorInfo.getNodeDTO();
        NodeDTO newNode = newIndicatorInfo.getNodeDTO();
        return checkIndicator(oldIndicators, newIndicators) || checkNodeDTO(oldNode, newNode);
    }
    /**
     * checkNodeDTO:
     * <p>检查 {@link NodeDTO} 是否相同
     *
     * @param oldNode
     * @param newNode
     * @return
     *
     * @since   v1.01
     */
    public boolean checkNodeDTO(NodeDTO oldNode, NodeDTO newNode) {
        boolean result = false;
        if (!oldNode.getNodeid().equals(newNode.getNodeid())) {
            result = true;
        } else if (!oldNode.getIpaddress().equals(newNode.getIpaddress())) {
            result = true;
        } else if (!oldNode.getType().equals(newNode.getType())) {
            result = true;
        } else if (!oldNode.getSubtype().equals(newNode.getSubtype())) {
            result = true;
        } else if (!oldNode.getSysOid().equals(newNode.getSysOid())) {
            result = true;
        }
        return result;
    }

    /**
     * checkIndicator:
     * <p>检查指标
     *
     * @param   oldIndicators
     *          - 原指标
     * @param   newIndicators
     *          - 新指标
     * @return
     *
     * @since   v1.01
     */
    public boolean checkIndicator(NodeGatherIndicators oldIndicators, NodeGatherIndicators newIndicators) {
        boolean result = false;
        if (!oldIndicators.getNodeid().equals(newIndicators.getNodeid())) {
            result = true;
        } else if (!oldIndicators.getType().equals(newIndicators.getType())) {
            result = true;
        } else if (!oldIndicators.getSubtype().equals(newIndicators.getSubtype())) {
            result = true;
        } else if (!oldIndicators.getName().equals(newIndicators.getName())) {
            result = true;
        } else if (!oldIndicators.getInterval_unit().equals(newIndicators.getInterval_unit())) {
            result = true;
        } else if (!oldIndicators.getPoll_interval().equals(newIndicators.getPoll_interval())) {
            result = true;
        }
        return result;
    }

    /**
     * getGatherServiceList:
     * <p>
     *
     * @return  List<GatherService>
     *          -
     * @since   v1.01
     */
    public List<GatherService> getGatherServiceList() {
        return gatherServiceList;
    }

    /**
     * setGatherServiceList:
     * <p>
     *
     * @param   gatherServiceList
     *          -
     * @since   v1.01
     */
    public void setGatherServiceList(List<GatherService> gatherServiceList) {
        this.gatherServiceList = gatherServiceList;
    }

    /**
     * getMaintainTimer:
     * <p>
     *
     * @return  Timer
     *          -
     * @since   v1.01
     */
    public Timer getMaintainTimer() {
        return maintainTimer;
    }

    /**
     * setMaintainTimer:
     * <p>
     *
     * @param   maintainTimer
     *          -
     * @since   v1.01
     */
    public void setMaintainTimer(Timer maintainTimer) {
        this.maintainTimer = maintainTimer;
    }

    /**
     * getMaintainTask:
     * <p>
     *
     * @return  MaintainTask
     *          -
     * @since   v1.01
     */
    public MaintainTask getMaintainTask() {
        return maintainTask;
    }

    /**
     * setMaintainTask:
     * <p>
     *
     * @param   maintainTask
     *          -
     * @since   v1.01
     */
    public void setMaintainTask(MaintainTask maintainTask) {
        this.maintainTask = maintainTask;
    }

}

