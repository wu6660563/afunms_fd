/*
 * @(#)DL476Converter.java     v1.01, 2013 12 10
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.afunms.common.util.ByteUtil;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

/**
 * ClassName:   DL476Converter.java
 * <p>
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 10 15:11:37
 */
public class DL476Converter {

    /**
     * FILE_SEGMENT_SIZE:
     * <p>�ļ��ֶγ��ȣ�32K
     *
     * @since   v1.01
     */
    private final static int FILE_SEGMENT_SIZE = 32 * 1024;

    /**
     * parseRequestPackets:
     * <p>��������� DL476 ����
     *
     * @param requestPackets
     * @return
     *
     * @since   v1.01
     */
    public static DL476Packets parseRequestPackets(DL476Packets requestPackets) {
        int size = requestPackets.getRequestPackets().size();
        byte requestControlDomain = -1;
        if (size > 0) {
            requestControlDomain = requestPackets.getRequestPackets().get(size - 1)[0];
        }
        String requestService = null;
        String requestServiceName = null;
        String requestServiceFileName = null;
        System.out.println("requestControlDomain:" + requestControlDomain);
        if (requestControlDomain == DL476Constant.CONTROL_DOMAIN_A_DATA) {
            // ����������� 0001010   A-DATA  ���� ����Ҫ���������������
            byte[] packets = requestPackets.getRequestPackets().get(0);
            short requestServiceSize = 0;
            if (packets.length >= 11) {
                byte[] requestServiceSizeByte = new byte[2];
                requestServiceSizeByte[0] = packets[9];
                requestServiceSizeByte[1] = packets[10];
                requestServiceSize = byteArray2Short(requestServiceSizeByte);
            }
            if (packets.length == 11 + requestServiceSize) {
                byte[] requestServiceByte = Arrays.copyOfRange(packets, 13, packets.length);
                requestService = new String(requestServiceByte);
                if (requestService.indexOf(DL476Constant.REQUEST_SERVICE_NAME_GET) >= 0) {
                    requestServiceName = DL476Constant.REQUEST_SERVICE_NAME_GET;
                } else if (requestService.indexOf(DL476Constant.REQUEST_SERVICE_NAME_DISPLAY) >= 0) {
                    requestServiceName = DL476Constant.REQUEST_SERVICE_NAME_DISPLAY;
                } else if (requestService.indexOf(DL476Constant.REQUEST_SERVICE_NAME_STOPDATA) >= 0) {
                    requestServiceName = DL476Constant.REQUEST_SERVICE_NAME_STOPDATA;
                }
                int start = requestService.indexOf("(");
                int end = requestService.indexOf(")");
                if (start >= 0 && end > start) {
                    String requestServiceTemp = requestService.substring(start + 1, end);
                    String[] paramter = requestServiceTemp.split(",");
                    for (String string : paramter) {
                    	if (string.indexOf('=') > 0 && string.indexOf('=') < string.length() - 1) {
                        	requestServiceFileName = string.substring(string.indexOf('=') + 1).trim();
                        }
					}
                }
            }
        }
        requestPackets.setRequestControlDomain(requestControlDomain);
        requestPackets.setRequestServiceName(requestServiceName);
        requestPackets.setRequestService(requestService);
        requestPackets.setRequestServiceFileName(requestServiceFileName);
        return requestPackets;
    }

    /**
     * createResponsePackets:
     * <p>��������� DL476 ���ģ�����Ӧ���ģ�Ӧ���ķ�Ϊ��Э����Ʊ��ĺ����ݴ��䱨�ĵ�Ӧ��
     *
     * @param requestPackets
     * @return
     *
     * @since   v1.01
     */
    public static DL476Packets createResponsePackets(DL476Packets requestPackets) {
        byte responseControlDomain = requestPackets.getResponseControlDomain();
        System.out.println("responseControlDomain:" + responseControlDomain);
        if (responseControlDomain < DL476Constant.CONTROL_DOMAIN_A_DATA) {
            createControlPackets(requestPackets);
        } else if (responseControlDomain == DL476Constant.CONTROL_DOMAIN_A_DATA) {
            createDataPackets(requestPackets);
        }
        return requestPackets;
    }

