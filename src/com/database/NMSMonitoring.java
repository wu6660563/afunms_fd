package com.database;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.afunms.initialize.ResourceCenter;

public class NMSMonitoring {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static NMSMonitoring instance = null;

    private static int openedConnectionNum = 0;

    private int closedConnectionNum = 0;

    private TimerTask timerTask = null;

    private Timer timer = new Timer();

    private String logFile = ResourceCenter.getInstance().getSysPath() + "WEB-INF/classes/log.txt";

    private Hashtable<String, ConnectionStackTrace> map = new Hashtable<String, ConnectionStackTrace>();

    private NMSMonitoring() {
        timer.schedule(geTimerTask(), 10000L, 60 * 1000L);
    }

    public static NMSMonitoring getInstance() {
        if (instance == null) {
            instance = new NMSMonitoring();
        }
        return instance;
    }

    public synchronized String openConnection() {
        Thread thread = Thread.currentThread();
        openedConnectionNum++;
        String id = String.valueOf(openedConnectionNum);
        ConnectionStackTrace connectionStackTrace = new ConnectionStackTrace();
        connectionStackTrace.setId(id);
        connectionStackTrace.setDate(new Date());
        connectionStackTrace.setStackTraceElements(thread.getStackTrace());
        map.put(id, connectionStackTrace);
        return id;
    }

    public synchronized void closeConnection(String id) {
        closedConnectionNum++;
        map.remove(id);
    }

    public synchronized int getCurrUnclosedConnection() {
        return openedConnectionNum - closedConnectionNum;
    }

    public synchronized int getCurrMapSize() {
        return map.size();
    }

    public synchronized int getOpenedConnectionNum() {
        return openedConnectionNum;
    }

    public synchronized int getClosedConnectionNum() {
        return closedConnectionNum;
    }

    public synchronized List<ConnectionStackTrace> getList() {
        List<ConnectionStackTrace> list = new ArrayList<ConnectionStackTrace>();
        Set<String> set = map.keySet();
        Iterator<String> iterator = set.iterator();
        Date date = new Date();
        while (iterator.hasNext()) {
            String elem = iterator.next();
            ConnectionStackTrace connectionStackTrace = map.get(elem);
            Long time = date.getTime() - connectionStackTrace.getDate().getTime();
            connectionStackTrace.setTime(time);
            if (time > 60 * 1000) {
                list.add(connectionStackTrace);
            }
        }
        return list;
    }

    public TimerTask geTimerTask() {
        if (timerTask == null) {
            timerTask = new TimerTask() {

                public void run() {
                    try {
                        NMSMonitoring monitoring = NMSMonitoring.getInstance();
                        File file = new File(logFile);
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        if (file.length() > 1024 * 1024 * 20) {
                            file.delete();
                            file.createNewFile();
                        }
                        FileWriter fileWriter = new FileWriter(file,true);
                        PrintWriter printWriter = new PrintWriter(fileWriter, true);
                        printWriter.println("------------------------------------------------");
                        printWriter.println("\n");
                        printWriter.println("当前时间为：" + sdf.format(new Date()));
                        printWriter.println("当前打开所有的连接数:" + monitoring.getOpenedConnectionNum());
                        printWriter.println("当前关闭所有的连接数:" + monitoring.getClosedConnectionNum());
                        printWriter.println("当前未关闭所有的连接数:" + monitoring.getCurrUnclosedConnection());
                        printWriter.println("当前Map中所有的连接总数:" + monitoring.getCurrMapSize());
                        List<ConnectionStackTrace> list = monitoring.getList();
                        printWriter.println("当前未及时关闭的所有连接数:" + list.size());
                        for (ConnectionStackTrace connectionStackTrace : list) {
                            StackTraceElement[] stackTraceElements = connectionStackTrace.getStackTraceElements();
                            String statckTrace = "";
                            for (int j = 0 ; j < stackTraceElements.length; j++) {
                                StackTraceElement stackTraceElement = stackTraceElements[j];
                                statckTrace += stackTraceElement.toString();
                            }
                            printWriter.println("id:" + connectionStackTrace.getId()
                                    + "\t" + "statckTrace:" + statckTrace + "\t" + "time:" + connectionStackTrace.getTime());
                        }
                        printWriter.println("\n");
                        printWriter.println("------------------------------------------------");
                        printWriter.flush();
                        fileWriter.flush();
                        printWriter.close();
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    
                }
                
            };
        }
        return timerTask;
    }

}
