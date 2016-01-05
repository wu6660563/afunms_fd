package com.afunms.alarm.send;


import java.util.List;

import com.afunms.alarm.dao.SendFvsdAlarmDao;
import com.afunms.alarm.fvsd.service.FvsdAlarmClientService;
import com.afunms.alarm.model.AlarmWayDetail;
import com.afunms.alarm.model.FvsdAlarm;
import com.afunms.event.model.CheckEvent;
import com.afunms.event.model.EventList;
import com.afunms.location.dao.FvsdReportLocationNodeDao;
import com.afunms.location.model.FvsdReportLocationNode;
import com.afunms.location.service.FvsdReportLocationService;

public class SendFVSDAlarm implements SendAlarm{

	public void sendAlarm(CheckEvent checkEvent, EventList eventList,AlarmWayDetail alarmWayDetail) {
	    if(checkEvent == null) {
	        return;
	    }

	    String fvsdCode = "";
	    String alarmId = checkEvent.getAlarmId();
	    String name = checkEvent.getName();
	    String nodeid = checkEvent.getNodeId();
	    String type = checkEvent.getType();
	    String subtype = checkEvent.getSubtype();
	    String sindex = checkEvent.getSindex();
	    int alarmLevel = checkEvent.getAlarmlevel();
	    String content = checkEvent.getContent();
	    String collecttime = checkEvent.getCollecttime();
	    
	    SendFvsdAlarmDao lastSendFvsdAlarmDao = new SendFvsdAlarmDao();
	    FvsdAlarm lastFvsdAlarm = null;
        try {
            lastFvsdAlarm = (FvsdAlarm) lastSendFvsdAlarmDao.findFvsdAlarmByAlarmIdAndSindex(alarmId, sindex);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lastSendFvsdAlarmDao.close();
        }
        if (lastFvsdAlarm != null && lastFvsdAlarm.getAlarmlevel() > 0) {
            // 如果工单还未处理完成进行压制不再告警
            return;
        }
        
        List<FvsdReportLocationNode> list = null;
        FvsdReportLocationNodeDao fvsdReportLocationNodeDao = new FvsdReportLocationNodeDao();
        try {
            list = fvsdReportLocationNodeDao.find(nodeid, type, subtype);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fvsdReportLocationNodeDao.close();
        }
        
        try {
            if (list != null && list.size() > 0) {
                for (FvsdReportLocationNode fvsdReportLocationNode : list) {
                    eventList.setBak(fvsdReportLocationNode.getFvsdReportLocationId());
                }
            } else {
                eventList.setBak(FvsdReportLocationService.getInstance().getDefaultFvsdReportLoaction().getId());
            }
            FvsdAlarmClientService.getInstance().add(checkEvent, eventList);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        
	}

	public static void saveToDB(CheckEvent checkEvent, EventList eventList, String fvsdCode) {
	    if (fvsdCode == null || fvsdCode.trim().length() == 0) {
            return;
        }
        String alarmId = checkEvent.getAlarmId();
        String name = checkEvent.getName();
        String nodeid = checkEvent.getNodeId();
        String type = checkEvent.getType();
        String subtype = checkEvent.getSubtype();
        String sindex = checkEvent.getSindex();
        int alarmLevel = checkEvent.getAlarmlevel();
        String content = checkEvent.getContent();
        String collecttime = checkEvent.getCollecttime();
        FvsdAlarm fvsdAlarm = new FvsdAlarm();
        fvsdAlarm.setFvsdCode(fvsdCode);
        fvsdAlarm.setAlarmId(alarmId);
        fvsdAlarm.setName(name);
        fvsdAlarm.setNodeId(nodeid);
        fvsdAlarm.setType(type);
        fvsdAlarm.setSubtype(subtype);
        fvsdAlarm.setSindex(sindex);
        fvsdAlarm.setAlarmlevel(alarmLevel);
        fvsdAlarm.setContent(content);
        fvsdAlarm.setCollecttime(collecttime);
        SendFvsdAlarmDao sendFvsdAlarmDao = new SendFvsdAlarmDao();
        try {
            sendFvsdAlarmDao.save(fvsdAlarm);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sendFvsdAlarmDao.close();
        }
	}

	public static void main(String[] args) {
	    CheckEvent fvsdAlarm = new CheckEvent();
        fvsdAlarm.setAlarmId("1");
        fvsdAlarm.setAlarmlevel(3);
        fvsdAlarm.setName("ping");
        fvsdAlarm.setNodeId("1");
        fvsdAlarm.setType("net");
        fvsdAlarm.setSubtype("h3c");
        fvsdAlarm.setSindex("");
        fvsdAlarm.setAlarmlevel(3);
        fvsdAlarm.setContent("ping 不通");
        fvsdAlarm.setCollecttime("2011-11-11 00:00:00");
        
        EventList eventList = new EventList();
        eventList.setContent(fvsdAlarm.getContent());
        eventList.setEventlocation("内网核心交换机(192.168.3.1)");
        eventList.setSubtype(fvsdAlarm.getType());
        eventList.setLevel1(fvsdAlarm.getAlarmlevel());
	    new SendFVSDAlarm().sendAlarm(fvsdAlarm, eventList, null);
	}
}
