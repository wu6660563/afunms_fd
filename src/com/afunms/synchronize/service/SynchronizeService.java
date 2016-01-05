/*
 * @(#)SynchronizeService.java     v1.01, 2013 12 23
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.synchronize.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.jdom.Element;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.afunms.common.util.SysLogger;
import com.afunms.common.util.threadPool.ThreadPool;
import com.afunms.common.util.threadPool.ThreadPoolRunnableAttributes;
import com.afunms.initialize.ResourceCenter;
import com.afunms.rmi.service.RMIParameter;

/**
 * ClassName:   SynchronizeService.java
 * <p>ͬ�����񣬰����������շ����Լ����ͷ����ȷ���
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 23 10:34:38
 */
@SuppressWarnings("unchecked")
public class SynchronizeService {

    /**
     * logger:
     * <p>��־
     *
     * @since   v1.01
     */
    private final static SysLogger logger = SysLogger.getLogger(SynchronizeService.class);

    /**
     * FILE:
     * <p>ͬ������������ļ�
     *
     * @since   v1.01
     */
    private final static String SYNCHRONIZE_SERVICE_FILE = ResourceCenter.getInstance().getSysPath() + "/WEB-INF/classes/synchronize-service.xml"; 

    /**
     * receiveServiceList:
     * <p>���շ����б�
     *
     * @since   v1.01
     */
    private List<ReceiveService> receiveServiceList = new ArrayList<ReceiveService>();

    /**
     * sendServiceList:
     * <p>���ͷ����б�
     *
     * @since   v1.01
     */
    private List<SendService> sendServiceList = new ArrayList<SendService>();

    /**
     * maintainRunnable:
     * <p>ά���߳�
     *
     * @since   v1.01
     */
    private MaintainRunnable maintainRunnable = new MaintainRunnable(this);

    /**
     * sendThreadPool:
     * <p>���͵��̳߳�
     *
     * @since   v1.01
     */
    private ThreadPool sendThreadPool = new ThreadPool();

    /**
     * sendTimes:
     * <p>���ʹ���
     *
     * @since   v1.01
     */
    private long sendTimes;
    /**
     * instance:
     * <p>ʵ��
     *
     * @since   v1.01
     */
    private static SynchronizeService instance = null;

    private SynchronizeService() {
        
    }

    /**
     * start:
     * <p>�������н��շ���
     *
     *
     * @since   v1.01
     */
    @SuppressWarnings("static-access")
    public void start() {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(SYNCHRONIZE_SERVICE_FILE);

            // �������ݴ�������
            parseDataConfig(document);

            // ����ά���߳�����
            parseMaintainConfig(document);

            // ������������
            parseReceiveConfig(document);

            // ������������
            parseSendConfig(document);

        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            logger.info("�������շ���");
            for (ReceiveService receiveService : receiveServiceList) {
                try {
                    receiveService.start();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        getSendThreadPool().start();
    }

    /**
     * send:
     * <p>���ͷ���
     *
     * @param   parameter
     *
     * @since   v1.01
     */
    public void send(RMIParameter parameter) {
        if (sendTimes >= Long.MAX_VALUE) {
            sendTimes = 0;
        }
        sendTimes ++;
        SendRunnable sendRunnable = new SendRunnable(this);
        ThreadPoolRunnableAttributes attributes = new ThreadPoolRunnableAttributes();
        attributes.setAttribute("parameter", parameter);
        sendRunnable.setThreadPoolRunnableAttributes(attributes);
        getSendThreadPool().add(sendRunnable);
    }

    /**
     * parseDataConfig:
     * <p>������������
     *
     * @param   document
     *          - �ĵ�
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     *
     * @since   v1.01
     */
    @SuppressWarnings("static-access")
    public void parseDataConfig(Document document) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        logger.info("�������ݴ�������");
        Element dataActionService = document.getRootElement().getChild("data").getChild("service");
        boolean dataActionServiceEnable = Boolean.valueOf(dataActionService.getChildText("enable"));
        if (dataActionServiceEnable) {
            List<Element> propertiesList = dataActionService.getChild("threadPool").getChild("properties").getChildren();
            Properties properties = new Properties();
            for (Element propertiesElement : propertiesList) {
                String propertieName = propertiesElement.getName();
                String propertieValue = propertiesElement.getText();
                properties.put(propertieName, propertieValue);
            }
            DataActionService.getInstance().start(properties);

            List<Element> elementlist = document.getRootElement().getChild("data").getChildren("action");
            for (Object object : elementlist) {
                Element element = (Element) object;
                boolean enable = Boolean.valueOf(element.getChildText("enable"));
                String className = element.getChildText("class");
                String name = element.getChildText("name");
                DataAction dataAction = (DataAction) Class.forName(className).newInstance();
                if (enable) {
                    DataActionService.getInstance().addDataAction(name, dataAction);
                }
            }
        }
    }

    /**
     * parseReceiveConfig:
     * <p>������������
     *
     * @param   document
     *          - �ĵ�
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     *
     * @since   v1.01
     */
    @SuppressWarnings("static-access")
    public void parseReceiveConfig(Document document) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        logger.info("������������");
        List<Element> elementlist = document.getRootElement().getChild("receive").getChildren("service");
        for (Object object : elementlist) {
            Element element = (Element) object;
            boolean enable = Boolean.valueOf(element.getChildText("enable"));
            String name = element.getChildText("name");
            String className = element.getChildText("class");
            List<Element> propertiesList = element.getChild("properties").getChildren();
            Properties properties = new Properties();
            properties.put("name", name);
            for (Element propertiesElement : propertiesList) {
                String propertieName = propertiesElement.getName();
                String propertieValue = propertiesElement.getText();
                properties.put(propertieName, propertieValue);
            }
            logger.info("��������" + name + ":" + enable);
            if (enable) {
                BaseReceiveService receiveService = (BaseReceiveService) Class.forName(className).newInstance();
                receiveService.setProperties(properties);
                receiveServiceList.add(receiveService);
            }
        }
    }

