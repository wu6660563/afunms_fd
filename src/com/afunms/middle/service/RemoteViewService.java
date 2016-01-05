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
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 19 09:22:07
 */
public class RemoteViewService {

    /**
     * execute:
     * <p>处理 DL476 报文
     *
     * @param   requestPackets
     *          - DL476 报文
     *
     * @since   v1.01
     */
    public void execute(DL476Packets requestPackets) {
        requestPackets = DL476Converter.parseRequestPackets(requestPackets);
        byte requestControlDomain = requestPackets.getRequestControlDomain();

        if (requestControlDomain == DL476Constant.CONTROL_DOMAIN_A_ASSOCIATE) {
            // 请在这里做出更具体的操作 协议控制报文
        	
        	//此处应分（联系应答）（联系否认），为方便测试，只先设置为联系应答，后续继续优化
        	requestPackets.setResponseControlDomain(DL476Constant.CONTROL_DOMAIN_A_ASSOCIATE_ACK);
        } else if (requestControlDomain == DL476Constant.CONTROL_DOMAIN_A_DATA) {
        	requestPackets.setResponseControlDomain(DL476Constant.CONTROL_DOMAIN_A_DATA_ACK);	//数据确认
        	
            String requestServiceName = requestPackets.getRequestServiceName();
            String fileName = requestPackets.getRequestServiceFileName();
            requestPackets.setResponseControlDomain(requestControlDomain);
            DL476Packets responsePackets = DL476Converter.createDataACKPackets(requestPackets);
            sendPackets(responsePackets);
            responsePackets.setResponsePackets(new ArrayList<byte[]>());
            System.out.println("requestServiceName:"+requestServiceName);
            requestPackets.setResponseControlDomain(DL476Constant.CONTROL_DOMAIN_A_DATA);
            if (DL476Constant.REQUEST_SERVICE_NAME_GET.equals(requestServiceName)) {
                // 处理 文件 请求
                requestPackets = executeFileRequest(requestPackets, fileName);
            } else if (DL476Constant.REQUEST_SERVICE_NAME_DISPLAY.equals(requestServiceName)) {
            	requestPackets.setResponseControlDomain(requestControlDomain);
            } else {
            	System.out.println("未找到请求！！！requestServiceName:" + requestServiceName);
            }
        } else if (requestControlDomain == DL476Constant.CONTROL_DOMAIN_A_DATA_ACK) {
            executeDataACKRequest(requestPackets);
        } else if (requestControlDomain == DL476Constant.CONTROL_DOMAIN_A_RELEASE) {
        	//释放-->释放确认
        	requestPackets.setResponseControlDomain(DL476Constant.CONTROL_DOMAIN_A_RELEASE_ACK);
        }

        DL476Packets responsePackets = DL476Converter.createResponsePackets(requestPackets);
        sendPackets(responsePackets);
    }
    
    public void executeDataRequest(DL476Packets requestPackets){}

    /**
     * send:
     * <p>发送应答报文数据包接口
     *
     * @param   responsePackets
     *          - 应答报文
     *
     * @since   v1.01
     */
    public void sendPackets(DL476Packets responsePackets) {
        List<byte[]> packets = responsePackets.getResponsePackets();
        for (byte[] packet : packets) {
            // 请调用发送方法（JMS接口）
        	//"192.168.102.103$192.168.102.100_GR"
        	MQSend send = new MQSend(MQSend.topicHash.get("remoteview"));
        	try {
        		System.out.println("发送报文：");
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
     * <p>处理文件请求
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
     * <p>处理数据请求
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
     * <p>处理数据请求
     *
     *
     * @since   v1.01
     */
    public DL476Packets executeDataACKRequest(DL476Packets requestPackets) {
        // 可丢弃 ,数据确认，暂不处理
        return requestPackets;
    }

    /**
     * getFile:
     * <p>获取文件
     *
     * @param   fileName
     *          - 文件名
     * @return  
     *
     * @since   v1.01
     */
    public File getFile(String fileName) {
    	//得到SVG文件
    	File file = new File(fileName);
        return file;
    }

    /**
     * getData:
     * <p>获取数据
     *
     *
     * @since   v1.01
     */
    public Object getData() {
        return null;
    };
}

