package com.afunms.alarm.fvsd.service;


import java.util.Hashtable;
import java.util.LinkedList;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.afunms.alarm.send.SendFVSDAlarm;
import com.afunms.common.util.SysLogger;
import com.afunms.event.model.CheckEvent;
import com.afunms.event.model.EventList;
import com.afunms.indicators.util.Constant;
import com.dhcc.itsm.webservice.BPIWebService;
import com.dhcc.itsm.webservice.BpiModel;



public class FvsdAlarmClientService implements Runnable {

    private static final SysLogger logger = SysLogger.getLogger(FvsdAlarmClientService.class);

    private static FvsdAlarmClientService instance = null;
    
    private Thread thread = null;

    private LinkedList<Hashtable<String, Object>> linkedList = new LinkedList<Hashtable<String, Object>>();

    private FvsdAlarmClientService() {
        thread = new Thread(this);
        thread.start();
    }

    public static FvsdAlarmClientService getInstance() {
        if (instance == null) {
            instance = new FvsdAlarmClientService();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public void run() {
        while(true) {
            try {
                synchronized (this) {
                    while (linkedList == null || linkedList.size() == 0) {
                        this.wait();
                    }
                    runIt(linkedList.removeFirst());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void runIt(Hashtable<String, Object> hashtable) {
        try {
            logger.info("开始发送至服务台。。。。");
            CheckEvent checkEvent =  (CheckEvent) hashtable.get("checkEvent");
            EventList eventList =  (EventList) hashtable.get("eventList");
            String categroy = "0";
            if (Constant.TYPE_NET.equals(eventList.getSubtype())) {
                categroy = "1";
            } else if (Constant.TYPE_HOST.equals(eventList.getSubtype())) {
                categroy = "2";
            } else if (Constant.TYPE_DB.equals(eventList.getSubtype())) {
                categroy = "4";
            } else if (Constant.TYPE_MIDDLEWARE.equals(eventList.getSubtype())) {
                categroy = "5";
            } else if (Constant.TYPE_APPLICATION.equals(eventList.getSubtype())) {
                categroy = "3";
            }
            
            BpiModel bpiModel = new BpiModel();
            
            bpiModel.setReportLocation(eventList.getBak());
            bpiModel.setSummary(eventList.getEventlocation());
            bpiModel.setDescription(eventList.getContent());
            bpiModel.setLevel(String.valueOf(eventList.getLevel1()));
            bpiModel.setCategory(categroy);
            bpiModel.setTriggerType("2");
            
            ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");  
            BPIWebService ts = (BPIWebService)context.getBean("service");
            String fvsdCode = ts.createAndSubmitBPI(bpiModel);
            logger.info("开始发送至服务台结束。。。。获取工单号为：" + fvsdCode);
            SendFVSDAlarm.saveToDB(checkEvent, eventList, fvsdCode);
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }

    public synchronized void add(CheckEvent checkEvent, EventList eventList) {
        Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
        hashtable.put("checkEvent", checkEvent);
        hashtable.put("eventList", eventList);
        linkedList.add(hashtable);
        this.notifyAll();
    }
}