    /**
     * parseSendConfig:
     * <p>������������
     *
     * @param   document
     *          - �ĵ�
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     *
     * @since   v1.01
     */
    @SuppressWarnings("static-access")
    public void parseSendConfig(Document document) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        logger.info("������������");
        boolean enable = Boolean.valueOf(document.getRootElement().getChild("send").getChildText("enable"));
        if (enable) {
            Element threadPoolElement = document.getRootElement().getChild("send").getChild("threadPool");
            List<Element> threadPoolPropertiesList = threadPoolElement.getChild("properties").getChildren();
            Properties threadPoolProperties = new Properties();
            for (Element propertiesElement : threadPoolPropertiesList) {
                String propertieName = propertiesElement.getName();
                String propertieValue = propertiesElement.getText();
                threadPoolProperties.put(propertieName, propertieValue);
            }
            getSendThreadPool().setProperties(threadPoolProperties);

            List<Element> elementlist = document.getRootElement().getChild("send").getChildren("service");
            for (Object object : elementlist) {
                Element element = (Element) object;
                enable = Boolean.valueOf(element.getChildText("enable"));
                String className = element.getChildText("class");
                List<Element> propertiesList = element.getChild("properties").getChildren();
                Properties properties = new Properties();
                for (Element propertiesElement : propertiesList) {
                    String propertieName = propertiesElement.getName();
                    String propertieValue = propertiesElement.getText();
                    properties.put(propertieName, propertieValue);
                }
                logger.info("��������:" + className + " �Ƿ�������" + enable);
                if (enable) {
                    BaseSendService baseSendService = (BaseSendService) Class.forName(className).newInstance();
                    baseSendService.setProperties(properties);
                    baseSendService.init();
                    sendServiceList.add(baseSendService);
                }
            }
        }
    }

    /**
     * parseMaintainConfig:
     * <p>����ά���߳�����
     *
     * @param   document
     *          - �ĵ�
     *
     * @since   v1.01
     */
    public void parseMaintainConfig(Document document) {
        Element maintainElement = document.getRootElement().getChild("maintain");
        List<Element> maintainPropertiesList = maintainElement.getChild("properties").getChildren();
        Properties properties = new Properties();
        for (Element propertiesElement : maintainPropertiesList) {
            String propertieName = propertiesElement.getName();
            String propertieValue = propertiesElement.getText();
            properties.put(propertieName, propertieValue);
        }
        getMaintainRunnable().setProperties(properties);
    }
    /**
     * getInstance:
     * <p>
     *
     * @return  SynchronizeService
     *          -
     * @since   v1.01
     */
    public static SynchronizeService getInstance() {
        if (instance == null) {
            instance = new SynchronizeService();
        }
        return instance;
    }

    /**
     * getSendThreadPool:
     * <p>
     *
     * @return  ThreadPool
     *          -
     * @since   v1.01
     */
    public ThreadPool getSendThreadPool() {
        return sendThreadPool;
    }

    /**
     * setSendThreadPool:
     * <p>
     *
     * @param   sendThreadPool
     *          -
     * @since   v1.01
     */
    public void setSendThreadPool(ThreadPool sendThreadPool) {
        this.sendThreadPool = sendThreadPool;
    }

    /**
     * getSendServiceList:
     * <p>
     *
     * @return  List<SendService>
     *          -
     * @since   v1.01
     */
    public List<SendService> getSendServiceList() {
        return sendServiceList;
    }

    /**
     * setSendServiceList:
     * <p>
     *
     * @param   sendServiceList
     *          -
     * @since   v1.01
     */
    public void setSendServiceList(List<SendService> sendServiceList) {
        this.sendServiceList = sendServiceList;
    }

    /**
     * getReceiveServiceList:
     * <p>
     *
     * @return  List<ReceiveService>
     *          -
     * @since   v1.01
     */
    public List<ReceiveService> getReceiveServiceList() {
        return receiveServiceList;
    }

    /**
     * setReceiveServiceList:
     * <p>
     *
     * @param   receiveServiceList
     *          -
     * @since   v1.01
     */
    public void setReceiveServiceList(List<ReceiveService> receiveServiceList) {
        this.receiveServiceList = receiveServiceList;
    }

    /**
     * getMaintainRunnable:
     * <p>
     *
     * @return  MaintainRunnable
     *          -
     * @since   v1.01
     */
    public MaintainRunnable getMaintainRunnable() {
        return maintainRunnable;
    }

    /**
     * setMaintainRunnable:
     * <p>
     *
     * @param   maintainRunnable
     *          -
     * @since   v1.01
     */
    public void setMaintainRunnable(MaintainRunnable maintainRunnable) {
        this.maintainRunnable = maintainRunnable;
    }

    /**
     * getSendTimes:
     * <p>
     *
     * @return  long
     *          -
     * @since   v1.01
     */
    public long getSendTimes() {
        return sendTimes;
    }

    /**
     * setSendTimes:
     * <p>
     *
     * @param   sendTimes
     *          -
     * @since   v1.01
     */
    public void setSendTimes(long sendTimes) {
        this.sendTimes = sendTimes;
    }

}

