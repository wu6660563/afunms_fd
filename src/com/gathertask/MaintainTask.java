package com.gathertask;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TimerTask;
import java.util.Hashtable;
import com.gathertask.dao.Taskdao;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.threadPool.ThreadPool;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.gathertask.TaskManager;

/**
 * 
 * 维护采集任务 5分钟检查一次采集任务，检查已经在跑的任务与被停止的
 * 
 * @author konglq
 * 
 */
public class MaintainTask extends TimerTask {

    private static SysLogger logger = SysLogger.getLogger(MaintainTask.class.getName());

    @SuppressWarnings("static-access")
    @Override
    public void run() {
        try {
            Taskdao taskdao = new Taskdao();
            Hashtable nlist = taskdao.GetRunTaskList();
            TaskManager taskManager = TaskManager.getInstance();
            Hashtable<String, BaseTask> taskHashtable = taskManager.getTimerTaskHashtable();

            List<NodeGatherIndicators> addOrUpdateList = new ArrayList<NodeGatherIndicators>();
            List<NodeGatherIndicators> deleteList = new ArrayList<NodeGatherIndicators>();
            
            if (logger.isInfoEnabled()) {
                logger.info("执行内存采集任务与数据采集指标信息对比...");
                logger.info("1.循环数据库采集指标信息，与内存中信息对比，确定需要添加以及需要修改采集时间的采集任务");
            }
            if (null != nlist && nlist.size() > 0) {
                Enumeration it1 = nlist.elements();
                while (it1.hasMoreElements()) {
                    // 开始对取出数据库中采集指标信息
                    NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators) it1.nextElement();
                    BaseTask baseTask = taskHashtable.get(String.valueOf(nodeGatherIndicators.getId()));
                    if (baseTask != null && "1".equals(nodeGatherIndicators.getIsCollection())) {
                        // 如果有该采集任务并且该指标需要进行采集
                        String pollIntervalTime = nodeGatherIndicators.getPoll_interval();
                        String intervalUnit = nodeGatherIndicators.getInterval_unit();

                        if (pollIntervalTime.equals(baseTask.getGather().getPoll_interval())
                                && intervalUnit.equals(baseTask.getGather().getInterval_unit())) {
                            // 如果相同则不作任何改动
                        } else {
                            // 如果已修改将任务放到加入或者修改列表中
                            if (logger.isInfoEnabled()) {
                                logger.info("修改采集任务：" + nodeGatherIndicators.getId() +
                                        " 的采集时间为：" + pollIntervalTime + " * " + intervalUnit);
                            }
                            addOrUpdateList.add(nodeGatherIndicators);
                        }
                    } else if (baseTask == null && "1".equals(nodeGatherIndicators.getIsCollection())) {
                        // 如果没有该任务并且该指标需要进行采集，放到加入或者修改列表中
                        logger.info("添加采集任务：" + nodeGatherIndicators.getId() + "==" + nodeGatherIndicators.getName());
                        addOrUpdateList.add(nodeGatherIndicators);
                    }
                }

                if (logger.isInfoEnabled()) {
                    logger.info("2.循环内存中采集指标信息，与数据库信息对比，确定需要删除的采集任务");
                }

                // 迭代内存列表查找当前定时的任务
                if (taskHashtable.size() > 0) {
                    it1 = taskHashtable.elements();
                    while (it1.hasMoreElements()) {
                        BaseTask baseTask = (BaseTask) it1.nextElement();
                        NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators) nlist.get(baseTask.getTaskid());
                        if (nodeGatherIndicators == null) {
                            if (logger.isInfoEnabled()) {
                                logger.info("数据库中已经将采集指标:" + baseTask.getGather().getId() + " 删除");
                            }
                            deleteList.add(baseTask.getGather());
                        } else if (nodeGatherIndicators != null && "0".equals(nodeGatherIndicators.getIsCollection())) {
                            if (logger.isInfoEnabled()) {
                                logger.info("数据库中已经将采集指标:" + nodeGatherIndicators.getId() + " 取消采集");
                            }
                            deleteList.add(nodeGatherIndicators);
                        }
                    }
                }
            } else {
                logger.info("数据库中已经将采集指标清空，删除所有在内存中执行的采集任务");
            }

            if (logger.isInfoEnabled()) {
                logger.info("更新内存中的采集任务...");
            }
            taskManager.addNodeGatherIndicatorsToTimer(addOrUpdateList);
            taskManager.deleteNodeGatherIndicatorsFromTimer(deleteList);

            logger.info("添加或者更新的采集任务个数为：" + addOrUpdateList.size());
            logger.info("删除的采集任务个数为：" + deleteList.size());
            logger.info("当前的采集任务个数为：" + taskManager.getTimerTaskHashtable().size());
            ThreadPool threadPool = taskManager.getThreadPool();
            logger.info("当前的采集线程池中，采集线程总数为：" + threadPool.getCurrentThreadCount());
            logger.info("当前的采集线程池中，正在采集线程数为：" + threadPool.getCurrentThreadsBusy());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
