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
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 3 16:31:42
 */
public class GatherService {

    /**
     * gatherDataPoolService:
     * <p>�ɼ������ݳط���
     *
     * @since   v1.01
     */
    private GatherDataPoolService gatherDataPoolService;

    /**
     * gatherDataSendService:
     * <p>�ɼ�ָ�����ݷ��ͷ���
     *
     * @since   v1.01
     */
    private List<GatherDataSendService> gatherDataSendServiceList = new ArrayList<GatherDataSendService>();

    /**
     * gatherDataSendService:
     * <p>�ɼ�ָ�����ݷ��ͷ���
     *
     * @since   v1.01
     */
    private IndicatorGatherTaskManager indicatorGatherTaskManager;

    /**
     * threadPoolProperties:
     * <p>�̳߳�����
     *
     * @since   v1.01
     */
    private Properties threadPoolProperties = null;

    /**
     * domain:
     * <p>��
     *
     * @since   v1.01
     */
    private String domain;

    /**
     * start:
     * <p>����
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
     * <p>���һ���ɼ�ָ����Ϣ
     *
     * @param   indicatorInfo
     *          - ָ����Ϣ
     * @return  {@link Boolean}
     *          - �Ƿ�ɹ�
     *
     * @since   v1.01
     */
    public boolean addIndicatorInfo(IndicatorInfo indicatorInfo) {
        return getIndicatorGatherTaskManager().addIndicatorInfo(indicatorInfo);
    }


    /**
     * addIndicatorInfo:
     * <p>���һ���ɼ�ָ����Ϣ�б�
     *
     * @param   list
     *          - ָ����Ϣ�б�
     * @return  {@link Integer}
     *          - �ɹ�����
     *
     * @since   v1.01
     */
    public int addIndicatorInfo(List<IndicatorInfo> list) {
        return getIndicatorGatherTaskManager().addIndicatorInfo(list);
    }

    /**
     * deleteIndicatorInfo:
     * <p>ɾ��һ���ɼ�ָ����Ϣ
     *
     * @param   indicatorInfo
     *          - ָ����Ϣ
     * @return  {@link Boolean}
     *          - �Ƿ�ɹ�
     *
     * @since   v1.01
     */
    public boolean deleteIndicatorInfo(IndicatorInfo indicatorInfo) {
        return getIndicatorGatherTaskManager().deleteIndicatorInfo(indicatorInfo);
    }

    /**
     * deleteIndicatorInfo:
     * <p>����ͨ��ָ����Ϣɾ���ɼ�ָ��
     *
     * @param   list
     *          - ָ����Ϣ
     * @return  {@link Integer}
     *          - ɾ���ɹ�����
     *
     * @since   v1.01
     */
    public int deleteIndicatorInfo(List<IndicatorInfo> list) {
        return getIndicatorGatherTaskManager().deleteIndicatorInfo(list);
    }
    /**
     * updateIndicatorValue:
     * <p>���²ɼ�ָ��ֵ��������ɺ󲢽��òɼ�ֵ���з���
     *
     * @param   indicatorId
     *          - ָ�� Id
     * @param   indicatorValue
     *          - ���βɼ�ָ��ֵ
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
     * <p>��ȡָ��ɼ����������
     *
     * @return  {@link Integer}
     *          - ָ��ɼ����������
     *
     * @since   v1.01
     */
    public int getIndicatorGatherTaskSize() {
        return getIndicatorGatherTaskManager().getIndicatorGatherTaskMap().size();
    }

    /**
     * getIndicatorGatherTaskSize:
     * <p>��ȡ����ִ�е�ָ��ɼ���������
     *
     * @return  {@link Integer}
     *          - ����ִ�е�ָ��ɼ����������
     *
     * @since   v1.01
     */
    public int getIndicatorGatherTaskRunningSize() {
        return getIndicatorGatherTaskManager().getThreadPool().getCurrentThreadsBusy();
    }

    /**
     * getIndicatorGatherTaskMap:
     * <p>��ȡ�ɼ����������еĲɼ�����
     *
     * @return  {@link Map<String, IndicatorGatherTask>}
     *          - �ɼ����������еĲɼ�����
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
     * <p>��Ӳɼ����ͷ���
     *
     * @param   gatherDataSendService
     *          - �ɼ����ͷ���
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

