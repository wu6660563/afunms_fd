package com.afunms.alarm.service;

import java.util.ArrayList;
import java.util.List;

import com.afunms.common.util.SysLogger;
import com.afunms.event.dao.CheckEventDao;
import com.afunms.event.model.CheckEvent;
import com.afunms.indicators.model.NodeDTO;

/**
 * ���ڻ�ȡ�豸��ǰ�澯��Ϣ�ķ�����
 * @author ����
 */
public class NodeAlarmService {

    private static SysLogger logger = SysLogger.getLogger(NodeAlarmService.class.getName());

    /**
     * ��ȡ�б����豸����߸澯�ȼ�
     * @param list
     * @return {@link Integer}
     *          -- ��߸澯�ȼ�
     */
    public int getMaxAlarmLevel(List<NodeDTO> list) {
        int maxAlarmLevel = 0;
        if (list == null || list.size() == 0) {
            return maxAlarmLevel;
        }
        CheckEventDao checkEventDao = new CheckEventDao();
        try {
            maxAlarmLevel = checkEventDao.findMaxAlarmLevel(list);
        } catch (Exception e) {
            logger.error("��ȡ�б����豸����߸澯�ȼ�����", e);
        } finally {
            checkEventDao.close();
        }
        return maxAlarmLevel;
    }

    /**
     * ��ȡ�б����豸����߸澯�ȼ�
     * @param list
     * @return {@link Integer}
     *          -- ��߸澯�ȼ�
     */
    public int getMaxAlarmLevel(NodeDTO nodeDTO) {
        List<NodeDTO> list = new ArrayList<NodeDTO>();
        list.add(nodeDTO);
        return getMaxAlarmLevel(list);
    }

    /**
     * ��ȡ�б����豸����߸澯�ȼ�
     * @param list
     * @return {@link Integer}
     *          -- ��߸澯�ȼ�
     */
    public List<CheckEvent> getAllAlarm(NodeDTO node) {
        List<CheckEvent> list = null;
        CheckEventDao checkEventDao = new CheckEventDao();
        try {
            list = checkEventDao.findCheckEvent(node);
        } catch (Exception e) {
            logger.error("��ȡ�豸�ĵ�ǰ�澯��Ϣ����", e);
        } finally {
            checkEventDao.close();
        }
        return list;
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
    }

}
