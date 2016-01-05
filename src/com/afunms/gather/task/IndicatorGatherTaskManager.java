/*
 * @(#)IndicatorGatherTaskManager.java     v1.01, 2014 1 3
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.task;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.afunms.common.util.threadPool.ThreadPool;
import com.afunms.gather.indicator.base.BaseIndicatorGather;
import com.afunms.gather.model.IndicatorInfo;
import com.afunms.gather.service.GatherService;

/**
 * ClassName:   IndicatorGatherTaskManager.java
 * <p>
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 3 15:28:50
 */
public class IndicatorGatherTaskManager {

    /**
     * gatherService:
     * <p>�����Ĳɼ�����
     *
     * @since   v1.01
     */
    private GatherService gatherService;

    /**
     * threadPool:
     * <p>�ɼ����̳߳�
     *
     * @since   v1.01
     */
    private ThreadPool threadPool = null;

    /**
     * threadPoolProperties:
     * <p>�ɼ����̳߳�����
     *
     * @since   v1.01
     */
    private Properties threadPoolProperties = null;

    /**
     * indicatorGatherTaskTimer:
     * <p>�ɼ�����Ķ�ʱ��
     *
     * @since   v1.01
     */
    private IndicatorGatherTimer indicatorGatherTimer = null;

    /**
     * taskMap:
     * <p>����ӳ���
     *
     * @since   v1.01
     */
    private Map<String, IndicatorGatherTask> indicatorGatherTaskMap = new Hashtable<String, IndicatorGatherTask>();

    /**
     * IndicatorGatherTaskManager.java:
     * ���췽��
     * @param   gatherService
     *          - �����Ĳɼ�����
     *
     * @since   v1.01
     */
    public IndicatorGatherTaskManager(GatherService gatherService) {
        this.gatherService = gatherService;
    }
    
    /**
     * start:
     * <p>����
     *
     * @return
     *
     * @since   v1.01
     */
    public boolean start() {
        indicatorGatherTimer = new IndicatorGatherTimer();
        ThreadPool threadPool = getThreadPool();
        if (threadPool == null) {
            setThreadPool(new ThreadPool(getThreadPoolProperties()));
        }
        return getThreadPool().start();
    }

    /**
     * addIndicatorInfo:
     * <p>��Ӳɼ�ָ����Ϣ
     *
     * @param indicatorInfo
     * @return
     *
     * @since   v1.01
     */
    public boolean addIndicatorInfo(IndicatorInfo indicatorInfo) {
        IndicatorGatherTask indicatorGatherTask = createIndicatorGatherTask(indicatorInfo);
        getIndicatorGatherTimer().addTask(indicatorGatherTask);
        getIndicatorGatherTaskMap().put(indicatorGatherTask.getId(), indicatorGatherTask);
        return true;
    }

    /**
     * addIndicatorInfo:
     * <p>������Ӳɼ�ָ����Ϣ�б�
     *
     * @param   list
     *          - �ɼ�ָ����Ϣ�б�
     * @return  {@link Integer}
     *          - ��ӳɹ�ָ����Ϣ�б�����
     *
     * @since   v1.01
     */
    public int addIndicatorInfo(List<IndicatorInfo> list) {
        int successNum = 0;
        for (IndicatorInfo indicatorInfo : list) {
            if (addIndicatorInfo(indicatorInfo)) {
                successNum ++;
            }
        }
        return successNum;
    }

    /**
     * deleteIndicatorInfo:
     * <p>ͨ��ָ�� Id ɾ���ɼ�ָ��
     *
     * @param   indicatorId
     *          - ָ�� Id
     * @return  {@link Boolean}
     *          - ɾ���Ƿ�ɹ�
     *
     * @since   v1.01
     */
    public boolean deleteIndicatorInfo(String indicatorId) {
        IndicatorGatherTask indicatorGatherTask = getIndicatorGatherTaskMap().remove(indicatorId);
        boolean result = false;
        if (indicatorGatherTask!= null) {
            result = getIndicatorGatherTimer().deleteTask(indicatorGatherTask);
        }
        return result;
    }

    /**
     * deleteIndicatorInfo:
     * <p>ͨ��ָ����Ϣɾ���ɼ�ָ��
     *
     * @param   indicatorInfo
     *          - ָ����Ϣ
     * @return  {@link Boolean}
     *          - ɾ���Ƿ�ɹ�
     *
     * @since   v1.01
     */
    public boolean deleteIndicatorInfo(IndicatorInfo indicatorInfo) {
        return deleteIndicatorInfo(String.valueOf(indicatorInfo.getGatherIndicators().getId()));
    }

