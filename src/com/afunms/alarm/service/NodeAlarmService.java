package com.afunms.alarm.service;

import java.util.ArrayList;
import java.util.List;

import com.afunms.common.util.SysLogger;
import com.afunms.event.dao.CheckEventDao;
import com.afunms.event.model.CheckEvent;
import com.afunms.indicators.model.NodeDTO;

/**
 * 用于获取设备当前告警信息的服务类
 * @author 聂林
 */
public class NodeAlarmService {

    private static SysLogger logger = SysLogger.getLogger(NodeAlarmService.class.getName());

    /**
     * 获取列表中设备的最高告警等级
     * @param list
     * @return {@link Integer}
     *          -- 最高告警等级
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
            logger.error("获取列表中设备的最高告警等级出错", e);
        } finally {
            checkEventDao.close();
        }
        return maxAlarmLevel;
    }

    /**
     * 获取列表中设备的最高告警等级
     * @param list
     * @return {@link Integer}
     *          -- 最高告警等级
     */
    public int getMaxAlarmLevel(NodeDTO nodeDTO) {
        List<NodeDTO> list = new ArrayList<NodeDTO>();
        list.add(nodeDTO);
        return getMaxAlarmLevel(list);
    }

    /**
     * 获取列表中设备的最高告警等级
     * @param list
     * @return {@link Integer}
     *          -- 最高告警等级
     */
    public List<CheckEvent> getAllAlarm(NodeDTO node) {
        List<CheckEvent> list = null;
        CheckEventDao checkEventDao = new CheckEventDao();
        try {
            list = checkEventDao.findCheckEvent(node);
        } catch (Exception e) {
            logger.error("获取设备的当前告警信息出错", e);
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
