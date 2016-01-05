/*
 * @(#)RemoteViewService.java     v1.01, 2013 12 19
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.afunms.initialize.ResourceCenter;
import com.afunms.middle.util.DL476Constant;
import com.afunms.middle.util.DL476Converter;
import com.afunms.middle.util.DL476Packets;
import com.afunms.middle.util.MQSend;

/**
 * ClassName:   RemoteViewService.java
 * <p>
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 19 09:22:07
 */
public class RemoteViewService {

    /**
     * execute:
     * <p>���� DL476 ����
     *
     * @param   requestPackets
     *          - DL476 ����
     *
     * @since   v1.01
     */
    public void execute(DL476Packets requestPackets) {
        requestPackets = DL476Converter.parseRequestPackets(requestPackets);
        byte requestControlDomain = requestPackets.getRequestControlDomain();

        if (requestControlDomain == DL476Constant.CONTROL_DOMAIN_A_ASSOCIATE) {
            // ������������������Ĳ��� Э����Ʊ���
        	
        	//�˴�Ӧ�֣���ϵӦ�𣩣���ϵ���ϣ���Ϊ������ԣ�ֻ������Ϊ��ϵӦ�𣬺��������Ż�
        	requestPackets.setResponseControlDomain(DL476Constant.CONTROL_DOMAIN_A_ASSOCIATE_ACK);
        } else if (requestControlDomain == DL476Constant.CONTROL_DOMAIN_A_DATA) {
        	requestPackets.setResponseControlDomain(DL476Constant.CONTROL_DOMAIN_A_DATA_ACK);	//����ȷ��
        	
            String requestServiceName = requestPackets.getRequestServiceName();
            String fileName = requestPackets.getRequestServiceFileName();
            requestPackets.setResponseControlDomain(requestControlDomain);
            DL476Packets responsePackets = DL476Converter.createDataACKPackets(requestPackets);
            sendPackets(responsePackets);
            responsePackets.setResponsePackets(new ArrayList<byte[]>());
            System.out.println("requestServiceName:"+requestServiceName);
            requestPackets.setResponseControlDomain(DL476Constant.CONTROL_DOMAIN_A_DATA);
            if (DL476Constant.REQUEST_SERVICE_NAME_GET.equals(requestServiceName)) {
                // ���� �ļ� ����
                requestPackets = executeFileRequest(requestPackets, fileName);
            } else if (DL476Constant.REQUEST_SERVICE_NAME_DISPLAY.equals(requestServiceName)) {
            	requestPackets.setResponseControlDomain(requestControlDomain);
            } else {
            	System.out.println("δ�ҵ����󣡣���requestServiceName:" + requestServiceName);
            }
        } else if (requestControlDomain == DL476Constant.CONTROL_DOMAIN_A_DATA_ACK) {
            executeDataACKRequest(requestPackets);
        } else if (requestControlDomain == DL476Constant.CONTROL_DOMAIN_A_RELEASE) {
        	//�ͷ�-->�ͷ�ȷ��
        	requestPackets.setResponseControlDomain(DL476Constant.CONTROL_DOMAIN_A_RELEASE_ACK);
        }

        DL476Packets responsePackets = DL476Converter.createResponsePackets(requestPackets);
        sendPackets(responsePackets);
    }
    
    public void executeDataRequest(DL476Packets requestPackets){}

    /**
     * send:
     * <p>����Ӧ�������ݰ��ӿ�
     *
     * @param   responsePackets
     *          - Ӧ����
     *
     * @since   v1.01
     */
    public void sendPackets(DL476Packets responsePackets) {
        List<byte[]> packets = responsePackets.getResponsePackets();
        for (byte[] packet : packets) {
            // ����÷��ͷ�����JMS�ӿڣ�
        	//"192.168.102.103$192.168.102.100_GR"
        	MQSend send = new MQSend(MQSend.topicHash.get("remoteview"));
        	try {
        		System.out.println("���ͱ��ģ�");
        		for (byte b : packet) {
					System.out.print(b + " ");
				}
        		System.out.println();
				send.sendMessage(packet);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }

    /**
     * executeFileRequest:
     * <p>�����ļ�����
     *
     *
     * @since   v1.01
     */
    public DL476Packets executeFileRequest(DL476Packets requestPackets, String fileName) {
    	String path = ResourceCenter.getInstance().getSysPath() + "/resource/svg/";
        File file = getFile(path + fileName);
        requestPackets.setResponseData(file);
        return requestPackets;
    }

    /**
     * executeFileRequest:
     * <p>������������
     *
     *
     * @since   v1.01
     */
    public DL476Packets executeDataRequest(DL476Packets requestPackets, String fileName) {
        File file = getFile(fileName);
        requestPackets.setResponseData(file);
        return requestPackets;
    }

    /**
     * executeFileRequest:
     * <p>������������
     *
     *
     * @since   v1.01
     */
    public DL476Packets executeDataACKRequest(DL476Packets requestPackets) {
        // �ɶ��� ,����ȷ�ϣ��ݲ�����
        return requestPackets;
    }

    /**
     * getFile:
     * <p>��ȡ�ļ�
     *
     * @param   fileName
     *          - �ļ���
     * @return  
     *
     * @since   v1.01
     */
    public File getFile(String fileName) {
    	//�õ�SVG�ļ�
    	File file = new File(fileName);
        return file;
    }

    /**
     * getData:
     * <p>��ȡ����
     *
     *
     * @since   v1.01
     */
    public Object getData() {
        return null;
    };
}

