/*
 * @(#)DL476Action.java     v1.01, 2013 12 10
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.util;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:   DL476Action.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 10 15:15:34
 */
public class DL476Packets {

    /**
     * requestControlDomain:
     * <p>请求控制域
     *
     * @since   v1.01
     */
    private byte requestControlDomain;

    /**
     * requestPackets:
     * <p>请求报文
     *
     * @since   v1.01
     */
    private List<byte[]> requestPackets;

    /**
     * responseControlDomain:
     * <p>应答控制域
     *
     * @since   v1.01
     */
    private byte responseControlDomain;

    /**
     * responseBid:
     * <p>应答报文数据类型
     *
     * @since   v1.01
     */
    private byte responseBid;

    /**
     * requestService:
     * <p>请求服务
     *
     * @since   v1.01
     */
    private String requestService;

    /**
     * requestService:
     * <p>请求服务名
     *
     * @since   v1.01
     */
    private String requestServiceName;

    /**
     * requestService:
     * <p>请求服务文件名
     *
     * @since   v1.01
     */
    private String requestServiceFileName;

    /**
     * responsePackets:
     * <p>应答报文
     *
     * @since   v1.01
     */
    private List<byte[]> responsePackets;

    /**
     * responsePackets:
     * <p>应答数据，根据应答的类型，设置不同的应答数据
     *
     * @since   v1.01
     */
    private Object responseData;

    /**
     * addRequestPackets:
     * <p>添加请求报文
     *
     * @param   packets
     *          - 请求报文
     *
     * @since   v1.01
     */
    public void addRequestPackets(byte[] packets) {
        getRequestPackets().add(packets);
    }
    
    /**
     * addResponsePackets:
     * <p>添加应答报文
     *
     * @param   packets
     *          - 应答报文
     *
     * @since   v1.01
     */
    public void addResponsePackets(byte[] packets) {
        getResponsePackets().add(packets);
    }

    /**
     * getRequestControlDomain:
     * <p>
     *
     * @return  byte
     *          -
     * @since   v1.01
     */
    public byte getRequestControlDomain() {
        return requestControlDomain;
    }

    /**
     * setRequestControlDomain:
     * <p>
     *
     * @param   requestControlDomain
     *          -
     * @since   v1.01
     */
    public void setRequestControlDomain(byte requestControlDomain) {
        this.requestControlDomain = requestControlDomain;
    }

    /**
     * getRequestPackets:
     * <p>
     *
     * @return  List<byte[]>
     *          -
     * @since   v1.01
     */
    public List<byte[]> getRequestPackets() {
    	if (requestPackets == null) {
    		requestPackets = new ArrayList<byte[]>();
    	}
        return requestPackets;
    }

    /**
     * setRequestPackets:
     * <p>
     *
     * @param   requestPackets
     *          -
     * @since   v1.01
     */
    public void setRequestPackets(List<byte[]> requestPackets) {
        this.requestPackets = requestPackets;
    }

    /**
     * getResponseControlDomain:
     * <p>
     *
     * @return  byte
     *          -
     * @since   v1.01
     */
    public byte getResponseControlDomain() {
        return responseControlDomain;
    }

    /**
     * setResponseControlDomain:
     * <p>
     *
     * @param   responseControlDomain
     *          -
     * @since   v1.01
     */
    public void setResponseControlDomain(byte responseControlDomain) {
        this.responseControlDomain = responseControlDomain;
    }

    /**
     * getResponseBid:
     * <p>
     *
     * @return  byte
     *          -
     * @since   v1.01
     */
    public byte getResponseBid() {
        return responseBid;
    }

    /**
     * setResponseBid:
     * <p>
     *
     * @param   responseBid
     *          -
     * @since   v1.01
     */
    public void setResponseBid(byte responseBid) {
        this.responseBid = responseBid;
    }

    /**
     * getRequestService:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getRequestService() {
        return requestService;
    }

    /**
     * setRequestService:
     * <p>
     *
     * @param   requestService
     *          -
     * @since   v1.01
     */
    public void setRequestService(String requestService) {
        this.requestService = requestService;
    }

    /**
     * getRequestServiceName:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getRequestServiceName() {
        return requestServiceName;
    }

    /**
     * setRequestServiceName:
     * <p>
     *
     * @param   requestServiceName
     *          -
     * @since   v1.01
     */
    public void setRequestServiceName(String requestServiceName) {
        this.requestServiceName = requestServiceName;
    }

    /**
     * getRequestServiceFileName:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getRequestServiceFileName() {
        return requestServiceFileName;
    }

    /**
     * setRequestServiceFileName:
     * <p>
     *
     * @param   requestServiceFileName
     *          -
     * @since   v1.01
     */
    public void setRequestServiceFileName(String requestServiceFileName) {
        this.requestServiceFileName = requestServiceFileName;
    }

    /**
     * getResponsePackets:
     * <p>
     *
     * @return  List<byte[]>
     *          -
     * @since   v1.01
     */
    public List<byte[]> getResponsePackets() {
    	if (responsePackets == null) {
    		responsePackets = new ArrayList<byte[]>();
    	}
        return responsePackets;
    }

    /**
     * setResponsePackets:
     * <p>
     *
     * @param   responsePackets
     *          -
     * @since   v1.01
     */
    public void setResponsePackets(List<byte[]> responsePackets) {
        this.responsePackets = responsePackets;
    }

    /**
     * getResponseData:
     * <p>
     *
     * @return  Object
     *          -
     * @since   v1.01
     */
    public Object getResponseData() {
        return responseData;
    }

    /**
     * setResponseData:
     * <p>
     *
     * @param   responseData
     *          -
     * @since   v1.01
     */
    public void setResponseData(Object responseData) {
        this.responseData = responseData;
    }

}

