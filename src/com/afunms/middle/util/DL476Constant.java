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
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 11 16:06:44
 */
public class DL476Constant {

    //-----------------------------------------------------------
    // 控制域
    //-----------------------------------------------------------
    /**
     * CONTROL_DOMAIN_A_ASSOCIATE:
     * <p>0000001 A-ASSOCIATE 联系
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_ASSOCIATE = 1;
    
    /**
     * CONTROL_DOMAIN_A_ASSOCIATE_ACK:
     * <p>0000010 A-ASSOCIATE-ACK 联系应答
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_ASSOCIATE_ACK = 2;
    
    /**
     * CONTROL_DOMAIN_A_ASSOCIATE_NAK:
     * <p>0000011 A-ASSOCIATE-NAK 联系否认
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_ASSOCIATE_NAK = 3;

    /**
     * CONTROL_DOMAIN_A_RELEASE:
     * <p>0000100 A-RELEASE   释放
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_RELEASE = 4;

    /**
     * CONTROL_DOMAIN_A_RELEASE_ACK:
     * <p>0000101 A-RELEASE-ACK   释放确认
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_RELEASE_ACK = 5;

    /**
     * CONTROL_DOMAIN_A_RELEASE_NAK:
     * <p>0000110 A-RELEASE-NAK   释放否认
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_RELEASE_NAK = 6;

    /**
     * CONTROL_DOMAIN_A_ABORT:
     * <p>0000111 A-ABORT 放弃
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_ABORT = 7;

    /**
     * CONTROL_DOMAIN_A_RESET:
     * <p>0001000 A-RESET 复位
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_RESET = 8;
    
    /**
     * CONTROL_DOMAIN_A_RESET_ACK:
     * <p>0001001 A-RESET-ACK    复位确认
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_RESET_ACK  = 9;

    /**
     * CONTROL_DOMAIN_A_DATA:
     * <p>0001010   A-DATA  数据
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_DATA  = 10;

    /**
     * CONTROL_DOMAIN_A_DATA:
     * <p>0001010   A-DATA  数据后续报文
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_DATA_NEXT  = (byte) 138;

    /**
     * CONTROL_DOMAIN_A_DATA_ACK:
     * <p>0001011   A-DATA-ACK  数据确认
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_DATA_ACK  = 11;

    /**
     * CONTROL_DOMAIN_A_DATA_NAK:
     * <p>0001100   A-DATA-NAK  数据否认
     *
     * @since   v1.01
     */
    public final static byte CONTROL_DOMAIN_A_DATA_NAK  = 12;


    //-----------------------------------------------------------
    // 协议控制报文头
    //-----------------------------------------------------------
    
    /**
     * CONTROL_DOMAIN_A_DATA_NAK:
     * <p>控制模式 0x10
     *
     * @since   v1.01
     */
    public final static byte CONTROL_HEADER_CONTROL_MODEL  = 16;

    /**
     * CONTROL_DOMAIN_A_DATA_NAK:
     * <p>状态标识 0x80
     *
     * @since   v1.01
     */
    public final static byte CONTROL_HEADER_STATUS_MARK  = -128;

    /**
     * REASON_CODE:
     * <p>原因码 0
     *
     * @since   v1.01
     */
    public final static byte CONTROL_HEADER_REASON_CODE = 0;

    //-----------------------------------------------------------
    // 数据类型
    //-----------------------------------------------------------

    
//    00001000    8   变化遥测（实型）
//    00001001    9   变化遥信
//    00100111    39  ASCII码数据块
//    00101010    42  文件属性块
//    00101011    43  文件内容块
//    00101100    44  曲线数据块
//    01100100    100 变化状态量(区别变化遥信)
//    01100101    101 变化模拟量
//    01100110    102 SVG文件属性块(增加版本号)
//    01101110    110 错峰预警
//    01101111    111 计划曲线

    /**
     * BID_A_FILE_ATTRIBUTE:
     * <p>00101010  42  文件属性块
     *
     * @since   v1.01
     */
    public final static byte BID_A_FILE_ATTRIBUTE  = 42;

    /**
     * BID_A_FILE_CONTENT:
     * <p>00101011  43  文件内容块
     *
     * @since   v1.01
     */
    public final static byte BID_A_FILE_CONTENT  = 43;
    
    /**
     * BID_A_VARITION_STATUS:
     * <p>01100100	100	变化状态量
     *
     * @since   v1.01
     */
    public final static byte BID_A_VARITION_STATUS = 100;
    
    /**
     * BID_A_VARITION_ANALOG:
     * <p>01100101	101	变化模拟量
     *
     * @since   v1.01
     */
    public final static byte BID_A_VARITION_ANALOG = 101;

    /**
     * BID_A_FILE_ATTRIBUTE_SVG:
     * <p>01100110    102 SVG文件属性块(增加版本号)
     *
     * @since   v1.01
     */
    public final static byte BID_A_FILE_ATTRIBUTE_SVG  = 102;

    //-----------------------------------------------------------
    // 文件类型
    //-----------------------------------------------------------

    //    00000001    G文件
    //    00000010    SVG文件
    //    00000011    XRG文件
    //    00000100    SVGZ

    /**
     * FILE_TYPE_G:
     * <p>00000001    G文件
     *
     * @since   v1.01
     */
    public final static byte FILE_TYPE_G  = 1;
    
    /**
     * FILE_TYPE_SVG:
     * <p>00000010    SVG文件
     *
     * @since   v1.01
     */
    public final static byte FILE_TYPE_SVG  = 2;
    
    /**
     * FILE_TYPE_XRG:
     * <p>00000011    XRG文件
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
    // 请求服务
    //-----------------------------------------------------------

    //   画面文件请求服务主要实现对文件传输的请求，其格式如下：
    //    CSG::GET(name=FULONG_直流场.svg,type=0,time=1000)
    //    CSG：表示我们公司的名称空间，此为唯一标识；
    //    GET：服务名，所以获取文件，括号内为该服务的请求参数；
    //    name：获取文件的名称；
    //    type：获取文件后打开的方式，直接替换打开、分图打开、新窗口打开；
    //    time：画面刷新周期，单位毫秒

    //    数据传输请求服务主要实现向子站发送请求，要求定时上送数据。其格式如下：
    //    CSG::DISPLAY(name=FULONG_直流场)
    //    CSG：表示我们公司的名称空间，此为唯一标识；
    //    DISPLAY：服务名，表示需要显示数据；
    //    name：需要显示数据的画面名；

    /**
     * REQUEST_SERVICE_NAME_SVG_FILE:
     * <p>文件请求服务名 画面文件请求服务
     *
     * @since   v1.01
     */
    public final static String REQUEST_SERVICE_NAME_GET = "CSG::GET";

    /**
     * REQUEST_SERVICE_NAME_SVG_FILE:
     * <p>文件请求服务名 数据传输请求服务
     *
     * @since   v1.01
     */
    public final static String REQUEST_SERVICE_NAME_DISPLAY = "CSG::DISPLAY";

    /**
     * REQUEST_SERVICE_NAME_SVG_FILE:
     * <p>文件请求服务名 数据传输停止服务
     *
     * @since   v1.01
     */
    public final static String REQUEST_SERVICE_NAME_STOPDATA = "CSG::STOPDATA";

    
}

