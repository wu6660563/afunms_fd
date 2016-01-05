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
 * ά���ɼ����� 5���Ӽ��һ�βɼ����񣬼���Ѿ����ܵ������뱻ֹͣ��
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
                logger.info("ִ���ڴ�ɼ����������ݲɼ�ָ����Ϣ�Ա�...");
                logger.info("1.ѭ�����ݿ�ɼ�ָ����Ϣ�����ڴ�����Ϣ�Աȣ�ȷ����Ҫ����Լ���Ҫ�޸Ĳɼ�ʱ��Ĳɼ�����");
            }
            if (null != nlist && nlist.size() > 0) {
                Enumeration it1 = nlist.elements();
                while (it1.hasMoreElements()) {
                    // ��ʼ��ȡ�����ݿ��вɼ�ָ����Ϣ
                    NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators) it1.nextElement();
                    BaseTask baseTask = taskHashtable.get(String.valueOf(nodeGatherIndicators.getId()));
                    if (baseTask != null && "1".equals(nodeGatherIndicators.getIsCollection())) {
                        // ����иòɼ������Ҹ�ָ����Ҫ���вɼ�
                        String pollIntervalTime = nodeGatherIndicators.getPoll_interval();
                        String intervalUnit = nodeGatherIndicators.getInterval_unit();

                        if (pollIntervalTime.equals(baseTask.getGather().getPoll_interval())
                                && intervalUnit.equals(baseTask.getGather().getInterval_unit())) {
                            // �����ͬ�����κθĶ�
                        } else {
                            // ������޸Ľ�����ŵ���������޸��б���
                            if (logger.isInfoEnabled()) {
                                logger.info("�޸Ĳɼ�����" + nodeGatherIndicators.getId() +
                                        " �Ĳɼ�ʱ��Ϊ��" + pollIntervalTime + " * " + intervalUnit);
                            }
                            addOrUpdateList.add(nodeGatherIndicators);
                        }
                    } else if (baseTask == null && "1".equals(nodeGatherIndicators.getIsCollection())) {
                        // ���û�и������Ҹ�ָ����Ҫ���вɼ����ŵ���������޸��б���
                        logger.info("��Ӳɼ�����" + nodeGatherIndicators.getId() + "==" + nodeGatherIndicators.getName());
                        addOrUpdateList.add(nodeGatherIndicators);
                    }
                }

                if (logger.isInfoEnabled()) {
                    logger.info("2.ѭ���ڴ��вɼ�ָ����Ϣ�������ݿ���Ϣ�Աȣ�ȷ����Ҫɾ���Ĳɼ�����");
                }

                // �����ڴ��б���ҵ�ǰ��ʱ������
                if (taskHashtable.size() > 0) {
                    it1 = taskHashtable.elements();
                    while (it1.hasMoreElements()) {
                        BaseTask baseTask = (BaseTask) it1.nextElement();
                        NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators) nlist.get(baseTask.getTaskid());
                        if (nodeGatherIndicators == null) {
                            if (logger.isInfoEnabled()) {
                                logger.info("���ݿ����Ѿ����ɼ�ָ��:" + baseTask.getGather().getId() + " ɾ��");
                            }
                            deleteList.add(baseTask.getGather());
                        } else if (nodeGatherIndicators != null && "0".equals(nodeGatherIndicators.getIsCollection())) {
                            if (logger.isInfoEnabled()) {
                                logger.info("���ݿ����Ѿ����ɼ�ָ��:" + nodeGatherIndicators.getId() + " ȡ���ɼ�");
                            }
                            deleteList.add(nodeGatherIndicators);
                        }
                    }
                }
            } else {
                logger.info("���ݿ����Ѿ����ɼ�ָ����գ�ɾ���������ڴ���ִ�еĲɼ�����");
            }

            if (logger.isInfoEnabled()) {
                logger.info("�����ڴ��еĲɼ�����...");
            }
            taskManager.addNodeGatherIndicatorsToTimer(addOrUpdateList);
            taskManager.deleteNodeGatherIndicatorsFromTimer(deleteList);

            logger.info("��ӻ��߸��µĲɼ��������Ϊ��" + addOrUpdateList.size());
            logger.info("ɾ���Ĳɼ��������Ϊ��" + deleteList.size());
            logger.info("��ǰ�Ĳɼ��������Ϊ��" + taskManager.getTimerTaskHashtable().size());
            ThreadPool threadPool = taskManager.getThreadPool();
            logger.info("��ǰ�Ĳɼ��̳߳��У��ɼ��߳�����Ϊ��" + threadPool.getCurrentThreadCount());
            logger.info("��ǰ�Ĳɼ��̳߳��У����ڲɼ��߳���Ϊ��" + threadPool.getCurrentThreadsBusy());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