    /**
     * deleteIndicatorInfo:
     * <p>ͨ���ɼ�ָ������ɾ���ɼ�ָ��
     *
     * @param   indicatorGatherTask
     *          - �ɼ�ָ������
     * @return  {@link Boolean}
     *          - ɾ���Ƿ�ɹ�
     *
     * @since   v1.01
     */
    public boolean deleteIndicatorInfo(IndicatorGatherTask indicatorGatherTask) {
        return deleteIndicatorInfo(indicatorGatherTask.getId());
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
        int successNum = 0;
        for (IndicatorInfo indicatorInfo : list) {
            if (deleteIndicatorInfo(indicatorInfo)) {
                successNum ++;
            }
        }
        return successNum;
    }

    /**
     * ͨ���ɼ�ָ����������
     * @param nodeGatherIndicators
     * @return
     */
    @SuppressWarnings("unchecked")
    public IndicatorGatherTask createIndicatorGatherTask(IndicatorInfo indicatorInfo) {
        // ������ʱ��
        String indicatorId = String.valueOf(indicatorInfo.getGatherIndicators().getId());
        String intervalUnit = indicatorInfo.getGatherIndicators().getInterval_unit();
        String classPath = indicatorInfo.getGatherIndicators().getClasspath();
        long intervalTime = Integer.parseInt(indicatorInfo.getGatherIndicators().getPoll_interval());
        if("m".equalsIgnoreCase(intervalUnit)) {
            intervalTime = intervalTime * 1000 * 60;
        }
        if("h".equalsIgnoreCase(intervalUnit)) {
            intervalTime = intervalTime * 1000 * 60 * 60;
        }
        if("d".equalsIgnoreCase(intervalUnit)) {
            intervalTime = intervalTime * 1000 * 60 * 60 * 24;
        }

        BaseIndicatorGather baseIndicatorGather = null;
        try {
            Class<BaseIndicatorGather> clazz = (Class<BaseIndicatorGather>) Class.forName(classPath);
            baseIndicatorGather = clazz.newInstance();
            baseIndicatorGather.setIndicatorInfo(indicatorInfo);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        IndicatorGatherTask indicatorGatherTask = new IndicatorGatherTask();
        indicatorGatherTask.setId(indicatorId);
        indicatorGatherTask.setIndicatorInfo(indicatorInfo);
        indicatorGatherTask.setIntervalTime(intervalTime);
        indicatorGatherTask.setIndicatorGather(baseIndicatorGather);
        indicatorGatherTask.setIndicatorGatherTaskManager(this);
        return indicatorGatherTask;
    }

    /**
     * getGatherService:
     * <p>
     *
     * @return  GatherService
     *          -
     * @since   v1.01
     */
    public GatherService getGatherService() {
        return gatherService;
    }

    /**
     * setGatherService:
     * <p>
     *
     * @param   gatherService
     *          -
     * @since   v1.01
     */
    public void setGatherService(GatherService gatherService) {
        this.gatherService = gatherService;
    }

    /**
     * getThreadPool:
     * <p>
     *
     * @return  ThreadPool
     *          -
     * @since   v1.01
     */
    public ThreadPool getThreadPool() {
        return threadPool;
    }

    /**
     * setThreadPool:
     * <p>
     *
     * @param   threadPool
     *          -
     * @since   v1.01
     */
    public void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
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
     * getIndicatorGatherTimer:
     * <p>
     *
     * @return  IndicatorGatherTimer
     *          -
     * @since   v1.01
     */
    public IndicatorGatherTimer getIndicatorGatherTimer() {
        return indicatorGatherTimer;
    }

    /**
     * setIndicatorGatherTimer:
     * <p>
     *
     * @param   indicatorGatherTimer
     *          -
     * @since   v1.01
     */
    public void setIndicatorGatherTimer(IndicatorGatherTimer indicatorGatherTimer) {
        this.indicatorGatherTimer = indicatorGatherTimer;
    }

    /**
     * getIndicatorGatherTaskMap:
     * <p>
     *
     * @return  Map<String,IndicatorGatherTask>
     *          -
     * @since   v1.01
     */
    public Map<String, IndicatorGatherTask> getIndicatorGatherTaskMap() {
        return indicatorGatherTaskMap;
    }

    /**
     * setIndicatorGatherTaskMap:
     * <p>
     *
     * @param   indicatorGatherTaskMap
     *          -
     * @since   v1.01
     */
    public void setIndicatorGatherTaskMap(
                    Map<String, IndicatorGatherTask> indicatorGatherTaskMap) {
        this.indicatorGatherTaskMap = indicatorGatherTaskMap;
    }

    
}

