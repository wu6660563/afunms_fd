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
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 12 13:12:17
 */
public class GatherMaintainService {

    /**
     * logger:
     * <p>��־
     *
     * @since   v1.01
     */
    private static SysLogger logger = SysLogger.getLogger(GatherMaintainService.class.getName());

    /**
     * maintainTimer:
     * <p>ά����ʱ��
     *
     * @since   v1.01
     */
    private Timer maintainTimer;

    /**
     * maintainTask:
     * <p>ά������
     *
     * @since   v1.01
     */
    private MaintainTask maintainTask;

    /**
     * gatherServiceList:
     * <p>�ɼ������б�
     *
     * @since   v1.01
     */
    private List<GatherService> gatherServiceList;

    /**
     * GatherMaintainService.java:
     * ���췽��
     * @param   gatherService
     *          - �����Ĳɼ�����
     *
     * @since   v1.01
     */
    public GatherMaintainService() {
        setMaintainTimer(new Timer());
        setMaintainTask(new MaintainTask(this));
    }

    /**
     * start:
     * <p>����
     *
     *
     * @since   v1.01
     */
    public void start() {
        getMaintainTimer().schedule(getMaintainTask(), 0, 60 * 1000);
    }

    /**
     * checkIndicatorInfo:
     * <p>���ɼ�������ָ����Ϣ�Ƿ��б仯
     *
     *
     * @since   v1.01
     */
    @SuppressWarnings("static-access")
    public void checkIndicatorInfo() {
        try {
            List<GatherService> gatherServiceList = getGatherServiceList();
            logger.info("ϵͳ����" + gatherServiceList.size() + "���ɼ���");
            for (GatherService gatherService : gatherServiceList) {
                GatherIndicatorInfoService gatherIndicatorInfoService = new GatherIndicatorInfoService();
                List<IndicatorInfo> list = gatherIndicatorInfoService.getAllIndicatorInfo(gatherService.getDomain());
                logger.info("�ɼ�������ά������������" + list.size() + "��");
                Map<String, IndicatorInfo> indicatorInfoMap = new Hashtable<String, IndicatorInfo>();
                Map<String, IndicatorGatherTask> indicatorGatherTaskMap = gatherService.getIndicatorGatherTaskManager().getIndicatorGatherTaskMap();
                List<IndicatorInfo> addOrUpdateList = new ArrayList<IndicatorInfo>();
                List<IndicatorInfo> deleteList = new ArrayList<IndicatorInfo>();

                // �Ȳ��ҳ���Ҫ�Բɼ������вɼ���������޸ĺ���ӵĲɼ���Ϣ������ #addOrUpdateList ��
                for (IndicatorInfo indicatorInfo : list) {
                    String key = String.valueOf(indicatorInfo.getGatherIndicators().getId());
                    indicatorInfoMap.put(key, indicatorInfo);
                    if ("0".equalsIgnoreCase(indicatorInfo.getGatherIndicators().getIsCollection())) {
                        // �������ɼ��������
                        continue;
                    }
                    IndicatorGatherTask indicatorGatherTask = indicatorGatherTaskMap.get(key);
                    if (indicatorGatherTask == null) {
                        // ����ɼ������в������òɼ������򽫲ɼ���Ϣ�� #addOrUpdateList ��
                        addOrUpdateList.add(indicatorInfo);
                    } else if (checkUpdateIndicatorInfo(indicatorGatherTask.getIndicatorInfo(), indicatorInfo)){
                        // ����ɼ������иòɼ�����Ĳɼ���Ϣ�����ڵĲ���ͬ����Ҳ��Ӳɼ���Ϣ�� #addOrUpdateList ��
                        addOrUpdateList.add(indicatorInfo);
                    }
                }

                // Ȼ����ҳ���Ҫɾ���Ĳɼ���Ϣ
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
                    logger.info("�����ڴ��еĲɼ�����...");
                    logger.info("��ӻ��߸��µĲɼ��������Ϊ��" + addOrUpdateList.size());
                    logger.info("ɾ���Ĳɼ��������Ϊ��" + deleteList.size());
                    logger.info("��ǰ�Ĳɼ��������Ϊ��" + gatherService.getIndicatorGatherTaskSize());
                    logger.info("��ǰ�Ĳɼ��̳߳��У����ڲɼ�������Ϊ��" + gatherService.getIndicatorGatherTaskRunningSize());
                }
                gatherService.addIndicatorInfo(addOrUpdateList);
                gatherService.deleteIndicatorInfo(deleteList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("���ɼ����вɼ������������", e);
        }
    }

    /**
     * checkUpdateIndicatorInfo:
     * <p>����Ƿ���Ҫ�޸Ĳɼ���Ϣ
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
     * <p>��� {@link NodeDTO} �Ƿ���ͬ
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
     * <p>���ָ��
     *
     * @param   oldIndicators
     *          - ԭָ��
     * @param   newIndicators
     *          - ��ָ��
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

