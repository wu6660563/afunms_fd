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
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 10 15:15:34
 */
public class DL476Packets {

    /**
     * requestControlDomain:
     * <p>���������
     *
     * @since   v1.01
     */
    private byte requestControlDomain;

    /**
     * requestPackets:
     * <p>������
     *
     * @since   v1.01
     */
    private List<byte[]> requestPackets;

    /**
     * responseControlDomain:
     * <p>Ӧ�������
     *
     * @since   v1.01
     */
    private byte responseControlDomain;

    /**
     * responseBid:
     * <p>Ӧ������������
     *
     * @since   v1.01
     */
    private byte responseBid;

    /**
     * requestService:
     * <p>�������
     *
     * @since   v1.01
     */
    private String requestService;

    /**
     * requestService:
     * <p>���������
     *
     * @since   v1.01
     */
    private String requestServiceName;

    /**
     * requestService:
     * <p>��������ļ���
     *
     * @since   v1.01
     */
    private String requestServiceFileName;

    /**
     * responsePackets:
     * <p>Ӧ����
     *
     * @since   v1.01
     */
    private List<byte[]> responsePackets;

    /**
     * responsePackets:
     * <p>Ӧ�����ݣ�����Ӧ������ͣ����ò�ͬ��Ӧ������
     *
     * @since   v1.01
     */
    private Object responseData;

    /**
     * addRequestPackets:
     * <p>���������
     *
     * @param   packets
     *          - ������
     *
     * @since   v1.01
     */
    public void addRequestPackets(byte[] packets) {
        getRequestPackets().add(packets);
    }
    
    /**
     * addResponsePackets:
     * <p>���Ӧ����
     *
     * @param   packets
     *          - Ӧ����
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