    /**
     * createDataACKPackets:
     * <p>��������ȷ�ϱ���
     *
     * @param   requestPackets
     * @return
     *
     * @since   v1.01
     */
    public static DL476Packets createDataACKPackets(DL476Packets requestPackets) {
    	requestPackets.setResponseControlDomain(DL476Constant.CONTROL_DOMAIN_A_DATA_ACK);
    	
    	byte[] responsePacket = new byte[4];
    	responsePacket[0] = DL476Constant.CONTROL_DOMAIN_A_DATA_ACK;	//������(0x0A)
    	responsePacket[1] = requestPackets.getRequestPackets().get(0)[2];	//������ţ�NR��
    	responsePacket[2] = 0;
    	responsePacket[3] = 0;
    	
    	requestPackets.addResponsePackets(responsePacket);
    	
        return requestPackets;
    }
    /**
     * createDataPackets:
     * <p>����������� DL476 ���ģ��ݴ��� Э����Ʊ��� ��Ӧ��
     *
     * @param   requestPackets
     *          - ����� DL476 ����
     * @return  {@link DL476Packets}
     *          - Ӧ����
     *
     * @since   v1.01
     */
    public static DL476Packets createControlPackets(DL476Packets requestPackets) {
        byte[] responsePacket = new byte[7];
        responsePacket[0] = requestPackets.getResponseControlDomain();
        responsePacket[1] = DL476Constant.CONTROL_HEADER_CONTROL_MODEL;
        responsePacket[2] = DL476Constant.CONTROL_HEADER_STATUS_MARK;
        responsePacket[3] = DL476Constant.CONTROL_HEADER_REASON_CODE;
        copyOf(responsePacket, 4, int2ByteArray(1, 2));
        responsePacket[6] = 0;
        requestPackets.addResponsePackets(responsePacket);
        return requestPackets;
    }

    /**
     * createDataPackets:
     * <p>��������� DL476 ���ģ����� ���ݴ��䱨�� ��Ӧ��
     *
     * @param   requestPackets
     *          - ����� DL476 ����
     * @return  {@link DL476Packets}
     *          - Ӧ����
     *
     * @since   v1.01
     */
    public static DL476Packets createDataPackets(DL476Packets requestPackets) {
        String requestServiceName = requestPackets.getRequestServiceName();
        if (DL476Constant.REQUEST_SERVICE_NAME_GET.equals(requestServiceName)) {
            createFilePackets(requestPackets);
        } else if (DL476Constant.REQUEST_SERVICE_NAME_DISPLAY.equals(requestServiceName)) {
        	//******************************������--�仯ģ����--start***************************
        	Hashtable data = new Hashtable();
        	Vector vector = new Vector();
        	
        	Hashtable hashtable1 = new Hashtable();
        	hashtable1.put("indicators_id", 240);
        	hashtable1.put("indicators_value", 52f);
        	hashtable1.put("indicators_name", "cpu");
        	Integer[] rgb1 = {255, 0, 0};
        	hashtable1.put("rgb", rgb1);
        	vector.add(hashtable1);
        	
        	Hashtable hashtable2 = new Hashtable();
        	hashtable2.put("indicators_id", 237);
        	hashtable2.put("indicators_value", 52f);
        	hashtable2.put("indicators_name", "memory");
        	Integer[] rgb2 = {0, 255, 0};
        	hashtable2.put("rgb", rgb2);
        	vector.add(hashtable2);
        	
        	Hashtable hashtable3 = new Hashtable();
        	hashtable3.put("indicators_id", 234);
        	hashtable3.put("indicators_value", 100f);
        	hashtable3.put("indicators_name", "ping");
        	Integer[] rgb3 = {0, 255, 0};
        	hashtable3.put("rgb", rgb3);
        	vector.add(hashtable3);
        	data.put("dataVariationAnalog", vector);
        	//******************************������----end*************************************
        	Vector vector1 = new Vector();
        	Hashtable hashtable4 = new Hashtable();
        	hashtable4.put("indicators_id", 231);
        	hashtable4.put("indicators_value", 1);
        	hashtable4.put("image_status", 1);
        	hashtable4.put("indicators_name", "status");
        	Integer[] rgb4 = {0, 255, 0};
        	hashtable4.put("rgb", rgb4);
        	vector1.add(hashtable4);
        	
        	Hashtable hashtable5 = new Hashtable();
        	hashtable5.put("indicators_id", 228);
        	hashtable5.put("indicators_value", 1);
        	hashtable5.put("image_status", 1);
        	hashtable5.put("indicators_name", "status");
        	Integer[] rgb5 = {0, 255, 0};
        	hashtable5.put("rgb", rgb5);
        	vector1.add(hashtable5);
        	data.put("dataVariationStatus", vector1);
        	requestPackets.setResponseData(data);
        	//******************************������--�仯ģ����--end*****************************
        	
        	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>requestServiceName:"+requestServiceName);
        	createDataTransferPackets(requestPackets);
        }
        return requestPackets;
    }

