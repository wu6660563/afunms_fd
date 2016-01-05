/*
 * @(#)SocketSendService.java     v1.01, 2013 12 23
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.synchronize.service;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.afunms.rmi.service.RMIParameter;

/**
 * ClassName:   SocketSendService.java
 * <p>Socket ���ͷ���ֱ�ӷ��� {@link RMIParameter} ����
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 23 11:39:34
 */
public class SocketSendService extends BaseSendService {

    /**
     * host:
     * <p>��������ַ
     *
     * @since   v1.01
     */
    private String host;

    /**
     * port:
     * <p>�������˿�
     *
     * @since   v1.01
     */
    private int port;

    /**
     * send:
     * <p>���Ͳ�������
     *
     * @param   parameter
     *          - ��������
     *
     * @since   v1.01
     * @see com.afunms.synchronize.service.SendService#send(com.afunms.rmi.service.RMIParameter)
     */
    public void send(RMIParameter parameter) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(getHost(), getPort()));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(parameter);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * init:
     * <p>��ʼ��
     *
     *
     * @since   v1.01
     */
    public void init() {
        String host = getProperties().getProperty("host");
        if (host.trim().length() > 0) {
            setHost(host);
        }
        int port = Integer.valueOf(getProperties().getProperty("port"));
        if (port > 0) {
            setPort(port);
        }
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

}

