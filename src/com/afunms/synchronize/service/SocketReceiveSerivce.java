/*
 * @(#)SocketReceiveSerivce.java     v1.01, 2013 12 19
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.synchronize.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.afunms.common.util.SysLogger;

/**
 * ClassName:   SocketReceiveSerivce.java
 * <p> Socket 接收服务
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 19 16:18:40
 */
@SuppressWarnings("static-access")
public class SocketReceiveSerivce extends BaseReceiveService implements Runnable {

    /**
     * logger:
     * <p>日志
     *
     * @since   v1.01
     */
    private final static SysLogger logger = SysLogger.getLogger(SocketReceiveSerivce.class);

    /**
     * name:
     * <p>名称
     *
     * @since   v1.01
     */
    private String name = "端口接收服务";

    /**
     * port:
     * <p>端口
     *
     * @since   v1.01
     */
    private int port = 9098;

    /**
     * port:
     * <p>端口
     *
     * @since   v1.01
     */
    private String host = "127.0.0.1";

    /**
     * thread:
     * <p>线程
     *
     * @since   v1.01
     */
    private Thread thread;

    /**
     * list:
     * <p>所有正在执行的 SocketReceiveRunnable
     *
     * @since   v1.01
     */
    private List<SocketReceiveRunnable> list = new ArrayList<SocketReceiveRunnable>();

    private long receiveTimes;
    /**
     * init:
     * <p>初始化方法
     *
     *
     * @since   v1.01
     */
    public void init() {
        try {
            Properties properties = getProperties();
            Integer port = Integer.valueOf((String) properties.get("port"));
            if (port > 0) {
                setPort(port);
            }
            String host = (String) properties.get("host");
            if (host != null) {
                setHost(host);
            }
            String name = (String) properties.get("name");
            if (name != null && name.trim().length() > 0) {
                setName(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * start:
     *
     * @param   properties
     *          - 配置文件
     *
     * @since   v1.01
     * @see com.afunms.transport.service.ReceiveService#start(java.util.Properties)
     */
    public void start() {
        if (getThread() == null || !getThread().isAlive()) {
            init();
            setThread(new Thread(this));
            getThread().start();
        }
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(getPort());
            //serverSocket.bind(new InetSocketAddress(getHost(), getPort()));
            logger.info("启动" + getName() + "成功，监听+ " + getHost() + "端口" + getPort());
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    if (receiveTimes >= Long.MAX_VALUE) {
                        receiveTimes = 0;
                    }
                    receiveTimes ++;
                    SocketReceiveRunnable runnable = new SocketReceiveRunnable(this);
                    runnable.setSocket(socket);
                    list.add(runnable);
                    Thread thread = new Thread(runnable);
                    thread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("启动" + getName() + "出错，监听端口" + getPort(), e);
        }
    }

    /**
     * getList:
     * <p>
     *
     * @return  List<SocketReceiveRunnable>
     *          -
     * @since   v1.01
     */
    public List<SocketReceiveRunnable> getList() {
        return list;
    }

    /**
     * setList:
     * <p>
     *
     * @param   list
     *          -
     * @since   v1.01
     */
    public void setList(List<SocketReceiveRunnable> list) {
        this.list = list;
    }

    /**
     * getName:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getName() {
        return name;
    }

    /**
     * setName:
     * <p>
     *
     * @param   name
     *          -
     * @since   v1.01
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getPort:
     * <p>
     *
     * @return  int
     *          -
     * @since   v1.01
     */
    public int getPort() {
        return port;
    }

    /**
     * setPort:
     * <p>
     *
     * @param   port
     *          -
     * @since   v1.01
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * getHost:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getHost() {
        return host;
    }
    /**
     * setHost:
     * <p>
     *
     * @param   host
     *          -
     * @since   v1.01
     */
    public void setHost(String host) {
        this.host = host;
    }
    /**
     * getThread:
     * <p>
     *
     * @return  Thread
     *          -
     * @since   v1.01
     */
    public Thread getThread() {
        return thread;
    }

    /**
     * setThread:
     * <p>
     *
     * @param   thread
     *          -
     * @since   v1.01
     */
    public void setThread(Thread thread) {
        this.thread = thread;
    }

}