    /**
     * addPacketHeader:
     * <p>����������� DL476 ���ģ���Ӧ������ӱ���ͷ
     *
     * @param   requestPackets
     *          - ����� DL476 ����
     *
     * @since   v1.01
     */
    public static void addPacketHeader(byte[] packet, byte responseControlDomain, byte receiveNumber) {
        packet[0] = responseControlDomain;
        packet[1] = receiveNumber;
        packet[2] = (byte) (receiveNumber + 1);
        packet[3] = (byte) (0);
        copyOf(packet, 4, int2ByteArray(packet.length - 6, 2));
        packet[6] = (byte) (0);
    }
    
    
    /**
     * createDataTransferPackets:
     * <p>��������� DL476 ���ģ����� ���ݴ��� ����
     *
     * @param requestPackets
     * @return
     *
     * @since   v1.01
     */
    public static DL476Packets createDataTransferPackets(DL476Packets requestPackets){
    	//---------------------------------ģ����-------------------------------
    	DL476Packets packets = createVariationDL476Packets(requestPackets);
    	
    	//---------------------------------״̬��-------------------------------
    	createVariationStatusPackets(requestPackets);
    	return packets;
    }
    
    public static DL476Packets createVariationDL476Packets(DL476Packets requestPackets){
    	//---------------------------------ģ����-------------------------------
    	List<byte[]> byteList = createVariationValueBlock(requestPackets);
    	
    	int segmentNumber = byteList.size() * 11 / FILE_SEGMENT_SIZE;
    	if (byteList.size() * 11 % FILE_SEGMENT_SIZE != 0) {
            segmentNumber += 1;
        }

    	System.out.println("����ң�����ݣ�����" + byteList + "��");
    	System.out.println("����ң�����ݱ��ģ�����" + segmentNumber + "�Σ�");
    	for (int i = 0; i < segmentNumber; i++) {
            int from = i * FILE_SEGMENT_SIZE / 11;
            int to = (i + 1) * FILE_SEGMENT_SIZE / 11;
            if (to > byteList.size()) {
            	to = byteList.size();
            }
            
            int dataLength = (to - from) * 11;

            byte[] variationSegmentContent = new byte[dataLength];

            for (int j = from; j < to; j++) {
            	copyOf(variationSegmentContent, j * 11, byteList.get(j));
			}

            byte[] variationSegmentContentBlock = createVariationContentBlock(i, variationSegmentContent);
            
            byte[] variationPackets = new byte[7 + variationSegmentContentBlock.length];
            copyOf(variationPackets, 7, variationSegmentContentBlock);
            byte responseControlDomain = requestPackets.getResponseControlDomain();
            if (i < segmentNumber -1) {
                responseControlDomain = DL476Constant.CONTROL_DOMAIN_A_DATA_NEXT;
            }
            
            addPacketHeader(variationPackets, responseControlDomain, requestPackets.getRequestPackets().get(0)[2]);
            System.out.println("����ң�����ݣ���" + i + "��������Ϊ��" + variationPackets.length + ":");
            for (byte b : variationPackets) {
				System.out.print(b + " ");
			}
            System.out.println();
            requestPackets.addResponsePackets(variationPackets);
		}
    	
    	//---------------------------------״̬��-------------------------------
//    	createVariationStatusPackets(requestPackets);
    	return requestPackets;
    }
    
    
    /**
     * createVariationStatusBlock:
     * <p> ���� �仯״̬��
     *
     * @param requestPackets
     * @return
     *
     * @since   v1.01
     */
    public static DL476Packets createVariationStatusPackets(DL476Packets requestPackets){
    	//---------------------------------״̬��-------------------------------
    	int contentLength = 0;
    	Hashtable hashData = (Hashtable) requestPackets.getResponseData();
    	Vector<Hashtable> vector = (Vector<Hashtable>) hashData.get("dataVariationStatus");
    	
    	List<byte[]> byteList = new ArrayList<byte[]>();
    	for (Hashtable hash : vector) {
    		byte[] variationValueBlock = new byte[9];
    		Integer id = (Integer) hash.get("indicators_id");
    		copyOf(variationValueBlock, 0, int2ByteArray(id, 2));

    		Integer value = (Integer) hash.get("indicators_value");
    		variationValueBlock[2] = value.byteValue();
    		Integer status = (Integer) hash.get("image_status");
    		variationValueBlock[3] = status.byteValue();
    		Integer[] rgb = (Integer[]) hash.get("rgb");
    		
    		//rgb(0,255,0)
        	variationValueBlock[4] = rgb[0].byteValue();
        	variationValueBlock[5] = rgb[1].byteValue();
        	variationValueBlock[6] = rgb[2].byteValue();
        	variationValueBlock[7] = 100;	//͸����
        	variationValueBlock[8] = 0;	//������
        	
        	contentLength += 11;
        	
        	byteList.add(variationValueBlock);
		}
    	
    	int segmentNumber = byteList.size() * 9 / FILE_SEGMENT_SIZE;
    	if (byteList.size() * 9 % FILE_SEGMENT_SIZE != 0) {
            segmentNumber += 1;
        }
    	for (int i = 0; i < segmentNumber; i++) {
    		int from = i * FILE_SEGMENT_SIZE / 9;
            int to = (i + 1) * FILE_SEGMENT_SIZE / 9;
            if (to > byteList.size()) {
                to = byteList.size();
            }
            int dataLength = (to - from) * 9;
            
            byte[] variationStatusContent = new byte[dataLength];
            for (int j = from; j < to; j++) {
            	copyOf(variationStatusContent, j * 9, byteList.get(j));
			}
            
            int dataBlockLength = 4 + dataLength;
        	byte[] variationContentBlock = new byte[dataBlockLength];

        	//��ȡ���ݿ�ͷ
        	byte[] dataBlockHeader = createVariationBlockHeader(DL476Constant.BID_A_VARITION_STATUS, (byte)0, dataBlockLength);
        	copyOf(variationContentBlock, 0, dataBlockHeader);
        	//��������
        	copyOf(variationContentBlock, 4, variationStatusContent);

        	byte[] variationStatusContentPacket = new byte[7 + variationContentBlock.length];
            copyOf(variationStatusContentPacket, 7, variationContentBlock);
            byte responseControlDomain = requestPackets.getResponseControlDomain();
            if (i < segmentNumber -1) {
                responseControlDomain = DL476Constant.CONTROL_DOMAIN_A_DATA_NEXT;
            }
            System.out.print("ҡ��ֵ:");
        	for (byte b : variationStatusContentPacket) {
				System.out.print(b + " ");
			}
        	System.out.println("");
            addPacketHeader(variationStatusContentPacket, responseControlDomain, requestPackets.getRequestPackets().get(0)[2]);
            requestPackets.addResponsePackets(variationStatusContentPacket);
    	}
    	return requestPackets;
    }
    
    
    
