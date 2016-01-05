/*
 * @(#)DL476Constant.java     v1.01, 2013 12 11
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.util;

/**
 * ClassName:   DL476Constant.java
 * <p>
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 11 16:06:44
 */
public class DL476Constant {

    //-----------------------------------------------------------
    // ������
    //-----------------------------------------------------------
    /**
     * CONTROL_DOMAIN_A_ASSOCIATE:
     * <p>0000001 A-ASSOCIATE ��ϵ
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_ASSOCIATE = 1;
    
    /**
     * CONTROL_DOMAIN_A_ASSOCIATE_ACK:
     * <p>0000010 A-ASSOCIATE-ACK ��ϵӦ��
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_ASSOCIATE_ACK = 2;
    
    /**
     * CONTROL_DOMAIN_A_ASSOCIATE_NAK:
     * <p>0000011 A-ASSOCIATE-NAK ��ϵ����
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_ASSOCIATE_NAK = 3;

    /**
     * CONTROL_DOMAIN_A_RELEASE:
     * <p>0000100 A-RELEASE   �ͷ�
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_RELEASE = 4;

    /**
     * CONTROL_DOMAIN_A_RELEASE_ACK:
     * <p>0000101 A-RELEASE-ACK   �ͷ�ȷ��
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_RELEASE_ACK = 5;

    /**
     * CONTROL_DOMAIN_A_RELEASE_NAK:
     * <p>0000110 A-RELEASE-NAK   �ͷŷ���
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_RELEASE_NAK = 6;

    /**
     * CONTROL_DOMAIN_A_ABORT:
     * <p>0000111 A-ABORT ����
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_ABORT = 7;

    /**
     * CONTROL_DOMAIN_A_RESET:
     * <p>0001000 A-RESET ��λ
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_RESET = 8;
    
    /**
     * CONTROL_DOMAIN_A_RESET_ACK:
     * <p>0001001 A-RESET-ACK    ��λȷ��
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_RESET_ACK  = 9;

    /**
     * CONTROL_DOMAIN_A_DATA:
     * <p>0001010   A-DATA  ����
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_DATA  = 10;

    /**
     * CONTROL_DOMAIN_A_DATA:
     * <p>0001010   A-DATA  ���ݺ�������
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_DATA_NEXT  = (byte) 138;

    /**
     * CONTROL_DOMAIN_A_DATA_ACK:
     * <p>0001011   A-DATA-ACK  ����ȷ��
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_DATA_ACK  = 11;

    /**
     * CONTROL_DOMAIN_A_DATA_NAK:
     * <p>0001100   A-DATA-NAK  ���ݷ���
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_DATA_NAK  = 12;


    //-----------------------------------------------------------
    // Э����Ʊ���ͷ
    //-----------------------------------------------------------
    
    /**
     * CONTROL_DOMAIN_A_DATA_NAK:
     * <p>����ģʽ 0x10
     *
     * @since   v1.01
     */
    public final static byte CONTROL_HEADER_CONTROL_MODEL  = 16;

    /**
     * CONTROL_DOMAIN_A_DATA_NAK:
     * <p>״̬��ʶ 0x80
     *
     * @since   v1.01
     */
    public final static byte CONTROL_HEADER_STATUS_MARK  = -128;

    /**
     * REASON_CODE:
     * <p>ԭ���� 0
     *
     * @since   v1.01
     */
    public final static byte CONTROL_HEADER_REASON_CODE = 0;

    //-----------------------------------------------------------
    // ��������
    //-----------------------------------------------------------

    
//    00001000    8   �仯ң�⣨ʵ�ͣ�
//    00001001    9   �仯ң��
//    00100111    39  ASCII�����ݿ�
//    00101010    42  �ļ����Կ�
//    00101011    43  �ļ����ݿ�
//    00101100    44  �������ݿ�
//    01100100    100 �仯״̬��(����仯ң��)
//    01100101    101 �仯ģ����
//    01100110    102 SVG�ļ����Կ�(���Ӱ汾��)
//    01101110    110 ���Ԥ��
//    01101111    111 �ƻ�����

