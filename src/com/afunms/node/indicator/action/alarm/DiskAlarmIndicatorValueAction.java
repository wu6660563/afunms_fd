/*
 * @(#)DiskAlarmIndicatorValueAction.java     v1.01, 2014 1 3
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.alarm;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.config.dao.DiskconfigDao;
import com.afunms.config.model.Diskconfig;
import com.afunms.node.indicator.action.AlarmIndicatorValueAction;
import com.afunms.polling.om.Diskcollectdata;

/**
 * ClassName:   DiskAlarmIndicatorValueAction.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 3 09:53:00
 */
public class DiskAlarmIndicatorValueAction extends AlarmIndicatorValueAction {

    private final static String ALARM_INDICATOR_NAME_DISKPERC = "diskperc";

    private final static String ALARM_INDICATOR_NAME_DISKINC = "diskinc";

    /**
     * executeToAlarm:
     *
     *
     * @since   v1.01
     * @see com.afunms.node.indicator.action.AlarmIndicatorValueAction#executeToAlarm()
     */
    @SuppressWarnings("unchecked")
    @Override
    public void executeToAlarm() {
        Vector<Diskcollectdata> vector = (Vector<Diskcollectdata>) getIndicatorValue().getValue();
        
        Hashtable<String, Diskconfig> diskconfigHashtable = new Hashtable<String, Diskconfig>();
        DiskconfigDao diskconfigDao = new DiskconfigDao();
        try {
            List<Diskconfig> list = (List<Diskconfig>) diskconfigDao.loadByIpaddress(getIpAddress());
            if (list != null) {
                for (Diskconfig diskconfig : list) {
                    diskconfigHashtable.put(getIpAddress() + ":" + diskconfig.getName() + ":" + diskconfig.getBak(), diskconfig);
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        
        AlarmIndicatorsNode alarmIndicatorsNodeDiskperc = getAlarmIndicatorsNode(ALARM_INDICATOR_NAME_DISKPERC);
        AlarmIndicatorsNode alarmIndicatorsNodeDiskinc = getAlarmIndicatorsNode(ALARM_INDICATOR_NAME_DISKINC);
        for (Diskcollectdata diskcollectdata : vector) {
            if (diskcollectdata.getEntity().equalsIgnoreCase("Utilization")) {
                // 利用率
                if (alarmIndicatorsNodeDiskperc == null) {
                    continue;
                }
                String diskname = diskcollectdata.getSubentity();
                if (getNodeSysOid().startsWith("1.3.6.1.4.1.311.1.1.3")) {
                    diskname = diskcollectdata.getSubentity().substring(0, 3);
                }
                Diskconfig diskconfig = diskconfigHashtable.get(getIpAddress()
                                + ":"
                                + diskname
                                + ":"
                                + "利用率阈值");
                if (diskconfig != null) {
                    int limevalue0 = diskconfig.getLimenvalue();
                    int limevalue1 = diskconfig.getLimenvalue1();
                    int limevalue2 = diskconfig.getLimenvalue2();
                    alarmIndicatorsNodeDiskperc.setLimenvalue0(limevalue0 + "");
                    alarmIndicatorsNodeDiskperc.setLimenvalue1(limevalue1 + "");
                    alarmIndicatorsNodeDiskperc.setLimenvalue2(limevalue2 + "");
                    executeToAlarm(alarmIndicatorsNodeDiskperc, diskcollectdata.getThevalue());
                }
            } else if (diskcollectdata.getEntity().equalsIgnoreCase("UtilizationInc")) {
                // 增长率
                if (alarmIndicatorsNodeDiskinc == null) {
                    continue;
                }
                String diskname = diskcollectdata.getSubentity();
                if (getNodeSysOid().startsWith("1.3.6.1.4.1.311.1.1.3")) {
                    diskname = diskcollectdata.getSubentity().substring(0, 3);
                }
                Diskconfig diskconfig = diskconfigHashtable.get(getIpAddress()
                                + ":"
                                + diskname
                                + ":"
                                + "增长率阈值");
                if (diskconfig != null) {
                    int limevalue0 = diskconfig.getLimenvalue();
                    int limevalue1 = diskconfig.getLimenvalue1();
                    int limevalue2 = diskconfig.getLimenvalue2();
                    alarmIndicatorsNodeDiskinc.setLimenvalue0(limevalue0 + "");
                    alarmIndicatorsNodeDiskinc.setLimenvalue1(limevalue1 + "");
                    alarmIndicatorsNodeDiskinc.setLimenvalue2(limevalue2 + "");
                    executeToAlarm(alarmIndicatorsNodeDiskperc, diskcollectdata.getThevalue());
                }
            }
        }
    }

}