    /**
     * createVariationValueBlock:
     * <p> ���� �仯����ֵ һ����������ɴ������
     *
     * @return
     *
     * @since   v1.01
     */
    public static List<byte[]> createVariationValueBlock(DL476Packets requestPackets){
    	int dataLength = 0;
    	Hashtable hashData = (Hashtable) requestPackets.getResponseData();
    	Vector vector = (Vector) hashData.get("dataVariationAnalog");
    	List<byte[]> byteList = new ArrayList<byte[]>();
    	for (int i = 0; i < vector.size(); i++) {
    		byte[] variationValueBlock = new byte[11];
    		
    		Hashtable hashtable = (Hashtable)vector.get(i);
    		Integer id = (Integer) hashtable.get("indicators_id");
        	Float value = (Float) hashtable.get("indicators_value");
//        	String indicators_name = (String) hashtable.get("indicators_name");
        	Integer[]  rgb = (Integer[]) hashtable.get("rgb");
        	
        	//variationValueBlock[0] = 0;	//����ֵ1��ţ��ͣ����ݶ�Ϊ0
        	//variationValueBlock[1] = 0;	//����ֵ1��ţ��ߣ����ݶ�Ϊ0
        	copyOf(variationValueBlock, 0, int2ByteArray(id));

        	copyOf(variationValueBlock, 2, float2ByteArray(value));
        	//rgb(0,255,0)
        	variationValueBlock[6] = rgb[0].byteValue();
        	variationValueBlock[7] = rgb[1].byteValue();
        	variationValueBlock[8] = rgb[2].byteValue();
        	
        	variationValueBlock[9] = 100;	//͸����
        	variationValueBlock[10] = 0;	//������1
        	
        	byteList.add(variationValueBlock);
        	dataLength += 11;
		}
    	return byteList;
    }

