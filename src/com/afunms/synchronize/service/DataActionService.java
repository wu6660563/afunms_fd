/*
 * @(#)DataActionService.java     v1.01, 2013 12 20
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.synchronize.service;

import java.util.Hashtable;
import java.util.Properties;

import com.afunms.common.util.threadPool.ThreadPool;
import com.afunms.common.util.threadPool.ThreadPoolRunnable;
import com.afunms.common.util.threadPool.ThreadPoolRunnableAttributes;
import com.afunms.rmi.service.RMIParameter;


/**
 * ClassName:   DataActionService.java
 * <p>
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 20 09:47:08
 */
public class DataActionService {

    /**
     * instance:
     * <p>ʵ��
     *
     * @since   v1.01
     */
    public static DataActionService instance = null;

    /**
     * threadPool:
     * <p>���ݴ��� �̳߳�
     *
     * @since   v1.01
     */
    private ThreadPool threadPool = null;

    /**
     * hashtable:
     * <p>���� ACTION
     *
     * @since   v1.01
     */
    private Hashtable<String, DataAction> hashtable = new Hashtable<String, DataAction>();

    /**
     * sendTimes:
     * <p>��¼ִ�д���
     *
     * @since   v1.01
     */
    private long executeTimes = 0L;

    /**
     * DataActionService:
     * <p>˽�еĹ��췽�������ڵ���
     *
     * @since   v1.01
     */
    private DataActionService() {
        
    }
    /**
     * getInstance:
     * <p>��ȡʵ��
     *
     * @return  {@link DataActionService}
     *          - ʵ��
     *
     * @since   v1.01
     */
    public static DataActionService getInstance() {
        if (instance == null) {
            instance = new DataActionService();
        }
        return instance;
    }

    public void start(Properties properties) {
        threadPool = new ThreadPool(properties);
        threadPool.start();
    }

    /**
     * execute:
     * <p>������
     *
     * @param   parameter
     *          - �������
     *
     * @since   v1.01
     */
    public void execute(final RMIParameter parameter) {
        ThreadPoolRunnable threadPoolRunnable = new ThreadPoolRunnable() {

            public ThreadPoolRunnableAttributes getThreadPoolRunnableAttributes() {
                return null;
            }

            public void runIt(
                    ThreadPoolRunnableAttributes threadPoolRunnableAttributes) {
                try {
                    String action = (String) parameter.getParameter("action");
                    hashtable.get(action).action(parameter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        threadPool.add(threadPoolRunnable);
        executeTimes ++;
    }
    /**
     * getHashtable:
     * <p>
     *
     * @return  Hashtable<String,DataAction>
     *          -
     * @since   v1.01
     */
    public Hashtable<String, DataAction> getHashtable() {
        return hashtable;
    }
    /**
     * setHashtable:
     * <p>
     *
     * @param   hashtable
     *          -
     * @since   v1.01
     */
    public void setHashtable(Hashtable<String, DataAction> hashtable) {
        this.hashtable = hashtable;
    }

    /**
     * addDataAction:
     * <p>������ݴ���
     *
     * @param name
     * @param dataAction
     *
     * @since   v1.01
     */
    public void addDataAction(String name, DataAction dataAction) {
        getHashtable().put(name, dataAction);
    }
    /**
     * getExecuteTimes:
     * <p>
     *
     * @return  long
     *          -
     * @since   v1.01
     */
    public long getExecuteTimes() {
        return executeTimes;
    }
    /**
     * setExecuteTimes:
     * <p>
     *
     * @param   executeTimes
     *          -
     * @since   v1.01
     */
    public void setExecuteTimes(long executeTimes) {
        this.executeTimes = executeTimes;
    }

    
}

