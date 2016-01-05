package com.afunms.alarm.fvsd.service;

import java.util.LinkedList;

public class FvsdAlarmBuffer {

    private static FvsdAlarmBuffer instance;

    private LinkedList<Object> linkedList = new LinkedList<Object>();

    private FvsdAlarmBuffer() {
        
    }

    public static FvsdAlarmBuffer getInstance() {
        if (instance == null) {
            instance = new FvsdAlarmBuffer();
        }
        return instance;
    }

    public synchronized void add(Object object) {
        getLinkedList().add(object);
        this.notifyAll();
    }

    /**
     * @return the linkedList
     */
    public LinkedList<Object> getLinkedList() {
        return linkedList;
    }

}