    /**
     * createFilePackets:
     * <p>����������� DL476 ���ģ������ļ�����
     *
     * @param   requestPackets
     * @return
     *
     * @since   v1.01
     */
    public static DL476Packets createFilePackets(DL476Packets requestPackets) {
        File file = (File) requestPackets.getResponseData();
        byte[] fileContent = getFileContent(file);
        Long lastModified = file.lastModified();
        byte[] fileName = file.getName().getBytes();

        // �����ļ����Կ�
        byte[] fileAttributeBlock = createFileAttributeBlock(fileName, fileContent.length, lastModified);
        byte[] fileAttributePacket = new byte[7 + fileAttributeBlock.length];
        copyOf(fileAttributePacket, 7, fileAttributeBlock);
        addPacketHeader(fileAttributePacket, requestPackets.getResponseControlDomain(), requestPackets.getRequestPackets().get(0)[2]);
        requestPackets.addResponsePackets(fileAttributePacket);
        
        // �����ļ����ݿ飬�����ļ����ݳ��ȣ�����N��
        int segmentNumber = fileContent.length / FILE_SEGMENT_SIZE;
        if (fileContent.length % FILE_SEGMENT_SIZE != 0) {
            segmentNumber += 1;
        }
        System.out.println("�����ļ����ļ�����" + segmentNumber);
        for (int i = 0; i < segmentNumber; i++) {
            int from = i * FILE_SEGMENT_SIZE;
            int to = i * FILE_SEGMENT_SIZE + FILE_SEGMENT_SIZE;
            if (to > fileContent.length) {
                to = fileContent.length;
            }
            byte[] fileSegmentContent = Arrays.copyOfRange(fileContent, from, to);
            byte[] fileSegmentContentBlock = createFileContentBlock(i, fileSegmentContent);
            
            byte[] fileSegmentContentPacket = new byte[7 + fileSegmentContentBlock.length];
            copyOf(fileSegmentContentPacket, 7, fileSegmentContentBlock);
            byte responseControlDomain = requestPackets.getResponseControlDomain();
            if (i < segmentNumber -1) {
                responseControlDomain = DL476Constant.CONTROL_DOMAIN_A_DATA_NEXT;
            }
            addPacketHeader(fileSegmentContentPacket, responseControlDomain, requestPackets.getRequestPackets().get(0)[2]);
            
            System.out.println("�ļ��Σ�" + i);
            for (byte b : fileSegmentContentPacket) {
				System.out.print(b + " ");
			}
            System.out.println();
            requestPackets.addResponsePackets(fileSegmentContentPacket);
        }
        return requestPackets;
    }