    /**
     * BID_A_FILE_ATTRIBUTE:
     * <p>00101010  42  �ļ����Կ�
     *
     * @since   v1.01
     */
    public final static byte BID_A_FILE_ATTRIBUTE  = 42;

    /**
     * BID_A_FILE_CONTENT:
     * <p>00101011  43  �ļ����ݿ�
     *
     * @since   v1.01
     */
    public final static byte BID_A_FILE_CONTENT  = 43;
    
    /**
     * BID_A_VARITION_STATUS:
     * <p>01100100	100	�仯״̬��
     *
     * @since   v1.01
     */
    public final static byte BID_A_VARITION_STATUS = 100;
    
    /**
     * BID_A_VARITION_ANALOG:
     * <p>01100101	101	�仯ģ����
     *
     * @since   v1.01
     */
    public final static byte BID_A_VARITION_ANALOG = 101;

    /**
     * BID_A_FILE_ATTRIBUTE_SVG:
     * <p>01100110    102 SVG�ļ����Կ�(���Ӱ汾��)
     *
     * @since   v1.01
     */
    public final static byte BID_A_FILE_ATTRIBUTE_SVG  = 102;

    //-----------------------------------------------------------
    // �ļ�����
    //-----------------------------------------------------------

    //    00000001    G�ļ�
    //    00000010    SVG�ļ�
    //    00000011    XRG�ļ�
    //    00000100    SVGZ

    /**
     * FILE_TYPE_G:
     * <p>00000001    G�ļ�
     *
     * @since   v1.01
     */
    public final static byte FILE_TYPE_G  = 1;
    
    /**
     * FILE_TYPE_SVG:
     * <p>00000010    SVG�ļ�
     *
     * @since   v1.01
     */
    public final static byte FILE_TYPE_SVG  = 2;
    
    /**
     * FILE_TYPE_XRG:
     * <p>00000011    XRG�ļ�
     *
     * @since   v1.01
     */
    public final static byte FILE_TYPE_XRG  = 3;
    
    /**
     * FILE_TYPE_SVGZ:
     * <p>00000100    SVGZ
     *
     * @since   v1.01
     */
    public final static byte FILE_TYPE_SVGZ  = 4;

    //-----------------------------------------------------------
    // �������
    //-----------------------------------------------------------

    //   �����ļ����������Ҫʵ�ֶ��ļ�������������ʽ���£�
    //    CSG::GET(name=FULONG_ֱ����.svg,type=0,time=1000)
    //    CSG����ʾ���ǹ�˾�����ƿռ䣬��ΪΨһ��ʶ��
    //    GET�������������Ի�ȡ�ļ���������Ϊ�÷�������������
    //    name����ȡ�ļ������ƣ�
    //    type����ȡ�ļ���򿪵ķ�ʽ��ֱ���滻�򿪡���ͼ�򿪡��´��ڴ򿪣�
    //    time������ˢ�����ڣ���λ����

    //    ���ݴ������������Ҫʵ������վ��������Ҫ��ʱ�������ݡ����ʽ���£�
    //    CSG::DISPLAY(name=FULONG_ֱ����)
    //    CSG����ʾ���ǹ�˾�����ƿռ䣬��ΪΨһ��ʶ��
    //    DISPLAY������������ʾ��Ҫ��ʾ���ݣ�
    //    name����Ҫ��ʾ���ݵĻ�������

    /**
     * REQUEST_SERVICE_NAME_SVG_FILE:
     * <p>�ļ���������� �����ļ��������
     *
     * @since   v1.01
     */
    public final static String REQUEST_SERVICE_NAME_GET = "CSG::GET";

    /**
     * REQUEST_SERVICE_NAME_SVG_FILE:
     * <p>�ļ���������� ���ݴ����������
     *
     * @since   v1.01
     */
    public final static String REQUEST_SERVICE_NAME_DISPLAY = "CSG::DISPLAY";

    /**
     * REQUEST_SERVICE_NAME_SVG_FILE:
     * <p>�ļ���������� ���ݴ���ֹͣ����
     *
     * @since   v1.01
     */
    public final static String REQUEST_SERVICE_NAME_STOPDATA = "CSG::STOPDATA";

    
}

