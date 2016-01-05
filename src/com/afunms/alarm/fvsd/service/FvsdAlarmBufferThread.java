package com.afunms.alarm.fvsd.service;

import java.util.LinkedList;

public class FvsdAlarmBufferThread implements Runnable {

    private static FvsdAlarmBufferThread instance;

    private Thread thread;

//    private FvsdAlarmBuffer fvsdAlarmBuffer = null;

    private LinkedList<Object> linkedList = null;

    private FvsdAlarmBufferThread() {
        thread = new Thread(this);
//        fvsdAlarmBuffer = FvsdAlarmBuffer.getInstance();
        linkedList = new LinkedList<Object>();;
    }

    public static FvsdAlarmBufferThread getInstance() {
        if (instance == null) {
            instance = new FvsdAlarmBufferThread();
        }
        return instance;
    }

    public boolean start() {
        thread.start();
        return true;
    }

    public void run() {
        try {
            while (true) {
                synchronized (this) {
                    while (linkedList == null || linkedList.size() == 0) {
                        System.out.println("========================wait");
                        this.wait();
                    }
                    runIt(linkedList.removeFirst());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void runIt(Object object) {
        FvsdAlarmService fvsdAlarmService = new FvsdAlarmService();
        fvsdAlarmService.deleteByFvsdCode((String)object);
    }

    public synchronized void add(Object object) {
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaa=======" + object);
        getLinkedList().add(object);
        System.out.println("getLinkedList=======" + getLinkedList().size());
        notify();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaa=======" + getLinkedList().size());
    }
    
    /**
     * @return the linkedList
     */
    public LinkedList<Object> getLinkedList() {
        return linkedList;
    }
}
