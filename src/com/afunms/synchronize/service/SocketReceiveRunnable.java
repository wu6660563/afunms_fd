/*
 * @(#)SocketReceiveRunnable.java     v1.01, 2013 12 19
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.synchronize.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.afunms.rmi.service.RMIParameter;

/**
 * ClassName:   SocketReceiveRunnable.java
 * <p> Socket ���� Runnable
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 19 16:27:04
 */
public class SocketReceiveRunnable extends ReceiveRunable {

    /**
     * socket:
     * <p>�ܵ�
     *
     * @since   v1.01
     */
    private Socket socket = null;
    
    /**
     * objectInputStream:
     * <p>����������
     *
     * @since   v1.01
     */
    private ObjectInputStream objectInputStream = null;

    /**
     * SocketReceiveRunnable:
     * @param   service
     *
     * @since   v1.01
     */
    public SocketReceiveRunnable(ReceiveService service) {
        super(service);
    }

    /**
     * run:
     * <p>�߳�ִ�з���
     *
     * @since   v1.01
     * @see     java.lang.Runnable#run()
     */
    public void run() {
        while (true) {
            try {
                RMIParameter parameter = (RMIParameter) getObjectInputStream()
                        .readObject();
                DataActionService.getInstance().execute(parameter);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        terminate();
    }

    /**
     * terminate:
     * <p>�жϸ��߳�
     *
     *
     * @since   v1.01
     */
    public void terminate() {
        try {
            if (getObjectInputStream() != null) {
                getObjectInputStream().close();
            }
            if (getSocket() != null) {
                getSocket().close();
            }
            SocketReceiveSerivce service = (SocketReceiveSerivce) getService();
            service.getList().remove(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * getSocket:
     * <p>
     *
     * @return  Socket
     *          -
     * @since   v1.01
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * setSocket:
     * <p>
     *
     * @param   socket
     *          -
     * @since   v1.01
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * getObjectInputStream:
     * <p>
     *
     * @return  ObjectInputStream
     *          -
     * @since   v1.01
     */
    public ObjectInputStream getObjectInputStream() {
        if (objectInputStream == null) {
            try {
                objectInputStream = new ObjectInputStream(getSocket().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return objectInputStream;
    }

    /**
     * setObjectInputStream:
     * <p>
     *
     * @param   objectInputStream
     *          -
     * @since   v1.01
     */
    public void setObjectInputStream(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
    }

}