    /**
     * createFileAttributeBlock:
     * <p>�����ļ����Կ�
     *
     * @param   fileName
     *          - byte
     * @param   fileConentLength
     *          - �ļ����ݳ���
     * @param   time
     *          - �ļ�����޸�ʱ��
     * @return  byte[]
     *          - �ļ����Կ�
     *
     * @since   v1.01
     */
    public static byte[] createFileAttributeBlock(byte[] fileName, int fileConentLength, long lastModified) {
        int fileAttributeBlockLength = 0;
        int dataBlockLength = 4 + 2 + 4 + 1 + 7 + fileName.length;
        fileAttributeBlockLength = dataBlockLength + 4;
        byte[] fileAttributeBlock = new byte[fileAttributeBlockLength];
        
        // ��ȡ���ݿ�ͷ
        byte[] dataBlockHeader = createDataBlockHeader(DL476Constant.BID_A_FILE_ATTRIBUTE_SVG, (byte) 0, dataBlockLength);
        copyOf(fileAttributeBlock, 0, dataBlockHeader);

        // �ļ�����
        copyOf(fileAttributeBlock, 4, int2ByteArray(fileConentLength));

        // �ļ��ֶγ���
        copyOf(fileAttributeBlock, 8, int2ByteArray(FILE_SEGMENT_SIZE, 2));

        // �ļ���ʶ���ͣ����ߣ��ļ��汾�ţ��ͣ����ߣ�
        for (int i = 0; i < 4; i++) {
            fileAttributeBlock[10 + i] = 0;
        }

        // �ļ���ʽ����
        fileAttributeBlock[14] = DL476Constant.FILE_TYPE_SVG;

        // �ļ�ʱ���
        copyOf(fileAttributeBlock, 15, createFileTimeBlock(lastModified));

        // �ļ�����
        copyOf(fileAttributeBlock, 22, fileName);
        return fileAttributeBlock;
    }

