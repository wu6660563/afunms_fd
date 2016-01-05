package com.afunms.rmi.service;

import java.rmi.RemoteException;
import java.util.LinkedList;

public class RMIClientActionThread implements Runnable {

    private static RMIClientActionThread instance;

    private Thread thread;

    private LinkedList<RMIClientAction> linkedList = null;

    private RMIClientActionThread() {
        thread = new Thread(this);
        linkedList = new LinkedList<RMIClientAction>();
        start();
    }

    public static RMIClientActionThread getInstance() {
        if (instance == null) {
            instance = new RMIClientActionThread();
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
    
    private void runIt(RMIClientAction clientAction) {
        try {
            clientAction.getClientService().excuteMessage(clientAction);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public synchronized void add(RMIClientAction clientAction) {
        getLinkedList().add(clientAction);
        this.notifyAll();
    }
    
    /**
     * @return the linkedList
     */
    public LinkedList<RMIClientAction> getLinkedList() {
        return linkedList;
    }
}