    /**
     * createFileTimeBlock:
     * <p>���� �ļ�ʱ���
     *
     * @param   time
     *          - long �͵� ʱ��
     * @return  byte[]
     *          - �ļ�ʱ�������
     *
     * @since   v1.01
     */
    public static byte[] createFileTimeBlock(Long time) {
        Date date = new Date(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        byte[] second = int2ByteArray((calendar.get(Calendar.SECOND) * 1000), 2);
        byte minute = (byte) calendar.get(Calendar.MINUTE);
        byte hour = (byte) calendar.get(Calendar.HOUR_OF_DAY);
        byte day = (byte) calendar.get(Calendar.DAY_OF_MONTH);
        byte month = (byte) (calendar.get(Calendar.MONTH));
        byte year = (byte) (calendar.get(Calendar.YEAR) % 100 );

        // �ļ�ʱ���
        byte[] fileTimeBlock = new byte[7];
        
        // ���루�ͣ�
        fileTimeBlock[0] = second[0];
        // ���루�ߣ�
        fileTimeBlock[1] = second[1];
        // �֣�0~59��
        fileTimeBlock[2] = minute;
        // Сʱ��0~23�����λΪ��ʱ�Ʊ�ʶ
        fileTimeBlock[3] = hour;
        // �죨0~31����3λ��ʾ����
        fileTimeBlock[4] = day;
        // �£�0~12��
        fileTimeBlock[5] = month;
        // �꣨0~99��
        fileTimeBlock[6] = year;
        
        return fileTimeBlock;
    }
    
    public static byte[] createVariationContentBlock(int segment,byte[] variationSegmentContent){
    	int dataBlockLength = 4 + variationSegmentContent.length;
    	byte[] variationContentBlock = new byte[dataBlockLength];

    	//��ȡ���ݿ�ͷ
    	byte[] dataBlockHeader = createVariationBlockHeader(DL476Constant.BID_A_VARITION_ANALOG, (byte)0, variationSegmentContent.length);
    	copyOf(variationContentBlock, 0, dataBlockHeader);

    	//��������
    	copyOf(variationContentBlock, 4, variationSegmentContent);
    	return variationContentBlock;
    }
    
    
    
    /**
     * createFileContentBlock:
     * <p>�����ļ����ݿ�
     *
     *
     * @since   v1.01
     */
    public static byte[] createFileContentBlock(int segment, byte[] fileSegmentContent) {
        int dataBlockLength = 4 + fileSegmentContent.length;
        int fileContentBlockLength = 4 + dataBlockLength;
        byte[] fileContentBlock = new byte[fileContentBlockLength];
        
        // ��ȡ���ݿ�ͷ
        byte[] dataBlockHeader = createDataBlockHeader(DL476Constant.BID_A_FILE_CONTENT, (byte) 0, dataBlockLength);
        copyOf(fileContentBlock, 0, dataBlockHeader);

        // �ļ���ʶ���ͣ����ߣ�
        fileContentBlock[4] = 0;
        fileContentBlock[5] = 0;

        // �ļ��ֶ���ţ��ͣ����ߣ�
        copyOf(fileContentBlock, 6, int2ByteArray(segment, 2));
        
        //�ļ�����
        copyOf(fileContentBlock, 8, fileSegmentContent);
        return fileContentBlock;
    }
    
    public static byte[] createVariationBlockHeader(byte bid, byte bidIndex,int dataBlockSize){
    	byte[] dataBlockHeader = new byte[4];
    	// ���ݿ����ͣ�BIDֵΪ102,0x65��
        dataBlockHeader[0] = bid;
        // ����������
        dataBlockHeader[1] = bidIndex;
        // ���ݿ鳤�� �͡���
        copyOf(dataBlockHeader, 2, int2ByteArray(dataBlockSize, 2));
        return dataBlockHeader;
    }
    
    
    /**
     * 
     * createDataBlockHeader:
     * <p>�������ݿ�ͷ
     *
     * @param   bid
     *          - ���ݿ����ͣ�BIDֵΪ102,0x65��
     * @param   bidIndex
     *          - ����������
     * @param   dataBlockSize
     *          - ���ݿ鳤��
     * @return
     *
     * @since   v1.01
     */
    public static byte[] createDataBlockHeader(byte bid, byte bidIndex, int dataBlockSize) {
        byte[] dataBlockHeader = new byte[4];
        // ���ݿ����ͣ�BIDֵΪ102,0x65��
        dataBlockHeader[0] = bid;
        // ����������
        dataBlockHeader[1] = bidIndex;
        // ���ݿ鳤�ȣ��ͣ�
        dataBlockHeader[2] = int2ByteArray(dataBlockSize)[0];
        // ���ݿ鳤�ȣ��ߣ�
        dataBlockHeader[3] = int2ByteArray(dataBlockSize)[1];
        return dataBlockHeader;
    }

    public static byte[] getFileContent(File file) {
        byte[] byteArray = null;
        FileInputStream fileInputStream = null;
        try {
            List<Byte> byteList = new ArrayList<Byte>();
            fileInputStream = new FileInputStream(file);
            while (true) {
                byte b = (byte) fileInputStream.read();
                if (b == -1) {
                    break;
                }
                byteList.add(b);
            }
            byteArray = new byte[byteList.size()];
            for (int i = 0; i < byteList.size(); i++) {
                byteArray[i] = byteList.get(i);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        	try {
				if(fileInputStream != null)fileInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return byteArray;
    }
    /**
     * main:
     * <p>
     *
     * @param args
     *
     * @since   v1.01
     */
    public static void main(String[] args) {
//        byte[] array = ByteUtil.int2ByteArray(230);
//        for (byte b : array) {
//            System.out.println(b);
//        }
//        int2ByteArray(230);
//        
//        File file = new File("D:\\ѧϰ����\\java �ο��ĵ�\\jdk142.chm");
//        System.out.println(file.getName());
//        System.out.println(Short.MAX_VALUE + "====" + Short.MIN_VALUE);
//        int aa = -1;
//        aaa(aa);
//        System.out.println(aa);
        
//        String a = "-118";//������ֵ 
//        BigInteger src = new BigInteger(a);//ת��ΪBigInteger���� 
//        System.out.println(src.toString(2));//ת��Ϊ2���Ʋ������� 

//        
//        String a = "10001010";//������ֵ 
//        for (byte string : hex2byte(a)) {
//            System.out.println(string);
//        }
//        byte a = 10001010;
//        byte binVal4 = (byte)10001010;
        System.out.println((byte) 138);
//        BigInteger src = new BigInteger(a, 2);//ת��ΪBigInteger���� 
//        System.out.println(src.toString());//ת��Ϊ2���Ʋ������� 

    }
    
    public static byte[] hex2byte(String str) { // �ַ���ת������
        if (str == null) {
            return null;
        }
        str = str.trim();
        int len = str.length();
        if (len == 0 || len % 2 == 1) {
            return null;
        }
        byte[] b = new byte[len / 2];
        try {
            for (int i = 0; i < str.length(); i += 2) {
                b[i / 2] = (byte) Integer.decode("0x" + str.substring(i, i + 2)).intValue();
            }
            return b;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static String byte2hex(byte[] b) { // ������ת�ַ���
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs;
    }

    public static void aaa(int aa) {
        aa = 1;
        System.out.println(aa);
    }
    /**
     * int2ByteArray:
     * <p>�� int ת�� byte ����
     *
     * @param   i
     *          - ��ת���� int ����
     * @return  byte[]
     *          - byte ����
     *
     * @since   v1.01
     */
    public static byte[] int2ByteArray(int i) {  
        return ByteUtil.int2ByteArray(i);
    }

    /**
     * int2ByteArray:
     * <p>�� int ת�� byte ���飬������ָ������
     *
     * @param   i
     *          - ��ת���� int ����
     * @param   length
     *          - ָ������
     * @return  byte[]
     *          - byte ����
     *
     * @since   v1.01
     */
    public static byte[] int2ByteArray(int i, int length) {
        return Arrays.copyOfRange(ByteUtil.int2ByteArray(i), 0, length);
    }

    /**
     * short2ByteArray:
     * <p>�� short ת�� byte ����
     *
     * @param   i
     *          - ��ת���� short ����
     * @return  byte[]
     *          - byte ����
     *
     * @since   v1.01
     */
    public static byte[] short2ByteArray(short s) {
        return ByteUtil.short2ByteArray(s);
    }

    /**
     * byteArray2Short:
     * <p>�� byte ���� ת�� short
     *
     * @param   b
     *          - byte ����
     * @return  short
     *          - short
     *
     * @since   v1.01
     */
    public static short byteArray2Short(byte[] b) {
        return ByteUtil.byteArray2Short(b);
    }

    public static byte[] float2ByteArray(float f) {
        return ByteUtil.float2ByteArray(f);
    }

    /**
     * copyOf:
     * <p>�� ���������е� Ԫ�ؿ����� ԭ�����У��ӵ� from ��ʼ
     *
     * @param   original
     *          - ԭ����
     * @param   from
     *          - ԭ����Ŀ�ʼλ��
     * @param   array
     *          - ����
     * @return  byte[]
     *          - ����ԭ����
     *
     * @since   v1.01
     */
    public static byte[] copyOf(byte[] original, int from, byte[] array) {
        for (int i = 0; i < array.length; i++) {
            original[from + i] = array[i];
        }
        return original;
    }
}

